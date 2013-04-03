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

import java.io.Serializable;
import java.awt.datatransfer.*;
import oncotcap.display.common.Droppable;

public class SearchText 
		implements oncotcap.display.common.Droppable,
							 Serializable {
		private static final DataFlavor [] flavors = {Droppable.searchText, DataFlavor.stringFlavor};
		String text = new String();
		public SearchText(String text) {
				this.text = text;
		}
		public boolean dropOn(Object dropOnObject){
				System.out.println("Do Nothing");
				return false;
		}
		public void setText(String str){
				text = str;
		}
		public String setText(){
				return text;
		}
		public boolean isDataFlavorSupported( DataFlavor flavor)
		{
				System.out.println("isDataFlavorSupported");
			return( flavor.equals(Droppable.searchText));
		}
	
		public Object getTransferData(DataFlavor flavor) 
				throws UnsupportedFlavorException  {
				System.out.println("getTransferData" + flavor);
				if(isDataFlavorSupported(flavor))
						{
								if(flavor == DataFlavor.stringFlavor)
										return(toString());
								else if(flavor == Droppable.searchText)
										return(this);
						}
				throw(new UnsupportedFlavorException(flavor));
		}

		public DataFlavor[] getTransferDataFlavors() {
				System.out.println("getTransferDataFlavors SearchText");
				oncotcap.util.ForceStackTrace.showStackTrace();
				return(flavors);
		}

		public String toString() {
				return text;
		}
}
