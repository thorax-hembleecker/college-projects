#include <stdlib.h>
#include <string.h>
#include "mem_utils.h"
#include <unistd.h>
#include <fcntl.h>
#include <sys/mman.h>

int max_message_size = 256;
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
        i++;
    }
}
void waitIterative(int wait_interval) {
    int i = 0;
    printf("3... ");
    fflush(stdout);
    while (i < wait_interval) {
        if (i == wait_interval/3) {
            printf("2... ");
            fflush(stdout);
        }
        else if (i == 2 * wait_interval/3) {
            printf("1... ");
            fflush(stdout);
        }
        i++;
    }
}

uint8_t *stringToBinary(char *s) {
    uint8_t *message = (uint8_t *)malloc(sizeof(uint8_t) * max_message_size * 8);
    for (int i=0; i<strlen(s); i++) {
        for (int b=7; b>=0; b--) {
            message[i*8 + (7 - b)] = (s[i] >> b) & 1;
        }
        if (s[i] == '\n') {
            message[(i+1)*8] = 255;
            return message;
        }
    }
    return message;
}

int main() {
    printf("Please type a message.\n");
    char message[max_message_size];
    fgets(message, max_message_size, stdin);
    printf("You typed: %s", message);

    uint8_t *binary = stringToBinary(message);

    printf("Coded string to binary.\n\n");

    int shared_descriptor = open("/shared_file", O_RDONLY);
    int shared_size = 4096; // I think the file is pretty small so this should be fine.
    void *shared_file = mmap(NULL, shared_size, PROT_READ, MAP_SHARED, shared_descriptor, 0);

    uint32_t timeStart = 0;
    uint32_t timeEnd = 0;

    waitIterative(1000000000);
    printf("Now sending.\n");
    syncTiming(interval);

    for (int index = 0; index < max_message_size * 8; index++) {
        if (binary[index] == 1) {
            uint32_t init_time = rdtscp();
            while ((rdtscp() + jitter + 1) % small_interval > jitter) {
                flush(shared_file);
            }
            timeStart = init_time;
            timeEnd = rdtscp();
            // flush(shared_file); // Flush to DRAM
        }
        else if (binary[index] == 0) {
            uint32_t init_time = rdtscp();
            while ((rdtscp() + jitter + 1) % small_interval > jitter) {
                maccess(shared_file);
            }
            timeStart = init_time;
            timeEnd = rdtscp();
            // maccess(shared_file);
        }

        printf("Sent %d at time %u - %u\n", binary[index], timeStart, timeEnd);
        fflush(stdout);

        if ((index + 1) % 8 == 0) {
            printf("\n");
            fflush(stdout);
        }
        if (binary[index] == 255)
            break;

        syncTiming(interval);
    }

    close(shared_descriptor);

    return 1;
}