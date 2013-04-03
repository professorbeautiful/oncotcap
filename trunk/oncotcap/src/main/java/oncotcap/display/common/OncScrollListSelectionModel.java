package oncotcap.display.common;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.lang.reflect.Method;

// From sun sample code
class OncScrollListSelectionModel extends DefaultListSelectionModel
{
    boolean gestureStarted = false;
    
    public void setSelectionInterval(int index0, int index1) {
		  
		  if (isSelectedIndex(index0) && !gestureStarted) {
				super.removeSelectionInterval(index0, index1);
		  }
		  else {
				super.addSelectionInterval(index0, index1);
		  }
		  gestureStarted = true;
    }
	 
    public void setValueIsAdjusting(boolean isAdjusting) {
		  if (isAdjusting == false) {
				gestureStarted = false;
		  }
    }
}
