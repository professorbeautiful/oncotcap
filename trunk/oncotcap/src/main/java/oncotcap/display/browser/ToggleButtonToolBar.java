package oncotcap.display.browser;

import javax.swing.*;
import java.awt.*;

public class ToggleButtonToolBar extends JToolBar {
    static Insets zeroInsets = new Insets(1,1,1,1);

	 public ToggleButtonToolBar() {
		  super();
	 }

	 JToggleButton addToggleButton(Action a) {
		  JToggleButton tb = new JToggleButton(
															(String)a.getValue(Action.NAME),
															(Icon)a.getValue(Action.SMALL_ICON)
															);
		  tb.setMargin(zeroInsets);
		  tb.setText(null);
		  tb.setEnabled(a.isEnabled());
		  tb.setToolTipText((String)a.getValue(Action.SHORT_DESCRIPTION));
		  tb.setAction(a);
		  add(tb);
		  return tb;
	 }
}
