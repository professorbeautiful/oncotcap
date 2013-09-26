#ifndef MAXCLASSES
#include "CONST.h"
#endif
#ifdef DLL
#include <stdlib.h>
#endif

unsigned int numPlotSubset;
int PlotOption;
int SubsetOption;
struct PlotSubsettype 
{
	int classIdx;
	int levelIdx;
} PlotSubset[MAXCLASSES];


unsigned int numPlotProperty;
int PlotProperty[MAXTYPES];

unsigned int numCellIndicesToPlot;
unsigned int maxCellIndicesToPlot;
unsigned int *cellIndicesToPlot;

unsigned int numPlotLines;
char PlotLineName[MAXTYPES][255];

void construct_type_name (unsigned int icelltype,char *typename );
double getcellcounts(unsigned int index,int flag);