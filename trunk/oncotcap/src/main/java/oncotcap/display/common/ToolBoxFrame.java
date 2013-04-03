package oncotcap.display.common;

import java.awt.*;

public class ToolBoxFrame extends Window
{
/*	public ToolBoxFrame()
	{
		super();
		init();
	} */
	public ToolBoxFrame(Frame owner)
	{
		super(owner);
		init();
	}
/*	public ToolBoxFrame(GraphicsConfiguration gc)
	{
		super(gc);
		init();
	} */
	public ToolBoxFrame(Window owner)
	{
		super(owner);
		init();
	}
	public ToolBoxFrame(Window owner, GraphicsConfiguration gc)
	{
		super(owner, gc);
		init();
	}
	private void init()
	{
		
	}
}