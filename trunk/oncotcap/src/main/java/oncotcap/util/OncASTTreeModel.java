package oncotcap.util;

import java.io.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.tree.*;
import javax.swing.*;
import java.util.regex.*;

import antlr.collections.AST;
import antlr.debug.misc.*;
import antlr.*;

import oncotcap.engine.VariableDependency;


/* Brief description from ANTLR 
A parse tree is a record of the rules (and tokens) used to match some input text whereas a syntax tree records the structure of the input and is insensitive to the grammar that produced it. Note that there are an infinite number of grammars for any single language and hence every grammar will result in a different parse tree form for a given input sentence because of all the different intermediate rules. An abstract syntax tree is a far superior intermediate form precisely because of this insensitivity and because it highlights the structure of the language not the grammar. 

Only simple, so-called syntax directed translations can be done with actions within the parser. These kinds of translations can only spit out constructs that are functions of information already seen at that point in the parse. Tree parsers allow you to walk an intermediate form and manipulate that tree, gradually morphing it over several translation phases to a final form that can be easily printed back out as the new translation. 


*/


//Class that can take a AST (Abstract Syntax Tree) Tree Model and 
// read it to return answers
// To common code generation questions i.e. getVariableDependencies,
// getMethodCalls, getDeclarations

public class OncASTTreeModel extends DefaultTreeModel {
		Hashtable backQuotedStrings = null;
		DissectString dissectString = null;
		AST astRootNode = null;
		DefaultMutableTreeNode root = null;
		// Every variable in the code segment
		UniqueVector allVariables = null; 
		// Every variable that is set in this code segment
		UniqueVector allSetVariables = null; 
		// Every variable with corresponding dependency that 
		// is created due to being in a conditional statement 
		// code block (while, if, for...)
		Hashtable allConditionalDependencies = null;
		Hashtable allSetDependencies = null;

		UniqueVector declaredVariables = new UniqueVector(); // not used
		UniqueVector methodCalls = null;
		UniqueVector dependencies = new UniqueVector();
		boolean showTree = false;

		public OncASTTreeModel(AST astRootNode, boolean showTree, 
													 DissectString dissectString){
				super(null);
				this.showTree = showTree;
				this.astRootNode = astRootNode;
				this.dissectString = dissectString;
				this.root = getTrueTreeModel(astRootNode);
				setRoot(root);
		}

		public OncASTTreeModel(AST astRootNode){
				super(null);
				this.astRootNode = astRootNode;
				this.root = getTrueTreeModel(astRootNode);
				setRoot(root);
		}

		public Vector getAllVariables() {
				// Only do once - calculate the first time accessed
				if ( allVariables == null ) {
						allVariables = getAllVariables(true);
				}
/*				else
					Logger.logTheTime("OncASTTreeModel.getAllVariables: already done.");*/
				return allVariables ;
		}

		// Get any and all variables mentioned in this code segment
		public UniqueVector getAllVariables(boolean compute) {
				// scan the whole tree and retrieve all IDENT (ifiers) 
				// that are not method calls
				UniqueVector allVariables = new UniqueVector();
				CommonAST currentAST = null;
				CommonAST parentAST = null;
				//Logger.log("\nOncASTTreeModel.getAllVariables:  variables are :");
				for (Enumeration e=root.depthFirstEnumeration(); 
						 e.hasMoreElements(); ) {
						DefaultMutableTreeNode n = 
								(DefaultMutableTreeNode)e.nextElement();
						DefaultMutableTreeNode parent = 
								(DefaultMutableTreeNode)n.getParent();
						if ( parent == null ) 
								continue;
						if ( n.getUserObject() instanceof CommonAST
								 && parent.getUserObject() instanceof CommonAST ) {
								currentAST = (CommonAST)n.getUserObject();
								String text = currentAST.getText();
								int tokenType = currentAST.getType();
								parentAST = 
										(CommonAST)parent.getUserObject();
								if ( currentAST.getType() == JavaRecognizer.IDENT 
										 && parentAST.getType() != JavaRecognizer.METHOD_CALL) {
										//Logger.log("  " + currentAST.getText());
										allVariables.add(currentAST.getText());
										if(currentAST.getText().contains("`"))
											System.out.println("OncASTTreeModel: Contains quote: " + currentAST.getText());
								}
								else if (currentAST.getType() == JavaRecognizer.IDENT 
										 && parentAST.getType() == JavaRecognizer.METHOD_CALL) {
								}
						}
				}
/*				System.out.print("OncASTTreeModel: allVariables = ");
				for(Object var: allVariables)
					System.out.print(" " + var);
				System.out.println();
*/
				return allVariables;
		}

