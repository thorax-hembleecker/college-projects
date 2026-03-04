
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// General note: for unspecified attributes, I used "*" for strings and -1 for integers.

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

typedef struct PNBs *PNB;
struct PNBs {
    int playerID;
    char* name;
    char* birthDate;
    PNB next;
};
PNB newPNB(int p, char* n, char* b, PNB next)
{
    PNB this = (PNB)malloc(sizeof(struct PNBs));
    this->playerID = p;
    this->name = n;
    this->birthDate = b;
    this->next = next;
    return this;
}
int PNBequals(PNB a, PNB b)
{
    if ((a->playerID == b->playerID || a->playerID == -1 || b->playerID == -1) && (strcmp(a->name, b->name) == 0 || strcmp(a->name, "*") == 0 || strcmp(b->name, "*") == 0) && (strcmp(a->birthDate, b->birthDate) == 0 || strcmp(a->birthDate, "*") == 0 || strcmp(b->birthDate, "*") == 0))
        return 1;
    else return 0;
}
typedef struct TPNs *TPN;
struct TPNs {
    char* team;
    int playerID;
    int number;
    TPN next;
};
TPN newTPN(char* t, int p, int n, TPN next)
{
    TPN this = (TPN)malloc(sizeof(struct TPNs));
    this->team = t;
    this->playerID = p;
    this->number = n;
    this->next = next;
    return this;
}
int TPNequals(TPN a, TPN b)
{
    if ((strcmp(a->team, b->team) == 0 || strcmp(a->team, "*") == 0 || strcmp(b->team, "*") == 0) && (a->playerID == b->playerID || a->playerID == -1 || b->playerID == -1) && (a->number == b->number || a->number == -1 || b->number == -1))
        return 1;
    else return 0;
}
typedef struct TCs *TC;
struct TCs {
    char* team;
    char* city;
    TC next;
};
TC newTC(char* t, char* c, TC next)
{
    TC this = (TC)malloc(sizeof(struct TCs));
    this->team = t;
    this->city = c;
    this->next = next;
    return this;
}
int TCequals(TC a, TC b)
{
    if ((strcmp(a->team, b->team) == 0 || strcmp(a->team, "*") == 0 || strcmp(b->team, "*") == 0) && (strcmp(a->city, b->city) == 0 || strcmp(a->city, "*") == 0 || strcmp(b->city, "*") == 0))
        return 1;
    else return 0;
}
typedef struct GHVDs *GHVD;
struct GHVDs {
    int gameID;
    char* homeTeam;
    char* awayTeam;
    char* date;
    GHVD next;
};
GHVD newGHVD(int g, char* h, char* a, char* d, GHVD next)
{
    GHVD this = (GHVD)malloc(sizeof(struct GHVDs));
    this->gameID = g;
    this->homeTeam = h;
    this->awayTeam = a;
    this->date = d;
    this->next = next;
    return this;
}
int GHVDequals(GHVD a, GHVD b)
{
    if ((a->gameID == b->gameID || a->gameID == -1 || b->gameID == -1) && (strcmp(a->homeTeam, b->homeTeam) == 0 || strcmp(a->homeTeam, "*") == 0 || strcmp(b->homeTeam, "*") == 0) && (strcmp(a->awayTeam, b->awayTeam) == 0 || strcmp(a->awayTeam, "*") == 0 || strcmp(b->awayTeam, "*") == 0) && (strcmp(a->date, b->date) == 0 || strcmp(a->date, "*") == 0 || strcmp(b->date, "*") == 0))
        return 1;
    else return 0;
}
typedef struct GPGs *GPG;
struct GPGs {
    int gameID;
    int playerID;
    int goals;
    GPG next;
};
GPG newGPG(int g1, int p, int g2, GPG next)
{
    GPG this = (GPG)malloc(sizeof(struct GPGs));
    this->gameID = g1;
    this->playerID = p;
    this->goals = g2;
    this->next = next;
    return this;
}
int GPGequals(GPG a, GPG b)
{
    if ((a->gameID == b->gameID || a->gameID == -1 || b->gameID == -1) && (a->playerID == b->playerID || a->playerID == -1 || b->playerID == -1) && (a->goals == b->goals || a->goals == -1 || b->goals == -1))
        return 1;
    else return 0;
}

typedef struct PNBTABLE {
    int size;
    PNB* table;
    int (*hash)(PNB key);
} *PNB_TABLE;
PNB_TABLE newPNBTable(int s, int (*h)(PNB))
{
    PNB_TABLE this = (PNB_TABLE)malloc(sizeof(struct PNBTABLE));
    this->size = s;
    this->table = malloc(s * sizeof(PNB));
    for (int i=0; i<s; i++)
        this->table[i] = NULL;
    this->hash = h;
    return this;
}
typedef struct TPNTABLE {
    int size;
    TPN* table;
    int (*hash)(TPN key);
} *TPN_TABLE;
TPN_TABLE newTPNTable(int s, int (*h)(TPN))   
{
    TPN_TABLE this = (TPN_TABLE)malloc(sizeof(struct TPNTABLE));
    this->size = s;
    this->table = malloc(s * sizeof(TPN));
    for (int i=0; i<s; i++)
        this->table[i] = NULL;
    this->hash = h;
    return this;
}
typedef struct TCTABLE {
    int size;
    TC* table;
    int (*hash)(TC key);
} *TC_TABLE;
TC_TABLE newTCTable(int s, int (*h)(TC))   
{
    TC_TABLE this = (TC_TABLE)malloc(sizeof(struct TCTABLE));
    this->size = s;
    this->table = malloc(s * sizeof(TC));
    for (int i=0; i<s; i++)
        this->table[i] = NULL;
    this->hash = h;
    return this;
}
typedef struct GHVDTABLE {
    int size;
    GHVD* table;
    int (*hash)(GHVD key);
} *GHVD_TABLE;
GHVD_TABLE newGHVDTable(int s, int (*h)(GHVD))   
{
    GHVD_TABLE this = (GHVD_TABLE)malloc(sizeof(struct GHVDTABLE));
    this->size = s;
    this->table = malloc(s * sizeof(GHVD));
    for (int i=0; i<s; i++)
        this->table[i] = NULL;
    this->hash = h;
    return this;
}
typedef struct GPGTABLE {
    int size;
    GPG* table;
    int (*hash)(GPG key);
} *GPG_TABLE;
GPG_TABLE newGPGTable(int s, int (*h)(GPG))   
{
    GPG_TABLE this = (GPG_TABLE)malloc(sizeof(struct GPGTABLE));
    this->size = s;
    this->table = malloc(s * sizeof(GPG));
    for (int i=0; i<s; i++)
        this->table[i] = NULL;
    this->hash = h;
    return this;
}

typedef struct DATA {
    PNB_TABLE pnb;
    TPN_TABLE tpn;
    TC_TABLE tc;
    GHVD_TABLE ghvd;
    GPG_TABLE gpg;
} *DATABASE;


