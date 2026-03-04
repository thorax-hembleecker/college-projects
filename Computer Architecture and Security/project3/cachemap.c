#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>
// #include <sys/mman.h>

static inline uint32_t rdtscp() {
    uint32_t rv;
    asm volatile("rdtscp" : "=a"(rv)::"edx", "ecx");
    return rv;
}
static inline uint64_t rdtscp64() {
    uint32_t low, high;
    asm volatile("rdtscp" : "=a" (low), "=d" (high) :: "ecx");
    return (((uint64_t)high) << 32) | low;
}
static inline void maccess(void *p) {
  asm volatile("movq (%0), %%rax\n" : : "c"(p) : "rax");
}
static inline void flush(void* addr) {
    asm volatile("clflush 0(%0)": : "r" (addr):);
}
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

uint32_t measure_latency(int iterations, int skip, uint64_t *addr, uint64_t **evict_buf, int evict_buf_rows, int evict_buf_cols, int i) {
    uint32_t latency = 0;
    uint32_t avg_latency = 0;
    for (int j=0; j<iterations + skip; j++) {
        maccess(addr);
        asm volatile("lfence");
        if (j >= skip) {
            for (int s=1; s<evict_buf_rows; s++) {
                // The if statement cuts down on the number of accesses to evict - I think the reason this worked better than shrinking the actual buffer is because more than 1 array entry is placed in cache when 1 is accessed, so you can skip over a handful of them safely.
                if (s % 7 == 0) {
                    for (int w=0; w<evict_buf_cols; w++) {
                        maccess(&evict_buf[s][w]);
                    }
                }
            }
            latency = memaccesstime(addr);
        }
        if (j >= skip) // Skips early iterations to avoid issues arising from idle cores' frequency changing.
            avg_latency += latency;
    }
    avg_latency /= iterations;
    return avg_latency;
}

