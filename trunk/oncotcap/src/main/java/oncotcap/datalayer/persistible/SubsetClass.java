package oncotcap.datalayer.persistible;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.*;
import oncotcap.Oncotcap;
import oncotcap.display.common.AbstractDroppableWithKeywords;
import oncotcap.display.common.Droppable;
import oncotcap.util.*;
import oncotcap.datalayer.Persistible;

public class SubsetClass extends AbstractDroppableWithKeywords
{	
	private Class pClass;
	public String classAndPackage;
	private static final DataFlavor [] flavors = {DataFlavor.stringFlavor, Droppable.droppableData};
	private static Vector allDefinedClasses = new Vector();
	static{ initAllClasses(); }

	private static void initAllClasses()
	{
		Iterator it = Oncotcap.getDataSource().find(SubsetClass.class).iterator();
		while(it.hasNext())
			allDefinedClasses.add(it.next());
	}
	public static Vector getAllClasses()
	{
		return(allDefinedClasses);
	}
	public SubsetClass(oncotcap.util.GUID guid){
		super(guid);
	}
	public SubsetClass(){}
	public SubsetClass(String pathAndName)
	{
		classAndPackage = pathAndName;
	}
	public String getClassAndPackage()
	{
		return(classAndPackage);
	}
	public void setClassAndPackage(String pathAndName)
	{
		classAndPackage = pathAndName;
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
	public Class getSubsetClass()
	{
		if(pClass == null)
			if(classAndPackage != null)
				pClass = ReflectionHelper.classForName(classAndPackage);
		return(pClass);
	}
	public String toString()
	{
		if(classAndPackage == null)
			return("null");
		else
			return(StringHelper.className(classAndPackage));
	}

	public boolean instanceOf(Persistible persistibleObj)
	{
		Class subClass = getSubsetClass();
		if(subClass != null)
			return(subClass.isInstance(persistibleObj));
		else
			return(false);
	}
}