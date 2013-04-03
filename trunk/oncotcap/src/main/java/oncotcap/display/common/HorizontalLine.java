package oncotcap.display.common;

import javax.swing.*;
import java.awt.*;


/** HorizontalLine
 ** Simple JComponent that displays a horizontal line.  The weight
 ** and color of the line can be controlled.
 **
 ** @version 1, 9/11/2002
 ** @author shirey
 **/
public class HorizontalLine extends JComponent
{

	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		jf.setSize(300,300);
		HorizontalLine line = new HorizontalLine(10f);
		jf.getContentPane().add(line);
		jf.setVisible(true);
	}
	private BasicStroke stroke;
	private Color color = Color.BLACK;
	private float weight;
	
	/**
	 ** Construct a black horizontal line with a weight of 1.
	 **/
	public HorizontalLine()
	{
		this(1f);
	}
	
	/**
	 ** Construct a horizontal line with weight of 1 and a specified
	 ** weight.
	 **
	 ** @param c Line color.
	 **/
	HorizontalLine(Color c)
	{
		this(1f, c);
	}
	
	/**
	 ** Construct a black horizontal line with a specified weight.
	 **
	 ** @param weight Line weight
	 **/
	public HorizontalLine(float weight)
	{
		this.weight = weight;

		//the line weight is implemented by setting the pen stoke in the
		//paint method.
		stroke = new BasicStroke(weight);
		
		//set the component size.  Fix the height to the weight of the
		//line.  The length (width) is not fixed.
		setMinimumSize(new Dimension(0, (int) weight));
		setPreferredSize(new Dimension(0, (int) weight));
		setMaximumSize(new Dimension(Short.MAX_VALUE, (int) weight));
	}
	
	/**
	 ** Construct a horizontal line with specified weight and color.
	 **
	 ** @param c Line color
	 ** @param weight line weight
	 **/
	public HorizontalLine(float weight, Color c)
	{
		this(weight);
		color = c;
	}
	
	/**
	 ** Set the line color.
	 **
	 ** @param c Line color
	 **/
	public void setColor(Color c)
	{
		color = c;
		repaint();
	}
	
	/**
	 ** Set the line weight.
	 **
	 ** @param Line weight
	 **/
	public void setWeight(float weight)
	{
		this.weight = weight;

		//the line weight is implemented by setting the pen stoke in the
		//paint method.
		stroke = new BasicStroke(weight);

		//set the component size.  Fix the height to the weight of the
		//line.  The length (width) is not fixed.
		setMinimumSize(new Dimension(0, (int) weight));
		setPreferredSize(new Dimension(0, (int) weight));
		setMaximumSize(new Dimension(Short.MAX_VALUE, (int) weight));
		
		//invalidate this component so the upper gui containers know it
		//has changed and needs to laid out again.
		invalidate();
	}

	//override paint method to draw our line
	public void paint(Graphics g)
	{
		//save the current color and stroke settings
		Color saveColor = g.getColor();
		g.setColor(color);
		Graphics2D g2 = (Graphics2D) g;
		Stroke saveStroke = g2.getStroke();
		
		//stroke is used to set the weight of the line.
		g2.setStroke(stroke);
		
		//draw a line the full width of the component
		g2.drawLine(0,0,getWidth(),0);

		//restore color and stroke
		g.setColor(saveColor);
		g2.setStroke(saveStroke);
	}
}