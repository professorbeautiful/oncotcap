package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import oncotcap.util.*;

/**
 ** <p>
 ** OncEditorPane adds the ability to set the "PreferredSize" of the
 ** pane via HTML attributes or javascript of the target HTML file.  The attributes height and
 ** width are added to the &lt;body> tag.  The javascript window.sizeTo
 ** function has been implemented in a simple form.
 ** </p>
 ** <blockquote>
 ** <p>To set the PreferredSize to 150, 150 via HTML use the
 ** following:<br><br>
 **   &lt;html><br>
 **   &nbsp;&nbsp;&nbsp; &lt;body height=150 width=150><br>
 **   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; .......<br>
 **   &nbsp;&nbsp;&nbsp; &lt;/body><br>
 **   &lt;html><br></p>
 **   <p>
 **   To set the Preferred size to 200, 200 via javascript use the
 **   following:<br><br>
 **   &lt;html><br>
 **   &nbsp;&nbsp;&nbsp; &lt;head><br>
 **   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;SCRIPT LANGUAGE="JavaScript"><br>
 **   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;!-- Begin<br>
 **   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
 **   window.resizeTo(500,500)<br>
 **   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; //  End --><br>
 **   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &lt;/script><br>
 **   &nbsp;&nbsp;&nbsp; &lt;/head><br>
 **   &nbsp;&nbsp;&nbsp; &lt;body><br>
 **   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; .....<br>
 **   &nbsp;&nbsp;&nbsp; &lt;/body><br>
 **   &lt;html></p>
 ** </blockquote>
 ** <p><br>
 ** The directive that is last in the target HTML file will take
 ** precedence.</p>
 ** 
 ** <p>
 ** The default preferred size of this component is (250,350)</p>
 **
 **/

public class OncEditorPane extends JEditorPane
{
	private String statement;
	private int bundleIndex = 0;
	private URL url;
	private boolean autoSize = true;

	private int preferredWidth = 350;
	private int preferredHeight = 250;
	private Dimension prefSize = null;
	private FontMetrics fontMetrics;
	private boolean followBackground = false;
	private static JFrame parent;
	public boolean addedToPanel = false;
	
	
	private static HTMLEditorKit.Parser parser = new javax.swing.text.html.parser.ParserDelegator();
	MyHTMLDocument.MyHTMLReader callback;
	
	public static void main(String [] args)
	{
		OncEditorPane cse = null;
		Logger.setConsoleLogging(true);
		JFrame jf = new JFrame();
		jf.setVisible(true);
		cse = new OncEditorPane("<html><body height=250 width=350>The initial dose will <a href=someref width=10 height=20>la la la</a> blah blah blah blah.</body></html>");
//		try{cse = new OncEditorPane(new URL("file:///u:\\java\\tcap\\oncotcap\\test\\test.html"));}
//		catch(MalformedURLException e){Logger.log("Malformed URL");}
		jf.getContentPane().add(cse);
		cse.setLocation(0,0);
// 		cse.setSize(cse.getPreferredWidth(), cse.getPreferredHeight());
		cse.setPreferredSize(new Dimension(cse.getPreferredWidth(), cse.getPreferredHeight()));
		jf.setSize(new Dimension(cse.getPreferredWidth(), cse.getPreferredHeight()));
		Logger.log("SIZE: " + cse.getPreferredWidth() + "," + cse.getPreferredHeight());
		cse.revalidate();
		jf.validate();
		jf.repaint();
	}
	public OncEditorPane()
	{
		init();
	}
	public OncEditorPane(String txtDisplay)
	{
		statement = txtDisplay;
		init();
		setText(txtDisplay);
	}
	public OncEditorPane(URL url)
	{
		init();
		setPage(url);
	}
	public void setText(String text)
	{
		super.setText(text);
		refreshStatement();
	}

	public void setPage(URL url)
	{
		try{super.setPage(url);}
		catch(IOException e){Logger.log("Cannot open page " + url + " IOException");}
		this.url = url;
		refreshStatement();
	}
	public void setFollowBackgroundColor(boolean follow)
	{
		followBackground = follow;
	}
	private void init()
	{
		setEditorKit(new HTMLEditorKit());
		setEditable(false);
		setBackground(TcapColor.lightBrown);
		setAutoscrolls(false);
		callback = (MyHTMLDocument.MyHTMLReader) ((MyHTMLDocument)(new MyHTMLEditorKit()).createDefaultDocument()).getReader(0);
	}
	public void refreshStatement()
	{
		try
		{
			if(url != null)
				parser.parse(new URLReader(url), callback, true);
			else
				parser.parse(new StringReader(statement), callback, true);
		}
		catch(IOException e){Logger.log("IOException: cannot open " + url);}
		catch(EmptyStackException e)
		{
			if(url != null)
				Logger.log("empty stack in " + url + " [OncEditorPane]");
			else
				Logger.log("empty stack in " + statement + " [OncEditorPane]");
		}
		if (callback.preferredWidth > 0)
		{
			preferredWidth = callback.preferredWidth;
		}
		if (callback.preferredHeight > 0)
			preferredHeight = callback.preferredHeight;
		if (callback.backgroundColor != null)
		{
			setBackground(callback.backgroundColor);
			repaint();
		}
	}

