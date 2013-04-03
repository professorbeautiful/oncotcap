package oncotcap.display.genericoutput;

import java.awt.BorderLayout;

import oncotcap.display.OutputDevice;
import oncotcap.display.browser.HelpMenu;
import oncotcap.display.browser.ModelBuilderToolbarPanel;
import oncotcap.display.browser.OncBrowser;
import oncotcap.engine.ProcessController;
import oncotcap.process.OncProcess;
import oncotcap.util.ScreenHelper;

public class GenericOutput implements OutputDevice
{
	private GenericOutputControllerFrame controllerFrame;
	
	public GenericOutput(OncProcess process, ProcessController controller)
	{
		OutputFrame mainOutputFrame = new OutputFrame();
		controllerFrame = createControllerFrame(process, controller, mainOutputFrame);
		controller.addShownScreen(controllerFrame);
		controllerFrame.setVisible(true);
		OncBrowser.enableHelp(mainOutputFrame);
//		ModelBuilderToolbarPanel mainToolBar = new
//		ModelBuilderToolbarPanel();
//		mainMenuPanel.add(menuBar, BorderLayout.WEST);
//		mainMenuPanel.add(mainToolBar, BorderLayout.EAST);
//		// mainMenuPanel.add(filter, BorderLayout.EAST);
//		getContentPane().add(mainMenuPanel, BorderLayout.NORTH);
	}
	private GenericOutputControllerFrame createControllerFrame(OncProcess process, ProcessController controller, OutputFrame mainOutputFrame)
	{
		GenericOutputControllerFrame f = new GenericOutputControllerFrame(process, controller, mainOutputFrame);
		f.setLocation(Math.min(800, ((int)ScreenHelper.getScreenDim().getWidth()) - f.getWidth()),0);
		return(f);
	}
	public void setDisplayObject(OncProcess process)
	{
		controllerFrame.setDisplayObject(process);
	}	
	
}
