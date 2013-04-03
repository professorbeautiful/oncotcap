package oncotcap.process.clinicaltrial;
/*                                                  buttonbad
  A basic extension of the java.applet.Applet
  */


import java.awt.*;
import java.applet.*;
import oncotcap.util.*;
//import symantec.itools.awt.FormattedTextField;
//import geo.emblaze.Emblaze20;
//import symantec.itools.awt.shape.Line;
public class Phase2Simon extends Applet
{
  final  int nmax=120;
  int         iCal=0;
  double      Alpha;
  double      Beta;
  double      PrResp0;
  double      PrResp1;

  String badString;
  String goodString;
  String stringThingsBad;
  Color red ;
  Color yellow ;
  TextField tarray [];
  Label cell[][] = new Label[3][3];

  public void init()
  {

    final int labelh=35;
    final int labelgap=13;
    final int ystart=24;
    // Take out this line if you don't use symantec.itools.net.RelativeURL or symantec.itools.awt.util.StatusScroller
    //symantec.itools.lang.Context.setApplet(this);

    // This code is automatically generated by Visual Cafe when you add
    // components to the visual environment. It instantiates and initializes
    // the components. To modify the code, only use code syntax that matches
    // what Visual Cafe can generate, or Visual Cafe may be unable to back
    // parse your Java file into its visual environment.
    //{{INIT_CONTROLS
    setLayout(null);
    setSize(980,510);
    setBackground(java.awt.Color.white);
    buttonGood.setActionCommand("button");
    buttonGood.setLabel("");
    add(buttonGood);
    buttonGood.setBackground(new java.awt.Color(193,207,18));
    buttonGood.setForeground(new java.awt.Color(172,0,86));
    buttonGood.setFont(new Font("Dialog", Font.PLAIN, 14));
    buttonGood.setBounds(12,15,528,36);
    buttonGood.setVisible(false);

    lblAlpha.setLabel("<html><body bgcolor=\"#CDCFCD\" text=\"#8C1400\"><p align=\"right\"><b>\"Alpha\" (Type I error)<br>Probability of accepting a poor drug.</b></p></html>");
    add(lblAlpha);
    lblAlpha.setBackground(java.awt.Color.yellow);
    lblAlpha.setFont(new Font("Dialog", Font.BOLD, 12));
    lblAlpha.setBounds(170,ystart+labelh+labelgap,243,labelh);
    txtAlpha.setText("");
    txtAlpha.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
    add(txtAlpha);
    txtAlpha.setBackground(java.awt.Color.lightGray);
    txtAlpha.setBounds(420,ystart+labelh+labelgap,126,labelh);

    lblBeta.setLabel("<html><body bgcolor=\"#CDCFCD\" text=\"#8C1400\"><p align=\"right\"><b>\"Beta\" (Type II error)<br>Probability of not accepting a good drug.</b></p></html>");
    add(lblBeta);
    lblBeta.setBackground(java.awt.Color.yellow);
    lblBeta.setFont(new Font("Dialog", Font.BOLD, 12));
    lblBeta.setBounds(170,ystart+(labelh+labelgap)*2,243,labelh);
    txtBeta.setText("");
    txtBeta.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
    add(txtBeta);
    txtBeta.setBackground(java.awt.Color.lightGray);
    txtBeta.setBounds(420,ystart+(labelh+labelgap)*2,126,labelh);

    lblPrResp0.setLabel("<html><body bgcolor=\"#CDCFCD\" text=\"#8C1400\"><p align=\"right\"><b>\"P0\" (Disappointing Response Rate)<br>Response probability of a poor drug.</b></p></html>");
    add(lblPrResp0);
    lblPrResp0.setBackground(java.awt.Color.yellow);
    lblPrResp0.setFont(new Font("Dialog", Font.BOLD, 12));
    lblPrResp0.setBounds(170,ystart+(labelh+labelgap)*3,243,labelh);
    txtPrResp0.setText("");
    txtPrResp0.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
    add(txtPrResp0);
    txtPrResp0.setBackground(java.awt.Color.lightGray);
    txtPrResp0.setBounds(420,ystart+(labelh+labelgap)*3,126,labelh);
    lblPrResp1.setLabel("lblPrResp1_label");
    add(lblPrResp1);
    lblPrResp1.setBackground(java.awt.Color.red);
    lblPrResp1.setFont(new Font("Dialog", Font.BOLD, 12));
    lblPrResp1.setBounds(170,ystart+(labelh+labelgap)*4,243,labelh);
    txtPrResp1.setText("txtPrResp1_text");
    txtPrResp1.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
    add(txtPrResp1);
    txtPrResp1.setBackground(java.awt.Color.lightGray);
    txtPrResp1.setBounds(420,ystart+(labelh+labelgap)*4,126,labelh);

    buttonBad.setActionCommand("button");
    buttonBad.setLabel("");
    add(buttonBad);
    buttonBad.setBackground(java.awt.Color.red);
    buttonBad.setForeground(java.awt.Color.white);
    buttonBad.setFont(new Font("Dialog", Font.PLAIN, 14));
    buttonBad.setBounds(12,15,528,36);



    lblLog.setBackground(java.awt.Color.white);
    lblLog.setForeground(java.awt.Color.blue);
    lblLog.setBounds(570,ystart+labelh+labelgap,270,25);
    lblLog.setFont(new Font("Dialog", Font.BOLD, 14));
    lblLog.setText("OPTIMAL DESIGNS");
    lblLog.setVisible(false);
    add(lblLog);

    txtLog.setBackground(java.awt.Color.white);
    txtLog.setForeground(java.awt.Color.black);
    txtLog.setBounds(570,ystart+labelh+labelgap+30,400,labelh*9+15);
    txtLog.setFont(new Font("Dialog", Font.PLAIN, 11));
    txtLog.append("Num  alpha  beta     p0         p1         n1      r1       n        r     E(n)     P(Stop|H0)\n");
    txtLog.setVisible(false);
    add(txtLog);

    // jTable from netbeans
    inittableAnswer ();
    add(tableAnswer);

    add(buttonGo);
    buttonGo.setFont(new Font("Dialog", Font.BOLD, 14));
    buttonGo.setLabel("Calculate");
    buttonGo.setActionCommand("button");
    buttonGo.setBounds(420,450,122,labelh);
    //}}

    //{{REGISTER_LISTENERS
    SymAction lSymAction = new SymAction();
    buttonGo.addActionListener(lSymAction);
    SymMouse aSymMouse = new SymMouse();
    lblBeta.addMouseListener(aSymMouse);
    lblPrResp0.addMouseListener(aSymMouse);
    lblAlpha.addMouseListener(aSymMouse);
    lblPrResp1.addMouseListener(aSymMouse);
    SymText lSymText = new SymText();
    SymKey aSymKey = new SymKey();
    //}}

    badString = new String("This entry ihas been changed. \nClick any buttons to update.");
    goodString = new String("Welcome!! You can change numbers and click any buttons to calculate.");
    buttonGood.setLabel(goodString);
    buttonBad.setLabel(badString);
    red = lblPrResp1.getBackground();
    yellow = lblBeta.getBackground(); //16776960
    extractValues();
    thingsAreGood();
    txtBeta.addTextListener(lSymText);
    txtPrResp0.addTextListener(lSymText);
    txtAlpha.addTextListener(lSymText);
    txtPrResp1.addTextListener(lSymText);
    txtBeta.addKeyListener(aSymKey);
    txtPrResp0.addKeyListener(aSymKey);
    txtAlpha.addKeyListener(aSymKey);
    txtPrResp1.addKeyListener(aSymKey);
  }


