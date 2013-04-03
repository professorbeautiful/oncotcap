package oncotcap.display.editor.persistibleeditorpanel;

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.SingleParameter;
import oncotcap.util.*;
import oncotcap.datalayer.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

import oncotcap.display.common.HyperLabel;
import oncotcap.display.common.HyperListener;
import oncotcap.display.common.ListDialog;
import oncotcap.display.editor.EditorFrame;
import oncotcap.display.modelcontroller.cellkineticsdemo.EditorSelectionListener;
import oncotcap.display.modelcontroller.cellkineticsdemo.SelectionManager;

public class StatementBundleEditorPanel extends StatementTemplateEditorPane implements SaveListener
{
	private StatementBundle statementBundle;
	private static EditorPanel myeditor;  //used only by TestButtons
//	private String statementBase;
	
	private EditorSelectionListener editSelectListener = null;
	private boolean lBoxVisible = true;
	private Box lBox;
	private JTextField shortName;
	
//	this statement bundle configuration is set and used only in DefaultInputEditor
	private StatementBundleConfiguration sbConfig;
	
	public boolean addedToPanel = false;  //used in StatementListPanel
	
	private Vector<StatementChangeListener> statementChangeListeners = new Vector<StatementChangeListener>();
	public StatementBundleEditorPanel()
	{
		super(WITH_VALUES);
		editorPane.setEditable(false);
		init();
	}
	public StatementBundleEditorPanel(StatementBundle sb)
	{
		super(WITH_VALUES);
		editorPane.setEditable(false);
		init();
		edit(sb);
	}
	public static void main(String [] args)
	{
		JFrame jf = new JFrame("Statement Bundle Editor");
		jf.setSize(new Dimension(800,300));

		Collection allBundles = StatementBundle.getAllStatementBundles();
		StatementBundle sbToEdit;

		int answer = Util.yesNoQuestion("Edit an existing statement bundle?");
		if(answer == JOptionPane.YES_OPTION)
		{
			if(allBundles.isEmpty())
			{
				OncMessageBox.showWarning("No statement bundles exist in the current project.", "Statement Bundle Editor");
				return;
			}
			Object bndl = ListDialog.getValue("Choose a Statement Bundle.", allBundles);
			if(bndl == null)
				return;
			else if (bndl instanceof StatementBundle)
			{
				sbToEdit = (StatementBundle) bndl;
			}
			else
				return;
		}
		else
			sbToEdit = new StatementBundle(new StatementTemplate());

		myeditor = EditorFrame.showEditor(sbToEdit);
		TestButtons btns = new TestButtons();
	}
	private void init()
	{
//		setBorder(BorderFactory.createLineBorder(Color.red, 4));
		JLabel lblShortName = new JLabel("Short Name: ");
		lblShortName.setSize(100,30);
		
		shortName = new JTextField();
		shortName.setMinimumSize(new Dimension(100,30));
		shortName.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
		HyperLabel editTemplate = new HyperLabel("Edit base template");
		editTemplate.setSize(100,30);
		editTemplate.addHyperListener(new ShowTemplateListener());
		lBox = Box.createHorizontalBox();
		lBox.add(Box.createHorizontalStrut(5));
		lBox.add(lblShortName);
		lBox.add(shortName);
		lBox.add(Box.createHorizontalStrut(10));
		//lBox.add(Box.createHorizontalGlue());
		lBox.add(editTemplate);
		lBox.add(Box.createHorizontalStrut(5));
		add(lBox, BorderLayout.NORTH);
		addMouseListener(new SBMouseListener());
	}
	public void setEditControlsVisible(boolean vis)
	{
		lBoxVisible = vis;
		lBox.setVisible(vis);
	}
	public void edit(Object obj)
	{
		if(obj instanceof StatementBundle)
			edit((StatementBundle)obj);
	}
	public void edit(StatementBundle sb)
	{
		statementBundle = sb;
		valueMap = sb.getValueMap();
		parameters = sb.getStatementTemplate().getParameters();
		if(sb.getStatement() == null)
			setStatement(sb.getStatementTemplate().getStatement());
		else
			setStatement(sb.getStatement());

//		if(sb.getStatementTemplate() != null && sb.getStatementTemplate().getStatement() != null)
//			statementBase = new String(sb.getStatementTemplate().getStatement());
//		else
//			statementBase = null;
		
		if(sb.getShortName() != null)
			shortName.setText(sb.getShortName());
		sb.getStatementTemplate().addSaveListener(this);
		
		refresh();
		editorPane.revalidate();
		editorPane.repaint();
	}
/*	public int setWidth(int width)
	{
		int height;
		setSize(width, (height = super.setWidth(width)));
		repaint();
		return(height);
	} */
	public void refreshStatement()
	{
		refresh();
	}
	private void refresh()
	{
		if(statementBundle != null && statementBundle.getStatementTemplate() != null)
		{
				StatementTemplate template = statementBundle.getStatementTemplate();
				parameters = template.getParameters();
				setStatement(template.getStatement());
				if(getHTMLText() != null && !getHTMLText().equals(""))
					statementBundle.setStatement(getHTMLText());
				else
					statementBundle.setStatement(template.getStatement());
		}
		else
			setStatement("");
		editorPane.repaint();
	}
	public Object getValue()
	{
		return(statementBundle);
	}

