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

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;

import java.lang.reflect.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import javax.accessibility.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.colorchooser.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;

import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.*;
import javax.swing.tree.DefaultMutableTreeNode;

import oncotcap.datalayer.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.storage.clips.*;
import edu.stanford.smi.protege.widget.*;
import edu.stanford.smi.protege.util.*;

public class GenerateModelSource implements ActionListener  {

		static String kbPath = 
				"C:/Program Files/Protege-2000/Task2Model2.pprj";

		ProjectDescription [] projectDescs = new ProjectDescription[2];
		ProtegeDataSource dataSource = null;
		Project project1 = null;
		Project project2 = null;
		KnowledgeBase baselineKB = null;

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
		JFrame mainFrame = null;
		JTextField project1Path = null;
		JTextField project2Path = null;
		JTree tree = null;
		Hashtable treeHashtable = null;
		Slot ATTRIBUTE_NAME_SLOT;
		Slot DS_ATTRIBUTE_NAME_SLOT;
		Slot DS_ATTRIBUTE_SLOT;
		Slot GET_METHOD_NAME_SLOT;
		Slot SET_METHOD_NAME_SLOT;
		Slot CLS_ATTRIBUTE_TYPE_SLOT;
		Slot CLS_ATTRIBUTE_SUBTYPE_SLOT;
		Slot DATA_SOURCE_OBJECT_LIST_SLOT ;
		Slot JAVA_CLASS_NAME_SLOT;
		Slot CLASS_AND_PACKAGE_SLOT;
		Slot NAME_SLOT;
		Slot ICON_SLOT;
		Slot SPECIAL_NAME_SLOT;
		Slot ATTRIBUTE_LIST_SLOT;
		Slot PROTEGE_CLASS_SLOT;
		Cls PERSISTIBLE_CLS;
		Cls BROWSER;
		Cls BROWSER_WITH_KEYWORD;
		Vector javaClasses = null;
		Hashtable metaDataHashtable = new Hashtable();
		Hashtable createMetaDataHashtable = new Hashtable();
		Vector dataSourceObjects = new Vector();
		Cls systemClass;
		Vector slotsToIgnore = new Vector();
		public GenerateModelSource(boolean reportMode) {
				run();
		}

		private ProjectDescription openProject(String projectPath) {
				
			// Read in the baseline knowledge base which will be the 
			// target of the save
			dataSource =
					new ProtegeDataSource(projectPath);
			ProjectDescription projectDesc = new ProjectDescription();
			projectDesc.project = dataSource.getProject();
			projectDesc.kb = dataSource.getProject().getKnowledgeBase();
			projectDesc.allInstances = 
					new Vector(projectDesc.kb.getInstances());
			projectDesc.allClasses = 
					new Vector(projectDesc.kb.getClses());

			projectDesc.name = projectPath;
			return projectDesc;
		}

		public void actionPerformed(ActionEvent e) {
				run();
				JFrame f = new JFrame();
				JPanel p = new JPanel();
				f.getContentPane().add(p);
				f.setSize(500,500);
				f.setVisible(true);
		}

