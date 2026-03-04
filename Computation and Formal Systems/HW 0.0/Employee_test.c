#include "Employee.h"       // IT'S A HEADER FILE NOW!

int main(int argc, char* argv[]) {

    /* TO RUN: 
       Put the following in the terminal.
            gcc -o [desired name of executable file, e.g. "test"] Employee.c Employee_test.c
            .\[desired name of executable file]
       The above will do the job, but prof recommends this:
            gcc -o [desired file name] -std=c99 -Wall -Werror Employee.c Employee_test.c
            .\[desired file name]
       These tell the compiler to use the “C99” version of the ANSI C Standard, to report all warnings, and to make warnings into errors
       so that even though you may not think they're important your program won't compile if they exist.
    */

    Employee chad = new_Employee("Chad", 1);
    Employee brad = new_Employee("Brad", 2);
    Employee vlad = new_Employee("Vlad", 3);

    Employee_print(chad);
    Employee_print(brad);
    Employee_print(vlad);
}