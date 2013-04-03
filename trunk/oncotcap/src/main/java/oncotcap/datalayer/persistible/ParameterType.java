package oncotcap.datalayer.persistible;

import oncotcap.display.common.UserInputDialog;
import oncotcap.display.editor.*;
import oncotcap.util.*;
import java.util.Vector;
import java.util.Iterator;
import oncotcap.datalayer.AbstractPersistible;
import oncotcap.datalayer.persistible.parameter.*;

public class ParameterType extends AbstractPersistible
{
	private String name;
	private static Vector parameterTypes = new Vector();
	private SingleParameterList singleParameters = new SingleParameterList();
	public final static ParameterType BLANK = addParameterType("");
	public final static ParameterType FLOAT = addParameterType("Float");
	public final static ParameterType INTEGER = addParameterType("Integer");
	public final static ParameterType STRING = addParameterType("String");
	public final static ParameterType POSITIVE = addParameterType("Positive");
	public final static ParameterType SWITCHABLEPOSITIVE = addParameterType("Switchable Positive");
	public final static ParameterType SCHEDULE = addParameterType("Treatment Schedule");
//	public final static ParameterType ENUM = addParameterType("Characteristic");
	public final static ParameterType ENUM_PICKER = addParameterType("Characteristic Picker");
		//public final static ParameterType TWO_LEVEL_PARAMETER = addParameterType("Two Level Parameter");	
		public final static ParameterType DECLARE_TWO_LEVEL_PARAMETER = addParameterType("Two Level Parameter");
	public final static ParameterType SIMON_TWO_STAGE_DESIGN = addParameterType("Simon Two Stage Phase II Clinical Trial Design");
	public final static ParameterType CATEGORY = addParameterType("Category");
		//public final static ParameterType FUNCTION_PARAMETER = 
		//	addParameterType("Function");
	public final static ParameterType CONDITIONAL_TABLE_PARAMETER = 
			addParameterType("Conditional Table");
	//public final static ParameterType CONDITIONAL_EVT_TABLE_PARAMETER = 
	//		addParameterType("Conditional Event Table");
	static
	{
		FLOAT.setEditor(ReflectionHelper.classForName("oncotcap.display.common.SingleValueInputDialog"));
		FLOAT.setDataType(ReflectionHelper.classForName("oncotcap.datalayer.persistible.parameter.TcapFloat"));
//		FLOAT.addSingleParameter("Numeric Value", "");
		INTEGER.setEditor(ReflectionHelper.classForName("oncotcap.display.common.SingleValueInputDialog"));
		INTEGER.setDataType(ReflectionHelper.classForName("oncotcap.datalayer.persistible.parameter.TcapInteger"));
//		INTEGER.addSingleParameter("Integer Value", "");
		STRING.setEditor(ReflectionHelper.classForName("oncotcap.display.common.SingleValueInputDialog"));
		STRING.setDataType(ReflectionHelper.classForName("oncotcap.datalayer.persistible.parameter.TcapString"));
//		STRING.addSingleParameter("Text Value", "");
		POSITIVE.setEditor(ReflectionHelper.classForName("oncotcap.display.common.SingleValueInputDialog"));
		POSITIVE.setDataType(ReflectionHelper.classForName("oncotcap.datalayer.persistible.parameter.DeclarePositive"));
//		POSITIVE.addSingleParameter("Positive Value", "");
		SWITCHABLEPOSITIVE.setEditor(ReflectionHelper.classForName("oncotcap.display.common.SingleValueInputDialog"));
		SWITCHABLEPOSITIVE.setDataType(ReflectionHelper.classForName("oncotcap.datalayer.persistible.parameter.DeclareSwitchablePositive"));
//		SWITCHABLEPOSITIVE.addSingleParameter("Positive Value", "");
		SCHEDULE.setEditor(ReflectionHelper.classForName("oncotcap.display.common.ScheduleInputDialog"));
		SCHEDULE.setDataType(ReflectionHelper.classForName("oncotcap.datalayer.persistible.parameter.TreatmentSchedule"));
//		SCHEDULE.addSingleParameter("Course Duration", "");
//		SCHEDULE.addSingleParameter("Treatement Days", "");
//		SCHEDULE.addSingleParameter("Number of Courses", "");
//		ENUM.setEditor(ReflectionHelper.classForName("oncotcap.display.editor.persistibleeditorpanel.EnumEditor"));
//		ENUM.setDataType(ReflectionHelper.classForName("oncotcap.datalayer.persistible.parameter.DeclareEnum"));
//		ENUM_PICKER.setEditor(ReflectionHelper.classForName("oncotcap.display.editor.persistibleeditorpanel.DeclareEnumPickerEditor"));
		ENUM_PICKER.setDataType(ReflectionHelper.classForName("oncotcap.datalayer.persistible.parameter.DeclareEnumPicker"));
		DECLARE_TWO_LEVEL_PARAMETER.setEditor(ReflectionHelper.classForName("oncotcap.display.editor.persistibleeditorpanel.DeclareTwoLevelParameterEditor"));
		DECLARE_TWO_LEVEL_PARAMETER.setDataType(ReflectionHelper.classForName("oncotcap.datalayer.persistible.parameter.DeclareTwoLevelParameter"));
		SIMON_TWO_STAGE_DESIGN.setEditor(ReflectionHelper.classForName("oncotcap.display.editor.persistibleeditorpanel.Phase2SimonEditorPanel"));
		SIMON_TWO_STAGE_DESIGN.setDataType(ReflectionHelper.classForName("oncotcap.datalayer.persistible.parameter.SimonCT"));
		CATEGORY.setEditor(ReflectionHelper.classForName("oncotcap.display.editor.persistibleeditorpanel.CategoryEditor"));
		CATEGORY.setDataType(ReflectionHelper.classForName("oncotcap.datalayer.persistible.parameter.DeclareCategory"));
		CONDITIONAL_TABLE_PARAMETER.setEditor(ReflectionHelper.classForName("oncotcap.display.editor.persistibleeditorpanel.ConditionalParameterEditor"));
		CONDITIONAL_TABLE_PARAMETER.setDataType(ReflectionHelper.classForName("oncotcap.datalayer.autogenpersistible.ConditionalTableParameter"));
		//CONDITIONAL_EVT_TABLE_PARAMETER.setEditor(ReflectionHelper.classForName("oncotcap.display.editor.persistibleeditorpanel.ConditionalParameterEditor"));
		//CONDITIONAL_EVT_TABLE_PARAMETER.setDataType(ReflectionHelper.classForName("oncotcap.datalayer.autogenpersistible.ConditionalTableEventParameter"));
		
	}
	public ParameterType() {}
	public ParameterType(oncotcap.util.GUID guid) {
		super(guid);
	}
	public ParameterType(String name)
	{
		this.name = name;
		parameterTypes.add(this);
	}
	public static Vector getParameterTypes()
	{
		return(parameterTypes);
	}
	public static ParameterType getByName(String name)
	{
		Iterator it = parameterTypes.iterator();
		while(it.hasNext())
		{
			ParameterType pType = (ParameterType) it.next();
			if(pType.getName().equalsIgnoreCase(name))
				return(pType);
		}
		return(null);

	}
/*	private static Class classForName(java.lang.String className)
	{
		Class rVal = null;
		try
		{
			rVal = Class.forName(className);
		}
		catch(ClassNotFoundException e)
		{
			Logger.log("Error: Cannot find class " + className + ". [ParameterType]");
			return(null);
		}
		finally
		{
			return(rVal);
		}
	}
*/
	private UserInputDialog editor = null;
	private Class clsEditor;
	private Class clsStorage;
	public void setDataType(Class clsStorage)
	{
		this.clsStorage = clsStorage;
	}
	public Parameter newStorageObject()
	{
		Parameter rVal = null;
		try
		{
			rVal = (Parameter) clsStorage.newInstance();
			if( !(rVal.getClass().equals(clsStorage)) )
				Logger.log("ERROR creating storage for parameter type " + getName() + "\nWrong type.  Expecting " + clsStorage.toString());

			if(rVal == null)
			{
				Logger.log("Couldn't instantiate storage for " + getName() + ". Null result");
				Logger.log("class: " + clsStorage);
			}

			return(rVal);
		}
		catch(InstantiationException e){Logger.log("Cannot Instantiate storage for parameter" + getName() + "\n" + e.getMessage()); return(rVal);}
		catch(IllegalAccessException e){Logger.log("Cannot Instantiate storage for parameter" + getName() + "\n" + e.getMessage()); return(rVal);}
		

	}
	public void setEditor(Class clsEditor)
	{
		this.clsEditor = clsEditor;
	}
	public UserInputDialog getEditor()
	{
		if(editor != null)
			return(editor);
		else
		{
			Object rVal = null;
			try
			{
				rVal = clsEditor.newInstance();
				if( !(rVal instanceof UserInputDialog))
					Logger.log("ERROR creating editor for parameter type " + name + ".\nWrong editor type.\nExpecting JDialog");
				
				if(rVal == null) Logger.log("Couldn't instantiate editor for " + name + ". Null result");
				editor = (UserInputDialog) rVal;
				
				return(editor);

			}
			catch(InstantiationException e){Logger.log("Cannot Instantiate editor for parameter" + name + "\n" + e.getMessage()); return(null);}
			catch(IllegalAccessException e){Logger.log("Cannot Instantiate editor for parameter" + name + "\n" + e.getMessage()); return(null);}
		}		
	}
	public String getName()
	{
		return(name);
	}
	public String toString()
	{
		return(getName());
	}
	private static ParameterType addParameterType(java.lang.String name)
	{
		ParameterType pt = new ParameterType(name);
		return(pt);
	}
/*	private void addSingleParameter(String displayName, String displayValue)
	{
		DefaultSingleParameter sp = new DefaultSingleParameter(displayName, displayValue);
		singleParameters.add(sp);
	} */
/*	public SingleParameterList getSingleParameters()
	{
		return(singleParameters);
	} */
}
