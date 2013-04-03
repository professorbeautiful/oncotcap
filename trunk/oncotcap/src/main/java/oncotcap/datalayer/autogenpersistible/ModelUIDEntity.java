package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class ModelUIDEntity extends AutoGenPersistibleWithKeywords 
 {


public ModelUIDEntity() {
init();
}


public ModelUIDEntity(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
}

	public String toString() {
		return getGUID().toString();
	}
	public Class getPanelClass()
	{
		return ModelUIDEntityPanel.class;
	}
	public String getPrettyName()
	{
		return "ModelUIDEntity";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
