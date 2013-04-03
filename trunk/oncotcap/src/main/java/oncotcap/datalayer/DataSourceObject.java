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

/**
 * Basic structure to hold a description of how to map information to and
 * from the persistent data source ( Protege or and RDB)
 * @author     morris
 * @created    March 11, 2003
 */
public class DataSourceObject {
	/** */
	public Vector attributeAccessMaps = null;
	/** */
	public String name = null;


	/**
	 *  Constructor for the DataSourceObject object
	 *
	 * @param  name data source object name (ex. protege frame or database table)
	 * @param  attributeAccessMaps List of java fields and corresponding datasource
	 *                             objects (ex. database columns and protege slots)
	 */
	public DataSourceObject(String name,
	                        Vector attributeAccessMaps) {
		this.name = name;
		this.attributeAccessMaps = attributeAccessMaps;
	}
}
