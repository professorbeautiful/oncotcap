/*
   * UserInputDialog.java         Version 0.001    2000/12/19
   *
   * Copyright (c) 2000 The University of Pittsburgh Cancer Institute
   * All rights reserved.
   *
   * This software is the confidential and proprietary information of
   * The University of Pittsburgh Cancer Institute  ("Confidential
   * Information").   You shall not disclose such Confidential
   * Information and  shall use it only in accordance with the terms of
   * the license agreement you entered into with the Cancer Institute
   *
   *
   */

package oncotcap.display.common;

import javax.swing.*;
import java.awt.*;
import java.beans.*;
import java.util.*;

import oncotcap.util.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;

/**
 ** This dialog window will be used to get user input when the user clicks on
 ** a italicised word representing a variable in the @param
 ** CurrentStatementEditor.
 ** 
 ** @author Sailesh Ramakrishnan.
 ** @version 0.001
 ** @date 200/12/19
 **/
abstract public class UserInputDialog extends JDialog {
	/**
	 ** Internal pane that contains the UI components.
	 **/ 	 
	protected JOptionPane optionPane;
	/**
	 ** Prompt String for the Variable whose value is being requested.
	 **/
	protected String promptString;
	protected Parameter parameter;
	final String btnString1 = "Done";
	final String btnString2 = "Cancel";
	final String btnString3 = "Help";
	Object[] options = {btnString1, btnString2, btnString3};
	Object[] promptArray;
	/**
	 ** Pointer to the Observer that is causing this dialog to pop up.
	 ** Usually a CurrentStatementEditorLabel.
	 **/
	protected Observer observer;
	/**
	 ** Pointer to the String that stores the input from this dialog.
	 **/
	protected String inputValue;
	/**
	 ** Constructor.
	 **/
	JFrame parentFrame;
//	CurrentStatementEditor cse;
	public UserInputDialog(JFrame frame, Observer observer){
		this(frame, observer, null);
	}
	public UserInputDialog(JFrame frame, Observer observer, Parameter parameter)
	{
		super(frame, true);
		this.parameter = parameter;
		this.observer = observer;
		init();
		if(frame != null)
		{
			setLocation( (int) (frame.getLocation().getX() + frame.getWidth()/2 - getWidth()/2),
						 (int) (frame.getLocation().getY() + frame.getHeight()/2 - getHeight()/2));
		}
		else
		{
			Rectangle bnds = getGraphicsConfiguration().getBounds();
			setLocation((((int) bnds.getWidth()) - getWidth())/2, (((int) bnds.getHeight())- getHeight())/2);
		}

		setVisible(true);
	}
	static public void makeDialog(JFrame parent, Parameter parameter, Observer observer)
	{
		UserInputDialog me = parameter.getParameterType().getEditor();
	}
	/**
	 ** Initialization.
	 **/ 
	public void init() {
		Logger.log("In the init of user Input dialog");
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setupEntry();
		promptArray = prompt();
		optionPane = new JOptionPane(promptArray, 
									 JOptionPane.QUESTION_MESSAGE,
									 JOptionPane.YES_NO_OPTION,
									 null,
									 options,
									 options[0]);
		setContentPane(optionPane);
		pack();
		myPropertyChangeListener mpcl = new myPropertyChangeListener();
		optionPane.addPropertyChangeListener(mpcl);
	}
	protected int getIconWidth()
	{
		if(optionPane == null)
			return(0);
		else
		{
			Icon ic = optionPane.getIcon();
			if(ic == null)
				return(0);
			else
				return(ic.getIconWidth());
		}
	}
	/**
	 ** Variable-type-specific setup actions.
	 **/
	abstract  void setupEntry();
	/** Create an array of Objects to add to the JOptionPane;
	 **/
	abstract Object [] prompt();
	/**
	 ** Validate the input got by this dialog and store it in
	 ** inputValue.  Return true if all is OK.
	 **/
	abstract  boolean validateInput();
	abstract Parameter getValue();
	abstract void save();
  
	/**
	 ** A custom property change listener inner class.
	 **/ 
	class myPropertyChangeListener implements PropertyChangeListener {
		/**
		 ** Property Change event handler.
		 **/
		public void propertyChange(PropertyChangeEvent e) {
			String prop = e.getPropertyName();
			Logger.log("Property was Changed");
			if (isVisible() 
				  && (e.getSource() == optionPane)
				  && (prop.equals(JOptionPane.VALUE_PROPERTY) ||
					  prop.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {
				Object value = optionPane.getValue();

				if (value == JOptionPane.UNINITIALIZED_VALUE) {
						//ignore reset
					return;
				}
					// Reset the JOptionPane's value.
					// If you don't do this, then if the user
					// presses the same button next time, no
					// property change event will be fired.
				optionPane.setValue(
									JOptionPane.UNINITIALIZED_VALUE);				
				if (value.equals("Done")) {
					Logger.log("Done button was pressed");
					boolean inputIsOK = validateInput();
					if (inputIsOK){
//						Logger.log("Reading inputValue = " + inputValue());
//						observer.update(new Observable(), inputValue());
						save();
						setVisible(false);
					}
					else {
						return;
					}					
				}
				else if (value.equals("Cancel")) {
					Logger.log("Cancel button was pressed");
					UserInputDialog.this.setVisible(false);
				}
			}
		}
	}
}
