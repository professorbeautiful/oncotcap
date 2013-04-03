package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class Journal extends AutoGenPersistibleWithKeywords 
 {
	private String journalHomeLink;
	private FullSourceProvider fullSourceProvider;
	private String abbreviation;
	private String name;
	private String jURLabbrev;
	private String sourceLocatorMethod;


public Journal() {
init();
}


public Journal(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setJournalHomeLink", String.class);
	setMethodMap.put("journalHomeLink", setter);
	getter = getGetter("getJournalHomeLink");
	getMethodMap.put("journalHomeLink", getter);
	setter = getSetter("setFullSourceProvider", FullSourceProvider.class);
	setMethodMap.put("fullSourceProvider", setter);
	getter = getGetter("getFullSourceProvider");
	getMethodMap.put("fullSourceProvider", getter);
	setter = getSetter("setAbbreviation", String.class);
	setMethodMap.put("abbreviation", setter);
	getter = getGetter("getAbbreviation");
	getMethodMap.put("abbreviation", getter);
	setter = getSetter("setName", String.class);
	setMethodMap.put("name", setter);
	getter = getGetter("getName");
	getMethodMap.put("name", getter);
	setter = getSetter("setJURLabbrev", String.class);
	setMethodMap.put("jURLabbrev", setter);
	getter = getGetter("getJURLabbrev");
	getMethodMap.put("jURLabbrev", getter);
	setter = getSetter("setSourceLocatorMethod", String.class);
	setMethodMap.put("sourceLocatorMethod", setter);
	getter = getGetter("getSourceLocatorMethod");
	getMethodMap.put("sourceLocatorMethod", getter);
}

	public String getJournalHomeLink(){
		return journalHomeLink;
	}
	public FullSourceProvider getFullSourceProvider(){
		return fullSourceProvider;
	}
	public String getAbbreviation(){
		return abbreviation;
	}
	public String getName(){
		return name;
	}
	public String getJURLabbrev(){
		return jURLabbrev;
	}
	public String getSourceLocatorMethod(){
		return sourceLocatorMethod;
	}
	public void setJournalHomeLink(String var ){
		journalHomeLink = var;
	}

	public void setFullSourceProvider(FullSourceProvider var ){
		fullSourceProvider = var;
	}

	public void setAbbreviation(String var ){
		abbreviation = var;
	}

	public void setName(String var ){
		name = var;
	}

	public void setJURLabbrev(String var ){
		jURLabbrev = var;
	}

	public void setSourceLocatorMethod(String var ){
		sourceLocatorMethod = var;
	}

	public String toString() {
		return abbreviation;
	}
	public Class getPanelClass()
	{
		return JournalPanel.class;
	}
	public String getPrettyName()
	{
		return "Journal";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
