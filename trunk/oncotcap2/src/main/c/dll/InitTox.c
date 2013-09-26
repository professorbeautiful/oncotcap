#include "build.h"
#ifdef DLL
#include <windows.h>
#else 
extern char *unix_strupr();
#endif
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "tox.h"

void InitToxTypes()
{
	int iagent,itox,jtox;
	boolean found;

	for(iagent=1;iagent<=ndrugs;iagent++)/* Do for all drugs */
	{
		for(itox=1;itox <= Agents[iagent].numToxTypes; itox ++) /* for all toxtypes */
		{
			found = FALSE;
			if(Agents[iagent].toxicity[itox].toxTypeIndex == -1)
				/*if the agents.toxicity.toxtypeindex has not been set then */
			{
				jtox=1;
				while((found != TRUE) && (jtox < nToxTypes))
				{
#ifndef UNIX
					if(strcmp(_strupr(Agents[iagent].toxicity[itox].name),_strupr(ToxTypes[jtox].name)) == 0)
#else
					if(strcmp(unix_strupr(Agents[iagent].toxicity[itox].name),unix_strupr(ToxTypes[jtox].name)) == 0)
#endif
					{
						/* try to find the toxicity name in the toxtypes array */
						found = TRUE;
						Agents[iagent].toxicity[itox].toxTypeIndex = jtox;
					}
					else
					{
						jtox++;
					}
				}
				if(found == FALSE)
				{
					/*if the search has failed this means that this toxicity is not in the 
					ToxTypes array */
					nToxTypes++;
					strcpy(ToxTypes[nToxTypes].name,Agents[iagent].toxicity[itox].name);
					Agents[iagent].toxicity[itox].toxTypeIndex = nToxTypes;
				}
			}
		}
	}
	
	for(jtox = 1;jtox <=nToxTypes;jtox++)
	{
		ToxTypes[jtox].toxResTime = 0.0;
		ToxTypes[jtox].toxResType = FALLTOZERO;
		ToxTypes[jtox].currGrade = 0;
		for(iagent=1;iagent <= ndrugs;iagent++)
		{
			for(itox=1;itox <=Agents[iagent].numToxTypes;itox++)
			{
				if(Agents[iagent].toxicity[itox].toxTypeIndex == jtox)
				{
					if(Agents[iagent].toxicity[itox].toxResType == BYONEGRADE)
					{
						ToxTypes[jtox].toxResType = BYONEGRADE;
					}

					if(Agents[iagent].toxicity[itox].toxResType == NEVERRESOLVES)
					{
						ToxTypes[jtox].toxResType = NEVERRESOLVES;
					}

					if(Agents[iagent].toxicity[itox].toxResTime > ToxTypes[jtox].toxResTime )
					{
						ToxTypes[jtox].toxResTime = Agents[iagent].toxicity[itox].toxResTime;
					}
				}
			}
		}
	}
}










				

	