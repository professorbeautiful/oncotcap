package oncotcap.display.editor.persistibleeditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.display.browser.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.process.treatment.AbstractPatient;
import oncotcap.util.OncMessageBox;

// custom
import java.awt.event.*;
import java.awt.Dimension;
import java.beans.PropertyChangeListener;


public class ModelControllerPanel extends DefaultEditorPanel
{
	private OncScrollList submodelGroups = null;
	private OncScrollList modelConfiguration = null;
	private OncScrollList introScreens = null;
	private OncScrollList starterProcess = null;
	private OncComboBox outputScreenDropDown = null;
	private OncTextField modelControllerName = null;
	private OncScrollList keywords = null;
	private OncIntegerTextField versionNumber = null;
	

		//custom code
	private WaitWindow waitWindow = null;

	public  ModelControllerPanel() {
		super();
		editObj = new ModelController();
		initUI();
		fillUiHashtable();
		addDefaultOutputScreen();
	}
	
	private void addDefaultOutputScreen() {
		ModelController mc = (ModelController) editObj;
		if(mc.getOutputScreen() ==  null){
			String defaultOutputScreen = "oncotcap.display.modelcontroller.cellkineticsdemo.SinglePatient";
			mc.setOutputScreen(defaultOutputScreen);
		}
		// Temporary!!  Until we do output screens right.
		// OK, this works.
	}

