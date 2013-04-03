package oncotcap.protege.plugins;

import edu.stanford.smi.protege.widget.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;


//import edu.pitt.upci.xmlexample.MedlineParser;
//import edu.pitt.upci.xmlexample.MedlineParserContentHandler;
// Now these are in package oncotcap.widgets .

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.*;

/**
 *
 * @author    Roger Day
 */
public class PubmedURLWidget extends AbstractSlotWidget {
	private JPanel _panel;
	private JButton _autofill_button;
	private JButton _goto_PubMed_button;
	private JButton _goto_FullText_button;
	private JTextField _pmid;    
	private JTextField _pubMedURL;    
    
    private boolean _isDirty;
	
	private String searchString;
	private boolean searchState = false;
	static final int TABSIZE = 4;
	static final int FONTSIZE = 14;
	
	boolean designTime = false;
	
	public static void main (String args[]) {
		PubmedURLWidget temp = new PubmedURLWidget();
		temp.designTime = true;
		temp.initialize();
		temp._panel.setSize(300,300);
		JFrame f = new JFrame();
		f.setSize(400,400);
		f.getContentPane().add(temp._panel);
		f.show();
		temp._panel.setVisible(true);
		temp._panel.setBackground(Color.red);
	}
    private DocumentChangedListener _documentListener =
        new DocumentChangedListener() {
            public void stateChanged(ChangeEvent event) {
                // valueChanged();   // character by character update
				_isDirty = true;
            }
        }
    ;

	private FocusListener _focusListener =
        new FocusAdapter() {
            public void focusLost(FocusEvent event) {
                if (_isDirty) {
                    valueChanged();
                }
            }
        }
    ;

