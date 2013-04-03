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
import javax.swing.JOptionPane;
import javax.swing.JFrame;

import edu.stanford.smi.protege.model.*;
import oncotcap.util.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.browser.TreeDisplayModePanel;
import oncotcap.display.common.OncTreeNode;
import oncotcap.datalayer.persistible.TcapLogicalOperator;
/**
 * Main class responsible for retrieving and storing information from/to the
 * Protege Knowledge Base and translating the objects betwteen Protege Instance
 * to Java classes
 *
 * @author   morris
 * @created  March 11, 2003
 */
public class ProtegeDataSource extends Object implements OncoTCapDataSource {
    
		Integer masterProjectVersion = null;
		ProjectInfo projectInfo = null;
    /** */
    private Cls dataSourceMapCls = null;
    /** */
    private Collection dataSourceMaps = null;
    /** Description of the Field */
    //private String className;
    /** Flag used to control the recursion in find routines */
    private boolean stillSearching = false;
    private boolean stillBuildingTree = false;
		private String searchPathId = null;
    private static String ROOT = String.valueOf(-1);
		private Vector doNotSaveList = null;
    /**
     * Hash that holds all the descriptions for mapping the objects and fields
     * between Java and Protege
     */
    private DataSourceMap dataSourceInfo = null;
    /** */
    private Hashtable dataSourceInfoInverseMap = null;
    /** Current open protege knowledge base */
    private KnowledgeBase kb = null;
    /** Current open Protege project */
    private Project project = null;
    /** Default Protege project name */
    private String projectName =
	"C:/Program Files/Protege-2000/Demo.pprj";
    private Hashtable instanceTreeHashtable = null;
		private Vector resultValues = new Vector();
    
    /** Constructors */
    public ProtegeDataSource() {
				setProject(null);
				initDataSource();
				System.out.println("PDS Hashcode " + projectName);
    }
    
    /**
     * Constructor for the ProtegeDataSource object
     *
     * @param projectName Protege file path name
     */
    public ProtegeDataSource(String projectName) {
				this.projectName = projectName;
				setProject(projectName);
				initDataSource();
				//System.out.println("PDS 1" + projectName);
				//oncotcap.util.ForceStackTrace.showStackTrace();
    }
    
    
    /** Save to disk the protege project */
    public boolean commit() {
				ArrayList arrayList = new ArrayList();
				try {
						// Increment the master projects version 
						Cls projectInfoCls =
								kb.getCls("ProjectInfo");
						//Get all instances of the the named class
						Vector theInstances = 
								new Vector(projectInfoCls.getInstances());
						if ( theInstances.size() <= 0) {
								System.out.println("Missing the project info instance");
						}
						else {
								((Instance)theInstances.firstElement()).setOwnSlotValue
										(kb.getSlot("versionNumber"),
										 new Integer(getProjectVersion()+1));
						}
						oncotcap.display.editor.EditorFrame.updateAll();
		
						project.save(arrayList);
						if ( arrayList.size() > 0 ) {
								System.out.println(arrayList);
								JOptionPane.showMessageDialog
										((JFrame)null, 
										 arrayList.toString());
								return false;
						}
						
				}
				catch (Exception e) {
						String protegeException = 
								"Unable to save project file. No error ArrayList given";
						System.out.println(protegeException);
						e.printStackTrace();
						JOptionPane.showMessageDialog
								((JFrame)null, 
								protegeException + " " + e);
						return false;
				}
				return true;

    }
    
		// Delete all orphaned instances
		public void cleanUpOrphans() {
				// Get rid of any CB that does not have a ST
				// Get rid of any SB not based on a ST
				// get rid of any ValueMapEntry not associated with a SB
				// Get rid of any action that is not attached to a cb
				// WHen you get rid of a CB get rid of all Variables etc attached. 
				// Get rid of any VariableModificatin with no modifyvariablecontainingme
				// VariableDefinition w/o parameters containing me
		}
    
    /** Find an instance of a particular Object with a given GUID.
		 * This would be used mainly to refresh  a persistible (obsolete)
     * @param obj  
     * @param guid 
     * @return     Persistible
     * @see        OncoTcapDataSource
     */
    public Persistible find(Persistible obj, GUID guid) {
				Class persistibleClass = null;
				String javaClassName = null;
				try {
						Instance inst = kb.getInstance(guid.toString());
						
						if ( obj == null ) {
								String protegeClassName = inst.getDirectType().getName();
								javaClassName =
										(String)getDataSourceInfo().javaClassForProtegeClass(protegeClassName);
								persistibleClass = 
										ReflectionHelper.classForName(javaClassName);
						}
						return (toPersistible(persistibleClass, inst));
				}
				catch (Exception e) {
						System.out.println("Error find(Persistible, GUID): Unable to findinstance.");
						//e.printStackTrace();
				}
				return null;
    }
    
    
    /** Find an instance of a particular Object with a given GUID.
		 *  Used to refresh or retrieve a object from the DS
     * @param guid
     * @return     Persistible
     * @see        OncoTcapDataSource
     */
    public Persistible find(GUID guid) {
				try {
						Instance inst = kb.getInstance(guid.toString());
						if ( inst == null ) {
								inst.getName();
								System.out.println("Unable to locate instance for GUID: " 
																	 + guid);
								return null;
						}
						return toPersistible(inst);
				}
				catch (Exception ex) {
						System.out.println("Error find(GUID): " +
															 " Unable to locate instance for GUID: " + guid);
						ex.printStackTrace();
				}
				return null;
    }
    
    
    
    /** Find an instance of a particular Object with a given GUID.
		 *  Used to refresh or retrieve a object from the DS
     * @param persistibleObject Oncotcap Java representation of an 
		 *                          object stored in the KB
     * @return                  Collection of Persistible objects
     * @see                     OncoTcapDataSource
     */
    public Collection find(Persistible persistibleObject) {
				String className = persistibleObject.getClass().getName();
				//System.out.println("className: " + className);
				
				String protegeClassName =
						getDataSourceInfo().protegeClassForJavaClass(className);
				Collection dsObjects = find(protegeClassName);
				
				// Convert the data source objects into the persistible
				// object format
				Iterator i = dsObjects.iterator();
				Vector persistibleList = new Vector();
				
				while (i.hasNext()) {
						Instance anInstance = (Instance)i.next();
						
						// Now fill it in
						Persistible pers = toPersistible(persistibleObject.getClass(),
																						 anInstance);
						if ( pers != null )
								persistibleList.addElement(pers);
				}
				return persistibleList;
    }
 
    /**Find all instance of a particular type
     * @param findClass Type of object to retrieve all KB instances of
     * @return          Collection of Persistible objects
     * @see             OncoTcapDataSource
     */
    public Collection find(Class findClass) {
				//System.out.println("FIND " + projectName + " class " + findClass);
				//				if ( projectName.equals("u:/wdir/tcapdata/oncotcap-test.pprj") ) 
				//	System.out.println("LOADING");
				Persistible persistibleObject = null;
				if (findClass == null) 
						return null;
								
				String className = findClass.getName();

				Collection dsObjects =
						find(getDataSourceInfo().protegeClassForJavaClass(className));
				
				// Convert the data source objects into the persistible
				// object format
				if (dsObjects == null ) 
						return new Vector();
				Iterator i = dsObjects.iterator();
				Vector persistibleList = new Vector();
				
				while (i.hasNext()) {
						Instance anInstance = (Instance)i.next();
						// Now fill it in
						Persistible pers = toPersistible(findClass, anInstance);
						if ( pers != null )
								persistibleList.addElement(pers);
				}
				return persistibleList;
    }
    
    /**
     * Find all instance of the named classDescription of the Method
     * obsolete
     * @param className
     * @return Collection of native datasource objects FUTURE add boolean
     *         to return persistible objects
     */
    public Collection find(String className) {
				if (className == null) {
						return null;
				}
				try {
						// Find the named class
						Cls aClass =
								kb.getCls(className);
						//Get all instances of the the named class
						Collection theInstances = aClass.getInstances();
						
						return theInstances;
				}
				catch (Exception e) {
						System.out.println("Unable to locate class " + className);
				}
				return null;
    }
    

    /**
     * Find all instance of the named class that are related to the selected
     * instance FUTURE add boolean to return persistible objects *
     *
     * @param className      Description of Parameter
     * @param selectedObject Description of Parameter
     * @return               Description of the Returned Value
     */
    public Collection find(Class clas, String keywordString) {
				String javaClassName = clas.getName();
				String protegeClassName =
								getDataSourceInfo().protegeClassForJavaClass(javaClassName);
				Cls searchClass = null;
				searchClass = kb.getCls(protegeClassName);
				Vector matchedInstances = new Vector();
				Persistible testPersistible = null;
				// Now search in the browser text field for the string form
				// of the keyword
				Collection allInstances = searchClass.getInstances();
				Iterator iter = allInstances.iterator();
				String lowerCaseKeyword = null;
				if ( keywordString != null ) {
						lowerCaseKeyword = keywordString.toLowerCase();
						while ( iter.hasNext() ) {
								Instance testInstance = (Instance)iter.next();
								String browserText = testInstance.getBrowserText();
								String lowerCaseBrowserText = browserText.toLowerCase();
								if ( lowerCaseBrowserText.indexOf(lowerCaseKeyword) > -1 ) {
										// word appears in the text - add it to the list
										testPersistible = toPersistible(testInstance);
										if ( matchedInstances.contains(testPersistible) == false ) {
												matchedInstances.addElement(testPersistible);
										}
								}
						}
				}
				return matchedInstances;
				
		}


