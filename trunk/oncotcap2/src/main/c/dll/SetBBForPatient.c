#include "bboard.h"

void SetBBForPatient()
{
	int i;

	/*the following should be moved to the dll init routine*/
	TRUEVAL = 1;
	FALSEVAL = 0;


	ClearBlackBoard(); /*also should initially set head and tail to NULL in init*/


	SetBA("TRUE",TRUEVAL);
	SetBA("FALSE",FALSEVAL);
	SetBA("PrimaryDiagnosisJustHappened",FALSEVAL);
	SetBA("RecurrenceJustHappened",FALSEVAL);

	for(i=0;i <nUserAddedVars;i++)
	{
		switch(UserAddedVars[i].varType)
		{
			case BBBOOL:
			case BBINT:
				SetIA(UserAddedVars[i].varName,(int)UserAddedVars[i].Value);
			break;

			case BBDOUBLE:
				SetDA(UserAddedVars[i].varName,UserAddedVars[i].Value);
			break;
		}
	}

			



}