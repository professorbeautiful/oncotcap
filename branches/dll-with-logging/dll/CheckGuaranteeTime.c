#include "build.h"
#ifdef DLL
#include <windows.h>
#endif
#include "defines.h"
#include "classes.h"
#include "cellp.h"

/*	Filename	: CheckGuaranteeTime.c 
	Author		: Sai
	Date		: 02/03/98
	Comments	: Checks if there are any cells at the Condition time.
	              If No then sends a RESETEVENT which causes the sim to restart.
*/

void CheckGuaranteeTime()
{
	int i,j,IsNotZero;


	for(j=1;j<=nconds;j++)
	{
		IsNotZero = False;
		if (t >= condition[j].t )
		{
			for(i=1;i<=active_ntypes;i++)
			{
				if(CN[i] > 0.0 ) 
				{
					IsNotZero = True;
				}
			}
			HasGuaranteeTimeChecked = True;
			if (IsNotZero == False)
			{
				AddToEventQ(t,RESETEVENT,"","","");
				GuaranteeReset = True;
			}
		}
	}
}
