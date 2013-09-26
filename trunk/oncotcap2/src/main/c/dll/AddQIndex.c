#include <stdlib.h>
#include <windows.h>
#include "build.h"
#include "defines.h"
#include "classes.h"
#include "cellp.h"

int AddQIndex(int h, int i)
{
	int itemp;

		itemp = h + i;
		if (itemp > MAXQLENGTH - 1)
		{
			itemp = itemp - MAXQLENGTH;
		}
	return (itemp);
}
