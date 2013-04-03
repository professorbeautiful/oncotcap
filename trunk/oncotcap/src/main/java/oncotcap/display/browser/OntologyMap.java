package oncotcap.display.browser;

import java.util.*;
import java.lang.reflect.*;
import javax.swing.ButtonGroup;

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;

public class OntologyMap {
		private static final int UNSET = -1;
		private OntologyButtonPanel ontologyButtonPanel = null;
		///////////////////////////////////
		static int groupMax[] = {0,12,22,32,40 };
		static int groupMin[] = {0,10,20,30,40 };

		// The indices for the checkboxes
		public static final int IS = 0;
		public static final int KN = 1;
		public static final int I = 2;
		public static final int E = 3;
		public static final int SB = 4;
		public static final int ST = 5;
		public static final int CB = 6;
//		public static final int OP = 7;
		public static final int SM = 7;
		public static final int SMG = 8;
		public static final int MC = 9;
		public static final int K = 10;

		
		
		// These names correspond to the knowledgebase names
		public static final String INFO_SOURCE = "InformationSource";
		public static final String NUGGET = "KnowledgeNugget";
		public static final String INTERPRETATION = "Interpretation";
		public static final String ENCODING = "Encoding";
		public static final String STATEMENT_TEMPLATE = "StatementTemplate";
		public static final String STATEMENT_BUNDLE = "StatementBundle";
		public static final String CODE_BUNDLE = "CodeBundle";
		public static final String MODEL_CONTROLLER = "ModelController";
		public static final String SUBMODEL = "SubModel";
		public static final String SUBMODEL_GROUP = "SubModelGroup";
//		public static final String ONC_PROCESS = "OncProcessClassWriter";
		public static final String KEYWORD = "Keyword";

		static Hashtable ontologyClassHashtable = new Hashtable();
		static Hashtable ontologyHashtable = new Hashtable();
		static Hashtable ontologyButtonHashtable = new Hashtable();
		static Hashtable abbreviationHashtable = new Hashtable();
		public static final int KNOWLEDGE_CAPTURE = 10;
		public static final int CODIFYING = 20;
		public static final int BUILDING = 30;
		public static final int KEYWORDS = 40;
		static {
				ontologyHashtable.put(INFO_SOURCE,new Integer(KNOWLEDGE_CAPTURE + 2 ));
				ontologyHashtable.put(NUGGET, new Integer(KNOWLEDGE_CAPTURE +1));
				ontologyHashtable.put(INTERPRETATION, new Integer(KNOWLEDGE_CAPTURE + 0));
				ontologyHashtable.put(ENCODING, new Integer(0));
				ontologyHashtable.put(STATEMENT_BUNDLE,  new Integer(CODIFYING + 0));
				ontologyHashtable.put(STATEMENT_TEMPLATE, new Integer(CODIFYING + 1));
				ontologyHashtable.put(CODE_BUNDLE,  new Integer(CODIFYING + 2));
				ontologyHashtable.put(MODEL_CONTROLLER, new Integer(BUILDING + 2 ));
				ontologyHashtable.put(SUBMODEL_GROUP, new Integer(BUILDING + 1));
				ontologyHashtable.put(SUBMODEL, new Integer(BUILDING + 0));
//				ontologyHashtable.put(ONC_PROCESS,  new Integer(CODIFYING + 3));
				ontologyHashtable.put(KEYWORD, new Integer(KEYWORDS));
		}
		static {
				abbreviationHashtable.put(INFO_SOURCE, "IS");
				abbreviationHashtable.put(NUGGET, "KN");
				abbreviationHashtable.put(INTERPRETATION,"I");
				abbreviationHashtable.put(ENCODING, "E");
				abbreviationHashtable.put(STATEMENT_BUNDLE, "SB");
				abbreviationHashtable.put(STATEMENT_TEMPLATE,"ST");
				abbreviationHashtable.put(CODE_BUNDLE,"CB");
				abbreviationHashtable.put(MODEL_CONTROLLER, "MC");
				abbreviationHashtable.put(SUBMODEL_GROUP, "SMG");
				abbreviationHashtable.put(SUBMODEL, "SM");
//				abbreviationHashtable.put(ONC_PROCESS, "OP");
				abbreviationHashtable.put(KEYWORD, "K");
		}
	static {
				ontologyClassHashtable.put(INFO_SOURCE,InformationSource.class);
				ontologyClassHashtable.put(NUGGET, KnowledgeNugget.class);
				ontologyClassHashtable.put(INTERPRETATION, Interpretation.class);
				ontologyClassHashtable.put(ENCODING, Encoding.class);
				ontologyClassHashtable.put(STATEMENT_TEMPLATE, StatementTemplate.class);
				ontologyClassHashtable.put(STATEMENT_BUNDLE,  StatementBundle.class);
				ontologyClassHashtable.put(CODE_BUNDLE,  CodeBundle.class);
				ontologyClassHashtable.put(MODEL_CONTROLLER, ModelController.class);
				ontologyClassHashtable.put(SUBMODEL_GROUP, SubModelGroup.class);
				ontologyClassHashtable.put(SUBMODEL, SubModel.class);
//				ontologyClassHashtable.put(ONC_PROCESS,  OncProcessClassWriter.class);
				ontologyClassHashtable.put(KEYWORD, Keyword.class);
		}

