package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;
import java.util.Vector;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class ConditionalDiscreteStateFunction extends AutoGenPersistibleWithKeywords 
 {
	private String instanceUsabilityStatus;
	private Persistible conditionValue;
	private FunctionParameter functionParameter; // containingMe
	private Integer versionNumber;
	private DefaultPersistibleList outcomes;


public ConditionalDiscreteStateFunction() {
init();
}


public ConditionalDiscreteStateFunction(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setInstanceUsabilityStatus", String.class);
	setMethodMap.put("instanceUsabilityStatus", setter);
	getter = getGetter("getInstanceUsabilityStatus");
	getMethodMap.put("instanceUsabilityStatus", getter);
	setter = getSetter("setConditionValue", Persistible.class);
	setMethodMap.put("conditionValue", setter);
	getter = getGetter("getConditionValue");
	getMethodMap.put("conditionValue", getter);
	setter = getSetter("setFunctionParameter", FunctionParameter.class);
	setMethodMap.put("functionParameter", setter);
	getter = getGetter("getFunctionParameter");
	getMethodMap.put("functionParameter", getter);
	setter = getSetter("setVersionNumber", Integer.class);
	setMethodMap.put("versionNumber", setter);
	getter = getGetter("getVersionNumber");
	getMethodMap.put("versionNumber", getter);
	setter = getSetter("setOutcomes", DefaultPersistibleList.class);
	setMethodMap.put("outcomes", setter);
	getter = getGetter("getOutcomes");
	getMethodMap.put("outcomes", getter);
}

	public String getInstanceUsabilityStatus(){
		return instanceUsabilityStatus;
	}
	public Persistible getConditionValue(){
		return conditionValue;
	}
	public FunctionParameter getFunctionParameter(){
		return functionParameter;
	}
	public Integer getVersionNumber(){
		return versionNumber;
	}
	public DefaultPersistibleList getOutcomes(){
		return outcomes;
	}
	public void setInstanceUsabilityStatus(String var ){
		instanceUsabilityStatus = var;
	}

	public void setConditionValue(Persistible var ){
		conditionValue = var;
	}

	public void setFunctionParameter(FunctionParameter var ){

		functionParameter = var;
	}

	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}

	public void setOutcomes(java.util.Collection  var ){
			if ( outcomes == null) {
					outcomes = new DefaultPersistibleList();
					
			}
			if ( outcomes != var)
					outcomes.set(var);
	}

	public String toString() {
			StringBuilder str = new StringBuilder();
			str.append("ConditionalDiscreteStateFunction ");
			if ( outcomes != null ) {
					str.append("outcomes: ");
					str.append(getOutcomeValues().toString());
			}
			str.append("condition ");
			str.append(conditionValue);

			str.append("stateMatrix is ");
			if ( functionParameter != null  
				&& functionParameter instanceof ConditionalTableParameter) 
				str.append(((ConditionalTableParameter)functionParameter).getStateMatrix().toString());
			return str.toString();
	}
	public Class getPanelClass() 
	{
		return ConditionalDiscreteStateFunctionPanel.class;
	}
 	public String getPrettyName()
	{
		return "ConditionalDiscreteStateFunction";
	}
 	public StateMatrix getStateMatrix() {
 		StateMatrix stateMatrix = null;
 		if ( functionParameter != null  
 				&& functionParameter instanceof ConditionalTableParameter) 
 			stateMatrix = ((ConditionalTableParameter)functionParameter).getStateMatrix();
 		return stateMatrix;
 	}
 	public ImageIcon getIcon()
	{
		return icon;
	}

		 // Convenience Method
		 public Vector getOutcomeValues() {
				 Vector outcomeValues = new Vector();
				 if ( outcomes != null ) {
						 int i = 0;
						 while (i < outcomes.size() ) {
								 ConditionalOutcome condOutcome = 
										 (ConditionalOutcome)outcomes.elementAt(i);
								 outcomeValues.addElement(condOutcome.getOutcome());
								 i++;
						}
				 }
				 return outcomeValues;
		 }

		 public Vector getHeadings() {
				 if (getStateMatrix() != null ) 
						 return getStateMatrix().getMatrixHeading();
				 return null;
		 }
		 

		 public Vector getFunctionValues(int row) {
				 // Get a row of data with the enum definition
				 // Create a temporary enum definition so the initial value 
				 // can be changed to be the level that the row has 
				 Vector enumDefinitions = getHeadings();
				 if ( outcomes != null && outcomes.size() > row ) {
						 ConditionalOutcome condOutcome = 
								 (ConditionalOutcome)outcomes.elementAt(row);
						 StateMatrixRow stateRow = condOutcome.getStateMatrixRow();
						 stateRow.getRowColumns();
				 }
				 return new Vector();
		 }
}
