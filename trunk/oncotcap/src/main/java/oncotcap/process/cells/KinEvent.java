package oncotcap.process.cells;

import java.lang.reflect.*;

public abstract class KinEvent {
	/* daughters(n, ct) returns the number and type of descendants,
	 * if there are n mother cells of type ct.
	 */
	public String name = "Unnamed kinetics event";
	String [] descriptors=null;
	public double hazard, runningHazardSum, condProb;
//	String cellpopLabel ;

	public KinEvent(/*String cellpopLabel */)
	{
//		this.cellpopLabel = cellpopLabel;
	
	}
	public KinEvent( /*String cellpopLabel,*/ String name ){
		this.name = name;
	}
	public boolean hasDescriptor(String s){
		for (int i=0; i<Array.getLength(descriptors);i++){
			if (s.equals(descriptors[i]))return(true);
		}
		return(false);
	}
	abstract public CellPopulation daughters(Object collectionParent, double n, AbstractCell ct);
}