		static {
				ontologyButtonHashtable.put(INFO_SOURCE, new Integer(IS));
				ontologyButtonHashtable.put(NUGGET,  new Integer(KN));
				ontologyButtonHashtable.put(INTERPRETATION,  new Integer(I));
				ontologyButtonHashtable.put(ENCODING,  new Integer(E));
				ontologyButtonHashtable.put(STATEMENT_BUNDLE,   new Integer(SB));
				ontologyButtonHashtable.put(STATEMENT_TEMPLATE,  new Integer(ST));
				ontologyButtonHashtable.put(CODE_BUNDLE,   new Integer(CB));
				ontologyButtonHashtable.put(MODEL_CONTROLLER,  new Integer(MC));
				ontologyButtonHashtable.put(SUBMODEL_GROUP,  new Integer(SMG));
				ontologyButtonHashtable.put(SUBMODEL,  new Integer(SM));
//				ontologyButtonHashtable.put(ONC_PROCESS,  new Integer(OP));
				ontologyButtonHashtable.put(KEYWORD,  new Integer(K));
		}

		/////////////////////////////////////////////

		public static final int MAX_KC = 13;
		public static final int MAX_B = 33;
		public static final int MAX_C = 24;
		public static final int MAX_K = 41;


		private static final int E_GROUP = 0;
		private static final int KC_GROUP = 1;
		private static final int C_GROUP = 2;
		private static final int MB_GROUP = 3;
		private static final int K_GROUP = 4;
		
		private int groupMaxDistance[] = {-1,-1,-1,-1,-1};
		private int groupLeaf[] = {-1,-1,-1,-1,-1};
		private Vector leaves = new Vector();
		
    public OntologyMap() {

    }
		
		public int getId(String ontologyName) {
				return ((Integer)ontologyHashtable.get(ontologyName)).intValue();
		}

		public static Integer getButtonIndex(String ontologyName) {
				return (Integer)ontologyButtonHashtable.get(ontologyName);
		}

		public static int getButtonIndex(int ontologyId) {
				return -1;	
		}

		public void setOntologyButtonPanel(OntologyButtonPanel obp) {
				ontologyButtonPanel = obp;
		}

