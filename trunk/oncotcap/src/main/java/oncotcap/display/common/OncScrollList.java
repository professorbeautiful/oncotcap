/*
 * Educational Resource for Tumor Heterogeneity
 *             Oncology Thinking Cap
 *
 * Copyright (c) 2003  University of Pittsburgh
 * All rights reserved.
 *
 *  SourceSafe Info:
 *               $Header: $
 *               Revision: $Revision$
 *               Author: $Author$
 *
 * Code Review History:
 *     (mm.dd.yyyy initials)
 *
 * Test History:
 *     (mm.dd.yyyy initials)
 */
package oncotcap.display.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.SaveEvent;
import oncotcap.datalayer.SaveListener;
import oncotcap.datalayer.persistible.CodeBundle;
import oncotcap.display.browser.OncBrowser;
import oncotcap.display.browser.RemoveNode;
import oncotcap.util.ReflectionHelper;

public class OncScrollList extends OncUiObject
		implements	 SaveListener,
								 DropTargetListener,
								 DragGestureListener { //,
		//Selectable {
		private final static String WINDOWS =
				"com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		boolean allowsMultiples = true;
		boolean sortItems = true;
		JList list = null;
		String [] testList = {"Item 1", "Item 2", "Item 3"};
		boolean showButtons = false;
		boolean showLabel = false;
		int deleteMode = RemoveNode.DELETE_PERSISTIBLE;
		String label = "Default Label";
		JScrollPane scrollPane = null;
		OncScrollListListener listener = new OncScrollListListener();
		Object editObject = null;
		Class listType = null;
		Collection linkTos = null;
		OncScrollListButtonPanel buttonPanel = null;
		RemoveSelectedFromList removeSelected = 
				new RemoveSelectedFromList("Remove From List");
		Toolkit kit = Toolkit.getDefaultToolkit();
		final Clipboard clipboard =
				kit.getSystemClipboard();

		// set up comparator
		oncotcap.display.browser.BrowserNodeComparator comparator = 
				new oncotcap.display.browser.BrowserNodeComparator();

		public OncScrollList(boolean showButtons, 
												 boolean showLabel) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				init();
		}

		public OncScrollList(Class listType, 
												 boolean showButtons, 
												 boolean showLabel) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				this.listType = listType;
				init();
		}

		public OncScrollList(Class listType, 
												 boolean showButtons, 
												 boolean showLabel,
												 boolean allowsMultiples) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				this.listType = listType;
				this.allowsMultiples = allowsMultiples;
				init();
		}

		public OncScrollList(Collection items, 
												 boolean showButtons, 
												 boolean showLabel, 
												 int deleteMode) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				this.deleteMode = deleteMode;	
				init();
				setData(items);
		}
		public OncScrollList(Collection items, 
												 boolean showButtons, 
												 boolean showLabel) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				init();
				setData(items);
		}


		public OncScrollList(Collection items, 
												 String label,
												 boolean showButtons, 
												 boolean showLabel) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				this.label = label;
				init();
				setData(items);
		}

		public OncScrollList(Class listType, 
												 Object editObj, 
												 String label,
												 boolean showButtons, 
												 boolean showLabel) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				this.label = label;
				this.listType = listType;
				this.editObject = editObj;
				init();
		}

		public OncScrollList(Class listType, 
												 Object editObj, 
												 String label,
												 boolean showButtons, 
												 boolean showLabel,
												 boolean allowsMultiples) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				this.label = label;
				this.listType = listType;
				this.editObject = editObj;
				this.allowsMultiples = allowsMultiples;
				init();
		}

		public OncScrollList(Object editObj, 
												 String label,
												 boolean showButtons, 
												 boolean showLabel) {
				this.showButtons = showButtons;
				this.showLabel = showLabel;
				this.label = label;
				this.editObject = editObj;
				init();
		}
		
		public OncScrollListButtonPanel getButtonPanel() {
				return buttonPanel;
		}

		private void init() {
				try {
				setInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT,
												 OncBrowser.getDefaultInputMap());
				setInputMap(JComponent.WHEN_FOCUSED,
												 OncBrowser.getDefaultInputMap());
				setActionMap(OncBrowser.getDefaultActionMap());
				GridBagLayout g = new GridBagLayout();

				setLayout(new BorderLayout());
				JPanel topPanel = new JPanel(new BorderLayout());
				GridBagConstraints c3 = new GridBagConstraints();
				//g.columnWeights = new double[]{0.0f, 1.0f};
				c3.fill = GridBagConstraints.HORIZONTAL;
				c3.anchor = GridBagConstraints.SOUTHEAST;
				c3.insets = new Insets(0,0,0,0); //t,l,b,r
				c3.ipadx = 0;
				c3.ipady = 0;
				c3.weightx = 1;
				// topPanel.setLayout(g);
				
// 				JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
				JLabel l = new JLabel(label);
				l.setFont(new Font("Helvetica", Font.BOLD, 11));
// 				labelPanel.add(l);
				if ( showLabel ) {
						topPanel.add(l, BorderLayout.WEST);
				}
				if ( showButtons ) {
						buttonPanel = 
								new OncScrollListButtonPanel(this);
						c3.weightx = 1;
						c3.anchor = GridBagConstraints.SOUTHEAST;
						c3.gridwidth = GridBagConstraints.REMAINDER;
						topPanel.add(buttonPanel, BorderLayout.CENTER);
				}
 				if ( showButtons || showLabel ) 
 						add(topPanel, BorderLayout.NORTH);
				Method getter = null;
				if ( editObject != null &&  label != null && 
						 editObject instanceof oncotcap.datalayer.AutoGenPersistibleWithKeywords) {
						// Fill the list with data from the editObject
						getter = 
								((oncotcap.datalayer.AutoGenPersistibleWithKeywords)editObject).getGetMethod(label);
						Object returnObject = null;
						if ( getter != null) {
								returnObject = ReflectionHelper.invoke(getter,
																											 editObject,
																											 null);
						}
						if ( returnObject instanceof Vector && ((Vector)returnObject).size() > 0 ) {
								list = new JList();	
								setData((Vector)returnObject);
						}
						else { // empty list
								list = new JList();
								setData(new Vector());
						}
				}
				else { // empty list
						list = new JList();
						setData(new Vector());
				}

				list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				list.setSelectionBackground(new Color(175,177,159));
				list.setSelectionForeground(Color.black);

				if ( allowsMultiples ) {
						list.setVisibleRowCount(5);
						scrollPane = new JScrollPane(list);
						c3.fill = GridBagConstraints.BOTH;

				}
				else {
						list.setVisibleRowCount(1);
						scrollPane = new JScrollPane(list);
						scrollPane.setVerticalScrollBarPolicy
								(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
						scrollPane.setHorizontalScrollBarPolicy
								(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				}
				list.addMouseListener(listener);
				list.addListSelectionListener(listener);
				list.setCellRenderer(new OncListCellRenderer());
				new DropTarget(list, 
											 DnDConstants.ACTION_COPY_OR_MOVE,
											 this);
				c3.weightx = 1;
				c3.weighty = 1;
				c3.gridwidth = GridBagConstraints.REMAINDER;
				c3.anchor = GridBagConstraints.NORTHWEST;
				add(scrollPane, BorderLayout.CENTER);
		} catch (Exception e) {
				e.printStackTrace();
		}
		}

		public void removeListener() {
				list.removeMouseListener(listener);
				list.removeListSelectionListener(listener);
				
		}
		public void addListener(EventListener listener ) {
				list.addMouseListener((MouseListener)listener);
				//list.addListSelectionListener((ListSelectionListener)listener);
				
		}
		private Vector sort(Collection collection) {
				Object[] objs = collection.toArray();
			 Arrays.sort(objs, comparator);
			 return oncotcap.util.CollectionHelper.arrayToVector(objs);
		}
	  public void sortItems(boolean sortItems) {
				this.sortItems = sortItems;
		}	
		public void setData(Collection items) {
				DefaultListModel listModel = getListModel();
				Object [] selectedItems = list.getSelectedValues();
				listModel.clear();
				Collection sortedItems = items;
				if ( sortItems ) 
						sortedItems = sort(items);
				Iterator i = sortedItems.iterator();
				Object obj = null;
				while ( i.hasNext()) {
						obj = i.next();
						if ( obj instanceof Persistible) 
								((Persistible)obj).addSaveListener(this);
						if ( !listModel.contains(obj) )
								listModel.addElement(obj);
				}
				
				list.setModel(listModel);
				resetSelected(selectedItems);
				list.revalidate();
		}

		private void resetSelected(Object[] selectedItems){
				for ( int i = 0; i < Array.getLength(selectedItems); i++) {
						list.setSelectedValue(selectedItems[i], false);
				}
		}


		public void setLinkTos(Collection linkTos) {
				// 	System.out.println("setLinkTos: " + linkTos + " in list " + getList()
				// 													 + " for list type " + getListType());
				this.linkTos = linkTos;
		}

		public Collection getLinkTos() {
				return linkTos;
		}

		public Object getLinkTo() {
				if (linkTos != null )
						return (linkTos.toArray())[0];
				else 
						return null;
		}
		public Object getData(int pos) {
				ListModel model = list.getModel();
				if ( model.getSize() > 0 && pos >= 0 && model.getSize() > pos) {
						return model.getElementAt(pos);
				}
				return null;
		}

				public Collection getData() {
				Vector data = new Vector();
				ListModel model = list.getModel();
				for ( int i = 0; i < model.getSize(); i++) {
						data.addElement(model.getElementAt(i));
				}
				return data;
		}



		public Class getListType() {
				return listType;
		}
		
		public boolean allowsMultiples() {
				return this.allowsMultiples;
		}

		public int getDeleteMode() {
				return this.deleteMode;
		}
		public String getLabel() {
				return label;
		}

		public Object getValue(){
				if ( allowsMultiples ) 
						return getData();
				else {
						// Get the single element from the vector and return that
						return getData(0);
				}
		}

		public void setValue(Object obj){
				if ( obj instanceof Collection ) 
						setData((Collection)obj);
				else {
						Vector v = new Vector();
						v.addElement(obj);
						setData(v);
				}
		}		

		public void addValue(Object obj){
				// Get all the existing values 
	
				Vector v = new Vector(getData());				
				if ( !v.contains(obj) ){
					// 	if ( obj instanceof Persistible) 
// 								((Persistible)obj).addSaveListener(this);
						v.addElement(obj);
				}
				setData(v);
		}		

		public void removeValue(Object obj){
				// Get all the existing values 
				Vector v = new Vector(getData());				
				if ( v.contains(obj) ) {
						v.remove(obj);
				}
				setData(v);
		}		

		public DefaultListModel getListModel(){
				if ( list.getModel() == null || 
						 !(list.getModel() instanceof DefaultListModel) ) {
						DefaultListModel listModel = new DefaultListModel();
						list.setModel(listModel);
				}
				return (DefaultListModel)list.getModel();
		}		

		public Object getEditedObject() {
				return editObject;
		}

		public JList getList(){
				return list;
		}	
		public void clear() {
				DefaultListModel listModel = new DefaultListModel();
				list.setModel(listModel);
		}
		private void clearSelection() {
				list.clearSelection();
		}

    // Find a string in  tree
    public int findString(String searchWord) {
				int currentPosition = getList().getMaxSelectionIndex(); 
				if ( currentPosition < 0 ) 
						currentPosition = -1;
        return findString(currentPosition+1, searchWord);
    }

		public int findString(int position, 
													String searchWord) {
				// Loop through the list and return the position
				// of the list row that contains the search word
				ListModel model = getListModel();
				for ( int i = position; i < model.getSize(); i++) {
						String listWord = model.getElementAt(i).toString();
							if ( listWord!= null && 
								 listWord.toLowerCase().indexOf
									 (searchWord.toLowerCase()) > -1 ) {
								// match found
								getList().setSelectedIndex(i);
								return i;
						}
				}
				clearSelection();
				return -1;
		}

		public String toString() {
				if (label != null ) 
						return label;
				return getClass().toString();
		}

		public static void main(String[] args) {
				try {
						UIManager.setLookAndFeel(WINDOWS);
				}
				catch (Exception ex) {
						System.out.println("Failed loading L&F: ");
				}
				JFrame f = new JFrame();
				Collection codeBundles =
						oncotcap.Oncotcap.getDataSource().find(CodeBundle.class);
				OncScrollList p = new OncScrollList(codeBundles, true, true);
				f.getContentPane().add(p);
				f.addWindowListener
						(	new WindowAdapter() {
										public void windowClosing(WindowEvent e) {
												System.exit(0);
										}
								});
				f.setSize(500,500);
				f.setVisible(true);
		}

		public void refresh() {
				revalidate();
				list.revalidate();
		}

		public void objectSaved(SaveEvent e) {
//				System.out.println("Object Saved in list: " + getLinkTo()
//						+ " " + e.getSavedObject());
				if ( e.getSavedObject() != getLinkTo()) 
					return;
			
				DefaultListModel listModel = getListModel();
				int pos = listModel.getSize();
				// See if the item is already in the list 
				// if ( e.getSavedObject() instanceof Persistible) {
// 						((AbstractPersistible)e.getSavedObject()).addSaveListener(this);
// 				}
				if (listModel.contains(e.getSavedObject()) ) {
						pos = listModel.indexOf(e.getSavedObject());
						//System.out.println("WHICH ONC SCROLLIST " + getLabel());
						listModel.set(pos,e.getSavedObject());
				}
				else {
						if ( allowsMultiples() || pos == 0) {
								// Append an item
								listModel.add(pos,e.getSavedObject() );
						}
						else { // replace current element
								if ( !allowsMultiples() ) 
										pos = 0;
								listModel.set(pos, e.getSavedObject());
						}
				}
				getList().revalidate();
		}

		public void objectDeleted(SaveEvent e) {
				removeValue(e.getSavedObject());
				//e.getSavedObject().removeSaveListener(this);
		}

			// Drop target
		public void dragEnter(DropTargetDragEvent dtde) {}
		public void dragExit(DropTargetEvent dte) {}
		public void dragOver(DropTargetDragEvent dtde) {}
		public void drop(DropTargetDropEvent dtde) {
				Point pt = dtde.getLocation();
				try {
						Transferable t = clipboard.getContents(clipboard);
						DataFlavor stringFlavor = DataFlavor.stringFlavor;
						Transferable tr = dtde.getTransferable();
						DataFlavor flavors[] = t.getTransferDataFlavors();

						if(tr.isDataFlavorSupported(Droppable.droppableData))
								{
										Object droppedData = 
												(Droppable) tr.getTransferData(Droppable.droppableData);
									// 	System.out.println("dropped " + droppedData + " - " 
// 																			 + droppedData.getClass());	
										addValue(droppedData);

								}

				} catch(Exception ufe) {
						ufe.printStackTrace();
				}
				
		}
		public void dropActionChanged(DropTargetDragEvent dtde) {}
		public void dragGestureRecognized(DragGestureEvent dge) {
 				//System.out.println("Drag Gesture: " + dge);
		}

		class RemoveSelectedFromList extends AbstractAction {
				public RemoveSelectedFromList(String actionName) {
						super(actionName);
				}
				public void actionPerformed(ActionEvent e) {
						// What is selected
						Object [] selectedItems = list.getSelectedValues();
						// take them out the list
						for ( int i = 0; i < Array.getLength(selectedItems); i++) {
								removeValue(selectedItems[i]);
						}
				}
		}
}
