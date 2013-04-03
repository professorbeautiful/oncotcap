package oncotcap.datalayer.persistible;

import java.util.*;
import javax.swing.tree.*;

import oncotcap.engine.ModelDefinition;

public class StatementBundleTreeModel extends DefaultTreeModel
{
//	public Vector statementBundles = new Vector();
//	public ArrayList seperateDisplay = new ArrayList();
//	Project project;
//	String packageName;
//	String kbFileName;
//	private DefaultMutableTreeNode topNode;
//	OncInstance interventionTop;
	ModelDefinition model;
	
/*	public StatementBundleTreeModel(OncTreeModel ot)
	{
		this(ot.kbFileName, ot.packageName);
	} */
	public StatementBundleTreeModel(ModelDefinition model)
	{
		super(model.getController().getConfiguration().getRootNode());
		this.model = model;
//		this.project = project;
//		this.packageName = packageName;
//		initialize(project, packageName);
	}
	
	 public TreePath findObject(StatementBundleConfiguration object)
	 {
		 java.util.Enumeration nodes = ( (DefaultMutableTreeNode) getRoot()).preorderEnumeration();
		 while (nodes.hasMoreElements())
		 {
			 DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodes.nextElement();
			 if (node.getUserObject() == object)
			 {
				 return new TreePath(node.getPath());
			 }
		 }
		 return null;
	 }

/*	public StatementBundleTreeModel(String kbFileName, String packageName)
	{
		super(new DefaultMutableTreeNode("TEMP2"));
		Logger.log("NEW OncTreeModel file = " + kbFileName + " packageName = " + packageName + "Project = " + project);
		topNode = (DefaultMutableTreeNode) getRoot();
		Collection errors = new ArrayList();
		edu.stanford.smi.protege.model.Project project = new edu.stanford.smi.protege.model.Project(kbFileName, errors);
		if (errors.size() == 0)
		{
			Logger.log("In the if Loop");
			this.kbFileName = kbFileName;
			this.project = project;
			this.packageName = packageName;
			Logger.log("Project " + project);
			initialize(project, packageName);
		}
		else
			Logger.log("error opening KB!!!!!!!!!!!!!!!!!");
	}
*/
/*	void initialize(edu.stanford.smi.protege.model.Project project, String packageName) 
	{
		lookForPackage(packageName, "StatementBundlePackage");
		Logger.log("Came in StatementBundlePackage");
		lookForPackage(packageName, "InterventionNode");
		Logger.log("Came in InterventionNode");
		lookForPackage(packageName, "Cancer Models");
		Logger.log("Came in Cancer Models");
		lookForPackage(packageName, "Question");
		Logger.log("Came in Question");
		//lookForPackage(packageName, "Question");
	}
*/
/*	void lookForPackage(String packageName, String packageType)
	{
		try {
			Logger.log("PackageName"+packageName);
			Logger.log("Came in Loop");
			edu.stanford.smi.protege.model.KnowledgeBase kb = project.getKnowledgeBase();
			Logger.log("After KnowledgeBase");
			edu.stanford.smi.protege.model.Cls cls = kb.getCls(packageType);
			Iterator k = cls.getInstances().iterator();
			OncInstance pkg = null;
			int i = 0;
			while(k.hasNext()) {
				pkg = new OncInstance((edu.stanford.smi.protege.model.Instance) (k.next()) );
				//Logger.log("name = " + pkg.instance().getBrowserText());
				//Logger.log(i++);
				if (pkg.getString(":NAME").equalsIgnoreCase(packageName)) {
					Logger.log("These are the printed values"+pkg.getString(":NAME"));
					interventionTop = pkg;
					topNode = new DefaultMutableTreeNode(pkg.getString(":NAME"));
					Logger.log("Found the statementBundlePackage, " + packageName);
					fillTree(pkg);
					break;
				}
			}
		}
		catch ( java.lang.NullPointerException e) {
			//Logger.log("Couldnt deal with cls " + packageType);
		}
	}
*/
/*	public void reload()
	{
		instructorCheatTopNodes.clear();
//		initialize(project, packageName);
		super.reload();
	}
	*/
	private void fillTree(ModelDefinition model)
	{
		this.model = model;
		setRoot(model.getController().getConfiguration().getRootNode());
		reload();
//		getChildren(top, controller.getRootNode(), true);	
		//Logger.log("Size of instructorCheatTopNodes is " + instructorCheatTopNodes.size());
	}

