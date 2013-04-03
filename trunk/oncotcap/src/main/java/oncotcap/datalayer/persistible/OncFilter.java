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

public class OncFilter extends AbstractDroppableWithKeywords 
		implements Editable
{
		public static TcapString rootFilterObj = null;
		static {
				TcapString filter = (TcapString)Oncotcap.getDataSource().find
						(new GUID("888e669800000001000000fc3b6600ec")); //FILTER
				if ( filter == null )
						rootFilterObj = new TcapString("FILTER");
				else 
						rootFilterObj= filter;
		}
	public static final DataFlavor filterFlavor = 
			new DataFlavor(Droppable.class, "Oncotcap Filter");
	private static DataFlavor [] myFlavors = 
	{ Droppable.droppableData, DataFlavor.stringFlavor, filterFlavor };
	public OncTreeNode rootNode;
	public boolean keywordsOnly = false;
	private Hashtable stagedObjects = new Hashtable();
	static {
			try {
					// Make the most props property transient so they won't show in
					// XML file
					BeanInfo info = Introspector.getBeanInfo(OncFilter.class);
					PropertyDescriptor[] propertyDescriptors = 
							info.getPropertyDescriptors();
					for (int i = 0; i < propertyDescriptors.length; ++i) {
							PropertyDescriptor pd = propertyDescriptors[i];
							if (!pd.getName().equals("GUID") 
									&& !pd.getName().equals("rootNode")) {
									pd.setValue("transient", Boolean.TRUE);
							}
					}
			} catch (IntrospectionException e) {
					System.out.println("ERROR: OncFilter Introspection exception");
			}
	}

	public OncFilter(oncotcap.util.GUID guid){
		super(guid);
		flavors = myFlavors;
	}
	
	public OncFilter()
	{
		this(true);
	}
	public OncFilter(boolean saveToDataSource)
	{
		if(!saveToDataSource)
		{
			setPersistibleState(Persistible.DO_NOT_SAVE);
			rootNode = new OncTreeNode(OncFilter.rootFilterObj, Persistible.DO_NOT_SAVE);
		}
		else
			rootNode = new OncTreeNode(OncFilter.rootFilterObj);
		flavors = myFlavors;
	}
	public OncTreeNode getRootNode()
	{
		return(rootNode);
	}
	public void addStagedObject(Persistible obj)
	{
		if(obj instanceof Droppable && ! obj.equals(this))
			stagedObjects.put(obj.getGUID(), obj);
	}
	public Collection getStagedObjects()
	{
		return(stagedObjects.values());
	}
	public void setRootNode(OncTreeNode rootNode)
	{
		this.rootNode = rootNode;	
		update();
	}
	public EditorPanel getEditorPanelWithInstance()
	{
		return(new FilterEditorPanel(this));
	}
	public EditorPanel getEditorPanel()
	{
		return(new FilterEditorPanel());
	}
	public boolean getKeywordsOnly()
	{
		return(keywordsOnly);
	}
	public void setKeywordsOnly(boolean keywordsOnly)
	{
		this.keywordsOnly = keywordsOnly;
		update();
	}
	public Object clone()
	{
		return(clone(true));
	}
	public OncFilter clone(boolean saveToDataSource)
	{
		OncFilter cFilter = new OncFilter(saveToDataSource);
		cFilter.rootNode = (OncTreeNode) rootNode.clone();
		cFilter.keywordsOnly = keywordsOnly;
		return(cFilter);		
	}
	public boolean match(Persistible candidate)
	{
		if(rootNode.getChildCount() < 1)
			return(false);
		else
		{
			Iterator it = rootNode.getChildren();
			Object firstNode = it.next();
			if(firstNode instanceof OncTreeNode)
			{
				return(match(candidate, (OncTreeNode) firstNode));
			}
			else
				return(false);
		}
	}
	private static boolean match(Persistible candidate, OncTreeNode node)
	{
		Object matchAgainst = node.getUserObject();
		if(matchAgainst instanceof TcapLogicalOperator)
			return(match(candidate, (TcapLogicalOperator) matchAgainst, node.getChildren()));
		else if(matchAgainst instanceof SubsetClass)
			return(((SubsetClass) matchAgainst).instanceOf(candidate));
		else if(matchAgainst instanceof Persistible)
			return(((Persistible) matchAgainst).connectedTo(candidate));
		else
			return(false);
	}
	private static boolean match(Persistible candidate, TcapLogicalOperator op, Iterator children)
	{
		if(op.equals(TcapLogicalOperator.AND))
		{
			if(! children.hasNext())
				return(false);
			
			while(children.hasNext())
			{
				if(! match(candidate, children.next()) )
					  return(false);
			}
			return(true);
		}//end if AND
		else if(op.equals(TcapLogicalOperator.OR))
		{
			while(children.hasNext())
			{
				if(match(candidate, children.next()))
					return(true);
			}
			return(false);
		} //end if OR
		else if(op.equals(TcapLogicalOperator.NOT))
		{
			if(children.hasNext())
			{
				return(! match(candidate, children.next()));
			}
			else
				return(true);
		} //end if NOT
		else
			return(false);
	}
	private static boolean match(Persistible candidate, Object possibleNode)
	{
		if(possibleNode instanceof OncTreeNode)
			return(match(candidate, (OncTreeNode) possibleNode));
		else
			return(false);
	}
	public static Collection resultSet(Collection coll, OncFilter filter)
	{
		Vector rColl = new Vector();
		Iterator it = coll.iterator();
		while(it.hasNext())
		{
			Object obj = it.next();
			if(obj instanceof Persistible)
			{
				if(filter.match((Persistible) obj))
					  rColl.add(obj);
			}
		}
		return(rColl);
	}
	public static Collection resultSet(OncFilter filter)
	{
		OncoTCapDataSource dataSource = Oncotcap.getDataSource();
		Class sbClass = ReflectionHelper.classForName("oncotcap.datalayer.Persistible");
		Collection persistibles = dataSource.find(sbClass);
		if(persistibles == null)
			return(new Vector());
		else
			return(resultSet(persistibles, filter));
	}

	//this is an and . . .
	public static Collection resultSet(OncFilter filter1, OncFilter filter2)
	{
		return(resultSet(resultSet(filter1), filter2));
	}

	public boolean isEmpty() {
			if (getRootNode().getChildCount() > 0 ) 
					return false;
			else
					return true;
	}

	public String toString() {
				return getClass() + " " + rootNode +
						"  name " + getDisplayName();
	}
		
		public String getDisplayName() {
				// Use the filter to construct a name
				return addToName().toString();
		}
		public StringBuffer addToName() {
				// Visit each node 
				return addToName(rootNode, new StringBuffer(), " ");
		}

		Vector nodesToRemove = new Vector();
		public boolean hasSameValuesAs(Object obj) {
			 
				if ( !(obj instanceof OncFilter) ) {
						//System.out.println("not a filter" + obj.getClass());
						return false;
				}
				else {
						// First copy the trees so you don't wreck the originals
						OncTreeNode copyOfThisRootNode = (OncTreeNode)rootNode.clone(false);
						OncTreeNode copyOfTheOtherRootNode = 
								(OncTreeNode)(((OncFilter)obj).getRootNode()).clone(false);
				
						// Sort the trees so they can be compared
						sort(copyOfThisRootNode);
						sort(copyOfTheOtherRootNode);
						// Get rid of extra nodes - can't do it while sorting because it
						// messes up enumerator
						for ( int k = 0; k < nodesToRemove.size(); k++) {
								DefaultMutableTreeNode removeNode = 
										(DefaultMutableTreeNode)nodesToRemove.elementAt(k);
								removeNode.removeFromParent();
						}
						// now spin through the nodes and see if they are equal 
						// if you hit a point where they are not equal get out
						Enumeration e2=
								copyOfTheOtherRootNode.depthFirstEnumeration();
						for (Enumeration e1=copyOfThisRootNode.depthFirstEnumeration(); 
								 e1.hasMoreElements(); ) {
								DefaultMutableTreeNode thisNode = 
										(DefaultMutableTreeNode)e1.nextElement();		
								DefaultMutableTreeNode compareNode = 
										(DefaultMutableTreeNode)e2.nextElement();
								Object thisObj = thisNode.getUserObject();
								Object compareObj = compareNode.getUserObject();
								if ( thisObj instanceof AbstractPersistible 
										 && compareObj instanceof AbstractPersistible) {
										// 					System.out.println("compare abstract pers "
										// 																			 + thisObj
										// 																			 + " " 
										// 																			 + compareObj);
										
										if ( (thisObj != null && compareObj != null
													&& !((AbstractPersistible)thisObj).hasSameValuesAs((AbstractPersistible)compareObj))
												 || (thisObj == null && compareObj != null) ) {
												return false;
										}
								}
						}
						if ( e2.hasMoreElements()) {
								return false;
						}
				}
				return true;
		}

		BrowserNodeComparator comparator = new BrowserNodeComparator();

		public void sort(DefaultMutableTreeNode node) {
        // sort the roots children
       Object[] objs = new Object[node.getChildCount()];
			 mergeLikeChildren(node);
       Enumeration children = node.children();
       for (int i=0;children.hasMoreElements();i++) {

            DefaultMutableTreeNode child = 
								(DefaultMutableTreeNode) children.nextElement();
            objs[i] = child;
       }
			 Arrays.sort(objs, comparator);
       node.removeAllChildren();
       
       // insert newly ordered children
       for (int i=0;i<objs.length;i++) {
					 DefaultMutableTreeNode orderedNode = 
							 (DefaultMutableTreeNode) objs[i];
            node.add(orderedNode);
         if (!orderedNode.isLeaf()) {
          sort(orderedNode);
         }
       }
		}
		private void mergeLikeChildren(DefaultMutableTreeNode node) {
				Enumeration children = node.children();
				Hashtable childrenUserObjects = new Hashtable();
				for (int i=0;children.hasMoreElements();i++) {
            DefaultMutableTreeNode child = 
								(DefaultMutableTreeNode) children.nextElement();
						Object childNode = 
								childrenUserObjects.get(child.getUserObject());
						if ( childNode != null) {
								// If this child user object already exists at this level
								// put its children on the existing node
								Enumeration grandChildren = child.children();
								for (int j=0;grandChildren.hasMoreElements();j++) {
										DefaultMutableTreeNode grandChild = 
												(DefaultMutableTreeNode) grandChildren.nextElement();
										((DefaultMutableTreeNode)childNode).add(grandChild);
								}
								nodesToRemove.add(child);
						}
						else {
								childrenUserObjects.put(child.getUserObject(), child);
						}
       }
				
		}

		public StringBuffer addToName(OncTreeNode node, StringBuffer nameString, 
													 String separator) {
				if ( node.getUserObject() instanceof TcapLogicalOperator ) {
						nameString.append("(");
						separator = " " + node.getUserObject().toString() + " ";
						if ( node.getUserObject() == TcapLogicalOperator.NOT)
								nameString.append(separator);
				}
				else {
						if ( node.getUserObject() instanceof BooleanExpression ) {
								BooleanExpression bool = 
										(BooleanExpression)node.getUserObject();
								nameString.append(bool.toString());
						}
						else if ( node.getUserObject() instanceof SearchText ) {
								nameString.append(node.getUserObject().toString());
						}
						// else {
						// 		nameString.append(node.getUserObject().toString());
						// }
					
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

		public OncFilter and(OncFilter filter)
		{
			return(and(this, filter));
		}
	public static OncFilter and(OncFilter filter1, OncFilter filter2)
	{
		OncTreeNode topNode1 = null, topNode2 = null;
		OncFilter rFilter = new OncFilter();
		OncTreeNode andNode = new OncTreeNode(TcapLogicalOperator.AND);
		rFilter.getRootNode().addChild(andNode);
		try{topNode1 = (OncTreeNode) filter1.getRootNode().getFirstChild();}
		catch(NoSuchElementException e){}
		try{topNode2 = (OncTreeNode) filter2.getRootNode().getFirstChild();}
		catch(NoSuchElementException e){}
			
		conditionallyAddChildren(andNode, topNode1);
		conditionallyAddChildren(andNode, topNode2);

		return(rFilter);
	}
	//this method is used ONLY by the and and or implementations here.  the second
	//if clause will break if used for any other purpose.
	private static void conditionallyAddChildren(OncTreeNode parentNode, OncTreeNode children)
	{
		if(parentNode != null && children != null)
		{
			if(children.getUserObject().equals(parentNode.getUserObject()))
			{
				Iterator it = children.getChildren();
				while(it.hasNext())
					parentNode.addChild((OncTreeNode) ((OncTreeNode) it.next()).clone());
			}
			else
				parentNode.addChild((OncTreeNode) children.clone());
		}	
	}

	public OncFilter or(OncFilter filter)
	{
		return(or(this, filter));
	}
	public static OncFilter or(OncFilter filter1, OncFilter filter2)
	{
		OncTreeNode topNode1 = null, topNode2 = null;
		OncFilter rFilter = new OncFilter();
		OncTreeNode orNode = new OncTreeNode(TcapLogicalOperator.OR);
		rFilter.getRootNode().addChild(orNode);
		try{topNode1 = (OncTreeNode) filter1.getRootNode().getFirstChild();}
		catch(NoSuchElementException e){}
		try{topNode2 = (OncTreeNode) filter2.getRootNode().getFirstChild();}
		catch(NoSuchElementException e){}
			
		conditionallyAddChildren(orNode, topNode1);
		conditionallyAddChildren(orNode, topNode2);

		return(rFilter);
	}
	public static OncFilter makeTransientFilter(Object filterNode) {
		// Create a filter
		OncFilter filter = new OncFilter(false);	
		OncTreeNode rootNode = filter.getRootNode();
		OncTreeNode andNode = new OncTreeNode(TcapLogicalOperator.AND, 
				 Persistible.DO_NOT_SAVE);
		OncTreeNode node = new OncTreeNode(filterNode, 
				Persistible.DO_NOT_SAVE);
		andNode.add(node);
		rootNode.add(andNode);
		return filter;
	}
}

