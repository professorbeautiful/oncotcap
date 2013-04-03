package oncotcap.display.common;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.text.*;

public class EditableComboBox extends JPanel {
    static JFrame frame;
    JLabel result;
    public String currentItem;
	 public Hashtable listItems = null;
	 public JComboBox comboBox;
	 public Vector listVector = null;	 
	 
    public EditableComboBox() {
		  // Set a default list of items
		  listItems = new Hashtable();
		  //        listItems.put("All", new GenericAction());

		  // Setup
		  init();
	 }

	 public EditableComboBox(Hashtable listOfItems) {
		  listItems = listOfItems;
		  init();
	 }

	 public EditableComboBox(Vector listOfItems) {
		  listVector = listOfItems;
		  init();
	 }
	 
	 public void init() {
		  // Make sure the list has at least one item
		  
		  // Convert the keywords into a vector to use in the 
		  // defaultcomboboxmodel
		  if ( listVector == null)
				listVector = new Vector();
		  if (listItems != null) {
				for (Enumeration e = listItems.keys() ; e.hasMoreElements() ;) {
					 listVector.addElement((String)e.nextElement());
				}
		  }
		  if ( listVector.size() > 0)
				currentItem = (String)listVector.elementAt(0);
 
		  DefaultComboBoxModel comboBoxModel = 
				new DefaultComboBoxModel(listVector);
        comboBox = new JComboBox(comboBoxModel);
        comboBox.setEditable(true);
        comboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                String newSelection = (String)cb.getSelectedItem();
                currentItem = newSelection;
					 execute(e);
					 // CHANGE THIS TO execute for generic purposes
                //reformat();
					 addToList();
            }
        });
    
    
        // Lay out everything
        JPanel patternPanel = new JPanel();
        patternPanel.setLayout(new BoxLayout(patternPanel, BoxLayout.Y_AXIS));
        patternPanel.add(comboBox);
    
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        patternPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
        add(patternPanel);
    } // constructor

    /** Formats and displays today's date. */
    public void reformat() {

        Date today = new Date();
        SimpleDateFormat formatter = 
           new SimpleDateFormat(currentItem);
        try {
            String dateString = formatter.format(today);
            //result.setForeground(Color.black);
            //result.setText(dateString);
        } catch (IllegalArgumentException iae) {
            //result.setForeground(Color.red);
            //result.setText("Error: " + iae.getMessage());
        }


    }

	 public void addToList() {
		  // Check to see if pattern is already in the list
		  if ( !listVector.contains(currentItem) ) {
				// Add to the list
				//System.out.println("adding pattern: " + currentItem);
				comboBox.addItem(currentItem);
		  }
	 }

	 public void execute(ActionEvent e) {
		  // Use a hashtable to determine what action to take. 
		  // The hashtable should contain a mapping between combo list
		  // string and the name of the Action object
		  // If the list string can't be found in the list then the
		  // default behavior is to subset the list bby the strings as
		  // if they were keywords
		  if ( listItems != null ) {
					//System.out.println("WHAT " + listItems.get(currentItem)
					//									 + " HAPPENED " + currentItem);
				AbstractAction action = (AbstractAction)listItems.get(currentItem);
				if (action != null) {
					 action.actionPerformed(e);
				}
		  }
	 }

	 public void setSelectedItem(Object obj) {
		  comboBox.setSelectedItem(obj);
	 }

	 public Object getSelectedItem() {
		  return comboBox.getSelectedItem();
	 }

	 public int getSelectedIndex() {
		  return comboBox.getSelectedIndex();
	 }

    public static void main(String s[]) {
         frame = new JFrame("EditableComboBox");
         frame.addWindowListener(new WindowAdapter() {
             public void windowClosing(WindowEvent e) {
                 System.exit(0);
             }
         });
 
         frame.setContentPane(new EditableComboBox());
         frame.pack();
         frame.setVisible(true);
    }
}
