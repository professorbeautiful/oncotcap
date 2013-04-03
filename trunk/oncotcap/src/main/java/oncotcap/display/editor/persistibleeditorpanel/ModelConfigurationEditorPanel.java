package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.Array;
import oncotcap.Oncotcap;
import oncotcap.util.*;
import oncotcap.datalayer.Persistible;
import oncotcap.display.browser.GenericTreeNode;
import oncotcap.display.browser.GenericTreeNodeRenderer;
import oncotcap.display.common.Droppable;
import oncotcap.display.common.InputDialog;
import oncotcap.display.common.OncList;
import oncotcap.display.common.OncTreeNode;
import oncotcap.display.editor.EditorFrame;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.datalayer.autogenpersistible.*;

import javax.swing.tree.*;

public class ModelConfigurationEditorPanel extends EditorPanel
{
	ModelConfiguration configuration;

	public static void main(String [] args)
	{
		SubModelGroup smg = (SubModelGroup) Oncotcap.getDataSource().find(new GUID("888e66a300000814000000f85fa4d3e6"));
		ModelConfiguration mc = new ModelConfiguration();
		mc.addStatementBundles(smg);
		EditorFrame.showEditor(mc);	
	}
	public void refresh()
	{
		edit(configuration);
	}
	public void edit(Object objectToEdit)
	{
		if(objectToEdit instanceof ModelConfiguration)
			edit((ModelConfiguration) objectToEdit);
	}
	public void edit(ModelConfiguration mc)
	{
		configuration = mc;

		rootNode = configuration.getRootNode();
		((DefaultTreeModel)exprTree.getModel()).setRoot(rootNode);
		((DefaultTreeModel)exprTree.getModel()).reload();
		exprTree.revalidate();

		sbList.clear();
		Iterator it = mc.getConfigurations().iterator();
				
		StatementBundleConfiguration sbc;
		while(it.hasNext())
		{
			sbc = (StatementBundleConfiguration) it.next();
			if(!sbc.isVisible())
				sbList.add(sbc);
		}
		repaint();
	}
	public void save()
	{
		if(configuration == null)
			configuration = new ModelConfiguration();
		rootNode.setPersistibleState(Persistible.DIRTY);
		rootNode.update();
		configuration.setRootNode(rootNode);
		configuration.update();
		
	}	
	public Object getValue()
	{
		return(configuration);
	}

	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


	protected JTree exprTree;
	protected OncTreeNode rootNode;
	protected DefaultTreeModel model;
	private OncList sbList;
	private Droppable droppedItem = null;

	//the following used by TreeDropTransferHandler 
	public static DataFlavor NODE_FLAVOR = new DataFlavor(OncTreeNode.class, "TreePath");
	private static final DataFlavor nodeFlavor [] = { NODE_FLAVOR };
	
   public ModelConfigurationEditorPanel()
	{
		init();
	}

	private void init()
	{
		rootNode = new OncTreeNode(ModelConfiguration.rootConfigObj, 
															 Persistible.DIRTY);
		model = new DefaultTreeModel(rootNode);
		exprTree = new JTree(model);
		sbList = new OncList();
		sbList.setDragEnabled(true);
		sbList.addKeyListener(new SBListKeyListener());
		setPreferredSize(new Dimension(600, 300));
		// Update only one tree instance
		GenericTreeNodeRenderer nodeRenderer = new GenericTreeNodeRenderer();
		exprTree.setCellRenderer(nodeRenderer);

		TreeDropTransferHandler th = new TreeDropTransferHandler();
		exprTree.setTransferHandler(th);		
		exprTree.setDragEnabled(true);
		exprTree.addKeyListener(new TreeKeyListener());
		exprTree.addMouseListener(new TreeMouseListener());
		exprTree.setRowHeight(25);


		JScrollPane spTree = new JScrollPane();
		spTree.setViewportView(exprTree);
		spTree.setPreferredSize(new Dimension(300, 300));
		JScrollPane spList = new JScrollPane(sbList);
		spList.setPreferredSize(new Dimension(300, 300));
		
		setLayout(new BorderLayout());
		add(spTree, BorderLayout.CENTER);
		add(spList, BorderLayout.EAST);
		
	}

