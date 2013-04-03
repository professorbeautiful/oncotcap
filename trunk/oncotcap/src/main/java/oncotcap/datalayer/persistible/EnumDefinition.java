/*
 * Created on Feb 17, 2004
 *
 */
package oncotcap.datalayer.persistible;

import java.util.*;

import oncotcap.util.*;
import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.persistible.parameter.LevelSingleParameter;
import oncotcap.datalayer.persistible.parameter.SingleParameter;
import oncotcap.engine.*;

public class EnumDefinition extends VariableDefinition implements InstructionProvider
{
//	private boolean idEnum = true;
	private EnumLevelList levelList = null;
	private Keyword keyword = null;
	private EnumLevel defaultLevel = null;
	
	private EnumDefinition selfreference;
	public EnumDefinition(oncotcap.util.GUID guid){
		super(guid, "Undefined Enum",
		      "oncotcap.datalayer.persistible.EnumDefinition");
		selfreference = this;
	}
	public EnumDefinition()
	{
		this(true);
	}
	public EnumDefinition(boolean saveToDataSource)
	{
		this("Undefined Enum", saveToDataSource);
	}
	public EnumDefinition(String variableName, boolean saveToDataSource)
	{
		super(variableName, "oncotcap.datalayer.persistible.EnumDefinition");
		selfreference = this;
		this.setPersistibleState((saveToDataSource ? Persistible.DIRTY: Persistible.DO_NOT_SAVE));
	}
	public EnumDefinition cloneSubstitute(ValueMapPath path)
	{
		EnumDefinition ed = new EnumDefinition(false);
		ed.varType = path.substitute(varType);
		ed.varName = path.substituteJavaName(varName);
		ed.defaultValue = path.substituteJavaName(defaultValue);
		ed.isArray = isArray;
//		ed.idEnum = idEnum;
		ed.setIsID(isID());
		ed.setIsNonID(isNonID());
		if(levelList != null)
			ed.levelList = levelList.cloneSubstitute(path);
		ed.keyword = keyword;
		if(defaultLevel != null)
			ed.defaultLevel = defaultLevel.cloneSubstitute(path);
		
		//fields from variabledefinition
		ed.processDef = processDef;
		ed.varType = path.substitute(varType);
		ed.varName = path.substituteJavaName(varName);
		ed.defaultValue = path.substituteJavaName(defaultValue);
		ed.isArray = isArray;
		
		return(ed);
	}
	public EnumLevelList getLevelList()
	{
		return(levelList);
	}
	public void setLevelList(EnumLevelList list)
	{
		levelList = list;
		update();
	}
	public EnumLevel getDefaultLevel()
	{
		if(defaultLevel == null)
		{
			if(levelList == null || levelList.getLevels().size() <= 0)
				return(null);
			else
				return((EnumLevel) levelList.getLevels().toArray()[0]);
		}
		else
			return(defaultLevel);
		
	}
	public void setDefaultLevel(EnumLevel level)
	{
		defaultLevel = level;
		update();
	}
/*	public boolean isID()
	{
		return(idEnum);
	}
	public Boolean isIDAsBoolean()
	{
		return(new Boolean(idEnum));
	}
	public void setID(boolean id)
	{
		idEnum = id;
		update();
	}
	public void setID(Boolean id)
	{
		if(id != null)
			idEnum = id.booleanValue();
	}*/
	public String getName()
	{
			return(varName);
	}
	public void setName(String name) {
			// Override this method because the name will come from the keyword
			varName = name;
	}

	public Keyword getKeyword() {
		return keyword;
	}

  public void setKeyword(Keyword key) {
			keyword = key;
		// 	if ( key != null )
// 					varName = key.toString();
			update();
	}

