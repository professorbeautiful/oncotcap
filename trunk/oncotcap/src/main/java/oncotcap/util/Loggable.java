package oncotcap.util;

public interface Loggable {

	public int verbose();
	public void setVerbose(int v);
	public String logName();
	//public String methodName();  -- can't be done, I think.
}