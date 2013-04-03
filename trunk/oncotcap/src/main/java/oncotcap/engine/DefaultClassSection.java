
package oncotcap.engine;

import oncotcap.util.StringHelper;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.datalayer.persistible.parameter.*;
import java.util.*;

public class DefaultClassSection implements ClassSection
{
	private ClassSectionDeclaration declaration = null;
	protected String name = null;
	protected SortedInstructionList instructions = new SortedInstructionList();
	
	public SortedInstructionList getInstructionsAndValues()
	{
		instructions.sort();
		return(instructions);
	}
	public void addInstruction(InstructionAndValues provider)
	{
		//this if block compares enum definitions that are already in this section agains new
		//enum definitions that are being added.  If an enum exists already with the same 
		//name as the enum that is being added the enums are unioned (the level lists are unioned)
		//instead of adding the new enum
		if(provider.getInstruction().getEnclosingInstructionProvider() instanceof EnumDefinition)
		{
			//this only works as long as EnumDefinition only returns one instruction (which, as
			//of 6/26 it does...)
			EnumDefinition clonedProvider = ((EnumDefinition) provider.getInstruction().getEnclosingInstructionProvider()).cloneSubstitute(provider.getValues());
			Instruction inst = (Instruction) clonedProvider.getInstructions().toArray()[0];
			InstructionAndValues newProvider = new InstructionAndValues(inst, provider.getValues());
			boolean unioned = false;
			for(InstructionAndValues iav : instructions)
			{
				if(iav.getInstruction().getEnclosingInstructionProvider() instanceof EnumDefinition)
				{
					EnumDefinition listDef = (EnumDefinition) iav.getInstruction().getEnclosingInstructionProvider();
					ValueMapPath listPath = iav.getValues();
					EnumDefinition checkDef = (EnumDefinition) newProvider.getInstruction().getEnclosingInstructionProvider();
					ValueMapPath checkPath = newProvider.getValues();
					if(listPath.substituteJavaName(listDef.getName()).equalsIgnoreCase(checkPath.substituteJavaName(checkDef.getName())))
					{
						listDef.equals(newProvider, iav.getValues());
						listDef.levelListUnion(checkDef);
						unioned = true;
					}
				}
			}
			if(!unioned)
				instructions.add(newProvider);
		}
		//this else if block watches for enum declarations that are part of the variable initializations
		//of an InstantiateAction.  If the declaration has been marked as an ID variable (via the INITIDENUM
		//directive in an OncProcesses hard coded, abstract super classes' .prop file) an EnumDefinition is
		//created for all referenced (referenced via a backquote pointer in the initial value field) EnumDefinitions.
		//These referenced EnumDefinitions are marked as being ID variables and sent then added as InstructionProviders.
		else if(provider.getInstruction() instanceof InstantiateAction.DeclarationInstruction)
		{
			InstantiateAction ia = (InstantiateAction) provider.getInstruction().getEnclosingInstructionProvider();
			Iterator it = ia.getVariableInitializations().iterator();
			while(it.hasNext())
			{
				Object obj = it.next();
				if(obj instanceof DeclareEnum)
				{
					DeclareEnum en  = (DeclareEnum) obj;
					if(en.isID())
					{
						String initVal = en.getInitialValue().trim();
						if(initVal.startsWith("`") && initVal.endsWith("`"))
						{
							String singleParamName = initVal.substring(1,initVal.length()-1);
							Parameter param = provider.getValues().getParameter(singleParamName);
							if(param instanceof SubsetParameter)
							{
								SubsetParameter subParam = (SubsetParameter) param;
								Collection varDefs = subParam.getVariableDefinitions();
								Iterator it2 = varDefs.iterator();
								while(it2.hasNext())
								{
									Object obj2 = it2.next();
									if(obj2 instanceof EnumDefinition)
									{
										EnumDefinition edef = (EnumDefinition) obj2;
										edef.setIsID(true);
										Collection<Instruction> insts = edef.getInstructions();
										for(Instruction inst : insts)
											addInstruction(new InstructionAndValues(inst, provider.getValues()));
									}
								}
							}
							else if(param instanceof TwoLevelParameter)
							{
								EnumDefinition edef = ((TwoLevelParameter) param).getEnumDefinition();
								edef.setIsID(true);
								Collection<Instruction> insts = edef.getInstructions();
								for(Instruction inst : insts)
									addInstruction(new InstructionAndValues(inst, provider.getValues()));								
							}
						}
					}
				}
			}
		}
		else if(!instructions.contains(provider))
			instructions.add(provider);
	}
	public String getName()
	{
		if(name == null)
			return(StringHelper.javaName(declaration.getName()));
		else
			return(name);
	}
	public void setDeclaration(ClassSectionDeclaration declaration)
	{
		this.declaration = declaration;
	}
	public ClassSectionDeclaration getDeclaration()
	{
		return(declaration);
	}
	public void sort()
	{
		instructions.sort();
	}

}
