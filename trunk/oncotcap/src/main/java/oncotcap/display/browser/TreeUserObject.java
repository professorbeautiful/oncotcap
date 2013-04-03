package oncotcap.display.browser;
import javax.swing.tree.TreeNode;
import java.util.Vector;

public interface TreeUserObject
{
		public Vector getTreeNodes();
		public void clearTreeNodes();
		public void addTreeNode(TreeNode treeNode);
		public void removeTreeNode(TreeNode treeNode);
		public boolean hasTreeNode(TreeNode treeNode);
		
}
