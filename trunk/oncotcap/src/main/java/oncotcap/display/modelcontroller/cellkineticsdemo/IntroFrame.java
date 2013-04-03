package oncotcap.display.modelcontroller.cellkineticsdemo;

import javax.swing.*;
import javax.swing.event.*;
import oncotcap.util.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import oncotcap.Oncotcap;
import oncotcap.display.browser.OncBrowser;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.engine.ModelDefinition;

public class IntroFrame extends JFrame implements ActionListener
{
	private static final int BUTTON_BAR_HEIGHT = 30;
	private JPanel pnlButtons = new JPanel();
	private JButton btnExit = new JButton("Exit");
	//private JButton btnInfo = new JButton("Info");
	private JButton btnNext = new JButton("Next ");
	private JButton btnPrevious = new JButton(" Previous");
	private JButton btnBeginning = new JButton(" Beginning");
	private JButton btnEditor = new JButton("Editor ");
	private ArrayList introFields;
	private int currentlyDisplayed = -1;
	private OncEditorPane txtDisplay = new OncEditorPane();
	private ModelController modelController;
//	private OncTreeModel statements;
	private DefaultInputEditor ne = null;
//	private oncotcap.display.Splash splash;
	private URL currentURL = null;
	
	public static void main(String [] args)
	{
		ModelController mc = (ModelController) Oncotcap.getDataSource().find(new GUID("888e66a3000009a3000000f8dc2f4079"));
		oncotcap.util.Logger.setConsoleLogging(true);
		oncotcap.Oncotcap.handleCommandLine(args);		
		IntroFrame ifr = new IntroFrame(mc);
	}

	public IntroFrame(ModelController mc)
	{
		this(" Oncology Thinking Cap Intro " + Oncotcap.getMajorVersion() + "." + Oncotcap.getMinorVersion() + "." + Oncotcap.getBuild(), mc);
	}

	public IntroFrame(String title, ModelController mc)
	{
		super(title);
		this.modelController = mc;
		introFields = new ArrayList(mc.getIntroScreens());
		init();
	}
	private void nextIntro()
	{
		Logger.log("size = " + introFields.size() + " currentlyDisplayed = " + currentlyDisplayed);
						
		if(introFields.size() >= currentlyDisplayed + 2)
			setDisplayText((String) introFields.get(++currentlyDisplayed));
		else
		{
			gotoEditor();
		}
		if(currentlyDisplayed > 0)
		{
			btnPrevious.setEnabled(true);
			btnBeginning.setEnabled(true);
		}
	}

	private void gotoEditor()
	{
		//splash.setVisible(false);
		//setVisible(false);

		if(ne == null)
		   ne = new DefaultInputEditor(new ModelDefinition(modelController), this);
		else
			ne.setVisible(true);
	}
	private void previousIntro()
	{
		if(currentlyDisplayed > 0)
			setDisplayText((String) introFields.get(--currentlyDisplayed));

		if(currentlyDisplayed <= 0)
		{
			btnPrevious.setEnabled(false);
			btnBeginning.setEnabled(false);
		}
	}

	public void gotoBeginning()
	{
		setDisplayText((String) introFields.get(0));
		currentlyDisplayed = 0;
		btnPrevious.setEnabled(false);
		btnBeginning.setEnabled(false);
	}

	String contentsOfFile (String fileName)
	{
		try {
			File file = new File(fileName);
			FileReader reader = new FileReader(file);
			BufferedReader in = new BufferedReader(reader);
			long length = file.length();
			final int two16 = 2^16;
			char[] cbuf = new char[two16];
			StringBuffer content = new StringBuffer();
			while (length > 0) {
				in.read(cbuf, 0, two16);
				content.append(cbuf);
				length -= two16;
			}
			//Logger.log(content);
			return (new String(content));
		}
		catch ( java.io.FileNotFoundException e) {
			Logger.log(e);
			return("File Not Found! " + fileName);
		}			
		catch ( java.io.IOException e) {
			Logger.log(e);
			return("File Not Found! " + fileName);
		}
	}

