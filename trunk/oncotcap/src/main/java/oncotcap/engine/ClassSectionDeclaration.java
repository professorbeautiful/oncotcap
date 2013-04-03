package oncotcap.engine;
import oncotcap.display.common.EventChooser;

public interface ClassSectionDeclaration
{
	public static final int METHOD = EventChooser.PROCESS;
	public static final int EVENT = EventChooser.EVENT;
	public static final int DECLARATION = EventChooser.DECLARATION_SECTION;
	
	public static final ClassSectionDeclaration DECLARATION_SECTION = new DeclarationSection();
	
	public String getName();
	
	public class DeclarationSection implements ClassSectionDeclaration
	{
		public String getName(){return("");}
	}
}