		public static Collection getValidDirectConnectedTypes(String name) {
				Vector validTypes = new Vector();
				// Determine based on the given type what related types the
				// user can create
				int maxCategoryValue = -1;
				int categoryValue = -1;
// 				System.out.println("getValidDirectConnectedTypes " + name);
				int val = ((Integer)ontologyHashtable.get(name)).intValue();
				if ( name.equals(ENCODING) ) {
						validTypes.addElement(STATEMENT_BUNDLE);
						validTypes.addElement(INTERPRETATION);
						validTypes.addElement(SUBMODEL);
// 						validTypes.addElement(KEYWORD);
				}
				else if ( name.equals(KEYWORD) ) {
						validTypes.addElement(KEYWORD);
						validTypes.addElement(INFO_SOURCE);
						validTypes.addElement(NUGGET);
						validTypes.addElement(ENCODING);
						validTypes.addElement(INTERPRETATION);
						validTypes.addElement(STATEMENT_TEMPLATE);
						validTypes.addElement(STATEMENT_BUNDLE);
						validTypes.addElement(CODE_BUNDLE);
						validTypes.addElement(SUBMODEL);
						validTypes.addElement(SUBMODEL_GROUP);
						validTypes.addElement(MODEL_CONTROLLER);
				}
				else if ( name.equals(STATEMENT_BUNDLE) ){
						validTypes.addElement(ENCODING);
				}
				else {
						// validTypes.addElement(KEYWORD);
						// What category does this item fall into
						int upstream = val -1;
						int downstream = val + 1;
						if ( upstream >= getGroupMin(getGroup(val)) ) 
								validTypes.addElement(getKey(upstream));
						else 
								validTypes.addElement(ENCODING);	
						if ( downstream <= getGroupMax(getGroup(val)) ) 
								validTypes.addElement(getKey(downstream));
				}
				//System.out.println("validTypes: " + validTypes);
				return validTypes;
		}

		static public String getValue() {
				return null;
		}

		static public String getKey(int value) {
				Integer integerValue = new Integer(value);
				for (Enumeration e = ontologyHashtable.keys(); 
						 e.hasMoreElements(); ) {
					
            String objName = (String)e.nextElement();
						Integer currentVal =
								(Integer)ontologyHashtable.get(objName);
            if (currentVal.compareTo(integerValue)== 0) 
						
                return objName;
				}
				return null;
		}

		static public String getClassKey(Class selectedClass) {
				for (Enumeration e = ontologyClassHashtable.keys(); 
						 e.hasMoreElements(); ) {
            String objName = (String)e.nextElement();
						Class currentVal =
								(Class)ontologyClassHashtable.get(objName);
// 						System.out.println("currentVal " + currentVal);
// 						System.out.println("selectedCLass " + selectedClass);
            if (currentVal.isAssignableFrom(selectedClass)) 
                return objName;
				}
				return null;
		}
		static public String getOntologyClassName(Class cls) {
				// Determine which Ontology class the specific class 
				// inherits from
			for (Enumeration e = ontologyClassHashtable.keys(); 
				  e.hasMoreElements();) {
				String className = (String)e.nextElement();
				Class oClass = (Class)ontologyClassHashtable.get(className);
				if ( oClass.isAssignableFrom(cls) ) {
								//System.out.println("OntologyMap " + className);
					return className;
				}
			}
				  return null;

		}
		static public String getButtonKey(int value) {
				Integer integerValue = new Integer(value);
				for (Enumeration e = ontologyButtonHashtable.keys(); 
						 e.hasMoreElements(); ) {
					
            String objName = (String)e.nextElement();
						Integer currentVal =
								(Integer)ontologyButtonHashtable.get(objName);
            if (currentVal.compareTo(integerValue)== 0) 
						
                return objName;
				}
				return null;
		}

		// This is going to have to take a ontologybuttonpanel instance to support 
		// multiple trees 
		static public boolean isVisibleNode(String name, 
																				OntologyButtonPanel ontologyButtonPanel) {
				//Loop through leaf nodes and check if visible if the
				// name of the leaf matches
				LeafCheckBox [] leafCheckBox  = 
						ontologyButtonPanel.getLeafCheckBoxArray(); /// this really needs an arg 
				if ( getRootNodeName(ontologyButtonPanel).equals(name) )
						 return true;
				for ( int i = 0; 
							i < Array.getLength(leafCheckBox); i++) {
						// For each selected determine if it is the max in its leg
						if ( leafCheckBox[i].getName().equals(name) ) {
								return leafCheckBox[i].isSelected();
						}
				}
				return false;
		}
		static private Vector addUniqueElement(Vector v, Object element) {
				if ( !v.contains(element) )
						v.addElement(element);
				return v;
		}
		public Vector getVisibleNodeNames() {
				return null;
		}

