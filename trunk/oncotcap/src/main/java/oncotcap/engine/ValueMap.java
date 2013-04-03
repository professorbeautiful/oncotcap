package oncotcap.engine;

import java.util.*;
import java.io.*;
import java.util.regex.*;

import oncotcap.datalayer.persistible.parameter.SingleParameter;
import oncotcap.datalayer.persistible.parameter.TwoLevelParameter;
import oncotcap.datalayer.persistible.ValueMapEntry;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.datalayer.autogenpersistible.*;

public class ValueMap extends Hashtable
{
	public static final int LOCAL = 0;
	public static final int GLOBAL = 1;
	private int scope;
	private Hashtable valuesByName = new Hashtable();
	private Vector clonedParameters = new Vector();
	private Hashtable<String, ValueMapEntry> valuesByClonedSingleParameterID = new Hashtable<String, ValueMapEntry>();
	private boolean saveToDataSource = true;
	public static final Pattern BACK_QUOTED_VAR = 
				Pattern.compile("`.*?`", Pattern.DOTALL);

	
//	private paramsToSingleParams
	
	public ValueMap()
	{
		super();
		this.scope = LOCAL;  //default to a local VM
	}
	public ValueMap(boolean saveToDataSource)
	{
		this.saveToDataSource = saveToDataSource;
		this.scope = LOCAL;
	}
	public ValueMap(int scope)
	{
		super();
		this.scope = scope;
	}
	public Object clone()
	{
		return(clone(true));
	}
	public ValueMap clone(boolean saveToDataSource)
	{
		Hashtable paramsTemp = new Hashtable();
		Iterator it = clonedParameters.iterator();
		while(it.hasNext())
		{
			Parameter oldParam = (Parameter) it.next();
			paramsTemp.put(oldParam, oldParam.clone());
		}
		
		ValueMap vmCopy = new ValueMap(saveToDataSource);
		it = getValueIterator();
		while(it.hasNext())
		{
			ValueMapEntry vme = (ValueMapEntry) it.next();
			if(vme != null && vme.getClonedParameter() != null)
			{
				Parameter origParam = vme.originalParameter;
				SingleParameter origSingleParam = vme.getOriginalSingleParameter();
				Parameter clonedParam = (Parameter) paramsTemp.get(vme.clonedParameter);
				vmCopy.put(new ValueMapEntry(origParam, origSingleParam, clonedParam, saveToDataSource));
			}
		}
		return(vmCopy);
	}

	public void printValues()
	{
		ValueMapEntry vme;
		Iterator it = getValueIterator();
		while(it.hasNext())
		{
			vme = (ValueMapEntry) it.next();
			System.out.println("\t\tNAME: " + vme.getName() + "\tVALUE: " + vme.getValue());
		}

	}

