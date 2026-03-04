#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "DFA.h"
#include "LinkedList.h"

extern int ascii(char grant)
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



struct NFA {
    int size;
    LinkedList init;
    LinkedList** transitions;
};

extern NFA newNFA(int states)
{
    NFA this = (NFA)malloc(sizeof(NFA));
    if (this==NULL)
        return NULL;
    this->size = states;
    this->init = newLinkedList();
    this->init->head = newNode(0);
    this->transitions = malloc(129 * sizeof(LinkedList));
    if (this->transitions==NULL)
        return NULL;
    for (int i=0; i<129; i++)
    {
        this->transitions[i] = malloc(states * sizeof(LinkedList));
        if (this->transitions[i]==NULL)
            return NULL;
    }
    for (int r=0; r<128; r++)
    {
        for (int c=0; c<states; c++)
        {
            this->transitions[r][c] = newLinkedList();
            this->transitions[r][c]->head = newNode(-1);
        }
    }
    for (int c=0; c<states; c++)
    {
        this->transitions[128][c] = newLinkedList();
        this->transitions[128][c]->head = newNode(0);
    }
    return this;
}
extern void freeNFA(NFA nfa)
{
    for (int i=0; i<129; i++)
    {
        for (int j=0; j<nfa->size; j++)
        {
            for (Node current = nfa->transitions[i][j]->head; current->next != NULL; current = current->next)
            {
            Node n = current->next;
            while (n->next != NULL)
                n = n->next;
            free(n);
            }
        free(nfa->transitions[i][j]->head);
        free(nfa->transitions[i][j]);
        }
        free(nfa->transitions[i]);
    }
    free(nfa->transitions);
    free(nfa);
}
extern int NFAgetSize(NFA nfa)
{
    return nfa->size;
}
extern LinkedList NFAgetTransition(NFA nfa, int src, char sym)
{
    return nfa->transitions[ascii(sym)][src];
}
extern void NFAsetTransition(NFA nfa, int src, char sym, int dst)
{
    LinkedList grant = newLinkedList();
    prepend(grant, dst);
    nfa->transitions[ascii(sym)][src] = merge(nfa->transitions[ascii(sym)][src], grant);
}
extern void NFAsetTransition_LL(NFA nfa, int src, char sym, LinkedList dst)
{
    nfa->transitions[ascii(sym)][src] = merge(nfa->transitions[ascii(sym)][src], dst);
}
extern void NFAsetTransition_str(NFA nfa, int src, char *str, int dst)
{
    LinkedList grant = newLinkedList();
    prepend(grant, dst);
    for (int i=0; i<strlen(str); i++)
    {
        nfa->transitions[ascii(str[i])][src] = merge(nfa->transitions[ascii(str[i])][src], grant);
    }
}
extern void NFAsetTransition_str_LL(NFA nfa, int src, char *str, LinkedList dst)
{
    for (int i=0; i<strlen(str); i++)
    {
        nfa->transitions[ascii(str[i])][src] = merge(nfa->transitions[ascii(str[i])][src], dst);
    }
}
extern void NFAsetTransition_all(NFA nfa, int src, int dst)
{
    LinkedList grant = newLinkedList();
    prepend(grant, dst);
    for (int i=0; i<128; i++)
    {
        nfa->transitions[i][src] = merge(nfa->transitions[i][src], grant);
    }
}
extern void NFAsetTransition_all_LL(NFA nfa, int src, LinkedList dst)
{
    for (int i=0; i<128; i++)
    {
        nfa->transitions[i][src] = merge(nfa->transitions[i][src], dst);
    }
}
extern void NFAresetTransition(NFA nfa, int src, char sym)
{
    for (Node current = nfa->transitions[ascii(sym)][src]->head; current->next != NULL; current = current->next)
    {
        Node n = current->next;
        while (n->next != NULL)
            n = n->next;
        free(n);
    }
    free(nfa->transitions[ascii(sym)][src]->head);
    free(nfa->transitions[ascii(sym)][src]);
    nfa->transitions[ascii(sym)][src] = newLinkedList();
    nfa->transitions[ascii(sym)][src]->head = newNode(-1);
}
extern void NFAsetAccepting(NFA nfa, int state, bool value)
{
    if (value == true)
    {
        nfa->transitions[128][state] = newLinkedList();
        nfa->transitions[128][state]->head = newNode(1);
    }
    else
    {
        nfa->transitions[128][state] = newLinkedList();
        nfa->transitions[128][state]->head = newNode(0);
    }
}
extern bool NFAgetAccepting(NFA nfa, LinkedList states)
{
    for (Node current = states->head; current != NULL; current = current->next)
    {
        if (nfa->transitions[128][current->data]->head->data == 1)
            return true;
    }
    return false;
}
extern bool runNFA(NFA nfa, char *input)
{
    LinkedList current = nfa->init;
    for (int i=0; i<strlen(input); i++)
    {
        if (input[i] != '\n')
        {
            LinkedList merged = newLinkedList();
            for (Node grant = current->head; grant != NULL; grant = grant->next)
                merged = merge(merged, NFAgetTransition(nfa, grant->data, input[i]));
            current = merged;
        }
    }
    return NFAgetAccepting(nfa, current);
}
extern void printNFA(NFA nfa, char *input)
{
    if (runNFA(nfa, input) == true)
        printf("The NFA accepts the input %s", input);
    else
        printf("The NFA rejects the input %s", input);
    fflush(stdout);
}
extern void replNFA(NFA nfa)
{
    char input[256];
    while (input[0] != '\n')
    {
        printf("\nProvide an input for the NFA (empty string to quit).\n");
        fflush(stdout);
        fflush(stdin);
        fgets(input, 255, stdin);
        printNFA(nfa, input);
    }
    printf("\nEND OF REPL.\n");
    fflush(stdout);
}




