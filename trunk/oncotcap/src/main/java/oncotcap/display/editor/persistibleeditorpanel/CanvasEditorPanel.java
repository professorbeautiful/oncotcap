package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.lang.reflect.*;
import java.awt.event.*;
import oncotcap.display.browser.OncBrowser;
import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.SaveListener;
import oncotcap.datalayer.SaveEvent;
import oncotcap.display.editor.*;

public abstract class CanvasEditorPanel extends EditorPanel {
		private Vector listeners = new Vector();
		public CanvasEditorPanel() {
				super();
		}

		// This methods allows classes to register for CanvasEditorChangeEvents
		public void addCanvasEditorChangeListener
				(CanvasEditorChangeListener listener) {
				if ( !listeners.contains(listener) )
						listeners.add(listener);
		}
    
		// This methods allows classes to unregister for CanvasEditorChangeEvents
		public void removeCanvasEditorChangeListener
				(CanvasEditorChangeListener listener) {
				listeners.remove(listener);
		}
    
		// This private class is used to fire CanvasEditorChangeEvents
		public void fireCanvasObjectChanged(CanvasObjectChangeEvent evt) {
				Object [] listenerArray = listeners.toArray();
				for (int i = 0; i < listenerArray.length; i++) {
						((CanvasEditorChangeListener)listenerArray[i]).canvasObjectChanged(evt);
				}
		}

}