void printPNB(PNB grant)
{
    if (grant == NULL)
        printf("\nNULL");
    else
    {
        printf("\n");
        PNB current = newPNB(grant->playerID, grant->name, grant->birthDate, grant->next);
        while (current != NULL)
        {
            printf("(%d, %s, %s)", current->playerID, current->name, current->birthDate);
            current = current->next;
            if (current != NULL)
                printf(", ");
        }
    }
    fflush(stdout);
}
void insertPNB(PNB grant, PNB_TABLE grantpog)
{
    if (grant->playerID == -1 || strcmp(grant->name, "*") == 0 || strcmp(grant->birthDate, "*") == 0)
    {
        printf("\nInvalid input.\n");
        return;
    }
    int index = grantpog->hash(grant);
    if (grantpog->table[index] == NULL)
    {
        grantpog->table[index] = grant;
    }
    else
    {
        PNB current = newPNB(grantpog->table[index]->playerID, grantpog->table[index]->name, grantpog->table[index]->birthDate, grantpog->table[index]->next);
        while (current != NULL)
        {
            if (PNBequals(current, grant) == 1)
            {
                printf("\nAlready exists.\n");
                return;
            }
            current = current->next; 
        }
        free(current);
        grant->next = grantpog->table[index];
        grantpog->table[index] = grant;
    }
}
void deletePNB(PNB grant, PNB_TABLE grantpog)
{
    if (grant->playerID == -1)
    {
        for (int i=0; i<grantpog->size; i++)
        {
            while (grantpog->table[i] == NULL)
            {
                i++;
                if (i >= grantpog->size)
                    return;
            }
            PNB current = grantpog->table[i];
            if (PNBequals(current, grant) == 1)
            {
                grantpog->table[i] = current->next;
                current = grantpog->table[i];
            }
            if (current != NULL)
            {
                while (current->next != NULL)
                {
                    if (PNBequals(current->next, grant) == 1)
                    {
                        PNB nextnext = current->next->next;
                        current->next = nextnext;
                    }
                    else
                        current = current->next;
                }
            }
        }
    }
    else
    {
        int index = grantpog->hash(grant);
        PNB current = grantpog->table[index];
        if (current == NULL)
        {
            printf("\nDoes not exist.\n");
            return;
        }
        if (PNBequals(current, grant) == 1)
        {
            grantpog->table[index] = current->next;
            current = grantpog->table[index];
        }
        if (current != NULL)
        {
            while (current->next != NULL)
            {
                if (PNBequals(current->next, grant) == 1)
                {
                    PNB nextnext = current->next->next;
                    current->next = nextnext;
                }
                else
                    current = current->next;
            }
        }
    }
}
PNB lookupPNB(PNB grant, PNB_TABLE grantpog)
{
    PNB result = NULL;
    if (grant->playerID == -1)
    {
        PNB current = NULL;
        for (int i=0; i<grantpog->size; i++)
        {
            while (grantpog->table[i] == NULL)
            {
                i++;
                if (i >= grantpog->size)
                    return result;
            }
            current = newPNB(grantpog->table[i]->playerID, grantpog->table[i]->name, grantpog->table[i]->birthDate, grantpog->table[i]->next);
            while (current != NULL)
            {
                if (PNBequals(current, grant) == 1)
                {
                    result = newPNB(current->playerID, current->name, current->birthDate, result);
                }
                current = current->next;
            }
        }
    }
    else
    {
        int index = grantpog->hash(grant);
        PNB current = newPNB(grantpog->table[index]->playerID, grantpog->table[index]->name, grantpog->table[index]->birthDate, grantpog->table[index]->next);
        while (current != NULL)
        {
            if (PNBequals(current, grant) == 1)
            {
                result = newPNB(current->playerID, current->name, current->birthDate, result);
            }
            current = current->next;
        }
    }
    return result;
}

void printTPN(TPN grant)
{
    if (grant == NULL)
        printf("\nNULL");
    else
    {
        printf("\n");
        TPN current = newTPN(grant->team, grant->playerID, grant->number, grant->next);
        while (current != NULL)
        {
            printf("(%s, %d, %d)", current->team, current->playerID, current->number);
            current = current->next;
            if (current != NULL)
                printf(", ");
        }
    }
    fflush(stdout);
}
void insertTPN(TPN grant, TPN_TABLE grantpog)
{
    if (strcmp(grant->team, "*") == 0 || grant->playerID == -1 || grant->number == -1)
    {
        printf("\nInvalid input.\n");
        return;
    }
    int index = grantpog->hash(grant);
    if (grantpog->table[index] == NULL)
    {
        grantpog->table[index] = grant;
    }
    else
    {
        TPN current = newTPN(grantpog->table[index]->team, grantpog->table[index]->playerID, grantpog->table[index]->number, grantpog->table[index]->next);
        while (current != NULL)
        {
            if (TPNequals(current, grant) == 1)
            {
                printf("\nAlready exists.\n");
                return;
            }
            current = current->next; 
        }
        free(current);
        grant->next = grantpog->table[index];
        grantpog->table[index] = grant;
    }
}
void deleteTPN(TPN grant, TPN_TABLE grantpog)
{
    if (strcmp(grant->team, "*") == 0 || grant->playerID == -1 || grant->number == -1)
    {
        for (int i=0; i<grantpog->size; i++)
        {
            while (grantpog->table[i] == NULL)
            {
                i++;
                if (i >= grantpog->size)
                    return;
            }
            TPN current = grantpog->table[i];
            if (TPNequals(current, grant) == 1)
            {
                grantpog->table[i] = current->next;
                current = grantpog->table[i];
            }
            if (current != NULL)
            {
                while (current->next != NULL)
                {
                    if (TPNequals(current->next, grant) == 1)
                    {
                        TPN nextnext = current->next->next;
                        current->next = nextnext;
                    }
                    else
                        current = current->next;
                }
            }
        }
    }
    else
    {
        int index = grantpog->hash(grant);
        TPN current = grantpog->table[index];
        if (current == NULL)
        {
            printf("\nDoes not exist.\n");
            return;
        }
        if (TPNequals(current, grant) == 1)
        {
            grantpog->table[index] = current->next;
            current = grantpog->table[index];
        }
        if (current != NULL)
        {
            while (current->next != NULL)
            {
                if (TPNequals(current->next, grant) == 1)
                {
                    TPN nextnext = current->next->next;
                    current->next = nextnext;
                }
                else
                    current = current->next;
            }
        }
    }
}
TPN lookupTPN(TPN grant, TPN_TABLE grantpog)
{
    TPN result = NULL;
    if (strcmp(grant->team, "*") == 0 || grant->playerID == -1 || grant->number == -1)
    {
        TPN current = NULL;
        for (int i=0; i<grantpog->size; i++)
        {
            while (grantpog->table[i] == NULL)
            {
                i++;
                if (i >= grantpog->size)
                    return result;
            }
            current = newTPN(grantpog->table[i]->team, grantpog->table[i]->playerID, grantpog->table[i]->number, grantpog->table[i]->next);
            while (current != NULL)
            {
                if (TPNequals(current, grant) == 1)
                {
                    result = newTPN(current->team, current->playerID, current->number, result);
                }
                current = current->next;
            }
        }
    }
    else
    {
        int index = grantpog->hash(grant);
        TPN current = newTPN(grantpog->table[index]->team, grantpog->table[index]->playerID, grantpog->table[index]->number, grantpog->table[index]->next);
        while (current != NULL)
        {
            if (TPNequals(current, grant) == 1)
            {
                result = newTPN(current->team, current->playerID, current->number, result);
            }
            current = current->next;
        }
    }
    return result;
}

