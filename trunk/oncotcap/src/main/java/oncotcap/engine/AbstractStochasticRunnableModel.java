
package oncotcap.engine;

import oncotcap.process.TopOncParent;

public abstract class AbstractStochasticRunnableModel extends TopOncParent implements StochasticRunnableModel 
{
	private OncModel model;
	private boolean useOneRNG = false;
	
	
	public AbstractStochasticRunnableModel()
	{
		super();
	}
	public OncModel getModel()
	{
		return(model);
	}
	public void setModel(OncModel model)
	{
		this.model = model;
	}

	//implementation of OncParent.getSeed. Overrides the default get seed method
	//because this is the "TopOncParent" implementation.
/*	public long getSeed()
	{
		return(getSimulationSeed());
	}
	public void setSeed(long seed)
	{
		setSimulationSeed(seed);
	} */

}
