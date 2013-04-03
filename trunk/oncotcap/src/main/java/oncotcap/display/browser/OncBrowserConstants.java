/*
 * Educational Resource for Tumor Heterogeneity
 *             Oncology Thinking Cap
 *
 * Copyright (c) 2003  University of Pittsburgh
 * All rights reserved.
 *
 *  SourceSafe Info:
 *               $Header: $
 *               Revision: $Revision$
 *               Author: $Author$
 *
 * Code Review History:
 *     (mm.dd.yyyy initials)
 *
 * Test History:
 *     (mm.dd.yyyy initials)
 */
package oncotcap.display.browser;

import java.awt.*;
import javax.swing.*;

public class OncBrowserConstants {
		public static final Dimension DEFAULT_LARGE_PANEL_SIZE = new Dimension(700,650);
		public static final Dimension DEFAULT_MEDIUM_PANEL_SIZE = new Dimension(700,650);
		public static final Dimension DEFAULT_SMALL_PANEL_SIZE = new Dimension(700,650);
		public static final ImageIcon filterOnIcon = 	
				oncotcap.util.OncoTcapIcons.getImageIcon("filter-on.jpg");
		public static final ImageIcon filterOffIcon = 	
				oncotcap.util.OncoTcapIcons.getImageIcon("filter-off.jpg");
		public static final ImageIcon siblingOnIcon = 	
				oncotcap.util.OncoTcapIcons.getImageIcon("leafless-hide.jpg");
		public static final ImageIcon siblingOffIcon = 	
				oncotcap.util.OncoTcapIcons.getImageIcon("leafless-show.jpg");
		public static final ImageIcon sortAlphaIcon = 	
				oncotcap.util.OncoTcapIcons.getImageIcon("sort-alpha.jpg");
		public static final ImageIcon sortDateIcon = 	
				oncotcap.util.OncoTcapIcons.getImageIcon("sort-date.jpg");
		public static final ImageIcon collapseIcon = 	
				oncotcap.util.OncoTcapIcons.getImageIcon("collapse-icon.jpg");
		public static final 		ImageIcon expandIcon = 	
				oncotcap.util.OncoTcapIcons.getImageIcon("expand-icon.jpg");
		public static final 		ImageIcon addLinkIcon = 	
				oncotcap.util.OncoTcapIcons.getImageIcon("add-link-icon.jpg");
		public static final 		ImageIcon addRootIcon = 	
				oncotcap.util.OncoTcapIcons.getImageIcon("add-root-icon.jpg");
		public static final 		ImageIcon addKeywordIcon = 	
				oncotcap.util.OncoTcapIcons.getImageIcon("add-key-icon.jpg");
		public static final 		ImageIcon removeIcon = 	
				oncotcap.util.OncoTcapIcons.getImageIcon("remove-small.jpg");
		public static final 		ImageIcon linkIcon = 	
				oncotcap.util.OncoTcapIcons.getImageIcon("link-icon.jpg");
		public static final 		ImageIcon unlinkIcon = 	
				oncotcap.util.OncoTcapIcons.getImageIcon("remove-link-icon.jpg");
		public static final 		ImageIcon findIcon = 	
				oncotcap.util.OncoTcapIcons.getImageIcon("find-icon.jpg");
		public static final 		ImageIcon filterIcon = 	
				oncotcap.util.OncoTcapIcons.getImageIcon("filter-icon.jpg");
		public static final 		ImageIcon mcIcon = 	
				oncotcap.util.OncoTcapIcons.getImageIcon("mc-icon.jpg");
		public final static String WINDOWS =
				"com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		public static final 		ImageIcon addIS = 	
				oncotcap.util.OncoTcapIcons.getImageIcon("informationsource.jpg");
		static public final ImageIcon EQUAL_ICON = 
				oncotcap.util.OncoTcapIcons.getImageIcon("equal.jpg");
		static public final Color highlightBGColor = new Color(175,177,159);
		static public final Color defaultTextColor = Color.black;
		static public final Color defaultBGColor = Color.WHITE;
		static public final Color modelBuildingColor = new Color(101,105,68);
		static public final Color KBColorPale = new Color(165, 140, 136);
		static public final Color EColorVeryPale = new Color(179,197,194);
		static public final Color MBColorPale = new Color(190,193,167);
		static public final Color MBColorDark = new Color(101,105,68);
		static public final Color MBColorVeryPale = new Color(216, 201, 198);
		static public final Color KCColorPale = new Color(169,169,192);
		static public final Color KColorDarkest = Color.black;
		static public final Color EColorDarkest = new Color(65, 103, 103);
		static public final Color KAColorDarkest = new Color(67,65,103);
		static public final Color MBColorDarkest = new Color(72,75,51);
		static public final Color CBColorPale = new Color(194, 168, 166);
		static public final Color CBColorDarkest = new Color(93, 61,61);
		//static public final Color CBColorPale = new Color(1
		static public final Color EColorPale = new Color(169,193,191);

		//public final static Vector autoAddKeywords = new Vector();
		//Fonts
		static public final Font DIRECTIONS_FONT = new Font("Dialog", Font.BOLD+Font.ITALIC, 12);
		static public final Font DIRECTIONS_FONT_SMALL = new Font("Dialog", Font.BOLD, 10);
		static public final Font DIRECTIONS_FONT_SMALL_ITALIC = 
				new Font("Dialog", Font.ITALIC, 12);
}