	//implement save listener to watch the StatementTemplate that the
	//StatementBundle being edited is based on
	public void objectSaved(SaveEvent e)
	{
		refresh();
	}
	public void objectDeleted(SaveEvent e)
	{
		refresh();
	}

	public void setStatement(String statement)
	{
		String oldStatement = getHTMLText();
		super.setStatement(statement);
		Iterator it = parameters.getSingleParameters().iterator();
		while(it.hasNext())
		{
			updateSingleParameter((SingleParameter) it.next());
		}
		
		String newStatement = getHTMLText();
		if(OperatorHelper.XOR(newStatement == null,oldStatement == null))
			fireStatementChanged();
		else if( (! (newStatement == null || oldStatement == null)))
		{
			String newStatementNoHTML = StringHelper.htmlToText(newStatement).trim();
			String oldStatementNoHTML = StringHelper.htmlToText(oldStatement).trim();
			if(!newStatementNoHTML.equals(oldStatementNoHTML)) 
				fireStatementChanged();
		}			
	}
	public void save()
	{
		statementBundle.setStatement(getHTMLText());
		statementBundle.setValueMap(valueMap);
		if(shortName.getText() != null && !shortName.getText().trim().equals(""))
			statementBundle.setShortName(shortName.getText());
	}
	public StatementBundle getStatementBundle()
	{
		return(statementBundle);
	}

	public void setEditorSelectionListener(EditorSelectionListener list)
	{
		editSelectListener = list;
	}
	
//	this statement bundle configuration is set and used only in DefaultInputEditor
	public void setStatementBundleConfiguration(StatementBundleConfiguration config)
	{
		sbConfig = config;
	}
//	this statement bundle configuration is set and used only in DefaultInputEditor
	public StatementBundleConfiguration getStatementBundleConfiguration()
	{
		return(sbConfig);
	}
	public void addStatementChangeListener(StatementChangeListener listener)
	{
		if(! statementChangeListeners.contains(listener))
			statementChangeListeners.add(listener);
	}
	public void fireStatementChanged()
	{
		for(StatementChangeListener listener : statementChangeListeners)
			listener.statementChanged(this);
	}
	public interface StatementChangeListener
	{
		public void statementChanged(StatementBundleEditorPanel panel);
	}
	static class TestButtons extends JFrame
	{
		TestButtons()
		{
			setSize(300,100);
			setLocation(800,0);
			getContentPane().setLayout(new FlowLayout());
			JButton test1 = new JButton("Save");
			JButton test2 = new JButton("Show Template");
			test1.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					myeditor.save();
					((Persistible) myeditor.getValue()).update();
					DataSourceStatus.getDataSource().commit();

				}
			}
			);
			test2.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					EditorFrame.showEditor(((StatementBundleEditorPanel) myeditor).getStatementBundle().getStatementTemplate());
				}
			}
			);

			getContentPane().add(test1);
			getContentPane().add(test2);
			setVisible(true);
		}
	} //end class TestButtons
	class ShowTemplateListener extends HyperListener
	{
		public void mouseActivated(MouseEvent e)
		{
			EditorFrame.showEditor(getStatementBundle().getStatementTemplate());			
		}

	} //end class ShowTemplateListener

	class SBMouseListener implements MouseListener
	{
		public void mouseClicked(MouseEvent e)
		{
			if (editSelectListener != null)
			{
				SelectionManager.select(statementBundle);
				editSelectListener.editorSelected(statementBundle);
			}
		}
		public void mousePressed(MouseEvent e){}
		public void mouseReleased(MouseEvent e){}
		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){}

	} //end class SBMouseListener
}