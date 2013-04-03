/*
 * Educational Resource for Tumor Heterogeneity
 *             Oncology Thinking Cap
 *
 * Copyright (c) 2003  University of Pittsburgh
 * All rights reserved.
 *
 *  SourceSafe Info:
 *               $Header: $
 *               Revision: $Revision$
 *               Author: $Author$
 *
 * Code Review History:
 *     (mm.dd.yyyy initials)
 *
 * Test History:
 *     (mm.dd.yyyy initials)
 */
package oncotcap.datalayer;

import java.lang.reflect.*;
import java.util.*;

/**
 * Basic structure to hold a description of how to map information to and
 * from the persistent data source ( Protege or and RDB)
 *
 * @author     morris
 * @created    March 11, 2003
 */
public class AttributeAccessMap {
		private String classFullName = null;
	/** The field name in the Persistible java class */
	public String attributeName;
	/** The type (full package name) of the field in the 
	    Persistible java class */
	public String attributeSubType = null;
	/** If the attribute is a collection of any sort this 
	    is the type (full package name) of the items contained 
	    in the collection (ex. if attributeType is list and the list
		 contains Strings java.lang.String would be the attributeSubType */
	public String attributeType = null;
	/** The name of the field, column or slot in the datasource 
		 (database, protege file, or text file) */
	public String dsAttributeName;
	/** Getter method. If Persitible java class provides 
		 public access to the attribute and no special processing
		 is required to retrieve the attribute value this 
		 value will be null*/
	 public Method getMethod = null;
	 /** List of attribute to pass to the setter method*/
	 public Class paramList[];
	 /** Setter method. If Persitible java class provides 
		  public access to the attribute and no special processing
		  is required to store the attribute value this 
		  value will be null*/
	 public Method setMethod = null;
		private static Hashtable classNameHashtable = new Hashtable();
		static {
				classNameHashtable.put("String", "java.lang.String");
				classNameHashtable.put("Float", "java.lang.Float");
				classNameHashtable.put("Double", "java.lang.Double");
				classNameHashtable.put("Integer", "java.lang.Integer");
				classNameHashtable.put("Boolean", "java.lang.Boolean");
				classNameHashtable.put("Class", "java.lang.Class");
				classNameHashtable.put("float", "java.lang.Float");
				classNameHashtable.put("double", "java.lang.Double");
				classNameHashtable.put("int", "java.lang.Integer");
				classNameHashtable.put("boolean", "java.lang.Boolean");
				classNameHashtable.put("Collection", "java.util.Collection");
				classNameHashtable.put("Vector", "java.util.Vector");
				classNameHashtable.put("List", "List");
		}
			 

	/**
	 *  Constructor for the AttributeAccessMap object
	 *  Translate and store the datasource description of how to map 
    *  the datasource representation into the OncoTCap model 
	 *
	 * @param  classFullName    full package name of the persitible class 
	 * @param  dsAttrName       (@see dsAttributeName)
	 * @param  attrName         (@see attributeName)
	 * @param  attributeType    (@see attributeType)
	 * @param  attributeSubType (see attributeSubType)
	 * @param  getMethodName    (name of getter in the Persistible java class)
	 * @param  setMethodName    (name of getter in the Persistible java class)
	 */
	public AttributeAccessMap(String classFullName,
	                          String dsAttrName,
	                          String attrName,
	                          String attributeType,
	                          String attributeSubType,
	                          String getMethodName,
	                          String setMethodName) {
	
		// FUTURE This always assumes the getmethod has no arguments
		// Which means a special work around will need to be created
		// For the less static classes like ProtegeObject
			this.classFullName = classFullName;
		this.attributeType = fullClassName(attributeType);
		this.attributeSubType = fullClassName(ifNull(attributeSubType));
		this.attributeName = ifNull(attrName);
		this.dsAttributeName = ifNull(dsAttrName);
		getMethodName = ifNull(getMethodName);
		setMethodName = ifNull(setMethodName);
		Class defaultCollectionParam[] = {Collection.class};


		try {
			Class theClass = Class.forName(classFullName);
			Class returnTypeClass = null;

			if (getMethodName != null) {
				getMethod = theClass.getMethod(getMethodName, (Class []) null);
				returnTypeClass = getMethod.getReturnType();
			}
			paramList = new Class[1];

			// If this is a oncotcap list
			if ("List".equals(attributeType)
					|| "HashtableList".equals(attributeType)
				 || "Collection".equals(attributeType)
				 || "Vector".equals(attributeType)) {
					paramList[0] = Class.forName(this.attributeSubType);
			}
			else if ("oncotcap.datalayer.PersistibleList".equals(attributeType)) {
					paramList[0] = Class.forName("java.util.Collection");
			}
			else {
				paramList[0] = returnTypeClass;
			}

			if (getMethodName != null && setMethodName != null) {
				setMethod =
					theClass.getMethod(setMethodName,
														 paramList);
			}
		}
		catch (ClassNotFoundException cnfe) {
			 System.out.println("Class not found: " + cnfe.getMessage()
													+ "for AttributeMap " + toString());
			 //System.exit(0);
		}
		catch (NoSuchMethodException nsme) {
			 System.out.println("Method not found: " + nsme.getMessage() 
													+ "for AttributeMap " + toString());
			 //System.exit(0);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		if ( this.attributeType == null ) {
				System.out.println("ERROR: null attribute type in attribute map for " 
													 + toString());
		}
		if ( this.attributeName == null ) {
				System.out.println("WARNING: null attribute name in attribute map for " 
													 + toString());
		}
		if ( this.dsAttributeName == null) {
				System.out.println("WARNING: null datasource attribute in map for " 
													 + toString());
		}
	}


	/**
	 * It creates a full package name. This is a convenience
    * for the attribute map so they won't have to type in 
    * the full package - probably should be done at the 
    * datasource
	 *`

	 * @param  originalName class name that was stored in the datasource
	 * @return              Returns a fixed up version of the passed in class
    *                      
	 */
	private String fullClassName(String originalName) {
		if (originalName == null) {
			return null;
		}
		String fullName = (String)classNameHashtable.get(originalName);
		if ( fullName == null) {
				return originalName;
		}
		return fullName;
	}


	/** 
	 * sets value to null if the value contains the string "null" or is empty
	 * @param  originalString 
	 * @return original string or null
	 */
	private String ifNull(String originalString) {
		if (originalString == null
			|| "null".equals(originalString)
			|| (originalString != null && originalString.trim().length() == 0)) {
			return null;
		}
		return originalString;
	}

		public String toString() {
				return getDebugString();
		}
	/** 
	 * creates a debug string with field values
	 * @return debug string
	 */
	 public String getDebugString() {
		return ("AttributeAccessMap: "
						+ classFullName + " "
				  + dsAttributeName + " "
				  + attributeName + " "
				  + attributeType + " "
				  + attributeSubType + " "
				  + getMethod + " "
				  + setMethod);
	 }
}
