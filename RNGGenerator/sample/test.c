#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <libxml/parser.h>
#include <libxml/xpath.h>

typedef struct teams_struct teams_struct;

typedef struct team_struct team_struct;

typedef struct members_struct members_struct;

typedef struct person_struct person_struct;
struct teams_struct {
    team_struct** team;
};

typedef struct team_struct {
    char* number;
    char* name;
    char* location;
    person_struct** person;
} team_struct;

struct members_struct {
    person_struct** person;
};

typedef struct person_struct {
    char* name;
    char* email;
    char* role;
    bool captain;
} person_struct;
void main(int argc, char** argv){
char *xmlfile = argv[1];
xmlDocPtr doc = xmlParseFile(xmlfile);
xmlNodePtr ptr = xmlDocGetRootElement(doc);
xmlNodePtr parent = ptr;
xmlXPathContextPtr xpathCtx = xmlXPathNewContext(doc);
int size = 0;
teams_struct* root = malloc(sizeof(teams_struct));
size = xmlChildElementCount(ptr);
parent = ptr;
ptr = ptr->children;
team_struct** teamlocalstructs = calloc(size*3,sizeof(team_struct*));
int iteam = 0;
while(ptr != NULL){
if(ptr->type == XML_ELEMENT_NODE){
xmlXPathSetContextNode(ptr,xpathCtx);
team_struct* teamlocalstruct = malloc(sizeof(team_struct));
teamlocalstruct->number= xmlXPathEval("./number",xpathCtx)->nodesetval->nodeTab[0]->children->content;

teamlocalstruct->name= xmlXPathEval("./name",xpathCtx)->nodesetval->nodeTab[0]->children->content;

teamlocalstruct->location= xmlXPathEval("./location",xpathCtx)->nodesetval->nodeTab[0]->children->content;

size = xmlChildElementCount(ptr);
parent = ptr;
ptr = ptr->children;
person_struct** personlocalstructs = calloc(size*3,sizeof(person_struct*));
int iperson = 0;
while(ptr != NULL){
if(ptr->type == XML_ELEMENT_NODE){
xmlXPathSetContextNode(ptr,xpathCtx);
person_struct* personlocalstruct = malloc(sizeof(person_struct));
personlocalstruct->name= xmlXPathEval("./name",xpathCtx)->nodesetval->nodeTab[0]->children->content;

personlocalstruct->email= xmlXPathEval("./email",xpathCtx)->nodesetval->nodeTab[0]->children->content;

personlocalstruct->role= xmlXPathEval("./role",xpathCtx)->nodesetval->nodeTab[0]->children->content;

personlocalstruct->captain= xmlXPathEval("./captain",xpathCtx)->nodesetval->nodeTab[0]->children->content;

personlocalstructs[iperson] = personlocalstruct;
iperson++;
}
ptr = ptr->next;
}
ptr = parent;
teamlocalstruct->person=personlocalstructs;

teamlocalstructs[iteam] = teamlocalstruct;
iteam++;
}
ptr = ptr->next;
}
ptr = parent;
root->team=teamlocalstructs;
}