		public void run() {
				// Make sure there weren't open projects
				closeAllProjects();
				// Right now this can only get new instance differences until I can 
				// determine whether the modification-timestamp will be usable
				projectDescs[0] = openProject(kbPath);
				systemClass = projectDescs[0].kb.getCls(":SYSTEM-CLASS");				
				// CReate a list of slots that are inherited or otherwise unnecessary
				slotsToIgnore.addElement(projectDescs[0].kb.getSlot("keywords"));
				//slotsToIgnore.addElement(projectDescs[0].kb.getSlot("keyword"));
				slotsToIgnore.addElement(projectDescs[0].kb.getSlot("parentKeyword"));
				slotsToIgnore.addElement(projectDescs[0].kb.getSlot(":MODIFIER"));
				//slotsToIgnore.addElement(projectDescs[0].kb.getSlot("creator"));
				//slotsToIgnore.addElement(projectDescs[0].kb.getSlot("creationTime"));
				//slotsToIgnore.addElement(projectDescs[0].kb.getSlot("modifier"));
				//slotsToIgnore.addElement(projectDescs[0].kb.getSlot("modificationTime"));
				slotsToIgnore.addElement(projectDescs[0].kb.getSlot("oncotcapOntologyVersion"));
				slotsToIgnore.addElement(projectDescs[0].kb.getSlot("child_keywords"));
				slotsToIgnore.addElement(projectDescs[0].kb.getSlot(":MODIFICATION-TIMESTAMP"));
				slotsToIgnore.addElement(projectDescs[0].kb.getSlot(":CREATION-TIMESTAMP"));

				// Setup some frequently used slots
				ATTRIBUTE_NAME_SLOT = projectDescs[0].kb.getSlot("attributeName");
				DS_ATTRIBUTE_NAME_SLOT = projectDescs[0].kb.getSlot("dataSourceAttributeName");
				DS_ATTRIBUTE_SLOT = projectDescs[0].kb.getSlot("dataSourceAttribute");
				GET_METHOD_NAME_SLOT = projectDescs[0].kb.getSlot("getMethodName");
				SET_METHOD_NAME_SLOT = projectDescs[0].kb.getSlot("setMethodName");
				CLS_ATTRIBUTE_TYPE_SLOT = projectDescs[0].kb.getSlot("clsAttributeType");
				CLS_ATTRIBUTE_SUBTYPE_SLOT = projectDescs[0].kb.getSlot("clsAttributeSubType");
				DATA_SOURCE_OBJECT_LIST_SLOT  = projectDescs[0].kb.getSlot("dataSourceObjectList");
				JAVA_CLASS_NAME_SLOT = projectDescs[0].kb.getSlot("javaClassName");
				CLASS_AND_PACKAGE_SLOT= projectDescs[0].kb.getSlot("classAndPackage");
				NAME_SLOT = projectDescs[0].kb.getSlot("name");
				ICON_SLOT = projectDescs[0].kb.getSlot("iconName");
				SPECIAL_NAME_SLOT = projectDescs[0].kb.getSlot(":NAME");
				PROTEGE_CLASS_SLOT = projectDescs[0].kb.getSlot("protegeClass");
				ATTRIBUTE_LIST_SLOT = projectDescs[0].kb.getSlot("attributeList");
				PERSISTIBLE_CLS = projectDescs[0].kb.getCls("Persistible");
				BROWSER = projectDescs[0].kb.getCls("BrowserNode");
				BROWSER_WITH_KEYWORD = projectDescs[0].kb.getCls("BrowserNodeWithKeywords");

				// Make sure there is not already model builder data for this class
				javaClasses = 
						new Vector(projectDescs[0].kb.getInstances(projectDescs[0].kb.getCls("JavaClass")));
				Iterator i = javaClasses.iterator();

				// Pre populate the hashtable with existing protege java classes
				while ( i.hasNext() ) {
						Instance inst = (Instance)i.next();
						metaDataHashtable.put(inst.getOwnSlotValue(NAME_SLOT), inst);
				}
				Vector dsos = 
						new Vector(projectDescs[0].kb.getInstances
											 (projectDescs[0].kb.getCls("DataSourceObject")));
				Iterator ii = dsos.iterator();

				// Pre populate the hashtable with existing data source objects
				while ( ii.hasNext() ) {
						Instance inst = (Instance)ii.next();
						Cls pCls = (Cls)inst.getOwnSlotValue(PROTEGE_CLASS_SLOT);
						dataSourceObjects.addElement(pCls.getName());
				}

				// Loop thru every non system class and 
				Iterator iii = projectDescs[0].allClasses.iterator();
				while ( iii.hasNext() ) {
						Cls cls = (Cls)iii.next();
						writeUIClass(cls);

						 writePersistibleClass(cls);
						 createModelBuilderJavaClass(cls); 
						// need to do this first so all classes 
						// exist before the process is started
				}
				for (Enumeration e = createMetaDataHashtable.keys();
						 e.hasMoreElements(); ) {
						Cls cls = projectDescs[0].kb.getCls((String)e.nextElement());
				
 						updateModelBuilderMetaData(cls);
				}
				//saveProject();
		}