		// Get all variables set in this code segment =, +=, ++, --, etc
		public Vector getAllSetVariables() {
				// Only do once - calculate the first time accessed
				//if ( allSetVariables == null ) {
						allSetVariables = getAllSetVariables(true);
				//}
/*				else
					Logger.logTheTime("OncASTTreeModel.getAllSetVariables: already done.");*/
				return allSetVariables ;
		}

		public UniqueVector getAllSetVariables(boolean compute) {
				// scan the whole tree and retrieve all IDENT (ifiers) 
				// that are not method calls
				UniqueVector allSetVariables = new UniqueVector();
				CommonAST currentAST = null;
				CommonAST parentAST = null;
				DefaultMutableTreeNode parent = null;
				for (Enumeration e=root.depthFirstEnumeration(); 
						 e.hasMoreElements(); ) {
						DefaultMutableTreeNode n = 
								(DefaultMutableTreeNode)e.nextElement();
						parent = (DefaultMutableTreeNode)n.getParent();
						if ( parent == null ) 
								continue;
						if ( n.getUserObject() instanceof CommonAST
								 && ((DefaultMutableTreeNode)n.getParent()).getUserObject()
								 instanceof CommonAST ) {
								currentAST = (CommonAST)n.getUserObject();
								parentAST = (CommonAST)parent.getUserObject();
								//System.out.println("OncASTTreeModel.getAllSetVariables: " + currentAST.getText());
								//if(currentAST.getText().contains("`"))
								//	System.out.println("OncASTTreeModel.getAllSetVariables: " + currentAST.getText());
								if ( currentAST.getType() == JavaRecognizer.IDENT 
										 && isAssignment(parentAST.getType())
										 && parent.getIndex(n) == 0) {
										allSetVariables.add(currentAST.getText());
								}
						}
				}
				return allSetVariables;
				
		}
		
		// Get all the dependencies for the named variable
		public Vector getVariableDependencies(String variableName) {
				// If this has not been computed compute now - do once per ast model 
				// instance
				if ( allConditionalDependencies == null ) {
						allConditionalDependencies = getAllConditionalDependencies();
				}
				if ( allSetDependencies == null ) {
						allSetDependencies = getAllSetDependencies();
				}

				// Get the conditional dependencies for this variable
				UniqueVector condDeps = 
						(UniqueVector)allConditionalDependencies.get(variableName);
				// Get the set dependencies for this variable
				Vector setDeps = 
						(Vector)allSetDependencies.get(variableName);
				// Combine lists and return unique list
				if ( condDeps != null) {
						if ( setDeps != null ) 
								condDeps.addAll(setDeps);
						return condDeps;
				}
				else
						if ( setDeps != null ) 
								return setDeps;
				return new Vector();
		}

		public Hashtable getAllConditionalDependencies() {
				Hashtable allConditionalDependencies = new Hashtable();
				Vector variables = getAllSetVariables();
				Vector condDeps = null;
				Vector variableNodes = null;
				Iterator ii = variables.iterator();
				String variableName = null;
				Object obj = null;
				while ( ii.hasNext() ) {
						variableName = (String)ii.next();
						System.out.println("The variable \"" + variableName + "\" is  set (getAllConditionalDependencies).");
						variableNodes = findVariableNodes(variableName);
						Iterator i = variableNodes.iterator();
						while ( i.hasNext() ) {
								obj = i.next();
								condDeps = getCondDependencies((DefaultMutableTreeNode)obj,
																							 variableName);
						}
						if ( condDeps != null ) 
								allConditionalDependencies.put(variableName, condDeps);
				}
				return allConditionalDependencies;
		}

