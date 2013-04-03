package oncotcap.datalayer.persistible;

import oncotcap.datalayer.*;
import oncotcap.engine.ClassSectionDeclaration;

public class MethodDeclaration  extends AbstractPersistible
		implements oncotcap.datalayer.persistible.TreeViewable, ClassSectionDeclaration
{
	public String name;
	public String displayName = null;
	
	public String code = null;

	public MethodDeclaration(oncotcap.util.GUID guid){
		super(guid);
	}
	public MethodDeclaration(){}

	public MethodDeclaration(String name)
	{
		this.name = name;
	}
			
	public String getName()
	{
		return(name);
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String toString()
	{
		if(displayName != null)
			return(displayName);
		else
			return(getName());
	}
	public String getDisplayString()
	{
		return toString();
	}

		public void addCode(String newCode) {
				if (code == null) 
						code = new String();
				code += newCode;
		}
		
		public void setCode(String newCode) {
				code = newCode;
		}

		public void clearCode() {
				code  = new String();
		}

		public boolean equals(Object obj)
		{
			if( ! (obj instanceof MethodDeclaration))
				return(false);
			else
			{
				MethodDeclaration comp = (MethodDeclaration) obj;
				return(comp.name.equalsIgnoreCase(name));
			}
		}

		public int hashCode()
		{
			return(name.toUpperCase().hashCode());
		}

	
}
