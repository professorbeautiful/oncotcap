package oncotcap.datalayer.persistible.action;

import java.util.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.AbstractPersistible;
import oncotcap.datalayer.persistible.*;
import oncotcap.util.*;
import oncotcap.display.editor.persistibleeditorpanel.ActionEditor;
import oncotcap.display.editor.persistibleeditorpanel.Editable;
import oncotcap.engine.*;
import oncotcap.engine.java.JavaParser;


public abstract class DefaultOncAction extends AbstractPersistible implements OncAction, PrimitiveData, TreeViewable, Editable, InstructionProvider
{
	private CodeBundle codeBundleContainingMe = null;
	private StatementBundle statementBundleUsingMe = null;
	protected DefaultOncAction me;
	public DefaultOncAction()
	{
		me = this;
	}
	public DefaultOncAction(oncotcap.util.GUID guid){
		super(guid);
		me = this;
	}
	public ActionEditor getEditor()
	{
		return (getType().getEditor());
	}
	public ActionType getType()
	{
		return (ActionType.actionToType((OncAction) this));
	}
	public String getDisplayString()
	{
		return(toString());
	}

//	public String getCode()
//	{
//		StringWriter w = new StringWriter();
//		try
//		{
//			writeDeclarationSection(w);
//			w.write("\n\n");
//			writeMethodSection(w);
//		}
//		catch(IOException e){Logger.log("Warning: unable to write code for Action:\n" + toString());}
//		return(w.toString());
//	}

/*	public String getDeclarationCode()
	{
		StringWriter w = new StringWriter();
		try
		{
			writeDeclarationSection(w);
		}
		catch(IOException e){Logger.log("Warning: unable to write declaration code for Action:\n" + toString());}
		return(w.toString());
	}
	public String getMethodCode()
	{
		StringWriter w = new StringWriter();
		try
		{
			writeMethodSection(w);
		}
		catch(IOException e){Logger.log("Warning: unable to write method code for Action:\n" + toString());}
		return(w.toString());
	}*/
	public CodeBundle getCodeBundleContainingMe()
	{
		return(codeBundleContainingMe);
	}
	public void setCodeBundleContainingMe(CodeBundle bundle)
	{
		codeBundleContainingMe = bundle;
	}

