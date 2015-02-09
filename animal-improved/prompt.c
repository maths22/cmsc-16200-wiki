//prompt.c
//Jacob Burroughs

#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <stdarg.h>

#include "prompt.h"

#include "debug.h"

static size_t lnsize = 0;

int readline_nonempty(char** resp);

bool vask_yn(const char* q, va_list chars){
    
    char format[strlen(q)+strlen(" (y/n) ")+1];
    strcpy(format, q);
    strcat(format, " (y/n) ");
    vfprintf(stdout, format, chars);
    char* resp = NULL;
    int linesize = readline_nonempty(&resp);
    
    
    bool ret = -1;
    if(linesize == 2){
        if(resp[0] == 'n'){
            ret = false;
        } else if (resp[0] == 'y') {
            ret = true;
        } else {
            printf("invalid input\n");
            ret = ask_yn(q);
        }
    } else {
        printf("invalid input\n");
        ret = ask_yn(q);
    }
    free(resp);
    return ret;
}

bool ask_yn(const char* q, ...){
    va_list argptr;
    va_start(argptr, q);
    bool ret = vask_yn(q, argptr);
    va_end(argptr);
    return ret;
}

char* vprompt_input(bool inlinep, const char* s, va_list chars){
    char* ps = inlinep?" > ":" > \n";
    char format[strlen(ps)+strlen(s)+1];
    strcpy(format, s);
    strcat(format, ps);
    vfprintf(stdout, format, chars);
    char* resp = NULL;
    readline_nonempty(&resp);
    return resp;
}

char* prompt_input_newline(const char* s, ...){
    va_list argptr;
    va_start(argptr, s);
    char* ret = vprompt_input(false, s, argptr);
    va_end(argptr);
    return ret;
}

char* prompt_input_inline(const char* s, ...){
    va_list argptr;
    va_start(argptr, s);
    char* ret = vprompt_input(true, s, argptr);
    va_end(argptr);
    return ret;
}

char* prompt_input_inline_default(const char* default_val, const char* s, ...){
    va_list argptr;
    va_start(argptr, s);
    char* ret = vprompt_input(true, s, argptr);
    va_end(argptr);
    if(ret[0] == '-')
    {
        free(ret);
        return strdup(default_val);
    }
    return ret;
}

char* prompt_input(bool inlinep, const char* s, ...){
    va_list argptr;
    va_start(argptr, s);
    char* ret = vprompt_input(inlinep, s, argptr);
    va_end(argptr);
    return ret;
}

//does NOT follow the pattern of getline
//resp will point to a newly allocated buffer
//the terminal newline will not be included
int readline_nonempty(char** ret){
    char* resp = NULL;
    int linesize = 0;
    while(resp == NULL || linesize <= 1){
        linesize = getline(&resp,&lnsize,stdin);
        if(linesize == -1){
            error_print("End of input reached.");
            exit(-1);
        }
        if(resp[linesize-1]=='\n'){
            resp[linesize-1]='\0';
        }
    }
    *ret = resp;
    return linesize;
}
