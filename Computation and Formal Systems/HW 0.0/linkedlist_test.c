#include "linkedlist.h"

/* To run: gcc -o [desired file name] -std=c99 -Wall -Werror Employee.c linkedlist.c linkedlist_test.c
   Note that you need to input Employee.c as well as linkedlist.c and linkedlist_test.c, since linkedlist.c needs Employee.c to run.
*/

int main(int argsc, char* argv[]) {
    Employee grant = new_Employee("Grant", 23);
    Employee chad = new_Employee("Chad", 576);
    Employee brad = new_Employee("Brad", 129);
    Employee vlad = new_Employee("Vlad", 434);

    LinkedList employees = newLinkedList();
    prepend(employees, grant);
    prepend(employees, chad);
    prepend(employees, brad);
    prepend(employees, vlad);
    Employee jeff = (Employee)peek(employees);      // Since peek() returns void*, the output must be cast to Employee
    char* wah = Employee_get_name(jeff);            // ...which can be avoided by defining an "EmployeeList" type with a built-in cast.
    printf("%s", wah);
}