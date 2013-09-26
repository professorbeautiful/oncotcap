#include <math.h>
#include <stdlib.h>


#define BINOM_MAX  1.0e9 
#define boolean int
#ifndef TRUE
#define TRUE -1
#define FALSE 0
#endif
#define NUMOFRAND 4
#define CUTOFF 2000.0

#define CNRAND  0

static double  u[98][NUMOFRAND], cx[NUMOFRAND], cd[NUMOFRAND], cm[NUMOFRAND];
static int i97[NUMOFRAND], j97[NUMOFRAND];
static boolean test[NUMOFRAND];

#ifndef RAND_MAX
#define RAND_MAX 2147483647
#endif

double (*RandFunc)(int);






