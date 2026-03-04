
/*
 * Modified from: dfa.h
    * Creator: George F.
    * Created: Thu Sep 1 2016
 */

#ifndef DFA_H
#define DFA_H

#include <stdbool.h>
#include "LinkedList.h"

#ifndef NFA_H
#define NFA_H

typedef struct NFA* NFA;

#endif

extern NFA newNFA(int states);
extern void freeNFA(NFA nfa);
extern int NFAgetSize(NFA nfa);
extern LinkedList NFAgetTransition(NFA nfa, int src, char sym);
extern void NFAsetTransition(NFA nfa, int src, char sym, int dst);
extern void NFAsetTransition_LL(NFA nfa, int src, char sym, LinkedList dst);
extern void NFAsetTransition_str(NFA nfa, int src, char *str, int dst);
extern void NFAsetTransition_str_LL(NFA nfa, int src, char *str, LinkedList dst);
extern void NFAsetTransition_all(NFA nfa, int src, int dst);
extern void NFAsetTransition_all_LL(NFA nfa, int src, LinkedList dst);
extern void NFAresetTransition(NFA nfa, int src, char sym);
extern void NFAsetAccepting(NFA nfa, int state, bool value);
extern bool NFAgetAccepting(NFA nfa, LinkedList states);
extern bool runNFA(NFA nfa, char *input);
extern void printNFA(NFA nfa, char *input);
extern void replNFA(NFA nfa);

extern int ascii(char grant);

typedef struct DFA* DFA;

#endif

extern DFA newDFA(int states);     // Allocate and return a new DFA containing the given number of states.
extern void freeDFA(DFA dfa);      // Free the given DFA.
extern int DFAgetSize(DFA dfa);   // Return the number of states in the given DFA.
extern int DFAgetTransition(DFA dfa, int src, char sym);      // Return the state specified by the DFA's transition function.
extern void DFAsetTransition(DFA dfa, int src, char sym, int dst);    // Set the transition from state src on input sym to be state dst.
extern void DFAsetTransition_str(DFA dfa, int src, char *str, int dst);   // Set the DFA's transitions for each symbol in the given str.
extern void DFAsetTransition_all(DFA dfa, int src, int dst);      // Set the transitions of the given DFA for all input symbols.
extern void DFAsetAccepting(DFA dfa, int state, bool value);      // Set whether the given DFA's state is accepting or not.
extern bool DFAgetAccepting(DFA dfa, int state);      // Return true if the given DFA's state is an accepting state.
extern bool runDFA(DFA dfa, char *input);      // Run the DFA on the input string and return true if it accepts the input.
extern void printDFA(DFA dfa, char *input);     // Print the given DFA to System.out.
extern void repl(DFA dfa);