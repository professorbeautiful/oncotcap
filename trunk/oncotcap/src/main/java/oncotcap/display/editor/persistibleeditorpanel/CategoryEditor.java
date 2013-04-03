package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.display.common.KeywordChooser;
import oncotcap.display.common.KeywordChooser2;
import oncotcap.display.editor.EditorFrame;

public class CategoryEditor extends EditorPanel
{
	private KeywordChooser2 constraintKeyword;
	private DeclareCategory decCat;
	
	public CategoryEditor()
	{
		init();
	}
	public static void main(String [] args)
	{
		EditorFrame.showEditor(new DeclareCategory());
	}
	void init()
	{
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(400,400));
		constraintKeyword = new KeywordChooser2();
		add(new JLabel("Category constraint"), BorderLayout.NORTH);
		add(constraintKeyword, BorderLayout.CENTER);
		//constraintKeyword.setRootKeyword(KeywordFilter.CHARACTERISTIC_ROOT);
//		Box constraintBox = Box.createHorizontalBox();
//		constraintBox.add(Box.createHorizontalStrut(5));
//		constraintBox.add(new JLabel("Category constraint"));
//		constraintBox.add(Box.createHorizontalStrut(10));
//		constraintBox.add(constraintKeyword);
//		constraintBox.add(Box.createHorizontalGlue());
//		add(Box.createVerticalStrut(5));
//		add(constraintBox);
//		add(Box.createVerticalStrut(10));
	}
	public void edit(Object var)
	{
		if(var instanceof DeclareCategory)
		{
			edit((DeclareCategory) var);
		}
	}
	public void edit(DeclareCategory var)
	{
		decCat = var;
		constraintKeyword.setKeyword(var.getTopKeyword());
	}
	public Object getValue()
	{
		return(decCat);
	}
	public void save()
	{
		//note: the property list inside the enum variable gets updated at
		//the same time as the enumTable-  Below in the HyperLabel
		//listeners for the add and remove property labels....
		if(decCat == null)
			decCat = new DeclareCategory();		
		decCat.setTopKeyword(constraintKeyword.getKeyword());
	}
}
