package oncotcap.display;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
/**
 * Each graph has an observed and unobserved data.
 * @author Roger Day
 * @version $Id:$
 *
 * @since JDK1.1
 */
public class GraphCompNew extends GraphComp0 {

	JCheckBox whatWeSee = new JCheckBox("What we see", true);
	JCheckBox whatWeDontSee = new JCheckBox("What we don't see", false);
	JLabel explainDash = new JLabel("Dashed line denotes treatment start");
	JButton btnDetach = new JButton("Detach");
	
	public Vector pointsWeSee = new Vector();
	public Vector linesWeDontSee = new Vector();
	JFrame parent = null;

	public GraphCompNew(JFrame parent) {
		this();
		this.parent = parent;
		setBounds(parent.getBounds());
		parent.getContentPane().add(this);
		parent.getContentPane().setLayout(null);
	}
	public GraphCompNew() {
		super();

//		parent.setBackground(Color.yellow);
//		parent.getContentPane().setBackground(Color.yellow);
		setBackground(new Color(defaultBackground.getRed(),defaultBackground.getGreen(),
								defaultBackground.getBlue(),255));
		//setOpaque(false);
		//parent.getContentPane().add(whatWeSee);
		//parent.getContentPane().add(whatWeDontSee);
		whatWeSee.setBounds((int) (xLeftOff+35.0),5,110,25);
		whatWeDontSee.setBounds((int) (xLeftOff+150.0),5,150,25);
		explainDash.setBounds((int) (xLeftOff+300.0),5,250,25);
		explainDash.setForeground(Color.red);
		explainDash.setVisible(false);
		btnDetach.setPreferredSize(new Dimension(100,22));
		btnDetach.setSize(100,22);
		btnDetach.setLocation(10,50);
		whatWeSee.setVisible(true);
		whatWeDontSee.setVisible(true);
		btnDetach.setVisible(true);
		add(whatWeSee);
		add(whatWeDontSee);
		add(explainDash);
//		add(btnDetach);
		repaint();
		whatWeSee.addMouseListener(new ChoiceListener());
		whatWeDontSee.addMouseListener(new ChoiceListener());
	}
	public static void main(String[] args) {
		JFrame j1 = new JFrame();
		j1.setSize(800,300);
		GraphCompNew gc = new GraphCompNew(j1);
		gc.setAxisIsLog10("y", true);
		gc.setAxisBounds("x", 0, 50, 10);
		gc.setAxisBounds("y", 1, 1.0e13, 1);
		gc.setAxisName("x", "Months");
		gc.setAxisName("y", "Cell counts");

		for (double t = 0; t<50; t += 0.1) {
			gc.addData(gc.linesWeDontSee, t, Math.exp(t/4), "SA/SB");
			gc.addData(gc.linesWeDontSee, t, 10*Math.exp(t/4), "SA/RB");
			gc.addData(gc.linesWeDontSee, t, 100*Math.exp(t/4), "RA/SB");
			gc.addData(gc.linesWeDontSee, t, 1000*Math.exp(t/4), "RA/RB");
		}
		for (double t = 0; t<50; t += 10) {
			gc.addData(gc.pointsWeSee, t, Math.exp(t/4), "SA/SB");
			gc.addData(gc.pointsWeSee, t, 10*Math.exp(t/4), "SA/RB");
			gc.addData(gc.pointsWeSee, t, 100*Math.exp(t/4), "RA/SB");
			gc.addData(gc.pointsWeSee, t, 1000*Math.exp(t/4), "RA/RB");
		}
		//j1.getContentPane().add(gc);
		j1.setVisible(true);
	}

	public void paint(Graphics g) {
		drawAxes(g);
		if (whatWeSee.isSelected())
			drawPoints(pointsWeSee, g);
		if (whatWeDontSee.isSelected())
			drawLines(linesWeDontSee, g,"lines");
		//whatWeSee.paint(g);
		//whatWeDontSee.paint(g);
		//Logger.log("Just painted checkboxes");
		paintComponents(g);
	}	
/*	public void drawPoints(Vector lines, Graphics g) {
		int x1 = getPixel(minXVal, minXVal, maxXVal, logXScale, nXPixels, xOff);
		int y1 = getPixel(maxYVal, minYVal, maxYVal, logYScale, nYPixels, yOff);
		int w = getPixel(maxXVal, minXVal, maxXVal, logXScale, nXPixels, xOff) - x1;
		int h = y1 - getPixel(minYVal, minYVal, maxYVal, logYScale, nYPixels, yOff);
		Rectangle clip = new Rectangle(x1, nYPixels-y1+yOff, w, h);
		Logger.log(" setClip(x1, nYPixels-y1+yOff, w, h) " + x1 + " "
						   + (nYPixels-y1+yOff) + " " + w + " " + h);
		//Util.log(this,2,"Completed Y axis.  lines.size() is " + lines.size());
		Font f1 = g.getFont();
		for (int i = 0; i < lines.size(); i++) {
			DataLine dl = (DataLine)lines.elementAt(i);
			g.setClip(clip);
			dl.drawPoints(g);
			g.setFont(f1);
			g.setColor(dl.pen);
			int y = (i+3)*f1h;
			g.setClip(0, 0, 800, 600);
			g.drawString(dl.name,5,y);
			g.setColor(dl.pen);
			y -= f1h/2;
			//g.drawLine(xOff+5,y,xOff+5+30,y);
		}
		Util.log(this,2,"Completed paint");
    }
    */
	class ChoiceListener extends MouseAdapter /*implements ItemListener*/ {
		//public void itemStateChanged(ItemEvent e) {
		//}
		public void mouseClicked(MouseEvent e) {
			//Logger.log("DayBoxListener mouseClicked");
			//updateTextFromBoxes();
		}
		public void mouseReleased(MouseEvent e) {
			//Logger.log("DayBoxListener mouseReleased");
//			offScreenImage = null;
			repaint();
		}
	}
	public void repaint() {
		super.repaint();
		//whatWeSee.repaint();
		//whatWeDontSee.repaint();
		//Logger.log("Repainting");
	}


	public void setDrawZeroAxis(boolean drawZeroAxis)
	{
		this.drawZeroAxis = drawZeroAxis;
		explainDash.setVisible(drawZeroAxis);
	}
}

