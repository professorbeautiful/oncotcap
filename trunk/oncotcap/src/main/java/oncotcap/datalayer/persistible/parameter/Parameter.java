package oncotcap.datalayer.persistible.parameter;

import java.util.*;
import java.awt.Color;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.util.GUID;
import oncotcap.engine.InstructionProvider;

public interface Parameter extends Editable, InstructionProvider
{
	public abstract Object clone();
	public Parameter clone(boolean saveToDataSource);
	public SingleParameter getMatchingSingleParameter(SingleParameter sp);
	public SingleParameter getSingleParameter(String singleParameterID);
	public String getName();
	public void setName(String name);
	public abstract ParameterType getParameterType();
	public String getTypeName();
	public DefaultPersistibleList getVariableDefinitions();
	public void setVariableDefinitions(java.util.Collection  var );
	public void addVariableDefinition(VariableDefinition varDef);
	public Parameter newStorageObject();
	public String toString();
	public SingleParameterList getSingleParameterList();
	public Iterator getSingleParameters();
	public boolean contains(SingleParameter sp);
	public abstract void initSingleParams();
	public abstract EditorPanel getParameterEditorPanelWithInstance();
	public abstract EditorPanel getParameterEditorPanel();
	public void clearSingleParameters();
	public boolean containsSingleParameter(SingleParameter singleP);
	public void addSaveListener(SaveListener handler);
	public void removeSaveListener(SaveListener listener);
	public void setBackground(Color c);
	public Color getBackground();
	public GUID getGUID();
	public void setOriginalSibling(Parameter param);
	public Parameter getOriginalSibling();
}
