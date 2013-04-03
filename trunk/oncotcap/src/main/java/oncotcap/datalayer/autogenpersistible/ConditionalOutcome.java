package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class ConditionalOutcome extends AutoGenPersistibleWithKeywords 
 {
	private Boolean isDefaultValue;
	private String instanceUsabilityStatus;
	private Persistible conditionValue;
	private Integer versionNumber;
	private String outcome;
	private StateMatrixRow stateMatrixRow;


public ConditionalOutcome() {
init();
}


public ConditionalOutcome(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setIsDefaultValue", Boolean.class);
	setMethodMap.put("isDefaultValue", setter);
	getter = getGetter("getIsDefaultValue");
	getMethodMap.put("isDefaultValue", getter);
	setter = getSetter("setInstanceUsabilityStatus", String.class);
	setMethodMap.put("instanceUsabilityStatus", setter);
	getter = getGetter("getInstanceUsabilityStatus");
	getMethodMap.put("instanceUsabilityStatus", getter);
	setter = getSetter("setConditionValue", Persistible.class);
	setMethodMap.put("conditionValue", setter);
	getter = getGetter("getConditionValue");
	getMethodMap.put("conditionValue", getter);
	setter = getSetter("setVersionNumber", Integer.class);
	setMethodMap.put("versionNumber", setter);
	getter = getGetter("getVersionNumber");
	getMethodMap.put("versionNumber", getter);
	setter = getSetter("setOutcome", String.class);
	setMethodMap.put("outcome", setter);
	getter = getGetter("getOutcome");
	getMethodMap.put("outcome", getter);
	setter = getSetter("setStateMatrixRow", StateMatrixRow.class);
	setMethodMap.put("stateMatrixRow", setter);
	getter = getGetter("getStateMatrixRow");
	getMethodMap.put("stateMatrixRow", getter);
}

	public Boolean getIsDefaultValue(){
		return isDefaultValue;
	}
	public String getInstanceUsabilityStatus(){
		return instanceUsabilityStatus;
	}
	public Persistible getConditionValue(){
		return conditionValue;
	}
	public Integer getVersionNumber(){
		return versionNumber;
	}
	public Object getOutcome(){
		return outcome;
	}
	public StateMatrixRow getStateMatrixRow(){
		return stateMatrixRow;
	}
	public void setIsDefaultValue(Boolean var ){
		isDefaultValue = var;
	}

	public void setInstanceUsabilityStatus(String var ){
		instanceUsabilityStatus = var;
	}

	public void setConditionValue(Persistible var ){
		conditionValue = var;
	}

	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}

	public void setOutcome(Object var ){
			if ( var instanceof String) 
					setOutcome((String)var);
	}
	public void setOutcome(String var ){
		outcome = var;
	}

	public void setStateMatrixRow(StateMatrixRow var ){
		stateMatrixRow = var;
	}

	public String toString() {
			if ( outcome != null )
					return outcome.toString();
			return getGUID().toString();
	}
	public Class getPanelClass()
	{
		return ConditionalOutcomePanel.class;
	}
	public String getPrettyName()
	{
		return "ConditionalOutcome";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