void print_data() {

    int sets = 1024;
    int ways = 16;

    int evict_buf_rows = sets * 16;  // Extra size to overcome complexity.
    int evict_buf_cols = ways * 4;  // Testing found 16 and 4 the most effective!

    uint64_t* evict_buf[evict_buf_rows];
    for (int i=0; i<evict_buf_rows; i++) {
        evict_buf[i] = (uint64_t*)malloc(sizeof(uint64_t) * evict_buf_cols);
        for (int j=0; j<evict_buf_cols; j++) {
            evict_buf[i][j] = INT64_MAX;  // I don't know if this makes a difference but might help?
        }
    }

    FILE *datafile = fopen("eviction-test.txt", "w");
    FILE *indexfile = fopen("eviction-indices.txt", "w");

    uint32_t data[sets * ways];

    int iterations = 10000;
    int skip = 4000;
    uint32_t min_latency = UINT32_MAX;
    uint32_t second_min_latency = UINT32_MAX;
    int current_top_length = 25;
    uint64_t *current_top[current_top_length];  // To keep track of lowest-latency addresses as the lowest latency is still being measured.
    for (int i=0; i<current_top_length; i++) {
        current_top[i] = NULL;
    }
    int split_index = 0;    // Keeps track of the separation between "old" and "new" top addresses.

    int runs = 3;   // The number of measurements taken for "certainty".

    // Initial run, tests 180 values
    for (int i=0; i<180; i++) {  // I actually think this goes out of bounds of evict_buf? But it's not causing an error and as long as I'm not accessing duplicate addresses I guess it's fine for these purposes.
        uint32_t latency = 0;
        uint32_t avg_latency = 0;
        
        avg_latency = measure_latency(iterations, skip, &evict_buf[0][i], evict_buf, evict_buf_rows, evict_buf_cols, i);
        
        if (avg_latency > 100 && avg_latency < min_latency) {
            second_min_latency = min_latency;
            min_latency = avg_latency;
        }
        else if (avg_latency > 100 && avg_latency != min_latency && avg_latency < second_min_latency) {
            second_min_latency = avg_latency;
        }

        if (avg_latency <= min_latency) {
            printf("%d. Minimum! (%d)... ", i, avg_latency);
            printf("\nLowest-measured latency: %d\nSecond-lowest-measured latency: %d", min_latency, second_min_latency);
            if (avg_latency == min_latency) {
                printf("\nCurrent top addresses: \n");
                for (int j=0; j<30; j++) {
                    if (j >= split_index || current_top[j] == NULL) {
                        current_top[j] = &evict_buf[0][i];
                        if (j == current_top_length - 1)
                            split_index = 0;
                        else
                            split_index = j + 1;
                        printf("  %d. %x\n", j, current_top[j]);
                        break;
                    }
                    else {
                        printf("  %d. %x\n", j, current_top[j]);
                    }
                }
            }
            else {
                // for (int j=0; j<30; j++) {
                //     current_top[j] = NULL;
                // }
                current_top[0] = &evict_buf[0][i];
            }
        } else {
            if (avg_latency == second_min_latency) {
                printf("%d. Near-minimum! (%d)... ", i, avg_latency);
                printf("\nLowest-measured latency: %d\nSecond-lowest-measured latency: %d", min_latency, second_min_latency);
                printf("\nCurrent top addresses: \n");
                for (int j=0; j<30; j++) {
                    if (j >= split_index || current_top[j] == NULL) {
                        current_top[j] = &evict_buf[0][i];
                        if (j == current_top_length - 1)
                            split_index = 0;
                        else
                            split_index = j + 1;
                        printf("  %d. %x\n", j, current_top[j]);
                        break;
                    }
                    else {
                        printf("  %d. %x\n", j, current_top[j]);
                    }
                }
            }
            else {
                printf("%d. (%d)... ", i, avg_latency);
            }
        }
        fflush(stdout);
    }

    printf("\n\n");
    uint64_t *certainties[runs+1][30];  // To keep track of the degree of confidence for the accuracy of lowest-latency addresses.
    for (int i=0; i<runs; i++) {
        for (int j=0; j<30; j++) {
            certainties[i][j] = NULL;
        }
    }
    uint64_t *outlier_certainties[runs+1][30];  // To keep track of the degree of confidence for the accuracy of second-lowest-latency addresses in case the lowest latency is actually an outlier.
    for (int i=0; i<runs; i++) {
        for (int j=0; j<30; j++) {
            outlier_certainties[i][j] = NULL;
        }
    }

    // Second run, focusing only on the minimum-latency values measured in the original run.
    for (int i=0; i<30; i++) {
        if (current_top[i] != NULL) {
            uint32_t latency = 0;
            uint32_t avg_latency = 0;
            
            int certainty = 0;
            int outlier_certainty = 0;

            for (int j=0; j<runs; j++) {
                avg_latency = measure_latency(iterations, skip, current_top[i], evict_buf, evict_buf_rows, evict_buf_cols, i);
                if (avg_latency <= min_latency)
                    certainty += 1;
                else if (avg_latency <= second_min_latency)
                    outlier_certainty += 1;
                printf("Trial %d (Latency: %d)...\n", j, avg_latency);
            }
            
            for (int k=0; k<30; k++) {
                if (certainties[certainty][k] == NULL) {
                    certainties[certainty][k] = current_top[i];
                    break;
                }
            }
            for (int k=0; k<30; k++) {
                if (outlier_certainties[outlier_certainty][k] == NULL) {
                    outlier_certainties[outlier_certainty][k] = current_top[i];
                    break;
                }
            }

            printf("Address: %x\nCertainty: %d/%d\n\n", current_top[i], certainty, runs);
            printf("Address: %x\nCertainty (outlier case): %d/%d\n\n", current_top[i], outlier_certainty, runs);
            fflush(stdout);
        }
    }

    printf("Lowest-measured latency: %d\nSecond-lowest-measured latency: %d\n", min_latency, second_min_latency);

    printf("Top ten addresses most likely mapped to local LLC slice:\n");
    int addr_number = 1;
    for (int i=runs-1; i>=0; i--) {
        for (int j=0; j<30; j++) {
            if (certainties[i][j] != NULL && addr_number <= 10) {
                printf("%d. %x (Certainty: %d/%d)\n", addr_number, certainties[i][j], i, runs);
                addr_number++;
            }
            else {
                break;
            }
        }
    }
    printf("Top ten addresses if lowest-measured latency is an outlier:\n");
    addr_number = 1;
    for (int i=runs-1; i>=0; i--) {
        for (int j=0; j<30; j++) {
            if (outlier_certainties[i][j] != NULL && addr_number <= 10) {
                printf("%d. %x (Certainty: %d/%d)\n", addr_number, outlier_certainties[i][j], i, runs);
                addr_number++;
            }
            else {
                break;
            }
        }
    }

    for (int i=0; i<evict_buf_rows; i++) {
        free(evict_buf[i]);
    }
}

int main() {
    printf("Starting.\n");
    print_data();
    printf("Finished.\n");
}
