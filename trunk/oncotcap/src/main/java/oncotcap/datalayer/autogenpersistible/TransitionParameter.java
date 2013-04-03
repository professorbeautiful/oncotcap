package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;
import oncotcap.display.editor.persistibleeditorpanel.EditorPanel;


public class TransitionParameter extends AutogenParameter
 {
	private DefaultPersistibleList valueMapEntriesContainingMe;
	private String instanceUsabilityStatus;
	private DefaultPersistibleList variableDefinitions;
	private Integer versionNumber;


public TransitionParameter() {
init();
}


public TransitionParameter(oncotcap.util.GUID guid) {
	super(guid);
	init();
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
		return name;
	}
	public Class getPanelClass()
	{
		return TransitionParameterPanel.class;
	}
	public String getPrettyName()
	{
		return "TransitionParameter";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
		 // AUTOGEN PARAMETER
		 public EditorPanel getParameterEditorPanel(){
				 return new TransitionParameterPanel();
		 }
		 public EditorPanel getParameterEditorPanelWithInstance()
		 {
				 EditorPanel ep = new TransitionParameterPanel();
				 ep.edit(this);
				 return(ep);
		 }
		 public void initSingleParams()
		 {
				 // 	setDefaultName("Positive Value");
				 // 		setSingleParameterID("Positive.Float");
				 // 		singleParameterList.add(this);
		 }
		 
		 public  ParameterType getParameterType(){
				 return null;
		 }
}
