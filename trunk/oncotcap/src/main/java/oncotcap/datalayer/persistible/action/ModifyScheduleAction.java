package oncotcap.datalayer.persistible.action;

import java.util.Collection;
import java.util.Vector;
import java.util.Iterator;

import oncotcap.display.browser.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.editor.persistibleeditorpanel.ModifyScheduleEditor;
import oncotcap.engine.ClassSectionDeclaration;
import oncotcap.engine.Instruction;
import oncotcap.engine.InstructionProvider;
import oncotcap.engine.ValueMapPath;
import oncotcap.engine.VariableDependency;
import oncotcap.util.*;

public class ModifyScheduleAction extends DefaultOncAction implements Editable
{
	private static Vector allTypes = new Vector();
	public static final ModifyScheduleType DELAY_START = new ModifyScheduleType("Delay Start Time", true, "Delay start time by: ", false);
	public static final ModifyScheduleType ADVANCE_START = new ModifyScheduleType("Advance Start Time", true, "Advance start time by: ", false);
	public static final ModifyScheduleType PLACE_ON_HOLD = new ModifyScheduleType("Place on hold", false, "This schedule will be placed on hold.", false);
	public static final ModifyScheduleType RESUME = new ModifyScheduleType("Resume", false, "This schedule will resume.", false);
	public static final ModifyScheduleType DELAY_END = new ModifyScheduleType("Delay End Time", true, "Delay end time by: ", false);
	public static final ModifyScheduleType ADVANCE_END = new ModifyScheduleType("Advance End Time", true, "Advance end time by: ", false);
	public static final ModifyScheduleType RECURRENCE_CHANGE = new ModifyScheduleType("Change Recurrence Gap", true, "Change the gap to: ", false);
	public static final ModifyScheduleType EXTEND_NO_OF_TIMES = new ModifyScheduleType("Extend Number of Times", false, "Extend the number of times by: ", true);
	public static final ModifyScheduleType REDUCE_NO_OF_TIMES = new ModifyScheduleType("Reduce Number of Times", false, "Reduce the number of times by: ", true);

	private static int counter = 0;
	
	public ScheduleEventAction scheduleToModify;
	public OncTime timeChange;
	public ModifyScheduleType modType;
	public int nTimesChange;

	private ModifyScheduleAction selfreference;
	
	public ModifyScheduleAction(oncotcap.util.GUID guid){
		super(guid);
		selfreference = this;
	}

	public ModifyScheduleAction() {

		selfreference = this;
	}
	public ModifyScheduleAction(ScheduleEventAction scheduleToModify)
	{
		this.scheduleToModify = scheduleToModify;
		selfreference = this;
	}
	public ScheduleEventAction getScheduleToModify()
	{
		return(scheduleToModify);
	}
	public EditorPanel getEditorPanel()
	{
		return(new ModifyScheduleEditor());
	}
	public EditorPanel getEditorPanelWithInstance()
	{
		return(new ModifyScheduleEditor(this));
	}

	//TODO:
	public TreeViewableList getViewableList(TreeViewableList viewableList, OncViewerTreeNode parent)
	{
		return(viewableList);
	}
	public String getDisplayString(){return(toString());}

	//TODO:
	public boolean check(){return(true);}
	public static Vector getAllTypes()
	{
		return(allTypes);
	}
	public String getModification() {
			return null;
	}
	public ModifyScheduleType getModificationType()
	{
		return(modType);
	}
	public String getModificationTypeName()
	{
		if(modType != null)
			return(modType.toString());
		else
			return(null);
	}
	public void setModificationType(String typeName)
	{
		modType = ModifyScheduleType.getType(typeName);
		update();
	}
	public void setModificationType(ModifyScheduleType type)
	{
		modType = type;
		update();
	}
	public OncTime getTime()
	{
		return(timeChange);
	}
	public void setTime(OncTime time)
	{
		timeChange = time;
		update();
	}
	public int getNTimesChange()
	{
		return(nTimesChange);
	}
	public void setNTimesChange(int times)
	{
		nTimesChange = times;
		update();
	}

