package oncotcap.datalayer.persistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.io.Serializable;
import java.util.*; // custom
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;
import oncotcap.display.editor.persistibleeditorpanel.*;

// custom
import oncotcap.display.modelcontroller.cellkineticsdemo.DefaultInputEditor;
import oncotcap.display.modelcontroller.cellkineticsdemo.IntroFrame;
import oncotcap.engine.ModelDefinition;


public class ModelController extends AutoGenPersistibleWithKeywords
		implements Popupable, Serializable {
	private DefaultPersistibleList submodelGroups;
	transient private ModelConfiguration configuration;
	private DefaultPersistibleList introScreens;
	private ProcessDeclaration starterProcess;
	private String outputScreen;
	private String modelControllerName;
	private Integer versionNumber;
	public ImageIcon icon =	
			oncotcap.util.OncoTcapIcons.getImageIcon("modelcontroller.jpg");
	static OncPopupMenu popup = null;

		public ModelController() {
				init();
		}
		
		
		public ModelController(oncotcap.util.GUID guid) {
				super(guid);
				init();
		}
		public static void main(String [] args)
		{
			OncoTCapDataSource dataSource = oncotcap.Oncotcap.getDataSource();
			Object controller = dataSource.find(new oncotcap.util.GUID("888e66a30000002c000000fd7ba1cff0"));
			((ModelController)controller).run();
		}
		private void init() {
				Method setter = null;
				Method getter = null;
				setter = getSetter("setSubmodelGroups", DefaultPersistibleList.class);
				setMethodMap.put("submodelGroups", setter);
				getter = getGetter("getSubmodelGroups");
				getMethodMap.put("submodelGroups", getter);
// 				setter = getSetter("setModelConfiguration", ModelConfiguration.class);
// 				setMethodMap.put("configuration", setter);
// 				getter = getGetter("getModelConfiguration");
// 				getMethodMap.put("configuration", getter);
				setter = getSetter("setIntroScreens", DefaultPersistibleList.class);
				setMethodMap.put("introScreens", setter);
				getter = getGetter("getIntroScreens");
				getMethodMap.put("introScreens", getter);
				setter = getSetter("setStarterProcess", ProcessDeclaration.class);
				setMethodMap.put("starterProcess", setter);
				getter = getGetter("getStarterProcess");
				getMethodMap.put("starterProcess", getter);
				setter = getSetter("setOutputScreen", String.class);
				setMethodMap.put("outputScreen", setter);
				getter = getGetter("getOutputScreen");
				getMethodMap.put("outputScreen", getter);
				setter = getSetter("setModelControllerName", String.class);
				setMethodMap.put("modelControllerName", setter);
				getter = getGetter("getModelControllerName");
				getMethodMap.put("modelControllerName", getter);
				setter = getSetter("setKeywords", DefaultPersistibleList.class);
				setMethodMap.put("keywords", setter);
				getter = getGetter("getKeywords");
				getMethodMap.put("keywords", getter);
				setter = getSetter("setVersionNumber", Integer.class);
				setMethodMap.put("versionNumber", setter);
				getter = getGetter("getVersionNumber");
				getMethodMap.put("versionNumber", getter);
		}

	public DefaultPersistibleList getSubmodelGroups(){
		return submodelGroups;
	}
// 	public ModelConfiguration getModelConfiguration(){
// 		return configuration;
// 	}
	public DefaultPersistibleList getIntroScreens(){
		return introScreens;
	}
	public ProcessDeclaration getStarterProcess(){
		return starterProcess;
	}
	public String getOutputScreen(){
		return outputScreen;
	}
	public String getModelControllerName(){
		return modelControllerName;
	}
	public Integer getVersionNumber(){
		return versionNumber;
	}
	public void setSubmodelGroups(java.util.Collection  var ){
		if ( submodelGroups== null)
			submodelGroups = new DefaultPersistibleList();
		submodelGroups.set(var);
	}

	// public void setModelConfiguration(ModelConfiguration var ){
// 		configuration = var;
// 	}

	public void setIntroScreens(java.util.Collection  var ){
		if ( introScreens== null)
			introScreens = new DefaultPersistibleList();
		introScreens.set(var);
	}
	public void setStarterProcess(ProcessDeclaration var ){
		starterProcess = var;
	}

	public void setOutputScreen(String  var ){
		outputScreen = var;
	}

	public void setModelControllerName(String var ){
		modelControllerName = var;
	}

	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}

	public String toString() {
		return modelControllerName;
	}
	public Class getPanelClass()
	{
		return ModelControllerPanel.class;
	}
	public String getPrettyName()
	{
		return "ModelController";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}

	public void refresh()
	{
		getConfiguration().refresh(this);
		update();
	}

	public void run()
	{
		if(getIntroScreens().size() > 0)
			startIntroFrame();
		else
			startInputScreen();
	}

	public void startIntroFrame()
	{
		IntroFrame intfr = new IntroFrame(this);
		intfr.setVisible(true);
	}

	public void startInputScreen()
	{
		DefaultInputEditor inpEd = new DefaultInputEditor(new ModelDefinition(this));
	}

	public ModelConfiguration getConfiguration()
	{
		if(configuration  == null)
		{
			configuration = new ModelConfiguration(this);
			configuration.refresh(this);
			update();
		}
		return(configuration);
	}
	public void setConfiguration(ModelConfiguration config)
	{
		configuration = config;
	}
	public Collection getStatementConfigurations()
	{
		return(configuration.getConfigurations());
	}
	public Collection getStatementBundles()
	{
		return(getHashedStatementBundles().values());
	}
	private Hashtable getHashedStatementBundles()
	{
		Hashtable bundles = new Hashtable();
		if(submodelGroups != null)
		{
			SubModelGroup sg;
			StatementBundle sb;
			Iterator it = submodelGroups.iterator();
			Iterator it2;
			while(it.hasNext())
			{
				sg = (SubModelGroup) it.next();
				Collection bndls = sg.getStatementBundles();
				if(bndls != null)
				{
					it2 = bndls.iterator();
					while(it2.hasNext())
					{
						sb = (StatementBundle) it2.next();
						bundles.put(sb, sb);
					}
				}
			}
		}
		return(bundles);
	}
	public boolean contains(StatementBundle sb)
	{
		return(getHashedStatementBundles().containsKey(sb));
	}

		// Popupable
		public void initPopupMenu() {
				if ( popup == null ) {
						// define the popup
						popup = new OncPopupMenu();
						JMenuItem mi;
						mi = new JMenuItem("Copy");
						CopyPersistibleAction copyAction = new CopyPersistibleAction();
						mi.setAction(copyAction);
						popup.add(mi);
						mi = new JMenuItem("Review Table");
						ReviewTableAction reviewTableAction = new ReviewTableAction();
						mi.setAction(reviewTableAction);
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
}
