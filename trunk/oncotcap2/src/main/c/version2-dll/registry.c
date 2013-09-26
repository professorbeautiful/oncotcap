#include "build.h"

#ifndef TESTMPI
#include <windows.h>
#include <stdio.h>
#include "defines.h"

void MsgBoxError();
int RegQueryStringValue();

void SetHomeDir()
{
	/* get the Home Directory
	 * first check to see if it is set in the environment variable $TCAPHOME
	 * otherwise try to get it from the registry
	 */
	HKEY hkey;
	char strKey[23];
	char strTcap[9];
	char strHomeDirKey[5];

	strcpy(strKey, "SOFTWARE\\UPCI\\OncoTCap");
	strcpy(strTcap, "TCAPHOME");
	strcpy(strHomeDirKey, "Home");
	
	if ( GetEnvironmentVariable(strTcap, ArchiveDir, 247) == 0)
	{
		/*if our key exists, set TreatHome. if not give an error message and end the program*/
		if (RegOpenKeyEx(HKEY_LOCAL_MACHINE, strKey, 0, KEY_READ, & hkey) == ERROR_SUCCESS)
		{
			//'get the installation date and expiration key from the registry
			RegQueryStringValue(hkey, strHomeDirKey, ArchiveDir);
			RegCloseKey(hkey);
		}
		else
			MsgBoxError("Error: Can't get Treatsim Home directory from the registry.");    
	}

	return;
}

int RegQueryStringValue(HKEY hkey, char *strValueName, char *strData)
{

int RetVal;
DWORD lValueType;
long lDataBufSize = 255;

RetVal = 0;

//Get data type
if (RegQueryValueEx(hkey, strValueName, NULL, & lValueType, NULL, NULL) == ERROR_SUCCESS)
    //if it's a string
    if (lValueType == REG_SZ)
        if(RegQueryValueEx(hkey, strValueName, NULL, NULL, strData, & lDataBufSize) == ERROR_SUCCESS)
            return(1);

return (0);
}

void SetVersion()
{
	/* get the Version and set strTCAPVERSION and TCAPVERSION
	 */
	HKEY hkey;
	char strKey[23];
	char strTcap[9];
	char strVersionKey[8];
    char strVsn[255];
    char strMessage[255];

	strcpy(strKey, "SOFTWARE\\Tcap\\Treatsim");
	strcpy(strTcap, "TCAPHOME");
	strcpy(strVersionKey, "Version");
	
	    //if our key exists, set TCAPVERSION. if not give an error message and end the program
		if (RegOpenKeyEx(HKEY_LOCAL_MACHINE, strKey, 0, KEY_ALL_ACCESS, & hkey) == ERROR_SUCCESS)
		{
			//'get the installation date and expiration key from the registry
			RegQueryStringValue(hkey, strVersionKey, strVsn);
			RegCloseKey(hkey);

			if (strcmp(strVsn,strDLLVERSION) != 0)
			{
				strcpy(strMessage, "DLL version ");
				strcat(strMessage, strDLLVERSION);
				strcat(strMessage, ", is incompatable with\ninstalled version ");
				strcat(strMessage, strVsn);
				strcat(strMessage, ".");
				MsgBoxWarning(strMessage);
			}

			strcpy(strTCAPVERSION, "Version = ");
			strcat(strTCAPVERSION, strVsn);

			strcpy(TCAPVERSION, strTCAPVERSION);
			strcat(TCAPVERSION, "\n");
		}
		else
			MsgBoxError("Error: Can't find program version.");    
	    return;
}
#endif
