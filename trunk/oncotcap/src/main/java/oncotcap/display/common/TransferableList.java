package oncotcap.display.common;

import java.util.*;
import java.lang.reflect.*;
import java.awt.datatransfer.*;
import oncotcap.display.common.*;
import oncotcap.display.browser.GenericTreeNode;



public class TransferableList implements Transferable, Droppable {
		//bug why? private static final DataFlavor [] flavors 
		//= {Droppable.transferableList};
		Vector data = null;

		public TransferableList(Vector alist) {
				data = alist;
		}
		
		public Object getTransferData(DataFlavor flavor) 
				throws UnsupportedFlavorException {
				if(isDataFlavorSupported(flavor))	{
						if(flavor == Droppable.transferableList) {  
								return data;
						}
				}
				throw(new UnsupportedFlavorException(flavor));
		}
		public DataFlavor[] getTransferDataFlavors() {
				DataFlavor[] flavors = new DataFlavor[1];
				try { 
						flavors[0] = Droppable.transferableList;
				}
				catch (Exception e) {
						e.printStackTrace();
				}
				return(flavors);
		}
		public boolean isDataFlavorSupported(DataFlavor flavor)	{
				if(flavor.equals(Droppable.transferableList)) {
						System.out.println("isDataFlavorSupported true");
						return(true);
				}
				else
						return(false);
		}

		public boolean dropOn(Object dropOnObject) {
				System.out.println("Transferable dropping on " + this);
			// 	if ( dropOnObject instanceof String) {
// 						return false; // really if this is root node it can be dropped 
// 				}
// 				else if (dropOnObject instanceof GenericTreeNode ) {
// 						Object dropOnUserObject = 
// 								((GenericTreeNode)dropOnObject).getUserObject();
// 						if ( dropOnUserObject instanceof Droppable 
// 								 &&	getUserObject() instanceof Droppable ) {
// 								return ((Droppable)getUserObject()).dropOn(dropOnUserObject);
// 						}
// 				}
				return false;
		}

		public String toString(){
				return "TransferableList [ " + data + "]";
		}
		
		
}