	private boolean insertIntoTree(OncTreeNode targetNode, OncTreeNode newChild)
	{
		Object target = targetNode.getUserObject();
		Object pkgObj = newChild.getUserObject();

		
		if(pkgObj instanceof Droppable)
		{
			Droppable pkg = (Droppable) pkgObj;

			OncTreeNode tNode;

			//dropping a valid leaf into a label
			if(isLabelNode(target) && isLeafType(pkg))
			{
				return(insertIntoTree(targetNode, newChild, targetNode.getChildCount()));
			}
			//dropping a leaf into a leaf..add the dropped leaf to the the
			//target's parent label
			else if(isLeafType(target) && isLeafType(pkg))
			{
				OncTreeNode parent = (OncTreeNode) targetNode.getParent();
				int dropIdx = parent.getIndex(targetNode) + 1;
				return(insertIntoTree(parent, newChild, dropIdx));
			}
			//dropping a leaf into the root node
			else if(isRootNode(target) && isLeafType(pkg))
			{
				//if this is the first thing there, add it to the root
				if(targetNode.getChildCount() == 0)
				{
					return(insertIntoTree(targetNode, newChild, 0));
				}
				//if there is a label under the root already, add the
				//dropped node to that label
				else if(isLabelNode(((OncTreeNode)targetNode.getFirstChild()).getUserObject()))
				{
					return(insertIntoTree((OncTreeNode) targetNode.getFirstChild(), newChild));
				}
				//ignore the case where a leaf node is already there
				return(false);
			}
			//dropping albel on the root node
			else if(isRootNode(target) && isLabelNode(pkg))
			{
				//if there's nothing there add the operator to the root node
				if(targetNode.getChildCount() == 0)
				{
					return(insertIntoTree(targetNode, newChild, 0));
				}
				//if there's already a label under the root, add this
				else if(isLabelNode(((OncTreeNode)targetNode.getFirstChild()).getUserObject()))
				{
					return(insertIntoTree(targetNode, newChild, targetNode.getChildCount()));
//						return(false);
				} 
				//if there's a leaf, add the leaf to this label and this
				//label to the root
				else if(isLeafType(((OncTreeNode)targetNode.getFirstChild()).getUserObject()))
				{
					Vector expandedNodes = getCurrentExpandedNodes();
					OncTreeNode moveChild = (OncTreeNode) targetNode.getFirstChild();
					targetNode.remove(moveChild);
					targetNode.setPersistibleState(Persistible.DIRTY);
					targetNode.insert(newChild, targetNode.getChildCount());
					newChild.insert(moveChild, newChild.getChildCount());
					model.nodeStructureChanged(targetNode);
					expandedNodes.add(moveChild);
					expandAllNodes(expandedNodes);
					return(true);
				}
				return(false);
			}
			//dropping a label onto a leaf
			else if(isLeafType(target) && isLabelNode(pkg))
			{
				//add the operator that is being dropped to the leafs
				//parent and move the leaf under this operator
				OncTreeNode upperParent = (OncTreeNode) targetNode.getParent();

				//only do this if the parent leaf isn't the same type of
				//operator that is being added.
				if(!upperParent.getUserObject().equals(pkg))
				{
					Vector expandedNodes = getCurrentExpandedNodes();
//						OncTreeNode newChild = new OncTreeNode((Persistible) pkg);
					upperParent.remove(targetNode);
					upperParent.insert(newChild, upperParent.getChildCount());
					upperParent.setPersistibleState(Persistible.DIRTY);
					newChild.insert(targetNode, newChild.getChildCount());
					model.nodeStructureChanged(upperParent);
					expandedNodes.add(targetNode);
					expandAllNodes(expandedNodes);
					return(true);
				}
				return(false);
			}
			//dropping a label onto a label
			else if(isLabelNode(target) && isLabelNode(pkg))
			{
				return(insertIntoTree(targetNode, newChild, targetNode.getChildCount()));
			}
		} //end if(pkgObj instanceof Droppable)
		return(false);
	} //end insertIntoTree(node, node)
	private boolean isLeafType(Object obj)
	{
		return(obj instanceof StatementBundleConfiguration);
	}
	private boolean isLabelNode(Object obj)
	{
		return(obj instanceof TcapString);
	}
	private boolean isRootNode(Object obj)
	{
		return(ModelConfiguration.rootConfigObj.equals(obj));
	}
	private boolean insertIntoTree(OncTreeNode parent, OncTreeNode child, int index)
	{
		Object parentObj = parent.getUserObject();
		Object childObj = child.getUserObject();

		Vector expandedNodes = getCurrentExpandedNodes();
		model.insertNodeInto(child, parent, index);
		model.nodeStructureChanged(parent);
		expandedNodes.add(child);
		expandAllNodes(expandedNodes);

		if(child.getUserObject() instanceof StatementBundleConfiguration)
		{
			((StatementBundleConfiguration) child.getUserObject()).setVisibility(true);
			sbList.removeElement(child.getUserObject());
		}
		return(true);
	}
	private void expandAllNodes(Vector nodes)
	{
		Iterator it = nodes.iterator();
		while(it.hasNext())
		{
			Object node = it.next();
			if(node instanceof OncTreeNode)
				expandToNode((OncTreeNode) node);
			else
				System.out.println("NOT TreeNode!!");
		}
	}
	private void expandToNode(OncTreeNode node)
	{
		TreePath pathToExpand = new TreePath(model.getPathToRoot(node));
		exprTree.expandPath(pathToExpand);
	}
	private Vector getCurrentExpandedNodes()
	{
		Vector allNodes = new Vector();
		int nNodes = exprTree.getRowCount();
		for(int i = 0; i < nNodes; i++)
		{
			allNodes.add(exprTree.getPathForRow(i).getLastPathComponent());
		}
		return(allNodes);
	}

