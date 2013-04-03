package oncotcap.display.editor.persistibleeditorpanel;

import oncotcap.display.common.LabeledTextBox;

public abstract class SingleValueEditorPanel extends EditorPanel
{

	protected LabeledTextBox valueBox;

	public SingleValueEditorPanel()
	{
		init();
	}
	private void init()
	{
		valueBox = new LabeledTextBox(" ");
		add(valueBox);
	}
	protected void setLabel(String label)
	{
		valueBox.setLabel(label);
		valueBox.repaint();
	}
	protected void setText(String text)
	{
		valueBox.setText(text);
	}
	protected String getText()
	{
		return(valueBox.getText());
	}
}