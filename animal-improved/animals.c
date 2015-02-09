//animals.c
//Jacob Burroughs

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>

#include "animals_strings.h"
#include "animal.h"
#include "prompt.h"

#include "debug.h"

bool continue_run(){
    printf("\n"); 
    return ask_yn(PLAYAGAIN_STR);
}

void win(){
    printf("%s\n",WIN_STR);
}

void lose(animal_question currq, bool correct){
    printf("%s\n",LOSE_STR);
    add_animal(currq, correct);
}

int main(int argc, char** argv){
    printf("%s\n\n",GREETING_STR);
    
    //read in input file name, if applicable
    animal_question root;
    char* in_fname = NULL;
    
    if(argc == 2) {
         in_fname = strdup(argv[1]);   
    } else if(argc == 1) {
        bool load = ask_yn(USE_FILE_STR);
        if(load) {
            in_fname = prompt_input_inline(READ_FILE_STR);
        }
        printf("\n");
    } else {
        error_print("Invalid number of parameters");
        exit(-1);
    }
    
    //open and read in input file if applicable; otherwise, create default question set
    FILE* in = fopen(in_fname,"r");
    if(in != NULL){
            root = deserialize_animal(in);
            fclose(in);
            if(root == NULL){
                error_print("Invalid input file");
                exit(-1);
            }
    } else {
        root = create_animal_question();
        root->question = strdup("Does your animal have four legs?");
        root->true_animal_name = strdup("a horse");
        root->false_animal_name = strdup("a duck");
    }
    
    //run the game
    bool done = false;
    animal_question currq = root;
    while(!done){
        bool ans = ask_animal_question(currq->question);
        if(ans ? currq->qtrue == NULL : currq->qfalse == NULL) {
            //if this is the end of the tree, make a guess
            bool correct = guess_animal(currq->true_animal_name);
            if(correct){
                win();
            } else {
                lose(currq,ans);
            }
            done = continue_run();
            currq = root;
        } else {
            //move down the tree
            currq = ans ? currq->qtrue : currq->qfalse;
        }
    }
    
    //write out file if applicable
    printf("\n");
    bool save = ask_yn(USE_FILE_OUT_STR);
    char* out_fname;
    if(save) {
        if(in_fname == NULL){
            in_fname = "animals.sav";
        }
        out_fname = prompt_input_inline_default(in_fname,WRITE_FILE_STR,in_fname);
        char* ser = serialize_animal(root);
        FILE* out = fopen(out_fname,"w");
        if(out!=NULL){
            fprintf(out,"%s",ser);
            fprintf(out,"\n");
            fclose(out);
        }
        free(ser);
        free(out_fname);
        free(in_fname);
    }
    
    destroy_animal_question(root);
    exit(0);
}
