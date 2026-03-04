#ifndef EMPLOYEE_H
#define EMPLOYEE_H

typedef struct Employee* Employee;      // Defines the type "Employee" as an instance of struct Employee.

#endif

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Yay! Now you don't have to say "struct" all the time!

extern Employee new_Employee(char* n, int i);        // "extern" is basically like "public" in Java, as I understand it.
extern char* Employee_get_name(Employee grant);
extern int Employee_get_id(Employee grant);
extern void Employee_set_name(Employee grant, char* n);
extern void Employee_set_id(Employee grant, int i);
extern void Employee_print(Employee grant);
extern void Employee_free(Employee grant);