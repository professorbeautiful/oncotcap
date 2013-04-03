/*
 * ValueMapPath is a linked list of ValueMaps.  ValueMaps are placed in the list in order
 * of the StatementBundle/StatementTemplate hierarchy.
 */
package oncotcap.engine;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

import oncotcap.datalayer.autogenpersistible.Interpretation;
import oncotcap.datalayer.autogenpersistible.QuantitativeInterpretation;
import oncotcap.datalayer.persistible.CodeBundle;
import oncotcap.datalayer.persistible.Encoding;
import oncotcap.datalayer.persistible.StatementBundle;
import oncotcap.datalayer.persistible.StatementTemplate;
import oncotcap.datalayer.persistible.parameter.Parameter;
import oncotcap.datalayer.persistible.parameter.SingleParameter;
import oncotcap.util.StringHelper;

/**
 * @author shirey
 */
public class ValueMapPath
{

	private ValueMapListItem first = null;
	private ValueMapListItem last = null;
	
	private static int serializer = 0;
	private int localSerialNum;
	
	public ValueMapPath(){
		serializer++;
		localSerialNum = serializer;
	}
	public ValueMapPath(ValueMapPath vmp)
	{
		serializer++;
		localSerialNum = serializer;
		ValueMapListItem newItem;
		ValueMapListItem oldItem = vmp.first;
		ValueMapListItem lastNewItem = null;
		while(oldItem != null)
		{
			newItem = new ValueMapListItem(oldItem);
			newItem.previousMap = lastNewItem;
			if(lastNewItem == null)
				first = newItem;
			lastNewItem = newItem;
			oldItem = oldItem.nextMap;
		}
		last = lastNewItem;
	}
	public int getSerialNum()
	{
		return(localSerialNum);
	}
	public void addToEnd(StatementBundle sb)
	{
		ValueMapListItem item = new ValueMapListItem(sb);
		
		if(first == null && last == null)
		{
			first = item;
			last = item;
		}
		else
		{
			last.nextMap = item;
			item.previousMap = last;
			last = item;
		}
	}
	public void addToBeginning(StatementBundle sb)
	{
		ValueMapListItem item = new ValueMapListItem(sb);
		if(first == null && last == null)
		{
			first = item;
			last = item;
		}
		else
		{
			first.previousMap = item;
			item.nextMap = first;
			first = item;
		}
	}
	public String getValue(String singleParameterName)
	{
		return(getValue(first, singleParameterName));
	}
	public String getValue(ValueMapListItem item, String singleParameterName)
	{
		if(item.map == null)
			return(singleParameterName);
		SingleParameter sp = item.map.getClonedSingleParameter(singleParameterName);

		if(sp != null && sp.getCodeValue() != null)
		{
			if(sp.getCodeValue().indexOf("`") >= 0)
			{
				String val = sp.getCodeValue();
				if(item.nextMap != null)
				{
					Matcher match = StringHelper.quotedVar.matcher(val);
					String codeout = "";
					int idx = 0;
					while(match.find())
					{
						codeout = codeout + val.substring(idx, match.start()) + getValue(item.nextMap, val.substring(match.start() + 1, match.end() - 1).trim());
						idx = match.end();
					}
					codeout = codeout + val.substring(idx, val.length());
					return(codeout);
				}
				else 
					return(sp.getCodeValue());
			}
			else
				return(sp.getCodeValue());
		}
		else
		{
			if(item.nextMap != null)
			{
				return(getValue(item.nextMap, singleParameterName));
			}
			else
				return(singleParameterName);		
		}
	}
	public SingleParameter getSingleParameter(String singleParameterName)
	{
		return(getSingleParameter(first, singleParameterName));
	}
	public SingleParameter getSingleParameter(ValueMapListItem item, String singleParameterName)
	{
		SingleParameter sp = null;
		String cVal;
		String paramName = singleParameterName.trim();
		sp = item.map.getClonedSingleParameter(paramName);
		
		if(sp == null)
			return(null);

		cVal = sp.getCodeValue().trim();
		if(cVal.length() > 2 && cVal.startsWith("`") && cVal.endsWith("`"))
		{
			if(item.nextMap != null)
				return(getSingleParameter(item.nextMap, cVal.substring(1, cVal.length() - 1)));
			else
				return(null);
		}
		else
			return(sp);
	}	
	public Parameter getParameter(String singleParameterName)
	{
		return(getParameter(first, singleParameterName));
	}
	public Parameter getParameter(ValueMapListItem item, String singleParameterName)
	{
		SingleParameter sp = null;
		String cVal;
		String paramName = singleParameterName.trim();
		sp = item.map.getClonedSingleParameter(paramName);
		if(sp == null)
			return(null);
		
		cVal = sp.getCodeValue().trim();
//		if(cVal.length() == 0)
//			return(item.map.getClonedParameter(sp));
		if(cVal.length() > 2 && cVal.startsWith("`") && cVal.endsWith("`"))
		{
			if(item.nextMap != null)
				return(getParameter(item.nextMap, cVal.substring(1, cVal.length() - 1)));
			else
				return(null);
		}
		else
			return(item.map.getClonedParameter(sp));
	}

