#include <stdlib.h>
#include <malloc.h>
#include "mem_utils.h"

void l1(int x, int trial) {
    int trialDigits = 1;
    if (trial >= 10)
        trialDigits = 2;
    if (trial >= 100)
        trialDigits = 3;

    int size = 16 + trialDigits;
    char filename[size];
    snprintf(filename, size, "l1-trial-%d.txt", trial);
    printf("%s\n", filename);

    FILE *datafile = fopen(filename, "w");

    uint8_t *x_addr = (uint8_t*) &x;

    uint32_t data[1000];
    uint32_t accessTime = 0;

    for (int i=0; i<1000; i++) {
        maccess(&x);
        accessTime = memaccesstime(&x);
        data[i] = accessTime;
        fprintf(datafile, "%d\n", accessTime);
    }
}

void l2(int x, int trial) {
    int trialDigits = 1;
    if (trial >= 10)
        trialDigits = 2;
    if (trial >= 100)
        trialDigits = 3;

    int size = 16 + trialDigits;
    char filename[size];
    snprintf(filename, size, "l2-trial-%d.txt", trial);
    printf("%s\n", filename);

    int sets = 64;
    int ways = 8;

    sets *= 16;  // Extra size to overcome complexity.
    ways *= 4;  // Testing found 16 and 4 the most effective!

    uint64_t* buf[sets * 4];
    for (int i=0; i<sets * 4; i++) {
        buf[i] = (uint64_t*)malloc(sizeof(uint64_t) * ways);
        for (int j=0; j<ways; j++) {
            buf[i][j] = INT64_MAX;  // I don't know if this makes a difference but might help?
        }
    }

    FILE *datafile = fopen(filename, "w");

    uint32_t data[1000];
    uint32_t accessTime = 0;

    for (int i=0; i<1000; i++) {
        maccess(&buf[0][0]);
        // maccess(&x);
        asm volatile("lfence");
        for (int s=1; s<sets * 4; s++) {
            for (int w=0; w<ways; w++) {
                maccess(&buf[s][w]);
            }
        }
        accessTime = memaccesstime(&buf[0][0]);
        // accessTime = memaccesstime(&x);
        data[i] = accessTime;
        fprintf(datafile, "%d\n", accessTime);
    }

    for (int i=0; i<sets * 4; i++) {
        free(buf[i]);
    }
}

void l3(int x, int trial) {
    int trialDigits = 1;
    if (trial >= 10)
        trialDigits = 2;
    if (trial >= 100)
        trialDigits = 3;

    int size = 16 + trialDigits;
    char filename[size];
    snprintf(filename, size, "l3-trial-%d.txt", trial);
    printf("%s\n", filename);

    int sets = 1024;
    int ways = 16;

    sets *= 4;  // Extra size to overcome complexity.
    ways *= 4;  // Testing found 4 and 4 the most effective!

    uint64_t* buf[sets * 4];
    for (int i=0; i<sets * 4; i++) {
        buf[i] = (uint64_t*)malloc(sizeof(uint64_t) * ways);
        for (int j=0; j<ways; j++) {
            buf[i][j] = INT64_MAX;  // I don't know if this makes a difference but might help?
        }
    }

    FILE *datafile = fopen(filename, "w");

    uint32_t data[1000];
    uint32_t accessTime = 0;

    for (int i=0; i<1000; i++) {
        maccess(&buf[0][0]);
        // maccess(&x);
        asm volatile("lfence");
        for (int s=1; s<sets * 4; s++) {
            for (int w=0; w<ways; w++) {
                maccess(&buf[s][w]);
            }
        }
        accessTime = memaccesstime(&buf[0][0]);
        // accessTime = memaccesstime(&x);
        data[i] = accessTime;
        fprintf(datafile, "%d\n", accessTime);
    }

    for (int i=0; i<sets * 4; i++) {
        free(buf[i]);
    }
}

void dram(int x, int trial) {
    int trialDigits = 1;
    if (trial >= 10)
        trialDigits = 2;
    if (trial >= 100)
        trialDigits = 3;

    int size = 16 + trialDigits;
    char filename[size];
    snprintf(filename, size, "dram-trial-%d.txt", trial);
    printf("%s\n", filename);

    FILE *datafile = fopen(filename, "w");

    uint8_t *x_addr = (uint8_t*) &x;

    uint32_t data[1000];
    uint32_t accessTime = 0;

    // Measuring DRAM Latency
    for (int i=0; i<1000; i++) {
        flush(x_addr);
        accessTime = memaccesstime(&x);
        data[i] = accessTime;
        fprintf(datafile, "%d\n", accessTime);   // Writes accessTime to file.
    }
}

int main() {
    int x = 0;  // Placeholder variable to access.
    int trial = 0;

    l1(x, trial);
    l2(x, trial);
    l3(x, trial);
    dram(x, trial);

    return 1;   // Exit main.
}