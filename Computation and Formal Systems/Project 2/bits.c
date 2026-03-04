/* -*- compile-command: "gcc -o parser -std=c99 -Wall -Werror parser.c"; -*-
 *
 * Based on: focs_11_27_gf.c
 *      The manyest of thanks to George F. for that lovely file.
 */
#include <stdlib.h>
#include <stdbool.h>
#include <stdio.h>
#include <string.h>

#define FAILED NULL

typedef struct NODE *TREE;
struct NODE {
    char label;
    TREE leftmostChild, rightSibling;
};

typedef struct JEFF *FUNCT;
struct JEFF {
    FUNCT (*function)();
};

typedef struct LNODE *LIST;
struct LNODE {
    char* label;
    LIST next;
    FUNCT production;
};

TREE makeNode0(char x);
TREE makeNode1(char x, TREE t);
TREE makeNode2(char x, TREE t1, TREE t2);
TREE makeNode3(char x, TREE t1, TREE t2, TREE t3);
TREE makeNode4(char x, TREE t1, TREE t2, TREE t3, TREE t4);

LIST makeList(char* label, TREE (*production)());
LIST newList(char* label, LIST next, TREE (*production)());
LIST addToList(LIST dest, char* label, TREE (*production)());

TREE B();
TREE S();
TREE ST();
TREE O();
TREE OT();
TREE A();
TREE AT();
TREE P();

FUNCT newFunct();

void prettyPrint(TREE tree);
void runPrint(TREE tree, int indent);

TREE parseWithTable(char** table, char* input, LIST productions);
void freeTree(TREE tree);
int syntacticCategory(char** table, char input);
int terminal(char** table, char input);
char* getProduction(LIST productions, char label);
FUNCT getFunction(LIST productions, char label);
// char** bitTable();

TREE parse(char* input);

int ascii(char grant);

TREE parseTree; /* holds the result of the parse */
char *nextTerminal; /* current position in input string */

/**
 * Returns true if the current input symbol is the given char, otherwise false.
 */
bool lookahead(char c) {
    return *nextTerminal == c;
}

/**
 * If the current input symbol is the given char, advance to the next
 * character of the input and return true, otherwise leave it and return false.
 */
bool match(char c) {
    if (lookahead(c)) {
	nextTerminal += 1;
	return true;
    } else {
	return false;
    }
}



TREE makeNode0(char x)
{
    TREE root;
    root = (TREE) malloc(sizeof(struct NODE));
    root->label = x;
    root->leftmostChild = NULL;
    root->rightSibling = NULL;
    return root;
}

TREE makeNode1(char x, TREE t)
{
    TREE root;
    root = makeNode0(x);
    root->leftmostChild = t;
    return root;
}

TREE makeNode2(char x, TREE t1, TREE t2)
{
    TREE root;
    root = makeNode1(x, t1);
    t1->rightSibling = t2;
    return root;
}

TREE makeNode3(char x, TREE t1, TREE t2, TREE t3)
{
    TREE root;
    root = makeNode1(x, t1);
    t1->rightSibling = t2;
    t2->rightSibling = t3;
    return root;
}

TREE makeNode4(char x, TREE t1, TREE t2, TREE t3, TREE t4)
{
    TREE root;
    root = makeNode1(x, t1);
    t1->rightSibling = t2;
    t2->rightSibling = t3;
    t3->rightSibling = t4;
    return root;
}

LIST makeList(char* label, TREE (*production)())
{
    LIST this;
    this = (LIST)malloc(sizeof(struct LNODE));
    this->next = NULL;
    this->label = label;
    return this;
}
LIST newList(char* label, LIST next, TREE (*production)())
{
    LIST head = makeList(label, production);
    head->next = next;
    return head;
}
LIST addToList(LIST dest, char* label, TREE (*production)())
{
    LIST this = makeList(label, production);
    this->next = dest;
    return this;
}



