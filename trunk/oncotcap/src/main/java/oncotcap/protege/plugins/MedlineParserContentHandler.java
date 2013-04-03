package oncotcap.protege.plugins;

import java.io.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.Iterator;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import oncotcap.util.CollectionHelper;

public class MedlineParserContentHandler extends DefaultHandler {

  private String currentElement;
  private int levelCounter = 0;
  private StringBuffer textBuffer;
  private Writer out;
	private Vector qualifier = new Vector();

	protected HashMap hashMap = new HashMap();
	
  	public HashMap getResult() {
		return hashMap;
	}
	
  public MedlineParserContentHandler() {

    try {
      // Set up output stream
      out = new OutputStreamWriter(System.out, "UTF8");
    }
    catch (Exception e) {
      System.out.println("Error initializing the output stream.");
    }
  }



  //===========================================================
  // SAX DocumentHandler methods
  //===========================================================

  //Doc starts here...
  public void startDocument()
  throws SAXException
  {
  }

  //Doc ends here...
  public void endDocument()
  throws SAXException
  {
  }

  protected boolean do_emit = false;
  
  //This event is fired for the start of each element
  public void startElement(String namespaceURI,
                           String sName, // simple name
                           String qName, // qualified name
                           Attributes attrs)
  throws SAXException
  {
      currentElement = qName; // element name
			qualifier.addElement(qName);
      levelCounter++;

      String s = buildSpace(levelCounter) ;
      if(do_emit) emit(s + "Starting element " + qName);

      if (attrs != null) {
          for (int i = 0; i < attrs.getLength(); i++) {
              String aName = attrs.getLocalName(i); // Attr name
              if ("".equals(aName)) aName = attrs.getQName(i);
              if(do_emit) emit(" ");
              if(do_emit) emit("     " + currentElement + " Attribute " + aName+"=\""+attrs.getValue(i)+"\"");
          }
      }
  }
	
  //This event is fired at the end of each element.
  //Retrieve the value of the element when this event is fired.
  public void endElement(String namespaceURI,
                         String sName, // simple name
                         String qName  // qualified name
                        )
  throws SAXException
  {
      //Display value of buffer
      echoText();
      if(do_emit) emit(buildSpace(levelCounter) + "End of element " + qName);
      levelCounter--;
			int last = qualifier.size();
			qualifier.removeElementAt(last-1);
      currentElement = null;
  }

  //This is the event that is fired as character data is read within an element
  public void characters(char buf[], int offset, int len)
  throws SAXException
  {
      String s = new String(buf, offset, len);
      if (textBuffer == null) {
         textBuffer = new StringBuffer(s);
      } else {
         textBuffer.append(s);
      }
  }

  //===========================================================
  // Utility Methods ...
  //===========================================================

  //===========================================================
  // Utility Methods ...
  //===========================================================

  // Display text accumulated in the character buffer
  private void echoText()
  throws SAXException
  {
      if (textBuffer == null || currentElement == null)
        return;
			// make this hashmap a little smarter handle multiple 
			// instances at the same level with the same tag- 
			// currently the last value in is the only value
			// THe value will be a vector if multiple instances exist (ordered)
			// Build currentElement string
			String key = getCurrentElement();
			hashMapPut(key,
								 textBuffer.toString().trim());
								 
      //hashMap.put(buildSpace(levelCounter + 1)  + currentElement, textBuffer.toString().trim());

			//System.out.println(buildSpace(levelCounter + 1)  + currentElement + "=" + textBuffer.toString().trim());

      textBuffer = null;
  }

		private String getCurrentElement() {
				StringBuffer ele = new StringBuffer();
				Iterator i = qualifier.iterator();
				boolean firstElement = true;
				while (i.hasNext()) {
						if ( firstElement ) 
								firstElement = false;
						else 
								ele.append(".");
						ele.append((String)i.next());
				}
				return ele.toString();
		}
		private void hashMapPut(Object key, Object value) {
// 				System.out.println("hashmapput " + key + "  == " + value);
				Object currentValue = hashMap.get(key);
				if ( currentValue != null) {
						if ( currentValue instanceof Vector ) {
								((Vector)currentValue).addElement(value);
								hashMap.put(key, currentValue);
						}
						else {
								Vector v = CollectionHelper.makeVector(currentValue);
								v.addElement(value);
								hashMap.put(key, v);
						}
				}
				else {
						hashMap.put(key, value);
				}
		}

  // Wrap I/O exceptions in SAX exceptions, to
  // suit handler signature requirements
  private void emit(String s)
  throws SAXException
  {
      try {
          out.write(s + "\n");
          out.flush();
      } catch (IOException e) {
          throw new SAXException("I/O error", e);
      }
  }

  //Build a space for the entry in the tree (This is only to make output readable)
  private String buildSpace(int numOfSpaces) {
      StringBuffer spaceBuffer = new StringBuffer();

      for (int i = 0; i < numOfSpaces; i++) {
        spaceBuffer.append(" ");
      }

      return spaceBuffer.toString();
  }

  // Start a new line
  private void nl()
  throws SAXException
  {
      String lineEnd =  System.getProperty("line.separator");
      try {
          out.write(lineEnd);
      } catch (IOException e) {
          throw new SAXException("I/O error", e);
      }
    }
}
