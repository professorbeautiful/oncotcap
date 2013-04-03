package oncotcap.engine.java;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oncotcap.datalayer.DefaultPersistibleList;
import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.autogenpersistible.ConditionalDiscreteStateFunction;
import oncotcap.datalayer.autogenpersistible.ConditionalTableParameter;
import oncotcap.datalayer.autogenpersistible.Interpretation;
import oncotcap.datalayer.autogenpersistible.QuantitativeInterpretation;
import oncotcap.datalayer.autogenpersistible.StateMatrix;
import oncotcap.datalayer.autogenpersistible.StateMatrixRow;
import oncotcap.datalayer.persistible.BooleanExpression;
import oncotcap.datalayer.persistible.CodeBundle;
import oncotcap.datalayer.persistible.Encoding;
import oncotcap.datalayer.persistible.EnumDefinition;
import oncotcap.datalayer.persistible.EnumLevel;
import oncotcap.datalayer.persistible.StatementBundle;
import oncotcap.datalayer.persistible.StatementTemplate;
import oncotcap.datalayer.persistible.VariableDefinition;
import oncotcap.datalayer.persistible.action.AddGenericCode;
import oncotcap.datalayer.persistible.action.AddVariableAction;
import oncotcap.datalayer.persistible.action.InitVariableAction;
import oncotcap.datalayer.persistible.action.InstantiateAction;
import oncotcap.datalayer.persistible.action.ModifyPositive;
import oncotcap.datalayer.persistible.action.ModifyPositiveInteger;
import oncotcap.datalayer.persistible.action.ModifyScheduleAction;
import oncotcap.datalayer.persistible.action.ModifyString;
import oncotcap.datalayer.persistible.action.ModifySwitchable;
import oncotcap.datalayer.persistible.action.ModifySwitchablePositive;
import oncotcap.datalayer.persistible.action.ModifyVariableAction;
import oncotcap.datalayer.persistible.action.OncAction;
import oncotcap.datalayer.persistible.action.ScheduleEventAction;
import oncotcap.datalayer.persistible.action.TriggerEventAction;
import oncotcap.datalayer.persistible.action.VariableModification;
import oncotcap.datalayer.persistible.parameter.CharacteristicSingleParameter;
import oncotcap.datalayer.persistible.parameter.DeclareEnum;
import oncotcap.datalayer.persistible.parameter.DeclareEnumPicker;
import oncotcap.datalayer.persistible.parameter.DeclarePositive;
import oncotcap.datalayer.persistible.parameter.DeclarePositiveInteger;
import oncotcap.datalayer.persistible.parameter.DeclareSwitchable;
import oncotcap.datalayer.persistible.parameter.DeclareSwitchablePositive;
import oncotcap.datalayer.persistible.parameter.DeclareVariable;
import oncotcap.datalayer.persistible.parameter.LevelSingleParameter;
import oncotcap.datalayer.persistible.parameter.Parameter;
import oncotcap.datalayer.persistible.parameter.SingleParameter;
import oncotcap.datalayer.persistible.parameter.SubsetParameter;
import oncotcap.datalayer.persistible.parameter.TcapString;
import oncotcap.datalayer.persistible.parameter.TwoLevelParameter;
import oncotcap.display.common.EventChooser;
import oncotcap.display.editor.EditorFrame;
import oncotcap.engine.ClassSectionDeclaration;
import oncotcap.engine.Instruction;
import oncotcap.engine.InstructionAndValues;
import oncotcap.engine.InstructionProvider;
import oncotcap.engine.ValueMapPath;
import oncotcap.util.ObjectSerializer;
import oncotcap.util.OncMessageBox;
import oncotcap.util.StringHelper;

public class JavaCodeProvider
{
	private static int nNewProcesses = 0;
	private static int varCount = 0;
	
	public static String getCode(InstructionAndValues iav)
	{
		return(getCode(iav.getInstruction(), iav.getValues()));
	}
	public static String getCode(Instruction inst, ValueMapPath path)
	{
		InstructionProvider provider = inst.getEnclosingInstructionProvider();
		Class pClass = provider.getClass();
		ClassSectionDeclaration decl = inst.getSectionDeclaration();
		
		String id;
		InstructionProvider prov = inst.getEnclosingInstructionProvider();
		if(prov instanceof Persistible)
			id = ((Persistible) prov).getGUID().getStrId();
		else
			id = "NO ID: " + prov.getClass().toString();

		String rVal = "\n//<------- " + id + ":" + prov.getClass() + "\n";
		if(prov instanceof OncAction)
		  rVal = rVal + "//         " + ((OncAction) prov).getCodeBundleContainingMe().getGUID() + ":CodeBundle\n"; 
		if(prov instanceof Parameter)
			rVal = rVal + "//         " + "Parameter\n";
		
		String code = "";
		if(decl instanceof ClassSectionDeclaration.DeclarationSection)
		{
			if(pClass.equals(AddGenericCode.class))
				code = genericCodeDecl(provider, path);
			else if(pClass.equals(AddVariableAction.class))
				code = addVariableActionDecl(provider, path);
			else if(pClass.equals(ScheduleEventAction.class))
				code = triggerEventDecl(provider, path);
			else if(pClass.equals(TriggerEventAction.class))
				code = triggerEventDecl(provider, path);
			else if(pClass.equals(EnumDefinition.class))
				code = enumDefinitionDecl(provider, path);
			else if(pClass.equals(SubsetParameter.class))
				code = subsetParameterDecl(provider, path);
		}
		else
		{
			if(pClass.equals(CodeBundle.class))
				code = codeBundleCode(provider, path);
			else if(pClass.equals(AddGenericCode.class))
				code = genericCode(provider, path);
			else if(pClass.equals(InitVariableAction.class))
				code = initVariable(provider, path);
			else if(pClass.equals(ModifyScheduleAction.class))
				code = modifySchedule(provider, path);
			else if(pClass.equals(InstantiateAction.class))
				code = instantiateAction(provider, path);
			else if(pClass.equals(ScheduleEventAction.class))
				code = scheduleEvent(provider, path);
			else if(pClass.equals(TriggerEventAction.class))
				code = triggerEvent(provider, path);
			else if(pClass.equals(ModifyVariableAction.class))
				code = modifyVariable(provider, path);
			code = wrapWithIfClause(provider, path, code);
		}

		rVal = rVal + code + "\n//<------------ " + id + "\n";
		return(code);			
	}

