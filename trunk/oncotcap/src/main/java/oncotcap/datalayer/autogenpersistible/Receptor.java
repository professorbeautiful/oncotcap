package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class Receptor extends AutoGenPersistibleWithKeywords 
 {
	private DefaultPersistibleList bindsTo;
	private DefaultPersistibleList upregulates;
	private DefaultPersistibleList isCodedBy;
	private DefaultPersistibleList downregulates;
	private DefaultPersistibleList participatesIn;
	private String name;
	private DefaultPersistibleList sentencesAboutThis;
	private DefaultPersistibleList activates;
	private DefaultPersistibleList deactivates;


public Receptor() {
init();
}


public Receptor(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setBindsTo", DefaultPersistibleList.class);
	setMethodMap.put("bindsTo", setter);
	getter = getGetter("getBindsTo");
	getMethodMap.put("bindsTo", getter);
	setter = getSetter("setUpregulates", DefaultPersistibleList.class);
	setMethodMap.put("upregulates", setter);
	getter = getGetter("getUpregulates");
	getMethodMap.put("upregulates", getter);
	setter = getSetter("setIsCodedBy", DefaultPersistibleList.class);
	setMethodMap.put("isCodedBy", setter);
	getter = getGetter("getIsCodedBy");
	getMethodMap.put("isCodedBy", getter);
	setter = getSetter("setDownregulates", DefaultPersistibleList.class);
	setMethodMap.put("downregulates", setter);
	getter = getGetter("getDownregulates");
	getMethodMap.put("downregulates", getter);
	setter = getSetter("setParticipatesIn", DefaultPersistibleList.class);
	setMethodMap.put("participatesIn", setter);
	getter = getGetter("getParticipatesIn");
	getMethodMap.put("participatesIn", getter);
	setter = getSetter("setName", String.class);
	setMethodMap.put("name", setter);
	getter = getGetter("getName");
	getMethodMap.put("name", getter);
	setter = getSetter("setSentencesAboutThis", DefaultPersistibleList.class);
	setMethodMap.put("sentencesAboutThis", setter);
	getter = getGetter("getSentencesAboutThis");
	getMethodMap.put("sentencesAboutThis", getter);
	setter = getSetter("setActivates", DefaultPersistibleList.class);
	setMethodMap.put("activates", setter);
	getter = getGetter("getActivates");
	getMethodMap.put("activates", getter);
	setter = getSetter("setDeactivates", DefaultPersistibleList.class);
	setMethodMap.put("deactivates", setter);
	getter = getGetter("getDeactivates");
	getMethodMap.put("deactivates", getter);
}

	public DefaultPersistibleList getBindsTo(){
		return bindsTo;
	}
	public DefaultPersistibleList getUpregulates(){
		return upregulates;
	}
	public DefaultPersistibleList getIsCodedBy(){
		return isCodedBy;
	}
	public DefaultPersistibleList getDownregulates(){
		return downregulates;
	}
	public DefaultPersistibleList getParticipatesIn(){
		return participatesIn;
	}
	public String getName(){
		return name;
	}
	public DefaultPersistibleList getSentencesAboutThis(){
		return sentencesAboutThis;
	}
	public DefaultPersistibleList getActivates(){
		return activates;
	}
	public DefaultPersistibleList getDeactivates(){
		return deactivates;
	}
	public void setBindsTo(java.util.Collection  var ){
		if ( bindsTo== null)
			bindsTo = new DefaultPersistibleList();
		bindsTo.set(var);
	}

	public void setUpregulates(java.util.Collection  var ){
		if ( upregulates== null)
			upregulates = new DefaultPersistibleList();
		upregulates.set(var);
	}

	public void setIsCodedBy(java.util.Collection  var ){
		if ( isCodedBy== null)
			isCodedBy = new DefaultPersistibleList();
		isCodedBy.set(var);
	}

	public void setDownregulates(java.util.Collection  var ){
		if ( downregulates== null)
			downregulates = new DefaultPersistibleList();
		downregulates.set(var);
	}

	public void setParticipatesIn(java.util.Collection  var ){
		if ( participatesIn== null)
			participatesIn = new DefaultPersistibleList();
		participatesIn.set(var);
	}

	public void setName(String var ){
		name = var;
	}

	public void setSentencesAboutThis(java.util.Collection  var ){
		if ( sentencesAboutThis== null)
			sentencesAboutThis = new DefaultPersistibleList();
		sentencesAboutThis.set(var);
	}

	public void setActivates(java.util.Collection  var ){
		if ( activates== null)
			activates = new DefaultPersistibleList();
		activates.set(var);
	}

	public void setDeactivates(java.util.Collection  var ){
		if ( deactivates== null)
			deactivates = new DefaultPersistibleList();
		deactivates.set(var);
	}

	public String toString() {
		return name;
	}
	public Class getPanelClass()
	{
		return ReceptorPanel.class;
	}
	public String getPrettyName()
	{
		return "Receptor";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
