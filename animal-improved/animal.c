//animal.c
//Jacob Burroughs

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>

#include "animals_strings.h"
#include "animal.h"
#include "prompt.h"

#define DEBUG 0
#include "debug.h"

animal_question create_animal_question(){
    animal_question ret = (animal_question)malloc(sizeof(animal_question_struct));
    if(ret == NULL){
        error_print("Failed to allocate memory.");
        exit(-1);
    }
    ret->qtrue = NULL;
    ret->qfalse = NULL;
    ret->true_animal_name = NULL;
    ret->false_animal_name = NULL;
    ret->question = NULL;
    return ret;
}

void destroy_animal_question(animal_question a){
    if(a->qtrue != NULL){
        destroy_animal_question(a->qtrue);
    }
    if(a->qfalse != NULL){
        destroy_animal_question(a->qfalse);
    }
    free(a->true_animal_name);
    free(a->false_animal_name);
    free(a->question);
    free(a);
}

char* serialize_string(char* str){
    if(str == NULL){
        return strdup("0");
    }
    char* ret = malloc(snprintf(NULL, 0, "%d %s", (int)strlen(str), str)+1);
    if(ret == NULL){
        error_print("Failed to allocate memory.");
        exit(-1);
    }
    sprintf(ret, "%d %s", (int)strlen(str), str);
    return ret;
}

char* serialize_animal(animal_question a){
    char* ta = serialize_string(a->true_animal_name);
    char* fa = serialize_string(a->false_animal_name);
    char* q = serialize_string(a->question);
    char* tq = a->qtrue!=NULL?serialize_animal(a->qtrue):strdup("N");
    char* fq = a->qfalse!=NULL?serialize_animal(a->qfalse):strdup("N");
    char* ret = malloc(snprintf(NULL, 0, "A\n%s\n%s\n%s\n%s\n%s", ta, fa, q, tq, fq)+1);
    if(ret == NULL){
        error_print("Failed to allocate memory.");
        exit(-1);
    }
    sprintf(ret, "A\n%s\n%s\n%s\n%s\n%s", ta, fa, q, tq, fq);
    free(ta);
    free(fa);
    free(q);
    free(tq);
    free(fq);
    return ret;
}

animal_question deserialize_animal(FILE* f){
    char ok = fgetc(f);
    debug_print("ok: %c",ok);
    fgetc(f);
    if(ok == 'A'){
        animal_question new_animal = create_animal_question();
        int size;
        fscanf(f," %d ",&size);
        debug_print("size: %i",size);
        if(size!=0){
            new_animal->true_animal_name = malloc(size+1);
            if(new_animal->true_animal_name == NULL){
                error_print("Failed to allocate memory.");
                exit(-1);
            }
            fgets(new_animal->true_animal_name,size+1,f);
            fgetc(f);
            debug_print("msg: %s",new_animal->true_animal_name);
        }
        fscanf(f," %d ",&size);
        debug_print("size: %i",size);
        if(size!=0){
            new_animal->false_animal_name = malloc(size+1);
            if(new_animal->false_animal_name == NULL){
                error_print("Failed to allocate memory.");
                exit(-1);
            }
            fgets(new_animal->false_animal_name,size+1,f);
            fgetc(f);
            debug_print("msg: %s",new_animal->false_animal_name);
        }
        fscanf(f," %d ",&size);
        debug_print("size: %i",size);
        if(size!=0){
            new_animal->question = malloc(size+1);
            if(new_animal->question == NULL){
                error_print("Failed to allocate memory.");
                exit(-1);
            }
            fgets(new_animal->question,size+1,f);
            fgetc(f);
            debug_print("msg: %s",new_animal->question);
        }
        new_animal->qtrue = deserialize_animal(f);
        new_animal->qfalse = deserialize_animal(f);
        return new_animal;
    } else {
        return NULL;
    }
}

bool ask_animal_question(char* s){
    return ask_yn(s);
}

bool guess_animal(char* s){
    size_t needed = snprintf(NULL, 0, ANIMAL_ASK_STR, s)+1;
    char  *question = malloc(needed);
    if(question == NULL){
        error_print("Failed to allocate memory.");
        exit(-1);
    }
    snprintf(question, needed, ANIMAL_ASK_STR, s);
    bool ret = ask_yn(question);
    free(question);
    return ret;
}

void add_animal(animal_question currq, bool side){
    animal_question new_animal = create_animal_question();
    new_animal->qfalse = NULL;
    new_animal->qtrue = NULL;
    new_animal->true_animal_name = prompt_input_inline(PROMPT_NAME_STR);
    new_animal->false_animal_name = side?currq->true_animal_name:currq->false_animal_name;
    new_animal->question = prompt_input_newline(PROMPT_QUESTION_STR,new_animal->true_animal_name,new_animal->false_animal_name);
    if(side){
        currq->qtrue = new_animal;
        currq->true_animal_name = NULL;
    } else {
        currq->qfalse = new_animal;
        currq->false_animal_name = NULL;
    }
}
