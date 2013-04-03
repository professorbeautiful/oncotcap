package oncotcap.engine;


import oncotcap.datalayer.persistible.EventDeclaration;

public class OncEvent extends DefaultClassSection implements ClassSection
{
	public OncEvent(EventDeclaration decl, String  substitutedName)
	{
		super();
		setDeclaration(decl);
		this.name = substitutedName;
	}
}