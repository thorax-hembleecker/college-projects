#include <stdio.h>      // Basically like the "import java.util.*" of C, I guess.
#include <string.h>     // Needed for strcmp (string compare, basically compareTo).

void stars(int stars)
{
    for (int i=0; i<stars; i++)
    {
        printf("*");
    }
    printf("\n");
    return;
}
float kelvinize(float f)
{
    return (5.0/9.0)*(f - 32) + 273.15;
}
void copyPaste(char file1[], char file2[])
{
    FILE* grant = fopen(file1, "r");
    if (grant == NULL)
        printf("Open file1 failed\n");
    FILE* grond = fopen(file2, "w");
    if (grond == NULL)
        printf("Open file2 failed\n");
    char todd[1000];
    fgets(todd, 999, grant);
    if (strcmp(todd, "") == 0)
        printf("Read file1 failed\n");
    fprintf(grond, todd);
    fclose(grant);
    fclose(grond);
}

int main(int argc, char* argv[]) {
    float fahrenheit = 212;
    float kelvin;
    kelvin = (5.0/9.0)*(fahrenheit - 32) + 273.15;
    printf("%.2f degrees F is %.2f K\n", fahrenheit, kelvin);       // "%f" is a placeholder for a float.

    if (fahrenheit <= 32)
        printf("That's freezing!\n");
    else if (fahrenheit >= 212)
        printf("That's boiling!\n");
    else
        printf("That's nice.\n");
    for (int i=1; i<11; i++)
        printf("%d, ", i);      // "%d" is a placeholder for an int (decimal).
    for (int i=10; i>0; i--)
        printf("%d, ", i);
    stars(12);
    printf("%.2f\n", kelvinize(212));

    int chad;
    float brad;
    char vlad[180];
    fflush(stdout);     // Flushes buffered memory in standard output (i.e. the previous print statements).
    scanf("%d,%f,%179s", &chad, &brad, vlad);       // The '&' means the thingy is a pointer.
    printf("%d, %.2f, %s\n", chad, brad, vlad);

    char input[256];
    fflush(stdin);
    while (input[0] != '\n')
    {
        printf("Please enter something excellent.\n");
        fgets(input, 255, stdin);
        printf("%s", input);
    }

    copyPaste("darthplagueis.txt", "plagueispartii.txt");
}