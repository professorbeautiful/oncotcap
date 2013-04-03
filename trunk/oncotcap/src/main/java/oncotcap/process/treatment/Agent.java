package oncotcap.process.treatment;

import oncotcap.util.*;

/** Note that an Agent by itself is not an Administrable
 **/
public class Agent  {
	public String name;
	public Descriptors descriptors;
	public Dose initialDose;

	public Agent(String name){
		this.name = new String(name);
	}
	public Agent(String name, Dose dose){
		this.name = new String(name);
		// Careful, this will not support changing the agent name from
		// outside!!
		//  Which way is best?
		// Probably, this should be the place a name is changed, then
		// anyone who cares should listen for the change.
		this.initialDose = dose;
	}
}