TREE B()
{
    if (lookahead('0'))
    {
        if (!match('0'))
        {
            printf("Line 113 error\n");
            return FAILED;
        }
        return makeNode1('B', makeNode0('0'));
    }
    else if (lookahead('1'))
    {
        if (!match('1'))
        {
            printf("Line 122 error\n");
            return FAILED;
        }
        return makeNode1('B', makeNode0('1'));
    }
    else
    {
        printf("Line 129 error\n");
        return FAILED;
    }
}
TREE S()
{
    TREE t1 = B();
    if (t1 == NULL)
    {
        printf("Line 138 error\n");
        return FAILED;
    }
    TREE t2 = ST();
    if (t2 == NULL)
    {
        printf("Line 144 error\n");
        return FAILED;
    }
    return makeNode2('S', t1, t2);
}
TREE ST()
{
    if (lookahead('0') || lookahead('1'))
        return makeNode1('s', S());
    return makeNode1('s', makeNode0('e'));
}
TREE O()
{
    TREE t1 = A();
    if (t1 == NULL)
    {
        printf("Line 160 error\n");
        return FAILED;
    }
    TREE t2 = OT();
    if (t2 == NULL)
    {
        printf("Line 166 error\n");
        return FAILED;
    }
    return makeNode2('O', t1, t2);
}
TREE OT()
{
    if (lookahead('|'))
    {
        if (!match('|'))
        {
            printf("Line 177 error\n");
            return FAILED;
        }
        TREE t1 = O();
        if (t1 == NULL)
        {
            printf("Line 183 error\n");
            return FAILED;
        }
        return makeNode2('o', makeNode0('|'), t1);
    }
    else
        return makeNode1('o', makeNode0('e'));
}
TREE A()
{
    TREE t1 = P();
    if (t1 == NULL)
    {
        printf("Line 196 error\n");
        return FAILED;
    }
    TREE t2 = AT();
    if (t2 == NULL)
        return FAILED;
    return makeNode2('A', t1, t2);
}
TREE AT()
{
    if (lookahead('&'))
    {
        if (!match('&'))
        {
            printf("Line 210 error\n");
            return FAILED;
        }
        TREE t1 = A();
        if (t1 == NULL)
        {
            printf("Line 216 error\n");
            return FAILED;
        }
        return makeNode2('a', makeNode0('&'), t1);
    }
    else
        return makeNode1('a', makeNode0('e'));
}
TREE P()
{
    if (lookahead('~'))
    {
        if (!match('~'))
        {
            printf("Line 230 error\n");
            return FAILED;
        }
        TREE t1 = P();
        if (t1 == NULL)
        {
            printf("Line 236 error\n");
            return FAILED;
        }
        return makeNode2('P', makeNode0('~'), t1);
    }
    else if (lookahead('('))
    {
        if (!match('('))
        {
            printf("Line 245 error\n");
            return FAILED;
        }
        TREE t1 = O();
        if (t1 == NULL)
        {
            printf("Line 251 error\n");
            return FAILED;
        }
        if (!match(')'))
        {
            printf("Line 256 error\n");
            return FAILED;
        }
        return makeNode3('P', makeNode0('('), t1, makeNode0(')'));
    }
    else
    {
        TREE t1 = S();
        if (t1 == NULL)
        {
            printf("Line 266 error\n");         // These are now the wrong lines, fix when needed.
            return FAILED;
        }
        return makeNode1('P', t1);
    }
}

FUNCT newFunct()
{
    FUNCT this;
    this = (FUNCT)malloc(sizeof(struct JEFF));
    return this;
}



/**
 * Attempt to parse the given string as a string of balanced parentheses.
 * If the parse succeeds, return the parse tree, else return FAILED (NULL).
 */
TREE parse(char* input) {
    nextTerminal = input;
    if (strcmp(input, "quit\n") != 0)
    {
        TREE parseTree = O();
        if (parseTree != NULL && lookahead('\n')) {
	        return parseTree;
        }
        else if (parseTree == NULL)
        {
            printf("\n  Tree not parsed.");
        }
        else
        {
            printf("\n  Complete input not read.");
        }
    }
    return FAILED;
}
TREE parseWithTable(char** table, char* input, LIST productions)
{
    TREE parseTree;
    /*
                0: ~    1: (    2: )    3: 0    4: 1    5: |    6: &    7: e
        0: O
        1: OT
        2: A
        3: AT
        4: P
        5: S
        6: ST
        7: B
    */
    nextTerminal = input;
    TREE stack = makeNode0('0');
    while (stack != NULL)
    {
        char pop = stack->label;
        TREE popped = stack->leftmostChild;
        int term = terminal(table, pop);
        int synCat = syntacticCategory(table, pop);
        if (term != -1)
        {
            if (!match(pop))
                return FAILED;
        }
        else if (synCat != -1)
        {
            if (table[synCat][ascii(*nextTerminal)] == '\n')            // Let's designate '\n' as the "not a real thing" syncat.
                return FAILED;
            else
            {
                // Push body of corresponding production to stack, in reverse order.
                
                char* prods = getProduction(productions, table[synCat][ascii(*nextTerminal)]);
                TREE newStack = makeNode0(prods[1]);
                TREE newNewStack;
                for (int i=2; i<strlen(prods); i++)
                {
                    newNewStack = makeNode0(prods[i]);
                    newNewStack->leftmostChild = newStack;
                    newStack = newNewStack;
                }
                freeTree(newNewStack);
                popped = newStack;
                freeTree(newStack);
            }
        }
        else
            return FAILED;
        stack = popped;
        freeTree(popped);
    }
    parseTree = stack; // THIS IS NOT WHAT THE PARSE TREE IS: JUST TO BE CLEAR. THIS IS JUST TO SUPRESS WARNINGS.
    return parseTree;
}

