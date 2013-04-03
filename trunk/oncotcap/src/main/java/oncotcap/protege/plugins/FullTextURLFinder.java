package oncotcap.protege.plugins;

import java.util.regex.*;
import java.lang.reflect.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.*;

import oncotcap.util.StringHelper;
import oncotcap.datalayer.Persistible;
import java.util.HashMap;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.awt.Color;
import javax.swing.*;
import javax.swing.text.*;
import java.util.Iterator;

public class FullTextURLFinder {
	static final String [][] fieldCodes = {
		{"T", "title"},
		{"V", "volume"},
		{"I", "issue"},
		{"Y", "year"},
		{"J", "name"},
		{"j", "jURLabbrev"},

		{"P", "pages"},    //FIRST PAGE ONLY; REMOVES LEADING ALPHAS, TRIMS "-.*".
		{"p", "pages"}		//AS-IS.
	};
	static 
		HashMap fieldCodeHashMap = new HashMap();
	static {
		for (int j = 0; j < fieldCodes.length; j++)
			fieldCodeHashMap.put(fieldCodes[j][0], fieldCodes[j][1]);
	}
	static HashMap fieldValuesHashMap;
/***** DONT NEED THESE???
	static final int SUBST=0;   //Use arg, after substituting for any "%" values.
	static final int USETHIS=0; //  "
	static final int CLICK=1;	//SEEK arg, SETURL, and RETRIEVE.
	static final int SEEK=2;	//Seek down (right) for arg; reposition pointer.
	static final int SETURL=3;	//Seek up (left) for HREF= and extract the link.
	static final int HREF=3;	//  "
	static final int RETRIEVE=4;  //Pull contents of current URL into "currentPageContents".
	static final int FINDVAR=5;		//Seek down for name=arg in an "<input type=..." tag. Save as var pair.
	static final int SETAPOSTPAIR=6;	//Seek down for the next key/value pair. Save as var pair.
	static final int SETAVAR=6;			//  "
	static final int SENDPOST=7;  // KLUGE!  send var pairs to OVID.
	static final int FINDFRAME=8; //Seek up for SRC= and extract the link.
	static final int SRC=8;		  //   "
	static final int COMMENT=9;   // Comment.
	static final int REPLACE=10;   // In currentURLString, replace arg1 with arg2.

	static HashMap COMMAND_MAP = new HashMap();
***/