	Vector instructorCheatTopNodes = new Vector();

	public void addInstructorCheatsToTree() {
		Iterator iter = instructorCheatTopNodes.iterator();
		for (; iter.hasNext();) {
			DefaultMutableTreeNode nextNode = (DefaultMutableTreeNode)iter.next();
//			topNode.add(nextNode);
			//Logger.log(".... userObject is " + nextNode.getUserObject());
		}
//		nodeStructureChanged(topNode);
	}

	public void removeInstructorCheatsFromTree() {
		Iterator iter = instructorCheatTopNodes.iterator();
//		for (; iter.hasNext();)
//			topNode.remove((MutableTreeNode)iter.next());
//		nodeStructureChanged(topNode);
	}

/*	private void getChildren(OncInstance kbParent, DefaultMutableTreeNode treeParent,
							 boolean addToTreeIfCalledFor)
	{
		OncInstance nextChild;
		DefaultMutableTreeNode nextNode;
		getStatementBundles(kbParent, treeParent, addToTreeIfCalledFor);
		Iterator children = kbParent.getDirectSubClasses().iterator();
		while(children.hasNext())
		{
			nextChild = new OncInstance((Instance) children.next());
			Logger.log(nextChild);
			String strInclude = nextChild.getString("IncludeInInterface");
			boolean includeSaysYes = strInclude.equalsIgnoreCase("yes");
			boolean includeSaysICheat = strInclude.equalsIgnoreCase("instructor cheat toggle key");
			boolean includeSaysSeperate = strInclude.equalsIgnoreCase("seperate window");
			nextNode = new DefaultMutableTreeNode(nextChild.getString(":NAME"));
			if(includeSaysSeperate || ! addToTreeIfCalledFor) {
				//Logger.log("..hiding permanently");
				getChildren(new OncInstance(nextChild), treeParent, false);
			}
			else if(includeSaysYes) {
				//Logger.log("..adding");
				treeParent.add(nextNode);
				getChildren(new OncInstance(nextChild), nextNode, true);
			}
			else if (includeSaysICheat) {
				if (treeParent==topNode) {
					instructorCheatTopNodes.add(nextNode);
					//Logger.log("..hiding-- cheat TopNode");
					//Logger.log(".... userObject is " + nextNode.getUserObject());
				}
				else {
					//Logger.log("..hiding under a cheat TopNode");
					treeParent.add(nextNode);
				}
				getChildren(new OncInstance(nextChild), nextNode, true);
			}
			else if (!strInclude.equalsIgnoreCase("no")) {
				Logger.log(".??? " + strInclude + " " +
								   nextChild.instance().getBrowserText());
			}
		}
	}
*/
/*	private void getStatementBundles(OncInstance kbParent, DefaultMutableTreeNode treeParent, boolean addToTreeIfCalledFor)
	{
		Iterator bundles = kbParent.getStatementBundles().iterator();
		while(bundles.hasNext())
		{
			StatementBundle sb = new StatementBundle( (Instance) bundles.next() );
			statementBundles.add(sb);
			String strInclude = sb.getString("IncludeInInterface");
			boolean includeSaysYes = strInclude.equalsIgnoreCase("yes");
			boolean includeSaysICheat = strInclude.equalsIgnoreCase("instructor cheat toggle key");
			boolean includeSaysSeperate = strInclude.equalsIgnoreCase("Seperate Window");
			if(includeSaysYes || includeSaysICheat)
				treeParent.add(new DefaultMutableTreeNode(sb));
			else if(includeSaysSeperate)
			{
				//Logger.log("added sb to seperateDisplay");
				seperateDisplay.add(sb);
				//Logger.log(seperateDisplay.size());
			}
		}
	}

	public boolean isComplete()
	{
		Iterator iter = statementBundles.iterator();
		StatementBundle sb;
		while (iter.hasNext())
		{
			sb = (StatementBundle) iter.next();
			if (sb.include().booleanValue())
			{
				if(sb.required().booleanValue())
				{
					if(!sb.isComplete()) {
						Logger.log("CAN'T RUN (1): the culprit is..." +
										   sb.instance().getBrowserText( ));
						return(false);
					}
				}
				else if(sb.anyShown() && ! sb.isComplete())
				{
					Logger.log("CAN'T RUN (2): the culprit is..." +
									   sb.instance().getBrowserText());
					return(false);
				}
			}
				
		}
		return(true);
	}
*/
}