		private void writeUIClass(Cls cls) {
				if ( cls.hasSuperclass(systemClass) || "JavaClass".equals(cls.getName()) )
								return;
				if ( StringHelper.isValidJavaName(cls.getName()) == false) {
						System.out.println("Invalid java Class name string: " + cls.getName());
						return;
				}
				System.out.println("Processing Class... " + cls);
		// 		ClsWidget widget = projectDescs[0].project.getDesignTimeClsWidget(cls);
// 				WidgetDescriptor wd = widget.getDescriptor();
	// 			System.out.println("ClsWidget " + widget);
// 				System.out.println("WidgetDescriptor " + wd);
 
				// Create a file
				String filePathName = "C:\\temp\\oncotcap\\gui\\panel\\" + cls.getName() + "Panel.java";
				FileWriter writer = openFileForWrite(filePathName);
				write(writer, "package oncotcap.display.common.panel;\n");
				write(writer, "import java.util.*;");
				write(writer, "import oncotcap.datalayer.*;");
				write(writer, "import oncotcap.display.common.*;");
				write(writer, "import javax.swing.*;");
				write(writer, "import oncotcap.datalayer.persistible.*;");
				write(writer, "import oncotcap.datalayer.autogenpersistible.*;");

				write(writer, "\n");
				write(writer, "public class " + 
													 cls.getName() + "Panel extends DefaultEditorPanel");
				write(writer, "{");
				//write(writer, "\tpublic  " +  cls.getName() + " editObj = null;");
 				declareInputFields(writer, cls);
				write(writer, "\n");
				write(writer, "\tpublic  " +  cls.getName() + "Panel() {"); 
				write(writer, "\t\tsuper();"); 
				write(writer, "\t\teditObj = new " + cls.getName() + "();"); 
				write(writer, "\t\tinitUI();"); 
				write(writer, "\t\tfillUiHashtable();"); 

				write(writer, "\t}"); 

				write(writer, "\n");
				write(writer, "\tpublic  " +  cls.getName() + "Panel(" +
							cls.getName() + " editObj) {"); 
				write(writer, "\t\tsuper();"); 
				write(writer, "\t\tthis.editObj = editObj;"); 
				write(writer, "\t\tinitUI();"); 
				write(writer, "\t\tfillUiHashtable();"); 

				write(writer, "\t}"); 
				write(writer, "\tpublic void initUI(Object editObj) {");
				write(writer, "\tthis.editObj = (" + cls.getName() +
							")editObj;");
				write(writer, "\tinitUI();");
				write(writer, "\t}");
				
				write(writer, "\tprivate void initUI() {"); 
 				writeInputFields(writer, cls);
				write(writer, "\t}");

 				writeUiHashtable(writer, cls);
// 				write(writer, "\tpublic void edit(Object objectToEdit){}");
// 				write(writer, "\tpublic void save(){}");
 				write(writer, "\tpublic Object getValue(){ return null; }");
				write(writer, "\tpublic static void main(String[] args) {");
				write(writer, "\t\tJFrame f = new JFrame();");
				write(writer, "\t\t" + cls.getName() + "Panel p = new " 
													 + cls.getName() + "Panel();");
				write(writer, "\t\tf.getContentPane().add(p);");
				write(writer, "\t\tf.setSize(500,500);");
				write(writer, "\t\tf.setVisible(true);");
				write(writer, "\t}");
				write(writer, "}");
				closeFile(writer);
		}

		private void createModelBuilderJavaClass(Cls cls) {
				if ( metaDataHashtable.get(cls.getName()) != null ) {
						return;
				}
				if ( cls.hasSuperclass(systemClass)  || "JavaClass".equals(cls.getName())
						 || "ModelBuilderData".equals(cls.getName()))
						return;
				if ( StringHelper.isValidJavaName(cls.getName()) == false) {
						System.out.println("Invalid java Class name string: " + cls.getName());
						return;
				}

				System.out.println("Creating a Java Class for " + cls);
				//cls.addDirectSuperclass(PERSISTIBLE_CLS);
				Instance javaClass =
						projectDescs[0].kb.createInstance((new GUID()).toString(),
																							projectDescs[0].kb.getCls("JavaClass"));
				javaClass.setOwnSlotValue(NAME_SLOT, cls.getName());
				javaClass.setOwnSlotValue(CLASS_AND_PACKAGE_SLOT, 
																	"oncotcap.datalayer.autogenpersistible.".concat(cls.getName()));	
				createMetaDataHashtable.put(cls.getName(), javaClass);
				metaDataHashtable.put(cls.getName(), javaClass);
		}

		private void updateModelBuilderMetaData(Cls cls) {
				System.out.println("Processing Class... " + cls);	
				if (dataSourceObjects.contains(cls.getName()) == true)
						return;


				// Create a DataSourceMap
				Instance dataSourceMap =
						projectDescs[0].kb.createInstance((new GUID()).toString(),
															projectDescs[0].kb.getCls("DataSourceMap"));
				dataSourceMap.setOwnSlotValue(JAVA_CLASS_NAME_SLOT, 
																	"oncotcap.datalayer.autogenpersistible.".concat(cls.getName()));
				// Create DataSourceObject
				Instance dataSourceObject =
						projectDescs[0].kb.createInstance((new GUID()).toString(),
															projectDescs[0].kb.getCls("DataSourceObject"));

				//create attribute map
				Vector attrMaps = createAttributeMap(cls);
				dataSourceObject.setOwnSlotValues(ATTRIBUTE_LIST_SLOT,attrMaps);
				dataSourceObject.setOwnSlotValue(PROTEGE_CLASS_SLOT, 
																			cls);

				dataSourceMap.setOwnSlotValue(DATA_SOURCE_OBJECT_LIST_SLOT, dataSourceObject);

		}