	public void setAutoSize(boolean size)
	{
		autoSize = size;
	}
	public Dimension getPreferredSize()
	{
		if (autoSize)
			return(null);
		else
			return(super.getPreferredSize());
	}
	public int getPreferredWidth()
	{
		if(autoSize)
			return(preferredWidth);
		else
			return((int)super.getPreferredSize().getWidth());
	}
	public int getPreferredHeight()
	{
		if(autoSize)
			return(preferredHeight);
		else
			return((int)super.getPreferredSize().getHeight());
	}
}

class MyHTMLEditorKit extends HTMLEditorKit
{
	public Document createDefaultDocument() {
		StyleSheet styles = getStyleSheet();
		StyleSheet ss = new StyleSheet();
		ss.addStyleSheet(styles);
		MyHTMLDocument doc = new MyHTMLDocument(ss);
		doc.setParser(getParser());
		doc.setAsynchronousLoadPriority(0);
		doc.setTokenThreshold(100);
		return doc;
	}
}

class MyHTMLDocument extends HTMLDocument
{
	MyHTMLDocument(StyleSheet ss)
	{
		super(ss);
	}

	public HTMLEditorKit.ParserCallback getReader(int pos)
	{
		Object desc = getProperty(Document.StreamDescriptionProperty);
		if (desc instanceof URL) { 
			setBase((URL)desc);
		}
		MyHTMLReader reader = new MyHTMLReader(pos);
		return reader;
	}

	public class MyHTMLReader extends HTMLDocument.HTMLReader
	{
		private boolean foundJScript = false;
		private static final String jScript = "JavaScript";
		private static final String lag = "LANGUAGE";
		private static final String strHeight = "height";
		private static final String strWidth = "width";
		private static final String strBackgroundColor = "bgcolor";
		
		int preferredWidth = -1;
		int preferredHeight = -1;
		Color backgroundColor = null;
		
		public MyHTMLReader(int offset)
		{
			super(offset, 0, 0, null);
		}
		private void setBackgroundColor(String color)
		{
			backgroundColor = TcapColor.getColor(color);
		}
		private void parseForSizeTo(char [] data)
		{
			int idx;
			int idxl;
			int xCoord, cIdx, yCoord;
			String script = StringHelper.removeWhiteSpace(new String(data).toLowerCase());
			String coords;
			if((idx = script.indexOf("window.resizeto(")) > 0)
			{
				idx = idx + 16;
				idxl = script.indexOf(")", idx);
				if(idxl > idx + 3)
				{
					coords = script.substring(idx, idxl);
					cIdx = coords.indexOf(',');
					preferredWidth = new Integer(coords.substring(0,cIdx)).intValue();
					preferredHeight = new Integer(coords.substring(cIdx+1)).intValue();
				}
			}
		}

		public void handleComment(char[] data, int pos)
		{
			if(foundJScript)
			{
				parseForSizeTo(data);
			}
			super.handleComment(data,pos);
		}

		public void handleEndOfLineString(String eol)
		{
			super.handleEndOfLineString(eol);
		}

		public void handleEndTag(HTML.Tag t, int pos)
		{
			super.handleEndTag(t,pos);
		}

		public void handleError(String errorMsg, int pos)
		{
			super.handleError(errorMsg,pos);
		}

		public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos)
		{
			super.handleSimpleTag(t,a,pos);
		}

		public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos)
		{
			String val;
			Object tag;
			String tagName;
			String strHref = null;
			boolean lookForJScript = false;
			boolean lookForBodyAttribs = false;
			foundJScript = false;
			if(t == HTML.Tag.SCRIPT)
				lookForJScript = true;
			else if(t == HTML.Tag.BODY)
				lookForBodyAttribs = true;
			
			super.handleStartTag(t,a,pos);
			java.util.Enumeration en = a.getAttributeNames();
			while(en.hasMoreElements())
			{
				tag = en.nextElement();
				tagName = tag.toString().trim();
				val = a.getAttribute(tag).toString().trim();
				if(lookForJScript && lag.equalsIgnoreCase(tagName) && jScript.equalsIgnoreCase(val))
					foundJScript = true;
				else if(lookForBodyAttribs && strHeight.equalsIgnoreCase(tagName))
					preferredHeight = (new Integer(val)).intValue();
				else if(lookForBodyAttribs && strWidth.equalsIgnoreCase(tagName))
					preferredWidth = (new Integer(val)).intValue();
				else if(lookForBodyAttribs && strBackgroundColor.equalsIgnoreCase(tagName))
					setBackgroundColor(val);
			}
			super.handleStartTag(t, a, pos);
		}
		public void handleText(char[] data, int pos)
		{
			super.handleText(data,pos);
		}
	}
}

