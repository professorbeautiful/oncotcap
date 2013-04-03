package oncotcap.display.browser;

import java.util.*;
import javax.swing.*;
import java.awt.*;


public class TestTree2 extends JPanel
{
		GenericTree tree = null;
		JScrollPane scrollPane = null;

		public TestTree2() {
				setLayout(new BorderLayout());
				tree = 
						new GenericTree(getTreeInstances(), 
														false);
				scrollPane = new JScrollPane();
				scrollPane.setViewportView(tree);
				scrollPane.revalidate();
				add(scrollPane, BorderLayout.CENTER);
		}


		public Hashtable getTreeInstances() {	
				Vector endingClassNames = new Vector();
				// get tree hashtable
				endingClassNames.addElement("QuoteNugget");
				// 				endingClassNames.addElement("SubModel"); //"CodeBundle");
				return oncotcap.Oncotcap.getDataSource().getInstanceTree
						("Keyword", endingClassNames);
		}
				
	public static void main(String[] args) {
		JFrame f = new JFrame();
		TestTree2 p = new TestTree2();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
