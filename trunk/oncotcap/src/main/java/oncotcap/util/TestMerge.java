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
package oncotcap.util;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import javax.swing.*;

import oncotcap.datalayer.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.storage.clips.*;
public class TestMerge implements ActionListener  {

		static String baselineKBPath = 
				"c:/Program Files/Protege-2000/roger.pprj";
		static String addOnKBPath = 
				"c:/Program Files/Protege-2000/bill.pprj";

		ProjectDescription [] projectDescs = new ProjectDescription[2];

		Project project1 = null;
		Project project2 = null;
		KnowledgeBase baselineKB = null;
		KnowledgeBase addOnKB = null;

		Vector addOnInstances = null;
		Vector dataSource2Guids = new Vector();
		Vector dataSourceGuids = new Vector();
		// Instances that are in base but not in add on
		Vector uniqueBaseInstances = new Vector();
		Vector uniqueBaseInstanceGUIDs = new Vector();
		// Instances that are in add on but not in base
		Vector uniqueAddOnInstances = new Vector();
		Vector uniqueAddOnInstanceGUIDs = new Vector();
		// Instances that are in both but with different modify times
		Vector overlapInstances = new Vector();
		JList newItemsProjectA = null;
		JList newItemsProjectB = null;
		JList overlapItems = null;
		JList updateItems = null;
		JFrame mainFrame = null;
		JTextField project1Path = null;
		JTextField project2Path = null;
		oncotcap.display.browser.BrowserNodeComparator comparator = 
			new oncotcap.display.browser.BrowserNodeComparator();

		public TestMerge(boolean reportMode) {
				init();
				mainFrame.setVisible(true);

		}

		private ProjectDescription openProject(String projectPath) {
				System.out.println("Opening project : " + projectPath);
			// Read in the baseline knowledge base which will be the 
			// target of the save
			ProtegeDataSource dataSource =
					new ProtegeDataSource(projectPath);
			ProjectDescription projectDesc = new ProjectDescription();
			projectDesc.project = dataSource.getProject();
			projectDesc.kb = dataSource.getProject().getKnowledgeBase();
			projectDesc.allInstances = 
					new Vector(projectDesc.kb.getInstances());
			projectDesc.name = projectPath;
			return projectDesc;
		}

