package oncotcap.datalayer.persistible.action;

import java.util.Iterator;
import java.util.Vector;
import java.util.Collection;

import oncotcap.Oncotcap;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.util.*;
import oncotcap.datalayer.*;
import oncotcap.display.browser.*;

public class ScheduleEventAction extends TriggerEventAction
{	
	public OncTime endDelayTime;	
	public String numberOfTimesUntilEnd;
	public ScheduleEndType scheduleEndType;
	public ScheduleStartType scheduleStartType;
	public OncTime startDelayTime;
	public boolean recur = false;
	public String dayList = null;
	public boolean isDayList = false;
	public OncTime gapTime;
	
	public static Vector allSchedules = new Vector();
	public static Vector allScheduleEndTypes = new Vector();
	public static Vector allScheduleStartTypes = new Vector();

	public final static ScheduleStartType FUTURE = new ScheduleStartType("Future");
	public final static ScheduleStartType NOW = new ScheduleStartType("Now");
//	public final static ScheduleStartType RECURRING = new ScheduleStartType("Recurring");
	public final static ScheduleEndType X_TIMES = new ScheduleEndType("X Times");
	public final static ScheduleEndType AFTER_PERIOD = new ScheduleEndType("After Time Period");
	public final static ScheduleEndType FOREVER = new ScheduleEndType("Forever");
	
	static{ fillAllSchedules(); }
	


	

	public ScheduleEventAction(oncotcap.util.GUID guid){
		super(guid);
		allSchedules.add(this);
	}

	public ScheduleEventAction() {
		super();
		allSchedules.add(this);
	}

	public String getDisplayString()
	{
		return toString();
	}

	public EditorPanel getEditorPanel()
	{
		return new ScheduleEventEditor();
	}

	public EditorPanel getEditorPanelWithInstance()
	{
		return new ScheduleEventEditor(this);
	}
	public boolean getRecur()
	{
		return(recur);
	}
	public void setRecur(boolean recur)
	{
		this.recur = recur;
		update();
	}
	public boolean isDayList()
	{
		return(isDayList);
	}
	public void setIsDayList(boolean val)
	{
		isDayList = val;
		update();
	}
	public String getDayList()
	{
		return(dayList);
	}
	public void setDayList(String list)
	{
		dayList = list;
		update();
	}
	public OncTime getEndDelayTime()
	{
		return (endDelayTime);
	}
	public OncTime getGapTime()
	{
		return(gapTime);
	}
	public void setGapTime(OncTime gap)
	{
		gapTime = gap;
		update();
	}
	public String getNumberOfTimesUntilEnd()
	{
		return(numberOfTimesUntilEnd);
	}

	public ScheduleEndType getScheduleEndType()
	{
		return (scheduleEndType);
	}

	public String getScheduleEndTypeName()
	{
		if (scheduleEndType != null)
		{
			return (scheduleEndType.toString());
		}
		else
		{
			return (null);
		}
	}

	public ScheduleStartType getScheduleStartType()
	{
		return (scheduleStartType);
	}

	public String getScheduleStartTypeName()
	{
		if (scheduleStartType != null)
		{
			return (scheduleStartType.toString());
		}
		else
		{
			return (null);
		}
	}

	public OncTime getStartDelayTime()
	{
		return (startDelayTime);
	}

	public ActionType getType()
	{
		return (ActionType.SCHEDULE_EVENT);
	}

	public TreeViewableList getViewableList(TreeViewableList viewableList,	OncViewerTreeNode parent)
	{
		OncViewerTreeNode oncViewerObject =	new OncViewerTreeNode(getDisplayString(),	parent);
		oncViewerObject.setCodeBundle(parent.getCodeBundle());
		oncViewerObject.setUserObject(this);
		viewableList.getViewableList().addElement(oncViewerObject);
		viewableList.setViewableObject(oncViewerObject);
		return viewableList;
	}

	public void setEndDelayTime(Object setTime)
	{
		if (setTime != null && setTime instanceof OncTime)
		{
			endDelayTime = (OncTime)setTime;
		}
		update();
	}

	public void setNumberOfTimesUntilEnd(String nTimes)
	{
		numberOfTimesUntilEnd = nTimes;
		update();
	}

	public void setScheduleEndType(String type)
	{
		scheduleEndType = ScheduleEndType.getEndType(type);
		update();
	}

	public void setScheduleEndType(ScheduleEndType type)
	{
			scheduleEndType = type;
			update();
	}

	public void setScheduleStartType(String type)
	{
		setScheduleStartType((Object)type);
	}

	public void setScheduleStartType(Object type)
	{
		if (type != null)
		{
			if (type instanceof ScheduleStartType)
			{
				scheduleStartType = (ScheduleStartType)type;
			}
			else if (type instanceof String)
			{
				scheduleStartType = ScheduleStartType.getStartType((String)type);
			}
		}
		update();
	}

