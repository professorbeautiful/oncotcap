package oncotcap.datalayer.persistible.parameter;

import java.util.Vector;
import java.util.Iterator;
import oncotcap.util.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.persistibleeditorpanel.*;

public class VariableType
{

	private static Vector allTypes = new Vector();
	private static Vector allTypesExceptEnumsAndSwitchablePositive = new Vector();
	
	public static final VariableType POSITIVE = new VariableType("Multiplyable",
																					 "oncotcap.display.editor.persistibleeditorpanel.PositiveEditor",
																					 "oncotcap.datalayer.persistible.parameter.DeclarePositive",
																					 "oncotcap.display.editor.persistibleeditorpanel.ModifyPositiveEditor", "oncotcap.datalayer.persistible.action.ModifyPositive",
																					 "oncotcap.datalayer.persistible.parameter.DeclarePositive");
	public static final VariableType SWITCHABLE = new VariableType("Switchable",
																						"oncotcap.display.editor.persistibleeditorpanel.SwitchableEditor",
																						"oncotcap.datalayer.persistible.parameter.DeclareSwitchable",
																						"oncotcap.display.editor.persistibleeditorpanel.ModifySwitchableEditor",
																						"oncotcap.datalayer.persistible.action.ModifySwitchable",
																						"oncotcap.datalayer.persistible.parameter.DeclareSwitchable" );
	public static final VariableType SWITCHABLE_POSITIVE = new VariableType("Switchable Multiplyable",
																									"oncotcap.display.editor.persistibleeditorpanel.SwitchablePositiveEditor",
																									"oncotcap.datalayer.persistible.parameter.DeclareSwitchablePositive",
																									"oncotcap.display.editor.persistibleeditorpanel.ModifySwitchablePositiveEditor",
																									"oncotcap.datalayer.persistible.action.ModifySwitchablePositive",
																									"oncotcap.datalayer.persistible.parameter.DeclareSwitchablePositive");
	public static final VariableType POSITIVE_INTEGER = new VariableType("Incrementable",
																								"oncotcap.display.editor.persistibleeditorpanel.PositiveIntegerEditor",
																								"oncotcap.datalayer.persistible.parameter.DeclarePositiveInteger",
																								"oncotcap.display.editor.persistibleeditorpanel.ModifyPositiveIntegerEditor",
																								"oncotcap.datalayer.persistible.action.ModifyPositiveInteger",
																								"oncotcap.datalayer.persistible.parameter.DeclarePositiveInteger");
	public static final VariableType STRING = new VariableType("String",
																				  "oncotcap.display.editor.persistibleeditorpanel.TcapStringEditor",
																				  "oncotcap.datalayer.persistible.parameter.TcapString",
																				  "oncotcap.display.editor.persistibleeditorpanel.ModifyStringEditor",
																				  "oncotcap.datalayer.persistible.action.ModifyString",
																				  "oncotcap.datalayer.persistible.parameter.TcapString");
	public static final VariableType ENUM = new VariableType("Characteristic", "oncotcap.display.editor.persistibleeditorpanel.EnumEditor",
																										"oncotcap.datalayer.persistible.parameter.DeclareEnum",
																										"oncotcap.display.editor.persistibleeditorpanel.ModifyEnumEditor",
																										"oncotcap.datalayer.persistible.action.ModifyEnum",
																									"oncotcap.datalayer.persistible.parameter.DeclareEnum");
	
//	public static final VariableType ENUM_PICKER = new VariableType("Characteristic Picker", "oncotcap.display.editor.persistibleeditorpanel.EnumEditor", "oncotcap.datalayer.persistible.DeclareEnum", "oncotcap.display.editor.persistibleeditorpanel.ModifyPositiveEditor", "oncotcap.datalayer.persistible.ModifyPositive", "oncotcap.datalayer.persistible.DeclareEnumPicker");


	
	private String name;
	private String varStorage;
	private TypeHelper typeHelper;
	private Class clsModifierStorage = null;
	private Class clsModifierEditor = null;
	private Class clsVarStorage = null;
	