		private void init() {
				mainFrame = new JFrame("Test Project Merge");
				mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				JPanel mainPanel= new JPanel(new BorderLayout());
				JPanel listPanel = new JPanel(new GridLayout(3,1));
				newItemsProjectA = new JList();

				JScrollPane pane1 = new JScrollPane(newItemsProjectA);
				ListMouseListener mouseListener = new ListMouseListener();
				ListMouseListener2 mouseListener2 = new ListMouseListener2();
				newItemsProjectA.setCellRenderer(new MyCellRenderer());
				newItemsProjectA.addMouseListener(mouseListener);
				JPanel panel1 = new JPanel(new BorderLayout());
				panel1.add(new Label("Unique to project 1"), BorderLayout.NORTH);
				panel1.add(pane1, BorderLayout.CENTER);

				newItemsProjectB = new JList();
				newItemsProjectB.setCellRenderer(new MyCellRenderer());
				newItemsProjectB.addMouseListener(mouseListener);
				JScrollPane pane2 = new JScrollPane(newItemsProjectB);
				JPanel panel2 = new JPanel(new BorderLayout());
				panel2.add(new Label("Items that do not exist in the base model"), BorderLayout.NORTH);
				panel2.add(pane2, BorderLayout.CENTER);

				overlapItems = new JList();
				overlapItems.setCellRenderer(new MyCellRenderer());
				overlapItems.addMouseListener(mouseListener2);
				JScrollPane pane3 = new JScrollPane(overlapItems);
				JPanel panel3 = new JPanel(new BorderLayout());
				panel3.add(new Label("Instances that exist in both projects w/ differing dates"), BorderLayout.NORTH);
				panel3.add(pane3, BorderLayout.CENTER);

				//listPanel.add(panel1);
				listPanel.add(panel2);
				listPanel.add(panel3);

				
				updateItems = new JList();
				updateItems.setCellRenderer(new MyCellRenderer());
				updateItems.addMouseListener(mouseListener);
				JButton addButton = new JButton("Add");
				addButton.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e)
								{
										addToUpdateList();
								}
						});
				JButton removeButton = new JButton("Remove");
				removeButton.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e)
								{
										removeFromUpdateList();
								}
						});
				JScrollPane pane4 = new JScrollPane(updateItems);
				JPanel panel4 = new JPanel(new BorderLayout());
				panel4.add(new Label("Update items"), BorderLayout.NORTH);
				panel4.add(pane4, BorderLayout.CENTER);
				JPanel panel6 = new JPanel();
				BoxLayout box = new BoxLayout(panel6, BoxLayout.Y_AXIS);
				panel6.setLayout(box);
				panel6.add(addButton);
				panel6.add(removeButton);
				panel4.add(panel6, BorderLayout.WEST);

				// Choose the two projects and run
				JPanel runPanel = new JPanel(new GridLayout(2,3));
				project1Path = new JTextField(baselineKBPath);
				project2Path = new JTextField(addOnKBPath);

				runPanel.add(new JLabel("Baseline Project Path"));
				runPanel.add(project1Path);
				JButton runButton = new JButton("Compare projects.");
				runButton.addActionListener(this);
				runPanel.add(runButton);
				runPanel.add(new JLabel("Other Project Path"));
				runPanel.add(project2Path);

				JPanel panel5 = new JPanel();
				JButton mergeButton = new JButton("Merge to base model");
				mergeButton.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e)
								{
										merge();
								}
						});
				JButton saveButton = new JButton("Save base model");
				saveButton.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e)
								{
										save();
								}
						});
				panel5.add(mergeButton);
				panel5.add(saveButton);
				mainPanel.add(runPanel, BorderLayout.NORTH);
				mainPanel.add(listPanel, BorderLayout.CENTER);
				mainPanel.add(panel4, BorderLayout.EAST);
				mainPanel.add(panel5, BorderLayout.SOUTH);

				mainFrame.getContentPane().add(mainPanel);
				mainFrame.setSize(600,600);

		}

		public void actionPerformed(ActionEvent e) {
				run(true);
		}

		public void run(boolean reportMode) {
				// Make sure there weren't open projects
				closeAllProjects();
				// Right now this can only get new instance differences until I can 
				// determine whether the modification-timestamp will be usable
				baselineKBPath = project1Path.getText();
				addOnKBPath = project2Path.getText();
				projectDescs[0] = openProject(baselineKBPath);
				projectDescs[1] = openProject(addOnKBPath);
				
		// 		Slot baseDateSlot = 
// 						projectDescs[0].kb.getSlot(":MODIFICATION-TIMESTAMP");
// 				Slot addOnDateSlot = 
// 						projectDescs[1].kb.getSlot(":MODIFICATION-TIMESTAMP");
				Slot baseDateSlot = projectDescs[0].kb.getSlot("modificationTime");
				Slot addOnDateSlot = projectDescs[1].kb.getSlot("modificationTime");

			// Now loop thru the models and build lists

			// Get instances ids keep guids in the same order as the instances
			Iterator ii = projectDescs[1].allInstances.iterator();
			for ( int j = 0; j < projectDescs[0].allInstances.size(); j++) {
					Instance instance = (Instance)projectDescs[0].allInstances.elementAt(j);
					String guid = instance.getName();
					dataSourceGuids.addElement(guid);
	// 	 			System.out.println(guid + 
//  														 " instance " + instance 
//  														 + " baseDateString " +
//  														 instance.getOwnSlotValue(baseDateSlot));
			}
			for ( int i = 0; i < projectDescs[1].allInstances.size(); i++) {
					String guid = ((Instance)projectDescs[1].allInstances.elementAt(i)).getName();
 					dataSource2Guids.addElement(guid);
// 					System.out.println(guid + " addOnDateString " +
// 			((Instance)projectDescs[1].allInstances.elementAt(i)).getOwnSlotValue(addOnDateSlot));
			}
			for ( int i = 0; i < dataSourceGuids.size(); i++) {
					String guid = (String)dataSourceGuids.elementAt(i);
					if ( dataSource2Guids.contains(guid) == false ) {
							if ( projectDescs[1].kb.getInstance(guid) instanceof Cls 
									 || projectDescs[1].kb.getInstance(guid) instanceof Slot) {
									// skip it
							}
							else {
									uniqueBaseInstances.addElement(projectDescs[0].kb.getInstance(guid));
									uniqueBaseInstanceGUIDs.addElement(guid);
							}
					}
					else {
							if ( projectDescs[1].kb.getInstance(guid) instanceof Cls 
									 || projectDescs[1].kb.getInstance(guid) instanceof Slot) {
									// skip it
							}
							else 
									overlapInstances.addElement(projectDescs[1].kb.getInstance(guid));	
					}
			}
			for ( int j = 0; j < dataSource2Guids.size(); j++) {
					String guid = (String)dataSource2Guids.elementAt(j);
					if ( dataSourceGuids.contains(guid) == false ) {
							uniqueAddOnInstances.addElement(projectDescs[1].kb.getInstance(guid));
							uniqueAddOnInstanceGUIDs.addElement(guid);
					}
					else {
							if ( overlapInstances.contains(guid) == false ) // no duplicates
									overlapInstances.addElement(guid);	
					}
			}
			System.out.println("uniqueAddOnInstances " + uniqueAddOnInstances.size());
			System.out.println("overlapInstances " + overlapInstances.size());
			

			Map valueMap = null;
			if (valueMap == null) {
					valueMap = ModelUtilities.createValueMap(projectDescs[1].kb, projectDescs[0].kb);
			}	
			// Add addOnInstances to baseInstances
			Iterator iter = uniqueAddOnInstanceGUIDs.iterator();
			while ( iter.hasNext() ) {
					String guid = (String)iter.next();
					Instance addOnInstance =
							projectDescs[1].kb.getInstance(guid);

					// Create new named instances of each add on class 
					Cls copyCls = (Cls) valueMap.get(addOnInstance.getDirectType());
					Instance copiedInstance = projectDescs[0].kb.createInstance(guid, copyCls);
			}

			//	copyValues(uniqueAddOnInstanceGUIDs);

			// Determine which overlap instances need to be copied - based on 
			// lastModified Date/Time
			Iterator overIter = overlapInstances.iterator();
			Vector addOverlapInstances = new Vector();
			Vector addOverlapInstanceNames = new Vector();
			//Slot baseDateSlot =  projectDescs[0].kb.getSlot(":MODIFICATION-TIMESTAMP");
			//System.out.println("baseDateSlot " + baseDateSlot);
			//Slot addOnDateSlot =  projectDescs[1].kb.getSlot(":MODIFICATION-TIMESTAMP");
			SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			//System.out.println("df " + df);
			Instance addOnInstance = null;
			System.out.println("Planned update for base model instances: ");
			while ( overIter.hasNext() ) {
					Object o = overIter.next();
					//System.out.println("o -- >> " + o);
					if ( o instanceof Instance ) 
							addOnInstance = (Instance)o;
					if ( addOnInstance == null ||
							 addOnInstance instanceof Cls ||
							 addOnInstance instanceof Slot ||
							 addOnInstance instanceof Facet)
							continue; // only copy over instance frames
					String guid = addOnInstance.getName();
					//System.out.println("addOnInstance " + addOnInstance);
					Instance baseInstance = projectDescs[0].kb.getInstance(guid);
					//Compare dates 
					String baseDateString = (String)baseInstance.getOwnSlotValue(baseDateSlot);
					String addOnDateString = 
							(String)addOnInstance.getOwnSlotValue(addOnDateSlot);
		// 			System.out.println("FIRST " + addOnInstance
// 														 + " , " + baseDateString 
// 														 + " , " + addOnDateString);
					Date baseDate = null;
					Date addOnDate  = null;
					try {
							if ( baseDateString != null ) 
									baseDate = df.parse(baseDateString);
							if ( addOnDateString != null ) 
									addOnDate = df.parse(addOnDateString);
					} catch (ParseException pe) {
							System.out.println("ParseException date " + baseDateString 
																 + " addOnDate " + baseDate);
							pe.printStackTrace();
					}
					if ( baseDate == null && addOnDate == null) {
					}
					if ( (baseDate == null && addOnDate != null) 
							 || ( baseDate != null && addOnDate != null &&
										addOnDate.after(baseDate)) ) {
							// Make sure it isn't already in the list
							if (	addOverlapInstanceNames.contains(addOnInstance.getName())
										== false ) {
									System.out.println("IN BOTH " + addOnInstance.getName()
																		 + " , " + baseDate 
																		 + " , " + addOnDate);
									addOverlapInstanceNames.addElement(addOnInstance.getName());
									addOverlapInstances.addElement(addOnInstance);
										
							}
					}
			}
			// Write report on model diferences
	 		//System.out.println("overlapInstances " + overlapInstances);
			System.out.println("addOverlapInstances " + addOverlapInstances.size());
			overlapItems.setListData(addOverlapInstances);
			//System.out.println("baseInstances " + uniqueBaseInstances);
			newItemsProjectA.setListData(uniqueBaseInstances);
			System.out.println("uniqueBaseInstances " + uniqueBaseInstances.size());
			//System.out.println("addOnInstances " + uniqueAddOnInstanceGUIDs);
			newItemsProjectB.setListData(uniqueAddOnInstances);
			System.out.println("uniqueAddOnInstances " + uniqueAddOnInstances.size());


			//copyValues(addOverlapInstanceNames);
			if ( reportMode == false ) {
					try {
							//project1.save(new ArrayList());
					}
					catch (Exception e) {
							System.out.println("Unable to save project file.");
					}
			}


		}
		
		private void save() {
				try {
						System.out.println("Saving to " + 	projectDescs[0].project);
						ArrayList arrayList = new ArrayList();
						projectDescs[0].project.save(arrayList);
						System.out.println("ArrayList " + arrayList);
				}
				catch (Exception e) {
						System.out.println("Unable to save project file.");
				}
		}

		private void merge() {
				Vector updateItemNames = new Vector();
				// Get the guids from the list of selected items
				ListModel model = updateItems.getModel();
				for ( int i = 0; i < model.getSize(); i++) {
						String guid = ((Instance)model.getElementAt(i)).getName();
						updateItemNames.addElement(guid);
				}
				copyValues(updateItemNames);
		}
		private void addToUpdateList() {
				// Get all the selected items from the two left hand lists and move to right hand
				//list
				Object [] topValues = newItemsProjectB.getSelectedValues();
				Object [] bottomValues = overlapItems.getSelectedValues();
		
				for ( int i = 0; i < Array.getLength(topValues); i++) {
						addValue(updateItems, topValues[i]);
						removeValue(newItemsProjectB, topValues[i]);
				}
				for ( int i = 0; i < Array.getLength(bottomValues); i++) {
						addValue(updateItems, bottomValues[i]);
						removeValue(overlapItems, bottomValues[i]);
				}

		}

		private void removeFromUpdateList() {
				// Get all the selected items from the two left hand lists and move to right hand
				//list
				Object [] topValues = updateItems.getSelectedValues();
		
				for ( int i = 0; i < Array.getLength(topValues); i++) {
						removeValue(updateItems, topValues[i]);
				}

		}

		public void addValue(JList list, Object obj){
				// Get all the existing values 
	
				Vector v = new Vector(getData(list));				
				if ( obj instanceof String ) {
						// make sure there isn't a matching string already in the list
				}
				if ( !v.contains(obj) )
						v.addElement(obj);
				setData(list, v);
		}		

		public void removeValue(JList list, Object obj){
				// Get all the existing values 
				Vector v = new Vector(getData(list));				
				if ( v.contains(obj) ) {
						v.remove(obj);
				}
				setData(list, v);
		}		

		public Collection getData(JList list) {
				Vector data = new Vector();
				ListModel model = list.getModel();
				for ( int i = 0; i < model.getSize(); i++) {
						data.addElement(model.getElementAt(i));
				}
				return data;
		}

		public void setData(JList list, Collection items) {
				DefaultListModel listModel = getListModel(list);
				listModel.clear();
				Object obj = null;
				Collection sortedItems = sort(items);
				Iterator i = sortedItems.iterator();
				while ( i.hasNext()) {
						obj = i.next();
						if ( !listModel.contains(obj) )
								listModel.addElement(obj);
				}
				list.setModel(listModel);
				//list.revalidate();
		}
		public Vector sort(Collection collection) {
			Object[] objs = collection.toArray();
			Arrays.sort(objs, comparator);
			return oncotcap.util.CollectionHelper.arrayToVector(objs);
		}
		public DefaultListModel getListModel(JList list){
				if ( list.getModel() == null || 
						 !(list.getModel() instanceof DefaultListModel) ) {
						DefaultListModel listModel = new DefaultListModel();
						list.setModel(listModel);
				}
				return (DefaultListModel)list.getModel();
		}		

		private void  closeAllProjects() {
				if ( 	projectDescs[0] != null && 
							projectDescs[0].project != null )
						projectDescs[0].project.dispose();
				if ( 	projectDescs[1] != null && 
							projectDescs[1].project != null )
						projectDescs[1].project.dispose();
		}

		public static void main(String[] args) {
				if ( args.length > 1) {
						baselineKBPath = args[0];
						addOnKBPath = args[1];
				}
				else {
						System.out.println(
															 "syntax: java oncotcap.util.Merge"
															 + " baselineKbPath addOnKbPath");
						System.out.println("Please provide Protege KB Path names.");
						//System.exit(0);
				}
				boolean reportMode = true;
				if ( args.length > 2 && "report".equals(args[2]) ) {
						// Run utility in report mode
						reportMode = true;
				}
				TestMerge t = new TestMerge(reportMode);
		}
		
		private void copyProject(String modelName) {
				// Create new project name
				String filePath = "C:/Program Files/Protege-2000/NewProject1";
				project1.setProjectFilePath(filePath + ".pprj");
				ClipsKnowledgeBaseFactory.setSourceFiles(project1.getSources(), 
																								 filePath+".pont", 
																								 filePath+".pins");
		}

		private void copyValues(Vector instancesToCopy) {
			// Now fill up the new instances
			Iterator addOns = instancesToCopy.iterator();
			while ( addOns.hasNext() ) {
					String guid = (String)addOns.next();
					Instance addOnInstance =
							projectDescs[1].kb.getInstance(guid);
					Instance copiedInstance =
							projectDescs[0].kb.getInstance(guid);
	 				System.out.println("copiedInstance " + copiedInstance);
					
					//addOnInstance.deepCopy(projectDescs[0].kb, null);
					// /* -----------
					// Get all the slots in the addOnInstance
					Collection addOnSlots = addOnInstance.getOwnSlots();
					Iterator slotsIterator = addOnSlots.iterator();
					Slot localSlot = null;
					while ( slotsIterator.hasNext() ) {
							// For each slot copy the values into the new instance
							Slot slot = (Slot)slotsIterator.next();
							
							// Make sure the slot exists in the base model
							if ( (localSlot = projectDescs[0].kb.getSlot(slot.getName())) == null) {
									System.out.println("Slot does not exist in project 1 " + slot);
									continue;
							}
							System.out.println("localSlot " + localSlot 
																 + " slot " + slot);
							Vector slotValues = 
									new Vector(addOnInstance.getOwnSlotValues(slot));
							// Translate the sourceSlotValues to their corresponding
							// values in the projectDescs[0].kb
							Iterator valueIter = slotValues.iterator();
							Vector localValues = new Vector();
							Object localValue = null;
							while ( valueIter.hasNext() ) {
									Object sourceValue = valueIter.next();
									if ( sourceValue instanceof Instance ) {
											localValue =
													projectDescs[0].kb.getInstance(((Instance)sourceValue).getName());
											if ( localValue == null ) 
													System.out.println("value does not EXIST FOR " + 
																						 ((Instance)sourceValue).getName());
											localValues.addElement(localValue);
									}
									else if ( sourceValue instanceof Cls) {
											localValue = 
													projectDescs[0].kb.getCls(((Cls)sourceValue).getName());
											localValues.addElement(localValue);
									}
									else 
											localValues.addElement(sourceValue);
									
							}
							Object slotValue = null;
							if ( localValues.size() > 0 )
									localValue = localValues.firstElement();
							try { 
									System.out.println("values: " + localValues);
									if (copiedInstance.getOwnSlotAllowsMultipleValues(localSlot))
											copiedInstance.setOwnSlotValues(localSlot, 
																											localValues);
									else
											copiedInstance.setOwnSlotValue(localSlot, 
																										 localValue);
							} catch (Exception e ) {
									System.out.println("Skipping localSlot " + localSlot 
																		 + " slot " + slot);
							}
					}
					//*/ //-------
			}
	}
		

