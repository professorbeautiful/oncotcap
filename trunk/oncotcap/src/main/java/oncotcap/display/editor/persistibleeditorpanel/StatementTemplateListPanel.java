package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import oncotcap.util.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.display.browser.*;
import oncotcap.display.common.HyperLabel;
import oncotcap.display.common.HyperListener;
import oncotcap.display.common.OncList;
import oncotcap.display.editor.EditorFrame;
import oncotcap.datalayer.*;
import javax.swing.table.DefaultTableModel;

public class StatementTemplateListPanel extends JPanel
{
	HyperLabel addStatement = new HyperLabel("Add A Statement Bundle");
	HyperLabel addCode = new HyperLabel("Add A Code Bundle");
	HyperLabel edit = new HyperLabel("Edit");
	HyperLabel remove = new HyperLabel("Remove");
	OncList cbList = new OncList(); //(0, 1);
	JScrollPane cbListSP = new JScrollPane(cbList);
	StatementTemplate statementTemplate;
	SaveListener listUpdater;
	StatementTemplateAndParametersPanel stpp = null;
	boolean isDirty = false;
	
	StatementTemplateListPanel()
	{
		init();
	}
	StatementTemplateListPanel(StatementTemplate st)
	{
		init();
		edit(st);
	}
	private void init()
	{
		isDirty = false;
		Box mainBox = Box.createVerticalBox();
		Box labelBox = Box.createHorizontalBox();

		addStatement.setAlignmentX(Component.LEFT_ALIGNMENT);
		addStatement.addHyperListener(new ClickToAddStatementBundle());
		addCode.setAlignmentX(Component.LEFT_ALIGNMENT);
		addCode.addHyperListener(new ClickToAddCodeBundle());
		edit.setAlignmentX(Component.LEFT_ALIGNMENT);
		edit.addHyperListener(new EditCodeOrStatementBundle());
		remove.setAlignmentX(Component.LEFT_ALIGNMENT);
		remove.addHyperListener(new RemoveCodeBundle());
		labelBox.add(addStatement);
		labelBox.add(Box.createHorizontalStrut(10));
		labelBox.add(addCode);
		labelBox.add(Box.createHorizontalStrut(10));
		labelBox.add(edit);
		labelBox.add(Box.createHorizontalStrut(10));
		labelBox.add(remove);
		labelBox.add(Box.createHorizontalGlue());
//		cbList.setModel(new STTableModel());
//		cbList.setTitle(0,"Statement Bundles and Code Bundles");
		MouseListener mouseListener = new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					int index = cbList.locationToIndex(e.getPoint());
					if(index >= 0 && index < cbList.getModel().getSize())
						editCodeOrStatmentBundle(cbList.getModel().getElementAt(index));
				}
			}
		};
		cbList.addMouseListener(mouseListener);

		
		listUpdater = new SaveListener()
		{
			public void objectSaved(SaveEvent e)
			{
				DefaultListModel lm = (DefaultListModel) cbList.getModel();
				Object obj = lm.getElementAt(lm.getSize() - 1);
				// if(obj != null && obj instanceof Persistible)
//							((Persistible) obj).removeSaveListener(listUpdater);
				
				((DefaultListModel) cbList.getModel()).removeElementAt(lm.getSize() -1);
				((DefaultListModel) cbList.getModel()).addElement(obj);
				cbList.revalidate();
				cbList.repaint();
				revalidate();
				repaint();
			}
			public void objectDeleted(SaveEvent e) {
					// Select the row in the code bundle list and then perform the same 
					// methods as you would if the user removed from this editor
					cbList.clearSelection();
					cbList.setSelectedValue(e.getSavedObject(), false);
					removeSelectedFromCBList();
			}
		};
		cbList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		cbListSP.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		mainBox.add(labelBox);
		mainBox.add(Box.createVerticalStrut(10));
		mainBox.add(cbListSP);
		add(mainBox);
		resize();
		addComponentListener(new ResizeListener());
	}
