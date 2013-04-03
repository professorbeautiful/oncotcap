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

public class LeafCheckBox extends JCheckBox {
		ImageIcon leafNodeSelectedIcon = 
				oncotcap.util.OncoTcapIcons.getImageIcon("visible-selected.jpg");
		ImageIcon leafNodeUnselectedIcon = 
				oncotcap.util.OncoTcapIcons.getImageIcon("visible-unselected.jpg");
		String ontologyObjectName = null;
		public LeafCheckBox(String name, 
												ActionListener buttonPanel, 
												ImageIcon unselectedIcon,
												ImageIcon selectedIcon) {
				super(unselectedIcon);
				ontologyObjectName = name;
				setDisabledIcon(unselectedIcon);
// 				setSelectedIcon(selectedIcon);
// 				setUnselectedIcon(leafNodeUnselectedIcon);
				setSelectedIcon(oncotcap.util.OncoTcapIcons.getImageIcon(name + ".jpg"));
	// 					LCBMouseListener ml = new LCBMouseListener();
// 						addMouseListener(ml);
						
// // 				addMouseListener( new MouseInputAdapter() {
// // 								public void mouseEntered(MouseEvent e) {
// // 										setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
// // 								}
// // 								public void mouseExited(MouseEvent e)	{
// // 										setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
// // 								}
// // 						});
				setBackground(new Color(235,232,227));
				setBorder(new EmptyBorder(new Insets(0,0,0,0)));
				addActionListener(buttonPanel);
		}
// 		private void setUnselectedIcon(Icon icon)
// 		{
// 				unselectedIcon = icon;
// 		}
// 		private void setSelectedIcon(Icon icon)
// 		{
// 				selectedIcon = icon;
// 		}
// 		public boolean isSelected()
// 		{
// 				return(selected);
// 		}
// 		public void addActionListener(ActionListener listener)
// 		{
// 				actionListeners.add(listener);
// 		}
// 		public void fireActionListeners()
// 		{
// 				Iterator it = actionListeners.iterator();
// 				while(it.hasNext())
// 						((ActionListener) it.next()).actionPerformed(new ActionEvent(this, 0, ""));
// 		}

		public String getName() {
				return ontologyObjectName;
		}	

// 		class LCBMouseListener extends MouseAdapter
// 		{
// 				public void mouseEntered(MouseEvent e)
// 				{
// 						setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
// 				}
// 				public void mouseExited(MouseEvent e)
// 				{
// 						setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
// 				}
// 				public void mousePressed(MouseEvent e)
// 				{
// 						selected = ! selected;
// 						if(selected)
// 								setIcon(selectedIcon);
// 						else
// 								setIcon(unselectedIcon);
// 						repaint();
// 						fireActionListeners();
// 				}
// 		}

}
