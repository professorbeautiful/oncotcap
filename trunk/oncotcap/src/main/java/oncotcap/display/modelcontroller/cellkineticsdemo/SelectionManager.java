package oncotcap.display.modelcontroller.cellkineticsdemo;

import javax.swing.*;

import java.awt.Color;
import java.util.*;
import oncotcap.util.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.persistibleeditorpanel.*;

public class SelectionManager
{
	static private StatementBundleEditorPanel selectedEditor = null;
	static private JTree statementTree = null;
	static private StatementListPanel statementList = null;
//	static private NoviceEditor noviceEd = null;
	static private DefaultInputEditor inputEditor = null;
	static private JList sList = null;
	
	public static void setTree(JTree tree){statementTree = tree;}
	public static void setStatementList(StatementListPanel list){statementList = list;}
	public static void setList(JList list){sList = list;}
	public static void setInputEditor(DefaultInputEditor ie){inputEditor = ie;}
	
	public static void select(StatementBundleEditorPanel se)
	{
		if (selectedEditor != null)
		{
			selectedEditor.setBackground(TcapColor.lightBlue);
			selectedEditor.setForeground(Color.red);
			selectedEditor.setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
		}
		selectedEditor = se;
		if(statementTree != null)
		{
			javax.swing.tree.TreePath tp = inputEditor.getModel().getConfigurations().getConfiguration(se.getStatementBundle()).getSelectionPath();
			if(tp != null)
			{
				statementTree.makeVisible(tp);
				statementTree.setSelectionPath(tp);
			}
		}
		if(statementList != null)
		{
			statementList.ensureIsShown(se);
			statementList.enableDelete();
			if(inputEditor.getModel().getConfigurations().getConfiguration(se.getStatementBundle()).areMultiplesAllowed())
				statementList.enableMore();
			else
				statementList.disableMore();
		}
		if(sList != null)
		{
			//code to select statement here in JList
		}
	}
	
	public static void select(StatementBundle sb)
	{
		StatementBundleEditorPanel se = sb.getStatementEditor();
		if(se != null)
		{
			select(se);
		}
		else
			Logger.log("ERROR: Statement editor is null [SelectionManager]");
		selectedEditor = se;
	}
	
	public static StatementBundleEditorPanel getSelectedEditor()
	{
		return(selectedEditor);
	}
	
	public static void unselect()
	{
		if(selectedEditor != null)
		{
			selectedEditor.setBackground(TcapColor.lightBrown);
			selectedEditor = null;
		}
		if(statementList != null)
		{
			statementList.disableMore();
			statementList.disableDelete();
		}
	}
	
	static boolean isSelected(StatementBundleEditorPanel se)
	{
		return(selectedEditor == se);
	}

/*	public boolean checkForCompleteModel()
	{
		if (noviceEd != null)
		{
			Vector bundles = noviceEd.getStatementBundles();
		}
		return(false);
	}
*/
}