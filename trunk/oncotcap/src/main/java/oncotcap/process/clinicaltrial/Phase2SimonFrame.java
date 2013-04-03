package oncotcap.process.clinicaltrial;

import java.awt.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.KeyEvent;
import oncotcap.util.*;
import oncotcap.Oncotcap;

public class Phase2SimonFrame extends JDialog
{
	final  int nmax=120;
	int         iCal=0;
	double      Alpha;
	double      Beta;
	double      PrResp0;
	double      PrResp1;

	private Calculate calc = new Calculate();
	private final java.util.Timer timer = new java.util.Timer();

	private Phase2SimonFrame myFrame = this;
	String stringThingsBad;
	Color red ;
	Color yellow ;
	TextField tarray [];
	JLabel cell[][] = new JLabel[4][3];

	final int HEADERSIZE = 14;

	JLabel lblAlpha = new JLabel();
	JLabel lblAlphaRange = new JLabel();
	public TextField txtAlpha = new TextField();
	JLabel lblBeta = new JLabel();
	JLabel lblBetaRange = new JLabel();
	public TextField txtBeta = new TextField();
	JLabel lblPrResp0 = new JLabel();
	JLabel lblPrResp0Range = new JLabel();
	public TextField txtPrResp0 = new TextField();
	JLabel lblPrResp1 = new JLabel();
	JLabel lblPrResp1Range = new JLabel();
	public TextField txtPrResp1 = new TextField();

	JTextArea  txtLog = new JTextArea(" ");
	JLabel lblLog = new JLabel();

	JPanel pnlAnswer = new JPanel();
	JLabel lblN1 = new JLabel();
	JLabel lblN = new JLabel();
	JLabel lblR1 = new JLabel();
	JLabel lblR = new JLabel();
	JLabel lblN1Desc = new JLabel();
	JLabel lblNDesc = new JLabel();
	JLabel lblR1Desc = new JLabel();
	JLabel lblRDesc = new JLabel();
	JLabel lblEN = new JLabel();
	JLabel lblENDesc = new JLabel();
	JLabel lblPStop = new JLabel();
	JLabel lblPStopDesc = new JLabel();

	public JButton buttonCalculate = new JButton("Calculate");
	private JLabel progBar = new JLabel("Calculating");
	JScrollPane catalogPane = new JScrollPane();
	protected DesignCatalog designCatalog;
	protected DesignTable designTable;
	DefaultTableModel designs = null;
	protected JButton buttonOK = new JButton("OK- use this design");
	
	Color labelColor = new Color(200,30,0).darker();
	Color parameterColor = labelColor;
	Color labelColorForBad = oncotcap.util.Util.lighter(Color.red, 2);
	Color currentDesignColor = new Color(20,20,240);
	
	final int labelgap=5;
	final int ystart=24;
	final int xbutton = 14;
	final int wbutton = 263;
	final int xlabel = xbutton + wbutton - 7;  //420
	final int wlabel = 126;
	final int hlabel=45;
	final int htxt = 20;
	final int wtxt = 50;

	final int cellh=25, cellw=87, cellxstart=190,cellystart=47;
	Rectangle titleBounds = new Rectangle(cellxstart,0,425-cellxstart,19);
	final int wTable = 415, hTable = 250;
	final int xTable = 418, yTable = 72;
	Rectangle tableBounds = new Rectangle(xTable,yTable,wTable,hTable);
	int xLog = xbutton; //420;
	int yLog = 330;
	int wLog = 600;
	Rectangle logRectangle = new Rectangle(xLog,yLog-10,270,15);
	Rectangle catalogBounds = new Rectangle(xLog,yLog+30,wLog,200);
	protected Rectangle buttonOKBounds = new Rectangle(xLog+wLog+10,yLog+56,150,75);
	Rectangle catalogPaneBounds = catalogBounds;

	DesignListener parent = null;
	protected Container cp;

	Phase2SimonCalculate calculator = new Phase2SimonCalculate ();

	TableCellRenderer tcr;

