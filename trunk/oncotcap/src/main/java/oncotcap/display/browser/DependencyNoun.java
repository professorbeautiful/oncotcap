package oncotcap.display.browser;
import oncotcap.engine.*;
import oncotcap.util.*;

public class DependencyNoun extends Object {
		String className = null;
		String objName = null;
		PreprocessedCodeBundle ppcb = null;
		public DependencyNoun(PreprocessedCodeBundle cb, String varName, boolean noClass) {
				// Instantiate as usual and use the ppcb to substitute but the representation
			  // of this noun should not be associated with any particular class
				this(cb, varName);
				ppcb = cb;
				if ( noClass = true)
						this.className = null;
		}
		public DependencyNoun(PreprocessedCodeBundle cb, String varName) {
				ppcb = cb;
				// Make sure the variables are substituted
				String subsClassName = ValueMap.substitute(cb.getValueMap(), cb.getProcessName());
				String subsName = ValueMap.substitute(cb.getValueMap(), varName);
				if ( subsClassName != null ) 
						this.className =  StringHelper.javaName(subsClassName);
				if ( subsName != null ) {
						// If the name is a full ClassName strip off the class 
						this.objName = StringHelper.javaName(subsName).replaceAll((this.className + "."), "");
				}
		}
		public DependencyNoun(String className, String varName) {
				if ( className != null ) 
						this.className = StringHelper.javaName(className);
				if ( varName != null ) 
						this.objName = StringHelper.javaName(varName);
		}
		public String getObjectName() {
				return this.objName;
		}
		public String getClassName() {
				return this.className;
		}
		public String getFullName() {
				String delimiter = ".";
				String theObjName = "";
				String theClassName = "";
				if ( className != null )
						theClassName = className;
				if ( objName != null ) 
						theObjName = objName;
				if ( className == null || objName == null)
						delimiter = "";
				//return theClassName + delimiter + objName;
				return theClassName + delimiter + theObjName;
		}
		public PreprocessedCodeBundle getPreprocessedCodeBundle() {
				return this.ppcb;
		}
		public String toString() {
				return getFullName();
		}
		public boolean equals(Object obj) {
				boolean classMatches = false;
				boolean objectMatches = false;
				if ( obj instanceof DependencyNoun && obj != null ) {
						if ( getClassName() == null && ((DependencyNoun)obj).getClassName() == null )
								classMatches = true;
						if ( getObjectName() == null && ((DependencyNoun)obj).getObjectName() == null )
								objectMatches = true;
						if ( getClassName() != null &&
								 getClassName().equals(((DependencyNoun)obj).getClassName()) )
								classMatches = true;
						if ( getObjectName() != null &&
								 getObjectName().equals(((DependencyNoun)obj).getObjectName()) )
								objectMatches = true;
						if ( classMatches && objectMatches)
								return true;
				}
				return false;
		}
}

