package oncotcap.display.editor;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.reflect.*;
import oncotcap.util.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.Persistible;
import oncotcap.display.browser.AddKeywordsPanel;
import oncotcap.display.browser.OncBrowser;
import oncotcap.display.browser.WindowMenu;
import oncotcap.display.browser.HelpMenu;
import oncotcap.display.editor.persistibleeditorpanel.*;

public class EditorFrame extends JFrame 
{
	private static boolean showGUID = true;
	private Container contentPane;
	private JSplitPane splitPane;
	protected EditorPanel editor;
	private EditorPanel previousEditor = null;
	private Editable returnVal;
	private static Vector allFrames = new Vector();
	private Editable editedObject;
	private boolean isNew = false;
  private static final int PANEL_MAX_WIDTH = 900;
		private WindowMenu windowMenu = null;
		private HelpMenu helpMenu = null;
		private static final int ADD_ON_WIDTH = 50;
		private static final int ADD_ON_HEIGHT = 150;
	private static final int PANEL_MAX_HEIGHT = 700;
		private 	JMenuBar menuBar = null;

	public EditorFrame()
	{ 
		init();
	}
	public EditorFrame(Editable obj)
	{
		init();
		edit(obj);
	}

	private void init()
	{
		
		allFrames.add(this);
		setSize(300,300);
		setIconImage(OncoTcapIcons.getDefault().getImage());
		contentPane = getContentPane();
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setOneTouchExpandable(false);
		splitPane.setDividerSize(10);
		contentPane.setLayout(new BorderLayout());
		contentPane.add(splitPane, BorderLayout.CENTER);
		JPanel menuPanel = new JPanel(new BorderLayout());
		menuBar = new JMenuBar();
		windowMenu = new WindowMenu("Windows");
		menuBar.add(windowMenu);
		menuPanel.add(menuBar, BorderLayout.NORTH);		
		contentPane.add(menuPanel, BorderLayout.NORTH);
		addWindowListener(new MyWindowListener());
		addWindowListener(WindowMenu.getWindowListener());
	}
		public WindowMenu getWindowMenu() {
				return this.windowMenu;
		}
		public HelpMenu getHelpMenu() {
				return this.helpMenu;
		}
	
	public void edit(Editable obj)
	{
	// 	if(previousEditor != null)
// 		{
// 			previousEditor.save();
// 			Object editObj = previousEditor.getValue();
// 			if(editObj instanceof Persistible)
// 				((Persistible) editObj).setCurrentEditor(null);
			
// 			contentPane.remove(previousEditor);
// 		}
			//System.out.println(" what is obj in EDITOR FRAME " + obj);
		editor = obj.getEditorPanel();  // Delete problems happen here.
		// Allow editor forwarding
		if ( obj != null && 
				 editor.getValue() != null &&
				 !obj.getClass().equals(editor.getValue().getClass()) ) {
				obj = (Editable)editor.getValue();
 		}
		editedObject = obj;
		editor.setFocusSavingEnabled(true);

		if(obj instanceof Persistible)
			((Persistible) obj).setCurrentEditor(this);
		updateTitle(obj, showGUID);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(editor);
		//contentPane.add(scrollPane, BorderLayout.CENTER);
		//OncMerge
		splitPane.setTopComponent(scrollPane);
		editor.edit(obj);
		editor.revalidate();
		editor.repaint();
		// Pass the focus to the editorPanel
		// This allows the keymaps to work even when in editors
		oncotcap.display.common.InitialFocusSetter.setInitialFocus(this, editor);

		// INitialize the help menu
		if ( helpMenu == null ) {
				helpMenu = new HelpMenu("Help", editor);
				menuBar.add(helpMenu);
		}
		
		//repaint();
		previousEditor = editor;
		requestFocus();
	}
		public void updateTitle(Object obj, boolean showGUID) { 
				ImageIcon windowIcon = null;
				String strGuid = "";
				
				if(showGUID && obj instanceof Persistible) {
						strGuid = StringHelper.htmlToText(((Persistible) obj).toString());
						windowIcon = ((Persistible) obj).getIcon();
				}

				// Reset window icon if more specific one exists
				if ( windowIcon != null ) 
						setIconImage(windowIcon.getImage());
				if ( obj instanceof Persistible ) {
						setTitle(((Persistible)obj).getPrettyName()
										 + " : " + strGuid);
				}
				else 
						setTitle(StringHelper.className(obj.getClass().toString()) 
										 + " : " + strGuid);
		}
	public static void setShowGUID(boolean show)
	{
		showGUID = show;
	}
	public EditorPanel getEditorPanel()
	{
		return(editor);
	}
	public static EditorFrame getEditor(Editable obj)
	{
		if(obj instanceof Persistible)
			return(((Persistible) obj).getCurrentEditor());
		else
			return(null);
	}
	protected static EditorFrame newFrame(Editable obj)
	{
		return(new EditorFrame(obj));
	}
	public static EditorPanel showEditor(Editable obj)
	{
		return(showEditor(obj, null));
	}

