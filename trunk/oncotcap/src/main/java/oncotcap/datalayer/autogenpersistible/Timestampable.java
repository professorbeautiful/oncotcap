package oncotcap.datalayer.autogenpersistible;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import javax.swing.*;
import java.lang.reflect.*;
import java.util.*;
import oncotcap.display.editor.autogeneditorpanel.*;

public class Timestampable { //extends AutoGenPersistibleWithKeywords 
		protected Hashtable setMethodMap = new Hashtable();
		protected Hashtable getMethodMap = new Hashtable();
		private String creationTime;
		private String modificationTime;
		private String modifier;
		private String creator;
		private Integer versionNumber;
		private String instanceUsabilityStatus;

	public Timestampable(){
		init();
	}
	private void init() {
			Method setter = null;
			Method getter = null;
			setter = getSetter("setCreationTime", String.class);
			setMethodMap.put("creationTime", setter);
			getter = getGetter("getCreationTime");
			getMethodMap.put("creationTime", getter);
			setter = getSetter("setModificationTime", String.class);
			setMethodMap.put("modificationTime", setter);
			getter = getGetter("getModificationTime");
			getMethodMap.put("modificationTime", getter);
			setter = getSetter("setModifier", String.class);
			setMethodMap.put("modifier", setter);
			getter = getGetter("getModifier");
			getMethodMap.put("modifier", getter);
			setter = getSetter("setCreator", String.class);
			setMethodMap.put("creator", setter);
			getter = getGetter("getCreator");
			getMethodMap.put("creator", getter);
			setter = getSetter("setVersionNumber", Integer.class);
			setMethodMap.put("versionNumber", setter);
			getter = getGetter("getVersionNumber");
			getMethodMap.put("versionNumber", getter);
			setter = getSetter("setInstanceUsabilityStatus", String.class);
			setMethodMap.put("instanceUsabilityStatus", setter);
			getter = getGetter("getInstanceUsabilityStatus");
			getMethodMap.put("instanceUsabilityStatus", getter);

	}

	public String getCreationTime(){
		return creationTime;
	}
	public Integer getVersionNumber() {
		return versionNumber;
	}
	public String getModificationTime(){
		return modificationTime;
	}
	public String getModifier(){
		return modifier;
	}
	public String getCreator(){
		return creator;
	}
	public String getInstanceUsabilityStatus(){
		return instanceUsabilityStatus;
	}
	public void setCreationTime(String var ){
		creationTime = var;
	}

	public void setModificationTime(String var ){
		modificationTime = var;
	}

	public void setModifier(String var ){
		modifier = var;
	}

	public void setCreator(String var ){
		creator = var;
	}
	public void setInstanceUsabilityStatus(String var){
		instanceUsabilityStatus = var;
	}
	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}

	public String toString() {
		return "Timestamp is " + getVersionNumber() + " " + 
				getModifier();
	}
	public Class getPanelClass()
	{
		return TimestampablePanel.class;
	}
	public String getPrettyName()
	{
		return "Timestampable";
	}
		public Method getSetter(String name, Class argName) {
				Class [] paramList = new Class[1];
				Method setMethod = null;
				try {
				
					// 	System.out.println("setter " + name + "  " 
						// 															 + paramList + " " + argName);
						if ( argName == DefaultPersistibleList.class ) 
								argName = Collection.class;
						// 	System.out.println("setter " + name + "  " 
						// 															 + paramList + " " + argName);
						paramList[0] = argName;
						setMethod = 
								this.getClass().getMethod(name,
																					paramList);
				}
				catch ( Exception ea ) {
						ea.printStackTrace();
				}
				return setMethod;
		}

		public Hashtable getGetterMap() {
				return getMethodMap;
		}
		public Hashtable getSetterMap() {
				return setMethodMap;
		}

		public Method getGetter(String name) {
				Method getMethod = null;
				try {
						getMethod = 
								this.getClass().getMethod(name, (Class []) null);
				}
				catch ( Exception ea ) {
						ea.printStackTrace();
				}
				return getMethod;
		}

		public Method getGetMethod(String name) {
				if ( name != null ) 
						return (Method)getMethodMap.get(name);
				else 
						return null;
		}

}
