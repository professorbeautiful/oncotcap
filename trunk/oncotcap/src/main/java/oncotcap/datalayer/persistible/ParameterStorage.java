package oncotcap.datalayer.persistible;

public interface ParameterStorage
{
	public Object clone();
	public void setValue(ParameterStorage storage);
	public void setValue(String storage);
	public String toString();
}