    /**
     * Find all instance of the named class that are related to the selected
     * instance FUTURE add boolean to return persistible objects *
     *
     * @param className      Description of Parameter
     * @param selectedObject Description of Parameter
     * @return               Description of the Returned Value
     */
    public Collection find(String className, Object selectedObject, 
													 boolean searchBrowserKey) {
				Instance selectedInstance = null;
				resultValues = new Vector();
				//FUTURE - need to make it possible to do this when the 
				// selected object is a persistible  object
				if ( selectedObject instanceof Persistible ) {
						// Convert to its protege KB couterpart
						selectedInstance = 
								kb.getInstance(((Persistible)selectedObject).getGUID().toString());
				}
				else
						selectedInstance = (Instance)selectedObject;
				// Make sure this is not a keyword search
				if ( selectedInstance.hasType(
						 getKnowledgeBase().getCls("Keyword")) ) {
						// Do a keyword search
						Slot keywordSlot = 
								getKnowledgeBase().getSlot("keyword");
						String keyword = 
								(String)selectedInstance.getOwnSlotValue(keywordSlot);
						return find(className, keyword, searchBrowserKey);
				}
				else {
						//System.out.println("find related " + selectedInstance +
						//		   " className  " + className);
						try {
								
								// Find the named class
								Cls aClass =
										kb.getCls(className);
								if ( className == null || aClass == null ) {
										System.out.println("Class is null or not found in Protege " 
																			 + className);
										return new Vector(); // bill prefers empty vector to null
								}

								// Find a path ( steps to take through framed & slots) between the
								// two class types ex. between Knowledge Nugget and StatementBundle
								ProtegePath thePath =
										new ProtegePath(className, selectedInstance, kb);
								Vector selectedInstances = new Vector();
								
								selectedInstances.addElement(selectedInstance);
								
								Vector theValues = findValues(thePath, selectedInstances, 0);
								
								return (Collection)theValues;
						}
						catch (Exception e) {
								e.printStackTrace();
						}
						return null;
				}
		}
    
    
    
    
    /**
     * Find objects using the keyword
     *
     * @param className     Description of Parameter
     * @param keywordString Description of Parameter
     * @return              Description of the Returned Value FUTURE add boolean to
     *                      return persistible objects
     */
    public Collection find(String className,
			   String keywordString, boolean searchBrowserKey) {
				Cls searchClass = null;
				searchClass = kb.getCls(className);
				
				Vector matchedInstances = new Vector();
				Cls keywordCls = kb.getCls("Keyword");
				Slot keywordSlot = kb.getSlot("keyword");
				Slot describedInstancesSlot = kb.getSlot("describedInstances");
				Collection keywords = keywordCls.getInstances();
				Iterator i = keywords.iterator();
				
				while (i.hasNext()) {
						Instance keyword = (Instance)i.next();
						String keywordValue = (String)keyword.getOwnSlotValue(keywordSlot);
					// 	System.out.println("keyword " + keyword 
// 															 + "keywordValue " + keywordValue);
						if (keywordString != null && keyword != null 
								&& keywordString.equalsIgnoreCase
								((String)keyword.getOwnSlotValue(keywordSlot))) {
								// Now find all the classes that are linked to this keyword
								// Could use an existing find function but in the
								// interest of time go directly at it
								Collection instances =
										keyword.getOwnSlotValues(describedInstancesSlot);
								// Now narrow it to the type of instance you would like
								Iterator ii = instances.iterator();
								
								while (ii.hasNext()) {
										Instance matchedCls = (Instance)ii.next();
										
										if (matchedCls.hasType(searchClass)) {
												matchedInstances.addElement(matchedCls);
										}
								}
						}
				}
				
				if ( searchBrowserKey ) {
						// Now search in the browser text field for the string form
						// of the keyword
						searchClass = kb.getCls(className);
						Collection allInstances = searchClass.getInstances();
						Iterator iter = allInstances.iterator();
						String lowerCaseKeyword = null;
						if ( keywordString != null ) {
								lowerCaseKeyword = keywordString.toLowerCase();
								while ( iter.hasNext() ) {
										Instance testInstance = (Instance)iter.next();
										String browserText = testInstance.getBrowserText();
										String lowerCaseBrowserText = browserText.toLowerCase();
										if ( lowerCaseBrowserText.indexOf(lowerCaseKeyword) > -1 ) {
												// word appears in the text - add it to the list
												if ( matchedInstances.contains(testInstance) == false ) {
														matchedInstances.addElement(testInstance);
												}
										}
								}
						}
				}
				
				return matchedInstances;
    }
    
    
    /**
     * Find objects using the keyword
     *
     * @param persistible   Description of Parameter
     * @param keywordString Description of Parameter
     * @return              Collection of native objects FUTURE add boolean to
     *                      return persistible objects
     */
    public Collection find(Persistible persistible,
			   String keywordString, boolean searchBrowserKey) {
				// Determine which type of object you are looking for
				String javaClassName = persistible.getClass().getName();
				String protegeClassName = null;
				
				try {
						protegeClassName =
								getDataSourceInfo().protegeClassForJavaClass(javaClassName);
				}
				catch (Exception e) {
						System.out.println("Unable to find protegeClass in data mapping  for " +
															 javaClassName);
						return null;
				}
				return find(protegeClassName, keywordString, searchBrowserKey);
    }
    
    
    /** Get all data source maps which is a list of each persistible
	     class and a description of the attribute mappings and
	     the methods in the persistible object to get data from
	     and put data in
		*/
    public void initDataSource() {
				try {
						// Get all data source maps which is a list of each persistible
						// class and a description of the attribute mappings and
						// the methods in the persistible object to get data from
						// and put data in
						dataSourceMapCls = kb.getCls("DataSourceMap");
						
						//Get all instances of the the named class
						dataSourceMaps = dataSourceMapCls.getInstances();
						
						// Create the data source map but only fill in the
						// java name and protege name
						// Do not instantiate any attribute access maps because that
						// would cause classes to be accessed and
						// static code executed which causes a problem
						Iterator i = dataSourceMaps.iterator();
						String javaClass = null;
						String protegeName = null;
						// For each object type ( e.x. statementbundle, keyword)
						int cnt = 0;
						
						while (i.hasNext()) {
								Instance dataSourceMap = (Instance)i.next();
								
								javaClass =
										(String)dataSourceMap.getOwnSlotValue(
																													kb.getSlot("javaClassName"));
								// Cannot report error for missing javaClass here because 
								// of the static block find methods in the "model" objects 
								// checking for class existence would execute those 
								// blocks of code and cause a gridlock
								// Only thing that can be checked at this point is the existence 
								// of slots on particular classes
								
								Instance dsObjectInstance =
										(Instance)dataSourceMap.getOwnSlotValue(
																														kb.getSlot("dataSourceObjectList"));
								
								if (dsObjectInstance == null ) {
										System.out.println("Error processing javaClass " 
																			 + javaClass
																			 + " in DataSOurceMap "
																			 + dataSourceMap );
								}
								else {
										Frame aFrame = 
												(Frame)dsObjectInstance.getOwnSlotValue(
																																kb.getSlot("protegeClass"));
										
										if ( aFrame != null ) {
												protegeName = aFrame.getName();
												// System.out.println("add to data source " + 
// 																					 cnt + " " + 
// 																					 javaClass
// 																					 + " " + 
// 																					 protegeName
// 																					 + " " + dsObjectInstance);
												getDataSourceInfo().put(cnt, javaClass, protegeName,
																								null, dsObjectInstance);
												cnt++;
										}
										else {
												System.out.println("WARNING: missing protegeClass for " 
																					 + dsObjectInstance);
										}
								}
								//		System.out.println("Init " + cnt + " " +
								//	   javaClass + " " + protegeName
								//	   + " " + dsObjectInstance);
						}
						
				}
				catch (Exception e) {
						System.out.println("DataSourceMap does not exist in Protege Project."
															 + projectName);
						e.printStackTrace();
						System.exit(0);
				}
				// Set the project version
				setProjectVersion();
		}
		
		private void setProjectVersion() {
				Vector masterProjectInfos = 
						new Vector(find(ProjectInfo.class));
				if ( masterProjectInfos.size() > 0) {
						projectInfo = 
								(ProjectInfo)masterProjectInfos.elementAt(0);
						System.out.println("master " + projectInfo);
						
						masterProjectVersion = projectInfo.getVersionNumber();
					
				}
		}
		
		public int getProjectVersion() {
				return masterProjectVersion.intValue();
		}
    
    /**
     * For protege insert is a little different since an object can be instantiated
     * empty so this basically just defers to update since I always want to make
     * sure the instance exists before I update anyway
     *
     * @param persistible
     * @return            guid of new object
     */
    public GUID insert(Persistible persistible) {
				doNotSaveList = new Vector();
				// To prevent looping 
				return update(persistible, doNotSaveList);
    }
    
    /**
     * For protege insert is a little different since an object can be instantiated
     * empty so this basically just defers to update since I always want to make
     * sure the instance exists before I update anyway
     *
     * @param persistible
     * @return            guid of new object
     */
    public GUID insert(Persistible persistible, Vector doNotSaveList) {
				// To prevent looping 
				// If the instance exists then just return guid RECURSION OFF
				if ( persistible.getGUID() != null &&
						 kb.getInstance(persistible.getGUID().toString()) //12/14
						 != null ) {
						//System.out.println("Already Exists " + persistible);
						return persistible.getGUID();
				}
				return update(persistible, doNotSaveList);
    }
    
    
    /**
     * Convert from a protege instance to the java desired java class
     *
     * @param classType desired Java class
     * @param inst      protege instance
     * @return          java instance
     */
    public Persistible toPersistible(Class classType, Object inst) {
				return toPersistible(classType, (Instance)inst);
    }
    
    
    /**
     * Convert from a protege instance to the java desired java class
     *
     * @param inst protege instance
     * @return     java instance
     */
    public Persistible toPersistible(Object inst) {
				//System.out.println("toPersistible " + inst);
				String protegeClassName = null;
				String javaClassName = null;
				try {
						protegeClassName = ((Instance)inst).getDirectType().getName();
						javaClassName =
								(String)getDataSourceInfo().javaClassForProtegeClass(protegeClassName);
						// Does this instance already exist in persistible form
						GUID guid1 = GUID.fromString(((Instance)inst).getName());
						if ( guid1 != null ) {
								//OncMergeBrowser
								//Persistible persistible = 
								//		AbstractPersistible.getPersistible(guid1);

								Persistible persistible = 
										AbstractPersistible.getPersistible(guid1, this);
							
								if (persistible != null) {
										//return AbstractPersistible.getPersistible(guid1);
										//OncMergeBrowser
										return AbstractPersistible.getPersistible(guid1, this);
								}


						}
						return toPersistible(ReflectionHelper.classForName(javaClassName), (Instance)inst);
				}
				catch (Exception e) {
						System.out.println("Cannot convert to persistible " + protegeClassName 
													 + " or find in the data mapping " + javaClassName);
						e.printStackTrace();
				}
				return null;
    }
    
    
    /**
     * Update data source for the specified persitible object
     *
     * @param persistible Description of Parameter
     * @param guid        Description of Parameter
     * @return            Description of the Returned Value
     */
    public GUID update(Persistible persistible, String guid) {
				//System.out.println("UPDATE " + guid);				
				doNotSaveList = new Vector();
				return update(persistible, doNotSaveList);
    }
    
    
    /**
     * Update protege
     *
     * @param persistible Description of Parameter
     * @param guid        Description of Parameter
     * @return            Description of the Returned Value
     */
    public GUID update(Persistible persistible, GUID guid) {
				//System.out.println("UPDATE " + guid);

				doNotSaveList = new Vector();
				return update(persistible, doNotSaveList);
    }
    
    /**
     * Description of the Method
     *
     * @param persistible Description of Parameter
     * @return            Description of the Returned Value
     */
    public GUID update(Persistible persistible) {
				doNotSaveList = new Vector();
				// if ( persistible instanceof oncotcap.datalayer.autogenpersistible.Article) {
				// 						System.out.println("Updating " + persistible + 
				// 	 " " + ((oncotcap.datalayer.autogenpersistible.Article)persistible).getKeywords() );
				// 				}
				return update(persistible, doNotSaveList);
		}   
		
