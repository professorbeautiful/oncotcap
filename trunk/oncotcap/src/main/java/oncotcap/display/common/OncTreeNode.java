package oncotcap.display.common;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;
import java.awt.Color;
import java.lang.reflect.*;
import java.io.Serializable;


import javax.swing.ImageIcon;
import javax.swing.tree.MutableTreeNode;
import java.awt.datatransfer.*;

import oncotcap.Oncotcap;
import oncotcap.util.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.TcapString;
import oncotcap.display.editor.*;
import oncotcap.display.common.Droppable;

public class OncTreeNode extends DefaultMutableTreeNode 
		implements Persistible ,  Droppable, Serializable
{
	private static final DataFlavor [] flavors = 
	{Droppable.droppableData, Droppable.oncTreeNode,DataFlavor.stringFlavor};

	private EditorFrame currentEditor;
	private int state = Persistible.DO_NOT_SAVE; // stop the proliferation!
	private OncoTCapDataSource dataSource = null;

	//these fields should be exposed via abstract methods in persistible
	public GUID guid = null;
	private Class setSource = null;
	public String creator = null;
	public String modifier = null;
	public String creationTime = null;
	public String modificationTime = null;
	private Integer versionNumber;
	private String instanceUsabilityStatus;
	public Color fgColor = Color.black;

 
	private Persistible metaData = null;

	public OncTreeNode(GUID guid)
	{
		super(guid);
		//ForceStackTrace.showStackTrace();
	}
	public OncTreeNode()
	{
		super();
		guid = new GUID();
		//ForceStackTrace.showStackTrace();
	}
	public OncTreeNode(Object userObject)
	{
		super(userObject);
		guid = new GUID();
		//ForceStackTrace.showStackTrace();
	}

	public OncTreeNode(Object userObject, int state)
	{
		super(userObject);
		guid = new GUID();
		setPersistibleState(state);
		//ForceStackTrace.showStackTrace();
	}
	public OncTreeNode(Persistible userObject)
	{
		super(userObject);
		guid = new GUID();
		//ForceStackTrace.showStackTrace();
	}
	public OncTreeNode(Persistible userObject, int state)
	{
		super(userObject);
		guid = new GUID();
		setPersistibleState(state);
		//ForceStackTrace.showStackTrace();
	}
	public OncTreeNode(Object userObject, boolean allowsChildren)
	{
		super(userObject, allowsChildren);
		guid = new GUID();
		//ForceStackTrace.showStackTrace();
	}
	public OncTreeNode(Object userObject, boolean allowsChildren, int state)
	{
		super(userObject, allowsChildren);
		guid = new GUID();
		setPersistibleState(state);

		//ForceStackTrace.showStackTrace();
	}
	public Persistible getPersistibleObject()
	{
			if (getUserObject() instanceof Persistible)
					return((Persistible) getUserObject());
			else
					return null;
	}
	public void setUserObject(Object obj)
	{
			//if ( obj instanceof Persistible)
					
			super.setUserObject(obj);
	}
	public void setPersistibleObject(Persistible obj)
	{
		setUserObject(obj);
	}

	public OncoTCapDataSource getDataSource() {
				return dataSource;
	}
	public void setDataSource(OncoTCapDataSource ds) {
				dataSource = ds;
	}

	public Persistible getMetaData()
	{
		return metaData;
	}
	public void setMetaData(Persistible obj)
	{
			metaData = obj;
	}
	public Iterator getChildren()
	{
		return(new OncTreeIterator());
	}
	public void addChild(Object child, boolean saveChildNodeToDataSource)
	{
		if(child instanceof OncTreeNode)
			addChild((OncTreeNode) child);
		else
			addChild(new OncTreeNode(child, (saveChildNodeToDataSource ? Persistible.DIRTY : Persistible.DO_NOT_SAVE)));
	}
	public void addChild(OncTreeNode child)
	{
		insert(child, getChildCount());
	}
	public void addAllChildren(OncTreeNode node)
	{
		if(node == null)
			return;

		addAllChildren(node.getChildren());
	}
	public void addAllChildren(Collection children)
	{
		if(children == null)
			return;

		addAllChildren(children.iterator());
	}
	public void addAllChildren(Iterator it)
	{
		if(it == null)
			return;
		
		while(it.hasNext())
		{
			Object obj = it.next();
			if(obj instanceof OncTreeNode)
				addChild((OncTreeNode) obj);
		}
	}
	public void remove(MutableTreeNode aChild) {
			//oncotcap.util.ForceStackTrace.showStackTrace();
			super.remove(aChild);
			update();
	}
		public void insert(MutableTreeNode child, int index) {
				super.insert(child, index);
			// 	System.out.println("insert node into tree " + child + " at " + index + 
// 													 " into " + this +
// 													 " that now has " + this.getChildCount());
			update();
	}
	public EditorFrame getCurrentEditor()
	{
		return(currentEditor);
	}
	public void setCurrentEditor(EditorFrame editor)
	{
		currentEditor = editor;
	}
	private Vector saveHandlers = new Vector();
	public void addSaveListener(SaveListener handler)
	{
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
		SaveEvent e = new SaveEvent(this);
		Iterator it = saveHandlers.iterator();
		while(it.hasNext())
			((SaveListener) it.next()).objectSaved(e);
	}
	public void fireDeleteEvent()
	{
		SaveEvent e = new SaveEvent(this);
		Iterator it = saveHandlers.iterator();
		while(it.hasNext())
			((SaveListener) it.next()).objectDeleted(e);
	}
	
	/**
	 * Looks in this node and all nodes under it for the the specified
	 * user object. 
	 */
	public boolean containsUserObject(Object obj)
	{
		if(getUserObject().equals(obj))
			return(true);
		
		Iterator it = getChildren();
		while(it.hasNext())
		{
			Object nxt = it.next();
			if(nxt instanceof OncTreeNode)
			{
				OncTreeNode nxtNode = (OncTreeNode) nxt;
				if(nxtNode.containsUserObject(obj))
					return(true);
			}
			else if(nxt instanceof DefaultMutableTreeNode)
			{
				DefaultMutableTreeNode dmNode = (DefaultMutableTreeNode) nxt;
				if(containsUserObject(dmNode, obj))
					return(true);
			}
		}
		return(false);
	}
	private boolean containsUserObject(MutableTreeNode node, Object obj)
	{
		if(node instanceof DefaultMutableTreeNode)
			if(((DefaultMutableTreeNode)node).getUserObject().equals(obj))
				return(true);
		
		Enumeration childs = node.children();
		while(childs.hasMoreElements())
		{
			Object nxt = childs.nextElement();
			if(nxt instanceof MutableTreeNode)
				return(containsUserObject((MutableTreeNode) nxt, obj));
		}
		return(false);
	}
	private class OncTreeIterator implements Iterator
	{
		Enumeration enoom;
		private OncTreeIterator()
		{
			enoom = children();
		}
		public boolean hasNext()
		{
			return(enoom.hasMoreElements());
		}
		public Object next()
		{
			return(enoom.nextElement());
		}
		public void remove(){}
	}
	public String getClassName()
	{
		return(getClass().toString());
	}
	public String getPrettyName() {
			return toString();
	}
	public Object getCurrent()
	{
		return(this);
	}
	public GUID getGUID()
	{
		return(this.guid);
	}
	public void setGUID(GUID guid)
	{
			this.guid = guid;
			AbstractPersistible.putPersistible(this);
	}
	public void setGUID(String guid)
	{
		setGUID(GUID.fromString(guid));
	}
	/**
	 * Sets the GUID attribute of the Persistible object
	 *
	 * @param guid The new GUID value
	 */
		public void setGUID(GUID guid, OncoTCapDataSource ds) {
				this.guid = guid;
				AbstractPersistible.putPersistible(this, ds);
		}
		
	public void update()
	{
			if (getPersistibleState() == Persistible.DO_NOT_SAVE
					|| ((OncTreeNode)getRoot()).getPersistibleState()  
					== Persistible.DO_NOT_SAVE ) {
//					System.out.println("reseting state " );
					//setPersistibleState( Persistible.DO_NOT_SAVE);
					return;
			}
			if ( (getPersistibleState() > Persistible.UNSET) || 
					 (getPersistibleState() == Persistible.UNSET 
						&& getSetSource() == null))  {
					//					 && !rootNodeIsFilter() 
					Date date = new Date();
					if (getCreator() == null ) {
							// set creation info if not set yet
							setCreationTime(formatter.format(date));
							setCreator(SystemUtil.getSystemProperty("user.name"));
							
					}		
					setModificationTime(formatter.format(date));
					setModifier(SystemUtil.getSystemProperty("user.name"));
					Oncotcap.getDataSource().update(this);
					AbstractPersistible.putPersistible(this);
					// Call update on children
					for ( int i = 0; i < getChildCount(); i++) {
							Object obj = getChildAt(i);
							if ( obj instanceof OncTreeNode ) {
									((OncTreeNode)obj).update();
							}
					}
					fireSaveEvent();
			}
			else {
					//System.out.println("this is a unsavable item " + this);
			}
	}
		private boolean rootNodeIsFilter() {
				// Make sure this is not a part of a generic filter tree 
				// Prohibit proliferation of temporary data should make 
				// non persistible version of onctree node
				OncTreeNode rootNode = (OncTreeNode)getRoot();
				Object obj = rootNode.getUserObject();
				System.out.println("Is root Node Filter ");
				if ( obj instanceof TcapString && "FILTER".equals(obj.toString()) ) {
						System.out.println("Is root Node Filter " + true);
						 return true;
				}
				else {
						System.out.println("Is root Node Filter " + false);
						return false;
				}
		}
		public Class getSetSource() {
				return setSource;
		}
		public void setSetSource(Class cls) {
				setSource = cls;
		}

	public Object clone()
	{
		return(clone(true));
	}
	public Object clone(boolean saveToDataSource)
	{
		int persistibleState;
		if(saveToDataSource)
			persistibleState = Persistible.DIRTY;
		else
			persistibleState = Persistible.DO_NOT_SAVE;
		
		OncTreeNode newNode = new OncTreeNode(getUserObject(), getAllowsChildren(), persistibleState);
		for (int n = 0; n < getChildCount(); n++)
		{
			OncTreeNode child = (OncTreeNode) ((OncTreeNode) getChildAt(n)).clone(saveToDataSource);
			newNode.insert(child, n);
		}
		return(newNode);

	}

	/*public Object cloneSubstitute(StatementBundle sb)
	{
		int persistibleState;
		persistibleState = Persistible.DO_NOT_SAVE;

		Object newUserObj = ReflectionHelper.cloneSubstitute(getUserObject(), sb);
		
		OncTreeNode newNode = new OncTreeNode(newUserObj, getAllowsChildren(), persistibleState);
		for (int n = 0; n < getChildCount(); n++)
		{
			OncTreeNode child = (OncTreeNode) ((OncTreeNode) getChildAt(n)).cloneSubstitute(sb);
			newNode.insert(child, n);
		}
		return(newNode);

	}*/
	public boolean equals(Object obj)
	{
		if (obj instanceof Persistible) {
			return (guid.equals(((Persistible)obj).getGUID()));
		}
		else {
			return (false);
		}
	} 


	public int hashCode()
	{
		return(guid.hashCode());
	} 
	public Object toDataSourceFormat()
	{
		return(null);
	}
	public String toDisplayString()
	{
			return(toString());
	}
		public boolean link(Persistible relatedPersistible){return false;}
		public boolean connectedTo(Persistible p){return(false);}

		// Implement droppable
		public Object getTransferData(DataFlavor flavor) {
				
				if(flavor == DataFlavor.stringFlavor)
						return(toString());
				else if(flavor == Droppable.droppableData)
						return(getUserObject());
				else if(flavor == Droppable.oncTreeNode)
						return(this);
				else 
						return null;
		}
		
		public DataFlavor[] getTransferDataFlavors()
		{
				return(flavors);
		}
		
		public boolean isDataFlavorSupported(DataFlavor flavor)
		{
 			// 	System.out.println("flavor is " + flavor);
//  				System.out.println("flavor is " + Droppable.oncTreeNode);
				if(flavor.equals(DataFlavor.stringFlavor))
						return(true);
				else if(flavor.equals(Droppable.droppableData)
								&& getUserObject() != null 
								&& getUserObject() instanceof Droppable ) {
						if ( !flavor.getHumanPresentableName().equals
								 (Droppable.genericTreeNode.getHumanPresentableName())) {
								return(true); // temp fix
						}
						else
								return false;
				}
				else
					return(false);
		}
		
		public boolean dropOn(Object dropOnObject) {
				if ( dropOnObject instanceof String) {
						return false; // really if this is root node it can be dropped 
				}
				return false;
		}

		public void setCreator(String creator) {
				// Creator and creation time
				// 				System.out.println("creator: " + creator );
				if ( creator == null ) {
						this.creator = oncotcap.util.SystemUtil.getSystemProperty("user.name");
				}
		}
		public String getCreator() {
				return this.creator;
		}
		public String getInstanceUsabilityStatus(){
				return instanceUsabilityStatus;
		}
		public void setInstanceUsabilityStatus(String var){
				instanceUsabilityStatus = var;
		}
		private java.text.Format formatter = new java.text.SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		public void setCreationTime(String creationTime) {
				if ( creationTime == null ) {
						Date date = new Date();
						this.creationTime = formatter.format(date);
				}
		}
		public boolean showText() {
				return true;
		}
		public String getCreationTime() {
				return this.creationTime;
		}
		
		public void setModifier(String modifier) {
		this.modifier = modifier;
		}
		
		public String getModifier() {
				return this.modifier;
		}
		
		public void setModificationTime(String modificationTime) {
				this.modificationTime = modificationTime;
		}
		
		public String getModificationTime() {
				return modificationTime;
		}
		public Integer getVersionNumber() {
				return versionNumber;
		}
		public void setVersionNumber(Integer var ){
				versionNumber = var;
		}
		public void setPersistibleState(int state){
				this.state = state;
		}
		
		public int getPersistibleState() {
				return state;
		}
		// Delete the node and all subordinate nodes
		public void delete(boolean cascade) {
			// 	// Get all subordinate nodes
// 				Vector <GenericTreeNode> deleteNodes = new Vector <GenericTreeNode>();
// 				for (Enumeration e=rootNode.depthFirstEnumeration(); 
// 						 e.hasMoreElements(); ) {
// 						deleteNodes.add((GenericTreeNode)e.nextElement());		
// 				}
		}
		public void delete() {
				AbstractPersistible.removePersistible(this);
				Oncotcap.getDataSource().delete(this);
				setPersistibleState(Persistible.DELETED);
				fireDeleteEvent();
		}
		public ImageIcon getIcon() {
				return null;
		}
		public Color getForeground(){
				return fgColor;
		}
		public void setForeground(Color color){
				fgColor = color;
		}
		public String toString() {
				if ( getUserObject() != null ) 
						return getUserObject().toString();
				else 
						return "no user object";
		}
}
