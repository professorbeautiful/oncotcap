package oncotcap.display.modelcontroller.cellkineticsdemo;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;

import oncotcap.util.*;

public class RetrieveQuestion extends JFrame implements ActionListener {
	private boolean laidOut = false;
	private boolean startedFromCommandLine = false;
	JLabel lblRetrieve;
	JButton btnRetrieve;
	File file;
//	String directory=new String("C:\\TcapData\\Questions");
	String fileName = new String();
	JTextArea txtRetrieve;


	public RetrieveQuestion(){
		setTitle("Opening files of all users");
		JPanel contentpane=(JPanel)getContentPane();
		contentpane.setLayout(null);

		lblRetrieve = new JLabel("If you want to retrieve the e-mails and comments of various users then click on the button named RETRIEVE");
		lblRetrieve.setFont(new Font("Comic Sans",Font.BOLD+Font.ITALIC,22));
		contentpane.add(lblRetrieve);

		txtRetrieve=new JTextArea();
		setIconImage(oncotcap.util.OncoTcapIcons.getDefault().getImage());
		setSize(750,450); 
		contentpane.add(txtRetrieve);

		btnRetrieve = new JButton("RETRIEVE");
		btnRetrieve.addActionListener(this);
		contentpane.add(btnRetrieve);

		Insets insets = contentpane.getInsets();

		lblRetrieve.setBounds(25,5,700,60);
		btnRetrieve.setBounds(600,300,100,50);
		txtRetrieve.setBounds(100,100,500,100);

		myadapter myapp= new myadapter();
		addWindowListener(myapp);
		}

	class myadapter extends WindowAdapter{
		public void windowClosing(WindowEvent e){
			if(startedFromCommandLine){
				System.exit(1);
			} 
			else
				setVisible(false);
			}
		}

		public void clear(){
			txtRetrieve.setText("");
		}

		public void actionPerformed(ActionEvent e){
			if(e.getSource() == btnRetrieve){
				FileDialog fd = new FileDialog(this, "Open File", FileDialog.LOAD);
				fd.setDirectory(oncotcap.Oncotcap.getInstallDir() + "TcapData\\Questions");                      
				fd.show();
				String curFile;
				if ((curFile = fd.getFile()) != null) {
					String filename = fd.getDirectory() + curFile;
					char[] data;
					File f1 = new File (filename);
					try {
						FileReader fin = new FileReader (f1);
						int filesize = (int)f1.length();
						data = new char[filesize];
						fin.read (data, 0, filesize);
						txtRetrieve.setText (new String (data));
					} 
					catch (FileNotFoundException exc) {Logger.log("IOException: " + filename);}
					catch (IOException ex) {Logger.log("File Not Found: " + filename);}

				}
			}

		}

		public static void main(String args[]){
			RetrieveQuestion rq=new RetrieveQuestion();
			rq.startedFromCommandLine = true;
			rq.setVisible(true);

		}

		}
