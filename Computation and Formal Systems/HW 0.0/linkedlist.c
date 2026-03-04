#include "linkedlist.h"
#include <stdlib.h>

typedef struct Node* Node;

struct Node {
    void* data;     // "void*" is a pointer to an unknown type. Jolly good if you ask me.
    Node next;
};
Node newNode(void* grant)
{
    Node this = (Node)malloc(sizeof(struct Node));
    if (this==NULL)
        return NULL;
    this->data = grant;
    this->next = NULL;
    return this;
}

typedef struct LinkedList* LinkedList;

struct LinkedList {
    Node head;
};
LinkedList newLinkedList()
{
    LinkedList this = (LinkedList)malloc(sizeof(struct LinkedList));
    if (this==NULL)
        return NULL;
    this->head = NULL;
    return this;
}
LinkedList prepend(LinkedList list, void* grant)
{
    Node this = newNode(grant);
    if (this == NULL)
        return NULL;
    this->next = list->head;
    list->head = this;
    return list;
}
void* peek(LinkedList list)
{
    if (list->head == NULL)
        return NULL;
    else
        return list->head->data;
}