class MyCellRenderer extends JLabel implements ListCellRenderer {
     // This is the only method defined by ListCellRenderer.
     // We just reconfigure the JLabel each time we're called.

     public Component getListCellRendererComponent(
       JList list,
       Object value,            // value to display
       int index,               // cell index
       boolean isSelected,      // is the cell selected
       boolean cellHasFocus)    // the list and the cell have the focus
     {
				 // Display the instance browser text
				 if ( value instanceof Instance) {
						 Instance currentInstance = (Instance)value;
						 String s = currentInstance.getBrowserText();
						 setText(s);
				 }
				 else if ( value instanceof String ) {
						 setText((String)value);
				 }
						 
				 if (isSelected) {
             setBackground(list.getSelectionBackground());
						 setForeground(list.getSelectionForeground());
				 }
         else {
						 setBackground(list.getBackground());
						 setForeground(list.getForeground());
				 }
				 setEnabled(list.isEnabled());
				 setFont(list.getFont());
         setOpaque(true);
         return this;
     }
 }

		class ListMouseListener extends MouseAdapter {
				JComponent protegeForm = null;
				public void mouseClicked(MouseEvent e) {
						String frameName = new String();
						if (e.getClickCount() == 2) {
								JList list = (JList)e.getSource();
								int index =list.locationToIndex(e.getPoint());
								Object value = list.getModel().getElementAt(index);
								System.out.println("what kind of object is this " + 
																	 value + value.getClass());
								if ( value instanceof Instance ) {
										// Determine what project this instance exists in 
								
										if ( list == newItemsProjectA) {
												frameName = projectDescs[0].name + " " + value;
												protegeForm =
														(JComponent)projectDescs[0].project.createRuntimeClsWidget((Instance)value);
										}
										else {
												frameName = projectDescs[1].name + " " + value;
												protegeForm = 
														(JComponent)projectDescs[1].project.createRuntimeClsWidget((Instance)value);
										}
										JScrollPane editorView = new JScrollPane(protegeForm);
										JFrame editorFrame = new JFrame(frameName);
										editorFrame.getContentPane().add(editorView);
										editorFrame.setSize(400,600);
										editorFrame.setVisible(true);
								}
								else 
										System.out.println("do nothing");

						}
				}
		}

		class ListMouseListener2 extends MouseAdapter {
				JComponent protegeForm2 = null;
				JComponent protegeForm = null;
				public void mouseClicked(MouseEvent e) {
						String frameName = new String();
						if (e.getClickCount() == 2) {
								JList list = (JList)e.getSource();
								int index =list.locationToIndex(e.getPoint());
								Object value = list.getModel().getElementAt(index);
								System.out.println("what kind of object is this " + 
																	 value + value.getClass());
								if ( value instanceof Instance ) {
										// This instance is in both projects pop up both
										// Determine what project this instance exists in 
										String instanceName = ((Instance)value).getName();
										protegeForm =
														(JComponent)projectDescs[0].project.createRuntimeClsWidget
												(projectDescs[0].kb.getInstance(instanceName));
										protegeForm2 =
												(JComponent)projectDescs[1].project.createRuntimeClsWidget
												(projectDescs[1].kb.getInstance(instanceName));
							
										JScrollPane editorView = new JScrollPane(protegeForm);
										JScrollPane editorView2 = new JScrollPane(protegeForm2);
										JLabel label = new JLabel("VALUE in PROJECT 1");
										JLabel label2 = new JLabel("VALUE in PROJECT 2");
										JPanel panel = new JPanel(new BorderLayout());
										JPanel panel2 = new JPanel(new BorderLayout());
										panel.add(label, BorderLayout.NORTH);
										panel2.add(label2, BorderLayout.NORTH);
										panel.setPreferredSize(new Dimension(400,600));
										panel2.setPreferredSize(new Dimension(400,600));
										panel.add(editorView, BorderLayout.CENTER);
										panel2.add(editorView2, BorderLayout.CENTER);

										JFrame editorFrame = new JFrame("VALUE in PROJECT 1");
										editorFrame.getContentPane().setLayout(new BorderLayout());
										editorFrame.getContentPane().add(panel, BorderLayout.WEST);
										editorFrame.getContentPane().add(panel2, BorderLayout.EAST);
										editorFrame.setSize(900,600);
										editorFrame.setVisible(true);
								}
								else 
										System.out.println("do nothing");

						}
				}
		}

		class ProjectDescription {
				public Project project = null;
				public KnowledgeBase kb = null;
				public Vector allInstances = new Vector();
				public String name = new String();
		}
}
