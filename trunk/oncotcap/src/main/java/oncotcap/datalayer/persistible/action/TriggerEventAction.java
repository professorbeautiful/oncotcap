package oncotcap.datalayer.persistible.action;

import java.util.*;

import oncotcap.display.common.EventChooser;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.engine.*;
import oncotcap.util.*;

public class TriggerEventAction extends DefaultOncAction
{
	public EventDeclaration event;
	public int eventType = EventChooser.EVENT;
	public MethodDeclaration method;
	public ProcessDeclaration process;
	public String name;

	private TriggerEventAction selfreference;
	
	public TriggerEventAction(oncotcap.util.GUID guid){
		super(guid);
		selfreference = this;
	}
	public TriggerEventAction(){selfreference = this;}
/*	public int getTriggerCount()
	{
		return(triggerCountLocal);
	}
	private void incrementTriggerCount()
	{
		++triggerCount;
		triggerCountLocal = triggerCount;
	}*/
	public EditorPanel getEditorPanel()
	{
		return new TriggerEventEditor();
	}
	public EditorPanel getEditorPanelWithInstance()
	{
		return new TriggerEventEditor(this);
	}
	public String getName()
	{
		return(name);
	}
	public void setName(String name)
	{
		this.name = name;
		update();
	}

	public EventDeclaration getEvent()
	{
		return (event);
	}

	public int getEventType()
	{
		return (eventType);
	}

	public MethodDeclaration getMethod()
	{
		return (method);
	}

	public ProcessDeclaration getTriggeredProcessDeclaration()
	{
		return (process);
	}

	public void setEvent(Object ev)
	{
		if (ev != null && ev instanceof EventDeclaration)
		{
			event = (EventDeclaration)ev;
		}
		update();
	}

	public void setEventType(int type)
	{
		eventType = type;
		update();
	}

	public void setMethod(Object meth)
	{
		if (meth != null && meth instanceof MethodDeclaration)
		{
			method = (MethodDeclaration)meth;
		}
		update();
	}

	public void setProcessDeclaration(ProcessDeclaration proc)
	{
		if (proc != null)
		{
			process = proc;
		}
		update();
	}
	public void setProcessDeclaration(Object proc)
	{
		if (proc != null && proc instanceof ProcessDeclaration)
		{
			setProcessDeclaration((ProcessDeclaration)proc);
		}
	}

	public boolean check()
	{
		return (true);
	}

	public String toString()
	{
		if(name == null || name.trim().equals(""))
		{
			String rVal = "Unnamed trigger: " + getEventDescription();
			return (rVal);
		}
		else
			return("Trigger Event: " + name + " " + getEventDescription());
	}

