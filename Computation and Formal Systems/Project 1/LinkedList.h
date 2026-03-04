#ifndef NODE_H
#define NODE_H

typedef struct Node* Node;

struct Node {
    int data;
    Node next;
};

#endif

extern Node newNode(int grant);

#ifndef LINKEDLIST_H
#define LINKEDLIST_H

typedef struct LinkedList* LinkedList;

struct LinkedList {
    Node head;
};

#endif

extern LinkedList newLinkedList();
extern LinkedList prepend(LinkedList list, int grant);
extern int contains(LinkedList list, int grant);
extern LinkedList merge(LinkedList one, LinkedList two);