struct DFA {
    NFA dfa;
};

extern DFA newDFA(int states)
{
    DFA this = (DFA)malloc(sizeof(DFA));
    if (this==NULL)
        return NULL;
    this->dfa = newNFA(states);
    return this;
}
extern void freeDFA(DFA dfa)
{
    freeNFA(dfa->dfa);
    free(dfa);
}
extern int DFAgetSize(DFA dfa)
{
    return NFAgetSize(dfa->dfa);
}
extern int DFAgetTransition(DFA dfa, int src, char sym)
{
    if (NFAgetTransition(dfa->dfa, src, sym)->head->next == NULL)
        return NFAgetTransition(dfa->dfa, src, sym)->head->data;
    else
    {
        printf("\nSomething is afoot here. Your DFA is not, in fact, a DFA.\n");
        return -1;
    }
}
extern void DFAsetTransition(DFA dfa, int src, char sym, int dst)
{
    NFAresetTransition(dfa->dfa, src, sym);
    NFAsetTransition(dfa->dfa, src, sym, dst);
}
extern void DFAsetTransition_str(DFA dfa, int src, char *str, int dst)
{
    for (int i=0; i<strlen(str); i++)
    {
        DFAsetTransition(dfa, src, str[i], dst);
    }
}
extern void DFAsetTransition_all(DFA dfa, int src, int dst)
{
    char* letters = " !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
    for (int i=32; i<128; i++)
    {
        DFAsetTransition(dfa, src, letters[i-32], dst);
    }
}
extern void DFAsetAccepting(DFA dfa, int state, bool value)
{
    NFAsetAccepting(dfa->dfa, state, value);
}
extern bool DFAgetAccepting(DFA dfa, int state)
{
    LinkedList grant = newLinkedList();
    prepend(grant, state);
    return NFAgetAccepting(dfa->dfa, grant);
}
extern bool runDFA(DFA dfa, char *input)
{
    return runNFA(dfa->dfa, input);
}
extern void printDFA(DFA dfa, char *input)
{
    if (runDFA(dfa, input) == true)
        printf("The DFA accepts the input %s", input);
    else
        printf("The DFA rejects the input %s", input);
    fflush(stdout);
}
extern void repl(DFA dfa)
{
    char input[256];
    while (input[0] != '\n')
    {
        printf("\nProvide an input for the DFA (empty string to quit).\n");
        fflush(stdout);
        fflush(stdin);
        fgets(input, 255, stdin);
        printDFA(dfa, input);
    }
    printf("\nEND OF REPL.\n");
    fflush(stdout);
}

extern DFA NFAtoDFA(NFA nfa)
{
    bool isDFA = true;
    for (int i=0; i<129; i++)
    {
        for (int j=0; j<nfa->size; j++)
        {
            if (nfa->transitions[i][j]->head->next != NULL)
                isDFA = false;
        }
    }
    if (isDFA)
    {
        DFA grant = newDFA(nfa->size);
        grant->dfa = nfa;
        return grant;
    }
    // NFA build = newNFA(pow(2, nfa->size));
    LinkedList states = newLinkedList();
    prepend(states, 0);
    // LinkedList* recognizedStates = malloc(pow(2, nfa->size) * sizeof(LinkedList));
    char* letters = " !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

    // for (Node current = states->head, current != NULL; current = current->next)
    // {
    //     // for (int i=0; i<pow(2, nfa->size); i++)
    //     {
    //         for (Node recog = recognizedStates->head, recog != NULL; recog = current->next)
    //         {

    //         }
    //     }
    // }

    // for (int i=32; i<128; i++)
    // {
    //     for (Node current = states->head, current != NULL; current = current->next)
    //     {
    //         NFAgetTransition(nfa, current->data, letters[i-32]);
    //         NFAsetTransition(build, )
    //     }
    // }
    
}