	private static String getMethodCode(VariableModification mod, Object originalVariable, ValueMapPath path)
	{
		Class pClass = mod.getClass();
		if(pClass.equals(ModifyPositive.class))
			return(modifyPositive(mod, originalVariable, path));
		else if(pClass.equals(ModifyPositiveInteger.class))
			return(modifyPositiveInteger(mod, originalVariable, path));
		else if(pClass.equals(ModifyString.class))
			return(modifyString(mod, originalVariable, path));
		else if(pClass.equals(ModifySwitchable.class))
			return(modifySwitchable(mod, originalVariable, path));
		else if(pClass.equals(ModifySwitchablePositive.class))
			return(modifySwitchablePositive(mod, originalVariable, path));
		else
			return(StringHelper.emptyString);
	}
	private static String getDeclarationCode(DeclareVariable var, ValueMapPath path)
	{
		Class pClass = var.getClass();
		if(pClass.equals(DeclarePositive.class))
			return(declarePositiveDecl(var, path));
		else if(pClass.equals(DeclarePositiveInteger.class))
			return(declarePositiveIntegerDecl(var, path));
		else if(pClass.equals(DeclareSwitchable.class))
			return(declareSwitchableDecl(var, path));
		else if(pClass.equals(DeclareSwitchablePositive.class))
			return(declareSwitchablePositiveDecl(var, path));
		else if(pClass.equals(TcapString.class))
			return(tcapStringDecl(var, path));
		else
			return(StringHelper.emptyString);
	}
	private static String codeBundleCode(InstructionProvider provider, ValueMapPath path)
	{
		//  this method may no longer be necessary - rd, jan 25, 2006.
		if(provider instanceof CodeBundle)
		{
			CodeBundle cb = (CodeBundle) provider;
			if(! cb.isValid())
			{
				OncMessageBox.showError("A code bundle does not have its Process/Method or Event set", "Model Error");
				EditorFrame.showEditor(cb);
				return(StringHelper.emptyString);
			}
		}
		System.err.println("codeBundleCode should no longer be used, I think!  (JavaCodeProvider.codeBundleCode()");
		return(StringHelper.emptyString);
	}
	private static String wrapWithIfClause(InstructionProvider provider, ValueMapPath path, String code){
		CodeBundle cb = ((OncAction) provider).getCodeBundleContainingMe();
		if(cb.getIfClause() != null && !cb.getIfClause().trim().equals(""))
		{
			String ifClause = cb.getIfClause().trim();
			if(ifClause.endsWith("`") && ifClause.startsWith("`"))
			{
				String singleParamName = ifClause.substring(1,ifClause.length()-1);
				SingleParameter singleParam = path.getSingleParameter(singleParamName);
				if(singleParam instanceof SubsetParameter || singleParam instanceof LevelSingleParameter)
					ifClause = getBooleanExpression(singleParam, path);
			}
			String wrappedCode = "\t\tif(" + path.substitute(ifClause) + ")\n\t{\n"
			+ code + "\n\t\t}\n";
			return(path.substitute(wrappedCode));
		}
		else return(code);
	}
	
	private static String genericCode(InstructionProvider provider, ValueMapPath path)
	{
		if(provider instanceof AddGenericCode)
		{
			AddGenericCode genericCode = (AddGenericCode) provider;
			if(genericCode.getGenericCode() != null && ! genericCode.getGenericCode().trim().equals(""))
			{
				if(genericCode.getAddCodeInsideMethod())
					return(path.substitute(genericCode.getGenericCode() + "\n"));
			}
		}
		return(StringHelper.emptyString);
	}
	private static String genericCodeDecl(InstructionProvider provider, ValueMapPath path)
	{
		if(provider instanceof AddGenericCode)
		{
			AddGenericCode genericCode = (AddGenericCode) provider;
			if(genericCode.getGenericCode() != null && ! genericCode.getGenericCode().trim().equals(""))
			{
				if(! genericCode.getAddCodeInsideMethod())
					return(path.substitute(genericCode.getGenericCode() + "\n"));
			}
		}
		return(StringHelper.emptyString);
	}
	private static String initVariable(InstructionProvider provider, ValueMapPath path)
	{
		if(provider instanceof InitVariableAction)
		{
			String code;
			InitVariableAction initVar = (InitVariableAction) provider;
			DeclareVariable newInit = initVar.getNewInitialization();
			if(newInit != null && newInit.getInitialValue() != null && !newInit.getInitialValue().trim().equals(""))
			{
				String initVal = newInit.getInitialValue().trim();
				code = standardInitVariableCode(initVar, newInit);
				if(newInit instanceof DeclareEnum)
				{
					if(initVal.startsWith( "`") && initVal.endsWith("`"))
					{
						Parameter param = path.getParameter(initVal.substring(1,initVal.length()-1));
						if(param instanceof SubsetParameter && ((SubsetParameter) param).getPickerType() == SubsetParameter.ASSIGNMENT)
						{
							SubsetParameter sp = (SubsetParameter) param;

							if(sp.getPickerType() == SubsetParameter.ASSIGNMENT)
							{
								Iterator it = sp.getAssignmentEnums().iterator();
								code = "";
								while(it.hasNext())
								{
									BooleanExpression expr = (BooleanExpression) it.next();
									EnumDefinition enumDef = (EnumDefinition) expr.getLeftHandSide();
									EnumLevel enumLevel = (EnumLevel) expr.getRightHandSide();
									String className = StringHelper.javaNameKeepBQuotes(enumDef.getProcess().getName()); 
									String val =  className +
						              			"." + StringHelper.javaNameKeepBQuotes(enumDef.getName()) +
						              			"." + StringHelper.javaNameKeepBQuotes(enumLevel.getName());
									code = code + "\t\t((" + className +")getContainerInstance(" + className + ".class)).changeProp(" + val + ");\n";
									//code = "\t\t_" + StringHelper.javaNameKeepBQuotes(enumDef.getName()) + " = " + val + ";\n";
								}
							}
						}
					}
				}
				else {
					if(initVal.startsWith( "`") && initVal.endsWith("`")) {
						Parameter param = 
							path.getParameter(initVal.substring(1,initVal.length()-1));
						if ( param instanceof ConditionalTableParameter ){
							StringBuilder snippet = new StringBuilder();
							snippet.append("\t");
							//snippet.append(initVar.getName());
							//snippet.append(" = \n");
							snippet.append(getConditionalTableParameter((ConditionalTableParameter)param, path, initVar.getName()));
							code = snippet.toString();
						}
					}
				}
				return(path.substitute(code));
			}
		}		
		return(StringHelper.emptyString);
	}
	private static String standardInitVariableCode(InitVariableAction initVar, DeclareVariable newInit)
	{
		if(! initVar.getName().trim().equals("")) {
//			if ( param instanceof ConditionalTableParameter ){
//				code = getConditionaltableParameter((ConditionalTableParameter)param);
//			}
			return("\t\t" + initVar.getName() + " = " + newInit.getInitialValue().toString() + ";\n");
		}
		else
			return(StringHelper.emptyString);
	}
	private static String instantiateAction(InstructionProvider provider, ValueMapPath path)
	{
		if(provider instanceof InstantiateAction)
		{
			InstantiateAction iAction = (InstantiateAction) provider;
			
			String strCode = "";

			String enumVars = "";
			if(iAction.getEnumInitializations() != null)
				enumVars = iAction.getEnumInitializations().trim();
			System.out.println("enumVars " + enumVars);


			//if this is a back-quoted reference (`ref`) to an instantiation
			//type SubsetPicker or Table Parameter
			if(StringHelper.isBackquoteRef(enumVars) ) //enumVars.endsWith("`") && enumVars.startsWith("`"))
			{
					String singleParamName = enumVars.substring(1,enumVars.length()-1);
					SingleParameter singleParam = path.getSingleParameter(singleParamName);
							
			
				if(singleParam != null && singleParam instanceof SubsetParameter)
				{
					SubsetParameter sp = (SubsetParameter) singleParam;
					strCode = instantiateUsingSubsetParameter(sp, iAction, path);			
				}
				else if(singleParam != null && singleParam instanceof ConditionalTableParameter)
				{
					ConditionalTableParameter tp = (ConditionalTableParameter) singleParam;
					strCode = instantiateUsingTableParameter(tp, iAction, path);
					
				}
			}
			else 
				strCode = instantiateWithoutParameters(iAction, path);
			
			

			return(path.substitute(strCode));

		//!!!!
		}
		return(StringHelper.emptyString);
	}
	private static String instantiateWithoutParameters(InstantiateAction iAction, ValueMapPath path) {
		String strCode = new String();
		String enumArrayName = new String("enums" + (++nNewProcesses));
		strCode = strCode + "\t\tOncEnum [] " + enumArrayName + " = {";
		strCode = strCode + "};\n";

		String procName = "process" + nNewProcesses;
		strCode = strCode + "\t\t" + StringHelper.javaName(iAction.getInitializationProcessDeclaration().getName()) + " " + procName + " = new " + StringHelper.javaName(iAction.getInitializationProcessDeclaration().getName()) + "(this, " + enumArrayName + ");\n";
		strCode = strCode + "\t\tprocesses.put(" + StringHelper.javaName(iAction.getInitializationProcessDeclaration().getName()) + ".class, process" + nNewProcesses + ");\n\n";
//		 Initialize Variables
		strCode = strCode + getVarInitializations( iAction,  path);
		return path.substitute(strCode.toString());
	}
	
