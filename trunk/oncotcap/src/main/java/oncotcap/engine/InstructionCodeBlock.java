
package oncotcap.engine;

public class InstructionCodeBlock
{
	private String code;
	private boolean encloseChildren;
	
	public InstructionCodeBlock(String code, boolean encloseChildren)
	{
		this.code = code;
		this.encloseChildren = encloseChildren;
	}
	
	public String getCode()
	{
		return(code);
	}
	public boolean encloseChildren()
	{
		return(encloseChildren);
	}
}
