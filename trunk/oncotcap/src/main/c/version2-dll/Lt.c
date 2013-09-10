/* Lt - Performs a LessThan comparison on two BlackBoard variables.
 *      result is stored in a third BlackBoard Variable as a Boolean.  
 *      Either argument can be an Int or a Double.
 *
 *  Input: args, pointer to an argument list that contains
 *         args[0]: a pointer to a BlackBoard key that will receive the result
 *         args[1]: a pointer to a BlackBoard key that contains the first argument
 *         args[2]: a pointer to a BlackBoard key that contains the second argument
 * Output: Boolean, the result of the operation
 */ 

#include "bboard.h"

int Lt(void *args[3])
{
	return(Comp((char *) args[0], (char *) args[1], (char*) args[2], "<"));
}
