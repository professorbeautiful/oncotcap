package oncotcap.util;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class ScreenHelper {
	static public Dimension getScreenDim(){  
		GraphicsEnvironment ge = GraphicsEnvironment
			.getLocalGraphicsEnvironment();
	   GraphicsDevice[] gs = ge.getScreenDevices();
	   for (int j = 0; j < gs.length; j++) { 
		  //System.out.println("Device " + j);
	      GraphicsDevice gd = gs[j];
	      GraphicsConfiguration[] gc =	gd.getConfigurations();
	      //System.out.println(gc.length + "   configurations ");
	      //System.out.println(gs[j].getDefaultConfiguration());
	      //System.out.println(gs[j].getDefaultConfiguration().getBounds());
	      for (int i=0; i < gc.length; i++) {
		      //System.out.println(gs[j].getConfigurations()[i].getBounds());
			 
/*			 JFrame f = new  JFrame(gs[j].getDefaultConfiguration());
			 Canvas c = new Canvas(gc[i]); 
			 Rectangle gcBounds = gc[i].getBounds();
			 int xoffs = gcBounds.x;
			 int yoffs = gcBounds.y;
			 f.getContentPane().add(c);
			 f.setLocation((i*50)+xoffs, (i*60)+yoffs);
*/	      }
	   }
	   Dimension desktopDim = gs[0].getDefaultConfiguration().getBounds().getSize();
	   return(desktopDim);  // for now.  we'll return the size of screen when we figure out how.
	}
}