	public static void main(String [] args)
	{
		System.out.println("IN MAIN");
		Iterator it = getAllTypes().iterator();
		while(it.hasNext())
			System.out.println("***" + it.next());
	}
   public VariableType(String name, String editor, String storage, String modEditor, String modStorage, String varStorage)
	{
		this.name = name;
		this.varStorage = varStorage;
		typeHelper = new TypeHelper(editor, storage);
		clsModifierEditor = ReflectionHelper.classForName(modEditor);
		clsModifierStorage = ReflectionHelper.classForName(modStorage);
		clsVarStorage = ReflectionHelper.classForName(varStorage);
		allTypes.add(this);
		
		if(! name.equals("Characteristic") && ! name.equals("Switchable Multiplyable"))
			allTypesExceptEnumsAndSwitchablePositive.add(this);
		
	}
	public String getName()
	{
		return(name);
	}
	public static Vector getAllTypes()
	{
		return(allTypes);
	}
	public String toString()
	{
		return(getName());
	}
	public Class getEditorClass()
	{
		return(typeHelper.getEditorClass());
	}
	public Class getStorageClass()
	{
		return(typeHelper.getStorageClass());
	}
	public void setStorageClass(Class storage)
	{
		typeHelper.setStorageClass(storage);
	}
	public DeclareVariable getStorageInstance()
	{
		return((DeclareVariable) typeHelper.getStorageInstance());
	}
	public VariableEditor getEditor()
	{
		return((VariableEditor) typeHelper.getEditorInstance());
	}
	public EditorPanel getEditor(DeclareVariable var)
	{
		return(typeHelper.getEditorInstance(var));
	}
	public EditorPanel getEditorWithInstance()
	{
		EditorPanel ep = (EditorPanel) getEditor();
		ep.edit(getStorageInstance());
		return(ep);
	}
/*	public static EditorPanel getDeclarationEditor(Variable var)
	{
		VariableType testType;
		Class compClass = var.getClass();
		Iterator it = allTypes.iterator();
		while(it.hasNext())
		{
			testType = (VariableType) it.next();
			if(var.equals(testType.clsVarStorage))
				return(testType.getEditor());
		}
		return(null);
	} */
	public static EditorPanel getDeclarationEditor(String variableClassName)
	{
		VariableType testType;
		if(variableClassName != null)
		{
			Iterator it = allTypes.iterator();
			while(it.hasNext())
			{
				testType = (VariableType) it.next();
				if(variableClassName.equalsIgnoreCase(testType.varStorage) || 
				   variableClassName.equalsIgnoreCase(testType.name))
					return(testType.getEditor());
			}
			return(null);
		}
		else
			return(null);
	}
	public static DeclareVariable getDeclarationInstance(String variableClassName)
	{
		VariableType testType;
		if(variableClassName != null)
		{
			Iterator it = allTypes.iterator();
			while(it.hasNext())
			{
				testType = (VariableType) it.next();
				if(variableClassName.equalsIgnoreCase(testType.varStorage) || 
					  variableClassName.equalsIgnoreCase(testType.name))
					return(testType.getStorageInstance());
			}
			return(null);
		}
		else
			return(null);
	}
	public static VariableModification getModificationInstance(String variableClassName)
	{
		VariableType testType;
		if(variableClassName != null)
		{
			Iterator it = allTypes.iterator();
			while(it.hasNext())
			{
				testType = (VariableType) it.next();
				if(variableClassName.equalsIgnoreCase(testType.varStorage) || 
					  variableClassName.equalsIgnoreCase(testType.name))
					return((VariableModification) testType.getModifierStorageInstance());
			}
			return(null);
		}
		else
			return(null);
	}
	public static VariableType getByName(String name)
	{
		Iterator it = allTypes.iterator();
		while(it.hasNext())
		{
			VariableType vType = (VariableType) it.next();
			if(vType.getName().equalsIgnoreCase(name))
				return(vType);
		}
		return(null);

	}
	private Vector modifiers = new Vector();
	private void addModifier(String modifier)
	{
		if(modifier != null)
			modifiers.add(modifier);
	}
	public Vector getModifiers()
	{
		return(modifiers);
	}
	public static DeclareVariable varChooser()
	{
		Object type = ListDialog.getValue("Choose a variable type.", allTypesExceptEnumsAndSwitchablePositive);
		if(type != null && type instanceof VariableType)
		{
			return(((VariableType) type).getStorageInstance());
		}
		else
			return(null);
	}
	public VariableModification getModifierStorageInstance()
	{
		try
		{
			return((VariableModification) clsModifierStorage.newInstance());
		}
		catch(InstantiationException e){return(null);}
		catch(IllegalAccessException e){return(null);}
	}
	public AbstractVariableEditor getModifierEditorInstance()
	{
		try
		{
			return((AbstractVariableEditor) clsModifierEditor.newInstance());
		}
		catch(InstantiationException e){return(null);}
		catch(IllegalAccessException e){return(null);}
	}
}