	public static void main(String [] args) {
	  Phase2SimonFrame f = new Phase2SimonFrame(new DesignListener() {
		  public void setDesign(Vector v) {
			  Logger.log("Listening to Design, which is " + v);
		  }
	  }
	  );
	  f.setVisible(true);
	}
	public Phase2SimonFrame(){
//	  super("Phase 2 two-stage clinical trial design");
	  super(oncotcap.Oncotcap.getMainFrame(), true);
	  init();
	}
	public Phase2SimonFrame(DesignListener parent){
	  this();
	  this.parent = parent;
	}

	public void fireCalculate()
	{
		if ( !extractAndCheckValues()) 
		{
			buttonCalculate.setVisible(false);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			progBar.setVisible(true);
			tryToCalculate();
		}
	}
	public void init()
	{

	cp = getContentPane();
	cp.setLayout(null);
	setSize(800,600);
	setBackground(java.awt.Color.white);

	JLabel parameterLabel = new JLabel ("Design requirement parameters");
	oncotcap.util.Util.setFontSize(parameterLabel, HEADERSIZE);
	parameterLabel.setBounds(xbutton, ystart, wbutton, hlabel);
	parameterLabel.setBackground(Color.lightGray);
	parameterLabel.setForeground(parameterColor);
	cp.add(parameterLabel);

	JLabel arrow1 =  new JLabel(oncotcap.util.OncoTcapIcons.getImageIcon("redArrowDown.gif"));
	arrow1.setLocation(xbutton + 50, ystart + 25);
	arrow1.setSize(10,25);	
	cp.add (arrow1);
	Logger.log("arrow1 size " + arrow1.getSize());
	arrow1.setVisible(false);

	JLabel valueLabel = new JLabel ("<html><font size=1 color=\"#550000\"> <i> "
									+ "Change values to explore choices"
								   + "</i></font></html>");
	oncotcap.util.Util.setFontSize(valueLabel, 1);
	valueLabel.setBounds(xlabel-15, ystart+12, wlabel * 2, hlabel);
	valueLabel.setBackground(Color.lightGray);
	valueLabel.setForeground(Color.black);
	cp.add(valueLabel);

	JLabel arrow2 =  new JLabel(oncotcap.util.OncoTcapIcons.getImageIcon("redArrowDown.gif"));
	arrow2.setLocation(xlabel + 50, ystart + 25);
	arrow2.setSize(10,25);	
	cp.add (arrow2);
	arrow2.setVisible(false);

	JLabel titleLabel = new JLabel("Current optimal design",JLabel.LEFT);
	oncotcap.util.Util.setFontSize(titleLabel, HEADERSIZE);
	titleLabel.setBackground(java.awt.Color.gray);
	titleLabel.setForeground(currentDesignColor);
	titleLabel.setBounds(titleBounds);
	Point titlePoint = valueLabel.getLocation();
	titlePoint.translate(valueLabel.getWidth()+35, 0);
	titleLabel.setLocation(titlePoint);
	cp.add(titleLabel);

	JLabel arrow3 =  new JLabel(oncotcap.util.OncoTcapIcons.getImageIcon("redArrowDown.gif"));
	arrow3.setLocation( titleLabel.getLocation().x+ 50,
					   titleLabel.getLocation().y + 25);
	arrow3.setSize(10,25);	
	cp.add (arrow3);
	arrow3.setVisible(false);

	lblAlpha.setText(Phase2SimonBundle.getString("lblAlpha_label"));
	lblAlphaRange.setText("<html><body bgcolor=\"#CDCFCD\" text=\"#8C1400\">[.05 - 0.3]</html>");
	cp.add(lblAlpha);
	cp.add(lblAlphaRange);
	lblAlpha.setBounds(xbutton,ystart+hlabel+labelgap,wbutton,hlabel);
	lblAlphaRange.setBounds(xlabel + wtxt + 5, ystart+hlabel+labelgap+(hlabel-htxt)/2,wtxt+10,htxt);
	txtAlpha.setText(Phase2SimonBundle.getString("txtAlpha_text"));
	txtAlpha.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
	cp.add(txtAlpha);
	txtAlpha.setBackground(java.awt.Color.lightGray);
	txtAlpha.setBounds(xlabel,ystart+hlabel+labelgap+(hlabel-htxt)/2,wtxt,htxt);
	
	
	lblBeta.setText(Phase2SimonBundle.getString("lblBeta_label"));
	lblBetaRange.setText("<html><body bgcolor=\"#CDCFCD\" text=\"#8C1400\"><p align=\"right\">[0.0 - 0.3]</p></html>");	
	cp.add(lblBeta);
	cp.add(lblBetaRange);
	lblBeta.setForeground(labelColor);
	lblBeta.setFont(new Font("Dialog", Font.BOLD, 12));
	lblBeta.setBounds(xbutton,ystart+(hlabel+labelgap)*2,wbutton,hlabel);
	lblBetaRange.setBounds(xlabel + wtxt + 5, ystart+(hlabel+labelgap)*2+(hlabel-htxt)/2,wtxt+10,htxt);
	txtBeta.setText(Phase2SimonBundle.getString("txtBeta_text"));
	txtBeta.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
	cp.add(txtBeta);
	txtBeta.setBackground(java.awt.Color.lightGray);
	txtBeta.setBounds(xlabel,ystart+(hlabel+labelgap)*2+(hlabel-htxt)/2,wtxt,htxt);

	lblPrResp0.setText(Phase2SimonBundle.getString("lblPrResp0_label"));
	lblPrResp0Range.setText("<html><body bgcolor=\"#CDCFCD\" text=\"#8C1400\"><p align=\"right\">[0.0 - 1.0]</p></html>");
	cp.add(lblPrResp0);
	cp.add(lblPrResp0Range);
	lblPrResp0.setForeground(labelColor);
	lblPrResp0.setFont(new Font("Dialog", Font.BOLD, 12));
	lblPrResp0.setBounds(xbutton,ystart+(hlabel+labelgap)*3,wbutton,hlabel);
	lblPrResp0Range.setBounds(xlabel + wtxt + 5, ystart+(hlabel+labelgap)*3+(hlabel-htxt)/2,wtxt+10,htxt);
	txtPrResp0.setText(Phase2SimonBundle.getString("txtPrResp0_text"));
	txtPrResp0.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
	cp.add(txtPrResp0);
	txtPrResp0.setBackground(java.awt.Color.lightGray);
	txtPrResp0.setBounds(xlabel,ystart+(hlabel+labelgap)*3+(hlabel-htxt)/2,wtxt,htxt);
	lblPrResp1.setText(Phase2SimonBundle.getString("lblPrResp1_label"));
	lblPrResp1Range.setText("<html><body bgcolor=\"#CDCFCD\" text=\"#8C1400\"><p align=\"right\">[0.0 - 1.0]</p></html>");
	cp.add(lblPrResp1);
	cp.add(lblPrResp1Range);
	lblPrResp1.setForeground(labelColor);
	lblPrResp1.setFont(new Font("Dialog", Font.BOLD, 12));
	lblPrResp1.setBounds(xbutton,ystart+(hlabel+labelgap)*4,wbutton,hlabel+10);
	lblPrResp1Range.setBounds(xlabel + wtxt + 5, ystart+(hlabel+labelgap)*4+(hlabel-htxt)/2,wtxt+10,htxt);
	txtPrResp1.setText(Phase2SimonBundle.getString("txtPrResp1_text"));
	txtPrResp1.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
	cp.add(txtPrResp1);
	txtPrResp1.setBackground(java.awt.Color.lightGray);
	txtPrResp1.setBounds(xlabel,ystart+(hlabel+labelgap)*4+(hlabel-htxt)/2,wtxt,htxt);

	lblLog.setBackground(java.awt.Color.gray.brighter());
	lblLog.setForeground(java.awt.Color.black);
	lblLog.setBounds(logRectangle);
	lblLog.setFont(new Font("Dialog", Font.BOLD, 14));
	lblLog.setText("Catalog of optimal designs");
	lblLog.setVisible(true);
	cp.add(lblLog);

	pnlAnswer.setBounds(tableBounds);
	pnlAnswer.setBackground(java.awt.Color.lightGray);
	pnlAnswer.setLayout(null);

	lblN1Desc.setBounds(10,5,300,20);
	lblR1Desc.setBounds(10,35,300,40);
	lblNDesc.setBounds(10,90,300,20);
	lblRDesc.setBounds(10,120,300,40);
	lblENDesc.setBounds(10,170,300,20);
	lblPStopDesc.setBounds(10,200,300,20);
	lblN1Desc.setHorizontalAlignment(JLabel.RIGHT);
	lblR1Desc.setHorizontalAlignment(JLabel.RIGHT);
	lblNDesc.setHorizontalAlignment(JLabel.RIGHT);
	lblRDesc.setHorizontalAlignment(JLabel.RIGHT);
	lblENDesc.setHorizontalAlignment(JLabel.RIGHT);
	lblPStopDesc.setHorizontalAlignment(JLabel.RIGHT);
	lblN1Desc.setText("<html><body bgcolor=\"#C6C3C6\" text=\"#1414F0\"><b>First Stage Sample Size (n1):</b></body></html>");
	lblNDesc.setText("<html><body bgcolor=\"#C6C3C6\" text=\"#1414F0\"><p align=\"right\"><b>Total Sample Size (n):</b></p></body></html>");
	lblR1Desc.setText("<html><body bgcolor=\"#C6C3C6\" text=\"#1414F0\"><p align=\"right\"><b>Stop and reject drug if the number of responses is less than or equal to (r1):</b></p></body></html>");
	lblRDesc.setText("<html><body bgcolor=\"#C6C3C6\" text=\"#1414F0\"><p align=\"right\"><b>Reject drug if the total number of responses is less than or equal to (r):</b></p></body></html>");
	lblENDesc.setText("<html><body bgcolor=\"#C6C3C6\" text=\"#848284\"><p align=\"right\"><b>Expected value of N (Exp(n)):</b></p></body></html>");
	lblPStopDesc.setText("<html><body bgcolor=\"#C6C3C6\" text=\"#848284\"><p align=\"right\"><b>Probability of Stopping (P(Stop)):</b></p></body></html>");

	lblN1.setBackground(Color.gray);
	lblN.setBackground(Color.gray);
	lblR1.setBackground(Color.gray);
	lblR.setBackground(Color.gray);
	lblEN.setBackground(Color.gray);
	lblPStop.setBackground(Color.gray);
	lblN1.setForeground(Color.white);
	lblN.setForeground(Color.white);
	lblR1.setForeground(Color.white);
	lblR.setForeground(Color.white);
	lblEN.setForeground(Color.white);
	lblPStop.setForeground(Color.white);
	lblN1.setHorizontalAlignment(JLabel.RIGHT);
	lblN.setHorizontalAlignment(JLabel.RIGHT);
	lblR1.setHorizontalAlignment(JLabel.RIGHT);
	lblR.setHorizontalAlignment(JLabel.RIGHT);
	lblEN.setHorizontalAlignment(JLabel.RIGHT);
	lblPStop.setHorizontalAlignment(JLabel.RIGHT);
	lblN1.setOpaque(true);
	lblN.setOpaque(true);
	lblR.setOpaque(true);
	lblR1.setOpaque(true);
	lblEN.setOpaque(true);
	lblPStop.setOpaque(true);
	lblN1.setBounds(315, 5, 35, 20);
	lblR1.setBounds(315, 55, 35, 20);
	lblN.setBounds(315, 90, 35, 20);
	lblR.setBounds(315, 140, 35, 20);
	lblEN.setBounds(315, 170, 35, 20);
	lblPStop.setBounds(315, 200, 35, 20);

	pnlAnswer.add(lblN1Desc);
	pnlAnswer.add(lblNDesc);
	pnlAnswer.add(lblR1Desc);
	pnlAnswer.add(lblRDesc);
	pnlAnswer.add(lblENDesc);
	pnlAnswer.add(lblPStopDesc);
	pnlAnswer.add(lblN1);
	pnlAnswer.add(lblN);
	pnlAnswer.add(lblR1);
	pnlAnswer.add(lblR);
	pnlAnswer.add(lblEN);
	pnlAnswer.add(lblPStop);
	cp.add(pnlAnswer);

	cp.add(buttonCalculate);
//	progBar.setIndeterminate(true);
	progBar.setHorizontalAlignment(JLabel.CENTER);
	progBar.setVerticalAlignment(JLabel.CENTER);
	progBar.setBackground(Color.red);
	progBar.setOpaque(true);
	progBar.setVisible(false);
	cp.add(progBar);
	buttonCalculate.setVisible(true);
	buttonCalculate.setFont(new Font("Dialog", Font.BOLD, 14));
//	buttonCalculate.setLabel("Calculate");
	buttonCalculate.setActionCommand("button");
	buttonCalculate.setBounds(xlabel,ystart+(hlabel+labelgap)*5,122,hlabel);
	progBar.setBounds(xlabel,ystart+(hlabel+labelgap)*5,122,hlabel);
	buttonCalculate.setMnemonic(KeyEvent.VK_C);

	buttonOK.setBounds(buttonOKBounds);
	cp.add(buttonOK);
	buttonOK.setEnabled(false);
	buttonOK.setMnemonic(KeyEvent.VK_O);
	String [] columnNames = {
		"#","alpha","beta","p0","p1","n1","n","r1","r","Exp(n)","P(Stop)"
	};

	designTable = new DesignTable();
	designTable.initialize();
	designs = new DefaultTableModel(columnNames, 0);

	Logger.log(designs);

	designCatalog = new DesignCatalog(designs);
	designCatalog.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	designs.addTableModelListener(new TableModelListener() {
		public void tableChanged(TableModelEvent event) {
		}
	}
	);

	catalogPane.setBounds( catalogPaneBounds);
	catalogPane.setViewportView(designCatalog);
	cp.add(catalogPane);

	JLabel paramLabel = new JLabel("Design parameters",JLabel.CENTER);
	paramLabel.setBackground(parameterColor);
	paramLabel.setOpaque(true);
	paramLabel.setForeground(Color.white);
	paramLabel.setBounds(new Rectangle(
										   catalogPaneBounds.x + 1*catalogPaneBounds.width/11,
										   catalogPaneBounds.y-15,
										   4*catalogPaneBounds.width/11 - 2,
										   titleLabel.getHeight())
							);
	cp.add(paramLabel);

	JLabel catDesignLabel = new JLabel("Current optimal design",JLabel.CENTER);
	catDesignLabel.setBackground(currentDesignColor);
	catDesignLabel.setOpaque(true);
	catDesignLabel.setForeground(Color.white);
	catDesignLabel.setBounds(new Rectangle(
										   catalogPaneBounds.x + 5*catalogPaneBounds.width/11,
										  catalogPaneBounds.y-15,
										   4*catalogPaneBounds.width/11 - 2,
										  titleLabel.getHeight())
							);
	cp.add(catDesignLabel);

	JLabel nullHypothLabel = new JLabel(" if P0 is true ",JLabel.CENTER);
	nullHypothLabel.setBackground(Color.gray);
	nullHypothLabel.setOpaque(true);
	nullHypothLabel.setForeground(Color.white);
	nullHypothLabel.setBounds(new Rectangle(
										   catalogPaneBounds.x + 9*catalogPaneBounds.width/11,
										   catalogPaneBounds.y-15,
										   2*catalogPaneBounds.width/11 - 2,
										   titleLabel.getHeight())
							);
	cp.add(nullHypothLabel);



	//{{REGISTER_LISTENERS
	SymAction lSymAction = new SymAction();
	buttonCalculate.addActionListener(lSymAction);
	SymMouse aSymMouse = new SymMouse();
	designCatalog.addMouseListener(aSymMouse);
//	SymText lSymText = new SymText();
//	SymKey aSymKey = new SymKey();

	red = lblPrResp1.getBackground();
	yellow = lblBeta.getBackground(); //16776960

//	txtBeta.addTextListener(lSymText);
//	txtPrResp0.addTextListener(lSymText);
//	txtAlpha.addTextListener(lSymText);
//	txtPrResp1.addTextListener(lSymText);
//	txtBeta.addKeyListener(aSymKey);
//	txtPrResp0.addKeyListener(aSymKey);
//	txtAlpha.addKeyListener(aSymKey);
//	txtPrResp1.addKeyListener(aSymKey);
	buttonOK.addActionListener(lSymAction);
	extractAndCheckValues();
	Logger.log( hTable + " " + txtPrResp1.getLocation().y + " " +
						txtPrResp1.getHeight()
						+ " " + txtAlpha.getLocation().y);

	}

