package oncotcap.engine;

import java.util.*;
import oncotcap.datalayer.persistible.ProcessDeclaration;

public interface InstructionProvider
{
	public ProcessDeclaration getProcessDeclaration();
	public Collection<InstructionProvider> getAdditionalProviders();
	public Collection<Instruction> getInstructions();
	public Collection<ProcessDeclaration> getReferencedProcesses();
}
