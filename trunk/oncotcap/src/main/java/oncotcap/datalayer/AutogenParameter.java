package oncotcap.datalayer;  // REPACKAGE THIS

import java.util.*;

import oncotcap.datalayer.persistible.parameter.SingleParameter;
import oncotcap.display.common.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.Parameter;
import oncotcap.engine.Instruction;
import oncotcap.engine.InstructionProvider;
import oncotcap.util.CollectionHelper;

import java.awt.Color;


abstract public class AutogenParameter 
		extends AutoGenPersistibleWithKeywords 
		implements Parameter{
		
		//protected Hashtable setMethodMap = new Hashtable();
		//protected Hashtable getMethodMap = new Hashtable();
		//public ImageIcon icon =	null;

		public String name;
		public SingleParameterList singleParameterList = new SingleParameterList();
		public DefaultPersistibleList variableDefinitions;
		private Color backgroundColor = Color.white;
		private Parameter originalSibling = null;

		public AutogenParameter(){}
		public AutogenParameter(oncotcap.util.GUID guid){
				super(guid);
		}

		abstract public Class getPanelClass();
		
		// Implement Parameter
		public Object clone()
		{
				return(super.clone());
		}
		public Parameter clone(boolean saveToDataSource)
		{
				// DeclarePositive rVal = new DeclarePositive(this, saveToDataSource);
// 				rVal.setOriginalSibling(getOriginalSibling());
// 				return(rVal);
				return null;
		}
		public void setOriginalSibling(Parameter param)
		{
				originalSibling = param;
		}
		public Parameter getOriginalSibling()
		{
				if(originalSibling == null)
						return(this);
				else
						return(originalSibling);
		}
		public SingleParameter getMatchingSingleParameter(SingleParameter sp)
		{
				return(singleParameterList.getSingleParameter(sp.getSingleParameterID()));
		}
		public SingleParameter getSingleParameter(String singleParameterID)
		{
				return(singleParameterList.getSingleParameter(singleParameterID));
		}
		public String getName()
		{
				return(name);
		}
		public void setName(String name)
		{
				this.name = name;
				update();
		}
		public abstract ParameterType getParameterType();
		public abstract EditorPanel getParameterEditorPanel();

		public String getTypeName()
		{
				return(getParameterType().toString());
		}
		
		public DefaultPersistibleList getVariableDefinitions()
		{
				return variableDefinitions;
		}
		public void setVariableDefinitions(java.util.Collection  var ){
				if ( variableDefinitions== null)
						variableDefinitions = new DefaultPersistibleList();
				variableDefinitions.set(var);
		}
		public void addVariableDefinition(VariableDefinition varDef)
		{
				variableDefinitions.add(varDef);
		}
		public Parameter newStorageObject()
		{
				return(getParameterType().newStorageObject());
		}
		public SingleParameterList getSingleParameterList()
		{
				return(singleParameterList);
		}
		public Iterator getSingleParameters()
		{
				return(singleParameterList.getIterator());
		}
		public boolean contains(SingleParameter sp)
		{
				return(singleParameterList.contains(sp));
		}
		public void clearSingleParameters()
		{
				singleParameterList.clear();
		}
		public boolean containsSingleParameter(SingleParameter singleP)
		{
				return(singleParameterList.contains(singleP.getID()));
		}
		
		public static Parameter newParameter()
		{
				ParameterType param = (ParameterType) ListDialog.getValue("Choose a Parameter type.", ParameterType.getParameterTypes());
				if(param != null)
						{
								Parameter p = param.newStorageObject();
								return(p);
						}
				else
						return(null);
	}
		public void addSaveListener(SaveListener handler)
		{
				super.addSaveListener(handler);
				singleParameterList.addSaveListener(handler);
		}
		public void removeSaveListener(SaveListener listener)
		{
				super.removeSaveListener(listener);
				
				//this condition needed in the case where a parameter is also a
				//single parameter (TcapFloat, TcapInteger, etc.)  Otherwise we
				//get into an infinite loop.
				if(! (this instanceof SingleParameter))
						singleParameterList.removeSaveListener(listener);
		}
		public void setBackground(Color c)
		{
				if(c != null)
						backgroundColor = c;
		}
	public Color getBackground()
		{
				return(backgroundColor);
		}
	public Collection<InstructionProvider> getAdditionalProviders()
	{
		return(new Vector<InstructionProvider>());
	}
	public Collection<Instruction> getInstructions()
	{
		return(new Vector<Instruction>());
	}
	public ProcessDeclaration getProcessDeclaration()
	{
		return(null);
	}
	public Collection<ProcessDeclaration> getReferencedProcesses()
	{
		return(new Vector<ProcessDeclaration>());
	}
}
