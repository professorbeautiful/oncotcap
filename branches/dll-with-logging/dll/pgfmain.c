#include "build.h"

#define MAIN
#ifndef TESTMPI
#include <windows.h>
#include <crtdbg.h> /* needed only for _ASSERTE when debugging */
#include <stdio.h>
#include <stdlib.h>
#include "defines.h"
#include "classes.h"
#include "cellp.h"
#include "msim.h"
#include "InducedConv.h"
#include "logger.h"

int WINAPI DllMain( HINSTANCE hInstance, DWORD fdwReason, PVOID pvReserved )
{

	FILE *inputfile;
    char strTemp[5];
	char strTcap[9];
    DWORD varsize;

	void TrialSetup();
	switch (fdwReason)
	{
	/* No multiple thread support  */
	case DLL_THREAD_ATTACH :
	case DLL_THREAD_DETACH :
		break;

	/* When process begins open logfile, initialize most stuff by calling
	 infile with a default parameter file, set conditions  */
	case DLL_PROCESS_ATTACH :
		strcpy(strTemp,"TEMP");
		strcpy(strTcap,"TCAPHOME");
		
		if ((varsize = GetEnvironmentVariable(strTemp, WorkingDir, 255)) == 0)
			MsgBoxError("TEMP environment variable not set");
	
/*		if ((varsize = GetEnvironmentVariable(strTcap, ArchiveDir, 247)) == 0)
            MsgBoxError("TCAPHOME environment variable not set");
*/
/*		SetVersion(); */
		SetHomeDir();
		
		strcat(ArchiveDir, "\\archive");

		INFINITESIMAL = 1e-12;
		INFINITY = 1e12;

		//TrialSetup();
		strcpy (ofilname, WorkingDir);
		strcat (ofilname, "\\pgf.dat");
		if (( eout = fopen(ofilname,"w")) ==  NULL)
		{
			MessageBox(NULL,"ERROR: pgf.dat failed open","treat.dll",MB_ICONEXCLAMATION | MB_OK);
			return (FALSE);
		}

		strcpy(logfilename,"c:\\tcaplog\\trace.log");
		if((logfile = fopen(logfilename, "a")) == NULL)
		{
			MessageBox(NULL,"ERROR: unable to open log file", "tread.dll", MB_ICONEXCLAMATION | MB_OK);
		}
		
		strcpy (infilname, ArchiveDir);
		strcat (infilname, "\\default.par");

		if (( inputfile = fopen(infilname,"r")) == NULL )
		{
			fprintf(eout,"Error opening default.par\n");
			MsgBoxWarning("Can't open default.par");
			fclose ( eout );
			return (FALSE);
		}
		else
		{

			fprintf(eout,"Reading default parameters (PAR.PANEL)\n");

			infile ( inputfile );
			fclose ( inputfile );
		}
			
		nconds = 0;
		nenvlist = 0;
		nICrules = 0;
		incondition ( /* inputfile */ );

		//create event objects to use in syncronizing event and cell
		//queue operations

		SimRunning = False;
		EndSim = False;
		MEndSim = False;
		break;
	
	/*  When process terminates close the logfile.*/
	case DLL_PROCESS_DETACH :
		fclose( eout );
		break;
	}

	return TRUE;
}


extern int EXPORT PASCAL DoPgf( )
{
int nowenvl,iindex;

 fprintf(logfile,"%s\tfunction:pgfmain.DoPgf\n", gettime());

/* all references to celpltfile comment out 11/15/96 -wes
  celpltfile = fopen("c:\\temp\\CELPLT.DAT", "w");*/

for ( iindex=1; iindex <=active_ntypes;iindex++) {
   for (nowenvl = 0; nowenvl < nenvirons; nowenvl++){
	  buildkill(iindex,nowenvl);
      setrates (iindex,nowenvl);
   }
}
   timecourse();
   return 1;
}
#endif