void printTC(TC grant)
{
    if (grant == NULL)
        printf("\nNULL");
    else
    {
        printf("\n");
        TC current = newTC(grant->team, grant->city, grant->next);
        while (current != NULL)
        {
            printf("(%s, %s)", current->team, current->city);
            current = current->next;
            if (current != NULL)
                printf(", ");
        }
    }
    fflush(stdout);
}
void insertTC(TC grant, TC_TABLE grantpog)
{
    if (strcmp(grant->team, "*") == 0 || strcmp(grant->city, "*") == 0)
    {
        printf("\nInvalid input.\n");
        return;
    }
    int index = grantpog->hash(grant);
    if (grantpog->table[index] == NULL)
    {
        grantpog->table[index] = grant;
    }
    else
    {
        TC current = newTC(grantpog->table[index]->team, grantpog->table[index]->city, grantpog->table[index]->next);
        while (current != NULL)
        {
            if (TCequals(current, grant) == 1)
            {
                printf("\nAlready exists.\n");
                return;
            }
            current = current->next; 
        }
        free(current);
        grant->next = grantpog->table[index];
        grantpog->table[index] = grant;
    }
}
void deleteTC(TC grant, TC_TABLE grantpog)
{
    if (strcmp(grant->team, "*") == 1)
    {
        for (int i=0; i<grantpog->size; i++)
        {
            while (grantpog->table[i] == NULL)
            {
                i++;
                if (i >= grantpog->size)
                    return;
            }
            TC current = grantpog->table[i];
            if (TCequals(current, grant) == 1)
            {
                grantpog->table[i] = current->next;
                current = grantpog->table[i];
            }
            if (current != NULL)
            {
                while (current->next != NULL)
                {
                    if (TCequals(current->next, grant) == 1)
                    {
                        TC nextnext = current->next->next;
                        current->next = nextnext;
                    }
                    else
                        current = current->next;
                }
            }
        }
    }
    else
    {
        int index = grantpog->hash(grant);
        TC current = grantpog->table[index];
        if (current == NULL)
        {
            printf("\nDoes not exist.\n");
            return;
        }
        if (TCequals(current, grant) == 1)
        {
            grantpog->table[index] = current->next;
            current = grantpog->table[index];
        }
        if (current != NULL)
        {
            while (current->next != NULL)
            {
                if (TCequals(current->next, grant) == 1)
                {
                    TC nextnext = current->next->next;
                    current->next = nextnext;
                }
                else
                    current = current->next;
            }
        }
    }
}
TC lookupTC(TC grant, TC_TABLE grantpog)
{
    TC result = NULL;
    if (strcmp(grant->team, "*") == 1)
    {
        TC current = NULL;
        for (int i=0; i<grantpog->size; i++)
        {
            while (grantpog->table[i] == NULL)
            {
                i++;
                if (i >= grantpog->size)
                    return result;
            }
            current = newTC(grantpog->table[i]->team, grantpog->table[i]->city, grantpog->table[i]->next);
            while (current != NULL)
            {
                if (TCequals(current, grant) == 1)
                {
                    result = newTC(current->team, current->city, result);
                }
                current = current->next;
            }
        }
    }
    else
    {
        int index = grantpog->hash(grant);
        TC current = newTC(grantpog->table[index]->team, grantpog->table[index]->city, grantpog->table[index]->next);
        while (current != NULL)
        {
            if (TCequals(current, grant) == 1)
            {
                result = newTC(current->team, current->city, result);
            }
            current = current->next;
        }
    }
    return result;
}

void printGHVD(GHVD grant)
{
    if (grant == NULL)
        printf("\nNULL");
    else
    {
        printf("\n");
        GHVD current = newGHVD(grant->gameID, grant->homeTeam, grant->awayTeam, grant->date, grant->next);
        while (current != NULL)
        {
            printf("(%d, %s, %s, %s)", current->gameID, current->homeTeam, current->awayTeam, current->date);
            current = current->next;
            if (current != NULL)
                printf(", ");
        }
    }
    fflush(stdout);
}
void insertGHVD(GHVD grant, GHVD_TABLE grantpog)
{
    if (grant->gameID == -1 || strcmp(grant->homeTeam, "*") == 0 || strcmp(grant->awayTeam, "*") == 0 || strcmp(grant->date, "*") == 0)
    {
        printf("\nInvalid input.\n");
        return;
    }
    int index = grantpog->hash(grant);
    if (grantpog->table[index] == NULL)
    {
        grantpog->table[index] = grant;
    }
    else
    {
        GHVD current = newGHVD(grantpog->table[index]->gameID, grantpog->table[index]->homeTeam, grantpog->table[index]->awayTeam, grantpog->table[index]->date, grantpog->table[index]->next);
        while (current != NULL)
        {
            if (GHVDequals(current, grant) == 1)
            {
                printf("\nAlready exists.\n");
                return;
            }
            current = current->next; 
        }
        free(current);
        grant->next = grantpog->table[index];
        grantpog->table[index] = grant;
    }
}
void deleteGHVD(GHVD grant, GHVD_TABLE grantpog)
{
    if (grant->gameID == -1)
    {
        for (int i=0; i<grantpog->size; i++)
        {
            while (grantpog->table[i] == NULL)
            {
                i++;
                if (i >= grantpog->size)
                    return;
            }
            GHVD current = grantpog->table[i];
            if (GHVDequals(current, grant) == 1)
            {
                grantpog->table[i] = current->next;
                current = grantpog->table[i];
            }
            if (current != NULL)
            {
                while (current->next != NULL)
                {
                    if (GHVDequals(current->next, grant) == 1)
                    {
                        GHVD nextnext = current->next->next;
                        current->next = nextnext;
                    }
                    else
                        current = current->next;
                }
            }
        }
    }
    else
    {
        int index = grantpog->hash(grant);
        GHVD current = grantpog->table[index];
        if (current == NULL)
        {
            printf("\nDoes not exist.\n");
            return;
        }
        if (GHVDequals(current, grant) == 1)
        {
            grantpog->table[index] = current->next;
            current = grantpog->table[index];
        }
        if (current != NULL)
        {
            while (current->next != NULL)
            {
                if (GHVDequals(current->next, grant) == 1)
                {
                    GHVD nextnext = current->next->next;
                    current->next = nextnext;
                }
                else
                    current = current->next;
            }
        }
    }
}
GHVD lookupGHVD(GHVD grant, GHVD_TABLE grantpog)
{
    GHVD result = NULL;
    if (grant->gameID == -1)
    {
        GHVD current = NULL;
        for (int i=0; i<grantpog->size; i++)
        {
            while (grantpog->table[i] == NULL)
            {
                i++;
                if (i >= grantpog->size)
                    return result;
            }
            current = newGHVD(grantpog->table[i]->gameID, grantpog->table[i]->homeTeam, grantpog->table[i]->awayTeam, grantpog->table[i]->date, grantpog->table[i]->next);
            while (current != NULL)
            {
                if (GHVDequals(current, grant) == 1)
                {
                    result = newGHVD(current->gameID, current->homeTeam, current->awayTeam, current->date, result);
                }
                current = current->next;
            }
        }
    }
    else
    {
        int index = grantpog->hash(grant);
        GHVD current = newGHVD(grantpog->table[index]->gameID, grantpog->table[index]->homeTeam, grantpog->table[index]->awayTeam, grantpog->table[index]->date, grantpog->table[index]->next);
        while (current != NULL)
        {
            if (GHVDequals(current, grant) == 1)
            {
                result = newGHVD(current->gameID, current->homeTeam, current->awayTeam, current->date, result);
            }
            current = current->next;
        }
    }
    return result;
}