	protected String getEventDescription()
	{
		String rVal = "";
		if (eventType == EventChooser.EVENT)
		{
			rVal = "Event: ";
			if (getEvent() != null)
			{
				rVal = rVal + getEvent();
			}
		}
		else
		{
			rVal = "Process: ";
			if (getTriggeredProcessDeclaration() != null)
			{
				rVal = rVal + getProcessDeclaration();
			}
			if (getMethod() != null)
			{
				rVal = rVal + " Method: " + getMethod();
			}
		}
		return (rVal);
	}
/*	public void writeDeclarationSection(Writer w) throws IOException
	{
//		incrementTriggerCount();
		StatementBundle sb = this.getCodeBundleContainingMe().getStatementTemplateContainingMe().getStatementBundleImplementingMe();
//		w.write("\t\tScheduler " + StringHelper.javaName(sb.substitute(name)) + ";\n");

		if(eventType == EventChooser.EVENT)
		{
			//set up arguments for event calls.  A copy of the caller
			//OncProcess and a Hashtable of all SingleParameters available
			//in the codebundle that triggers the event will be sent.

			w.write("\t\tprotected static EventParameters paramTable" + triggerCountLocal + " = new EventParameters();\n");
			StatementTemplate st = null;
			CodeBundle cb = getCodeBundleContainingMe();
			if(cb != null)
				st = cb.getStatementTemplateContainingMe();

			if(st != null)
			{
				w.write("\t\tstatic\n\t\t{\n");
				Iterator singleParams  = st.getParameters().getSingleParameters().iterator();
				while(singleParams.hasNext())
					writeParamTableAdd((SingleParameter) singleParams.next(), w, sb);
				w.write("\n\t\t}\n");
			}
		}

	}
	private static int vtIdx = 0;
	private void writeParamTableAdd(SingleParameter singleParam, Writer w, StatementBundle sb) throws IOException
	{
		w.write("\t\t\tValueAndType vt" + (++vtIdx) + " = new ValueAndType(\"" + sb.getParameterValue(singleParam.getDisplayName()) + "\", " + singleParam.getClass().getName() + ".class);\n");
		w.write("\t\t\tparamTable" + triggerCountLocal + ".put(\"" + singleParam.getDisplayName() + "\", vt" + vtIdx + ");\n");
	}

	public void writeMethodSection(Writer w) throws IOException
	{
		if(eventType == EventChooser.EVENT)
		{
			w.write("\t\tEventManager.EventScheduler e = new EventManager.EventScheduler(MasterScheduler.globalTime, this, " + "\"" + StringHelper.javaName(event.getName()) + "\"" + ", paramTable" + triggerCountLocal + ");\n");
		}
		else
		{
			boolean isCollection = false;
			String methodName = getMethod().toString();
			if(methodName.equals("is updated"))
			{
				methodName = "update";
			}
			else if(methodName.equals("in initialized"))
			{
				methodName = "init";
			}
			else if(methodName.equals("collection is updated"))
			{
				methodName = "update";
				isCollection = true;
			}
			else if(methodName.equals("collection is initialized"))
			{
				methodName = "init";
				isCollection = true;
			}
			String collectionMethod = "";
			if(isCollection)
				collectionMethod = ".getCollection()";
			methodName = StringHelper.javaName(methodName.trim());
			w.write("\t\tIterator procs = ((Vector) processes.get(" + StringHelper.javaName(getProcessDeclaration().getName()) + ".class)).iterator();\n");
			w.write("\t\twhile(procs.hasNext())\n");
			w.write("\t\t\tMasterScheduler.installTriggerAbsolute(MasterScheduler.globalTime, ((OncProcess) procs.next())" + collectionMethod + ", \"" + methodName + "\");\n");
		}
	}*/

	public Collection<ProcessDeclaration> getReferencedProcesses()
	{
		if(getEventType() == EventChooser.EVENT)
			return new Vector<ProcessDeclaration>();
		else if(getTriggeredProcessDeclaration() == null)
			return new Vector<ProcessDeclaration>();
		else
		{
			Vector<ProcessDeclaration> rVec = new Vector<ProcessDeclaration>();
			rVec.add(getTriggeredProcessDeclaration());
			return(rVec);
		}
	}

	public Collection<Instruction> getInstructions()
	{
		Vector<Instruction> instructions = new Vector<Instruction>();
		instructions.add(new TriggerMethodInstruction());
		instructions.add(new TriggerDeclarationInstruction());
		return(instructions);
	}
	public class TriggerMethodInstruction implements Instruction
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
	public class TriggerDeclarationInstruction implements Instruction
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
			return(ClassSectionDeclaration.DECLARATION_SECTION);
		}
		public InstructionProvider getEnclosingInstructionProvider()
		{
			return(selfreference);
		}
		
		//TriggerEventAction declarations are equal if their base StatementBundle is
		//the same because the only information generated from this instruction
		//is a list of parameters from the StatementBundle
		public boolean equals(InstructionAndValues iav, ValueMapPath myPath)
		{
			if(myPath.equals(iav.getValues()))
				return(true);
			else
				return(false);
		}
	}
}
