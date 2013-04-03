package oncotcap.process.adverseevent;

import oncotcap.*;
import oncotcap.sim.OncReporter;
import oncotcap.util.*;
import oncotcap.process.treatment.*;
import java.lang.reflect.*;

public class SimpleToxTarget extends AETarget
{
	private double concentrationMultiplier, nd;
	private int n;
	private Double [] pastConcentrations;
	private Double [] timeIndexedWeights;
	
	public SimpleToxTarget(AgentDistributor agentDist,
						   double updateGap,
						   OncTime resolutionTime,
						   double referenceFunctionStatus,
						   double referenceConcentrationForToxicity)
	{		
		super(agentDist, updateGap);
		//start();
		double i;
		int ii;
		nd = Math.floor(OncTime.convert(resolutionTime,Oncotcap.modelTimeUnits)/updateGap);
		n = (int) nd;
		// length of the resolutionTime in units of updateGap.
		concentrationMultiplier = (2*(1 - referenceFunctionStatus/maxFunctionStatus)/referenceConcentrationForToxicity)/nd;
		pastConcentrations = (Double[]) Array.newInstance(Double.class, n+1);
		timeIndexedWeights = (Double[]) Array.newInstance(Double.class, n+1);
		for (i = 1; i<=n;  i++)
		{
			timeIndexedWeights[(int) i] = new Double((1.0 - (i-1.0)/nd));
			pastConcentrations[(int) i] = new Double(0.0);
		}
		// 
		zeroConcentrationHistory();
	}

	public double functionStatus()
	{
		return(fStatus);
	}

	public void update()
	{
		double i;
		int ii;
/*		if(agentDist.getConcentration() < 1e-5 && treatmentEnded)
		{
			stop();
			zeroConcentrationHistory();
		}*/
		for (ii=n;ii>=2; ii--)
			pastConcentrations[ii] = pastConcentrations[ii - 1];
		pastConcentrations[1] = new Double(agentDist.getConcentration());

		fStatus = maxFunctionStatus;
		for (ii=1; ii<=n; ii++)
			fStatus = fStatus - maxFunctionStatus*pastConcentrations[ii].doubleValue() * concentrationMultiplier * timeIndexedWeights[ii].doubleValue();
			
		if (fStatus <= 0.0)
		{
			agentDist.getPatient().getCTReporter().notifyObservers(OncEvent.DEATHDUETOTOXICITY, "", this.agentDist.getPatient().getMasterScheduler().globalTime);
			agentDist.getPatient().getReporter().notifyObservers(OncEvent.DEATHDUETOTOXICITY, "",agentDist.getPatient().getMasterScheduler().globalTime);
			agentDist.getPatient().stopSimulation();
		}

	}
	public void zeroConcentrationHistory()
	{
		int ii;
		for(ii = 1; ii<=n; ii++)
			pastConcentrations[ii] = new Double(0.0);
	}
}
