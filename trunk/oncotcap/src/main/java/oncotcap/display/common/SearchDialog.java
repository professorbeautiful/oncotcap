package oncotcap.display.common;

import javax.swing.*;
import java.beans.*;
import oncotcap.util.*;

public class SearchDialog extends JDialog {

	protected JTextField textInput;
	protected JRadioButton radio1;
	protected JRadioButton radio2;
	protected ButtonGroup buttonGroup;
	protected JOptionPane optionPane;
	protected JFrame parentFrame;
	private String searchTerm;
	final String btnString1 = "Done";
	final String btnString2 = "Cancel";
	final String btnString3 = "Help";
	Object[] options = {btnString1, btnString2, btnString3};
	
	public SearchDialog(JFrame frame, String searchTerm){
		super(frame, "Search for a " + searchTerm);
		this.searchTerm = searchTerm;
		parentFrame = frame;
		init();
		setVisible(true);
	}
	public static void main(String [] args)
	{
		SearchDialog sd = new SearchDialog(null, "TEST");
	}
	private void init()
	{
		textInput = new JTextField();
		radio1 = new JRadioButton("match ALL keywords");
		radio2 = new JRadioButton("match ANY keyword");
		Object[] promptArray = (Object[]) Util.newArray("java.lang.Object", 4);
		promptArray[0] = new String("Please enter keywords");
		promptArray[1] = textInput;
		promptArray[2] = radio1;
		promptArray[3] = radio2;
		buttonGroup = new ButtonGroup();
		buttonGroup.add(radio1);
		buttonGroup.add(radio2);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		optionPane = new JOptionPane(promptArray, 
											  JOptionPane.QUESTION_MESSAGE,
											  JOptionPane.YES_NO_OPTION,
											  null,
											  options,
											  options[0]);
		setContentPane(optionPane);
		pack();
		SearchPropertyChangeListener changeListener = new SearchPropertyChangeListener();
		optionPane.addPropertyChangeListener(changeListener);
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
				SearchResultsDialog.show(parentFrame, searchTerm);
			}
			else if (value.equals("Cancel"))
			{
				setVisible(false);
			}
		}
	}
	public static void show(JFrame frame, String searchTerm)
	{
		SearchDialog sd = new SearchDialog(frame, searchTerm);
	}
}
