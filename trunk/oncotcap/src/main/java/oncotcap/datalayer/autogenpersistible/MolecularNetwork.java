package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class MolecularNetwork extends AutoGenPersistibleWithKeywords 
 {
	private DefaultPersistibleList molecules;
	private String name;
	private DefaultPersistibleList biorelationships;
	private DefaultPersistibleList sentencesAboutThis;


public MolecularNetwork() {
init();
}


public MolecularNetwork(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setMolecules", DefaultPersistibleList.class);
	setMethodMap.put("molecules", setter);
	getter = getGetter("getMolecules");
	getMethodMap.put("molecules", getter);
	setter = getSetter("setName", String.class);
	setMethodMap.put("name", setter);
	getter = getGetter("getName");
	getMethodMap.put("name", getter);
	setter = getSetter("setBiorelationships", DefaultPersistibleList.class);
	setMethodMap.put("biorelationships", setter);
	getter = getGetter("getBiorelationships");
	getMethodMap.put("biorelationships", getter);
	setter = getSetter("setSentencesAboutThis", DefaultPersistibleList.class);
	setMethodMap.put("sentencesAboutThis", setter);
	getter = getGetter("getSentencesAboutThis");
	getMethodMap.put("sentencesAboutThis", getter);
}

	public DefaultPersistibleList getMolecules(){
		return molecules;
	}
	public String getName(){
		return name;
	}
	public DefaultPersistibleList getBiorelationships(){
		return biorelationships;
	}
	public DefaultPersistibleList getSentencesAboutThis(){
		return sentencesAboutThis;
	}
	public void setMolecules(java.util.Collection  var ){
		if ( molecules== null)
			molecules = new DefaultPersistibleList();
		molecules.set(var);
	}

	public void setName(String var ){
		name = var;
	}

	public void setBiorelationships(java.util.Collection  var ){
		if ( biorelationships== null)
			biorelationships = new DefaultPersistibleList();
		biorelationships.set(var);
	}

	public void setSentencesAboutThis(java.util.Collection  var ){
		if ( sentencesAboutThis== null)
			sentencesAboutThis = new DefaultPersistibleList();
		sentencesAboutThis.set(var);
	}

	public String toString() {
		return name;
	}
	public Class getPanelClass()
	{
		return MolecularNetworkPanel.class;
	}
	public String getPrettyName()
	{
		return "MolecularNetwork";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
