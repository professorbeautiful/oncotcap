package oncotcap.display.editor.persistibleeditorpanel;

import java.util.*;
import java.io.*;
import java.lang.reflect.Array;
import oncotcap.util.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;


public class LevelAndList {
		EnumLevelList enumLevelList = null;
		EnumLevel enumLevel = null;
		
		public  LevelAndList(EnumLevelList ll, EnumLevel l) {
				enumLevelList = ll;
				enumLevel = l;
		}			
		public EnumLevel getLevel() {
				return enumLevel;
		}
		public EnumLevelList getLevelList() {
				return enumLevelList;
		}
		public String toString(){
				return enumLevel.toString();
		}
}
