package oncotcap.display.browser;
import java.util.*;

import java.awt.event.*;
import javax.swing.event.*;

public class OntologyPanelEvent extends EventObject {
		ActionEvent actionEvent = null;
		public OntologyPanelEvent(Object source, ActionEvent event) {
				super(source);
				actionEvent = event;
		}
		public ActionEvent getActionEvent() {
				return actionEvent;
		}
}
