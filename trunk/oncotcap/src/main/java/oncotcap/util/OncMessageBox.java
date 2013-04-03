package oncotcap.util;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.net.*;

import oncotcap.display.editor.persistibleeditorpanel.OncEditorPane;

public class OncMessageBox extends JDialog implements ActionListener
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
	private String message = null;
	private URL messageFile = null;
	private boolean textSet = false;
	private boolean urlSet = false;
	private JPanel pnlMessage = new JPanel();
	private JPanel pnlButtons = new JPanel();
	private JButton btnOk = new JButton("OK");
	private JLabel lblIcon;
	private OncEditorPane htmlMessage;
	private int screenWidth;
	private int screenHeight;
	private int type;
	
	protected boolean dismiss = true;

	private OncMessageBox(URL url)
	{
		super();
		this.messageFile = url;
		urlSet = true;
		this.type = JOptionPane.PLAIN_MESSAGE;
		this.title = "OncoTCap";
		init();
	}
	protected OncMessageBox(Frame owner, URL message, String title, int type)
	{
		super(owner, title);
		this.messageFile = message;
		urlSet = true;
		this.title = title;
		this.type = type;
		init();
	}
	protected OncMessageBox(Frame owner, URL message, String title, int type, boolean modal)
	{
		super(owner, title, modal);
		this.messageFile = message;
		urlSet = true;
		this.title = title;
		this.type = type;
		init();
	}

	private OncMessageBox(String text)
	{
		super();
		this.message = text.toString();
		textSet = true;
		this.type = JOptionPane.PLAIN_MESSAGE;
		this.title = "OncoTCap";
		init();
	}
	protected OncMessageBox(Frame owner, Object message, String title, int type)
	{
		super(owner, title);
		this.message = message.toString();
		textSet = true;
		this.title = title;
		this.type = type;
		init();
	}
	protected OncMessageBox(Frame owner, Object message, String title, int type, boolean modal)
	{
		super(owner, title, modal);
		this.message = message.toString();
		textSet = true;
		this.title = title;
		this.type = type;
		init();
	}
	private void init()
	{
		setTitle(title);
		setResizable(false);
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
		pnlMessage.setLayout(null);
		pnlMessage.setBackground(BACKGROUND_COLOR);
		pnlMessage.setLocation(0,0);
		if(textSet)
			htmlMessage = new OncEditorPane(this.message);
		else if(urlSet)
			htmlMessage = new OncEditorPane(this.messageFile);
		htmlMessage.setBackground(BACKGROUND_COLOR);
		pnlMessage.add(htmlMessage);
		htmlMessage.setEditable(false);
		if(icon != null)
		{
			lblIcon = new JLabel(icon);
			lblIcon.setSize(32,32);
			lblIcon.setPreferredSize(new Dimension(32,32));
			lblIcon.setLocation(10,10);
			htmlMessage.setLocation(52,16);
			pnlMessage.add(lblIcon);
			screenWidth = htmlMessage.getPreferredWidth() + 72;
			screenHeight = htmlMessage.getPreferredHeight() + 16 + BUTTON_HEIGHT + 35;
		}
		else
		{
			htmlMessage.setLocation(10,10);
			screenWidth = htmlMessage.getPreferredWidth() + 25;
			screenHeight = htmlMessage.getPreferredHeight() + 10 + BUTTON_HEIGHT + 40;
		}

		setSize(screenWidth, screenHeight);
		pnlMessage.setSize(screenWidth, htmlMessage.getPreferredHeight() + 10);
		pnlMessage.setPreferredSize(new Dimension(screenWidth, htmlMessage.getPreferredHeight() + 10));
		pnlButtons.setSize(screenWidth, BUTTON_HEIGHT + 5);
		pnlButtons.setPreferredSize(new Dimension(screenWidth, BUTTON_HEIGHT + 5));
		pnlButtons.setLocation(0, htmlMessage.getPreferredHeight() + 10);
		htmlMessage.setSize(htmlMessage.getPreferredWidth(), htmlMessage.getPreferredHeight());
		htmlMessage.setPreferredSize(new Dimension(htmlMessage.getPreferredWidth(), htmlMessage.getPreferredHeight()));
		htmlMessage.setVisible(true);
		htmlMessage.revalidate();
		setLocation();
		pnlMessage.add(htmlMessage);
		getContentPane().setLayout(null);
		getContentPane().add(pnlMessage);
		getContentPane().add(pnlButtons);
		setVisible(true);
		validate();
//		setResizable(false);
		repaint();
	}
	private void setLocation()
	{
		Rectangle bnds = getGraphicsConfiguration().getBounds();
		setLocation((((int) bnds.getWidth()) - getWidth())/2, (((int) bnds.getHeight())- getHeight())/2);
	}
	public static OncMessageBox showMessageDialog(Frame parent, Object message, String title, int type)
	{
		return(new OncMessageBox(parent, message, title, type, true));
	}
	public static OncMessageBox showMessageDialog(Frame parent, Object message, String title, int type, boolean modal)
	{
		return(new OncMessageBox(parent, message, title, type, modal));
	}
	public static OncMessageBox showMessageDialog(Frame parent, URL message, String title, int type)
	{
		return(new OncMessageBox(parent, message, title, type, true));
	}
	public static OncMessageBox showMessageDialog(Frame parent, URL message, String title, int type, boolean modal)
	{
		return(new OncMessageBox(parent, message, title, type, modal));
	}
	private final static JFrame jf = new JFrame();
	
	public static OncMessageBox showWarning(Object message, String title)
	{
		return(new OncMessageBox(jf, message, title, JOptionPane.WARNING_MESSAGE, true));
	}
	public static OncMessageBox showError(Object message, String title)
	{
		return(new OncMessageBox(jf, message, title, JOptionPane.ERROR_MESSAGE, true));
	}
	public static void main(String [] args)
	{
		Logger.setConsoleLogging(true);
		JFrame jf = new JFrame();
		jf.setSize(300,300);
		jf.setVisible(true);
		OncMessageBox omb;
//		String url = "http://www.yahoo.com";
		String url = "file:///u:\\java\\tcap\\oncotcap\\test\\test.html";
		omb = showWarning("HEY THIS IS A WARNING", "Test warning");
//		try
//		{
//			omb = showMessageDialog( null, new URL("file:///u:\\oncotcap\\TcapData\\resource_files\\AUC_info.html"), "BLECHHHHH", JOptionPane.WARNING_MESSAGE, false);
//			omb = showMessageDialog( null, new URL(url), "BLECHHHHH", /* JOptionPane.WARNING_MESSAGE */ -7, false);
//			omb.setSize(300,300);
//		}
//		catch(MalformedURLException e){Logger.log("Malformed URL.  [OncMessageBox.main]");}

		//System.exit(1);
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
