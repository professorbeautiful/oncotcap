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
package oncotcap.display.common;

import java.applet.*;
import java.awt.*;
import java.io.*;

import java.util.*;
import java.text.ParseException;
import javax.swing.*;
import javax.swing.border.*;
import oncotcap.datalayer.*;

// Display an icon and a string for each object in the list.
public class OncListCellRenderer extends JLabel implements ListCellRenderer {
		public Component getListCellRendererComponent
				(
				 JList list,
				 Object value,            // value to display
				 int index,               // cell index
				 boolean isSelected,      // is the cell selected
				 boolean cellHasFocus)    // the list and the cell have the focus
		{
				// If the value is a persistible item use the Display String
				String s = "Empty";
				if (value != null ) {
						if ( value instanceof AbstractPersistible ) {
								s = ((AbstractPersistible)value).toDisplayString();
								setIcon(((AbstractPersistible)value).getIcon());
						}
						else if (value instanceof Class) {
								// Strip the package name 
								s = "New " + oncotcap.util.StringHelper.className(value.toString());
						}
						else 
								s = value.toString();
				}
				setText(s);
				setBorder(new LineBorder(new Color(219,215,215)));
				if (isSelected) {
						setBackground(list.getSelectionBackground());
						setForeground(list.getSelectionForeground());
				}
				else {
						 setBackground(list.getBackground());
						 setForeground(list.getForeground());
				}
				setEnabled(list.isEnabled());
				setFont(list.getFont());
				setOpaque(true);
				
				return this;
		}
}
