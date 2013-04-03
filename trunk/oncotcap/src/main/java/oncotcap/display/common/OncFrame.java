package oncotcap.display.common;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.reflect.*;
import oncotcap.display.browser.*;

public class OncFrame extends JFrame  {
		private static boolean showGUID = true;
		private Container contentPane;
		private Component framedComponent = null;
		private WindowMenu windowMenu = null;

		public OncFrame() {
				init();
		}
		public OncFrame(Component component) {
				init();
				contentPane.add(component, BorderLayout.CENTER);
		}				
		
		private void init()
		{
				//setSize(300,300);
				setIconImage(oncotcap.util.OncoTcapIcons.getDefault().getImage());
				contentPane = getContentPane();
				contentPane.setLayout(new BorderLayout());
				JPanel menuPanel = new JPanel(new BorderLayout());
				JMenuBar menuBar = new JMenuBar();
				windowMenu = new WindowMenu("Windows"); 
				menuBar.add(windowMenu);
				menuPanel.add(menuBar, BorderLayout.NORTH);		
				contentPane.add(menuPanel, BorderLayout.NORTH);
				addWindowListener(WindowMenu.getWindowListener());
		}
		
		public void addFrameableComponent(Component component, String title, ImageIcon icon){
				addFrameableComponent(component);
				if ( icon != null )
						setIconImage(icon.getImage());
				if ( title != null ) 
						setTitle(title);
		}
		
		public void addFrameableComponent(Component component) {
				framedComponent = component;
				contentPane.add(component, BorderLayout.CENTER);
				if ( component instanceof OncFrameable ) {
						if ( ((OncFrameable)component).getImageIcon() != null )
								setIconImage(((OncFrameable)component).getImageIcon().getImage());
						setTitle(((OncFrameable)component).getTitle());
				}
				else {
						setIconImage(oncotcap.util.OncoTcapIcons.getDefault().getImage());
						setTitle(component.toString());
				}
				// Pass the focus to the editorPanel
				// This allows the keymaps to work even when in editors
				oncotcap.display.common.InitialFocusSetter.setInitialFocus(this, component);
				requestFocus();
		}

		public Component getFramedComponent() {
				return framedComponent;
		}
		public WindowMenu getWindowMenu() {
				return windowMenu;
		}
}