	public void setStartDelayTime(Object setTime)
	{
		if (setTime != null && setTime instanceof OncTime)
		{
			startDelayTime = (OncTime)setTime;
		}
		update();
	}

	public String toString()
	{
		if(name == null || name.trim().equals(""))
		{
			String rVal = "Unnamed schedule: " + getEventDescription();
			return (rVal);
		}
		else
			return("Schedule: " + name + " " + getEventDescription());
	}

	public static Vector getAllScheduleEndTypes()
	{
		return (allScheduleEndTypes);
	}

	public static Vector getAllScheduleStartTypes()
	{
		return (allScheduleStartTypes);
	}

	public static void main(String[] args)
	{
		ScheduleEventAction se = new ScheduleEventAction();
	}
	public static Vector getAllSchedules()
	{
		return(allSchedules);
	}
	private static void fillAllSchedules()
	{
		Class clsSchedEvent = ReflectionHelper.classForName("oncotcap.datalayer.persistible.action.ScheduleEventAction");
		OncoTCapDataSource dataSource = Oncotcap.getDataSource();
		Collection scheds = dataSource.find(clsSchedEvent);

	}
	public static class ScheduleEndType
	{
		
		String name;

		private ScheduleEndType() { }
		private ScheduleEndType(String name)
		{
			this.name = name;
			allScheduleEndTypes.add(this);
		}

		public String toString()
		{
			return (name);
		}

		public static ScheduleEndType getEndType(String type)
		{
			Iterator it = allScheduleEndTypes.iterator();
			ScheduleEndType tType;
			while (it.hasNext())
			{
				if ((tType = (ScheduleEndType)it.next()).toString().equalsIgnoreCase(type.trim()))
				{
					return (tType);
				}
			}
			return (null);
		}
	}

	public static class ScheduleStartType
	{
		
		String name;

		private ScheduleStartType() { }

		private ScheduleStartType(String name)
		{
			this.name = name;
			allScheduleStartTypes.add(this);
		}

		public String getName()
		{
			return(name);
		}
		public String toString()
		{
			return (name);
		}