	private static String instantiateUsingSubsetParameter(SubsetParameter sp, InstantiateAction iAction, ValueMapPath path) {
		//assemble the Enums used to instantiate this
		//new process in an array so they can be passed to the
		//constructor.
		Vector<BooleanExpression> instantiateEnumVars = new Vector<BooleanExpression>();
		Iterator it;
		Object obj;
		Object leftSide;
		Object rightSide;
		String strCode = "";
		if(sp.getPickerType() == DeclareEnumPicker.INSTANTIATION)
		{					
			//these enums should be returned as a vector of type
			//BooleanExpression, where the left side of the
			//expession is an EnumDefinition and the right side is
			//an EnumLevel
			Vector enums = sp.getInstantiationEnums();
			
			it = enums.iterator();
			while(it.hasNext())
			{
				obj = it.next();
				if(obj instanceof BooleanExpression)
				{
					instantiateEnumVars.add((BooleanExpression) obj);
				}
			}
		}
		String enumArrayName = new String("enums" + (++nNewProcesses));
		strCode = strCode + "\t\tOncEnum [] " + enumArrayName + " = {";
		it = instantiateEnumVars.iterator();
		boolean firstVar = true;
		while(it.hasNext())
		{
			obj = it.next();
			leftSide = ((BooleanExpression) obj).getLeftHandSide();
			rightSide =((BooleanExpression) obj).getRightHandSide();
			if(leftSide instanceof EnumDefinition && rightSide instanceof EnumLevel)
			{
				EnumDefinition enumDef = (EnumDefinition) leftSide;
				EnumLevel level = (EnumLevel) rightSide;
				
				if(!firstVar)
					strCode = strCode + " ,";
				else
					firstVar = false;

				strCode = strCode + StringHelper.javaName(enumDef.getProcess().getName()) + "."
				                  + StringHelper.javaName(enumDef.getName()) + "."
				                  + StringHelper.javaName(level.getName());
			}
		}
		strCode = strCode + "};\n";

		String procName = "process" + nNewProcesses;
		strCode = strCode + "\t\t" + StringHelper.javaName(iAction.getInitializationProcessDeclaration().getName()) + " " + procName + " = new " + StringHelper.javaName(iAction.getInitializationProcessDeclaration().getName()) + "(this, " + enumArrayName + ");\n";
		strCode = strCode + "\t\tprocesses.put(" + StringHelper.javaName(iAction.getInitializationProcessDeclaration().getName()) + ".class, process" + nNewProcesses + ");\n\n";
//		 Initialize Variables
		strCode = strCode + getVarInitializations( iAction,  path);
		return strCode;
	}

	/** Instantiate using a conditional table parameter - generate code to create N 
	 * instances of the selected process. Use the table to determine the 
	 * distribution of instantiated object types
	 * @param tableParam
	 * @param iAction
	 * @param valueMapPath
	 * @return String instantiation code
	 */
	// TODO determine why this has to be static
	private static String instantiateUsingTableParameter(ConditionalTableParameter tableParam, 
			InstantiateAction iAction,
			ValueMapPath valueMapPath) {
		String enumVars = null;
		if(iAction.getEnumInitializations() != null)
			enumVars = iAction.getEnumInitializations().trim();
		Vector condTableParams = 
			valueMapPath.getParameters(enumVars); 
		
		Iterator ii = condTableParams.iterator();
		Collection probabilities = null;
		ConditionalDiscreteStateFunction cdsf = null;
		while ( ii.hasNext() ) {
			Object obj = ii.next();
			if ( obj instanceof ConditionalTableParameter) {
				ConditionalTableParameter ctp = (ConditionalTableParameter)obj;
				Collection cdsfs = ctp.getFunction();
				
				for ( Object cdsfObj : cdsfs){
					if ( cdsfObj instanceof ConditionalDiscreteStateFunction)
						cdsf = (ConditionalDiscreteStateFunction)cdsfObj;
					if (cdsf != null){
						probabilities = cdsf.getOutcomeValues(); // last column
						//						System.out.println("PROBS " + new Vector(probabilities));
					}
				}
			}
		}
		nNewProcesses++;
		int firstEnumIndex =nNewProcesses; 
		if (iAction.getInstantiateAggregate())
			return initOncEnums(probabilities.size(), iAction, cdsf) 
			+ getProbabilityArray(probabilities, valueMapPath, firstEnumIndex)
			+ getConditionalInstantiationCodeAggregate(iAction, valueMapPath); 		
		else
			return initOncEnums(probabilities.size(), iAction, cdsf) 
			+ getProbabilityArray(probabilities, valueMapPath, firstEnumIndex)
			+ getConditionalInstantiationCode(iAction, valueMapPath);
	}
	
	private static String getVarInitializations(InstantiateAction iAction, ValueMapPath path) {
		String strCode = new String();
		Object obj = null;
		String varType = "unknowntype";
		Iterator it = iAction.getVariableInitializations().iterator();
		while(it.hasNext()) {
			obj = it.next();
			if(obj instanceof DeclareVariable) {
				DeclareVariable declareVar = (DeclareVariable) obj;
				if(declareVar instanceof DeclareEnum) {
					DeclareEnum de = (DeclareEnum) declareVar;
					if(de.isID())
						varType = "OncIDEnum";
					else
						varType = "OncEnum";
				}
//				System.out.println("DeclarVariable initial value " + e.getClass()
//				+ " - " +  e.getInitialValue());
				String initialValue;
				if(declareVar != null && declareVar.getInitialValue() != null)
					initialValue = declareVar.getInitialValue().trim();
				else
					initialValue = "";
				// Backquoted initial value
				if ( StringHelper.isBackquoteRef(initialValue) ) {
					SingleParameter singleParam = path.getSingleParameter(initialValue.substring(1,initialValue.length()-1));
					if(singleParam != null && singleParam instanceof SubsetParameter) {
						SubsetParameter sp = (SubsetParameter) singleParam;
						if(sp.getPickerType() == DeclareEnumPicker.ASSIGNMENT) {
							String assignmentVarName = "enumAssignment" + nNewProcesses;					
							strCode = strCode + "\t\t" + varType + " [] " + assignmentVarName + " = {" + sp.getAssignmentValues() +"};\n";
							initialValue = assignmentVarName;
						}
					}
					else if(singleParam != null && singleParam instanceof LevelSingleParameter) {
						String assignmentVarName = "enumAssignment" + nNewProcesses;					
						strCode = strCode + "\t\t" + varType + " [] " + assignmentVarName + " = {" + getAssignmentExpression(singleParam, path) +"};\n";
						initialValue = assignmentVarName;							
					}
				}
				strCode = strCode + "\t\t" + "process" + nNewProcesses + "." + StringHelper.javaName(path.substitute(declareVar.getName())) + " = " + initialValue + ";\n";
			}
			else
				System.out.println("WARNING: Initialization of type " + obj.getClass() + " in InstantiateAction");
		}
		return(path.substitute(strCode));
	}
	