    /**
     * Description of the Method
     *
     * @param persistible Description of Parameter
     * @return            Description of the Returned Value
     */
    public GUID update(Persistible persistible, Vector doNotSaveList) {
				//System.out.println("UPDATE doNotSaveList" + persistible.getGUID());
				// 		if ( persistible instanceof CodeBundle
				// 					|| persistible instanceof StatementTemplate) {
				// 									System.out.println("updating in PDS " 
				// 																		 + persistible.getClass()
				// 																 + " with guid " + persistible.getGUID());	 
				// 				}

				GUID guid = null;
				// Make sure the do not save persistibles do not slip through
				if ( persistible.getPersistibleState() == Persistible.DO_NOT_SAVE ) {
						doNotSaveList.addElement(persistible);
						return persistible.getGUID();
				} 
				if ( persistible.getGUID()!= null &&
						 doNotSaveList.contains(persistible) )
						return persistible.getGUID();
				else
						doNotSaveList.addElement(persistible);

				String javaClassName = persistible.getClass().getName();
				// Convert from the persistible object to a
				// Protege instance
				//				System.out.println("Update persistible " + javaClassName
				//									 + " " + persistible + " " + persistible.getGUID());
				Instance instance = getValidInstance(persistible);
				// 				if ( instance.hasDirectType(kb.getCls("QuoteNugget")))
				// 					System.out.println("THE INSTANCE " 
				// 						 + instance.getOwnSlotValue(kb.getSlot("quote")));
				
				Vector dsObjects =
						getDataSourceInfo().mapForJavaClass(javaClassName);
				
				for (int n = 0; n < dsObjects.size(); n++) {
						// For each protege class in the mapping
						// Get the attributes
						DataSourceObject dsObject = (DataSourceObject)dsObjects.elementAt(n);
						String protegeClassName = dsObject.name;
						
						for (Enumeration e = dsObject.attributeAccessMaps.elements();
								 e.hasMoreElements(); ) {
								AttributeAccessMap attribute =
										(AttributeAccessMap)e.nextElement();
								
								//if the object is not a user defined class
								//create it - create an interface that is called UserDefined
								// No necessary methods just a tag
								// Get the slot value that corresponds to the attribute and call to
								// toPersistible recursively then attach the returned value to the
								// current object
								
								Slot aSlot =
										kb.getSlot(attribute.dsAttributeName);
								
								try {
										Iterator i = null;
										Object value = null;
										value = getValueFromPersistible(persistible,
 																										attribute);
										if (value != null) {
												// If it is a single value
												if (attribute.attributeSubType == null ||
														"null".equals(attribute.attributeSubType)) {
														if (value instanceof 
																oncotcap.datalayer.Persistible) {
																GUID newObjectGUID = 
																		insert((Persistible)value, doNotSaveList);
																// And put it in
																// throw an error right here if you are saving a keyword
																if ( newObjectGUID != null ) {
																		setProtegeValue(instance,
																										aSlot, 
																										kb.getInstance
																										(newObjectGUID.toString()));
																}
														}
														else {
																setProtegeValue(instance, aSlot, value);
														}
												}

												else { // This is a collection
														if (isOfType("java.util.Collection", 
																				 value.getClass())) {
																i = ((Collection)value).iterator();
														}
														else if (isOfType("java.util.Iterator", 
																							value.getClass())) {
																i = (Iterator)value;
														}
														if (isOfType("oncotcap.datalayer.Persistible",
																				 ReflectionHelper.classForName
																				 (attribute.attributeSubType))) {
																
																// Remove all slot values before 
																// inserting adding
																removeAllSlotValues(instance, aSlot);
																// Loop thru and create them then attach
																// them to this object -
																// this is bad but thought it may be 
																// a performance boost
																Vector instances = new Vector();
																Persistible pers = null;
																while (i != null && i.hasNext()) {
																		pers = (Persistible)i.next();
																		GUID newInstGUID = insert(pers, doNotSaveList);
																
																		// Add instance to collection
																		instances.addElement(
																												 kb.getInstance(newInstGUID.toString()));
																}
																//System.out.println("instance count " + instances.size());
																// Set the values this should delete existing instances
																// Make sure the value does not already exist
																// before adding it - not sure if that wasn't working before
																// and if it was why i had the following code but will test to
																// see
																try {
																
																		setProtegeValues(instance, aSlot, instances);
																	// 	instance.setOwnSlotValues(aSlot, 
// 																															instances);
																} 
																catch ( Exception pe) {
																		System.out.println("Error updating Protege model while " +
  																											 "accessing " + pers 
																											 + " for attribute "  +
																											 attribute.dsAttributeName.getClass()
																											 + " instance " +
																											 attribute.dsAttributeName);
																}
																// while
														}
														//if persistible
														else {
																// non UD type
																//System.out.println("called again ");
																removeAllSlotValues(instance, aSlot);
																while (i.hasNext()) {
																		Object obj = i.next();
																		try {
																				//System.out.println("saving string " + obj);
																				instance.addOwnSlotValue(aSlot, obj);
																		} 
																		catch ( Exception pe) {
																				System.out.println("Error updating Protege model while " +
																													 "accessing " + instance 
																													 + "slot " + aSlot
																													 + " with value type "  +
																													 obj.getClass()
																													 + " instance " +
																													 obj);
						
																		}		
																} // while
														}
														// non UD type
												}
												//collection
										}
										// Value is null
								}
								catch (Exception e6) {
										e6.printStackTrace();
								}
								
						}
						//dsObjects
				}
				return persistible.getGUID();
    }


    /**
     * Determine if the object has any different values from the 
		 * version stored in the data source
     * just to keep this function separate and since it does not require 
		 * any recursion i will not check this as i update the protege information
     *
     * @param persistible 
     * @return boolean    true if object is different
     */
    public boolean isModified(Persistible persistible ) {
				boolean changed = false;
				if ( persistible == null || persistible.getGUID() == null ) 
						return false;
				GUID guid = persistible.getGUID();
				String javaClassName = persistible.getClass().getName();
				// Get protege instance matching this guid
				Instance instance = getValidInstance(persistible);
				if ( instance == null )
						return true; // new instance
				// Get attribute mapping info
				Vector dsObjects =
						getDataSourceInfo().mapForJavaClass(javaClassName);
				
				for (int n = 0; n < dsObjects.size(); n++) {
						// For each protege class in the mapping
						// Get the attributes
						DataSourceObject dsObject = 
								(DataSourceObject)dsObjects.elementAt(n);
						String protegeClassName = dsObject.name;
					
						for (Enumeration e = dsObject.attributeAccessMaps.elements();
								 e.hasMoreElements(); ) {
								AttributeAccessMap attribute =
										(AttributeAccessMap)e.nextElement();
								if ( attribute.dsAttributeName.equals("creator") 
										 || attribute.dsAttributeName.equals("creationTime")
										 || attribute.dsAttributeName.equals("modifier")
										 || attribute.dsAttributeName.equals
										 ("modificationTime")
										 || attribute.dsAttributeName.equals
										 ("versionNumber") )
										continue; // don't use these fields to determine mods

								Slot aSlot =
										kb.getSlot(attribute.dsAttributeName);
							
								try {
										Object persistibleValue = null;
										Object protegeValue = null;
										// get value from persistible
										persistibleValue = 
												getValueFromPersistible(persistible, 
																								attribute);
										// get value from protege
										protegeValue = instance.getOwnSlotValues(aSlot);
										if (isNull(persistibleValue) 
												&& isNull(protegeValue) )
												continue;
										if ((!isNull(persistibleValue) 
												 && isNull(protegeValue))
												|| (isNull(persistibleValue)
													&& !isNull(protegeValue)) ) {
											// 	System.out.println
// 														("Instance modified one of the items is null "
// 														 + attribute.dsAttributeName 
// 														 + " pers  " + persistibleValue 
// 														 + " prot " + protegeValue);
												return true; // instance changed the list sizes <>
										}
										// because getownslotvalues used the 
										// protegeValue if non null will always be a 
										//collection so use persistible to determine if there 
										// is really a list
										// both non null
										else if ( persistibleValue instanceof Collection && 
															protegeValue instanceof Collection ) {
												changed = hasCollectionChanged
														((Collection)persistibleValue,
														 (Collection)protegeValue);
												if ( changed ) 
														return true;
										}
										else if ( persistibleValue instanceof Iterator && 
															protegeValue instanceof Collection ) {
												changed = hasCollectionChanged
														((Iterator)persistibleValue,
														 (Collection)protegeValue);
												if ( changed ) 
														return true;
										}
										else {
												Object[] protegeValueArray = 
														((Collection)protegeValue).toArray();
												if ( Array.getLength(protegeValueArray) > 0 ) 
														changed = 
																hasSingleValueChanged
																(persistibleValue,
																 protegeValueArray[0]);
												if ( changed ) 
														return true;
										}
								}
								catch (Exception e6) {
										e6.printStackTrace();
								}
						}
						//dsObjects
				}
				return false;
    }
		
		private boolean isNull(Object obj) {
				if ( obj == null )
						return true;
				else if ( obj instanceof Collection && 
									((Collection)obj).size() == 0)
						return true;
				else if ( obj instanceof String 
									&& ( ((String)obj).length() == 0
										 || StringHelper.removeWhiteSpace
											 ((String)obj).length() == 0) )
						return true;
				return false;
		}
		private boolean hasCollectionChanged(Iterator persistibleValue,
																				 Collection protegeValue) {
				Vector collection = new Vector();
				// Put it into a collection
				while ( persistibleValue.hasNext()) {
						collection.addElement(persistibleValue.next());
				}
				return hasCollectionChanged(collection,
																		protegeValue);
		}
		private boolean hasCollectionChanged(Collection persistibleValue,
																				 Collection protegeValue) {
				
				if ( persistibleValue.size() != protegeValue.size() ) {
						//System.out.println("Instance modified dif size lists " );
						return true; // instance changed the list sizes <>
				}
// 				if ( protegeValue != null 
// 						 && protegeValue instanceof Collection) {
// 						// Loop thru and compare to persistible values
// 						Iterator i = 
// 								((Collection)protegeValue).iterator();
// 					// 	while (i.hasNext()) {
								
// // 						}
// 				}
				return false;
		}
		
		private boolean hasSingleValueChanged(Object persistibleValue,
																		 Object protegeValue) {
				if ( protegeValue instanceof Instance ){// && persistibleValue instanceof Persistible) {
						// Instances
						//System.out.println("SINGLE INSTANCE " + protegeValue);
						if ( !(persistibleValue instanceof Persistible) ) {
								System.out.println("WHat is this case " 
																	 + persistibleValue.getClass()
																	 + " " + protegeValue);
						}
						else if ( !((Persistible)persistibleValue).getGUID().equals
								 (((Instance)protegeValue).getName())) {
								//System.out.println("Instance modified instance diff" );
								return true; // instance changed the list sizes <>
						}
						return false;
				}
				else if (persistibleValue.getClass().equals(protegeValue.getClass() )) {
						if ( !persistibleValue.equals(protegeValue) ) {
// 								System.out.println("single instance " 
// 																	 + protegeValue.getClass()
// 																	 + " " 
// 																	 + persistibleValue.getClass());
								return true; // instance changed the list sizes <>
						}
						return false;
				}
				return false;
		}
		
		public void delete(Persistible persistible){ 
				GUID guid = persistible.getGUID();
				if ( guid != null ) {
						Instance instance = kb.getInstance(guid.toString());
						if ( instance != null ) 
								instance.delete();
				}
		}

    /**
     * Gets the SlotNames attribute of the ProtegeDataSource object
     *
     * @param className         Description of Parameter
     * @param includeMultiSlots Description of Parameter
     * @return                  The SlotNames value
     */
    public Collection getSlotNames(String className,
				   boolean includeMultiSlots) {
	// Find the named class
	Cls aClass =
	    kb.getCls(className);
	
	// Get a list of all the slot names
	Collection theSlots = aClass.getTemplateSlots();
	Iterator i = theSlots.iterator();
	Vector slotNames = new Vector();
	
	while (i.hasNext()) {
	    Slot theSlot = (Slot)i.next();
	    
	    // Do not add multislots to the list if flag is set to false
	    if ((includeMultiSlots == true) ||
		(includeMultiSlots == false
		 && theSlot.getAllowsMultipleValues() == false)) {
		slotNames.addElement(theSlot.getName());
	    }
	}
	return slotNames;
    }
    
    
    /**
     * Gets the Slots attribute of the ProtegeDataSource object
     *
     * @param className         Description of Parameter
     * @param includeMultiSlots Description of Parameter
     * @return                  The Slots value
     */
    public Collection getSlots(String className, boolean includeMultiSlots) {
	// Find the named class
	Cls aClass =
	    kb.getCls(className);
	
	// Get a list of all the slot names
	Collection theSlots = aClass.getTemplateSlots();
	Iterator i = theSlots.iterator();
	Vector slots = new Vector();
	
	while (i.hasNext()) {
	    Slot theSlot = (Slot)i.next();
	    
	    // Do not add multislots to the list if flag is set to false
	    if ((includeMultiSlots == true) ||
		(includeMultiSlots == false
		 && theSlot.getAllowsMultipleValues() == false)) {
		slots.addElement(theSlot);
	    }
	}
	return slots;
    }
    
    
    /**
     * Description of the Method
     *
     * @param thePath      Description of Parameter
     * @param theInstances Description of Parameter
     * @param pathPosition Description of Parameter
     * @return             Description of the Returned Value
     */
    private Vector findValues(ProtegePath thePath, Vector theInstances,
			      int pathPosition) {
	
				//				oncotcap.util.ForceStackTrace.showStackTrace();
				// If there is no path you won't be able to find any values
				if ( thePath.getSlotPath().isEmpty() ) {
						//System.out.println("Unable to find a path for " + thePath);
						return new Vector();
				}
			// 	System.out.println("Found a path for (findValues)" + thePath + "via" + 
//  												 thePath.getSlotPath());

				// Determine the current slot and classes being sought
				//System.out.println("slot path " + thePath.getSlotPath());
				
				Slot searchSlot = (Slot)(thePath.getSlotPath()).elementAt(pathPosition);
				Cls searchClass = (Cls)(thePath.getClassPath()).elementAt(pathPosition);
				
				// If there are instances set the still searching flag
				if (theInstances.size() > 0) {
						stillSearching = true;
				}
				else {
						stillSearching = false;
						return theInstances;
				}
				
				// For each instance find related instances of the appropriate
				// type using the searchSlot provided in the path
				Vector allRelatedInstances = new Vector();
				
				for (int i = 0; i < theInstances.size(); i++) {
						Instance anInstance = (Instance)theInstances.elementAt(i);
						Vector relatedInstances =
								new Vector(anInstance.getOwnSlotValues(searchSlot));
						
						// Make sure they are of the appropriate type
						for (int j = 0; j < relatedInstances.size(); j++) {
								Instance aRelatedInstance =
										(Instance)relatedInstances.elementAt(j);
								
								if (aRelatedInstance.hasType(searchClass) == true) {
										allRelatedInstances.addElement(aRelatedInstance);
								}
						}
						// for relatedInstances
				}
				//for theInstances
				// Is this the end of the path
				
				if ((thePath.getSlotPath()).size() == (pathPosition + 1)) {
						// End of the path you have found the instances
						// you are looking for
						stillSearching = false;
						return allRelatedInstances;
				}
				else {
						// Keep going you have not reached the end of the path
						allRelatedInstances =
								findValues(thePath, allRelatedInstances, pathPosition + 1);
						return allRelatedInstances;
				}
    }