		private Vector createAttributeMap(Cls cls) {
				Vector attributeMaps = new Vector();
				// Loop thru every slot
				Collection slots = cls.getTemplateSlots();
				Iterator ii = slots.iterator();
				while ( ii.hasNext() ) {
						Slot slot = (Slot)ii.next();
						if ( slotsToIgnore.contains(slot) )
								continue;
						if ( StringHelper.isValidJavaName(slot.getName()) == false) {
								System.out.println("Invalid java field name string: " + slot.getName());
								continue;
						}
						Instance attributeMap =
								projectDescs[0].kb.createInstance((new GUID()).toString(),
																	projectDescs[0].kb.getCls("DataSourceAttributeMap"));
						attributeMap.setOwnSlotValue(ATTRIBUTE_NAME_SLOT, slot.getName());
						attributeMap.setOwnSlotValue(DS_ATTRIBUTE_NAME_SLOT, slot.getName());
						attributeMap.setOwnSlotValue(DS_ATTRIBUTE_SLOT, slot);
						attributeMap.setOwnSlotValue(GET_METHOD_NAME_SLOT, 
																				 "get" + StringHelper.capitalize(slot.getName()));
						attributeMap.setOwnSlotValue(SET_METHOD_NAME_SLOT, 
																				 "set" + StringHelper.capitalize(slot.getName()));
						String type = getType(slot, false);
						String subType = null;
						if ( "DefaultPersistibleList".equals(type) ) {
								type = "PersistibleList";
								subType = getType(slot, true);
						}
						Instance typeJavaClass;
						Instance subTypeJavaClass;
						if ( type != null ) {

								 typeJavaClass = (Instance)metaDataHashtable.get(type);
								 //								 System.out.println("TYPE " + type + " " + typeJavaClass);
								 if ( typeJavaClass != null)
										 attributeMap.setOwnSlotValue(CLS_ATTRIBUTE_TYPE_SLOT, 
																									typeJavaClass);
						}
						if ( subType != null ) {
								Object o = metaDataHashtable.get(subType);
								if (o != null) {
										subTypeJavaClass = (Instance)o;
										if ( subTypeJavaClass != null)
												attributeMap.setOwnSlotValue(CLS_ATTRIBUTE_SUBTYPE_SLOT, 
																										 subTypeJavaClass);	
								}
						}
						attributeMaps.addElement(attributeMap);
				}
				return attributeMaps;
		}

		private void writePersistibleClass(Cls cls) {
				if ( cls.hasSuperclass(systemClass) )
								return;
				if ( StringHelper.isValidJavaName(cls.getName()) == false) {
						System.out.println("Invalid java Class name string: " + cls.getName());
						return;
				}
				// See if this has a java class record already if so 
				// get pretty name and icon
				
				System.out.println("Processing Class... " + cls);
				// Create a file
				String filePathName = "C:\\temp\\oncotcap\\modelx\\" + cls.getName() + ".java";
				FileWriter writer = openFileForWrite(filePathName);
				write(writer,"package oncotcap.datalayer.autogenpersistible;");
				write(writer,"import oncotcap.datalayer.*;");
				write(writer,"import oncotcap.display.common.panel.*;");
				write(writer,"import oncotcap.datalayer.persistible.*;");
				write(writer,"import javax.swing.*;");
				write(writer,"import java.lang.reflect.*;");
				write(writer,"\n");
				// Determine the proper class to extend
				if ( cls.hasSuperclass(BROWSER) || cls.hasSuperclass(BROWSER_WITH_KEYWORD)) {
						if ( !cls.hasDirectSuperclass(BROWSER) &&  
								 !cls.hasDirectSuperclass(BROWSER_WITH_KEYWORD)) {
								Vector superClasses = new Vector(cls.getDirectSuperclasses());
								// Take the first one
								Cls superCls  = null;
								if ( superClasses.size() > 0 )
										superCls = (Cls)superClasses.firstElement();
								write(writer,"public class " 
											+ cls.getName() 
											+ " extends " + superCls.getName());
						
						}
						else {
								write(writer,"public class " 
											+ cls.getName() 
											+ " extends AutoGenPersistibleWithKeywords ");
						}
				}
				else {
						write(writer,"public class " 
									+ cls.getName() 
									+ " extends AutoGenPersistibleWithKeywords ");
				}
				Vector allSuperClasses = new Vector(cls.getSuperclasses());
				if ( allSuperClasses.contains(BROWSER_WITH_KEYWORD) ) {
						write(writer,
									" implements oncotcap.display.common.tree.TreeBrowserNode ");
				}
				write(writer," {");
				declareSlots(writer, cls);
			
				// Get the image icon - there is definitely a better way but 
				// brute force works just fine
				Iterator i = javaClasses.iterator();
				boolean hasIcon = false;
				while ( i.hasNext() ) {
						Instance inst = (Instance)i.next();
						if ( inst.getOwnSlotValue(NAME_SLOT).equals(cls.getName()) ) {
								System.out.println("INSTANCE: " + inst);
								System.out.println("ICON_SLOT: " + ICON_SLOT);
								String iconName = (String)inst.getOwnSlotValue(ICON_SLOT);
								if ( ifNull(iconName) != null ) {
										write(writer,"public ImageIcon icon =	");
										write(writer,
													"\toncotcap.utilities.OncoTcapIcons.getImageIcon(\"" 
													+ iconName + "\");");
										hasIcon = true;
								}
								break;
						}
				}
			
				write(writer,"\n");
				write(writer,"public " 
							+ cls.getName() 
							+ "() {");
				writeMethodMaps(writer, cls);

				writeGetters(writer, cls);
				writeSetters(writer, cls);
				writeToString(writer, cls);
				write(writer,"\tpublic Class getPanelClass()");
				write(writer,"\t{");
				write(writer,"\t\treturn " + cls.getName() + "Panel.class;");
				write(writer,"\t}");

				write(writer,"\tpublic String getPrettyName()");
				write(writer,"\t{");
				write(writer,"\t\treturn \"" + cls.getName() + "\";");
				write(writer,"\t}");

				write(writer,"\tpublic ImageIcon getIcon()");
				write(writer,"\t{");
				write(writer,"\t\treturn icon;");
				write(writer,"\t}");
				write(writer,"}");


				closeFile(writer);
		}