void printGPG(GPG grant)
{
    if (grant == NULL)
        printf("\nNULL");
    else
    {
        printf("\n");
        GPG current = newGPG(grant->gameID, grant->playerID, grant->goals, grant->next);
        while (current != NULL)
        {
            printf("(%d, %d, %d)", current->gameID, current->playerID, current->goals);
            current = current->next;
            if (current != NULL)
                printf(", ");
        }
    }
    fflush(stdout);
}
void insertGPG(GPG grant, GPG_TABLE grantpog)
{
    if (grant->gameID == -1 || grant->playerID == -1 || grant->goals == -1)
    {
        printf("\nInvalid input.\n");
        return;
    }
    int index = grantpog->hash(grant);
    if (grantpog->table[index] == NULL)
    {
        grantpog->table[index] = grant;
    }
    else
    {
        GPG current = newGPG(grantpog->table[index]->gameID, grantpog->table[index]->playerID, grantpog->table[index]->goals, grantpog->table[index]->next);
        while (current != NULL)
        {
            if (GPGequals(current, grant) == 1)
            {
                printf("\nAlready exists.\n");
                return;
            }
            current = current->next; 
        }
        free(current);
        grant->next = grantpog->table[index];
        grantpog->table[index] = grant;
    }
}
void deleteGPG(GPG grant, GPG_TABLE grantpog)
{
    if (grant->gameID == -1 || grant->playerID == -1 || grant->goals == -1)
    {
        for (int i=0; i<grantpog->size; i++)
        {
            while (grantpog->table[i] == NULL)
            {
                i++;
                if (i >= grantpog->size)
                    return;
            }
            GPG current = grantpog->table[i];
            if (GPGequals(current, grant) == 1)
            {
                grantpog->table[i] = current->next;
                current = grantpog->table[i];
            }
            if (current != NULL)
            {
                while (current->next != NULL)
                {
                    if (GPGequals(current->next, grant) == 1)
                    {
                        GPG nextnext = current->next->next;
                        current->next = nextnext;
                    }
                    else
                        current = current->next;
                }
            }
        }
    }
    else
    {
        int index = grantpog->hash(grant);
        GPG current = grantpog->table[index];
        if (current == NULL)
        {
            printf("\nDoes not exist.\n");
            return;
        }
        if (GPGequals(current, grant) == 1)
        {
            grantpog->table[index] = current->next;
            current = grantpog->table[index];
        }
        if (current != NULL)
        {
            while (current->next != NULL)
            {
                if (GPGequals(current->next, grant) == 1)
                {
                    GPG nextnext = current->next->next;
                    current->next = nextnext;
                }
                else
                    current = current->next;
            }
        }
    }
}
GPG lookupGPG(GPG grant, GPG_TABLE grantpog)
{
    GPG result = NULL;
    if (grant->gameID == -1 || grant->playerID == -1 || grant->goals == -1)
    {
        GPG current = NULL;
        for (int i=0; i<grantpog->size; i++)
        {
            while (grantpog->table[i] == NULL)
            {
                i++;
                if (i >= grantpog->size)
                    return result;
            }
            current = newGPG(grantpog->table[i]->gameID, grantpog->table[i]->playerID, grantpog->table[i]->goals, grantpog->table[i]->next);
            while (current != NULL)
            {
                if (GPGequals(current, grant) == 1)
                {
                    result = newGPG(current->gameID, current->playerID, current->goals, result);
                }
                current = current->next;
            }
        }
    }
    else
    {
        int index = grantpog->hash(grant);
        GPG current = newGPG(grantpog->table[index]->gameID, grantpog->table[index]->playerID, grantpog->table[index]->goals, grantpog->table[index]->next);
        while (current != NULL)
        {
            if (GPGequals(current, grant) == 1)
            {
                result = newGPG(current->gameID, current->playerID, current->goals, result);
            }
            current = current->next;
        }
    }
    return result;
}

void printPNB_TABLE(PNB_TABLE grantpog)
{
    for (int i=0; i<grantpog->size; i++)
        printPNB(grantpog->table[i]);
}
void printTPN_TABLE(TPN_TABLE grantpog)
{
    for (int i=0; i<grantpog->size; i++)
        printTPN(grantpog->table[i]);
}
void printTC_TABLE(TC_TABLE grantpog)
{
    for (int i=0; i<grantpog->size; i++)
        printTC(grantpog->table[i]);
}
void printGHVD_TABLE(GHVD_TABLE grantpog)
{
    for (int i=0; i<grantpog->size; i++)
        printGHVD(grantpog->table[i]);
}
void printGPG_TABLE(GPG_TABLE grantpog)
{
    for (int i=0; i<grantpog->size; i++)
        printGPG(grantpog->table[i]);
}

int PNBhash(PNB key)
{
    return key->playerID % 11;
}
int TPNhash(TPN key)
{
    return (ascii(key->team[0]) + key->playerID + key->number) % 11;
}
int TChash(TC key)
{
    return (ascii(key->team[0])) % 5;
}
int GHVDhash(GHVD key)
{
    return key->gameID % 11;
}
int GPGhash(GPG key)
{
    return (key->gameID + key->playerID + key->goals) % 11;
}

