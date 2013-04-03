package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 
import java.util.*;
import oncotcap.display.common.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.util.*;

public class LevelListEditorPanel extends EditorPanel
	 {
	EnumLevelList levels = null;
	Box listBox;
	Box minMaxBox;
	KeywordChooser2 keywordChooser = null;
	JCheckBox orderedCheck = new JCheckBox("Ordered List");
	JCheckBox numericCheck = new JCheckBox("Numeric List");
	JRadioButton addButton = new JRadioButton("Add");
	JRadioButton multButton = new JRadioButton("Multiply");

//	JButton testButton = new JButton("TEST");
	
	OncList levelList = new OncList();
	JButton levelUp = new JButton(OncoTcapIcons.getImageIcon("uparrow.gif"));
	JButton levelDown = new JButton(OncoTcapIcons.getImageIcon("downarrow.gif"));
	LabeledTextBox minField = new LabeledTextBox("MIN");
	LabeledTextBox maxField = new LabeledTextBox("MAX");
	LabeledTextBox incField = new LabeledTextBox("INCREMENT");
	HyperLabel addLabel = new HyperLabel("Add Level");
	HyperLabel removeLabel = new HyperLabel("Remove Level");

	Keyword endingKeyword = null;
	
	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		jf.setSize(800,600);
		LevelListEditorPanel ll = new LevelListEditorPanel();
		EnumLevelList list = (EnumLevelList) oncotcap.Oncotcap.getDataSource().find(new GUID("888e66a300000608000000fac9727952"));
		ll.edit(list);
		jf.getContentPane().add(ll);
		jf.setVisible(true);
	}
	public LevelListEditorPanel()
	{
		init();
	}
	public LevelListEditorPanel(Keyword kw)
	{
		setEndingKeyword(kw);
		init();
		
	}
	public LevelListEditorPanel(EnumLevelList ll) {
		if ( ll != null )
			this.endingKeyword = ll.getKeyword();
		init();
		edit(ll);
	}
		
	private void init()
	{
		keywordChooser = new KeywordChooser2(this.endingKeyword);
		keywordChooser.setPreferredSize(new Dimension(300, 300));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(Box.createVerticalStrut(5));
		if (endingKeyword == null )
			keywordChooser.setRootKeyword(KeywordFilter.CHARACTERISTIC_ROOT);
		else
			keywordChooser.setRootKeyword(endingKeyword);
		add(keywordChooser);
		
		add(Box.createHorizontalStrut(10));

		Box optionBox = Box.createHorizontalBox();
		orderedCheck.setSelected(true);
		orderedCheck.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {updateBoxes();}});
		optionBox.add(orderedCheck);
		optionBox.add(Box.createHorizontalStrut(10));
		optionBox.add(numericCheck);
		numericCheck.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){updateBoxes();}});
		optionBox.add(Box.createHorizontalGlue());
		add(Box.createHorizontalStrut(10));
		add(optionBox);
		
		add(Box.createHorizontalStrut(10));
		listBox = Box.createVerticalBox();
		listBox.setPreferredSize(new Dimension(300,400));
		Box addRemoveBox = Box.createHorizontalBox();
		addRemoveBox.add(Box.createVerticalGlue());
		addRemoveBox.add(addLabel);
		addLabel.addHyperListener(new HyperListener(){public void mouseActivated(MouseEvent e){
			String enumProp = InputDialog.show(null, "Enter a level name").getInput();
			if(enumProp != null)
			{
				int nextIdx = 0;
				EnumLevel lastLevel = (EnumLevel)levels.getLevels().getLast();
				if(lastLevel != null)
					nextIdx = lastLevel.getListIndex() + 1;
				levels.getLevels().add(new EnumLevel(enumProp, nextIdx));
			}
		}});
		addRemoveBox.add(Box.createHorizontalStrut(10));

		removeLabel.addHyperListener(new HyperListener(){public void mouseActivated(MouseEvent e){
			if(levelList.getSelectedIndex() > 0)
			{
				int idx = levelList.getSelectedIndex();
				EnumLevel level = (EnumLevel) levels.getLevels().get(idx);
				levels.getLevels().remove(level);
			}

		}});

		addRemoveBox.add(removeLabel);
		
		Box innerListBox = Box.createHorizontalBox();
		JScrollPane levelListSP = new JScrollPane(levelList);
