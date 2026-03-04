#include "Employee.h"

#ifndef NODE_H
#define NODE_H

typedef struct Node* Node;

#endif

Node newNode(void* grant);

#ifndef LINKEDLIST_H
#define LINKEDLIST_H

typedef struct LinkedList* LinkedList;

#endif

LinkedList newLinkedList();
LinkedList prepend(LinkedList list, void* grant);
void* peek(LinkedList list);