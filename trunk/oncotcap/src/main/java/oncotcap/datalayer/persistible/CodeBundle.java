package oncotcap.datalayer.persistible;

import java.util.*;

import javax.swing.ImageIcon;

import oncotcap.Oncotcap;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.util.*; 
import oncotcap.display.common.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.display.editor.EditorFrame;
import oncotcap.engine.*;
import oncotcap.engine.java.*;
import oncotcap.display.browser.TreeBrowserNode;

public class CodeBundle extends AbstractDroppableWithKeywords //PersistibleWithKeywords 
		implements Editable, TreeBrowserNode, InstructionProvider
{
	public static int PROCESS = EventChooser.PROCESS;
	public static int EVENT = EventChooser.EVENT;
	public static int METHOD = EventChooser.PROCESS;

	private static String ARROW = (new Character((char) 0x279c)).toString();
	
	public EventDeclaration oncEvent;
	public ProcessDeclaration oncProcess;
	public MethodDeclaration method;
	public int eventType = EventChooser.PROCESS;
	
	private ActionList actions = new ActionList();
	public String ifClause = null;
	public String ifClauseSubstituted = null;
	private static Vector allCodeBundles = new Vector();
	private ImageIcon icon =	
			oncotcap.util.OncoTcapIcons.getImageIcon("codebundle.jpg");

	private StatementTemplate statementTemplateContainingMe = null;
	private StatementBundle statementBundleUsingMe = null;
	
	private CodeBundle me;
	
	static{ fillCodeBundleTables(); }

	public CodeBundle(oncotcap.util.GUID guid){
		super(guid);
		me = this;
		addCodeBundle(this);
	}

	public CodeBundle() {
		this(true);
	}
	public CodeBundle(boolean saveToDataSource)
	{
		if(!saveToDataSource)
			setPersistibleState(Persistible.DO_NOT_SAVE);
		me = this;
		addCodeBundle(this);
	}
	private static void fillCodeBundleTables()
	{
		OncoTCapDataSource dataSource = Oncotcap.getDataSource();
		Class cbClass = ReflectionHelper.classForName("oncotcap.datalayer.persistible.CodeBundle");
		Collection processes = dataSource.find(cbClass);
	}
	private static void addCodeBundle(CodeBundle cb)
	{
		allCodeBundles.add(cb);
	}
	public static Vector getAllCodeBundles()
	{
		return(allCodeBundles);
	}

	public static void main(String [] args)
	{
		OncoTCapDataSource dataSource = Oncotcap.getDataSource();
		Object o = dataSource.find(new GUID("888e66b8000003ed000000f7ba095543"));
	}
	/**
	 **	Finds all Enum variables that are attached to a given
	 **	OncProcessClassWriter.
	 **
	 **	@returns A vector of Enums
	 **/
//	static Vector findEnums(ProcessDeclaration process)
//	{
//		return(findVariables(process, VariableType.ENUM, true));
//	}
	static Vector findVariables(ProcessDeclaration process)
	{
		return(findVariables(process, null, true));
	}
		/*	static Vector findActions(OncProcessClassWriter process)
	{
		return(findActions(process, null, true));
	}
		*/
	/**
	 ** Finds all variables that are attached to a given process.  The
	 ** type specification will cause only that type (included) to be
	 ** returned if the include flag is set to true, if the include flag
	 ** is set to false the specified type will be excluded from the
	 ** find.  If the type argument is null all variable type will be
	 ** returned.
	 **/
	public static Vector findVariables(ProcessDeclaration process, VariableType type, boolean include)
	{
		Vector variables = new Vector();
		CodeBundle checkCB;
		ProcessDeclaration checkProcess;
		OncAction checkAction;
		DeclareVariable checkVariable;
		Iterator allActions;

		if(process != null)
		{
			//traverse code bundles looking for matching processes
			Iterator it = allCodeBundles.iterator();
			while(it.hasNext())
			{
				checkCB = (CodeBundle) it.next();
				checkProcess = checkCB.getProcessDeclaration();
				if(checkProcess != null && checkProcess.equals(process))
				{
					//traverse actions looking for AddVariableActions
					allActions = checkCB.getActionList().iterator();
					while(allActions.hasNext())
					{
						Object nextObj = allActions.next();
						checkAction = (OncAction) nextObj;
						if(checkAction.getType() == ActionType.ADD_VARIABLE)
						{
							checkVariable = ((AddVariableAction)checkAction).getVariable();
							if(checkVariable != null && (type == null || (include == true && checkVariable.getType() == type)
														  || (include == false && checkVariable.getType() != type)))
								if(! variables.contains(checkVariable))
									variables.add(checkVariable);
						}
					}
				}
			}
		}
		
		return(variables);
	}
	public StatementTemplate getStatementTemplateContainingMe()
	{
		return(statementTemplateContainingMe);
	}
	public void setStatementTemplateContainingMe(StatementTemplate template)
	{
		statementTemplateContainingMe = template;
	}
	public ProcessDeclaration getProcessDeclaration()
	{
		return(oncProcess);
	}
	public void setProcessDeclaration(Object proc)
	{
		if(proc != null && proc instanceof ProcessDeclaration) {
			oncProcess = (ProcessDeclaration) proc;

		}
		else
			oncProcess = null;
		
	}
	public void setMethodDeclaration(Object meth)
	{
		if(meth != null && meth instanceof MethodDeclaration)
			method = (MethodDeclaration) meth;
		else
			method = null;
	}
	public MethodDeclaration getMethodDeclaration()
	{
		return(method);
	}
	public EventDeclaration getEventDeclaration()
	{
		return(oncEvent);
	}
	public void setEventDeclaration(Object event)
	{
		if(event != null && event instanceof EventDeclaration)
			oncEvent = (EventDeclaration) event;
		else
			oncEvent = null;
	}
	public void setEventType(int type)
	{
		eventType = type;
	}
	public int getEventType()
	{
		return(eventType);
	}
	public String getIfClause()
	{
		return(ifClause);
	}
	public void setIfClause(Object clause)
	{
		if(clause != null && clause instanceof String)
			ifClause = (String) clause;
		else
			ifClause = null;
	}
	public ActionList getActions()
	{
		return(actions);
	}
	public Collection<OncAction> getActionList()
	{
		return(actions.getActions());
	}
	public void addAction(OncAction action)
	{
		actions.add(action);
	}
	public void removeAction(OncAction action)
	{
		actions.remove(action);
		action.delete();
	}
	public void clearActions()
	{
		actions.clear();
	}
	public void setActionList(Collection<OncAction> listOfActions) {
				if ( actions == null) 
						actions = new ActionList();
				actions.set(listOfActions);
	}
	public String getName()
	{
		return(toString());
	}
	public void setName(String name)
	{

	}
	public EditorPanel getEditorPanel()
	{
		return(new CodeBundleEditorPanel());
	}

	public EditorPanel getEditorPanelWithInstance()
	{
			CodeBundleEditorPanel cbPanel	=	new CodeBundleEditorPanel();
			cbPanel.edit(this);
			return(cbPanel);
	}
	public String toString()
	{
		String rString = "WHEN ";
		String procName = "";
		String methName = "";
		String eventName = "";

		if(getProcessDeclaration() != null) procName = getProcessDeclaration().toString();
		if(getMethodDeclaration() != null) methName = getMethodDeclaration().toString();
		if(getEventDeclaration() != null) eventName = getEventDeclaration().toString();
		rString = rString + " " +
			  (procName.trim().equals("") ? "UNSET PROCESS" : procName)
			  + " RUNS \"";
		
		if(eventType == EventChooser.PROCESS)
		{
			if(!methName.trim().equals(""))
				rString = rString + methName + "\", ";
		}
		else
		{
			if(! eventName.trim().equals(""))
				rString = rString + eventName + "\", ";
		}
		
		if(ifClause != null && ifClause.trim().length()> 0)
			rString = rString + "IF(...) ";
		if ( actions != null ) {
			Iterator it = actions.iterator();
			while(it.hasNext()) {
				Object obj = it.next();
				rString = rString + ARROW + obj.toString();
			}
		}
		if(rString.trim().equals(""))
			rString = "Empty Code Bundle";
		return(rString);
	}
	public ImageIcon getIcon()
	{
		return(icon);
	}
	public boolean dropOn(Object dropOnObject) {
		if ( dropOnObject instanceof Keyword ) {
			boolean link1 = link((Persistible)dropOnObject);
			boolean link2 = ((Persistible)dropOnObject).link(this);
			if ( link1 || link2){
				update();
				((Persistible)dropOnObject).update();
				return true;
			}
		}
		if ( dropOnObject instanceof StatementTemplate ) {
			boolean link2 = ((Persistible)dropOnObject).link(this);
			if ( link2){
				((Persistible)dropOnObject).update();
				return true;
			}
		}
		return false;
	}
	public Object clone()
	{
		return(clone(true));
	}
	public CodeBundle clone(boolean saveToDataSource)
	{
		CodeBundle cb = new CodeBundle(saveToDataSource);
		cb.setEventDeclaration(oncEvent);
		cb.setProcessDeclaration(oncProcess);
		cb.setMethodDeclaration(method);
		cb.setEventType(eventType);
		// Clone any actions 
		Iterator i = getActionList().iterator();
		while(i.hasNext()) {
			Object obj = i.next();
			if ( obj instanceof DefaultOncAction ) { 
				DefaultOncAction clonedAction = 
					(DefaultOncAction)((DefaultOncAction)obj).clone();
				clonedAction.setCodeBundleContainingMe(cb);
				cb.addAction(clonedAction);
			}
		}
		//cb.setActionList(actions.clone());
		if ( ifClause != null ) 
			cb.setIfClause(new String(ifClause));
		return(cb);		
	}
/*	public Object cloneSubstitute(StatementBundle sb, StatementTemplate st)
	{
		CodeBundle cb = new CodeBundle(false);
		cb.setStatementBundleUsingMe(sb);
		cb.setStatementTemplateContainingMe(st);
		cb.setEventDeclaration(oncEvent);
		cb.setProcessDeclaration(oncProcess);
		cb.setMethodDeclaration(method);
		cb.setEventType(eventType);
		// Clone any actions 
		Iterator i = getActionList().iterator();
		while(i.hasNext()) {
			Object obj = i.next();
			if ( obj instanceof DefaultOncAction ) { 
				DefaultOncAction clonedAction = 
					(DefaultOncAction)((DefaultOncAction)obj).cloneSubstitute(sb, cb);
				clonedAction.setCodeBundleContainingMe(cb);
				cb.addAction(clonedAction);
			}
		}
		//cb.setActionList(actions.clone());
		if ( ifClause != null ) 
			cb.setIfClause(new String(sb.substitute(ifClause)));
		return(cb);	
	} */
	protected void finalize() throws Throwable
	{
		allCodeBundles.removeElement(this);
		super.finalize();
	}
/*	public CodeSection getDeclarationSection()
	{
		CodeSection declarationSection = new CodeSection();
		declarationSection.setProcessDeclaration(getProcessDeclaration());
		declarationSection.setEventDeclaration(null);
		declarationSection.setMethodDeclaration(MethodDeclaration.declaration);
		declarationSection.setEventType(CodeSection.DECLARATION_SECTION);
		String rCode = "";
		Iterator it = actions.iterator();
		while(it.hasNext())
		{
//sub			rCode = rCode + "\n" + sb.substitute(((OncAction) it.next()).getDeclarationCode());
			rCode = rCode + "\n" + ((OncAction) it.next()).getDeclarationCode();
		}
		declarationSection.setCode(rCode);
		return(declarationSection);
	} */
/*	public Collection getDeclarationSections(StatementBundle sbContainingMe)
	{
		String rCode = "";
		Vector sections = new Vector();

		Iterator it = actions.iterator();
		while(it.hasNext())
		{
			rCode = "";
			OncAction action = (OncAction) it.next();
//			rCode = "\n" + action.getDeclarationCode();

			CodeSection cs = new CodeSection(this, rCode);
			cs.setMethodDeclaration(this.getMethodDeclaration());
			cs.setProcessDeclaration(this.getProcessDeclaration());
			cs.setEventType(CodeSection.DECLARATION_SECTION);
			cs.setCodeSource(action);
			cs.setMethodName(sbContainingMe);
			sections.add(cs);
		}

		return(sections);		
	}
	public void writeDeclarationSection(Writer w) throws IOException
	{
		Iterator it = actions.iterator();
		while(it.hasNext())
		{
			w.write(vmSubstitution(((OncAction) it.next()).getDeclarationCode()));
			w.write("\n");
		}
	}
	*/
/*	public Collection getMethodSections()
	{
			return getMethodSections(this.getStatementTemplateContainingMe().getStatementBundleImplementingMe());
	}
	public Collection getDeclarationSections()
	{
			return getDeclarationSections(this.getStatementTemplateContainingMe().getStatementBundleImplementingMe());
	}
	public Collection getMethodSections(StatementBundle sbContainingMe)
	{
		String rCode = "";
		Vector sections = new Vector();


		Iterator it = actions.iterator();
		while(it.hasNext())
		{
			rCode = "";
			OncAction action = (OncAction) it.next();
			if(ifClause != null && !ifClause.trim().equals(""))
				rCode = rCode + "\n\tif( " + sbContainingMe.substitute(ifClause) + ")\n\t{";
//sub				rCode = rCode + "\n\tif( " + sb.substitute(ifClause) + ")\n\t{";

//sub			rCode = rCode + "\n" + sb.substitute(((OncAction) it.next()).getMethodCode());
//			rCode = rCode + "\n" + action.getMethodCode();

			if(ifClause != null && !ifClause.trim().equals(""))
				rCode = rCode + "\n\t}";

			ProcessDeclaration processRef = null;
			if(action instanceof InstantiateAction)
			{
				processRef = ((InstantiateAction) action).getProcessDeclaration();
			}
			else if(action instanceof ScheduleEventAction)
			{
				processRef = ((ScheduleEventAction) action).getProcessDeclaration();
			}
			else if(action instanceof TriggerEventAction)
			{
				processRef = ((TriggerEventAction) action).getTriggeredProcessDeclaration();
			}


			CodeSection cs = new CodeSection(this, rCode);
			if(processRef != null)
				cs.addProcessReference(processRef);
			cs.setMethodDeclaration(this.getMethodDeclaration());
			cs.setProcessDeclaration(this.getProcessDeclaration());
			cs.setEventType(this.getEventType());
			cs.setEventDeclaration(this.getEventDeclaration());
			cs.setCodeSource(action);
			cs.setMethodName(sbContainingMe);
			sections.add(cs);
		}

		return(sections);
	} */
	public String getContainingProcessClassName()
	{
		if(getEventType() == EVENT)
			return(getProcessDeclaration().getName());
		else
		{
			if(getMethodDeclaration().getName().equals("collection init")  ||
			   getMethodDeclaration().getName().equals("collection update"))
			   return(getProcessDeclaration().getName() + "Collection");
			else
				return(getProcessDeclaration().getName());
		}
	}
	public void setStatementBundleUsingMe(StatementBundle sb)
	{
		statementBundleUsingMe = sb;
		Iterator it = actions.iterator();
		while(it.hasNext())
		{
			((OncAction)it.next()).setStatementBundleUsingMe(sb);
		}
	}
	public StatementBundle getStatementBundleUsingMe()
	{
		return(statementBundleUsingMe);
	}
	
	private Vector<Instruction> instructions = null;
	
	public Collection<Instruction> getInstructions()
	{
		return actions.getInstructions();
	}
	private Vector<String> ifClauseVariables = null;
	public Collection<String> getIfClauseVariables(ValueMapPath path){
		if (ifClauseVariables == null) {
			if(StringHelper.notEmpty(ifClause))
			{
				ifClauseSubstituted = path.substitute(ifClause);
				if(StringHelper.notEmpty(ifClauseSubstituted))
					ifClauseVariables = new Vector<String>(JavaParser.getAllVariables(ifClauseSubstituted));
			}
			else
				ifClauseVariables = new Vector<String> ();
		}
		return ifClauseVariables;
	}
	private void extractDeclarationInstructions(InstructionProvider provider)
	{		
			for(Instruction inst : provider.getInstructions())
			{
				if(inst.getSectionDeclaration() instanceof ClassSectionDeclaration.DeclarationSection)
					instructions.add(inst);
			}
			for(InstructionProvider prov : provider.getAdditionalProviders())
				extractDeclarationInstructions(prov);
	}
	public class MethodInstruction implements Instruction
	{
		public Collection<VariableDependency> getVariableDependencies(ValueMapPath path)
		{
				ifClauseSubstituted = path.substitute(ifClause);
				
				OncAction act;
				Vector<String> setVars = new Vector<String>();
				Vector<VariableDependency> dependencies = new Vector<VariableDependency>();
				
				Iterator<OncAction> it = actions.iterator();
				// Any LHS vars will depend on all the ifClause vars, so check each Action.
				while(it.hasNext())
				{
					act = (OncAction) it.next();
					for(Instruction inst:act.getInstructions())
					{
						if(inst.getSectionDeclaration().equals(me.getSectionDeclaration()))
						{
							dependencies.addAll(inst.getVariableDependencies(path));  // HERE!
							setVars.addAll(inst.getSetVariables(path));
						}
					}
				}
				Collection<String> ifVars = JavaParser.getAllVariables(ifClauseSubstituted);
				for(String ifVar:ifVars)
				{
					for(String setVar:setVars)
					{
						dependencies.add(new VariableDependency(setVar, ifVar, VariableDependency.LITERAL_if));
					}
				}
				dependencies.addAll(JavaParser.getDependencies(ifClauseSubstituted));
				return(dependencies);
//			}
		}
		public ClassSectionDeclaration getSectionDeclaration()
		{
			return(me.getSectionDeclaration());
		}
		public InstructionProvider getEnclosingInstructionProvider()
		{
			return(me);
		}
		public Collection<String> getAllVariables(ValueMapPath path)
		{
			OncAction act;
			Vector<String> vars = new Vector<String>();
			if(StringHelper.notEmpty(ifClause))	{
				ifClauseSubstituted = path.substitute(ifClause);
				vars.addAll(JavaParser.getAllVariables(ifClauseSubstituted));
			}
			Iterator<OncAction> it = actions.iterator();
			while(it.hasNext())
			{
				act = (OncAction) it.next(); 
				for(Instruction inst : act.getInstructions())
				{
					if(inst.getSectionDeclaration().equals(me.getSectionDeclaration()))
						vars.addAll(inst.getAllVariables(path));
				}
			}
			return(vars);
		}
		public Collection<String> getSetVariables(ValueMapPath path)
		{
			OncAction act;
			//  Will there ever be a set variable in an ifClause?
			ifClauseSubstituted = path.substitute(ifClause);
			Vector<String> vars = new Vector<String>();
			vars.addAll(JavaParser.getSetVariables(ifClauseSubstituted));
			
			Iterator<OncAction> it = actions.iterator();
			while(it.hasNext())
			{
				act = (OncAction) it.next();
				for(Instruction inst : act.getInstructions())
				{
					if(inst.getSectionDeclaration().equals(getSectionDeclaration()))
						vars.addAll(inst.getSetVariables(path));
				}
			}
			return(vars);
		}
	}   // End of class MethodInstruction.
	
	public ClassSectionDeclaration getSectionDeclaration()
	{
		int ttype = getEventType();
		if(ttype == EVENT)
			return(getEventDeclaration());
		else
			return(getMethodDeclaration());
	}
	
	
	/* TODO:  If there is an ifClause, return individual actions each one
	 * packaged with the ifClause.  Otherwise, return the Actions as here.
	 * This would simplify and clarify the code considerably.
	 * Also, when there is an ifClause, Actions will sort correctly, since 
	 * they will be separated instead of sorted as a group.
	 * The one drawback:  during execution, the ifClause will need to
	 * be evaluated for each subordinate action.
	 * Therefore, warn the user to use simple variables in the ifClause,
	 * not complex costly expressions.
	 * How expensive is a subset picker to evaluate?
	 * OK, DONE!  Oct 27, 2005, R.D.
	*/
	public Collection<InstructionProvider> getAdditionalProviders()
	{
		return new Vector<InstructionProvider>();
	}
	public Collection<ProcessDeclaration> getReferencedProcesses()
	{
		Collection<ProcessDeclaration> rVec  = new Vector<ProcessDeclaration>();
		if(StringHelper.notEmpty(this.ifClause))
		{
			Iterator it = getActionList().iterator();
			while(it.hasNext())
			{
				InstructionProvider ip = (InstructionProvider) it.next();
				Collection<ProcessDeclaration> refProcs = ip.getReferencedProcesses(); 
				for (Object refProc : refProcs){
					if ( ! (refProc instanceof ProcessDeclaration)) {
						System.err.println("CodeBundle.getReferencedProcesses: "
								+ " not a ProcessDeclaration");
					@SuppressWarnings("unused") String temp = "Breakpoint";
					}
				}
				rVec.addAll(refProcs);
			}
		}
		return(rVec);
	}
	
	public boolean isValid()
	{
		if(eventType == CodeBundle.EVENT &&	oncEvent == null)
		         
		{
			return(false);
		}
		else if(eventType == CodeBundle.METHOD && (method == null || oncProcess == null))
		{
			return(false);
		}
		else
			return(true);
	}
	
	public Collection getVariablesByType(ActionType actionType) {
		// Get all variables for this code bundle
		Collection variables = new Vector();
		for (OncAction oncAction : getActionList()) {
			if(oncAction.getType() == actionType) {
				if ( oncAction instanceof VariableActionable) {
					Persistible var = ((VariableActionable)oncAction).getVariable();
					if(var != null && !variables.contains(var))
						variables.add(var);
				}
			}
		}
		return variables;
	}
	
/*	public void writeMethodSection(Writer w) throws IOException
	{
		Iterator it = actions.iterator();
		while(it.hasNext())
		{
			w.write(vmSubstitution(((OncAction) it.next()).getMethodCode()));
			w.write("\n");
		}
	}*/
}

