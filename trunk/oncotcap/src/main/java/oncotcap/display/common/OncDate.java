package oncotcap.display.common;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.Button;
import javax.swing.text.html.HTMLEditorKit;
import java.util.Calendar;

  
 public class OncDate extends JFrame implements ActionListener{
	private boolean laidOut = false;
	JPanel contentPane, windowPane,buttonPane;
	HTMLEditorKit htmlKit;
	JScrollPane history;
	JEditorPane txtHistory;
	Button btnDate;
	Button btnDateChanged;
	Calendar cal;
	String str = new String();
	int a;
	StringBuffer str1 = new StringBuffer();
	public OncDate()
	{
		contentPane=(JPanel)getContentPane();
        contentPane.setLayout(null);
		contentPane.setPreferredSize(new Dimension(800, 600));
		
		

		Rectangle bnds = getGraphicsConfiguration().getBounds();
		setLocation((((int) bnds.getWidth()) - getWidth())/2, (((int) bnds.getHeight())- getHeight())/2);
		setIconImage(oncotcap.util.OncoTcapIcons.getDefault().getImage());
 		                
		windowPane = new JPanel();
		windowPane.setBorder(BorderFactory.createTitledBorder(" Date Display "));
		txtHistory = new JEditorPane();
		htmlKit = new HTMLEditorKit();
		txtHistory.setEditorKit(htmlKit);
		txtHistory.setContentType("text/html");
		history = new JScrollPane(txtHistory);
		history.setVisible(true);		
		txtHistory.setEditable(true);
		contentPane.add(windowPane);
		windowPane.add(history);
	
						 
		buttonPane = new JPanel();
		buttonPane.setLayout(null);
		buttonPane.setBorder(BorderFactory.createTitledBorder(" Action Buttons "));
		btnDate = new Button("Original date");
		btnDateChanged = new Button("Changed date");
		contentPane.add(buttonPane);
		buttonPane.add(btnDate);
		buttonPane.add(btnDateChanged);
		btnDate.addActionListener(this);
		btnDateChanged.addActionListener(this);
	
		cal = Calendar.getInstance();
		Insets insets = contentPane.getInsets();
		windowPane.setBounds(10,16,450,540);
		buttonPane.setBounds(420,16,300,535);
	
		Insets insets1 = buttonPane.getInsets();
		btnDate.setBounds(80,25,160,30);
		btnDateChanged.setBounds(80,65,160,30);
	
		addComponentListener(new ResizeListener());
		setSize(800,600);
		dateFormat();
		str1 = str1.append("<B><I>").append(str).append(cal.get(Calendar.DAY_OF_MONTH)).append(",").append(cal.get(Calendar.YEAR));
		
	}
	public void actionPerformed(ActionEvent e){

		if (e.getSource()==btnDate){
			txtHistory.repaint();
			txtHistory.revalidate();
			dateFormat();
			txtHistory.setText(" "+str1+" ");
			//txtHistory.setText("<B><I>"+"  "+str+cal.get(Calendar.DAY_OF_MONTH)+","+cal.get(Calendar.YEAR)+"</B></I><br>");
			
			txtHistory.repaint();
			txtHistory.revalidate();
			
				
			
		}
		if(e.getSource()==btnDateChanged){
			addDays(30);
			dateFormat();
			
			txtHistory.setText(str1+"<B><I>"+"  "+"<BR>"+str+cal.get(Calendar.DAY_OF_MONTH)+","+cal.get(Calendar.YEAR)+"</B></I><br>");
			txtHistory.repaint();
			txtHistory.revalidate();
		}
	}
	
	public void resize(){
	
		windowPane.setSize((getWidth()*50/100),getHeight()-65);
		history.setPreferredSize(new Dimension((windowPane.getWidth()-20),getHeight()-100));
		buttonPane.setLocation(getWidth()-windowPane.getWidth()+30,16);
		buttonPane.setSize((getWidth()*44/100),getHeight()-65);
	
	}
	private class ResizeListener implements ComponentListener{
	
		public void componentHidden(ComponentEvent e) {}
		public void componentMoved(ComponentEvent e) {}
		public void componentResized(ComponentEvent e){resize();}
		public void componentShown(ComponentEvent e) {}

	}

	public void addDays(int daysToAdd)
	{
		Date dte = cal.getTime();
		dte.setTime(dte.getTime() + ((long)daysToAdd) * 1000 * 60 * 60 * 24);
		cal.setTime(dte);
	}

	public void dateFormat()
	{
		int a;
		
	    a = cal.get(Calendar.MONTH);
		if (a == 0)
			str = "January";
			else if (a==1)
				str ="February";
			else if (a==2)
				str="March";
			else if (a==3)
				str="April";
			else if (a==4)
				str="May";
			else if (a==5)
				str="June";
			else if (a==6)
				str="July";
			else if(a==7)
				str="August";
			else if(a==8)
				str="September";
			else if(a==9)
				str="October";
			else if(a==10)
				str="November";
			else if (a==11)
				str = "December";
	    }

	public static final void main(String [] args)
	{
		OncDate od = new OncDate();
		od.setSize(800,600);
		od.setVisible(true);
	}

}


