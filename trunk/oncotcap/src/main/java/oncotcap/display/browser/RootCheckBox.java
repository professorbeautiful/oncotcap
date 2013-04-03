package oncotcap.display.browser;

import java.awt.*;
import java.util.*;
import java.lang.reflect.Array;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;

public 	class RootCheckBox extends JCheckBox {
		String ontologyObjectName = null;
		public RootCheckBox(String name, ActionListener buttonPanel,
												ImageIcon unselectedIcon,
												ImageIcon selectedIcon) {
				super(unselectedIcon);
				ontologyObjectName = name;
				setDisabledIcon(unselectedIcon);
				setSelectedIcon(selectedIcon);
				addMouseListener( new MouseInputAdapter() {
								public void mouseEntered(MouseEvent e) {
										setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
								}
								public void mouseExited(MouseEvent e)	{
										setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
								}
						});
				setBorder(new EmptyBorder(new Insets(0,0,0,0)));
				setBackground(new Color(235,232,227));
				addActionListener(buttonPanel);
				
		}
		public String getName() {
				return ontologyObjectName;
		}
		public void fireActionPerformed(ActionEvent e)
		{
				super.fireActionPerformed(e);
		}
}
