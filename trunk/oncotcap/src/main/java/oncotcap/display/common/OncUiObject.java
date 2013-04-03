package oncotcap.display.common;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JPanel;


public abstract class OncUiObject extends JPanel {
		public int initialX = -1;
		public int initialY = -1;
		public int initialWidth = -1;
		public int initialHeight = -1;	
		public double originalWidthPerc = -1.0;
		public double originalHeightPerc = -1.0;	
		public boolean resizeHeight = true;
		public boolean resizeWidth = true;

		public abstract Object getValue();
		public abstract void setValue(Object obj);
		//public abstract String toString();
		public boolean resizeHeight(){
				return resizeHeight;
		}
		public boolean resizeWidth(){
				return resizeWidth;
		}
		public void setBounds(int x,
													int y,
													int width,
													int height){

				super.setBounds(x, y, width, height);
		}
		public void resize(Container container) {
				// This object can grow in width and height
				// Determine what percentage of the container the ui 
				// object normally occupies
						
				if ( initialX == -1 && initialY == -1 
						 && initialWidth == -1 &&  initialHeight == -1
						 && getParent() != null) {
						System.out.println("initializing initial positions " 
															 +  getParent());
						// initialize original positions
						this.initialX = getX();
						this.initialY = getY();
						this.initialWidth = getWidth();
						this.initialHeight = getHeight();
						this.originalWidthPerc = getWidth()/getParent().getPreferredSize().getWidth();
						this.originalHeightPerc = getHeight()/getParent().getPreferredSize().getHeight();
				}
				double width = container.getSize().getWidth();
				double height = container.getSize().getHeight();
				if ( originalWidthPerc > -1.0 && originalHeightPerc > -1.0) {
						double newWidth = width * originalWidthPerc;
						double newHeight = height * originalHeightPerc;
						System.out.println("resizing " + toString() );
						//+ " container " + container.getBounds());
						System.out.println("obj-- " + getWidth() + " " 
															 + getHeight() + " bounds " + getBounds()); 
						System.out.println("new " + newWidth + " " + newHeight); 
						if ( newWidth <= 0.0 ||  !resizeWidth() )
								newWidth = initialWidth;
						if ( newHeight <= 0.0 ||  !resizeHeight() )
								newHeight = initialHeight;
						setBounds(getX(), getY(), (int)newWidth, (int)newHeight);
				}
		}

}
