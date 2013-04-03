package oncotcap.display.genericoutput;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import oncotcap.display.common.HyperLabel;
import oncotcap.display.common.HyperListener;
import oncotcap.process.ParentListener;
import oncotcap.process.OncCollection;
import oncotcap.process.OncProcess;
import oncotcap.util.ImmutableHashtable;

public class CollectionBrowser extends JPanel implements ParentListener
{
	private OncProcess process;
	private Vector<OncCollection> collections = new Vector<OncCollection>();
	private Vector<OutputFrame> outputFrames = new Vector<OutputFrame>();
	private Component endOfTheLine = Box.createHorizontalGlue();
	private boolean firstLabelAdded = false;
	private JLabel drillLabel;

	public CollectionBrowser()
	{
		init();
	}
	public CollectionBrowser(OncProcess process)
	{
		init();
		setProcess(process);
	}
	private void init()
	{
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		drillLabel = new JLabel("Drill down into: ");
		drillLabel.setMinimumSize(new Dimension(0, 30));
		setupLabel();
	}
	private void setupLabel()
	{
		add(Box.createHorizontalStrut(23));
		add(drillLabel);
		add(endOfTheLine);
	}
	public void setProcess(OncProcess process)
	{
		if(process != null)
			process.removeCollectionListener(this);
		this.process = process;
		clear();
		processCollections();
		process.addCollectionListener(this);
	}
	public void collectionChanged(ImmutableHashtable<Class, OncCollection> collectionTable)
	{
		processCollections();
	}
	public void clear()
	{
		destroyChildrenWindows();
		collections.clear();
		outputFrames.clear();
		removeAll();
		firstLabelAdded = false;
		setupLabel();
	}
	//get all collection types and public fields in the OncProcess that we are displaying
	//
	private void processCollections()
	{
		//JRadioButton r;
		VariableGraph vg;
		ImmutableHashtable<Class, OncCollection> procColls = process.getCollectionsTable();
		Set<Class> types = procColls.keySet();
		for(Class c : types)
		{
			OncCollection coll = procColls.get(c);
			if(! collections.contains(coll))
			{
				collections.add(coll);
				OutputFrame display = new OutputFrame(coll);
				HyperLabel lbl = new HyperLabel(c.getSimpleName());
				lbl.addHyperListener(new CollectionHyperListener(display));
				outputFrames.add(display);
				remove(endOfTheLine);
				add(Box.createHorizontalStrut(2));
				if(firstLabelAdded)
				{
					add(new JLabel("|"));
					add(Box.createHorizontalStrut(3));
				}
				add(lbl);
				add(endOfTheLine);
				firstLabelAdded = true;
			}
		}

		revalidate();
		repaint();
	}
	
	private void destroyChildrenWindows()
	{
		for(OutputFrame frm : outputFrames)
		{
			frm.destroyChildrenWindows();
			frm.dispose();
		}
	}
	class CollectionHyperListener extends HyperListener
	{
		private OutputFrame displayWindow;
		CollectionHyperListener(OutputFrame displayWindow)
		{
			this.displayWindow = displayWindow;
		}
		public void mouseActivated(MouseEvent e)
		{
			displayWindow.setVisible(true);
		}
	}
}