	public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
		boolean isSuitable;
		System.out.println("PubmedURLWidget " +
						   cls + " " + slot.getName() + " " + slot);
		if (cls == null || slot == null)
			return false;
		if ((slot.getName()=="PMID" || slot.getName()=="pubMedURL" ))
			return true;
		return true;
	}

	public void dispose() {
		super.dispose();
		if (_isDirty) {
			valueChanged();
			_pmid.getDocument().removeDocumentListener(_documentListener);
			_pubMedURL.getDocument().removeDocumentListener(_documentListener);
			_pmid.removeFocusListener(_focusListener);
			_pubMedURL.removeFocusListener(_focusListener);
		}
	}

	Instance thisInstance () {
		return getInstance();
	}
	
	String xmlURL;
	void updateXMLURL(String s) {
		xmlURL = "http://www.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id="
			+ s + "&retmode=xml";
	}
	
	String pubMedURL;
	
	void updateURL(String s) {
		pubMedURL =	"http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=PubMed&list_uids="
		+ s + "&dopt=Abstract";
		_pubMedURL.setText(pubMedURL);
	}
	
	public Collection getValues() {
		_isDirty = false;
		String s;
		if (getSlot().getName()=="PMID") {
			s = _pmid.getText();
			while(s.charAt(s.length()-1)==' ')
				s = s.substring(0,s.length()-1);
			updateURL(s);
			try {
				Integer i = new Integer(s);
				Slot slot = getProject().getKnowledgeBase().getSlot("pubMedURL");
				getInstance().setOwnSlotValue(slot, _pubMedURL.getText());
				return CollectionUtilities.createList(i);
			} catch(NumberFormatException e) {
				return CollectionUtilities.createList(null);
			}
		}
		else if (getSlot().getName()=="pubMedURL") {
			s = _pubMedURL.getText();
		}
		else return null;
		if (s.length() == 0) {
			s = null;
		}
		return CollectionUtilities.createList(s);
	}
	
	/*class PubmedClassLoader extends URLClassLoader {

		public PubmedClassLoader(ClassLoader parent) {
				super(PubmedURLWidget.getLoadPath(), parent);
				System.out.println("0: " + getURLs()[0]);
		}
	}
	
	static public URL[] getLoadPath() {
		final String s =  "file://"
				+ "c:\\u\\my documents\\eclipse\\workspace\\PubMedURLCapture\\pub.jar";
		System.out.println(s);
		try {
			URL[] location = {new URL(s)};
			return location;
		} catch (MalformedURLException e)
		{ return null;
		}
	}*/
	Action buttonAction = new AbstractAction() {
		public void actionPerformed(ActionEvent ev) {
			if (((Instance)getInstance()).
			  getDirectType().
			  toString().
			  equals("WebLink"))
				article_instance = (Instance) getInstance().getOwnSlotValue(article_slot);
			else
				article_instance = (Instance) getInstance();
			
			System.out.println("button is pressed, " + ((JButton)(ev.getSource())).getText());
			
			if ( ev.getSource()==_autofill_button) {
				String s = _pmid.getText();
				if(s.equals(""))
					s = oncotcap.util.ClipboardHelper.get();
				while(s.charAt(s.length()-1)==' ')
					s = s.substring(0,s.length()-1);
				_pmid.setText(s);
				updateURL(s);
				System.out.println("authorsSlot" + authorsSlot);
				System.out.println("CollectionUtilities.createList(null)" + CollectionUtilities.createList(null));
				if(article_instance != null)
					article_instance.setOwnSlotValues(authorsSlot,
						CollectionUtilities.createList(null));
				updateArticleFields();
				_isDirty=true;
				valueChanged();
			}
			if ( ev.getSource()==_goto_PubMed_button) {
				java.awt.Cursor curs = getCursor();
				setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
				try {
					String location = _pubMedURL.getText();
					System.out.println("Launching URL:\n" + location);
					oncotcap.util.BrowserLauncher2.openURL(location);
				} catch (MalformedURLException e)
									 { return; }
				catch (java.io.IOException ioe)
									  { System.out.println("IOException"); return; }
				finally {
					setCursor(curs);
				}
			}
			if ( ev.getSource()==_goto_FullText_button) {
				java.awt.Cursor curs = getCursor();
				setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
				try {
					String location = FullTextURLFinder.find(article_instance);
					System.out.println("Launching URL:\n" + location);
					oncotcap.util.BrowserLauncher2.openURL(location);
				} catch (MalformedURLException e)
									  { return; }
				catch (java.io.IOException ioe)
						{ System.out.println("IOException"); return; }
				finally {
					setCursor(curs);
				}
			}
		}
	};

   	static Integer XYZZYcount = new Integer(1);
	Instance article_instance;
	
	KnowledgeBase kb;
	Slot article_slot;
	Slot authorsSlot;
	Slot authorNameSlot;
	Slot titleSlot;
	Slot volumeSlot;
	Slot issueSlot;
	Slot pagesSlot;
	Slot journalSlot;
	Slot journalAsStringSlot;
	Slot yearSlot;
	Slot PMIDSlot;


	
	void updateArticleFields() {
		//loadPubmedClasses();	

		HashMap h = null;
		
		try {
			MedlineParserContentHandler handler = new MedlineParserContentHandler() {
					String sNameCurrent;
					String qNameCurrent;
    				boolean reported_article_instance = false;

					void setArticle() {
	    				//System.out.println("Entering setArticle(), currently "
	    				// 	+ article_instance);
	    				if (reported_article_instance) return;
	    				if(article_slot==null)
	    					System.out.println("ERROR: article_slot is null");
	    				if(authorsSlot==null)
	    					System.out.println("ERROR: authorsSlot is null");
	    				if(authorNameSlot==null)
	    					System.out.println("ERROR: authorNameSlot is null");
	    				if(titleSlot==null)
	    					System.out.println("ERROR: titleSlot is null");
	    				if(issueSlot==null)
	    					System.out.println("ERROR: issueSlot is null");
	    				if(pagesSlot==null)
	    					System.out.println("ERROR: pagesSlot is null");
	    				if(volumeSlot==null)
	    					System.out.println("ERROR: volumeSlot is null");
	    				if(journalSlot==null)
	    					System.out.println("ERROR: journalSlot is null");
	    				if(journalAsStringSlot==null)
	    					System.out.println("ERROR: journalAsStringSlot is null");
	    				if(yearSlot==null)
	    					System.out.println("ERROR: yearSlot is null");
						if(PMIDSlot==null)
							System.out.println("ERROR: PMIDSlot is null");
	    				if (article_instance == null) {
				    		// create it.
				    		// name = ?????? ;
				    		String name = "XYZZY";
			    			System.out.println("Creating article_instance...");
			    			try {
					    		article_instance  = kb.getCls("Article")
					    			.createDirectInstance(null);
					    			//.createDirectInstance("XYZZY_"+XYZZYcount.toString());
					    		XYZZYcount = new Integer(XYZZYcount.intValue() + 1);
				    			System.out.println("Created article_instance");
					            getInstance().setOwnSlotValue(article_slot, article_instance);
					            System.out.println("Set article slot");
			    			}
			    			catch (Throwable th) {
			    				System.out.println("Error making article");
			    				th.printStackTrace();
			    			}
				    	}
				    	else {
				    		System.out.print("Found the article already there:  title is /");
				    		System.out.println(article_instance.getOwnSlotValue(titleSlot) + "/");
				    	}
				    	reported_article_instance = true;
    				}

				boolean insidePubDate = false;
				
				public void endElement(String namespaceURI,
										 String sName, // simple name
										 String qName // qualified name
										 )
				{
					if (qName.equals("PubDate")) {
						System.out.println("Exiting PubDate...");
						insidePubDate = false;
					}
				}
				
			  	public void startElement(String namespaceURI,
			       String sName, // simple name
				   String qName, // qualified name
				   Attributes attrs)
				{ 
					System.out.println("startElement: " + sName + "/" + qName + "/");
					setArticle();
					//do_emit = true;
				   	sNameCurrent = sName;
				   	qNameCurrent = qName;
					if (qName.equals("PubDate")) {
						System.out.println("Beginning PubDate...");
						insidePubDate = true;
					}
				}
				
			  	String lastName="DUMMY";
			  	
			  	public void characters(char buf[], int offset, int len)
				  throws SAXException
				  {
				  	String s = new String(buf, offset, len);
		  			//final Slot authorsSlot = kb.getSlot("authorsAsStrings");

			  		System.out.println("parser found " + sNameCurrent+ "/" + qNameCurrent + "/"+ s);
				  try {
			  		if (sNameCurrent.equals("ArticleTitle") || qNameCurrent.equals("ArticleTitle")) {
			  			updateSlotWithString(article_instance, titleSlot, s);
			  			//article_instance.setOwnSlotValue(titleSlot, s);
			  		}
			  		if (sNameCurrent.equals("Volume") || qNameCurrent.equals("Volume")) {
			  			updateSlotWithInteger(article_instance, volumeSlot, s);
			  		}
			  		if (sNameCurrent.equals("Issue") || qNameCurrent.equals("Issue")) {
			  			updateSlotWithInteger(article_instance, issueSlot, s);
			  		}
			  		if (sNameCurrent.equals("MedlinePgn") || qNameCurrent.equals("MedlinePgn")) 
			  			article_instance.setOwnSlotValue(pagesSlot, s);
			  		if (sNameCurrent.equals("MedlineTA") || qNameCurrent.equals("MedlineTA")) {
			  			article_instance.setOwnSlotValue(journalAsStringSlot, s);
			  			new InstanceMatcher (article_instance, kb.getCls("Journal"), 
			  					journalSlot, s);
			  		}
			  		if (sNameCurrent.equals("Year") || qNameCurrent.equals("Year")){
						if (insidePubDate) {
							System.out.println("Updating the Year field");
							updateSlotWithInteger(article_instance, yearSlot, s);
						}
			  		}
			  		if (sNameCurrent.equals("LastName") || qNameCurrent.equals("LastName"))
			  			lastName = s;
			  		if (sNameCurrent.equals("Initials") || qNameCurrent.equals("Initials")){
			  			// CAUTION this code assumes LastName always comes before Initials.
			  			// Better would be to trigger this after seeing an EndOfAuthor (whatever).
			  			System.out.println("sNameCurrent = " + sNameCurrent);
			  			String thisAuthorString = lastName + " " + s;
			  			System.out.println("Author: " + lastName + " " + s);
			  			Collection currentAuthors = article_instance.getOwnSlotValues(authorsSlot);
			  			System.out.println("Number of authors so far is " + currentAuthors.size());
			  			new InstanceMatcher (article_instance, kb.getCls("Author"), 
			  					authorsSlot, thisAuthorString);
						
					}
				  }
				  catch (Exception e) {
				  	System.out.print("OOPS!!!   ");
				  	e.printStackTrace();
				  }
				  finally {
				  	//System.out.println("Finally! end of loop");
				  }
				}
			};
			h = (HashMap) MedlineParser.getContentsByPMID(
					new Integer(_pmid.getText()), 
					handler);
		} catch (ClassCastException cce)
		{	cce.printStackTrace(); 
		}
		Iterator it = h.entrySet().iterator();
	    while (it.hasNext()) {
	    	java.util.Map.Entry m = (java.util.Map.Entry) it.next();
	    	System.out.println("HASHMAP: " + m.getKey() + "=" + m.getValue());
	    	// next, create or trace back to article, then fill in fields such as title.
	    	// following the example of the pubmedURL slot.
	    }
	}

	class InstanceMatcher  {
		public InstanceMatcher (Instance inst, Cls cls, Slot slot, String target) {
	  			Collection currentSlotValues = inst.getOwnSlotValues(slot);
	  			System.out.println("Number of slot entries so far is " 
	  					+ currentSlotValues.size());
	  			KnowledgeBase kb = getKnowledgeBase();
	  			Iterator iterCurrentSlotValues = currentSlotValues.iterator();
	  			Instance foundInst = null;
	  			while (iterCurrentSlotValues.hasNext()) {
	  				Instance nextInst = (Instance)iterCurrentSlotValues.next();
	  				System.out.println("Getting instance \"" + nextInst.getBrowserText()
	  						+ " \"...");
	  				// CAUTION:  Depends on browserText set correctly.
	  				if (nextInst.getBrowserText().equals(target)) {
	  					foundInst = nextInst;
	  					System.out.println(" Instance already in the slot. Skip it.");
	  					return; 
	  				}
	  			}
	  			Collection allKBValues = ((DefaultKnowledgeBase)kb).getInstances(cls);
	  			System.out.println("Number of instances total is " + allKBValues.size());
	  			Iterator iter = allKBValues.iterator();
	  			while (iter.hasNext()) {
	  				//System.out.println("Getting instance...");
	  				Instance nextInst = (Instance)iter.next();
	  				// CAUTION:  Depends on browserText set correctly.
	  				if (nextInst.getBrowserText().equals(target)) {
	  					foundInst = nextInst;
	  					System.out.println(" Found the instance \"" + target + "\" elsewhere.");
	  					break; 
	  				}
	  			}
	  			if (foundInst==null) {
	  				System.out.print ("Gotta make a new instance...");
	  				foundInst = kb.createInstance(null, cls);
	  				System.out.print ("did it...");
	  				System.out.print (foundInst.getDirectType()==cls);
	  				Slot browserSlot = cls.getBrowserSlot();
	  				System.out.println("browserSlot is " + browserSlot.getName() + "...");
	  				foundInst.setOwnSlotValue(browserSlot, target);
	  				System.out.println ("...and set the browserSlot to the target.");
	  			}
				System.out.println(currentSlotValues.size());
				Collection values = new Vector();
				if (slot.getAllowsMultipleValues())
					values.addAll(currentSlotValues);
				values.add(foundInst);
				System.out.print ("Added instance...");
		  		inst.setOwnSlotValues(slot, values);
		  		// You cannot do this by currentSlotValues.add() !!!!!
  				System.out.println ("DONE: Set slot for collection.");
		}
	}
	
	void updateSlotWithInteger(Instance inst, Slot theSlot, String fieldValue) {
		inst.setOwnSlotValue(theSlot, truncUntilInteger(fieldValue));
		System.out.println ("Updating "
			 + " slot = " + theSlot.getName() 
			 + " fieldValue = " + fieldValue );
	}
	Integer truncUntilInteger(String s) {
		try {
			Integer i;
			i= new Integer(s);
			System.out.println("truncUntilInteger: success! " + i);
			return(i);
		}
		catch(java.lang.NumberFormatException nfe)	
		{
			System.out.println("Trying again by truncating");
			if (s.length()>=0) {
				s = s.substring(0, s.length()-1);
				return(truncUntilInteger(s));
			}
		}
		System.out.println("truncUntilInteger: failure! ");
		return(null);
	}
	void updateSlotWithString(Instance inst, Slot theSlot, String fieldValue) {
		System.out.println ("Updating "
			 + " slot = " + theSlot.getName() 
			 + " fieldValue = " + fieldValue );
		try {

		    if (fieldValue != null && fieldValue != "")
		    	inst.setOwnSlotValue(theSlot, fieldValue);
		}
		catch (Exception ex) {
			System.out.println ("Error in updateField ");
		}
	}

	public void initialize() {
		kb = getKnowledgeBase();
		article_slot = kb.getSlot("article");
		authorsSlot = kb.getSlot("authors");
		authorNameSlot = kb.getSlot("name");
		titleSlot = kb.getSlot("title");
		volumeSlot = kb.getSlot("volume");
		issueSlot = kb.getSlot("issue");
		pagesSlot = kb.getSlot("pages");
		journalSlot = kb.getSlot("journal");
		journalAsStringSlot = kb.getSlot("journalAsString");
		yearSlot = kb.getSlot("year");
		PMIDSlot = kb.getSlot("PMID");

		
		/*if (((Instance)getInstance()).
			  getDirectType().
			  toString().
			  equals("WebLink"))
			article_instance = (Instance) getInstance().getOwnSlotValue(article_slot);
		else*/

		article_instance = (Instance) getInstance();
		_panel = ComponentFactory.createPanel();
		if (!designTime)
			add(new LabeledComponent(getLabel(), new JScrollPane(_panel)));
		//setCanStretchVertically(true);
		System.out.println(_panel.getLayout());
		//_panel.setLayout(new GridBagLayout());
		
		setPreferredColumns(3);
		setPreferredRows(1);

		_pmid = ComponentFactory.createTextField();
		_pmid.setFont(new java.awt.Font("COURIER", java.awt.Font.PLAIN, FONTSIZE));
		_pmid.getDocument().addDocumentListener(_documentListener);
		_pmid.addFocusListener(_focusListener);
		_pmid.setSize(100,40);
		_pmid.setPreferredSize(new Dimension(100,40));
		_panel.add(_pmid);

		_pubMedURL = ComponentFactory.createTextField();
		_pubMedURL.setFont(new java.awt.Font("COURIER", java.awt.Font.PLAIN, FONTSIZE));
		_pubMedURL.getDocument().addDocumentListener(_documentListener);
		_pubMedURL.addFocusListener(_focusListener);
		_pubMedURL.setSize(300,40);
		_pubMedURL.setPreferredSize(new Dimension(300,40));
		//_panel.add(_pubMedURL);

		_autofill_button = ComponentFactory.createButton(buttonAction);
		_autofill_button.setText("autofill");
		_autofill_button.setSize(40,30);
		_autofill_button.setMnemonic(KeyEvent.VK_A);
		_panel.add(_autofill_button);
		
		_goto_PubMed_button = ComponentFactory.createButton(buttonAction);
		_goto_PubMed_button.setText("PubMed");
		_goto_PubMed_button.setSize(40,30);
		_goto_PubMed_button.setMnemonic(KeyEvent.VK_P);
		_panel.add(_goto_PubMed_button);

		_goto_FullText_button = ComponentFactory.createButton(buttonAction);
		_goto_FullText_button.setText("full text");
		_goto_FullText_button.setSize(40,30);
		_goto_FullText_button.setMnemonic(KeyEvent.VK_F);
		_panel.add(_goto_FullText_button);
	}

	public void setEditable(boolean b) {
		_pmid.setEditable(b);
		_pubMedURL.setEditable(false);
		  // for now, change only through PMID.
	}

	public void setText(String text) {
		try {
			Integer i = truncUntilInteger(text);
		} catch(Exception e) {
			text = "";
			System.out.println("Bad integer in PMID field");
			System.out.println("Currently, it works only for numbers in PMID field");
		}
		_documentListener.disable();	
		_pmid.setText(text == null ? "" : text);
		updateURL(text);
		_documentListener.enable();
		_isDirty = false;
	}

	public void setValues(Collection values) {
		Object o = CollectionUtilities.getFirstItem(values);
		if (o == null) setText("");
		else {
			System.out.println("class " + o.getClass().toString());
			System.out.println("value " + o.toString());
			setText(CollectionUtilities.getFirstItem(values).toString());
		}
	}
		// void loadPubmedClasses() {
		/*PubmedClassLoader loader = new PubmedClassLoader(
			Thread.currentThread().
			getContextClassLoader());
		try {
			loader.loadClass("edu.pitt.upci.xmlexample.MedlineParser");
			loader.loadClass("edu.pitt.upci.xmlexample.MedlineParserContentHandler");
			System.out.println("SUCCESSFUL class load");
		}
		catch(java.lang.NoClassDefFoundError e)
		{
			System.out.println("java.lang.NoClassDefFoundError in PubmedClassLoader: "  + " " + e);
			return;
		}
		catch(ClassNotFoundException e)
		{
			System.out.println("Class Not Found Exception in PubmedClassLoader: " +  " " + e);
			return;
		}
		*/
}
