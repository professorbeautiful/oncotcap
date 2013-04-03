package oncotcap.display.common;

import java.util.*;
import javax.swing.*;

import java.awt.Component;
import java.awt.event.*;

import oncotcap.display.browser.GenericTree;
import oncotcap.display.browser.GenericTreeNode;

public class OncFindPanel extends JPanel implements ActionListener
{
		private  JTextField searchWordField = new JTextField(30);

		private  String searchWord = null;
		private  Component searchComponent = null;

		public  OncFindPanel() {
				super();
				initUI();
		}
		
		private void initUI() {
				searchWordField.addActionListener(this);
				JButton searchUp = new JButton("Search Up");
				searchUp.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e){ search(false); }});
				JButton searchDown = new JButton("Search Down");
				searchDown.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){ search(true); }});
				
				add(searchWordField);
				//add(searchUp);
				add(searchDown);
		}

		public void setSearchComponent(Component searchComponent) {
				this.searchComponent = searchComponent;
		}

		public void search(boolean up) {
				searchWord = searchWordField.getText();
				if (searchWord == null || "".equals(searchWord) )
						return;

				if ( searchComponent instanceof oncotcap.display.browser.GenericTree) {
						GenericTree tree = (GenericTree)searchComponent;
						GenericTreeNode node = tree.findString(searchWord);
						if ( node == null ) {
								JOptionPane.showMessageDialog
										((JFrame)null, 
										 "<html>No more matches. Search will wrap.</html>");
						}
				}
				else if ( searchComponent instanceof OncScrollList) {
						OncScrollList list = (OncScrollList)searchComponent;
						int position = list.findString(searchWord);
						if ( position < 0 ) {
								JOptionPane.showMessageDialog
										((JFrame)null, 
										 "<html>No more matches. Search will wrap.</html>");
						}
				}
		}

    public void actionPerformed(ActionEvent e) {
				search(false);
    }

    public Component getSearchComponent(){
    	return searchComponent;
    }
		public static void main(String[] args) {
				JFrame f = new JFrame();
				OncFindPanel p = new OncFindPanel();
				f.getContentPane().add(p);
				f.setSize(300,100);
				f.setVisible(true);
		}
}
