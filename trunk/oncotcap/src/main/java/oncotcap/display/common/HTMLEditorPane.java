package oncotcap.display.common;
 
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.text.html.HTMLEditorKit.Parser;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.undo.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.awt.event.*;
import oncotcap.util.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.*;
import java.awt.datatransfer.*;
 
public class HTMLEditorPane extends JTextPane implements ClipboardOwner
{
 public static int ser = 0;
 private HTMLEditorPane selfreference;
 public static void main(String [] args)
 {
  JFrame jf = new JFrame();
  JButton btnWrite = new JButton("Write");
  JButton btnInsert = new JButton("Insert");
  JButton btnCopy = new JButton("Copy");
  JButton btnPaste = new JButton("Paste");
  JButton btnAttribs = new JButton("Attribs");
  jf.setSize(new Dimension(200,330));
  jf.getContentPane().setLayout(null);
  jf.setVisible(true);
  final HTMLEditorPane ste = new HTMLEditorPane();
  String testtxt = "<html>\n  <head>\n    \n  </head>\n  <body>\n    ab&nbsp&nbsp\n<br>123  </body>\n</html>";
  testtxt = "<html>\n  <head>\n    \n  </head>\n  <body>\n    <p>\n      Growth is Gompertzian.\n    </p>\n    <p>\n      When the tumor burden is &#160; <a id=\"cfc6d18b0000109000000104a2a353e9Positive.Float\" href=\"cfc6d18b0000109000000104a2a353e9Positive.Float\">refCellCount</a> \n      cells, the mitosis rate equals&#160;the reciprocal of the mean time to \n      mitosis, <a id=\"888e66a80000044d000000fd80eeed66Positive.Float\" href=\"888e66a80000044d000000fd80eeed66Positive.Float\">initialMeanMitosisTime</a> \n      month(s).\n    </p>\n    <p>\n      The mitosis rate decreases exponentially with the total cell population \n      size. The limiting population size is&#160; <a id=\"888e66a80000044f000000fd80f162fePositive.Float\" href=\"888e66a80000044f000000fd80f162fePositive.Float\">plateau</a> \n      . &#160; &#160; &#160; &#160; &#160; &#160; &#160; &#160; &#160; &#160; &#160; &#160; &#160; &#160; &#160;\n    </p>\n  </body>\n</html>";
//  testtxt = "<html>\n  <head>\n    \n  </head>\n  <body>1234567890</body>\n</html>";
  
  ste.setText(testtxt);
  jf.getContentPane().setLayout(new BorderLayout());
  jf.getContentPane().add(ste, BorderLayout.CENTER);
//  ste.setLocation(0,0);
//   ste.setSize(200,200);
//  btnWrite.setLocation(0,200);
//  btnCopy.setSize(100,30);
//  btnCopy.setLocation(0,230);
//  btnPaste.setSize(100,30);
//  btnPaste.setLocation(100,230);
//  btnInsert.setSize(100,30);
//  btnInsert.setLocation(100,200);
//  btnAttribs.setSize(100,30);
//  btnAttribs.setLocation(0,260);
//  btnWrite.setSize(100,30);
  btnWrite.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e)             {
              System.out.println(ste.getText());
             }} );
  btnInsert.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e) {
	  			ste.insertTextAtCaret("Z" + ser++);
             }});
    btnCopy.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){
             }});
    btnPaste.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e)
    {
    	System.out.println(" STRING: " + HTML.Tag.BODY.toString());
    	System.out.println("  BREAK: " + HTML.Tag.BODY.breaksFlow());
    	System.out.println("  BLOCK: " + HTML.Tag.BODY.isBlock());
    	System.out.println("PREFORM: " + HTML.Tag.BODY.isPreformatted());
    	MyTag nt = new MyTag("a");
        HTMLEditorKit kit = (HTMLEditorKit) ste.getEditorKit();
        try{kit.insertHTML((HTMLDocument) ste.getDocument(), ste.getCaretPosition(), "<a href=BLECH>sometext</a>", 0, 0, HTML.Tag.A);}
    	catch(BadLocationException ex){System.out.println("Bad Location");}
        catch(IOException exx){System.out.println("IO Exception");}
    }});
 
    btnAttribs.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
    	
    	HTMLDocument doc = (HTMLDocument) ste.getDocument();
    	int start = ste.getSelectionStart();
    	int end = ste.getSelectionEnd();
    	AttributeSet attribs = ste.getCharacterAttributes();
    	Enumeration atts = attribs.getAttributeNames();
    	while(atts.hasMoreElements())
    		System.out.println(atts.nextElement());
           }});
 
    JPanel buttonPanel = new JPanel();
  jf.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
  buttonPanel.add(btnWrite);
  buttonPanel.add(btnInsert);
  buttonPanel.add(btnCopy);
  buttonPanel.add(btnPaste);
  buttonPanel.add(btnAttribs);
  jf.setVisible(true);
  jf.getContentPane().repaint();
 }
 public HTMLEditorPane()
 {
  init();
 }
 private void init()
 {
	 selfreference = this;
//  HTMLEditorKit edKit = new MyHTMLEditorKit();
	 HTMLEditorKit edKit = new HTMLEditorKit(); 
  setEditorKit(edKit);
  setEditable(true);
  setBackground(TcapColor.lightBrown);
 
  //the following two lines are needed because of a bug in
  //JEditorPane that returns the incorrect position of the cursur
  //before some text has been set in the pane.
  setText("XXXX");
  setText("");  
  this.addKeyListener(new MyKeyListener());
 }
 
 private static String paragraphStartRegexp = "<[pPHhBb][tToO]?[mMdD]?[lLyY]?\\s*[.[^>]]*>";
 private static String paragraphEndRegexp = "</[pPHhBb][tToO]?[mMdD]?[lLyY]?\\s*[.[^>]]*>";
 
 public boolean getScrollableTracksViewportWidth()
 {
  return(false);
 }
 public void cut()
 {
  super.cut();  
//  Clipboard cBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
//  cBoard.setContents(new HTMLTransferable(removeHTMLStartTags(getClipboard())), this);
 }
 private String removeHTMLStartTags(String code)
 {
  String newClip = "";
  if(code != null)
  {
   newClip = code.replaceAll(paragraphStartRegexp, "");
   newClip = newClip.replaceAll(paragraphEndRegexp, "").trim();
  }
  return(newClip);
 }
 
 //required for ClipboardOwner
 public void lostOwnership(Clipboard clipboard, Transferable contents){}
 
 public void paste()
 {
  try
  {
   Position p = getDocument().createPosition(getCaretPosition());
   super.paste();
   ((HTMLDocument) getDocument()).insertAfterEnd(((HTMLDocument) getDocument()).getCharacterElement(p.getOffset()-1), "&nbsp");
  }
  catch(BadLocationException e){System.out.println("Bad location");}
  catch(IOException e){}
//  insertAtPosition(" " + removeHTMLStartTags(getClipboard()) + " ", getCaretPosition());
 }
 
 public void insertTextAtCaret(String textToInsert)
 {
	 HTMLDocument doc = (HTMLDocument) this.getDocument();
	 try{doc.insertString(this.getCaretPosition(), textToInsert, null);}
	 catch(BadLocationException e){System.out.println("Bad Location");}
 }
 public void insertTextAtCaretSAVE(String textToInsert) {
  String insertText = StringHelper.replaceHTMLSpacesAndCRs(textToInsert);
  int caretPos = this.getCaretPosition();
  if(caretPos <= 0) caretPos = 1;
  Matcher mat;
  int position;
  StringBuffer line;
  System.out.println(StringHelper.replaceHTMLSpacesAndCRs(getText()));
//  System.out.println(StringHelper.removeEOL(getText()));
 // line = new StringBuffer(getText());
  line = new StringBuffer(StringHelper.replaceHTMLSpacesAndCRs(getText()));
  Pattern bodyEnd = Pattern.compile("^</body>");
  Pattern brTag = Pattern.compile("^<br>");
  Pattern paragraphStart = Pattern.compile("^\\s*<p\\s*[.[^>]]*>\\s*", Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
  Pattern tagEnd = Pattern.compile("^\\s*</[^a][.[^>]]*>\\s*", Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
  Pattern tagStart = Pattern.compile("^\\s*<[^a][.[^>]]*>\\s*", Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
  Pattern aTagStart = Pattern.compile("^<a[.[^>]]*>", Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
  Pattern aTagEnd = Pattern.compile("^ *</a[.[^>]]*>", Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
  Pattern htmlCharEntity = Pattern.compile("^&[.[^;]]*;");
  Pattern eol = Pattern.compile("[\\n\\r\\f]+");
  Pattern spaces = Pattern.compile("^\\x0B*", Pattern.DOTALL);
  Pattern spacesL = Pattern.compile("^ +[\\n\\r\\f]*");
  Pattern spacesR = Pattern.compile("^[\\n\\r\\f]* +");
  Pattern spacesLR = Pattern.compile("^ +[\\n\\r\\f]* +");
  Pattern oneChar = Pattern.compile(".");
  int caret = 0;
  int previousPosition = 0;
  position = 0;
  String originalLine = new String(line);
  Boolean same = new Boolean(true);
  boolean hitEndBodyTag = false;
  while(line.length() > 0 && caret != caretPos && !hitEndBodyTag)
  {
   if( (mat = paragraphStart.matcher(line)).lookingAt()) {
    caret = advanceTheCaret("para", caret, mat);
   }
   else if((mat = bodyEnd.matcher(line)).lookingAt()) {hitEndBodyTag = true;}
   else if((mat = brTag.matcher(line)).lookingAt())
   { /*skip("brTag", mat);}*/caret = advanceTheCaret("brTag", caret, mat);}
   else if((mat = aTagStart.matcher(line)).lookingAt()) {skip("aTagStart", mat);}
   else if((mat = aTagEnd.matcher(line)).lookingAt()) {skip("aTagEnd", mat);}
   else if((mat = tagEnd.matcher(line)).lookingAt()) {skip("tagEnd", mat);}
   else if((mat = tagStart.matcher(line)).lookingAt()) {skip("tagStart", mat);}
   else if((mat = spacesLR.matcher(line)).lookingAt())
    { caret = advanceTheCaret("spacesLR", caret, mat); }
   else if((mat = spacesR.matcher(line)).lookingAt())
    { caret = advanceTheCaret("spacesR", caret, mat); }
   else if((mat = spacesL.matcher(line)).lookingAt())
    { caret = advanceTheCaret("spacesL", caret, mat);}
   else if((mat = htmlCharEntity.matcher(line)).lookingAt())
    { caret = advanceTheCaret("htmlChar", caret, mat);}
   
   else if((mat = eol.matcher(line)).lookingAt()) {skip("eol", mat);}
   else if((mat = oneChar.matcher(line)).lookingAt()) 
    { caret = advanceTheCaret("oneChar", caret, mat);}
    //System.out.println(line.toString().replaceAll("\r","\\r").replaceAll("\n","\\n"));
   else  
    System.out.println("ERROR- no match in insertAtPosition()");
   
   previousPosition = position;
   position = position + mat.end();
   line = line.delete(0,mat.end());
   
//   int lengthdiff = line.length() -originalLine.substring(position).length(); 
//  if(lengthdiff != 0) {
//   System.out.println("!" + lengthdiff);
//   }
  }
//  System.out.println("Caret Position: " + caretPos);
//  System.out.println("Position: " + previousPosition);
//  System.out.println(StringHelper.removeEOL(getText()));
  String newText = new StringBuffer(StringHelper.replaceHTMLSpacesAndCRs(getText())).insert(previousPosition, insertText).toString();
  setText(newText);
  if(textToInsert.equalsIgnoreCase("<br>") || textToInsert.equalsIgnoreCase("&nbsp;"))
	  this.setCaretPosition(caretPos);
 }
 private void skip(String whatWasFound, Matcher mat) {
  //System.out.println("found " + whatWasFound + " /" + mat.group() + " /");
 }
 private int advanceTheCaret(String whatWasFound, int caret, Matcher mat) {
  caret++;   /// was "insertTest".
  //System.out.println("advanceTheCaret:" + caret + ": " + whatWasFound + ": /" + mat.group() + "/");
  return(caret);
 }
/* public void insertAtPosition(String insertText, int insertPosition)
 {
  if(insertPosition == 0) insertPosition = 1;
  int position = getTextPositionFromCaretPosition(insertPosition);
  //if(insertTest == insertPosition) {
  String newText = new StringBuffer(getText()).insert(position, insertText).toString();
  setText(newText);
  String textAfterSetting = getText();
  //System.out.println("Before setting text: " + newText);
  //System.out.println("After  setting text: " + textAfterSetting);
   // It appears that extra \r\n and space are added where a space appears in the parameter name.
   // We need to troubleshoot setText() some day.
   // Probably related to the "paste" problem - you just don't see it in a parameter.
  //}  else if(insertTest < insertPosition)   // Probably no longer needed.  insertAtEnd(insertText);
 } */
 public void setText(String t) { //override?
  super.setText(t);
 }
 public String getText() { //override?
  return(super.getText());
 }
 
 private final static Pattern htmlLineEndPattern = Pattern.compile("\\s*</p>\\s*</body>\\s*</html>\\s*$", Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
 public void insertAtEnd(String insertText)
 {
  Matcher lineEndMatcher = htmlLineEndPattern.matcher(getText());
  setText(lineEndMatcher.replaceAll(insertText));
 }
 
 private String getClipboard()
 {
  Reader in = null;
  Transferable stuff = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(this);
 
  DataFlavor [] flavors = stuff.getTransferDataFlavors();
  for(int n = 0; n < flavors.length; n++)
  {
   String strStuff = "";
   String mimeType = flavors[n].getMimeType();
   Class repClass = flavors[n].getRepresentationClass();
   if(flavors[n].getMimeType().trim().startsWith("text/html") &&
      flavors[n].getRepresentationClass().equals(String.class))
   {
    try{strStuff = (String) stuff.getTransferData(flavors[n]);}
    catch(UnsupportedFlavorException e){return(null);}
    catch(IOException e){return(null);}
    return(strStuff);
   }
  }

  try{ in = DataFlavor.selectBestTextFlavor(stuff.getTransferDataFlavors()).getReaderForText(stuff);}
  catch(UnsupportedFlavorException e){}
  catch(IOException e){}  
  if(in != null)
  {
   int rIn = 0;
   StringBuffer bufString = new StringBuffer();
   while(rIn != -1)
   {
    try{rIn = in.read();}
    catch(IOException e){return(null);}
    if(rIn >= 0)
     bufString.append((char) rIn);
   }
   return(new String(bufString));
  }
  return(null);
 }
/* class MyHTMLEditorKit extends HTMLEditorKit
 {
     public void read(Reader in, Document doc, int pos) throws IOException, BadLocationException {
 
      if (doc instanceof HTMLDocument) {
          HTMLDocument hdoc = (HTMLDocument) doc;
          Parser p = getParser();
          if (p == null) {
       throw new IOException("Can't load parser");
          }
          if (pos > doc.getLength()) {
           System.err.println("read:   Error: pos=" + pos +  "  doc.getLength()=" + doc.getLength());
       throw new BadLocationException("Invalid location", pos);
          }
 
          ParserCallback receiver = hdoc.getReader(pos);
          Boolean ignoreCharset = (Boolean)doc.getProperty("IgnoreCharsetDirective");
          p.parse(in, receiver, (ignoreCharset == null) ? false : ignoreCharset.booleanValue());
          receiver.flush();
      } else {
          //super.read(in, doc, pos);
      }
         }
 
 }
 
 class MyHTMLDocument extends HTMLDocument
 {
 
 }*/
 private static DataFlavor htmlFlavor = null;
 static{
  try{htmlFlavor = new DataFlavor("text/html; class=java.lang.String");}
  catch(ClassNotFoundException e){System.out.println("Cannot create HTML data flavor!");}
 }
 protected static DataFlavor [] flavors = {htmlFlavor, DataFlavor.stringFlavor};
 class HTMLTransferable implements Transferable
 {
 
  String html;
  public HTMLTransferable(String data)
  {
   html = data;
  }
  public Object getTransferData(DataFlavor flavor)
  {
   if(isDataFlavorSupported(flavor))
    return(html);
   else
    return(null);
  }
  public DataFlavor[] getTransferDataFlavors()
  {
   return(flavors);
  }
  public boolean isDataFlavorSupported(DataFlavor flavor)
  {
   DataFlavor [] flavors = getTransferDataFlavors();
   for(int n = 0; n < flavors.length; n++)
    if(flavors[n] == flavor)
     return(true);
 
   return(false);
  }
 
 } 
 //!!!!!
 private class MyKeyListener implements KeyListener
 {
  public void keyPressed(KeyEvent e)
  {
	  if(e.getKeyCode() == KeyEvent.VK_V && e.isControlDown())
	  {
		  e.consume();
		  try{((HTMLEditorKit)getEditorKit()).insertHTML((HTMLDocument) getDocument(), getCaretPosition(), "<font>" + removeHTMLStartTags(getClipboard()) + "</font>", 0, 0, HTML.Tag.FONT);}
		  catch(BadLocationException ex){System.out.println("Bad Location");}
		  catch(IOException exx){System.out.println("IO Exception");}
	  }
//	  else if(e.getKeyCode() == KeyEvent.VK_C && e.isControlDown())
//	  {
//		  e.consume();
//		  Clipboard cBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
//		  cBoard.setContents(new HTMLTransferable(removeHTMLStartTags(getClipboard())), selfreference);
//	  }

//   if(e.getKeyCode() == KeyEvent.VK_SPACE)
//   {
// 		e.consume();
// 		insertTextAtCaret("&nbsp;");
// 		//System.out.println(getText());
//   }
//   else if(e.getKeyCode() == KeyEvent.VK_ENTER)
//   {
//	   e.consume();
//	   insertTextAtCaret("<br>");
//  }
//   else if(e.getKeyCode() == KeyEvent.VK_V && e.isControlDown())
//   {
//	   e.consume();
//	   System.out.println("PASTE");
//   }   
  }
  public void keyReleased(KeyEvent e){}
  public void keyTyped(KeyEvent e){} 
 }
 //!!!!!
}

class MyTag extends HTML.Tag 
{
	public MyTag(String name)
	{
		super("body");
	}
	public boolean breaksFlow()
	{
		return(true);
	}
	public boolean isBlock()
	{
		return(true);
	}
	public boolean isPreformatted()
	{
		return(false);
	}
}