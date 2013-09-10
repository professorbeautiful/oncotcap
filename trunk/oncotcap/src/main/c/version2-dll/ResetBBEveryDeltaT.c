#include "bboard.h"

void ResetBBEveryDeltaT()
{
	if (Comp("~NULL","PrimaryDiagnosisJustHappened","TRUE","=="))
		SetBA("PrimaryDiagnosisJustHappened",FALSEVAL);

	if (Comp("~NULL","RecurrenceJustHappened","TRUE","=="))
		SetBA("RecurrenceJustHappened",FALSEVAL);
}