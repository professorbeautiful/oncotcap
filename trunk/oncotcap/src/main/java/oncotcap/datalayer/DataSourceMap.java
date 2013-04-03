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

/*
 * public void addTransaction() {
 * This method is unecessary for this datasource since an
 * in memory copy is provided via protege mechanisms
 * therefore the persistance storage is achieved simply
 * by issuing a protege project save command
 * The down side is that it is all or nothing save (no undo)
 * }
 */
/**
 * @author   morris
 * @created  March 25, 2003
 */
public  class DataSourceMap {
	/** */
	Vector attributeMaps = null;
	/** */
	Vector dataSourceMapInstances = null;
	/** */
	Vector javaClassNames = null;
	/** */
	Vector primaryProtegeClassNames = null;
	
	private KnowledgeBase kb = null;

	/** Constructor for the DataSourceMap object */
	public DataSourceMap(KnowledgeBase kb) {
			this.kb = kb;
			//System.out.println("CREATING DATA SOURCE MAP");
	    javaClassNames = new Vector();
	    primaryProtegeClassNames = new Vector();
	    dataSourceMapInstances = new Vector();
	    attributeMaps = new Vector();
	}
	
	
	/**
	 * Initialize the datasource. Read in mapping information from the datasource
	 * and store info for later use
	 *
	 * @param javaClassName The feature to be added to the MapInfo attribute
	 * @return              Description of the Returned Value
	 */
	public Vector addMapInfo(String javaClassName, int pos) {
	    //System.out.println("addingMapInfo for " + javaClassName);
	    
	    // Get position from the other map
			
	    int position = javaClassNames.indexOf(javaClassName);
			//System.out.println("addmapinfo position " + position);
	    AttributeAccessMap attrAccessMap = null;
	    Instance dsObjectInstance =
					(Instance)dataSourceMapInstances.elementAt(position);
	    Instance dataSourceMap = null;
	    Instance dsAttributeMap = null;
	    String primaryProtegeClassName = null;
	    Vector dsObjectInstanceVector = new Vector();
	    Vector attributeMapVector = new Vector();
	    // Now pull out the map
	    Frame aFrame = (Frame)dsObjectInstance.getOwnSlotValue(
								   kb.getSlot("protegeClass"));
	    
	    primaryProtegeClassName = aFrame.getName();
	    
	    Collection attributeList =
					dsObjectInstance.getOwnSlotValues(
						  kb.getSlot("attributeList"));
	    Iterator iii = attributeList.iterator();
	    
	    while (iii.hasNext()) {
					Instance attributeMap = (Instance)iii.next();
		
					attrAccessMap = instantiateAttrMap(attributeMap, javaClassName);
					if ( attrAccessMap != null )
							attributeMapVector.addElement(attrAccessMap);
	    }
	    //attributes

			// Add any default attributes like timestamp and modifiers
			addDefaultAttributes(attributeMapVector, javaClassName);
	    
	    DataSourceObject dsObject =
					new DataSourceObject(primaryProtegeClassName,
															 attributeMapVector);
	    
	    dsObjectInstanceVector.addElement(dsObject);
	    
	    // assumes 1st mapped object is the primary object
	    attributeMaps.set(position, dsObjectInstanceVector);
	    return dsObjectInstanceVector;
	}

