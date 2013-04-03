package oncotcap.datalayer.autogenpersistible;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import javax.swing.*;
import java.lang.reflect.*;
import oncotcap.display.editor.autogeneditorpanel.*;

public class Dataset extends InformationSource
 implements oncotcap.display.browser.TreeBrowserNode 
 {
	private DefaultPersistibleList quotesInMe;
	private String creationTime;
	private Integer sampleSize;
	private String modificationTime;
	private String modifier;
	private String creator;
	private String dataSource;


	public Dataset(oncotcap.util.GUID guid){
		super(guid);
	Method setter = null;
	Method getter = null;
	setter = getSetter("setQuotesInMe", DefaultPersistibleList.class);
	setMethodMap.put("quotesInMe", setter);
	getter = getGetter("getQuotesInMe");
	getMethodMap.put("quotesInMe", getter);
	setter = getSetter("setCreationTime", String.class);
	setMethodMap.put("creationTime", setter);
	getter = getGetter("getCreationTime");
	getMethodMap.put("creationTime", getter);
	setter = getSetter("setSampleSize", Integer.class);
	setMethodMap.put("sampleSize", setter);
	getter = getGetter("getSampleSize");
	getMethodMap.put("sampleSize", getter);
	setter = getSetter("setModificationTime", String.class);
	setMethodMap.put("modificationTime", setter);
	getter = getGetter("getModificationTime");
	getMethodMap.put("modificationTime", getter);
	setter = getSetter("setModifier", String.class);
	setMethodMap.put("modifier", setter);
	getter = getGetter("getModifier");
	getMethodMap.put("modifier", getter);
	setter = getSetter("setCreator", String.class);
	setMethodMap.put("creator", setter);
	getter = getGetter("getCreator");
	getMethodMap.put("creator", getter);
	setter = getSetter("setDataSource", String.class);
	setMethodMap.put("dataSource", setter);
	getter = getGetter("getDataSource");
	getMethodMap.put("dataSource", getter);
}

public Dataset() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setQuotesInMe", DefaultPersistibleList.class);
	setMethodMap.put("quotesInMe", setter);
	getter = getGetter("getQuotesInMe");
	getMethodMap.put("quotesInMe", getter);
	setter = getSetter("setCreationTime", String.class);
	setMethodMap.put("creationTime", setter);
	getter = getGetter("getCreationTime");
	getMethodMap.put("creationTime", getter);
	setter = getSetter("setSampleSize", Integer.class);
	setMethodMap.put("sampleSize", setter);
	getter = getGetter("getSampleSize");
	getMethodMap.put("sampleSize", getter);
	setter = getSetter("setModificationTime", String.class);
	setMethodMap.put("modificationTime", setter);
	getter = getGetter("getModificationTime");
	getMethodMap.put("modificationTime", getter);
	setter = getSetter("setModifier", String.class);
	setMethodMap.put("modifier", setter);
	getter = getGetter("getModifier");
	getMethodMap.put("modifier", getter);
	setter = getSetter("setCreator", String.class);
	setMethodMap.put("creator", setter);
	getter = getGetter("getCreator");
	getMethodMap.put("creator", getter);
	setter = getSetter("setDataSource", String.class);
	setMethodMap.put("dataSource", setter);
	getter = getGetter("getDataSource");
	getMethodMap.put("dataSource", getter);
}

	public DefaultPersistibleList getQuotesInMe(){
		return quotesInMe;
	}
	public String getCreationTime(){
		return creationTime;
	}
	public Integer getSampleSize(){
		return sampleSize;
	}
	public String getModificationTime(){
		return modificationTime;
	}
	public String getModifier(){
		return modifier;
	}
	public String getCreator(){
		return creator;
	}
	//public String getDataSource(){
	//	return dataSource;
	//}
	public void setQuotesInMe(java.util.Collection  var ){
		if ( quotesInMe== null)
			quotesInMe = new DefaultPersistibleList();
		quotesInMe.set(var);
	}

	public void setCreationTime(String var ){
		creationTime = var;
	}

	public void setSampleSize(Integer var ){
		sampleSize = var;
	}

	public void setModificationTime(String var ){
		modificationTime = var;
	}

	public void setModifier(String var ){
		modifier = var;
	}

	public void setCreator(String var ){
		creator = var;
	}

	public void setDataSource(String var ){
		dataSource = var;
	}

	public String toString() {
		return getGUID().toString();
	}
	public Class getPanelClass()
	{
		return DatasetPanel.class;
	}
	public String getPrettyName()
	{
		return "Dataset";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
