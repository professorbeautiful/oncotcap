package oncotcap.util;

import java.net.*;
import java.util.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.image.*;
import java.awt.Image;

import java.io.*;



public final class OncoTcapIcons {
	private static Map theIcons = new HashMap();
	private static Map theImages = new HashMap();

	public static ImageIcon getClinTrialWizardIcon() 		{return getImageIcon("erth16x16.gif");}
	public static ImageIcon getSplashScreenImage() 		{return getImageIcon("erthsplash.jpg");}
	public static ImageIcon getSmallLeftArrow()  {return getImageIcon("smallleftarrow.gif");}
	public static ImageIcon getSmallRightArrow() {return getImageIcon("smallrightarrow.gif");}
	//  ".ico" files fail.
	public static ImageIcon getExclamIcon() {return getImageIcon("exclam.bmp");}
	public static ImageIcon getDefault() {return getImageIcon("tricorn-icon.jpg");}
	//public static ImageIcon getDefault() {return getImageIcon("erth16x16.gif");}
	// ".bmp" files don't work.
	//public static Icon getAddIcon()					{return getImageIcon("Add");}

	public static ImageIcon getImageIcon(String name) {
			//if ( !"null".equals(name) )
				//	return null;
			ImageIcon icon = (ImageIcon) theIcons.get(name);
			if (icon == null || icon.getIconWidth() == -1) {
					icon = loadIcon(name);
					theIcons.put(name, icon);
			}
		return icon;
	}

	public static Image getImage(String name) {
			Image image = (Image) theImages.get(name);
			if (image == null) {
					image = loadImage(name);
					theImages.put(name, image);
			}
		return image;
	}

	private static ImageIcon loadIcon(String name) {
		ImageIcon icon = null;
		String path = "../resource/image/" + name;
		URL url = OncoTcapIcons.class.getResource(path);
		//You need the ".class".  Don't know why.
		if (url != null) {
			icon = new ImageIcon(url);
			if (icon.getIconWidth() == -1) {
				Logger.log("Error; failed to load " 
									 + OncoTcapIcons.class + " loadIcon "
									 +  name + " URL:" + url);
				
			}
		}
		if (icon == null) {
				if ( !"null.jpg".equals(name) )
						Logger.log("Icon is " + name + " null");
		}
		return icon;
	}
	private static Image loadImage(String name) {
		Image image= null;
		String path = "../resource/image/" + name;
		URL url = OncoTcapIcons.class.getResource(path);
		//You need the ".class".  Don't know why.
		if (url != null) {
				try {
						image = ImageIO.read(url);
				}catch (IOException ioe) {
						Logger.log("Error; failed to read " 
											 + OncoTcapIcons.class + " loadImage " 
											 +  name + " URL:" + url);
				}
			if (image == null) {
				Logger.log("Error; failed to load " + OncoTcapIcons.class + " loadIcon "
								 +  name + " URL:" + url);
			}
		}
		if (image == null) {
			Logger.log("Icon is " + name + " null");
		}
		return image;
	}
}