		public Hashtable getAllSetDependencies() {
				Hashtable allSetDependencies = new Hashtable();
				Vector variables = getAllSetVariables();
				Vector variableNodes = null;
				Iterator ii = variables.iterator();
				String variableName = null;
				Object obj = null;
				while ( ii.hasNext() ) {
						Collection setDepV = new Vector();
						variableName = (String)ii.next();
						variableNodes = findVariableNodes(variableName);
						Iterator i = variableNodes.iterator();
						while ( i.hasNext() ) {
								obj = i.next();
								Vector setDeps = getSetDependencies((DefaultMutableTreeNode)obj, 
												variableName);
								if ( setDeps != null)
										setDepV = CollectionHelper.or(setDepV, setDeps);
						}
						if ( setDepV != null )   // THE BUG WAS HERE!!!
								allSetDependencies.put(variableName, setDepV);
				}
				return allSetDependencies;
		}

		// For the given compilation unit ( as small as a statement ) 
		// Return the variable names of variables declared
		// Not implemented yet
		public Vector getVariableDeclarations() {
				return null;
		}

		// Get all the variable conditions under which a variable depends on 
		// to be set
		public UniqueVector getCondDependencies(DefaultMutableTreeNode node, 
																						String varName) {
					DefaultMutableTreeNode condNode = 
							getProximalConditional(node);
					UniqueVector deps = new UniqueVector();
					UniqueVector tempDeps = null;
					while ( condNode != null ) {
							// If there is a condition get all variables in the expression
							Vector allSubordinateVariables = null;
							DefaultMutableTreeNode condExpr = null;
							condExpr = (DefaultMutableTreeNode)condNode.getFirstChild();
							if ( condExpr != null ) {
									tempDeps = getAllSubordinateVariables(condExpr);
									deps.addAll(createDependencies(varName, tempDeps, getOperatorType((DefaultMutableTreeNode)condNode)));

							}
							condNode = 
									getProximalConditional((DefaultMutableTreeNode)condNode.getParent());
					}

					return deps;
		}
		
		private int	getOperatorType(DefaultMutableTreeNode node) {
				if (node != null && node.getUserObject() instanceof CommonAST) {
						CommonAST ast = (CommonAST)node.getUserObject();
						return ast.getType();
				}
				return -1;
		}
		private UniqueVector 
				getAllSubordinateVariables(DefaultMutableTreeNode node) {
				UniqueVector allSubs = new UniqueVector();
				for (Enumeration e=node.depthFirstEnumeration(); 
						 e.hasMoreElements(); ) {
						DefaultMutableTreeNode n = 
								(DefaultMutableTreeNode)e.nextElement();
						if ( n.getUserObject() instanceof CommonAST ) {
								if ( isIdent(((CommonAST)n.getUserObject()).getType()) ) { 
 										allSubs.add(((CommonAST)n.getUserObject()).getText());
								}
						}
				}
				return allSubs;
		}
		
		// Get a variables that a given node that is being set is dependent upon
		public UniqueVector getSetDependencies(DefaultMutableTreeNode node,
																					 String varName) {
				if ( isDescendantOfCondExpr(node) ) 
						return null;
				if ( !isSetNode(node) ) 
						return null;
				dependencies.clear();

				// Starting with a tree node of an variable
				// that is being set
				DefaultMutableTreeNode sibling = node.getNextSibling(); //+ or ident
				if ( sibling == null ) 
						return dependencies;
				if ( ((CommonAST)sibling.getUserObject()).getType() == 
						 JavaRecognizer.IDENT ) {
						dependencies.add(
										 ((CommonAST)sibling.getUserObject()).getText());
				}
				else 
					collectIdentifiers(sibling);

				return createDependencies(varName, dependencies, 
																	getOperatorType((DefaultMutableTreeNode)node.getParent()));
		}
		
