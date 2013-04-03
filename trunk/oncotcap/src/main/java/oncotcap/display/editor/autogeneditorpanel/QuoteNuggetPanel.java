package oncotcap.display.editor.autogeneditorpanel;


import java.util.*;
import javax.swing.*;

import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class QuoteNuggetPanel extends DefaultEditorPanel
{
	private OncComboBox quoteSection = null;
	private OncIntegerTextField bookmarkLocation = null;
	private OncScrollList relevanceAssessments = null;
	private OncScrollList confidenceAssessments = null;
	private OncScrollTable assessments = null;
	private OncScrollList interpretations = null;
	private OncScrollableTextArea quote = null;
	private OncIntegerTextField versionNumber = null;
	private OncScrollList keywords = null;
	private OncScrollList informationSource = null;


	public  QuoteNuggetPanel() {
		super();
		editObj = new QuoteNugget();
		initUI();
		fillUiHashtable();
	}


	public  QuoteNuggetPanel(QuoteNugget editObj) {
		super();
		this.editObj = editObj;
		initUI();
		fillUiHashtable();
	}
	public void initUI(Object editObj) {
	this.editObj = (QuoteNugget)editObj;
	initUI();
	}
	private void initUI() {
		informationSource = new OncScrollList(InformationSource.class, editObj, "InformationSource", true,true, false);
		versionNumber = new OncIntegerTextField(editObj, "VersionNumber", true);
		quote = new OncScrollableTextArea(editObj, "Quote", true);
		bookmarkLocation = new OncIntegerTextField(editObj, "BookmarkLocation", true);
		Object[] comboBoxList1 = {"Abstract","Introduction","Methods","Results","Discussion"};
		quoteSection = new OncComboBox(editObj, "QuoteSection", true, comboBoxList1);
		relevanceAssessments = new OncScrollList(RelevanceItem.class, editObj, "RelevanceAssessments", true,true);
		confidenceAssessments = new OncScrollList(ConfidenceItem.class, editObj, "ConfidenceAssessments", true,true);
		assessments = new OncScrollTable(AssessmentItem.class, editObj, "Assessments", "assessments", true,true);
		interpretations = new OncScrollList(Interpretation.class, editObj, "Interpretations", true,true);
		keywords = new OncScrollList(Keyword.class, editObj, "Keywords", true,true);
informationSource.setBounds(10,120,680,60);
versionNumber.setBounds(0,590,125,60);
quote.setBounds(10,0,680,120);
bookmarkLocation.setBounds(140,260,125,60);
quoteSection.setBounds(10,260,125,60);
assessments.setBounds(300,290,410,160);
interpretations.setBounds(10,180,680,80);
keywords.setBounds(10,330,250,120);
informationSource.setVisible(true);
versionNumber.setVisible(true);
quote.setVisible(true);
bookmarkLocation.setVisible(true);
quoteSection.setVisible(true);
relevanceAssessments.setVisible(true);
confidenceAssessments.setVisible(true);
assessments.setVisible(true);
interpretations.setVisible(true);
keywords.setVisible(true);
		add(informationSource);
		add(versionNumber);
		add(quote);
		add(bookmarkLocation);
		add(quoteSection);
		add(relevanceAssessments);
		add(confidenceAssessments);
		add(assessments);
		add(interpretations);
		add(keywords);
	}
	private void fillUiHashtable() {
		uiHashtable = new Hashtable();
		uiHashtable.put(informationSource, "informationSource");
		uiHashtable.put(versionNumber, "versionNumber");
		uiHashtable.put(quote, "quote");
		uiHashtable.put(bookmarkLocation, "bookmarkLocation");
		uiHashtable.put(quoteSection, "quoteSection");
		uiHashtable.put(relevanceAssessments, "relevanceAssessments");
		uiHashtable.put(confidenceAssessments, "confidenceAssessments");
		uiHashtable.put(assessments, "assessments");
		uiHashtable.put(interpretations, "interpretations");
		uiHashtable.put(keywords, "keywords");
	}
	public Object getValue(){ return null; }
	public static void main(String[] args) {
		JFrame f = new JFrame();
		QuoteNuggetPanel p = new QuoteNuggetPanel();
		f.getContentPane().add(p);
		f.setSize(500,500);
		f.setVisible(true);
	}
}