	/** 
	 * sets value to null if the value contains the string "null" or is empty
	 * @param  originalString 
	 * @return original string or null
	 */
	private String ifNull(String originalString) {
		if (originalString == null
			|| "null".equals(originalString)
			|| (originalString != null && originalString.trim().length() == 0)) {
			return null;
		}
		return originalString;
	}
		private void declareSlots(FileWriter writer, Cls cls) {
				// Loop thru every slot
				Collection slots = cls.getTemplateSlots();
				Iterator ii = slots.iterator();
				while ( ii.hasNext() ) {
						Slot slot = (Slot)ii.next();
						if ( slotsToIgnore.contains(slot) )
								continue;
						if ( StringHelper.isValidJavaName(slot.getName()) == false) {
								System.out.println("Invalid java field name string: " + slot.getName());
								continue;
						}
						String type = getType(slot,false);
						if ( type != null ) {
								write(writer, "\tprivate " + type + " " + slot.getName() + ";");
						}
						else {
								System.out.println("Project incomplete in class " 
																	 + cls.getName() 
																	 + " for slot " + slot.getName());
						}
				}
		}

		private void declareInputFields(FileWriter writer, Cls cls) {
				// Get the locations of the widgets from protege
				PropertyList list = 
						dataSource.getProject().getClsWidgetPropertyList(cls);
				// Loop thru every slot
				Collection slots = cls.getTemplateSlots();
				Iterator ii = slots.iterator();
				while ( ii.hasNext() ) {
						Slot slot = (Slot)ii.next();
						if ( slotsToIgnore.contains(slot) )
								continue;
						if ( StringHelper.isValidJavaName(slot.getName()) == false) {
								System.out.println("Invalid java field name string: " 
																	 + slot.getName());
								continue;
						}
						boolean isMultiple = getCardinality(slot);
						String type = getType(slot,false);

						WidgetDescriptor desc2 = list.getWidgetDescriptor(slot.getName());
						System.out.println("widget name " + desc2.getWidgetClassName());
						System.out.println("widget label " + desc2.getLabel());
						String widgetType =  desc2.getWidgetClassName();
						if ( isMultiple || isInstance(slot)) {
								write(writer, "\tprivate OncScrollList "  
											+ slot.getName() + " = null;");	
						}
						else {
								if ( isInstance(slot) ) {
										write(writer, "\tprivate OncScrollList "  
													+ slot.getName() + " = null;");	
								}
								if ( "edu.stanford.smi.protege.widget.IntegerFieldWidget".equals
										 (widgetType) ) {
										write(writer, "\tprivate OncIntegerTextField "  
													+ slot.getName() + " = null;");	
								}
								else if ( "edu.stanford.smi.protege.widget.FloatFieldWidget".equals
													(widgetType) ) {
										write(writer, "\tprivate OncDoubleTextField "  
													+ slot.getName() + " = null;");	
								}
								else if ( "edu.stanford.smi.protege.widget.TextFieldWidget".equals
													(widgetType) ) {
										write(writer, "\tprivate OncTextField "  
													+ slot.getName() + " = null;");	
										
								}
								else {
										write(writer, "\tprivate OncScrollableTextArea "  
													+ slot.getName() + " = null;");	
								}//else
						}
				}
		}

