#include <stdlib.h>
#include "build.h"
#ifdef DLL
#include <windows.h>
#endif
#include <string.h>
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "tox.h"
#include "bboard.h"

void AddToEventQ(double etime,int event,char det1[255],char det2[255], char det3[255])
{

	void InterpretEvents(double etime,int event,char det1[255],char det2[255], char det3[255]);

	SetBBFromEvents(etime, event, det1, det2, det3);

	if ( !((event == CUREEVENT || event == TUMORSTARTEVENT) && TrialSim == True))
	{
		Event_List[0].time = etime;
		Event_List[0].event = event;
		strcpy(Event_List[0].detail1,det1);
		strcpy(Event_List[0].detail2,det2);
		strcpy(Event_List[0].detail3,det3);
		PostEvent();
	}

	if ( (event == METEVENT))
	{
		if ((TrialInfo.ProgressionOnNewMets == True) && (OffTrial == False))
		{
			AddToEventQ(t,OFFTRIALEVENT,"Progression due to new metastasis",".",".");
			RemoveTreatments();
			OffTrial = True;
		}
	}

	if (TrialSim == True)
		InterpretEvents(etime, event, det1, det2, det3);
}