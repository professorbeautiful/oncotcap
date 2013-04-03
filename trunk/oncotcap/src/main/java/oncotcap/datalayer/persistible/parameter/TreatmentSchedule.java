package oncotcap.datalayer.persistible.parameter;

import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.persistible.*;

public class TreatmentSchedule extends AbstractParameter
{
	private DefaultSingleParameter durationSingleParam;
	private DefaultSingleParameter coursesSingleParam;
	private DefaultSingleParameter dayListSingleParam;
	public String duration;
	public String nCourses;
	public String dayList;
	
	public TreatmentSchedule(oncotcap.util.GUID guid){
		super(guid);
	}
	public TreatmentSchedule()
	{
		this(true);
	}
	public TreatmentSchedule(boolean saveToDataSource)
	{
		super(saveToDataSource);
	}
	private TreatmentSchedule(TreatmentSchedule ts, boolean saveToDataSource)
	{
		super(saveToDataSource);
		this.name = new String((ts.getName() == null) ? "" : ts.getName());
/*		Iterator it = ts.getSingleParameters();
		while(it.hasNext())
		{
			singleParameterList.add(new DefaultSingleParameter((DefaultSingleParameter) it.next(), this));
		}
		if(ts.duration != null)
			setDuration(new String(ts.duration));
		else
			setDuration("");
		if(ts.nCourses != null)
			setNumberOfCourses(ts.nCourses);
		else
			setNumberOfCourses("");
		if(ts.dayList != null)
			setDayList(new String(ts.dayList));
		else
			setDayList(""); */

		if(ts.duration != null)
			this.duration = new String(ts.duration);
		if(ts.nCourses != null)
			this.nCourses = new String(ts.nCourses);
		if(ts.dayList != null)
			this.dayList = new String(ts.dayList);

		DefaultSingleParameter dursp = (DefaultSingleParameter) ts.singleParameterList.getSingleParameter("TreatmentSchedule.Duration");
		DefaultSingleParameter coursesp = (DefaultSingleParameter) ts.singleParameterList.getSingleParameter("TreatmentSchedule.Courses");
		DefaultSingleParameter daylistsp = (DefaultSingleParameter) ts.singleParameterList.getSingleParameter("TreatmentSchedule.DayList");
		DefaultSingleParameter durspclone = (DefaultSingleParameter) dursp.clone();
		DefaultSingleParameter courseclone = (DefaultSingleParameter) coursesp.clone();
		DefaultSingleParameter daylistclone = (DefaultSingleParameter) daylistsp.clone();
		
		addSingleParameter(durspclone);
		addSingleParameter(courseclone);
		addSingleParameter(daylistclone);
	}
	public void setDuration(String dur)
	{
		if(durationSingleParam == null)
			initSingleParams();
		
		if(! (dur == null || dur.trim().equals("")))
		{
			duration = new String(dur);
			durationSingleParam.setDisplayValue(duration);
			update();
		}
	}
	public void setDuration(int dur)
	{
		setDuration(Integer.toString(dur));
	}
	public String getDuration()
	{
		if(duration == null)
			return("");
		else
			return duration;
	}
	public void setNumberOfCourses(String courses)
	{
		if(coursesSingleParam == null)
			initSingleParams();
		
		if(! (courses == null || courses.trim().equals("")))
		{
			this.nCourses = new String(courses);
			coursesSingleParam.setDisplayValue(this.nCourses);
			update();
		}
	}
	public void setNumberOfCourses(int nCourses)
	{
		setNumberOfCourses(Integer.toString(nCourses));
	}
	public String getValue()
	{
		return("");
	}
	public String getNumberOfCourses()
	{
		if(nCourses == null)
			return("");
		else
			return(nCourses);
	}
	public void setDayList(String dayList)
	{
		if(dayListSingleParam == null)
			initSingleParams();
		
		if( ! (dayList == null || dayList.trim().equals("")))
		{
			this.dayList = new String(dayList);
			dayListSingleParam.setDisplayValue(this.dayList);
			update();
		}
	}
	public String getDayList()
	{
		return(dayList);
	}
	public EditorPanel getEditorPanelWithInstance()
	{
		EditorPanel tp = new ParameterEditor();
		tp.edit(this);
		return(tp);
	}
	public EditorPanel getEditorPanel()
	{
		return(new ParameterEditor());
	}
	public EditorPanel getParameterEditorPanelWithInstance()
	{
		TreatmentScheduleEditorPanel tp = new TreatmentScheduleEditorPanel();
		tp.edit(this);
		return(tp);
	}
	public EditorPanel getParameterEditorPanel()
	{
		return(new TreatmentScheduleEditorPanel());
	}

	public Object clone()
	{
		return(clone(true));
	}
	public Parameter clone(boolean saveToDataSource)
	{
		TreatmentSchedule rVal = new TreatmentSchedule(this, saveToDataSource); 
		rVal.setOriginalSibling(getOriginalSibling());
		return(rVal);
	}
	public boolean check()
	{
		return(true);
	}
	public String getStringValue()
	{
		return(toString());
	}
	public VariableType getType()
	{
		return(null);	
	}
	public ParameterType getParameterType()
	{
		return(ParameterType.SCHEDULE);
	}
	public void initSingleParams()
	{
		durationSingleParam = new DefaultSingleParameter("Duration", this, "TreatmentSchedule.Duration");
		coursesSingleParam = new DefaultSingleParameter("Courses", this, "TreatmentSchedule.Courses");
		dayListSingleParam = new DefaultSingleParameter("Day List", this, "TreatmentSchedule.DayList");
/*		duration = "28";
		durationSingleParam.setDisplayValue(duration);
		nCourses = "1";
		coursesSingleParam.setDisplayValue(nCourses);
		dayList = "1";
		dayListSingleParam.setDisplayValue(dayList); */
		singleParameterList.add(dayListSingleParam);
		singleParameterList.add(durationSingleParam);
		singleParameterList.add(coursesSingleParam);
	}
	public void addSingleParameter(SingleParameter sp)
	{
		if(sp.getSingleParameterID().equals("TreatmentSchedule.Duration"))
		{
			singleParameterList.replace(durationSingleParam, sp);
			durationSingleParam = (DefaultSingleParameter) sp;
		}
		else if(sp.getSingleParameterID().equals("TreatmentSchedule.Courses"))
		{
			singleParameterList.replace(coursesSingleParam, sp);
			coursesSingleParam = (DefaultSingleParameter) sp;
		}
		else if(sp.getSingleParameterID().equals("TreatmentSchedule.DayList"))
		{
			singleParameterList.replace(dayListSingleParam, sp);
			dayListSingleParam = (DefaultSingleParameter) sp;
		}
	}
/*	public void writeDeclarationSection(Writer write, StatementBundle sb) throws IOException
	{

	}

	public void writeMethodSection(Writer write, StatementBundle sb) throws IOException
	{

	}*/

	public String getDurationParamName()
	{
		return(durationSingleParam.getDisplayName());
	}
	public void setDurationParamName(String name)
	{
		durationSingleParam.setDisplayName(name);
	}
	public String getCoursesParamName()
	{
		return(coursesSingleParam.getDisplayName());
	}
	public void setCoursesParamName(String name)
	{
		coursesSingleParam.setDisplayName(name);
	}
	public String getDayListParamName()
	{
		return(dayListSingleParam.getDisplayName());
	}
	public void setDayListParamName(String name)
	{
		dayListSingleParam.setDisplayName(name);
	}
}
