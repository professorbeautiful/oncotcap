package oncotcap.datalayer.persistible.action;

import java.util.Collection;
import java.util.Vector;

import oncotcap.display.browser.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.engine.ClassSectionDeclaration;
import oncotcap.engine.Instruction;
import oncotcap.engine.InstructionProvider;
import oncotcap.engine.ValueMapPath;
import oncotcap.engine.VariableDependency;
import oncotcap.util.CollectionHelper;

public class BlankAction extends DefaultOncAction
{
	private BlankAction selfreference;
	public BlankAction() { selfreference  = this;}
	public BlankAction(oncotcap.util.GUID guid) {
		super(guid);
		selfreference = this;
	}
	public boolean check()
	{
		return(true);
	}
	public String getName() {
		return toString();
	}
	// Implement Editable
	/**
	 * @return  null
	 */
	public EditorPanel getEditorPanelWithInstance() {
		return null;
	}

	/**
	 * @return  null
	 */
	public EditorPanel getEditorPanel() {
		return null;
	}
	
	/**
	 * Gets the ViewableList attribute of the ModifyVariableAction object
	 *
	 * @param viewableList Description of Parameter
	 * @param parent       Description of Parameter
	 * @return             The ViewableList value
	 */
		public TreeViewableList getViewableList(TreeViewableList viewableList,
																						OncViewerTreeNode parent) {
				OncViewerTreeNode oncViewerObject =
						new OncViewerTreeNode(getDisplayString(),
																	parent);
				oncViewerObject.setCodeBundle(parent.getCodeBundle());
				oncViewerObject.setUserObject(this);
				
				viewableList.getViewableList().addElement(oncViewerObject);
				viewableList.setViewableObject(oncViewerObject);
				return viewableList;
		}
		public String toString(){return("");}
		/*public void writeDeclarationSection(Writer write) throws IOException{}

		public void writeMethodSection(Writer write) throws IOException{}
		*/
		private Vector<Instruction> instructions = null;
		public Collection<Instruction> getInstructions()
		{
			if(instructions == null)
			{
				instructions = new Vector<Instruction>();
				instructions.add(new DeclarationInstruction());
			}
			return(instructions);
		}
		public class DeclarationInstruction implements Instruction
		{
			public Collection<String> getSetVariables(ValueMapPath path)
			{
				return(new Vector<String>());
			}
			public Collection<String> getAllVariables(ValueMapPath path)
			{
				return(new Vector<String>());
			}
			public Collection<VariableDependency> getVariableDependencies(ValueMapPath path)
			{
				return(new Vector<VariableDependency>());
			}
			public ClassSectionDeclaration getSectionDeclaration()
			{
				return(ClassSectionDeclaration.DECLARATION_SECTION);
			}
			public InstructionProvider getEnclosingInstructionProvider()
			{
				return(selfreference);
			}
		}
}
