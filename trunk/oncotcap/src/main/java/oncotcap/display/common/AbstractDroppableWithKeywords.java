package oncotcap.display.common;

import java.awt.datatransfer.*;

import oncotcap.datalayer.PersistibleWithKeywords;
import oncotcap.datalayer.Persistible;
public abstract class AbstractDroppableWithKeywords extends PersistibleWithKeywords implements Droppable
{

	/**
	 **	flavors	used by getTransferDataFlavors.
	 **	@version July 1, 2003 by shirey
	 **/
	 protected static DataFlavor [] flavors = {Droppable.droppableData, DataFlavor.stringFlavor};

		public AbstractDroppableWithKeywords() {  }
		public AbstractDroppableWithKeywords(oncotcap.util.GUID guid) {
				super(guid);
		}

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

		public boolean dropOn(Object dropOnObject) {
				if ( dropOnObject instanceof Persistible &&
						 this instanceof Persistible ) {
						boolean link1 = link((Persistible)dropOnObject);
						boolean link2 = ((Persistible)dropOnObject).link(this);
						if ( link1 || link2){
								update();
								((Persistible)dropOnObject).update();
						return true;
						}
				}
				return false;
		}
}
