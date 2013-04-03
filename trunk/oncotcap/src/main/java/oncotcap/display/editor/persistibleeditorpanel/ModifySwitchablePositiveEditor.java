package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.Dimension;
import java.awt.event.*;

import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.datalayer.persistible.parameter.*;

public class ModifySwitchablePositiveEditor extends AbstractVariableEditor implements ModifyVariableEditor
{
	private JTextArea modification = new JTextArea();
	private JLabel posModifier = new JLabel("*=");
	private JLabel posVarName = new JLabel();
	private JCheckBox switchControl = new JCheckBox();
	private JLabel switchModifier = new JLabel();
	private JLabel switchVarName = new JLabel();
	
	public ModifySwitchablePositiveEditor()
	{
		init();
	}
	private void init()
	{
/*		Box mainBox = Box.createVerticalBox();
		JPanel switchPanel = new JPanel();
		JPanel posPanel = new JPanel();
		modification.setPreferredSize(new Dimension(100,25));
		modification.setMinimumSize(new Dimension(100,25));
		switchPanel.add(switchVarName);
		switchPanel.add(switchModifier);
		switchPanel.add(switchBox);
		posPanel.add(posVarName);
		posPanel.add(posModifier);
		posPanel.add(modification);
		mainBox.add(switchPanel);
		mainBox.add(posPanel);
		add(mainBox);
		switchBox.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 checkState();
			 }
		}); */

		setLayout(new BorderLayout(10,10));
		Box topBox = Box.createVerticalBox();
		Box switchBox = Box.createHorizontalBox();
		switchBox.add(switchVarName);
		switchBox.add(switchModifier);
		switchBox.add(switchControl);
		switchBox.add(Box.createHorizontalGlue());
		Box varBox = Box.createHorizontalBox();
		varBox.add(posVarName);
		varBox.add(posModifier);
		varBox.add(Box.createHorizontalGlue());
		topBox.add(switchBox);
		topBox.add(varBox);
		modification.setLineWrap(true);
		modification.setWrapStyleWord(true);
		JScrollPane modificationSP = new JScrollPane(modification, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(topBox, BorderLayout.NORTH);
		add(Box.createHorizontalStrut(40), BorderLayout.WEST);
		add(modificationSP, BorderLayout.CENTER);

		switchControl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkState();
			}
		});

	}

	public void editAction(ModifyVariableAction modVar)
	{
		Persistible origVar = modVar.getVariable();
		VariableModification vm = modVar.getModification();
		if(vm != null && vm instanceof ModifySwitchablePositive && origVar != null && origVar instanceof VariableInstance)
		{
				ModifySwitchablePositive mod = (ModifySwitchablePositive) vm;
				VariableInstance var = (VariableInstance) origVar;
				switchVarName.setText("Turn " + var.getName() + " ");
				if(var instanceof DeclareSwitchablePositive)
				{
					if(((DeclareSwitchablePositive)var).getInitialState())
						switchModifier.setText("OFF");
					else
						switchModifier.setText("ON");
				}
				else
					switchModifier.setText("ON");

				switchControl.setSelected(mod.getSwitch());

				posVarName.setText(var.getName());
				modification.setText(mod.getModification());
				checkState();
		}
	}
	
	//check the state of the variable and modification
	//if the current state (variable state + modification) is ON, make
	//the positive variable stuff available for modification
	private void checkState()
	{
		if(modifyAction != null && modifyAction.getModification() != null && modifyAction.getModification() instanceof ModifySwitchablePositive)
		{
			if(modifyAction.getVariable() != null && modifyAction.getVariable() instanceof DeclareSwitchablePositive)
			{
				DeclareSwitchablePositive var = (DeclareSwitchablePositive) modifyAction.getVariable();

				//if the current combined state is "ON"
				if( var.getInitialState() && ! switchControl.isSelected() ||
					 ! var.getInitialState() && switchControl.isSelected() )
				{
					modification.setVisible(true);
					posModifier.setVisible(true);
					posVarName.setVisible(true);
				}
				else
				{
					modification.setVisible(false);
					posModifier.setVisible(false);
					posVarName.setVisible(false);
				}
			}
		}
	}
	public void save()
	{
		((ModifySwitchablePositive) modifyAction.getModification()).setSwitch(switchControl.isSelected());
		((ModifySwitchablePositive) modifyAction.getModification()).setModification(modification.getText());
	}
	public VariableModification getVariableModification()
	{
		return(modifyAction.getModification());
	}
}