	public void addLabel(String label)
	{
		OncTreeNode dropTarget = (OncTreeNode) exprTree.getSelectionPath().getLastPathComponent();
		insertIntoTree(dropTarget, new OncTreeNode(new TcapString(label), Persistible.DIRTY));
	}
	public void setRequired(boolean req)
	{
		Object target = ((OncTreeNode) exprTree.getSelectionPath().getLastPathComponent()).getUserObject();
		if(target instanceof StatementBundleConfiguration)
			((StatementBundleConfiguration) target).setRequired(req);
	}
	public void setMultiples(boolean mult)
	{
		Object target = ((OncTreeNode) exprTree.getSelectionPath().getLastPathComponent()).getUserObject();
		if(target instanceof StatementBundleConfiguration)
			((StatementBundleConfiguration) target).setAllowMultiples(mult);
	}
	class TreeDropTransferHandler extends TransferHandler implements Transferable
	{
		private OncTreeNode oldNode = null;
		
		public int getSourceActions(JComponent c)
		{
			return(COPY_OR_MOVE);
		}

		public boolean importData(JComponent c, Transferable t)
		{
			try
			{
				OncTreeNode dropTarget = (OncTreeNode) exprTree.getSelectionPath().getLastPathComponent();
				
			//	OncTreeNode myNode = null;
			//	if(t.isDataFlavorSupported(Droppable.oncTreeNode))
			//		myNode = (OncTreeNode) t.getTransferData(Droppable.oncTreeNode);
				
				
				if(t.isDataFlavorSupported(Droppable.droppableData))
				{
					if(c.equals(sbList))
					{
						Object [] sbs = sbList.getSelectedValues();
						for(int n = 0; n < sbs.length; n++)
						{
							insertIntoTree(dropTarget, new OncTreeNode(sbs[n], Persistible.DIRTY));
						}
						return(true);
					}
					else
					{
						Droppable sbObj = (Droppable) t.getTransferData(Droppable.droppableData);
						if(sbObj instanceof StatementBundleConfiguration)
						{
							OncTreeNode sbNode = new OncTreeNode(sbObj, Persistible.DIRTY);
							return(insertIntoTree(dropTarget, sbNode));
						}
						else
							return(false);	
					}
				}
				else if( t.isDataFlavorSupported(NODE_FLAVOR) && c.equals(exprTree))
				{
					OncTreeNode droppedNode = oldNode; //(OncTreeNode) t.getTransferData(NODE_FLAVOR);
					TreePath currentPath = ((JTree) c).getSelectionPath();
//					OncTreeNode parent  = (OncTreeNode) currentPath.getLastPathComponent();
//					DefaultTreeModel model = (DefaultTreeModel)((JTree)c).getModel();
//					model.removeNodeFromParent(droppedNode);
					OncTreeNode newNode = (OncTreeNode) droppedNode.clone();
					return(insertIntoTree(dropTarget, newNode));
				}
				else
					return(false);
			}
			catch(UnsupportedFlavorException e){System.out.println("Unsupported Flavor"); return(false);}
			catch(IOException e){System.out.println("IO Exception"); return(false);}
		}

