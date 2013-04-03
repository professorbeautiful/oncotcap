package oncotcap.display;

import oncotcap.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.util.*;
import javax.swing.*;
import java.lang.reflect.Array;
/**
 * Graphing component. This component is used to view lines of data. Each line
 * is added using the <code>addLine</code> method and then the data is added
 * to each line using the <code>addData</code> method.
 *
 * @author David Markley
 * @version $Id:$
 *
 * @version 2, rewrote to fix problems with grid not aligning with
 * data, added pan/zoom capabilities. -shirey
 *
 * @since JDK1.1
 */
public class GraphComp0 extends JComponent implements Loggable, ActionListener
{
    protected Vector<DataLine> lines;
    protected Vector<StringSet> stringSets;
    private double base10 = 0.4342944819032520;
    protected boolean logXScale = false, logYScale = false;
    protected double minXVal, minYVal, maxXVal, maxYVal;
	double minXOrig, minYOrig, maxXOrig, maxYOrig;
	int xPlaces, yPlaces;
	private double minY = java.lang.Double.NaN;
	private double minX = java.lang.Double.NaN;
	private double maxX = java.lang.Double.NaN;
	private double maxY = java.lang.Double.NaN;
	private int xPos, yPos;
	protected double xStep = 2.0, yStep = 10.0;
	protected double xStepOrig = 2.0, yStepOrig = 10.0;
    protected String xName = "Months", yName = "Cell Counts";
	protected double xLeftOff = 225.0, xRightOff = 10.0, yTopOff = 20.0, yBottomOff = 35.0;
	protected boolean drawZeroAxis = false;
	private static final BasicStroke dashStroke = new BasicStroke((float)1.0, BasicStroke.CAP_BUTT,
											 BasicStroke.JOIN_BEVEL, (float)1.0,
											 new float[]{10.0F,10.0F},
												(float)0.0);
	private static final BasicStroke basicStroke = new BasicStroke();
	private static final BasicStroke defaultStroke = new BasicStroke();
	private static final BasicStroke basicStroke1 = new BasicStroke(1);
	private static final BasicStroke basicStroke3 = new BasicStroke(3);
	
	private static final int XAXIS = 1;
	private static final int YAXIS = 2;
	private LegendPanel legend = null;
	
	private static final double event_y_del = 1.25;

	private Hashtable<String, Double> yAxisLabels = new Hashtable<String, Double>(); //holds Labels for the y axis and
    //the Y position <Label Name, Position>

	String name;
	
	protected Container parent;
	Color defaultBackground =  (Util.lighter(Color.yellow,1));

	JPopupMenu popup;
	
	/**
     * Constructor.
     *
     * @author David Markley
     *
     * @since JDK1.1
     */
	
