package oncotcap.datalayer;  // REPACKAGE THIS
import java.util.*;
import oncotcap.datalayer.*;
import oncotcap.Oncotcap;
import oncotcap.util.ReflectionHelper;
import oncotcap.display.common.*;
import oncotcap.datalayer.persistible.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.reflect.*;
import java.util.*;

public interface AutoGenEditable {
		

		
		public Method getSetter(String name, Class argName);
		public Hashtable getGetterMap();
		public Hashtable getSetterMap();
		public Method getGetter(String name);
		public Method getGetMethod(String name);
}
