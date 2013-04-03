package oncotcap.engine;

import oncotcap.sim.EventParameters;

public interface EventHandler
{
	public void handleEvent(String eventName, Object caller, EventParameters eventParameters);
}
