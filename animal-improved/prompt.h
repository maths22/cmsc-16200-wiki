//prompt.h
//Jacob Burroughs

#ifndef PROMPT_H
#define PROMPT_H

//ask q as a yes/no question, using q as a format string
bool ask_yn(const char* q, ...);
//using s as a format string, prompt on a new line
char* prompt_input_newline(const char* s, ...);
//same as above prompt on the same line
char* prompt_input_inline(const char* s, ...);
//same as above, but if the user inputs '\n', default_val will be returned
char* prompt_input_inline_default(const char* default_val, const char* s, ...);

#endif
