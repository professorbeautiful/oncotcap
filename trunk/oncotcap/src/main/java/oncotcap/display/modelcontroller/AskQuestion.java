package oncotcap.display.modelcontroller;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import oncotcap.util.*;

public class AskQuestion extends JFrame implements ActionListener {
	private boolean laidOut = false;
	int i=0;
	JLabel lblIntro,lblComment,lblName,lblCommunication,lblStore;
	JTextField txtName;
	JTextArea txtComment;
	JRadioButton rbStoreToDisk,rbNetMeeting,rbEmail;
	JRadioButton rb4,rb5,rb6;
	ButtonGroup grp = new ButtonGroup();
	ButtonGroup grp1 = new ButtonGroup();
	JButton btnSave;
	String strComment,strName;
	String path = new String();

	public static boolean fileExists(String fileName){
		File f = new File(fileName);
		return(f.exists());
	}

	public AskQuestion(){

		setTitle("COMMENTS");
		JPanel contentpane=(JPanel)getContentPane();
		setSize(800,600);
		Rectangle bnds = getGraphicsConfiguration().getBounds();
		setLocation((((int) bnds.getWidth()) - getWidth())/2, (((int) bnds.getHeight())- getHeight())/2);
		setIconImage(oncotcap.util.OncoTcapIcons.getDefault().getImage());

		contentpane.setLayout(null);
		lblIntro = new JLabel("What is your question or comment?");
		lblIntro.setFont(new Font("Comic Sans",Font.BOLD+Font.ITALIC,22));
		contentpane.add(lblIntro);

		lblComment = new JLabel("Comments...");
		lblComment.setFont(new Font("Arial",Font.BOLD,18));
		contentpane.add(lblComment);

		lblName = new JLabel("User Name");
		lblName.setFont(new Font("Arial",Font.BOLD,20));
		contentpane.add(lblName);

		txtName=new JTextField(50);
		txtName.setFont(new Font("Arial",Font.PLAIN,12));
		contentpane.add(txtName);

		txtComment= new JTextArea(10,30);
		txtComment.setFont(new Font("Comic Sans",Font.PLAIN,14));
		contentpane.add(txtComment);

		lblCommunication = new JLabel("Communications mode......");
		lblCommunication.setFont(new Font("Arial",Font.BOLD,20));
		contentpane.add(lblCommunication);

		rbStoreToDisk=new JRadioButton("Store to disk");
		rbStoreToDisk.addActionListener(this);
		rbStoreToDisk.setActionCommand("One Activated");
		rbStoreToDisk.setSelected(true);
		contentpane.add(rbStoreToDisk);

		rbNetMeeting= new JRadioButton("Live internet Connection via NetMeeting");
		//rbNetMeeting.addActionListener(this);
		rbNetMeeting.setActionCommand("Two Activated");
		rbNetMeeting.setEnabled(false);
		contentpane.add(rbNetMeeting);

		rbEmail=new JRadioButton("E-Mail");
		//rbEmail.addActionListener(this);
		rbEmail.setActionCommand("Three Activated");
		rbEmail.setEnabled(false);
		contentpane.add(rbEmail);

		lblStore = new JLabel("Direct Question to......");
		lblStore.setFont(new Font("Arial",Font.BOLD,20));
		contentpane.add(lblStore);


		rb4=new JRadioButton("Instructor");
		rb4.addActionListener(this);
		rb4.setActionCommand("One Activated");
		rb4.setSelected(true);
		contentpane.add(rb4);

		rb5=new JRadioButton("Technical Staff");
		//rb5.addActionListener(this);
		rb5.setActionCommand("Two Activated");
		rb5.setEnabled(false);
		contentpane.add(rb5);

		rb6=new JRadioButton("OncoTCap research oncologist");
		//rb6.addActionListener(this);
		rb6.setActionCommand("Three Activated");
		rb6.setEnabled(false);
		contentpane.add(rb6);

		btnSave = new JButton("OK");
		btnSave.addActionListener(this);
		contentpane.add(btnSave);

		strComment = new String();
		grp.add(rbStoreToDisk);
		grp.add(rbNetMeeting);
		grp.add(rbEmail);
		grp1.add(rb4);
		grp1.add(rb5);
		grp1.add(rb6);

		Insets insets = contentpane.getInsets();

		lblIntro.setBounds(25, 5 , 700, 60);
		lblComment.setBounds(25, 110 , 120, 60);
		lblName.setBounds(25,50,120,60); 
		txtName.setBounds(180,70,120,30);
		txtComment.setBounds(180, 120, 350, 90);
		lblCommunication.setBounds(25, 240, 500, 60);
		rbStoreToDisk.setBounds(30, 280,300, 60);
		rbNetMeeting.setBounds(30, 320,300, 60);
		rbEmail.setBounds(30, 360,300, 60);
		lblStore.setBounds(25, 400,300, 60);
		rb4.setBounds(30, 440,300, 60);
		rb5.setBounds(30, 480,300, 60);
		rb6.setBounds(30, 520,300, 60);
		btnSave.setBounds(500, 500,75, 60);


		myadapter myapp= new myadapter();
		addWindowListener(myapp);
	}

	class myadapter extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			setVisible(false);
		}
	}

	public void actionPerformed(ActionEvent e){

		if (e.getSource()==rbStoreToDisk){
			rbStoreToDisk.setEnabled(true);
			rbNetMeeting.setEnabled(false);
			rbEmail.setEnabled(false);
		}

		if(e.getSource()==rb4){
			 rb4.setEnabled(true);
			 rb5.setEnabled(false);
			 rb6.setEnabled(false);
		}

		if(e.getSource()==btnSave){

			 setTitle("Your Comments are being saved");
			 strComment = txtComment.getText();
			 strName= txtName.getText();
			 try {
				 path = oncotcap.Oncotcap.getInstallDir() + "TcapData\\Questions\\";
				 int counter = 1;
				 String filename = new String(path + strName + counter + ".txt");
				 while(fileExists(filename))
				 filename = new String(path + strName + (counter++) + ".txt");
				 RandomAccessFile rf = new RandomAccessFile(filename,"rw");
				 rf.seek(rf.length());	
				 rf.writeBytes(strComment + "\n");
				 rf.close();
				 setVisible(false);
			}
			catch (IOException u){Logger.log ("The error is :" + u);}
		}
	}

	public void clear(){
		txtComment.setText("");
		txtName.setText("");
	}
	public static void main(String args[]) throws IOException {
		AskQuestion aq=new AskQuestion();
		aq.setSize(700,700);
		aq.setVisible(true);

	}
}