		/** Set all leaves between the selected leaf and the selected root
		 */

		public static Vector whatIsBetween(OntologyButtonPanel obp, 
																				 LeafCheckBox leafCheckBox) {
				// Get root node
				int root = ((Integer)ontologyHashtable.get(getRootNodeName(obp))).intValue();

				// Determine leafs between root and leaf
				// Are the leaf and root in the same leg?
				//System.out.println("ROOT " + root + "LEAF " + leafCheckBox);
				int leaf = ((Integer)ontologyHashtable.get(leafCheckBox.getName())).intValue();
				Vector leaves = new Vector();
				// Are they in the same leg
				int rootGroup = getGroup(root);
				int leafGroup = getGroup(leaf);
				if ( rootGroup !=  leafGroup) {
						leaves.addAll(rollDown(root, getGroupMin(rootGroup)));
						leaves.addAll(rollOver());
						leaves.addAll(rollUp(leaf, getGroupMin(leafGroup)));
				}
				else {
						if ( root > leaf ) 
								leaves.addAll(rollDown(root, leaf));
						else
								leaves.addAll(rollUp(root,leaf));
										
				}
				return leaves;
		}
		
		private static Vector rollUp(int id, int min) {
				//System.out.println("rollUp " + id + " -- " + min);
				Vector leaves = new Vector();
				if ( id == 0 )
						return leaves;
				// start from min and work way to id
				//int  min = getGroupMin(getGroup(id));
				for ( int i = min; i <= id; i++) {
						leaves.addElement(getButtonIndex(getKey(i)));
				}
				//System.out.println("roll up " + leaves);
				return leaves;
		}

		private static Vector rollDown(int id, int min) {
// 				System.out.println("rollDown " + id + " -- " + min);
				Vector leaves = new Vector();
				if ( id == 0 )
						return leaves;
				// start from id and work way to min
				//int  min = getGroupMin(getGroup(id));
				for ( int i = id; i >= min; i--) {
						leaves.addElement(getButtonIndex(getKey(i)));
				}
				//System.out.println("roll down " + leaves);
				return leaves;
		}

		private static Vector rollOver() {
				return oncotcap.util.CollectionHelper.makeVector(getButtonIndex(getKey(0)));
		}

		static public Class getClassValue(String objName) {
				return (Class)ontologyClassHashtable.get(objName);
		}

		static public String getRootNodeName(OntologyButtonPanel ontologyButtonPanel) {
				ButtonGroup bg = ontologyButtonPanel.getButtonGroup();
        for (Enumeration e = bg.getElements(); e.hasMoreElements(); ) {
            RootCheckBox b = (RootCheckBox)e.nextElement();
            if (b.getModel() == bg.getSelection()) {
                return b.getName();
            }
        }
        return null;
		}

		static public Class getRootNodeClass(OntologyButtonPanel ontologyButtonPanel) {
				String rootNodeName = getRootNodeName(ontologyButtonPanel);
				return (Class)ontologyClassHashtable.get(rootNodeName);
		}

	

		public Vector getEndNodeNames(OntologyButtonPanel ontologyButtonPanel) {
				String rootNodeName = getRootNodeName(ontologyButtonPanel);
				int root = ((Integer)ontologyHashtable.get(rootNodeName)).intValue();
				Vector ends = getLeaves(root);
				//System.out.println("end Nodes " + ends);
		
				return ends;
		}

		// Return a vector of leaf names ( correspond to protege class names )
		public Vector getAllUnselectedLeaveNames() {
				String name = null;
				// Loop through leaf checkboxes
				 LeafCheckBox [] leafCheckBox  = 
						 ontologyButtonPanel.getLeafCheckBoxArray();
				Vector unselectedLeaves = new Vector();
				String rootName = getRootNodeName(ontologyButtonPanel);
				for ( int i = 0; i < Array.getLength(leafCheckBox); i++) {
						if ( !leafCheckBox[i].isSelected() ) {
								name = leafCheckBox[i].getName();
								if ( !rootName.equals(name) ) 
										unselectedLeaves.addElement(name);
						}
				}
				return unselectedLeaves;
		}

