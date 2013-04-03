package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;
import java.util.Vector;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class ConditionalTableEventParameter extends ConditionalTableParameter
 {
	private String instanceUsabilityStatus;
	private Persistible conditionValue;
	private Integer versionNumber;
	private DefaultPersistibleList valueMapEntriesContainingMe;
	private DefaultPersistibleList possibleOutcomes;
	private StateMatrix stateMatrix;
	private DefaultPersistibleList variableDefinitions;
	private DefaultPersistibleList inputs;
	private DefaultPersistibleList outputs;
	private DefaultPersistibleList function;


public ConditionalTableEventParameter() {
		super();
}


public ConditionalTableEventParameter(oncotcap.util.GUID guid) {
	super(guid);
}

	public Class getPanelClass()
	{
		return ConditionalTableEventParameterPanel.class;
	}
	public String getPrettyName()
	{
		return "ConditionalTableEventParameter";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}

		 public void initSingleParams() {
				 setDefaultName("ConditionalEventTable");
				 setSingleParameterID("Conditional.Table");
				 singleParameterList.add(this);
		 }
}
