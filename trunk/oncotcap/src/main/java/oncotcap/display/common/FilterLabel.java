package oncotcap.display.common;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.datatransfer.*;
import java.awt.Dimension;
import java.io.IOException;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.Persistible;
import oncotcap.display.editor.*;

public class FilterLabel extends DragDropLabel
{
	private static JFrame frm;
	
	public FilterLabel()
	{
		init();
		setTarget(new OncFilter());
	}
	public FilterLabel(OncFilter filter)
	{
		super(filter);
		init();
	}
	public FilterLabel(Icon icon)
	{
		super(icon);
		init();
		setTarget(new OncFilter());
	}
	public static void main(String [] args)
	{
		frm = new JFrame();
		frm.setSize(300,300);
		frm.getContentPane().setLayout(new BoxLayout(frm.getContentPane(), BoxLayout.Y_AXIS));
		frm.getContentPane().add(new FilterLabel(new OncFilter()));
		frm.getContentPane().add(new FilterLabel(new OncFilter()));
		frm.getContentPane().add(new DragDropLabel(new Keyword("TWO")));
		frm.setVisible(true);
	}
	private void init()
	{
		setText("FILTER");
		setHyperFeature(true);
		setTransferHandler(new FilterTransferHandler());
	}
	public void setFilter(OncFilter filter)
	{
		setTarget(filter);
	}
	public OncFilter getFilter()
	{
		return((OncFilter) target);
	}
	public void setTarget(Droppable target)
	{
		this.target = target;
		if(currentHyperListener != null)
		{
			removeHyperListener(currentHyperListener);
			currentHyperListener = null;
		}
		if(target instanceof OncFilter)
		{
			currentHyperListener = new ShowFilter((OncFilter) target);
			addHyperListener(currentHyperListener);
		}
	}
   protected class FilterTransferHandler extends DNDTransferHandler
	{
		public boolean importData(JComponent c, Transferable t)
		{
			if(t.isDataFlavorSupported(Droppable.droppableData))
			{
				try
				{
					Object obj = t.getTransferData(Droppable.droppableData);
					if(obj instanceof Persistible)
					{
						if(target == null)
							target = new OncFilter();

						((OncFilter) target).addStagedObject((Persistible) obj);

//						(new ShowFilter(((OncFilter) target))).run();
						Thread thr = new Thread(new ShowFilter(((OncFilter) target)));
						thr.start();
					}
				}
				catch(UnsupportedFlavorException e){System.out.println("Unsupported Flavor"); return(false);}
				catch(IOException e){System.out.println("IO Exception"); return(false);}
				return(true);
			}
			else
				return(false);
		}

	}
	private class ShowFilter extends HyperListener implements Runnable
	{
		OncFilter filter;

		public ShowFilter(OncFilter filter)
		{
			this.filter = filter;
		}
		public void run()
		{
			EditorFrame.showEditor(filter, new Dimension(500, 400));
		}
		public void mouseActivated(MouseEvent e)
		{
			run();
		}
	}	
}
