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


import java.util.*;

import oncotcap.util.GUID;
import oncotcap.datalayer.persistible.OncFilter;

/**
 * Defines minimum functionality that each datasource must provide
 * @author     morris
 * @created    March 11, 2003
 */
public interface OncoTCapDataSource {

	/** Any necessary initialization like opening connections setting contexts, etc*/
	public void initDataSource();


	/**
	 * Find a specific objects of the type represented by the persistible obj
	 * with the specified globally unique id
	 *
	 * @param  obj  Description of Parameter
	 * @param  guid Description of Parameter
	 * @return       Description of the Returned Value
	 */
	public Persistible find(Persistible obj, GUID guid);
	public Persistible find(GUID guid);

	/**
	 *  Find All objects of the type represented by the persistible obj
	 *
	 * @param  obj The persistible object type to search for
	 * @return List of Persistible objects
	 */
	public Collection find(Persistible obj);

	/**
	 *  Find All objects of the type represented by the persistible obj
	 *
	 * @param  clss The class to search for
	 * @return List of Persistible objects
	 */
	public Collection find(Class clss);

	/**
	 *  Find All objects of the type represented by the full package name
    *  objectType
	 *
	 * @param  objectType full package name of the persitible object to find
	 * @return List of Persistible objects
	 */
	public Collection find(String objectType);


// DELETE 	/**
// 	 *  Find all objects that have a direct/indirect relationship to the selected
// 	 *  object
// 	 *
// 	 * @param  objectType    full package name of the persitible object to find
// 	 * @param  relatedObject full package name of the persitible object to find
// 	 * @return  List of Persistible objects              
// 	 */
// 	public Collection find(String objectType, Object relatedObject);

//   public Vector find(String findClass, 
// 										 Collection relatedObjects,
// 										 boolean inclusiveSearch);

//     public Vector find(Class cls, 
// 											 Collection relatedObjects,
// 											 boolean inclusiveSearch,
// 											 boolean persistible);
// 		public Vector find(String protegeClassName, 
// 											 String javaClassName, 
// 											 Collection relatedObjects,
// 											 boolean inclusiveSearch,
// 											 boolean persistible);
// 	/**
// 	 *  Find all objects that have the value (attributeValue) in the field
// 	 *  attributeName ** currently not in use
// 	 *  FUTURE: support more generic searchs
// 	 * @param  objectType     
// 	 * @param  attributeName  
// 	 * @param  attributeValue 
// 	 * @return  List of Persistible objects                             
// 	 */
// 	public Collection find(Persistible objectType,
// 	                       String attributeName,
// 	                       Object attributeValue);


	/** 
	 * Find objects of specified type that are related to the specified keyword
	 * @param  objectType  The persistible object type to search for
	 * @param  keyword    
	 * @return List of Persistible objects                                        
	 */
	public Collection find(Persistible objectType,
	                       String keyword,
												 boolean searchBrowserText);


	/** Find objects of specified type that are related to the specified keyword
	 * @param  objectName full package name of the persitible object to find
	 * @param  keyword    
	 * @return List of Persistible objects                             
	 */
	public Collection find(String objectName,
	                       String keyword,
												 boolean searchBrowserText);
		/* Do  a string search on a class type */
		public Collection find(Class clas, String keywordString);

	/**
	 * Create new instance of specified type and store values in the datasource
	 * @param  obj THe Persistible instance of the object to create/store
	 * @return GUID of the newly created object
	 */
	public GUID insert(Persistible obj);


	/**
	 * Update the datasource values for the specified Persistible object
	 * @param  obj Latest version of the persistible object to store
	 * @param  guidString ( probably not necessary) 
	 * @return GUID
	 */
	public GUID update(Persistible obj, String guid);


	/**
	 * @param  obj Latest version of the persistible object to store
	 * @param  guid ( probably not necessary) 
	 * @return GUID
	 */
	public GUID update(Persistible obj, GUID guid);

	/**
	 * @param  obj Latest version of the persistible object to store
	 * @return GUID
	 */
	public GUID update(Persistible obj);

	public void delete(Persistible persistible);

	/** Save information to disk*/
	public boolean commit();

		/** Determine if anything has changed */
		public boolean hasChanged();

	/** Using the datamap information translate the datasource form of the object
	 * ( table/columns , instance frames/slots) to the Persistible Java model 
	 * format used by the application
	 * 
	 * @param  toObjectType   Persitible Class
	 * @param  dataSourceObject native datasource object ( i.e. protege instance)
	 * @return instantiated persitible class
	 */
	public Persistible toPersistible(Class toObjectType,
	                                 Object dataSourceObject);


		public Vector getInstances(String startClassName, 
															 OncFilter filter);
		/** Force the search to use a particular  path that is defined in the
		 data source */
		public Vector getInstances(String startClassName, 
															 OncFilter filter,
															 String searchPathId);	
	/** Retrieve instances from the data source in a tree format 
				-- current problem is that all objects do not exist as persistibles
				and model objects have no convienet way of  getting link information without
				knowing the specific data structure unlike protege
		*/
		public Hashtable getInstanceTree(Class startJavaClass, 
																		 Collection endJavaClasses);
		public Hashtable getInstanceTree(String startClassName, Collection endClassNames);
	// DELETE 	public Hashtable getInstanceTree(String startClassName, Collection endClassNames,
// 																		 Collection relatedItems, int allRootNone);
		public Hashtable getParentTree(String startClassName, 
																	 Collection endClassNames,
																	 Vector listOfChildren,
																	 int allRootNone);
		public Hashtable getInstanceTree(String startClassName, 
																		 Collection endClassNames,
																		 OncFilter filter,
																		 int allRootNone);
		public Hashtable getInstanceTree(String startClassName, 
																		 Collection endClassNames,
																		 Persistible startingObject);
		public Vector getOnly(Hashtable h, String classType);
		// DELETE public Hashtable getInstanceTreeInstances(String startClassName, Collection endClassNames);
	//public void addTransaction();

		public Collection getSubclasses(Class persistibleType);
		public Collection getSuperClasses(Class persistibleType);
		public boolean isDirectlyLinkable(Class linkFrom, Class linkTo);
		public Object directlyLinkableVia(Class linkFrom, Class linkTo);
		public Collection getDirectlyLinkable(Class linkFrom);
// 	DELETE 	public boolean isConnectedAsChild(Persistible persistible,
// 																			Persistible persistible2);
// 		public boolean isConnectedAsParent(Persistible persistible,
// 																			 Persistible persistible2);
		public boolean isConnected(Persistible persistible, 
															 Persistible persistible2);

		public boolean isModified(Persistible persistible );
		public int getProjectVersion();
		public DataSourceMap getDataSourceInfo();
		public Object getValueFromPersistible(Persistible persistible, 
																					AttributeAccessMap attribute);
		public void setValueInPersistible(Persistible persistible, 
																				AttributeAccessMap attribute,
																				Object value);
}
