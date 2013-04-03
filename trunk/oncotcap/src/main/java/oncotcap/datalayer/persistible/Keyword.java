package oncotcap.datalayer.persistible;

import java.util.*;
import java.awt.datatransfer.*;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import java.awt.datatransfer.*;
import javax.swing.*;


import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.AbstractPersistible;
import oncotcap.display.common.Droppable;
import oncotcap.display.browser.TreeDisplayModePanel;
import oncotcap.datalayer.AutoGenEditable;
import oncotcap.datalayer.DefaultPersistibleList;
import oncotcap.datalayer.PersistibleWithKeywords;
import oncotcap.display.editor.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.editor.persistibleeditorpanel.KeywordPanel;
import oncotcap.util.ReflectionHelper;
import oncotcap.util.CollectionHelper;
import java.lang.reflect.*;


public class Keyword extends AbstractPersistible 
		implements Droppable, Editable, AutoGenEditable,
							 oncotcap.display.browser.TreeBrowserNode,
							 Popupable
{
	/**
	 **	flavors	used by getTransferDataFlavors for Droppable interface
	 **	@version July 9, 2003 by shirey
	 **/
	private static final DataFlavor [] flavors = {Droppable.droppableData, DataFlavor.stringFlavor};

	public String keyword = null;
	public Vector children = new Vector();
	public DefaultPersistibleList parentKeywords = null;
	private ImageIcon icon =	
			oncotcap.util.OncoTcapIcons.getImageIcon("keyword.jpg");
	private Hashtable setMethodMap = new Hashtable();
	private Hashtable getMethodMap = new Hashtable();

	public Vector subordinateKeywords = null;
	public DefaultPersistibleList describedInstances;
	public static Vector allKeywords = new Vector();

	public Keyword(oncotcap.util.GUID guid){
		super(guid);
		init();
	}	
	
	public Keyword(){
			init();
	}

	public Keyword(String keyword){
			init();
			setKeyword(keyword);
	}
	private void init() {
		initGetterMap();
		initSetterMap();
		allKeywords.addElement(this);
		initPopupMenu();
	}

	public void initPopupMenu() {
		super.initPopupMenu();
		if ( getPopupMenu() != null ) {
					JMenu otherDataSourcesMenu = new JMenu("Other Data Sources");
					getPopupMenu().add(otherDataSourcesMenu);
					JMenuItem mi;
					mi = new JMenuItem("Show Level Lists");
					ShowLevelListAction showLevelList = 
							new ShowLevelListAction("Show Level List");
					mi.setAction(showLevelList);
					getPopupMenu().add(mi);

					mi = new JMenuItem("Add Level Lists");
					AddLevelListAction addLevelList = 
							new AddLevelListAction("Add Level List");
					mi.setAction(addLevelList);
					getPopupMenu().add(mi);

					mi = new JMenuItem("OMIM");
					mi.setActionCommand("goto OMIM");
					otherDataSourcesMenu.add(mi);
					mi = new JMenuItem("PubMed");
					mi.setActionCommand("goto PubMed");
					otherDataSourcesMenu.add(mi);
					mi = new JMenuItem("NCI Thesaurus");
					mi.setActionCommand("goto NCI Thesaurus Advice");
					otherDataSourcesMenu.add(mi);  
					otherDataSourcesMenu.setOpaque(true);
					getPopupMenu().setOpaque(true);
					getPopupMenu().setLightWeightPopupEnabled(true);
			}
	}

	
	public static void main(String [] args)
	{
		Keyword t = new Keyword("TEST KEYWORD");
		t.update();
		oncotcap.Oncotcap.getDataSource().commit();
	}

		private void initGetterMap() {
				Method getter = null;
				getter = getGetter("getKeyword");
				getMethodMap.put("keyword", getter);
				getter = getGetter("getParents");
				getMethodMap.put("parentKeyword", getter);
				getter = getGetter("getDescribedInstances");
				getMethodMap.put("describedInstances", getter);
		// 		getter = getGetter("getSubordinateKeywords");
// 				getMethodMap.put("subordinateKeywords", getter);
		}
		private void initSetterMap() {
				Method setter = null;
				setter = getSetter("setKeyword", String.class);
				setMethodMap.put("keyword", setter);
				setter = getSetter("setParents", DefaultPersistibleList.class);
				setMethodMap.put("parentKeyword", setter);
				setter = getSetter("setDescribedInstances", DefaultPersistibleList.class);
				setMethodMap.put("describedInstances", setter);
//  				setter = getSetter("getSubordinateKeywords");
//  				getMethodMap.put("subordinateKeywords", setter);
		}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getKeyword() {
		return keyword;
	}

// 	public void setParentKeyword(Keyword keyword) {
// 		this.parentKeyword = keyword;
// 	}

 	public Keyword getParentKeyword() {
 		return (Keyword)parentKeywords.firstElement();
 	}

		public void setParents(java.util.Collection  var ){
		if ( parentKeywords == null)
			 parentKeywords = new DefaultPersistibleList();
		parentKeywords.set(var);
	}

		public void setPar(Keyword keyword) {
		if(! parentKeywords.contains(keyword))
			parentKeywords.add(keyword);
	}

	public void addChild(Keyword keyword) {
		if(! children.contains(keyword))
			children.add(keyword);
	}

	public Vector getChildren() {
		return(children);
	}

	public DefaultPersistibleList getDescribedInstances() {
	// 		System.out.println("described instance" 
// 												 + describedInstances);
		return(describedInstances);
	}
		public void setDescribedInstances(java.util.Collection  var ){
				if ( describedInstances== null)
						describedInstances = new DefaultPersistibleList();
				describedInstances.set(var);
		}
		public void addDescribedInstance(Object  var ){
				if ( describedInstances== null)
						describedInstances = new DefaultPersistibleList();
				if ( !describedInstances.contains(var) ) 
						describedInstances.addElement(var);
	// 			System.out.println("add described instance" 
// 												 + describedInstances);
		}
		public void removeDescribedInstance(Object  var ){
// 				System.out.println("remove described instance" 
// 												 + describedInstances);
				if ( describedInstances != null)
						describedInstances.removeElement(var);
		}
// 	public void addSubordinateKeyword(Keyword keyword) {
// 			//			System.out.println("addSubordinateKeyword " + keyword);
// 		if ( subordinateKeywords == null) {
// 			subordinateKeywords = new Vector();
// 		}
// 		subordinateKeywords.addElement(keyword);
// 	}

// 	public Vector getSubordinateKeywords() {
// 		return subordinateKeywords;
// 	}

	public void addSubordinateKeyword(Keyword keyword)
	{
		addChild(keyword);
	}

	public Vector getSubordinateKeywords()
	{
		return(getChildren());
	}
	
	public Iterator getChildrenIterator()
	{
		return(children.iterator());
	}

	public Iterator getParentIterator()
	{
		return(parentKeywords.iterator());
	}

	public DefaultPersistibleList getParents(){
		return parentKeywords;
	}

	public boolean isChildOf(Keyword word)
	{
		Iterator parents = getParentIterator();
		while ( parents.hasNext() ) {
				Keyword par = (Keyword)parents.next();
				if(par == null)
						return(false);
				else if(par.equals(word))
						return(true);
				else
						return(par.isChildOf(word));
		}
		return false;
	}
	public String toString() {
		return(keyword);
//		return "The keyword is " + keyword + " the parent is " + parentKeyword;
	}
		
	public Collection getConnectedNodes() {
		Vector connectedNodes = new Vector();
		connectedNodes.addElement(Keyword.class);
		return connectedNodes;
	}

	public Vector getAllChildren() { 
			return getChildren(this, new Vector());
	}

	private Vector getChildren(Keyword keyword, Vector allKids) {
			allKids.addElement(keyword);
// 			System.out.println("Addtoallkids " + keyword + "  " + allKids );
			Vector children = keyword.getChildren();
			if ( children != null ) {
					Iterator i = children.iterator();
					while ( i.hasNext() ) {
							Keyword child = (Keyword)i.next();
							getChildren(child, allKids);
					}
			}
			return allKids;
	}

Hashtable getTreeInstances(Keyword rootKeyword) {
				// Get all the Keywords the current 'context' from the data source
				// and return a vector of them
		Hashtable keywordHashtable = new Hashtable();
		addKeyword(keywordHashtable, rootKeyword, null);
		return keywordHashtable;
	}
	private void addKeyword(Hashtable table, Keyword word, Keyword parent)
	{
		boolean recurse = true;

		if(table.containsKey(word))
			recurse = false;

		if(parent == null)
			table.put(word, String.valueOf(-1));
		else
			table.put(word, parent);

		if(recurse)
		{
			Iterator it = word.getChildren().iterator();
			while(it.hasNext())
			{
				addKeyword(table, (Keyword) it.next(), word);
			}
		}
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
	 **
	 ** @returns	The list of supported DataFlavors.
	 **
	 ** @author	shirey July 9, 2003
	 **/
	public DataFlavor[] getTransferDataFlavors()
	{
		return(flavors);
	}
	/**
	 ** isDataFlavorSupported	Required for the Droppable interface.
	 **								Used to query this object to find out if
	 **								a particular DataFlavor is supported.
	 **								Only Droppable.droppableData and
	 **								DataFlavor.stringFlavor are supported.
	 **
	 ** @param	DataFlavor	The DataFlavor to compare to the supported
	 **							flavors.
	 **
	 ** @returns	true if the flavor is supported, false if it isn't.
	 ** @author	shirey July 9, 2003
	 **/
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		for(int n = 0; n < flavors.length; n++)
			if(flavors[n] == flavor)
				return(true);

		return(false);
	}

		public boolean dropOn(Object dropOnObject) {
				if ( dropOnObject instanceof Keyword ) {
						Keyword parentKeyword = null;
						Keyword newParentKeyword = (Keyword)dropOnObject;
						// Add child to new parent
						newParentKeyword.addChild(this);
						// Add new parent to child
						setPar(newParentKeyword);
						update();
						newParentKeyword.update();		
						return true;
				}
				else if ( dropOnObject instanceof PersistibleWithKeywords) {
// 						System.out.println("droping a keyword on " + dropOnObject);
						// THis seems to cause a duplicate entry 
						addDescribedInstance((PersistibleWithKeywords)dropOnObject);
						update();

						((PersistibleWithKeywords)dropOnObject).link(this);
						((PersistibleWithKeywords)dropOnObject).update();
						return true;
				}
				return false;
		}

	 	public void unlink(oncotcap.datalayer.Persistible relatedPersistible) {
				if ( relatedPersistible instanceof Keyword ) {
// 						System.out.println("removing the keyword p/c" + relatedPersistible);
						if ( children != null )
								children.remove(relatedPersistible);
						if ( parentKeywords != null ) 
								parentKeywords.remove(relatedPersistible);
				}
				else {
	// 					System.out.println("removing the keyword described instance" 
// 															 + relatedPersistible
// 															 + " from list " + describedInstances);
						if ( describedInstances != null ) 
								describedInstances.remove(relatedPersistible);
				}
		}
		public EditorPanel getEditorPanel() { 
				EditorPanel thePanel = null;
				try { 
						thePanel = new KeywordPanel();
						thePanel.edit(this);
				}	catch ( Exception ea ) {
						ea.printStackTrace();
				}
				return(thePanel);
		}

		public EditorPanel getEditorPanelWithInstance() { 
				EditorPanel thePanel = null;
				try { 
						thePanel = new KeywordPanel();
						thePanel.edit(this);
				}	catch ( Exception ea ) {
						ea.printStackTrace();
				}
				return(thePanel);
		}

		public ImageIcon getIcon() {
				return icon;
		}

		public Method getSetter(String name, Class argName) {
				Class [] paramList = new Class[1];
				Method setMethod = null;
				try {
				
					// 	System.out.println("setter " + name + "  " 
// 															 + paramList + " " + argName);
						if ( argName == DefaultPersistibleList.class ) 
								argName = Collection.class;
					// 	System.out.println("setter " + name + "  " 
// 															 + paramList + " " + argName);
						paramList[0] = argName;
						setMethod = 
								this.getClass().getMethod(name,
																					paramList);
				}
				catch ( Exception ea ) {
						ea.printStackTrace();
				}
				return setMethod;
		}

		public Hashtable getGetterMap() {
				return getMethodMap;
		}
		public Hashtable getSetterMap() {
				return setMethodMap;
		}

		public Method getGetter(String name) {
				Method getMethod = null;
				try {
						getMethod = 
								this.getClass().getMethod(name,
																					(Class [])null);
				}
				catch ( Exception ea ) {
						ea.printStackTrace();
				}
				return getMethod;
		}

		public Method getGetMethod(String name) {
				if ( name != null ) 
						return (Method)getMethodMap.get(name);
				else 
						return null;
		}


		public static Vector extractKeywordFromText(String text) {
				// see if there are any existing keywords represented 
				// in this text
				// Get all keywords
				
				Vector extractedKeywords = new Vector();
				Iterator keywords = allKeywords.iterator();
				while( keywords.hasNext() ) {
						Keyword keyword = (Keyword)keywords.next();
						if ( text != null && keyword.toString() != null &&
								 text.toLowerCase().indexOf(
								 keyword.toString().toLowerCase()) > -1 ) {
								extractedKeywords.addElement(keyword);
						}
				}
				return extractedKeywords;
		}
		public Vector getAssociatedLevelLists() {
				Vector levelLists = new Vector();
				Vector endPts = CollectionHelper.makeVector("Keyword");
				Hashtable keywordParents = 
						oncotcap.Oncotcap.getDataSource().getParentTree
						("Keyword",
						 endPts,
						 CollectionHelper.makeVector(this),
						 TreeDisplayModePanel.ROOT );
				// Take each of the parents and get the associated level lists
				if (keywordParents.size() <= 0)
						return levelLists;
				Hashtable levelListsHashtable = 
						oncotcap.Oncotcap.getDataSource().getInstanceTree
						("EnumLevelList",
						 new Vector(),
						 makeLevelListFilter(keywordParents),
						 TreeDisplayModePanel.ROOT);
				for(Enumeration e = keywordParents.keys(); e.hasMoreElements();){
					System.out.println("Keyword.getAssociatedLevelLists: " + e.nextElement());
				}
				// Collect all the level lists from the hashtable
				Vector selectedItems = CollectionHelper.makeVector(keyword);
				for (Enumeration e = levelListsHashtable.keys();
						 e.hasMoreElements(); ) {
						Object obj = e.nextElement();
						if ( obj instanceof EnumLevelList ) {
								levelLists.addElement(obj);
						}										
				}
				return levelLists;
		}
		private OncFilter makeLevelListFilter(Hashtable keywords) {
				// Create a filter
				OncFilter filter = new OncFilter(false);	
				OncTreeNode rootNode = filter.getRootNode();
				OncTreeNode orNode = new OncTreeNode(TcapLogicalOperator.OR, 
										 Persistible.DO_NOT_SAVE);
				OncTreeNode keywordNode;
				for (Enumeration e = keywords.keys();
						 e.hasMoreElements(); ) {
						Object obj = e.nextElement();
						if ( obj instanceof Keyword )
								orNode.add(new OncTreeNode(obj, 
									 (Persistible.DO_NOT_SAVE)));
				}
				rootNode.add(orNode);
				return filter;
				
		}
		public static EnumDefinition 
				dropKeywordCreateEnum(EnumDefinition enumDefinition, 
															Keyword keyword, boolean saveEnum) {
				if ( enumDefinition == null ) {
						enumDefinition = 
								new EnumDefinition(keyword.toString(), 
																	 saveEnum);
				}
				enumDefinition.setKeyword(keyword);
				return enumDefinition;
		}
		public static EnumDefinition dropKeywordCreateEnum(Keyword keyword, boolean saveEnum) {
				EnumDefinition enumDefinition = 
						new EnumDefinition(keyword.toString(), 
															 saveEnum);
				enumDefinition.setKeyword(keyword);
				return enumDefinition;
		}
}	 

