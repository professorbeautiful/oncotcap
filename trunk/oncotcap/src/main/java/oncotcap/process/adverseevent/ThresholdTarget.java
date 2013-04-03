package oncotcap.process.adverseevent;

import oncotcap.process.treatment.*;

public class ThresholdTarget extends AETarget
{
	

	private double concentrationThreshold;
	private double statusConstant = 0.01;
	
	public ThresholdTarget(double concentrationThreshold, AgentDistributor agentDist, double updateGap)
	{
		super(agentDist, updateGap);
		this.concentrationThreshold = concentrationThreshold;
	}

	public double functionStatus()
	{
		return(Math.max(0, maxFunctionStatus
						       - statusConstant * Math.max(0, agentDist.getConcentration()
			                                                         - concentrationThreshold)));
	}

	public void setStatusConstant(double sc)
	{
		statusConstant = sc;
	}
	public double getStatusConstant()
	{
		return(statusConstant);
	}
}
