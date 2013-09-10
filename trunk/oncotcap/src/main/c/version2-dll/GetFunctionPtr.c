#include <stdlib.h>
#include "BBoard.h"
PFI GetFunctionPtr(int fConst)
{
	PFI fptr;

	fptr = NULL;

	switch (fConst)
	{
	case FRSETI:
		fptr = (PFI) &SetI;
		break;

	case FRSETB:
		fptr = (PFI) &SetB;
		break;

	case FRSETD:
		fptr = (PFI) &SetD;
		break;

	case FRSET:
		fptr = (PFI) &Set;
		break;

	case FRCLEARREG:
		fptr = (PFI) &ClearBBRegimen;
		break; 

	case FRADDTOREG:
		fptr =  &AddtoRegimen;
		break; 

	case FRAPPLYREG:
		fptr =  &ApplyRegimenIntoSchedule;
		break; 

	case FRADD:
		fptr = (PFI) &Add;
		break;

	case FRAND:
		fptr = &And;
		break;

	case FREQ:
		fptr = &Eq;
		break;

	case FRLT:
		fptr = &Lt;
		break;

	case FRGT:
		fptr = &Gt;
		break;

	case FRLTE:
		fptr = &Lte;
		break;

	case FRGTE:
		fptr = &Gte;
		break;

	case FRNOW:
		fptr = (PFI) &Now;
		break;

	case FRRANDLOGNORM:
		fptr = (PFI) &RandLogNorm;
		break;
		
	case FRNE:
		fptr = &Ne;
		break;
	case FRAPPLYCONTINDRUG:
		fptr = (PFI) &ApplyContinDrug;
		break;
	case FRREMOVECONTINDRUG:
		fptr = (PFI) &RemoveContinDrug;
	}
	return(fptr);
}