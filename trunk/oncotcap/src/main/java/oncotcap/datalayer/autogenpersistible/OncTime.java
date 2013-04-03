package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class OncTime extends AutoGenPersistibleWithKeywords 
 {
	private String time;
	private Integer versionNumber;
	private Integer timeUnit;


public OncTime() {
init();
}


public OncTime(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setTime", String.class);
	setMethodMap.put("time", setter);
	getter = getGetter("getTime");
	getMethodMap.put("time", getter);
	setter = getSetter("setVersionNumber", Integer.class);
	setMethodMap.put("versionNumber", setter);
	getter = getGetter("getVersionNumber");
	getMethodMap.put("versionNumber", getter);
	setter = getSetter("setTimeUnit", Integer.class);
	setMethodMap.put("timeUnit", setter);
	getter = getGetter("getTimeUnit");
	getMethodMap.put("timeUnit", getter);
}

	public String getTime(){
		return time;
	}
	public Integer getVersionNumber(){
		return versionNumber;
	}
	public Integer getTimeUnit(){
		return timeUnit;
	}
	public void setTime(String var ){
		time = var;
	}

	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}

	public void setTimeUnit(Integer var ){
		timeUnit = var;
	}

	public String toString() {
		return getGUID().toString();
	}
	public Class getPanelClass()
	{
		return OncTimePanel.class;
	}
	public String getPrettyName()
	{
		return "OncTime";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
