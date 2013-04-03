package oncotcap.datalayer.persistible.parameter;

import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.autogenpersistible.TransitionParameter;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class DeclareTwoLevelParameter extends TransitionParameter {
		private DefaultPersistibleList valueMapEntriesContainingMe;
		private String instanceUsabilityStatus;
		private DefaultPersistibleList variableDefinitions;
		private Integer versionNumber;
		
		protected String singleParameterID = null;
		public String displayName = null;

		protected String defaultDisplayName = null;
		
		private String idString = null;

		public DefaultSingleParameter startLevelParam = null;
		public DefaultSingleParameter endLevelParam = null;
		public DefaultSingleParameter characteristicParam = null;
		
		public DeclareTwoLevelParameter() {
		System.out.println("initialize DeclareTwoLevelParameter");

				init();
				initSingleParams();
		}
		
		
		public DeclareTwoLevelParameter(oncotcap.util.GUID guid) {
				super(guid);
				init();
				initSingleParams();

		}
		private void init() {
				Method setter = null;
				Method getter = null;
				setter = getSetter("setValueMapEntriesContainingMe", DefaultPersistibleList.class);
				setMethodMap.put("valueMapEntriesContainingMe", setter);
				getter = getGetter("getValueMapEntriesContainingMe");
				getMethodMap.put("valueMapEntriesContainingMe", getter);
				setter = getSetter("setInstanceUsabilityStatus", String.class);
				setMethodMap.put("instanceUsabilityStatus", setter);
				getter = getGetter("getInstanceUsabilityStatus");
				getMethodMap.put("instanceUsabilityStatus", getter);
				setter = getSetter("setVariableDefinitions", DefaultPersistibleList.class);
				setMethodMap.put("variableDefinitions", setter);
				getter = getGetter("getVariableDefinitions");
				getMethodMap.put("variableDefinitions", getter);
				setter = getSetter("setVersionNumber", Integer.class);
				setMethodMap.put("versionNumber", setter);
				getter = getGetter("getVersionNumber");
				getMethodMap.put("versionNumber", getter);
				setter = getSetter("setKeywords", DefaultPersistibleList.class);
				setMethodMap.put("keywords", setter);
				getter = getGetter("getKeywords");
				getMethodMap.put("keywords", getter);

				setter = getSetter("setStartLevelParam", String.class);
				setMethodMap.put("startLevelParam", setter);
				getter = getGetter("getStartLevelParam");
				getMethodMap.put("startLevelParam", getter);
				setter = getSetter("setEndLevelParam", String.class);
				setMethodMap.put("endLevelParam", setter);
				getter = getGetter("getEndLevelParam");
				getMethodMap.put("endLevelParam", getter);
				setter = getSetter("setCharacteristicParam", String.class);
				setMethodMap.put("characteristicParam", setter);
				getter = getGetter("getCharacteristicParam");
				getMethodMap.put("characteristicParam", getter);
		}
		
		public DefaultPersistibleList getValueMapEntriesContainingMe(){
				return valueMapEntriesContainingMe;
		}
		public String getInstanceUsabilityStatus(){
				return instanceUsabilityStatus;
		}
		public DefaultPersistibleList getVariableDefinitions(){
				return variableDefinitions;
		}
		public Integer getVersionNumber(){
				return versionNumber;
		}
		public void setValueMapEntriesContainingMe(java.util.Collection  var ){
				if ( valueMapEntriesContainingMe== null)
						valueMapEntriesContainingMe = new DefaultPersistibleList();
				valueMapEntriesContainingMe.set(var);
		}
		
		public void setInstanceUsabilityStatus(String var ){
				instanceUsabilityStatus = var;
		}
		
		public void setVariableDefinitions(java.util.Collection  var ){
				if ( variableDefinitions== null)
			variableDefinitions = new DefaultPersistibleList();
				variableDefinitions.set(var);
		}
		
		public void setVersionNumber(Integer var ){
				versionNumber = var;
		}
		
		public String toString() {
				return "DeclareTwoLevelParameter";
		}
		public Class getPanelClass()
		{
				return null;
		}
		public String getPrettyName()
		{
				return "DeclareTwoLevelParameter";
		}
		public ImageIcon getIcon()
		{
				return icon;
		}

		public Keyword getConstraintKeyword() {
				PersistibleList keywords = getKeywords();
				if ( keywords instanceof DefaultPersistibleList 
						 && ((DefaultPersistibleList)keywords).size() > 0 ) {
						return((Keyword)((DefaultPersistibleList)keywords).firstElement()); 
				}
				return null;
		}
		// Implement SingleParameter
		
		public  ParameterType getParameterType(){
				return ParameterType.DECLARE_TWO_LEVEL_PARAMETER;
		}
		public void initSingleParams() {
				characteristicParam = 
						new DefaultSingleParameter("Characteristic", 
																			 this, 
																			 "TwoLevel.Characteristic");
				startLevelParam = 
						new DefaultSingleParameter("From Level", 
																			 this, 
																			 "TwoLevel.StartLevel");
				endLevelParam = 
						new DefaultSingleParameter("To Level", 
																			 this, 
																			 "TwoLevel.EndLevel");
				singleParameterList.add(characteristicParam);
				singleParameterList.add(startLevelParam);
				singleParameterList.add(endLevelParam);	 
		}
		public void addSingleParameter(SingleParameter sp) {
				System.out.println("addSingleParameter " + sp);
				if(sp.getSingleParameterID().equals("TwoLevel.Characteristic"))
						{
								singleParameterList.replace(characteristicParam, sp);
								characteristicParam = (DefaultSingleParameter) sp;
						}
				else if(sp.getSingleParameterID().equals("TwoLevel.StartLevel"))
						{
								singleParameterList.replace(startLevelParam, sp);
								startLevelParam = (DefaultSingleParameter) sp;
						}
				else if(sp.getSingleParameterID().equals("TwoLevel.EndLevel"))
						{
								singleParameterList.replace(endLevelParam, sp);
								endLevelParam= (DefaultSingleParameter) sp;
						}
		}
		  
		 public String getID(){
				 if(idString == null)
						 idString = getGUID().toString() + getSingleParameterID();
				 return(idString);
		 }
		 public String getSingleParameterID() {
				 return(singleParameterID);
		 }
		 public void setSingleParameterID(String id) {
				 singleParameterID = id;
		 }
		public String getDefaultName(){ return defaultDisplayName; }
		 
		 public String getCodeValue(){
				 return "getCodeValue"; //toString();
		 }
		 public String getDisplayValue(){ return toString();
		 }
		 public void setDisplayValue(Object value){}

		 public void setDisplayName(String displayName) {
				 this.displayName = displayName;
				 if ( this instanceof Persistible) {
						 update();
				 }
		 }
		 public String getDisplayName() {
				 if ( displayName == null ) 
						 return defaultDisplayName;
				 else
						 return displayName;
		 }
		 public void setStartLevelParam(String displayName) {
				 this.startLevelParam.setDisplayName(displayName);
		 }
		 public String getStartLevelParam() {
				 if ( startLevelParam != null ) 
						 return startLevelParam.getDisplayName();
				 return "Null default single param";
		 }
		 public void setEndLevelParam(String displayName) {
				 this.endLevelParam.setDisplayName(displayName);
		 }
		 public String getEndLevelParam() {
				 if ( endLevelParam != null ) 
						 return endLevelParam.getDisplayName();
				 return "Null default single param";
		 }
		 public void setCharacteristicParam(String displayName) {
				 this.characteristicParam.setDisplayName(displayName);
			
		 }
		 public String getCharacteristicParam() {
				 if ( characteristicParam != null ) 
						 return characteristicParam.getDisplayName();
				 return "Null default single param";

		 }
		 protected void setDefaultName(String defaultDisplayName) {
				 this.defaultDisplayName = defaultDisplayName;
		 }

		 public EditorPanel getEditorPanel()
		 {
				 return(new ParameterEditor());	
		 }

		 public EditorPanel getEditorPanelWithInstance()
		 { 
				 EditorPanel ep = new ParameterEditor();
				 ep.edit(this);
				 return(ep);
		 }
	public EditorPanel getParameterEditorPanel()
	{
		return(new ParameterEditor());	
	}
	public EditorPanel getParameterEditorPanelWithInstance()
	{
			System.out.println("DeclareTwoLevelParameter");
			
		EditorPanel ep = new DeclareTwoLevelParameterEditor();
		//ep.setPreferredSize(new Dimension(500,500));
		ep.edit(this);
		return(ep);
	}

	
}
