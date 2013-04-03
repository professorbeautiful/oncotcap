package oncotcap.display.common;

import java.awt.datatransfer.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.io.*;


public class TreeTransferHandler extends TransferHandler
                                 implements Transferable
{
        public static DataFlavor NODE_FLAVOR = new DataFlavor(DefaultMutableTreeNode.class, "TreePath" );

        private static final DataFlavor flavors [] = { NODE_FLAVOR };


		  public static void main(String [] args)
		  {
			  JFrame jf = new JFrame();
			  jf.setSize(600,600);
			  JTree jt = new JTree();
			  jt.setTransferHandler(new TreeTransferHandler());
			  jt.setDragEnabled(true);
			  jf.getContentPane().add(jt);
			  jf.setVisible(true);

		  }
        // Transferable
        public DataFlavor[] getTransferDataFlavors()
        {
            return flavors;
        }

        // checks flavor of data to be dragged&dropped
        public boolean isDataFlavorSupported( DataFlavor flavor )
        {
            return ( flavor.getRepresentationClass() ==
                         DefaultMutableTreeNode.class );
        }

        public Object getTransferData( DataFlavor flavor )
              throws UnsupportedFlavorException, IOException
        {
                if (isDataFlavorSupported(flavor))
					 {
                        return (Object)oldNode;
                }
					 else
					 {
                      throw new UnsupportedFlavorException(flavor);
                }
        }

        // TransferHandler
        public int getSourceActions( JComponent c )
        {
                return TransferHandler.COPY_OR_MOVE;
        }

        /**
         * @return true if the component supports one of the flavors.
         */
        public boolean canImport( JComponent comp, DataFlavor[] transferFlavors )
        {
                if (! (comp instanceof JTree) )
					 {
                        // return false if src or dest not JTree
                        return false;
                }

                for ( int i = 0, n = transferFlavors.length; i < n; i++ )
					 {
                        if ( transferFlavors[i].equals(NODE_FLAVOR))
								{
                                return true;
                        }
                } return false;
        }

        /**
         * Saves the data to be transferred. (copy op) 
         */
        public Transferable createTransferable( JComponent c )
		  {
                if ( c instanceof JTree )
					 {
                        TreePath treePath = ((JTree)c).getSelectionPath();
                        oldNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();
                        return this;
                }
					 return null;
        }
        
        // called by DnD to remove moved data when export is complete
        protected void exportDone(JComponent source, Transferable data, int action)
		  {
            if (action == MOVE)
				{
                DefaultTreeModel model = (DefaultTreeModel)((JTree)source).getModel();
                model.removeNodeFromParent(oldNode);
            }
        }
        
        DefaultMutableTreeNode oldNode = null;
        
        /**
         * checks dataflavor of data to be transferred and gets it.
         * (paste op)
         */
        public boolean importData( JComponent comp, Transferable t )
        {
            try
				{        
                DefaultMutableTreeNode tn = null;
                if ( t.isDataFlavorSupported(NODE_FLAVOR) )
					 {
                   tn = (DefaultMutableTreeNode)t.getTransferData(NODE_FLAVOR);
                }

                if ( comp instanceof JTree )
					 {
                    TreePath curSelection = ((JTree)comp).getSelectionPath();
                            
                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode)curSelection.getLastPathComponent();
                            
                    DefaultTreeModel model = (DefaultTreeModel)((JTree)comp).getModel();
                                
                    // deep copying first
                    DefaultMutableTreeNode copyOld = copy( tn );
                                
                    model.insertNodeInto(copyOld, parent, parent.getChildCount() );
                           
                    return true;
                }
            }
				catch (UnsupportedFlavorException ignored) {
						ignored.printStackTrace();
				}
				catch (IOException ignored) {ignored.printStackTrace(); }
				return false;
        }

        DefaultMutableTreeNode copy(DefaultMutableTreeNode old )
        {
            DefaultMutableTreeNode n = new DefaultMutableTreeNode(old.getUserObject(), true);
            for ( int i = 0; i < old.getChildCount(); i++ )
				{
                    DefaultMutableTreeNode child = copy((DefaultMutableTreeNode)old.getChildAt(i));
                    n.insert( child, i );
            }
				return n;
        }
}