	public  ModelControllerPanel(ModelController editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
		addDefaultOutputScreen();
	}
	public void initUI(Object editObj) {
	this.editObj = (ModelController)editObj;
	initUI();
	}
	private void initUI() {
		// Custom code - put in subclass 
		setPreferredSize(new Dimension(725, 500));
		initButtons();
		// Custom Code - use a combo box instead of scroll list
		// get the info for the list from kb
		String  outputScreenNames [] = 
		{"oncotcap.display.modelcontroller.cellkineticsdemo.SinglePatient",
			"oncotcap.display.genericoutput.GenericOutput",
			
			};
		outputScreenDropDown = 
				 new OncComboBox(editObj, "Output Screens", true, outputScreenNames );
		//outputScreenDropDown.setIsCollection(true);
		// autogen code
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		modelControllerName = new OncTextField(editObj, "Model Controller Name", true);
		starterProcess = new OncScrollList(ProcessDeclaration.class, editObj, "StarterProcess", true,true, false);
		//		modelConfiguration = new OncScrollList(ModelConfiguration.class, editObj, "ModelConfiguration", true,true, false);
		submodelGroups = new OncScrollList(SubModelGroup.class, editObj, "Submodel Groups", true,true);
		introScreens = new OncScrollList(String.class, editObj, "Introduction Screens", true,true);
		//outputScreens = new OncScrollList(String.class, editObj, "Output Screens", true,true);
		keywords = new OncScrollList(Keyword.class, editObj, "Keywords", true,true);
		versionNumber.setBounds(20,540,125,60);
		modelControllerName.setBounds(10,0,470,60);
		starterProcess.setBounds(10, 60, 350, 60);
		//modelConfiguration.setBounds(530,670,250,60);
		submodelGroups.setBounds(10,140,350,230);
		introScreens.setBounds(390,140,270,120);
		outputScreenDropDown.setBounds(390,270,270,60);
		keywords.setBounds(10,380,250,120);
		versionNumber.setVisible(true);
		modelControllerName.setVisible(true);
		starterProcess.setVisible(true);
		//modelConfiguration.setVisible(true);
		submodelGroups.setVisible(true);
		introScreens.setVisible(true);
		outputScreenDropDown.setVisible(true);
		keywords.setVisible(true);
		add(versionNumber);
		add(modelControllerName);
		add(starterProcess);
		//add(modelConfiguration);
		add(submodelGroups);
		add(introScreens);
		add(outputScreenDropDown);
		add(keywords);
		
	}


	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(modelControllerName, "modelControllerName");
		uiHashtable.put(starterProcess, "starterProcess");
		//		uiHashtable.put(modelConfiguration, "modelConfiguration");
		uiHashtable.put(submodelGroups, "submodelGroups");
		uiHashtable.put(introScreens, "introScreens");
		uiHashtable.put(outputScreenDropDown, "outputScreen");
		uiHashtable.put(keywords, "keywords");
	}

	public Object getValue(){ return null; }
	
	public static void main(String[] args)
	{
		JFrame f = new JFrame();
		ModelControllerPanel p = new ModelControllerPanel();
		OncoTCapDataSource dataSource = oncotcap.Oncotcap.getDataSource();
		Object controller = dataSource.find(new oncotcap.util.GUID("888e66a3000001c8000000fceff724ad"));
		p.edit(controller);
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}

		// Custom Code 

		private void initButtons() {
			/*
			JButton outputScreenToggleButton = new JButton("Toggle Output");
			outputScreenToggleButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					String  outputScreenNames [] = 
						{"oncotcap.display.modelcontroller.cellkineticsdemo.SinglePatient",
							"oncotcap.display.genericoutput.GenericOutput",
							"oncotcap.display.genericoutput.GenericOutputControllerFrame",
							};
					// third one not used?
					if(outputScreenNames[0].equals((String)outputScreens.getData(0)))
						outputScreens.setValue(outputScreenNames[1]);
					else if(outputScreenNames[1].equals((String)outputScreens.getData(0)))
						outputScreens.setValue(outputScreenNames[0]);
				}		
			});
			
			*/
			
				JButton btnConfigure = new JButton("Configure");
				btnConfigure.setMnemonic('c');
				btnConfigure.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e)
					{
						if(editObj != null)
						{
							ModelConfiguration mc = ((ModelController) editObj).getConfiguration();
							if(mc == null)
							{
								mc = new ModelConfiguration((ModelController) editObj);
									((ModelController) editObj).setConfiguration(mc);
							}
							EditorPanel edPan = EditorFrame.showEditor(mc);
							((ModelController) editObj).refresh();
							((ModelConfigurationEditorPanel) edPan).refresh();				}
						}
					}
				);
				JButton btnLaunch = new JButton("Launch");
				btnLaunch.setMnemonic('L');
				btnLaunch.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e)
								{
										if(editObj != null)
										{
											boolean singleP = false;
											ModelController mc = (ModelController) editObj;
											String os = mc.getOutputScreen();
											if(os.endsWith(".SinglePatient") || os.equals("SinglePatient"))
											{
												singleP = true;
											}
											if(mc.getStarterProcess() == null)
												 OncMessageBox.showError("Must set a starter process.", "Model Launch");
											else if(singleP && ! (AbstractPatient.class.isAssignableFrom(mc.getStarterProcess().getProcessClass())))
												OncMessageBox.showError("The starter process must be a Patient when SinglePatient is used as an output screen.", "Model Launch");
											else
												mc.run();
										}
								}
						});
				
				JButton btnRunNow = new JButton("Run Now");
				btnRunNow.setMnemonic('R');
				btnRunNow.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e)
								{
										if(waitWindow == null)
												waitWindow = new WaitWindow(getMyFrame(), "Compiling the model. Please wait.");
										
										if(editObj != null)
												{
														waitWindow.setVisible(true);
														ModelCompiler mc = new ModelCompiler();
														Thread t = new Thread(mc);
														t.start();
												}
								}
						});

				JButton btnReview = new JButton("Review");
				btnReview.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e) {
										if(editObj != null && editObj instanceof ModelController) {
												JFrame frame = new JFrame();
												DependencyTablePanel dependencyTable = 
														new DependencyTablePanel();
												dependencyTable.buildDependencyTable((ModelController)editObj);
												frame.getContentPane().add(dependencyTable);
												frame.pack();
												frame.setSize(new Dimension(800,600));
												frame.setVisible(true);

										}
								}
						});
				
				btnConfigure.setBounds(565, 15, 90, 30);
				btnLaunch.setBounds(565, 45, 90, 30);
				btnRunNow.setBounds(565, 75, 90, 30);
				btnReview.setBounds(565, 105, 90, 30);
				//outputScreenToggleButton.setBounds(390,335,270,38);

				btnRunNow.setEnabled(false);
				add(btnConfigure);
				add(btnLaunch);
				add(btnRunNow);
				add(btnReview);
				//add(outputScreenToggleButton);
		}

	private class ModelCompiler implements Runnable
	{
		public void run()
		{
	/*		if(editObj != null)
			{
				ModelController mc = (ModelController) editObj;
				Iterator it = mc.getStatementConfigurations().iterator();
				ModelDefinition rm = new ModelDefinition((ModelController) editObj);
				while(it.hasNext())
				{
					StatementBundleConfiguration sbc = (StatementBundleConfiguration) it.next();
					rm.addStatementBundle(sbc.getStatementBundle(), sbc);
				}
				rm.assembleModel();
				rm.compile();
				if(waitWindow != null)
					waitWindow.setVisible(false);
				rm.run();
			}*/
		} 
	}
}
