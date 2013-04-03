package oncotcap.engine;

import oncotcap.datalayer.persistible.MethodDeclaration;

public class OncMethod extends DefaultClassSection implements ClassSection
{
	public OncMethod(MethodDeclaration decl, String substitutedName)
	{
		super();
		setDeclaration(decl);
		this.name = substitutedName;
	}
}