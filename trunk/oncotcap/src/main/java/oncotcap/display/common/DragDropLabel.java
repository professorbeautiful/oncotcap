package oncotcap.display.common;

import javax.swing.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import oncotcap.datalayer.persistible.Keyword;

public class DragDropLabel extends HyperLabel
{

	protected Droppable target;
	private String myid;
	public DragDropLabel()
	{
		init();
	}
	public DragDropLabel(Droppable target)
	{
		setTarget(target);
		init();
	}
	public DragDropLabel(Droppable target, Icon icon)
	{
		super(icon);
		setTarget(target);
		setText("");
		init();
	}
	public DragDropLabel(Icon icon)
	{
		super(icon);
		init();
	}
	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		jf.setSize(300,300);
		jf.getContentPane().setLayout(new BoxLayout(jf.getContentPane(), BoxLayout.Y_AXIS));
		jf.getContentPane().add(new DragDropLabel(new Keyword("ONE")));
		jf.getContentPane().add(new DragDropLabel(new Keyword("TWO")));
		jf.setVisible(true);
	}
	private void init()
	{
		setTransferHandler(new DNDTransferHandler());
		addMouseListener(new DragAdapter());
		setHyperFeature(false);
	}
	public void setTarget(Droppable target)
	{
		this.target = target;
		if(target == null)
			setText("");
		else
			setText(target.toString());
	}
	protected class DNDTransferHandler extends TransferHandler
	{
		public DNDTransferHandler()
		{
			super("droppable");
		}
		protected Transferable createTransferable(JComponent c)
		{
			return(target);
		}
		public int getSourceActions(JComponent c)
		{
			return(COPY);
		}
		public boolean importData(JComponent c, Transferable t)
		{
			if(t.isDataFlavorSupported(Droppable.droppableData))
			{
				try{setTarget((Droppable) t.getTransferData(Droppable.droppableData));}
				catch(UnsupportedFlavorException e){System.out.println("Unsupported Flavor"); return(false);}
				catch(IOException e){System.out.println("IO Exception"); return(false);}
				return(true);
			}
			else
				return(false);
		}
		public boolean canImport(JComponent c, DataFlavor[] flavors)
		{
				return(false);
		}
		public boolean isDataFlavorSupported( DataFlavor flavor)
		{
				if ( flavor.equals(Droppable.droppableData)
						 || flavor.equals(Droppable.searchText) )
						return true;
				return false;
		}
	}
	public class DragAdapter extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
			JComponent c = (JComponent) e.getSource();
			TransferHandler handler = c.getTransferHandler();
			resetColor();
			handler.exportAsDrag(c, e, TransferHandler.COPY);
		}
	}

	public Droppable getDroppable()
	{
		return(target);
	}
}
