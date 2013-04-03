package oncotcap.engine;

import java.util.Collection;
import java.util.Vector;

import oncotcap.util.CollectionHelper;

public interface Instruction
{
	public static final Instruction blankInstruction = new BlankInstruction();
	
	public Collection<String> getAllVariables(ValueMapPath path);
	public Collection<VariableDependency> getVariableDependencies(ValueMapPath path);
	public Collection<String> getSetVariables(ValueMapPath path);
	public ClassSectionDeclaration getSectionDeclaration();
	public InstructionProvider getEnclosingInstructionProvider();
	
	public class BlankInstruction implements Instruction
	{
		public Collection<String> getAllVariables(ValueMapPath path){return new Vector<String>();}
		public Collection<VariableDependency> getVariableDependencies(ValueMapPath path){return  new Vector<VariableDependency>();}
		public Collection<String> getSetVariables(ValueMapPath path){return new Vector<String>();}
		public ClassSectionDeclaration getSectionDeclaration(){return(null);}
		public InstructionProvider getEnclosingInstructionProvider(){return(null);}
	}
}