  //{{DECLARE_CONTROLS
  java.awt.Button lblBeta = new java.awt.Button();
  java.awt.Button buttonGood = new java.awt.Button();
  TextField txtBeta = new TextField();
  java.awt.Button lblPrResp0 = new java.awt.Button();
  TextField txtPrResp0 = new TextField();
  java.awt.Button lblAlpha = new java.awt.Button();
  TextField txtAlpha = new TextField();
  java.awt.Button lblPrResp1 = new java.awt.Button();
  TextField txtPrResp1 = new TextField();
  TextArea  txtLog = new TextArea(" ",0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
  Label lblLog = new Label();
  java.awt.Button buttonBad = new java.awt.Button();
  java.awt.Panel tableAnswer = new java.awt.Panel();
  java.awt.Button buttonGo = new java.awt.Button();
  //}}

  void outputToLog(){

    if ( Val_Final  == INIT_VAL_FINAL ) {
      stringThingsBad="Sorry. For the previous input values, the sample size will be larger than "+nmax+".";
    }
    else {
       for ( int m=1; m<=2; m++) //row
          for ( int l=1; l<=2; l++ ){    //col
            cell[m][l].setForeground(java.awt.Color.black);
      }
      cell[1][1].setText("   "+n1_final);
      cell[1][2].setText("   "+n_final);
      cell[2][1].setText("   "+r1_Final);
      cell[2][2].setText("   "+r_Final);

      if ( iCal == 1 ){
          goodString = new String("Answers are now updated. \nChange numbers to explore other possibilities.");
          buttonGood.setLabel(goodString);
          txtLog.setVisible(true);
          lblLog.setVisible(true);
      }
      buttonGood.setLabel(goodString);
      tableAnswer.setVisible(true);

      txtLog.append(iCal+"         ");
      txtLog.append(Alpha+"     ");
      txtLog.append(Beta+"     ");
      txtLog.append(PrResp0+"     ");
      txtLog.append(PrResp1+"     ");

      for (  int i=1; i<=2; i++) {//col
              for ( int j=1; j<=2; j++ ){    //row
                 txtLog.append(cell[j][i].getText()+"    ");
             }
       }
       txtLog.append(round2(Val_Final)+"   "+round2(PET_final));
       txtLog.append("\n");
    }
 }
  class SymAction implements java.awt.event.ActionListener
  {
    public void actionPerformed(java.awt.event.ActionEvent event)
    {
      Object object = event.getSource();
      if ( extractAndCheckValues()) {
        thingsAreBad();
      }
      else {
        if (object == buttonGo){
           thingsAreGood();
           iCal++;
           Phase2Calculate();
           outputToLog();
        }
      }
     }
  }

     boolean extractAndCheckValues(){
      extractValues();
      stringThingsBad="";
      try
      {
         if ( Alpha < 0.0 || Alpha > 0.5 ){
                stringThingsBad="Please enter value between 0 and 0.5.";
                return true;
               }
          if ( Beta < 0.0 || Beta > 0.3 ){
                stringThingsBad="Please enter the value between 0 and 0.3 only.";
                return true;
          }
          if ( PrResp0 < 0.0 || PrResp0 > 1.0 ){
                stringThingsBad="Please enter the value between 0 and 1.0 only.";
                return true;
          }
          if ( PrResp1 < 0.0 || PrResp1 > 1.0 ){
                stringThingsBad="Please enter the value between 0 and 1.0 only.";
                return true;
          }
          if ( PrResp1 <= PrResp0 ) {
               stringThingsBad="Value for 'good' response rate should be greater than 'poor' response rate.";
               return true;
          }
          return false;
      }
      catch (java.lang.NumberFormatException e){
          Logger.log("Number format error\\n");
          return false;
      }
    }

    void extractValues(){
      try
      {
          Alpha = (new Double(txtAlpha.getText())).doubleValue();
          Beta = (new Double(txtBeta.getText())).doubleValue();
          PrResp0 = (new Double(txtPrResp0.getText())).doubleValue();
          PrResp1 = (new Double(txtPrResp1.getText())).doubleValue();
          return;
      }
      catch (java.lang.NumberFormatException e){
          Logger.log("Number format error\\n");
      }
    }
    void thingsAreBad() {
      buttonBad.setLabel(stringThingsBad);
      buttonBad.setBackground(java.awt.Color.blue);
    }
    void thingsAreGood(){
      lblBeta.setBackground(yellow);
      lblAlpha.setBackground(yellow);
      lblPrResp0.setBackground(yellow);
      lblPrResp1.setBackground(yellow);

    //  buttonBad.setBackground(java.awt.Color.red);
    //  buttonBad.setLabel("");
      buttonBad.setVisible(false);
      buttonGood.setVisible(true);
    }

  class SymMouse extends java.awt.event.MouseAdapter
  {
    public void mouseClicked(java.awt.event.MouseEvent event)
    {
      if ( extractAndCheckValues())
         thingsAreBad();
      else {
         thingsAreGood();
         iCal++;
         Phase2Calculate();
         outputToLog();
      }
    }
  }
  String round2(double d){
      int i = (int) (d*100+0.5);
      String s = Double.toString(((double)i)/100);
      int decimal = s.indexOf(".");
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
      //button1.setLabel(badString);
      //refreshNumbers(object);
    }
  }
  void refreshNumbers(Object object){
        try
      {
        double d =  new Double(((TextField)object).getText())
                        .doubleValue();
        if (object==txtBeta)Beta= d;
        if (object==txtPrResp0)PrResp0= d;
        if (object==txtAlpha)Alpha= d;
        if (object==txtPrResp1)PrResp1= d;

        if (object==txtBeta)lblBeta.setBackground(red);
        if (object==txtPrResp0)lblPrResp0.setBackground(red);
        if (object==txtAlpha)lblAlpha.setBackground(red);
        if (object==txtPrResp1)lblPrResp1.setBackground(red);
    }
    catch (java.lang.NumberFormatException e){
        Logger.log("Number format error\\n");
    }
    return;
  }

