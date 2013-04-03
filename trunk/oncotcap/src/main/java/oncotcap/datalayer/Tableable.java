package oncotcap.datalayer;

public interface Tableable
{
  public String [] getColumnsInOrder();
	public String [] getColumnNamesInOrder();
	public String getColumnLabel(String columnName);
}
