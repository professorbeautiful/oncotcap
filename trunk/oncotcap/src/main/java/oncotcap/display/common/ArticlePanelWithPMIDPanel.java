package oncotcap.display.common;

import java.util.*;
import oncotcap.datalayer.*;
import oncotcap.display.editor.autogeneditorpanel.*;
import javax.swing.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;


public class ArticlePanelWithPMIDPanel extends ArticlePanel  
{
	public PMIDPanel pmidPanel = null;

	public  ArticlePanelWithPMIDPanel() {
		super();
		addPMIDPanel();
	}


	public  ArticlePanelWithPMIDPanel(Article editObj) {
		super(editObj);
		addPMIDPanel();
	}

	private void addPMIDPanel() {
		pmidPanel = new PMIDPanel(this);
		pmidPanel.setBounds(110,215,270,60);
		pmidPanel.setVisible(true);
		//PMID.setBounds(0,240,100,60);
		add(pmidPanel);
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		ArticlePanel p = new ArticlePanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