DATABASE createDatabase()
{
    PNB_TABLE pnb = newPNBTable(11, &PNBhash);
    PNB moore1 = newPNB(75196, "A. Moore", "28 Aug 1985", NULL);
    PNB jones1 = newPNB(59797, "G. Jones", "26 Dec 1986", NULL);
    PNB hughes = newPNB(87977, "U. Hughes", "13 Feb 1990", NULL);
    PNB morgan = newPNB(20945, "Q. Morgan", "14 Feb 1998", NULL);
    PNB king = newPNB(70513, "G. King", "13 Apr 1993", NULL);
    PNB sullivan = newPNB(51213, "T. Sullivan", "24 Jun 1995", NULL);
    PNB moore2 = newPNB(61367, "A. Moore", "18 Mar 2000", NULL);
    PNB hernandez = newPNB(55870, "D. Hernandez", "25 Jul 1997", NULL);
    PNB stewart = newPNB(47087, "W. Stewart", "5 Jan 1994", NULL);
    PNB jones2 = newPNB(39468, "G. Jones", "25 Feb 1990", NULL);

    TPN_TABLE tpn = newTPNTable(11, &TPNhash);
    TPN gurkengabels1 = newTPN("Gurkengabels", 87977, 11, NULL);
    TPN gurkengabels2 = newTPN("Gurkengabels", 75196, 7, NULL);
    TPN gurkengabels3 = newTPN("Gurkengabels", 61367, 99, NULL);
    TPN glips1 = newTPN("Glips", 75196, 7, NULL);
    TPN glips2 = newTPN("Glips", 20945, 24, NULL);
    TPN glops1 = newTPN("Glops", 70513, 10, NULL);
    TPN glops2 = newTPN("Glops", 20945, 10, NULL);
    TPN gurgles1 = newTPN("Gurgles", 51213, 1, NULL);
    TPN gurgles2 = newTPN("Gurgles", 51213, 9, NULL);
    TPN gurgles3 = newTPN("Gurgles", 55870, 13, NULL);

    TC_TABLE tc = newTCTable(5, &TChash);
    TC gurkengabels = newTC("Gurkengabels", "Blathers", NULL);
    TC glips = newTC("Glips", "Jefferson", NULL);
    TC glops = newTC("Glops", "Johnson", NULL);
    TC gurgles = newTC("Gurgles", "Chili Stand", NULL);

    GHVD_TABLE ghvd = newGHVDTable(11, &GHVDhash);
    GHVD game1 = newGHVD(1, "Gurkengabels", "Glips", "3 Jan 2023", NULL);
    GHVD game2 = newGHVD(2, "Gurgles", "Glops", "3 Jan 2023", NULL);
    GHVD game3 = newGHVD(3, "Gurkengabels", "Gurgles", "6 Jan 2023", NULL);
    GHVD game4 = newGHVD(4, "Glops", "Glips", "6 Jan 2023", NULL);
    GHVD game5 = newGHVD(5, "Glops", "Gurkengabels", "8 Jan 2023", NULL);
    GHVD game6 = newGHVD(6, "Glips", "Gurgles", "8 Jan 2023", NULL);
    GHVD game7 = newGHVD(7, "Glips", "Gurgles", "9 Jan 2023", NULL);
    GHVD game8 = newGHVD(8, "Gurkengabels", "Glops", "10 Jan 2023", NULL);
    GHVD game9 = newGHVD(9, "Gurgles", "Gurkengabels", "12 Jan 2023", NULL);
    GHVD game10 = newGHVD(10, "Glops", "Glips", "12 Jan 2023", NULL);

    GPG_TABLE gpg = newGPGTable(11, &GPGhash);
    GPG two1 = newGPG(2, 55870, 1, NULL);
    GPG two2 = newGPG(2, 70513, 2, NULL);
    GPG three = newGPG(3, 51213, 1, NULL);
    GPG five = newGPG(5, 20945, 4, NULL);
    GPG six = newGPG(6, 55870, 2, NULL);
    GPG seven = newGPG(7, 75196, 1, NULL);
    GPG eight1 = newGPG(8, 87977, 1, NULL);
    GPG eight2 = newGPG(8, 75196, 2, NULL);
    GPG nine1 = newGPG(9, 55870, 3, NULL);
    GPG nine2 = newGPG(9, 61367, 2, NULL);

    insertPNB(moore1, pnb);
    insertPNB(jones1, pnb);
    insertPNB(hughes, pnb);
    insertPNB(morgan, pnb);
    insertPNB(king, pnb);
    insertPNB(sullivan, pnb);
    insertPNB(moore2, pnb);
    insertPNB(hernandez, pnb);
    insertPNB(stewart, pnb);
    insertPNB(jones2, pnb);

    insertTPN(gurkengabels1, tpn);
    insertTPN(gurkengabels2, tpn);
    insertTPN(gurkengabels3, tpn);
    insertTPN(glips1, tpn);
    insertTPN(glips2, tpn);
    insertTPN(glops1, tpn);
    insertTPN(glops2, tpn);
    insertTPN(gurgles1, tpn);
    insertTPN(gurgles2, tpn);
    insertTPN(gurgles3, tpn);

    insertTC(gurkengabels, tc);
    insertTC(glips, tc);
    insertTC(glops, tc);
    insertTC(gurgles, tc);

    insertGHVD(game1, ghvd);
    insertGHVD(game2, ghvd);
    insertGHVD(game3, ghvd);
    insertGHVD(game4, ghvd);
    insertGHVD(game5, ghvd);
    insertGHVD(game6, ghvd);
    insertGHVD(game7, ghvd);
    insertGHVD(game8, ghvd);
    insertGHVD(game9, ghvd);
    insertGHVD(game10, ghvd);

    insertGPG(two1, gpg);
    insertGPG(two2, gpg);
    insertGPG(three, gpg);
    insertGPG(five, gpg);
    insertGPG(six, gpg);
    insertGPG(seven, gpg);
    insertGPG(eight1, gpg);
    insertGPG(eight2, gpg);
    insertGPG(nine1, gpg);
    insertGPG(nine2, gpg);

    DATABASE this = (DATABASE)malloc(sizeof(struct DATA));
    this->pnb = pnb;
    this->tpn = tpn;
    this->tc = tc;
    this->ghvd = ghvd;
    this->gpg = gpg;

    return this;
}

/* Write a function to answer the query \"What number 
did Name wear when playing for Team?\" similar to FOCS 
Section 8.6 \"Navigation Among Relations.\" The code for 
your function must look like the pseudocode in FOCS 
Fig. 8.8 or 8.9. (I recommend using the pseudocode as 
comments in your code.) */
void numberNameTeam(char* name, char* team, DATABASE data)
{
    int dataFound = 0;
    name[strlen(name)-1] = '\0';
    team[strlen(team)-1] = '\0';    
    PNB pnbTuples = lookupPNB(newPNB(-1, name, "*", NULL), data->pnb); // (1) for each tuple t in PlayerID-Name-BirthDate do: (2) if t has "name" in its Name component then begin
    while (pnbTuples != NULL)
    {
        int i = pnbTuples->playerID; // (3) let i be the PlayerID component of tuple t;
        TPN tpnTuples = lookupTPN(newTPN(team, i, -1, NULL), data->tpn); // (4) for each tuple s in Team-PlayerID-Number do: (5) if s has Team component "team" and PlayerID component i then
        while (tpnTuples != NULL)
        {
            dataFound = 1;
            printf("\n%s wore #%d while playing for the %s.\n", name, tpnTuples->number, team); // (6) print the Number component of t
            tpnTuples = tpnTuples->next;
        }
        pnbTuples = pnbTuples->next;
    }
    if (dataFound == 0)
        printf("\nNo data is available for the query.\n");
}

/* Write a function to answer the query \"How many 
goals did Name score on Date?\" The code for your 
function must follow the model shown in FOCS Fig. 8.10 
(described starting on p. 426), also seen in class. */
void goalsNameDate(char* name, char* date, DATABASE data)
{
    int dataFound = 0;
    name[strlen(name)-1] = '\0';
    date[strlen(date)-1] = '\0';    
    PNB pnbTuples = lookupPNB(newPNB(-1, name, "*", NULL), data->pnb);
    while (pnbTuples != NULL)
    {
        int i = pnbTuples->playerID;
        GHVD ghvdTuples = lookupGHVD(newGHVD(-1, "*", "*", date, NULL), data->ghvd);
        while (ghvdTuples != NULL)
        {
            int j = ghvdTuples->gameID;
            GPG gpgTuples = lookupGPG(newGPG(j, i, -1, NULL), data->gpg);
            while (gpgTuples != NULL)
            {
                dataFound = 1;
                printf("\n%s scored %d goals on %s.\n", name, gpgTuples->goals, date);
                gpgTuples = gpgTuples->next;
            }
            ghvdTuples = ghvdTuples->next;
        }
        pnbTuples = pnbTuples->next;
    }
    if (dataFound == 0)
        printf("\nNo data is available for the query.\n");
}

typedef struct Ts *T;
struct Ts {
    char* team;
    T next;
};
T newT(char* t, T next)
{
    T this = (T)malloc(sizeof(struct Ts));
    this->team = t;
    this->next = next;
    return this;
}
int Tequals(T a, T b)
{
    if (strcmp(a->team, b->team) == 0 || strcmp(a->team, "*") == 0 || strcmp(b->team, "*") == 0)
        return 1;
    else return 0;
}
typedef struct TTABLE {
    int size;
    T* table;
    int (*hash)(T key);
} *T_TABLE;
T_TABLE newTTable(int s, int (*h)(T))
{
    T_TABLE this = (T_TABLE)malloc(sizeof(struct TTABLE));
    this->size = s;
    this->table = malloc(s * sizeof(T));
    for (int i=0; i<s; i++)
        this->table[i] = NULL;
    this->hash = h;
    return this;
}