		private void writeInputFields(FileWriter writer, Cls cls) {
				// Loop thru every slot
				Collection tempSlots = cls.getTemplateSlots();
				// Build three vectors 1) instantiation
				// 2) location
				// 3) adding it
				//then in the end write them all out
				Vector instantiationVector = new Vector();
				Vector locationVector = new Vector();
				Vector addVector = new Vector();
				Vector visibleVector = new Vector();

				// Get the locations of the widgets from protege
				PropertyList list = 
						dataSource.getProject().getClsWidgetPropertyList(cls);
				
				// Sort slots so the textAreas will be together before the 
				// scroll lists
				Vector slots = new Vector();
				Iterator i = tempSlots.iterator();
				while ( i.hasNext() ) {
						Slot tempSlot = (Slot)i.next();
						boolean isMultiple = getCardinality(tempSlot);
						if ( isMultiple ) {
								// end of the list
								slots.add(tempSlot);
								
						}
						else {
								// top of the list
								slots.add(0,tempSlot);
						}
				}
				
				Iterator ii = slots.iterator();
				boolean scrollLists = false;
				String instantiateString = null;
				while ( ii.hasNext() ) {
						Slot slot = (Slot)ii.next();
						if ( slotsToIgnore.contains(slot) )
								continue;
						if ( StringHelper.isValidJavaName(slot.getName()) == false) {
								System.out.println("Invalid java field name string: " 
																	 + slot.getName());
								continue;
						}
						String type = getType(slot,true);
						boolean isMultiple = getCardinality(slot);
						boolean isInstance = isInstance(slot);
						WidgetDescriptor desc2 = list.getWidgetDescriptor(slot.getName());
						System.out.println("widget name " + desc2.getWidgetClassName());
						String widgetType =  desc2.getWidgetClassName();
						String widgetLabel =  desc2.getLabel();
						String theLabel = null;
						if ( widgetLabel == null )
								theLabel = slot.getName();
						else
								theLabel = widgetLabel;
						if ( isMultiple ) {
								instantiateString =  "\t\t" + 
											slot.getName()
											+ " = new OncScrollList("
											+ type + ".class, "
											+ "editObj, \""
											+ theLabel +"\", true,true);";
						} 
						else {
								if (isInstance(slot)) {
										instantiateString =  "\t\t" + 
													slot.getName()
													+ " = new OncScrollList("
													+ type + ".class, "
													+ "editObj, \""
													+ theLabel +"\", true,true, false);";
								}
								else {
										if ( "edu.stanford.smi.protege.widget.IntegerFieldWidget".equals
												 (widgetType) ) {
												//(type.equals("Integer") == true) {
												instantiateString =  "\t\t" + 
														slot.getName()
														+ " = new OncIntegerTextField("
														+ "editObj, \""
														+ theLabel +"\", true);";
										}
										else if ( "edu.stanford.smi.protege.widget.FloatFieldWidget".equals
															(widgetType) ) {
												//|| type.equals("Double") == true ) {
												instantiateString =  "\t\t" + 
														slot.getName()
														+ " = new OncDoubleTextField("
														+ "editObj, \""
														+ theLabel +"\", true);";
										}
										else if ( "edu.stanford.smi.protege.widget.TextFieldWidget".equals
															(widgetType) ) {
												instantiateString =  "\t\t" + 
														slot.getName()
														+ " = new OncTextField("
														+ "editObj, \""
														+ theLabel +"\", true);";
										}

										else {
												instantiateString =  "\t\t" + 
															slot.getName()
															+ " = new OncScrollableTextArea("
															+ "editObj, \""
															+ theLabel +"\", true);";
										}//else
								}//else
						}						
						instantiationVector.addElement(instantiateString);
						if ( desc2 != null ) {
								System.out.println("name " + slot  );
								Rectangle bounds = desc2.getBounds();
								boolean isVisible = desc2.isVisible();
								String visibleString = slot.getName() + 
										".setVisible(" + isVisible + ");";
								if ( bounds != null ) {
										String boundsString = slot.getName() 
												+ ".setBounds(" + (int)bounds.getX()
												+ "," + (int)bounds.getY()
												+ "," + (int)bounds.getWidth()
												+ "," + (int)bounds.getHeight()
												+ ");";
								System.out.println("boundsString " + boundsString);
								locationVector.addElement(boundsString);
								}
								System.out.println("visibleString " + visibleString);
								visibleVector.addElement(visibleString);
						}
						String addString = "\t\tadd(" + slot.getName() + ");";
						addVector.addElement(addString);

					
				}
				// Write out all the strings so like actions will be grouped
				writeCollection(writer, instantiationVector);
				writeCollection(writer, locationVector);
				writeCollection(writer, visibleVector);
				writeCollection(writer, addVector);

		}
		
