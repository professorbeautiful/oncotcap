package oncotcap.display;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;

import com.sun.tools.javac.util.Log;

import oncotcap.display.genericoutput.OutputFrame;
import oncotcap.util.OncEnum;
import oncotcap.util.ScreenHelper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.*;
import java.awt.*;
import java.util.Vector;

public class LegendPanel extends JPanel
{
	
	DefaultTableModel model = new DefaultTableModel();
	JScrollPane scrollPane;
	public JTable table = new JTable(model);
	String [] enumNames;
	private GraphComp0 graph;
	private String title;
	private LegendDialog owner;
	private Vector<LegendPanelListener> listeners = new Vector<LegendPanelListener>();
	
	public LegendPanel(LegendDialog dialog, GraphComp0 graph)
	{
		this(graph, dialog, "Legend for cell types");
	}
	public LegendPanel(GraphComp0 graph, LegendDialog dialog, String title)
	{
//		super(owner, false);
		owner = dialog;
		this.graph = graph;
		this.title = title;
		init();
	}
	public static final int SMALL = 1;
	public static final int LARGE = 2;
	public static final int INVISIBLE = 0;
	
	int state = SMALL;
	void reverseWindowState(){
		state = 3 - state;
		setWindowState(state);
	}
	void setWindowState(int state){
		this.state = state;
		if(state==SMALL) {
			setVisible(true);
			this.setPreferredSize(new Dimension(190, 170));
			table.setPreferredScrollableViewportSize(new java.awt.Dimension(190, 70));
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			fireResized();
		}
		else if(state==LARGE) {
			setVisible(true);
			java.awt.Dimension screenDim = ScreenHelper.getScreenDim();
			int newWidth = Math.round(Math.round(screenDim.getWidth()));
			setPreferredSize(new Dimension(newWidth, 170));		
			scrollPane.setSize(new java.awt.Dimension(newWidth-1, 170-1));
			scrollPane.setMinimumSize(new java.awt.Dimension(newWidth-1, 170-1));
			table.setPreferredScrollableViewportSize(new java.awt.Dimension(newWidth, 70));
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			table.setMinimumSize(new Dimension(newWidth-2, 70));
			fireResized();
		}
		else if(state==INVISIBLE)
		{
			setVisible(false);
			setPreferredSize(new Dimension(0,0));
			fireResized();
		}
		validate();
		repaint();
	}
	