	class DesignCatalog extends JTable {
	  public DesignCatalog(TableModel m) {
		  super(m);
	  }
	  // Override of JTable.valueChanged ( for ListSelectionListener).
	  public void valueChanged(ListSelectionEvent e)  {
		  DefaultListSelectionModel dlsm = (DefaultListSelectionModel) e.getSource();
		  Logger.log("===" + e.getSource());
		  //if (e.getSource().equals(designs)) {
		  try {
			  super.valueChanged(e);
			  int first = e.getFirstIndex();
			  int last = e.getLastIndex();
			  int selected = -9999;
			  if (dlsm.isSelectedIndex(first))
				  selected = first; 
			  else if (dlsm.isSelectedIndex(last))
				  selected = last;
			  Logger.log("Model selected: " + selected + ": " + first + " " + last
								+ dlsm.isSelectedIndex(first) + dlsm.isSelectedIndex(last));
			  setDesignIndex(selected);
		  }
		  catch(NullPointerException ex) {
			  Logger.log("valueChanged nullpointerexception");
		  }
	  }
	}

	protected Vector designVector;

	void setDesignIndex(int iDesign) {
	  Logger.log("setDesign: iDesign = " + iDesign);
	  designVector = ((Vector)(designs.getDataVector().elementAt(iDesign))); 
	  designTable.setDesign(designVector);
	  parent.setDesign(designVector);
	}