		public  Vector getParameters(String str) {
				Vector <SingleParameter> parameters = new Vector<SingleParameter>();
				// Currently a instantiation can have a reference to 
				// one Subset Parameter OR 
				// reference one or more ConditionalTableParameters 
				// SO get the first reference to a backquoted item
				Vector quotedStrings = 
						StringHelper.BQuoteReplacer.retrieveQuotes(str);
				if ( quotedStrings != null && quotedStrings.size() > 0 ) {
						 SingleParameter singleParam = 
								 getSingleParameter((String)quotedStrings.elementAt(0));
						 if ( singleParam != null ) 
								 parameters.addElement(singleParam);
				}
				return parameters;
	}
	public List<QuantitativeInterpretation> getQuantifiedInterpretations()
	{
		List<QuantitativeInterpretation> interpretations = new Vector<QuantitativeInterpretation>();
		return(getQuantifiedInterpretations(first, interpretations));
	}
	public List<QuantitativeInterpretation> getQuantifiedInterpretations(ValueMapListItem item, List<QuantitativeInterpretation> interpretations)
	{
		interpretations.addAll(item.statementBundle.getQuantifiedInterpretations());
		if(item.nextMap != null)
			return(getQuantifiedInterpretations(item.nextMap, interpretations));
		else
			return(interpretations);
	}
	public String substitute(String code)
	{
		return(substituteMacros(substituteBackQuotes(code)));
	}
	private String substituteBackQuotes(String code)
	{	
		if(code == null)
			return(null);
		
		Matcher match = StringHelper.quotedVar.matcher(code);
		String codeout = "";
		String value;
		int idx = 0;
		while(match.find())
		{
				//System.out.println(match.group());
			codeout = codeout + code.substring(idx, match.start()) + getValue(code.substring(match.start() + 1, match.end() - 1).trim());
			idx = match.end();
		}
		codeout = codeout + code.substring(idx, code.length());
		match = StringHelper.quotedVar.matcher(codeout);
		if(match.find())
			codeout = substitute(codeout);
		
		return(codeout);		
	}
	public String substituteJavaName(String code)
	{
		return(StringHelper.javaName(substitute(code)));
	}
	
	private static final Pattern observationMacro = Pattern.compile("<%\\s*observation\\s*%>", Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
	private String substituteMacros(String code)
	{
		if(code == null)
			return(null);
		
		Matcher mat = observationMacro.matcher(code);
		if(mat.find())
			return(replaceObservations(code));
		else
			return(code);
	}

	private String replaceObservations(String code)
	{
		if(code == null)
			return(null);
		
		Matcher mat = observationMacro.matcher(code);

		List<QuantitativeInterpretation> ints = getQuantifiedInterpretations();
		if(ints.size() > 0)
		{
			String rCode = ints.get(0).getRCode();
			if(rCode != null)
				return(mat.replaceAll("\"" + StringHelper.quadBackslashes(StringHelper.replaceNewLineChars(rCode)) + "\""));
		}
		return(code);
	}

	private class ValueMapListItem
	{
		StatementBundle statementBundle;
		ValueMap map;
		ValueMapListItem nextMap = null;
		ValueMapListItem previousMap = null;
		
		ValueMapListItem(StatementBundle sb)
		{
			this.statementBundle = sb;
			this.map = sb.getValueMap();
		}
		ValueMapListItem(ValueMapListItem li)
		{
			this.statementBundle = li.statementBundle;
			this.map = li.map;
		}
	}
}