    /**
     * Description of the Method
     *
     * @param thePath      Description of Parameter
     * @param theInstances Description of Parameter
     * @param pathPosition Description of Parameter
     * @return             Description of the Returned Value
     */
    private Vector findValues(ProtegeClassPath thePath, Vector theInstances) {
				//oncotcap.util.ForceStackTrace.showStackTrace();
				ProtegeClassPath currentClassPath = null;
				// If there is no path you won't be able to find any values
				if ( thePath == null ||
						 thePath.getSlotPaths() == null 
						 || thePath.getSlotPaths().isEmpty() ) {
						//System.out.println("Unable to find a path for " + thePath);
						return new Vector();
				}
				//System.out.println("Found a path for (findValues)" + thePath);

				// Determine the current slot and classes being sought
				// For each slot in the class path work your way down
				Vector allRelatedInstances = new Vector();
						
				Iterator slotPathIterator = thePath.getSlotPaths().iterator();;
				while ( slotPathIterator.hasNext() ) {
						stillSearching = true;
						ProtegeSlotPath slotPath = 
								(ProtegeSlotPath)(slotPathIterator.next());
						Slot searchSlot = slotPath.getSlot();

						for (int k = 0; k < theInstances.size(); k++) {
								Instance anInstance = (Instance)theInstances.elementAt(k);
								Vector relatedInstances =
										new Vector(anInstance.getOwnSlotValues(searchSlot));
								for (int j = 0; j < relatedInstances.size(); j++) {
										Instance aRelatedInstance =
												(Instance)relatedInstances.elementAt(j);
										// removed from here 6.16
										// The instances are of the appropriate type 
										// as long as it matches any of the classPaths 
										// associated with this slot
										if ( (currentClassPath = 
													hasProperClsType(aRelatedInstance, slotPath)) 
												 != null ) {
												// Put each of the retrieved instances in the tree 
												// hashtable with the parent guid
												GUID parentGUID = 
														GUID.fromString(anInstance.getName());
												GUID guid = 
														GUID.fromString(aRelatedInstance.getName());
												if ( parentGUID != null  && guid != null ){		
														Persistible parentPersistible = 
																toPersistible(anInstance);
														Persistible persistible = 
																toPersistible(aRelatedInstance);
														if ( persistible != null 
																 && parentPersistible != null ) {
																addParent(persistible,
																					parentPersistible);
																// 06.23 oncotcap.util.ForceStackTrace.showStackTrace();
														} // if
												}	// if
										}//if
										findValues
												(currentClassPath, 
												 CollectionHelper.makeVector(aRelatedInstance));
										// the end of the road has been reached
										if (currentClassPath != null &&
												currentClassPath.getSlotPaths() == null) {
												// End of the path you have found the instances
												// you are looking for plop it on the result list
												//stillSearching = false;
												if ( resultValues.contains(aRelatedInstance) == 
														 false ) 
														resultValues.add(aRelatedInstance);
										}//if
								}// for
						}	//for
				}	//while
				return allRelatedInstances;
		}
				
		private ProtegeClassPath hasProperClsType(Instance inst, 
																							ProtegeSlotPath slotPath) {
				Vector classPaths = slotPath.getClassPaths();
				if (classPaths != null ) {
						Iterator i = classPaths.iterator();
						while ( i.hasNext() ) {
								ProtegeClassPath currentClassPath =
										(ProtegeClassPath)i.next();
								Cls searchClass = 
										currentClassPath.getCls();
								// May need to move this inside 
								//the currentClassPath.getslotpath else blockl
								if (inst.hasType(searchClass) == true){
										return currentClassPath;
								}//if
						}//while
				}//if
				return null;
		}

    /**
     * Convert this native format object to the proper persistible format
     *
     * @param classType Description of Parameter
     * @param inst      Description of Parameter
     * @return          Description of the Returned Value
     */
    private Persistible toPersistible(Class classType, Instance inst) {
				//				System.out.println("toPersistible " + inst);
				Persistible po = null;
				String protegeClassName = null;
				String javaClassName = null;
				GUID guid = null;
				
				try {
						protegeClassName = ((Instance)inst).getDirectType().getName();
						javaClassName =
								(String)getDataSourceInfo().javaClassForProtegeClass(protegeClassName);
						GUID guid1 = GUID.fromString(((Instance)inst).getName());
						if ( guid1 != null ) {
								//OncMergeBrowser
								Persistible persistible = AbstractPersistible.getPersistible(guid1, this);
								//OncMergeBrowser
								if (persistible != null) 
										return AbstractPersistible.getPersistible(guid1,this);
						}
						Class cls = ReflectionHelper.classForName(javaClassName);
						if ( inst == null ) {
								System.out.println("Cannot convert null instance to a Persistible");
								return null;
						}
						guid = GUID.fromString(inst.getName());
						// if ( classType == oncotcap.datalayer.persistible.ProcessDefinition.class)
// 								System.out.println("INSTANCE : " + inst.getName() + 
// 																	 " guid from string " + guid);
// 						if ( classType == 
// 								 oncotcap.datalayer.persistible.StatementBundle.class)
// 								System.out.println("SB INSTANCE : " + inst.getName() + 
// 																	 " guid from string " + guid);
						// Check to see if there is already a persistible instance 
						// of this object
						//System.out.println("GUID : " + guid);
						Persistible existingPersistible = null;
						if ( guid == null ) {
 								System.out.println(" Fixing GUID on instance" + inst);
								guid = new GUID();
								inst.setName(guid.toString());
								//inst.setOwnSlotValue(NAME_SLOT, guid.toString());
						}
						//OncMergeBrowser
						//existingPersistible = AbstractPersistible.getPersistible(guid);
						existingPersistible = AbstractPersistible.getPersistible(guid,this);
						if ( existingPersistible != null ) {
								// Use the existing object 
							// 	System.out.println("EXISTING PERSISTIBLE: " + this + " " +
// 																	 existingPersistible.getGUID());
								po = existingPersistible;
						}
						else {  // 04.13.04 does this further stop recursion
								// Create a new one
								po = createNewPersistible(cls, guid);
								//po = (Persistible)cls.newInstance();  
								//po.setGUID(guid);
								// System.out.println("NEW PERSISTIBLE: " + po.getClass() 
// 								 																	 + " " + po.getGUID());
								//	oncotcap.util.ForceStackTrace.showStackTrace();
						}
						if (po == null ) {
								System.out.println("null po " + cls);
						}
				}
				catch (NullPointerException npe) {
						System.out.println("Unable to convert " + protegeClassName 
															 + " instance to persistible");
						npe.printStackTrace();
						return null;
				}
				catch (Exception e1) {
						e1.printStackTrace();
				}
			
				if ( po instanceof Persistible ) {
						((Persistible)po).setSetSource(ProtegeDataSource.class);
				}
				if (  po instanceof AbstractPersistible ) {
						((AbstractPersistible)po).setDataSource(this);
				}
				Vector dsObjects =
						getDataSourceInfo().mapForJavaClass(javaClassName);
				
				for (int n = 0; n < dsObjects.size(); n++) {
						// For each protege class in the mapping
						// Get the attributes
						DataSourceObject dsObject = (DataSourceObject)dsObjects.elementAt(n);
						
						protegeClassName = dsObject.name;
						
						for (Enumeration e = dsObject.attributeAccessMaps.elements();
								 e.hasMoreElements(); ) {
								AttributeAccessMap attribute = (AttributeAccessMap)e.nextElement();
								
								//if the object is not a user defined class
								//create it - create an interface that is called UserDefined
								// No necessary methods just a tag
								// Get the slot value that corresponds to the attribute and call to
								// toPersistible recursively then attach the returned value to the
								// current object
								
								//Otherwise using the specified setter set the
								//object
								
								Method setter = attribute.setMethod;
								Object args[] = new Object[1];
								Slot aSlot = kb.getSlot(attribute.dsAttributeName);
								Collection values = null;
								
								//System.out.println("Setting : " + setter);
								if (inst.getOwnSlotValueCount(aSlot) > 0) {
										values = inst.getOwnSlotValues(aSlot);
								}
								args[0] = inst.getOwnSlotValue(aSlot);
								
								Class cls = null;
								Class collectionCls = null;
								boolean isPersistible = false;
								boolean isPersistibleList = false;
								boolean isCollection = false;
								boolean collectionIsPersistible = false;
								
								try {
										if ("List".equals(attribute.attributeType) ||
												"java.util.Collection".equals(attribute.attributeType) 
												|| "java.util.Vector".equals(attribute.attributeType)) {
												isCollection = true;
										}
										else if ("oncotcap.datalayer.PersistibleList".equals(
																							 attribute.attributeType)) {
												isPersistibleList = true;
										}
										//Determine what the subtype ( what is being stired in the 
										// 'collection' of objects
										if (isCollection || isPersistibleList) {
												collectionCls = 
														ReflectionHelper.classForName(attribute.attributeSubType);
												collectionIsPersistible =
														isOfType("oncotcap.datalayer.Persistible", 
																		 collectionCls);
										}
										else {
												// See if the object is an interface via subclass
												cls = ReflectionHelper.classForName(attribute.attributeType);
												if ( cls.isInterface() 
														 && cls.getPackage().getName().startsWith("oncotcap.") 
														 && !cls.getName().equals("oncotcap.datalayer.PersistibleList")) {
														// THis is probably a persisitable that is referenced
														// by its interface
														isPersistible = true;
												}
												else {
														isPersistible =
																isOfType("oncotcap.datalayer.Persistible",
																				 cls);
												}
										}
								}
								catch (ExceptionInInitializerError eiie) {
										System.out.println("ExceptionInInitializerError trying to get attribute/slot type");
								}
								catch (Exception e3) {
										System.out.println("Error determining type of attribute/slot " +
																			 "in ProtegeDataSource.toPersistible() " +
																			 "for " + attribute);
								}
								

								// If the object is a persistible interface
								// Need to determine what subclass to instantiate
								// Single Persitible value
								if (isPersistible) {
										setPersistibleItem(args,cls,po,attribute);
					
								}
								// Groups of persitible values
								else if (isCollection && collectionIsPersistible) {
										setPersistibleCollection(values, collectionCls, 
																						 po, attribute);
								}
								// Groups of persistible objects held in a PersistibleList
								else if (isPersistibleList && collectionIsPersistible) {
										setPersistiblePersistibleList(values, collectionCls, 
																						 po, attribute);
								}
								// A group of primitive (non user defined type) objects
								else if (isCollection && collectionIsPersistible == false) {
										setPrimitiveCollection(values, collectionCls, 
																						 po, attribute);
								}
								else if (isPersistibleList 
												 && collectionIsPersistible == false) {	
										setPrimitivePersistibleList(values, collectionCls, 
																					 po, attribute);
				
								}
								// Single primitive value
								else {
										try {
												setValue(setter, attribute, po, args);
										}
										catch (Exception e2) {
												e2.printStackTrace();
										}
								}
								// else
								
						}
						// for each attribute
				}
				//for each dsObject
				po.setGUID(GUID.fromString(inst.getName()));
				//				System.out.println("PDS inst name : " + inst + " po " + po);
				if ( po instanceof Persistible ) 
						((Persistible)po).setSetSource(null);
				if (  po instanceof AbstractPersistible ) {
						((AbstractPersistible)po).setDataSource(this);
				}

				po.setPersistibleState(Persistible.CLEAN);
				return po;
    }