	WindowListener dialogListener = new WindowListener(){
		public void windowOpened(WindowEvent e) {			}
		public void windowClosing(WindowEvent e) {			}
		public void windowClosed(WindowEvent e) {			}
		public void windowIconified(WindowEvent e) {
			setWindowState(SMALL);
		}
		public void windowDeiconified(WindowEvent e) {
			setWindowState(LARGE);
		}
		public void windowActivated(WindowEvent e) {
		}
		public void windowDeactivated(WindowEvent e) {
		}
	};
	public void addedToDialog(LegendDialog dialog)
	{
		dialog.addWindowListener(dialogListener);
		dialog.setTitle(title);
	}
	public void removedFromDialog(LegendDialog dialog)
	{
		if(dialog != null)
		{
			dialog.removeWindowListener(dialogListener);
			dialog.setTitle("");
		}
	}
	private void init()
	{
		this.setLayout(new BorderLayout());
		KeyListener kl = new KeyListener(){
			public void keyTyped(KeyEvent e) {
				reverseWindowState();
			}
			public void keyPressed(KeyEvent e) {
//				reverseWindowState();
			}
			public void keyReleased(KeyEvent e) {
//				reverseWindowState();
			}
		};
		table.addKeyListener(kl);
		addKeyListener(kl);
//		table.setEnabled(false);
		addMouseListener(new MouseAdapter(){
			public void mouseEntered(MouseEvent e){
				//TODO:  possibly expand Legend to the width required to see all columns well.
				//TODO:  possibly add a "pin" feature.
			}
		});
		table.setDefaultRenderer(Object.class, new MyTableCellRenderer());
		table.addMouseMotionListener(new MouseMotionListener(){
			public void mouseDragged(MouseEvent e) {			}
			public void mouseMoved(MouseEvent e) {
				//System.out.println("Class: " + e.getComponent().getClass());   ==>>JTable
				if(e.getComponent() instanceof JTable){
					JTable t = (JTable) e.getSource();
					/*int rownum = (e.getY()-t.getY())/t.getRowHeight();
					int i1 = scrollPane.getY();
					int i2 = scrollPane.getViewport().getY();
					int i3= scrollPane.getLocationOnScreen().y;
					int idiff = rownum - i4 ; // i4 is correct.
					int toprow = t.rowAtPoint(new Point(0,0));
					i1=t.getCellRect(0,0,true).y;
					NONE OF THIS STUFF HELPS!
					*/
					int rownum = t.rowAtPoint(new Point(e.getPoint()));
					// The previous line solves the "wrong row selected when table scroll is not at top" bug.(april 2006 - rd)
					t.setRowSelectionInterval(rownum,rownum);
				}
			}
		});
/*		if(owner != null)
		{
			int x = ((int) owner.getLocation().getX()) + 40;
			int y = ((int) owner.getLocation().getY()) + 40;
			setLocation(x, y);
		}
*/		
//		setIconImage(oncotcap.util.OncoTcapIcons.getDefault().getImage());
		//labelBox = Box.createVerticalBox();
//		getContentPane().setLayout(new BoxLayout(getContentPane(),
//		BoxLayout.Y_AXIS));   
		scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
		setWindowState(SMALL);
		table.setSelectionModel(new javax.swing.DefaultListSelectionModel(){
			
		});
        adjustLegendSize();
	    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    //(ALLOW_ROW_SELECTION) { // true by default
        ListSelectionModel rowSM = table.getSelectionModel();
        rowSM.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                //Ignore extra messages.
                if (e.getValueIsAdjusting()) {
                	// make sure the selected row is visible
                	
                	return;
                }

                ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                if (lsm.isSelectionEmpty()) {
                    //System.out.println("No rows are selected.");
                	graph.setAllUnSelected();
//			((oncotcap.display.modelcontroller.cellkineticsdemo.SinglePatient)owner).getCellGraphPanel().setAllUnSelected();
                    //send message to graph, no thickened lines.
                } else {
                    int selectedRow = lsm.getMinSelectionIndex();
                    /*
                     *System.out.println("Row " + selectedRow
                                       + " is now selected.");
                    System.out.println(stringsReversed.get(new Integer(selectedRow)));
                    */
		    String lineName = (String)stringsReversed.get(new Integer(selectedRow));
	    	graph.setIsSelected(lineName, true);
//            		((oncotcap.display.modelcontroller.cellkineticsdemo.SinglePatient)owner).getCellGraphPanel().setIsSelected(lineName, true);
                    //  send message to graph, thicken this line.
            		adjustLegendSize();
                }
            }
        });
    }

	void clearSelection(){
		table.getSelectionModel().clearSelection();
		graph.setAllUnSelected();
		//((oncotcap.display.modelcontroller.cellkineticsdemo.SinglePatient)owner).getCellGraphPanel().setAllUnSelected();
	}

	int tableRows;
	int tableCols;
	

	public void setColumnIdentifiers(Object [] enums) {
		try {
			tableCols = enums.length;
			enumNames = (String [])java.lang.reflect.Array.newInstance(
				String.class, tableCols);
			int nTotalChars = 0;
			for (int i = 0; i<enumNames.length; i++) {
				if(enums[i] instanceof OncEnum)
				{
					enumNames[i] = enums[i].getClass().getName();
					enumNames[i] = enumNames[i].substring(enumNames[i]
						.indexOf("$")+1);
					nTotalChars += enumNames[i].length();
				}
				else if(enums[i] != null)
				{
					enumNames[i] = enums[i].toString();
					nTotalChars += enumNames[i].length();
				}
			}
			model.setColumnIdentifiers(enumNames);
			if(nTotalChars == 0)
				setWindowState(INVISIBLE);
			else if (nTotalChars > 15 || enumNames.length > 4)
				setWindowState(LARGE);
			else
				setWindowState(SMALL);
		}
		catch (NegativeArraySizeException e)
		{System.out.println("NegativeArraySizeException in setColumnIndentifiers");
		}
		table.getTableHeader().setFont(
					       table.getTableHeader().getFont()
					       .deriveFont(java.awt.Font.BOLD));
		adjustLegendSize();
	}
	public int getWindowState()
	{
		return(state);
	}
	public void addLine(Object [] row, Color c)
	{
		if(! colors.containsValue(c)){
			//  TODO: This would be dangerous if colors repeat.
			model.addRow(row);
			adjustLegendSize();
		}
	}
	
	public java.util.Hashtable colors = new java.util.Hashtable();
	public java.util.Hashtable strings = new java.util.Hashtable();
	public java.util.Hashtable stringsReversed = new java.util.Hashtable();

	public void addLine(String name, Color c)
	{
		if (strings.containsKey(name))
			return;
		colors.put(new Integer(model.getRowCount()), c);
		strings.put(name, new Integer(model.getRowCount()));
		stringsReversed.put(new Integer(model.getRowCount()), name);
		int rc = model.getRowCount();
		rc = model.getColumnCount();
		model.setRowCount(model.getRowCount()+1);
		rc = model.getRowCount();
		int col = 0;
		String word;
		//String objName = name.substring(0,name.indexOf(':')-1);
		//name = name.substring(name.indexOf(':')+1);
		while(name.length()>0) {
			int slash = name.indexOf(']');
			if(slash > -1) {
				word = name.substring(1,slash);
				name = name.substring(slash+2);
			}
			else {
				word = name.substring(1);
				name = "";
			}
			//Object [] value = {word, c};
			rc = model.getRowCount();
			model.setValueAt(word, model.getRowCount()-1, col++);
		}
		adjustLegendSize();
	}
	class MyTableCellRenderer extends JLabel implements TableCellRenderer {
		public java.awt.Component getTableCellRendererComponent(
			JTable table,
			Object value,
			boolean isSelected,
			boolean hasFocus,
			int row,
			int column) 
		{
			final javax.swing.border.LineBorder selectedBorder = new javax.swing.border.LineBorder(Color.red, 2);
			final javax.swing.border.LineBorder unselectedBorder = new javax.swing.border.LineBorder(Color.black, 0);
			if(value != null)
				setText(value.toString());
			setForeground((Color)colors.get(new Integer(row)));
			setOpaque(true);
			if(isSelected){
				setBorder(selectedBorder);
				Rectangle rect = 
						new Rectangle( row*table.getRowHeight(), 0, table.getRowHeight(), 0);
				//table.scrollRectToVisible(rect);  DOESNT WORK YET.
				//table.validate();
				//center(table,rect,true);  Doesnt work as advertised.
			}
			else
				setBorder(unselectedBorder);
			return this;
		}
	}
	public static void center(JComponent c, Rectangle r, boolean withInsets)
	{
		// From http://www.chka.de/swing/components/scrolling.html
	    Rectangle visible = c.getVisibleRect();

	    visible.x = r.x - (visible.width - r.width) / 2;
	    visible.y = r.y - (visible.height - r.height) / 2;

	    Rectangle bounds = c.getBounds();
	    Insets i = withInsets ? new Insets(0, 0, 0, 0) : c.getInsets();
	    bounds.x = i.left;
	    bounds.y = i.top;
	    bounds.width -= i.left + i.right;
	    bounds.height -= i.top + i.bottom;

	    if (visible.x < bounds.x)
	        visible.x = bounds.x;

	    if (visible.x + visible.width > bounds.x + bounds.width)
	        visible.x = bounds.x + bounds.width - visible.width;

	    if (visible.y < bounds.y)
	        visible.y = bounds.y;

	    if (visible.y + visible.height > bounds.y + bounds.height)
	        visible.y = bounds.y + bounds.height - visible.height;

	    c.scrollRectToVisible(visible);
	}

	public void addLegendPanelListener(LegendPanelListener listener)
	{
		if(! listeners.contains(listener))
			listeners.add(listener);
	}
	public void removeLegendPanelListener(LegendPanelListener listener)
	{
		if(listeners.contains(listener))
			listeners.remove(listener);
	}
	private void fireResized()
	{
		for(LegendPanelListener listener : listeners)
			listener.legendPanelResized(this);
	}
	public void adjustLegendSize(){
/*		int newHeight = (table.getRowCount()+4)*table.getRowHeight();
		newHeight = Math.min(325, newHeight);
		int maxEnumNameLength = 0;
		if(enumNames != null) {
			for(int iCol = 0; iCol<enumNames.length; iCol++) {
				if(maxEnumNameLength < enumNames[iCol].length())
					maxEnumNameLength = enumNames[iCol].length();
			}
		}  
		float charToPixels = 10;  // a wild guess!  Use fontmetrics instead.
		int newWidth = Math.round(table.getColumnCount()* maxEnumNameLength * charToPixels);
//		table.setSize(newWidth,newHeight);
		owner.setLocation(0,0);
		//setLocation(0,owner.getHeight());
		setLocation(owner.getWidth()/2, 0); //Right half of top of screen.
		java.awt.Dimension screenDim = ScreenHelper.getScreenDim();
		newWidth = Math.min(newWidth, 
				Math.round(Math.round(screenDim.getWidth())));
		//newHeight = Math.min(newHeight, 
		//		Math.round(Math.round(screenDim.getHeight()-owner.getHeight())));
		java.awt.Dimension newDim = new java.awt.Dimension(newWidth,newHeight);
		setSize(newWidth,newHeight);
		//System.out.println(table.getSize());
		//System.out.println("Validating");
		owner.validate();
		validate();
		repaint();
		try {
			((oncotcap.display.modelcontroller.cellkineticsdemo.SinglePatient)owner)
				.getCellGraphPanel().repaint();
		}
		catch (NullPointerException e) {
			//System.out.println("NullPointerException on the repaint.");
		}
		*/
	}
	
//    public class ColumnChooser extends JFrame {
//    }
//    public void componentResized(ComponentEvent e){
//   	if(e.getSource() == owner){
//   		//adjustLegendSize();
 //   	}
  //  	oncotcap.util.Logger.log("Legend: w, h = " + getWidth() + ", " + getHeight());
 //   }
//    public void componentMoved(ComponentEvent e){
 //   	oncotcap.util.Logger.log("Legend:::: x, y = " + getLocation().x + ", " + getLocation().y);
//    }
//    public void componentShown(ComponentEvent e){}
//    public void componentHidden(ComponentEvent e){}
}