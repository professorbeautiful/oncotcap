package oncotcap.engine;

public class VariableDeclaration
{
	private String modifier;
	private String varName;
	private String varType;
	private Object initialValue;
	
	public VariableDeclaration(String modifier,
			                   String variableName,
			                   String variableType,
			                   Object initialValue)
	{
		this.modifier = modifier;
		this.varName = variableName;
		this.varType = variableType;
		this.initialValue = initialValue;
	}
}