	private void addDefaultAttributes(Vector attrMaps, String javaClassName) {
// 			System.out.println("attrMaps " + attrMaps.size());
			// Add the attribute that are on every persistible like the 
			// creation time, etc.....
			// Get the data source Object instance for TimeStampable
			int position = javaClassNames.indexOf("oncotcap.datalayer.autogenpersistible.Timestampable");

			Instance dsObjectInstance = 
					(Instance)dataSourceMapInstances.elementAt(position);
	    Collection attributeList =
					dsObjectInstance.getOwnSlotValues(
																						kb.getSlot("attributeList"));
	    Iterator iii = attributeList.iterator();
	    AttributeAccessMap attrAccessMap = null;
	    while (iii.hasNext()) {
					Instance attributeMap = (Instance)iii.next();
					attrAccessMap = instantiateAttrMap(attributeMap, 
																						 javaClassName);
					// System.out.println("Adding default attributemap : " 
// 														 + attrAccessMap);
					if ( attrAccessMap != null ) 
							attrMaps.addElement(attrAccessMap);
	    }
			//System.out.println("attributeMaps " + attributeMaps.size());
			//return attributeMaps;
			
	}
	
	
	/**
	 * @param protegeName Description of Parameter
	 * @return            Description of the Returned Value
	 */
	public String javaClassForProtegeClass(String protegeName) {
	    int position = primaryProtegeClassNames.indexOf(protegeName);
	    if (position < 0)
					return null;
	    return (String)javaClassNames.elementAt(position);
	}
	
	
	/**
	 * @param javaClassName Description of Parameter
	 * @return              Description of the Returned Value
	 */ 
	public Vector mapForJavaClass(String javaClassName) {
	    int position = javaClassNames.indexOf(javaClassName);
	    // If map is empty initialize it
			if ( position < 0 ){
					System.out.println("javaClassNames " + javaClassNames);

					System.out.println("ERROR: Unable to locate mapForJavaClass  " 
														 + javaClassName);
			}
	    Object attributeMap = attributeMaps.elementAt(position);
	    if (attributeMap == null) {
					return addMapInfo(javaClassName, position);
	    }
	    else {
					return (Vector)attributeMaps.elementAt(position);
	    }
	}
	
	
	/**
	 * @param protegeName Description of Parameter
	 * @return            Description of the Returned Value
	 */
	protected Vector mapForProtegeClass(String protegeName) {
	    int position = primaryProtegeClassNames.indexOf(protegeName);
	    
	    // If map is empty initialize it
	    return (Vector)attributeMaps.elementAt(position);
	}
	
	
	/**
	 * @param javaClassName Description of Parameter
	 * @return              Description of the Returned Value
	 */
	protected String protegeClassForJavaClass(String javaClassName) {
	    int position = javaClassNames.indexOf(javaClassName);
			//			System.out.println("protegeClassForJavaClass " + javaClassNames
			//									 + " - " + javaClassName);
	    if ( position < 0 ) 
					return null;
	    return (String)primaryProtegeClassNames.elementAt(position);
	}
	
	
	/**
	 * @param position                 Description of Parameter
	 * @param javaClassName            Description of Parameter
	 * @param primaryProtegeClassName  Description of Parameter
	 * @param attributeMap             Description of Parameter
	 * @param dataSourceObjectInstance Description of Parameter
	 */
	protected void put(int position,
			   String javaClassName,
			   String primaryProtegeClassName,
			   Vector attributeMap,
			   Instance dataSourceObjectInstance) {
			
	    // Make sure java class name and primary protege class
	    // names are unique they are really keys
	    if (javaClassNames.contains(javaClassName)) {
				// 	System.out.println("java class name not unique " + javaClassName + " " + 
// 												 primaryProtegeClassName + " " + position);
					return;
	    }
	    if (primaryProtegeClassNames.contains(primaryProtegeClassName)) {
			// 		System.out.println("Primary Protege Class Name not unique " + javaClassName + " " + 
// 												 primaryProtegeClassName + " " + position);
					return;
	    }
		// 	System.out.println("PUTTING " + javaClassName + " " + 
// 												 primaryProtegeClassName + " " + position);
	    javaClassNames.add(position, javaClassName);
	    dataSourceMapInstances.add(position, dataSourceObjectInstance);
	    primaryProtegeClassNames.add(position, primaryProtegeClassName);
	    attributeMaps.add(position, attributeMap);
	}
	
	
	/**
	 * @param position     Description of Parameter
	 * @param attributeMap Description of Parameter
	 */
	protected void put(int position, Vector attributeMap) {
	    attributeMaps.add(position, attributeMap);
	}
	
	
	/**
	 * @param attributeMapInstance Description of Parameter
	 * @param javaClassName        Description of Parameter
	 * @return                     Description of the Returned Value
	 */
	private AttributeAccessMap instantiateAttrMap(
																								Instance attributeMapInstance,
																								String javaClassName) {
	    AttributeAccessMap attributeMap = null;
			String attributeType = null;
			String attributeSubType = null;
	    Instance attributeTypeInstance =
					(Instance)attributeMapInstance.getOwnSlotValue(
																			kb.getSlot("clsAttributeType"));
			if (attributeTypeInstance != null) {
					attributeType = 
							String.valueOf(attributeTypeInstance.getOwnSlotValue(
										kb.getSlot("classAndPackage")));
			}
	    Instance attributeSubTypeInstance =
					(Instance)attributeMapInstance.getOwnSlotValue(
																					 kb.getSlot("clsAttributeSubType"));
			if (attributeSubTypeInstance != null) {
					attributeSubType = 
							String.valueOf(attributeSubTypeInstance.getOwnSlotValue(
										kb.getSlot("classAndPackage")));
			}			
			Slot dsa = kb.getSlot("dataSourceAttribute");
			Instance dsAttr = null;
			if (dsa != null ) 
					dsAttr = (Instance)attributeMapInstance.getOwnSlotValue(dsa);

			// Backward compatibility
			String dsAttrName = null;
			if (dsAttr == null) {
					 dsAttrName =
							String.valueOf(attributeMapInstance.getOwnSlotValue(
																	kb.getSlot("dataSourceAttributeName")));
			}
			else {
					dsAttrName = 
							dsAttr.getName();
			}
		
	    String attrName =
					String.valueOf(attributeMapInstance.getOwnSlotValue(
								    kb.getSlot("attributeName")));
	    String setMethodName =
					String.valueOf(attributeMapInstance.getOwnSlotValue(
								    kb.getSlot("setMethodName")));
	    
	    String getMethodName =
					String.valueOf(attributeMapInstance.getOwnSlotValue(
								    kb.getSlot("getMethodName")));
	  //   if ( dsAttrName.equals("creator") 
// 					 || dsAttrName.equals("creationTime")
// 					 || dsAttrName.equals("modifier")
// 					 || dsAttrName.equals("modificationTime")){
// 					return null;
// 			}

					attributeMap = new AttributeAccessMap(javaClassName,
				       dsAttrName,
				       attrName,
				       attributeType,
				       attributeSubType,
				       getMethodName,
				       setMethodName);
			
	    return attributeMap;
	}
    }
		
