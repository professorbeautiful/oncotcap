package oncotcap.datalayer.persistible;

import oncotcap.datalayer.*;
import oncotcap.display.editor.autogeneditorpanel.*;
import oncotcap.datalayer.autogenpersistible.*;
import javax.swing.*;
import java.lang.reflect.*;
import java.util.*;


public class SubModelGroup extends AutoGenPersistibleWithKeywords 
 implements oncotcap.display.browser.TreeBrowserNode 
 {
	private String creationTime;
	private DefaultPersistibleList modelControllersUsingMe;
	private String modificationTime;
	private String modifier;
	private String name;
	private String creator;
	private DefaultPersistibleList submodelsInGroup;
  private String modelType;
	public ImageIcon icon =	
	oncotcap.util.OncoTcapIcons.getImageIcon("submodelgroup.jpg");


	public SubModelGroup(oncotcap.util.GUID guid){
		super(guid);
		initSettersGetters();
	}

	public SubModelGroup() {
		initSettersGetters();
	}
	private void initSettersGetters() {
		Method setter = null;	
		Method getter = null;	
		setter = getSetter("setCreationTime", String.class);
		setMethodMap.put("creationTime", setter);
		getter = getGetter("getCreationTime");
		getMethodMap.put("creationTime", getter);
		setter = getSetter("setModelControllersUsingMe", DefaultPersistibleList.class);
		setMethodMap.put("modelControllersUsingMe", setter);
		getter = getGetter("getModelControllersUsingMe");
		getMethodMap.put("modelControllersUsingMe", getter);
		setter = getSetter("setModificationTime", String.class);
		setMethodMap.put("modificationTime", setter);
		getter = getGetter("getModificationTime");
		getMethodMap.put("modificationTime", getter);
		setter = getSetter("setModifier", String.class);
		setMethodMap.put("modifier", setter);
		getter = getGetter("getModifier");
		getMethodMap.put("modifier", getter);
		setter = getSetter("setName", String.class);
		setMethodMap.put("name", setter);
		getter = getGetter("getName");
		getMethodMap.put("name", getter);
		setter = getSetter("setKeywords", DefaultPersistibleList.class);
		setMethodMap.put("keywords", setter);
		getter = getGetter("getKeywords");
		getMethodMap.put("keywords", getter);
		setter = getSetter("setCreator", String.class);
		setMethodMap.put("creator", setter);
		getter = getGetter("getCreator");
		getMethodMap.put("creator", getter);
		setter = getSetter("setSubmodelsInGroup", DefaultPersistibleList.class);
		setMethodMap.put("submodelsInGroup", setter);
		getter = getGetter("getSubmodelsInGroup");
		getMethodMap.put("submodelsInGroup", getter);
		setter = getSetter("setModelType", String.class);
		setMethodMap.put("modelType", setter);
		getter = getGetter("getModelType");
		getMethodMap.put("modelType", getter);
	}
	public String getCreationTime(){
		return creationTime;
	}
	public String getModelType(){
		return modelType;
	}
	public void setModelType(String var ){
		modelType = var;
	}
	public DefaultPersistibleList getModelControllersUsingMe(){
		return modelControllersUsingMe;
	}
	public String getModificationTime(){
		return modificationTime;
	}
	public String getModifier(){
		return modifier;
	}
	public String getName(){
		return name;
	}
	public String getCreator(){
		return creator;
	}
	public DefaultPersistibleList getSubmodelsInGroup(){
		return submodelsInGroup;
	}
	public void setCreationTime(String var ){
		creationTime = var;
	}

	public void setModelControllersUsingMe(java.util.Collection  var ){
		if ( modelControllersUsingMe== null)
			modelControllersUsingMe = new DefaultPersistibleList();
		modelControllersUsingMe.set(var);
	}

	public void setModificationTime(String var ){
		modificationTime = var;
	}

	public void setModifier(String var ){
		modifier = var;
	}

	public void setName(String var ){
		name = var;
	}

	public void setCreator(String var ){
		creator = var;
	}

	public void setSubmodelsInGroup(java.util.Collection  var ){
		if ( submodelsInGroup== null)
			submodelsInGroup = new DefaultPersistibleList();
		submodelsInGroup.set(var);
	}

	public String toString() {
		return name;
	}
	public Class getPanelClass()
	{
		return SubModelGroupPanel.class;
	}
	public String getPrettyName()
	{
		return "SubModelGroup";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}

	public Collection getStatementBundles()
	{
		if(submodelsInGroup != null)
		{
			SubModel sm;
			StatementBundle sb;
			Hashtable bundles = new Hashtable();
			Iterator it = submodelsInGroup.iterator();
			Iterator it2;
			while(it.hasNext())
			{
				sm = (SubModel) it.next();
				it2 = sm.getStatementBundles().iterator();
				while(it2.hasNext())
				{
					sb = (StatementBundle) it2.next();
					bundles.put(sb, sb);
				}
			}
			return(bundles.values());
		}
		else
			return(null);
	}
}