	public void insert(ValueMap vm)
	{
		Iterator it = vm.getValueIterator();
		while(it.hasNext())
			put((ValueMapEntry) it.next());
	}
	public Parameter createClonedParameter(Parameter originalParameter)
	{
		SingleParameter sp;
		ValueMapEntry vme;
		Parameter clonedParam;
		if(originalParameter instanceof DeclareEnumPicker)
			clonedParam = (Parameter) (new SubsetParameter((DeclareEnumPicker) originalParameter, saveToDataSource));
		else if(originalParameter instanceof DeclareCategory)
			clonedParam = (Parameter) (new ModifyCategory((DeclareCategory) originalParameter, saveToDataSource));
		else if(originalParameter instanceof DeclareTwoLevelParameter)
			clonedParam = (Parameter) (new TwoLevelParameter((DeclareTwoLevelParameter)originalParameter));
		else
			clonedParam = (Parameter) originalParameter.clone();
		
		Iterator it = originalParameter.getSingleParameters();
		while(it.hasNext())
		{
			sp = (SingleParameter) it.next();
			vme = new ValueMapEntry(originalParameter, sp, clonedParam);
			put(vme);
		}
		return(clonedParam);
	}
	private Parameter getClonedParameter(String singleParameterID)
	{
		if(containsKey(singleParameterID))
		{
			ValueMapEntry vme = (ValueMapEntry) get(singleParameterID);
			if(vme.clonedParameter != null)
				return(vme.clonedParameter);
		}
		else if(valuesByClonedSingleParameterID.containsKey(singleParameterID))
		{
			return(valuesByClonedSingleParameterID.get(singleParameterID).clonedParameter);
		}
		return(null);
	}
	public Parameter getClonedParameter(SingleParameter originalSingleParameter)
	{
		return(getClonedParameter(originalSingleParameter.getID()));
	}
	public Parameter getClonedParameter(Parameter originalParameter)
	{
		if(originalParameter != null)
		{
			Parameter rParam = null;
			Iterator it = originalParameter.getSingleParameters();
			if(it.hasNext())
			{
				rParam = getClonedParameter((SingleParameter) it.next());
				if(rParam != null)
					return(rParam);
				else
					return(createClonedParameter(originalParameter));
			}
			else
			{
				return(createClonedParameter(originalParameter));
			}
		}
		else return(null);
		
	}
	public void put(ValueMapEntry entry)
	{
		if(entry != null && 
		   entry.getOriginalSingleParameter() != null &&
		   entry.getOriginalSingleParameter().getDisplayName() != null)
		{
			put(entry.getOriginalSingleParameter().getID(), entry);
			valuesByName.put(entry.getOriginalSingleParameter().getDisplayName().toUpperCase(), entry);
			// System.out.println("entry.getClonedSingleParameter() " + entry.getClonedSingleParameter());
			valuesByClonedSingleParameterID.put(entry.getClonedSingleParameter().getID(), entry);
			if(! clonedParameters.contains(entry.getClonedParameter()))
				clonedParameters.add(entry.getClonedParameter());
		}
	}

	public Parameter getParameter(String singleParameterName)
	{
		if(singleParameterName == null)
			return(null);
		else if(valuesByName.containsKey(singleParameterName.toUpperCase()))
		{
			ValueMapEntry entry = (ValueMapEntry) valuesByName.get(singleParameterName.toUpperCase());
			return(entry.getClonedParameter());
		}
		else
			return(null);
	}
	public Vector getClonedParameters()
	{
		return(clonedParameters);
	}
	public String getDisplayValue(String name)
	{
		Object obj = valuesByName.get(name.toUpperCase());
		if(obj != null && obj instanceof ValueMapEntry)
		{
			ValueMapEntry vme = (ValueMapEntry) obj;
			String val = vme.getClonedSingleParameter().getDisplayValue();
			if(val != null && !val.trim().equals(""))
				return(val);
			else
				return(null);
		}
		else
			return(null);
	}
	public String getDisplayValue(SingleParameter originalSingleParameter)
	{
		if(containsKey(originalSingleParameter.getID()))
		{
			ValueMapEntry vme = (ValueMapEntry) get(originalSingleParameter.getID());
			SingleParameter clonedSingleParam = vme.getClonedSingleParameter();
			if(clonedSingleParam != null)
				return(clonedSingleParam.getDisplayValue());
			else
				return(originalSingleParameter.getDisplayValue());
		}
		else
			return(originalSingleParameter.getDisplayValue());
	}
	
	public String getSBDisplayValue(SingleParameter origSingleParam)
	{
		String value = "";
			if (contains(origSingleParam))
			{
				if (getDisplayValue(origSingleParam) != null)
				{
					value = getDisplayValue(origSingleParam);
					if (!getDisplayValue(origSingleParam).toString().trim().equals(""))
						value = getDisplayValue(origSingleParam);
					else
						value = origSingleParam.getDisplayName();
				}
			}
			else
				value = origSingleParam.getDisplayName();
		return (value);
	}
	public String getCodeValue(String name)
	{
		Object obj = valuesByName.get(name.toUpperCase());
		if(obj != null && obj instanceof ValueMapEntry)
		{
			ValueMapEntry vme = (ValueMapEntry) obj;
			String val = vme.getClonedSingleParameter().getCodeValue();
			if(val != null && !val.trim().equals(""))
				return(val);
			else
				return(null);
		}
		else
			return(null);
	}
	public String getCodeValue(SingleParameter originalSingleParameter)
	{
		if(containsKey(originalSingleParameter.getID()))
		{
			SingleParameter clonedSingleParam = ((ValueMapEntry)get(originalSingleParameter.getID())).getClonedSingleParameter();
			if(clonedSingleParam != null)
				return(clonedSingleParam.getCodeValue());
			else
				return(originalSingleParameter.getCodeValue());
		}
		else
			return(originalSingleParameter.getCodeValue());
	}
	public SingleParameter getClonedSingleParameter(String parameterName)
	{
		String paramName = parameterName.trim();
		if(paramName.startsWith("`") && paramName.endsWith("`"))
			paramName = paramName.substring(1, paramName.length() - 1);
		Object obj = valuesByName.get(paramName.toUpperCase());
		if(obj != null && obj instanceof ValueMapEntry)
		{
			ValueMapEntry vme = (ValueMapEntry) obj;
			SingleParameter sp = vme.getClonedSingleParameter();
			return(sp);
		}
		else
			return(null);		
	}

