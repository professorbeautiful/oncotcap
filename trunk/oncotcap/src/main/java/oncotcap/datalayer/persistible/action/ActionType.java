package oncotcap.datalayer.persistible.action;

import java.util.*;

import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;

public class ActionType
{
	private static Vector allTypes = new Vector();

	public static final Vector allActions = new Vector();
	public static Vector eventOnly = null;
	public static final ActionType ADD_VARIABLE = new ActionType("Add Variable", "oncotcap.display.editor.persistibleeditorpanel.AddVariableEditor", "oncotcap.datalayer.persistible.action.AddVariableAction");
	public static final ActionType INSTANTIATE = new ActionType("Instantiate A Process", "oncotcap.display.editor.persistibleeditorpanel.InstantiateActionEditor", "oncotcap.datalayer.persistible.action.InstantiateAction" );
	public static final ActionType TRIGGER_EVENT = new ActionType("Trigger Event", "oncotcap.display.editor.persistibleeditorpanel.TriggerEventEditor", "oncotcap.datalayer.persistible.action.TriggerEventAction");
	public static final ActionType MODIFY_VARIABLE = new ActionType("Modify Variable", "oncotcap.display.editor.persistibleeditorpanel.AbstractVariableEditor", "oncotcap.datalayer.persistible.action.ModifyVariableAction");
	public static final ActionType SCHEDULE_EVENT = new ActionType("Schedule Event", "oncotcap.display.editor.persistibleeditorpanel.ScheduleEventEditor", "oncotcap.datalayer.persistible.action.ScheduleEventAction");
	public static final ActionType MODIFY_SCHEDULE = new ActionType("Modify a Schedule", "oncotcap.display.editor.persistibleeditorpanel.ModifyScheduleEditor", "oncotcap.datalayer.persistible.action.ModifyScheduleAction");
	public static final ActionType INIT_VARIABLE = new ActionType("Initialize a Variable", "oncotcap.display.editor.persistibleeditorpanel.InitVariableEditor", "oncotcap.datalayer.persistible.action.InitVariableAction");
	public static final ActionType ADD_CODE = new ActionType("Add generic code", "oncotcap.display.editor.persistibleeditorpanel.GenericCodeActionEditor", "oncotcap.datalayer.persistible.action.AddGenericCode");

	private String name;
	private TypeHelper typeHelper;

	public static void main(String [] args)
	{
		System.out.println("IN MAIN");
		java.util.Iterator it = getAllTypes().iterator();
		while(it.hasNext())
			System.out.println("***" + it.next());
	}
	public ActionType(String name, String editor, String storage)
	{
		this.name = name;
		typeHelper = new TypeHelper(editor, storage);
		allTypes.add(this);
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
	public OncAction getStorageInstance()
	{
		return((OncAction) typeHelper.getStorageInstance());
	}
	public ActionEditor getEditorWithStorage()
	{
		return((ActionEditor) typeHelper.getEditorInstanceWithStorage());
	}
	public ActionEditor getEditor()
	{
		return((ActionEditor) typeHelper.getEditorInstance());
	}
	public EditorPanel getEditor(OncAction var)
	{
		return(typeHelper.getEditorInstance(var));
	}
	public static ActionType typeChooser(int eventType)
	{
		Object type;
		if(eventType == EventChooser.PROCESS)
			type = ListDialog.getValue("Choose an Action type.", allTypes);
		else
		{
			//type = ListDialog.getValue("Choose an Action type.", eventOnlyTypes());
			type = ListDialog.getValue("Choose an Action type.", allTypes);
		}
		if(type != null && type instanceof ActionType)
		{
			return((ActionType) type);
		}
		else
			return(null);
	}

	//not used right now just return allTypes
	public static Vector eventOnlyTypes()
	{
/*		if(eventOnly == null)
		{
			eventOnly = new Vector();
			Iterator it = allTypes.iterator();
			while(it.hasNext())
			{
				ActionType testType = (ActionType) it.next();
				if(testType != MODIFY_VARIABLE && testType != ADD_VARIABLE && testType != INSTANTIATE)
					eventOnly.add(testType);
			}
		}
		return(eventOnly);
		*/
		return(allTypes);
	}
	public static ActionType actionToType(OncAction action)
	{
		Iterator it = allTypes.iterator();
		while(it.hasNext())
		{
			ActionType testAction = (ActionType) it.next();
			if(testAction.typeHelper.getStorageClass().isInstance(action))
			{
				return(testAction);
			}
		}
		return(null);
	}
}
