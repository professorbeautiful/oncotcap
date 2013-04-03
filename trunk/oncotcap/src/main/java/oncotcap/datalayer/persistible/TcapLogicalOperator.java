package oncotcap.datalayer.persistible;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.*;
import oncotcap.Oncotcap;
import oncotcap.display.common.AbstractDroppableWithKeywords;
import oncotcap.display.common.Droppable;

public class TcapLogicalOperator extends AbstractDroppableWithKeywords
{
	public static TcapLogicalOperator AND;
	public static TcapLogicalOperator OR;
	public static TcapLogicalOperator NOT;
	
	private static final DataFlavor [] flavors = {Droppable.droppableData, DataFlavor.stringFlavor};
	private static Vector allSystemDefinedOperators = new Vector();
	public Integer maxArguments = new Integer(-1);
	private String javaCodeString = null;

	static{ initAllOperators(); }

	private static void initAllOperators()
	{
		Class operClass = oncotcap.util.ReflectionHelper.classForName("oncotcap.datalayer.persistible.TcapLogicalOperator");
		Collection ops = Oncotcap.getDataSource().find(operClass);
		Iterator it = ops.iterator();
		while(it.hasNext())
		{
			TcapLogicalOperator oper = (TcapLogicalOperator) it.next();
			allSystemDefinedOperators.add(oper);
			if(oper.getName().equalsIgnoreCase("AND"))
				AND = oper;
			else if(oper.getName().equalsIgnoreCase("OR"))
				OR = oper;
			else if(oper.getName().equalsIgnoreCase("NOT"))
				NOT = oper;
		}
	}
	public static Vector getDefinedOperators()
	{
		return(allSystemDefinedOperators);
	}
	public static void main(String [] args)
	{
		Vector all = getDefinedOperators();
		Iterator it = all.iterator();
		while(it.hasNext())
			System.out.println(it.next());
	}
	public String name;

	public TcapLogicalOperator(oncotcap.util.GUID guid){
		super(guid);
	}
	public TcapLogicalOperator(){}
	public TcapLogicalOperator(String name)
	{
		this.name = name;
	}


	public int getMaxArguments()
	{
		return(maxArguments.intValue());
	}
	public String getName()
	{
		return(name);
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
	{
		if(isDataFlavorSupported(flavor))
		{
			if(flavor == DataFlavor.stringFlavor)
				return(toString());
			else if(flavor == Droppable.droppableData)
				return(this);
		}
		throw(new UnsupportedFlavorException(flavor));
	}
	public DataFlavor[] getTransferDataFlavors()
	{
		return(flavors);
	}
	public String toString()
	{
		if(name == null)
			return("null");
		else
			return(name);
	}
		public String getJavaCodeString() {
				return javaCodeString;
		}
		public void setJavaCodeString(String str) {
				javaCodeString = str;
		}
}
