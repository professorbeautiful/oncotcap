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
package oncotcap.display.browser;

import java.awt.datatransfer.*;
import javax.swing.tree.*;

import oncotcap.display.common.Droppable;

public class GenericTreeNode extends DefaultMutableTreeNode implements Droppable {

	/**
	 **	flavorsWithDroppable	used by getTransferDataFlavors for Droppable interface
	 **	@version July 16, 2003 by shirey
	 **/
	private static final DataFlavor [] flavorsWithDroppable = 
	{Droppable.droppableData, Droppable.genericTreeNode,DataFlavor.stringFlavor};
	/**
	 **	flavorsWithoutDroppable	used by getTransferDataFlavors for Droppable interface
	 **	@version July 16, 2003 by shirey
	 **/
	private static final DataFlavor [] flavorsWithoutDroppable = {DataFlavor.stringFlavor};
		public GenericTreeNode(Object userObject) {
				super(userObject);
				if ( userObject instanceof TreeUserObject ) {
						((TreeUserObject)userObject).addTreeNode(this);
				}
														
		}
		
		public void setUserObject(Object obj) {
				// if ( obj == null ) {
						System.out.println("setting ull user object WHAT");
						oncotcap.util.ForceStackTrace.showStackTrace();
// 				}
				super.setUserObject(obj);
		}
		public String toString() {
				if ( getUserObject() == null ) 
						return " user object is null ";
				else
						getUserObject().getClass();
				return "NODE " + getUserObject().toString();
		}
		public void registerType(Object type) {
				// add yourself to a list of similar type objects
		}

			/**
	 ** getTransferData	Required for the Droppable interface. Used to
	 **						retrieve this object when it is being used as a
	 **						Transferable (Droppable) object for DnD or
	 **						Clipboard operations.
	 **
	 ** @param	DataFlavor the flavor of data that is required.  Only
	 **			DataFlavor.stringFlavor and Droppable.persistible are
	 **			supported.
	 **			
	 ** @returns	This object if a persistible DataFlavor is asked for or
	 **				the toString() result if a stringFlavor is asked for.
	 **
	 ** @author	shirey July 9, 2003
    **/
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
		{
			if(isDataFlavorSupported(flavor))
			{
				if(flavor == DataFlavor.stringFlavor)
					return(toString());
				else if(flavor == Droppable.droppableData)
					return(getUserObject());
				else if(flavor == Droppable.genericTreeNode)
					return(this);
			}
			throw(new UnsupportedFlavorException(flavor));
		}

	/**
	 ** getTransferDataFlavors	Required for the Droppable interface.
	 **								Used to get a list of all DataFlavors
	 **								supported by this object.  Only
	 **								Droppable.droppableData and
	 **								DataFlavor.stringFlavor are supported.
	 **								droppableData is only supported if the
	 **								node is holding an oncotcap.display.common.Droppable
	 **								object.
	 **
	 ** @returns	The list of supported DataFlavors.
	 **
	 ** @author	shirey July 16, 2003
	 **/
		public DataFlavor[] getTransferDataFlavors()
		{
			if(getUserObject() != null && getUserObject() instanceof Droppable)
				return(flavorsWithDroppable);
			else
				return(flavorsWithoutDroppable);
		}
	/**
	 ** isDataFlavorSupported	Required for the Droppable interface.
	 **								Used to query this object to find out if
	 **								a particular DataFlavor is supported.
	 **								Only Droppable.droppableData and
	 **								DataFlavor.stringFlavor are supported and
	 **								droppableData is only supported if this
	 **								node is holding a oncotcap.display.common.Droppable
	 **								object.
	 **
	 ** @param	DataFlavor	The DataFlavor to compare to the supported
	 **							flavors.
	 **
	 ** @returns	true if the flavor is supported, false if it isn't.
	 ** @author	shirey July 16, 2003
	 **/
		public boolean isDataFlavorSupported(DataFlavor flavor)
		{
				if(flavor.equals(DataFlavor.stringFlavor))
						return(true);
				else if(flavor.equals(Droppable.droppableData) && getUserObject() != null && getUserObject() instanceof Droppable)
						return(true);
			else if (flavor.equals(Droppable.genericTreeNode))
					return true;
			else
				return(false);
		}

		public boolean dropOn(Object dropOnObject) {
				if ( dropOnObject instanceof String) {
						return false; // really if this is root node it can be dropped 
				}
				else if (dropOnObject instanceof GenericTreeNode ) {
						Object dropOnUserObject = 
								((GenericTreeNode)dropOnObject).getUserObject();
						if ( dropOnUserObject instanceof Droppable 
								 &&	getUserObject() instanceof Droppable ) {
								return ((Droppable)getUserObject()).dropOn(dropOnUserObject);
						}
				}
				return false;
		}

		// Find the first user object of the first node with user object type 'cls'
		// connected to this tree node 
		//movingtowards tree root
		static public GenericTreeNode getClosestNode(Class cls,
																								 GenericTreeNode treeNode) {
				TreeNode[] objectsToRoot = treeNode.getPath();
				for (int i=objectsToRoot.length-2; i > 0; i--) {
						if ( cls.isInstance(((DefaultMutableTreeNode)objectsToRoot[i]).getUserObject() ))
								return (GenericTreeNode)objectsToRoot[i];
				}
				return null;
		} //end class PreprocessedCodeBundle
		
		// Find the first node with user object type 'cls'
		// connected to this tree node 
		//movingtowards tree root
		static public Object  getClosest(Class cls,
																		 GenericTreeNode treeNode) {
				GenericTreeNode node = getClosestNode(cls, treeNode);
				return node.getUserObject();
		} 
		

}