  class SymKey extends java.awt.event.KeyAdapter
  {
    public void keyTyped(java.awt.event.KeyEvent event)
    {
      Object object = event.getSource();
      buttonGood.setVisible(false);
      buttonBad.setLabel("This entry ihas been changed. \nClick any buttons to update.");
      buttonBad.setVisible(true);
      refreshNumbers(object);
    }
  }
   /** This method is called from within the init() method to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the FormEditor.
   */
   private void inittableAnswer () {
    int i,j;
    final int cellh=25, cellw=87, cellxstart=349,cellystart=47;

    tableAnswer.setLayout(null);
    tableAnswer.setSize(525,100);
    tableAnswer.setFont(new Font("Dialog", Font.BOLD, 12));

    Label title = new Label("THE OPTIMAL SOLUTION",Label.LEFT);
    title.setBackground(java.awt.Color.white);
    title.setForeground(java.awt.Color.red);
    title.setBounds(0,0,525,19);
    tableAnswer.add(title);

    cell[0][0]=new Label("Design Parameter",Label.CENTER);
    cell[0][0].setBounds(1,cellystart-cellh-1,cellxstart-2,cellh);
    tableAnswer.add(cell[0][0]);
    cell[0][1] = new Label("First Stage",Label.CENTER);
    cell[0][1].setBounds(cellxstart,cellystart-cellh-1,cellw,cellh);
    tableAnswer.add(cell[0][1]);
    cell[0][2] = new Label("Second Stage",Label.CENTER);
    cell[0][2].setBounds(cellxstart+(cellw+1),cellystart-cellh-1,cellw,cellh);
    tableAnswer.add(cell[0][2]);
    cell[1][0] = new Label("Sample Size ",Label.CENTER);
    cell[1][0].setBounds(1,cellystart,cellxstart-2,cellh);
    tableAnswer.add(cell[1][0]);
    cell[2][0] = new Label("Upper Limit Of Rejecting Drug Due To Inadequate Response",Label.CENTER);
    cell[2][0].setBounds(1,(cellystart+(2-1)*(cellh+1)),cellxstart-2,cellh);
    tableAnswer.add(cell[2][0]);

    i=0;
    for ( j=0; j<=2; j++ ){    //col
        cell[i][j].setBackground(java.awt.Color.white);
        cell[i][j].setForeground(java.awt.Color.blue);
       // cell[i][j].setFont(new Font("Dialog", Font.BOLD, 12));
    }
    j=0;
    for ( i=0; i<=2; i++ ){    //row
        cell[i][j].setBackground(java.awt.Color.white);
        cell[i][j].setForeground(java.awt.Color.blue);
       // cell[i][j].setFont(new Font("Dialog", Font.BOLD, 12));
    }

    for ( i=1; i<=2; i++) //row
      for ( j=1; j<=2; j++ ){    //col
        cell[i][j] = new Label(" ",Label.LEFT);
        cell[i][j].setBackground(java.awt.Color.white);
        cell[i][j].setBounds((cellxstart+(j-1)*(cellw+1)),(cellystart+(i-1)*(cellh+1)),cellw,cellh);
        cell[i][j].setFont(new Font("Dialog", Font.PLAIN, 12));
        tableAnswer.add(cell[i][j]);
      }

    tableAnswer.setBackground(java.awt.Color.lightGray);
    tableAnswer.setBounds(20,330,525,100);
    tableAnswer.setVisible(false);
  }
 void SetWaitInfo(){
    buttonGood.setLabel("Your computing is in progress.");
    buttonGood.setVisible(true);
    buttonBad.setVisible(false);
    for ( int i=1; i<=2; i++) //row
      for ( int j=1; j<=2; j++ ){    //col
        cell[i][j].setForeground(java.awt.Color.lightGray);
    }
 }

