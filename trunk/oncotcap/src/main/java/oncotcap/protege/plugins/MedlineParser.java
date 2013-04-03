package oncotcap.protege.plugins;
 
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

public class MedlineParser {


  public MedlineParser() {
  }

  public static void main(String[] args) {

    if (args.length != 1) {
      System.err.println("Usage: cmd filename or number");
      System.exit(1);
    }
    HashMap h = (HashMap) getContentsByPMID(new Integer(args[0]));
    Iterator it = h.entrySet().iterator();
    while (it.hasNext()) {
    	java.util.Map.Entry m = (java.util.Map.Entry) it.next();
    	System.out.println(m.getKey() + "=" + m.getValue());
    }
    System.exit(0);

  }
  
  public static Object getContentsByPMID(Integer PMID) {
  	return getContentsByPMID(PMID, new MedlineParserContentHandler());
  }
  
  public static Object getContentsByPMID(Integer PMID,
  		MedlineParserContentHandler handler) {
	URL url=null;
	try {
		System.out.println(PMID);
		url = new URL(
			"http://www.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=pubmed&id="
			+ PMID + "&retmode=xml");
	} /*catch (NumberFormatException e) {
		try {
			url = new URL(args[0]);
		} catch (MalformedURLException urle) {
			urle.printStackTrace();
		}
			e.printStackTrace();
	}	 
	*/
	catch (MalformedURLException urle) {
		urle.printStackTrace();
		return(null);
	}
	
  	try {
	    URLConnection urlConn = url.openConnection();
	    urlConn.setDoInput (true);
	    urlConn.setDoOutput (true);
	    urlConn.setUseCaches (false);
	    urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	    DataOutputStream printout = new DataOutputStream (urlConn.getOutputStream ());
	
	    //  String content = "Content-type=application/x-www-form-urlencoded&orig_db=PubMed&cmd=Text&uid=10593478&list_uids=10593478&query_type=retrieve&query_sort=&"; // your form key/value pairs here
	    String content = "Content-type=application/x-www-form-urlencoded"; // your form key/value pairs here
	
	    printout.writeBytes (content);
	    printout.flush ();
	    printout.close ();
  		InputStream istream = urlConn.getInputStream ();
    // Get instance of SAX event handler

	    SAXParserFactory factory = SAXParserFactory.newInstance();
       // Parse the input
      	SAXParser saxParser = factory.newSAXParser();
      	System.out.println("saxParser class is " 
      		+ saxParser.getClass());
      	saxParser.parse( istream, handler );
      	istream.close();
		return (handler.getResult());
    } 
    catch (Throwable t) {
      t.printStackTrace();
    }
	return null;
  }
}