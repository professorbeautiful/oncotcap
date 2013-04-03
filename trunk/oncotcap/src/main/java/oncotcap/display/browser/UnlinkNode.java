package oncotcap.display.browser;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.tree.*;
import java.util.*;
import javax.swing.*;
import java.lang.reflect.Array;

import javax.swing.event.*;
import oncotcap.display.common.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.util.*;

public class UnlinkNode extends OncAbstractAction {

		GenericTree tree = null;
		Object source = null;
		Hashtable childParent = new Hashtable();
		OncScrollList list = null;

		public UnlinkNode() {
				super("Unlink");
				
		}
		public void actionPerformed(ActionEvent e) {
				setSource(getBrowserActionSource(e));
				setTree(getBrowserActionTree(getSource()));
				childParent.clear();
				if ( getSource() instanceof OncScrollList ) {
						list = (OncScrollList)getSource();
						Object parent = list.getLinkTo();
						// Get selected elements
						JList jlist = list.getList();
						Object[] selectedValues = jlist.getSelectedValues();
						for ( int i = 0; i < Array.getLength(selectedValues); i++) {
								System.out.println("selectedValues " + selectedValues[i]);
								if ( !(selectedValues[i] instanceof Persistible) ) {
									list.removeValue(selectedValues[i]);
								}
								else
									childParent.put(selectedValues[i], parent);
						}
				}
				else if ( getSource() instanceof OntologyTree ) {
						Vector selectedItems = tree.getSelected(); 
						Vector selectedParents = tree.getSelectedParents(); 
						Iterator i = selectedItems.iterator();
						Iterator ii = selectedParents.iterator();
						while ( i.hasNext() && ii.hasNext() ) {
								childParent.put(i.next(), ii.next());
						}
				}
				unlink(childParent);
				OncBrowser.refresh();
		}

		private void unlink(Hashtable unlinkFrom) {
				// Go through the hashtable and unlink the pairs
				for (Enumeration e = unlinkFrom.keys() ; 
						 e.hasMoreElements() ;) {
						AbstractPersistible child = null;
						AbstractPersistible parent = null;
						Object key = e.nextElement();
						Object value = unlinkFrom.get(key);
						
						if ( value instanceof AbstractPersistible ) 
								parent = (AbstractPersistible)value;
						if ( key instanceof AbstractPersistible ) 
								child = (AbstractPersistible)key;
						if ( parent != null && child != null) {
								child.unlink(parent);
								parent.unlink(child);
								child.update();
								parent.update();
								if ( tree != null ) 
										tree.removeNode(child);
								if ( list != null ) 
										list.removeValue(child);
						}
						else {
								System.out.println("Error parent or child is null -> Parent: " 
																	 + parent + " Child: " + child);
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
