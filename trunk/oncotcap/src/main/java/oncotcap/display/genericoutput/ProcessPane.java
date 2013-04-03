package oncotcap.display.genericoutput;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import oncotcap.process.OncProcess;

public class ProcessPane extends JPanel
{
	private ProcessGraph processGraph;
	private EventGraph eventGraph;
	private EventTable eventTable;
	private CollectionBrowser collectionBrowser;
	private OutputFrame owner;
	private OncProcess process;
	
	public ProcessPane(OutputFrame owner)
	{
		this.owner = owner;
		init();
	}
	public ProcessPane(OncProcess process, OutputFrame owner)
	{
		this.owner = owner;
		init();
		setDisplayObject(process);
	}
	public void setDisplayObject(OncProcess process)
	{
		this.process = process;
		processGraph.setProcess(process);
		eventGraph.setProcess(process);
		collectionBrowser.setProcess(process);
		eventTable.setProcess(process);
	}
	public OncProcess getProcess()
	{
		return(process);
	}
	public void destroyChildrenWindows()
	{
		collectionBrowser.clear();
	}
	private void init()
	{
		JPanel eventTableAndCollectionBrowser = new JPanel();
		eventTableAndCollectionBrowser.setLayout(new BoxLayout(eventTableAndCollectionBrowser, BoxLayout.Y_AXIS));
		eventTable = new EventTable();
		processGraph = new ProcessGraph();
		processGraph.setOwner(owner);
		eventGraph = new EventGraph();
		collectionBrowser = new CollectionBrowser();
		eventTableAndCollectionBrowser.add(collectionBrowser);
		eventTableAndCollectionBrowser.add(eventTable);
		JSplitPane topSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, eventTableAndCollectionBrowser, processGraph);
		//JSplitPane bottomSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, eventGraph, collectionBrowser);
		JSplitPane outerSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, topSplit, eventGraph);
		outerSplit.setDividerLocation(400);
		topSplit.setDividerLocation(100);
		outerSplit.setDividerSize(5);
		topSplit.setDividerSize(5);
		//bottomSplit.setDividerSize(5);
		//bottomSplit.setDividerLocation(100);

		setLayout(new BorderLayout());
		add(outerSplit, BorderLayout.CENTER);

	}
	
	
}