	private void setDisplayText(String intro)
	{
		String strDisplay = intro;
		if (strDisplay.substring(0,1).equals("@")) {
			Logger.log("Retrieving statement from file " + strDisplay);
//			strDisplay = contentsOfFile(oncotcap.Oncotcap.getInstallDir() + "TcapData" + File.separator + "resource_files" + File.separator + strDisplay.substring(1));
		}
/*		Instance frag;
		Iterator frags = intro.getInstances("Fragments").iterator();
		while (frags.hasNext())
		{
			frag = (Instance) frags.next();
			strDisplay = strDisplay + StatementBundle.fragString(frag);
		} */
//		txtDisplay.setText(strDisplay);
		//ClassLoader cl = Oncotcap.class.getClassLoader();
		//String curDir = System.getProperty("user.dir");
		
		String infoFileName = "resource/docs/model apps/" + strDisplay.trim();
		
	//	System.out.println("file:///"+oncotcap.Oncotcap.getInstallDir() + "TcapData" + File.separator + "resource_files" + File.separator + strDisplay.trim());
	//	String infoFileName = "file:///"+ curDir + "\\oncotcap\\resource\\docs\\modeling apps" 
	//		+ "\\" + strDisplay.trim();
	//	System.out.println(infoFileName);
	//	try {
	//		URL infoFile = new URL(infoFileName);
	//		System.out.println(infoFile);
			currentURL = Oncotcap.class.getResource(infoFileName);
			txtDisplay.setPage(currentURL);
	//	}
	//	catch(MalformedURLException e){
	//		System.out.println("Malformed URL for intro screen:  \n    "+ infoFileName);
	//	}
/*		try{
			txtDisplay.setPage("file:///"+oncotcap.Oncotcap.getInstallDir() + "TcapData" + File.separator + "resource_files" + File.separator + strDisplay.trim());
		}
		catch(IOException e){
			Logger.log("WARNING: IO Error on file " + strDisplay.trim());
		}
*/	}
	
	private void init()
	{
		oncotcap.Oncotcap.setMainFrame(this);
		
//		addWindowListener(new WindowAdapter(){public void windowClosing(WindowEvent e){
//			System.exit(0);}});

		//splash = new oncotcap.display.Splash(this);
		//splash.showSplash();
		
		addComponentListener(new ResizeListener());
		getContentPane().setLayout(null);
		pnlButtons.setLayout(null);

		ImageIcon ii = OncoTcapIcons.getDefault();
		Image  im = ii.getImage();
		setIconImage(im);
//		setIconImage(OncoTcapIcons.getDefault().getImage());
		setSize(800,600);

		Rectangle bnds = getGraphicsConfiguration().getBounds();

		btnExit.setMnemonic('X');
		btnNext.setMnemonic('N');
		btnPrevious.setMnemonic('P');
		btnBeginning.setMnemonic('B');
		btnEditor.setMnemonic('E');
		btnExit.addActionListener(this);
		btnNext.addActionListener(this);
		btnEditor.addActionListener(this);
		btnPrevious.addActionListener(this);
		btnPrevious.setIcon(OncoTcapIcons.getImageIcon("leftarrow.gif"));
		btnPrevious.setDisabledIcon(OncoTcapIcons.getImageIcon("leftarrow-disabled.gif"));
		btnPrevious.setToolTipText("Go to previous screen");
		btnBeginning.addActionListener(this);
		btnBeginning.setIcon(OncoTcapIcons.getImageIcon("leftarrow-end.gif"));
		btnBeginning.setDisabledIcon(OncoTcapIcons.getImageIcon("leftarrow-end-disabled.gif"));
		btnBeginning.setToolTipText("Go to first screen");
		btnNext.setHorizontalTextPosition(SwingConstants.LEFT);
		btnNext.setIcon(OncoTcapIcons.getImageIcon("rightarrow.gif"));
		btnNext.setDisabledIcon(OncoTcapIcons.getImageIcon("rightarrow-disabled.gif"));
		btnNext.setToolTipText("Go to the next screen");
		btnEditor.setHorizontalTextPosition(SwingConstants.LEFT);
		btnEditor.setIcon(OncoTcapIcons.getImageIcon("rightarrow-end.gif"));
		btnEditor.setDisabledIcon(OncoTcapIcons.getImageIcon("rightarrow-end-disabled.gif"));
		btnEditor.setToolTipText("Go to the editor");
		btnExit.setToolTipText("Exit the program");
		//set button sizes
		btnExit.setSize(105,30);
		//btnInfo.setSize(100,50);
		btnNext.setSize(105,30);
		btnPrevious.setSize(105,30);
		btnBeginning.setSize(110,30);
		btnEditor.setSize(105,30);
		btnPrevious.setEnabled(false);
		btnBeginning.setEnabled(false);
		
		//add buttons to button panel
		pnlButtons.add(btnExit);
		//pnlButtons.add(btnInfo);
		pnlButtons.add(btnNext);
		pnlButtons.add(btnPrevious);
		pnlButtons.add(btnBeginning);
		pnlButtons.add(btnEditor);
		getContentPane().add(pnlButtons);
		txtDisplay.setAutoSize(false);
		txtDisplay.setBackground(TcapColor.gray);
		txtDisplay.setFollowBackgroundColor(true);
		getContentPane().add(txtDisplay);
		pnlButtons.setLocation(0,0);
		txtDisplay.setLocation(0,BUTTON_BAR_HEIGHT);
//		txtDisplay.setEditorKit(new javax.swing.text.html.HTMLEditorKit());
//		txtDisplay.setEditable(false);
		txtDisplay.addHyperlinkListener(new LinkListen());
		Insets inset;
	        Insets marg = txtDisplay.getMargin();
		if(marg == null) 
		    inset = new Insets(0,0,0,0);
		else
		    inset = (Insets) marg.clone();
		inset.left = inset.left + 10;
		inset.right = inset.right + 10;
		txtDisplay.setMargin(inset);
		
		setLocation((((int) bnds.getWidth()) - 800)/2, (((int) bnds.getHeight())- 600)/2);

		resize();
		setVisible(true);
		nextIntro();

	}

