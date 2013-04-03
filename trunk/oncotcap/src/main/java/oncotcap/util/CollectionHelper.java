package oncotcap.util;

import java.util.*;
import java.lang.reflect.Array;
import oncotcap.datalayer.DefaultPersistibleList;

public class CollectionHelper {
	
		public static Collection and(Collection collection1,
																 Collection collection2) {
				Vector completeList = new Vector();

				if ( collection2 == null) 
						return completeList;
				Iterator i = collection2.iterator();
				while ( i.hasNext() ){
						Object addlItem = i.next();
						if ( collection1.contains(addlItem) == true ) {
								completeList.addElement(addlItem);
						}
				}
				return completeList;
		}

		public static Collection or(Collection collection1,
																 Collection collection2) {
				Vector completeList = new Vector(collection1);

				if ( collection2 == null) 
						return collection1;
				Iterator i = collection2.iterator();
				// Do the long way tro make sure no dups
				while ( i.hasNext() ){
						Object addlItem = i.next();
						if ( collection1.contains(addlItem) == false ) {
								completeList.addElement(addlItem);
						}
				}
				return completeList;
		}

		public static Collection not(Collection collection1,
																 Collection collection2) {
				// Remove items from second list from first list 
				Vector completeList = new Vector(collection1);

				if ( collection2 == null) 
						return collection1;
				Iterator i = collection2.iterator();
				// Do the long way tro make sure no dups
				while ( i.hasNext() ){
						Object addlItem = i.next();
						completeList.removeElement(addlItem);
				}
				return completeList;
		}
		
		public static Vector arrayToVector(Object [] list) {
			Vector v = new Vector();
			for (int i=0; i<list.length; i++) 
				v.add(list[i]);
			return (v);
		}		
		public static Vector arrayToVector(Object [][] list) {
				// Convert into a vector of vectors
				Vector v = new Vector();
				Vector subVector = null;
				for (int i=0; i<list.length; i++) {
								subVector = new Vector();
								for ( int j=0;  j<list[i].length; j++) {
										subVector.add(list[i][j]);
								}
								v.add(subVector);
				}
				return (v);
		}		

		public static oncotcap.datalayer.DefaultPersistibleList arrayToDefaultPersistibleList(Object [][] list) {
				// Convert into a DefaultPersistibleList of DefaultPersistibleList
				DefaultPersistibleList v = new DefaultPersistibleList();
				DefaultPersistibleList subVector = null;

				for (int i=0; i<list.length; i++) {
								subVector = new DefaultPersistibleList();
								for ( int j=0;  j<list[i].length; j++) {
										subVector.add(list[i][j]);
								}
								v.add(subVector);
				}
				return (v);
		}		
		public static Vector makeVector(Object singleItem) {
				// Creata new vector and add the single element to it
				Vector newVector = new Vector();
				newVector.addElement(singleItem);
				return newVector;
		}

		public static Vector makeVector(Iterator iterator) {
				// Creata new vector and add the single element to it
				Vector newVector = new Vector();
				while ( iterator.hasNext() ) {
						newVector.addElement(iterator.next());
				}
				return newVector;
		}

}
