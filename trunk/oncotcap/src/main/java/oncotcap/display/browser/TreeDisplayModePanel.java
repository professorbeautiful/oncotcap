/*
 * Educational Resource for Tumor Heterogeneity
 *             Oncology Thinking Cap
 *
 * Copyright (c) 2003  University of Pittsburgh
 * All rights reserved.
 *
 *  SourceSafe Info:
 *               $Header: $
 *               Revision: $Revision$
 *               Author: $Author$
 *
 * Code Review History:
 *     (mm.dd.yyyy initials)
 *
 * Test History:
 *     (mm.dd.yyyy initials)
 */
package oncotcap.display.browser;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import javax.swing.*;

import oncotcap.display.browser.*;
import oncotcap.display.common.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.datalayer.*;

public class TreeDisplayModePanel extends JPanel 
		implements ActionListener {
			 public static int NONE = 0;
			 public static int ALL = 1;
			 public static int ROOT = 2;

			 JRadioButton rootOnly = null;
			 JRadioButton none = null;
			 JRadioButton all = null;
		ButtonGroup bg = null;
		
		public TreeDisplayModePanel() {
				init();
		}

		private void init() {
				setLayout(new FlowLayout());
				//setBackground(Color.WHITE);
				// Create the radio buttons using the actions
				JLabel label = new JLabel("Apply Filter: " );
			// 	label.setForeground(new Color(109,82,65));
// 				label.setBackground(Color.WHITE);
// 				label.setFont(new Font("Helvetica", Font.BOLD, 12));

				all = new JRadioButton("All");
				all.setToolTipText("<html>Display only tree elements that "
													 + "<br>are linked to the item selected in "
													 + "<br>the tree controller list"
													 + "(Not implemented yet)</html>");
				rootOnly = new JRadioButton("On");
				rootOnly.setToolTipText("<html>Display only tree elements that "
																+ "<br>have a root node that is linked to "
																+ "<br> the item selected in "
																+ "<br>the tree controller list. "
																+ "<br>Example of when this is necessary "
																+ "<br>when a keyword is attached to an "
																+ "<br>info source but not the quote nugget. "
																+ "<br>With this option 'on' the quote nugget "
																+ "<br>can still be displayed with the all "
																+ "<br>option 'on' it would not be visible"
																+ "</html>");
				none = new JRadioButton("Off");
				none.setToolTipText("<html>Do not modify tree when the user selects "
														+ "<br> an item in the Tree Controller list"
														+ "<br> default option. Good for linking "
														+ "<br>existing elements</html>");
 				all.addActionListener(this);
 				rootOnly.addActionListener(this);
 				none.addActionListener(this);
				
				// Associate the two buttons with a button group
				bg = new ButtonGroup();
				//bg.add(all);
				bg.add(rootOnly);
				bg.add(none);	
				// Create a tree controller panel
				GridBagLayout gridBagLayout = new GridBagLayout();
				setLayout(gridBagLayout);
				GridBagConstraints c = new GridBagConstraints();
				//c.gridwidth = GridBagConstraints.REMAINDER;
				add(label,c);
				c.gridwidth = -1;
				//add(all,c);
				add(rootOnly,c);
				add(none,c);

				all.setEnabled(false);
				// Default to none
				ButtonModel model = none.getModel();
				bg.setSelected(model, true);

		}

			 public int getDisplayMode() {
					 if ( none.isSelected() ) 
							 return NONE;
					 else if ( all.isSelected() ) 
							 return ALL;
					 else 
							 return ROOT;
					 
			 }

			 
			 // This method is called whenever the radio button is pressed,
			 // even if it is already selected; this method is not called
			 // if the radio button was selected programmatically
			 public void actionPerformed(ActionEvent evt) {
					 // Perform action
					 OncBrowser.refresh();
			 }

}