	private void resize()
	{
		pnlButtons.setSize(getWidth(), BUTTON_BAR_HEIGHT);
		
		btnExit.setLocation(getWidth() - btnExit.getWidth()-10, 0);
		btnEditor.setLocation(getWidth() - btnEditor.getWidth() - btnExit.getWidth() - 20, 0);
		btnNext.setLocation(getWidth() - btnNext.getWidth() - btnEditor.getWidth() - btnExit.getWidth() - 30, 0);
		btnPrevious.setLocation(getWidth() - btnNext.getWidth() - btnPrevious.getWidth() - btnEditor.getWidth() - btnExit.getWidth() - 40, 0);
		btnBeginning.setLocation(getWidth() - btnNext.getWidth() - btnPrevious.getWidth() - btnBeginning.getWidth() - btnEditor.getWidth() - btnExit.getWidth() - 50, 0);
		

		txtDisplay.setSize(getWidth(), getHeight() - BUTTON_BAR_HEIGHT - 5);
	}

	private class ResizeListener implements ComponentListener
	{

		public void componentHidden(ComponentEvent e) {}
		public void componentMoved(ComponentEvent e) {}
		public void componentResized(ComponentEvent e){ resize(); }
		public void componentShown(ComponentEvent e) {}

	}

	public void actionPerformed(ActionEvent e)
	{
		Object o = e.getSource();
		if (o instanceof JButton)
		{
			JButton button = (JButton) o;
			switch(button.getMnemonic())
			{
				case 'X':
				{
					//System.exit(1);
					dispose();
				}
				break;
				case 'N':
				{
					nextIntro();
				}
				break;
				case 'P':
				{
					previousIntro();
				}
				break;
				case 'B':
				{
					gotoBeginning();
				}
				break;
				case 'E':
				{
					gotoEditor();
				}
				break;
				default:
				{
					Logger.log("WARNING: UNHANDLED BUTTON ACTION IN FieldNavigator");
				}
			}
		}
	}

	private class LinkListen implements HyperlinkListener
	{
		public void hyperlinkUpdate(HyperlinkEvent e)
		{
			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
			{
				if(currentURL != null)
				{
					BrowserLauncher.launch("resource:/" + FileHelper.getDirectory(currentURL.getPath()) + "/" + e.getDescription());
				}
			}
		}
	}
}