	private static String modifySchedule(InstructionProvider provider, ValueMapPath path)
	{
		if(provider instanceof ModifyScheduleAction)
		{
			String code = "";
			ModifyScheduleAction mod = (ModifyScheduleAction) provider;
			//!!
			if(mod.modType == ModifyScheduleAction.PLACE_ON_HOLD)
			{
				code = "\t\tgetEventManager().endRecurrentEvents(\"" + StringHelper.javaName(path.substitute(mod.scheduleToModify.getName())) + "\");\n";
		/*			String containingClassName = StringHelper.javaName(sb.substitute(scheduleToModify.getCodeBundleContainingMe().getContainingProcessClassName()));
				//((cancer_cell) getContainerInstance(cancer_cell.class))
				String procName = "process" + counter++;
				write.write("\t\t" + containingClassName + " " + procName + " = " +
								"(" + containingClassName + ") getContainerInstance(" + containingClassName + ".class);\n");
				write.write("\t\tif(" + procName + " != null)\n");
				write.write("\t\t\t" + procName + "." + StringHelper.javaName(sb.substitute(scheduleToModify.getName())) + ".endRecurrentEvent();\n");
				write.write("\t\telse\n");
				write.write("\t\t\tLogger.log(\"WARNING: Unable to modify schedule " + scheduleToModify.getName() + ". getContainerInstance() returns null trying to find " + containingClassName + "\");\n"); */
			    return(path.substitute(code));
			}
		}
		return(StringHelper.emptyString);
	}
	private static String modifyVariable(InstructionProvider provider, ValueMapPath path)
	{
		if(provider instanceof ModifyVariableAction)
		{
			ModifyVariableAction mod = (ModifyVariableAction) provider;
			Object originalVariable = mod.getVariable();
			return(path.substitute(getMethodCode(mod.getModification(), originalVariable, path)));
		}
		return(StringHelper.emptyString);
	}
	private static String scheduleEvent(InstructionProvider provider, ValueMapPath path)
	{
		if(provider instanceof ScheduleEventAction)
		{
			varCount++;
			String code = "";
			ScheduleEventAction ev = (ScheduleEventAction) provider;
			//int triggerCount = ObjectSerializer.getSerialNumber(ev.getCodeBundleContainingMe().getStatementTemplateContainingMe());
			int triggerCount = ObjectSerializer.getSerialNumber(path);
			
			if(ev.getScheduleStartType() == ScheduleEventAction.NOW)
				code += "\t\tdouble startTime" + varCount + " = getMasterScheduler().globalTime;\n";
			else
				code += "\t\tdouble startTime" + varCount + " = OncTime.convert(" + ev.startDelayTime.getTimeString() + ", " + ev.startDelayTime.getTimeUnit().getVarName() +", getMasterScheduler().getTimeUnit());\n";


			if(! ev.recur)
			{
				code += triggerCode(ev, "startTime" + varCount, path, triggerCount);
			}
			else
			{
				if(ev.isDayList())
				{
					code += "\t\tDayList list" + varCount + " = null;\n";
					code += "\t\ttry{list" + varCount + " = new DayList(\"" + ev.dayList + "\");}\n";
					code += "\t\tcatch(Throwable e){System.out.println(\"WARNING: Invalid DayList: " + ev.dayList + " in schedule: \" + \"" + ev.getName() + "\");}\n";
					code += "\t\tif(list" + varCount + " != null)\n\t\t{\n";
						code += "\t\t\tIterator it = list" + varCount + ".iterator();\n";
						code += "\t\t\twhile(it.hasNext())\n\t\t\t{\n";
							code += "\t\t\t\tdouble dayN = (double) ((Integer) it.next()).intValue();\n";
							code += "\t\t\t\tdouble sTime = OncTime.convert(dayN, OncTime.DAY, getMasterScheduler().getTimeUnit());\n";
							code += triggerCode(ev, "startTime" + varCount + " + sTime", path, triggerCount);
							code += "\t\t\t}\n\t\t}\n";
				}
				else
				{
					code += "\t\tdouble interval" + varCount + " = OncTime.convert(" + ev.gapTime.getTimeString() +", " + ev.gapTime.getTimeUnit().getVarName() + ", getMasterScheduler().getTimeUnit());\n";
					if(ev.scheduleEndType == ScheduleEventAction.X_TIMES)
					{
						code += "\t\tdouble intervalSum" + varCount + " = 0.0;\n";
						code += "\t\t\tfor(int n = 1; n <= " + ev.numberOfTimesUntilEnd + " ; n++)\n\t\t\t{\n";
						code += triggerCode(ev, "startTime" + varCount + " + intervalSum" + varCount, path, triggerCount);
						code += "\t\t\t\tintervalSum" + varCount + " += interval" + varCount + ";\n";
						code += "\t\t\t}\n";
					}
					else if(ev.scheduleEndType == ScheduleEventAction.AFTER_PERIOD)
					{
						code += "\t\tdouble scheduleEnd = scheduleStart " + " OncTime.convert(" + ev.endDelayTime.getTimeString() + ", " + ev.endDelayTime.getTimeUnit().getVarName() + ", getMasterScheduler().getTimeUnit());\n";
						code += "\t\tfor(double t = scheduleStart; t <= scheduleEnd; t+=interval" + varCount + ")\n\t\t{\n";
						code += triggerCode(ev, "t", path, triggerCount);
						code += "\t\t}\n";
					}
					else if(ev.scheduleEndType == ScheduleEventAction.FOREVER)
					{
						String procName;
						if(ev.eventType == EventChooser.EVENT)
						{
							//TODO: not yet implimented
							code += "\t\tscheduling events forever not yet implimented....\n";
							//something like this?
							//w.write("\t\tEventManager.EventScheduler e = new EventManager.EventScheduler(startTime" + triggerCountLocal + ", \"" + procName + "\", \"" + sb.substituteJavaName(eventname) + "\", paramTable" + triggerCountLocal + ", interval" + triggerCountLocal + ");\n");
						}
						else
						{
							if(ev.method.getName().equals("collection update"))
								procName = "getCollection()";
							else
								procName =  path.substituteJavaName(ev.process.getName());
							
							String methName = "\"" + path.substituteJavaName(ev.getCodeBundleContainingMe().getProcessDeclaration().getName()) + "." + path.substituteJavaName(ev.method.getName())  + "\"";
//							>       EventManager.registerEvent(this, "Cancer_cellCollection.collection_update");
//							>       EventManager.EventScheduler e = new EventManager.EventScheduler(startTime3, getCollection(), "Cancer_cellCollection.collection_update", null, interval3);
							
							code += "\t\tgetEventManager().registerEvent(this, " + methName + ");\n";
							code += "\t\tEventManager.EventScheduler e" + varCount +" = getEventManager().new EventScheduler(startTime"+ varCount + ", " + procName + ", " + methName + ", null, interval" + varCount + ");\n";
//							w.write("\t\t" + sb.substituteJavaName(name) + " = new Scheduler(" + procName + ", \"" + sb.substituteJavaName(method.getName()) + "\");\n");
//							w.write("\t\t" + sb.substituteJavaName(name) + ".addRecurrentEvent(startTime" + triggerCountLocal + ", interval" + triggerCountLocal + ");\n");
						}					
					}
				}
			}
			return(path.substitute(code));
		}
		return(StringHelper.emptyString);
	}
	private static int schedulerCount = 0;
	private static String triggerEvent(InstructionProvider provider, ValueMapPath path)
	{
		if(provider instanceof TriggerEventAction)
		{
			TriggerEventAction ti = (TriggerEventAction) provider;
		//	int triggerCount = ObjectSerializer.getSerialNumber(ti.getCodeBundleContainingMe().getStatementTemplateContainingMe());
			int triggerCount = ObjectSerializer.getSerialNumber(path);
			String code = "";
			if(ti.eventType == EventChooser.EVENT)
			{
				code += "\t\tEventManager.EventScheduler e" + (schedulerCount++) + " = getEventManager().new EventScheduler(getMasterScheduler().globalTime, this, " + "\"" + StringHelper.javaName(ti.event.getName()) + "\"" + ", paramTable" + triggerCount + ");\n";
			}
			else
			{
				boolean isCollection = false;
				String methodName = ti.getMethod().toString();
				if(methodName.equals("is updated"))
				{
					methodName = "update";
				}
				else if(methodName.equals("is initialized"))
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
				code += "\t\tIterator procs = ((Vector) processes.get(" + StringHelper.javaName(ti.getTriggeredProcessDeclaration().getName()) + ".class)).iterator();\n";
				code += "\t\twhile(procs.hasNext())\n";
				code += "\t\t\tgetMasterScheduler().installTriggerAbsolute(getMasterScheduler().globalTime, ((OncProcess) procs.next())" + collectionMethod + ", \"" + methodName + "\");\n";
			}	
			return(path.substitute(code));
		}
		return(StringHelper.emptyString);
	}
	private static String subsetParameterDecl(InstructionProvider provider, ValueMapPath path)
	{
		return(StringHelper.emptyString);
	}
	private static String enumDefinitionDecl(InstructionProvider provider, ValueMapPath path)
	{
		if(provider instanceof EnumDefinition)
		{
			EnumDefinition ed = (EnumDefinition) provider;
			//if an Enum has no levels don't print a definition 'cause it can't be used anyway
			if(ed.getLevelList() != null && ed.getLevelList().getNumberOfLevels() > 0)
			{
				String superType;
				String code = "";
				Iterator iter2;
				boolean didFirst;
				String val, valLine = "";
		
				if(ed.isID())
					superType = "OncIDEnum";
				else
					superType = "OncEnum";
				
				String className = path.substituteJavaName(ed.className());
				code += "\tpublic static class " + className + " extends " + superType + "\n\t{\n\t\tprivate " + className + "() { }\n\t\tprivate " + className + "(String name) { super(name); }\n";
	//			System.out.println("ed.getLevelList() " + ed.getLevelList());
				iter2 = ed.getLevelList().getLevelIterator();
				didFirst = false;
				while(iter2.hasNext())
				{
					val = path.substituteJavaName(iter2.next().toString());
					code += "\t\tpublic static final " + className + " " + val + " = new " + className + "(\"" + val + "\");\n";
					if(didFirst)
						valLine = valLine + ", " + val;
					else
					{
						valLine = "\t\tpublic static final " + className + " [] VALUES = { " + path.substituteJavaName(val);
						didFirst = true;
					}
				}
				valLine = valLine + " };";
				code += "\n" + valLine + "\n";
				code += "\t\tpublic OncEnum [] getValues()\n\t\t{\n\t\t\treturn(VALUES);\n\t\t}\n\t}\n";
				code += "\tpublic " + className + " " + path.substituteJavaName(ed.varName()) + " = " + className + "." + path.substituteJavaName(ed.getDefault()) + ";\n\n";
				return(code);
			}
			else
				return(StringHelper.emptyString);
		}
		else
			return(StringHelper.emptyString);
	}
	private static String triggerEventDecl(InstructionProvider provider, ValueMapPath path)
	{
		if(provider instanceof TriggerEventAction || provider instanceof ScheduleEventAction)
		{
			String code = "";
			TriggerEventAction ti = (TriggerEventAction) provider;
			//int triggerCount = ObjectSerializer.getSerialNumber(ti.getCodeBundleContainingMe().getStatementTemplateContainingMe());
			int triggerCount = ObjectSerializer.getSerialNumber(path);
			if(ti.eventType == EventChooser.EVENT)
			{
				//set up arguments for event calls.  A copy of the caller
				//OncProcess and a Hashtable of all SingleParameters available
				//in the codebundle that triggers the event will be sent.
		
				code += "\t\tprotected static EventParameters paramTable" + triggerCount + " = new EventParameters();\n";
				StatementTemplate st = null;
				CodeBundle cb = ti.getCodeBundleContainingMe();
				if(cb != null)
					st = cb.getStatementTemplateContainingMe();
		
				if(st != null)
				{
					code += "\t\tstatic\n\t\t{\n";
					Iterator singleParams  = st.getParameters().getSingleParameters().iterator();
					while(singleParams.hasNext())
						code += getParamTableAddLine((SingleParameter) singleParams.next(), ti, path, triggerCount );
					code += "\n\t\t}\n";
				}
			}
			return(path.substitute(code));
		}
		return(StringHelper.emptyString);
	}
	private static int vtIdx = 0;
	private static String getParamTableAddLine(SingleParameter singleParam, TriggerEventAction te, ValueMapPath path, int triggerCount)
	{
		String code = "";
		code += "\t\t\tValueAndType vt" + (++vtIdx) + " = new ValueAndType(\"" + path.getValue(singleParam.getDisplayName()) + "\", " + singleParam.getClass().getName() + ".class);\n";
		code += "\t\t\tparamTable" + triggerCount + ".put(\"" + singleParam.getDisplayName() + "\", vt" + vtIdx + ");\n";
		return(code);
	}
	private static String addVariableActionDecl(InstructionProvider provider, ValueMapPath path)
	{
		if(provider instanceof AddVariableAction)
		{
			String code = "";
			AddVariableAction var = (AddVariableAction) provider;
			code += getDeclarationCode(var.getVariable(), path);
/*			if(var.getCodeBundleContainingMe().getMethodDeclaration().displayName.equals("collection is initialized")){
				System.out.println(var.getCodeBundleContainingMe());
				System.out.println(var.getCodeBundleContainingMe().getMethodDeclaration().displayName);
			}
*/			return(path.substitute(code));
		}
		return(StringHelper.emptyString);
	}
	private static String triggerCode(ScheduleEventAction ev, String time, ValueMapPath path, int triggerCount)
	{
		String code = "";
		if(ev.eventType == EventChooser.EVENT)
		{
			code += "\t\tEventManager.EventScheduler e" + triggerCount + " = getEventManager().new EventScheduler(" + time + ", this, " + "\"" + StringHelper.javaName(path.substitute(ev.event.getName())) + "\"" + ", paramTable" + triggerCount + ");\n";
		}
		else
		{
			code += "\t\tgetMasterScheduler().installTriggerAbsolute(" + time + ", this.collection, \"updateAll\", eventArgs)\n";
		}
		return(code);
	}

	
	private static String getModifyVariableName(Object originalVariable, ValueMapPath path)
	{
		String varName = null;
		
		if(originalVariable instanceof DeclareVariable)
			varName = ((DeclareVariable) originalVariable).getName();
		else if(originalVariable instanceof VariableDefinition)
			varName = ((VariableDefinition) originalVariable).getName();
		if(varName != null)
		{
			varName = StringHelper.javaName(path.substitute(varName));
		}
		return(varName);
	}
	private static String genericModifyVar(VariableModification mod, Object originalVariable, ValueMapPath path, String operator)
	{
		return("\t\t" + getModifyVariableName(originalVariable, path) + " " + operator + " " + mod.getModification() + ";");
	}
	
