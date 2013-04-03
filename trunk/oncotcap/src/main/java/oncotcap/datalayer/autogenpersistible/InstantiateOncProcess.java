package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class InstantiateOncProcess extends AutoGenPersistibleWithKeywords
 {
	private DefaultPersistibleList initializationList;
	private Integer versionNumber;
	private ProcessDeclaration oncProcess;
	private CodeBundle codeBundlesContainingMe;
	private String name;
	private String enumInitializations;


public InstantiateOncProcess() {
init();
}


public InstantiateOncProcess(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setInitializationList", DefaultPersistibleList.class);
	setMethodMap.put("initializationList", setter);
	getter = getGetter("getInitializationList");
	getMethodMap.put("initializationList", getter);
	setter = getSetter("setVersionNumber", Integer.class);
	setMethodMap.put("versionNumber", setter);
	getter = getGetter("getVersionNumber");
	getMethodMap.put("versionNumber", getter);
	setter = getSetter("setKeywords", DefaultPersistibleList.class);
	setMethodMap.put("keywords", setter);
	getter = getGetter("getKeywords");
	getMethodMap.put("keywords", getter);
	setter = getSetter("setOncProcess", ProcessDeclaration.class);
	setMethodMap.put("oncProcess", setter);
	getter = getGetter("getOncProcess");
	getMethodMap.put("oncProcess", getter);
	setter = getSetter("setCodeBundlesContainingMe", CodeBundle.class);
	setMethodMap.put("codeBundlesContainingMe", setter);
	getter = getGetter("getCodeBundlesContainingMe");
	getMethodMap.put("codeBundlesContainingMe", getter);
	setter = getSetter("setName", String.class);
	setMethodMap.put("name", setter);
	getter = getGetter("getName");
	getMethodMap.put("name", getter);
	setter = getSetter("setEnumInitializations", String.class);
	setMethodMap.put("enumInitializations", setter);
	getter = getGetter("getEnumInitializations");
	getMethodMap.put("enumInitializations", getter);
}

	public DefaultPersistibleList getInitializationList(){
		return initializationList;
	}
	public Integer getVersionNumber(){
		return versionNumber;
	}
	public ProcessDeclaration getOncProcess(){
		return oncProcess;
	}
	public CodeBundle getCodeBundlesContainingMe(){
		return codeBundlesContainingMe;
	}
	public String getName(){
		return name;
	}
	public String getEnumInitializations(){
		return enumInitializations;
	}
	public void setInitializationList(java.util.Collection  var ){
		if ( initializationList== null)
			initializationList = new DefaultPersistibleList();
		initializationList.set(var);
	}

	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}

	public void setOncProcess(ProcessDeclaration var ){
		oncProcess = var;
	}

	public void setCodeBundlesContainingMe(CodeBundle var ){
		codeBundlesContainingMe = var;
	}

	public void setName(String var ){
		name = var;
	}

	public void setEnumInitializations(String var ){
		enumInitializations = var;
	}

	public String toString() {
		return getGUID().toString();
	}
	public Class getPanelClass()
	{
		return InstantiateOncProcessPanel.class;
	}
	public String getPrettyName()
	{
		return "InstantiateOncProcess";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