	public int getScope()
	{
		return(scope);
	}
	public boolean contains(SingleParameter originalSingleParameter)
	{
		return(containsKey(originalSingleParameter.getID()));
	}
	public Iterator getValueIterator()
	{
		return(new ValueIterator());
	}
/*	public String javaName(String name)
	{
		return(StringHelper.javaName(substitute(name)));
	}
	*/
	public void write(Writer w) throws IOException
	{
		Iterator it = getValueIterator();
		while(it.hasNext())
			((ValueMapEntry) it.next()).write(w);
	}
	private class ValueIterator implements Iterator
	{
		private final Enumeration keyEnum = elements();

		/**
		 ** Returns true if the iteration has more elements. 
		 **/
		public boolean hasNext() {return(keyEnum.hasMoreElements());}
		/**
		 ** Returns the next element in the iteration.
		 **/
		public Object next() { return(((ValueMapEntry)keyEnum.nextElement()));}

		/**
		 ** Removes from the underlying collection the last element
		 ** returned by the iterator (optional operation).  This method
		 ** is not implemented.
		 **/
		public void remove() {}		
	}
		public Object remove(Object key) {
				if ( key instanceof ValueMapEntry ) {
						ValueMapEntry vme = (ValueMapEntry)key;
						clonedParameters.remove(vme.getClonedParameter());		
						valuesByName.remove(vme.getOriginalSingleParameter().getDisplayName().toUpperCase());			
						return super.remove(vme.getOriginalSingleParameter().getID());
				}
				return null;
		}

		public Hashtable getValuesByNameMap() {
				Hashtable map = new Hashtable();
				Iterator values = values().iterator();
				String name = null;
				String value = null;
				while (values.hasNext()) {
						ValueMapEntry myVme = (ValueMapEntry)values.next();
						name = myVme.getName().toUpperCase();
						value = myVme.getValue();
						if ( name != null  && value != null)
							map.put(name, value);
				}
				return map;
		}

		static public String substitute(Hashtable valuesByNameMap, 
																		String unresolvedString) {
				// this substitution uses a plan hashtable where key is vme.getName(),
				// and value is vme.getValue() to substitute for backquoted 
				// strings
				// If there is an empty unresolved
				// string pass empty back
				if (unresolvedString == null
				    || unresolvedString.length() <= 0 )
					return "NoValueToSubstitute";
				StringBuffer resolvedString = new StringBuffer();
				String backQuotedString = null;
				// Use this value map ( and this valuemap only) to resolve 
				// backquoted strings Note: this value map is already
				// reconciled in terms of no back quoted values - up the tree 
				int idx = 0;
				Matcher match = BACK_QUOTED_VAR.matcher(unresolvedString);
				// If there are no back quotes pass the string back as is
				String value = null;
				if ( !match.find() ) {
						return unresolvedString;
				}
				else {
						match.reset();
						// Patch the string back together fully
						// substituted 
						while(match.find()) {
								resolvedString.append(unresolvedString.substring(idx, 
																																 match.start()));
								backQuotedString = 
										unresolvedString.substring(match.start()+1, 
																							 match.end()-1).trim();
								value = (String)valuesByNameMap.get(backQuotedString.toUpperCase());
								if ( value != null ) 
										resolvedString.append(value);
								idx = match.end();
						}
				}
				// put the rest of the string on the end
				resolvedString.append(
															unresolvedString.substring(idx, 
																				 unresolvedString.length()));
				return resolvedString.toString();
		}	
		
}
