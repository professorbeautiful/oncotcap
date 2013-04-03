package oncotcap.display.editor;

import javax.swing.*;

public class StationaryInternalFrame extends JInternalFrame {  
		public StationaryInternalFrame(String Name) { super(Name); }
		public void setBounds(int x, int y, int w, int h) {}
		public void setMyBounds(int x, int y, int w, int h) {
				super.setBounds(x,y,w,h); 
    }
}
