package oncotcap.util;

public class LoggableAdapter implements Loggable {


	private int verbose = 0;
	public LoggableAdapter(int v) {
		setVerbose(v);
	}
	public int verbose(){
		return (verbose);
	}
	public void setVerbose(int v) {
		verbose = v;
	}
	public String logName() {
		return (this.getClass().getName());
	}
	public String logName(Object it) {
		return (it.getClass().getName());
	}

}

/** The following could be included into a class to implement
 ** LoggableAdapter in a way that will persist if this class changes:
 **
	LoggableAdapter loggableAdapter = new LoggableAdapter(1);
	public int verbose() { return loggableAdapter.verbose(); }
	public void setVerbose(int v) { loggableAdapter.setVerbose(v); }
	public String logName() { return loggableAdapter.logName(this); }
*/
