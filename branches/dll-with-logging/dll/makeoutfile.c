#include "build.h"

#include "defines.h"

extern int EXPORT PASCAL makeoutfile(char *filename)
{
	outfile(filename);
	return 1;
}