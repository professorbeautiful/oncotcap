package oncotcap.datalayer.persistible;

import java.util.*;
import java.awt.Color;
import javax.swing.table.*;
import javax.swing.event.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.util.TcapColor;

public class ParameterList implements Observer,
																			PersistibleList
{
	private AbstractTableModel tm = new ParameterTableModel();
	private Vector list = new Vector();
	private SingleParameterList singleParameters = new SingleParameterList();
	public ParameterList(){}

	public void add(Parameter param)
	{
		param.setBackground(nextColor());
		int currentRows = singleParameters.getSize();
		list.add(param);
		singleParameters.addAll(param.getSingleParameterList().getValues());
		tm.fireTableRowsInserted( currentRows ,singleParameters.getSize() - 1);
	}
	public Object clone()
	{
		ParameterList newList = new ParameterList();
		Iterator it = list.iterator();
		while(it.hasNext())
			newList.list.add(it.next());

		it = singleParameters.getIterator();
		while(it.hasNext())
			newList.singleParameters.add((SingleParameter) it.next());
		
		return(newList);
	}
	public void remove(Parameter param)
	{
		Iterator it = param.getSingleParameterList().getIterator();
		while(it.hasNext())
			singleParameters.remove((SingleParameter) it.next());
		list.remove(param);
		tm.fireTableDataChanged();
	}
	public void remove(int singleParameterIndex)
	{
		SingleParameter singleParam = getSingleParameterByIndex(singleParameterIndex);
		Parameter param = getParameter(singleParam);
		remove(param);
	}
	
	public int getSingleParameterSize()
	{
		if(singleParameters == null)
			return(0);
		else
			return(singleParameters.getSize());
	}
	public boolean parameterSetDiffers(ParameterList cList)
	{
		if(cList == null)
			return(true);
		if(singleParameters.getSize() != cList.singleParameters.getSize())
			return(true);
			
		Iterator it = singleParameters.getIterator();
		while(it.hasNext())
			if(! cList.contains((SingleParameter) it.next()))
				return(true);

		return(false);
	}
	public void fireTableDataChanged()
	{
		tm.fireTableDataChanged();
	}
	public void fireTableChanged()
	{
		tm.fireTableChanged(new TableModelEvent(tm));
	}
	public void update(Observable o,	 Object arg)
	{
		tm.fireTableDataChanged();
	}
	public Iterator getIterator()
	{
		return(list.iterator());
	}
	public TableModel getTableModel()
	{
		return(tm);
	}
	public void updateParameter(Parameter param)
	{
		if(list.contains(param))
		{
			Vector singleParamsToRemove = new Vector();
			SingleParameter sp;
			Iterator it = param.getSingleParameters();
			while(it.hasNext())
			{
				sp = (SingleParameter) it.next();
				if(!containsSingleParameter(sp))
					singleParameters.add(sp);
			}

			//remove any orphaned parameters
			//this is currently not needed AND the orphanded method
			//doesn't appear to be working correctly...
/*			it = singleParameters.getIterator();
			while(it.hasNext())
			{
				sp = (SingleParameter) it.next();
				if(orphaned(sp))
					singleParamsToRemove.add(sp);
			}

			it = singleParamsToRemove.iterator();
			while(it.hasNext())
				singleParameters.remove((SingleParameter) it.next());
*/
		}
	} 
	public boolean contains(SingleParameter sp)
	{
		Iterator it = list.iterator();
		while(it.hasNext())
			if ( ((Parameter) it.next()).getSingleParameterList().contains(sp))
				return(true);

		return(false);
	}

	//this method isn't always working . . .
/*	public boolean orphaned(SingleParameter sp)
	{
		boolean rVal = true;
		Iterator params = list.iterator();
		while(params.hasNext())
		{
			Parameter p = (Parameter) params.next();
			Iterator sps = p.getSingleParameters();
			while(sps.hasNext()) 
			{
				SingleParameter spt = (SingleParameter) sps.next();
				if(sp.equals(spt))
					rVal = false;
			}
		}
		return(rVal);
	}
*/
	public SingleParameter getSingleParameterById(String guid)
	{
		Parameter param;
		SingleParameter singleParam;
		SingleParameter rVal = null;
		Iterator it = list.iterator();
		while(it.hasNext())
		{
			param = (Parameter) it.next();
			System.out.println("Singleparam " + param);
			if(! ((singleParam = param.getSingleParameterList().getByID(guid)) == null))
				rVal = singleParam;
		}
		return(rVal);

	}
	public SingleParameter getSingleParameterByID(String singleParameterID)
	{
		Parameter param;
		SingleParameter singleParam;
		SingleParameter rVal = null;
		Iterator it = list.iterator();
		while(it.hasNext())
		{
			param = (Parameter) it.next();
			if(! ((singleParam = param.getSingleParameterList().getByID(singleParameterID)) == null))
				rVal = singleParam;
		}
		return(rVal);
	}
	public SingleParameter getSingleParameterByDefaultName(String name)
	{
		SingleParameter singleParam;
		SingleParameter rVal = null;
		Iterator it = singleParameters.getIterator();
		while(it.hasNext())
		{
			singleParam = (SingleParameter) it.next();
			if(singleParam.getDefaultName().equalsIgnoreCase(name))
				rVal = singleParam;
		}
		return(rVal);
	}
	public SingleParameter getSingleParameterByIndex(int index)
	{
		SingleParameter singleParam = (SingleParameter) singleParameters.get(index);
		return(singleParam);
	}

	public Parameter getParameterByID(String singleParameterID)
	{
		return(getParameter(getSingleParameterByID(singleParameterID)));
	}
	public Parameter getParameter(SingleParameter singleParam)
	{
		Parameter param;
		Parameter rVal = null;
		Iterator it = list.iterator();
		while(it.hasNext())
		{
			param = (Parameter) it.next();
			if(param.contains(singleParam))
				rVal = param;
		}
		return(rVal);
	}
	public boolean containsSingleParameter(SingleParameter singleP)
	{
		return(singleParameters.contains(singleP.getID()));
	}
	public int getRow(SingleParameter sp)
	{
		return(singleParameters.indexOf(sp));
	}
	class ParameterTableModel extends AbstractTableModel
	{
		public int getRowCount()
		{
			return(singleParameters.getSize());
		}
		public int getColumnCount()
		{
			return(2);
		}
		public Object getValueAt(int row, int column)
		{
			if(row < singleParameters.getSize())
			{
				SingleParameter singleParam = (SingleParameter) (singleParameters.get(row));
				return(singleParam);
//				if (column == 0) return( singleParam.getName() ); else return( singleParam.getValue() );
			}
			else
				return(null);
		}
		
	}
	public void printSingleParameters()
	{
		Iterator it = singleParameters.getIterator();
		while(it.hasNext())
			System.out.println("\t" + it.next());
	}
	public void set(Collection listItems)
	{
		list = new Vector();
		singleParameters = new SingleParameterList();
		Iterator it = listItems.iterator();
		while(it.hasNext())
		{
			Object param = it.next();
			if(param instanceof Parameter)
			{
				add((Parameter) param);
			}
		}
	}

	public Vector getSingleParameters()
	{
		return(new Vector(singleParameters.getValues()));
	}
	public Vector getParameters()
	{
		return(list);
	}

	private final static Color [] colors = {Color.white, TcapColor.lightGreen};
	private static int colorIdx = 0;
	private static Color nextColor()
	{
		colorIdx++;

		if(colorIdx >= colors.length)
			colorIdx = 0;

		return(colors[colorIdx]);
	}
}
