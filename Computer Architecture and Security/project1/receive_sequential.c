#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "mem_utils.h"
#include <unistd.h>
#include <fcntl.h>
#include <sys/mman.h>

int max_message_size = 1024;
int iterations = 100000;
int interval = 2000000000;
int small_interval = 20000;
int jitter = 400;

void syncTiming(int sync_interval) {
    int i = 0;
    while (rdtscp() % sync_interval >= jitter) {
        i++;
    }
}
void wait(int wait_interval) {
    int i = 0;
    uint32_t time = 0;
    uint32_t init_time = rdtscp();
    while ((time + jitter + 1) % wait_interval >= jitter) {
        time = rdtscp() - init_time;
        if (time == 0)
            time = 1;
        i++;
    }
}
void waitIterative(int wait_interval) {
    int i = 0;
    while (i < wait_interval) {
        i++;
    }
}

uint8_t *flushAndReload() {
    uint8_t *message = (uint8_t*)malloc(max_message_size * 8);
    uint8_t lastByte[8];
    uint8_t lineBreak[] = {0, 0, 0, 0, 1, 0, 1, 0};
    uint8_t end[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    uint8_t last2Bytes[16];

    uint32_t accessTime = 0;

    int shared_descriptor = open("/shared_file", O_RDONLY);
    int shared_size = 4096; // I think the file is pretty small so this should be fine.
    void *shared_file = mmap(NULL, shared_size, PROT_READ, MAP_SHARED, shared_descriptor, 0);

    // Measuring DRAM Latency
    
    printf("Please press enter.\n");
    char blank[2];
    fgets(blank, 2, stdin);

    printf("Receiver now listening.\n");
    fflush(stdout);

    maccess(shared_file);   // Bring into cache

    syncTiming(interval);
    
    // uint32_t time = 0;
    uint32_t timeStart = 0;
    uint32_t timeEnd = 0;

    int bit = -1;
    for (int index = 0; index < max_message_size * 8; index++) {
        accessTime = 0;
        int loops = 0;
        timeStart = rdtscp();
        while ((rdtscp() + jitter + 1) % small_interval > jitter) {
            // waitIterative(1000);
            wait(300);
            accessTime += memaccesstime(shared_file);
            loops++;
        }
        timeEnd = rdtscp();
        if (accessTime/loops < 100) {
            bit = 0;
        }
        else {
            bit = 1;
        }
        printf("Received %d (Latency: %d) at time %u - %u\n", bit, accessTime/loops, timeStart, timeEnd);

        // wait(1000);
        // accessTime = memaccesstime(shared_file);
        // time = rdtscp();
        // if (accessTime < 100) {
        //     bit = 0;
        //     // wait(100);
        // }
        // else {
        //     bit = 1;
        // }

        // printf("Received %d (Latency: %d) at time %u\n", bit, accessTime, time);
        // printf("Received %d (Latency: %d)\n", bit, accessTime);

        message[index] = bit;
        lastByte[index % 8] = bit;
        last2Bytes[index % 16] = bit;

        if ((index + 1) % 8 == 0) {
            printf("\n");
            fflush(stdout);

            if (memcmp(lastByte, lineBreak, 8) == 0) {
                close(shared_descriptor);
                return message;
            }
            if (memcmp(last2Bytes, end, 16) == 0) {
                close(shared_descriptor);
                return message;
            }
        }
        bit = -1;
        syncTiming(interval);
    }
    
    // Remember you should have a "next character coming" signal!

    close(shared_descriptor);
    return message;
}

unsigned char *binaryToString(uint8_t *b) {
    unsigned char *s = (unsigned char *)malloc(max_message_size * 8);

    int i = 0;
    int bit = 0;
    uint8_t c_temp = 0;
    while (i != -1) {
        c_temp = 0;
        for (int j=0; j<8; j++) {
            bit = b[(i * 8) + j];
            c_temp += (bit << (7 - j));
        }
        if (c_temp != 10 && i < 255) {
            unsigned char c = c_temp;
            s[i] = c;
        }
        else {
            i = -2;
        }
        i++;
    }
    s[i] = '\0';
    return s;
}

int main() {
    uint8_t *binary = flushAndReload();
    printf("Finished receiving!\n");
    printf("%s\n", binaryToString(binary));

    return 1;
}