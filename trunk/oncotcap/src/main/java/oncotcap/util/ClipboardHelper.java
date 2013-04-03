package oncotcap.util;

import java.awt.datatransfer.*;
import java.awt.*;

public class ClipboardHelper {
	public static void main(String [] args) {
		System.out.println(get());
	}

	public static String get(){
		try{	
			return((String)
						   (Toolkit.getDefaultToolkit().
										 getSystemClipboard().getContents(null)
						   )
						  .
						   getTransferData(DataFlavor.
										   stringFlavor))
			;
		}
		catch(java.awt.datatransfer.UnsupportedFlavorException exc)
		{return(null);}
		catch(java.io.IOException exc)
		{return(null);}
	}
}
