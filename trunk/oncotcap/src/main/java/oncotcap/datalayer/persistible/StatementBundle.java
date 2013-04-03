package oncotcap.datalayer.persistible;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import oncotcap.*;
import oncotcap.display.common.*; 
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.*;
import oncotcap.display.browser.*;
import oncotcap.datalayer.autogenpersistible.Interpretation;
import oncotcap.datalayer.autogenpersistible.QuantitativeInterpretation;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.engine.*;
import oncotcap.util.*;

public class StatementBundle extends AbstractDroppableWithKeywords implements Editable, TreeBrowserNode
{
	private String statement = null;
	public StatementTemplate statementTemplate = null;
	private StatementTemplate statementTemplateUsingMe = null;
	private ValueMap valueMap = null;
	public DefaultPersistibleList encodings = null;
	private	ImageIcon icon =	
				oncotcap.util.OncoTcapIcons.getImageIcon("statementbundle.jpg");
	private Vector helpInfo = new Vector();
	private StatementBundleEditorPanel editor;
	private int sortKey = 0;
	private String shortName = null;
	private Vector codeSections = new Vector();
	private Vector variableDefinitions = new Vector();
	
	private static StatementTreeRendererComponent stRenderer = new StatementTreeRendererComponent();

	
	public StatementBundle(StatementTemplate st, ValueMap vm)
	{
		statementTemplate = st;
		valueMap = vm;
	}
	public StatementBundle(StatementTemplate st)
	{
		this(st, new ValueMap());
	}
	public StatementBundle(oncotcap.util.GUID guid){
		super(guid);
		valueMap = new ValueMap();

	}
	public StatementBundle() {

			// A statement bundle has to have a statement template to be 
			// instantiated this sets up a weird situation where it is 
			// based on an empty templat
		//	new StatementBundle(new StatementTemplate(), new ValueMap());
		this(true);
	}
	public StatementBundle(boolean saveToDataSource)
	{
		if(!saveToDataSource)
			setPersistibleState(Persistible.DO_NOT_SAVE);
		
		valueMap = new ValueMap(saveToDataSource);

	}
	public static void main(String [] args)
	{
		StatementBundle sb = getStatementBundle(new GUID("888eaae50000007800000111324ef371"));
		Collection<Encoding> encodings = sb.getEncodings();
		Collection<QuantitativeInterpretation> ints = sb.getQuantifiedInterpretations();
		System.out.println(sb);

/*		Iterator it = getAllStatementBundles().iterator();
		//while(it.hasNext())
		//	((StatementBundle) it.next()).convertStatement();
		if(it.hasNext())
		{
			it.next(); it.next(); it.next();
			new StatementTreeRendererComponent(((StatementBundle) it.next()).toString());
		}
		
		System.out.println("saving....");
		DataSourceStatus.getDataSource().commit();
		System.out.println("done");
		//	((StatementBundle)it.next()).printXML(0);
*/
	}
	