void freeTree(TREE tree)
{
    if (tree == NULL)
        return;
    if (tree->leftmostChild != NULL)
        freeTree(tree->leftmostChild);
    if (tree->rightSibling != NULL)
        freeTree(tree->rightSibling);
    free(tree);
}
int syntacticCategory(char** table, char input)
{
    for (int i=0; i<sizeof(table[0])/sizeof(table[0][0]); i++)
    {
        if (table[i][0] == input)
            return i;
    }
    return -1;
}
int terminal(char** table, char input)
{
    for (int i=0; i<sizeof(table)/sizeof(table[0]); i++)
    {
        if (table[0][i] == input)
            return i;
    }
    return -1;
}

char* getProduction(LIST productions, char label)
{
    for (LIST current = productions; current != NULL; current = current->next)
    {
        if (current->label[0] == label)
            return current->label;
    }
    return FAILED;
}

FUNCT getFunction(LIST productions, char label)
{
    for (LIST current = productions; current != NULL; current = current->next)
    {
        if (current->label[0] == label)
            return current->production;
    }
    return newFunct();
}

char** bitTable()
{
    char** table;
    table = malloc(129);
    if (table == NULL)
        return NULL;
    for (int i=0; i<129; i++)
    {
        table[i] = malloc(8);
        if (table[i]==NULL)
            return NULL;
    }

    table[0][ascii('~')] = 'A';
    table[0][ascii('(')] = 'A';
    table[0][ascii('0')] = 'A';
    table[0][ascii('1')] = 'A';
    table[0][ascii('|')] = 'o';
    table[0][ascii('&')] = 'A';
    table[1][ascii('|')] = 'O';
    table[2][ascii('~')] = 'P';
    table[2][ascii('(')] = 'P';
    table[2][ascii('0')] = 'P';
    table[2][ascii('1')] = 'P';
    table[2][ascii('&')] = 'a';
    table[3][ascii('&')] = 'A';
    table[4][ascii('(')] = 'O';
    table[4][ascii('0')] = 'S';
    table[4][ascii('1')] = 'S';
    table[5][ascii('0')] = 'B';
    table[5][ascii('1')] = 'B';

    return table;
}

int ascii(char grant)
{
    char* letters = " !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
    for (int i=0; i<96; i++)
    {
        if (grant == letters[i])
            return i + 32;
    }
    printf("A sadness has occurred.");
    return -1;
}

void prettyPrint(TREE tree)
{
    runPrint(tree, 0);
}
void runPrint(TREE tree, int indent)
{
    if (tree == NULL)
        return;
    for (int i=0; i<indent; i++)
        printf(" ");
    printf("%c\n", tree->label);
    if (tree->leftmostChild != NULL)
        runPrint(tree->leftmostChild, indent+1);
    if (tree->rightSibling != NULL)
        runPrint(tree->rightSibling, indent);
}

void repl()
{
    char input[256];
    while (strlen(input) < 4 || strcmp(input, "quit\n") != 0)
    {
        printf("\nProvide an input to be parsed.\n");
        fflush(stdout);
        fflush(stdin);
        fgets(input, 255, stdin);
        TREE grant = parse(input);
        if (grant == NULL && strcmp(input, "quit\n") != 0)
            printf("\nSomething is afoot.");
        prettyPrint(grant);
    }
    printf("\nEND OF REPL.\n\n");
    fflush(stdout);
}

int main()
{
    printf("Hello there.\n");
    repl();
    freeTree(parseTree);


    LIST productions = makeList("B", B);
    productions = addToList(productions, "sS", ST);
    productions = addToList(productions, "se", ST);
    productions = addToList(productions, "SBs", S);
    productions = addToList(productions, "PP", P);
    productions = addToList(productions, "PO", P);
    productions = addToList(productions, "PS", P);
    productions = addToList(productions, "aA", AT);
    productions = addToList(productions, "ae", AT);
    productions = addToList(productions, "APa", A);
    productions = addToList(productions, "oO", OT);
    productions = addToList(productions, "oe", OT);
    productions = addToList(productions, "OAo", O);
    TREE jeff = parseWithTable(bitTable(), "010\n", productions);
    prettyPrint(jeff);
    freeTree(jeff);
    printf("If you haven't noticed, the table-driven parsing doesn't work on account of being half-finished. My deepest apologies.");
}