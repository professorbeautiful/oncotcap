#include "build.h"

#ifndef UNIX
#include <windows.h>
#include <crtdbg.h>
#include <stdio.h>
#include <string.h>

int MsgBoxBreak(char mesg[], int breaknum)
{
	char boxmesg[256], initmesg[256], caption[256];
	int ret;
	static enabled[20];

	if ( ! enabled[breaknum]) return(0);
	strcpy(caption, "Do you want to break? ");
	strcpy(initmesg,"INFO: ");
	strcpy(boxmesg, initmesg);
	strncat(boxmesg, mesg, min(strlen(mesg),256-strlen(boxmesg)));
    if ((ret=MessageBox(NULL, boxmesg ,caption, 
		MB_ICONEXCLAMATION | MB_YESNOCANCEL |MB_DEFBUTTON2|MB_HELP)
		) == IDYES)
		_ASSERTE(breaknum<0);
	if (ret==IDCANCEL) enabled[breaknum]=1;
	return(ret);
}

int MsgBoxDoubleValue(char varname[], double value, int breaknum)
{
	char temp[256], boxmesg[256];
	strcpy(boxmesg,varname);
	strcat(boxmesg,"\n = ");
	sprintf(temp,"%lf",value);
	strcat(boxmesg, temp);
	return(MsgBoxBreak(boxmesg,breaknum));
}

int MsgBoxWarning(char mesg[])
{
	char warnmesg[256];

	if (strlen(mesg) > 246) mesg[246] = '\0';	
	strcpy(warnmesg, "Warning: ");
	strcat(warnmesg, mesg);

	return(  1); //MessageBox(NULL, warnmesg ,"treat.dll",MB_ICONEXCLAMATION | MB_OK));
}

void MsgBoxError(char mesg[])
{
	char warnmesg[256];


	if (strlen(mesg) > 248) mesg[248] = '\0';	
	strcpy(warnmesg, "Error: ");
	strcat(warnmesg, mesg);	

    MessageBox(NULL, warnmesg ,"treat.dll",MB_ICONEXCLAMATION | MB_OK);
	exit(-1);
	return;
}
#endif