 /*
function phaseii_simon() to create results of phaseII design
input: Alpha -- type I error
     beta  -- type II error
     PrResp0 -- "bad" response rate
     PrResp1 -- "good" response rate
     nmax -- patient upper limit (maximum sample size )
output r1_final, n1_final, r_final, n_final
*/
final int PrResp0_TYPE= 1;
final int PrResp1_TYPE= 0;
final int MAX_SAMPLE_SIZE=210;
final int INIT_VAL_FINAL=1000;
double[][]  AB_array_PrResp0 = new double [MAX_SAMPLE_SIZE+1][MAX_SAMPLE_SIZE+1];
double[][]  AB_array_PrResp1 = new double [MAX_SAMPLE_SIZE+1][MAX_SAMPLE_SIZE+1];
double[][]  b_array_PrResp0 = new double [MAX_SAMPLE_SIZE+1][MAX_SAMPLE_SIZE+1];
double[][]  b_array_PrResp1 = new double [MAX_SAMPLE_SIZE+1][MAX_SAMPLE_SIZE+1];
double PET_final, Val_Final;
int r1_Final,r_Final,n1_final, n_final;
void  Phase2Calculate()
{
    int i,n,nmin,r; /* loop control variables */
    int n1_low,r_Old,r1,r2,n1,n2;
    int[] R_max=new int[MAX_SAMPLE_SIZE];
    boolean start_next_n2;
    double Fun_val,Val;

    SetWaitInfo();

    /* set R_max array to -1 in the begining */
    for (i=0; i < MAX_SAMPLE_SIZE; i++)
      R_max[i] = -1;

    /* initialize Val_Final, r1_Final and n1_final */
    r1_Final = 0;r_Final=0;n1_final = nmax;n_final=0;
    Val_Final  = (double)INIT_VAL_FINAL;

    /* done with reding input values now create tables */

    for (n=1; n <= nmax; n++)
    {
      /****************************************************
        calculate values of B(0,n,PrResp0) = b(0,n,PrResp0) = (1-PrResp0)^n
        calculate values of B(0,n,PrResp1) = b(0,n,PrResp1) = (1-PrResp1)^n
       *****************************************************/
       AB_array_PrResp0[0][n] = b_array_PrResp0[0][n] = (double)Math.pow((double)(1.0 - PrResp0),
           (double)n);
       AB_array_PrResp1[0][n] = b_array_PrResp1[0][n] = (double)Math.pow((double)(1.0 - PrResp1),
           (double)n);
       for(r=1; r <= n; r++)
       {
             b_array_PrResp0[r][n] = comp_cumu_binomial(r,n,PrResp0,PrResp0_TYPE);
             b_array_PrResp1[r][n] = comp_cumu_binomial(r,n,PrResp1,PrResp1_TYPE);

             AB_array_PrResp0[r][n] = AB_array_PrResp0[r-1][n] + b_array_PrResp0[r][n];
             AB_array_PrResp1[r][n] = AB_array_PrResp1[r-1][n] + b_array_PrResp1[r][n];
        }  /* end of loop over r */
    } /* end of loop over n */

    /* done with building arrays now process them */

    /* find n1_low from the array AB_array_PrResp1 */
    n1_low = 0;
    for ( n=1; n <= nmax; n++)
    {
       if ( AB_array_PrResp1[0][n] <= Beta)
       {
            n1_low = n;
            break;
       }
    } /* end of for n < nmax */

    /* intialize value of r_Old */
    r_Old = nmax;

    /* load the R_max array with values */
    for(n=nmax; n >= n1_low; --n)
    {
      for(r=r_Old; r >= 0; --r)
      {
           if (AB_array_PrResp1[r][n] <= Beta)
           {
             r_Old = r;
             R_max[n] = r_Old;
             break;
           }
           else
           R_max[n] = -1;
      } /* end of for r=r_Old */
    } /* end of for n > n1_low */

    /* compute nmin */
    nmin = compute_nmin();

    /* do the final computations now */

    for (n1=n1_low; n1 <= nmax-1; ++n1)   /* loop 6 */
    {
      if(R_max[n1] < 0)
         continue;

     for (n2=Math.max(1,nmin-n1); n2 <= nmax-n1; ++n2)  /* loop 7 */
     {
         start_next_n2 = true;

          /* loop 8 */
         for(r1=R_max[n1]; r1 >= 0 && start_next_n2; --r1)
         {
         /* loop 9 */
            for(r2=R_max[n1+n2]; r2 >= 0 && start_next_n2; --r2)
            {
                 if ((Fun_val = compute_FUN(r1,r2,n1,n2,PrResp0_TYPE)) <
                    (1-Alpha))
                     break;

                 if((Fun_val = compute_FUN(r1,r2,n1,n2,PrResp1_TYPE)) > Beta)
                    continue;

                 Val = (n1+n2) - (n2*AB_array_PrResp0[r1][n1]);

                 if(Val < Val_Final)
                 {
                       Val_Final = Val;
                       r1_Final = r1;
                       r_Final = r2;
                       n1_final = n1;
                       n_final = n1+n2;
                       start_next_n2 = false;
                 } /* end of if Val < Val_Final */

          }  /* end of for r2 > 0 loop */
      } /* end of for r1 > 0 loop */
    }   /* end of n2 <= nmax-n1 loop */
  } /* end of n1 <= nmax-1 loop */

    /* compute PET_final and PET_max */
    PET_final = AB_array_PrResp0[r1_Final][n1_final];

}

/* function to compute nmin */
int compute_nmin()
{   int nmin;
    double z_Alpha,z_beta;

    z_Alpha = get_z_factor(Alpha);
    z_beta  = get_z_factor(Beta);

    nmin = (int)Math.pow (
     (
      z_Alpha * Math.pow(PrResp0*(1-PrResp0),0.5) +z_beta * Math.pow(PrResp1*(1-PrResp1),0.5)
    ) / (PrResp1 - PrResp0),2.0   );
    return(nmin-5);
} /* end of compute_nmin() */

double get_z_factor(double probability)
{
    if(probability <= (double)0.05)
       return((double)1.645);
    else if(probability <= (double)0.1)
            return((double)1.28);
         else if(probability <= (double)0.15)
              return((double)1.03);
              else if(probability <= (double)0.2)
                   return((double)0.84);
                   else if(probability <= (double)0.25)
                      return((double)0.67);
                        else if(probability <= (double)0.3)
                              return((double)0.52);
    /* should not get here */
    return (2.0);
} /* end of get_z_factor() */

double compute_FUN(int r1,int r2,int n1,int n2,int array_to_use)
{
      double FUN_value,sum,product;
      int x,limit;

      /* compute limit for summation */
      FUN_value = (double)0.0;
      limit = Math.min(n1,r2);

      if(array_to_use == PrResp0_TYPE)
      {
       sum = (double)0.0;
       if(limit > 0)
        {
             for (x=r1+1; x <= limit; x++)
             {
              product = b_array_PrResp0[x][n1] * AB_array_PrResp0[r2-x][n2];
              sum += product;
              }
        }
        FUN_value = AB_array_PrResp0[r1][n1] + sum;
      }
      else if (array_to_use == PrResp1_TYPE)
      {
             sum = (double)0.0;
             for (x=r1+1; x <= limit; x++)
             {
               product = b_array_PrResp1[x][n1] * AB_array_PrResp1[r2-x][n2];
               sum += product;
              }
              FUN_value = AB_array_PrResp1[r1][n1] + sum;
     }
      return(FUN_value);
} /* end of compute_FUN() */

double comp_cumu_binomial(int r,int n,double p,int array_to_use)
{
    double value1,value2;

    /* compute binomial probability */
    if ( p >= 1.0 )  {
      return (1.0);
    }
    else {
      value1 = p/(1-p);
      value2 = ((double)(n-r+1)/(double)r);

      if (array_to_use == PrResp0_TYPE)
        return(value1 * value2 * b_array_PrResp0[r-1][n]);
      else if (array_to_use == PrResp1_TYPE)
        return(value1 * value2 * b_array_PrResp1[r-1][n]);
    }
    /* should not get heare */
    return(0.0);
} /* end of comp_cumu_binomial() */
} // Phase2Calculate()