	public GraphComp0 ()
	{
		super();
		init(this, defaultBackground);
	}
	public GraphComp0 (LegendPanel legend)
	{
		this();
		this.legend = legend;
	}
	public GraphComp0 (String name)
	{
		this(name, null);
	}
	public GraphComp0 (String name, LegendPanel legend)
	{
		this();
		this.legend = legend;
		setName(name);
	}
	public void setLegend(LegendPanel legend)
	{
		this.legend = legend;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public GraphComp0 (Container parent) {
		super();
		init(parent, defaultBackground);
	}
	public GraphComp0 (Container parent, Color background) {
		super();
		init(parent, background);
	}
	
	java.util.Hashtable  lineHash = new Hashtable();
	
	void init(Container parent, Color background) {
		this.parent = parent;
	
		lines = new Vector<DataLine>(10,5);
		stringSets = new Vector(10,5);
		
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				refreshPixelsForAllLines();
				repaint();
			}
		});
		MouseAdapter resizer = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				caughtMouseDown(e);
			}	
			public void mouseReleased(MouseEvent e) {
				caughtMouseUp(e);
			}
		}
		;
		addMouseListener(resizer);

		if (parent != null){ 
			if (parent instanceof JFrame)
				((JFrame)parent).getContentPane().setBackground(background);
			else
				parent.setBackground(background);
		}

		//Create the popup menu.
		popup = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("Zoom in");
		menuItem.addActionListener(this);
		popup.add(menuItem);
		menuItem = new JMenuItem("Zoom in X axes");
		menuItem.addActionListener(this);
		popup.add(menuItem);
		menuItem = new JMenuItem("Zoom in Y axes");
		menuItem.addActionListener(this);
		popup.add(menuItem);
		popup.addSeparator();
		menuItem = new JMenuItem("Zoom out");
		menuItem.addActionListener(this);
		popup.add(menuItem);
		menuItem = new JMenuItem("Zoom out X axes");
		menuItem.addActionListener(this);
		popup.add(menuItem);
		menuItem = new JMenuItem("Zoom out Y axes");
		menuItem.addActionListener(this);
		popup.add(menuItem);
		popup.addSeparator();
		menuItem = new JMenuItem("Original size & position");
		menuItem.addActionListener(this);
		popup.add(menuItem);
		
		MouseListener popupListener = new PopupListener();
		addMouseListener(popupListener);
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("Zoom in"))
			zoomIn();
		else if(e.getActionCommand().equals("Zoom out"))
			zoomOut();
		else if(e.getActionCommand().equals("Zoom in X axes"))
			zoomInX();
		else if(e.getActionCommand().equals("Zoom out X axes"))
			zoomOutX();
		else if(e.getActionCommand().equals("Zoom in Y axes"))
			zoomInY();
		else if(e.getActionCommand().equals("Zoom out Y axes"))
			zoomOutY();
		else if(e.getActionCommand().equals("Original size & position"))
			zoomOriginal();
	}
	private void zoomOriginal()
	{
		minXVal = minXOrig;
		maxXVal = maxXOrig;
		xStep = xStepOrig;
		minYVal = minYOrig;
		maxYVal = maxYOrig;
		yStep = yStepOrig;
		repaint();
		refreshPixelsForAllLines();
	}
	private void zoomIn()
	{
		double mouseX, mouseY;
		mouseX = unXPixel(xPos);
		mouseY = unYPixel(yPos);
		zoomInX(mouseX);
		zoomInY(mouseY);
		refreshPixelsForAllLines();
		repaint();
	}
	private void zoomInX()
	{
		double mouseX;
		mouseX = unXPixel(xPos);
		zoomInX(mouseX);
		refreshPixelsForAllLines();
		repaint();
	}
	private void zoomInX(double mouseX)
	{
		double newSpanX;
		if(logXScale)
		{
			newSpanX = (Util.log10(maxXVal) - Util.log10(minXVal))/4.0;
			minXVal = Math.pow(10.0,Util.log10(mouseX) + newSpanX);
			maxXVal = Math.pow(10.0,Util.log10(mouseX) - newSpanX);
			xStep = Math.pow(10.0,Util.log10(xStep)/2.0);
		}
		else
		{
			newSpanX = (maxXVal - minXVal)/4.0;
			minXVal = mouseX - newSpanX;
			maxXVal = mouseX + newSpanX;	
			xStep /= 2.0;
		}
	}
	private void zoomInY()
	{
		double mouseY = unXPixel(yPos);
		zoomInY(mouseY);
		refreshPixelsForAllLines();
		repaint();
	}
	
	private void zoomInY(double mouseY)
	{
		double newSpanY;
		if(logYScale)
		{
			newSpanY = (Util.log10(maxYVal) - Util.log10(minYVal))/4.0;
			minYVal = Math.pow(10.0,Util.log10(mouseY) - newSpanY);
			maxYVal = Math.pow(10.0,Util.log10(mouseY) + newSpanY);
			yStep /= 2.0;
			yStep = Math.max(yStep, 1.0);
		}
		else
		{
			newSpanY = (maxYVal - minYVal)/4.0;
			minYVal = mouseY - newSpanY;
			maxYVal = mouseY + newSpanY;
			yStep /= 2.0;
		}
	}
	private void zoomOut()
	{
		double mouseX = unXPixel(xPos);
		double mouseY = unYPixel(yPos);
		zoomOutX(mouseX);
		zoomOutY(mouseY);
		refreshPixelsForAllLines();
		repaint();
	}
	private void zoomOutX()
	{
		double mouseX = unYPixel(yPos);
		zoomOutX(mouseX);
		refreshPixelsForAllLines();
		repaint();
	}
	private void zoomOutX(double mouseX)
	{
		double newSpanX;
		if(logXScale)
		{
			newSpanX = (Util.log10(maxXVal/minXVal));
			minXVal = Math.pow(10.0,Util.log10(mouseX) - newSpanX);
			maxXVal = Math.pow(10.0,Util.log10(mouseX) + newSpanX);			
			xStep = Math.pow(10.0,Util.log10(xStep)*2.0);
		}
		else
		{
			newSpanX = (maxXVal - minXVal)/2.0;
			minXVal -= newSpanX;
			maxXVal += newSpanX;
			xStep *= 2.0;
		}
	}
	private void zoomOutY()
	{
		double mouseY = unYPixel(yPos);
		zoomOutY(mouseY);
		refreshPixelsForAllLines();
		repaint();
	}
	private void zoomOutY(double mouseY)
	{
		double newSpanY;
		if(logYScale)
		{
			newSpanY = Util.log10(maxYVal/minYVal);
			minYVal = Math.pow(10.0,Util.log10(mouseY) - newSpanY);
			maxYVal = Math.pow(10.0,Util.log10(mouseY) + newSpanY);			
			yStep *= 2;
		}
		else
		{
			newSpanY = (maxYVal - minYVal)/2.0;
			minYVal -= newSpanY;
			maxYVal += newSpanY;			
			yStep *= 2.0;
		}
	}
	private void pan(int x, int y)
	{
		double xDist, yDist;
		if(logXScale)
		{
			xDist = (((double)x)/(getWidth()-xRightOff-xLeftOff)) * (log10(maxXVal) - log10(minXVal));
			minXVal = Math.pow(10.0, log10(minXVal) + xDist);
			maxXVal = Math.pow(10.0, log10(maxXVal) + xDist);
		}
		else
		{
			xDist = (((double)x)/(getWidth()-xRightOff-xLeftOff)) * (maxXVal - minXVal);
			minXVal += xDist;
			maxXVal += xDist;
		}
		if(logYScale)
		{
			yDist = (((double)y)/(getHeight()-yTopOff-yBottomOff)) * (log10(maxYVal) - log10(minYVal));
			minYVal = Math.pow(10.0, log10(minYVal) + yDist);
			maxYVal = Math.pow(10.0, log10(maxYVal) + yDist);
		}
		else
		{
			yDist = (((double)y)/(getHeight()-yTopOff-yBottomOff)) * (maxYVal - minYVal);
			minYVal += yDist;
			maxYVal += yDist;
		}
		refreshPixelsForAllLines();
		repaint();
	}
	private int downX, downY;
	private static final int panSensitivity = 5;
	private Rectangle rect = new Rectangle();
	void caughtMouseDown(MouseEvent e)
	{
		downY = e.getY();
		downX = e.getX();
	}
	void caughtMouseUp(MouseEvent e)
	{
		int nowY = e.getY();
		int nowX = e.getX();
		rect.setBounds(downX - panSensitivity, downY - panSensitivity, panSensitivity * 2, panSensitivity * 2);
		if( downX > xLeftOff &&
			downY > yTopOff &&
			downX < getWidth()-xRightOff &&
			downY < getHeight() - yBottomOff &&
			!rect.contains(nowX, nowY))
		{
			pan(downX - nowX, nowY - downY);
		}
	} 
	public double xZero = 0.0;
	
	public void setXZero(double xZero) {
		// adjust X scale
		this.xZero = xZero;
		refreshPixelsForAllLines();
		repaint();		
	}
	public void setXZero(oncotcap.util.OncTime time) {
		this.xZero = OncTime.convert(time,OncTime.MONTHS);
		refreshPixelsForAllLines();
		repaint();		
	}

	public static void main(String[] args) {
		JFrame j1 = new JFrame();
		j1.setSize(800,400);
		GraphComp0 gc = new GraphComp0();
		gc.setSize(800,400);
//		gc.setAxisIsLog10("y", true);
//		gc.setAxisIsLog10("y", false);
		gc.setAxisBounds("x", 0, 12, 1);
		gc.setAxisBounds("y", 1, 200, 20);
//		gc.setAxisBounds("y", 0, 11, 1);
		gc.setAxisName("x", "Months");
		gc.setAxisName("y", "Cell counts");
		
		for (double t = 0; t<50; t += 0.1) {
			gc.addData(t, Math.exp(t/2)/1000, "SA/SB");
			gc.addData(t, Math.exp(t/2)/100, "SA/RB");
			gc.addData(t, Math.exp(t/2)/10, "RA/SB");
			gc.addData(t, Math.exp(t/2), "RA/RB");
		}
		
//		gc.addData(0.0, 0.0, "Line");
//		gc.addData(10.0, 10.0, "Line");

/*		for (double t = 0; t<10; t += 1) {
			gc.addData(t, t, "SA/SB");
//			gc.addData(t, t/6 + 2, "SA/RB");
//			gc.addData(t, t/6 + 3, "RA/SB");
//			gc.addData(t, t/6 + 4, "RA/RB");
		} */
		gc.setVisible(true);
		j1.getContentPane().add(gc);
		j1.setVisible(true);
/*
		JFrame j2 = new JFrame();
		j2.setSize(800,300);
		j2.setLocation(0,300);
		GraphComp0 gc2 = new GraphComp0();
		gc2.setAxisBounds("x", 0, 50, 10);
		gc2.setAxisBounds("y", 0, 5, 0.5);
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
*/
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
		setAxisBounds(axis, minVal, maxVal, step, 1) ;
	}

	public void setAxisBounds(String axis, double minVal, double maxVal, double step, int places) {
		if ( axis.equals("x")) {
			this.minXVal = minVal;
			this.minXOrig = minVal;
			this.maxXVal = maxVal;
			this.maxXOrig = maxVal;
			this.xStep = step;
			this.xStepOrig = step;
			this.xPlaces = places;
		}else if ( axis.equals("y")) {
			this.minYVal = minVal;
			this.minYOrig = minVal;
			this.maxYVal = maxVal;
			this.maxYOrig = maxVal;
			this.yStep = step;
			this.yStepOrig = step;
			this.yPlaces = places;
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
	public void autoScaleY()
	{
		if(!Double.isNaN(minY) &&  !Double.isNaN(maxY))
		{
			double max = maxY * 1.1;
			double min;
			double step = (max - minY) /10.0;
			double roundToNearest = Math.pow(10, Math.round(log10(step)));
			max = (Math.floor(max/roundToNearest) + 1) * roundToNearest;
			min = Math.floor(minY/roundToNearest) * roundToNearest;

			if(maxY > 1e8)
			{
				step = Math.max(Math.round(step/roundToNearest), 1.0);
				setAxisIsLog10("y", true);
				setAxisBounds("y", 1, 1e13, 1);
			}
			else
			{
//				step = Math.round(step/roundToNearest) * roundToNearest;
//				if(step <= 0)
					step = roundToNearest;
					double logRoundToNearest = log10(roundToNearest);
					int places = 1;
					if(logRoundToNearest < 0)
						places = (int) (-1 * logRoundToNearest) + 1;
				setAxisBounds("y", min, max, step, places);
			}
			
			repaint();
		}
	}
    /**
     * Supporting inner-class for GraphComp0.
     *
     * @author David Markley
     *
     * @since JDK1.1
     */
    protected class StringSet extends Object {
		protected String name = "Unknown";
		ArrayList<GraphPoint> locations = new ArrayList<GraphPoint>();
		ArrayList<String> strings = new ArrayList<String>();
		GraphComp0 gc;
		public Color pen = Color.black;
		protected boolean isVisible = true;

		StringSet(GraphComp0 gc, String name, Color pen) {
			this.gc = gc;
			this.name = name;
			this.pen = pen;
		}
		public synchronized void drawSelf(Graphics g) {
			drawSelf (g, "lines");
		}
		public synchronized void drawPoints(Graphics g) {
			drawSelf (g, "points");
		}
		public void refresh()
		{
			for(GraphPoint p : locations)
				p.refresh();
		}
		private void drawSelf(Graphics g, String option)
		{
			drawSelf((Graphics2D) g, option);
		}
		public synchronized void drawSelf(Graphics2D g, String option) {
			if (! isVisible()) { return; }
			GraphPoint p1;
			int x1, y1;
			g.setColor(pen);
			for (int i = 0; i < locations.size(); i++) {
				p1 = locations.get(i);
				String the_string = (String)strings.get(i);
				if ( p1.forceRepaint() && (! (YaxisLogged() && (p1.y <= 0.005  )))) // || p2[1] <= 0.005))
				{
					x1 = p1.getX();
					y1 = p1.getY();
					g.drawString(the_string, x1, y1);
					p1.resetForceRepaint();
				}
			}
		}
    }    
    public void setIsSelected(String name, boolean status){
    	setAllUnSelected();
    	((DataLine)(lineHash.get(name))).isSelected = status;
    	invalidate();
    	repaint();
    }
    
    public void setAllUnSelected(){
    	Iterator iter = lines.iterator(); 
    	while (iter.hasNext()){
    		 DataLine dline = (DataLine)(iter.next());
    		 dline.isSelected = false;
    	}
    }
    protected void refreshPixelsForAllLines(){
    	Iterator iter = lines.iterator();
    	while(iter.hasNext())
    		((DataLine)iter.next()).refreshPixels();
    	
    	for(StringSet sSet : stringSets)
    		sSet.refresh();
    }
	public DataLine whichLineIsClosest(int mouseX, int mouseY){
		DataLine closestLine = null;
		Double closestDistance2 = Double.MAX_VALUE;
		for(Iterator iter = lines.iterator(); iter.hasNext(); ){
			DataLine line = (DataLine)iter.next();
			int i = line.iForClosestX(mouseX);
			int closestX = line.closestX(i);
			int y = line.yForClosestX(i);
			double distance2 = Math.pow(y-mouseY,2) + Math.pow(closestX-mouseX,2) ;
			//System.out.println("whichLineIsClosest: \n   "
			//		+ line.getName() + "\n   i=" + i + "  x=" + closestX + "  y=" + y
			//			+ "  distance=" + distance2); 
			if(distance2 < closestDistance2){
				closestLine = line;
				closestDistance2 = distance2;
			}
		}
		return closestLine;
	}
    protected class DataLine extends Object {
		protected String name = "Unknown";
		ArrayList<GraphPoint> data = new ArrayList<GraphPoint>();
		GraphComp0 gc;
		public Color pen = Color.black;
		protected boolean isVisible = true;
		public boolean isSelected = false;
		public ArrayList<GraphPoint> getData(){
			return(data);
		}
		public GraphPoint getLastPoint()
		{
			if(data.size() <= 0)
				return(null);
			else
				return(data.get(data.size() - 1));
		}
		public String getName()
		{
			return name;
		}
		public void addElement(	GraphPoint point)
		{
			data.add(point);
		}
		public void refreshPixels()
		{
			for(GraphPoint point : data)
				point.refresh();
		}
		public int yForClosestX(int i)
		{
			int y = data.get(i).getY();
			return y;
		}
		public int closestX(int i){
			return data.get(i).getX();
		}
		public int iForClosestX(int mouseX){
			//binary search
			int smallestX = data.get(0).getX();
			int biggestX = data.get(data.size()-1).getX();
			if(mouseX <= smallestX) return(0);
			else if(mouseX >= biggestX) return(data.size()-1);
			else {
				int previousI = 0;
				int previousJ = data.size()-1;
				int nextIndex = 1;
				int counter = 0;
				int counterMax = 1000;
				while((previousI < previousJ)
						&& (counter < counterMax)){
					counter++;
					nextIndex = (previousI+previousJ)/2;
					int xValue = data.get(nextIndex).getX(); // ((int[])pixels.elementAt(nextIndex))[0];
/*					System.out.println("iForClosestX: " + counter 
							+ "  I=" + previousI
							+ "  J=" + previousJ
							+ "  xValue=" + xValue
							+ "  nextIndex=" + nextIndex
							+ "  mouseX=" + mouseX
					);
*/ 					if(xValue == mouseX) 
						return nextIndex;
					else if(nextIndex == previousI)
						return nextIndex;
					else if(nextIndex == previousJ)
						return nextIndex;
					else if(xValue < mouseX)
						previousI = nextIndex;
					else previousJ = nextIndex;
				}
				if(counter == counterMax)
					System.err.println("iForClosestX: counterMax is reached");
				return(nextIndex);
			}
		}
		DataLine (GraphComp0 gc, String name, Color pen) {
			this.gc = gc;
			this.name = name;
			this.pen = pen;
			lineHash.put(name,this);
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
		public synchronized void drawSelf(Graphics g) {
			drawSelf (g, "lines");
		}
		public synchronized void drawPoints(Graphics g) {
			drawSelf (g, "points");
		}

		private void drawSelf(Graphics g, String option)
		{
			drawSelf((Graphics2D) g, option);
		}
		private Stroke getMyStroke(){
			if(isSelected) 
				return basicStroke3;
			else return basicStroke1;
		}
		public synchronized void drawSelf(Graphics2D g, String option) {
			if (! isVisible()) { return; }

			GraphPoint p1;
			GraphPoint p2;
			int x1, y1, x2, y2;

			try {
				p1 = data.get(0);
				g.setColor(pen);
				for (int i = 1; i < data.size(); i++) {
					p2 = data.get(i);
						
					if ( (p1.forceRepaint() || p2.forceRepaint() ) && (! (YaxisLogged() && (p1.y <= 0.005  || p1.x == p2.x)))) // || p2[1] <= 0.005))
					{

						x1 = p1.getX();
						y1 = p1.getY();
						x2 = p2.getX();
						y2 = p2.getY();
						int radius = 5;
						if (option.equals("lines")) {
							((Graphics2D)g).setStroke(getMyStroke());
							g.drawLine(x1, y1, x2, y2);
							((Graphics2D)g).setStroke(defaultStroke);
						}
						else if (option.equals("points")) {
							java.awt.geom.Rectangle2D.Double rect
									= new java.awt.geom.Rectangle2D.Double();
							rect.setRect( (double) x1-radius, (double) y1-radius,
										  (double) 2*radius, (double) 2*radius);
							((Graphics2D)g).draw(rect);
						}
						p1.resetForceRepaint();
					}
					p1 = p2;
				}
			}
			catch(ArrayIndexOutOfBoundsException e) {
				System.out.println("drawSelf: data has no content for the line labelled "
					+ name );
			}
		}
    }
    protected class GraphPoint
    {
    	public double x, y;
    	private int xPixel, yPixel;
    	private boolean valid = true;
    	private boolean forceRepaint = true;
    	
    	public GraphPoint(double x, double y)
    	{
    		this.x = x;
    		this.y = y;
    		refresh();
    	}
    	public void refresh()
    	{
    		xPixel = getXPixel(x);
    		yPixel = getYPixel(y);
    		valid = true;
    		forceRepaint = true;
    	}
    	public void invalidate()
    	{
    		valid = false;
    		forceRepaint = true;
    	}
    	public int getX()
    	{
    		if(! valid)
    			refresh();
    		return(xPixel);
    	}
    	public int getY()
    	{
    		if(! valid)
    			refresh();
    		return(yPixel);
    	}
    	public boolean forceRepaint()
    	{
    		//return(forceRepaint);
    		//always repaint for now.  There is some sort of refresh problem 
    		//that is causeing the old lines to get overwritten on a repaint...
    		return(true);
    	}
    	public void resetForceRepaint()
    	{
    		forceRepaint = false;
    	}
    }
	
	int getXPixel(double value)
	{
		double xScaleFactor = (getWidth() - xLeftOff - xRightOff)/(maxXVal - minXVal);
		if(!logXScale)
			return( (int) Math.round((xLeftOff + ((value - minXVal)* xScaleFactor))));
		else
		{
			long rv = Math.round((xLeftOff + (log10(value/minXVal) * (getWidth() - xLeftOff - xRightOff))/log10(maxXVal/minXVal)));
			return( (int) Math.max(rv, Math.round(xLeftOff - 1.0)));
		}
	}
	int getYPixel(double value)
	{
		if(!logYScale)
			return( (int) Math.round((getHeight() - yBottomOff - (((value - minYVal)*(getHeight() - yBottomOff - yTopOff))/(maxYVal - minYVal)))));
		else
		{
			long rv = Math.round((getHeight() - yBottomOff - ((log10(value/minYVal))*(getHeight() - yBottomOff - yTopOff))/( log10(maxYVal/minYVal))));
			return( (int) Math.min(rv, Math.round(getHeight() - yBottomOff + 1.0) ));
		}
	}
	double unPixel(int pixel, int axis)
	{
		if(axis == XAXIS)
			return(unXPixel(pixel));
		else
			return(unYPixel(pixel));
	}
	double unXPixel(int pixel)
	{
		if(!logXScale)
		{
			return( minXVal + ((maxXVal - minXVal) * (pixel - xLeftOff))/(getWidth() - xLeftOff - xRightOff));
		}
		else
		{
			return( Math.pow(10, log10(minXVal) + ((log10(maxXVal/minXVal)) * (pixel - xLeftOff))/(getWidth() - xLeftOff - xRightOff)));
		}
	}
	double unYPixel(int pixel)
	{
		if(!logYScale)
		{
			return( minYVal +  ((maxYVal - minYVal) * ( getHeight() - yBottomOff - pixel))/(getHeight() - yBottomOff - yTopOff));
		}
		else
		{
			return( Math.pow(10.0, log10(minYVal) + ((log10(maxYVal/minYVal)) * ( getHeight() - yBottomOff - pixel))/(getHeight() - yBottomOff - yTopOff)));
		}
		
	}
	double log10 (double x) {
		if(x <= 0.0)
			return(Double.NEGATIVE_INFINITY);
		else
			return Math.log(x)/Math.log(10.0);
	}




	public void addStringSet( String name){
		stringSets.addElement(new StringSet(this, name, Color.black));
	}
	public void addLineToVector(Vector lines, String name) {

		lines.addElement(new DataLine(this, name, Color.black));
	}
    public void addLine(String name) {
		this.lines.addElement(new DataLine(this, name, Color.black));
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
    public void addLine(Vector lines, String name, Color c){
		lines.addElement(new DataLine(this, name,c));
    }
    public void addStringSet(Vector strings, String name, Color c){
    	stringSets.addElement(new StringSet(this, name, c));
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
    public void setLineVisible(Vector lines, String name, boolean isVisible) {
		Enumeration en = lines.elements();
		while (en.hasMoreElements()) {
			DataLine dl = (DataLine)en.nextElement();
			if (dl.name.equals(name)) {
				dl.isVisible = isVisible;
				repaint();
				return;
			}
		}
    }
    
	public Color addString( double x, double y, String s, String stringName) {
		return addString(stringSets, x, y, s, stringName);
	}
	public boolean addYAxisLabel(String labelName)
	{
		if ( !yAxisLabels.containsKey(labelName) ) {

			yAxisLabels.put(labelName, new Double((yAxisLabels.size()+1)*event_y_del));
			return(true);
		}
		else
			return(false);

//		addString(-(strLength/xScaleFactor), ((Double)yAxisLabels.get(labelName)).doubleValue(), drugString, labelName);
	}
	public double getYAxisLabelPosition(String labelName)
	{
		if(yAxisLabels.containsKey(labelName))
			return(((Double)yAxisLabels.get(labelName)).doubleValue());
		else
			return(0.0);
	}
	public Color addString(Vector<StringSet> stringSets, double x, double y, String s, String stringName) {
		StringSet theStringSet = findStringSet(stringSets, stringName);
		GraphPoint point = new GraphPoint( x, y );

		theStringSet.locations.add(point);
		theStringSet.strings.add(s);
		if (isVisible())
			repaint();
		return theStringSet.pen;
	}

	public void addData( double x, double y, String lineName) {
		addData(lines, x, y, lineName);
	}


	public void addData(Vector lines, double x, double y, String lineName) {
		if(Double.isNaN(maxX) || x > maxX) maxX = x;
		if(Double.isNaN(maxY) || y > maxY) maxY = y;
		if(Double.isNaN(minX) || x < minX) minX = x;
		if(Double.isNaN(minY) || y < minY) minY = y;
		GraphPoint point = new GraphPoint( x, y );
		DataLine theDataLine = findDataLine(lines, lineName);
//		GraphPoint lastPoint = theDataLine.getLastPoint();
		theDataLine.addElement(point);
		if (isVisible())
		{
//			if(lastPoint == null)
				repaint();
//			else
//				this.repaint(lastPoint.getX(), lastPoint.getY(), point.getX() - lastPoint.getX(), point.getY() - lastPoint.getY());
		}
	}
	public void clear()
	{
		lines = new Vector<DataLine>();
		if(isVisible())
			repaint();
	}
	boolean XaxisLogged() {
		return logXScale;
	}
	boolean YaxisLogged() {
		return logYScale;
	}
	DataLine findDataLine(Vector lines, String lineName) {
		Iterator iter = lines.iterator();
		while (iter.hasNext()) {
			DataLine dataLine = (DataLine)iter.next();
			if (dataLine.name.equals(lineName))
				return dataLine;
		}
		Color lineColor;
		lineColor = getColor(lines);
		DataLine newDataLine = new DataLine(this, lineName, lineColor);

		if(legend != null)
			legend.addLine(lineName, lineColor);
		lines.add(newDataLine);
		return newDataLine;
	}
	StringSet findStringSet(Vector<StringSet> strings, String stringName) {
		Iterator iter = stringSets.iterator();
		while (iter.hasNext()) {
			StringSet stringSet = (StringSet)iter.next();
			if (stringSet.name.equals(stringName))
				return stringSet;
		}
		StringSet newStringSet = new StringSet(this, stringName, getColor(stringSets));
		stringSets.add(newStringSet);
		return newStringSet;
	}

	Color getColor(Vector lines) {
		Color colorArray [] = {
			Color.red, Color.blue, Color.green.darker().darker(), Color.magenta,
			Color.orange.darker().darker(),  Color.black, Color.gray, new Color(99,00,99)
		};
		int whichLine = lines.size() + 1;
		Color myColor = colorArray [whichLine % Array.getLength(colorArray)];
		int lightness = whichLine / Array.getLength(colorArray);
		for (int i=0; i<lightness; i++)
			myColor = myColor.brighter();
		return myColor;
	}
			
	private Hashtable<String, Color> textColors = new Hashtable<String, Color>();
	Color getColor(Vector<StringSet> stringSet, String lineName)
	{
		if(textColors.containsKey(lineName))
			return(textColors.get(lineName));
		
		for(StringSet sset : stringSet)
		{
			if(sset.name.equals(lineName))
			{
				textColors.put(lineName, sset.pen);
				return(sset.pen);
			}
		}
		
		return(Color.BLACK);
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
		drawAxes(g);
		int x1 = getXPixel(minXVal);
		int y1 = getYPixel(maxYVal);
		int w = getXPixel(maxXVal) - getXPixel(minXVal);
		int h = getYPixel(minYVal) - getYPixel(maxYVal);
		Area clip = new Area(new Rectangle(x1, y1 , w, h));
		Shape savedClip = g.getClip();
		Area currentClip = new Area(savedClip);
		currentClip.intersect(clip);
		g.setClip(currentClip);
		drawLines(lines, g);
		drawText(stringSets, g);
		g.setClip(savedClip);
	}

//	 public void update(Graphics g)
//	 {
//		 paint(g);
//	 }

	Font f1;
	FontMetrics fm1;
	int f1h;
	Font f2;
	FontMetrics fm2;
	int f2h;

	public void setFontInfo(Graphics g) {
		f1 = g.getFont();
		fm1 = g.getFontMetrics();
		f1h = fm1.getAscent() - fm1.getLeading();
		f2 = new Font(f1.getName(), Font.PLAIN, (int)((double)f1.getSize()*0.7));
		g.setFont(f2);
		fm2 = g.getFontMetrics();
		f2h = fm2.getHeight();
		g.setFont(f1);
	}
	boolean pleaseDrawXAxis=true;
	public void dontDrawXAxis() {pleaseDrawXAxis=false;}
	boolean pleaseDrawYAxis=true;
	public void dontDrawYAxis() {pleaseDrawYAxis=false;}
	
	public void drawAxes(Graphics g) {
		if(pleaseDrawXAxis) drawXAxis(g);
		if(pleaseDrawYAxis) drawYAxis(g);
		this.drawYAxisLabels(g);
		g.setFont(g.getFont().deriveFont(Font.BOLD));
		int fY = (int) (getHeight() - yBottomOff + fm1.getAscent());
		drawString(g, xName,(int) xLeftOff-30-fm1.stringWidth(xName), fY+5);
		g.setFont(g.getFont().deriveFont(Font.PLAIN));
		g.setFont(g.getFont().deriveFont(Font.BOLD));
		drawString(g, yName, (int) (xLeftOff-fm1.stringWidth(yName)) - 40, fm1.getHeight());
		g.setFont(g.getFont().deriveFont(Font.PLAIN));
	}
	public void drawXAxis(Graphics g) {
		setFontInfo(g);
		int fY = (int) (getHeight() - yBottomOff + fm1.getAscent());
		//X Axis
		g.drawLine(getXPixel(minXVal), getYPixel(minYVal), getXPixel(maxXVal) + 5, getYPixel(minYVal));
		//X Axis labels
		if (logXScale) {
			for (double d = 0.0; d <= log10(maxXVal); d += 1.0) {
				String s = new Integer((int)d).toString();
				int x = getXPixel(d), y = getYPixel(minYVal) - 5;
				g.drawLine(x,y,x,y+10);
				x -= fm1.stringWidth(s)+3;
				g.drawString(s,x,y+f1h/2-f2h/2);
				g.setFont(f1);
				int xW = fm1.stringWidth("10")/2;
				g.drawString("10",x-xW,fY);
				g.setFont(f2);
				g.drawString(s,x+xW+5,fY-f2h/2);
			}
		} else {
				double d;
				g.setFont(f1);
				for (d = xZero; d <= maxXVal; d += xStep)
					if(d >= minXVal)
						drawXTick(g, d);
				for (d = xZero - xStep; d >= minXVal; d -= xStep)
				{
					if(d <= maxXVal)
						drawXTick(g, d);
				}
				if(drawZeroAxis)
				{
					Graphics2D g2 = ((Graphics2D) g);
					g2.setStroke(dashStroke);
					g2.setColor(Color.red);
					g2.drawLine(getXPixel(xZero),getYPixel(minYVal),
								getXPixel(xZero),getYPixel(maxYVal));
					g2.setStroke(basicStroke);
					g2.setColor(Color.black);
				}
		}
	}
	
	public void drawYAxis(Graphics g) {
		setFontInfo(g);
		g.setColor(Color.black);
		
		//Y Axis
		g.drawLine(getXPixel(minXVal), getYPixel(minYVal), getXPixel(minXVal), getYPixel(maxYVal) - 5);

		//Y axes lables
		double d;
		if (logYScale)
		{
			for(d = 0.0; d <= Util.roundToDouble(log10(maxYVal), yPlaces); d+= yStep)
			{
				if(d >= (log10(minYVal) - Math.pow(10.0, -yPlaces) / 2))
				   drawLogYTick(g, d);
			}
			for(d = -yStep; d >= log10(minYVal); d-=yStep)
			{
				if( d <= (log10(maxYVal) + Math.pow(10, -yPlaces) /2))
					drawLogYTick(g, d);
			}
		}
		else
		{
			for(d = 0.0; d <= maxYVal; d+= yStep)
				if(d >= minYVal)
					drawYTick(g, d);
			for(d = -yStep; d>=minYVal; d-= yStep)
				if(d <= maxYVal)
					drawYTick(g, d);
		}
		

		
	}
	void drawYAxisLabels(Graphics g)
	{
		//additional Y axes labels 
		for(String labelName : yAxisLabels.keySet())
		{
			String drugString = labelName + " ";
			int strLength = getFontMetrics(getFont()).stringWidth(drugString);
//			double width = getWidth();
//			double xScaleFactor = (width - xLeftOff - xRightOff)/(maxXVal - minXVal);
			int x = getXPixel(minXVal) - strLength;
			int y = getYPixel(yAxisLabels.get(labelName).doubleValue());
			Color savedColor = g.getColor();
			g.setColor(getColor(stringSets, labelName));
			g.drawString(drugString, x , y);
			g.setColor(savedColor);
		}
	}
	void drawXTick(Graphics g, double value)
	{
		String s = Util.roundToString(value - xZero, xPlaces);
		int x = getXPixel(value);
		int y = getYPixel(minYVal) - 5;
		g.drawLine(x,y,x,y+10);
		g.drawString(s,x-fm1.stringWidth(s)/2, y + 10 + fm1.getAscent());
	}
	void drawYTick(Graphics g, double value)
	{
		String s = oncotcap.util.Util.roundToString(value, yPlaces);
		int x = getXPixel(minXVal) - 5;
		int y = getYPixel(value);
		g.drawLine(x,y,x+10,y);
		g.drawString(s,x-fm1.stringWidth(s)-5,(int) (y + f1h/2.0));
	}
	void drawLogYTick(Graphics g, double value)
	{
		g.setFont(f2);
		int i = (int) value;
		String s = new Integer(i).toString();
		int x = getXPixel(minXVal) - 5;
		int y = getYPixel(Math.pow(10.0,value));
		g.drawLine(x,y,x+10,y);
		x -= fm2.stringWidth(s)+3;
		g.drawString(s,x,y+f1h/2-f2h/2);
		g.setFont(f1);
		g.drawString("10",x-fm1.stringWidth("10")-1,y+f1h/2);
	}
	void drawString(Graphics g, String text, int x, int y){
		int loc = 0;
		int x0 = x; 
		String t = text;
		while ((loc = t.indexOf("\n")) >= 0 ) {
			g.drawString(t.substring(0,loc-1), x, y);
			x = x0; y = y + fm1.getHeight();
			t = t.substring(loc+1);
		}
		g.drawString(t, x, y);
	}
	public void drawLines(Vector lines, Graphics g) {
		drawLines(lines, g, "lines");
	}
	public void drawPoints(Vector lines, Graphics g) {
		drawLines(lines, g, "points");
	}
	public void drawLines(Vector lines, Graphics g, String option)
	{
		Graphics2D g2 = (Graphics2D) g;		
		Object oldAntiAlias = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
//		int x1 = getXPixel(minXVal);
//		int y1 = getYPixel(maxYVal);
//		int w = getXPixel(maxXVal) - getXPixel(minXVal);
//		int h = getYPixel(minYVal) - getYPixel(maxYVal);
//		Rectangle clip = new Rectangle(x1, y1 , w, h);
		Font f1 = g.getFont();
		for (int i = 0; i < lines.size(); i++) {
			DataLine dl = (DataLine)lines.elementAt(i);
//			g2.setClip(clip);
			dl.drawSelf(g2, option);
			g2.setFont(f1);
			g2.setColor(dl.pen);
			int y = (i+4)*f1h;
//			g2.setClip(0, 0, getWidth(), getHeight());
			//g2.drawString(dl.name,5,y);
			g2.setColor(dl.pen);
			y -= f1h/2;
		}
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntiAlias);
    }
	public void drawText(Vector stringSets, Graphics g) {
		Graphics2D g2 = (Graphics2D) g;		
		Object oldAntiAlias = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		for (int i = 0; i < stringSets.size(); i++) {
			StringSet s = (StringSet)stringSets.elementAt(i);
			//g2.setClip(clip);
			g2.setFont(f1);
			g2.setColor(s.pen);
			s.drawSelf(g2);
			int y = (i+4)*f1h;
			//g2.setClip(0, 0, getWidth(), getHeight());
			//g2.drawString(dl.name,5,y);
			g2.setColor(s.pen);
			y -= f1h/2;
		}
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAntiAlias);
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
//    public void update( Graphics g ) {
//		super.update(g);
//		if ( offScreenImage == null ) {
//			offScreenImage = createImage( getSize().width, getSize().height );
//			offScreenGC = (Graphics2D)offScreenImage.getGraphics();
//		}
//		paint( offScreenGC );
//		if (null != offScreenImage) {
//		  g.drawImage(offScreenImage, 0, 0, this);
//		}
//		paint(g);
//		repaint();
//		if(isVisible())
//			paint(g);
//    }

	LoggableAdapter loggableAdapter = new LoggableAdapter(0);
	public int verbose() { return loggableAdapter.verbose(); }
	public void setVerbose(int v) { loggableAdapter.setVerbose(v); }
	public String logName() { return loggableAdapter.logName(this); }


	class PopupListener extends MouseAdapter
	{
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popup.show(e.getComponent(),
						   e.getX(), e.getY());
				xPos = e.getX();
				yPos = e.getY();
			}
		}
	}	
}