		public static ScheduleStartType getStartType(String type)
		{
			Iterator it = allScheduleStartTypes.iterator();
			ScheduleStartType tType;
			while (it.hasNext())
			{
				if ((tType = (ScheduleStartType)it.next()).toString().equalsIgnoreCase(type.trim()))
				{
					return (tType);
				}
			}
			return (null);
		}
	}

/*	public void writeDeclarationSection(Writer w) throws IOException
	{
	    incrementTriggerCount();
		StatementBundle sb = this.getCodeBundleContainingMe().getStatementTemplateContainingMe().getStatementBundleImplementingMe();
		w.write("\t\tScheduler " + StringHelper.javaName(sb.substitute(name)) + ";\n");

		if(eventType == EventChooser.EVENT)
		{
			//set up arguments for event calls.  A copy of the caller
			//OncProcess and a Hashtable of all SingleParameters available
			//in the codebundle that triggers the event will be sent.

			w.write("\t\tEventParameters paramTable = new EventParameters();\n");
			StatementTemplate st = null;
			CodeBundle cb = getCodeBundleContainingMe();
			if(cb != null)
				st = cb.getStatementTemplateContainingMe();

			if(st != null)
			{
				Iterator singleParams  = st.getParameters().getSingleParameters().iterator();
				while(singleParams.hasNext())
					writeParamTableAdd((SingleParameter) singleParams.next(), w);
			}

			w.write("\n\t\tObject [] eventArgs = {this, paramTable};\n\n");
		}

	}
	private static int vtIdx = 0;
	private void writeParamTableAdd(SingleParameter singleParam, Writer w) throws IOException
	{
		w.write("\t\tValueAndType vt" + (vtIdx++) + " = new ValueAndType(\"" + singleParam.getDisplayValue() + "\", " + singleParam.getClass().getName() + ");\n");
		w.write("\t\tparamTable.put(\"" + singleParam.getDisplayName() + "\", vt" + vtIdx + ");\n");
	} */
/*	public void writeMethodSection(Writer w) throws IOException
	{
		StatementBundle sb = this.getCodeBundleContainingMe().getStatementTemplateContainingMe().getStatementBundleImplementingMe();
		if(scheduleStartType == NOW)
			w.write("\t\tdouble startTime" + triggerCountLocal + " = MasterScheduler.globalTime;\n");
		else
			w.write("\t\tdouble startTime" + triggerCountLocal + " = OncTime.convert(" + startDelayTime.getTimeString() + ", " + startDelayTime.getTimeUnit().getVarName() +", MasterScheduler.getTimeUnit());\n");


		if(! recur)
		{
			writeTrigger("startTime" + triggerCountLocal, w, sb);
		}
		else
		{
			if(isDayList())
			{
				w.write("\t\tDayList list" + triggerCountLocal + " = null;\n");
				w.write("\t\ttry{list" + triggerCountLocal + " = new DayList(\"" + dayList + "\");}\n");
				w.write("\t\tcatch(Throwable e){System.out.println(\"WARNING: Invalid DayList: " + dayList + " in schedule: \" + \"" + getName() + "\");}\n");
				w.write("\t\tif(list" + triggerCountLocal + " != null)\n\t\t{\n");
					w.write("\t\t\tIterator it = list" + triggerCountLocal + ".iterator();\n");
					w.write("\t\t\twhile(it.hasNext())\n\t\t\t{\n");
						w.write("\t\t\t\tdouble dayN = (double) ((Integer) it.next()).intValue();\n");
						w.write("\t\t\t\tdouble sTime = OncTime.convert(dayN, OncTime.DAY, MasterScheduler.getTimeUnit());\n");
						writeTrigger("startTime" + triggerCountLocal + " + sTime", w, sb);
						w.write("\t\t\t}\n\t\t}\n");
			}
			else
			{
				w.write("\t\tdouble interval" + triggerCountLocal + " = OncTime.convert(" + gapTime.getTimeString() +", " + gapTime.getTimeUnit().getVarName() + ", MasterScheduler.getTimeUnit());\n");
				if(scheduleEndType == X_TIMES)
				{
					w.write("\t\tdouble intervalSum" + triggerCountLocal + " = 0.0;\n");
					w.write("\t\t\tfor(int n = 1; n <= " + numberOfTimesUntilEnd + " ; n++)\n\t\t\t{\n");
					writeTrigger("startTime" + triggerCountLocal + " + intervalSum" + triggerCountLocal, w, sb);
					w.write("\t\t\t\tintervalSum" + triggerCountLocal + " += interval" + triggerCountLocal + ";\n");
					w.write("\t\t\t}\n");
				}
				else if(scheduleEndType == AFTER_PERIOD)
				{
					w.write("\t\tdouble scheduleEnd = scheduleStart " + " OncTime.convert(" + endDelayTime.getTimeString() + ", " + endDelayTime.getTimeUnit().getVarName() + ", MasterScheduler.getTimeUnit());\n");
					w.write("\t\tfor(double t = scheduleStart; t <= scheduleEnd; t+=interval" + triggerCountLocal + ")\n\t\t{\n");
					writeTrigger("t", w, sb);
					w.write("\t\t}\n");
				}
				else if(scheduleEndType == FOREVER)
				{
					String procName;
					if(eventType == EventChooser.EVENT)
					{
						//TODO: not yet implimented
						w.write("\t\tscheduling events forever not yet implimented....\n");
						//something like this?
						//w.write("\t\tEventManager.EventScheduler e = new EventManager.EventScheduler(startTime" + triggerCountLocal + ", \"" + procName + "\", \"" + sb.substituteJavaName(eventname) + "\", paramTable" + triggerCountLocal + ", interval" + triggerCountLocal + ");\n");
					}
					else
					{
						if(method.getName().equals("collection update"))
							procName = "getCollection()";
						else
							procName =  sb.substituteJavaName(process.getName());
						
						String methName = "\"" + sb.substituteJavaName(getCodeBundleContainingMe().getProcessDeclaration().getName()) + "." + sb.substituteJavaName(method.getName())  + "\"";
//						>       EventManager.registerEvent(this, "Cancer_cellCollection.collection_update");
//						>       EventManager.EventScheduler e = new EventManager.EventScheduler(startTime3, getCollection(), "Cancer_cellCollection.collection_update", null, interval3);
						
						w.write("\t\tEventManager.registerEvent(this, " + methName + ");\n");
						w.write("\t\tEventManager.EventScheduler e = new EventManager.EventScheduler(startTime"+ triggerCountLocal + ", " + procName + ", " + methName + ", null, interval" + triggerCountLocal + ");\n");
//						w.write("\t\t" + sb.substituteJavaName(name) + " = new Scheduler(" + procName + ", \"" + sb.substituteJavaName(method.getName()) + "\");\n");
//						w.write("\t\t" + sb.substituteJavaName(name) + ".addRecurrentEvent(startTime" + triggerCountLocal + ", interval" + triggerCountLocal + ");\n");
					}					
				}
			}
		}
	}
*/
/*	private void writeTrigger(String time, Writer w, StatementBundle sb) throws IOException
	{
		if(eventType == EventChooser.EVENT)
		{
			w.write("\t\tEventManager.EventScheduler e = new EventManager.EventScheduler(" + time + ", this, " + "\"" + StringHelper.javaName(sb.substitute(event.getName())) + "\"" + ", paramTable" + triggerCountLocal + ");\n");
		}
		else
		{
//			w.write("\t\tObject [] args = {\"" + StringHelper.javaName(vm.substitute(method.getName())) + "\"};\n");
			w.write("\t\tMasterScheduler.installTriggerAbsolute(" + time + ", this.collection, \"updateAll\", eventArgs)\n");
		}
	}*/
}
