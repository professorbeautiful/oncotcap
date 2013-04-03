package oncotcap.util;

import java.io.*;
import java.util.*;
import java.awt.event.*;
import java.util.regex.*;

import antlr.collections.AST;
import antlr.collections.impl.*;
import antlr.debug.misc.*;
import antlr.*;

public class DissectString {

		static boolean showTree = false;
		public String wholeString = null;
		static int counter = 0;
		//OncASTTreeModel astTreeModel = null;
		Hashtable backQuotedStrings = null;

		public DissectString(String str) {
				this.wholeString = str;
				try {
						if ( wholeString.indexOf(";") <= -1 ) 
								wholeString = wholeString + ";";
						wholeString = "{"+wholeString+"}";
				}	catch(Exception e) {
						System.err.println("exception: "+e);
						e.printStackTrace(System.err);   // so we can get stack trace
				}
		}

    public static void main(String[] args) {
				// Use a try/catch block for parser exceptions
				OncASTTreeModel astTreeModel = null;
				try {
						// if we have at least one command-line argument
						if (args.length > 0 ) {
								System.err.println("Parsing...");
								
								// for each directory/file specified on the command line
								for(int i=0; i< args.length;i++) {
										if ( args[i].equals("-showtree") ) {
												showTree = true;
										}
										else if ( args[i].equals("-file") ) {
												// Get string out of file 
												astTreeModel = 
														doFile(new File(args[i+1])); // parse it
												// Make sure there is no ; at the end of the string
										
												//OncASTTreeModel astTreeModel = dissectString();
												System.out.println("All Variables 2" + 
																					 astTreeModel.getAllVariables());
												System.out.println("Set Variables 2" + 
																					 astTreeModel.getAllSetVariables());
												System.out.println("Variable Dependencies" + 
																					 astTreeModel.getVariableDependencies());
												System.out.println("Variable Dependencies" 
																					 + astTreeModel.getVariableDependencies("A"));
												return;
										}
										else {
												// Make sure there is no ; at the end of the string
												DissectString dissectString = 
														new DissectString(args[i]);
												astTreeModel = dissectString.dissectString();
												System.out.println("All Variables 2" + 
																					 astTreeModel.getAllVariables());
												System.out.println("Set Variables 2" + 
																					 astTreeModel.getAllSetVariables());
												System.out.println("Variable Dependencies" + 
																					 astTreeModel.getVariableDependencies());
												System.out.println("Variable Dependencies" 
																					 + astTreeModel.getVariableDependencies("A"));

										}
								} 
						}
						else
								System.err.println("Usage: java DissectString [-showtree] "+
                                   "<string to dissect>");
				}
				catch(Exception e) {
						System.err.println("exception: "+e);
						e.printStackTrace(System.err);   // so we can get stack trace
				}
		}

		// This method decides what action to take based on the type of
		//   file we are looking at
		public static OncASTTreeModel doFile(File f)
				throws Exception {
				// If this is a directory, walk each file/dir in that directory
				if (f.isDirectory()) {
						String files[] = f.list();
						for(int i=0; i < files.length; i++)
								doFile(new File(f, files[i]));
				}
				
				// otherwise, if this is a java file, parse it!
				else if ((f.getName().length()>4) &&
								 f.getName().substring(f.getName().length()-4).equals(".txt")) {
						System.err.println("   "+f.getAbsolutePath());
						// parseFile(f.getName(), new FileInputStream(f));
						return dissectString(new BufferedReader(new FileReader(f)));
				}
				return null;
		}
		
