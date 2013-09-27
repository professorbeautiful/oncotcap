#include "PlotHandler.h"
/***************************************************************************
* Name     :PlotHandler.c
* Author   :Sai
* Date     :07/15/99
* Purpose  :setupPlotHandler: Sets up the array of the plothandler.  The 
*           celltypes that correspond to the subset are stored in cellIndicesToPlot
*           The number of lines to plot are calculated and the CNsave vector is
*			initialized
*           PlotHandler:  The handler that calculates the cellcounts for each 
*           line from the subset celltypes and calls the VB PlotData
*****************************************************************************/
#include <stdlib.h>
#include <stdio.h>
#include "build.h"
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "logger.h"

int EXPORT PASCAL  getCellLevelbyClassforPlotting();
void get_macro_levelname(int cn,int lnum,char *name);

int EXPORT PASCAL setupPlotHandler()
{
	unsigned int i;
	unsigned int j;
	int classIdx,levelIdx;
	boolean selectCellType;
	int retVal;
	boolean selectcellTypebyHetclass[MAXCLASSES];
	char tStr[255],tStr1[255], errStr[255];

	retVal = 0;
	//Init CNsave
	fprintf(logfile,"%s\tfunction:PlotHandler.setupPlotHandler\n", gettime());
	if (CNsave.cellcount != NULL)
	{
		free(CNsave.cellcount);
		CNsave.cellcount = NULL;
	}
	// set up the subsetting
	if (SubsetOption ==SUBSETSOMECELLSSELECTED)
	{
		//setup the subset
		numCellIndicesToPlot = 0;
		//initially allocate cellIndicesToPlot to MAXLOOKUPS
		cellIndicesToPlot = (unsigned int *)calloc(MAXLOOKUPS, sizeof(unsigned int));
		 if (cellIndicesToPlot == NULL)
		 {
			 sprintf(errStr,"setupPlotHandler: Out of memory, unable to allocate cellIndicesToPlot");
			 MsgBoxError(errStr);
		 }

		maxCellIndicesToPlot = MAXLOOKUPS;
		//for all possible celltypes
		for(i=1;i<=ntypes;i++)
		{
			 //initialize the boolean
			 for(j=0;j<(unsigned int)Number_of_Classes;j++)
			 {
			 	selectcellTypebyHetclass[j] = false;
			 }
			 selectCellType = true;
			 //check conditions
			 for(j=1;j<=numPlotSubset;j++)
			 {
				 levelIdx = getcelllevelbyclass(i,PlotSubset[j].classIdx);
				 //check is this celltype matches the subset class and level
				 if (levelIdx == PlotSubset[j].levelIdx)
				 {
					 selectcellTypebyHetclass[PlotSubset[j].classIdx] = true;
				 }
			 }
			 //if this celltype has matched atleast one subset condition for each class
			 for(j=1;j<=numPlotSubset;j++)
			 {
				 if( selectcellTypebyHetclass[PlotSubset[j].classIdx] == false)
				 {
					 selectCellType = false;
				 }
			 }
			 if (selectCellType == true) 
			 {
				 numCellIndicesToPlot++;
				 if (numCellIndicesToPlot >= maxCellIndicesToPlot)
				 {
					 maxCellIndicesToPlot += MAXLOOKUPS;
					 cellIndicesToPlot = (unsigned int *) realloc (cellIndicesToPlot , maxCellIndicesToPlot  * sizeof(unsigned int));
					 if (cellIndicesToPlot == NULL)
					 {
						 sprintf(errStr,"setupPlotHandler: Out of memory, unable to allocate cellIndicesToPlot");
						 MsgBoxError(errStr);
					 }

				 }
				 cellIndicesToPlot[numCellIndicesToPlot] = i;
			 }
		}
	}	

	//Allocate CNsave.cellcount, calculate numPlotLines
	switch(PlotOption)
	{
		case PLOTNOPROPSELECTED:
			CNsave.cellcount = (double *) calloc (2, sizeof(double));
			numPlotLines = 1;
			strcpy(PlotLineName[1],"Total Cellcount");
		break;

		case PLOTALLPROPSELECTED:
			CNsave.cellcount = (double *) calloc (ntypes+1, sizeof(double));
			numPlotLines = ntypes;
//			PlotLineName = (char *) calloc 
			for(i=1;i<=ntypes;i++)
			{
				strcpy(PlotLineName[i],"");
				construct_type_name(i,PlotLineName[i]);
			}
		break;

		case PLOTSOMEPROPSELECTED:
			//calculcate the number of lines
			numPlotLines=1;
			for(i=1;i<=numPlotProperty;i++)
			{
				numPlotLines = numPlotLines * Class[PlotProperty[i]].no_levels;
			}
			CNsave.cellcount = (double *) calloc (numPlotLines+1, sizeof(double));

			//Create Plot line names
			for(i=1;i<=numPlotLines;i++)
			{
//				PlotLineName[i] = (char *) calloc(255, sizeof(char));
				strcpy(PlotLineName[i],"");
				for(j=1;j<=numPlotProperty;j++)
				{
					classIdx = PlotProperty[j];
					levelIdx = getCellLevelbyClassforPlotting(i,j-1);
					if (Class[classIdx].class_type == MACRO)
					{
						get_macro_levelname(classIdx,levelIdx,tStr1);
					}
					else
					{
						strcpy(tStr1,Class[classIdx].Level[levelIdx].level_name);
					}

					if (j>1)
					{
						sprintf(tStr,"/%s",tStr1);
						strcat(PlotLineName[i],tStr);
					}
					else
					{
						sprintf(PlotLineName[i],"%s",tStr1);
					}
				}
			}
		break;
	}//end select
	return(retVal);
}