	//this is used only to strip the HTML from statements for
	//testing purposes.  THIS WILL CAUSE PROBLEMS IF USED IN 
	//PRODUCTION
	public void convertStatement()
	{
		if(statement != null)
			statement = StringHelper.htmlToText(statement);
		
		this.update();
		
		if(statementTemplate != null)
			statementTemplate.convertStatement();
	}
/*	public String getParameterValue(String singleParameterName)
	{
		SingleParameter sp = getClonedSingleParameter(singleParameterName);
		// 	System.out.println("getParameterValue " + sp
		// 												 + " code Value " + sp.getCodeValue()
		// 												 + " displayValue " + sp.getDisplayValue()
		// 												 + " getDisplayName() " 
		// 												 + sp.getDisplayName() );

		if(sp != null && sp.getCodeValue() != null)
		{
		//	if(sp instanceof SubsetParameter)
		//	{
		//		return(((SubsetParameter) ((SubsetParameter) sp).cloneSubstitute(this)).getCodeValue());
		//	}
		//	else 
			if(sp.getCodeValue().indexOf("`") >= 0)
			{
				String val = sp.getCodeValue();
				StatementTemplate baseTemplate = getStatementTemplateUsingMe();
				if(baseTemplate != null)
				{
					Matcher match = quotedVar.matcher(val);
					String codeout = "";
					int idx = 0;
					while(match.find())
					{
							//System.out.println(match.group());
						codeout = codeout + val.substring(idx, match.start()) + baseTemplate.getParameterValue(val.substring(match.start() + 1, match.end() - 1).trim());
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
			return(singleParameterName);
	}
*/
	private SingleParameter getClonedSingleParameter(String singleParameterName)
	{
		String paramName = singleParameterName.trim();
		SingleParameter sp = valueMap.getClonedSingleParameter(paramName);
		return(sp);

/*		if(sp == null)
			return(null);
		
		String val = sp.getCodeValue();
		if(val == null)
			return(sp);
			
		val = val.trim();
	*/	
/*		if(val.startsWith("`") && val.endsWith("`"))
		{
			val = val.substring(1, val.length() - 1);
			StatementTemplate baseTemplate = getStatementTemplateUsingMe();
			if(baseTemplate != null)
				return(baseTemplate.getClonedSingleParameter(val));
			else
				return(null);
		}
		*/
	}
	public Parameter getClonedParameter(String singleParameterName)
	{
		return(valueMap.getParameter(singleParameterName));
	}
	public Object clone()
	{
		return(clone(true));
	}
	public StatementBundle clone(boolean saveToDataSource)
	{
		StatementBundle sb = new StatementBundle(saveToDataSource);
		if(statement != null)
			sb.statement = new String(statement);
		sb.setEncodings(encodings);
		sb.statementTemplate = statementTemplate;
		sb.valueMap = (ValueMap) valueMap.clone(saveToDataSource);
		sb.sortKey = sortKey;
		return(sb);		
	}
//	private static void fillStatementBundleTables()
//	{
//		OncoTCapDataSource dataSource = Oncotcap.getDataSource();
//		Class sbClass = StatementBundle.class;
//		Collection processes = dataSource.find(sbClass);
//	}
	private static Class sbClass = StatementBundle.class;
	public static Collection getAllStatementBundles()
	{
		OncoTCapDataSource dataSource = Oncotcap.getDataSource();
		Collection bundles = dataSource.find(sbClass);	
		return(bundles);
	}
	public static StatementBundle getStatementBundle(GUID guid)
	{
		OncoTCapDataSource dataSource = Oncotcap.getDataSource();
		StatementBundle sb = (StatementBundle) dataSource.find(guid);	
		return(sb);
	}	
	public void printXML(int tabLevel)
	{
		String tabs = "";
		for(int i = 0; i < tabLevel; i++)
			tabs = tabs + "\t";
		System.out.println(tabs + "<StatementBundle>");
		System.out.println(tabs + "\t<ID>" + guid.toString() + "</ID>");
		System.out.println(tabs + "\t<DISPLAY>" + toString() + "</DISPLAY>");
		if(statementTemplate != null)
			statementTemplate.printXML(tabLevel + 1);
		System.out.println(tabs + "</StatementBundle>");
	}
	public StatementTemplate getStatementTemplate()
	{
		return(statementTemplate);
	}
	public EditorPanel getEditorPanel()
	{
		return(new StatementBundleEditorPanel());
	}

	public EditorPanel getEditorPanelWithInstance()
	{
			StatementBundleEditorPanel sbPanel	=	new StatementBundleEditorPanel();
			sbPanel.edit(this);
			return(sbPanel);
	}
	public void refresh()
	{
		//rebuild the HTML statement
		statement = statementTemplate.getStatement();
		Iterator it = statementTemplate.getParameterList();
		while(it.hasNext())
		{
			Parameter param = (Parameter) it.next();
			Iterator it2 = param.getSingleParameters();
			while(it2.hasNext())
				replaceSingleParameter((SingleParameter) it2.next());
		}
		
		//find any parameters that were previously in the ValueMap but are now
		//not present in the StatementTemplate and remove them
		it = valueMap.getValueIterator();
		Vector<ValueMapEntry> deletedEntries = new Vector<ValueMapEntry>();
		while(it.hasNext())
		{
			ValueMapEntry vme = (ValueMapEntry) it.next();
			if(! statementTemplate.getParameters().contains(vme.getOriginalSingleParameter()))
				deletedEntries.add(vme);
		}
		for(ValueMapEntry vmee : deletedEntries)
		{
			valueMap.remove(vmee);
			vmee.delete();
		}
	}
	 public void replaceSingleParameter(SingleParameter singleParam)
	 {
//	  System.out.println(statement);
//	  System.out.println();
      String line = statement;
	  String matchParamID = HTMLParserHelper.interMingleWithWhiteSpace(singleParam.getID());
	  StringBuffer matchString = new StringBuffer();
	  matchString.append(HTMLParserHelper.matchHREF);
	  matchString.append(HTMLParserHelper.matchQuote);
	  matchString.append(matchParamID);
	   matchString.append(HTMLParserHelper.matchQuote);
	   matchString.append(HTMLParserHelper.matchID);
	   matchString.append(HTMLParserHelper.matchQuote);
	   matchString.append(matchParamID);
	   matchString.append(HTMLParserHelper.matchQuote);
	   matchString.append(HTMLParserHelper.matchEndTag);
	  Pattern matchHtml = 
	    Pattern.compile(matchString.toString(), Pattern.DOTALL);
//	  System.out.println(matchString.toString());
	  // Replace the old param string 
	  String rLine = StringHelper.substituteRegEx(matchHtml,
	                     line, 
	                     variableToHTML(singleParam));
	  statement = rLine;
//	  System.out.println(statement);
	 }
	 public String variableToHTML(SingleParameter origSingleParam)
	 {
		String value = "";
		value  = valueMap.getSBDisplayValue(origSingleParam);
		
		return(HTMLParserHelper.variableValueToHTML(origSingleParam, value));
	 }
	public void setStatement(String statement)
	{
		this.statement = statement;
		
		//preload the renderer's cache so the first display of
		//SBs in a GenericTree isn't slow
		stRenderer.setText(statement);
	}
	public String getStatement()
	{
		return(statement);
	}
	public void setStatementTemplate(StatementTemplate st)
	{
		statementTemplate = st;
	}
	public String getShortName()
	{
		return(shortName);
	}
	public void setShortName(String shortName)
	{
		this.shortName = shortName;
	}
	public ParameterList getParameters()
	{
		return(statementTemplate.getParameters());
	}
	public ValueMap getValueMap()
	{
		return(valueMap);
	}
	public void setValueMap(ValueMap valMap)
	{	
			valueMap = valMap;
	}
	public Iterator getValueMapList()
	{
		if(valueMap != null)
			return(valueMap.getValueIterator());
		else
			return(null);
	}

