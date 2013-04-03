package oncotcap.datalayer.persistible.parameter;

import oncotcap.datalayer.persistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.engine.ValueMapPath;

public class DeclareSwitchablePositive extends AbstractParameter implements DeclareVariable
{
	private DeclareVariableHelper dVHelper = new DeclareVariableHelper();
	private Boolean state = null;
	public boolean initialState = false;
	private boolean setOnce = false;
	private DefaultSingleParameter positiveSingleParam;
	private DefaultSingleParameter switchSingleParam;

	public DeclareSwitchablePositive(oncotcap.util.GUID guid){
		super(guid);
	}
	public DeclareSwitchablePositive()
	{
		this(true);
	}
	public DeclareSwitchablePositive(boolean saveToDataSource)
	{
		super(saveToDataSource);
	}
	private DeclareSwitchablePositive(DeclareSwitchablePositive sp, boolean saveToDataSource)
	{
		super(saveToDataSource);
		if(sp.getValue() != null)
			setValue(sp.getValue());
		if(sp.state != null)
			setState(sp.state.booleanValue());
		setInitialState(sp.getInitialState());
	}
	public void setValue(String val)
	{
		if(positiveSingleParam == null)
			initSingleParams();
		
		if(val != null)
		{
			String tVal;
			dVHelper.setValue((tVal = new String(val)));
			positiveSingleParam.setDisplayValue(tVal);
		}
		else
		{
			dVHelper.setValue(null);
			positiveSingleParam.setDisplayValue("");
		}
		update();
	}
	public void setValue(float val)
	{
		setValue(Float.toString(val));
	}
	public void setValue(Float val)
	{
			setValue(val.toString());
	}
	public String getValue()
	{
		return(dVHelper.getValue());
	}
	public boolean check()
	{
		return(true);
	}
	public String getStringValue()
	{
		return(getValue());
	}
	public void setState(boolean state)
	{
		if(this.state == null)
		{
			  this.state = new Boolean(state);
		}
		else if( (!setOnce) && ! (this.state.booleanValue() && state) )
		{
			this.state = new Boolean(state);
			setOnce = true;
		}
		if(switchSingleParam == null)
			initSingleParams();
		
		switchSingleParam.setDisplayValue(this.state.toString());
		update();
	}
	public boolean getState()
	{
		if(state != null)
			return(state.booleanValue());
		else
			return(false);
	}
	public boolean getInitialState()
	{
		return(initialState);
	}
	public void setInitialState(boolean state)
	{
		initialState = state;
		update();
	}
	public VariableType getType()
	{
		return(VariableType.SWITCHABLE_POSITIVE);
	}
	public void setSwitchDisplayName(String name)
	{
		switchSingleParam.setDisplayName(name);
	}
	public String getSwitchDisplayName()
	{
		return(switchSingleParam.getDisplayName());
	}
	public void setPositiveDisplayName(String name)
	{
		positiveSingleParam.setDisplayName(name);
	}
	public String getPositiveDisplayName()
	{
		return(positiveSingleParam.getDisplayName());
	}
	public EditorPanel getEditorPanel()
	{
		return(new ParameterEditor());
	}
	public EditorPanel getEditorPanelWithInstance()
	{
		EditorPanel ep = new ParameterEditor();
		ep.edit(this);
		return(ep);
	}
	public EditorPanel getParameterEditorPanel()
	{
		return(new SwitchablePositiveEditor());
	}
	public EditorPanel getParameterEditorPanelWithInstance()
	{
		EditorPanel ep = new SwitchablePositiveEditor();
		ep.edit(this);
		return(ep);
	}

