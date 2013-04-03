package oncotcap.display.common;

import javax.swing.*;
import java.util.*;
import java.awt.datatransfer.*;

public class OncList extends JList
{
	private DefaultListModel listModel;
	
	public OncList()
	{
		super();
		init();
	}
	public OncList(ListModel dataModel)
	{
		super();
		init();
		addAll(dataModel);
	}
	public OncList(Object[] listData)
	{
		super();
		init();
		addAll(listData);
	}
	public OncList(Vector listData)
	{
		super();
		init();
		addAll(listData);
	}

	private void init()
	{
		listModel = new DefaultListModel();
		setModel(listModel);
		setTransferHandler(new ListTransferHandler());
	}
	public void addAll(Enumeration enoom)
	{
		while(enoom.hasMoreElements())
			listModel.addElement(enoom.nextElement());
	}
	public void addAll(Collection vec)
	{
		Iterator it = vec.iterator();
		while(it.hasNext())
			listModel.addElement(it.next());
	}
	public void addAll(Object [] objs)
	{
		int n;
		for(n = 0; n< objs.length; n++)
			listModel.addElement(objs[n]);
	}
	public void addAll(ListModel lm)
	{
		int n;
		for(n = 0; n< lm.getSize(); n++)
			listModel.addElement(lm.getElementAt(n));
	}
	public void add(Object obj)
	{
		listModel.addElement(obj);
	}
	public void clear()
	{
		listModel.clear();
	}
	public void removeElement(Object elem)
	{
		listModel.removeElement(elem);
	}
	public void setModel(DefaultListModel model)
	{
		listModel = model;
		super.setModel(model);
	}

	private class ListTransferHandler extends TransferHandler
	{
		public Transferable createTransferable(JComponent c)
		{
			return(new ListTransferable(getSelectedValue()));
		}
		public int getSourceActions(JComponent c)
		{
			return(COPY_OR_MOVE);
		}

	}
	private static DataFlavor [] flavs = {Droppable.droppableData};
	public class ListTransferable implements Transferable
	{
		Object storedObj;
		
		public ListTransferable(Object data)
		{
			storedObj = data;
		}
		public Object getTransferData(DataFlavor flavor)
		{
			if(flavor.equals(Droppable.droppableData))
				return(storedObj);
			else
				return(null);
		}
		public DataFlavor[] getTransferDataFlavors()
		{
			return(flavs);
		}
		public boolean isDataFlavorSupported(DataFlavor flavor)
		{
			if(flavor.equals(Droppable.droppableData))
				return(true);
			else
				return(false);
		}
	}
}
