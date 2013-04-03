package oncotcap.datalayer.persistible;

import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.Oncotcap;
import oncotcap.util.ReflectionHelper;
import oncotcap.util.StringHelper;
import oncotcap.display.common.*;
import oncotcap.display.browser.*;
import oncotcap.display.editor.persistibleeditorpanel.*;

public class StatementTemplate extends AbstractDroppableWithKeywords
		implements Editable, TreeBrowserNode, Popupable
{ 
//	private static Vector allTemplates = new Vector();
//	static {fillStatementTemplateTables();}
	
	private String statement;
	private StatementList statementBundles;
	private ParameterList parameters;
	private CodeBundleList codeBundles;
//	private PersistibleList statementBundlesBasedOnMe;
	
	private ImageIcon icon =	
			oncotcap.util.OncoTcapIcons.getImageIcon("statementtemplate.jpg");
	private StatementBundle statementBundleImplementingMe = null;

	private static StatementTreeRendererComponent stRenderer = new StatementTreeRendererComponent();
	
	static OncPopupMenu popup = null;

	public StatementTemplate(oncotcap.util.GUID guid){
		super(guid);
		statementBundles = new StatementList();
		parameters = new ParameterList();
		codeBundles = new CodeBundleList();
//		allTemplates.add(this);
	}

	public StatementTemplate()
	{
		this(true);
	}
	
	public void printXML(int tabLevel)
	{
		String tabs = "";
		for(int i = 0; i < tabLevel; i++)
			tabs = tabs + "\t";
		System.out.println(tabs + "<StatementTemplate>");
		System.out.println(tabs + "\t<ID>" + guid.toString() + "</ID>");
		System.out.println(tabs + "\t<DISPLAY>" + toString() + "</DISPLAY>");
		Iterator it = statementBundles.getIterator();
		while(it.hasNext())
			((StatementBundle) it.next()).printXML(tabLevel + 1);
		System.out.println(tabs + "</StatementTemplate>");
	}
	/**
	 ** Allow this template to not be saved to the backend.  This will
	 ** not prevent any new StatementBundles, Parameters or CodeBundles
	 ** that are created and attached to this Template from being
	 ** persisted to the backend.
	 **/
	public StatementTemplate(boolean saveToDataSource)
	{
		if(!saveToDataSource)
			setPersistibleState(Persistible.DO_NOT_SAVE);
		statement = new String();
		statementBundles = new StatementList();
		parameters = new ParameterList();
		codeBundles = new CodeBundleList();
		//allTemplates.add(this);
	}
	
	//this is used only to strip the HTML from statements for
	//testing purposes.  THIS WILL CAUSE PROBLEMS IF USED IN 
	//PRODUCTION
	public void convertStatement()
	{
		if(statement != null)
			statement = StringHelper.htmlToText(statement);
		
		Iterator it = statementBundles.getIterator();
		while(it.hasNext())
			((StatementBundle) it.next()).convertStatement();
		
		this.update();
		
	}
/*	public String getParameterValue(String singleParameterName)
	{
		if(getStatementBundleImplementingMe() != null)
			return(getStatementBundleImplementingMe().getParameterValue(singleParameterName));
		else
			return(singleParameterName);
	}*/
	public StatementBundle getStatementBundleImplementingMe()
	{
		return(statementBundleImplementingMe);
	}
	public void setStatementBundleImplementingMe(StatementBundle bundle)
	{
		statementBundleImplementingMe = bundle;
	}
	private static void fillStatementTemplateTables()
	{
		OncoTCapDataSource dataSource = Oncotcap.getDataSource();
		System.out.println("Datasource is: " + dataSource);
		Class sbClass = ReflectionHelper.classForName("oncotcap.datalayer.persistible.StatementTemplate");
		Collection processes = dataSource.find(sbClass);
	}

	public void addCodeBundle(CodeBundle cb)
	{
		if(codeBundles == null)
			codeBundles = new CodeBundleList();

		codeBundles.add(cb);
		cb.setStatementTemplateContainingMe(this);
	}
	public void addParameter(Parameter param)
	{
		if(parameters == null)
			parameters = new ParameterList();

		parameters.add(param);
	}
	public void addStatementBundle(StatementBundle sb)
	{
		if(statementBundles == null)
			statementBundles = new StatementList();

		statementBundles.add(sb);
	}
	public String getStatement()
	{
		return(statement);
	}
	public void setStatement(String statement)
	{
		this.statement = statement;
		
		//preload the renderer's cache so the first disply of
		//StatementTemplates in a GenericTree isn't slow
		stRenderer.setText(statement);
	}
//	public static Vector getAllStatementTemplates() 
//	{
//		return(allTemplates);
//	}
	public String toString()
	{
		return(statement);
	}
	public Iterator getStatementList()
	{
		return(statementBundles.getIterator());
	}
	public void setStatementList(Collection list) {
				if ( statementBundles == null) 
						statementBundles = new StatementList();
				statementBundles.set(list);
	}
	public StatementList getStatementBundles()
	{
		return(statementBundles);
	}
	public Iterator getParameterList()
	{
		return(parameters.getIterator());
	}
  public void setParameterList(Collection list) {
				if ( parameters == null) 
						parameters = new ParameterList();
				parameters.set(list);
	}
  
	public ParameterList getParameters()
	{
		return(parameters);
	}
	public void setParameters(ParameterList params)
	{
		parameters = params;
	}
	private OncFilter connectedToMe = null;
	public Iterator getStatementBundlesBasedOnMe()
	{
       if(connectedToMe == null)
		 {
          connectedToMe = new OncFilter(false);
          connectedToMe.getRootNode().addChild(this, false);
		 }
       return(Oncotcap.getDataSource().getInstances("StatementBundle", connectedToMe, "StatementTemplateToStatementBundle").iterator());
	}
	public void setStatementBundlesBasedOnMe(Collection list)
	{
//		if(statementBundlesBasedOnMe == null)
//			statementBundlesBasedOnMe = new DefaultPersistibleList();
//		statementBundlesBasedOnMe.set(list);
	}
	public Iterator getCodeBundleList()
	{
		return(codeBundles.getIterator());
	}
   public void setCodeBundleList(Collection list)
	{
				if ( codeBundles == null) 
						codeBundles = new CodeBundleList();
				codeBundles.set(list);
	}
	public CodeBundleList getCodeBundles()
	{
		return(codeBundles);
	}
	public void update()
	{
		if(getDataSource().isModified(this))
			refreshStatementBundlesBasedOnMe();
		super.update();
	}
	private void refreshStatementBundlesBasedOnMe()
	{
		Iterator it = this.getStatementBundlesBasedOnMe();
		while(it.hasNext())
		{
			StatementBundle sb = (StatementBundle) it.next();
			sb.refresh();
			sb.update();
		}
	}
	// public StatementList getStatementBundlesBasedOnMe()
// 	{
// 		return(statementBundles);
// 	}
//	protected void finalize() throws Throwable
//	{
//		allTemplates.removeElement(this); 
//		super.finalize();
//	}
	public EditorPanel getEditorPanel()
	{
		return(new StatementTemplateEditorPanel());
	}
	public EditorPanel getEditorPanelWithInstance() {
			StatementTemplateEditorPanel stPanel	=	
					new StatementTemplateEditorPanel();
			stPanel.edit(this);
			return(stPanel);
	}
	public ImageIcon getIcon() {
		return icon;
	}

		public boolean dropOn(Object dropOnObject) {
				if ( dropOnObject instanceof Keyword ) {
						boolean link1 = link((Persistible)dropOnObject);
						boolean link2 = ((Persistible)dropOnObject).link(this);
						if ( link1 || link2){
								update();
								((Persistible)dropOnObject).update();
						return true;
						}
				}
				return false;
		}
		public boolean link(Persistible relatedPersistible) {
				//oncotcap.util.ForceStackTrace.showStackTrace();
				// Make sure a circular relationship is not set up w/ ST & SB
				if ( relatedPersistible instanceof StatementBundle ) 
						return false;
				return super.link(relatedPersistible);
		}

		public void initPopupMenu() {
				if ( popup == null ) {
						// define the popup
						popup = new OncPopupMenu();
						JMenuItem mi;
						mi = new JMenuItem("Copy");
						CopyPersistibleAction copyAction = new CopyPersistibleAction();
						mi.setAction(copyAction);
						popup.add(mi); //temp
						//block until clone can
						//be better tested
						mi = new JMenuItem("Sort");
						SortNodeAction sortAction = new SortNodeAction();
						mi.setAction(sortAction);
						popup.add(mi);  
						popup.setOpaque(true);
						popup.setLightWeightPopupEnabled(true);
						
				}
		}

		public JPopupMenu getPopupMenu() {
				if ( popup == null ) 
						initPopupMenu();
				return popup;
		}
		public Object clone()
		{
			return(clone(true));
		}
		public Object clone(boolean saveToDataSource)
		{
			StatementTemplate st = new StatementTemplate(saveToDataSource);
			Iterator it = codeBundles.getIterator();
			while(it.hasNext())
				st.addCodeBundle((CodeBundle) ((CodeBundle) it.next()).clone(saveToDataSource));
			
			it = keywords.getIterator();
			while(it.hasNext())
				st.addKeyword((Keyword) it.next());
			

			it = statementBundles.getIterator();
			while(it.hasNext())
			{
				if(saveToDataSource)
					st.addStatementBundle((StatementBundle) it.next());
				else
					st.addStatementBundle(((StatementBundle) it.next()).clone(false));
			}

			String clonedStatement = new String(statement);
			it = parameters.getIterator();
			if(saveToDataSource)
			{
				Parameter param;
				Parameter clonedParam;
				SingleParameter singleParam;
				SingleParameter clonedSingleParam;
				
				
				while(it.hasNext())
				{
					param = (Parameter) it.next();
					clonedParam = (Parameter) param.clone();
					Iterator it2 = param.getSingleParameters();
					while(it2.hasNext())
					{
						singleParam = (SingleParameter) it2.next();
						clonedSingleParam = clonedParam.getSingleParameter(singleParam.getSingleParameterID());
						clonedStatement = StringHelper.substituteInPlace(clonedStatement, singleParam.getID(), clonedSingleParam.getID());
					}
					st.addParameter(clonedParam);
				}
			}
			else
			{
				while(it.hasNext())
				{
					st.addParameter((Parameter) it.next());
				}
			}
			st.setStatement(clonedStatement);
			return(st);
		}
		

		public boolean contains(StatementBundle sb)
		{
			return(statementBundles.contains(sb));
		}
		/**
		 ** Makes a copy of this StatementTemplate replacing all back-quoted
		 ** entries in string fields with the associated value from the 
		 ** StatmentBundle.  This DOES NOT make a deep copy; Parameters are
		 ** not cloned.
		 **/
/*		public Object cloneSubstitute(StatementBundle sb)
		{
			StatementBundle nextSB;
			CodeBundle cb;
			StatementTemplate st = new StatementTemplate(false);
			st.setStatementBundleImplementingMe(sb);
			Iterator it = codeBundles.getIterator();
			while(it.hasNext())
			{
				cb = (CodeBundle) it.next();
				st.addCodeBundle((CodeBundle) cb.cloneSubstitute(sb, this));
			}
			
			it = parameters.getIterator();
			while(it.hasNext())
			{
				st.addParameter((Parameter) it.next());
			}
			
			it = keywords.getIterator();
			while(it.hasNext())
				st.addKeyword((Keyword) it.next());
			
			it = statementBundles.getIterator();
			while(it.hasNext())
			{
				nextSB = (StatementBundle) it.next();
				st.addStatementBundle((StatementBundle) nextSB.cloneSubstitute(st));
			}
			
			st.setStatement(new String(statement));
			return(st);
		}*/
}
