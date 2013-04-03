package oncotcap.datalayer.persistible;

import java.util.*;
import oncotcap.util.GUID;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.parameter.Parameter;
import oncotcap.datalayer.persistible.parameter.SingleParameter;

public class DefaultSingleParameter implements SingleParameter
{
	public String defaultDisplayName;
	public String displayName;
	private String displayValue;
	public String codeValue = null;
	public String singleParameterID;

	private String idString = null;
	private Parameter owner;
	
/*	public DefaultSingleParameter(Parameter owner)
	{
		this.owner = owner;
		defaultDisplayName = new String();
		displayName = new String();
		displayValue = new String();
		singleParameterID = new String();
	}
	public DefaultSingleParameter(String name, String value, Parameter owner)
	{
		this.owner = owner;
		defaultDisplayName = name;
		displayName = name;
		displayValue = value;
	}*/
	public DefaultSingleParameter(String name, Parameter owner, String id)
	{
		singleParameterID = id;
		this.owner = owner;
		defaultDisplayName = name;
		displayName = name;
		displayValue = new String();
	}
	public DefaultSingleParameter(DefaultSingleParameter sp, Parameter owner)
	{
		singleParameterID = sp.getSingleParameterID();
		this.owner = owner;
		if(sp.getDefaultName() != null)
			defaultDisplayName = new String(sp.getDefaultName());
		if(sp.getDisplayName() != null)
			displayName = new String(sp.getDisplayName());
		if(sp.getDisplayValue() != null)
		{
			displayValue = new String(sp.getDisplayValue());
		}
		if(sp.codeValue != null)
			codeValue = new String(sp.codeValue);
		if(sp.getSingleParameterID() != null)
			singleParameterID = new String(sp.getSingleParameterID());
	}
	public Object clone()
	{
		return(new DefaultSingleParameter(this, owner));
	}

	public String getID()
	{
		if(idString == null)
			idString = owner.getGUID() + getSingleParameterID();
			
		return(idString);
	}
	protected Vector saveHandlers = new Vector();
	public void addSaveListener(SaveListener handler)
	{
			//System.out.println("Addingsave listener " + this);
		if(! saveHandlers.contains(handler))
			saveHandlers.add(handler);
	}
	public void removeSaveListener(SaveListener listener)
	{
		if(saveHandlers.contains(listener))
			  saveHandlers.removeElement(listener);
	}
	public void fireSaveEvent()
	{
			//System.out.println("fireSaveEvent " + this);
			SaveEvent e = new SaveEvent(this);
			Object [] it = saveHandlers.toArray();
			for ( int i = 0; i < it.length; i++) {
					SaveListener sl = (SaveListener)it[i];
					sl.objectSaved(e);
			}

	}
	public void setDisplayName(String name)
	{
		displayName = name;
	}
	public void setDisplayValue(Object value)
	{
		if(value instanceof String)
			displayValue = (String) value;
		else
			System.out.println("!!!!!!!!!!!!!WARNING: Trying to set display value of: " + value.toString());
	}
	public String getDisplayValue()
	{
		if(! (displayValue == null || displayValue.trim().length() == 0))
			return(displayValue);
		else
			return(null);
	}
	public void setCodeValue(String value)
	{
		codeValue = value;
	}
	public String getCodeValue()
	{
		if(codeValue == null)
			return(getDisplayValue());
		else
			return(codeValue);
	}
	public String getDefaultName()
	{
		return(defaultDisplayName);
	}
	public String getDescription()
	{
		if(displayValue == null || displayValue.trim().length() == 0)
			return(getDisplayName());
		else
			return(displayValue);
	}
	public String getDisplayName()
	{
		if(displayName == null || displayName.trim().length() == 0)
			return(defaultDisplayName);
		else
			return(displayName);
	}
	public String toString()
	{
		return(getDisplayName());
	}
	public String getSingleParameterID()
	{
		return(singleParameterID);
	}
	public void setSingleParameterID(String id)
	{
		singleParameterID = id;
	}
	public Parameter getParameter(){
		return owner;
	}
	/*	public boolean equals(Object obj)
	{
		if(obj instanceof DefaultSingleParameter)
		{
			return(((DefaultSingleParameter) obj).getID().equals(getID()));			
		}
		else
			return(false);
	}
	public int hashCode()
	{
		return(getID().hashCode());
	} */
}
