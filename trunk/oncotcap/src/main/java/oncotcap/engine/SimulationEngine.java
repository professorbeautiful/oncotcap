package oncotcap.engine;


public interface SimulationEngine
{
	public void setModel(OncModel model);
	public OncModel getModel();
	public boolean validModel(OncModel model);
	public RunnableModel assemble();
}