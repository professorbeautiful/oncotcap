package oncotcap.datalayer.persistible;

import java.util.regex.*;
import java.io.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.AbstractPersistible;

public class ValueMapEntry extends oncotcap.datalayer.AbstractPersistible
{
	public Parameter originalParameter;
	public Parameter clonedParameter;
	public String singleParameterID;
//	public SingleParameter originalSingleParameter;
//	public SingleParameter clonedSingleParameter;

	public ValueMapEntry(oncotcap.util.GUID guid){
		super(guid);
	}
	public ValueMapEntry(){
			update();
	}
	public ValueMapEntry(Parameter originalParameter, 
											 SingleParameter originalSingleParameter, 
											 Parameter clonedParameter)
	{
		this(originalParameter, originalSingleParameter, clonedParameter, true);
	}

	public ValueMapEntry(Parameter originalParameter, SingleParameter originalSingleParameter, Parameter clonedParameter, boolean saveToDataSource)
	{
		if(!saveToDataSource)
			setPersistibleState(Persistible.DO_NOT_SAVE);
			
		this.singleParameterID = originalSingleParameter.getSingleParameterID();
		this.originalParameter = originalParameter;
		this.clonedParameter = clonedParameter;
		
		if(saveToDataSource)
			update();

	}
	public String getValue()
	{
		return(getClonedSingleParameter().getCodeValue());
	}
	public SingleParameter getClonedSingleParameter()
	{
			if (clonedParameter != null) // This may be masking another problem 04.30
					return(clonedParameter.getSingleParameter(singleParameterID));
			else 
					return null;
	}
	public SingleParameter getOriginalSingleParameter()
	{
		return(originalParameter.getSingleParameter(singleParameterID));
	}
	public String getName()
	{ 
		return(getOriginalSingleParameter().getDisplayName());
	}
	public String substitute(String code)
	{
		Pattern name = Pattern.compile("`" + getOriginalSingleParameter().getDisplayName() + "`", Pattern.CASE_INSENSITIVE);
		return(name.matcher(code).replaceAll(getClonedSingleParameter().getCodeValue()));
	}
	public void write(Writer w) throws IOException
	{
		w.write("\t\tName: " + getOriginalSingleParameter().getDisplayName() + "\t\t|  " + getClonedSingleParameter().getCodeValue() + "\n");
	}
	public Parameter getClonedParameter()
	{
		return(clonedParameter);
	}

/*	public void deleteClonedParameters() {
			Parameter clonedParameter = getClonedParameter();
			if ( clonedParameter instanceof AbstractPersistible) 
					clonedParame
					getDataSource().delete((AbstractParameter)clonedParameter);
	} */
	
	//only deletes the ValueMapEntry itself and the cloned parameter
	//the original parameter can be referenced in a StatementTemplate and
	//other ValueMapEntries so they are not deleted
	public void delete()
	{
		if(clonedParameter instanceof AbstractPersistible)
			((AbstractPersistible)clonedParameter).delete();
		super.delete();
	}
}