		public void exportAsDrag(JComponent comp, InputEvent e, int action)
		{
			super.exportAsDrag(exprTree, e, action); 
		}
		public void exportToClipboard(JComponent comp, Clipboard clip, int action)
		{
			super.exportToClipboard(exprTree, clip, action);
		}
		public Transferable createTransferable(JComponent c)
		{
			if(c instanceof JTree)
			{
				TreePath path = ((JTree)c).getSelectionPath();
				oldNode = (OncTreeNode) path.getLastPathComponent();
				return(this);
			}
			else
				return(null);
		}
		protected void exportDone(JComponent c, Transferable t, int action)
		{
			if(action == MOVE && c instanceof JTree && oldNode != null)
			{
				DefaultTreeModel model = (DefaultTreeModel) ((JTree) c).getModel();
				if(! isRootNode(oldNode))
					model.removeNodeFromParent(oldNode);
			}
		}
		public boolean canImport(JComponent c, DataFlavor[] flavors)
		{
			for(int n = 0; n<flavors.length; n++)
				if(flavors[n].equals(Droppable.droppableData) || flavors[n].equals(NODE_FLAVOR))
					return(true);

			return(true);
		}
		public DataFlavor [] getTransferDataFlavors()
		{
			return(nodeFlavor);
		}
		public boolean isDataFlavorSupported( DataFlavor flavor)
		{
			return( flavor.equals(NODE_FLAVOR));
		}
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
		{
			if (isDataFlavorSupported(flavor))
				return(oldNode);
			else
				throw(new UnsupportedFlavorException(flavor));
		}
		private OncTreeNode copyNode(OncTreeNode node)
		{
				OncTreeNode newNode = new OncTreeNode(node.getUserObject(), true,
																							Persistible.DIRTY);
			for (int n = 0; n < node.getChildCount(); n++)
			{
				OncTreeNode child = copyNode((OncTreeNode) node.getChildAt(n));
				newNode.insert(child, n);
			}
			return(newNode);
		}

	}  // end class TreeDropTransferHandler

	
class SBListKeyListener implements KeyListener
{
	public void keyPressed(KeyEvent e)
	{
		int n;
		if(e.getKeyCode() == KeyEvent.VK_INSERT || e.getKeyCode() == KeyEvent.VK_DELETE)
		{
			OncTreeNode dropNode;
			if(exprTree.getSelectionCount() > 0)
			{
				dropNode = (OncTreeNode) exprTree.getSelectionPath().getLastPathComponent();
			}
			else
				dropNode = configuration.getRootNode();
			
			Object [] sbs = sbList.getSelectedValues();
			
			for(n = 0; n < sbs.length; n++)
			{
				insertIntoTree(dropNode, new OncTreeNode(sbs[n], Persistible.DIRTY));
				sbList.removeElement(sbs[n]);
			}
		}
// the following code is used to completely delete a SB from a configuration
// this was used when there was a bug that allowed the SB to be added to the 
// configuration more than once
/*		else if(e.getKeyCode() == KeyEvent.VK_DELETE)
		{
			Object [] sbs = sbList.getSelectedValues();
			if(sbs.length > 0)
			{
				String delQuestion;
				if(sbs.length == 1)
					delQuestion = "Are you sure you want to delete this item?";
				else
					delQuestion = "Are you sure you want to delete these items?";
				int deleteAnswer = 1;
				deleteAnswer = JOptionPane.showConfirmDialog
						((JFrame)null, 
						 delQuestion,
						 "Delete Confirmation",
						 JOptionPane.YES_NO_OPTION, 
						 JOptionPane.INFORMATION_MESSAGE);
				if ( deleteAnswer == JOptionPane.YES_OPTION)
				{
					for(n = 0; n < sbs.length; n++)
					{
						configuration.removeFromConfigurationList((StatementBundleConfiguration) sbs[n]);
						sbList.removeElement(sbs[n]);
					}
				}
			}
		} */
	}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
} //end of class SBListKeyListener
	class TreeMouseListener implements MouseListener
	{
		private JPopupMenu labelRightClickMenu = new JPopupMenu("Label menu");
		private JPopupMenu configRightClickMenu = new JPopupMenu("Config menu");
		private RMenuItem labelItem  = new RMenuItem("Add Label");
		private RMenuItem labelItem2 = new RMenuItem("Add Label");
		private JCheckBoxMenuItem requiredItem = new JCheckBoxMenuItem("Required");
		private JCheckBoxMenuItem multiplesItem = new JCheckBoxMenuItem("Multiples Allowed");
		
