package oncotcap.display.genericoutput;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Set;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import oncotcap.annotation.VariablePlotDescriptor;
import oncotcap.display.LegendPanel;
import oncotcap.display.LegendDialog;
import oncotcap.process.ParentListener;
import oncotcap.process.OncCollection;
import oncotcap.process.OncProcess;
import oncotcap.util.*;

/**
 * Graphs all of the variables available in the children of an OncProcess
 * 
 * @author shirey
 *
 */
public class ProcessGraph extends JPanel implements ParentListener
{
	//private MasterScheduler scheduler;
	//private OncModel model;
	private JComboBox variableCombo;
	private OncProcess process = null;
	private Hashtable<Class, CollectionInfo> collections = new Hashtable<Class, CollectionInfo>();
	private OutputFrame owner = null;
	private LegendPanel currentLegend = null;
	private Hashtable<String, VariableGraph> variables = new Hashtable<String, VariableGraph>();
	private VariableGraph currentVG = null;
	private MessagePane messages = null;
	private JPanel messagePanel;
	private JPanel variablePanel;
	private LegendDialog legendDialog = null;
	private boolean legendLocationSet = false;
	private JTabbedPane tabPane;
	public ProcessGraph()
	{
		init();
	}
	public ProcessGraph(OncProcess process)
	{
		init();
		setProcess(process);
	}
	public void setOwner(OutputFrame owner)
	{
		if(this.owner == null || (this.owner != owner))
		{
			this.owner = owner;
			if(legendDialog != null)
			{
				legendDialog.setVisible(false);
				legendDialog.dispose();
			}
			
			legendDialog = new LegendDialog(owner);
			if(currentLegend != null)
			{
				legendDialog.showLegend(currentLegend);
				legendDialog.setLocation();
				legendLocationSet = true;
			}
		}
	}
	private void init()
	{
		JPanel controlPane = new JPanel();
		controlPane.setLayout(new BoxLayout(controlPane, BoxLayout.X_AXIS));
		setLayout(new BorderLayout());
		
		variableCombo = new JComboBox();
		variableCombo.setModel(new SortedComboBoxModel(new StringCompareIgnoreCase()));
		variableCombo.setEditable(false);
		controlPane.add(Box.createHorizontalStrut(70));
		controlPane.add(new JLabel("Select a variable to graph:  "));
		controlPane.add(variableCombo);
		controlPane.add(Box.createHorizontalStrut(40));
		//add(controlPane, BorderLayout.NORTH);
		
		messagePanel = new JPanel();
		messagePanel.setLayout(new BorderLayout());
		variablePanel = new JPanel();
		variablePanel.setLayout(new BorderLayout());
		tabPane = new JTabbedPane();
		tabPane.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				if(legendDialog != null)
				{
					if(tabPane.getSelectedIndex() == 1)
						legendDialog.setVisible(true);
					else
						legendDialog.setVisible(false);
				}
			}
		});
		variablePanel.add(controlPane, BorderLayout.NORTH);
		tabPane.addTab("Messages", messagePanel);
		tabPane.addTab("Graphs", variablePanel);
		add(tabPane, BorderLayout.CENTER);
		
		variableCombo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				refreshVariableGraph();
			}
		}
		);

	}

	private void refreshVariableGraph()
	{
		VariableGraph vg = null;
		String varName = (String) variableCombo.getSelectedItem();
		if(varName != null && ! varName.trim().equals(""))
		{
			vg = variables.get(varName);
		}
		if(vg != null)
			display(vg);
		
		currentLegend = vg.legend;
		if(legendDialog != null)
		{
			if(owner == null || ! owner.isVisible())
			{
					legendDialog.setVisible(false);
			}
			else
			{
				legendDialog.showLegend(currentLegend);
				if(!legendLocationSet)
				{
					legendDialog.setLocation();
					legendLocationSet = true;
				}
				legendDialog.setVisible(true);
			}
		}
	}
	public void setProcess(OncProcess process)
	{
		if(this.process == null || !process.getOncProcessClass().equals(this.process.getOncProcessClass()))
		{
			this.process = process;
			clear();
			//add variables from this process
			for(Field f : process.getClass().getFields())
				addVariableGraph(f);
			processCollections();
			process.addCollectionListener(this);
			
			if(messages != null)
				messagePanel.remove(messages);
			messages = new MessagePane(process);
			messagePanel.add(messages, BorderLayout.CENTER);
		}
	}
	public void collectionChanged(ImmutableHashtable<Class, OncCollection> collectionTable)
	{
		processCollections();
	}
	//get all collection types and public fields in the OncProcess that we are displaying
	//
	private void processCollections()
	{
		ImmutableHashtable<Class, OncCollection> procColls = process.getCollectionsTable();
		Set<Class> types = procColls.keySet();
		for(Class c : types)
		{
			if(! collections.containsKey(c))
			{
				for(Field f : c.getFields())
				{
					VariableGraph vg = addVariableGraph(f);
					if(vg != null)
						collections.put(c, new CollectionInfo(c, procColls.get(c), f, vg));

				}
			}
		}

//		revalidate();
//		repaint();
	}
	
	private VariableGraph addVariableGraph(Field fieldToAdd)
	{
		VariableGraph vg;
		Class varType = fieldToAdd.getType();
		Class c = fieldToAdd.getDeclaringClass();
		if(varType.equals(double.class) || varType.equals(float.class) || varType.equals(int.class) || varType.equals(long.class))
		{
			VariablePlotDescriptor desc = fieldToAdd.getAnnotation(VariablePlotDescriptor.class);
			if(desc != null)
			{
				vg = new VariableGraph();
				vg.setVariable(process, c, fieldToAdd);
				vg.setAxisIsLog10("y", desc.isYAxisLogScale());
				vg.setAxisBounds("y", desc.getYMin(), desc.getYMax(), desc.getYStep());
				String varName = desc.getTitle();
				if(varName == null || varName.trim().equals(""))
					varName = fieldToAdd.getName();
				vg.setAxisName("y", varName);
				//if we know the JFame that this graph is part of create a legend
				String name = c.getSimpleName() + "." + varName;
				if(owner != null && ReflectionHelper.getDeclaredEnums(c).size() != 0 )
				{
					LegendPanel legend = new LegendPanel(vg, legendDialog, " Legend for " + name);
					vg.setLegend(legend);
				}
				variables.put(name, vg);
				variableCombo.addItem(name);
				refreshVariableGraph();
				return(vg);
			}
		}
		return(null);
	}
	private void display(VariableGraph vg)
	{
		if(currentVG != null)
			variablePanel.remove(currentVG);
		currentVG = vg;
		variablePanel.add(vg, BorderLayout.CENTER); //BorderLayout.CENTER);
		revalidate();
		repaint();
	}
	private void clear()
	{
		if(currentVG != null)
			variablePanel.remove(currentVG);
		if(messages != null)
			messagePanel.remove(messages);
		collections.clear();
		variableCombo.removeAllItems();
	}
	class CollectionInfo
	{
		Class type;
		OncCollection collection;
		Field field;
		Object displayControl;
		
		CollectionInfo(Class type, OncCollection collection, Field field, Object displayControl)
		{
			this.type = type;
			this.field = field;
			this.collection = collection;
			this.displayControl = displayControl;
		}
	}
}
