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
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import java.lang.reflect.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import javax.swing.tree.TreeNode;

import oncotcap.Oncotcap;
import oncotcap.util.GUID;
import oncotcap.util.SystemUtil;
import oncotcap.util.ReflectionHelper;
import oncotcap.util.CollectionHelper;
import oncotcap.display.browser.TreeUserObject;
import oncotcap.display.common.OncPopupMenu;
import oncotcap.display.common.Popupable;
import oncotcap.display.editor.EditorFrame;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;

import oncotcap.datalayer.persistible.parameter.*;

/**
 * Superclass of any object that would like to be saved to the datasource
 * (protege, RDB,etc). Makes sure object has a GUID.
 *
 * @author   morris
 * @created  March 11, 2003
 */
public abstract class AbstractPersistible extends Timestampable 
		implements Persistible, TreeUserObject, SaveListener, Popupable {

	OncPopupMenu popup = null;
	private EditorFrame currentEditor;
	private Format formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	private Vector treeNodes = null;
	private Class setSource = null;
	private int state = Persistible.UNSET;
	private OncoTCapDataSource dataSource = null;

	/** */
	public GUID guid = null;
// 	public String creator = null;
//  	public String modifier = null;
//  	public String creationTime = null;
//  	public String modificationTime = null;
// 	public Integer versionNumber;

	public Color fgColor = Color.black;



	/** */
	public static Hashtable persistibles = new Hashtable();
	public static Hashtable dataSources = new Hashtable();

	/** Constructor for the Persistible object */
	public AbstractPersistible() {
			setGUID(new GUID());				
			initCreationTime();

			// Add it to the list of persistible objects to support
			// add by reference
	}

		/** Constructor used by the data source for instantiating objects 
			 from the DS versus the UI */
		public AbstractPersistible(GUID guid) {
				setGUID(guid);			
				initCreationTime();
		}

		public boolean shouldSave() {
				if ( getPersistibleState() == Persistible.DO_NOT_SAVE ) 
						return false;
				else
						return true;
		}
		public void initCreationTime() {
				Date date = new Date();
				if (getCreator() == null ) {
						// set creation info if not set yet
						setCreationTime(formatter.format(date));
						setCreator(SystemUtil.getSystemProperty("user.name"));
				}		
		}

	/**
	 * Gets the ClassName attribute of the Persistible object Not sure if this is
	 * in use
	 *
	 * @return  The ClassName value
	 */
	public String getClassName() {
		return oncotcap.util.StringHelper.className(getClass().toString());
	}


	/**
	 * Gets the Current attribute of the Persistible object
	 *
	 * @return  this
	 */
	public Object getCurrent() {
		return this;
	}


	/**
	 * Gets the GUID attribute of the Persistible object
	 *
	 * @return  The GUID value
	 */
	public GUID getGUID() {
		return this.guid;
	}


	/**
	 * Sets the GUID attribute of the Persistible object
	 *
	 * @param guid The new GUID value
	 */
	public void setGUID(GUID guid) {
		this.guid = guid;
		putPersistible(this);
	}

	/**
	 * Sets the GUID attribute of the Persistible object
	 *
	 * @param guid The new GUID value
	 */
		public void setGUID(GUID guid, OncoTCapDataSource ds) {
				this.guid = guid;
				putPersistible(this, ds);
		}
		
		public Class getSetSource() {
				return setSource;
		}
		public void setSetSource(Class cls) {
				setSource = cls;
		}

		public OncoTCapDataSource getDataSource() {
				if ( Oncotcap.getDataSourceMode() == false ) 
						return dataSource;
				return Oncotcap.getDataSource();
		}
		public void setDataSource(OncoTCapDataSource ds) {
				dataSource = ds;
				putPersistible(this, ds);
				
		}

	/**
	 * Sets the GUID attribute of the Persistible object from string
	 *
	 * @param guid The new GUID value
	 */
	public void setGUID(String guid) {
		setGUID(GUID.fromString(guid));
	}

	/** */
	public void update() {
		
			// If this is a new instance being instantiated from the user side
			// or a fully persisted persistible update it - do not update
			// instances where the persistible creation is being driven by a 
			// request from the knowledge base 
			if ( (getPersistibleState() > Persistible.UNSET) || 
					 (getPersistibleState() == Persistible.UNSET 
						&& getSetSource() == null) ) {
				// 	if ( this instanceof oncotcap.datalayer.persistible.action.OncAction ) {
// 							oncotcap.util.ForceStackTrace.showStackTrace();
// 					}
					if ( getDataSource() != null 
							 && getDataSource().isModified(this) ) {
							//System.out.println("IS MODIFIED " );
							initCreationTime();
					    updateModification();
							getDataSource().update(this);
							putPersistible(this);
					}
			}
			else if (getPersistibleState() == Persistible.DELETED) {
					// Make sure it is deleted in protege and 
					// removed from the persistible table

			}
			// If this is being called during a save then don't tell everyone
			// If you tell they will "shudder" when they hear  the news
			// really wish this was not called protege save action 
			if (oncotcap.display.browser.OncBrowser.notifyUIOfPersistibleChanges()) 
					fireSaveEvent();
	}
			
	private void updateModification() {
			Date date = new Date();
			setModificationTime(formatter.format(date));
			setModifier(SystemUtil.getSystemProperty("user.name"));
			setVersionNumber(new Integer(getDataSource().getProjectVersion()+1));
	}
	public void delete() {
			setPersistibleState(Persistible.DELETED);
			removePersistible(this);
			getDataSource().delete(this);
			fireDeleteEvent();
	}
	/**
	 * Over-rides Object.equals() compares the object's guid for equalness.
	 *
	 * @param obj    Description of Parameter
	 * @return       A boolean. False if the compared object isn't a Persistible or
	 *               if the guid of the compared object isn't the same as the
	 *               current. True if the compared object has the same guid.
	 * @author       shirey
	 * @version      initial 04/07/2003
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Persistible) {
			return (guid.equals(((Persistible)obj).getGUID()));
		}
		else {
			return (false);
		}
	}

//     /**
//      *  Sets the GUID attribute of the Persistible object
//      *
//      * @param  guid The new GUID value
//      */
//     public void setGUID(GUID guid) {
// 	this.guid = guid;
//     }

	/**
	 * Over-rides Object.hashCode()
	 *
	 * @return   An integer hashCode derived from the object's GUID.
	 * @author   shirey
	 * @version  initial 04/07/2003
	 */
	public int hashCode() {
		return (guid.hashCode());
	}


	/**
	 * Not in use
	 *
	 * @return
	 */
	public Object toDataSourceFormat() {

		return null;
	}


	public String getPrettyName() {
		return this.getClassName();
	}

	public String toDisplayString() {
		return toString();
	}

	public ImageIcon getIcon() {
		return null;
	}

// 	public ImageIcon setIcon() {
// 		return ;
// 	}
 
	public boolean showText() {
				return true;
	}


	public EditorFrame getCurrentEditor()
	{
			return(currentEditor);
	}
	public void setCurrentEditor(EditorFrame editor)
	{
		currentEditor = editor;
	}
	/**
	 * Gets the Persistible attribute of the Persistible class
	 *
	 * @param guid Description of Parameter
	 * @return      The Persistible value
	 */
	public static Persistible getPersistible(GUID guid) {
			Persistible persistible = (Persistible)persistibles.get(guid);
			// 	if ( persistible  == null ){
			// 					// Try to retrieve it from the knowledge base
			// 					persistible = 
			// 							DataSourceStatus.getDataSource().find((Persistible)null , guid) ;
			// 			}

			return persistible;
	}
		public static void addDataSource(OncoTCapDataSource ds) {
				//System.out.println("addDataSources " + ds);
				dataSources.put(ds, new Hashtable());
		}
	public static Persistible getPersistible(GUID guid, OncoTCapDataSource ds) {
			Persistible persistible = null;
			if ( ds != null && Oncotcap.getDataSourceMode() == false ) {
					// This is an app that has more than 
					// one data source open get the appropriate hashtable
					// with the persistible map in it

					Hashtable p = (Hashtable)dataSources.get(ds);
					if ( p == null ) {
					// 		System.out.println("For ds " + ds + 
// 																 " there is no hashtable " + 
// 																 dataSources.keySet());
							persistibles.get(guid);
					}
					if ( p != null ) {
							persistible = (Persistible)p.get(guid);
 							// System.out.println("its here " + dataSources.keySet() + "  hashtable " + p.keySet()); 

//  							System.out.println("getting out " + guid + " pers " + persistible
//  																 + " ds " + ds); 
						// 	if (persistible == null) 
// 									System.out.println("P == > " + p);
					}
					else {
							//System.out.println("where did it go " + dataSources.keySet()); 
					}
			}
			else 
					 persistible = (Persistible)persistibles.get(guid);
			
			return persistible;
	}

	/**
	 * @param persistible Description of Parameter
	 */
	public static void putPersistible(Persistible persistible) {
			// System.out.println("Putting persistible no ARG");
// 			oncotcap.util.ForceStackTrace.showStackTrace();
			persistibles.put(persistible.getGUID(), persistible);
	}

	/**
	 * @param persistible Description of Parameter
	 */
	public static void putPersistible(Persistible persistible, 
																		OncoTCapDataSource ds) {
	 		//System.out.println("DS " + " -- " + ds + " guid " 
			//+ persistible.getGUID());
			if ( persistible == null || persistible.getGUID() == null )
					return;
			if ( ds != null ) {
					// This is an app that has more than 
					// one data source open get the appropriate hashtable
					// with the persistible map in it

					Hashtable p = (Hashtable)dataSources.get(ds);
					if ( p == null ) {
							p = new Hashtable();
							dataSources.put(ds, p);
					}
				// 	System.out.println("putting " + persistible.getGUID() 
//   														 + " -- " + ds);
					//oncotcap.util.ForceStackTrace.showStackTrace();
					p.put(persistible.getGUID(), persistible);
			}
			persistibles.put(persistible.getGUID(), persistible);


	}

	/**
	 * @param persistible Description of Parameter
	 */
	public static void removePersistible(Persistible persistible) {
			
 		persistibles.remove(persistible.getGUID());
	}

		// /** Return the field that contains the value of the specified persistible type
// 				-- somewhat similar to getting allowed slot types in protege
// 				-- this could be abstract
// 		 */
// 		public Method getMethodFor(Persistible persistible) {
// 				return null;
// 		}

// 		public Collection getConnectedNodes() {
// 				return null;
// 		}
		public void setPersistibleAttribute(String attributeName, 
																				Object parameter) {
				Class cls = this.getClass();
				try {
						Field attribute = null;
// 						System.out.println("Setting field " + attributeName 
// 															 + " for " + cls + " to " + parameter );
// 						System.out.println("What is this class " + cls);
						try {
								attribute	= cls.getDeclaredField(attributeName);
						} catch ( NoSuchFieldException nsfe) {
								attribute =cls.getField(attributeName);
						}
						attribute.setAccessible(true);
						attribute.set(this, parameter);
						update();
				} catch ( Exception e) {
						System.out.println(attributeName 
															 + " does not exist in " + this.getClass());
						System.out.println("Fields " + CollectionHelper.arrayToVector(cls.getDeclaredFields()));
											//e.printStackTrace();
				}
		}
    /**
     * Sets the Value attribute of the ProtegeDataSource object
     *
     * @param setter    The new Value value
     * @param attribute The new Value value
     * @param persObj   The new Value value
     * @param values    The new Value value
     */
//     private void setValue(Method setter,
// 								  AttributeAccessMap attribute,
// 								  Object persObj,
// 								  Object values[]) {
// 		  try {
// 				// By default direct access to  public variables
// 				if (setter == null) {
// 					 Field theField =
// 						  persObj.getClass().getField(attribute.attributeName);
// 					 values[0] = toPersistibleValue(theField.getType(), values[0]);
// 					 theField.set(persObj, values[0]);
// 					 	if (values[0] != null && values[0] instanceof Persistible
// 											&& persObj != null && persObj instanceof SaveListener ) 
// 								((Persistible)values[0]).addSaveListener((SaveListener)persObj);
					 
// 				}
// 				else {
// 					 // Make sure the arg does not need translated
// 					 // to another type
// 					 Class [] paramTypes = setter.getParameterTypes();
// 					 // Compare with the types of the values
					 
// 					 if ( Array.getLength(values) == Array.getLength(paramTypes) ) {
// 						  for ( int i = 0; i < Array.getLength(values); i++ ) {
// 								// 	System.out.println("paramTypes[i] = " + paramTypes[i]
// // 																		 + " values[i] = " + values[i]);
// 									values[i] = toPersistibleValue(paramTypes[i], values[i]);
// 									if (values[i] != null && values[i] instanceof Persistible
// 											&& persObj != null && persObj instanceof SaveListener ) 
// 											((Persistible)values[i]).addSaveListener((SaveListener)persObj);
// 						  }
// 					 }
					 
// 					 ReflectionHelper.invoke(setter, persObj, values);
// 				}
// 		  }
// 			catch (IllegalArgumentException iae) {
// 					System.out.println("Illegal argument for setter method " +
// 														 setter + " on " + persObj);
// 			}
// 		  catch (Exception e) {
// 					System.out.println("Problem with setter method for " +
// 														 setter + " on " + persObj);
// 				e.printStackTrace();
// 		  }
//     }

	protected Vector saveHandlers = new Vector();
	public void addSaveListener(SaveListener handler)
	{
			//System.out.println("Addingsave listener " + this);
		if(! saveHandlers.contains(handler))
			saveHandlers.add(handler);
	}
	public void removeSaveListener(SaveListener listener)
	{
		if(saveHandlers.contains(listener))
			  saveHandlers.removeElement(listener);
	}
	public void fireSaveEvent()
	{
			//System.out.println("fireSaveEvent " + this);
			SaveEvent e = new SaveEvent(this);
			Object [] it = saveHandlers.toArray();
			for ( int i = 0; i < Array.getLength(it); i++) {
					SaveListener sl = (SaveListener)it[i];
					sl.objectSaved(e);
			}

	}
	public void fireDeleteEvent()
	{
			//System.out.println("fireDeleteEvent " + this);
			SaveEvent e = new SaveEvent(this);
			Object [] it = saveHandlers.toArray();
			for ( int i = 0; i < Array.getLength(it); i++) {
					SaveListener sl = (SaveListener)it[i];
					if(sl != null && sl instanceof SaveListener)
							sl.objectDeleted(e);
			}
	}

		public boolean link(Persistible relatedPersistible) {
				// Make sure a circular relationship is not set up w/ ST & SB
				if ( this instanceof StatementTemplate &&
						 relatedPersistible instanceof StatementBundle ) 
						return false;
				boolean linked = true;
				// Get the types of the two objects
				Class linkFrom = getClass();
				Class linkTo = relatedPersistible.getClass();
				
				// Change this in next round of upgrades to allow 
				// each object manage their own link - make this a 
				// static method with object independent code only
				// Determine if they have a direct relationship
				Object linkPathObject =
						getDataSource().directlyLinkableVia(linkFrom, linkTo);

				// can the link be accessed directly thru public var or
				// through a public method
				if ( linkPathObject instanceof Field){
						// Get the field on this instance
						try {
								((Field)linkPathObject).set(this, relatedPersistible);
						} catch ( Exception ex ) {
								System.out.println
										("Unable to link persistibles thru direct field access." +
										 linkPathObject);
						}
				}
				else if ( linkPathObject instanceof Vector ) {
						// This must be a list of Methods setter then getter
						Object values[] = new Object[1];
// 						System.out.println("linking a vector");
					// 	System.out.println("relatedPersistible " 
// 															 + relatedPersistible.getClass());
						Vector methods = (Vector)linkPathObject;
						Method setter = (Method)methods.elementAt(0);
						Method getter = (Method)methods.elementAt(1);

						Class[] params = setter.getParameterTypes();
						// There should be a single arg
						Vector existingList = null;
						if ( params != null && Array.getLength(params) > 0) {
								if ( params[0].isAssignableFrom(Collection.class) ) {
										// Get existing items
										Object returnValue = ReflectionHelper.invoke
												((Method)getter,
												 this, 
												 null);
										// System.out.println("getter " + getter);
// 										System.out.println("what is returnValue  " + returnValue.getClass());
										if ( returnValue instanceof Collection )
												existingList = new Vector((Collection)returnValue);
										else if (  returnValue instanceof Iterator ) {// sucks
												existingList = 
														CollectionHelper.makeVector
														((Iterator)returnValue);
										}

										// Add to list make sure your current item isn't 
										// already linked
										// add it to the list
										if ( existingList == null ) 
												existingList = new Vector();
										existingList.addElement(relatedPersistible);
										values[0] = existingList;
								}
								else {
										values[0] = relatedPersistible;
								}
						// 		System.out.println("what values " + values[0] 
// 																	 + " setter " + setter);									
// 								System.out.println("invoke setter " + setter + " " 
//  																			+  values);
								ReflectionHelper.invoke(setter,
																				this, 
																				values);
						}
					// 	System.out.println("LINKED these objects " + this 
// 															 + " to  " + relatedPersistible);
				}
				else {
						System.out.println("Can't link these objects " + this 
															 + " TO >>  " + relatedPersistible);
						linked = false;
				}
				update();

				return linked;
		}

		public void unlink(Object obj) {
				if(obj instanceof Persistible)
						{
								unlink((Persistible) obj);
						}
		}

		public void unlink(Persistible relatedPersistible) {
				// Get the types of the two objects
				Class linkFrom = getClass();
				Class linkTo = relatedPersistible.getClass();

				// Determine if they have a direct relationship
				Object linkPathObject =
						getDataSource().directlyLinkableVia(linkFrom, linkTo);

				// can the link be accessed directly thru public var or
				// through a public method
				if ( linkPathObject instanceof Field){
						// Get the field on this instance
						try {
								if ( ((Field)linkPathObject).get(this) 
										 == relatedPersistible ) 
// 										System.out.println("Unlink using field " + this);
										((Field)linkPathObject).set(this, null);
						} catch ( Exception ex ) {
								System.out.println
										("Unable to UNlink persistibles thru direct field access." +
										 linkPathObject);
						}
				}
				else if ( linkPathObject instanceof Vector ) {
						// This must be a list of Methods setter then getter
						Object values[] = new Object[1];
					
					// 	System.out.println("relatedPersistible " 
// 															 + relatedPersistible.getClass());
						Vector methods = (Vector)linkPathObject;
						Method setter = (Method)methods.elementAt(0);
						Method getter = (Method)methods.elementAt(1);

						Class[] params = setter.getParameterTypes();
						// There should be a single arg
						Vector existingList = null;
						if ( params != null && Array.getLength(params) > 0) {
								Object returnValue = ReflectionHelper.invoke
										((Method)getter,
										 this, 
										 null);
								//System.out.println(" getter " + getter + " params " + returnValue + " this " + this + " " + relatedPersistible
								//																	 );
								// Get existing items  MAKEVECTOR
								if ( returnValue instanceof Collection ) {
										//System.out.println("Unlink collection " + existingList);

										existingList = new Vector((Collection)returnValue);
										//Remove from list if it is in there
										if ( existingList != null ) {
												existingList.remove(relatedPersistible);
										}
										//System.out.println("Unlink collection " + existingList);
										values[0] = existingList;
								}
								else {
										if ( returnValue == relatedPersistible ) {
												values[0] = null;
										}
								}
								//System.out.println("what values " + values[0] + " setter " + setter);
								ReflectionHelper.invoke(setter,
																				this, 
																				values);
						}
// 						System.out.println("Unlinking abstractpersistible");
				}
				else 
						System.out.println("Can't unlink these objects " + this 
															 + " FROM " + relatedPersistible);
		}

		public void objectSaved(SaveEvent e) {
				// do nothing
		}

		public void objectDeleted(SaveEvent e) {
	// 			System.out.println("objectDeleted unlink " + this 
// 													 + " from " + e.getSavedObject());
				unlink(e.getSavedObject());
		}
		
		
		//TODO !!!! Needs to be generalized for all objects
		public boolean connectedTo(Persistible persistibleObject)
		{
				return DataSourceStatus.getDataSource().isConnected
						(this, persistibleObject);
			// 		System.out.println("is " + this + " connected to " + 
			// 												persistibleObject + " " + 
			// 												isConnected);
		}

/*if ( this instanceof AutoGenPersistibleWithKeywords && 
	persistibleObject instanceof AutoGenPersistibleWithKeywords) {
	return connected((AutoGenPersistibleWithKeywords)this, 
	(AutoGenPersistibleWithKeywords)persistibleObject);
*/
		private boolean connected(AutoGenPersistibleWithKeywords thisPersistible,
													AutoGenPersistibleWithKeywords persistibleObject) {
					// go through all the fields and see if the two objects are linked
					Hashtable methodMap = 
							thisPersistible.getGetterMap();
					for (Enumeration e = methodMap.keys();
							 e.hasMoreElements(); ) {
							Object obj = e.nextElement();
								Object getReturn = 
										ReflectionHelper.invoke((Method)obj, 
																						thisPersistible, 
																						null);
								if ( getReturn != null) {
										if ( getReturn instanceof Collection ) {
												Iterator i = ((Collection)getReturn).iterator();
												while (i.hasNext() ) {
														Object obj2 = i.next();
														if ( obj2.equals(persistibleObject) ) 
																return true;
												}
										}
										else {
												// Individual value does it match the persistible obj
												if ( getReturn.equals(persistibleObject) ) 
														return true;
										}
										
								}
					}
					return false;
		}
	
		public void setPersistibleState(int state){
				this.state = state;
		}

		public int getPersistibleState() {
				return state;
		}

		// Tree user object
		public Vector getTreeNodes() {
				return treeNodes;
		}
		
		public void addTreeNode(TreeNode treeNode) {
				if ( treeNodes == null )
						treeNodes = new Vector();
				if ( !treeNodes.contains(treeNode) )
						treeNodes.addElement(treeNode);
				//	System.out.println("Nodes on a persistible " + this 
				//+ " " + treeNodes.size());

		}
		public void clearTreeNodes() {
				if ( treeNodes != null )
						treeNodes.clear();
				treeNodes = null;
		}
		public void removeTreeNode(TreeNode treeNode) {
				if ( treeNodes != null )
						treeNodes.remove(treeNode);
		}
		public boolean hasTreeNode(TreeNode treeNode) {
				if ( treeNodes != null )
						return treeNodes.contains(treeNode);
				else
						return false;
		}
		public Color getForeground(){
				return fgColor;
		}
		public void setForeground(Color color){
				fgColor = color;
		}

		public Object clone() {
				// Create the copy -- do i need a guid here
				Object newInstance  = null;
				try {
						newInstance = getClass().newInstance();
				} catch ( Exception e) {
						e.printStackTrace();
				}
				OncoTCapDataSource ds = getDataSource();
				Vector attributeMaps  = 
						ds.getDataSourceInfo().mapForJavaClass(getClass().getName());
			
				DataSourceObject dsObject = 
						(DataSourceObject)attributeMaps.firstElement();
				// For every attribute that gets stored in the ds 
				//set the values from the original
				for (Enumeration e = dsObject.attributeAccessMaps.elements();
						 e.hasMoreElements(); ) {
						AttributeAccessMap attribute =
								(AttributeAccessMap)e.nextElement();
						Object originalValue = 
								ds.getValueFromPersistible((Persistible)this,
																					 attribute);
						//System.out.println("originalValue " 
						// + attribute.dsAttributeName + " " + originalValue);
						ds.setValueInPersistible((Persistible)newInstance, 
																	attribute,
																	originalValue);
				}

				return newInstance;
		}

		public boolean hasSameValuesAs(Object obj) {
				if ( this.getClass() != obj.getClass() ) 
						return false;
				// Loop thru the object and see if the values are 
				// the same except for the GUID
				OncoTCapDataSource ds = getDataSource();
				Vector attributeMaps  = 
						ds.getDataSourceInfo().mapForJavaClass(getClass().getName());
			
				DataSourceObject dsObject = 
						(DataSourceObject)attributeMaps.firstElement();
				// For every attribute that gets stored in the ds 
				//set the values from the original
				for (Enumeration e = dsObject.attributeAccessMaps.elements();
						 e.hasMoreElements(); ) {
						AttributeAccessMap attribute =
								(AttributeAccessMap)e.nextElement();
						//Skip any gui or timestamp or version attribute
						if ( ignoreAttribute(attribute) ) 
								continue;
						Object originalValue = 
								ds.getValueFromPersistible((Persistible)this,
																					 attribute);
						Object compareValue = 
								ds.getValueFromPersistible((Persistible)obj,
																					 attribute);
					// 	// Need special code here to compare trees
// 						if ( originalValue instanceof AbstractPersistible ) {
// 								if ( originalValue instanceof AbstractPersistible 
// 										 && compareValue instanceof AbstractPersistible) {
// 										if ( (originalValue != null 
// 													&& !((AbstractPersistible)obj).hasSameValuesAs((AbstractPersistible)compareValue))
// 												 || (originalValue == null && compareValue != null) )
// 												System.out.println("abs pers differ");
// 										return false;
// 								}
// 						}
								
						if ( (originalValue != null 
									&& !originalValue.equals(compareValue) ) 
								 || (originalValue == null && compareValue != null) ) {
							// 	System.out.println("values differ: " + originalValue 
// 																	 + " " + compareValue
// 																	 + " attribute name " 
// 																	 + attribute.attributeName);
								return false;
						}
				}

				return true;
		}
		private boolean ignoreAttribute(AttributeAccessMap attribute) {
				if ( "modificationTime".equals(attribute.attributeName.trim())
						|| "modifier".equals(attribute.attributeName.trim())
						 || "creator".equals(attribute.attributeName.trim())
						 || "creationTime".equals(attribute.attributeName.trim())
						 || "versionNumber".equals(attribute.attributeName.trim())
						 || "oncotcapOntologyVersion".equals(attribute.attributeName.trim())) {
							return true;
				}
				return false;
		}
		public void initPopupMenu() {
			//TODO:  Create a static hashtable of popupmenus, one for each persistible type.
			// Pro:  reduce #objects and start=up time.
			// Con:  Cannot customize menus as easily based on SUBtype of persistible - small potatos!
			popup = new OncPopupMenu();
			popup.setCurrentObject(this);
			JMenuItem mi;
			mi = new JMenuItem("Copy");
			CopyPersistibleAction copyAction = new CopyPersistibleAction();
			mi.setAction(copyAction);
			popup.add(mi);
			mi = new JMenuItem("Delete");
//					DeletePersistibleAction copyAction = new CopyPersistibleAction();
			//mi.setAction(copyAction);
			mi.setEnabled(false);
			popup.add(mi);
			popup.setOpaque(true);
			popup.setLightWeightPopupEnabled(true);
		}
		public JPopupMenu getPopupMenu() {
			//TODO:  look up the static menu in the hashtable, and set the user object.
				if ( popup == null ) 
						initPopupMenu();
				return popup;
		}
}
