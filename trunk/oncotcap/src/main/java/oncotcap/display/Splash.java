package oncotcap.display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.TimerTask;
import oncotcap.util.*;
import oncotcap.Oncotcap;

public class Splash extends JWindow
{
	private Screen sc;
	private boolean holdOpen = false;
	private final java.util.Timer timer = new java.util.Timer();

	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		jf.setSize(400,400);
		jf.setVisible(true);
		Splash s = new Splash(jf);
		//Splash s = new Splash();
		s.showSplash();
	}

	public Splash()
	{
		sc = new Screen();
	}

	public Splash(JFrame jf)
	{
		sc = new Screen(jf);
	}
	
	public void showSplash()
	{
		new Thread(sc).start();
	}

	private class Screen extends JWindow implements Runnable
	{
		private JLabel lab = new JLabel();
		private JLabel versionLabel = new JLabel();
		private ImageIcon icon;
		
		Screen()
		{
			setup();
		}
		Screen(JFrame jf)
		{
			super(jf);
			setup();
		}

		private void setup()
		{
			icon = Util.getIcon("splash.gif");
			Box mainBox = Box.createVerticalBox();
			Rectangle bnds = getGraphicsConfiguration().getBounds();
			addKeyListener(new KeyListen());
			addFocusListener(new FocusListen());
			versionLabel.setText("Version " + Oncotcap.getMajorVersion() + "." + Oncotcap.getMinorVersion() + "." + Oncotcap.getBuild());
			versionLabel.setOpaque(true);
			versionLabel.setBackground(TcapColor.mediumBlue);
			versionLabel.setHorizontalTextPosition(SwingConstants.CENTER);
			versionLabel.setHorizontalAlignment(SwingConstants.CENTER);
			versionLabel.setPreferredSize(new Dimension(500,20));
			versionLabel.setMinimumSize(new Dimension(500,20));
			versionLabel.setMaximumSize(new Dimension(500,320));
			versionLabel.setSize(500,20);
			lab.setIcon(icon);
			lab.setVisible(true);
			mainBox.add(lab);
			mainBox.add(versionLabel);
			getContentPane().add(mainBox);
			setSize(500,320);
			setLocation((((int) bnds.getWidth()) - 500)/2, (((int) bnds.getHeight())- 300)/2);
		}
		public void run()
		{
			holdOpen = false;
			setVisible(true);
			toFront();
		/*
		 * set the window to hide after 3 seconds unless the space
		 * bar is being held down.  If the space bar is being held down
		 * wait (check every .1 seconds) until it is released before
		 * hiding the window.
		 */
			timer.scheduleAtFixedRate(new TimerTask()	{	public void run()
															{
																if (!holdOpen)
																{
																	timer.cancel();
																	setVisible(false);
																}				
															}
														},
									  (long) 3000.0, (long) 100.0);

		}
		
	}
	
	private class KeyListen implements KeyListener
	{
		public void keyPressed(KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_SPACE)
				holdOpen = true;
		}
		public void keyReleased(KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_SPACE)
				holdOpen = false;
		}
		public void keyTyped(KeyEvent e){}
	}

	private class FocusListen implements FocusListener
	{
		public void focusGained(FocusEvent e){}
		public void focusLost(FocusEvent e)
		{
			holdOpen = false;
		}
	}
}