		public Object clone() {
				// Create the copy -- do i need a guid here
				Object newInstance  = null;
				try {
						newInstance = getClass().newInstance();
				} catch ( Exception e) {
						e.printStackTrace();
				}
				OncoTCapDataSource ds = getDataSource();
				Vector attributeMaps  = ds.getDataSourceInfo().mapForJavaClass(getClass().getName());
				System.out.println("attributeMaps: " + attributeMaps.firstElement() +
													 " " + attributeMaps.size());
				DataSourceObject dsObject = 
						(DataSourceObject)attributeMaps.firstElement();
				// For every attribute that gets stored in the ds 
				//set the values from the original
				for (Enumeration e = dsObject.attributeAccessMaps.elements();
						 e.hasMoreElements(); ) {
						AttributeAccessMap attribute =
								(AttributeAccessMap)e.nextElement();
						// Do not copy codeBundlesContainingMe
						if ( "codeBundleContainingMe".equals(attribute.dsAttributeName))
								continue;
						Object originalValue = 
								ds.getValueFromPersistible((Persistible)this,
																					 attribute);
						if ( originalValue instanceof VariableModification ) 
								originalValue = ((AbstractPersistible)originalValue).clone();
						System.out.println("DefaultOncAction.clone: originalValue " 
															 + attribute.dsAttributeName + " " 
															 + originalValue);
						ds.setValueInPersistible((Persistible)newInstance, 
																	attribute,
																	originalValue);
				}

				return newInstance;

		}
		
/*		public Object cloneSubstitute(StatementBundle sb, CodeBundle cb) {
			// Create the copy -- do i need a guid here
			DefaultOncAction newInstance  = null;
			try {
					Class boolClassArg [] = {boolean.class};
					Object boolArg [] = {new Boolean(false)};
					Constructor [] constructs = getClass().getConstructors();
					for(int n = 0; n < constructs.length; n++)
					{
						Class [] params = constructs[n].getParameterTypes();
						if(params != null && params.length == 1)
							if(params[0].equals(boolean.class))
								newInstance = (DefaultOncAction) constructs[n].newInstance(boolArg);
					}
					if(newInstance == null)
					{
						newInstance = (DefaultOncAction) getClass().newInstance();
						((AbstractPersistible) newInstance).setPersistibleState(Persistible.DO_NOT_SAVE);
					}
			} catch ( Exception e) {
					e.printStackTrace();
			}
			this.setCodeBundleContainingMe(cb);
			OncoTCapDataSource ds = getDataSource();
			Vector attributeMaps  = 
					ds.getDataSourceInfo().mapForJavaClass(getClass().getName());
		
			DataSourceObject dsObject = 
					(DataSourceObject)attributeMaps.firstElement();
			// For every attribute that gets stored in the ds 
			//set the values from the original
			for (Enumeration e = dsObject.attributeAccessMaps.elements();
					 e.hasMoreElements(); ) {
					AttributeAccessMap attribute =
							(AttributeAccessMap)e.nextElement();
					Object originalValue = 
							ds.getValueFromPersistible((Persistible)this, attribute);
					
					if ( originalValue instanceof VariableModification ) 
						originalValue = ((VariableModification)originalValue).cloneSubstitute(sb);
					if(originalValue instanceof DeclareVariable)
						originalValue = ((DeclareVariable) originalValue).cloneSubstitute(sb);
					if(originalValue instanceof String)
						originalValue = sb.substitute((String) originalValue);
					
					ds.setValueInPersistible((Persistible)newInstance, 
																attribute,
																originalValue);
			}

			return newInstance;
	}
*/		
/*		public Object cloneSubstitute(StatementBundle sb) {
			// Create the copy -- do i need a guid here
			Object newInstance  = null;
			try {
					newInstance = getClass().newInstance();
			} catch ( Exception e) {
					e.printStackTrace();
			}
			OncoTCapDataSource ds = getDataSource();
			Vector attributeMaps  = ds.getDataSourceInfo().mapForJavaClass(getClass().getName());
			System.out.println("attributeMaps: " + attributeMaps.firstElement() +
												 " " + attributeMaps.size());
			DataSourceObject dsObject = 
					(DataSourceObject)attributeMaps.firstElement();
			// For every attribute that gets stored in the ds 
			//set the values from the original
			for (Enumeration e = dsObject.attributeAccessMaps.elements();
					 e.hasMoreElements(); ) {
					AttributeAccessMap attribute =
							(AttributeAccessMap)e.nextElement();
					// Do not copy codeBundlesContainingMe
					if ( "codeBundleContainingMe".equals(attribute.dsAttributeName))
							continue;
					Object originalValue = 
							ds.getValueFromPersistible((Persistible)this,
																				 attribute);
					else if (originalValue instanceof String)
						originalValue = (Object) sb.substitute((String) originalValue);
					System.out.println("originalValue " 
														 + attribute.dsAttributeName + " " 
														 + originalValue);
					ds.setValueInPersistible((Persistible)newInstance, 
																attribute,
																originalValue);
			}

			return newInstance;

	} */
		public void setStatementBundleUsingMe(StatementBundle sb)
		{
			statementBundleUsingMe = sb;
		}
		public StatementBundle getStatementBundleUsingMe()
		{
			return(statementBundleUsingMe);
		}
		public ProcessDeclaration getProcessDeclaration()
		{
			return(getCodeBundleContainingMe().getProcessDeclaration());
		}
		public ClassSectionDeclaration getSectionDeclaration()
		{
			return(getCodeBundleContainingMe().getSectionDeclaration());
		}
		public Collection<InstructionProvider> getAdditionalProviders()
		{
			return(new Vector<InstructionProvider> ());
		}
		public Collection<ProcessDeclaration> getReferencedProcesses()
		{
			return(new Vector<ProcessDeclaration> ());
		}
		
		Collection<String> setVariables = null;
		public Collection<String> getSetVariables(ValueMapPath path){
			if(setVariables == null){
				setVariables = new Vector<String> ();
				for(Instruction instr : getInstructions())
					setVariables.addAll(instr.getSetVariables(path));
				// Above line corrected 4/27/2006 -rd.
			}
			return setVariables;
		}

		
		Collection<String> varsFromIfClause = null;
		public Collection<String> getIfClauseVariables(ValueMapPath path){
			return getCodeBundleContainingMe().getIfClauseVariables(path);
		}
		
		Collection<String> allVariables = null;
		public Collection<String> getAllVariables(ValueMapPath path){
			if(allVariables == null){
				Collection<String> allVariables = new Vector<String>();
				allVariables.addAll(getIfClauseVariables(path));
				for(Instruction instr : getInstructions())
					allVariables.addAll(instr.getAllVariables(path));
			}
			return allVariables;
		}

		Collection<VariableDependency> variableDependencies = null;
		public void addIfVarsToDependencies(Collection<VariableDependency> varDeps, ValueMapPath path){
			for(VariableDependency varDep : varDeps)
				for(String var : getIfClauseVariables(path))
					varDeps.add(
							new VariableDependency(varDep.getLeftVariableName(),
									var, VariableDependency.LITERAL_if));
		}
		public Collection<VariableDependency> getVariableDependencies(ValueMapPath path){
			if (variableDependencies == null) {
				for(Instruction instr : getInstructions())
					variableDependencies.addAll(instr.getVariableDependencies(path));
				addIfVarsToDependencies(variableDependencies, path);
			}
			return(variableDependencies);
		}
		
		public InstructionProvider getEnclosingInstructionProvider(){
			return  getCodeBundleContainingMe();
		}
		public Collection<Instruction> getInstructions(){
			return CollectionHelper.makeVector(this);
		}
}
