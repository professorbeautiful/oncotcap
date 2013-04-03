package oncotcap.display.common;

import java.awt.Color;
import java.awt.Font;

import oncotcap.process.OncProcess;
import oncotcap.util.FontHelper;

public class DisplayMessage
{
	private String name;
	private String message;
	private OncProcess sender;
	private double time;
	private Font font = FontHelper.defaultFont;
	private Color foreground = Color.BLACK;
	
	public void setName(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		return(name);
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public String getMessage()
	{
		return(message);
	}
	public void setSender(OncProcess sender)
	{
		this.sender = sender;
	}
	public OncProcess getSender()
	{
		return(sender);
	}
	public void setTime(double time)
	{
		this.time = time;	}
	public double getTime()
	{
		return(time);
	}
	public void setFont(Font font)
	{
		if(font != null)
			this.font = font;
	}
	public Font getFont()
	{
		return(font);
	}
	public Color getForeground()
	{
		return(foreground);
	}
	public void setForeground(Color foreground)
	{
		if(foreground != null)
			this.foreground = foreground;
	}
}