		// Here's where we do the real work...
		public static OncASTTreeModel dissectString(BufferedReader bufferedReader) {
				try {
						// Create a scanner that reads from the input stream passed to us
						//StringReader stringReader = new StringReader(wholeString);
						JavaLexer lexer = new JavaLexer(bufferedReader);
						
						// Create a parser that reads from the scanner
						JavaRecognizer parser = new JavaRecognizer(lexer);
						
						// start parsing at the compilationUnit rule
						parser.compilationUnit();
						
						// build the tree that will be used to get the variables 
						// and methods
						return buildASTTree(null, parser.getAST(), parser.getTokenNames(), new DissectString(""));
				}
				catch (Exception e) {
						System.err.println("parser exception: "+e
															 );
						e.printStackTrace();   // so we can get stack trace		
				}
				return null;
		}
		// Here's where we do the real work...
		public OncASTTreeModel dissectString()
				throws Exception {
				try {
						if ( wholeString == null || wholeString.trim().length() == 0 )
								return null;
						// Preprocess string to handle backquoted strings
						wholeString = processStringForBackquotes(wholeString);
						if ( wholeString.indexOf(";" ) <= -1 )
								wholeString = wholeString + ";";

						// Create a scanner that reads from the input stream passed to us
						StringReader stringReader = new StringReader("{"+wholeString+"\n}");
						JavaLexer lexer = new JavaLexer(stringReader);
						
						// Create a parser that reads from the scanner
						JavaRecognizer parser = new JavaRecognizer(lexer);
						
						// start parsing at the compilationUnit rule
						parser.compilationUnit();
						
						// build the tree that will be used to get the variables 
						// and methods
						AST ast = parser.getAST();
						return buildASTTree(null, ast, parser.getTokenNames(),
																this);
				}
				catch (Exception e) {
						System.err.println("parser exception: "+ e);
						System.err.println("parsing string: " + "{" + wholeString + "\n}");
						//e.printStackTrace();   // so we can get stack trace		
				}
				return null;
		}

		// Extract all backquoted strings substitute with a backquoted 
		// guid put backquoted guid string and original string in a hashtable
		public String processStringForBackquotes(String str) {
				backQuotedStrings = new Hashtable();
				StringWriter resolvedString = new StringWriter();
				String backQuotedString = null;
				String guidString = null;
				// Use this value map guid = originalstring
				// backquoted strings 
				int idx = 0;
				Matcher match = 
						oncotcap.engine.ValueMap.BACK_QUOTED_VAR.matcher(str);
				
				// If there are no back quotes pass the string back as is
				String value = null;
				if ( !match.find() ) {
						return str;
				}
				else {
						match.reset();
						// Patch the string back together fully
						// substituted 
						while(match.find()) {
								resolvedString.append(str.substring(idx, 
																										match.start()));
								backQuotedString = 
										str.substring(match.start()+1, 
																							 match.end()-1).trim();
								guidString = GUID.nextGUID();
								
								backQuotedStrings.put(guidString, backQuotedString);
								resolvedString.append("_");
								resolvedString.append(guidString);
								resolvedString.append("_");
								idx = match.end();
						}
				}
				// put the rest of the string on the end
				resolvedString.append(str.substring(idx, 
																							str.length()));
				return resolvedString.toString();
				
		}
		// Convert backquoted GUIDs into their original backquoted string 
		public String fixBackquotes(String ident) {
				// this substitution uses a plan hashtable where key is vme.getName(),
				// and value is vme.getValue() to substitute for backquoted 
				// strings
				StringWriter resolvedString = new StringWriter();
				String backQuotedString = null;
				// Use this value map guid = originalstring
				// backquoted strings 
				// Take the identifier string and loop thru the hashtable and
				// replace all hashtable keys in the string
				String guidString = null;
				String originalString = null;
				for (Enumeration e= backQuotedStrings.keys(); 
						 e.hasMoreElements(); ) {
						guidString = (String)e.nextElement();	
						String searchString = "_" + guidString + "_";
						originalString = (String)backQuotedStrings.get(guidString);
						String bQuoteString = "`" + originalString + "`";
						ident = ident.replaceAll(searchString, bQuoteString);
				}	
				return ident;
		}

		public static OncASTTreeModel buildASTTree(String f, AST t, 
																							 String[] tokenNames,
																							 DissectString dissectString) {
				if ( t==null ) return null;
				((CommonAST)t).setVerboseStringConversion(true, tokenNames);
				ASTFactory factory = new ASTFactory();
				AST r = factory.create(0,"AST ROOT");
				r.setFirstChild(t);
				OncASTTreeModel astTreeModel = new OncASTTreeModel(r, showTree, 
																													 dissectString);
				
				if ( counter < 0) {
						counter++;
						final ASTFrame frame = new ASTFrame("Java AST", r);
						frame.setVisible(true);
						frame.addWindowListener
								(
								 new WindowAdapter() {
										 public void windowClosing (WindowEvent e) {
												 frame.setVisible(false); // hide the Frame
												 frame.dispose();
												 System.exit(0);
										 }
								 }
								 );
						//System.out.println(t.toStringList());
				}
				return astTreeModel;
		}
		
}