	private static String modifyPositive(VariableModification mod, Object originalVariable, ValueMapPath path)
	{
		if(mod instanceof ModifyPositive)
		{
			return(genericModifyVar(mod, originalVariable, path, "*="));
		}
		return(StringHelper.emptyString);
	}
	
	private static String modifyPositiveInteger(VariableModification mod, Object originalVariable, ValueMapPath path)
	{
		if(mod instanceof ModifyPositiveInteger)
		{
			return(genericModifyVar(mod, originalVariable, path, "+="));
		}
		return(StringHelper.emptyString);
	}
	private static String modifyString(VariableModification mod, Object originalVariable, ValueMapPath path)
	{
		if(mod instanceof ModifyString)
		{
			return(genericModifyVar(mod, originalVariable, path, "="));
		}
		return(StringHelper.emptyString);
	}
	private static String modifySwitchable(VariableModification mod, Object originalVariable, ValueMapPath path)
	{
		if(mod instanceof ModifySwitchable)
		{
				return("\t\t" + getModifyVariableName(originalVariable, path) + " = " + ((ModifySwitchable) mod).getSwitch() + ";");
		}
		return(StringHelper.emptyString);
	}
	private static String modifySwitchablePositive(VariableModification mod, Object originalVariable, ValueMapPath path)
	{
		if(mod instanceof ModifySwitchablePositive)
		{
				String code;
				String varName = getModifyVariableName(originalVariable, path);
				code = "\t\t" + varName + ".setValue(" + varName + ".getValue() *= " + mod.getModification() + ");\n";
				code = code + "\t\t" + varName + ".setState(" + ((ModifySwitchablePositive) mod).getSwitch() + ")";
				return(code);
		}
		return(StringHelper.emptyString);
	}	
	private static String declareVariableDecl(String type, DeclareVariable var, ValueMapPath path)
	{
		String code = "";
		String varName;
		varName = StringHelper.javaName(path.substitute(var.getName()));
		String initVal = var.getInitialValue();
		if(initVal == null)
			initVal = "";
		else
			initVal = initVal.trim();
		code += "\t@VariablePlotDescriptor()\n";
		code += "\tpublic " + type + " " + varName +	((! initVal.equals("")) ? " = " + initVal : "") + ";\n";
		return(code);
	}
	private static String declarePositiveDecl(DeclareVariable var, ValueMapPath path)
	{
		if(var instanceof DeclarePositive)
			return(declareVariableDecl("double", var, path));
		else
			return(StringHelper.emptyString);
	}	
	private static String declarePositiveIntegerDecl(DeclareVariable var, ValueMapPath path)
	{
		if(var instanceof DeclarePositiveInteger)
			return(declareVariableDecl("int", var, path));
		else
			return(StringHelper.emptyString);
	}
	private static String declareSwitchableDecl(DeclareVariable var, ValueMapPath path)
	{
		String code = "";
		if(var instanceof DeclareSwitchable)
		{
			DeclareSwitchable ds = (DeclareSwitchable) var;
			String varName;
			varName = StringHelper.javaName(path.substitute(ds.getName()));
			code += "\tboolean " + varName + " = " + ds.getState() + ";\n";
		}
		return(code);
	}
	private static String declareSwitchablePositiveDecl(DeclareVariable var, ValueMapPath path)
	{
		String code = "";
		if(var instanceof DeclareSwitchablePositive)
		{
			DeclareSwitchablePositive dsp = (DeclareSwitchablePositive) var;
			String varName;
			varName = StringHelper.javaName(path.substitute(dsp.getName()));
				
			code += "\tSwitchablePositive " + varName + " = new SwitchablePositive();\n";
			if(! dsp.getInitialValue().trim().equals(""))
				code += "\t" + varName + ".setValue(" + dsp.getInitialValue() + ");\n";

			code += "\t" + varName + ".setState(" + dsp.getState() + ");\n";
		}
		return(code);
	}
	private static String tcapStringDecl(DeclareVariable var, ValueMapPath path)
	{
		if(var instanceof TcapString)
		{
			return(declareVariableDecl("String", var, path));
		}
		else
			return(StringHelper.emptyString);
	}
		private static String getBooleanExpression(SingleParameter singleParam, ValueMapPath path) {
				EnumDefinition enumDefinition = null;
				String className = null;
				String enumName = null;
				String levelName = null;
				StringBuilder expr = new StringBuilder();
				if ( singleParam instanceof LevelSingleParameter ) {
						// Get the parameter that this single param is a part of
						Parameter param = singleParam.getParameter();
						System.out.println("PARAMETER " + param);
						if ( param instanceof TwoLevelParameter ) {
								// get the EnumDefinition to build the string
								enumDefinition = ((TwoLevelParameter)param).getEnumDefinition();
								if ( enumDefinition != null ) {
										 if ( enumDefinition.getProcess() != null ) 
												 className = path.substituteJavaName(enumDefinition.getProcess().getName());
										 enumName = path.substituteJavaName(enumDefinition.getVariableName());
										 levelName = path.substituteJavaName(((LevelSingleParameter)singleParam).getDisplayValue());
//										 ((Cancer_cell) getContainerInstance(Cancer_cell.class))_p53 == CancerCell.p53.level
										 expr.append("((");
										 expr.append(className);
										 expr.append(") getContainerInstance(");
										 expr.append(className);
										 expr.append(".class))._");
										 expr.append(enumName);
										 expr.append(" == ");
										 expr.append(className);
										 expr.append(".");
										 expr.append(enumName);
										 expr.append(".");
										 expr.append(levelName);
								}
						}
						return expr.toString();
				}
				else if ( singleParam instanceof CharacteristicSingleParameter) {
						return ((LevelSingleParameter)singleParam).getDisplayName();
				}
				else if ( singleParam instanceof SubsetParameter ) {
						return ((SubsetParameter)singleParam).subsetFilter.getBooleanValue();
				}
				return expr.toString();
		}