	static java.util.HashMap postHash = new java.util.HashMap();
	static String postArgs = "";
	static String currentURLString;
	static KnowledgeBase kb;
	static Instance journal;
	static Instance article;
	static String [] methodList =  {
		"subst",
		"seek", 
		"seturl", 
		"href", 
		"retrieve", 
		"findVar", 
		"setAPostPair", 
		"setavar", 
		"sendPost", 
		"findFrame", 
		"src",
		"useThis",
		"comment", 
		"replace"
	};
	static URL currentURL;
	static String result;
	static String script;
	static String thisLine;
	static String theRest;
	static String currentPageContents;
	static int	currentPagePosition = 0;
	static String journalHomeLink;
	static String host;

/**	static void fill_COMMAND_MAP() {
		COMMAND_MAP.put("REPLACE", new Integer(REPLACE));
		COMMAND_MAP.put("SEEK", new Integer(SEEK));
		COMMAND_MAP.put("SETURL", new Integer(SETURL));
		COMMAND_MAP.put("HREF", new Integer(HREF));
		COMMAND_MAP.put("RETRIEVE", new Integer(RETRIEVE));
		COMMAND_MAP.put("FINDVAR", new Integer(FINDVAR));
		COMMAND_MAP.put("SETAPOSTPAIR", new Integer(SETAPOSTPAIR));
		COMMAND_MAP.put("SETAVAR", new Integer(SETAVAR));
		COMMAND_MAP.put("SENDPOST", new Integer(SENDPOST));
		COMMAND_MAP.put("FINDFRAME", new Integer(FINDFRAME));
		COMMAND_MAP.put("SRC", new Integer(SRC));
		COMMAND_MAP.put("COMMENT", new Integer(COMMENT));
		COMMAND_MAP.put("#", new Integer(COMMENT));
		COMMAND_MAP.put("REPLACE", new Integer(REPLACE));
	}
	**/

	
	public static void main(String[] args) {
		//FullTextURLFinder.findVar("S");
		if ("-xml".equals(args[0])) {
			String PMID = oncotcap.util.ClipboardHelper.get();
			System.out.println("PMID /" + PMID + "/");
			while (PMID.substring(PMID.length()-1).equals(" ")) {
				PMID = PMID.substring(0, PMID.length() - 1);
				System.out.println("PMID /" + PMID + "/");
			}
			getNewValuesFromPubmed(PMID);
			return;
		}
		if ("-pmid".equals(args[0])) {
			String PMID = oncotcap.util.ClipboardHelper.get();
			System.out.println("PMID /" + PMID + "/");
			while (PMID.substring(PMID.length()-1).equals(" ")) {
				PMID = PMID.substring(0, PMID.length() - 1);
				System.out.println("PMID /" + PMID + "/");
			}
			if (tryHSLSservice( PMID)) {
					String location = null;
				try {
					location = currentURLString;
					System.out.println("Launching URL 2:\n" + location);
					oncotcap.util.BrowserLauncher2.openURL(location);
					reporterFrame.setVisible(false);
				} catch (Exception e){
							JOptionPane.showMessageDialog
									((JFrame)null, 
									 "<html>"
									 + "Error launching " + 
									 location + ": " 
									 + e.getMessage()
									 + "</html>");
					return;
				}
			}
			else {  // try something else
				// 1. extract fields
				// 2. find provider from HSLS page
				// 3. if provider is OVID, proceed.
				// Keep script here. Alternatively,
				// lookup only providers and scripts in
				// protege project, but don't store
				// articles.
			}
			return;
		}
		java.util.Collection errors = new java.util.Vector();
		Project project = Project.loadProjectFromFile("c:\\program files\\oncotcap\\Data\\oncotcap.pprj",
									errors);
		kb = project.getKnowledgeBase();
		java.util.Collection arts = kb.getCls("Article").getDirectInstances();
		java.util.Iterator iter = arts.iterator();
		article = null;
		//String title = "Second-line chemotherapy with irinotecan and vinorelbine";
		String title = "A conformational switch";
		//String title = "Test Article";
		if (args.length > 0) {
			title = args[0];
		}
		System.out.println("Probing for " + title + "\n");
		while(iter.hasNext()) {
			Instance tryarticle = (Instance) iter.next();
			//System.out.println("Retrieved article \n" +
			//				   tryarticle.getOwnSlotValue(kb.getSlot("title")).toString());
			if (tryarticle.getOwnSlotValue(kb.getSlot("title")).toString().startsWith(title)) {
				System.out.println("Got it");
				article = tryarticle;
				break;
			}
		}
		if(article==null) System.out.println("Article not found");
		System.out.println("article title found is\n" +
						   article.getOwnSlotValue(kb.getSlot("title")).toString());
		try {
			String location = find(article);
			System.out.println("Launching URL 1:\n" + location);
			oncotcap.util.BrowserLauncher2.openURL(location);
			reporterFrame.setVisible(false);
			System.out.println("setVisibleFalse");
		} catch (MalformedURLException e)
							  { return; }
		catch (java.io.IOException ioe)
		{ System.out.println("IOException"); return; }
	}
	static JTextPane reporterPane = new JTextPane();
	static JScrollPane reporterScrollPane = new JScrollPane(reporterPane);
	static JFrame reporterFrame = new JFrame("How are you?");

	static Document reporter = reporterPane.getDocument();
	//new DefaultStyledDocument?
	
	static JTextArea reporterArea = new JTextArea(reporter, "Hello!\n\n", 600,500);

