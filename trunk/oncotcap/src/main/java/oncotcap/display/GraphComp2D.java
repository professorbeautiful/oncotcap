package oncotcap.display;

import oncotcap.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.lang.reflect.Array;
import java.awt.geom.*;
/**
 * Graphing component. This component is used to view lines of data. Each line
 * is added using the <code>addLine</code> method and then the data is added
 * to each line using the <code>addData</code> method.
 *
 * @author David Markley
 * @version $Id:$
 *
 * @since JDK1.1
 */
public class GraphComp2D extends Canvas implements Loggable{
    private Image offScreenImage;
    private Graphics2D offScreenGC;
    private Vector lines;
    private double base10 = 0.4342944819032520;
    protected boolean logXScale = false, logYScale = false;
    double minXVal, minYVal, maxXVal, maxYVal;
	int nXPixels, nYPixels;
	protected double xStep, yStep;
    protected String xName = "Months", yName = "Cell Counts";
    protected int xOff = 150, yOff = 20;
	AffineTransform trans;
	javax.swing.JMenuBar menubar = new javax.swing.JMenuBar();

	/**
     * Constructor.
     *
     * @author David Markley
     *
     * @since JDK1.1
     */
	public GraphComp2D () {
		lines = new Vector(10,5);
		setBackground (Util.lighter(Color.yellow,4));
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				offScreenImage = null;
				repaint();
			}
		});
	}

	static void menuCrap(JFrame j) {
		JMenuBar menubar = new JMenuBar();
		j.setJMenuBar(menubar);
		JMenu menuZoom = new JMenu("Zoom");
		menuZoom.setMnemonic(KeyEvent.VK_Z);
		menubar.add( menuZoom);

		JMenuItem menuItemIn = new JMenuItem("In");
		menuItemIn.setMnemonic(KeyEvent.VK_I);
		JMenuItem menuItemOut = new JMenuItem("Out");
		menuItemOut.setMnemonic(KeyEvent.VK_O);
		menuZoom.add(menuItemIn);
		menuZoom.add(menuItemOut);

		JMenu menuHoriz = new JMenu("Horizontal");
		menubar.add( menuHoriz);
		menuHoriz.setMnemonic(KeyEvent.VK_H);

		JMenuItem menuItemRight = new JMenuItem("Right");
		menuItemRight.setMnemonic(KeyEvent.VK_R);
		menuHoriz.add(menuItemRight);
	}
	
	public static void main(String[] args) throws java.lang.InterruptedException{
		JFrame j1 = new JFrame();
		j1.setSize(800,300);
		//menuCrap(j1);
		GraphComp2D graphcomp = new GraphComp2D();
		graphcomp.setAxisIsLog10("y", false);
		graphcomp.setAxisBounds("x", 0, 50, 5);
		graphcomp.setAxisBounds("y", 1, 1.0e2, 1e1);
		graphcomp.setAxisName("x", "Months");
		graphcomp.setAxisName("y", "Cell counts");
		/*
		for (double t = 0; t<50; t += 0.1) {
			graphcomp.addData(t, Math.exp(t/4), "SA/SB");
			graphcomp.addData(t, 10*Math.exp(t/4), "SA/RB");
			graphcomp.addData(t, 100*Math.exp(t/4), "RA/SB");
			graphcomp.addData(t, 1000*Math.exp(t/4), "RA/RB");
		}
		*/
		for (double t = 0; t<50; t += 0.1) {
			graphcomp.addData(t, Math.exp(t/10), "SA/SB");
		}
		Logger.log(new Double(Math.exp(50/10)));
		j1.getContentPane().add(graphcomp);
		j1.setVisible(true);
		//while (true) {
			Graphics2D g2d = (Graphics2D)graphcomp.getGraphics();
			//g.setTransform(new AffineTransform());
			//Logger.log(g.getTransform());
			//g.rotate(1.5);
			//g2d.setTransform(AffineTransform.getRotateInstance(1.5));
			graphcomp.update(g2d);
			//g2d.setTransform(AffineTransform.getTranslateInstance(100.,0.));
			/*graphcomp.update(g2d);
			graphcomp.offScreenGC.rotate(1.4);
			Logger.log("Start sleep 1");
			Thread.currentThread().sleep(2000);
			Logger.log("End sleep 1");
			graphcomp.update(g2d);
			j1.paint(g2d);
			//g2d.setTransform(AffineTransform.getRotateInstance(1.5).
			//				 concatenate(AffineTransform.getScaleInstance(0.3,0.7)	));
			g2d.getTransform().
							 concatenate(AffineTransform.getScaleInstance(0.3,0.7)	);
			graphcomp.update(g2d);
			Logger.log("Start sleep 2");
			Thread.currentThread().sleep(2000);
			Logger.log("End sleep 2");
			j1.paint(g2d);
			*/
	//}			
/*
		JFrame j2 = new JFrame();
		j2.setSize(800,300);
		j2.setLocation(0,300);
		GraphComp2D gc2 = new GraphComp2D();
		gc2.setAxisBounds("x", 0, 50);
		gc2.setAxisBounds("y", 0, 5);
		gc2.setAxisName("x", "Months");
		gc2.setAxisName("y", "Toxicity Grade");

		gc2.addData(0, 0, "Neuro");
		gc2.addData(30, 0, "Neuro");
		gc2.addData(31, 1, "Neuro");
		gc2.addData(32, 2, "Neuro");
		gc2.addData(33, 3, "Neuro");
		gc2.addData(34, 2, "Neuro");
		gc2.addData(35, 1, "Neuro");
		gc2.addData(36, 0, "Neuro");
		gc2.addData(0, 0, "Hemat");
		gc2.addData(34, 0, "Hemat");
		gc2.addData(35, 1, "Hemat");
		gc2.addData(36, 2, "Hemat");
		gc2.addData(37, 3, "Hemat");
		gc2.addData(38, 1, "Hemat");
		j2.getContentPane().add(gc2);
		j2.setVisible(true);
		Graphics2D g = (Graphics2D)gc2.getGraphics();
		Logger.log(g);
		g.setTransform(new AffineTransform());
		Logger.log(g.getTransform());
		g.rotate(1.5);
		gc2.update(g);
		Logger.log("off " + gc2.offScreenGC);
		gc2.offScreenGC.rotate(1.4);
		j2.repaint();
//		gc2.concatenate(AffineTransform.getScaleInstance(0.3,0.7));
//		*/
	}
	public void setAxisIsLog10(String axis, boolean whether) {
		if ( axis.equals("x"))
			logXScale = whether;
		else if ( axis.equals("y"))
			logYScale = whether;
		else
			Logger.log("setAxisIsLog10 bad value for axis " + axis);
	}
	public void setAxisBounds(String axis, double minVal, double maxVal, double step) {
		if ( axis.equals("x")) {
			this.minXVal = minVal;
			this.maxXVal = maxVal;
			this.xStep = step;
		} else if ( axis.equals("y")) {
			this.minYVal = minVal;
			this.maxYVal = maxVal;
			this.yStep = step;
		} else
			Logger.log("setAxisBounds bad value for axis " + axis);
	}
	public void setAxisName(String axis, String name) {
		if ( axis.equals("x")) {
			xName = new String(name);
		}else if ( axis.equals("y")) {
			yName = new String(name);
		} else
			Logger.log("setAxisBounds bad value for axis " + axis);
	}
	
    /**
     * Supporting inner-class for GraphComp2D.
     *
     * @author David Markley
     *
     * @since JDK1.1
     */
    class DataLine  {
		protected String name = "Unknown";
		Vector data = new Vector();
		GeneralPath path = new GeneralPath();
		GraphComp2D gc;
		public Color pen = Color.black;
		protected boolean isVisible = true;
		Point2D.Double p_save = null;
		
		void add(Point2D.Double p) {
			data.addElement(new double[] {p.getX(), p.getY() } );
			if (p_save != null) 
				path.append(new Line2D.Double(p_save,p), false);
			p_save = p;
		}

		DataLine (GraphComp2D gc, String name, Color pen) {
			this.gc = gc;
			this.name = name;
			this.pen = pen;
		}

		/**
		 * Draws the this in the specified <code>Graphics</code> context.
		 *
		 * @author David Markley
		 *
		 * @param  g  Graphics context to draw in
		 *
		 * @since JDK1.1
		 */

		public synchronized void drawSelf(Graphics2D g) {
			if (! isVisible) { return; }
			double p1[];
			double p2[];
			int x1, y1, x2, y2;

			
			PathIterator pathIt = path.getPathIterator(new AffineTransform());
			pathIt.next();
			double [] coords = new double[6];
			int pathType = pathIt.currentSegment(coords);
			int j=0;
			//Logger.log("coords " + coords[j++] + " "  + coords[j++] + " "
			//				   + coords[j++] + " " + coords[j++] + " "
			//				   + coords[j++] + " " + coords[j++] + " ");
			p1 = (double [])data.firstElement();
			g.setColor(pen);
			x1 = getPixel(p1[0], minXVal, maxXVal, logXScale, nXPixels, xOff);
			y1 = getPixel(p1[1], minYVal, maxYVal, logYScale, nYPixels, yOff);

			/*if (YAxisIsLogged() && (0 != p1[1])) {
				g.drawLine(xOff, (int)nYPixels, x1, (int)nYPixels-y1);
			}/*/
			
			for (int i = 1; i < data.size(); i++) {
				p2 = (double [])data.elementAt(i);
				if (i==1 || i== data.size()-1)
					Util.log(gc,1,"Line " + name + " p2[0]=" + p2[0]
								   +  " p2[1]=" + p2[1] + " i=" + i + " out of " + data.size());
				if (YaxisLogged() && (0 == p1[1] || 0 == p2[1])) {
					//Skip the drawing-- cell count went to zero.
					p1 = p2;
					continue;
				}
				x1 = getPixel(p1[0], minXVal, maxXVal, logXScale, nXPixels, xOff);
				y1 = getPixel(p1[1], minYVal, maxYVal, logYScale, nYPixels, yOff);
				x2 = getPixel(p2[0], minXVal, maxXVal, logXScale, nXPixels, xOff);
				y2 = getPixel(p2[1], minYVal, maxYVal, logYScale, nYPixels, yOff);
				g.drawLine(x1, nYPixels-y1+yOff, x2, nYPixels-y2+yOff);
				p1 = p2;
				/**/
			}
			g.draw(path);
			g.setColor(Color.orange);
			g.getTransform().concatenate(AffineTransform.getTranslateInstance(-100,20));
			g.draw(path);
			g.setColor(Color.red);
			g.getTransform().concatenate(AffineTransform.getRotateInstance(Math.PI/4.));
			g.draw(path);

		}
		int getPixel(double value, double minVal, double maxVal
					 , boolean logScale, int nPixels, int offset) {
			double fraction = logScale ?
									     log10(value/minVal)/log10(maxVal/minVal)
									   : (value-minVal)/(maxVal-minVal);
			int returnval =  (int)(fraction * nPixels + offset);
			Util.log(gc,3,"getPixel args: " + value + " " + minVal + " " + maxVal + " " +  logScale + " " +  nPixels + " " +  offset);
			Util.log(gc,3,"getPixel return: " + returnval);
			return returnval; 
		}
    }
	double log10 (double x) {
		return Math.log(x)/Math.log(10.0);
	}

    /**
     * Add a new, named data line to the graph.
     *
     * @author David Markley
     *
     * @param  name name to be used for the dataline
     *
     * @since JDK1.1
     */
    public void addLine(String name) {
		lines.addElement(new DataLine(this, name, Color.black));
    }

    /**
     * Add a new, named data line to the graph using the specified color.
     *
     * @author David Markley
     *
     * @param  name   name to be used for the dataline
     * @param  color  Color to be used when displaying the dataline
     *
     * @since JDK1.1
     */
    public void addLine(String name, Color c) {
		lines.addElement(new DataLine(this, name,c));
    }

    /**
     * Set the visibility of a named dataline.
     *
     * @author David Markley
     *
     * @param  name       name of the data line for which to set visibility
     * @param  isVisible  <code>true</code> if the named data line is visible 
     *
     * @since JDK1.1
     */
    public void setLineVisible(String name, boolean isVisible) {
		Enumeration en = lines.elements();
		while (en.hasMoreElements()) {
			DataLine dl = (DataLine)en.nextElement();
			if (dl.name.equals(name)) {
				dl.isVisible = isVisible;
				offScreenImage = null;
				repaint();
				return;
			}
		}
    }

    /**
     * Add data for all the data lines in this.
     *
     * @author David Markley
     *
     * @param  x  x position for the data in all data lines
     * @param  y  array of y positions that are mapped to each of the
     *            data lines in this.
     *
     * @since JDK1.1
     */