    /** Create a new instance using the GUID constructor - this constructor
				sets the guid to the data source guid during instantiation instead
				of immediately after
		*/
		private Persistible createNewPersistible(Class cls, GUID guid) {
				try {
						// System.out.println("Creating new persistible " + cls 
// 															 + " with guid " + guid);
						Class[] instantiationArgs = new Class[] {GUID.class};
						Constructor constructor = cls.getConstructor(instantiationArgs);

						Object arguments[] = new Object[] {guid};
						Persistible pers = (Persistible)constructor.newInstance(arguments);
						// Make sure the instance persistible list has this registered
						// OncMergeBrowser
						//AbstractPersistible.putPersistible(pers);	
						AbstractPersistible.putPersistible(pers, this);
						//System.out.print(".");
						return pers;
				} catch (NoSuchMethodException e) {
					 	System.out.println
								("Warning: GUID Constructor does not exists for " 
								 + cls); 
						try { 
								Persistible pers = (Persistible)cls.newInstance();
								pers.setGUID(guid);
								// OncMergeBrowser
								//AbstractPersistible.putPersistible(pers);
								AbstractPersistible.putPersistible(pers, this);
								return pers;
						} catch (Exception ee) {
								System.out.println(ee);
								return null;
						}
				} catch (IllegalAccessException e) {
						System.out.println(e);
						return null;
				} catch (InvocationTargetException e) {
						System.out.println("COULDNT Creat new persistible " + cls 
															 + " with guid " + guid);
						e.printStackTrace();
						System.exit(0);
						return null;
				} catch (InstantiationException e) {
						System.out.println(e);
						return null;
				}
		}
    
    /**
     * Gets the DataSourceInfo attribute of the ProtegeDataSource object
     *
     * @return  The DataSourceInfo value
     */
    public DataSourceMap getDataSourceInfo() {
				if (dataSourceInfo == null) {
						dataSourceInfo = new DataSourceMap(kb);
				}
				return dataSourceInfo;
    }
    
    
    /**
     * Gets the ValidInstance attribute of the ProtegeDataSource object
     *
     * @param persistible Description of Parameter
     * @return            The ValidInstance value
     */
    private Instance getValidInstance(Persistible persistible) {
				GUID guid = persistible.getGUID();
				Instance instance = null;
				
				try {
						// If the object does not have a guid - create one
						if (guid == null) {
								guid = new GUID();
						}
						
						// Make sure guid is set
						persistible.setGUID(guid);
						instance = kb.getInstance(guid.toString());
						
						// If the instance cannot be found in the data source
						// Create one
						if (instance == null) {
								String className =
										persistible.getClass().getName();
								Vector dsObjects =
										getDataSourceInfo().mapForJavaClass(className);
								String protegeClassName =
										getDataSourceInfo().protegeClassForJavaClass(className);
	
								instance =
										kb.createInstance(guid.toString(),
																			kb.getCls(protegeClassName));
						}
						return instance;
				}
				catch (Exception e) {
						e.printStackTrace();
				}
				return null;
    }
    
    
    /**
     * Sets the Value attribute of the ProtegeDataSource object
     *
     * @param setter    The new Value value
     * @param attribute The new Value value
     * @param persObj   The new Value value
     * @param values    The new Value value
     */
    private void setValue(Method setter,
								  AttributeAccessMap attribute,
								  Object persObj,
								  Object values[]) {
		  try {
				// By default direct access to  public variables
				if (setter == null) {
					 Field theField =
						  persObj.getClass().getField(attribute.attributeName);
					 values[0] = toPersistibleValue(theField.getType(), values[0]);
					 theField.set(persObj, values[0]);
					 	if (values[0] != null && values[0] instanceof Persistible
											&& persObj != null && persObj instanceof SaveListener ) 
								((Persistible)values[0]).addSaveListener((SaveListener)persObj);
					 
				}
				else {
					 // Make sure the arg does not need translated
					 // to another type
					 Class [] paramTypes = setter.getParameterTypes();
					 // Compare with the types of the values
					 
					 if ( Array.getLength(values) == Array.getLength(paramTypes) ) {
						  for ( int i = 0; i < Array.getLength(values); i++ ) {
								// 	System.out.println("paramTypes[i] = " + paramTypes[i]
// 																		 + " values[i] = " + values[i]);
									values[i] = toPersistibleValue(paramTypes[i], values[i]);
									if (values[i] != null && values[i] instanceof Persistible
											&& persObj != null && persObj instanceof SaveListener ) 
											((Persistible)values[i]).addSaveListener((SaveListener)persObj);
						  }
					 }
					 
					 ReflectionHelper.invoke(setter, persObj, values);
				}
		  }
			catch (IllegalArgumentException iae) {
					System.out.println("Illegal argument for setter method " +
														 setter + " on " + persObj);
			}
			catch (NoSuchFieldException nsfe) {
					System.out.println("Field does not exist " + attribute.attributeName
														 + " in " + persObj.getClass());
			}
		  catch (Exception e) {
					System.out.println("Problem with setter method for " +
														 setter + " on " + persObj);
				e.printStackTrace();
		  }
    }

		/** Set persisitibleList values
				This sets values as a java collection
		 */
		private void setPersistiblePersistibleList(Collection values, 
																							 Class collectionCls, 
																							 Persistible persistible,
																							 AttributeAccessMap attribute) {
				Vector collectionValues = new Vector();
				if (values != null) {
						// Loop thru collection and build a collection of persistible
						Iterator i = values.iterator();
						// Fill these as a collection instead of one by one
						Persistible pers = null;
						int cnt = 0;
						while (i.hasNext()) {
								Object o = i.next();
								pers = toPersistible(collectionCls,
																		 (Instance)o);
								if ( pers != null )
										collectionValues.addElement(pers);
						}
				}
				try {
						setValues(attribute.setMethod, 
											attribute, 
											persistible, 
											collectionValues);
				}
				catch (Exception e4) {
						e4.printStackTrace();
				}
		}

		private void setPersistibleCollection(Collection values, 
																				 Class collectionCls, 
																				 Persistible persistible,
																				 AttributeAccessMap attribute) {
				Object[] args = new Object[1];
				if (values != null) {
						// Loop thru collection and process one by one
						Iterator i = values.iterator();
						// Now fill the primitive single slots
						Persistible pers = null;
						while (i.hasNext()) {
								Object o = i.next();
								pers = toPersistible(collectionCls,
																		 (Instance)o);
								if ( pers != null ) {
										args[0] = pers;
										try {
												if ( persistible == null ) {
														System.out.println("setPersistibleCollection " +
																							 args);
												}
														
												setValue(attribute.setMethod, attribute, 
																 persistible, args);
										}
										catch (Exception e4) {
												e4.printStackTrace();
										}
								}
						}
				}
		}

		private void setPersistibleItem(Object[] args, 
																		Class cls, 
																		Persistible persistible,
																		AttributeAccessMap attribute) {
				if ( args[0] != null) {
						// Convert the value to persistible
						Persistible newPersistibleValue =
								(Persistible)toPersistible(cls,
																					 (Instance)args[0]);
						if ( newPersistibleValue == null )
								return;
						args[0] = newPersistibleValue;
				}
				try {
						setValue(attribute.setMethod, attribute, persistible, args);
				}
				catch (Exception e2) {
						e2.printStackTrace();
				}
		}

		private void setPrimitiveCollection(Collection values, 
																				Class collectionCls, 
																				Persistible persistible,
																				AttributeAccessMap attribute) {
				Object[] args = new Object[1];
				if (values != null) {
						// Loop thru collection and process
						Iterator i = values.iterator();
						while (i.hasNext()) {
								Object o = i.next();
								args[0] = o;
								try {
										setValue(attribute.setMethod, 
														 attribute, persistible, args);
								}
								catch (Exception e4) {
										e4.printStackTrace();
								}
						}
				}
		}

		private void setPrimitivePersistibleList(Collection values, 
																				Class collectionCls, 
																				Persistible persistible,
																				AttributeAccessMap attribute) {
				Vector collectionValues = new Vector();
				if (values != null) {
						// Loop thru collection and build a collection of persistible
						Iterator i = values.iterator();
						// Fill these as a collection instead of one by one
						Persistible pers = null;
						while (i.hasNext()) {
								Object o = i.next();
								collectionValues.addElement(o);
						}
				}
				try {
						setValues(attribute.setMethod, 
											attribute, 
											persistible, 
											collectionValues);
				}
				catch (Exception e4) {
						e4.printStackTrace();
				}
		}
		

		/** Set 'collection' ( vector, hashtable, list, etc values
				This sets values one by one
		*/

		
    /**
     * Sets the Value attribute of the ProtegeDataSource object
     *
     * @param setter    The new Value value
     * @param attribute The new Value value
     * @param persObj   The new Value value
     * @param values    The new Value value
     */
    private void setValues(Method setter,
								  AttributeAccessMap attribute,
								  Object persObj,
								  Collection values) {
				Object args[] = new Object[1];
				try {
						// By default direct access to  public variables
						if (setter == null) {
							Field theField =
									persObj.getClass().getField(attribute.attributeName);
							theField.set(persObj, values);
							
						}
						else {
								// Make sure the arg does not need translated
								// to another type
								Class [] paramTypes = setter.getParameterTypes();
								args[0] = values;
								ReflectionHelper.invoke(setter, persObj, args);
						}
						// add save listeners to linked persistibles 
						if (values != null && persObj != null 
							&& persObj instanceof SaveListener ) {
								Iterator i = values.iterator();
								while ( i.hasNext() ) {
										Object value  = i.next();
										if ( value instanceof Persistible )
												((Persistible)value).addSaveListener
														((SaveListener)persObj);
								}
						}
				}
				catch (Exception e) {
						e.printStackTrace();
				}
    }
		public void setValueInPersistible(Persistible persistible, 
																			AttributeAccessMap attribute,
																			Object value) {
				Method setter = attribute.setMethod;
				// By default direct access to  public variables
				if (setter == null) {
						try { 
								persistible.getClass().
										getField(attribute.attributeName).
										set(persistible, value);
						}
						catch ( NoSuchFieldException nsfe) {
								System.out.println
										("Error occured while direct access setting " 
										 + attribute.attributeName + " in "
										 + persistible.getClass().getName()
										 + " check access it must be public"); 
						}
						catch (IllegalAccessException  iae) {
								System.out.println
										("Error occured while direct access setting " 
										 + attribute.attributeName + " in "
										 + persistible.getClass().getName()
										 + " check access it must be public"); 
						}
				}
				else {
						if ( value instanceof Iterator ) {
								// Convert to collection 
								Vector collection = new Vector();
								while ( ((Iterator)value).hasNext() ) {
										collection.addElement(((Iterator)value).next());
								}
								Object [] args = {collection};
								ReflectionHelper.invoke(setter, persistible,
																				args);
						}
						else {
								Object [] args = {value};
								ReflectionHelper.invoke(setter, persistible,
																				args);
						}
						
				}
		}

		public Object getValueFromPersistible(Persistible persistible, 
																					 AttributeAccessMap attribute) {
				Method getter = attribute.getMethod;
				Object value = null;
				// By default direct access to  public variables
				if (getter == null) {
						try { 
								value =
										persistible.getClass().
										getField(attribute.attributeName).
										get(persistible);
						}
						catch ( NoSuchFieldException nsfe) {
								System.out.println("Error occured while accessing " 
																	 + attribute.attributeName + " in "
																	 + persistible.getClass().getName()
																	 + " check access it must be public"); 
						}
						catch (IllegalAccessException  iae) {
								System.out.println("Error occured while accessing " 
																	 + attribute.attributeName + " in "
																	 + persistible.getClass().getName()
																	 + " check access it must be public"); 
						}
				}
				else {
						
						value = ReflectionHelper.invoke(getter, persistible,
																						null);
				}
				return value;
		}