		// Return a vector of classes
		public Vector getAllUnselectedLeaves() {
				String name = null;
				// Loop through leaf checkboxes
				 LeafCheckBox [] leafCheckBox  = 
						 ontologyButtonPanel.getLeafCheckBoxArray();
				Vector unselectedLeaves = new Vector();
				String rootName = getRootNodeName(ontologyButtonPanel);
				for ( int i = 0; i < Array.getLength(leafCheckBox); i++) {
						if ( !leafCheckBox[i].isSelected() ) {
								name = leafCheckBox[i].getName();
								if ( !rootName.equals(name) ) 
										unselectedLeaves.addElement(getClassValue(name));
						}
				}
				return unselectedLeaves;
		}

		static public int getGroup(int leaf) {
				double a = leaf/10.0;
				return (int)a;
		}
		
		static public int getGroupMax(int group) {
				return groupMax[group];
		}
		static public int getGroupMin(int group) {
				return groupMin[group];
		}
		
		public int getDistance(int leaf, int root) {
				int distance = 0;
				if ( getGroup(leaf) != getGroup(root) )
						distance = 0 - leaf;
				else 
						distance = leaf;
				return Math.abs(root - distance);
		}

		private void getMaxDistances(int root) {
				String name = null;
				int group = -1;
				int leaf = -1;
				int distance = -1;
				// Loop through leaf checkboxes
				 LeafCheckBox [] leafCheckBox  = 
						 ontologyButtonPanel.getLeafCheckBoxArray();
				// Drop the max for each group in the proper box
				for ( int i = 0; i < Array.getLength(leafCheckBox); i++) {
						// For each selected determine if it is the max in its leg
						if ( leafCheckBox[i].isSelected() ) {
								name = leafCheckBox[i].getName();
								leaf = ((Integer)ontologyHashtable.get(name)).intValue();
								group = getGroup(leaf);
								distance = getDistance(leaf,root);
								if (distance >  groupMaxDistance[group]) {
										groupMaxDistance[group] = distance;
										groupLeaf[group] = leaf;
								}
						}
				}
		}

		public boolean isLeafSelected(String name) {
				// not implemeneted yet getAllSelectedLeaves();
				return false;
		}
		public Vector getAllSelectedLeafClasses() {
			
				Vector selectedLeafClasses = new Vector();
				Vector selectedLeafNames = getAllSelectedLeaves();
				Iterator i = selectedLeafNames.iterator();
				while ( i.hasNext() ) {
						selectedLeafClasses.addElement(getClassValue((String)i.next()));
				}
				return selectedLeafClasses;
		}
		public Vector getAllSelectedLeaves() {
				String name = null;
				// Loop through leaf checkboxes
				 LeafCheckBox [] leafCheckBox  = 
						 ontologyButtonPanel.getLeafCheckBoxArray();
				Vector selectedLeaves = new Vector();
				for ( int i = 0; i < Array.getLength(leafCheckBox); i++) {
						// For each selected determine if it is the max in its leg
						if ( leafCheckBox[i].isSelected() ) {
								name = leafCheckBox[i].getName();
								selectedLeaves.addElement(name);
						}
				}
				return selectedLeaves;
		}