	public static EditorPanel showEditor(Editable obj, Dimension size, 
																			 double x, double y , 
																			 Component relativeComponent)
	{
		EditorFrame frm;
		
		if(! (obj instanceof Persistible))
		{
				System.out.println("Not a persistible " + obj);
			frm = newFrame(obj);
		}
		else
		{
			Persistible pObj = (Persistible) obj;
			if(pObj.getCurrentEditor() == null)
			{
				frm = newFrame(obj);
				if(size != null)
					frm.setSize(size);
				else if ( frm.editor instanceof DefaultEditorPanel ) {
						pObj.addSaveListener(frm.editor);
						// if no default size determine the optimum initial 
						// panel size 
						int editorWidth = 0;
						int editorHeight = 0;
						if(frm.editor.getPreferredSize().getWidth() > 0 
							 && frm.editor.getPreferredSize().getHeight() > 0) {
								editorWidth = (int)frm.editor.getPreferredSize().getWidth();
								editorHeight = (int)frm.editor.getPreferredSize().getHeight();
						}
						else {
								editorHeight = ((DefaultEditorPanel)frm.editor).getMaxHeight();
								editorWidth = ((DefaultEditorPanel)frm.editor).getMaxWidth();
						}
						boolean horizontalScrollBar = false;
						boolean verticalScrollBar = false;
						
						if ( editorWidth > PANEL_MAX_WIDTH ) {
								horizontalScrollBar = true;
						}
						if ( editorHeight > PANEL_MAX_HEIGHT ) {
								verticalScrollBar = true;
						}
						
						frm.editor.setPreferredSize(new Dimension(editorWidth,
																											editorHeight));
						frm.editor.setSize(new Dimension(editorWidth,
																					editorHeight));
						frm.setSize(new Dimension(editorWidth + ADD_ON_WIDTH ,
																			editorHeight + ADD_ON_HEIGHT));
						

				}
				else if(frm.editor.getPreferredSize().getWidth() > 0 
								&& frm.editor.getPreferredSize().getHeight() > 0) {
						//System.out.println("preferred size set " + frm.editor.getPreferredSize());
						frm.setSize(new Dimension((int)frm.editor.getPreferredSize().getWidth()+40, 
																			(int)(frm.editor.getPreferredSize().getHeight()+40)));
				}
				// not a default editor panel
				else {
						//System.out.println("NOT DEFAULT EDITOR PANEL AND A 0 width or height");
				}
			}
			else
			{
				frm = pObj.getCurrentEditor();
			}
		}
		setEditorLocation(obj,size,x,y,relativeComponent, frm);
		frm.setVisible(true);
		
		if( (obj instanceof StatementTemplate) && 
		    (ScreenHelper.getScreenDim().height==600)) {
			// Open maximized if the screen is 800x600. 
			frm.setExtendedState(Frame.MAXIMIZED_BOTH);
		}


		if(frm.getExtendedState() == Frame.ICONIFIED)
		{
			frm.setExtendedState(Frame.NORMAL);
		}
		frm.editor.setMyFrame(frm);
		frm.editor.revalidate();
		frm.repaint();
		frm.requestFocus();

		return(frm.editor);
	}