		private boolean isOfType(String theType, Class cls) {
				if (theType != null && cls != null) {
						try {
								return ReflectionHelper.classForName(theType).isAssignableFrom(cls);
						}
						catch (Exception ex) {
								ex.printStackTrace();		
						}
				}
				return false;
		}
    /**
     * Gets the KnowledgeBase attribute of the ProtegeDataSource class
     *
     * @return  The KnowledgeBase value
     */
    public KnowledgeBase getKnowledgeBase() {
				return kb;
    }
    
    
    /**
     * Gets the Project attribute of the ProtegeDataSource class
     *
     * @return  The Project value
     */
    public Project getProject() {
	return project;
    }
    

    /**
     * Initialization
     *
     * @param name The new Project value
     */
    public void setProject(String name) {
				Collection protegeErrors = new ArrayList();
				
				if (name == null) {
						project = new Project(projectName,
																	protegeErrors);
				}
				else {
						project = new Project(name,
																	protegeErrors);
				}
				// If the load is unsuccessful bail 
				if ( protegeErrors.size() > 0 ) {
						System.out.println(protegeErrors);
						System.exit(0);
				}
				kb = project.getKnowledgeBase();
    }
    

    private void setProtegeValue( Instance instance, 
											 Slot aSlot, 
											 Object value) {
		  // This is a non user defined type just put it in			
		  // Cnnvert classes into their name string 
		  try {

					instance.setOwnSlotValue(aSlot, toSavableValue(value));
		  }
		  catch ( Exception pe) {
				System.out.println("Error setting protege value: line 1990 PDS "
													 + instance
													 + " slot " + aSlot
													 + " value " + toSavableValue(value));

		  }
	 }

    private void setProtegeValues( Instance instance, 
											 Slot aSlot, 
											 Collection values) {
				try {
						// 	System.out.println("setProtegeValues " +
						// 															 aSlot +
						// 															 " " + values);
						instance.setOwnSlotValues(aSlot, values);
				}
				catch ( Exception pe) {
						//System.out.println("Error setting protege value");
						pe.printStackTrace();
				}
		}
		
		private Object toSavableValue(Object originalValue) {
			
				if ( originalValue == null) {
						return originalValue;
				}
				else if ( originalValue.getClass() == java.lang.Class.class
									|| originalValue.getClass() == java.lang.Double.class ) {
						String newValue = 
								originalValue.toString().replaceFirst("class ", "");
 						return newValue;
 				}
				else
						return originalValue;
		}

		private Object toPersistibleValue(Class paramType, 
																			Object originalValue) {
				try {
						if ( paramType.getName().equals("java.lang.Class") &&
								 originalValue != null) {
								return ReflectionHelper.classForName((String)originalValue);
						}
						else if ( (paramType.getName().equals("java.lang.Double") 
											 || paramType.getName().equals("double")) &&
											originalValue != null) {
								return Double.valueOf((String)originalValue);
						}
						else
								return originalValue;
				}
				catch (Exception cnfe) {
						System.out.println("Error class not found " + originalValue 
															 + " while toPersistibleValue. ");
				}
				return originalValue;
		}


		public boolean hasChanged() {
				if ( project != null )
						return project.isDirty();
				return false; //if there is no project there cannot be any changes
				
		}

		public Hashtable getInstanceTree(Class startJavaClass, 
																		 Collection endJavaClasses) {
				// Convert classes to java names
				String startClassName = 
						getDataSourceInfo().protegeClassForJavaClass(startJavaClass.getName());
				Iterator i = endJavaClasses.iterator();
				String endClassName = null;
				Vector endClassNames = new Vector();
				while ( i.hasNext() ) {
						endClassName = 
								getDataSourceInfo().protegeClassForJavaClass(((Class)i.next()).getName());
						endClassNames.addElement(endClassName);
				}
				return getInstanceTree(startClassName, 
												endClassNames,
												null);
		}

		public Hashtable getInstanceTree(String startClassName, 
																		 Collection endClassNames) {
				return getInstanceTree(startClassName, 
												endClassNames,
												null);
		}

		// Return a hashtable of guids
		public Hashtable getInstanceTree(String startClassName, 
																		 Collection endClassNames,
																		 Persistible startingObject) {
				instanceTreeHashtable = new Hashtable();
				// Find the named class
				Cls aClass =
						kb.getCls(startClassName);
				//Get all instances of the the named class
				Vector theInstances = null;
				if ( startingObject == null ) 
						 theInstances = new Vector(aClass.getInstances());
				else {
						// Get instance for the startingObject Persistible
						Instance startingInstance =
								kb.getInstance(startingObject.getGUID().toString());
						theInstances = CollectionHelper.makeVector(startingInstance);
				}
				
				// Add all the instances of the start class to tree hashtable
				addToHashtable(theInstances, instanceTreeHashtable);

				// For each path end class 
				Cls endClass = null;
				String endClassName = null;
				Iterator endClassIter = endClassNames.iterator();
				while ( endClassIter.hasNext() ) {
						Object obj = endClassIter.next();
						if ( obj instanceof Class) {
								endClassName = 
										getDataSourceInfo().protegeClassForJavaClass
										(((Class)obj).getName());
						}
						else 
								endClassName = (String)obj;
						endClass = 	kb.getCls(endClassName);
						// Find a path ( steps to take through framed & slots) between the
						// two class types ex. between Knowledge Nugget and StatementBundle
						ProtegePath thePath =
								new ProtegePath(startClassName, endClassName, kb);
					// 	System.out.println("Found a path for (1) " + thePath + " via " + 
//  				 													 thePath.getClassPath());
						boolean isRecursivePath = false;					
						Vector theValues = buildTree(thePath, new Vector(theInstances), 
																				 0, false);
				} 
				searchPathId = null;
				return instanceTreeHashtable;
		}

		private void addToHashtable(Vector items,  Hashtable hashtable) {
			
				// Add all the instances of the start class to tree hashtable
				Iterator i = items.iterator();
				while ( i.hasNext() ) {
						Instance theInstance = (Instance)i.next();
						GUID guid = GUID.fromString(theInstance.getName());
						if ( guid == null ) {
								System.out.println(" Fixing GUID on instance" + theInstance);
								guid = new GUID();
								theInstance.setName(guid.toString());
						}
						if ( guid != null ) {
								Persistible pers = toPersistible(theInstance);
								if ( pers != null ) {
										addParent(pers, ROOT);
								}
						}
				}
		}
		private void removeExistingParents(Object child) {
				if ( child != null )
						instanceTreeHashtable.put(child, new Vector());
		}

		// Return a hashtable of guids
		public Hashtable getParentTree(String startClassName, 
																	 Collection endClassNames,
																	 Vector listOfChildren,
																	 int allRootNone) {

				//System.out.println("end class names " + new Vector(endClassNames));
				instanceTreeHashtable = new Hashtable();
				
				// Find the named class
				String javaClassString = 
						getDataSourceInfo().javaClassForProtegeClass(startClassName);
				Class javaClass = ReflectionHelper.classForName(javaClassString);
				Cls protegeClass = 	kb.getCls(startClassName);
// 				System.out.println("listOfChildren " + listOfChildren);
				// Convert list of children into a list of instances
				Iterator kids = listOfChildren.iterator();
				Instance inst = null;
				Vector kidInstances = new Vector();
				while (kids.hasNext() ) {
						inst = kb.getInstance(((Persistible)kids.next()).getGUID().toString());
						if ( inst != null )
								kidInstances.addElement(inst);
				}
				addToHashtable(kidInstances, instanceTreeHashtable);

				// Build a tree with 
				// For each path end class 
				Cls endClass = null;
				String endClassName = null;
				Iterator endClassIter = endClassNames.iterator();
				while ( endClassIter.hasNext() ) {
						endClassName = (String)endClassIter.next();
						endClass = 	kb.getCls(endClassName);
						ProtegePath thePath;
						if ( endClassName.equals("Keyword") && 
								 startClassName.equals("Keyword") ) {
								// Create a fixed path
								thePath =	new ProtegePath();
								thePath.addClass(endClass);
								thePath.addSlot(kb.getSlot("parentKeywords"));
						}
						else {
								thePath = new ProtegePath(startClassName, endClassName, kb);
								//thePathTree = thePath.getPathTree();
						}
						//Vector theValues = buildTree(thePathTree, kidInstances, 0, true);
						Vector theValues = buildTree(thePath, kidInstances, 0, true);

				} 
				//System.out.println("instanceTreeHashtable " + instanceTreeHashtable);
				// add the node itself to the instanceTreeHashtable
				searchPathId = null;

				return instanceTreeHashtable;
		}
		// Return a hashtable of guids
		public Vector getInstances(String startClassName, 
															 OncFilter filter,
															 String pathId) {
				searchPathId = pathId;
				return getInstances(startClassName, 
										 filter);
		}
		// Return a hashtable of guids
		public Vector getInstances(String startClassName, 
															 OncFilter filter) {
				// remove the filtered item from the tree 
				// convert the hashtable to a vector
				resultValues = new Vector();
				Hashtable h =  getInstanceTree(startClassName, 
																			 new Vector(),
																			 filter,
																			 TreeDisplayModePanel.ROOT);
				searchPathId = null;
				return getOnly(h, startClassName);
		}

		public Vector getOnly(Hashtable h, String classType) {
				Persistible obj = null;
				Cls cls = kb.getCls(classType);
				Cls instanceCls = null;

				Vector only = new Vector();
				if ( cls == null) {
						System.out.println("Error: Protege does not recognize " + classType
															 + " as a class. Returning empty result set ");
						return new Vector();
				}
				for (Enumeration e = h.keys(); 
						 e.hasMoreElements(); ) {
            obj = (Persistible)e.nextElement();
						instanceCls = 
								kb.getInstance(obj.getGUID().toString()).getDirectType();
						if ( instanceCls == cls || instanceCls.hasSuperclass(cls) ) 
								only.addElement(obj);
				}
				return only;
		}
		// Return a hashtable of guids
		public Hashtable getInstanceTree(String startClassName, 
																		 Collection endClassNames,
																		 OncFilter filter,
																		 int allRootNone,
																		 String pathId) {
				searchPathId = pathId;
				resultValues = new Vector();
				return getInstanceTree(startClassName, 
												endClassNames,
												filter,
												allRootNone);
		}
		// Return a hashtable of guids
		public Hashtable getInstanceTree(String startClassName, 
																		 Collection endClassNames,
																		 OncFilter filter,
																		 int allRootNone) {
				// System.out.println("starrtclass names " + startClassName);
// 				System.out.println("end class names " + new Vector(endClassNames));
				resultValues = new Vector();
				instanceTreeHashtable = new Hashtable();
				
				// Find the named class
				String javaClassString = 
						getDataSourceInfo().javaClassForProtegeClass(startClassName);
				Class javaClass = ReflectionHelper.classForName(javaClassString);
				Cls protegeClass = 	kb.getCls(startClassName);
				Vector filterResult  = null;
				
				if ( filter != null && allRootNone == TreeDisplayModePanel.ROOT 
						 && !filter.isEmpty() ) {
						//System.out.println("getInstanceTree in PDS " + filter);
						filterResult = new Vector(getResult(protegeClass, filter));
						// Empty anything that has collected while building the treetable
						// for the filter
						instanceTreeHashtable.clear();
				}
				else {
						//System.out.println("getInstanceTree in PDS NO FILTER " + filter);

						filterResult = new Vector(find(startClassName));
				}
				addToHashtable(filterResult, instanceTreeHashtable);

				// Build a tree
				// For each path end class 
				Cls endClass = null;
				String endClassName = null;
				Iterator endClassIter = endClassNames.iterator();
				while ( endClassIter.hasNext() ) {
						endClassName = (String)endClassIter.next();
						endClass = 	kb.getCls(endClassName);
						// Find a path ( steps to take through framed & slots) between the
						// two class types ex. between Knowledge Nugget and StatementBundle
						// 	ProtegePath thePath =  06.15
						// 								new ProtegePath(protegeClass.getName(), inst, 
						// 																kb);
						// 						ProtegeClassPath treePath = thePath.getTreePath();
						ProtegePath thePath =
								new ProtegePath(startClassName, endClassName, kb);
						if ( endClassName.equals("Keyword") && 
								 startClassName.equals("Keyword") ) {
								//System.out.println("KEYWORD SEARCH");
								// keywords is a special case because it recurses on itsself
								// therefore the path will generally go one step and stop
								// which means the keyword tree would be max 3 levels 
								// so need to dod something special here 
								// ( may have to do something special for SB sts too
								//getKeywordsForFilter(thePath, filterResult);
								Vector theValues = buildTree(thePath, filterResult, 0, true);
						}
						else if ( (endClassName.equals("StatementTemplate") ) 
											|| (endClassName.equals("StatementBundle") ) ) {
								Vector theValues = buildTree(thePath, filterResult, 0, true);
						}
						else {
								Vector theValues = buildTree(thePath, filterResult, 
																						 0, false);
								//System.out.println("hashtable " + instanceTreeHashtable);

						}
				} 
				//System.out.println("instanceTreeHashtable " + instanceTreeHashtable);
				// add the node itself to the instanceTreeHashtable
				searchPathId = null;
				return instanceTreeHashtable;
		}