		private void writeCollection(FileWriter writer, Vector v) {
				Iterator i = v.iterator();
				while ( i.hasNext() ) {
						write(writer, (String)i.next());
				}
		}
		private void writeUiHashtable(FileWriter writer, Cls cls) {
				write(writer, "\tprivate void fillUiHashtable() {"); 
				write(writer, "\t\tuiHashtable = new Hashtable();"); 

				// Loop thru every slot
				Collection tempSlots = cls.getTemplateSlots();
				// Sort slots so the textAreas will be together before the 
				// scroll lists
				Vector slots = new Vector();
				Iterator i = tempSlots.iterator();
				while ( i.hasNext() ) {
						Slot tempSlot = (Slot)i.next();
						boolean isMultiple = getCardinality(tempSlot);
						if ( isMultiple ) {
								// end of the list
								slots.add(tempSlot);
								
						}
						else {
								// top of the list
								slots.add(0,tempSlot);
						}
				}
				Iterator ii = slots.iterator();
				int columnCount = 0;
				int rowCount = 0;
				int maxColumns = 1;
				boolean scrollLists = false;

				while ( ii.hasNext() ) {
						Slot slot = (Slot)ii.next();
						if ( slotsToIgnore.contains(slot) )
								continue;
						if ( StringHelper.isValidJavaName(slot.getName()) == false) {
								System.out.println("Invalid java field name string: " 
																	 + slot.getName());
								continue;
						}
						write(writer, "\t\tuiHashtable.put(" + slot.getName()
									+ ", \"" + slot.getName() + "\");");
				}
				write(writer, "\t}");
		}


		private void writeMethodMaps(FileWriter writer, Cls cls) {
				write(writer, "\tMethod setter = null;");
				write(writer, "\tMethod getter = null;");
				// Loop thru every slot
				Collection slots = cls.getTemplateSlots();
				Iterator ii = slots.iterator();
				while ( ii.hasNext() ) {
						Slot slot = (Slot)ii.next();
						if ( slotsToIgnore.contains(slot) )
								continue;
						if ( StringHelper.isValidJavaName(slot.getName()) == false) {
								System.out.println("Invalid java field name string: " 
																	 + slot.getName());
								continue;
						}
						String type = getType(slot,false);
						if ( type != null ) {
								write(writer,"\tsetter = getSetter(\"set"
											+ StringHelper.capitalize(slot.getName()) +
											"\", " + type + ".class);");
								write(writer, "\tsetMethodMap.put(\""
											+ slot.getName() + "\", setter);");
								write(writer,"\tgetter = getGetter(\"get"
											+ StringHelper.capitalize(slot.getName()) +
											"\");");
								write(writer, "\tgetMethodMap.put(\""
											+ slot.getName() + "\", getter);");
						}
						else {
								System.out.println("Project incomplete in class " 
																	 + cls.getName() 
																	 + " for slot " + slot.getName());
						}
				}
				write(writer, "}\n");
				
		}


		private void writeGetters(FileWriter writer, Cls cls) {
				// Loop thru every slot
				Collection slots = cls.getTemplateSlots();
				Iterator ii = slots.iterator();
				while ( ii.hasNext() ) {
						Slot slot = (Slot)ii.next();
						if ( slotsToIgnore.contains(slot) )
								continue;
						if ( StringHelper.isValidJavaName(slot.getName()) == false) {
								System.out.println("Invalid java field name string: " 
																	 + slot.getName());
								continue;
						}
						String type = getType(slot,false);
						if ( type != null ) {
								write(writer, "\tpublic " + type + " get" 
											+ StringHelper.capitalize(slot.getName()) + "(){");
								write(writer, "\t\treturn " + slot.getName() + ";");
								write(writer, "\t}");
						}
						else {
								System.out.println("Project incomplete in class " 
																	 + cls.getName() 
																	 + " for slot " + slot.getName());
						}
				}
				
		}

		private void writeToString(FileWriter writer, Cls cls) {
				Slot browserSlot = cls.getBrowserSlot();
			
				write(writer, "\tpublic String toString() {");
				if ( browserSlot == SPECIAL_NAME_SLOT 
						|| "String".equals(getType(browserSlot,false)) == false ) {
						write(writer, "\t\treturn getGUID().toString();");
				} 			
				else 
						write(writer, "\t\treturn " + browserSlot.getName() + ";");
				write(writer, "\t}"); 				
		}

