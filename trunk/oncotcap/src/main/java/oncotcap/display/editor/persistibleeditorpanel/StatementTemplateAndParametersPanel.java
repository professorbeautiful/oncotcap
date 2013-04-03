package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import oncotcap.util.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.datalayer.*;
import oncotcap.display.common.HyperLabel;
import oncotcap.display.common.HyperListener;
import oncotcap.display.common.OncTable;
import oncotcap.display.editor.EditorFrame;

public class StatementTemplateAndParametersPanel extends JPanel implements SaveListener
{
	ParameterList parameterList;
	public final static int SBWIDTH = 300;
	StatementTemplateEditorPane editor;
	StatementTemplate statementTemplate;
//	JScrollPane editorSP;
	Box mainBox;
	SBControls sbControls;
	private StatementTemplateAndParametersPanel mainPanel;
	private static StatementTemplateAndParametersPanel editorPanel;
	private static JLabel renderLabel = new JLabel();
	private StatementTemplateListPanel stlp;
	
	StatementTemplateAndParametersPanel()
	{
		statementTemplate = new StatementTemplate();
		init();
	}
	StatementTemplateAndParametersPanel(StatementTemplate st)
	{
		statementTemplate = st;
		init();
	}

	
	private void init()
	{
		mainPanel = this;
		mainBox = Box.createHorizontalBox();
		editor = new StatementTemplateEditorPane(statementTemplate);
		editor.setStatementTemplateAndParametersPanel(this);
		editor.setHTMLText(statementTemplate.getStatement());
		sbControls = new SBControls(statementTemplate.getParameters());
		sbControls.setAlignmentX(Component.LEFT_ALIGNMENT);
//		editorSP = new JScrollPane(editor);
//		editorSP.getViewport().setBackground(TcapColor.lightBrown);
//		editorSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		addComponentListener(new ComponentListener(){   public void componentHidden(ComponentEvent e){}
																		public void componentMoved(ComponentEvent e){}
																		public void componentResized(ComponentEvent e){
																			resize();
																		}
																		public void componentShown(ComponentEvent e){} });

		mainBox.add(editor);
		mainBox.add(Box.createHorizontalStrut(5));
		mainBox.add(sbControls);
		mainBox.add(Box.createHorizontalGlue());
		add(mainBox);
		resize();
		SwingUtil.revalidateAll(this);
		repaint();
	}
	public void addParameter()
	{
		sbControls.addParameter();
	}
	public void objectSaved(SaveEvent e)
	{
		Object savedObj = e.getSavedObject();
		if(savedObj != null)
		{
			if(savedObj instanceof SingleParameter)
			{
				updateSingleParameter((SingleParameter) savedObj);
			}
			else if(savedObj instanceof Parameter)
			{
				updateParameter((Parameter) savedObj);
			}
		}
	}

	public void objectDeleted(SaveEvent e) {
			// Not sure if this is necessary
			//listener removed by persistible
	}

	private void resize()
	{
		mainBox.setSize(new Dimension( (int)getSize().getWidth(), (int) getSize().getHeight()));
		Dimension d = new Dimension((int)(getSize().getWidth()) - SBWIDTH - 5,(int) getHeight() - 5);
		editor.setPreferredSize(d);
		editor.editorPane.setPreferredSize(d);
		editor.setSize(d);
		editor.editorPane.setSize(d);
		Dimension d2 = new Dimension(SBWIDTH, (int) getSize().getHeight() - 55);
		sbControls.setPreferredSize(d2);
		sbControls.setSize(d2);
		SwingUtil.revalidateAll(this);
		repaint();
	}
	public static void main(String [] args)
	{
		
		 JFrame jf = new JFrame();
		 jf.setSize(600,300);
		 StatementTemplateAndParametersPanel sbep = new StatementTemplateAndParametersPanel();
		 jf.getContentPane().add(sbep);
		 jf.setVisible(true);
		 SwingUtil.revalidateAll(sbep);
		 jf.getContentPane().repaint();

		 editorPanel = sbep;
		 TestButtons tb = new TestButtons();

	}

	public void edit(StatementTemplate st)
	{
		statementTemplate = st;
		editor.edit(statementTemplate);
		sbControls.edit(statementTemplate.getParameters());
		Iterator it = st.getParameterList();
		while(it.hasNext())
		{
			Object obj = it.next();
			if(obj instanceof Parameter)
				((Parameter) obj).addSaveListener(this);
		}
		revalidate();
		repaint();
	}
	public void save()
	{
		if(statementTemplate != null)
		{
			statementTemplate.setStatement(editor.getHTMLText());
			statementTemplate.setParameters(sbControls.getParameters());
		}
	}

	public void setListPanel(StatementTemplateListPanel stlp)
	{
		this.stlp = stlp;
		//editor.setListPanel(stlp);
	}
	private void updateSingleParameter(SingleParameter sp)
	{
		Parameter paramToUpdate = null;
		if(parameterList != null)
		{
			Parameter param;
			Iterator it = parameterList.getIterator();
			if(sp != null)
			{
				while(it.hasNext())
				{
					param = (Parameter) it.next();
					if(param.containsSingleParameter(sp))
					{
						paramToUpdate = param;
					}
				}
			}

			if(paramToUpdate != null)
			{
				updateParameter(paramToUpdate);
			}
		}
	}
	private void updateParameter(Parameter param)
	{
		if(parameterList != null)
		{
			parameterList.updateParameter(param);
			parameterList.fireTableChanged();
			editor.updateParameter(param);
			repaint();
		}
	}
	boolean isDirty()
	{
		return(sbControls.isDirty() || !editor.getHTMLText().equals(statementTemplate.getStatement()));
	}
	class SBControls extends JPanel
	{
		SBControls me;
		Box mainBox;
		HyperLabel addParam;
		HyperLabel editParam;
		HyperLabel removeParam;
		OncTable pTable = new OncTable(0,2);
		JScrollPane pTableSP;
		boolean sbDirty = false;
		SBControls()
		{
//			parameterList = new ParameterList();
			init();
		}
		SBControls(ParameterList paramList)
		{
			parameterList = paramList;
			init();
		}
		private void init()
		{
			sbDirty = false;
			me = this;
			mainBox = Box.createVerticalBox();
			addParam = new HyperLabel("Add", SwingConstants.LEFT);
			editParam = new HyperLabel("Edit", SwingConstants.LEFT);
			removeParam = new HyperLabel("Remove", SwingConstants.LEFT);
			addParam.addHyperListener(new ClickToAddParameter());
			editParam.addHyperListener(new ClickToEditParameter());
			removeParam.addHyperListener(new ClickToRemoveParameter());
			Box paramControlBox = Box.createHorizontalBox();
			paramControlBox.add(addParam);
			paramControlBox.add(Box.createHorizontalStrut(40));
			paramControlBox.add(editParam);
			paramControlBox.add(Box.createHorizontalStrut(40));
			paramControlBox.add(removeParam);

			pTable.setDefaultRenderer(Object.class, new ParamTableCellRenderer());
			pTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
//			pTable.setModel(parameterList.getTableModel());
			pTable.setTitle(0, "Parameter Name");
			pTable.setTitle(1, "Default Value");

			pTableSP = new JScrollPane(pTable);
			pTableSP.setMaximumSize(new Dimension(300, Short.MAX_VALUE));
			editor.addParameterDeletedListener(new LocalParameterDeleteListener());
			editor.addParameterAddedListener(new LocalParameterAddListener());
			editor.setParameterSelectionListener(new STParameterSelectionListener());
			
			mainBox.add(paramControlBox);
			mainBox.add(pTableSP);
			add(mainBox);
			addComponentListener(new ComponentListener(){ public void componentHidden(ComponentEvent e){}
																		 public void componentMoved(ComponentEvent e){}
																		 public void componentResized(ComponentEvent e){
																			 resize();
																		 }
																		 public void componentShown(ComponentEvent e){} });																																						

		}
		void resize()
		{
			Dimension d = new Dimension((int) getSize().getWidth()-5,(int) getSize().getHeight()-20);
			pTableSP.setPreferredSize(d);
			pTableSP.setSize(d);
			oncotcap.util.SwingUtil.revalidateAll(me);
			repaint();
		}
	   ParameterList getParameters()
		{
			return(parameterList);
		}
		void edit(ParameterList params)
		{
			pTable.clear();
			parameterList = params;
			pTable.setModel(parameterList.getTableModel());
			pTable.setTitle(0, "Parameter Name");
			pTable.setTitle(1, "Default Value");

			//parameterList.printSingleParameters();
			pTable.revalidate();
			pTable.repaint();
			sbDirty = false;
		}
		public void addParameter()
		{
			Parameter editedParam = AbstractParameter.newParameter();
			if(editedParam != null)
			{
					System.out.println("editedParam " + editedParam + 
														 " editor " + editor);

				parameterList.add(editedParam);
				editor.insertParameter(editedParam);
				editedParam.addSaveListener(mainPanel);
				ParameterEditor.editParameter(editedParam);
				sbDirty = true;
			}
		}
		boolean isDirty()
		{
			return(sbDirty);
		}
		class LocalParameterAddListener implements ParameterAddListener
		{
			public void parametersAdded(Collection<Parameter> params)
			{
				for(Parameter p : params)
				{
					parameterList.add(p);
					sbDirty = true;
				}
			}
		}

		class LocalParameterDeleteListener implements ParameterDeleteListener
		{
			public void parametersDeleted(Collection<Parameter> params)
			{
				for(Parameter p : params)
				{
					parameterList.remove(p);
					sbDirty = true;
				}
			}
		}
		class STParameterSelectionListener implements StatementTemplateEditorPane.ParameterSelectionListener
		{
			public void selected(SingleParameter param)
			{
				int row = parameterList.getRow(param);
				if(row >= 0)
					pTable.setRowSelectionInterval(row, row);
			}
		}
		class ClickToAddParameter extends HyperListener
		{
			public void mouseActivated(MouseEvent e) {
				
				addParameter();
			}
		} //end class ClickToAddParameter

		class ClickToEditParameter extends HyperListener
		{
			public void mouseActivated(MouseEvent e)
			{
				int row = pTable.getSelectedRow();					
				if(row >= 0 && row <= pTable.getRowCount())
				{
					Parameter param;
					Iterator it = parameterList.getIterator();
					SingleParameter singleP = parameterList.getSingleParameterByIndex(row);

					if(singleP != null)
					{
						while(it.hasNext())
						{
							param = (Parameter) it.next();
							if(param.containsSingleParameter(singleP))
							{
							  EditorFrame.showEditor(param);
							  sbDirty = true;
							}
						}
					}
				}
			}
		} //end class ClickToEditParameter
		class ClickToRemoveParameter extends HyperListener
		{
			public void mouseActivated(MouseEvent e)
			{
				int row = pTable.getSelectedRow();
				if(row >= 0)
				{
					Parameter param = parameterList.getParameter(parameterList.getSingleParameterByIndex(row));
					if(param != null)
					{
						editor.removeParameter(param);
						param.removeSaveListener(mainPanel);
					}
					parameterList.remove(row);
				}
			}
		} //end of class ClickToRemoveParameter

		class ParamTableCellRenderer implements TableCellRenderer
		{
			public ParamTableCellRenderer()
			{
				renderLabel.setOpaque(true);
			}
			public Component getTableCellRendererComponent(JTable table,
				Object value,
				boolean isSelected,
				boolean hasFocus,
				int row,
				int column)
			{
				if(value == null)
				{
					renderLabel.setText("");
					renderLabel.setBackground(Color.white);
				}
				else
				{
					if(value instanceof SingleParameter)
					{
						SingleParameter param = (SingleParameter) value;
						renderLabel.setText((column == 0) ? param.getDisplayName() : param.getDisplayValue());
						Parameter p = parameterList.getParameter((SingleParameter) param);
						if(p != null)
						{
							renderLabel.setBackground(p.getBackground());
							renderLabel.setForeground(Color.black);
						}
						else
						{
							renderLabel.setBackground(TcapColor.blue);
							renderLabel.setForeground(Color.white);
						}
					}
					else
					{
						renderLabel.setBackground(Color.white);
						renderLabel.setForeground(Color.black);
						renderLabel.setText(value.toString());
					}
				}
				if(isSelected)
				{
					renderLabel.setBackground(TcapColor.blue /*table.getSelectionBackground() */);
					renderLabel.setForeground(Color.white);
				}

				return(renderLabel);
			}
		}//end class ParamTableCellRenderer
	}//end class sbcontrols
	
	
	static class TestButtons extends JFrame
	{
		TestButtons()
		{
			setSize(300,100);
			setLocation(800,0);
			getContentPane().setLayout(new FlowLayout());
			JButton test1 = new JButton("Write");
			JButton test2 = new JButton("Save");
			test1.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					System.out.println(editorPanel.editor.getHTMLText());
				}
			}
			);
			test2.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					editorPanel.save();
					 DataSourceStatus.getDataSource().update(editorPanel.statementTemplate);
					 DataSourceStatus.getDataSource().commit();
				}
			}
			);

			getContentPane().add(test1);
			getContentPane().add(test2);
			setVisible(true);
		} 
	}
}

