package oncotcap.engine;


public abstract class AbstractSimulationEngine implements SimulationEngine
{
	private OncModel model;
	public AbstractSimulationEngine(OncModel model)
	{
		this.model = model;
	}
	public OncModel getModel()
	{
		return(model);
	}
	public void setModel(OncModel model)
	{
		this.model = model;
	}
}