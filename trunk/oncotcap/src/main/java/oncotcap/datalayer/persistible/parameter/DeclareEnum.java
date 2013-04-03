package oncotcap.datalayer.persistible.parameter;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.engine.*;

public class DeclareEnum extends AbstractIndividualParameter implements DeclareVariable
{
	DeclareVariableHelper dVHelper = new DeclareVariableHelper();
	private boolean isNonID = false;
	private boolean isID = false;
	private boolean fromPropFile = false;
	
	public DeclareEnum(oncotcap.util.GUID guid){
		super(guid);
	}
	public DeclareEnum()
	{
		this(true);
	}
	public DeclareEnum(boolean saveToDataSource)
	{
		super(saveToDataSource);
	}
	private DeclareEnum(DeclareEnum enum1, boolean saveToDataSource)
	{
		super(saveToDataSource);
		if(enum1.getValue() != null)
			setValue(new String(enum1.getValue()));
		dVHelper = (DeclareVariableHelper) enum1.dVHelper.clone();
		this.isID = enum1.isID;
		this.isNonID = enum1.isNonID;
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
	public boolean isNonID()
	{
		return(isNonID);
	}
	public Boolean isNonIDAsBoolean()
	{
		return(new Boolean(isNonID));
	}
	public void setIsNonID(boolean isNonID)
	{
		this.isNonID = isNonID; 
	}
	public void setIsNonID(Boolean isNonID)
	{
		if(isNonID == null)
			setIsNonID(false);
		else
			setIsNonID(isNonID.booleanValue());
	}
	public boolean isID()
	{
		return(isID);
	}
	public Boolean isIDAsBoolean()
	{
		return(new Boolean(isID));
	}
	public void setIsID(boolean isID)
	{
		this.isID = isID;
	}
	public void setIsID(Boolean isID)
	{
		if(isID == null)
			setIsID(false);
		else
			setIsID(isID.booleanValue());
	}

	public void setValue(String val)
	{
		dVHelper.setValue(val);
		update();
	}
	public String getStringValue()
	{
		return(getValue());
	}
	public String getValue()
	{
		return(dVHelper.getValue());
	}
	public void setDisplayValue(Object val)
	{
		if(val instanceof String)
			setValue((String) val);		
	}
	public String getDisplayValue()
	{
		return(getValue());
	}
	public String getCodeValue()
	{
		return(getValue());
	}
	public boolean check()
	{
		return(true);
	}
	public VariableType getType()
	{
		return(VariableType.ENUM);
	}
	public EditorPanel getEditorPanel()
	{
		return(new EnumEditor());
	}
	public EditorPanel getEditorPanelWithInstance()
	{
		EditorPanel ep = new EnumEditor();
		ep.edit(this);
		return(ep);
	}
	public EditorPanel getParameterEditorPanel()
	{
		return(new EnumEditor());
	}
	public EditorPanel getParameterEditorPanelWithInstance()
	{
		EditorPanel ep = new EnumEditor();
		ep.edit(this);
		return(ep);
	}

	public Object clone()
	{
		return(clone(true));
	}
	public Parameter clone(boolean saveToDataSource)
	{
		DeclareEnum rVal = new DeclareEnum(this, saveToDataSource); 
		rVal.setOriginalSibling(getOriginalSibling());
		return(rVal);
	}
/*	public Object cloneSubstitute(StatementBundle sb)
	{
		DeclareEnum de = new DeclareEnum(false);
		de.setValue(sb.substitute(getValue()));
		de.setName(sb.substitute(getName()));
		de.setInitialValue(sb.substitute(getInitialValue()));
		return(de);
	}*/
	public ParameterType getParameterType()
	{
		return(null);
	}
	public void initSingleParams()
	{
		setDefaultName("Level Value");
		setSingleParameterID("Enum.Level");
		singleParameterList.add(this);
	}

/*	public void writeDeclarationSection(Writer writer, StatementBundle sb) throws IOException
	{

	}
	public void writeMethodSection(Writer writer, StatementBundle sb) throws IOException
	{
	}*/
	public String getName()
	{
		return(dVHelper.getName());
	}
	public void setName(String name)
	{
		dVHelper.setName(name);
		update();
	}
	public void setInitialValue(String val)
	{
//			System.out.println("setInitialValue " + val);
		dVHelper.setInitialValue(val);
		update();
	}
	public String getInitialValue()
	{
		return(dVHelper.getInitialValue());
	}
	public String getDisplayString()
	{
		return(dVHelper.getDisplayString());
	}
	public int equalDeclaration(DeclareVariable obj, ValueMapPath compPath, ValueMapPath myPath)
	{
		return(DeclareVariableHelper.equalDeclarations(this, myPath, obj, compPath));
	}
	public void setStatementBundleUsingMe(StatementBundle sb)
	{
		dVHelper.setStatementBundleUsingMe(sb);
	}
	public StatementBundle getStatementBundleUsingMe()
	{
		return(dVHelper.getStatementBundleUsingMe());
	}
	public String toString()
	{
		return(getName());
	}
}
