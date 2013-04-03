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
public class PubmedURLHelper  {
	private String searchString;
	private boolean searchState = false;
	
	boolean designTime = false;
	
	String xmlURL;

	void updateXMLURL(String s) {
		xmlURL = "http://www.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id="
			+ s + "&retmode=xml";
	}
	
	String pubMedURL;
	
	void updateURL(String s) {
		pubMedURL =	"http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&db=PubMed&list_uids="
		+ s + "&dopt=Abstract";
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
	}

	static HashMap hashMapForSubstituting = null;

   	static public HashMap updateArticleFields(String _pmid) {
		PubmedURLHelper.hashMapForSubstituting =  new HashMap();
		HashMap hForAllFields = new HashMap();
		try {
			MedlineParserContentHandler handler = new MedlineParserContentHandler() {
				String sNameCurrent;
				String qNameCurrent;
				
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
						PubmedURLHelper.hashMapForSubstituting.put("T", s);
					}
					if (sNameCurrent.equals("Volume") || qNameCurrent.equals("Volume")) {
						PubmedURLHelper.hashMapForSubstituting.put("V", s);
					}
					if (sNameCurrent.equals("Issue") || qNameCurrent.equals("Issue")) {
						PubmedURLHelper.hashMapForSubstituting.put("I", s);
					}
					if (sNameCurrent.equals("MedlinePgn") || qNameCurrent.equals("MedlinePgn")) 
						PubmedURLHelper.hashMapForSubstituting.put("P", s);
					if (sNameCurrent.equals("MedlineTA") || qNameCurrent.equals("MedlineTA")) {
						PubmedURLHelper.hashMapForSubstituting.put("J", s);
					}
					if (sNameCurrent.equals("Year") || qNameCurrent.equals("Year")){
						if (insidePubDate) {
							PubmedURLHelper.hashMapForSubstituting.put("Y", s);
						}
					}
					if (sNameCurrent.equals("LastName") || qNameCurrent.equals("LastName"))
						PubmedURLHelper.hashMapForSubstituting.put("LastName",  s);
					if (sNameCurrent.equals("Initials") || qNameCurrent.equals("Initials")){
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
			hForAllFields = (HashMap) MedlineParser.getContentsByPMID(
					new Integer(_pmid), 
					handler);
		} catch (ClassCastException cce)
		{	cce.printStackTrace(); 
		}
		Iterator it = hForAllFields.entrySet().iterator();
		while (it.hasNext()) {
			java.util.Map.Entry m = (java.util.Map.Entry) it.next();
			System.out.println("HASHMAP: " + m.getKey() + "=" + m.getValue());
		}
		return(hashMapForSubstituting);
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
}
