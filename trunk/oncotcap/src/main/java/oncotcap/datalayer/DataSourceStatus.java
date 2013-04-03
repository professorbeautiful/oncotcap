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

import oncotcap.datalayer.persistible.ProblemContext;

/**
 * Provided static methods for data source access
 *
 * @author   morris
 * @created  March 12, 2003
 */
public class DataSourceStatus {
	/** */
	public static OncoTCapDataSource dataSource = null;
	/** */
	public static ProblemContext problemContext = null;


	/**
	 * Gets the DataSource attribute of the DataSourceStatus class
	 *
	 * @return  The DataSource value
	 */
	public static OncoTCapDataSource getDataSource() {
		return dataSource;
	}

	/**
	 * Gets the ProblemContext attribute of the DataSourceStatus class
	 *
	 * @return  The ProblemContext value
	 */
	public static ProblemContext getProblemContext() {
		return problemContext;
	}


	/**
	 * Sets the DataSource attribute of the DataSourceStatus class
	 *
	 * @param ds The new data source value
	 */
	public static void setDataSource(OncoTCapDataSource ds) {
		dataSource = ds;
	}


	/**
	 * Sets the ProblemContext attribute of the DataSourceStatus class
	 *
	 * @param currentProblemContext The new problem context value
	 */
	public static void setProblemContext(ProblemContext currentProblemContext) {
		problemContext = currentProblemContext;
	}


}

