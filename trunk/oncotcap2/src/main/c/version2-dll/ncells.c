#include "build.h"

#include "defines.h"
		/**@ ncells.p **/
 real /** FUNCTION**/ ncells (real lognumber)   /*COMPILED*/
/*	 real lognumber;      */
{
	return(expo (lognumber * LN10));
}