		private static String getAssignmentExpression(SingleParameter singleParam, ValueMapPath path) {
				EnumDefinition enumDefinition = null;
				String className = null;
				String enumName = null;
				String levelName = null;
				StringBuilder expr = new StringBuilder();
				if ( singleParam instanceof LevelSingleParameter ) {
						// Get the parameter that this single param is a part of
						Parameter param = singleParam.getParameter();
//						System.out.println("PARAMETER " + param);
						if ( param instanceof TwoLevelParameter ) {
								// get the EnumDefinition to build the string
								enumDefinition = ((TwoLevelParameter)param).getEnumDefinition();
								if ( enumDefinition != null ) {
										 if ( enumDefinition.getProcess() != null ) 
										 	className = path.substituteJavaName(enumDefinition.getProcess().getName());
							 			 enumName = path.substituteJavaName(enumDefinition.getVariableName());
										 levelName = path.substituteJavaName(((LevelSingleParameter)singleParam).getDisplayValue());
										 expr.append(className);
										 expr.append(".");
										 expr.append(enumName);
										 expr.append(".");
										 expr.append(levelName);
								}
						}
						return expr.toString();
				}
				else if ( singleParam instanceof CharacteristicSingleParameter) {
						return ((LevelSingleParameter)singleParam).getDisplayName();
				}
				else if ( singleParam instanceof SubsetParameter ) {
						return ((SubsetParameter)singleParam).subsetFilter.getBooleanValue();
				}
				return expr.toString();
		}
		
