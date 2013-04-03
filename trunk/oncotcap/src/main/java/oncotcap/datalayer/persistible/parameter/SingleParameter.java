package oncotcap.datalayer.persistible.parameter;

import oncotcap.datalayer.SaveListener;

public interface SingleParameter
{
	public String getID();
	public String getSingleParameterID();
	public void setSingleParameterID(String id);
	public String getDefaultName();
	public void addSaveListener(SaveListener listner);
	public void removeSaveListener(SaveListener listener);
	public void fireSaveEvent();
	public String getCodeValue();
	public String getDisplayName();
	public void setDisplayName(String name);
	public String getDisplayValue();
	public void setDisplayValue(Object value);
	public Parameter getParameter();
}