		//Collect all identifiers below this node in the tree path
		public UniqueVector collectIdentifiers(DefaultMutableTreeNode node) {
				if ( node == null || node.getChildCount() <= 0 ) 
						return new UniqueVector();
				DefaultMutableTreeNode firstChild = 
						(DefaultMutableTreeNode)node.getFirstChild();
				while ( firstChild != null ) {
						DefaultMutableTreeNode secondChild = null;
						// If this is an assignment only get the first child
						if ( isAssignment(((CommonAST)node.getUserObject()).getType() ) ) {
								dependencies.add(
																 ((CommonAST)firstChild.getUserObject()).getText());
								return dependencies;
						}
						if (isMethodCall(((CommonAST)node.getUserObject()).getType()) ) {
								secondChild = firstChild.getNextSibling();
								collectIdentifiers(secondChild);
						}
						else if (isIdent(((CommonAST)firstChild.getUserObject()).getType())) {
								dependencies.add(
																 ((CommonAST)firstChild.getUserObject()).getText());
								// Get the second child which must also be a IDENT if it exists
								secondChild = (DefaultMutableTreeNode)firstChild.getNextSibling();
								if ( secondChild != null ) 
										dependencies.add(
																		 ((CommonAST)secondChild.getUserObject()).getText());
						}
						else {
								// Go deeper
								collectIdentifiers(firstChild);
								// Now take care of second child
								secondChild = firstChild.getNextSibling();
								if ( secondChild != null &&
										 ((CommonAST)secondChild.getUserObject()).getType() == 
										 JavaRecognizer.IDENT ) {
										dependencies.add(
																		 ((CommonAST)secondChild.getUserObject()).getText());
								}
						}
						firstChild = firstChild.getNextSibling();
				}
				return dependencies;
		}

		// CReate Variable dependencies left side  / right side
		private UniqueVector createDependencies(String varName, Vector tempDeps,
																						int operatorType) {
				UniqueVector depV = new UniqueVector();
				Iterator i = tempDeps.iterator();
				String thisVar = null;
				while ( i.hasNext() ) {
						thisVar = (String)i.next();
						if ( !varName.equals(thisVar) ) {
								VariableDependency varDep = 
										new VariableDependency(varName, thisVar, operatorType);
								depV.add(varDep);
						}
				}
				return depV;
		}

		private boolean isDescendantOfCondExpr(DefaultMutableTreeNode node) {
				// If this variable is a descendent of a conditional statement
				// determine if it is a part of the expression versus 
				// the conditionally executed block of statements ( 1 or more)
				if ( getProximalConditional(node) != null ){
						DefaultMutableTreeNode firstChild = 
								(DefaultMutableTreeNode)(getProximalConditional(node)).getFirstChild();
						if ( firstChild.isNodeDescendant(node) )
								return true;
				}
				return false;
		}
		private boolean isSetNode(DefaultMutableTreeNode node) {
				// If this variablenode is a variable being set
				DefaultMutableTreeNode parent = 
						(DefaultMutableTreeNode)node.getParent();
				if ( parent.getUserObject() != null 
						 && parent.getUserObject() instanceof CommonAST) {
						return isAssignment(((CommonAST)parent.getUserObject()).getType());
				}
				return false;
		}

		private DefaultMutableTreeNode 
				getProximalConditional(DefaultMutableTreeNode node) {
				//DefaultMutableTreeNode previousNode = node.getPreviousNode();
				DefaultMutableTreeNode previousNode = (DefaultMutableTreeNode)node.getParent();
				if ( previousNode == null )
						return null;
				else if ( isConditionalStatement(((CommonAST)previousNode.getUserObject()).getType()) ) {
						return previousNode;
				}
				else 
						return getProximalConditional(previousNode);
		}
				
