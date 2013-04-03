package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import oncotcap.Oncotcap;
import oncotcap.util.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.*;
import oncotcap.display.common.ListDialog;
import oncotcap.display.editor.EditorFrame;

public class StatementTemplateEditorPanel extends EditorPanel
{
	StatementTemplateAndParametersPanel statementAndParamsPanel;
	StatementTemplateListPanel statementsAndCodeListPanel;
	StatementTemplate statementTemplate;


	private static EditorPanel myeditor;  //used only by TestButtons
	public StatementTemplateEditorPanel()
	{
		statementTemplate = new StatementTemplate();
		init();
		edit(statementTemplate);
	}

	public StatementTemplateEditorPanel(StatementTemplate st)
	{
		statementTemplate = st;
		init();
		edit(statementTemplate);
	}
	private void init()
	{
		JSplitPane	 mainBox = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		statementAndParamsPanel = new StatementTemplateAndParametersPanel(statementTemplate);
		statementsAndCodeListPanel = new StatementTemplateListPanel();
		statementAndParamsPanel.setListPanel(statementsAndCodeListPanel);
		statementsAndCodeListPanel.setTemplatePanel(statementAndParamsPanel);
		mainBox.setTopComponent(statementAndParamsPanel);
		mainBox.setBottomComponent(statementsAndCodeListPanel);
		mainBox.setDividerLocation(0.50);
		add(mainBox);
		resize();
		addComponentListener(new ResizeListener());
		setPreferredSize(new Dimension(800,600));
//		TestButtons tb = new TestButtons();
	}
	static JFrame jf;
	public static void main(String[] args)
	{
//		oncotcap.Oncotcap.handleCommandLine(args);
		jf = new JFrame("Statement Template Editor");
		jf.setSize(new Dimension(800,600));

//		Vector allTemplates = StatementTemplate.getAllStatementTemplates();
		StatementTemplate stToEdit;
//		int answer = Util.yesNoQuestion("Edit an existing statement template?");
//		if(answer == JOptionPane.YES_OPTION)
//		{
//		if(allTemplates.isEmpty())
//		{
//			OncMessageBox.showWarning("No statement templates exist in the current project.", "Statement Bundle Editor");
//			return;
//		}
//		Object bndl = ListDialog.getValue("Choose a Statement Template.", allTemplates);
//		if(bndl == null)
//			return;
//		else if (bndl instanceof StatementTemplate)
//		{
//			stToEdit = (StatementTemplate) bndl;
//		}
//		else
//			return;			
//		}
//		else
			stToEdit = new StatementTemplate();

/*		System.out.println("StatementTemplate: " + stToEdit);
		System.out.println("Parameters:");
		ParameterList pl = stToEdit.getParameters();
		Iterator pi = pl.getParameters().iterator();
		while(pi.hasNext())
			System.out.println("\t" + pi.next());
		System.out.println("Single Parameters:");
		Iterator spi = pl.getSingleParameters().iterator();
			System.out.println("\t" + spi.next());
*/		
/*		StatementTemplateEditorPanel ste = new StatementTemplateEditorPanel();
		ste.edit(stToEdit);
		
//		EditorDialog.showEditor(stToEdit);
		jf.getContentPane().add(ste);
		jf.setVisible(true);

		oncotcap.util.SwingUtil.revalidateAll(ste);
		ste.repaint();
*/
		myeditor = EditorFrame.showEditor(stToEdit);
		//TestButtons btns = new TestButtons();

		
	}

	final static int LIST_HEIGHT = 150;
	private void resize()
	{
		int pw = ((int) getSize().getWidth()) - 10;
		int ph = ((int) getSize().getHeight()) - 20;
		statementAndParamsPanel.setPreferredSize(new Dimension(pw, ph - LIST_HEIGHT));
		statementAndParamsPanel.setSize(new Dimension(pw, ph - LIST_HEIGHT));
		statementsAndCodeListPanel.setPreferredSize(new Dimension(pw, LIST_HEIGHT));
		statementsAndCodeListPanel.setSize(new Dimension(pw, LIST_HEIGHT));
//		cbEditor.setPreferredSize(new Dimension(pw, (ph -LIST_HEIGHT)/2));
//		cbEditor.setSize(new Dimension(pw, (ph - LIST_HEIGHT)/2));
		SwingUtil.revalidateAll(this);
		repaint();
	}

	public void edit(Object obj)
	{
		if(obj instanceof StatementTemplate)
			edit((StatementTemplate) obj);
	}
	public void edit(StatementTemplate st)
	{
		statementTemplate = st;
		statementAndParamsPanel.edit(st);
		statementsAndCodeListPanel.edit(st);
		repaint();
	}
	public Object getValue()
	{
		return(statementTemplate);
	}
	public void save()
	{
		if(statementTemplate != null)
		{
			statementAndParamsPanel.save();
			statementsAndCodeListPanel.save();
		}
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

	static class TestButtons extends JFrame
	{
		TestButtons()
		{
			setSize(300,100);
			setLocation(800,0);
			getContentPane().setLayout(new FlowLayout());
			JButton test1 = new JButton("Save");
			JButton test2 = new JButton("Redo");
			test1.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					myeditor.save();
					((Persistible) myeditor.getValue()).update();
					DataSourceStatus.getDataSource().commit();

/*					HTMLEditorPane h = ((StatementTemplateEditorPanel) myeditor).statementAndParamsPanel.editor.editorPane;
					AttributeSet attr = ((HTMLDocument) h.getDocument()).getCharacterElement(h.getCaretPosition()).getAttributes();
					Enumeration names = attr.getAttributeNames();
					System.out.println("===============================");
					while(names.hasMoreElements())
						System.out.println("\t" + names.nextElement());
						*/
				}
			}
			);
			test2.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					String txt = ((StatementTemplateEditorPanel) myeditor).statementAndParamsPanel.editor.editorPane.getText();
	//				myeditor.setVisible(false);
	//				myeditor = EditorFrame.showEditor(new StatementTemplate());
				}
			}
			);

			getContentPane().add(test1);
			getContentPane().add(test2);
			setVisible(true);
		}
	}
}