		public DefaultPersistibleList getEncodings(){
				return encodings;
		}

		public void setEncodings(java.util.Collection  var ){
				if ( encodings== null)
						encodings = new DefaultPersistibleList();
				encodings.set(var);
		}


	public void addValueMapEntry(ValueMapEntry entry)
	{
			if (entry != null)
					valueMap.put(entry);
	}
	public String toString()
	{
		if(shortName != null && (!shortName.trim().equals("")))
			return(shortName);
		else if(statement != null)
			return(statement);
		else if(statementTemplate != null)
			return(statementTemplate.toString());
		else
			return("Unnamed Statement Bundle " + getGUID().toString());
	}
	public ImageIcon getIcon() {
		return icon;
	}
	public boolean dropOn(Object dropOnObject) {
			if ( dropOnObject instanceof Keyword 
					 || dropOnObject instanceof Encoding) {
					boolean link1 = link((Persistible)dropOnObject);
					boolean link2 = ((Persistible)dropOnObject).link(this);
					if ( link1 || link2){
							update();
							((Persistible)dropOnObject).update();
					return true;
					}
			}
			if ( dropOnObject instanceof StatementTemplate ) {
					boolean link2 = ((Persistible)dropOnObject).link(this);
					if ( link2){
							((Persistible)dropOnObject).update();
							return true;
					}
			}
			return false;
	}

	public Collection getHelpInfo()
	{
		return(helpInfo);
	}
	public void addHelpInfo(HelpInfo info)
	{
		helpInfo.add(info);
	}

	public StatementBundleEditorPanel getStatementEditor()
	{
		return(editor);
	}
	public void setStatementEditor(StatementBundleEditorPanel editor)
	{
		this.editor = editor;
	}

