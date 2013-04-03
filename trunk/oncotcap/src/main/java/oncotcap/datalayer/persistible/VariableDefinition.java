package oncotcap.datalayer.persistible;

import java.util.Collection;
import java.util.Vector;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.util.*;
import oncotcap.engine.*;

public class VariableDefinition extends AbstractPersistible implements VariableInstance
{
	protected ProcessDeclaration processDef;
	protected String varType;
	protected String varName;
	protected String defaultValue;
	protected Parameter param;
	protected boolean isArray = false;
	private boolean isNonID = false;
	private boolean isID = false;
	private boolean fromPropFile = false;
	
	public VariableDefinition(oncotcap.util.GUID guid){
		super(guid);
		init("Undefined Variable", "Variable", false);
	}
	public VariableDefinition(oncotcap.util.GUID guid,
				  String variableName, String variableType)
	{	super(guid);
		init(variableName, variableType, false);
	}
	public VariableDefinition()
	{
		this(true);
	}
	public VariableDefinition(boolean saveToDataSource)
	{
		if(!saveToDataSource)
			setPersistibleState(Persistible.DO_NOT_SAVE);
		init("Undefined Variable", "Variable", false);
	}
	public VariableDefinition(String variableName, String variableType)
	{
		init(variableName, variableType, false);
	}
	public VariableDefinition(String variableName, String variableType, boolean isArray)
	{
		init(variableName, variableType, isArray);
	}
	private void init(String variableName, String variableType, boolean isArray) {
		
		varType = variableType;
		varName = variableName;
		this.isArray = isArray;
	}
/*	public Object cloneSubstitute(StatementBundle sb)
	{
		VariableDefinition vd = new VariableDefinition(false);
		vd.varType = sb.substitute(varType);
		vd.varName = sb.substitute(varName);
		vd.defaultValue = sb.substitute(defaultValue);
		vd.isArray = isArray;
		return(vd);
	}*/
	public boolean isID()
	{
		return(isID);
	}
	public void setIsID(boolean isID)
	{
		if(isNonID() && isID)
			System.out.println("WARNING: Cannot set Enum " + this.getName() + " as an ID Enum because it is already set as a Non-ID Enum.");
		else
			this.isID = isID;
	}
	public void setIsID(Boolean isID)
	{
		if(isID == null)
			setIsID(false);
		else
			setIsID(isID.booleanValue());
	}
	public boolean isNonID()
	{
		return(isNonID);
	}
	public void setIsNonID(boolean isNonID)
	{
		if(isID() && isNonID)
			System.out.println("WARNING: Cannot set Enum " + this.getName() + " as a Non-ID Enum because it is already set as an ID Enum.");
		else
			this.isNonID = isNonID;
	}
	public void setIsNonID(Boolean isNonID)
	{
		if(isNonID == null)
			setIsNonID(false);
		else
			setIsNonID(isNonID.booleanValue());
	}
	public boolean isFromPropFile()
	{
		return(fromPropFile);
	}
	public boolean isFromPropFileAsBoolean()
	{
		return(new Boolean(fromPropFile));
	}
	public void setIsFromPropFile(boolean fromProp)
	{
			this.fromPropFile = fromProp;
	}
	public void setIsFromPropFile(Boolean fromProp)
	{
		if(fromProp == null)
			setIsFromPropFile(false);
		else
			setIsFromPropFile(fromProp.booleanValue());
	}
	public boolean isArray()
	{
		return(isArray);
	}
	public Boolean isArrayAsBoolean()
	{
		return(new Boolean(isArray));
	}
	public void setArray(boolean isArray)
	{
		this.isArray = isArray;
		update();
	}
	public void setArray(Boolean isArray)
	{
		setArray(isArray.booleanValue());
	}
	public String getName()
	{
		if(varName != null)
			return(varName);
		else
			return("");
	}
	public String getVariableName()
	{
		return(getName());
	}
	public String getClassName()
	{
		return(StringHelper.javaName(getName()));
	}
	public void setName(String name)
	{
		varName = name;
		update();
	}
	public String getType()
	{
		return(varType);
	}
	public void setType(String type)
	{
		varType = type;
	}
/*	public Class getTypeClass()
	{
		return(ReflectionHelper.classForName(varType));
	}*/
	public Parameter getParametersContainingMe()
	{
		return(param);
	}
	public void setParametersContainingMe(Parameter param)
	{
		this.param = param;
		update();
	}

	public String toString()
	{
		return(getName());
	}
	public ProcessDeclaration getProcess()
	{
		return(processDef);
	}
	public void setProcess(ProcessDeclaration def)
	{
		processDef = def;
		update();
	}
	public String getDefault()
	{
		return(defaultValue);
	}
	public void setDefault(String val)
	{
		defaultValue = val;
		update();
	}

	public EditorPanel getDeclarationEditor()
	{
		return(VariableType.getDeclarationEditor(varType));
	}
	public DeclareVariable getDeclarationInstance()
	{
		DeclareVariable dv = VariableType.getDeclarationInstance(varType);
		if(dv instanceof DeclareEnum)
		{
			((DeclareEnum)dv).setIsNonID(this.isNonID);
			((DeclareEnum)dv).setIsID(this.isID);
		}
		return(dv);
	}
	public boolean equals(Object obj)
	{
		boolean rVal = true;
		
		if(obj instanceof VariableDefinition)
		{
			VariableDefinition def = (VariableDefinition) obj;
			if(processDef != null)
				rVal = processDef.equals(def.processDef);
			if(varType != null)
				rVal &= varType.equals(def.varType);
			if(varName != null)
				rVal &= varName.equals(def.varName);			
		}
		else
			rVal = false;

		return(rVal);
	}
	public boolean equals(InstructionAndValues iav, ValueMapPath myPath)
	{
		boolean rVal = true;
		
		if(iav.getInstruction().getEnclosingInstructionProvider() instanceof VariableDefinition)
		{
			ValueMapPath compPath = iav.getValues();
			VariableDefinition def = (VariableDefinition) iav.getInstruction().getEnclosingInstructionProvider();
			if(processDef != null)
				rVal = processDef.equals(def.processDef);
			if(varType != null)
				rVal &= myPath.substitute(varType).equals(compPath.substitute(def.varType));
			if(varName != null)
				rVal &= myPath.substitute(varName).equals(compPath.substitute(def.varName));			
		}
		else
			rVal = false;

		return(rVal);
	}
	public int hashCode()
	{
		int rHash = 0;
		if(processDef != null)
			rHash = processDef.hashCode();
		if(varType != null)
			rHash = Hash.nextHash(rHash, varType.hashCode());
		if(varName != null)
			rHash = Hash.nextHash(rHash, varName.hashCode());
		
		return(rHash);
	}
	//implement CodeProvider interface
	//private static final Vector emptyCollection = new Vector();
//	private static final CodeSection blankCodeSection = CodeSection.blankCodeSection;
/*	public Collection getDeclarationSections()
	{
		return(emptyCollection);
	}
	public Collection getMethodSections()
	{
		return(emptyCollection);
	}
	public Collection getActionList()
	{
		return(emptyCollection);
	}
	public ProcessDeclaration getProcessDeclaration()
	{
		return(getProcess());
	}
	public int getEventType()
	{
		return(CodeBundle.METHOD);
	}
	public MethodDeclaration getMethodDeclaration()
	{
		return(null);
	}
	public EventDeclaration getEventDeclaration()
	{
		return(null);
	}	*/
}
