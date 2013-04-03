package oncotcap.display.common;


public class HeaderInfo
{

	String tipText;
	String helpFile;
	
	public HeaderInfo(String tipText, String helpFile)
	{
		this.tipText = tipText;
		this.helpFile = helpFile;
	}

	public String getTip() { return(tipText); }
	public String getHelpFile() { return(helpFile); }

}