		// TODO: Don't forget the Conditional value
		private static String getConditionalTableParameter(ConditionalTableParameter ctp, ValueMapPath path, String var){
			EnumDefinition condEnumDef = (EnumDefinition)ctp.getConditionValue();
			EnumDefinition enumDef = null;
			String condEnumDefString = condEnumDef.getInstanceString(path);
			String numDefString = null;
			String enumLevelString = null;
			StringBuilder code = new StringBuilder();
			StateMatrix stateMatrix = ctp.getStateMatrix();
			// numRows represents the number of possible outcomes # of conditional statements
			int numRows = stateMatrix.getNumberRows();
			// Get each function - each function represents a separate "conditional"
			boolean firstIf = true;
			for ( Object fn : ctp.getFunction()){
				ConditionalDiscreteStateFunction cdsf = (ConditionalDiscreteStateFunction)fn;
				DefaultPersistibleList outs = cdsf.getOutcomes();
				StateMatrixRow headingRow = stateMatrix.getStateMatrixRowHeading();
				//num cols represents the number of statements in the conditional statement
				// the number of && plus 1
				int numCols = headingRow.getNumberOfColumns();
				DefaultPersistibleList stateMatrixRows = stateMatrix.getStateMatrixRows();
				// Each row represents a conditional statement and the out come
				int rowCount = 0;
//				if ( numRows > 0 )
//				code.append("(");
				
				EnumLevel enumLevel = (EnumLevel)cdsf.getConditionValue(); // enum level
				if ( enumLevel != null )
					enumLevelString = condEnumDef.getInstanceLevelString(path, enumLevel);
				for (Object obj : stateMatrixRows){
					StateMatrixRow row = (StateMatrixRow)obj;
					
					if ( !row.getIsHeading() ) {
						if ( firstIf == false ) {
							code.append("\n\t\telse "); // close if
						}
						else {
							code.append("\n\t");
							firstIf = false;
						}
						//If a conditional exists prepend  it  here
						// ex. (ATM == EXPRESSED && bcl-2 == FUNCTIONAL)
						//code.append("("); // open if
						code.append("if (");
						if ( enumLevelString != null ) {
							code.append(condEnumDefString);
							code.append("==");
							code.append(enumLevelString);
							if ( stateMatrixRows != null && stateMatrixRows.size() > 0 )
								code.append("\n\t\t && ");
						}
						
						for ( int i = 0; i < numCols; i++ ) {
							enumDef = null;
							if ( headingRow.getRowColumn(i) instanceof EnumDefinition){
								enumDef = (EnumDefinition)headingRow.getRowColumn(i);
								code.append(((EnumDefinition)headingRow.getRowColumn(i)).getInstanceString(path));
							}
							else 
								code.append(headingRow.getRowColumn(i).toString());
							code.append("==");
							if (enumDef != null && row.getRowColumn(i) instanceof EnumLevel)
								code.append(enumDef.getInstanceLevelString(path, (EnumLevel)row.getRowColumn(i)));
							else 
								code.append(row.getRowColumn(i).toString());
							
							
							if ( i+1 < numCols)
								code.append("\n&&");
						}
						//code.append("?");
						code.append(" ) \n");
						if ( outs.elementAt(rowCount) != null ){
							code.append("\t\t");
							code.append(var);
							code.append("=");
							code.append(outs.elementAt(rowCount).toString());
							code.append(";\n");
						}
						else
							code.append("not defined");
						
						rowCount++;
					}
					
				}
			}
			if ( numRows > 0 ) {
				//code.append(")");
			code.append("\n");
				
		
			}
			return code.toString();
		}
		
		private static String getProbabilityArray(Collection probabilities, ValueMapPath valueMapPath, int firstEnumIndex){
			StringBuilder strCode = new StringBuilder();
			double probArray[] = (double[])Array.newInstance(double.class, probabilities.size());
			double probSum = 0.0;
			int probCount = 0;
			strCode.append("\t\tdouble probArray");
			strCode.append(nNewProcesses);
			strCode.append("[] = { ");
			//double probArray36[] = { .5d, .3d, .1d, 1.0d-(.5d+.3d+.1d)}
			//OncEnum [][]      enumArray45 = { enum38, enum41, enum44, enum47 }
			for ( Object probObject : probabilities) {
				probArray[probCount] = translateProbabilityValue(probObject, valueMapPath);
				strCode.append(String.valueOf(probArray[probCount]));
				if ( probCount < probabilities.size()-1)
					strCode.append(",");
				probCount++;
			}
			strCode.append("};\n");
			strCode.append("\t\tOncEnum [][] enumArray");
			strCode.append(nNewProcesses);
			strCode.append(" = { ");
			for ( int i = 0; i < probCount; i++){
				strCode.append("enums");
				strCode.append(firstEnumIndex+i);
				if ( i < probCount - 1)
					strCode.append(",");
			}
			strCode.append("};\n");
			
			return strCode.toString();
		}
		// Convert value to double - this may be backquoted string also
		private static double translateProbabilityValue(Object probObject, ValueMapPath valueMapPath){
			String value = null;
			Double doubleValue = null;
			// Is this a backquoted string
			if ( StringHelper.isBackquoteRef(probObject.toString()) ) {
				SingleParameter param = valueMapPath.getSingleParameter(probObject.toString().substring(1,probObject.toString().length()-1));
				value = param.getCodeValue();
			}
			else
				value = probObject.toString();
			try {
				doubleValue = (Double.parseDouble(value));
			} catch( Exception pe ){
				System.out.println("Conditional Table parameter" + probObject + " cannot be converted to a double.");
			}
			if (doubleValue > 1) {
				System.out.println("Conditional Table parameter" + probObject + " is greater than 1");
				return -1d;
			}
			return doubleValue;
		}
		
