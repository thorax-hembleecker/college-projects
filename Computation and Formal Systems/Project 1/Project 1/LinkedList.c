#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "LinkedList.h"

extern Node newNode(int grant)
{
    Node this = (Node)malloc(sizeof(Node));
    if (this==NULL)
        return NULL;
    this->data = grant;
    this->next = NULL;
    return this;
}

extern LinkedList newLinkedList()
{
    LinkedList this = (LinkedList)malloc(sizeof(LinkedList));
    if (this==NULL)
        return NULL;
    this->head = NULL;
    return this;
}
extern LinkedList prepend(LinkedList list, int grant)
{
    Node this = newNode(grant);
    if (this == NULL)
        return NULL;
    this->next = list->head;
    list->head = this;
    return list;
}
extern int contains(LinkedList list, int grant)
{
    Node current = list->head;
    while (current != NULL)
    {
        if (current->data == grant)
            return 1;
        current = current->next;
    }
    return 0;
}
extern LinkedList merge(LinkedList one, LinkedList two)
{
    LinkedList merged = newLinkedList();
    Node current = one->head;
    while (current != NULL)
    {
        if (contains(merged, current->data) == 0 && current->data != -1)
            prepend(merged, current->data);
        current = current->next;
    }
    current = two->head;
    while (current != NULL)
    {
        if (contains(merged, current->data) == 0 && current->data != -1)
            prepend(merged, current->data);
        current = current->next;
    }
    return merged;
}