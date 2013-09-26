#include "Const.h"
#include "bboard.h"

void SetBBFromEvents(double etime,int event,char det1[255],char det2[255], char det3[255])
{
	if (event == DIAGNOSISEVENT) 
	{
		SetBA("PrimaryDiagnosisJustHappened",TRUEVAL);
	}

	if (event == RECURRENCEEVENT)
	{
		SetBA("RecurrenceJustHappened",TRUEVAL);
	}
}