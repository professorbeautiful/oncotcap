package oncotcap.display.common;

import javax.swing.*;
import java.awt.*;
import java.beans.*;
import oncotcap.util.*;

public class SearchResultsDialog extends JDialog
{	
	protected OncTable resultsTable;
	protected JScrollPane resultsTableSP;
	protected JRadioButton radio1;
	protected JRadioButton radio2;
	protected ButtonGroup buttonGroup;
	protected JOptionPane optionPane;
	private String searchTerm;
	final String btnString1 = "Done";
	final String btnString2 = "Cancel";
	final String btnString3 = "Help";
	Object[] options = {btnString1, btnString2, btnString3};
	
	public SearchResultsDialog(JFrame frame, String searchTerm){
		super(frame, searchTerm + "s");
		this.searchTerm = searchTerm;
		init();
		setVisible(true);
	}
	public static void main(String [] args)
	{
		SearchResultsDialog sd = new SearchResultsDialog(null, "TEST");
	}
	private void init()
	{
		resultsTable = new OncTable(100, 1);
		resultsTableSP = new JScrollPane(resultsTable);
		resultsTableSP.setMaximumSize(new Dimension(300,300));
		resultsTableSP.setPreferredSize(new Dimension(300,300));
		resultsTableSP.setMinimumSize(new Dimension(300,300));
		resultsTableSP.setSize(new Dimension(300,300));
		Object[] promptArray = (Object[]) Util.newArray("java.lang.Object", 2);
		promptArray[0] = new String("Please choose a " + searchTerm);
		promptArray[1] = resultsTableSP;
		buttonGroup = new ButtonGroup();
		buttonGroup.add(radio1);
		buttonGroup.add(radio2);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		optionPane = new JOptionPane(promptArray, 
											  JOptionPane.PLAIN_MESSAGE,
											  JOptionPane.YES_NO_OPTION,
											  null,
											  options,
											  options[0]);
		setContentPane(optionPane);
		pack();
		SearchPropertyChangeListener changeListener = new SearchPropertyChangeListener();
		optionPane.addPropertyChangeListener(changeListener);
	}
	public static void show(JFrame frame, String searchTerm)
	{
		SearchResultsDialog srd = new SearchResultsDialog(frame, searchTerm);
		srd.setVisible(true);
	}
	class SearchPropertyChangeListener implements PropertyChangeListener {
		/**
		 ** Property Change event handler.
		 **/
		public void propertyChange(PropertyChangeEvent e)
		{
			Object value = optionPane.getValue();

			if (value == JOptionPane.UNINITIALIZED_VALUE)
			{
				return;
			}
			if (value.equals("Done"))
			{
				setVisible(false);
			}
			else if (value.equals("Cancel"))
			{
				setVisible(false);
			}
		}
	}
}