	public boolean isComplete()
	{
		if(statementTemplate == null)
			return(false);
		if(statementTemplate.getParameters().getSingleParameterSize() == 0)
			return(true);
		if(valueMap == null)
			return(false);
		Iterator it = statementTemplate.getParameters().getSingleParameters().iterator();
		while(it.hasNext())
		{
			SingleParameter sp = (SingleParameter) it.next();
			if(!valueMap.contains(sp))
				return(false);
			else
			{
				String val = valueMap.getDisplayValue(sp);
				if(val == null || val.trim().equals(""))
					return(false);
			}
		}
		return(true);
	}
	public int getSortKey()
	{
		return(sortKey);
	}
	public void setSortKey(int key)
	{
		sortKey = key;
	}
	public void setStatementTemplateUsingMe(StatementTemplate template)
	{
		statementTemplateUsingMe = template;
	}
	public StatementTemplate getStatementTemplateUsingMe()
	{
		return(statementTemplateUsingMe);
	}
/*	public Vector getVariableDefinitions()
	{
		variableDefinitions.clear();
		extractVariableDefinitions();
		return(variableDefinitions);
	}
	public Vector getCodeSections()
	{
		codeSections.clear();
		addCodeSections();
		return(codeSections);
	}
	private void addCodeSections()
	{
		CodeSection cs;
		statementTemplate.setStatementBundleImplementingMe(this);
		//get the code sections from the code bundles 
		//contained in this StatementBundle
		Iterator it = statementTemplate.getCodeBundleList();
		while(it.hasNext())
		{
			Object obj = it.next();
			if(obj instanceof CodeBundle)
			{
				CodeBundle cb = (CodeBundle) obj;
				addCodeSections(cb.getDeclarationSections());
				addCodeSections(cb.getMethodSections());
				cb.setStatementBundleUsingMe(this);			}
		}
		it = statementTemplate.getStatementBundles().getIterator();
		while(it.hasNext())
		{
			Object obj = it.next();
			if(obj instanceof StatementBundle)
			{
				((StatementBundle)obj).setStatementTemplateUsingMe(statementTemplate);
				addCodeSections(((StatementBundle)obj).getCodeSections());
			}
		}		
	}
	public void addCodeSection(CodeSection cs)
	{
		cs.setCode(substitute(cs.getCode()));
		codeSections.add(cs);
	}
	public void addCodeSections(Collection sections)
	{
		Iterator it = sections.iterator();
		while(it.hasNext())
		{
			Object section = it.next();
			if(section instanceof CodeSection)
			{
				addCodeSection((CodeSection) section);
			}
		}
	}
	private void extractVariableDefinitions()
	{
			//	get the definitions inside this StatementBundle
		Iterator it = valueMap.getClonedParameters().iterator();
		while(it.hasNext())
		{
			Object obj = it.next();
			if(obj instanceof Parameter)
				extractVariableDefinitions((Parameter) obj);
		}
		
		//get the definitions inside any StatementBundles used by 
		//the StatementTemplate that this is based on
		it = getStatementTemplate().getStatementList();
		while(it.hasNext())
		{
			variableDefinitions.addAll(((StatementBundle) it.next()).getVariableDefinitions());
		}
	}
	private void extractVariableDefinitions(Parameter sp)
	{
		Collection defs = sp.getVariableDefinitions();
		if(defs != null)
		{
			Iterator it = defs.iterator();
			while(it.hasNext())
			{
				VariableDefinition vd = (VariableDefinition) it.next();
				variableDefinitions.add(vd);
				addCodeSections(vd.getMethodSections());
				addCodeSections(vd.getDeclarationSections());
			}
		}
	}*/
	private static Pattern quotedVar = Pattern.compile("`.*?`", Pattern.DOTALL);
/*	public String substitute(String code)
	{	
		if(code == null)
			return(null);
		
		Matcher match = quotedVar.matcher(code);
		String codeout = "";
		String value;
		int idx = 0;
		while(match.find())
		{
				//System.out.println(match.group());
			codeout = codeout + code.substring(idx, match.start()) + getParameterValue(code.substring(match.start() + 1, match.end() - 1).trim());
			idx = match.end();
		}
		codeout = codeout + code.substring(idx, code.length());
		//System.out.println("Code in: " + code 
		//									 + "\n Code out: " + codeout);
		return(codeout);		
	}

	public String substituteJavaName(String code)
	{
		return(StringHelper.javaName(substitute(code)));
	}
	public Object cloneSubstitute()
	{
		return(cloneSubstitute(null));
	}
	public Object cloneSubstitute(StatementTemplate templateUsingMe)
	{
		//return(this);
		StatementBundle sb = new StatementBundle(false);
		sb.setStatementTemplateUsingMe(templateUsingMe);
		if(statement != null)
			sb.statement = new String(statement);
		sb.valueMap = (ValueMap) valueMap.clone(false);
		//StatementTemplate st = (StatementTemplate) statementTemplate.clone(false);
		//st.setStatementBundleImplementingMe(sb);
		sb.statementTemplate = (StatementTemplate) statementTemplate.cloneSubstitute(sb);
		//st.substituteSelf();
		sb.sortKey = sortKey;
		return(sb); 
	}*/
	public Collection<InstructionProvider> getLocalInstructionProviders()
	{
		Vector<InstructionProvider> providers = new Vector<InstructionProvider>();
		if(statementTemplate != null)
		{
			providers.addAll(statementTemplate.getCodeBundles().getList());
		}
		
		Vector params = valueMap.getClonedParameters();
		//ParameterList pl = getParameters();
		//Iterator it = pl.getIterator();
		Iterator it = params.iterator();
		while(it.hasNext())
		{
			InstructionProvider ip = (InstructionProvider) it.next();
			providers.add(ip);
		}
		return(providers);
	}
	public List<QuantitativeInterpretation> getQuantifiedInterpretations()
	{
		List<QuantitativeInterpretation> qInterpretations = new Vector<QuantitativeInterpretation>();
		Collection<Encoding> encodings = getEncodings();
		if(encodings != null)
		{
			for(Encoding e : encodings)
			{
				Collection<Interpretation> interpretations = e.getInterpretationsIEncode();
				for(Interpretation i : interpretations)
				{
					if(i instanceof QuantitativeInterpretation)
					{
						qInterpretations.add((QuantitativeInterpretation)i);
					}
				}
			}
		}
		return(qInterpretations);
	}
	
	public boolean contains(StatementBundle sb)
	{
		if(statementTemplate == null)
			return(false);
		else
			return(statementTemplate.contains(sb));
	}
}
