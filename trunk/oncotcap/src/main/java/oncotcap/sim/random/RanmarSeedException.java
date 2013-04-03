package oncotcap.sim.random;

public class RanmarSeedException extends Error {
	String errstring;
	RanmarSeedException(String errstring){
		this.errstring = new String(errstring);
	}
	String getErrString(){
		return(errstring);
	}
}