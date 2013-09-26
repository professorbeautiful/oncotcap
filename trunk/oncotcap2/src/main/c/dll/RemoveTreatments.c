#include "build.h"
#ifdef DLL
#include <windows.h>
#endif
#include "defines.h"
#include "classes.h"
#include "cellp.h"

void RemoveTreatments()
{
	int n,nowenvl,iindex;
	for (n=2; n<=Ndrug; n++)
	{
		evaluate_schedule("n",n);
		buildsched();
		sortsched();
	}
	for ( iindex=1; iindex <=active_ntypes;iindex++) {	   
	   for (nowenvl = 0; nowenvl < nenvirons; nowenvl++)
	   {
		  buildkill(iindex,nowenvl);
		  setrates (iindex,nowenvl);
	   }
	}
}