  	private Vector sections = new Vector();
/*  	public Collection getDeclarationSections()
	{
//sub		String rCode = "\n" + sb.substitute(getDeclarationCode(sb));
  		sections.clear();
		String rCode = "\n" + getDeclarationCode();
		CodeSection declarationSection = new CodeSection(this, rCode);
		declarationSection.setProcessDeclaration(processDef);
		declarationSection.setEventType(CodeSection.DECLARATION_SECTION);
		declarationSection.setCodeSource(this);
		sections.add(declarationSection);
		return(sections);
	}
	public String getDeclarationCode()
	{
		StringWriter w = new StringWriter();
		try
		{
			writeDeclarationSection(w);
		}
		catch(IOException e){Logger.log("Warning: unable to write declaration code for Enum:\n" + getProcess().getName() + "." + getName());}
		return(w.toString());
	}
	public void writeDeclarationSection(Writer fw) throws IOException
	{
		Iterator iter2;
		boolean didFirst;
		String val, valLine = "";

//sub		fw.write("\tpublic static class " + className(sb) + " extends Enum\n\t{\n\t\tprivate " + className(sb) + "() { }\n\t\tprivate " + className(sb) + "(String name) { super(name); }\n");
		fw.write("\tpublic static class " + className() + " extends OncEnum\n\t{\n\t\tprivate " + className() + "() { }\n\t\tprivate " + className() + "(String name) { super(name); }\n");
		iter2 = levelList.getLevelIterator();
		didFirst = false;
		while(iter2.hasNext())
		{
			val = iter2.next().toString();
//sub			fw.write("\t\tpublic static final " + className(sb) + " " + StringHelper.javaName(val) + " = new " + className(sb) + "(\"" + StringHelper.javaName(val) + "\");\n");
			fw.write("\t\tpublic static final " + className() + " " + StringHelper.javaName(val) + " = new " + className() + "(\"" + StringHelper.javaName(val) + "\");\n");
			if(didFirst)
				valLine = valLine + ", " + StringHelper.javaName(val);
			else
			{
//sub				valLine = "\t\tpublic static final " + className(sb) + " [] VALUES = { " + StringHelper.javaName(val);
				valLine = "\t\tpublic static final " + className() + " [] VALUES = { " + StringHelper.javaName(val);
				didFirst = true;
			}
		}
		valLine = valLine + " };";
		fw.write("\n" + valLine + "\n");
		fw.write("\t\tpublic OncEnum [] getValues()\n\t\t{\n\t\t\treturn(VALUES);\n\t\t}\n\t}\n");
//sub		fw.write("\t" + className(sb) + " " + varName(sb) + " = " + className(sb) + "." + getDefault() + ";\n\n");
		fw.write("\t" + className() + " " + varName() + " = " + className() + "." + getDefault() + ";\n\n");
	} */
	public String className()
	{
//sub		return(StringHelper.javaName(sb.substitute(getName())));
		return(StringHelper.javaNameKeepBQuotes(getName()));
	}
	public String varName()
	{
//sub		return("_" + StringHelper.javaName(sb.substitute(getName())));
		return(StringHelper.variableNameKeepBackQuotes(getName()));
	}
	
		public String toString() {
				StringBuilder str = new StringBuilder();
				if ( getProcessDeclaration() != null )
						str.append( getProcessDeclaration());
				else
						str.append("Undefined Process");
				str.append(".");
				if ( getName() != null ) 
								str.append( getName());
				else
						str.append("Undefined Process");
				//if (getDefaultLevel() != null ) {
				//		str.append(".");
				//		str.append(getDefaultLevel());
				//}
				
				return str.toString();
				
		}
	
	public String getDefault()
	{
		if(levelList == null)
			return(null);
		else
		{
				if ( levelList.isNumericList() ) {
						// Make the default the minimum
						return levelList.getMinAsString();
				}
				else if(levelList.getLevels() != null)
						return(levelList.getLevels().get(0).toString());
				else
						return(null);
		}
	}
	
	/////
	
