package oncotcap.display.common;

import javax.swing.event.*;
import java.util.*;
import java.lang.reflect.*;

public interface OncListModelListener extends ListDataListener {
		public void intervalRemoved(ListDataEvent e, Vector removedItems);
}
