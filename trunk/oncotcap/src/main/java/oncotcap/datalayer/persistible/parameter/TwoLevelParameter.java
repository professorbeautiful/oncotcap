package oncotcap.datalayer.persistible.parameter;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;
import java.util.Collection;

import oncotcap.datalayer.*;
import oncotcap.engine.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.persistibleeditorpanel.*;

import oncotcap.display.editor.autogeneditorpanel.*;


public class TwoLevelParameter extends DeclareTwoLevelParameter
		implements HyperTextMenuListener {
		private DefaultPersistibleList valueMapEntriesContainingMe;
		private String instanceUsabilityStatus;
		private DefaultPersistibleList variableDefinitions;
		private Integer versionNumber;
		private EnumLevel endLevel;
		private EnumLevel startLevel;
		private DeclareTwoLevelParameter baseParameter;

		public TwoLevelParameter() {
				System.out.println("initialize TwoLevelParameter");
				initSingleParams();
				init();
		}
		
		
		public TwoLevelParameter(oncotcap.util.GUID guid) {
				super(guid);
				initSingleParams();
				init();
		}
		
		 public TwoLevelParameter(DeclareTwoLevelParameter declareParam) {
				 super();
				 baseParameter = declareParam;
				 initSingleParams();
				 init();
				 // Set the keyword constraint
				 if ( declareParam != null 
							&& declareParam.getKeywords() != null 
							&& declareParam.getKeywords()  instanceof DefaultPersistibleList
							&& ((DefaultPersistibleList)declareParam.getKeywords()).size() > 0 ) {
						 System.out.println("TwoLevelParameter set constraint " 
																+ ((DefaultPersistibleList)declareParam.getKeywords()).firstElement());
						 
						 setKeywords((Keyword)((DefaultPersistibleList)declareParam.getKeywords()).firstElement());
						 characteristicParam.setDisplayName(declareParam.getCharacteristicParam());
						 startLevelParam.setDisplayName(declareParam.getStartLevelParam());
						 endLevelParam.setDisplayName(declareParam.getEndLevelParam());
				 }
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
				 setter = getSetter("setEndLevel", EnumLevel.class);
				 setMethodMap.put("endLevel", setter);
				 getter = getGetter("getEndLevel");
				 getMethodMap.put("endLevel", getter);
				 setter = getSetter("setStartLevel", EnumLevel.class);
				 setMethodMap.put("startLevel", setter);
				 getter = getGetter("getStartLevel");
				 getMethodMap.put("startLevel", getter);
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
		public EnumDefinition getEnumDefinition() {
				if ( variableDefinitions != null &&
						 variableDefinitions.size() > 0 ) {
						return (EnumDefinition)variableDefinitions.firstElement();
				}
				return null;
		}
	public Integer getVersionNumber(){
		return versionNumber;
	}
	public EnumLevel getEndLevel(){
		return endLevel;
	}
	public EnumLevel getStartLevel(){
		return startLevel;
	}

	public DeclareTwoLevelParameter getBaseParameter(){
		return baseParameter;
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
		if ( variableDefinitions != null 
				 && variableDefinitions.size() > 0 
				 && variableDefinitions.elementAt(0) instanceof EnumDefinition ) {
				// System.out.println("characteristicParam 1 : " +
// 													 characteristicParam + " -- " + ((EnumDefinition)variableDefinitions.elementAt(0)).toString());
				characteristicParam.setDisplayValue(((EnumDefinition)variableDefinitions.elementAt(0)).getName());
		}
	}

	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}

	public void setEndLevel(EnumLevel var ){
			if ( var != null ) 
					endLevelParam.setDisplayValue(var.toString());
			endLevel = var;
	}

	public void setStartLevel(EnumLevel var ){
			if ( var != null ) 
					startLevelParam.setDisplayValue(var.toString());
			startLevel = var;
	}
	public void setBaseParameter(DeclareTwoLevelParameter param ){
			baseParameter = param;
	}

	public String toString() {
		return variableDefinitions + " " + startLevel + 
				" " + endLevel + " labels " + getKeywords();
	}
	public Class getPanelClass()
	{
		return TwoLevelParameterPanel.class;
	}
	public String getPrettyName()
	{
		return "TwoLevelParameter";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}

// 		// Implement SingleParameter
		
// 		public  ParameterType getParameterType(){
// 				System.out.println("TwoLevelParameter.getParameterType " 
// 													 + ParameterType.TWO_LEVEL_PARAMETER);
// 				return ParameterType.TWO_LEVEL_PARAMETER;
// 		}



// 		public String getDefaultName(){ return defaultDisplayName; }
		 
// 		 public String getCodeValue(){
// 				 return "getCodeValue"; //toString();
// 		 }
// 		 public String getDisplayValue(){ return toString();
// 		 }
// 		 public void setDisplayValue(Object value){}

// 		 public void setDisplayName(String displayName) {
// 				 this.displayName = displayName;
// 				 if ( this instanceof Persistible) {
// 						 update();
// 				 }
// 		 }
// 		 public String getDisplayName() {
// 				 if ( displayName == null ) 
// 						 return defaultDisplayName;
// 				 else
// 						 return displayName;
// 		 }
// 		 protected void setDefaultName(String defaultDisplayName) {
// 				 this.defaultDisplayName = defaultDisplayName;
// 		 }

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
		return(new TwoLevelParameterEditor());	
	}
	public EditorPanel getParameterEditorPanelWithInstance()
	{
		EditorPanel ep = new TwoLevelParameterEditor();
		//ep.setPreferredSize(new Dimension(500,500));
		ep.edit(this);
		return(ep);
	}

		 public void hyperTextMenuChanged(HyperTextMenuEvent e) {
				 if (e.getSelectedItem() instanceof LevelAndList) {
						 EnumLevel level = ((LevelAndList)e.getSelectedItem()).getLevel();
						 EnumLevelList levelList =
								 ((LevelAndList)e.getSelectedItem()).getLevelList();
						 level.setLevelList(levelList);
						 // Restrict the other level menu to the same level lis
						 //setRightHandSide(level);
				 }
		 }

		// Override  to support code provider 
		public Collection<InstructionProvider> getAdditionalProviders() {
				return(getVariableDefinitions());
		}

		// SingleParameter stuff
		public void initSingleParams() {
				characteristicParam = 
						new CharacteristicSingleParameter("Characteristic", 
																			 this, 
																			 "TwoLevel.Characteristic");
				startLevelParam = 
						new LevelSingleParameter("From Level", 
																			 this, 
																			 "TwoLevel.StartLevel");
				endLevelParam = 
						new LevelSingleParameter("To Level", 
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
		  

		// Create a single parameter to return the proper code for this 
		// parameter - not sure if there is 1,2 or three different code values that 
		// need to be returned - need a use case


}
