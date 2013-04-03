package oncotcap.process.adverseevent;

import oncotcap.process.treatment.*;

public class CumulativeThresholdTarget extends AETarget
{
	private double concentrationThreshold;
	private double timeAboveThreshold = 0;
	private double statusConstant = 16.7;
	public CumulativeThresholdTarget(double concentrationThreshold, AgentDistributor agentDist, double updateGap)
	{
		super(agentDist, updateGap);
		this.concentrationThreshold = concentrationThreshold;
	}

	public double functionStatus()
	{
		if (agentDist.getConcentration() > concentrationThreshold)
			timeAboveThreshold += gap;
		else
			timeAboveThreshold = Math.max(timeAboveThreshold - gap, 0.0);

		timeAboveThreshold = Math.max(timeAboveThreshold, 0.0);

		return(Math.max(0,maxFunctionStatus - statusConstant * timeAboveThreshold));
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

