#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "mem_utils.h"
#include <unistd.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <sys/time.h>
#include <time.h>

uint32_t max_message_size = 1024;
// uint32_t iterations = 100000;
uint32_t interval = 100000;
uint32_t small_interval = 10000;
uint32_t jitter = 400;

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
    uint8_t start[] = {1, 1, 1, 1, 1, 1, 1, 1};
    uint8_t end[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    uint8_t last2Bytes[16];
    int trackDigit = 0;
    int desync_risk = 0;

    uint32_t accessTime = 0;

    int shared_descriptor = open("/shared_file", O_RDONLY);
    int shared_size = 4096; // I think the file is pretty small so this should be fine.
    void *shared_file = mmap(NULL, shared_size, PROT_READ, MAP_SHARED, shared_descriptor, 0);

    struct timeval clock_init, clock_final;

    // Measuring DRAM Latency
    
    printf("Please press enter.\n");
    char blank[2];
    fgets(blank, 2, stdin);

    maccess(shared_file);   // Bring into cache

    uint32_t timeStart = 0;
    uint32_t timeEnd = 0;

    int bit = -1;
    // Okay this doesn't work, as expected. I have no idea what I did that made it work so spectacularly before everything else broke.
    for (int index = 0; ; index++) {
        accessTime = 0;
        int loops = 0;
        timeStart = rdtscp();

        // Only works for intervals that are multiples of 10.
        int compareDigit = (timeStart / interval) - (timeStart / (interval * 10)) * 10;
        if (index != 0 && compareDigit - trackDigit > 1) {
            // index += compareDigit - trackDigit;
            // syncTiming(interval);
            desync_risk++;
        }
        trackDigit = compareDigit;

        while (loops == 0 || (rdtscp() + jitter + 1) % small_interval > jitter) {
            wait(300);
            accessTime += memaccesstime(shared_file);
            loops++;
        }
        timeEnd = rdtscp();
        if (accessTime/loops < 100) {
            bit = 0;
            printf("Received 0 (Latency: %d)  at time %u - %u\n", accessTime/loops, timeStart, timeEnd);
        }
        else {
            bit = 1;
            printf("Received 1 (Latency: %d) at time %u - %u\n", accessTime/loops, timeStart, timeEnd);
        }

        message[index] = bit;
        lastByte[index % 8] = bit;
        last2Bytes[index % 16] = bit;

        if (memcmp(lastByte, start, 8) == 0) {
            break;
        }

        bit = -1;
        syncTiming(interval);
    }

    printf("Receiver now listening.\n");
    fflush(stdout);

    maccess(shared_file);   // Bring into cache

    // Making this predefined as large for ease of use during testing.
    // syncTiming(2000000000);

    // May want to move this before syncTiming if intervals get smaller.
    gettimeofday(&clock_init, NULL);
    
    // uint32_t time = 0;
    /*uint32_t*/ timeStart = 0;
    /*uint32_t*/ timeEnd = 0;

    /*int*/ bit = -1;
    for (int index = 0; index < max_message_size * 8; index++) {
        accessTime = 0;
        int loops = 0;
        timeStart = rdtscp();

        // Only works for intervals that are multiples of 10.
        int compareDigit = (timeStart / interval) - (timeStart / (interval * 10)) * 10;
        if (index != 0 && compareDigit - trackDigit > 1) {
            // index += compareDigit - trackDigit;
            // syncTiming(interval);
            desync_risk++;
        }
        trackDigit = compareDigit;

        while (loops == 0 || (rdtscp() + jitter + 1) % small_interval > jitter) {
            wait(300);
            accessTime += memaccesstime(shared_file);
            loops++;
        }
        timeEnd = rdtscp();
        if (accessTime/loops < 100) {
            bit = 0;
            printf("Received 0 (Latency: %d)  at time %u - %u\n", accessTime/loops, timeStart, timeEnd);
        }
        else {
            bit = 1;
            printf("Received 1 (Latency: %d) at time %u - %u\n", accessTime/loops, timeStart, timeEnd);
        }

        message[index] = bit;
        lastByte[index % 8] = bit;
        last2Bytes[index % 16] = bit;

        if ((index + 1) % 8 == 0) {
            printf("\n");
            fflush(stdout);

            if (memcmp(lastByte, lineBreak, 8) == 0) {
                gettimeofday(&clock_final, NULL);
                int seconds = clock_final.tv_sec - clock_init.tv_sec;
                int microseconds = clock_final.tv_usec - clock_init.tv_usec;
                if (microseconds < 0) {
                    seconds--;
                    microseconds += 1000000;
                }
                printf("Total time: %d seconds %d microseconds\n", seconds, microseconds);
                printf("Possible moments of desync: %d\n", desync_risk);
                printf("Estimated error rate: %f\n", ((double)desync_risk / (double)index));
                close(shared_descriptor);
                return message;
            }
            if (memcmp(last2Bytes, end, 16) == 0) {
                gettimeofday(&clock_final, NULL);
                int seconds = clock_final.tv_sec - clock_init.tv_sec;
                int microseconds = clock_final.tv_usec - clock_init.tv_usec;
                if (microseconds < 0) {
                    seconds--;
                    microseconds += 1000000;
                }
                printf("Total time: %d seconds %d microseconds\n", seconds, microseconds);
                printf("Possible moments of desync: %d\n", desync_risk);
                printf("Estimated error rate: %f\n", ((double)desync_risk / (double)index));
                close(shared_descriptor);
                return message;
            }
        }
        bit = -1;
        syncTiming(interval);
    }

    gettimeofday(&clock_final, NULL);
    int seconds = clock_final.tv_sec - clock_init.tv_sec;
    int microseconds = clock_final.tv_usec - clock_init.tv_usec;
    if (microseconds < 0) {
        seconds--;
        microseconds += 1000000;
    }
    printf("Total time: %d seconds %d microseconds\n", seconds, microseconds);
    printf("Possible moments of desync: %d\n", desync_risk);
    printf("Estimated error rate: %f\n", ((double)desync_risk / (double)(max_message_size * 8)));
    
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