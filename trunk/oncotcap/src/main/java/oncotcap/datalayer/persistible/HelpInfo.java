package oncotcap.datalayer.persistible;

import oncotcap.datalayer.AbstractPersistible;

public class HelpInfo extends AbstractPersistible
{
	public static int UNKNOWN_HELP = 0;
	public static int TEXT_HELP = 1;
	public static int URL_HELP = 2;

	private int type = UNKNOWN_HELP;
	private String infoText;
	private String shortDescription;

	public HelpInfo() {
	}
	public HelpInfo(oncotcap.util.GUID guid) {
		super(guid);
	}
	public int getType(){return(type);}
	public void setType(int type){this.type = type;}
	public String getInfoText(){return(infoText);}
	public void setInfoText(String text){this.infoText = text;}
	public String getShortDescription(){return(shortDescription);}
	public void setShortDescription(String desc){shortDescription = desc;}
}