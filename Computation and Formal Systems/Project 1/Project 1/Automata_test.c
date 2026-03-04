#include "Automata.h"
#include <stdio.h>

DFA aho()
{
    DFA aho = newDFA(4);
    DFAsetTransition(aho, 0, 'A', 1);
    DFAsetTransition(aho, 1, 'h', 2);
    DFAsetTransition(aho, 2, 'o', 3);
    DFAsetAccepting(aho, 3, true);
    return aho;
}
DFA oneTwoThree()
{
    DFA ott = newDFA(4);
    DFAsetTransition_all(ott, 0, 0);
    DFAsetTransition(ott, 0, '1', 1);
    DFAsetTransition_all(ott, 1, 1);
    DFAsetTransition(ott, 1, '2', 2);
    DFAsetTransition_all(ott, 2, 2);
    DFAsetTransition(ott, 2, '3', 3);
    DFAsetAccepting(ott, 3, true);
    DFAsetTransition_all(ott, 3, 3);
    return ott;
}
DFA startNum()
{
    DFA start = newDFA(2);
    DFAsetTransition_str(start, 0, "123", 1);
    DFAsetTransition_all(start, 1, 1);
    DFAsetAccepting(start, 1, true);
    return start;
}
DFA binary()
{
    DFA bin = newDFA(4);
    DFAsetTransition(bin, 0, '0', 1);   // 0: even both
    DFAsetTransition(bin, 0, '1', 2);   // 1: odd 0, even 1
    DFAsetTransition(bin, 1, '0', 0);   // 2: even 0, odd 1
    DFAsetTransition(bin, 1, '1', 3);   // 3: odd both
    DFAsetTransition(bin, 2, '0', 3);
    DFAsetTransition(bin, 2, '1', 0);
    DFAsetTransition(bin, 3, '0', 2);
    DFAsetTransition(bin, 3, '1', 1);
    DFAsetAccepting(bin, 1, true);
    return bin;
}

NFA ded()
{
    NFA ded = newNFA(4);
    NFAsetTransition_all(ded, 0, 0);
    NFAsetTransition_all(ded, 3, 0);
    NFAsetTransition(ded, 0, 'd', 1);
    NFAsetTransition(ded, 1, 'e', 2);
    NFAsetTransition(ded, 2, 'd', 3);
    NFAsetAccepting(ded, 3, true);
    return ded;
}
NFA gg()
{
    NFA gg = newNFA(3);
    NFAsetTransition_all(gg, 0, 0);
    NFAsetTransition(gg, 0, 'g', 1);
    NFAsetTransition(gg, 1, 'g', 2);
    NFAsetTransition_all(gg, 2, 2);
    NFAsetAccepting(gg, 2, true);
    return gg;
}
NFA unbach()
{
    NFA ub = newNFA(11); // Whatever number this is
    NFAsetTransition_all(ub, 0, 0);
    NFAsetTransition_all(ub, 1, 1);
    NFAsetTransition_all(ub, 2, 2);
    NFAsetTransition_all(ub, 3, 3);
    NFAsetTransition_all(ub, 4, 4);
    NFAsetTransition_all(ub, 5, 5);
    NFAsetTransition_all(ub, 6, 6);
    NFAsetTransition_all(ub, 7, 7);
    NFAsetTransition_all(ub, 8, 8);
    NFAsetTransition_all(ub, 9, 9);
    NFAsetTransition_all(ub, 10, 10);
    NFAsetTransition(ub, 0, 'b', 1);
    NFAsetTransition(ub, 1, 'b', 2);
    NFAsetTransition(ub, 0, 'r', 3);
    NFAsetTransition(ub, 3, 'r', 2);
    NFAsetTransition(ub, 0, 'c', 4);
    NFAsetTransition(ub, 4, 'c', 5);
    NFAsetTransition(ub, 5, 'c', 2);
    NFAsetTransition(ub, 0, 'h', 6);
    NFAsetTransition(ub, 6, 'h', 7);
    NFAsetTransition(ub, 7, 'h', 2);
    NFAsetTransition(ub, 0, 'a', 8);
    NFAsetTransition(ub, 8, 'a', 9);
    NFAsetTransition(ub, 9, 'a', 10);
    NFAsetTransition(ub, 10, 'a', 2);
    NFAsetAccepting(ub, 2, true);
    return ub;
}

int main(int argc, char* argv[]) {
    DFA hank = aho();
    replDFA(hank);
    freeDFA(hank);

    hank = oneTwoThree();
    replDFA(hank);
    freeDFA(hank);

    hank = startNum();
    replDFA(hank);
    freeDFA(hank);

    hank = binary();
    replDFA(hank);
    freeDFA(hank);



    NFA john = ded();
    replNFA(john);
    freeNFA(john);
    
    john = gg();
    replNFA(john);
    freeNFA(john);

    john = unbach();
    replNFA(john);
    freeNFA(john);
}