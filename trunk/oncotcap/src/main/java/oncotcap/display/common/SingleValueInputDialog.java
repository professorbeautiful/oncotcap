/*
   * StringInputDialog.java         Version 0.001    2000/12/19
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
import java.awt.event.*;
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
public class SingleValueInputDialog extends UserInputDialog {
	/**
	 ** Input box for getting string input.
	 **/
	protected JTextField textInput;
	
	/**
	 ** Constructor.
	 **/
	public SingleValueInputDialog(JFrame frame, Parameter variable, Observer observer){
		super(frame, observer, variable);
		Logger.log("StringInputDialog created");
	}
	Object[] prompt(){
		Object[] objects = (Object[]) Util.newArray("java.lang.Object", 2);
		objects[0] = new String("Please enter text for " + parameter.getName());
		objects[1] = textInput;
		return(objects);
	}
	void setupEntry(){
		textInput = new JTextField();
		if (parameter.getSingleParameterList() != null)
			textInput.setText(parameter.getSingleParameterList().getFirst().toString());
		optionPane = new JOptionPane(prompt(), 
									 JOptionPane.QUESTION_MESSAGE,
									 JOptionPane.YES_NO_OPTION,
									 null,
									 options,
									 options[0]);
		setContentPane(optionPane);
		
		textInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Logger.log("The text entered was " + textInput.getText());
				optionPane.setValue("Done");
			}
		});
	}
	
	/**
	 ** Validate the input got by this dialog and store it in
	 ** inputValue.  Return true if all is OK.
	 **/
	public boolean validateInput()
	{
		if(textInput.getText().trim().equals(""))
		{
			oncotcap.util.OncMessageBox.showWarning("Please enter a value for " + parameter.getName(), "Invalid Input");			
			return(false);
		}
		return(true);
	}
	public void save()
	{
		if(parameter != null)
		{
			SingleParameterList pList  = parameter.getSingleParameterList();
			if(pList.getSize() != 1)
			{
				oncotcap.util.OncMessageBox.showWarning("Warning: Unable to save parameter, incorrect number of primitives. [SingleValueInputDialog]", "Internal Error");
			}
			else
			{
				pList.getFirst().setDisplayValue(textInput.getText());
			}
		}
	}

	public Parameter getValue()
	{
		if(validateInput())
		{
			save();
			return(parameter);
		}
		else
			return(null);
	}
}
