package oncotcap.datalayer.persistible;

import java.awt.datatransfer.*;
import oncotcap.display.common.*;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.parameter.SingleParameter;
import oncotcap.engine.*;

import javax.swing.ImageIcon;

public class EnumLevel extends AbstractDroppable
{
	private ImageIcon icon =	
			oncotcap.util.OncoTcapIcons.getImageIcon("enumlevel.jpg");

	private String name;
	private int listIndex = 0;
  private EnumLevelList levelList = null;

	public EnumLevel(oncotcap.util.GUID guid){
		super(guid);
	}
	public EnumLevel()
	{
		this(true);
	}
	public EnumLevel(boolean saveToDataSource)
	{
		if(!saveToDataSource)
			setPersistibleState(Persistible.DO_NOT_SAVE);
	}
	public EnumLevel(String levelName)
	{
		this(levelName, 0);
	}
	public EnumLevel(String levelName, int index)
	{
		this(levelName, index, true);
	}
	public EnumLevel(String levelName, int index, boolean saveToDataSource)
	{
		if(!saveToDataSource)
			setPersistibleState(Persistible.DO_NOT_SAVE);

		name = levelName.trim().toUpperCase();
		listIndex = index;
	}
	public EnumLevel cloneSubstitute(ValueMapPath path)
	{
		return(new EnumLevel(path.substituteJavaName(name).toUpperCase(), listIndex, false));
	}
	public String getName()
	{
		return(name);
	}
	public void setName(String name)
	{
		this.name = name;
		update();
	}
	public ImageIcon getIcon() {
			return icon;
	}
	public String toString()
	{
		return(name);
	}
	public String toDisplayString() {
		return toString();
	}
	public int getListIndex()
	{
		return(listIndex);
	}
	public Integer getListIndexAsInteger()
	{
		return(new Integer(listIndex));
	}
	public EnumLevelList getLevelList()
	{
		return(levelList);
	}
	public void setListIndex(Integer index)
	{
		if(index == null)
			listIndex = 0;
		else
			listIndex = index.intValue();
		update();
	}
	public void setListIndex(int index)
	{
		listIndex = index;
		update();
	}
	public void setLevelList(EnumLevelList levels)
	{
		levelList = levels;
		update();
	}
	
	public boolean equals(Object obj)
	{
		if(obj instanceof EnumLevel)
			return(((EnumLevel) obj).getName().equalsIgnoreCase(name));
		else
			return(false);
	}
	public Object clone()
	{
		return(new EnumLevel(new String(name)));
	}
	public int hashCode()
	{
		return(name.hashCode());
	}
	
	/**
	 **	flavors	used by getTransferDataFlavors.
	 **	@version July 1, 2003 by shirey
	 **/
	protected static DataFlavor [] flavors = {Droppable.droppableData, DataFlavor.stringFlavor};

	/**
	 ** isDataFlavorSupported	Required for the Droppable interface.
	 **								Used to query this object to find out if
	 **								a particular DataFlavor is supported.
	 **								Only Droppable.droppableData and
	 **								DataFlavor.stringFlavor are supported in
	 **								this implementation, if however
	 **								getTransferDataFlavors is overridden,
	 **								this method will correctly compare
	 **								against the overridden list of supported
	 **								flavors.
	 **
	 ** @param	DataFlavor	The DataFlavor to compare to the supported
	 **							flavors.
	 **
	 ** @returns	true if the flavor is supported, false if it isn't.
	 ** @author	shirey July 1, 2003
    **/
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		DataFlavor [] flavors = getTransferDataFlavors();
		for(int n = 0; n < flavors.length; n++)
			if(flavors[n] == flavor)
				return(true);

		return(false);
	}

	/**
	 ** getTransferData	Required for the Droppable interface. Used to
	 **						retrieve this object when it is being used as a
	 **						Transferable (Droppable) object for DnD or
	 **						Clipboard operations.
	 **
	 ** @param	DataFlavor the flavor of data that is required.  Only
	 **			DataFlavor.stringFlavor and Droppable.persistible are
	 **			supported.
	 **			
	 ** @returns	This object if a persistible DataFlavor is asked for or
	 **				the toString() result if a stringFlavor is asked for.
	 **
	 ** @author	shirey July 1, 2003
	 **/
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
	{
		if(isDataFlavorSupported(flavor))
		{
			if(flavor == DataFlavor.stringFlavor)
				return(toString());
			else
				return(this);
		}
		else
			throw(new UnsupportedFlavorException(flavor));
	}
	/**
	 ** getTransferDataFlavors	Required for the Droppable interface.
	 **								Used to get a list of all DataFlavors
	 **								supported by this object.  Only
	 **								Droppable.droppableData and
	 **								DataFlavor.stringFlavor are supported.
	 **
	 ** @returns	The list of supported DataFlavors.
	 **
	 ** @author	shirey July 1, 2003
	 **/
	public DataFlavor[] getTransferDataFlavors()
	{
		return(flavors);
	}


}
