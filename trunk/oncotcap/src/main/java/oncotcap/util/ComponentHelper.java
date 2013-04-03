package oncotcap.util;

import java.awt.*;
import javax.swing.*;

public class ComponentHelper {

		// Get the top level frame for this component
		public static Component getTopParent(Component comp) {
				while ( comp.getParent() != null ) {
						comp = comp.getParent();
				}
				return comp;
		}
		public static Component getParentOfType(Component comp,  Class ofType) {
				Component current = comp; 
				while ( current.getParent() != null ) {
						if ( ofType.isInstance(current.getParent()) )
								return current.getParent();
						current = current.getParent();
				}
				return null;
		}
		// With focus
		public static Component getFirstChildComponent(Container container,
																									 Class ofType) {
				java.awt.Component [] comps = container.getComponents();
				Component comp = null;
				for(int n = 0; n<comps.length; n++) {
						System.out.println("WHat component am I " + n + ":" + comps[n] + " IN " + container );
						if ( ofType.isInstance(comps[n]) )
								return comps[n];
						if(comps[n] instanceof JComponent || comps[n] instanceof Container)
								comp = getFirstChildComponent ((Container)comps[n], ofType);
						if (comp != null) {
							// Does this component have the focus?
							boolean hasFocus = comp.hasFocus();
							if ( hasFocus )
								return comp;
						}
				}
				return null;
		}
}