		TreeMouseListener()
		{
			init();
		}
		private void init()
		{
			labelItem = new RMenuItem("Add Label");
			labelRightClickMenu.setInvoker(exprTree);
			labelRightClickMenu.add(labelItem);
			configRightClickMenu.setInvoker(exprTree);
			configRightClickMenu.add(labelItem2);
			configRightClickMenu.addSeparator();
			requiredItem.addActionListener(new RequiredListener());
			multiplesItem.addActionListener(new MultiplesListener());
			configRightClickMenu.add(requiredItem);
			configRightClickMenu.add(multiplesItem);
		}
		public void mouseClicked(MouseEvent e){}
		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){}
		public void mousePressed(MouseEvent e)
		{
			if(e.getButton() == MouseEvent.BUTTON3)
			{
				TreePath selPath = exprTree.getPathForLocation(e.getX(), e.getY());
				exprTree.setSelectionPath(selPath);
				Object usrObj = ((OncTreeNode)selPath.getLastPathComponent()).getUserObject();
				if( usrObj instanceof StatementBundleConfiguration)
				{
					StatementBundleConfiguration conf = (StatementBundleConfiguration) usrObj;
					multiplesItem.setState(conf.areMultiplesAllowed());
					requiredItem.setState(conf.isRequired());
					configRightClickMenu.setLocation(e.getX(), e.getY());
					configRightClickMenu.show(exprTree, e.getX(), e.getY());
				}
				else
				{
					labelRightClickMenu.setLocation(e.getX(), e.getY());
					labelRightClickMenu.show(exprTree, e.getX(), e.getY());
				}
			}
		}
		public void mouseReleased(MouseEvent e){}

		private MenuFocusListener menuFocusListener = new MenuFocusListener();
		private class RMenuItem extends JMenuItem
		{
			String menuName;
			RMenuItem me;
			RMenuItem(String name)
			{
				super(name);
				me = this;
				menuName = name;
				addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					if(me == labelItem || me == labelItem2)
					{
						String label = InputDialog.getValue("Enter the label");
						if(label != null)
						{
							addLabel(label);
						}
					}
				}});
				
			}
		} //end class AddLabelAction
		private class RequiredListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				setRequired(requiredItem.getState());
			}			
		} //end class RequiredListener
		private class MultiplesListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				setMultiples(multiplesItem.getState());
			}
		} //end class MultiplesListener
		private class MenuFocusListener implements FocusListener
		{
			public void focusGained(FocusEvent e) {}
			public void focusLost(FocusEvent e)
			{
				labelRightClickMenu.setVisible(false);
			}
		} // end class MenuFocusListener
	} // end class TreeMouseListener

	class TreeKeyListener implements KeyListener
	{
		public void keyPressed(KeyEvent e)
		{
			if(e.getKeyCode() == KeyEvent.VK_DELETE)
			{
				TreePath path = exprTree.getSelectionPath();
				if(path != null)
				{
					OncTreeNode node = (OncTreeNode) path.getLastPathComponent();
					if(node != null && ! OncFilter.rootFilterObj.equals(node.getUserObject()))
					{
						//walk node and move all configurations back to the
						//"invisible" list
						((DefaultTreeModel) exprTree.getModel()).removeNodeFromParent(node);
						removeNode(node);
						configuration.update();
					}
				}
			}
		}
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e) {}

		private void removeNode(OncTreeNode node)
		{
			Object userObj = node.getUserObject();
			if(userObj instanceof StatementBundleConfiguration)
			{
				((StatementBundleConfiguration) userObj).setVisibility(false);
				node.delete();
				sbList.add(userObj);
			}
			removeChildren(node);
		}
		private void removeChildren(OncTreeNode node)
		{
			Iterator children = node.getChildren();
			while(children.hasNext())
			{
				Object nextNode = children.next();
				if(nextNode instanceof OncTreeNode)
				{
					removeNode((OncTreeNode) nextNode);
				}
			}
		}

	} //end of class TreeKeyListener

} //end of class ModelConfigurationEditorPanel