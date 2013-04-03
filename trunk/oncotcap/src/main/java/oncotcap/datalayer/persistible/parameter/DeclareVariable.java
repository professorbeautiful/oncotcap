package oncotcap.datalayer.persistible.parameter;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.engine.*;
import oncotcap.datalayer.persistible.StatementBundle;

public interface DeclareVariable extends Persistible, PrimitiveData, TreeViewable, Editable, VariableOperation, VariableInstance
{
	public final static int DECLARATION_EQUAL = 0;
	public final static int DECLARATION_NOT_EQUAL = 1;
	public final static int DECLARATION_CONFLICTS = 2;
	
	public abstract VariableType getType();
	public abstract String getStringValue();
	public abstract Object clone();
	//public abstract Object cloneSubstitute(StatementBundle sb);
	public abstract int equalDeclaration(DeclareVariable compVar, ValueMapPath compPath, ValueMapPath myPath );
	
	public abstract void setInitialValue(String initVal);
	public abstract String getInitialValue();
	public abstract String getValue();

	public abstract void setName(String name);
	public abstract String getName();
	public abstract String toString();
	public abstract String getDisplayString();
	//public abstract void writeDeclarationSection(Writer w, StatementBundle sb) throws IOException;
	//public abstract void writeMethodSection(Writer w, StatementBundle sb) throws IOException;
	public abstract void setStatementBundleUsingMe(StatementBundle sb);
	public abstract StatementBundle getStatementBundleUsingMe();
}