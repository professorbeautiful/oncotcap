package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Hashtable;
import java.awt.event.*;
import oncotcap.util.GUID;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.DefaultPersistibleList;
import oncotcap.display.browser.OncBrowser;
import oncotcap.display.browser.TreeDisplayModePanel;
import oncotcap.util.CollectionHelper;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.display.common.KeywordChooser;
import oncotcap.display.common.OncTreeNode;

public class DeclareTwoLevelParameterEditor extends EditorPanel 
		{
	private DeclareTwoLevelParameter twoLevelParameter;
	private KeywordChooser keywordChooser;
	private JTextField nameField;
				private Keyword rootKeyword = KeywordFilter.CHARACTERISTIC_ROOT;

	public DeclareTwoLevelParameterEditor()
	{
		init();
	}
	public DeclareTwoLevelParameterEditor(DeclareTwoLevelParameter picker)
	{
		init();
		edit(picker);
	}
	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		jf.setSize(300,300);
		jf.getContentPane().add(new DeclareTwoLevelParameterEditor(new DeclareTwoLevelParameter()));
		jf.setVisible(true);
	}
	private void init()
	{
		setLayout(null);
		setPreferredSize(new Dimension(430,100));
		JLabel nameLabel = new JLabel("Display name");
		JLabel subLabel1 = new JLabel("Subset will be defined by");
		JLabel subLabel2 = new JLabel("attributes of type:");
		nameField = new JTextField();
		nameField.setMaximumSize(new Dimension(130, 25));

		keywordChooser = new KeywordChooser();
		keywordChooser.setRootKeyword(rootKeyword);
		nameLabel.setSize(100,25);
		nameField.setSize(115,25);
		keywordChooser.setSize(200, 25);
		nameLabel.setLocation(40,10);
		nameField.setLocation(170,10);
		subLabel1.setSize(155,25);
		subLabel2.setSize(155,25);
		subLabel1.setLocation(10,15);
		subLabel2.setLocation(10,30);
		keywordChooser.setLocation(175, 22);
		
		add(subLabel1);
		add(subLabel2);
		add(keywordChooser);
	}
	public void edit(Object obj)
	{
		if(obj instanceof DeclareTwoLevelParameter)
			edit((DeclareTwoLevelParameter) obj);
	}
	public void edit(DeclareTwoLevelParameter picker)
	{
		twoLevelParameter = picker;
		Keyword keyword = twoLevelParameter.getConstraintKeyword();
		if ( keyword != null ) {
				keywordChooser.setKeyword(keyword);
				keywordChooser.setRootKeyword(keyword);
		}
		nameField.setText(twoLevelParameter.toString());
	}

	public Object getValue()
	{
		return(twoLevelParameter);
	}

	public void save()
	{
			// Set the contraint keyword 
			DefaultPersistibleList constraints = new  DefaultPersistibleList();
			if ( keywordChooser != null ) {
					constraints.add(keywordChooser.getKeyword());
					//System.out.println("Save constraint: " + keywordChooser.getKeyword());
					twoLevelParameter.setKeywords(constraints);
			}
			twoLevelParameter.update();
	}
}
