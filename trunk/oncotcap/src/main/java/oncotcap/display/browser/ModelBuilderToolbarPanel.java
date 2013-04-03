/*
 * Educational Resource for Tumor Heterogeneity
 *             Oncology Thinking Cap
 *
 * Copyright (c) 2003  University of Pittsburgh
 * All rights reserved.
 *
 *  SourceSafe Info:
 *               $Header: $
 *               Revision: $Revision$
 *               Author: $Author$
 *
 * Code Review History:
 *     (mm.dd.yyyy initials)
 *
 * Test History:
 *     (mm.dd.yyyy initials)
 */
package oncotcap.display.browser;


import javax.swing.*;
import java.awt.event.*;

/**
 * @author    morris
 * @created   April 22, 2003
 */
public class ModelBuilderToolbarPanel extends ToolBarPanel {
		/** */
		JToolBar toolbar = null;
		JButton openProjectBtn = null;
		public static ProtegeSaveAction saveAction = new ProtegeSaveAction();
		public static OptionAction optionAction = new OptionAction();

		/**
	 * Constructor for the ModelBuilderToolbarPanel object
	 *
	 * @param modelBuilder Description of Parameter
	 */
	public ModelBuilderToolbarPanel() {
		try {
			toolbar = new JToolBar();
			add(toolbar);
			openProjectBtn = new JButton("Open Project");
			if ( oncotcap.Oncotcap.getDataSourceMode() == false )  {

					toolbar.add(openProjectBtn);
			}
			JButton saveBtn = new JButton(saveAction);
			JButton optionBtn = new JButton(optionAction);
			saveBtn.setToolTipText("<html>Save project to data storage.<br> CTRL-S</html>");
			optionBtn.setToolTipText("<html>Display option panel.<br> CTRL-O</html>");
			//toolbar.add(saveBtn);
			//toolbar.add(optionBtn);
			//toolbar.add(new JButton(new ViewAction()));
			//toolbar.add(new JButton(new RunAction()));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
		public void addActionListener(ActionListener al) {
			openProjectBtn.addActionListener(al);
		}
}
