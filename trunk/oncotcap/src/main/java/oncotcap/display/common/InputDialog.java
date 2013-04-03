package oncotcap.display.common;

import javax.swing.*;
import java.beans.*;
import oncotcap.util.*;

public class InputDialog extends JDialog {

	protected JTextField textInput;
	protected JFrame parentFrame;
	protected JOptionPane optionPane;
	private String title;
	private String returnVal;
	final String btnString1 = "Done";
	final String btnString2 = "Cancel";
	Object[] options = {btnString1, btnString2};
	
	public InputDialog(JFrame frame, String title){
		super(frame, title, true);
		this.title = title;
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
		Object[] promptArray = (Object[]) Util.newArray("java.lang.Object", 2);
		promptArray[0] = new String(title);
		promptArray[1] = textInput;
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		optionPane = new JOptionPane(promptArray, 
											  JOptionPane.QUESTION_MESSAGE,
											  JOptionPane.YES_NO_OPTION,
											  null,
											  options,
											  options[0]);
		setContentPane(optionPane);
		optionPane.addPropertyChangeListener(new InputPropertyChangeListener());
		pack();
	}
	class InputPropertyChangeListener implements PropertyChangeListener {
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
				returnVal = textInput.getText();
				setVisible(false);
			}
			else if (value.equals("Cancel"))
			{
				returnVal = null;
				setVisible(false);
			}
		}
	}

	public static InputDialog show(JFrame frame, String title)
	{
		return(new InputDialog(frame, title));
	}
	public static String getValue()
	{
		return(getValue("Enter value."));
	}
	public static String getValue(String title)
	{
		InputDialog id = new InputDialog(SwingUtil.getModeFrame(), title);
		return(id.getInput());
	}

	public String getInput()
	{
		return(returnVal);
	}
}
