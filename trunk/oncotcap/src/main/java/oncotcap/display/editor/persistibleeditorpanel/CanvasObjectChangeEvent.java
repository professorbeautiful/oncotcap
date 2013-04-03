package oncotcap.display.editor.persistibleeditorpanel;
import java.util.*;

import java.awt.event.*;
import javax.swing.event.*;

public class CanvasObjectChangeEvent extends EventObject {
		public static final int ADD = 1;
		public static final int REMOVE = 1;
		public static final int MODIFY = 1;
		
		int changeEventType = -1;
		Object changedObject = null;
		Object source = null;

		public CanvasObjectChangeEvent(int changeEventType,
														 Object changedObject,
														 Object caller) {
				super(caller);
				this.source = caller;
				this.changeEventType = changeEventType;
				this.changedObject = changedObject;
		}
		public int getChangeEventType() {
				return changeEventType;
		}
		public Object getChangedObject() {
				return changedObject;
		}
		public Object getSource() {
				return source;
		}
}
