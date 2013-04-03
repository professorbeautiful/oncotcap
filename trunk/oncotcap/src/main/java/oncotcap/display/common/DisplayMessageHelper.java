package oncotcap.display.common;

import java.awt.Color;
import java.awt.Font;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

import oncotcap.process.OncProcess;

public class DisplayMessageHelper
{
	private static Hashtable<OncProcess, Collection<DisplayMessageListener>> listenersByProcess = new Hashtable<OncProcess, Collection<DisplayMessageListener>>();
	public static void sendMessage(OncProcess sender, String messageName, String message, Font font, Color foreground)
	{
		if(listenersByProcess.containsKey(sender))
		{
			Collection<DisplayMessageListener>listeners = listenersByProcess.get(sender);
			if(listeners != null && listeners.size() > 0)
			{
				DisplayMessage displayMessage = new DisplayMessage();
				displayMessage.setMessage(message);
				displayMessage.setName(messageName);
				displayMessage.setSender(sender);
				displayMessage.setTime(sender.getLocalTime());
				displayMessage.setFont(font);
				displayMessage.setForeground(foreground);
				for(DisplayMessageListener listener : listeners)
					listener.handleMessage(displayMessage);
			}
		}
		
	}
	
	public static void addDisplayMessageListener(OncProcess sender, DisplayMessageListener listener)
	{
		Collection<DisplayMessageListener> listeners;
		if(listenersByProcess.containsKey(sender))
			listeners = listenersByProcess.get(sender);
		else
		{
			listeners = new Vector<DisplayMessageListener>();
			listenersByProcess.put(sender, listeners);
		}
		
		if(! listeners.contains(listener))
			listeners.add(listener);
	}
	
	//TODO:
	/*public static void RemoveDisplayMessageListener(DisplayMessageListener listener)
	{
		
	}*/
}
