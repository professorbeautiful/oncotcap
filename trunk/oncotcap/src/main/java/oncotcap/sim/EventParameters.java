package oncotcap.sim;

import java.util.*;

public class EventParameters extends Hashtable<String, ValueAndType>
{
	public String stringValue(String argName) throws ArgumentNotFoundException
	{
		Object rObj = value(argName);
		if(rObj == null)
			throw(new ArgumentNotFoundException(argName));
		else if(!(rObj instanceof String))
			return(rObj.toString());
		else
			return((String) rObj);
	}

	public double doubleValue(String argName) throws ArgumentNotFoundException, ConversionException
	{
		Object rObj = value(argName);
		
		if(rObj == null)
			throw(new ArgumentNotFoundException(argName));
		else if(rObj instanceof String)
		{
			try{return(Double.parseDouble((String) rObj));}
			catch(NumberFormatException e){throw(new ConversionException(argName));}
		}
		else if(rObj instanceof Double)
			return(((Double) rObj).doubleValue());
		else
			throw(new ConversionException(argName));
	}
	public int intValue(String argName) throws ArgumentNotFoundException, ConversionException
	{
		Object rObj = value(argName);
		if(rObj == null)
			throw(new ArgumentNotFoundException(argName));
		else if(rObj instanceof String)
		{
			try{return(Integer.parseInt((String) rObj));}
			catch(NumberFormatException e){throw(new ConversionException(argName));}
		}
		else if(rObj instanceof Integer)
			return(((Integer) rObj).intValue());
		else
			throw(new ConversionException(argName));
	}
	public Object value(String argName) throws ArgumentNotFoundException
	{
		Object vt = get(argName);

		if(vt == null || ! (vt instanceof ValueAndType))
			throw(new ArgumentNotFoundException(argName));

		Object rObj = ((ValueAndType) vt).getValue();
		if(rObj != null)
			return(rObj);
		else
			throw(new ArgumentNotFoundException(argName));

	}
	public Class argType(String argName) throws ArgumentNotFoundException
	{
		Object vt = get(argName);

		if(vt == null || !(vt instanceof ValueAndType))
			throw(new ArgumentNotFoundException(argName));

		Class type = ((ValueAndType) vt).getType();
		if(type != null)
			return(type);
		else
			throw(new ArgumentNotFoundException(argName));

	}

}