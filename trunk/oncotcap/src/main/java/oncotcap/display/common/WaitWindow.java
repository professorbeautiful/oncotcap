/*
 * Created on Jun 23, 2004
 *
 */
package oncotcap.display.common;

import javax.swing.*;
import java.awt.Color;
import oncotcap.util.TcapColor;

/**
 * @author day
 *
 */
public class WaitWindow extends JWindow
{
	public static WaitWindow theWaitWindow;
	private JFrame owner;
	private JLabel lblTitle;
	private JLabel lblDetail;
	public JProgressBar progressBar, progressBarL, progressBarR, progressBarT, progressBarB;
	private boolean withProgressBar = true;
	private Color panelColor = TcapColor.lightBlue;

	public WaitWindow(JFrame owner)
	{
		this(owner, "Please wait . . .");
	}
	public WaitWindow(JFrame owner, String title)
	{
			this(owner, title, true, TcapColor.orange);
	}
	public WaitWindow(JFrame owner, String title, boolean withProgressBar,
										Color panelColor)
	{
		super(owner);
		theWaitWindow = this;  // only one should show at a time.
		
		this.owner = owner;
		this.withProgressBar = withProgressBar;
		setBackground(panelColor);
		getContentPane().setBackground(TcapColor.silver);
		getContentPane().getInsets();
		setSize(320,150);
		lblTitle = new JLabel(title, SwingConstants.CENTER);
		add(lblTitle);
		lblTitle.setSize(getWidth()-2,20);
		lblTitle.setLocation(10,15);
		lblTitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		lblTitle.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		getContentPane().setLayout(null);
		int progWidth = 12;
		progressBarR = new JProgressBar();
		progressBarR.setSize(progWidth, getHeight());
		progressBarR.setLocation((getWidth()-progressBarR.getWidth()), 0);
		progressBarR.setIndeterminate(true);
		progressBarL = new JProgressBar();
		progressBarL.setSize(progressBarR.getSize());
		progressBarL.setLocation(0, 0);
		progressBarL.setIndeterminate(true);
		progressBarT = new JProgressBar();
		progressBarT.setSize(getWidth() - 2*progWidth, progWidth);
		progressBarT.setLocation(progWidth, 0);
		progressBarT.setIndeterminate(true);
		progressBarT.setOrientation(JProgressBar.VERTICAL);
		progressBarB = new JProgressBar();
		progressBarB.setSize(progressBarT.getSize());
		progressBarB.setLocation(progWidth, getHeight()-progWidth);
		progressBarB.setIndeterminate(true);
		progressBarB.setOrientation(JProgressBar.VERTICAL);
		lblDetail= new JLabel();
		add(lblDetail);
		lblDetail.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		lblDetail.setVisible(false);
		getContentPane().add(lblDetail);
		getContentPane().add(progressBarR);
		getContentPane().add(progressBarL);
		getContentPane().add(progressBarT);
		getContentPane().add(progressBarB);
		if ( withProgressBar ) {
			progressBar = new JProgressBar();
			int progBarH = 20;
			int progBarW = 70;
			progressBar.setSize(progBarW, progBarH);
			progressBar.setLocation(getWidth()/2-progBarW/2, getHeight()/2-progBarW/2);
			progressBar.setIndeterminate(true);
			getContentPane().add(progressBar);
		}
		}
	public void setProgress(double percent){
		progressBar.setMaximum(100);
		progressBar.setMinimum(0);
		progressBar.setValue(Math.round(Math.round(percent * 100)));
	}
	public void setText(String text){
		lblTitle.setText(text);
	}
	public void setDetail(String text){
		showDetail(text);
	}
	/**
	 * @param s - running commentary on the process progressing.
	 */
	public void showDetail(String s){
		if(lblDetail.isVisible()==false){
			lblDetail.setSize(getWidth()-2,20);
			lblDetail.setLocation(15,progressBar.getY()/2 + getHeight()/2);
			lblDetail.setVisible(true);
			lblDetail.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		}
		//setSize(200,getHeight() + 20); 
		lblDetail.setText(s);
	}
	public void setVisible(boolean visibility)
	{
		if(visibility)
			localSetLocation();
		progressBarR.setIndeterminate(visibility);
		progressBarL.setIndeterminate(visibility);
		progressBarT.setIndeterminate(visibility);
		progressBarB.setIndeterminate(visibility);
		progressBar.setIndeterminate(visibility);
		super.setVisible(visibility);
	}
	private void localSetLocation()
	{
		if ( owner != null ) {
		int xLoc = ((int) owner.getLocation().getX()) + owner.getWidth()/2 - getWidth()/2;
		int yLoc = ((int) owner.getLocation().getY()) + owner.getHeight()/2 - getHeight()/2;
		setLocation(xLoc, yLoc);
		}
	}
	
	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		jf.setSize(500,300);
		jf.setVisible(true);
		WaitWindow ww = new WaitWindow(jf);
		ww.setVisible(true);
	}
}