		static public void setEditorLocation(Editable obj, 
																					 Dimension size, 
																					 double x, double y , 
																					 Component relativeComponent,
																					 EditorFrame frm) {
				if ( relativeComponent != null ) {
						frm.setLocation((int)x,(int)y);
						frm.setLocationRelativeTo(relativeComponent);
						frm.setLocation((int)x
												 +(int)relativeComponent.getLocationOnScreen().getX(),
												 (int)y
												 +(int)relativeComponent.getLocationOnScreen().getY());
				}
				else {
						frm.setLocation((int)x,(int)y);
				}


				// If the editor is off screen shift it a bit
				Dimension screenSize = 
						Toolkit.getDefaultToolkit().getScreenSize();
				int w = frm.getSize().width;
				int h = frm.getSize().height;
				//System.out.println("WHERE ARE WE PUTTING THIS " + frm.getX() + ",  " 
				//									 + frm.getY() );
				// Current location
				if ( frm.getX() < 0 || frm.getY() < 0 
						 || frm.getX()+w > screenSize.width
						 || frm.getY()+h > screenSize.height) {
						// Move it somewhere it can be handled
						//System.out.println("w " + w + "screen width " 
						//+ screenSize.width);
						if ( w > screenSize.width ) {
								x = 10;
								w = (int)(screenSize.width-(x+10));
						}
						if ( h > screenSize.height ) {
								y = 20;
								h = (int)(screenSize.height-(y+10));
						}
						x = 0;
						y = 0;
						frm.setLocation((int)x, (int)y);
						frm.setSize(w,h);
				}

				return;
	}

	public static EditorPanel showEditor(Editable obj, Dimension size)
	{
			return EditorFrame.showEditor(obj, size, 0.0,0.0, null);
	}
	public static void updateAll()
	{
			// Turn of ui notification off because when you do this bulk update 
			// it causes a flicker in the trees
			OncBrowser.setNotifyUIOfPersistibleChanges(false);
			Iterator it = allFrames.iterator();
			while(it.hasNext())
					((EditorFrame) it.next()).save();
			OncBrowser.setNotifyUIOfPersistibleChanges(true);
	}
	private void save()
	{
		if(editor != null && editedObject != null)
		{
			editor.save();
			editedObject.update();
			updateTitle(editedObject, showGUID);
		}
	}
	private void saveAndQuit()
	{
		save();
		setVisible(false);
	}

		private void suggestKeywords() {
				if ( !(editedObject instanceof 
							 oncotcap.datalayer.PersistibleWithKeywords) )
						return;

				if ( ((Persistible)editedObject).getPersistibleState() 
						 == Persistible.UNSET ) {
						
						AddKeywordsPanel addKeywordsPanel = 
								new AddKeywordsPanel(editedObject);
						addKeywordsPanel.setShowKeywords(true);
						addKeywordsPanel.setSize(new Dimension(600, 400)); // modal dialog
						((Persistible)editedObject).setPersistibleState(Persistible.DIRTY); 
						addKeywordsPanel.show();
						addKeywordsPanel.dispose();
						save();

				}
		}

		public void addEditor(JComponent editorPanel, boolean onRight) {
				splitPane.setOneTouchExpandable(true);
				if ( onRight ) 
						splitPane.setBottomComponent(editorPanel);
				else {
						Component topComponent = splitPane.getTopComponent();
						splitPane.setBottomComponent(topComponent);
						splitPane.setTopComponent(editorPanel);
				}
				
				repaint();
		}

		public void placeOnRightSplitPane() {
				splitPane.setOneTouchExpandable(false);
				Component topComponent = splitPane.getTopComponent();
				splitPane.setBottomComponent(topComponent);
				repaint();
		}

	private class MyWindowListener implements WindowListener
	{
		public void windowActivated(WindowEvent e) {}
		public void windowClosed(WindowEvent e)
		{
				save();
			editedObject = null;
		}
		public void windowClosing(WindowEvent e)
		{
				save();
				// If the suggest keywords is ON then suggest a few 
				// If this is a persistible see what the state is 
				// - not implemented yet
				setVisible(false);
				if ( OncBrowser.suggestKeywords() ) 
						suggestKeywords();
		}
		public void windowDeactivated(WindowEvent e)
		{
			
				if(editor != null)
						editor.save();
				updateTitle(editedObject, showGUID);
		}
		public void windowDeiconified(WindowEvent e) {}
		public void windowIconified(WindowEvent e)
		{
			if(editor != null)
				editor.save();
			updateTitle(editedObject, showGUID);
		}
		public void windowOpened(WindowEvent e) {}
	}



}