//		levelListSP.setPreferredSize(new Dimension(500, 300));
		innerListBox.add(levelListSP);
		Box upDownBox = Box.createVerticalBox();
		levelUp.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
			if(levelList.getSelectedIndex() > 0)
			{
				int idx = levelList.getSelectedIndex();
				EnumLevel topLevel = (EnumLevel) levels.getLevels().get(idx - 1);
				EnumLevel botLevel = (EnumLevel) levels.getLevels().get(idx);
				int topIndex = topLevel.getListIndex();
				topLevel.setListIndex(botLevel.getListIndex());
				botLevel.setListIndex(topIndex);
				levels.getLevels().resort();
				levelList.setSelectedIndex(idx - 1);
			}

		}});

		levelUp.setPreferredSize(new Dimension(30,50));
		levelUp.setMaximumSize(new Dimension(30,50));
		levelUp.setMinimumSize(new Dimension(30,50));

		levelDown.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
			if(levelList.getSelectedIndex() < (levels.getLevels().size() - 1))
			{
				int idx = levelList.getSelectedIndex();
				EnumLevel topLevel = (EnumLevel) levels.getLevels().get(idx);
				EnumLevel botLevel = (EnumLevel) levels.getLevels().get(idx + 1);
				int topIndex = topLevel.getListIndex();
				topLevel.setListIndex(botLevel.getListIndex());
				botLevel.setListIndex(topIndex);
				levels.getLevels().resort();
				levelList.setSelectedIndex(idx + 1);
			}

		}});
		levelDown.setPreferredSize(new Dimension(30,50));
		levelDown.setMaximumSize(new Dimension(30,50));
		levelDown.setMinimumSize(new Dimension(30,50));
		upDownBox.add(levelUp);
		upDownBox.add(Box.createVerticalGlue());
		upDownBox.add(levelDown);
		innerListBox.add(upDownBox);
		innerListBox.add(Box.createHorizontalGlue());
		listBox.add(addRemoveBox);
		listBox.add(Box.createVerticalStrut(10));
		listBox.add(innerListBox);
		listBox.add(Box.createVerticalGlue());
		add(listBox);

		minMaxBox = Box.createVerticalBox();
		Box minBox = Box.createHorizontalBox();
		minField.setPreferredSize(new Dimension(100, 40));
		minField.setMaximumSize(new Dimension(100, 40));
		minBox.add(minField);
		minBox.add(Box.createHorizontalGlue());
		minMaxBox.add(minBox);
		Box maxBox = Box.createHorizontalBox();
		maxField.setPreferredSize(new Dimension(100, 40));
		maxField.setMaximumSize(new Dimension(100, 40));
		maxBox.add(maxField);
		maxBox.add(Box.createHorizontalGlue());
		minMaxBox.add(maxBox);
		Box incBox = Box.createHorizontalBox();
		incField.setPreferredSize(new Dimension(100, 40));
		incField.setMaximumSize(new Dimension(100, 40));
		incBox.add(incField);
		incBox.add(Box.createHorizontalGlue());
		minMaxBox.add(incBox);
		
		addButton.setSelected(true);
		Box addBox = Box.createHorizontalBox();
		addBox.add(addButton);
		addBox.add(Box.createHorizontalGlue());
		minMaxBox.add(addBox);
		multButton.setSelected(false);
		Box multBox = Box.createHorizontalBox();
		multBox.add(multButton);
		multBox.add(Box.createHorizontalGlue());
		minMaxBox.add(multBox);

		ButtonGroup addMultGroup = new ButtonGroup();
		addMultGroup.add(addButton);
		addMultGroup.add(multButton);

		minMaxBox.add(Box.createVerticalGlue());
		minMaxBox.setVisible(false);
		add(minMaxBox);
		
		add(Box.createVerticalGlue());
		Box keywordBox = Box.createHorizontalBox();
		keywordBox.add(Box.createHorizontalStrut(10));
		keywordBox.add(new JLabel("Level list will be attached to chosen Keyword"));
		keywordBox.add(Box.createVerticalStrut(5));
		keywordBox.add(Box.createHorizontalGlue());
		add(keywordBox);
		setSize(500,600);
	}
		//add(testButton);
/*		testButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				save();
				levels.update();
				oncotcap.Oncotcap.getDataSource().commit();
				
				Iterator it = levels.getLevelIterator();
				while(it.hasNext())
					System.out.println(it.next());

				System.out.println("----------------------------------"); 
			}

		});
	}
*/	private void updateBoxes()
	{
		listBox.setVisible(!orderedCheck.isSelected() || !numericCheck.isSelected());
		minMaxBox.setVisible(orderedCheck.isSelected() && numericCheck.isSelected());
		levelUp.setVisible(orderedCheck.isSelected());
		levelDown.setVisible(orderedCheck.isSelected());
		numericCheck.setVisible(orderedCheck.isSelected());
	}

	public void setEndingKeyword(Keyword k) {
			endingKeyword = k;
			keywordChooser.setEndingKeyword(k);
			keywordChooser.setKeyword(k);
			System.out.println("setEndingKeyword: " + keywordChooser.getKeyword() + " " + k);
			levels.setKeyword(k);
	}
	public void edit(Object obj)
	{
		if(obj instanceof EnumLevelList)
			edit((EnumLevelList) obj);
	}
	public void edit(EnumLevelList levelList)
	{
		keywordChooser.setKeyword(levelList.getKeyword());
		this.levels = levelList;
		this.levelList.setModel(levelList.getLevels());
		orderedCheck.setSelected(levelList.isOrdered());
		numericCheck.setSelected(levelList.isNumericList());
		addButton.setSelected(levelList.isArithmatic());
		multButton.setSelected(!levelList.isArithmatic());
		minField.setText(levelList.getMinAsString());
		maxField.setText(levelList.getMaxAsString());
		incField.setText(levelList.getIncrementAsString());
		updateBoxes();
	}

	public void save()
	{
		if(levels == null)
			return;

		levels.setKeyword(keywordChooser.getKeyword());
		levels.setOrdered(orderedCheck.isSelected());
		levels.setNumericList(numericCheck.isSelected());
		levels.setArithmatic(addButton.isSelected());
		levels.setMin(minField.getText());
		levels.setMax(maxField.getText());
		levels.setIncrement(incField.getText());
		System.out.println("Saving level " + levels + " to keyword " + levels.getKeyword());
	}
	public Object getValue()
	{
		return(levels);
	}
}
