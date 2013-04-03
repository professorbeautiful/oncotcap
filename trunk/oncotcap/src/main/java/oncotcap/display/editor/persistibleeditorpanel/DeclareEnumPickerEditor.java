package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Hashtable;
import java.awt.event.*;
import oncotcap.util.GUID;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.Persistible;
import oncotcap.display.browser.OncBrowser;
import oncotcap.display.browser.TreeDisplayModePanel;
import oncotcap.util.CollectionHelper;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.display.common.KeywordChooser;
import oncotcap.display.common.OncTreeNode;

public class DeclareEnumPickerEditor extends EditorPanel 
		implements ActionListener
{
	private DeclareEnumPicker enumPicker;
	private KeywordChooser keywordChooser;
	private JTextField nameField;
	private JRadioButton booleanExpression = 
			new JRadioButton("Boolean Expression");
	private JRadioButton assignment = new JRadioButton("Assignment");
	private JRadioButton instantiation = new JRadioButton("Instantiation");
	private JCheckBox allowMultiples = new JCheckBox("Allow multiple values?");
	private ButtonGroup buttonGroup = new ButtonGroup();
		private JRadioButton currentSelectedType = null;
	
	public DeclareEnumPickerEditor()
	{
		init();
	}
	public DeclareEnumPickerEditor(DeclareEnumPicker picker)
	{
		init();
		edit(picker);
	}
	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		jf.setSize(300,800);
		jf.getContentPane().add(new DeclareEnumPickerEditor(new DeclareEnumPicker()));
		jf.setVisible(true);
	}
	private void init()
	{
		setLayout(null);
		setPreferredSize(new Dimension(430,200));
		JLabel nameLabel = new JLabel("Display name");
		nameField = new JTextField();
		nameField.setMaximumSize(new Dimension(130, 25));

		keywordChooser = new KeywordChooser();
		keywordChooser.setRootKeyword(KeywordFilter.CHARACTERISTIC_ROOT);
		JLabel subLabel1 = new JLabel("Subset will be defined by");
		JLabel subLabel2 = new JLabel("attributes of type:");
		nameLabel.setSize(100,25);
		nameField.setSize(115,25);
		subLabel1.setSize(155,25);
		subLabel2.setSize(155,25);
		keywordChooser.setSize(200, 25);
		nameLabel.setLocation(40,10);
		nameField.setLocation(170,10);
		subLabel1.setLocation(10,15);
		subLabel2.setLocation(10,30);
		keywordChooser.setLocation(175, 22);
		
		// Ask user how to restrict the picker and what type of variable 
		// output 
		JPanel pickerPanel = new JPanel(new GridLayout(2,1));
		JPanel pickerTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel typeLabel = new JLabel("Use as ...");
		pickerTypePanel.add(typeLabel);
		pickerTypePanel.add(booleanExpression);
		pickerTypePanel.add(assignment);
		pickerTypePanel.add(instantiation);
		//instantiation.setEnabled(false);
		booleanExpression.addActionListener(this);
		assignment.addActionListener(this);
		instantiation.addActionListener(this);
		buttonGroup.add(booleanExpression);
		buttonGroup.add(assignment);
		buttonGroup.add(instantiation);
		pickerPanel.add(allowMultiples);
		pickerPanel.add(pickerTypePanel);
		pickerPanel.setSize(400,50);
		pickerPanel.setLocation(10,50);
		add(subLabel1);
		add(subLabel2);
		add(keywordChooser);
		add(pickerPanel);
	}
	public void edit(Object obj)
	{
		if(obj instanceof DeclareEnumPicker)
			edit((DeclareEnumPicker) obj);
	}
	public void edit(DeclareEnumPicker picker)
	{
		this.enumPicker = picker;
		keywordChooser.setKeyword(enumPicker.getAttributeBaseKeyword());
		nameField.setText(enumPicker.getName());
		allowMultiples.setSelected(enumPicker.getAllowMultiples());
		setPickerType(enumPicker.getPickerType());
	}
 public int getPickerType() {
		 if ( booleanExpression.isSelected() ) {
							 return DeclareEnumPicker.BOOLEAN_EXPRESSION;
		 }
		 else if ( assignment.isSelected() ) {
							 return DeclareEnumPicker.ASSIGNMENT;
		 }
		 else { 
				 return DeclareEnumPicker.INSTANTIATION;
		 }
					 
	}

	public void setPickerType(JRadioButton radio) {
			if ( radio == null ) 
					radio = booleanExpression;
			buttonGroup.setSelected(radio.getModel(), true);
			currentSelectedType = radio;
	}
	public void setPickerType(int type) {
			if (type == DeclareEnumPicker.BOOLEAN_EXPRESSION)
					 setPickerType(booleanExpression);
			else if ( type == DeclareEnumPicker.ASSIGNMENT ) 
					setPickerType(assignment);
			else
					setPickerType(instantiation);
	}
		public void actionPerformed(ActionEvent ae) {
				if ( ae.getSource().equals(assignment) ||
						ae.getSource().equals(instantiation) ||
						 ae.getSource().equals(booleanExpression) ) {
						// if their are already instances based on this declareEnumPicker
						// then do not allow user to change base type
						OncFilter filter = new OncFilter();
						filter.getRootNode().addChild(enumPicker, getSaveToDataSourceOnCreate());
						
						Persistible pers = 
								oncotcap.Oncotcap.getDataSource().find(enumPicker.getGUID());
						 // This is incorrect but kind of works because instance 
						 // has not saved on original creation and exists 
						/* buggy if (pers != null ) {
								JOptionPane.showMessageDialog
										((JFrame)null, 
										 "<html>"
										 + "Error: Cannot change picker type."
										 + "</html>");
								// set the radio button back
								setPickerType(currentSelectedType);
								return;
						}
						*/
				}
				if ( ae.getSource().equals(assignment) ) {
						// allow multiples must be turned off
						allowMultiples.setSelected(false);
				}
				if ( ae.getSource().equals(instantiation) ) {
						// If the user is assigning or instantiating force a one one 
						// editor - may just be necessary for assignment
				}
				currentSelectedType = (JRadioButton)ae.getSource();
		}

	public Object getValue()
	{
		return(enumPicker);
	}

	public void save()
	{
		enumPicker.setAttributeBaseKeyword(keywordChooser.getKeyword());
		enumPicker.setPickerType(getPickerType());
		enumPicker.setAllowMultiples(allowMultiples.isSelected());
	}
}