		public Vector getVariableDependencies() {
				UniqueVector variableDeps = new UniqueVector();
				// Combine both hashtables
				// If this has not been computed compute now - do once per ast model 
				// instance
				if ( allConditionalDependencies == null ) {
						allConditionalDependencies = getAllConditionalDependencies();
				}
				if ( allSetDependencies == null ) {
						allSetDependencies = getAllSetDependencies();
				}
				Collection condDepsByVar = allConditionalDependencies.values();
				Collection setDepsByVar = allSetDependencies.values();
				// Unpack to a single vector of VariableDependencies
				Vector v = null;
				Iterator i = condDepsByVar.iterator();
				while ( i.hasNext() ) {
						v = (Vector)i.next();
						variableDeps.addAll(v);
				}
				i = setDepsByVar.iterator();
				while ( i.hasNext() ) {
						 v = (Vector)i.next();
						 variableDeps.addAll(v);
				}
				return variableDeps;
		}
		public Vector getVariableDependenciesAsStrings() {
				return declaredVariables;
		}
		public Vector getMethodCalls() {
				return methodCalls;
		}
		public Vector getUndefinedVariables() {
				return null;
		}


		// Walk the ASTTreeModel convert to really usable tree model
		// and clean ( collapse dots etc)
		public DefaultMutableTreeNode getTrueTreeModel(AST astRootNode) {
				CommonAST currentNode = (CommonAST)astRootNode;
				DefaultMutableTreeNode root = new DefaultMutableTreeNode();
				getChildren(root, 
										null,
										currentNode,
										null);
				if ( showTree ) {
						JTree tree = new JTree(root);
						final JFrame treePanel = new JFrame();
						treePanel.getContentPane().add(tree);
						treePanel.setVisible(true);
						treePanel.addWindowListener
								(
								 new WindowAdapter() {
										 public void windowClosing (WindowEvent e) {
												 treePanel.setVisible(false); // hide the TreePanel
												 treePanel.dispose();
												 System.exit(0);
										 }
								 }
								 );
				}
				return root;
		}

		private void getChildren(DefaultMutableTreeNode currentNode, 
														 DefaultMutableTreeNode parentNode,
														 AST node, AST myParentNode) {
				String currentIdentifier = null;
				currentNode.setUserObject(node);
				AST nextSibling = node;
				DefaultMutableTreeNode newNode = null;
				while ( nextSibling != null ) {
						//System.out.println("nextSibling is " + nextSibling);
						AST firstChild = nextSibling.getFirstChild();
						if ( firstChild != null ) {
								if (nextSibling.getType() == JavaRecognizer.DOT) {
										String compoundIdentifier = "";
										compoundIdentifier = 
												collapseDotIdentifier(nextSibling, 
																							compoundIdentifier);	
										// replace backquotes with appropriate string
										nextSibling.setText(dissectString.fixBackquotes(compoundIdentifier));
										nextSibling.setType(JavaRecognizer.IDENT);
										currentNode =
												new DefaultMutableTreeNode(nextSibling);
										if ( parentNode != null )
												parentNode.add(currentNode);	

								}// has dot
								else { // no dot
										if ( parentNode != null ) {
												currentIdentifier = nextSibling.getText();
												nextSibling.setText(dissectString.fixBackquotes(currentIdentifier));
												currentNode =
														new DefaultMutableTreeNode(nextSibling);
												parentNode.add(currentNode);
										}
										currentIdentifier = firstChild.getText();
										firstChild.setText(dissectString.fixBackquotes(currentIdentifier));
										newNode =
												new DefaultMutableTreeNode(firstChild);
										
										// drill down
										getChildren(newNode, currentNode, firstChild, nextSibling);

										if ( parentNode != null )
												parentNode.add(currentNode);	
								}
						} // if firstChild != null
						else { // does not have a child

								if ( !currentNode.getUserObject().equals(nextSibling) ) {
										currentIdentifier = nextSibling.getText();
										nextSibling.setText(dissectString.fixBackquotes(currentIdentifier));
										currentNode = new DefaultMutableTreeNode(nextSibling);
								}
								if ( parentNode != null )
										parentNode.add(currentNode);	

						}
						nextSibling = nextSibling.getNextSibling();
				}// while
		}
		//////////////////////////////
		private void fileType(AST node, AST parent) {
				if (parent == null)
						return;
				switch(parent.getType()) {
				case JavaRecognizer.VARIABLE_DEF:
						if ( node.getType() == JavaRecognizer.IDENT ) {
								declaredVariables.add(node);
								//System.out.println("Does it go here ");
								//addSetVariable(blockVariables,node);
						}
						break;
				default:
						break;
				}
		}