typedef struct GHVDPGs *GHVDPG;
struct GHVDPGs {
    int gameID;
    char* homeTeam;
    char* awayTeam;
    char* date;
    int playerID;
    int goals;
    GHVDPG next;
};
GHVDPG newGHVDPG(int g1, char* h, char* a, char* d, int p, int g2, GHVDPG next)
{
    GHVDPG this = (GHVDPG)malloc(sizeof(struct GHVDPGs));
    this->gameID = g1;
    this->homeTeam = h;
    this->awayTeam = a;
    this->date = d;
    this->playerID = p;
    this->goals = g2;
    this->next = next;
    return this;
}
int GHVDPGequals(GHVDPG a, GHVDPG b)
{
    if ((a->gameID == b->gameID || a->gameID == -1 || b->gameID == -1) && (strcmp(a->homeTeam, b->homeTeam) == 0 || strcmp(a->homeTeam, "*") == 0 || strcmp(b->homeTeam, "*") == 0) && (strcmp(a->awayTeam, b->awayTeam) == 0 || strcmp(a->awayTeam, "*") == 0 || strcmp(b->awayTeam, "*") == 0) && (strcmp(a->date, b->date) == 0 || strcmp(a->date, "*") == 0 || strcmp(b->date, "*") == 0) && (a->playerID == b->playerID || a->playerID == -1 || b->playerID == -1) && (a->goals == b->goals || a->goals == -1 || b->goals == -1))
        return 1;
    else return 0;
}
typedef struct GHVDPGTABLE {
    int size;
    GHVDPG* table;
    int (*hash)(GHVDPG key);
} *GHVDPG_TABLE;
GHVDPG_TABLE newGHVDPGTable(int s, int (*h)(GHVDPG))   
{
    GHVDPG_TABLE this = (GHVDPG_TABLE)malloc(sizeof(struct GHVDPGTABLE));
    this->size = s;
    this->table = malloc(s * sizeof(GHVDPG));
    for (int i=0; i<s; i++)
        this->table[i] = NULL;
    this->hash = h;
    return this;
}

typedef struct PGs *PG;
struct PGs {
    int playerID;
    int goals;
    PG next;
};
PG newPG(int p, int g, PG next)
{
    PG this = (PG)malloc(sizeof(struct PGs));
    this->playerID = p;
    this->goals = g;
    this->next = next;
    return this;
}
int PGequals(PG a, PG b)
{
    if ((a->playerID == b->playerID || a->playerID == -1 || b->playerID == -1) && (a->goals == b->goals || a->goals == -1 || b->goals == -1))
        return 1;
    else return 0;
}
typedef struct PGTABLE {
    int size;
    PG* table;
    int (*hash)(PG key);
} *PG_TABLE;
PG_TABLE newPGTable(int s, int (*h)(PG))
{
    PG_TABLE this = (PG_TABLE)malloc(sizeof(struct PGTABLE));
    this->size = s;
    this->table = malloc(s * sizeof(PG));
    for (int i=0; i<s; i++)
        this->table[i] = NULL;
    this->hash = h;
    return this;
}

void printT(T grant)
{
    if (grant == NULL)
        printf("\nNULL");
    else
    {
        printf("\n");
        T current = newT(grant->team, grant->next);
        while (current != NULL)
        {
            printf("(%s)", current->team);
            current = current->next;
            if (current != NULL)
                printf(", ");
        }
    }
    fflush(stdout);
}
void insertT(T grant, T_TABLE grantpog)
{
    if (strcmp(grant->team, "*") == 0)
    {
        printf("\nInvalid input.\n");
        return;
    }
    int index = grantpog->hash(grant);
    if (grantpog->table[index] == NULL)
    {
        grantpog->table[index] = grant;
    }
    else
    {
        T current = newT(grantpog->table[index]->team, grantpog->table[index]->next);
        while (current != NULL)
        {
            if (Tequals(current, grant) == 1)
            {
                printf("\nAlready exists.\n");
                return;
            }
            current = current->next; 
        }
        free(current);
        grant->next = grantpog->table[index];
        grantpog->table[index] = grant;
    }
}
void deleteT(T grant, T_TABLE grantpog)
{
    if (strcmp(grant->team, "*") == 0)
    {
        for (int i=0; i<grantpog->size; i++)
        {
            while (grantpog->table[i] == NULL)
            {
                i++;
                if (i >= grantpog->size)
                    return;
            }
            T current = grantpog->table[i];
            if (Tequals(current, grant) == 1)
            {
                grantpog->table[i] = current->next;
                current = grantpog->table[i];
            }
            if (current != NULL)
            {
                while (current->next != NULL)
                {
                    if (Tequals(current->next, grant) == 1)
                    {
                        T nextnext = current->next->next;
                        current->next = nextnext;
                    }
                    else
                        current = current->next;
                }
            }
        }
    }
    else
    {
        int index = grantpog->hash(grant);
        T current = grantpog->table[index];
        if (current == NULL)
        {
            printf("\nDoes not exist.\n");
            return;
        }
        if (Tequals(current, grant) == 1)
        {
            grantpog->table[index] = current->next;
            current = grantpog->table[index];
        }
        if (current != NULL)
        {
            while (current->next != NULL)
            {
                if (Tequals(current->next, grant) == 1)
                {
                    T nextnext = current->next->next;
                    current->next = nextnext;
                }
                else
                    current = current->next;
            }
        }
    }
}
T lookupT(T grant, T_TABLE grantpog)
{
    T result = NULL;
    if (strcmp(grant->team, "*") == 0)
    {
        T current = NULL;
        for (int i=0; i<grantpog->size; i++)
        {
            while (grantpog->table[i] == NULL)
            {
                i++;
                if (i >= grantpog->size)
                    return result;
            }
            current = newT(grantpog->table[i]->team, grantpog->table[i]->next);
            while (current != NULL)
            {
                if (Tequals(current, grant) == 1)
                {
                    result = newT(current->team, result);
                }
                current = current->next;
            }
        }
    }
    else
    {
        int index = grantpog->hash(grant);
        T current = newT(grantpog->table[index]->team, grantpog->table[index]->next);
        while (current != NULL)
        {
            if (Tequals(current, grant) == 1)
            {
                result = newT(current->team, result);
            }
            current = current->next;
        }
    }
    return result;
}