/*    public void addData(double x, double [] y)
	 throws ArrayIndexOutOfBoundsException {
	     if ( y.length != lines.size() ) {
			 throw new ArrayIndexOutOfBoundsException("Data points don't match number of lines");
	     }
	     for (int i = 0; i < y.length; i++) {
			 DataLine tmp = (DataLine)lines.elementAt(i);
			 double p [] = new double [4];
			 p[0] = x;
			 p[1] = y[i];
			 p[2] = Math.log(x)*base10;
			 //p[3] = Math.log(y[i])*base10;
			 p[3] = y[i];
			 tmp.data.addElement(p);
			 repaint();
	     }
    }
*/
	public void addData(double x, double y, String lineName) {
		/*double point [] = new double [2];
		point[0] = XaxisLogged() ? Math.log(x)*base10 : x;
		point[1] = YaxisLogged() ? Math.log(y)*base10 : y;
		point[0] = XaxisLogged() ? Math.log(x)*base10 : x;
		point[1] = YaxisLogged() ? Math.log(y)*base10 : y;
		*/
		Point2D.Double point = new Point2D.Double (x, y);
		DataLine theDataLine = findDataLine(lineName);
		theDataLine.add(point);
		repaint();
	}
	boolean XaxisLogged() {
		return logXScale;
	}
	boolean YaxisLogged() {
		return logYScale;
	}
	DataLine findDataLine(String lineName) {
		Iterator iter = lines.iterator();
		while (iter.hasNext()) {
			DataLine dataLine = (DataLine)iter.next();
			if (dataLine.name.equals(lineName))
				return dataLine;
		}
		DataLine newDataLine = new DataLine(this, lineName, getColor());
		lines.add(newDataLine);
		return newDataLine;
	}

	Color getColor() {
		Color colorArray [] = {
			Color.red, Color.blue, Color.green, Color.magenta,
			Color.cyan, Color.orange, Color.yellow
		};
		int whichLine = lines.size() + 1;
		Color myColor = colorArray [whichLine % Array.getLength(colorArray)];
		int lightness = whichLine / Array.getLength(colorArray);
		for (int i=0; i<lightness; i++)
			myColor = myColor.brighter();
		return myColor;
	}
			
    /**
     * Draw the graph represented by this.
     *
     * @author David Markley
     *
     * @param  g  Graphics context in which to draw the graph
     *
     * @since JDK1.1
     */
    public void paint( Graphics g ) {
		Graphics2D g2d = (Graphics2D) g;
		if (null == offScreenImage) { update(g2d); }
		Font f1 = g2d.getFont();
		FontMetrics fm = g2d.getFontMetrics();
		int fY = (int)nYPixels+yOff-fm.getAscent();
		g2d.setColor(Color.black);
		g2d.drawString(yName, (xOff-fm.stringWidth(yName))/2,fm.getHeight()+5);
		g2d.drawString(xName, xOff-5-fm.stringWidth(xName), fY);
		g2d.drawLine(xOff,0,xOff,(int)nYPixels);
		g2d.drawLine(xOff,(int)nYPixels,xOff+(int)nXPixels,(int)nYPixels);

		int f1h = fm.getHeight();
		Font f2 = new Font(f1.getName(), Font.PLAIN, (int)((double)f1.getSize()*0.7));
		g2d.setFont(f2);
		fm = g2d.getFontMetrics();
		int f2h = fm.getHeight();

		double rangeXVal = maxXVal - minXVal;
		if (logXScale) {
			for (double d = log10(minXVal); d <= log10(maxXVal); d += 1.0) {
				String s = new Integer((int)d).toString();

				int x = (int)(d/maxXVal*nXPixels)+xOff;
				int y = ((int)nYPixels)-5;
				g.drawLine(x,y,x,y+10);

				x -= fm.stringWidth(s)+3;
				g.drawString(s,x,y+f1h/2-f2h/2);
				g.setFont(f1);
				int xW = fm.stringWidth("10")/2;
				g.drawString("10",x-xW,fY);
				g.setFont(f2);
				fm = g.getFontMetrics();
				g.drawString(s,x+xW+5,fY-f2h/2);
			}
		} else {
				g2d.setFont(f1);
				fm = g2d.getFontMetrics();
				for (double d = minXVal; d <= maxXVal; d += xStep) {
					String s = new Integer((int)d).toString();
					int x = (int)((d-minXVal)/rangeXVal*nXPixels)+xOff;
					int y = ((int)nYPixels)-5;
					g2d.drawLine(x,y,x,y+10);
					g2d.drawString(s,x-fm.stringWidth(s)/2,fY);
				}
		}
		Util.log(this,2,"Completed X axis");

		if (logYScale) {
			for (double d = 1.0; d <= log10(maxYVal); d += 1.0) {
				g2d.setFont(f2);
				fm = g2d.getFontMetrics();
				String s = new Integer((int)d).toString();

				int x = ((int)xOff)-5;
				int y = (int)(nYPixels*(1-d/log10(maxYVal)));
				g2d.drawLine(x,y,x+10,y);
				x -= fm.stringWidth(s)+3;
				g2d.drawString(s,x,y+f1h/2-f2h/2);
				g2d.setFont(f1);
				g2d.drawString("10",x-fm.stringWidth("10")-10,y+f1h/2);
			}
		} else {
			double rangeYVal = maxYVal - minYVal;
			g2d.setFont(f1);
			fm = g2d.getFontMetrics();
			for (double d = minYVal; d <= maxYVal; d += yStep) {
			String s = new Integer((int)d).toString();
			int x = ((int)xOff)-5, y = (int)(nYPixels* (1-(d-minYVal)/rangeYVal));
			//Changed nXPixels to xOff, to get the vertical scale
			//positioned correctly=    Feb 19, R.D..
			g2d.drawLine(x,y,x+10,y);
			g2d.drawString(s,x-fm.stringWidth(s)-5,y-f1h/2);
			}
		}
		Util.log(this,2,"Completed Y axis.  lines.size() is " + lines.size());
		for (int i = 0; i < lines.size(); i++) {
			DataLine dl = (DataLine)lines.elementAt(i);
			dl.drawSelf(g2d);
			g2d.setFont(f1);
			g2d.setColor(Color.black);
			int y = (i+3)*f1h;
			g2d.drawString(dl.name,xOff+5+30+5,y);
			g2d.setColor(dl.pen);
			y -= f1h/2;
			g2d.drawLine(xOff+5,y,xOff+5+30,y);
		}
		Util.log(this,2,"Completed paint");
    }

    /**
     * Scale the graph appropriately and request a redraw. 
     *
     * @author David Markley
     *
     * @param  g  Graphics context in which to draw the graph
     *
     * @since JDK1.1
     */
    public void update( Graphics g ) {
		drawVerticalAxis(g);
	}
	
	void drawVerticalAxis( Graphics g) {
		if ( offScreenImage == null ) {
			yOff = g.getFontMetrics().getHeight()*2;
			nYPixels = (getSize().height-yOff);
			nXPixels = (getSize().width-xOff);
			offScreenImage = createImage( getSize().width, getSize().height );
			offScreenGC = (Graphics2D)offScreenImage.getGraphics();
		}
		paint( offScreenGC );
		if (null != offScreenImage) {
		  //((Graphics2D)g).drawImage(offScreenImage, 
			//						AffineTransform.getRotateInstance(1.0), this);
			((Graphics2D)g).drawImage(offScreenImage, 0, 0, this);
		}
    }

    /**
     * Specifies the preferred size for this.
     *
     * @author David Markley
     *
     * @return Dimension preferred size
     *
     * @since JDK1.1
     */
    public Dimension getPreferredSize() {
		return getMinimumSize();
    }

    /**
     * Specifies the mimimum size for this.
     *
     * @author David Markley
     *
     * @return Dimension mimimum size
     *
     * @since JDK1.1
     */
    public Dimension getMinumumSize() {
        return new Dimension(250,100);
    }
	/** Loggable interface implementation:
	 **/
	LoggableAdapter loggableAdapter = new LoggableAdapter(0);
	public int verbose() { return loggableAdapter.verbose(); }
	public void setVerbose(int v) { loggableAdapter.setVerbose(v); }
	public String logName() { return loggableAdapter.logName(this); }

}