//	public void objectSaved(SaveEvent event)
//	{
//		
//	}
	public void edit(StatementTemplate st)
	{
		isDirty = false;
		statementTemplate = st;
		cbList.clear();
		Iterator it = statementTemplate.getCodeBundleList();
		while(it.hasNext())
			addCodeBundle((CodeBundle) it.next());
		it = statementTemplate.getStatementList();
		while(it.hasNext())
			addStatementBundle((StatementBundle) it.next());
		repaint();
	}
	public void save()
	{
		statementTemplate.getStatementBundles().clear();
		statementTemplate.getCodeBundles().clear();
		DefaultListModel tableModel = (DefaultListModel) cbList.getModel();
		for(int n = 0; n < tableModel.getSize(); n++)
		{
			Object val = tableModel.getElementAt(n);
			if(val instanceof CodeBundle)
				statementTemplate.addCodeBundle((CodeBundle) val);
			else if(val instanceof StatementBundle)
				statementTemplate.addStatementBundle((StatementBundle) val);
		}
	}
	boolean isDirty()
	{
		return(isDirty);
	}
	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		jf.setSize(600,300);
		jf.getContentPane().add(new StatementTemplateListPanel());
		jf.setVisible(true);
	}
	public void addStatementBundle(StatementBundle sb)
	{
			if ( sb != null &&
					 sb.getStatementTemplate().equals(statementTemplate) )
					JOptionPane.showMessageDialog
							(null, 
							 "Error: Attempting to make a circular relationship StatementBundle is based on current Statement Template");
			else
					addToList(sb);
	}
	public void addCodeBundle(CodeBundle cb)
	{
		addToList(cb);
	}
	private void addToList(Persistible obj)
	{
		((DefaultListModel) cbList.getModel()).addElement(obj);
		obj.addSaveListener(listUpdater);
		isDirty = true;
	}
	public void resize()
	{
		int pw = (int) (getSize().getWidth() - addCode.getSize().getHeight()) - 10;
		int ph = (int) getSize().getHeight() - 50;
		cbListSP.setPreferredSize(new Dimension(pw, ph));
		cbListSP.setSize(new Dimension(pw, ph));
		SwingUtil.revalidateAll(this);
		repaint();
	}
	private class ResizeListener implements ComponentListener
	{
		public void componentHidden(ComponentEvent e) {}
		public void componentMoved(ComponentEvent e) {}
		public void componentResized(ComponentEvent e)
		{
			resize();
		}
		public void componentShown(ComponentEvent e) {}

	}
	class ClickToAddStatementBundle extends HyperListener
	{
		public void mouseActivated(MouseEvent e)
		{
				Object editableObj = null;
				boolean showEditor = true;

				AddLinkPanel addLinkPanel = new AddLinkPanel(StatementBundle.class,
					"Add a new SB");
				addLinkPanel.setSize(new Dimension(500, 300)); // modal dialog
				addLinkPanel.selectRowLikeUser(0);
				addLinkPanel.show();
				Object selectedObject = addLinkPanel.getValue();
				addLinkPanel.dispose();
				if ( selectedObject == null )  {// nothing to edit 
						return; // user must have cancelled out
				}
				if ( selectedObject instanceof Class ) {
						editableObj =
							AddTreeNode.instantiateStatementBundle(CollectionHelper.makeVector(statementTemplate));
				}
				else if ( selectedObject instanceof Persistible ) {
						editableObj = selectedObject;
				}
				if ( editableObj instanceof StatementBundle) {
						addStatementBundle((StatementBundle)editableObj);
					//	((StatementBundle) editableObj).addSaveListener(this);
				}
				/*
			Vector allTemplates = StatementTemplate.getAllStatementTemplates();
			StatementTemplate stToEdit;
			if(allTemplates.isEmpty())
			{
				OncMessageBox.showWarning("No statement bundles exist in the current project.", "Statement Template Editor");
				return;
			}
			Object bndl = ListDialog.getValue("Choose a StatementTemplate.", allTemplates);
			if(bndl == null)
				return;
			else if (bndl instanceof StatementTemplate)
			{
				stToEdit = (StatementTemplate) bndl;
				if(stToEdit == statementTemplate)
				{
					OncMessageBox.showWarning("A statement bundle can not be added to the statement template that it is based on.", "Statement Template Editor");
					return;
				}
			}
			else
				return;			
			*/			
		}



	}

	class ClickToAddCodeBundle extends HyperListener
	{
		public void mouseActivated(MouseEvent e)
		{
				Object editableObj = null;
				boolean showEditor = true;

				AddLinkPanel addLinkPanel = new AddLinkPanel(CodeBundle.class, 
																										 "Add Code Bundle");
				addLinkPanel.setSize(new Dimension(500, 300)); // modal dialog
				addLinkPanel.selectRowLikeUser(0);
				addLinkPanel.show();
				Object selectedObject = addLinkPanel.getValue();
				addLinkPanel.dispose();
			if ( selectedObject == null )  {// nothing to edit 
					return; // user must have cancelled out
			}
			if ( selectedObject instanceof Class ) {
					editableObj = new CodeBundle();
					EditorFrame.showEditor
								((CodeBundle)editableObj, new Dimension(700,650));
			}
			else if ( selectedObject instanceof Persistible ) {
					editableObj = selectedObject;
			}
			if ( editableObj instanceof CodeBundle) {
					addCodeBundle((CodeBundle)editableObj);
			}
		}
	}
	public void setTemplatePanel(StatementTemplateAndParametersPanel stpp)
	{
		this.stpp = stpp;
	}
	private void editCodeOrStatmentBundle(Object obj)
	{
		if(obj != null)
		{
			if(obj instanceof CodeBundle)
			{
				CodeBundleEditorPanel cbep = (CodeBundleEditorPanel) EditorFrame.showEditor((CodeBundle) obj);
				cbep.setTemplatePanel(stpp);
				isDirty = true;
			}
			else if(obj instanceof StatementBundle)
			{
				EditorFrame.showEditor((StatementBundle) obj);
				isDirty = true;
			}
		}

	}
	private class RemoveCodeBundle extends HyperListener
	{
		public void mouseActivated(MouseEvent e)
		{
				removeSelectedFromCBList();
		}
	}

		private void removeSelectedFromCBList() {
			int selectedRow = cbList.getSelectedIndex();
			if(selectedRow >= 0)
					{
							DefaultListModel lm = (DefaultListModel) cbList.getModel();
							Object obj = lm.getElementAt(selectedRow);
							// if(obj != null && obj instanceof Persistible)
// 									((Persistible) obj).removeSaveListener(listUpdater);
							
							((DefaultListModel) cbList.getModel()).removeElementAt(selectedRow);
							repaint();
							isDirty = true;
					}
		}
	private class EditCodeOrStatementBundle extends HyperListener
	{
		public void mouseActivated(MouseEvent e)
		{
			Object selectedItem = cbList.getSelectedValue();
			editCodeOrStatmentBundle(selectedItem);
		}
	}
	private class STTableModel extends DefaultTableModel
	{
		public boolean isCellEditable(int row, int col)
		{
			return false;
		}  
	}

}