		public Vector getLeaves(int root) {
				boolean otherGroupLeaves = false;
				int rootGroup = -1;
				int leafGroup = -1;
				int sameGroupMax = -1;

				resetVars();
				// If the root is keyword all leaves are valid  
				if ( getGroup(root) == K_GROUP ) {
						return getAllSelectedLeaves();
				}
				
				// get special case leaves  ST and SB which 
				// if selected are always end nodes
// 				System.out.println("root is " + root);
				LeafCheckBox leafCheckBox = null;
				// if (root == 
// 						((Integer)ontologyHashtable.get(STATEMENT_TEMPLATE)).intValue()) {
// 						leafCheckBox = 
// 								ontologyButtonPanel.getLeafCheckBox(OntologyMap.ST);
// 				} 
// 				if (root == 
// 						((Integer)ontologyHashtable.get(STATEMENT_BUNDLE)).intValue() ) {
// 						System.out.println("root is ST " );
						
// 				}
				LeafCheckBox sbLeafCheckBox = 
								ontologyButtonPanel.getLeafCheckBox(OntologyMap.ST);
				LeafCheckBox stLeafCheckBox = 
								ontologyButtonPanel.getLeafCheckBox(OntologyMap.SB);
				if ( stLeafCheckBox != null && stLeafCheckBox.isSelected() ) {
						leaves.addElement(STATEMENT_TEMPLATE);
				}
				if ( sbLeafCheckBox != null && sbLeafCheckBox.isSelected() ) {
						leaves.addElement(STATEMENT_BUNDLE);
				}

				// Otherwise determine what are the proper leaf nodes
				getMaxDistances(root);

				// If keyword leaf is on always add it to list
				if ( groupMaxDistance[K_GROUP] > UNSET  ) {
						leaves.addElement(getKey(groupLeaf[K_GROUP]));
				}

				// Loop backwards through the groups
				for ( int i = MB_GROUP; i >= KC_GROUP; i--) {
						leafGroup = getGroup(groupLeaf[i]);
						rootGroup = getGroup(root);
						if ( root == groupLeaf[i] ) {
								continue;
						}
						if ( groupMaxDistance[i] > UNSET ) {
								if ( leafGroup != rootGroup) {
										//System.out.println("adding leaves diff group" 
										// + getKey(groupLeaf[i]));
										leaves.addElement(getKey(groupLeaf[i]));
										otherGroupLeaves = true;
								}
						}
				}

				//System.out.println("other " + otherGroupLeaves);
				if ( groupMaxDistance[E_GROUP] > UNSET  &&
						 !otherGroupLeaves) {
						leaves.addElement(getKey(groupLeaf[E_GROUP]));
						otherGroupLeaves = true;
				}	
				//				System.out.println("leaves " + leaves);

				// Add any same leg ( group ) leaves 
				addSameGroupSelectedLeaves(root, otherGroupLeaves, leaves);

				//				System.out.println("leaves " + leaves);
				return leaves;
		}

// 								if ( leafGroup == rootGroup &&
// 										 groupMaxDistance[E_GROUP] == UNSET 
// 										 && otherGroupLeaves == false) {
// 										System.out.println("adding leaves " + getKey(groupLeaf[i]));
// 										leaves.addElement(getKey(groupLeaf[i]));
// 								}
		private void addSameGroupSelectedLeaves(int root, boolean otherGroups, Vector leaves) {
				Vector selectedLeaves = getAllSelectedLeaves();
				//				System.out.println("other groups groupLeaf " + otherGroups + " " 
				//									 + groupLeaf[getGroup(root)]);
// || 
// 						 (!otherGroups && groupLeaf[getGroup(root)] > -1 
// 							&& groupLeaf[getGroup(root)] < root)
				if ( !otherGroups  ) {
						for ( int i = getGroupMin(getGroup(root)); i < root; i++) {
								if ( selectedLeaves.contains(getKey(i)) ) {
										leaves.addElement(getKey(i));
										break;
								}
						}
				}
				for ( int i = getGroupMax(getGroup(root)); i > root; i--) {
						if ( selectedLeaves.contains(getKey(i)) ) {
								leaves.addElement(getKey(i));
								break;
						}
				}
		}
		public static String getAbbreviation(String longName) {
				if ( longName == null ) 
						return null;

				return (String)abbreviationHashtable.get(longName);
		}
		private void resetVars() {
				leaves.clear();
				for ( int i = 0; i < Array.getLength(groupMaxDistance); i++) {
						groupMaxDistance[i] = UNSET;
						groupLeaf[i] = UNSET;
				}
		}

}
