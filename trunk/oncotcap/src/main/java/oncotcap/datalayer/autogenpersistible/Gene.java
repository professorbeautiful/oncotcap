package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class Gene extends AutoGenPersistibleWithKeywords 
 {
	private DefaultPersistibleList isRegulatedBy;
	private DefaultPersistibleList bindsTo;
	private DefaultPersistibleList upregulates;
	private DefaultPersistibleList codesFor;
	private DefaultPersistibleList downregulates;
	private DefaultPersistibleList participatesIn;
	private String name;
	private DefaultPersistibleList sentencesAboutThis;
	private DefaultPersistibleList activates;
	private DefaultPersistibleList deactivates;


public Gene() {
init();
}


public Gene(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setIsRegulatedBy", DefaultPersistibleList.class);
	setMethodMap.put("isRegulatedBy", setter);
	getter = getGetter("getIsRegulatedBy");
	getMethodMap.put("isRegulatedBy", getter);
	setter = getSetter("setBindsTo", DefaultPersistibleList.class);
	setMethodMap.put("bindsTo", setter);
	getter = getGetter("getBindsTo");
	getMethodMap.put("bindsTo", getter);
	setter = getSetter("setUpregulates", DefaultPersistibleList.class);
	setMethodMap.put("upregulates", setter);
	getter = getGetter("getUpregulates");
	getMethodMap.put("upregulates", getter);
	setter = getSetter("setCodesFor", DefaultPersistibleList.class);
	setMethodMap.put("codesFor", setter);
	getter = getGetter("getCodesFor");
	getMethodMap.put("codesFor", getter);
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

	public DefaultPersistibleList getIsRegulatedBy(){
		return isRegulatedBy;
	}
	public DefaultPersistibleList getBindsTo(){
		return bindsTo;
	}
	public DefaultPersistibleList getUpregulates(){
		return upregulates;
	}
	public DefaultPersistibleList getCodesFor(){
		return codesFor;
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
	public void setIsRegulatedBy(java.util.Collection  var ){
		if ( isRegulatedBy== null)
			isRegulatedBy = new DefaultPersistibleList();
		isRegulatedBy.set(var);
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

	public void setCodesFor(java.util.Collection  var ){
		if ( codesFor== null)
			codesFor = new DefaultPersistibleList();
		codesFor.set(var);
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
		return GenePanel.class;
	}
	public String getPrettyName()
	{
		return "Gene";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