void PlotHandler()
{
	unsigned int i,j,k;
	int classIdx1,levelIdx1,classIdx2,levelIdx2;
	boolean found, addCellcountToLine;

	switch(PlotOption)
	{
		case PLOTNOPROPSELECTED:
			CNsave.cellcount[1] = 0.0;
			for(i=1;i<=(unsigned int)active_ntypes;i++)
			{
				if (SubsetOption == SUBSETALLCELLSSELECTED)
				//if all cells selected, no need to check in cellindicesToPlot
				{
					CNsave.cellcount[1] = CNsave.cellcount[1] + CN[i];
				}
				else
				{
					k=1;
					found = false;
					while((k<=numCellIndicesToPlot) && (!found))
					{					
						if(LookUp[i].LookUpId == cellIndicesToPlot[k])
						{
							found = true;
							CNsave.cellcount[1] = CNsave.cellcount[1] + CN[i];
						}
						else
						{
							k++;
						}
					}
				}
			}
		break;

		case PLOTALLPROPSELECTED:
			for(i=1;i<=(unsigned int)active_ntypes;i++)
			{
				if (SubsetOption == SUBSETALLCELLSSELECTED)
				//if all cells selected, no need to check in cellindicesToPlot
				{
					CNsave.cellcount[LookUp[i].LookUpId] =  CN[i];
				}
				else
				{
					k=1;
					found = false;
					while((k<=numCellIndicesToPlot) && (!found))
					{					
						if(LookUp[i].LookUpId == cellIndicesToPlot[k])
						{
							found = true;
							CNsave.cellcount[LookUp[i].LookUpId] =  CN[i];
						}
						else
						{
							k++;
						}
					}
				}
			}
		break;

		case PLOTSOMEPROPSELECTED:
			// for each line
			for(i=1;i<=numPlotLines;i++)
			{
				CNsave.cellcount[i] = 0.0;
				if (SubsetOption == SUBSETALLCELLSSELECTED)
				//if all cells selected, no need to check in cellindicesToPlot
				{
					// for each celltype in the subset
					for(j=1;j<=(unsigned int)active_ntypes;j++)
					{
						addCellcountToLine = true;
						for(k=1;k<=numPlotProperty;k++)
						{
							classIdx1 = PlotProperty[k];
							levelIdx1 = getCellLevelbyClassforPlotting(i,k-1);
							classIdx2 = classIdx1;
							levelIdx2 = getcelllevelbyclass(LookUp[j].LookUpId,classIdx2);
							//if the level of the line does not match the level of the celltype then
							//do not add this celltype to the line
							if(levelIdx2 != levelIdx1)
							{
								addCellcountToLine = false;
								
							}
						}
						if (addCellcountToLine  == true)
						{
							CNsave.cellcount[i]=CNsave.cellcount[i] + getcellcounts(LookUp[j].LookUpId,!VEGF);
						}
					}
				}
				else
				{
					// for each celltype in the subset
					for(j=1;j<=numCellIndicesToPlot;j++)
					{
						addCellcountToLine = true;
						for(k=1;k<=numPlotProperty;k++)
						{
							classIdx1 = PlotProperty[k];
							levelIdx1 = getCellLevelbyClassforPlotting(i,k-1);
							classIdx2 = classIdx1;
							levelIdx2 = getcelllevelbyclass(cellIndicesToPlot[j],classIdx2);
							//if the level of the line does not match the level of the celltype then
							//do not add this celltype to the line
							if(levelIdx2 != levelIdx1)
							{
								addCellcountToLine = false;
								
							}
						}
						if (addCellcountToLine  == true)
						{
							CNsave.cellcount[i]=CNsave.cellcount[i] + getcellcounts(cellIndicesToPlot[j],!VEGF);
						}
					}

				}

			}
		break;
	}
	PlotData();
}






int EXPORT PASCAL  getCellLevelbyClassforPlotting( int icell, int iclass)
{
	unsigned int i;
	int preProd,postProd;
	int sLevel,retVal;

	fprintf(logfile,"%s\tfunction:PlotHandler.getCellLevelbyClassforPlotting\ticell:%d\ticlass:%d\n", gettime(),icell,iclass);
	preProd = 1;
	icell--;
	for(i=iclass;i<numPlotProperty;i++)
	{
		preProd = preProd * Class[PlotProperty[i+1]].no_levels;
	}
	postProd = 1;
	for(i=iclass+1;i<numPlotProperty;i++)
	{
		postProd = postProd * Class[PlotProperty[i+1]].no_levels;
	}
	sLevel = icell % preProd;
	retVal = (sLevel / postProd);
	return(retVal);
}




	







	
