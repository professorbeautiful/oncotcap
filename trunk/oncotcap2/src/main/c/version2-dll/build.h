/* In order to use this suite:
For DLL version, set preprocessor definition: DLL and (COMP or UNCOMP) 
For API version on PC, set preprocessor definition: TEST(no MPI), TESTMPI and (COMP or UNCOMP) 
For API version on UNIX, #define UNIX, and #define TEST after this.
For API MPI version on UNIX, #define MPI
COMP or UNCOMP should be set after other macro
*/


/*#ifndef UNIX
  #define UNIX 
  #define TEST
  #define UNCOMP
  #define TESTMPI
  #endif
  */


#ifndef DLL
#define DLL
#define UNCOMP
#endif