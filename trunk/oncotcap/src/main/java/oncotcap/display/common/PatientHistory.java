package oncotcap.display.common;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.awt.Button;
import javax.swing.text.html.HTMLEditorKit;
import java.util.Calendar;
  
  

						 
public class PatientHistory extends JFrame implements ActionListener {
	private boolean laidOut = false;
	JPanel contentPane, windowPane,buttonPane;
	HTMLEditorKit htmlKit;
	JScrollPane history;
	Calendar rightNow = Calendar.getInstance();
	String str1 = new String();
	String str = new String();
	StringBuffer str2 = new StringBuffer();
	String str3 = new String();
	String str4 = new String();
	JEditorPane txtHistory;
	Button btnCheck1;
	Button btnCheck2;
	public PatientHistory(){
		contentPane=(JPanel)getContentPane();
        contentPane.setLayout(null);
		contentPane.setPreferredSize(new Dimension(800, 600));
		
		

		Rectangle bnds = getGraphicsConfiguration().getBounds();
		setLocation((((int) bnds.getWidth()) - getWidth())/2, (((int) bnds.getHeight())- getHeight())/2);
		setIconImage(oncotcap.util.OncoTcapIcons.getDefault().getImage());
 		                
		
	
	
	windowPane = new JPanel();
	windowPane.setBorder(BorderFactory.createTitledBorder(" Initial Presentation "));
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
	btnCheck1 = new Button("Check 1");
	btnCheck2 = new Button("Check 2");
	contentPane.add(buttonPane);
	buttonPane.add(btnCheck1);
	buttonPane.add(btnCheck2);
	btnCheck1.addActionListener(this);
	btnCheck2.addActionListener(this);
	
	int a;
		
	    a = rightNow.get(Calendar.MONTH);
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
	txtHistory.setText("<B><I>"+"  "+str+rightNow.get(Calendar.DAY_OF_MONTH)+","+rightNow.get(Calendar.YEAR)+"</B></I><br>"+ "<b>End of Medical Report<br>");
	
	Insets insets = contentPane.getInsets();
	windowPane.setBounds(10,16,450,540);
	buttonPane.setBounds(420,16,300,535);
	
	Insets insets1 = buttonPane.getInsets();
	btnCheck1.setBounds(80,25,160,30);
	btnCheck2.setBounds(80,65,160,30);
	
	addComponentListener(new ResizeListener());
	setSize(800,600);
	}
		
	class myadapter extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			setVisible(false);
		}
}
	public void addText(){
		str2 = str2.append("<br>").append(str3).append("<br>").append(str4).append("<br>");
			txtHistory.setText("<B><I><br>"+"  "+ str + rightNow.get(Calendar.DAY_OF_MONTH)+","+rightNow.get(Calendar.YEAR)+"</B></I><br>"+ str2+"<br>"+ "<b>End of Medical Report<br>");
	}
	public void AddDate(int offset,int days){
	}
	public void actionPerformed(ActionEvent e){

		if (e.getSource()==btnCheck1){
			str3 = "Check 1 Button :";
			str4 = "It checks the working of this object";
			addText();
			
		}
		if(e.getSource()==btnCheck2){
			str3 = "Check 2 Button :";
			str4 = "This confirms the working";
			addText();
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
	public static void main(String args[]) throws IOException {
		PatientHistory ph=new PatientHistory();
		ph.setSize(800,600);
		ph.setVisible(true);

	}
}