		private void writeSetters(FileWriter writer, Cls cls) {
				// Loop thru every slot
				Collection slots = cls.getTemplateSlots();
				Iterator ii = slots.iterator();
				while ( ii.hasNext() ) {
						Slot slot = (Slot)ii.next();
						if ( slotsToIgnore.contains(slot) )
								continue;
						String type = getType(slot,false);
						if ( type != null ) {
								if ( type.equals("DefaultPersistibleList") ) {
								write(writer, "\tpublic void set" 
																	 + StringHelper.capitalize(slot.getName()) + "("
																	 + "java.util.Collection  var ){");
										// Make sure it is instantiated first
										write(writer, "\t\tif ( " + slot.getName() + "== null)");
										write(writer, "\t\t\t" 
																			 + slot.getName() 
																			 + " = new DefaultPersistibleList();");
										write(writer, "\t\t" + slot.getName() + ".set(var);");
								}
								else {
										write(writer, "\tpublic void set" 
													+ StringHelper.capitalize(slot.getName()) + "("
													+ type + " var ){");
										
										write(writer, "\t\t" + slot.getName() + " = var;");
								}
						}
						else {
								System.out.println("Project incomplete in class " 
																	 + cls.getName() 
																	 + " for slot " + slot.getName());
						}
						write(writer, "\t}\n");

				}
				
		}

		private boolean getCardinality(Slot slot) {
				
				if ( slot.getAllowsMultipleValues()) 
						return true;
				return false;
		}

		private boolean isInstance(Slot slot) {
			if ( slot.getValueType() == ValueType.INSTANCE ) {
					return true;
				}
			return false;
		}

		private String getType(Slot slot, boolean ignoreCardinality) {
				if ( ignoreCardinality == false ) {
						if ( slot.getAllowsMultipleValues()) 
								return "DefaultPersistibleList";
				}
				if ( slot.getValueType() == ValueType.ANY){
						return "Object";
				}
				if ( slot.getValueType() == ValueType.BOOLEAN  ) {
						return "Boolean";
				}
				if ( slot.getValueType() == ValueType.CLS ) {
								return "Object";
				}
				if ( slot.getValueType() == ValueType.FLOAT ) {
						return "Float";
				}
				if ( slot.getValueType() == ValueType.INSTANCE ) {
						// Get the allowable types
						Collection allowableClses = slot.getAllowedClses();
						if ( allowableClses.size() > 1) {
								return "DefaultPersistibleList";
						}
						else if (allowableClses.size() == 1) {
								Iterator i = allowableClses.iterator();
								Cls allowableCls = (Cls)i.next(); 
								return allowableCls.getName();
								
						}
				}
				if ( slot.getValueType() == ValueType.INTEGER ) {
						return "Integer";
				}
				if ( slot.getValueType() == ValueType.STRING ) {
						return "String";
				}
				if ( slot.getValueType() == ValueType.SYMBOL ) {
							return "String";
				}
				return null;
		}
		
		private void  closeAllProjects() {
				if ( 	projectDescs[0] != null && 
							projectDescs[0].project != null )
						projectDescs[0].project.dispose();
		}

		private void saveProject() {
				try {
						projectDescs[0].project.save(new ArrayList());
				}
				catch (Exception e) {
						System.out.println("Unable to save project file.");
				}
		}

		private FileWriter openFileForWrite(String filePathName) {
				File file = null;
				FileWriter fileWriter = null;

				if ( (file = FileHelper.openFileForWrite(filePathName, true)) 
						 == null) {
						System.out.println("Error: unable to open file for write " + 
															 filePathName);
						return null;
				}
				try {
						fileWriter = new FileWriter(file);
				}
				catch (Exception e ) { 
						System.out.println("can't create a filewriter " + filePathName);
				}
				return fileWriter;
		}
		
		private void write(FileWriter fileWriter, String info) {
				try
						{
								fileWriter.write(info);
								fileWriter.write("\n");
						}
				catch(IOException e){ 
						System.out.println("Error: unable to write to file ");
						
				}
		}


		private void closeFile(FileWriter fileWriter) {
				try {
						fileWriter.close();
				}
				catch ( Exception e ) {
						System.out.println("Error: Unable to close file" ) ;
				}
		}

		public static void main(String[] args) {
				if ( args.length > 0) {
						kbPath = args[0];
				}
				else {
						System.out.println( "syntax: java oncotcap.util.Merge"
															 + " baselineKbPath addOnKbPath");
						System.out.println("Please provide Protege KB Path names.");
						//System.exit(0);
				}
				boolean reportMode = true;
				if ( args.length > 2 && "report".equals(args[2]) ) {
						// Run utility in report mode
						reportMode = true;
				}
				GenerateModelSource t = new GenerateModelSource(reportMode);
		}

		class ProjectDescription {
				public Project project = null;
				public KnowledgeBase kb = null;
				public Vector allInstances = new Vector();
				public Vector allClasses = new Vector();
				public String name = new String();
		}


}


