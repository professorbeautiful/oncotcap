package oncotcap.datalayer.autogenpersistible;

import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class QuantitativeInterpretation extends Interpretation 
 {
	private DefaultPersistibleList encodingsImplementingMe;
	private Integer versionNumber;
	private DefaultPersistibleList sourceNuggets;
	private String sentenceText;
	private String rCode;
	

public QuantitativeInterpretation() {
 super.init();
 init();
}


public QuantitativeInterpretation(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
protected void init() {
	Method setter = null;
	Method getter = null;

	setter = getSetter("setRCode", String.class);
	setMethodMap.put("rCode", setter);
	getter = getGetter("getRCode");
	getMethodMap.put("rCode", getter);

}

	public String getRCode()
	{
		return(rCode);
	}
	public void setRCode(String code)
	{
		rCode = code;
	}

	public Class getPanelClass()
	{
		return QuantitativeInterpretationPanel.class;
	}

}
