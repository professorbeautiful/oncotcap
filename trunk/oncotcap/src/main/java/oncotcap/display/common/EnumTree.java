package oncotcap.display.common;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.reflect.*;
import oncotcap.util.ReflectionHelper;


public class EnumTree extends JTree
{
	private Point mousePosition = null;
	private EnumTreePopupMenu menu;
	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		jf.setSize(400,300);
		EnumTree tree = new EnumTree();
		JScrollPane sp = new JScrollPane(tree);
		jf.getContentPane().add(sp);
		jf.setVisible(true);
	}
	public EnumTree()
	{
		setExpandsSelectedPaths(false);
		addMouseListener(new EnumTreeMouseListener());
		addMouseMotionListener(new EnumTreeMouseMotionListener());
		setSelectionModel(new EnumTreeSelectionModel());
		menu = new EnumTreePopupMenu();
		MenuWindowListener ml;
		menu.addWindowListener(ml = new MenuWindowListener());
		menu.addFocusListener(ml);
		menu.addWindowStateListener(ml);
		menu.add(new EnumTreeAction("one"));
		menu.add(new EnumTreeAction("two"));
	}
	private class MenuWindowListener implements WindowListener, FocusListener, WindowStateListener
	{
		public void windowActivated(WindowEvent e) {System.out.println("Activated");}
		public void windowClosed(WindowEvent e) {}
		public void windowClosing(WindowEvent e) {}
		public void windowDeactivated(WindowEvent e)
		{
			System.out.println("Deactivated");
			menu.setVisible(false);
		}
		public void windowDeiconified(WindowEvent e) {System.out.println("Deiconified");}
		public void windowIconified(WindowEvent e) {System.out.println("Iconified");}
		public void windowOpened(WindowEvent e)  {System.out.println("Opened");}

		public void focusGained(FocusEvent e){System.out.println("Focus gained");}
		public void focusLost(FocusEvent e)
		{
			System.out.println("Focus lost");
			menu.setVisible(false);
		}
		public void windowStateChanged(WindowEvent e)  {System.out.println("Window State Changed");}
	}
	private class EnumTreeAction extends AbstractAction
	{
		private String name;
		public EnumTreeAction(String name)
		{
			super(name);
			this.name = name;
		}
		public void actionPerformed(ActionEvent e)
		{
			System.out.println(name);
			menu.setVisible(false);
		}
	}
	private class EnumTreePopupMenu extends JWindow
	{
		private JPopupMenu iMenu;
		EnumTreePopupMenu()
		{
			iMenu = new JPopupMenu();
			iMenu.setVisible(true);
//			getContentPane().setLayout(null);
//			iMenu.setLocation(new Point(0,0));
			getContentPane().add(iMenu);
		}
		public void add(Action action)
		{
			iMenu.add(action);
			setSize(iMenu.getPreferredSize());
		}
	}
	private class EnumTreeMouseListener implements MouseListener
	{
		public void mouseClicked(MouseEvent e)
		{
			if(e.getButton() == MouseEvent.BUTTON3)
			{
				System.out.println("Right click");
				menu.setLocation(e.getPoint());
				menu.setVisible(true);
				menu.requestFocus();
			}
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
	private class EnumTreeMouseMotionListener implements MouseMotionListener
	{
		public void mouseDragged(MouseEvent e) {}
		public void mouseMoved(MouseEvent e)
		{
			mousePosition = e.getPoint();
		}
	}
	private class EnumTreeSelectionModel extends DefaultTreeSelectionModel
	{
		private Vector currentSelections = new Vector();
		EnumTreeSelectionModel()
		{
			super();
			addTreeExpansionListener(new MyTreeExpansionListener());
		}
		private TreePath getLastClicked()
		{
			if(mousePosition != null)
			{
				return(getPathForLocation((int) mousePosition.getX(), (int) mousePosition.getY()));
			}
			else return(null);
		}
		private void toggleSelection(TreePath clickedPath)
		{
			boolean found;
			if(clickedPath != null)
			{
				if(currentSelections.contains(clickedPath))
					currentSelections.remove(clickedPath);
				else
					currentSelections.add(clickedPath);

				setSelectionPaths(currentSelections);
			}
		}
		private void setSelectionPaths(Vector paths)
		{
			if(paths != null)
			{
				clearSelection();
				if(paths.size() > 0)
				{
					TreePath [] tPaths = (TreePath []) Array.newInstance(ReflectionHelper.classForName("javax.swing.tree.TreePath"), paths.size());
					Iterator it = paths.iterator();
					int i = 0;
					while(it.hasNext())
					{
						tPaths[i++] = (TreePath) it.next();
					}
					super.setSelectionPaths(tPaths);
				}
			}
		}
		public void addSelectionPath(TreePath path)
		{
			toggleSelection(getLastClicked());
		}
		public void addSelectionPaths(TreePath [] paths)
		{
			toggleSelection(getLastClicked());
		}
		public void setSelectionPath(TreePath path)
		{
			toggleSelection(getLastClicked());
		}
		public void setSelectionPaths(TreePath[] paths)
		{
			toggleSelection(getLastClicked());
		}
		class MyTreeExpansionListener implements TreeExpansionListener
		{
			public void treeCollapsed(TreeExpansionEvent event) {}
			public void treeExpanded(TreeExpansionEvent event)
			{
				setSelectionPaths(currentSelections);
			}
		}
	}
}
