#include <stdio.h>
#include <stdlib.h>

struct Node
{
    int val;
    struct Node* left;
    struct Node* right;
};      // This is an important semicolon. Don't forget it.
struct Node* newNode(int v)
{
    struct Node* this = (struct Node*)malloc(sizeof(struct Node));
    if (this == NULL)
        return NULL;
    this->val = v;
    this->left = NULL;
    this->right = NULL;
    return this;
}
void printNode(struct Node* grant)
{
    if (grant->left != NULL)
        printNode(grant->left);
    printf("%d ", grant->val);
    if (grant->right != NULL)
        printNode(grant->right);
}
struct Node* addToTree(struct Node* root, int i)
{
    if (root == NULL)
    {
        root = newNode(i);
        return root;
    }
    else
    {
        struct Node* current = root;
        while (1 == 1)
        {
            if (current->val > i)
            {
                if (current->left == NULL)
                {
                    current->left = newNode(i);
                    return root;
                }
                current = current->left;
            }
            else if (current->val < i)
            {
                if (current->right == NULL)
                {
                    current->right = newNode(i);
                    return root;
                }
                current = current->right;
            }
            else
                return root;
        }
    }
}
struct Tree {
    struct Node* root;
};
struct Tree* newTree()
{
    struct Tree* this = (struct Tree*)malloc(sizeof(struct Tree));
    if (this == NULL)
        return NULL;
    this->root = NULL;
    return this;
}
void Tree_add(struct Tree* benedictCumberbatch, int i)
{
    benedictCumberbatch->root = addToTree(benedictCumberbatch->root, i);
}
void Tree_print(struct Tree* benedictWong)
{
    printNode(benedictWong->root);
}
int lookup(struct Tree* tree, int i)
{
    struct Node* current = tree->root;
    while (1 == 1)
    {
        if (current->val > i)
        {
            if (current->left == NULL)
                return 0;
            current = current->left;
        }
        else if (current->val < i)
        {
            if (current->right == NULL)
                return 0;
            current = current->right;
        }
        else
            return 1;
    }
}

int main(int argc, char* argv[]) {
    struct Node* grant = newNode(56);
    struct Node* charles = newNode(23);
    struct Node* jimmy = newNode(84);
    printNode(grant);
    printNode(charles);
    printNode(jimmy);
    printf("\n");

    struct Tree* jeremy = newTree();
    Tree_add(jeremy, 34);
    Tree_add(jeremy, 67);
    Tree_add(jeremy, 104);
    Tree_add(jeremy, 205);
    Tree_add(jeremy, 31);
    Tree_add(jeremy, 12);
    Tree_add(jeremy, 98);
    Tree_add(jeremy, 34);
    Tree_print(jeremy);
    printf("\n");
    printNode(jeremy->root->left);
    printf("\n");
    printNode(jeremy->root->right);
    printf("\n");

    printf("%d", lookup(jeremy, 34));
    printf("%d", lookup(jeremy, 35));
    printf("%d", lookup(jeremy, 67));
    printf("%d", lookup(jeremy, 12));
    printf("%d", lookup(jeremy, 205));
    printf("%d", lookup(jeremy, 0));
}