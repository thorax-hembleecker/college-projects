#include <stdio.h>
#include <stdlib.h>     // Needed for malloc, which is pretty important.
#include <string.h>

struct Employee
{
    char* name;
    int id;
};
struct Employee* newEmployee(char* n, int i)
{
    struct Employee* this = (struct Employee*)malloc(sizeof(struct Employee));
    if (this == NULL)       // This means the thingy is out of memory.
        return NULL;
    this->name = strdup(n);     // This is necessary to ensure the name can be freed, which can only happen if it's allocated with malloc.
    this->id = i;
    return this;
}
char* getName(struct Employee* grant)
{
    return grant->name;
}
int getID(struct Employee* grant)
{
    return grant->id;
}
void setName(struct Employee* grant, char* n)
{
    grant->name = strdup(n);
}
void setID(struct Employee* grant, int i)
{
    grant->id = i;
}
void printEmployee(struct Employee* grant)
{
    if (grant == NULL)
        printf("The specified employee does not exist.");
    else
        printf("%s has ID #%d.\n", grant->name, grant->id);
}
char* strdup(const char* old)
{
    char* this = (char*)malloc(strlen(old));
    strcpy(this, old);
    return this;
}
void freeEmployee(struct Employee* grant)
{
    free(grant->name);
    free(grant);
}

int main(int argc, char* argv[]) {
    struct Employee* grant = newEmployee("Grant", 323);
    struct Employee* grond = newEmployee("Grond", 284);
    struct Employee* bombadil = newEmployee("Tom Bombadil", 1);
    struct Employee* joe = newEmployee("Indiana Joe", 1989);

    printEmployee(grant);
    printEmployee(grond);
    printEmployee(bombadil);
    printEmployee(joe);
    fflush(stdout);

    setName(bombadil, "Iarwain");
    setID(bombadil, 0);
    printEmployee(bombadil);
    freeEmployee(bombadil);
    printEmployee(bombadil);
}