	public Object clone()
	{
		return(clone(true));
	}
	public Parameter clone(boolean saveToDataSource)
	{
		DeclareSwitchablePositive rVal = new DeclareSwitchablePositive(this, saveToDataSource); 
		rVal.setOriginalSibling(getOriginalSibling());
		return(rVal);
	}
/*	public Object cloneSubstitute(StatementBundle sb)
	{
		DeclareSwitchablePositive dsp = new DeclareSwitchablePositive(false);
		dsp.initialState = initialState;
		dsp.setName(sb.substitute(getName()));
		dsp.setInitialState(getInitialState());
		dsp.setState(getState());
		dsp.setValue(sb.substitute(getValue()));
		dsp.setPositiveDisplayName(sb.substitute(getPositiveDisplayName()));
		dsp.setSwitchDisplayName(sb.substitute(getSwitchDisplayName()));
		return(dsp);
	}*/
	public ParameterType getParameterType()
	{
		return(ParameterType.SWITCHABLEPOSITIVE);
	}
	public void initSingleParams()
	{
		positiveSingleParam = new DefaultSingleParameter("Positive Value", this, "SwitchablePositive.Float");
		//positiveSingleParam.setDisplayValue("0.0");
		switchSingleParam = new DefaultSingleParameter("Switch", this, "SwitchablePositive.Switch");
		//switchSingleParam.setDisplayValue("true");
		singleParameterList.add(positiveSingleParam);
		singleParameterList.add(switchSingleParam);
	}
	public void addSingleParameter(SingleParameter sp)
	{
		if(sp.getSingleParameterID().equals("SwitchablePositive.Float"))
		{
			singleParameterList.replace(positiveSingleParam, sp);
			positiveSingleParam = (DefaultSingleParameter) sp;
		}
		else if(sp.getSingleParameterID().equals("SwitchablePositive.Switch"))
		{
			singleParameterList.replace(switchSingleParam, sp);
			switchSingleParam = (DefaultSingleParameter) sp;
		}
	}

/*	public void writeDeclarationSection(Writer writer, StatementBundle sb) throws IOException
	{
		String varName;
		if(sb != null)
			varName = StringHelper.javaName(sb.substitute(getName()));
		else
			varName = StringHelper.javaName(getName());
			
		writer.write("\tSwitchablePositive " + varName + " = new SwitchablePositive();\n");
		if(! getInitialValue().trim().equals(""))
			writer.write("\t" + varName + ".setValue(" + getInitialValue() + ");\n");
		if(state != null)
			writer.write("\t" + varName + ".setState(" + state + ");\n");
	}*/
	public int equalDeclaration(DeclareVariable obj, ValueMapPath compPath, ValueMapPath myPath)
	{
		if(! (obj instanceof DeclareSwitchablePositive))
			return(DeclareVariable.DECLARATION_NOT_EQUAL);
		else
		{
			int varStat = DeclareVariableHelper.equalDeclarations(this, myPath, obj, compPath);
			if(varStat == DeclareVariable.DECLARATION_CONFLICTS)
				return(DeclareVariable.DECLARATION_CONFLICTS);
			else if(varStat == DeclareVariable.DECLARATION_NOT_EQUAL)
				return(DeclareVariable.DECLARATION_NOT_EQUAL);
			else
			{
				if(((DeclareSwitchablePositive) obj).getInitialState() == getInitialState())
					return(DeclareVariable.DECLARATION_EQUAL);
				else
					return(DeclareVariable.DECLARATION_CONFLICTS);
			}
		}
	}
	public void setStatementBundleUsingMe(StatementBundle sb)
	{
		dVHelper.setStatementBundleUsingMe(sb);
	}
	public StatementBundle getStatementBundleUsingMe()
	{
		return(dVHelper.getStatementBundleUsingMe());
	}
/*	public void writeMethodSection(Writer write, StatementBundle sb) throws IOException
	{

	}*/
	public String getDisplayString()
	{
		return(dVHelper.getDisplayString());
	}
	public String getName()
	{
		return(dVHelper.getName());
	}
	public void setName(String name)
	{
		dVHelper.setName(name);
	}
	public void setInitialValue(String val)
	{
		dVHelper.setInitialValue(val);
	}
	public String getInitialValue()
	{
		return(dVHelper.getInitialValue());
	}
	public String toString()
	{
		return(getName());
	}

}
