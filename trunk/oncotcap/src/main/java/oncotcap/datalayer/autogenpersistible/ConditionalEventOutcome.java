package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class ConditionalEventOutcome extends ConditionalOutcome 
 {
	private EventDeclaration outcome;
	private StateMatrixRow stateMatrixRow;
	private String instanceUsabilityStatus;
	private Boolean isDefaultValue;
	private Integer versionNumber;
	private Persistible conditionValue;


public ConditionalEventOutcome() {
init();
}


public ConditionalEventOutcome(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setOutcome", EventDeclaration.class);
	setMethodMap.put("outcome", setter);
	getter = getGetter("getOutcome");
	getMethodMap.put("outcome", getter);
	setter = getSetter("setStateMatrixRow", StateMatrixRow.class);
	setMethodMap.put("stateMatrixRow", setter);
	getter = getGetter("getStateMatrixRow");
	getMethodMap.put("stateMatrixRow", getter);
	setter = getSetter("setInstanceUsabilityStatus", String.class);
	setMethodMap.put("instanceUsabilityStatus", setter);
	getter = getGetter("getInstanceUsabilityStatus");
	getMethodMap.put("instanceUsabilityStatus", getter);
	setter = getSetter("setIsDefaultValue", Boolean.class);
	setMethodMap.put("isDefaultValue", setter);
	getter = getGetter("getIsDefaultValue");
	getMethodMap.put("isDefaultValue", getter);
	setter = getSetter("setVersionNumber", Integer.class);
	setMethodMap.put("versionNumber", setter);
	getter = getGetter("getVersionNumber");
	getMethodMap.put("versionNumber", getter);
	setter = getSetter("setConditionValue", Persistible.class);
	setMethodMap.put("conditionValue", setter);
	getter = getGetter("getConditionValue");
	getMethodMap.put("conditionValue", getter);
}

	public EventDeclaration getOutcome(){
		return outcome;
	}
	public StateMatrixRow getStateMatrixRow(){
		return stateMatrixRow;
	}
	public String getInstanceUsabilityStatus(){
		return instanceUsabilityStatus;
	}
	public Boolean getIsDefaultValue(){
		return isDefaultValue;
	}
	public Integer getVersionNumber(){
		return versionNumber;
	}
	public Persistible getConditionValue(){
		return conditionValue;
	}
	public void setOutcome(EventDeclaration var ){
		outcome = var;
	}

	public void setStateMatrixRow(StateMatrixRow var ){
		stateMatrixRow = var;
	}

	public void setInstanceUsabilityStatus(String var ){
		instanceUsabilityStatus = var;
	}

	public void setIsDefaultValue(Boolean var ){
		isDefaultValue = var;
	}

	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}

	public void setConditionValue(Persistible var ){
		conditionValue = var;
	}

	public String toString() {
			return "Outcome " + outcome + " " 
					+ conditionValue  + " is default " + isDefaultValue;
	}
	public Class getPanelClass()
	{
		return ConditionalEventOutcomePanel.class;
	}
	public String getPrettyName()
	{
		return "ConditionalEventOutcome";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