void printGHVDPG(GHVDPG grant)
{
    if (grant == NULL)
        printf("\nNULL");
    else
    {
        printf("\n");
        GHVDPG current = newGHVDPG(grant->gameID, grant->homeTeam, grant->awayTeam, grant->date, grant->playerID, grant->goals, grant->next);
        while (current != NULL)
        {
            printf("(%d, %s, %s, %s, %d, %d)", current->gameID, current->homeTeam, current->awayTeam, current->date, current->playerID, current->goals);
            current = current->next;
            if (current != NULL)
                printf(", ");
        }
    }
    fflush(stdout);
}
void insertGHVDPG(GHVDPG grant, GHVDPG_TABLE grantpog)
{
    if (grant->gameID == -1 || strcmp(grant->homeTeam, "*") == 0 || strcmp(grant->awayTeam, "*") == 0 || strcmp(grant->date, "*") == 0 || grant->playerID == -1 || grant->goals == -1)
    {
        printf("\nInvalid input.\n");
        return;
    }
    int index = grantpog->hash(grant);
    if (grantpog->table[index] == NULL)
    {
        grantpog->table[index] = grant;
    }
    else
    {
        GHVDPG current = newGHVDPG(grantpog->table[index]->gameID, grantpog->table[index]->homeTeam, grantpog->table[index]->awayTeam, grantpog->table[index]->date, grantpog->table[index]->playerID, grantpog->table[index]->goals, grantpog->table[index]->next);
        while (current != NULL)
        {
            if (GHVDPGequals(current, grant) == 1)
            {
                printf("\nAlready exists.\n");
                return;
            }
            current = current->next; 
        }
        free(current);
        grant->next = grantpog->table[index];
        grantpog->table[index] = grant;
    }
}
void deleteGHVDPG(GHVDPG grant, GHVDPG_TABLE grantpog)
{
    if (grant->gameID == -1)
    {
        for (int i=0; i<grantpog->size; i++)
        {
            while (grantpog->table[i] == NULL)
            {
                i++;
                if (i >= grantpog->size)
                    return;
            }
            GHVDPG current = grantpog->table[i];
            if (GHVDPGequals(current, grant) == 1)
            {
                grantpog->table[i] = current->next;
                current = grantpog->table[i];
            }
            if (current != NULL)
            {
                while (current->next != NULL)
                {
                    if (GHVDPGequals(current->next, grant) == 1)
                    {
                        GHVDPG nextnext = current->next->next;
                        current->next = nextnext;
                    }
                    else
                        current = current->next;
                }
            }
        }
    }
    else
    {
        int index = grantpog->hash(grant);
        GHVDPG current = grantpog->table[index];
        if (current == NULL)
        {
            printf("\nDoes not exist.\n");
            return;
        }
        if (GHVDPGequals(current, grant) == 1)
        {
            grantpog->table[index] = current->next;
            current = grantpog->table[index];
        }
        if (current != NULL)
        {
            while (current->next != NULL)
            {
                if (GHVDPGequals(current->next, grant) == 1)
                {
                    GHVDPG nextnext = current->next->next;
                    current->next = nextnext;
                }
                else
                    current = current->next;
            }
        }
    }
}
GHVDPG lookupGHVDPG(GHVDPG grant, GHVDPG_TABLE grantpog)
{
    GHVDPG result = NULL;
    if (grant->gameID == -1)
    {
        GHVDPG current = NULL;
        for (int i=0; i<grantpog->size; i++)
        {
            while (grantpog->table[i] == NULL)
            {
                i++;
                if (i >= grantpog->size)
                    return result;
            }
            current = newGHVDPG(grantpog->table[i]->gameID, grantpog->table[i]->homeTeam, grantpog->table[i]->awayTeam, grantpog->table[i]->date, grantpog->table[i]->playerID, grantpog->table[i]->goals, grantpog->table[i]->next);
            while (current != NULL)
            {
                if (GHVDPGequals(current, grant) == 1)
                {
                    result = newGHVDPG(current->gameID, current->homeTeam, current->awayTeam, current->date, current->playerID, current->goals, result);
                }
                current = current->next;
            }
        }
    }
    else
    {
        int index = grantpog->hash(grant);
        GHVDPG current = newGHVDPG(grantpog->table[index]->gameID, grantpog->table[index]->homeTeam, grantpog->table[index]->awayTeam, grantpog->table[index]->date, grantpog->table[index]->playerID, grantpog->table[index]->goals, grantpog->table[index]->next);
        while (current != NULL)
        {
            if (GHVDPGequals(current, grant) == 1)
            {
                result = newGHVDPG(current->gameID, current->homeTeam, current->awayTeam, current->date, current->playerID, current->goals, result);
            }
            current = current->next;
        }
    }
    return result;
}

void printPG(PG grant)
{
    if (grant == NULL)
        printf("\nNULL");
    else
    {
        printf("\n");
        PG current = newPG(grant->playerID, grant->goals, grant->next);
        while (current != NULL)
        {
            printf("(%d, %d)", current->playerID, current->goals);
            current = current->next;
            if (current != NULL)
                printf(", ");
        }
    }
    fflush(stdout);
}
void insertPG(PG grant, PG_TABLE grantpog)
{
    if (grant->playerID == -1)
    {
        printf("\nInvalid input.\n");
        return;
    }
    int index = grantpog->hash(grant);
    if (grantpog->table[index] == NULL)
    {
        grantpog->table[index] = grant;
    }
    else
    {
        PG current = newPG(grantpog->table[index]->playerID, grantpog->table[index]->goals, grantpog->table[index]->next);
        while (current != NULL)
        {
            if (PGequals(current, grant) == 1)
            {
                printf("\nAlready exists.\n");
                return;
            }
            current = current->next; 
        }
        free(current);
        grant->next = grantpog->table[index];
        grantpog->table[index] = grant;
    }
}
void deletePG(PG grant, PG_TABLE grantpog)
{
    if (grant->playerID == -1)
    {
        for (int i=0; i<grantpog->size; i++)
        {
            while (grantpog->table[i] == NULL)
            {
                i++;
                if (i >= grantpog->size)
                    return;
            }
            PG current = grantpog->table[i];
            if (PGequals(current, grant) == 1)
            {
                grantpog->table[i] = current->next;
                current = grantpog->table[i];
            }
            if (current != NULL)
            {
                while (current->next != NULL)
                {
                    if (PGequals(current->next, grant) == 1)
                    {
                        PG nextnext = current->next->next;
                        current->next = nextnext;
                    }
                    else
                        current = current->next;
                }
            }
        }
    }
    else
    {
        int index = grantpog->hash(grant);
        PG current = grantpog->table[index];
        if (current == NULL)
        {
            printf("\nDoes not exist.\n");
            return;
        }
        if (PGequals(current, grant) == 1)
        {
            grantpog->table[index] = current->next;
            current = grantpog->table[index];
        }
        if (current != NULL)
        {
            while (current->next != NULL)
            {
                if (PGequals(current->next, grant) == 1)
                {
                    PG nextnext = current->next->next;
                    current->next = nextnext;
                }
                else
                    current = current->next;
            }
        }
    }
}
PG lookupPG(PG grant, PG_TABLE grantpog)
{
    PG result = NULL;
    if (grant->playerID == -1)
    {
        PG current = NULL;
        for (int i=0; i<grantpog->size; i++)
        {
            while (grantpog->table[i] == NULL)
            {
                i++;
                if (i >= grantpog->size)
                    return result;
            }
            current = newPG(grantpog->table[i]->playerID, grantpog->table[i]->goals, grantpog->table[i]->next);
            while (current != NULL)
            {
                if (PGequals(current, grant) == 1)
                {
                    result = newPG(current->playerID, current->goals, result);
                }
                current = current->next;
            }
        }
    }
    else
    {
        int index = grantpog->hash(grant);
        PG current = newPG(grantpog->table[index]->playerID, grantpog->table[index]->goals, grantpog->table[index]->next);
        while (current != NULL)
        {
            if (PGequals(current, grant) == 1)
            {
                result = newPG(current->playerID, current->goals, result);
            }
            current = current->next;
        }
    }
    return result;
}

void printT_TABLE(T_TABLE grantpog)
{
    for (int i=0; i<grantpog->size; i++)
        printT(grantpog->table[i]);
}
void printGHVDPG_TABLE(GHVDPG_TABLE grantpog)
{
    for (int i=0; i<grantpog->size; i++)
        printGHVDPG(grantpog->table[i]);
}
void printPG_TABLE(PG_TABLE grantpog)
{
    for (int i=0; i<grantpog->size; i++)
        printPG(grantpog->table[i]);
}

int Thash(T key)
{
    return (ascii(key->team[0])) % 11;
}
int GHVDPGhash(GHVDPG key)
{
    return key->gameID % 11;
}
int PGhash(PG key)
{
    return key->playerID % 11;
}

// 1. Selection: σPlayerId=51213(TPN)
TPN_TABLE TPNselectPlayerID(int p, TPN_TABLE table)
{
    TPN selection = lookupTPN(newTPN("*", p, -1, NULL), table);
    TPN_TABLE result = newTPNTable(11, &TPNhash);
    while (selection != NULL)
    {
        insertTPN(newTPN(selection->team, p, selection->number, NULL), result);
        selection = selection->next;
    }
    free(selection);
    return result;
}

// 2. Projection: πTeam(σPlayerId=51213(TPN))
T_TABLE TPNprojectTeam(TPN_TABLE table)
{
    TPN list = lookupTPN(newTPN("*", -1, -1, NULL), table);
    T_TABLE result = newTTable(11, &Thash);
    while (list != NULL)
    {
        insertT(newT(list->team, NULL), result);
        list = list->next;
    }
    free(list);
    return result;
}

