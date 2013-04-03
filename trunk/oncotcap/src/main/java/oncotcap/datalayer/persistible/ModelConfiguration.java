package oncotcap.datalayer.persistible;

import java.util.*;
import oncotcap.datalayer.AbstractPersistible;
import oncotcap.datalayer.Persistible;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.datalayer.persistible.parameter.*;

public class ModelConfiguration extends AbstractDroppableWithKeywords implements Editable
{
	public static final TcapString rootConfigObj = new TcapString("VISIBLE STATEMENTS");
	private OncTreeNode rootNode = null;
	private Hashtable<StatementBundle, StatementBundleConfiguration> sbConfigurations = new Hashtable<StatementBundle, StatementBundleConfiguration>();
	private ModelController controller = null;
	
	public ModelConfiguration(oncotcap.util.GUID guid)
	{
		super(guid);
		init();
	}

	public ModelConfiguration()
	{
		init();
	}
	public ModelConfiguration(ModelController controller)
	{
		init();
		this.controller = controller;
		addStatementBundles(controller.getSubmodelGroups());
	}
	private void init()
	{
	}
	public void addStatementBundleConfiguration(StatementBundleConfiguration sbc)
	{
		if(sbc != null && sbc.getStatementBundle() != null)
		{
			sbConfigurations.put(sbc.getStatementBundle(), sbc);
			update();
		}
		else if(sbc != null && sbc.getStatementBundle() == null)
			sbc.delete();
	}
	public Hashtable getStatementBundleConfigurations()
	{
		return(sbConfigurations);
	}
	public void refresh(ModelController controller)
	{
		StatementBundle sb;
		Enumeration sbs = sbConfigurations.keys();
		while(sbs.hasMoreElements())
		{
			sb = (StatementBundle) sbs.nextElement();
			if(!controller.contains(sb))
			{
				StatementBundleConfiguration sbc2 = sbConfigurations.get(sb);
				sbc2.delete();
				sbConfigurations.remove(sb);
//				refreshNode(rootNode);
			}
		}
		if(this.controller == null)
			this.controller = controller;
		if(! this.controller.equals(controller))
		{
			clear();
			this.controller = controller;
		}
		addStatementBundles(controller.getSubmodelGroups());
		
		//walk tree of visible statements and remove any statement bundles
		//that no longer exist in this model.
		refreshNode(rootNode);
		update();
			
	}
	private void refreshNode(OncTreeNode node)
	{
		Iterator it = node.getChildren();
		while(it.hasNext())
		{
			Object obj = it.next();
			if(obj instanceof OncTreeNode)
			{
				OncTreeNode child = (OncTreeNode) obj;
				if(! nodeValid(child))
					node.remove(child);
				else
					refreshNode(child);
			}
		}
	}
	private boolean nodeValid(OncTreeNode node)
	{
		if(node.getUserObject() instanceof StatementBundleConfiguration)
		{
			StatementBundleConfiguration sbc = (StatementBundleConfiguration) node.getUserObject();
			if(sbc == null || sbc.getStatementBundle() == null)
				return(false);
			else
				return(sbConfigurations.containsKey(sbc.getStatementBundle()));
		}
		else
			return(true);
	}
	private void clear()
	{
		rootNode.delete();
		rootNode = null;
		sbConfigurations.clear();
	}
	private boolean contains(StatementBundle sb)
	{
		if(sbConfigurations.containsKey(sb))
			return(true);
		
		Enumeration bundles = sbConfigurations.keys();
		while(bundles.hasMoreElements())
		{
			StatementBundle sbTest = (StatementBundle) bundles.nextElement();
			if(sbTest.contains(sb))
				return(true);
		}
		
		return(false);
	}
	public void addStatementBundle(StatementBundle sb)
	{
		if(!sbConfigurations.containsKey(sb))
		{
			StatementBundleConfiguration sbc = new StatementBundleConfiguration(sb);
			sbc.setVisibility(true);
			sbConfigurations.put(sb, sbc);
			getRootNode().addChild(sbc, true);
		}
	}
	public void addStatementBundles(SubModel subMod)
	{
		addStatementBundles(subMod.getEncodingsInMe());
	}
	public void addStatementBundles(Encoding enc)
	{
		addStatementBundles(enc.getStatementBundlesImplementingMe());
	}
	public void addStatementBundles(Collection sbs)
	{
		Iterator it = sbs.iterator();
		while(it.hasNext())
		{
			Object sbObj = it.next();
			if(sbObj instanceof StatementBundle)
				addStatementBundle((StatementBundle) sbObj);
			else if(sbObj instanceof StatementBundleConfiguration)
			{
				System.out.println("WARNING: Trying to add a StatementBundle Configuration to " + this);
//				addStatementBundleConfiguration((StatementBundleConfiguration) sbObj);
			}
			else if(sbObj instanceof SubModelGroup)
				addStatementBundles((SubModelGroup) sbObj);
			else if(sbObj instanceof SubModel)
				addStatementBundles((SubModel) sbObj);
			else if(sbObj instanceof Encoding)
				addStatementBundles((Encoding) sbObj);
		}
	}
	public void addStatementBundles(SubModelGroup group)
	{
		addStatementBundles(group.getSubmodelsInGroup());
	}
	public Collection getConfigurations()
	{
		return(sbConfigurations.values());
	}
	public EditorPanel getEditorPanelWithInstance()
	{
		ModelConfigurationEditorPanel mcPanel = new ModelConfigurationEditorPanel();
		mcPanel.edit(this);
		return(mcPanel);
	}
	public EditorPanel getEditorPanel()
	{
		return(new ModelConfigurationEditorPanel());
	}
	public void setRootNode(OncTreeNode rootNode)
	{
		this.rootNode = rootNode;
	}
	public OncTreeNode getRootNode()
	{
		if(rootNode == null ||
			rootNode.getUserObject() == null ||
			! (rootNode.getUserObject() instanceof TcapString) ||
			! (((TcapString) rootNode.getUserObject()).getValue().equals("VISIBLE STATEMENTS")))
				rootNode = new OncTreeNode(rootConfigObj, Persistible.DIRTY);
			
		return(rootNode);
	}
	private int sortKey = 0;
	public void sortVisibleConfigurations()
	{
		sortKey = 0;
		if(rootNode != null)
			setSortOrder(rootNode);
	}
	private void setSortOrder(OncTreeNode node)
	{
		if(node.getUserObject() instanceof StatementBundleConfiguration)
			((StatementBundleConfiguration) node.getUserObject()).setSortKey(sortKey++);
			
		Iterator it = node.getChildren();
		while(it.hasNext())
		{
			Object nextNode = it.next();
			if(nextNode instanceof OncTreeNode)
				setSortOrder((OncTreeNode) nextNode);
		}
	}
	public void removeFromConfigurationList(StatementBundleConfiguration sbc)
	{
		this.sbConfigurations.remove(sbc.getStatementBundle());
	}
}