	public static int INDEX_DesignNumber = 0;
	public static int INDEX_Alpha = 1;
	public static int INDEX_Beta = 2;
	public static int INDEX_PrResp0 = 3;
	public static int INDEX_PrResp1 = 4;
	public static int INDEX_n1_final = 5;
	public static int INDEX_n_final = 6;
	public static int INDEX_r1_final = 7;
	public static int INDEX_r_final = 8;
	public static int INDEX_Val_Final = 9;
	public static int INDEX_PET_Final = 10;

	void outputToLog(){
	  if ( iCal == 1 ){
		  txtLog.setVisible(true);
		  lblLog.setVisible(true);
	  }
	  Vector newDesign = new Vector();
		newDesign.add(new Integer(iCal));
		newDesign.add(new Double(Alpha));
		newDesign.add(new Double(Beta));
		newDesign.add(new Double(PrResp0));
		newDesign.add(new Double(PrResp1));
		newDesign.add(new Integer(calculator.n1_final));
		newDesign.add(new Integer(calculator.n_final));
		newDesign.add(new Integer(calculator.r1_Final));
		newDesign.add(new Integer(calculator.r_Final));
		newDesign.add(round2(calculator.Val_Final));
		newDesign.add(round2(calculator.PET_final));
	  Logger.log("Adding row");
	  designs.addRow(newDesign);
	  designCatalog.changeSelection(designCatalog.getRowCount()-1,1,false,false);
	  //This kicks off the notification of DesignListener objects.
	}

