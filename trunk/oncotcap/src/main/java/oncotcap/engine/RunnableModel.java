package oncotcap.engine;


public interface RunnableModel
{
	public void run();
	public void pause();
	public OncModel getModel();
	public void setModel(OncModel model);
	public long getSimulationSeed();
	public void setSimulationSeed(long seed);
	public void setSingleRNG(boolean singleRNG);
	public boolean usesSingleRNG();
}
