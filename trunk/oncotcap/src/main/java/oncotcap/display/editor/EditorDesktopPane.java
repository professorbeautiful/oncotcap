package oncotcap.display.editor;
import javax.swing.*;


public class EditorDesktopPane extends JDesktopPane {
		public EditorDesktopPane() {
				super();
				StationaryInternalFrame jif = new StationaryInternalFrame("Hello");
				int i= 1;
				jif.setMyBounds(i*20,i*20,100,100);
		}
		

}
