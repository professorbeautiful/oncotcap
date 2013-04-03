package oncotcap.datalayer.persistible.action;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.autogenpersistible.Interpretation;
import oncotcap.datalayer.autogenpersistible.QuantitativeInterpretation;
import oncotcap.datalayer.persistible.*;
import oncotcap.engine.*;
import oncotcap.engine.java.*;
import oncotcap.util.Logger;

public class AddGenericCode extends DefaultOncAction
{
		private String genericCode = null;
		private boolean addCodeInsideMethod = true;
		
		private AddGenericCode thisAction;
		
		public AddGenericCode(oncotcap.util.GUID guid){
				super(guid);
				thisAction = this;
		}
		public AddGenericCode() { thisAction = this;}
		public AddGenericCode(PrimitiveData storage) { thisAction = this;}
		
		public String getDisplayString()
		{
				return toString();
		}
		public String getName() {
			return getDisplayString();
		}
		public EditorPanel getEditorPanel()
		{
				return new GenericCodeActionEditor();
		}
		public EditorPanel getEditorPanelWithInstance()
		{
				EditorPanel ed = (EditorPanel) getEditor();
				ed.edit(this);
				return(ed);
		}
		
		public void setGenericCode(String str) {
			genericCode = str;
		}
		public String getGenericCode()
		{
			return(genericCode + "\n");
		}
		public void setAddCodeInsideMethod(boolean bool)
		{
			addCodeInsideMethod = bool;
		}
		public boolean getAddCodeInsideMethod()
		{
			return addCodeInsideMethod;
		}
		public boolean check()
		{
				return (true);
		}
		
		public String toString()
		{
			return ("Add generic code: " + genericCode );
		}
		
		private Vector<Instruction> instructions = null;
		public Collection<Instruction> getInstructions()
		{
			if(instructions == null)
			{
				instructions = new Vector<Instruction>();
				instructions.add(new GenericCodeInstruction());
			}
			return(instructions);
		}
		public class GenericCodeInstruction implements Instruction
		{
			Collection<String> setVariables = null;
			public Collection<String> getSetVariables(ValueMapPath path)
			{
				//if (setVariables == null)  BAD IDEA!! That's why so many CD68's.
					setVariables = JavaParser.getSetVariables(path.substitute(genericCode));
				return setVariables;
			}
			Collection<String> allVariables = null;
			public Collection<String> getAllVariables(ValueMapPath path)
			{
				if (allVariables == null) {
					allVariables = JavaParser.getAllVariables(path.substitute(genericCode));
				}
				Collection<String> ifVars = thisAction.getCodeBundleContainingMe().getIfClauseVariables(path);
				allVariables.addAll(ifVars);
				return(allVariables);
			}
			Collection<VariableDependency> variableDependencies = null;
			public Collection<VariableDependency> getVariableDependencies(ValueMapPath path)
			{
				//if (variableDependencies == null) {
					variableDependencies = JavaParser.getDependencies(path.substitute(genericCode));
				//}
				for(VariableDependency vardep : variableDependencies){
					for(String s: thisAction.getIfClauseVariables(path)){
						variableDependencies.add(
								new VariableDependency(
										vardep.getLeftVariableName(), s, vardep.getOperatorType())); 
					}
				}
				return(variableDependencies);
			}
			public ClassSectionDeclaration getSectionDeclaration()
			{
				if(getAddCodeInsideMethod())
					return(getCodeBundleContainingMe().getSectionDeclaration());
				else
					return(ClassSectionDeclaration.DECLARATION_SECTION);
			}
			public InstructionProvider getEnclosingInstructionProvider()
			{
				return(thisAction);
			}
		}
}