	  class SymAction implements java.awt.event.ActionListener
	  {
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == buttonOK)
				allDone();
			else if (object == buttonCalculate){
				Logger.log("buttonCalculate clicked");
				fireCalculate();
			}
		 }
	  }

	  private class Calculate implements Runnable
	  {
		  public void run ()
		  {
			  buttonOK.setEnabled(true);
			  thingsAreGood();
			  iCal++;
//			  calculator.calculate(myFrame);
			  buttonCalculate.repaint();
			  if ( calculator.Val_Final  == calculator.INIT_VAL_FINAL || calculator.n_final == 0)
			  {
				  stringThingsBad="Sorry. For the previous input values, the sample size is too large "+nmax+".";
				  Logger.log (stringThingsBad);
				  JOptionPane.showMessageDialog(myFrame,
					  "No Can Do!  Required sample size is too large",
					  "No Can Do!  Required sample size is too large",
					  JOptionPane.ERROR_MESSAGE);
			  }
			  else
			  {
				  outputToLog();
			  }
			  progBar.setVisible(false);
			  setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			  buttonCalculate.setEnabled(true);
			  buttonCalculate.setVisible(true);
		  }
	  }



	  class DesignTable extends JTable implements DesignListener {
		  public void initialize() {
			  Vector v = new Vector();
			  for (int i=0; i<11; i++)
				  v.add("     ");
			  setDesign(v);
		  }
		  public void setDesign(Vector v) {
			  Logger.log("setTableFromCatalog called");
			  lblN1.setText(v.elementAt(INDEX_n1_final).toString());
			  lblN.setText(v.elementAt(INDEX_n_final).toString());
			  lblR1.setText(v.elementAt(INDEX_r1_final).toString());
			  lblR.setText(v.elementAt(INDEX_r_final).toString());
			  lblEN.setText(v.elementAt(INDEX_Val_Final).toString());
			  lblPStop.setText(v.elementAt(INDEX_PET_Final).toString());
		  }
	  }

	 protected boolean extractAndCheckValues()
	 {
		  if (extractValues() == false) {
			  return true;   //not good
		  }
		  try
		  {
			 if ( Alpha < 0.05 || Alpha > 0.3 ){
				 OncMessageBox.showMessageDialog(Oncotcap.getMainFrame(), 
					 "Please enter a value for alpha between 0.05 and 0.3.",
					 "Phase II Design",
					 OncMessageBox.WARNING_MESSAGE);
					return true;
				   }
			  if ( Beta < 0.0 || Beta > 0.3 ){
				  OncMessageBox.showMessageDialog(Oncotcap.getMainFrame(), 
						  "Please enter a value for beta between 0 and 0.3 only.",
						  "Phase II Design",
						  OncMessageBox.WARNING_MESSAGE);
					return true;
			  }
			  if ( PrResp0 < 0.0 || PrResp0 > 1.0 ){
					OncMessageBox.showMessageDialog(Oncotcap.getMainFrame(), "Please enter a value for p0 between 0 and 1.0 only.", "Phase II Design", OncMessageBox.WARNING_MESSAGE);
					return true;
			  }
			  if ( PrResp1 < 0.0 || PrResp1 > 1.0 ){
					OncMessageBox.showMessageDialog(Oncotcap.getMainFrame(), "Please enter a value for p1 between 0 and 1.0 only.", "Phase II Design", OncMessageBox.WARNING_MESSAGE);
					return true;
			  }
			  if ( PrResp1 <= PrResp0 ) {
				   OncMessageBox.showMessageDialog(Oncotcap.getMainFrame(), "The value for p1 should be greater than p0.", "Phase II Design", OncMessageBox.WARNING_MESSAGE);
				   return true;
			  }
			  return false;
		  }
		  catch (java.lang.NumberFormatException e){
			  Logger.log(Phase2SimonBundle.getString("System_out_println"));
			  return false;
		  }
	}

	boolean  extractValues()
	{
		try
		{
		  Alpha = (new Double(txtAlpha.getText())).doubleValue();
		  Beta = (new Double(txtBeta.getText())).doubleValue();
		  PrResp0 = (new Double(txtPrResp0.getText())).doubleValue();
		  PrResp1 = (new Double(txtPrResp1.getText())).doubleValue();
		  return(true);
		}
		catch (java.lang.NumberFormatException e){
		  Logger.log("NumberFormatException...");
		  Logger.log(Phase2SimonBundle.getString("System_out_println"));
		  return(false);
		}
	}
	void thingsAreGood(){
	  lblBeta.setBackground(yellow);
	  lblAlpha.setBackground(yellow);
	  lblPrResp0.setBackground(yellow);
	  lblPrResp1.setBackground(yellow);

	//  buttonBad.setBackground(java.awt.Color.red);
	//  buttonBad.setLabel(Phase2SimonBundle.getString("buttonBad_label"));
	//      buttonBad.setVisible(false);
	//      buttonGood.setVisible(true);
	  buttonCalculate.setEnabled(true);
	}

	class SymMouse extends java.awt.event.MouseAdapter
	{
	public void mouseClicked(java.awt.event.MouseEvent event)
	{
		Logger.log(event.getSource().getClass().getName());
		if (event.getSource().equals(designCatalog)) {
			buttonOK.setEnabled(true);
		}
//		else
//			tryToCalculate();
	}
	}

	protected void tryToCalculate() {
	  if (!extractAndCheckValues())
	  {
		 thingsAreGood();
		 timer.schedule(new java.util.TimerTask()	{	public void run()
			 {
				 new Thread(calc).start();			 
			 }
		 },
		 (long) 100.0); 
	//		 calc.run();
	  }
	}
	String round2(double d){
	  int i = (int) (d*100+0.5);
	  String s = Double.toString(((double)i)/100);
	  int decimal = s.indexOf(Phase2SimonBundle.getString("int_decimal___s_indexOf"));
	  if (decimal > 0 && decimal+3 < s.length())
		  s = s.substring(0,decimal+3);
	  return ( s);
	//            return ( d);
	}
	class SymText implements java.awt.event.TextListener
	{
		//NOT USED!
	public void textValueChanged(java.awt.event.TextEvent event)
	{
	  Object object = event.getSource();
	//	  refreshNumbers(object);
	  extractAndCheckValues();
	  //button1.setLabel(badString);
	}
	}
	void refreshNumbers(Object object){
	try
	  {
		TextField tf = (TextField)object;
		double d =  new Double(tf.getText())
						.doubleValue();
		if (tf==txtBeta)Beta= d;
		if (object==txtPrResp0)PrResp0= d;
		if (object==txtAlpha)Alpha= d;
		if (object==txtPrResp1)PrResp1= d;

		if (object==txtBeta)lblBeta.setBackground(red);
		if (object==txtPrResp0)lblPrResp0.setBackground(red);
		if (object==txtAlpha)lblAlpha.setBackground(red);
		if (object==txtPrResp1)lblPrResp1.setBackground(red);
		buttonCalculate.setEnabled(true);
	}
	catch (java.lang.NumberFormatException e){
		Logger.log(Phase2SimonBundle.getString("System_out_println_1"));
		buttonCalculate.setEnabled(false);
	}
	return;
	}

	class SymKey extends java.awt.event.KeyAdapter
	{
	public void keyTyped(java.awt.event.KeyEvent event)
	{
	  Object object = event.getSource();
	//      buttonGood.setVisible(false);
	//      buttonBad.setLabel(Phase2SimonBundle.getString("Phase2Simon_badString___new_String"));
	//      buttonBad.setVisible(true);
	  refreshNumbers(object);
	}
	}

	 void SetWaitInfo(){
	//		buttonGood.setLabel("Your computing is in progress.");
	//		buttonGood.setVisible(true);
	//		buttonBad.setVisible(false);
	 }

	 protected void allDone() {
		 Logger.log("Should have been overridden");
		 setVisible(false);
	 }

	 static java.util.ResourceBundle Phase2SimonBundle = java.util.ResourceBundle.getBundle("oncotcap.clinicaltrial.Phase2SimonBundle");

}