	public String toString()
	{
		return("Modify Schedule: " + scheduleToModify.getName());
	}
	public String getName() {
			return scheduleToModify.getName();
	}

	public static class ModifyScheduleType
	{
		private String name;
		private boolean timeRequired;
		private boolean nTimesRequired;
		private String description;
		public ModifyScheduleType(String name, boolean timeRequired, String description, boolean nTimesRequired)
		{
			this.name = name;
			this.description = description;
			this.timeRequired = timeRequired;
			this.nTimesRequired = nTimesRequired;
			allTypes.add(this);
		}
		public String getDescription()
		{
			return(description);
		}
		public boolean isTimeRequired()
		{
			return(timeRequired);
		}
		public boolean isNTimesRequired()
		{
			return(nTimesRequired);
		}
		public static ModifyScheduleType getType(String typeName)
		{
			ModifyScheduleType tempType;
			if(typeName == null || typeName.trim().equals(""))
				return(null);
			else
			{
				Iterator it = allTypes.iterator();
				while(it.hasNext())
				{
					tempType = (ModifyScheduleType) it.next();
					if(tempType.toString().equalsIgnoreCase(typeName))
						return(tempType);
				}
			}
			return(null);
		}
		public String toString()
		{
			return(name);
		}
		public final boolean equals(Object o) { return(super.equals(o)); }
		public final int hashCode() { return(super.hashCode()); }
	}

/*	public void writeDeclarationSection(Writer write) throws IOException
	{

	}

	public void writeMethodSection(Writer write) throws IOException
	{
		writeMethodSection(write, null);
	}
	public void writeMethodSection(Writer write, DeclareVariable origVar) throws IOException
	{
		StatementBundle sb = this.getCodeBundleContainingMe().getStatementTemplateContainingMe().getStatementBundleImplementingMe();
		if(modType == PLACE_ON_HOLD)
		{
			write.write("\t\tEventManager.endRecurrentEvents(\"" + sb.substituteJavaName(scheduleToModify.getName()) + "\");\n");
/*			String containingClassName = StringHelper.javaName(sb.substitute(scheduleToModify.getCodeBundleContainingMe().getContainingProcessClassName()));
			//((cancer_cell) getContainerInstance(cancer_cell.class))
			String procName = "process" + counter++;
			write.write("\t\t" + containingClassName + " " + procName + " = " +
							"(" + containingClassName + ") getContainerInstance(" + containingClassName + ".class);\n");
			write.write("\t\tif(" + procName + " != null)\n");
			write.write("\t\t\t" + procName + "." + StringHelper.javaName(sb.substitute(scheduleToModify.getName())) + ".endRecurrentEvent();\n");
			write.write("\t\telse\n");
			write.write("\t\t\tLogger.log(\"WARNING: Unable to modify schedule " + scheduleToModify.getName() + ". getContainerInstance() returns null trying to find " + containingClassName + "\");\n"); */
			
/*		}
	}*/
	
	
	
	private Vector<Instruction> instructions = null;
	public Collection<Instruction> getInstructions()
	{
		if(instructions == null)
		{
			instructions = new Vector<Instruction>();
			instructions.add(new MethodInstruction());
		}
		return(instructions);
	}
	public class MethodInstruction implements Instruction
	{
		public Collection<String> getSetVariables(ValueMapPath path)
		{
			return new Vector<String>();
		}
		public Collection<String> getAllVariables(ValueMapPath path)
		{
			return new Vector<String>();
		}
		public Collection<VariableDependency> getVariableDependencies(ValueMapPath path)
		{
			return new Vector<VariableDependency>();
		}

		public ClassSectionDeclaration getSectionDeclaration()
		{
			return(getCodeBundleContainingMe().getSectionDeclaration());
		}
		public InstructionProvider getEnclosingInstructionProvider()
		{
			return(selfreference);
		}
	}
}