		private void 	getKeywordsForFilter(ProtegePath thePath, 
																			 Vector filterResult) {
				// Get all the keywords
				buildTree(thePath, filterResult, 0, true);
				//System.out.println("hashtable " + instanceTreeHashtable);
				// Prune the hashtable to remove all keywords that 
				// were connected to root
				// and not a part of the keyword filter 
				pruneFromRoot(filterResult);
				
		}
		private void pruneFromRoot(Vector filterResult) {
				for (Enumeration e = instanceTreeHashtable.keys(); 
						 e.hasMoreElements(); ) {
            Object child = e.nextElement();
						Vector parentList =
								(Vector)instanceTreeHashtable.get(child);
						if ( parentList.contains(ROOT) && 
								 !filterResult.contains(child)){
								parentList.remove(ROOT);

								//if (parentList.empty())
								//		parents.removeElement(index);
						}
				}
		}

		public Collection getResult(Cls protegeClass, OncFilter filter) {
				// 	System.out.println("Getting results " + protegeClass.getName() 
				// 													 + " with Filter " + filter); 
				Collection resultSet = null;
				resultSet = getResult(protegeClass, filter.getRootNode());
				return resultSet;
		}


	private Collection getResult(Cls protegeClass, OncTreeNode node, 
																			Collection resultSet)
	{
		Object userObject = node.getUserObject();
		if(userObject instanceof TcapLogicalOperator)
			return(getResult(protegeClass, (TcapLogicalOperator)userObject, 
											 node.getChildren(), resultSet));
		//else if(userObject instanceof SubsetClass)
		//	return(((SubsetClass) userObject).instanceOf(candidate));
		else if(userObject instanceof Persistible) {
				if ( node.getChildCount() > 0 ) {
						return(getResult(protegeClass, (Persistible)userObject, 
														 node.getChildren(), resultSet));
				}
				else 
						return(getResult(protegeClass, (Persistible)userObject));
		}
		else if (userObject instanceof oncotcap.datalayer.persistible.OntologyObjectName) {
				// get all instances of protegeClass that are attached to 
				// at least one instance of userObject class type
				return(getResult(protegeClass, userObject.toString()));
		}	
		else if (userObject instanceof oncotcap.datalayer.SearchText) {
				// get all instances of protegeClass that are attached to 
				// at least one instance of userObject class type
				return(getResult(protegeClass, (SearchText)userObject));
		}	
		else
			return resultSet;
	}

	private Collection getResult(Cls cls, 
																				 Persistible persistible,
																				 Iterator children,
																				 Collection resultSet){
// 			System.out.println("getResult persistible node " + cls 
// 												 + " " + persistible + " " + children + " " + resultSet);

			if(! children.hasNext())
					return resultSet;
			
			while(children.hasNext())
					{
							Object child = children.next();
							if ( child instanceof OncTreeNode ) {
									Collection currentSet = getResult(cls, (OncTreeNode)child, resultSet);
									if( currentSet.size() > 0 ) {
											// with the current collection
											resultSet = CollectionHelper.and(resultSet, currentSet);
									}
									else // current set is empty so the and must be empty
											return new Vector();
							}
					}
			return(resultSet);
	}

	private Collection getResult(Cls cls, 
															 TcapLogicalOperator op, 
															 Iterator children,
															 Collection resultSet)
	{
// 			System.out.println("getResult operator node " + cls 
// 												 + " " + op + " " + children + " " + resultSet);
		if(op.equals(TcapLogicalOperator.AND))
		{
			if(! children.hasNext())
					return resultSet;
			
			while(children.hasNext()) {
					Object child = children.next();
					if ( child instanceof OncTreeNode ) {	
// 							System.out.println("AND just starting resultset " + resultSet);
							Collection currentSet = getResult(cls, (OncTreeNode)child, resultSet);
// 							System.out.println("AND just starting resultset " + currentSet);
							if( currentSet.size() > 0 ) {
									// with the current collection
									if ( resultSet.size() <= 0)
											resultSet = currentSet;
									else {
// 											System.out.println("AND before resultset " + resultSet);
											resultSet = CollectionHelper.and(resultSet, currentSet);
// 											System.out.println("AND after resultset " + resultSet);
									}

							}
							else // current set is empty so the and must be empty
									return new Vector();
					}
			}
// 			System.out.println("AND resultset " + resultSet);
			return(resultSet);
		}//end if AND
		else if(op.equals(TcapLogicalOperator.OR))
		{
			while(children.hasNext())
			{
					Object child = children.next();
					if ( child instanceof OncTreeNode ) {	
							Collection currentSet = getResult(cls, (OncTreeNode)child, resultSet);
							if( currentSet.size() > 0 ) {
									// with the current collection
									resultSet = CollectionHelper.or(resultSet, currentSet);
							}
					}
			}
		} //end if OR
		else if(op.equals(TcapLogicalOperator.NOT))
		{
			if(children.hasNext())
			{
					Object child = children.next();
					if ( child instanceof OncTreeNode ) {	
							Collection currentSet = getResult(cls, (OncTreeNode)child, resultSet);
							Collection all = kb.getInstances(cls);
							resultSet = CollectionHelper.not(all, currentSet);
					}
			}
		} //end if NOT
		return resultSet;
	}

	public Collection getResult(Cls protegeClass, OncTreeNode filterNode)
	{
		if (filterNode.getChildCount() < 1) {
				return new Vector();
		}
		else
		{
			Iterator it = filterNode.getChildren();
			Object node = it.next();
			if(node instanceof OncTreeNode)
			{
				return(getResult(protegeClass, (OncTreeNode)node, new Vector()));
			}
			else
				return  new Vector();
		}
	}


		private Collection getResult(Cls protegeClass, Persistible persistible) {
				//	oncotcap.util.ForceStackTrace.showStackTrace();
				// 	System.out.println("getResult individual persistible node " 
				// +  protegeClass
				// 	+ " " + persistible + " " + persistible.getClass() );
				// Find all instances of protegeClass that are 
				// directly/indirectly related to inst
				// Convert persistible to instance
				Instance inst = kb.getInstance(persistible.getGUID().toString());
				if ( inst == null ) {
						// Trying to get a guid that does not exist in the KB
						System.out.println("ERROR: " + persistible.getGUID()
															 + " does not exist in KB. Protege Class: "
															 + protegeClass);
						return new Vector();
				}
				// If the persistible is the same class or a subclass of the protegeClass
				// then just return the instance
				if ( inst.getDirectType().hasSuperclass(protegeClass) ) 
						return CollectionHelper.makeVector(inst);
				ProtegePath thePath = null;
				ProtegeClassPath treePath = null;
				if ( searchPathId != null ){
						thePath = 
								new ProtegePath(protegeClass.getName(), inst, 
																kb, searchPathId);
						treePath = thePath.getTreePath();
				}
				else {
						thePath = 
								new ProtegePath(protegeClass.getName(), inst, 
																kb);
						treePath = thePath.getTreePath();

				}
				Vector selectedInstances = CollectionHelper.makeVector(inst);
				// 			 	System.out.println("Found a path for (getResult)" 
				// + thePath + " via " + 
				//  													 thePath.getSlotPath() 
				// 													 + "selected instances " 
				// + selectedInstances);
				Collection resultSet = null;
				if ( treePath != null ) {
					 
						// Not using an autogenerated path 
						Collection testResultSet = findValues(treePath, selectedInstances);
						resultSet = resultValues;
		// 				System.out.println("Defined Path " + treePath +
// 															 " resultSet " + resultSet
// 															 + " testResultSet " + testResultSet);
				}
				else {
// 						System.out.println("FindValues " + treePath +
// 															 " thePath " + thePath);
						resultSet = findValues(thePath, selectedInstances, 0);
				}
				//  				System.out.println("resultSet " + resultSet);
				// ADD THE ITEM ITSELF if it is the same as the root class ???
				resultSet.add(inst); 
				return resultSet;
		}

		private Collection getResult(Cls protegeClass, String protegeClassName) {
				// Get all instances of protege class that are connected to at least
				// on instance of java class
				// Convert java class to protege class 
				Cls connectedToClass = kb.getCls(protegeClassName);
				Vector allInstances = new Vector(connectedToClass.getInstances());

				// If the persistible is the same class or a subclass of 
				// the protegeClass then just return the instance
				if ( connectedToClass.hasSuperclass(protegeClass) ) 
						return allInstances;
				ProtegePath thePath =
						new ProtegePath(connectedToClass.getName(), 
														protegeClass.getName(),kb);
				// Always use a defined path first If the path is not found see if there is a 
				// defined path in the kb
				Collection resultSet = null;
				if ( thePath.hasDefinedPath() ) {
	
						//System.out.println("Found a path for " + thePath.getTreePath());
						findValues(thePath.getTreePath(), allInstances);
						resultSet = resultValues;
				}
				else {
						resultSet = findValues(thePath, allInstances, 0);
				}
				// 	System.out.println("Found a path for " + thePath + " via " + 
				// thePath.getSlotPath() + "selected instances " + selectedInstances);
				// 				System.out.println("resultSet " + resultSet);
				// ADD THE ITEM ITSELF if it is the same as the root class ???
				//resultSet.add(inst); 
				return resultSet;
		}

		private Collection getResult(Cls protegeClass, SearchText text) {
				// Get all instances of protege class that have the 'text' in their 
				// browser text - depends on the project to have the proper field as
				// browser text
				Vector resultSet = new Vector();
				Collection allInstances = protegeClass.getInstances();
				Iterator i = allInstances.iterator();
				while ( i.hasNext() ) {
						Instance inst = (Instance)i.next();
					
						if ( (inst.getBrowserText() != null)
								 && (inst.getBrowserText().toLowerCase().indexOf(text.toString().toLowerCase()) > -1)  )

								resultSet.addElement(inst);
				}
				return resultSet;
		}

