package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import java.lang.reflect.*;
import java.util.Collection;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.util.CollectionHelper;

public class FunctionParameter extends AutogenParameter 
 {
	private String instanceUsabilityStatus;
	private Integer versionNumber;
	private DefaultPersistibleList valueMapEntriesContainingMe;
	private DefaultPersistibleList inputs;
	private DefaultPersistibleList variableDefinitions;
	private DefaultPersistibleList outputs;
	private DefaultPersistibleList function;


public FunctionParameter() {
init();
}


public FunctionParameter(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setKeywords", DefaultPersistibleList.class);
	setMethodMap.put("keywords", setter);
	getter = getGetter("getKeywords");
	getMethodMap.put("keywords", getter);
	setter = getSetter("setInstanceUsabilityStatus", String.class);
	setMethodMap.put("instanceUsabilityStatus", setter);
	getter = getGetter("getInstanceUsabilityStatus");
	getMethodMap.put("instanceUsabilityStatus", getter);
	setter = getSetter("setVersionNumber", Integer.class);
	setMethodMap.put("versionNumber", setter);
	getter = getGetter("getVersionNumber");
	getMethodMap.put("versionNumber", getter);
	setter = getSetter("setValueMapEntriesContainingMe", DefaultPersistibleList.class);
	setMethodMap.put("valueMapEntriesContainingMe", setter);
	getter = getGetter("getValueMapEntriesContainingMe");
	getMethodMap.put("valueMapEntriesContainingMe", getter);
	setter = getSetter("setInputs", DefaultPersistibleList.class);
	setMethodMap.put("inputs", setter);
	getter = getGetter("getInputs");
	getMethodMap.put("inputs", getter);
	setter = getSetter("setVariableDefinitions", DefaultPersistibleList.class);
	setMethodMap.put("variableDefinitions", setter);
	getter = getGetter("getVariableDefinitions");
	getMethodMap.put("variableDefinitions", getter);
	setter = getSetter("setOutputs", DefaultPersistibleList.class);
	setMethodMap.put("outputs", setter);
	getter = getGetter("getOutputs");
	getMethodMap.put("outputs", getter);
	setter = getSetter("setFunction", DefaultPersistibleList.class);
	setMethodMap.put("function", setter);
	getter = getGetter("getFunction");
	getMethodMap.put("function", getter);
}

	public String getInstanceUsabilityStatus(){
		return instanceUsabilityStatus;
	}
	public Integer getVersionNumber(){
		return versionNumber;
	}
	public DefaultPersistibleList getValueMapEntriesContainingMe(){
		return valueMapEntriesContainingMe;
	}
	public DefaultPersistibleList getInputs(){
		return inputs;
	}
	public DefaultPersistibleList getVariableDefinitions(){
		return variableDefinitions;
	}
	public DefaultPersistibleList getOutputs(){
		return outputs;
	}
	public DefaultPersistibleList getFunction(){
		return function;
	}
	public void setInstanceUsabilityStatus(String var ){
		instanceUsabilityStatus = var;
	}

	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}

	public void setValueMapEntriesContainingMe(java.util.Collection  var ){
		if ( valueMapEntriesContainingMe== null)
			valueMapEntriesContainingMe = new DefaultPersistibleList();
		valueMapEntriesContainingMe.set(var);
	}

	public void setInputs(java.util.Collection  var ){
		if ( inputs== null)
			inputs = new DefaultPersistibleList();
		inputs.set(var);
	}

	public void setVariableDefinitions(java.util.Collection  var ){
		if ( variableDefinitions== null)
			variableDefinitions = new DefaultPersistibleList();
		variableDefinitions.set(var);
	}

	public void setOutputs(java.util.Collection  var ){
		if ( outputs== null)
			outputs = new DefaultPersistibleList();
		outputs.set(var);
	}

	public void setFunction(java.util.Collection  var ){
		if ( function== null)
			function = new DefaultPersistibleList();
		function.set(var);
	}

	public String toString() {
		return name;
	}
	public Class getPanelClass()
	{
		return FunctionParameterPanel.class;
	}
		 public String getPrettyName() {
				 return "FunctionParameter";
		 }
		 public ImageIcon getIcon()
		 {
				 return icon;
		 }

		 // AUTOGEN PARAAMETER
		 public EditorPanel getParameterEditorPanel(){
				 return new FunctionParameterPanel();
		 }
		 public EditorPanel getParameterEditorPanelWithInstance()
		 {
				 EditorPanel ep = new FunctionParameterPanel();
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