		private String collapseDotIdentifier(AST node, String compoundIdentifier) {
				AST nextSibling = node;
				String ident = null;
				AST currentSibling = nextSibling.getFirstChild();
				while ( currentSibling != null ) {
						if (currentSibling.getType() == JavaRecognizer.DOT) {
								ident = 
										collapseDotIdentifier(currentSibling, 
																					compoundIdentifier);
								compoundIdentifier = ident + "." 
										+ currentSibling.getNextSibling().getText();
								currentSibling = null;
						}
						else if (currentSibling.getType() == JavaRecognizer.IDENT) {
								ident =  currentSibling.getText();
								if ( compoundIdentifier.trim().length() > 0 )
										compoundIdentifier = compoundIdentifier + "." + ident  ;
								else 
										compoundIdentifier = ident;
								currentSibling = currentSibling.getNextSibling();
						}
						else {
								currentSibling = currentSibling.getNextSibling();
								
						}
				}
				return compoundIdentifier;
		}

		public boolean isIdent(int type) {
				if ( type == JavaRecognizer.IDENT ) 
						return true;
				return false;
		}
		public boolean isMethodCall(int type) {
				if ( type == JavaRecognizer.METHOD_CALL ) 
						return true;
				return false;
		}
		//	public boolean isMethodCall(DefaultMutableTreeNode node) {
		//		DefaultMutableTreeNode parent = 
		//				(DefaultMutableTreeNode)node.getParent();
		//		if ( node.getParent().
		//		if ( type == JavaRecognizer.IDENT ) 
		//				return true;
		//		return false;
		//}

		public boolean isConditionalStatement(int type) {
				switch ( type ) {
				case JavaRecognizer.LITERAL_if:
				case JavaRecognizer.LITERAL_else:
				case JavaRecognizer.LITERAL_for:
				case JavaRecognizer.LITERAL_while:
				case JavaRecognizer.LITERAL_do:
						return true;
				default:
						return false;
				}
		}
		
		public boolean isAssignment(int type) {
				switch ( type ) {
				case JavaRecognizer.ASSIGN:
				case JavaRecognizer.PLUS_ASSIGN:
				case JavaRecognizer.MINUS_ASSIGN:
				case JavaRecognizer.STAR_ASSIGN:
				case JavaRecognizer.DIV_ASSIGN:
				case JavaRecognizer.MOD_ASSIGN:
				case JavaRecognizer.SR_ASSIGN:
				case JavaRecognizer.BSR_ASSIGN:
				case JavaRecognizer.SL_ASSIGN:
				case JavaRecognizer.BAND_ASSIGN:
				case JavaRecognizer.BXOR_ASSIGN:
				case JavaRecognizer.BOR_ASSIGN:
				case JavaRecognizer.POST_INC:
				case JavaRecognizer.POST_DEC:
						return true;
				default:
						return false;
				}
		}

		private Vector findVariableNodes(String variableName) {
				Vector variableNodes = new Vector();
				for (Enumeration e=root.depthFirstEnumeration(); 
						 e.hasMoreElements(); ) {
						DefaultMutableTreeNode node = 
								(DefaultMutableTreeNode)e.nextElement();
						if ( variableName.equals(((CommonAST)node.getUserObject()).getText()) )
								variableNodes.add(node);
				}
				return variableNodes;
		}
		

		//// CLASS
		class UniqueVector extends Vector {
				public boolean add(Object obj) {
						if ( !this.contains(obj) ) 
								super.add(obj);
						return true;
				}
				public void print(){
					System.out.println(this);
				}
				public String toString(){
					return printableList();
				}
				public String printableList(){
					String s = "";
					Iterator it = this.iterator();
					while(it.hasNext()) s = s + it.next().toString();
					return(s);	
				}
		}
}