// 3. Join: GHVD ▷◁ GPG
GHVDPG_TABLE GHVDjoinGPG(GHVD_TABLE ghvd, GPG_TABLE gpg)
{
    GHVD ghvdTuples = lookupGHVD(newGHVD(-1, "*", "*", "*", NULL), ghvd);
    GHVDPG_TABLE result = newGHVDPGTable(11, &GHVDPGhash);
    while (ghvdTuples != NULL)
    {
        GPG gpgTuples = lookupGPG(newGPG(-1, -1, -1, NULL), gpg);
        while (gpgTuples != NULL)
        {
            if (ghvdTuples->gameID == gpgTuples->gameID)
                insertGHVDPG(newGHVDPG(ghvdTuples->gameID, ghvdTuples->homeTeam, ghvdTuples->awayTeam, ghvdTuples->date, gpgTuples->playerID, gpgTuples->goals, NULL), result);
            gpgTuples = gpgTuples->next;
        }
        ghvdTuples = ghvdTuples->next;
        free(gpgTuples);
    }
    free(ghvdTuples);
    return result;
}

// 4. All of the above: πPlayerId,Goals(σDate="8Jan2023"(GHVD ▷◁ GPG))
GHVDPG_TABLE GHVDPGselectDate(char* d, GHVDPG_TABLE table)
{
    GHVDPG selection = lookupGHVDPG(newGHVDPG(-1, "*", "*", d, -1, -1, NULL), table);
    GHVDPG_TABLE result = newGHVDPGTable(11, &GHVDPGhash);
    while (selection != NULL)
    {
        insertGHVDPG(newGHVDPG(selection->gameID, selection->homeTeam, selection->awayTeam, d, selection->playerID, selection->goals, NULL), result);
        selection = selection->next;
    }
    free(selection);
    return result;
}
PG_TABLE GHVDPGprojectPlayerIDGoals(GHVDPG_TABLE table)
{
    GHVDPG list = lookupGHVDPG(newGHVDPG(-1, "*", "*", "*", -1, -1, NULL), table);
    PG_TABLE result = newPGTable(11, &PGhash);
    while (list != NULL)
    {
        insertPG(newPG(list->playerID, list->goals, NULL), result);
        list = list->next;
    }
    free(list);
    return result;
}



int main(int argc, char *argv[])
{
    DATABASE part1 = createDatabase();

    printf("\n\n(a) lookup(<\"Gurkengabels\", 61367, 99>, Team-PlayerId-Number)\n   Rendered: lookupTPN(newTPN(\"Gurkengabels\", 61367, 99, NULL), part1->tpn)\n");
    printTPN(lookupTPN(newTPN("Gurkengabels", 61367, 99, NULL), part1->tpn));
    printf("\n\nb) lookup(<\"Gurgles\", 51213, *>, Team-PlayerId-Number)\n   Rendered: lookupTPN(newTPN(\"Gurgles\", 51213, -1, NULL), part1->tpn)\n");
    printTPN(lookupTPN(newTPN("Gurgles", 51213, -1, NULL), part1->tpn));
    printf("\n\nc) lookup(<\"Gurkengabels\", \"Jefferson\">, Team-City)\n   Rendered: lookupTC(newTC(\"Gurkengabels\", \"Jefferson\", NULL), part1->tc)\n");
    printTC(lookupTC(newTC("Gurkengabels", "Jefferson", NULL), part1->tc));
    
    printf("\n\nd) delete(<4, \"Glops\", \"Glips\", \"6 Jan 2023\">, GameId-HomeTeam-AwayTeam-Date)\n   Rendered: deleteGHVD(newGHVD(4, \"Glops\", \"Glips\", \"6 Jan 2023\", NULL), part1->ghvd)\n");
    deleteGHVD(newGHVD(4, "Glops", "Glips", "6 Jan 2023", NULL), part1->ghvd);
    printGHVD_TABLE(part1->ghvd);
    printf("\n\ne) delete(<*, \"Glops\", \"Gurgles\", *>, GameId-HomeTeam-AwayTeam-Date)\n   Rendered: deleteGHVD(newGHVD(-1, \"Glops\", \"Gurgles\", \"*\", NULL), part1->ghvd)\n");
    deleteGHVD(newGHVD(-1, "Glops", "Gurgles", "*", NULL), part1->ghvd);
    printGHVD_TABLE(part1->ghvd);
    printf("\n\nf) delete(<*, \"Gurkengabels\", *, *>, GameId-HomeTeam-AwayTeam-Date)\n   Rendered: deleteGHVD(newGHVD(-1, \"Gurkengabels\", \"*\", \"*\", NULL), part1->ghvd)\n");
    deleteGHVD(newGHVD(-1, "Gurkengabels", "*", "*", NULL), part1->ghvd);
    printGHVD_TABLE(part1->ghvd);

    printf("\n\ng) insert(<\"Ice Pilots\", \"Pensacola\">, Team-City)\n   Rendered: insertTC(newTC(\"Ice Pilots\", \"Pensacola\", NULL), part1->tc)\n");
    insertTC(newTC("Ice Pilots", "Pensacola", NULL), part1->tc);
    printTC_TABLE(part1->tc);
    printf("\n\nh) insert(<\"Gurgles\", \"Chili Stand\">, Team-City)\n   Rendered: insertTC(newTC(\"Gurgles\", \"Chili Stand\", NULL), part1->tc)\n");
    insertTC(newTC("Gurgles", "Chili Stand", NULL), part1->tc);
    printTC_TABLE(part1->tc);



    DATABASE part2 = createDatabase();

    printf("\n\n\"What number did Name wear when playing for Team?\"");
    while (1 == 1)
    {
        char name[256];
        char team[256];
        printf("\nInput a name (or press ENTER to quit).\n");
        fgets(name, 255, stdin);
        if (strcmp(name, "\n") == 0)
            break;
        printf("\nInput a team (or press ENTER to quit).\n");
        fgets(team, 255, stdin);
        if (strcmp(team, "\n") == 0)
            break;
        numberNameTeam(name, team, part2);
    }

    printf("\n\"How many goals did Name score on Date?\"\n");
    while (1 == 1)
    {
        char name[256];
        char date[256];
        printf("\nInput a name (or press ENTER to quit).\n");
        fgets(name, 255, stdin);
        if (strcmp(name, "\n") == 0)
            break;
        printf("\nInput a date (or press ENTER to quit).\n");
        fgets(date, 255, stdin);
        if (strcmp(date, "\n") == 0)
            break;
        goalsNameDate(name, date, part2);
    }



    DATABASE part3 = createDatabase();

    TPN_TABLE select = TPNselectPlayerID(51213, part3->tpn);
    printf("\n\nBelow: the result of selecting only the tuples from the TPN relation whose player ID is 51213.\n");
    printTPN_TABLE(select);

    T_TABLE project = TPNprojectTeam(select);
    printf("\n\nBelow: the result of projecting only the Team attribute from the selection above.\n");
    printT_TABLE(project);

    GHVDPG_TABLE join = GHVDjoinGPG(part3->ghvd, part3->gpg);
    printf("\n\nBelow: the result of performing a natural join on the GHVD and GPG relations.\n");
    printGHVDPG_TABLE(join);

    PG_TABLE all = GHVDPGprojectPlayerIDGoals(GHVDPGselectDate("8 Jan 2023", join));
    printf("\n\nBelow: the result of selecting only the tuples from the joined table above whose date is 8 Jan 2023, then projecting only the PlayerID and Goals attributes.\n");
    printPG_TABLE(all);
}

// Might want to remove tuple-prints from while loops in delete functions.
