package oncotcap.datalayer.persistible;

import java.awt.datatransfer.DataFlavor;
import java.util.*;
import java.beans.*;
import java.io.Serializable;
import javax.swing.tree.*;

import oncotcap.display.common.*;
import oncotcap.display.browser.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.util.*;
import oncotcap.Oncotcap;

public class OncFilterLite implements Serializable {
	public OncTreeNode rootNode;


	public OncFilterLite() {
	}
	public OncFilterLite(OncTreeNode rootNode) {
		this.rootNode = rootNode;
	}
	public void copyFilter(OncTreeNode rootNode) {
		// Build a skinny tree modelled after the original oncfilter
		OncTreeNode liteRootNode = new OncTreeNode(rootNode.getUserObject());
		this.rootNode = liteRootNode;
		copyNodeSkinny(liteRootNode, rootNode);
	}
	private void copyNodeSkinny(OncTreeNode liteNode, OncTreeNode parentNode) {
			Enumeration children = parentNode.children();
			for (int i=0;children.hasMoreElements();i++) {
					DefaultMutableTreeNode currentNode = 
							(DefaultMutableTreeNode) children.nextElement();
					Object userObject = 
							currentNode.getUserObject();
					OncTreeNode newTreeNode = null;
					if ( userObject instanceof AbstractPersistible
							 ||  userObject instanceof TcapLogicalOperator
							 ||  userObject instanceof SystemDefinedTcapString ) {
							// Add guid string to the OncFilterLites  

									newTreeNode = 
										new OncTreeNode(((AbstractPersistible)userObject).getGUID().toString());
							if ( newTreeNode != null ) {
									liteNode.add(newTreeNode);
							}
					}
					else {
							newTreeNode = new OncTreeNode(userObject);
							liteNode.add(newTreeNode);
					}
					if ( currentNode.getChildCount() > 0) 
							copyNodeSkinny(newTreeNode, (OncTreeNode)currentNode);
			} // for each node

	}

	public OncFilter buildFilter() {
			// Build a skinny tree modelled after the original oncfilter
			OncTreeNode fatRootNode = new OncTreeNode(rootNode.getUserObject());
			copyNodeFat(fatRootNode, rootNode);
			OncFilter oncFilter = new OncFilter(false);
			oncFilter.setRootNode(fatRootNode);
			return(oncFilter);
	}
	private void copyNodeFat(OncTreeNode fatNode, OncTreeNode parentNode) {
			Enumeration children = parentNode.children();
			for (int i=0;children.hasMoreElements();i++) {
					DefaultMutableTreeNode currentNode = 
							(DefaultMutableTreeNode) children.nextElement();
					Object userObject = 
							currentNode.getUserObject();
					OncTreeNode newTreeNode = null;
					if ( userObject instanceof String ) {
							// Search the DS for the object
							Object userObj = Oncotcap.getDataSource().find
									(new GUID((String)userObject)); //FILTER
							if ( userObj != null ) {
									// Add the real persistible to the filter
									newTreeNode 
											= new OncTreeNode((AbstractPersistible)userObj);
									if ( newTreeNode != null )
											fatNode.add(newTreeNode);
							}
					}
					else {
							newTreeNode = new OncTreeNode(userObject);
							fatNode.add(newTreeNode);
					}
					if ( currentNode.getChildCount() > 0) 
							copyNodeFat(newTreeNode, (OncTreeNode)currentNode);
			} // for each node

	}

	public String toString() {
			return getDisplayName();
	}
	public String getDisplayName() {
			// Use the filter to construct a name
			return addToName().toString();
	}
	public StringBuffer addToName() {
			// Visit each node 
			return addToName(rootNode, new StringBuffer(), " ");
	}
	public StringBuffer addToName(OncTreeNode node, StringBuffer nameString, 
												 String separator) {
			Object userObj = node.getUserObject();
			if ( userObj instanceof TcapLogicalOperator ||
					 userObj instanceof SystemDefinedTcapString) {
					nameString.append("(");
					separator = " " + userObj.toString() + " ";
					if ( userObj == TcapLogicalOperator.NOT)
							nameString.append(separator);
			}
			else {
							nameString.append(userObj.toString());
			}
if (node.getChildCount() >= 0) {
    for (Enumeration e=node.children(); e.hasMoreElements(); ) {
	OncTreeNode n = (OncTreeNode)e.nextElement();
	addToName(n, nameString, separator);
							if ( e.hasMoreElements() ) 
									nameString.append(separator);
    }
}
			if ( node.getUserObject() instanceof TcapLogicalOperator ) {
					nameString.append(")");
			}
			return nameString;
	}

	public void setRootNode(OncTreeNode rootNode)
	{
		this.rootNode = rootNode;	
	}

	public OncTreeNode getRootNode()
	{
		return(rootNode);
	}

}
