package oncotcap.datalayer;  // REPACKAGE THIS
import java.util.*;
import java.io.Serializable;
import oncotcap.Oncotcap;
import oncotcap.util.ReflectionHelper;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.persistible.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.reflect.*;
import java.util.*;

abstract public class AutoGenPersistibleWithKeywords 
		extends AbstractDroppableWithKeywords 
		implements AutoGenEditable, Editable, FocusListener, Serializable {
		
		protected Hashtable setMethodMap = new Hashtable();
		protected Hashtable getMethodMap = new Hashtable();
		public ImageIcon icon =	null;

		public AutoGenPersistibleWithKeywords(){}
		public AutoGenPersistibleWithKeywords(oncotcap.util.GUID guid){
				super(guid);
		}

		public Method getSetter(String name, Class argName) {
				Class [] paramList = new Class[1];
				Method setMethod = null;
				try {
				
					// 	System.out.println("setter " + name + "  " 
						// 															 + paramList + " " + argName);
						if ( argName == DefaultPersistibleList.class ) 
								argName = Collection.class;
						// 	System.out.println("setter " + name + "  " 
						// 															 + paramList + " " + argName);
						paramList[0] = argName;
						setMethod = 
								this.getClass().getMethod(name,
																					paramList);
				}
				catch ( Exception ea ) {
						ea.printStackTrace();
				}
				return setMethod;
		}

		public Hashtable getGetterMap() {
				return getMethodMap;
		}
		public Hashtable getSetterMap() {
				return setMethodMap;
		}

		public Method getGetter(String name) {
				Method getMethod = null;
				try {
						getMethod = 
								this.getClass().getMethod(name, (Class []) null);
				}
				catch ( Exception ea ) {
						ea.printStackTrace();
				}
				return getMethod;
		}

		public Method getGetMethod(String name) {
				if ( name != null ) 
						return (Method)getMethodMap.get(name);
				else 
						return null;
		}

		public Collection getAll() {
				OncoTCapDataSource dataSource = Oncotcap.getDataSource();
				 Collection allItemsOfThisType = 
						dataSource.find(this.getClass());			
				return allItemsOfThisType;
		}
		public static Collection getAll(Class cls) {
				OncoTCapDataSource dataSource = Oncotcap.getDataSource();
				 Collection allItemsOfThisType = 
						dataSource.find(cls);			
				return allItemsOfThisType;
		}
		public void focusGained(FocusEvent e) {
				//				System.out.println("focus gained" + e.getSource());
				Object source = e.getSource();
				if ( source instanceof JTextArea ) {
						try {
								String fieldName = 
										(String)((JTextArea)source).getClientProperty("Field");
								String setMethodName = 
										(String)((JTextArea)source).getClientProperty("setMethod");
								String fieldValue = ((JTextArea)source).getText();
								//System.out.println("FieldValue " + fieldValue);
						}
						catch ( Exception ea ) {
								ea.printStackTrace();
						}
				}
				
				
		}

		public void focusLost(FocusEvent e) {
			/* TODO: field focus
				Object source = e.getSource();
				Class paramList[];
				Object args[] = new Object[1];
				String fieldName = null;
				if ( source instanceof JTextArea ) {
					Method setMethod = null;
						try {
								fieldName = 
										(String)((JTextArea)source).getClientProperty("Field");
								String setMethodName = 
										(String)((JTextArea)source).getClientProperty("setMethod");
								String fieldValue = ((JTextArea)source).getText();
								paramList = new Class[1];
								// 								System.out.println("Field" + fieldName);
								// 								System.out.println("getMethod" + fieldName);
								//								System.out.println("setMethod " 
								//									 + (Method)setMethodMap.get(fieldName));
								//System.out.println("FieldValue " + fieldValue);
								setMethod = (Method)setMethodMap.get(fieldName);
								
								args[0] = fieldValue ;
								if ( setMethod != null )
										ReflectionHelper.invoke(setMethod, this, args);
								else {
										
										Field field = this.getClass().getField(fieldName);
										if ( field != null )
												field.set(this, fieldValue);
								}
						}
						catch ( NoSuchFieldException nsfe ) {
								System.out.println("Field doesn't exist or is not public: " +
																	 fieldName + " setMethod " + setMethod + " on instance of" + this.getClass());
								
						}
						catch ( Exception ea ) {
								ea.printStackTrace();
						}
				}
				*/
		}

		public String toString() {
				return this.toString();
		}
		public String getDisplayString() {
				return this.toString();
		}
		public EditorPanel getEditorPanel()
		{ 
				EditorPanel thePanel = null;
				try { 
						// Get the constructor
						Object paramTypes = Array.newInstance(Class.class, 1);
						Array.set(paramTypes, 0, this.getClass());
						Constructor constr = 
								this.getPanelClass().getConstructor((Class[])paramTypes);
						Object [] constructorParams = (Object [])Array.newInstance(Object.class, 1);
						Array.set(constructorParams, 0, this);
						thePanel = 
								(EditorPanel)constr.newInstance(constructorParams);
						thePanel.edit(this);
				}	catch ( Exception ea ) {
						ea.printStackTrace();
						if (ea instanceof java.lang.reflect.InvocationTargetException)
								System.out.println("CAUSE" + ((java.lang.reflect.InvocationTargetException)ea).getCause());
				}
				return(thePanel);
		}

		public EditorPanel getEditorPanelWithInstance() {
				EditorPanel thePanel = null;
				try { 
						thePanel = 
								(EditorPanel)this.getPanelClass().newInstance();
						thePanel.edit(this);
				}	catch ( Exception ea ) {
						ea.printStackTrace();
				}
				return(thePanel);
		}
		public boolean showText() {
				return true;
		}
		abstract public Class getPanelClass();
		
}
