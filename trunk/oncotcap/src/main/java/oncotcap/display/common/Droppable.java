package oncotcap.display.common;

import java.awt.datatransfer.*;
import oncotcap.display.browser.GenericTreeNode;
import oncotcap.datalayer.persistible.OntologyObjectName;
import oncotcap.datalayer.SearchText;

public interface Droppable extends Transferable
{
	public static final DataFlavor droppableData = new DataFlavor(Droppable.class, "Persistible Object");
	public static final DataFlavor genericTreeNode = new DataFlavor(Droppable.class, "Generic Tree Node");
	public static final DataFlavor oncTreeNode = new DataFlavor(Droppable.class, "OncTreeNode");
	public static final DataFlavor ontologyObjectName = 
			new DataFlavor(OntologyObjectName.class, "Ontology object name ex. Keyword");
	public static final DataFlavor searchText = 
			new DataFlavor(SearchText.class, "Search text string");
	public static final DataFlavor transferableList = new DataFlavor(TransferableList.class, "List of Transferables");
		public boolean dropOn(Object dropOnObject);
}
