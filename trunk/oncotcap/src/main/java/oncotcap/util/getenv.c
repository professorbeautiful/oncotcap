#include <jni.h>
#include <stdlib.h>
#include "getenv.h"

/* oncotcap.utilities.SystemUtil.getenv
 * 
 * This functions is used by the Java SystemUtil class via jni to get
 * system environment variables.
 *
 * Use makedll.bat to build this.  makedll.bat requires that MS Visual
 * Studio 6 is installed, cl.exe is in your PATH and the jdk JNI
 * includes are in c:\jdk1.4\include.
 *
 * Place the resulting DLL in the c:\windows\system32 directory.
 */

JNIEXPORT jstring JNICALL 
Java_oncotcap_util_SystemUtil_getenv(JNIEnv *env, jobject obj, jstring var)
{
	char *val;
	const jbyte *strVar;
	strVar = (*env)->GetStringUTFChars(env, var, NULL);
	if (strVar == NULL) {
		return NULL; /* OutOfMemoryError already thrown */
	}
	val = getenv(strVar);
	(*env)->ReleaseStringUTFChars(env, var, strVar);
	  /* We assume here that the user does not type more than
      * 127 characters */
	return (*env)->NewStringUTF(env, val);
}
