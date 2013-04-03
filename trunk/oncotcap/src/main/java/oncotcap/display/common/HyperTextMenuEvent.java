package oncotcap.display.common;
import java.util.EventObject;

public class HyperTextMenuEvent extends EventObject{
				private Object selectedItem = null;
				public HyperTextMenuEvent(Object s, Object i) {
						super(s);
						selectedItem = i;
				}

				public Object getSelectedItem() {
						return selectedItem;
				}
		}
