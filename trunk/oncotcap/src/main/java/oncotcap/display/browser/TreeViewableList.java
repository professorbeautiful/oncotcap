package oncotcap.display.browser;

import java.util.*;

public class TreeViewableList {
		public Vector viewables = null;
		public OncViewerTreeNode viewerObject = null;
		
		public TreeViewableList () {
				viewables = new Vector();
		}
		public void setViewableList(Vector list) {
				viewables = list;
		}
		public Vector getViewableList() {
				return viewables;
		}
		public void setViewableObject(OncViewerTreeNode viewerObj) {
				viewerObject = viewerObj;
		}
		public OncViewerTreeNode getViewableObject() {
				return viewerObject;
		}
}
