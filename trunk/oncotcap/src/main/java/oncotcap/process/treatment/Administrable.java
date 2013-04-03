package oncotcap.process.treatment;

import oncotcap.util.*;
import oncotcap.sim.schedule.AbstractSchedulable;

public abstract class Administrable
	   extends AbstractSchedulable implements HasDescriptors{
	public String name;

	/** The administer method is abstract, allowing for Regimens, Cycles and
	 ** Combos as well as individual Agents.
	 **/
	abstract public void administer(AbstractPatient patient, double time);
	
	public void administer(AbstractPatient patient) {
		administer(patient, 0.0);
	}

	/** Administration of a treatment to a particular patient.
	 ** If patient is null, this is a template, e.g. in a clinical
	 ** trial protocol.
	 **/
	public Administrable (String name){
		this.name = name;
	}

	Descriptors descriptors;
	public boolean hasDescriptor(String s){
		return(descriptors.hasDescriptor(s));
	}
}