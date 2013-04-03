package oncotcap.display.common;

import javax.swing.*;
import java.awt.Dimension;

public class LabeledTextBox extends JPanel
{
	private static final int height = 40;
	JLabel title;
	JTextField textBox;
	public LabeledTextBox()
	{
		textBox = new JTextField();
		init("");
	}
	public LabeledTextBox(String title)
	{
		textBox = new JTextField();
		init(title);
	}
	public LabeledTextBox(String title, String displayText)
	{
		textBox = new JTextField(displayText);
		init(title);
	}

	private void init(String strTitle)
	{
		title = new JLabel(strTitle);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		title.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		textBox.setAlignmentX(JComboBox.LEFT_ALIGNMENT);
		add(title);
		add(textBox);
	}
	public void setLabel(String label)
	{
		title.setText(label);
	}
	public String getText()
	{
		return(textBox.getText());
	}

	public void setText(String val)
	{
		textBox.setText(val);
	}
	public void setTextBoxWidth(int wid)
	{
		textBox.setMaximumSize(new Dimension(wid, Short.MAX_VALUE));
	}
}