#include "build.h"

#include <stdio.h>
#include <string.h>
#include <math.h>

#include "defines.h"
#define COND condition[nconds]

void incondition (/* p */)
/*   FILE *p; */
{
   integer itype;
    
      typeconds ( );

      nconds = 1;
      for ( itype = 1; itype  <= (int)ntypes; itype++ )
      COND.qtypes[itype] = YES;
      COND.dir = GE;
      COND.size = 1;
      COND.t = 18;
      typeconds();
}