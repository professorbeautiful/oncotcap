package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class InputWindow extends AutoGenPersistibleWithKeywords 
 {
	private DefaultPersistibleList instantiationParameters;
	private String name;


public InputWindow() {
init();
}


public InputWindow(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setInstantiationParameters", DefaultPersistibleList.class);
	setMethodMap.put("instantiationParameters", setter);
	getter = getGetter("getInstantiationParameters");
	getMethodMap.put("instantiationParameters", getter);
	setter = getSetter("setName", String.class);
	setMethodMap.put("name", setter);
	getter = getGetter("getName");
	getMethodMap.put("name", getter);
}

	public DefaultPersistibleList getInstantiationParameters(){
		return instantiationParameters;
	}
	public String getName(){
		return name;
	}
	public void setInstantiationParameters(java.util.Collection  var ){
		if ( instantiationParameters== null)
			instantiationParameters = new DefaultPersistibleList();
		instantiationParameters.set(var);
	}

	public void setName(String var ){
		name = var;
	}

	public String toString() {
		return getGUID().toString();
	}
	public Class getPanelClass()
	{
		return InputWindowPanel.class;
	}
	public String getPrettyName()
	{
		return "InputWindow";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