	public ProcessDeclaration getProcessDeclaration()
	{
		return(this.getProcess());
	}
	public ClassSectionDeclaration getSectionDeclaration()
	{
		return(SystemDefinedOncMethod.INIT_SECTION);
	}
	public Collection<InstructionProvider> getAdditionalProviders()
	{
		return(new Vector<InstructionProvider>());
	}
	private Vector<Instruction> instructions = null;
	public Collection<Instruction> getInstructions()
	{
		//if this method is ever changed to return more than one 
		//instruction check the comment in DefaultClassSection.addInstruction
		//first
		if(instructions == null)
		{
			instructions = new Vector<Instruction>();
			instructions.add(new DeclarationInstruction());
		}
		return(instructions);
	}
	public class DeclarationInstruction implements Instruction
	{
		public Collection<String> getSetVariables(ValueMapPath path)
		{
			Vector<String> vars = new Vector<String>();
			vars.add("_" + getName());
			return(vars);
		}
		public Collection<String> getAllVariables(ValueMapPath path)
		{
			return(getSetVariables(path));
		}
		public Collection<VariableDependency> getVariableDependencies(ValueMapPath path)
		{
			return(new Vector<VariableDependency>());
		}
		public ClassSectionDeclaration getSectionDeclaration()
		{
			return(ClassSectionDeclaration.DECLARATION_SECTION);
		}
		public InstructionProvider getEnclosingInstructionProvider()
		{
			return(selfreference);
		}
		public boolean equals(Object obj)
		{
			if(obj instanceof DeclarationInstruction)
				return(selfreference.equals(((DeclarationInstruction)obj).getEnclosingInstructionProvider()));
			else
				return(false);
		}
		public int hashCode()
		{
			return(selfreference.hashCode());
		}
		
	}
	public Collection<ProcessDeclaration> getReferencedProcesses()
	{
		if(getProcessDeclaration() != null)
		{
			Vector<ProcessDeclaration> rVec = new Vector<ProcessDeclaration>();
			rVec.add(getProcessDeclaration());
			return(rVec);
		}
		else
			return(new Vector<ProcessDeclaration>());
	}
	public void levelListUnion(EnumDefinition def)
	{
		if ( levelList == null )
			levelList = new EnumLevelList();
		levelList.union(def.getLevelList());
		if((def.isNonID() && this.isID()) || (def.isID() && this.isNonID()))
			System.out.println("WARNING: Enum " + this.getName() + " and Enum " + def.getName() + " cannot be combined becase one must be an ID Enum and the other must be a Non-ID Enum");
		else if(def.isNonID())
			this.setIsNonID(true);
		else if(def.isID())
			this.setIsID(true);
	}
	
	public String getInstanceString(ValueMapPath path){
		StringBuilder instanceString = new StringBuilder();
		String className = null;
		String enumName = null;
				
		if ( getProcess() != null ) 
			 className = path.substituteJavaName(getProcess().getName());
		else 
			System.out.println("Error: enumDefintion missing process " + toString());
		enumName = path.substituteJavaName(getVariableName());
		//	 ((Cancer_cell) getContainerInstance(Cancer_cell.class))_p53 
		instanceString.append("((");
		instanceString.append(className);
		instanceString.append(") getContainerInstance(");
		instanceString.append(className);
		instanceString.append(".class))._");
		instanceString.append(enumName);
		return instanceString.toString();
	}
	
	public String getInstanceLevelString(ValueMapPath path, EnumLevel enumLevel){
		StringBuilder instanceString = new StringBuilder();
		String className = null;
		String enumName = null;
		if ( getProcess() != null ) 
			 className = path.substituteJavaName(getProcess().getName());
		else 
			System.out.println("Error: enumDefintion missing process " + toString());
		enumName = path.substituteJavaName(getVariableName());
		//CancerCell.p53.level
		instanceString.append(className);
		instanceString.append(".");
		instanceString.append(enumName);
		instanceString.append(".");
		instanceString.append(path.substituteJavaName(enumLevel.toString()));
		return instanceString.toString();
	}
}
