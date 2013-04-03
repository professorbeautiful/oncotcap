package oncotcap.display.common;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class OncTable extends JTable
{

	private Component defaultRenderer = null;
	
	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		jf.setSize(500,500);
		TableColumn cols[] = { new TableColumn(), new TableColumn(), new TableColumn() };
		TableModel dataModel = new AbstractTableModel() {
			public int getColumnCount() { return 10; }
			public int getRowCount() { return 10;}
			public Object getValueAt(int row, int col) { return new Integer(row*col); }
		};
		OncTable tab = new OncTable(dataModel, jf);
		for (int n=0; n<3; n++)
			cols[n] = tab.getColumnModel().getColumn(n);

		cols[0].setHeaderValue("One");
		cols[1].setHeaderValue("Two");
		cols[2].setHeaderValue("Three");
		tab.addHeaderInfo(cols[0], new HeaderInfo("First Column", "glossary.html"));
		tab.addHeaderInfo(cols[2], new HeaderInfo("Third Column", "Intro_help.html"));
		TcapScrollPane scrollPane = new TcapScrollPane(tab);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jf.getContentPane().add(scrollPane);
		jf.addWindowListener(new java.awt.event.WindowAdapter(){public void windowClosing(java.awt.event.WindowEvent e){
			System.exit(0);}});

		jf.setVisible(true);
	}
	
	private Hashtable headerInfo = new Hashtable();
	private Frame parentFrame = null;
	private ToolTip toolTip;
	private MouseWatch listen;
	private static final int TIPPOPUP = 1;
	private static final int INFO = 2;

	public OncTable(int rows, int cols)
	{
		super(rows, cols);
	}
	public OncTable(TableModel model, Frame parentFrame)
	{
		super(model);
		this.parentFrame = parentFrame;
		toolTip = new ToolTip(parentFrame);
		listen = new MouseWatch();
		tableHeader.addMouseListener(listen);
		addMouseListener(listen);
		tableHeader.addMouseMotionListener(listen);
		addMouseMotionListener(listen);
		parentFrame.addKeyListener(listen);
	}
	public  boolean isCellEditable(int row, int column)
	{
		return(false);
	}
	public Object addHeaderInfo(TableColumn column, HeaderInfo info)
	{
		return(headerInfo.put(column, info));
	}
	public void removeHeaderInfo(TableColumn column)
	{
		if(headerInfo.containsKey(column))
			headerInfo.remove(column);
	}
	private void showHelp(TableColumn column, int type)
	{
		if(headerInfo.containsKey(column))
		{
			HeaderInfo info = (HeaderInfo) headerInfo.get(column);
			if(type == TIPPOPUP)
			{
				String tip = info.getTip();
				if(tip != null && !tip.trim().equals(""))
				{
					toolTip.setTip(tip);
					toolTip.setLocation(listen.currentPoint);
					toolTip.setVisible(true);
				}
			}
			else if(type == INFO)
			{
				String fileName = info.getHelpFile();
				if(fileName != null && !fileName.trim().equals(""))
				{
					oncotcap.util.BrowserLauncher.launch("tcap://" + fileName.trim());
				}
			}
		}
	}
	public void clear()
	{
		if(getModel() instanceof DefaultTableModel)
		{
			int nRows = getRowCount();
			for(int n = nRows - 1; n >= 0; n--)
				((DefaultTableModel) getModel()).removeRow(n);
			repaint();
		}
	}

	private class MouseWatch implements MouseListener, MouseMotionListener, KeyListener
	{
		private int currentColumn;
		private Point currentPoint = new Point();
		private boolean mouseOn = false;
		private Point tPoint;
		private final javax.swing.Timer timer;
		private static final int popUpInterval = 1000;
		private TipPopup tipPop = new TipPopup();
		private boolean timerState = false;

		public MouseWatch()
		{
			timer = new javax.swing.Timer(popUpInterval, tipPop);
			timer.stop();
			timer.setRepeats(false);
		}
				
		public void mouseClicked(MouseEvent e){}
		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e)
		{
			timer.stop();
			mouseOn = false;
			toolTip.setVisible(false);
		}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mouseDragged(MouseEvent e) {}
		public void mouseMoved(MouseEvent e)
		{
			timer.stop();
			toolTip.setVisible(false);
			tPoint = e.getPoint();
			
			//this try-catch block needed because on Apple OS X a
			//NoSuchMethod exception is reported for the
			//tableHeader.columnAtPoint, but only when compiled on
			//Apple...
			try
			{
				currentColumn = tableHeader.columnAtPoint(tPoint);
				currentPoint.setLocation(tPoint.getX() + getLocationOnScreen().getX()+5,
												 tPoint.getY() + getLocationOnScreen().getY()-35);
				timer.start();
				mouseOn = true;
			}
			catch(java.lang.NoSuchMethodError ex){}
			
		}

		public void keyPressed(KeyEvent e)
		{
			if(mouseOn && e.getKeyCode() == KeyEvent.VK_F1)
			{
				toolTip.setVisible(false);
				showHelp(getColumnModel().getColumn(listen.currentColumn), INFO);
			}
		}
		public void keyReleased(KeyEvent e) {}
		public void keyTyped(KeyEvent e)  {}

	}

	
	private class TipPopup implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			showHelp(getColumnModel().getColumn(listen.currentColumn), TIPPOPUP);			
		}
	}

	public void setTitle(int column, String title)
	{
		TableColumnModel cm = getColumnModel();
		if(cm != null && column >= 0 && (column + 1) <= cm.getColumnCount())
		{
			TableColumn col = cm.getColumn(column);
			col.setHeaderValue(title);
		}
	}

	public void setDefaultRenderer(Component c)
	{
		defaultRenderer = c;
	}

	public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
	{
		if(defaultRenderer != null)
			return(defaultRenderer);
		else
			return(super.prepareRenderer(renderer, row, column));
	}
}
