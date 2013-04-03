package oncotcap.display.browser;

import java.util.*;

public class HashtableHelper {

		// Make keys value and values keys - this is geared to
		// hashtables that have vectors in the value field
		public static Hashtable invertHashtable(Hashtable h) {
				Hashtable reHashed = new Hashtable();

				for (Enumeration e = h.keys() ; 
						 e.hasMoreElements() ;) {
						Object key = e.nextElement();
						Vector values = (Vector)h.get(key);
						if ( values != null ) {
								Iterator i = values.iterator();
								while ( i.hasNext() ) {
										addVectorValue(reHashed, i.next(), key);
								}
						}
				}				
				return reHashed;
		}
		
		public static void addVectorValue(Hashtable hashTable,
																Object key, Object value) {
				if ( key != null && value != null ) {
						// Allow a node to have more than one value
						Vector values  = 
								(Vector)hashTable.get(key);
						if ( values == null ) 
								values = new Vector();
						// If  the key had a value that was the root remove it 
						// before adding non root values
						values.addElement(value);
						hashTable.put(key, values);
				}
		}
}
