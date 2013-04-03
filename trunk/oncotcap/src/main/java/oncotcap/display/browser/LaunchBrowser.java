package oncotcap.display.browser;

import java.net.*;
import java.io.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class LaunchBrowser extends AbstractAction {

		GenericTree tree = null;
		Object source = null;
		static Dimension defaultSize = new Dimension(600,400);

		public LaunchBrowser() {
				super("Edit");
				
		}
		public void actionPerformed(ActionEvent e) {
				Object obj = null;
				JTable table  = null;
				JList list = null;
				if ( getSource() instanceof OncScrollList) {
						list = ((OncScrollList)getSource()).getList();
						// select item
						obj = list.getSelectedValue();
					// 	System.out.println("edit selected value " 
// 															 + ((OncScrollList)getSource()).getValue());
				}
				else if ( getSource() instanceof OncScrollTable) {
						table = ((OncScrollTable)getSource()).getTable();
						int selectedRow = table.getSelectedRow();
						obj = ((OncScrollTable)getSource()).getDataObject(selectedRow);
				}
				// If the object is a string see if it is a valid url
				if ( obj instanceof String ) {
						try {
								// if it is a valid url launch browser
								oncotcap.util.BrowserLauncher2.openURL((String)obj);
								
						}catch(MalformedURLException mfue) {
								System.out.println("ERROR Malformed URL: "
																	 + obj);
						}
						catch(IOException ioe) {
								System.out.println("ERROR IO exception  launching browser: "
																	 + obj);
						}
						
				}
		}

		public void setTree(GenericTree tree) {
				this.tree = tree;
		}
		public GenericTree getTree() {
				return this.tree;
		}
		public void setSource(Object source) {
				this.source = source;
		}
		public Object getSource() {
				return this.source;
		}

		public void setSourceParent(Object source) {
				this.source = source;
		}
		public Object getSourceParent() {
				return this.source;
		}

}
