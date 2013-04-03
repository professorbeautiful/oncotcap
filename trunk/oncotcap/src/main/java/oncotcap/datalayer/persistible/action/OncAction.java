package oncotcap.datalayer.persistible.action;

import oncotcap.util.GUID;
import oncotcap.display.editor.persistibleeditorpanel.ActionEditor;
import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.persistible.CodeBundle;
import oncotcap.datalayer.persistible.*;
import oncotcap.engine.InstructionProvider;
import oncotcap.engine.ValueMap;

import java.io.Writer;
import java.io.IOException;

public interface OncAction extends Persistible, InstructionProvider
{
//	public void writeDeclarationSection(Writer writer) throws IOException;
//	public void writeMethodSection(Writer writer) throws IOException;
//	public String getDeclarationCode();
//	public String getMethodCode();
	public ActionType getType();
	public GUID getGUID();
	public ActionEditor getEditor();
	public CodeBundle getCodeBundleContainingMe();
	public void setCodeBundleContainingMe(CodeBundle bundle);
	public String getName();
	public void setStatementBundleUsingMe(StatementBundle sb);
	public StatementBundle getStatementBundleUsingMe();
	public Object clone();
}