package oncotcap.display.editor.persistibleeditorpanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.*;
import java.lang.reflect.*;

import oncotcap.display.browser.OncBrowser;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.*;
import oncotcap.util.*;

abstract public class DefaultEditorPanel extends EditorPanel implements SaveListener {

		public Hashtable uiHashtable = null;
		public Object editObj  = null;
    public ComponentListener listener = null;
		boolean screenInitialized = false;
		GridBagConstraints c = new GridBagConstraints();
		GridBagLayout g = null;

		public DefaultEditorPanel() {
				setLayout(new ResizingLayout());
				// Register the listener with the component
				listener = new MyComponentListener();
				addComponentListener(listener);
				setPreferredSize(new Dimension(0, 0));
				setupInputMaps();
		}

		public void edit(Object objectToEdit){
				//System.out.println("Object to edit " + objectToEdit);
				editObj = objectToEdit;
				fillFields(objectToEdit);
		}

		abstract public void initUI(Object editObj);

		public void fillFields(Object objectToEdit) {
				// If the object is a auto gen persistible - go to work
				AutoGenEditable persistible = null;
				if ( objectToEdit instanceof AutoGenEditable ) {
						persistible = (AutoGenEditable)objectToEdit;
				}
				
				// Loop through each UI object and place the corresponding data 
				// in its appropriate place
				if (uiHashtable == null)
						return;

				for (Enumeration e = uiHashtable.keys();
						 e.hasMoreElements(); ) {
						Object obj = e.nextElement();
						Object valueObj  = uiHashtable.get(obj);

// 									System.out.println("OBJECT " + obj
// 																					 + " type " + obj.getClass()
// 																					 + " value " + valueObj);
						
						if (obj instanceof OncScrollList ) {
								// Set the current editable object to the Link To for the 
								// list so when items are added to
								// the list they get linked probably
								Vector linkTos = new Vector();
								linkTos.addElement(editObj);
								((OncScrollList)obj).setLinkTos(linkTos);
						}
						else if (obj instanceof OncScrollTable) {
								// Set the current editable object to the Link To for the 
								// list so when items are added to
								// the list they get linked probably
								Vector linkTos = new Vector();
								linkTos.addElement(editObj);
								((OncScrollTable)obj).setLinkTos(linkTos);
						}
						if ( obj instanceof JTextField) {
								((JTextField)obj).requestFocus();
						}
						OncUiObject field = (OncUiObject)obj;
						try {
								Object object = field.getValue();
								Hashtable getterMap = persistible.getGetterMap();
								
								Method getter = (Method)getterMap.get((String)valueObj);
								Object getReturn = 
										ReflectionHelper.invoke(getter, persistible, null);
								if ( getReturn != null) {
										field.setValue(getReturn);
								}
						}catch ( Exception ee) {
								ee.printStackTrace();
						}
				}				
				screenInitialized = true;
				// Put the cursor on the first text field
		}

		public void setupInputMaps() {
//			InputMap im = getInputMap();
//			if ( im != )
//			getInputMap().setParent(OncBrowser.getDefaultInputMap());
		}

		public int  getMaxWidth() {
				// Go through all the components on this panel to determine the width
				int max = 0;
        for (int i = 0; i < getComponentCount(); ++i) {
            Rectangle r = getComponent(i).getBounds();
            max = Math.max(max, r.x + r.width);
        }
				return max;
		}
		public int  getMaxHeight() {
				// Go through all the components on this panel to determine the height
								int max = 0;
        for (int i = 0; i < getComponentCount(); ++i) {
            Rectangle r = getComponent(i).getBounds();
            max = Math.max(max, r.y + r.height);
        }
				return max;
		}

		public void getData() {
			// 	if ( screenInitialized ) {
// 						System.out.println("screen is initialized  " );
// 						oncotcap.util.ForceStackTrace.showStackTrace();
// 				}
						
				if ( !screenInitialized ) {
						//System.out.println("screen is initialized - go ahead and scrape it");
						//else {
						//System.out.println("screen is NOT initialized ");
						return;
				}
				AutoGenEditable persistible = 
						(AutoGenEditable)editObj;
				// Scrape the screen and put the data in the persistible 
				// instance
				for (Enumeration e = uiHashtable.keys();
						 e.hasMoreElements(); ) {
						Object obj = e.nextElement();
						// 	System.out.println("OBJECT " + obj
						// + " type " + obj.getClass());
						OncUiObject field = (OncUiObject)obj;
						//						System.out.println("Field " + field);
						try {
								Object object = field.getValue();
								Object values[] =  new Object[1];
								Hashtable setterMap = persistible.getSetterMap();
								String fieldName = (String)uiHashtable.get(obj);
								values[0] = object;
								Method setter = (Method)setterMap.get(fieldName);
								// System.out.println("setter " + setter + " persistible " + 
// 																	 persistible + " values " + values[0]);
								// if ( fieldName.equals("assessments") ) 
// 										oncotcap.util.ForceStackTrace.showStackTrace();
								
								ReflectionHelper.invoke(setter, persistible, values);
							 
						}catch ( Exception ee) {
								ee.printStackTrace();
								//System.out.println("hashtable value " + uiHashtable.get(obj));
						}
				}
			// 	System.out.println("DefaultEditorPanel.getData " 
// 														 + persistible);
				if ( persistible instanceof Persistible ) 
						((Persistible)persistible).update();
		}

