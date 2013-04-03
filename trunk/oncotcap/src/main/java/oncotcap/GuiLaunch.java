package oncotcap;

import java.awt.*;
import java.awt.event.*;

import oncotcap.display.browser.OncBrowser;

public class GuiLaunch
{
	public static void main(String [] args)
	{
		String javaVersion = System.getProperty("java.version").trim();
		if(! javaVersion.startsWith("1.5"))
		{
			System.err.println("Failed check for java version 1.5.\nFound version " + javaVersion + " at " + System.getProperty("java.home"));
			ErrorFrame eFrame = new ErrorFrame();
			eFrame.setVisible(true);
		}
		else
			OncBrowser.start(args);		
	}
	
	static class ErrorFrame extends Dialog
	{
		ErrorFrame()
		{
			super(new Frame());
			Rectangle bnds = getGraphicsConfiguration().getBounds();
			setLocation((((int) bnds.getWidth()) - 500)/2, (((int) bnds.getHeight())- 300)/2);
			this.setSize(300,180);
			this.setTitle("Oncotcap");
			this.setBackground(Color.yellow);
			Label label1 = new Label("Java Version 1.5 is required for this software.");
			Label label2 = new Label("Please ensure that Java 1.5 is installed on");
			Label label3 = new Label("your system and rerun the installation program.");
			label1.setSize(300,10);
			label2.setSize(300,10);
			label3.setSize(300,15);
			label1.setLocation(20,40);
			label2.setLocation(20,70);
			label3.setLocation(20,85);
			this.setLayout(null);
			this.add(label1);
			this.add(label2);
			this.add(label3);
			
			Button okay = new Button("OK");
			okay.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					System.exit(1);
				}
			});
			okay.setSize(60, 30);
			okay.setLocation(120, 120);
			this.add(okay);
			
			this.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e)
				{
					System.exit(1);
				}
			});
		}
	}
}
