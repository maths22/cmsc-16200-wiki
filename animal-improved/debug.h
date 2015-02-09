#ifndef DEBUG_H
#define DEBUG_H

#include <stdio.h>

#if DEBUG
#define debug_print(a,...) fprintf(stderr,"[DEBUG] (%s:%i) "a"\n",__FILE__,__LINE__,##__VA_ARGS__)
#define error_print(a,...) fprintf(stderr,"[ERROR] (%s:%i) "a"\n",__FILE__,__LINE__,##__VA_ARGS__)
#else
#define debug_print(a,...)
#define error_print(a,...) fprintf(stderr,"[ERROR] "a"\n",##__VA_ARGS__)
#endif

#endif
