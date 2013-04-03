package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class DummyFormClass extends AutoGenPersistibleWithKeywords 
 {
	private DefaultPersistibleList dummyField;


public DummyFormClass() {
init();
}


public DummyFormClass(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setDummyField", DefaultPersistibleList.class);
	setMethodMap.put("dummyField", setter);
	getter = getGetter("getDummyField");
	getMethodMap.put("dummyField", getter);
}

	public DefaultPersistibleList getDummyField(){
		return dummyField;
	}
	public void setDummyField(java.util.Collection  var ){
		if ( dummyField== null)
			dummyField = new DefaultPersistibleList();
		dummyField.set(var);
	}

	public String toString() {
		return getGUID().toString();
	}
	public Class getPanelClass()
	{
		return DummyFormClassPanel.class;
	}
	public String getPrettyName()
	{
		return "DummyFormClass";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