	static void report(String s) {
		SimpleAttributeSet attset = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attset, "SansSerif");
		StyleConstants.setFontSize(attset, 14);
		StyleConstants.setForeground(attset, Color.green.darker().darker());
		report(s, attset);
	}
	static void report(String s, SimpleAttributeSet attset) {
		System.out.println("REPORT: " + s);
		//reporterPane.setText(reporterPane.getText() + s + "\n");
		try {
			reporter.insertString(reporter.getLength(),
							  s + "\n",
							 attset);
			reporterArea.repaint();
		}
		catch(Exception e)
		{
				System.out.println("Unable to execute report : " + e.getMessage());
		}
	}

	static SimpleAttributeSet redAttset = new SimpleAttributeSet();
	static {
		StyleConstants.setFontFamily(redAttset, "SansSerif");
		StyleConstants.setFontSize(redAttset, 14);
		StyleConstants.setForeground(redAttset, Color.red);
	//	finalize redAttset;
	}
	
	static void reportError(String s) {
		report(s, redAttset);
	}
	
	static void reportError(Exception e) {
		reportError("EXCEPTION: " + e);
		StackTraceElement st [] = e.getStackTrace();
		for (int i=0; i<Math.min(10,st.length); i++)
			reportError("	" + st[i].toString());
	}
	

	public static String find(Persistible article) {
		// Get the database
		 oncotcap.datalayer.OncoTCapDataSource ds = 
				 oncotcap.Oncotcap.getDataSource();
		 if ( ds instanceof oncotcap.datalayer.ProtegeDataSource) {
				 KnowledgeBase kb = ((oncotcap.datalayer.ProtegeDataSource)ds).getProject().getKnowledgeBase();
				 Instance articleInstance = kb.getInstance(article.getGUID().toString());
				 String location = FullTextURLFinder.find(articleInstance);
				 reporterFrame.setVisible(false);				 
				 return location;
		 }
		 reporterFrame.setVisible(false);
		 return null;
	}

	static boolean tryHSLSservice(Instance article) {
		String PMID = article.getOwnSlotValue(article.getKnowledgeBase().getSlot("PMID")).toString();
		return(tryHSLSservice(PMID));
	}
	static boolean tryHSLSservice(String PMID) {
		//Start a Pitt session.
		try {
			currentURL=new URL("http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?holding=upittlib"
			   + "&cmd=Retrieve&db=PubMed&list_uids="
			   + PMID
			   + "&dopt=Abstract");
			report("tryHSLSservice():  trying URL " + currentURL.toString());
			currentPageContents = getCurrentPageContents(); 
			//report(currentPageContents);
			//theRest = "upittlib";
			theRest = "Click here to read";
			seek();	
			href();
		}
		catch(Exception e) {
				JOptionPane.showMessageDialog
						((JFrame)null, 
						 "<html>"
						 + "Error launching " + 
						 currentURL.toString() + ": " 
						 + e.getMessage()
						 + "</html>");
				return(false);
		}
		report("tryHSLSservice():  URL is now " + currentURL.toString());
		return(true);
	}
	
	public static String find(Instance article){
		FullTextURLFinder.article = article;
		//fill_COMMAND_MAP();
		reporterFrame.getContentPane().add(reporterScrollPane);
		//reporterScrollPane.add(reporterPane);
		reporterFrame.setSize(600,500);
		reporterFrame.setVisible(true);
		report("\n\n==========================");
		report("\n\nI'm fine,how are you???\n");

		kb = article.getKnowledgeBase();
		report("article " + article.getBrowserText());
		journal = (Instance) article.getOwnSlotValue(kb.getSlot("journal"));
		report("journal " + journal.getBrowserText());
		journalHomeLink = (String) journal.getOwnSlotValue(kb.getSlot("journalHomeLink"));
		report("journalHomeLink " + journalHomeLink);
		if (journalHomeLink!=null & !("".equals(journalHomeLink))) {
				System.out.println("journalHomeLink");
				try {
			currentURLString = substituteKeys(journalHomeLink, true);
				}catch ( Exception ee){
						ee.printStackTrace();
				}
			System.out.println("currentURLString " + currentURLString);

			boolean b = setCurrentURL();
			System.out.println("boolean " + b);
			report ("setCurrentURL returns " + b);
		}
		boolean status  = false;	
		status = tryHSLSservice(article);
		if (status) {
				System.out.println("STATUS " + status + " currentURLString " + currentURLString);
				return(currentURLString);   // Hooray, HSLS button works!
		}
		else {
				System.out.println("Not good");
		}

		script = (String) journal.getOwnSlotValue(kb.getSlot("sourceLocatorMethod"));
		// Doesnt get here! Could this be a Java version mismatch???
		//report("Length of script from JOURNAL sourceLocatorMethod slot = " + script.length());
		if (script == null || "".equals(script)) {
			Instance provider = (Instance) journal.getOwnSlotValue(kb.getSlot("fullSourceProvider"));
			report("provider " + provider.getBrowserText());
			script = (String) provider.getOwnSlotValue(kb.getSlot("sourceLocatorMethod"));
			report("Length of script from PROVIDER sourceLocatorMethod slot = " + script.length());
		}
		report("===SCRIPT=======================\n" + script + "\n");
		script = substituteKeys(script, true);
		report("===SCRIPT====after subst========\n" + script + "\n");
		try {
			Method method;
			while(  (method=getNextCommand()) != null ) {
				//method.invoke(null, null);  HARD TO DEBUG.
				String methodName = method.getName();
				if (methodName.equals("subst")) subst();
				else if (methodName.equals("seek")) seek(); 
				else if (methodName.equals("setURL")) setURL(); 
				else if (methodName.equals("href")) href(); 
				else if (methodName.equals("retrieve")) retrieve(); 
				else if (methodName.equals("findVar")) findVar(); 
				else if (methodName.equals("setAPostPair")) setAPostPair(); 
				//else if (methodName.equals("setVar")) setVar(); 
				else if (methodName.equals("sendPost")) sendPost(); 
				else if (methodName.equals("findFrame")) findFrame(); 
				else if (methodName.equals("src")) src();
				else if (methodName.equals("click")) click();
				else if (methodName.equals("useThis")) useThis();
				else if (methodName.equals("comment")) comment(); 
				else if (methodName.equals("replace")) replace(); 
				else report("Unrecognized method " + methodName);
				report("currentURLString is \n" + currentURLString);
				report("currentURL is \n" + currentURL);
				script = script.substring(thisLine.length()+1);
			}
			report("OK, FullTextURLFinder.find() returns\n" + currentURLString);
			reporterFrame.setVisible(false);
			return(currentURLString);
		}
		catch(Exception e) {
			reportError(e);
			report("Upon error, FullTextURLFinder.find() returns\n" + currentURLString);
			reporterFrame.setVisible(false);
			return(currentURLString);
		}
		//finally {
			//report("FullTextURLFinder.find() returns\n" + currentURL.toString());
		//	report("FullTextURLFinder.find() returns\n" + currentURLString);
			//return(currentURL.toString());
		//}
	}
	public static boolean setCurrentURL() {
		try {
			currentURL = new URL(currentURLString);
			report("setCurrentURL: \n  " + currentURL);
			host = currentURL.getHost();
			currentPageContents = getCurrentPageContents();
			//report("setCurrentURL:  currentPageContents is null ?" + (null==currentPageContents));
			//report("setCurrentURL: length of currentPageContents is " + currentPageContents.length());
			return(true);
		}	catch (MalformedURLException urle) {
			report("URL error, " + currentURLString + " " + urle.getMessage());
			return(false);
		}
	}

	static boolean isLetter(char c){
		if (c < 'A') return false;
		if (c > 'z') return false;
		return true;
		//new Matcher(new Pattern(^"\D"), 
	}
	/** Finds the next hyperlink underneath the string "theRest"
	 ** following current location (after SEEK commands),
	 ** and "clicks on it", i.e. replaces currentURL
	 ** and currentPageContents.
	 **/
	public static void comment(){
	}
	public static void click(){
		seek();    //SEEK
		href();		//SETURL
		retrieve();
	}
	
	public static void retrieve() {
			currentPageContents = getCurrentPageContents();  //RETRIEVE
	}
	/** To do:  clickPrev(String the Rest)
	 **/ 
	
	/** Finds the string theRest in currentPageContents, and discards
	 ** anything up to (not including).
	 **/
	public static void seek(){
		report("seek:   currentPagePosition moved from " + currentPagePosition);
		report("seek:  theRest |" + theRest + "|");
		String contentsToUpper = currentPageContents.toUpperCase();
		String theRestToUpper = theRest.toUpperCase();
		String theRestToUpper_rightside = contentsToUpper.substring(currentPagePosition);
		int where = theRestToUpper_rightside.indexOf(theRestToUpper);
		if(where < 0) {
			reportError("seek:  Error:  did not find \"theRest\", " + theRest);
		}
		else {
			currentPagePosition = currentPagePosition + where + theRest.length() + 2;			  
			report("         to " + currentPagePosition);
		}
		//currentPageContents = currentPageContents.substring(pos);
		//report("seek:   50 char to the left of currentPageContents: /"
		//	   + currentPageContents.substring(currentPagePosition-50 ,currentPagePosition-1) + "/");
		//report("seek:   50 char to the right of currentPageContents: /"
		//				   + currentPageContents.substring(currentPagePosition ,currentPagePosition+49) + "/");
	}

	public static void findVar(){
		String key = theRest;
		Pattern p; Matcher m;
		/*p = Pattern.compile("a+"); m = p.matcher("aaaaa");  //GREEDY
		report(m.find() + " " + m.group());
		p = Pattern.compile("a+?"); m = p.matcher("aaaaa"); //RELUCTANT
		report(m.find() + " " + m.group());
		p = Pattern.compile("a++"); m = p.matcher("aaaaa"); //POSSESSIVE
		report(m.find() + " " + m.group());
		*/
		String patt = "<input type=\"*hidden\"* name=\"" + key + "\" value=\"(.*?)\"";
		//String testString = "XYZ <input type=hidden name=\"S\" value=\"PDPKILNKJHJBOL00D\"> XYZ ";
		try {
			p = Pattern.compile(patt);
			m = p.matcher(currentPageContents.substring(currentPagePosition));
			m.find();
			String value=m.group(1);
			//postHash.put(key,value);
			addPair(key,value);
		}
		catch(java.lang.IllegalStateException e) {
			report("No match, pattern is " + patt );
		}
	}
	
	public static void addPair(String key, String value){
		key = StringHelper.substitute(key, " ", "%20");
		value = StringHelper.substitute(value, " ", "%20");
		//postHash.put(key, value);
		if (postArgs == "") postArgs = "?";
		else postArgs = postArgs.concat("&");
		postArgs = postArgs.concat(key + "=" + value);
		report("addPair:   key is " + key + "  value is " + value
						   + "\n       postArgs is " + postArgs);
	}
	

	public static String getTagValue(String tag) {
		String toTheLeft = currentPageContents.substring(0,currentPagePosition);
		
		toTheLeft = toTheLeft.substring(toTheLeft.toUpperCase()
										.lastIndexOf(" " + tag.toUpperCase() + "="));
		report("getTagValue: length of toTheLeft is : " +
						   toTheLeft.length());
		report("getTagValue: toTheLeft is : " + toTheLeft);
		String retval;
		if(toTheLeft.indexOf('=') + 1 == toTheLeft.indexOf('"')) {
			int firstQuote = toTheLeft.indexOf('"');
			int secondQuote = toTheLeft.indexOf('"', firstQuote+1);
			retval = toTheLeft.substring(firstQuote+1, secondQuote);
		}
		else
			retval = toTheLeft.substring(toTheLeft.indexOf('=') + 1, toTheLeft.indexOf(' '));
		report("getTagValue: tag is " + tag + "  returning " + retval);
		return(retval);
	}
	
	public static boolean setAPostPair() {  /// Look left for an input name=SOMETHING
		String key=null;
		String value=null;
		//Pattern p = Pattern.compile(".*<input .*?name=\"(.*?):(.*?)\"");
		// This should be greedy enough to find the last match, but
		// isn't, hence the loop.
		/*Pattern p = Pattern.compile(".*name=\"(.*?):(.*?)\"");
		Matcher m = p.matcher(toTheLeft);
		while(m.find()){
			report("setAPostPair: match is " + m.group());
			//int whereIsColon = m.group(1).indexOf(':');
			key=m.group(1); //m.group(1).substring(0,whereIsColon);
			value=m.group(2);  //.substring(whereIsColon);
		}
		*/
		String [] pair = getTagValue("name").split(":");
		key = pair[0];
		value = pair[1];
		if(key!=null) {
			addPair(key, value);
			return(true);
		}
		return(false);
	}
	
	public static void sendPost() {
		// KLUGE!!!! only for OVID.
		currentURLString = new String("http://"+ host + ":80/ovidweb.cgi");
		currentURLString = currentURLString.concat(postArgs);
		report("\nSENDPOST  currentURLString=\n" + currentURLString);
		//java.util.Iterator iter = postHash.entrySet().iterator();
		//while(iter.hasNext()){
		//	java.util.Map.Entry m = (java.util.Map.Entry) iter.next();
		//	currentURLString = currentURLString.concat(m.getKey()+"="+m.getValue()+"&");
		//}
		if (setCurrentURL())
			currentPageContents = getCurrentPageContents();
	}
	
	public static void setURL() {
		href();
	}
	public static void href() {
		setTheURL(getTagValue("href"));
	}
	public static void findFrame() {
		src();
	}
	public static void src() {
		seek();
		setTheURL(getTagValue("src"));
	}

	public static void replace() {
		String [] args = theRest.split(" ");
		currentURLString = currentURLString.replaceFirst(args[1], args[2]);
		setCurrentURL();
	}


	public static void subst() {
		useThis();
	}

	public static void useThis() {
		currentURLString = substituteKeys(thisLine, true);
		setCurrentURL();
	}
	//  END OF COMMAND methods.

	
	//  Utility functions are placed below.
	static void getNewValuesFromPubmed(String _pmid) {
		fieldValuesHashMap = PubmedURLHelper.updateArticleFields(_pmid);
	}
	static void getNewValuesFromProtege() {
		Iterator iter = fieldCodeHashMap.keySet().iterator();
		if ( fieldValuesHashMap == null )  
				return;
		while( iter.hasNext()) {
			String key = (String) iter.next();
			String field = (String) fieldCodeHashMap.get(key);
			String newval;
			char letter = key.charAt(0);
			Slot slot = kb.getSlot(field);
			if (letter == 'j' || letter == 'J') { 
					if ( journal.getOwnSlotValue(slot) != null )
							newval = journal.getOwnSlotValue(slot).toString();
					else
							newval = "";
			}
			else {
				newval = article.getOwnSlotValue(slot).toString();
			}
			fieldValuesHashMap.put(key, newval);
		}	     
	}
	static String getNewVal(char letter) {
		// not using Protege:
		String newval = (String) fieldValuesHashMap.get(String.valueOf(letter));
		report("getNewVal: " + letter + " " + newval);
		return(newval);
	}
	
	static String substituteKeys(String script, String _pmid){
			try { 
					getNewValuesFromPubmed(_pmid);
					return(substituteKeys(script, false));
			}
			catch ( Exception e) {
					e.printStackTrace();
			}
			return "EMPTYx";
	}

	static String substituteKeys(String script, boolean fromProtege){
		if (fromProtege)
			getNewValuesFromProtege();
		while ( script.indexOf("%") >= 0) {
			char letter = script.charAt(script.indexOf("%")+1);
			String newval;
			try {
				newval = getNewVal(letter);
				int k=newval.indexOf("-");
				if (letter=='P' && (k>=0))
					newval = newval.substring(0,k);
				while (letter=='P' && isLetter(newval.charAt(0)))
					newval = newval.substring(1);
				if (letter=='T' && (newval.charAt(newval.length()-1) == '.'))
					newval = newval.substring(0,newval.length()-2);
				//report(" Substituting field " + letter + " " + field + "   set to " + newval);
				script = StringHelper.substitute(script, "%"+letter, newval);
			}
			catch (edu.stanford.smi.protege.model.NullFrameException e) {
				//reportError("Slot content not found, letter " + letter + " field " + field);
				script = StringHelper.substitute(script, "%"+letter, "??");
			}
		}
		return script;
	}
	
	static void setTheURL(String href) {
		try {
			if (href.startsWith("http://"))
				currentURLString = href;
			else if (href.startsWith("/"))
				currentURLString = "http://" + host + href;
			else {
				int i = currentURLString.lastIndexOf("/");
				String stripped = currentURLString.substring(0,i+1);
				report(" stripped = " + stripped);
				currentURLString = stripped + href;
			}
			report("\nSETURL  currentURLString=\n" + currentURLString);
			currentURL = new URL(currentURLString);
			report("\nSETURL  currentURL=\n" + currentURL);
		}	catch (MalformedURLException urle) {
			reportError(urle);
		}
	}

	static String getCurrentPageContents() {
		host = currentURL.getHost();
		currentPagePosition = 0;
		postHash.clear();
		postArgs = "";
		try {
			URLConnection urlConn = currentURL.openConnection();
			urlConn.setDoInput (true);
			urlConn.setDoOutput (true);
			urlConn.setUseCaches (false);
			urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			//report("getCurrentPageContents: urlConn \n" + urlConn);
			try { 
					report("  content type " + urlConn.getContentType());
					report("  content length " + urlConn.getContentLength());
			}catch (Exception reportErr) {
					System.out.println("report error in getCurrentPageContents");
			}
			// Trouble with the redirect.
			Object urlContent = urlConn.getContent();
			report("  content class " + urlContent.getClass());
			InputStream istream = urlConn.getInputStream ();
			int zerocounter = 0;
			ByteArrayOutputStream content = new ByteArrayOutputStream();
			int b;
			urlConn.connect();
			while ( (b = istream.read()) != -1)
				content.write(b);
			String sContent = content.toString();
			report("getCurrentPageContents:   Finished reading URL, size is "
				   + sContent.length());
			return(content.toString());
		}
		catch (Throwable t) {
			t.printStackTrace();
			return(null);
		}
	}


	private static Method getNextCommand() {
		SimpleAttributeSet attset = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attset, "SansSerif");
		StyleConstants.setFontSize(attset, 16);
		StyleConstants.setBold(attset, true);
		StyleConstants.setForeground(attset, Color.blue.darker());
		if(script.length()==0)
			return(null);  // all done
		if(script.charAt(script.length()-1)!='\n')
		   script = script + "\n";
		if(script.charAt(0)=='#')
			script = "COMMENT " + script;
		thisLine = script.substring(0,script.indexOf('\n'));
		report( "\ngetNextCommand():  thisLine is " + thisLine, attset);
		int space = thisLine.indexOf(" ");
		String command = (space < 0 ?  thisLine : thisLine.substring(0,space).toUpperCase());
		//int Ithinkitis = ((Integer) COMMAND_MAP.get(command)).intValue();
		theRest = (space < 0 ?  "" : thisLine.substring(space+1));
		//try {
		report( "getNextCommand():  command is " + command, attset);

		Method [] methods = FullTextURLFinder.class.getMethods();
		try {
			for (int i=0; i < methods.length; i++) {
				//report("   " + methods[i].getName());
				if (methods[i].getName().toUpperCase().equals(command.toUpperCase())) {
					report( "\ngetNextCommand():  method is found: " + methods[i].getName()
						+ " "	+ methods[i].getParameterTypes().length, attset);
					return (FullTextURLFinder.class.getMethod(methods[i].getName(), (Class[])null));
				}
			}
			// CAUTION: assumes there are no methods of the same
			// name with a non-empty parameter signature.
			Method substMethod = FullTextURLFinder.class.getMethod("useThis", (Class[])null);
			reportError( "\ngetNextCommand():  method NOT found; returning useThis()");
			return(substMethod);
		} catch (Exception e){
			reportError( "\ngetNextCommand():  Exception; " + e.getMessage()
					+ "   returning null");
			reportError(e);
			return(null);
		}
	}
	/**
		catch(Exception e1) {
			if (e1 instanceof NoSuchMethodException)
				return (substMethod);
			else {
				reportError(e1);
				return(null);
			}
		}
	}
	**/
}