   /**
     * Description of the Method
     *
     * @param thePath      Description of Parameter
     * @param theInstances Description of Parameter
     * @param pathPosition Description of Parameter
     * @return             Description of the Returned Value
     */
    private Vector buildTree(ProtegePath thePath, Vector theInstances,
			      int pathPosition, boolean isRecursivePath) {
			// 	 	System.out.println("buildTree " + thePath +
// 				 										 " treePath " + thePath.getTreePath());
				if ( thePath.getTreePath() != null ) {
						Vector foundValues = 
								findValues(thePath.getTreePath(), theInstances);
						// 	System.out.println("Building tree using the TREEPATH " + instanceTreeHashtable);
						
						return foundValues;

				}
			// 	Vector foundValues = findValues(thePath.getTreePath(), theInstances);
// 				return foundValues;
// 		}
		
				// 	if ( isRecursivePath ) {
				// 		System.out.println("buildTree " + thePath 
				// 															 + " " + theInstances 
				// 															 + " " + pathPosition
				// 															 + " " + isRecursivePath);
				// 				}
				// If there is no path you won't be able to find any values
				if ( thePath.getSlotPath().isEmpty() ) {
						//System.out.println("Unable to find a path for " + thePath);
						return new Vector();
				}
				// System.out.println("Found a path for " + thePath + "via" + 
// 				 													 thePath.getClassPath());
				
				// Determine the current slot and classes being sought
				//System.out.println("slot path " + thePath.getSlotPath());
				
				Slot searchSlot = (Slot)(thePath.getSlotPath()).elementAt(pathPosition);
				Cls searchClass = (Cls)(thePath.getClassPath()).elementAt(pathPosition);
				
				// If there are instances set the still searching flag
				if (theInstances.size() > 0) {
						stillBuildingTree = true;
				}
				else {
						stillBuildingTree = false;
						return theInstances;
				}
				
				// For each instance find related instances of the appropriate
				// type using the searchSlot provided in the path
				Vector allRelatedInstances = new Vector();
				GUID parentGUID = null;
				for (int i = 0; i < theInstances.size(); i++) {
						Instance anInstance = (Instance)theInstances.elementAt(i);
						parentGUID = GUID.fromString(anInstance.getName());
						Vector relatedInstances =
								new Vector(anInstance.getOwnSlotValues(searchSlot));
						// Make sure they are of the appropriate type
						for (int j = 0; j < relatedInstances.size(); j++) {
								Instance aRelatedInstance =
										(Instance)relatedInstances.elementAt(j);
								
								if (aRelatedInstance.hasType(searchClass) == true) {
										allRelatedInstances.addElement(aRelatedInstance);
										// Put each of the retrieved instances in the tree 
										// hashtable with the parent guid
										parentGUID = GUID.fromString(anInstance.getName());
										GUID guid = GUID.fromString(aRelatedInstance.getName());
										if ( parentGUID != null  && guid != null ){
												Persistible parentPersistible = toPersistible(anInstance);
												Persistible persistible = toPersistible(aRelatedInstance);
												if ( persistible != null && parentPersistible != null ) {
														addParent(persistible,
																											parentPersistible);
												}
										}
								}
						}
						// for relatedInstances
				}
				//for theInstances
				// Is this the end of the path
// 				System.out.println(" recursive " + isRecursivePath);
				if ((thePath.getSlotPath()).size() == (pathPosition + 1)) {
					// 	System.out.println(" right before recursive check" 
// 															 + thePath + "via" + 
// 															 thePath.getSlotPath() + " classes "
// 															 + thePath.getClassPath());
						if ( isRecursivePath ) {
								// start at the beginning of the path and go again
								// Keep going you have not reached the end of the path
								// Determine what path to use - make separate method
								// What is the ending class 
								ProtegePath theRecursivePath = thePath;
								if ( thePath.getClassPath() != null 
										 && thePath.getClassPath().lastElement() 
										 == kb.getCls("StatementTemplate") ) {
// 										System.out.println("this is recuresive with st as end node ");
										theRecursivePath = 
												new ProtegePath("StatementTemplate", 
																				"StatementTemplate",
																				kb);
								}
						// 		System.out.println("Is recursive path start over " + thePath);
//  								System.out.println("Found a path for " + thePath + "via" + 
//  												 thePath.getSlotPath());
// 								System.out.println("allRelatedInstances " + allRelatedInstances);
								allRelatedInstances =
										buildTree(theRecursivePath, allRelatedInstances, 0, true);
								return allRelatedInstances;
						}
						else {
								// End of the path you have found the instances
								// you are looking for
								stillBuildingTree = false;
								return allRelatedInstances;
						}
				}
				else {
						// Keep going you have not reached the end of the path
						// 		System.out.println("WHAT IS THE PROBLEM " + 
						// 										 thePath + " -- " + allRelatedInstances
						// 										 + " -- " + isRecursivePath + " -- " 
						// 										 + pathPosition);
						allRelatedInstances =
								buildTree(thePath, allRelatedInstances, pathPosition + 1, 
													isRecursivePath);
						//System.out.println("allRelatedInstances " + allRelatedInstances);
						return allRelatedInstances;
				}
    }

 		private void addParent(Object child, Object parent) {
				if ( child != null && parent != null ) {
						// Allow a node to have more than one parent
						Vector parents  = 
								(Vector)instanceTreeHashtable.get(child);
						if ( parents == null ) 
								parents = new Vector();
						// If  the child had a parent that was the root remove it 
						// before adding non root parents
						if ( parents.contains(ROOT) ) // can't be root an leaf
								parents.remove(ROOT);
						if ( !parents.contains(parent) ) 
								parents.addElement(parent);
						if ( child instanceof oncotcap.datalayer.persistible.SubModelGroup ) {
								//System.out.println("addParent " + parents + " TO CHILD " + child);
								//06.23 oncotcap.util.ForceStackTrace.showStackTrace();
						}
						instanceTreeHashtable.put(child,
																			parents);
				}
		}

		public void removeAllSlotValues(Instance instance, Slot slot) {
				Collection values = instance.getOwnSlotValues(slot);
				Iterator i = values.iterator();
				while ( i.hasNext() ) {
						instance.removeOwnSlotValue(slot,i.next());
				}
		}
		public Collection getSubclasses(Class persistibleType) {
				String persistibleClassName = persistibleType.getName();
				String protegeClassName = 
						getDataSourceInfo().protegeClassForJavaClass(persistibleClassName);
				Cls protegeCls = kb.getCls(protegeClassName);
				Vector persistibleSubClasses = new Vector();
				try {
						Collection subClasses = protegeCls.getSubclasses();
						// Translate to persisitble classes
						Iterator i = subClasses.iterator();
						while ( i.hasNext() ) {
								protegeClassName = (String)((Cls)i.next()).getName();
								persistibleClassName = 
										getDataSourceInfo().javaClassForProtegeClass(protegeClassName);
								if (persistibleClassName != null) {
										Class persistibleClass =
												ReflectionHelper.classForName(persistibleClassName);
										if (persistibleClass != null)
												persistibleSubClasses.addElement(persistibleClass);
								}
						}
				}catch(Exception e) {
						System.out.println("Unable to create a subclass list for "
															 + persistibleType);
				}
				return persistibleSubClasses;
		}

		public Collection getSuperClasses(Class persistibleType) {
				return null; // not implemented 
		}

		public Object directlyLinkableVia(Class linkFrom, Class linkTo) {
			// Get the protege classes
				Cls protegeLinkTo =
						kb.getCls(getDataSourceInfo().protegeClassForJavaClass
											(linkTo.getName()));
				Cls protegeLinkFrom =
						kb.getCls(getDataSourceInfo().protegeClassForJavaClass
											(linkFrom.getName()));
				// 	System.out.println("Trying to find link from " + linkFrom
				// 													 + " to " + linkTo);

				// Get all the slots that the fromClass has
				Collection fromClassSlots = protegeLinkFrom.getTemplateSlots();
				
				// Find any instance slot and see if the toClass is an allowable type
				for (Iterator i = fromClassSlots.iterator(); i.hasNext(); ) {
						//Determine if the slot is a classInstance or Class Type
						Slot fromSlot = (Slot)i.next();
// 						System.out.println("fromSLot " + fromSlot);
						if (fromSlot.getValueType() != ValueType.INSTANCE) {
								continue;
						}
						
						// Get all the classes allowed to be connected to this class
						// via this slot
						// Collection allowedClasses = fromSlot.getAllowedClses();
						Collection allowedClasses = ProtegePath.getAllAllowedClasses(fromSlot);
// 						System.out.println("allowedClasses " + allowedClasses);
						if (allowedClasses.contains(protegeLinkTo)) {
// 								System.out.println("protegeLinkTo " + protegeLinkTo);

								// get the field or method that is used to access
								Vector attributeMapVector =
										getDataSourceInfo().mapForProtegeClass(protegeLinkFrom.getName());
								// Future make this a hashtable instead of vector
								// Now loop thru and grab the field or method
								String slotName = fromSlot.getName();
								Iterator ii = attributeMapVector.iterator();
								while ( ii.hasNext() ) {
										DataSourceObject obj = (DataSourceObject)ii.next();
										Vector attrMaps = obj.attributeAccessMaps;
										Iterator iii = attrMaps.iterator();
										while ( iii.hasNext() ) {
												AttributeAccessMap attrMap = 
														(AttributeAccessMap)iii.next();
												if ( slotName.equals(attrMap.dsAttributeName) ) {
// 														System.out.println(attrMap);

														// FUTURE - Need a better way at this but this 
														// will do for now - shady !!
														// Need setter and getter in case this is a 
														// list
														if ( attrMap.setMethod != null  
																 && attrMap.getMethod != null ) {
																Vector methods = new Vector();
																methods.addElement(attrMap.setMethod);
																methods.addElement(attrMap.getMethod);
																return methods;
														}
														else {
																try {
																		Field accessibleField =
																				linkFrom.getField(attrMap.attributeName);
																		// return the field
																		return accessibleField;
																} catch ( Exception ex) {
																		System.out.println("can't find the field " +
																											 attrMap.attributeName);	 
																}
														}
												}
										}
								}
						}
				}
				return null;
		}
		
		public Collection getDirectlyLinkable(Class linkFrom) {
				// Get the protege classes
				Cls protegeLinkFrom =
						kb.getCls(getDataSourceInfo().protegeClassForJavaClass
											(linkFrom.getName()));
	
				// Get all the slots that the fromClass has
				Collection fromClassSlots = protegeLinkFrom.getTemplateSlots();
				Vector completeList = new Vector();
				// Find any instance slot and see if the toClass is an allowable type
				Iterator i = fromClassSlots.iterator();
				while (i.hasNext() ) {
						//Determine if the slot is a classInstance or Class Type
						Slot fromSlot = (Slot)i.next();
// 						System.out.println("fromSLot " + fromSlot);
						if (fromSlot.getValueType() != ValueType.INSTANCE) {
								continue;
						}
						
						// Get all the classes allowed to be connected to this class
						// via this slot
						// Collection allowedClasses = fromSlot.getAllowedClses();
						Collection allowedClasses = ProtegePath.getAllAllowedClasses(fromSlot);
// 						System.out.println("allowedClasses " + allowedClasses);
						// Add the allowed classes to the complete list in java class form
						Iterator ai = allowedClasses.iterator();
						while (ai.hasNext()) {
								Cls cls = (Cls)ai.next();
								String javaClassName =
										getDataSourceInfo().javaClassForProtegeClass(cls.getName());
								if ( javaClassName != null ) {
										completeList.addElement
												(ReflectionHelper.classForName(javaClassName));
								}
						}
						return completeList;
				}
				return null;
		}

		public boolean isDirectlyLinkable(Class linkFrom, Class linkTo) {
				// Get the protege classes
				Cls protegeLinkTo =
						kb.getCls(getDataSourceInfo().protegeClassForJavaClass
											(linkFrom.getName()));
				Cls protegeLinkFrom =
						kb.getCls(getDataSourceInfo().protegeClassForJavaClass
											(linkTo.getName()));
				// Get all the slots that the fromClass has
				Collection fromClassSlots = protegeLinkFrom.getTemplateSlots();
				
				// Find any instance slot and see if the toClass is an allowable type
				for (Iterator i = fromClassSlots.iterator(); i.hasNext(); ) {
						//Determine if the slot is a classInstance or Class Type
						Slot fromSlot = (Slot)i.next();
						
						if (fromSlot.getValueType() != ValueType.INSTANCE) {
								continue;
						}
						
						// Get all the classes allowed to be connected to this class
						// via this slot
						// Collection allowedClasses = fromSlot.getAllowedClses();
						Collection allowedClasses = ProtegePath.getAllAllowedClasses(fromSlot);
						if (allowedClasses.contains(protegeLinkTo)) {
								return true;
						}
				}
				return false;
		}
		
		public boolean isConnectedUsingRelationship(Persistible persistible,
													Persistible persistible2,
													String attribute){
				return false;
		}

		public boolean isConnected(Persistible persistible,
															 Persistible persistible2){
				ProtegePath thePath = new ProtegePath(persistible,
																							persistible2,
																							kb);
				Instance theInstance = 
						kb.getInstance(persistible.getGUID().toString());
				Instance theInstance2 = 
						kb.getInstance(persistible2.getGUID().toString());
				Vector theValues = 
						findValues(thePath, 
											 CollectionHelper.makeVector(theInstance),
											 0);
				return theValues.contains(theInstance2);
		}

		public String toString() {
				return "Protege Project " + projectName;
		}


		
		
}

