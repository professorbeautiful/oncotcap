package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class SimulationEngine extends AutoGenPersistibleWithKeywords 
 {
	private String name;


public SimulationEngine() {
init();
}


public SimulationEngine(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setName", String.class);
	setMethodMap.put("name", setter);
	getter = getGetter("getName");
	getMethodMap.put("name", getter);
}

	public String getName(){
		return name;
	}
	public void setName(String var ){
		name = var;
	}

	public String toString() {
		return name;
	}
	public Class getPanelClass()
	{
		return SimulationEnginePanel.class;
	}
	public String getPrettyName()
	{
		return "SimulationEngine";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
