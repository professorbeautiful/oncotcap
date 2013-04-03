package oncotcap.datalayer.persistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;
import java.util.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class SubModel extends AutoGenPersistibleWithKeywords 
 {
	private DefaultPersistibleList encodingsInMe;
	private Integer versionNumber;
	private DefaultPersistibleList submodelGroupsIJoin;
	private String name;
  private String modelType;

	public ImageIcon icon =	
			oncotcap.util.OncoTcapIcons.getImageIcon("submodel.jpg");


public SubModel() {
init();
}


public SubModel(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setEncodingsInMe", DefaultPersistibleList.class);
	setMethodMap.put("encodingsInMe", setter);
	getter = getGetter("getEncodingsInMe");
	getMethodMap.put("encodingsInMe", getter);
	setter = getSetter("setVersionNumber", Integer.class);
	setMethodMap.put("versionNumber", setter);
	getter = getGetter("getVersionNumber");
	getMethodMap.put("versionNumber", getter);
	setter = getSetter("setSubmodelGroupsIJoin", DefaultPersistibleList.class);
	setMethodMap.put("submodelGroupsIJoin", setter);
	getter = getGetter("getSubmodelGroupsIJoin");
	getMethodMap.put("submodelGroupsIJoin", getter);
	setter = getSetter("setName", String.class);
	setMethodMap.put("name", setter);
	getter = getGetter("getName");
	getMethodMap.put("name", getter);
	setter = getSetter("setKeywords", DefaultPersistibleList.class);
	setMethodMap.put("keywords", setter);
	getter = getGetter("getKeywords");
	getMethodMap.put("keywords", getter);
	setter = getSetter("setModelType", String.class);
	setMethodMap.put("modelType", setter);
	getter = getGetter("getModelType");
	getMethodMap.put("modelType", getter);
}

	public DefaultPersistibleList getEncodingsInMe(){
		return encodingsInMe;
	}
	public Integer getVersionNumber(){
		return versionNumber;
	}
	public DefaultPersistibleList getSubmodelGroupsIJoin(){
		return submodelGroupsIJoin;
	}
	public String getName(){
		return name;
	}
	public String getModelType(){
		return modelType;
	}
	public void setModelType(String var ){
		modelType = var;
	}
	public void setEncodingsInMe(java.util.Collection  var ){
		if ( encodingsInMe== null)
			encodingsInMe = new DefaultPersistibleList();
		encodingsInMe.set(var);
	}

	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}

	public void setSubmodelGroupsIJoin(java.util.Collection  var ){
		if ( submodelGroupsIJoin== null)
			submodelGroupsIJoin = new DefaultPersistibleList();
		submodelGroupsIJoin.set(var);
	}

	public void setName(String var ){
		name = var;
	}

	public String toString() {
		return name;
	}
	public Class getPanelClass()
	{
		return SubModelPanel.class;
	}
	public String getPrettyName()
	{
		return "SubModel";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
	public Collection getStatementBundles()
	{
		if(encodingsInMe != null)
		{
			Encoding enc;
			StatementBundle sb;
			Hashtable bundles = new Hashtable();
			Iterator it = encodingsInMe.iterator();
			Iterator it2;
			while(it.hasNext())
			{
				enc = (Encoding) it.next();
				it2 = enc.getStatementBundles().iterator();
				while(it2.hasNext())
				{
					Object obj = it2.next();
					if(obj instanceof StatementBundle)
					{
						sb = (StatementBundle) obj;
						bundles.put(sb, sb);
					}
					else
					{
						System.out.println("WARNING: ENCODING CONTAINS A NON-STATEMENTBUNDLE");
					}
				}
			}
			return(bundles.values());
		}
		else
			return(null);
	}
}
