package oncotcap.display.browser;
import java.util.*;

import java.awt.event.*;
import javax.swing.event.*;

public class DoubleClickEvent extends EventObject {
		MouseEvent mouseEvent = null;
		public DoubleClickEvent(Object source, MouseEvent event) {
				super(source);
				mouseEvent = event;
		}
		public MouseEvent getMouseEvent() {
				return mouseEvent;
		}
}