		private static String initOncEnums(int numProbs, InstantiateAction iAction, ConditionalDiscreteStateFunction cdsf) {
			StringBuilder strCode = new StringBuilder();
			for ( int iDist = 0; iDist <  numProbs; iDist++){
				StateMatrixRow headings = (StateMatrixRow)cdsf.getStateMatrix().getStateMatrixRows().elementAt(0);
				StateMatrixRow levels = (StateMatrixRow)cdsf.getStateMatrix().getStateMatrixRows().elementAt(iDist+1); // first row is heading
				//System.out.println("LEVELS " + iDist + " " + levels);
				StringBuilder enumString = new StringBuilder();
				enumString.append("\t\tOncEnum [] enums");
				enumString.append(String.valueOf(nNewProcesses++));
				enumString.append(" ={ ");
				for ( int j = 0; j < levels.getNumberOfColumns(); j++){
					enumString.append(StringHelper.javaName(headings.getRowColumn(j).toString()));
					enumString.append(".");
					enumString.append(StringHelper.javaName(levels.getRowColumn(j).toString()));
					if ( j < levels.getNumberOfColumns()-1 )
						enumString.append(", ");
				}
				enumString.append(" };\n");
				//System.out.println("ENUM " + enumString);
				strCode.append(enumString);
			}
			return strCode.toString();
		}
		private static String getConditionalInstantiationCode(InstantiateAction iAction, ValueMapPath path) {
			StringBuilder strCode = new StringBuilder();
			strCode.append("\t\tdouble distribution");
			strCode.append(nNewProcesses);
			strCode.append("[] = Gbinomi.getMulti(");
			strCode.append(getNumNewProcesses(path,iAction));
			strCode.append(", probArray");
			strCode.append(nNewProcesses);
			strCode.append(", getRNG());\n");
			strCode.append("\t\tfor ( int distIndex");
			strCode.append(nNewProcesses);
			strCode.append("= 0; distIndex");
			strCode.append(nNewProcesses);
			strCode.append("< Array.getLength(distribution");
			strCode.append(nNewProcesses);
			strCode.append("); distIndex");
			strCode.append(nNewProcesses);
			strCode.append("++) {\n");
			
			strCode.append("\t\t\tfor ( int numProcs");
			strCode.append(nNewProcesses);
			strCode.append("= 0; numProcs");
			strCode.append(nNewProcesses);
			strCode.append("< distribution");
			strCode.append(nNewProcesses);
			strCode.append("[distIndex");
			strCode.append(nNewProcesses);
			strCode.append("]");
			strCode.append("; numProcs");
			strCode.append(nNewProcesses);
			strCode.append("++) {\n");
			
			String procName = "process" + nNewProcesses;
		
			strCode.append("\t\t\t"); 
			strCode.append(StringHelper.javaName(iAction.getInitializationProcessDeclaration().getName()));
			strCode.append(" ");
			strCode.append(procName);
			strCode.append(" = new ");
			strCode.append(StringHelper.javaName(iAction.getInitializationProcessDeclaration().getName()));
			strCode.append("(this, enumArray");
			strCode.append(nNewProcesses);
			strCode.append("[distIndex");
			strCode.append(nNewProcesses);
			strCode.append("]);\n");
			
//			 Set count 
			strCode.append("\t\tprocess");
			strCode.append(nNewProcesses);
			strCode.append(".setCount(1);\n");
			
			strCode.append("\t\t\tprocesses.put("); 
			strCode.append(StringHelper.javaName(iAction.getInitializationProcessDeclaration().getName()));
			strCode.append(".class, process"); 
			strCode.append(nNewProcesses); 
			strCode.append(");\n\n");
//			 Initialize Variables  - for each new process
			strCode.append(getVarInitializations( iAction,  path));
			strCode.append("\t\t\t}\n");
			strCode.append("\t\t}\n");
			
			nNewProcesses++;

			System.out.println(strCode.toString());
			return strCode.toString();
		}
		private static String getConditionalInstantiationCodeAggregate(InstantiateAction iAction, ValueMapPath path) {
			String className = StringHelper.javaName(iAction.getInitializationProcessDeclaration().getName());
			StringBuilder strCode = new StringBuilder();
			strCode.append("\t\tdouble distribution");
			strCode.append(nNewProcesses);
			strCode.append("[] = Gbinomi.getMulti(");
			strCode.append(getNumNewProcesses(path,iAction));
			strCode.append(", probArray");
			strCode.append(nNewProcesses);
			strCode.append(", getRNG());\n");
			
			// Declare process
			String procName = "process" + nNewProcesses;
			strCode.append("\t\t"); 
			strCode.append(className);
			strCode.append(" ");
			strCode.append(procName);
			strCode.append(" = null;\n ");
			
			strCode.append("\t\tfor ( int distIndex");
			strCode.append(nNewProcesses);
			strCode.append("= 0; distIndex");
			strCode.append(nNewProcesses);
			strCode.append("< Array.getLength(distribution");
			strCode.append(nNewProcesses);
			strCode.append("); distIndex");
			strCode.append(nNewProcesses);
			strCode.append("++) {\n");
			
			strCode.append("\t\tVector matchingProcesses");
			strCode.append(nNewProcesses);
			strCode.append(" = getMatchingOncProcesses(");
			strCode.append(className);
			strCode.append(".class, enumArray");
			strCode.append(nNewProcesses);
			strCode.append("[distIndex");
			strCode.append(nNewProcesses);
			strCode.append("]);\n");
			
			// If there exists a process with these characteristics
			strCode.append("\t\tif (matchingProcesses");
			strCode.append(nNewProcesses);
			strCode.append(" != null && ");
			strCode.append("matchingProcesses");
			strCode.append(nNewProcesses);
			strCode.append(".size() > 0) {\n");
			
			strCode.append("\t\tprocess");
			strCode.append(nNewProcesses);
			strCode.append(" = ");
			strCode.append("(");
			strCode.append(className);
			strCode.append(")matchingProcesses");
			strCode.append(nNewProcesses);
			strCode.append(".get(0);\n");
			
			//increment count
			strCode.append("\t\tprocess");
			strCode.append(nNewProcesses);
			strCode.append(".incrementCount((int)distribution");
			strCode.append(nNewProcesses);
			strCode.append("[distIndex");
			strCode.append(nNewProcesses);
			strCode.append("]);\n");
			
			strCode.append("\t\t}\n");
			
			strCode.append("\t\telse {\n");

			
			//Instantiate process 
			strCode.append("\t\t\t"); 
			//strCode.append(className);
			strCode.append(" ");
			strCode.append(procName);
			strCode.append(" = new ");
			strCode.append(className);
			strCode.append("(this, enumArray");
			strCode.append(nNewProcesses);
			strCode.append("[distIndex");
			strCode.append(nNewProcesses);
			strCode.append("]);\n");
			
			// Set count 
			strCode.append("\t\tprocess");
			strCode.append(nNewProcesses);
			strCode.append(".setCount((int)distribution");
			strCode.append(nNewProcesses);
			strCode.append("[distIndex");
			strCode.append(nNewProcesses);
			strCode.append("]);\n");
			
			strCode.append("}\n"); // close else block
			
			strCode.append("\t\t\tprocesses.put("); 
			strCode.append(className);
			strCode.append(".class, process"); 
			strCode.append(nNewProcesses); 
			strCode.append(");\n\n");
//			 Initialize Variables  - for each new process
			strCode.append(getVarInitializations( iAction,  path));
			strCode.append("\t\t}\n");
			
			nNewProcesses++;

			System.out.println(strCode.toString());
			return strCode.toString();
		}
		
		private static int getNumNewProcesses(ValueMapPath path, InstantiateAction iAction){
		//		 How many instances of this Class should be instantiated
				int numInstances = 1; // default value if not set
				String numInstancesString = iAction.getNumNewProcesses();
				String backquotedValue = null;
				// If this is a backquoted string make sure it can be converted to a number
				if ( StringHelper.isBackquoteRef(numInstancesString) ) {
					SingleParameter param = path.getSingleParameter(numInstancesString.substring(1,numInstancesString.length()-1));
					backquotedValue = param.getCodeValue();
				}
				else
					backquotedValue = numInstancesString;
				try {
					numInstances = (Integer.parseInt(backquotedValue));
				} catch( Exception pe ){
					System.out.println("Conditional Table parameter" +  numInstancesString + " cannot be converted to a int.");
				}
				return numInstances;
			}
		
}

	