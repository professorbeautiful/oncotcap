package oncotcap.datalayer.persistible.parameter;

import java.util.*;
import oncotcap.datalayer.*;

public abstract class AbstractIndividualParameter extends AbstractParameter implements SingleParameter
{
//	public static final CodeSection blankCodeSection = CodeSection.blankCodeSection;
	//public static final Vector emptyCollection = new Vector();
	
	protected String singleParameterID = null;
	public String displayName = null;
	protected String defaultDisplayName = null;
	
	private String idString = null;
	public AbstractIndividualParameter(oncotcap.util.GUID guid){
		super(guid);
		initSingleParams();
	}
	public AbstractIndividualParameter() {

		this(true);
	}
	public AbstractIndividualParameter(boolean saveToDataSource)
	{
		if(!saveToDataSource)
			setPersistibleState(Persistible.DO_NOT_SAVE);
		
		initSingleParams();
	}
	public abstract void initSingleParams();
/*	public String getSingleParameterValue(DefaultSingleParameter sp)
	{
		DefaultSingleParameter sp2 = singleParameterList.getSingleParameter(sp.getSingleParameterID());
		if(sp2 == null)
			return(null);
		else
			return(sp2.getValue());
	} */
	public Parameter getParameter(){
			return this;
	}
	public String toString()
	{
		return(name);
	}
	public String getID()
	{
		if(idString == null)
			idString = getGUID().toString() + getSingleParameterID();
		return(idString);
	}
/*	public void addSingleParameter(DefaultSingleParameter sp)
	{
		singleParameterList.add(sp);
	}
	*/
	public Object clone()
	{
		return(super.clone());
	}
	public void setSingleParameterID(String id)
	{
		singleParameterID = id;
	}
	public String getSingleParameterID()
	{
		return(singleParameterID);
	}
	public String getDisplayName()
	{
		if(displayName == null)
			return(defaultDisplayName);
		else
			return(displayName);
	}
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
		if ( this instanceof Persistible)
				update();
	}
	protected void setDefaultName(String defaultDisplayName)
	{
		this.defaultDisplayName = defaultDisplayName;
	}
	public String getDefaultName()
	{
		return(defaultDisplayName);
	}
}
