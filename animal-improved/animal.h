//animal.h
//Jacob Burroughs

#ifndef ANIMAL_H
#define ANIMAL_H

typedef struct animal_question_struct animal_question_struct, *animal_question;

struct animal_question_struct{
    char* question;
    char* true_animal_name;
    char* false_animal_name;
    animal_question qtrue;
    animal_question qfalse;
};

animal_question create_animal_question();
void destroy_animal_question(animal_question a);
bool ask_animal_question(char* s);
bool guess_animal(char* s);
void add_animal(animal_question currq, bool side);
char* serialize_animal(animal_question a);
animal_question deserialize_animal(FILE* f);

#endif