		public void save() {
				getData();	
		}

		public Object getValue() {return null;}
		
		public Object getEditObject() {
				return this.editObj;
		}

		public static Dimension getSize(Container container) {
        Dimension size = new Dimension();
        for (int i = 0; i < container.getComponentCount(); ++i) {
            Rectangle r = container.getComponent(i).getBounds();
            size.width = Math.max(size.width, r.x + r.width);
            size.height = Math.max(size.height, r.y + r.height);
        }
        return size;
    }

		public void objectSaved(SaveEvent e) {
				if ( e.getSavedObject() instanceof Persistible ) 
						fillFields(e.getSavedObject());
		}

		public void objectDeleted(SaveEvent e) {
				// Do nothing the data source will refresh the tree
		}
		
		public String toString() {
				String editObjString = "NULL edit obj";
				if ( editObj != null ) 
						editObjString = editObj.toString();
				return super.toString() + editObjString;
		}

//  private void resize(Component c, Dimension oldContainerSize, Dimension newContainerSize, Point slidePoint, Component horizontalStretcher, Component verticalStretcher) {
//         Rectangle r = c.getBounds();
//         if (horizontalStretcher == null) {
//             r.x = (int) Math.round(((double) r.x * newContainerSize.width) / oldContainerSize.width);
//             r.width = (int) Math.round(((double) r.width * newContainerSize.width) / oldContainerSize.width);
//         } else if (c == horizontalStretcher) {
//             r.width += newContainerSize.width - oldContainerSize.width;
//         } else if (r.x >= slidePoint.x) {
//             r.x += newContainerSize.width - oldContainerSize.width;
//         } else {
//             // do nothing
//         }
//         if (verticalStretcher == null) {
//             r.y = 
// 								(int) Math.round(((double) r.y * newContainerSize.height)/ 
// 																 oldContainerSize.height);
//             r.height = 
// 								(int) Math.round(((double) r.height * newContainerSize.height)/
// 																 oldContainerSize.height);
//         } else if (c == verticalStretcher) {
//             r.height += newContainerSize.height - oldContainerSize.height;
//         } else if (r.y >= slidePoint.y) {
//             r.y += newContainerSize.height - oldContainerSize.height;
//         } else {
//             // do nothing
//         }
//         c.setBounds(r);
//     }

		public void setBounds(int x,
													int y,
													int width,
													int height){
	// 			if ( x == -1 && y == -1 && width == -1 && height == -1 ) {
// 						// initialize original positions
// 						this.initialX = x;
// 						this.initialY = y;
// 						this.initialWidth = width;
// 						this.intialHeight = height;
// 				}
						
				super.setBounds(x, y, width, height);
		}

		// Create a listener for component events
		public class MyComponentListener implements ComponentListener {
				// This method is called only if the component 
				//was hidden and setVisible(true) was called
				public void componentShown(ComponentEvent evt) {
						// Component is now visible
						Component c = (Component)evt.getSource();
				}
				
        // This method is called only if the component was 
				// visible and setVisible(false) was called
        public void componentHidden(ComponentEvent evt) {
            // Component is now hidden
            Component c = (Component)evt.getSource();
        }
				
        // This method is called after the component's location
				// within its container changes
        public void componentMoved(ComponentEvent evt) {
            Component c = (Component)evt.getSource();
						
            // Get new location
            Point newLoc = c.getLocation();
        }
				
        // This method is called after the component's size changes
        public void componentResized(ComponentEvent evt) {
            Component c = (Component)evt.getSource();
						
            // Get new size
            Dimension newSize = c.getSize();
						// Resize all the components on this panel accordingly
						// Defer the resize of each ui object type
						Container container = null;
						if ( c instanceof Container ) {
								container = (Container)c;
								for (int i = 0; i < container.getComponentCount(); ++i) {
										Component comp = container.getComponent(i);
										if ( comp instanceof OncUiObject) {
												//((OncUiObject)comp).resize(container);
										}
										Rectangle r = container.getComponent(i).getBounds();
										// size.width = Math.max(size.width, r.x + r.width);
// 										size.height = Math.max(size.height, r.y + r.height);

								}
						}
						repaint();
        }
    }
    
    
		/* class InitialFocusSetter {
        public static void setInitialFocus(Window w, Component c) {
            w.addWindowListener(new FocusSetter(c));
        }
    
        public static class FocusSetter extends WindowAdapter {
            Component initComp;
            FocusSetter(Component c) {
                initComp = c;
            }
            public void windowOpened(WindowEvent e) {
                initComp.requestFocus();
    
                // Since this listener is no longer needed, remove it
                e.getWindow().removeWindowListener(this);
            }
        }
    }
		*/
}
