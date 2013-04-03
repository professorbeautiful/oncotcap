package oncotcap.display.modelcontroller.cellkineticsdemo;

import oncotcap.display.common.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.persistible.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class EditorTest extends JFrame implements ActionListener
{
	private StatementTemplateEditorPane step = new StatementTemplateEditorPane();
	private HTMLEditorPane editorPane = new HTMLEditorPane();
	private JScrollPane sp = new JScrollPane(editorPane);
	private JButton btnResize = new JButton("Resize");
	private Container cp;
	
	public EditorTest()
	{
		init();
	}

	public static void main(String [] args)
	{
		EditorTest et = new EditorTest();
		et.setVisible(true);
	}

	private void init()
	{
		setSize(800,600);
		cp = getContentPane();
		cp.setLayout(null);
		btnResize.addActionListener(this);
		btnResize.setLocation(0,0);
		btnResize.setSize(100,30);
		sp.setLocation(0,31);
		sp.setBorder(BorderFactory.createLineBorder(Color.blue, 2));
		editorPane.setBorder(BorderFactory.createLineBorder(Color.red,2));
		step.setLocation(0,31);
		step.setSize(100,100);
		cp.add(step);
//		cp.add(sp);
		cp.add(btnResize);
	}

	public void setWidth(int width)
	{
		int height = 50;
		editorPane.setSize(width, 2000);
		try{height = (int) editorPane.modelToView(editorPane.getDocument().getLength() - 1).getY() + 25;}
		catch(BadLocationException e){}
		editorPane.setSize(width, height);
		sp.setSize(width + 1, height + 1);
		sp.revalidate();
		editorPane.revalidate();
		cp.repaint();
	}
	public void actionPerformed(ActionEvent e)
	{
		step.setWidth(200);
		cp.repaint();
		System.out.println(step.getSize());
	}
}
