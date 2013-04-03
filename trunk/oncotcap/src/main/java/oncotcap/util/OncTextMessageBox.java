package oncotcap.util;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;


public class OncTextMessageBox extends JDialog implements ActionListener
{
	public static final int ERROR_MESSAGE = JOptionPane.ERROR_MESSAGE;
	public static final int INFORMATION_MESSAGE = JOptionPane.INFORMATION_MESSAGE;
	public static final int WARNING_MESSAGE = JOptionPane.WARNING_MESSAGE;
	public static final int QUESTION_MESSAGE = JOptionPane.QUESTION_MESSAGE;
	public static final int PLAIN_MESSAGE = JOptionPane.PLAIN_MESSAGE;

	protected static final int BUTTON_HEIGHT = 30;
	protected static final Color BACKGROUND_COLOR = TcapColor.lightGray;

	private Icon icon = null;
	private String title = null;
	private JPanel pnlMessage = new JPanel();
	private JPanel pnlButtons = new JPanel();
	private JButton btnOk = new JButton("OK");
	private JLabel lblIcon;
	private JTextArea messagePane;
	private int type = ERROR_MESSAGE;
	
	protected boolean dismiss = true;

	protected OncTextMessageBox(String title, String text, int type)
	{
		super((Frame) null, title, true);
		this.type = type;
		this.title = title;
		init(text);
	}

	private void init(String text)
	{
		setTitle(title);
		setSize(800, 600);
		addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){
			setVisible(false);
			dismiss = true;
		}});
		switch(type)
		{
			case ERROR_MESSAGE:
			{
				icon = OncoTcapIcons.getImageIcon("error.gif");
			}
			break;
			case INFORMATION_MESSAGE:
			{
				icon = OncoTcapIcons.getImageIcon("infosmall.gif");
			}
			break;
			case WARNING_MESSAGE:
			{
				icon = OncoTcapIcons.getImageIcon("warning.gif");
			}
			break;
			case QUESTION_MESSAGE:
			{
				icon = OncoTcapIcons.getImageIcon("question.gif");
			}
		}
		setBackground(BACKGROUND_COLOR);
		pnlButtons.setLayout(new FlowLayout());
		pnlButtons.setBackground(BACKGROUND_COLOR);
		btnOk.setSize(50, BUTTON_HEIGHT);
		btnOk.setPreferredSize(new Dimension(60, BUTTON_HEIGHT));
		btnOk.addActionListener(this);
		pnlButtons.add(btnOk);
		pnlMessage.setLayout(new BorderLayout());
		pnlMessage.setBackground(BACKGROUND_COLOR);
		pnlMessage.setLocation(0,0);
		messagePane = new JTextArea();
		messagePane.setLineWrap(false);
		messagePane.setText(text);
		pnlMessage.add(messagePane);
		messagePane.setEditable(true);
		if(icon != null)
		{
			lblIcon = new JLabel(icon);
			lblIcon.setSize(32,32);
			lblIcon.setPreferredSize(new Dimension(32,32));
			JPanel iconPanel = new JPanel();
			iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.X_AXIS));
			iconPanel.add(lblIcon);
			iconPanel.add(Box.createHorizontalGlue());
			pnlMessage.add(iconPanel, BorderLayout.NORTH);
		}
		else
		{
			messagePane.setLocation(10,10);
		}

		setLocation();
		JScrollPane messageSB = new JScrollPane(messagePane);
		pnlMessage.add(messageSB);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(pnlMessage, BorderLayout.CENTER);
		getContentPane().add(pnlButtons, BorderLayout.SOUTH);
		setVisible(true);
		validate();
		repaint();
	}
	private void setLocation()
	{
		Rectangle bnds = getGraphicsConfiguration().getBounds();
		setLocation((((int) bnds.getWidth()) - getWidth())/2, (((int) bnds.getHeight())- getHeight())/2);
	}
	public static OncTextMessageBox showErrorMessage(String title, String message)
	{
		return(new OncTextMessageBox(title, message, JOptionPane.ERROR_MESSAGE));
	}

	public static void main(String [] args)
	{
		Logger.setConsoleLogging(true);
		JFrame jf = new JFrame();
		jf.setSize(300,300);
		jf.setVisible(true);
		OncTextMessageBox omb;
		String url = "file:///u:\\java\\tcap\\oncotcap\\test\\test.html";
		omb = showErrorMessage("Some title", "HEY THIS IS A WARNING");
	}

	protected void waitForDismiss()
	{
		dismiss = false;
		while(!dismiss)
		{
			try{Thread.sleep(100);}
			catch(InterruptedException e){Logger.log("Thread Interrupted " + e);}
		}
	}
	protected void addButton(JButton btn)
	{
		pnlButtons.add(btn);
		pnlButtons.revalidate();
		repaint();
		Logger.log("button added");
	}
	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();
		if (o instanceof JButton)
		{
			setVisible(false);
			dismiss = true;
		}
	}
}