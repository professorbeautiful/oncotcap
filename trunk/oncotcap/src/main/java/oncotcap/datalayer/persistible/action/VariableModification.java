package oncotcap.datalayer.persistible.action;

import java.io.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.persistible.parameter.*;

public interface VariableModification
{
	public ModifyVariableEditor getEditor();
	public String getModification() ;
	//write modification code.  variable here is of type DeclareVariable or VariableDefinition
//	public void writeMethodSection(Writer w, Object variable, StatementBundle sb) throws IOException;
//	public Object cloneSubstitute(StatementBundle sb);
	
	//operator type from constants defined in VariableDependency
	public int getOperatorType();
}
