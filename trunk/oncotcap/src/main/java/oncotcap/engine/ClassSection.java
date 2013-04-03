
package oncotcap.engine;

import java.util.*;

public interface ClassSection 
{
	public SortedInstructionList getInstructionsAndValues();
	public void addInstruction(InstructionAndValues provider);
	public String getName();
	public void setDeclaration(ClassSectionDeclaration declaration);
	public ClassSectionDeclaration getDeclaration();
}
