package oncotcap.datalayer.persistible.parameter;

import oncotcap.datalayer.persistible.*;
import oncotcap.util.OperatorHelper;
import oncotcap.engine.ValueMapPath;

public class DeclareVariableHelper
{
	private String name;
	private String initialValue;
	private String value;
	private StatementBundle statementBundleUsingMe;
	public Object clone()
	{
		DeclareVariableHelper newDVH = new DeclareVariableHelper();
		if(name != null)
			newDVH.name = new String(name);
		if(initialValue != null)
			newDVH.initialValue = new String(initialValue);
		if(value != null)
			newDVH.value = new String(value);
		newDVH.statementBundleUsingMe = statementBundleUsingMe;
		return(newDVH);
	}
	public void setInitialValue(String initVal)
	{
			initialValue = initVal;
			//update();
	}
	public String getInitialValue()
	{
		return(initialValue);
	}

	public void setName(String name)
	{
			this.name = name;
			//update();
	}
	public String getName()
	{
		if(name != null)
			return(name);
		else
			return("");
	}
	public String toString()
	{
		return(name);
	}
	public String getDisplayString()
	{
		return(toString());
	}
	public static int equalDeclarations(Object obj1, ValueMapPath path1, Object obj2, ValueMapPath path2)
	{
		if(! (obj1 instanceof DeclareVariable && obj2 instanceof DeclareVariable))
			return(DeclareVariable.DECLARATION_NOT_EQUAL);
		else
		{
			DeclareVariable var1 = (DeclareVariable) obj1;
			DeclareVariable var2 = (DeclareVariable) obj2;
			String name1 = path1.substitute(var1.getName());
			String name2 = path2.substitute(var2.getName());
			String val1 = path1.substitute(var1.getInitialValue());
			String val2 = path2.substitute(var2.getInitialValue());
			
			if( OperatorHelper.XOR( name1 == null, name2 == null ))
				return(DeclareVariable.DECLARATION_NOT_EQUAL);
			if( OperatorHelper.XOR( val1 == null, val2 == null))
				return(DeclareVariable.DECLARATION_NOT_EQUAL);

			boolean namesEqual = false;
			if( name1 == null && name2 == null)
				namesEqual = true;
			else if(name1.trim().equals(name2.trim()))
				namesEqual = true;
			
			boolean valsEqual = false;
			if(val1 == null && val2 == null)
				valsEqual = true;
			else if(val1.trim().equals(val2.trim()))
				valsEqual = true;
			
			if(namesEqual && valsEqual)
				return(DeclareVariable.DECLARATION_EQUAL);
			else if(!namesEqual)
				return(DeclareVariable.DECLARATION_NOT_EQUAL);
			else
				return(DeclareVariable.DECLARATION_CONFLICTS);				
		}
	}
	public void setValue(String val)
	{
		if(val != null)
		{
			value = new String(val);
		}
		else
		{
			value = null;
		}
	}
	public String getValue()
	{
		if(value == null)
			return("");
		else
			return(value);
	}
	public void setStatementBundleUsingMe(StatementBundle sb)
	{
		statementBundleUsingMe = sb;
	}
	public StatementBundle getStatementBundleUsingMe()
	{
		return(statementBundleUsingMe);
	}
}
