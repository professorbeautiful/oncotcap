package oncotcap.datalayer.persistible.parameter;

import java.lang.Object;
import java.util.*;

import javax.swing.ImageIcon;

import oncotcap.datalayer.DefaultPersistibleList;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.engine.*;
import oncotcap.util.CollectionHelper;
import oncotcap.util.StringHelper;

public class SubsetParameter extends DeclareEnumPicker
{
	public DeclareEnumPicker basePicker;
	public SubsetFilter subsetFilter = null;
	private SubsetParameter me;
	public SubsetParameter(oncotcap.util.GUID guid)
	{
		super(guid);
		me = this;
	}
	public SubsetParameter()
	{
		this(true);
	}
	public SubsetParameter(DeclareEnumPicker picker, boolean saveToDataSource)
	{
		super(saveToDataSource);
//			System.out.println(" instantiate subset parameter  from " + 
//												 picker.getPickerType());
		me = this;
		copy(picker);
	}
	public SubsetParameter(boolean saveToDataSource)
	{
		super(saveToDataSource);
		me = this;
	}
	private void copy(DeclareEnumPicker picker)
	{
			if ( picker == null ) {
					System.out.println("ERROR: Unable to copy null DeclareEnumPicker.");
					return;
			}
		basePicker = picker;
		if(picker.getDisplayName() != null)
				setDisplayName(new String(picker.getDisplayName()));
		setPickerType(picker.getPickerType());
		setAllowMultiples(picker.getAllowMultiples());
		subsetFilter = new SubsetFilter();
		
		//setKeyword(picker.getAttributeBaseKeyword());
	}
	public DeclareEnumPicker getBasePicker() {
			return basePicker;
	}

	public void setBasePicker(DeclareEnumPicker base) {
			basePicker = base;
	}
	public Keyword getAttributeBaseKeyword()
	{
		return(basePicker.getAttributeBaseKeyword());
	}

	private static final Vector blankVector = new Vector();
	public Vector getInstantiationEnums()
	{
		Object obj;
		if(getPickerType() != INSTANTIATION || subsetFilter == null)
			return(blankVector);
		else
		{
			Vector rVec = new Vector();
			Iterator it = subsetFilter.getRootNode().getChildren();
			while(it.hasNext())
			{
				obj = it.next();
				if(obj instanceof OncTreeNode)
					obj = ((OncTreeNode)obj).getUserObject();
					
				if(obj instanceof BooleanExpression)
					rVec.add(obj);
			}
			return(rVec);
		}
	}
	public Vector getAssignmentEnums()
	{
		Object obj;
		if(getPickerType() != ASSIGNMENT || subsetFilter == null)
			return(blankVector);
		else
		{
			Vector rVec = new Vector();
			Iterator it = subsetFilter.getRootNode().getChildren();
			while(it.hasNext())
			{
				obj = it.next();
				if(obj instanceof OncTreeNode)
					obj = ((OncTreeNode)obj).getUserObject();
					
				if(obj instanceof BooleanExpression)
					rVec.add(obj);
			}
			return(rVec);
		}		
	}
	public String getAssignmentValues()
	{
		if(getPickerType() == ASSIGNMENT)
		{
			String vals = "";
			Iterator it = getAssignmentEnums().iterator();
			boolean firstEnum = true;
			while(it.hasNext())
			{
				BooleanExpression expr = (BooleanExpression) it.next();
				EnumDefinition enumDef = (EnumDefinition) expr.getLeftHandSide();
				EnumLevel enumLevel = (EnumLevel) expr.getRightHandSide();
				
				if(!firstEnum)
					vals = vals + ", ";
				else
					firstEnum = false;
								
				vals = vals + StringHelper.javaNameKeepBQuotes(enumDef.getProcess().getName()) +
				              "." + StringHelper.javaNameKeepBQuotes(enumDef.getName()) +
                              "." + StringHelper.javaNameKeepBQuotes(enumLevel.getName());
				
			}
			return(vals);
		}
		else
			return("");
	}
	// Override to get variable defintions from the SubsetFilter
	public DefaultPersistibleList getVariableDefinitions(){
			DefaultPersistibleList varDefns = new DefaultPersistibleList();
			// retrieve all the enum definitions from the subsetFilter tree
			
			if ( subsetFilter != null && subsetFilter.getRootNode() != null ) {
					for (Enumeration e =
									 subsetFilter.getRootNode().depthFirstEnumeration();
							 e.hasMoreElements(); ) {
							OncTreeNode n = (OncTreeNode)e.nextElement();	
							if ( n.getUserObject() instanceof BooleanExpression) {
									Object lhs = 
											((BooleanExpression)n.getUserObject()).getLeftHandSide();
									if ( lhs instanceof EnumDefinition)
									{	
										if(basePicker.pickerType == DeclareEnumPicker.INSTANTIATION)
											((EnumDefinition)lhs).setIsID(true);
										varDefns.addElement(lhs);
									}
							}
					}
			}
			return varDefns;
	}
	public Object clone()
	{
			return(clone(true));		 
	}
	public Parameter clone(boolean saveToDataSource)
	{
		SubsetParameter rParam = new SubsetParameter(saveToDataSource);
		
		rParam.basePicker = this.basePicker;
		rParam.setOriginalSibling(getOriginalSibling());
		if(getDisplayName() != null)
				rParam.setDisplayName(new String(getDisplayName()));
		rParam.setPickerType(getPickerType());
		rParam.setAllowMultiples(getAllowMultiples());
		if(subsetFilter != null)
			rParam.subsetFilter = (SubsetFilter) subsetFilter.clone(saveToDataSource);
		return(rParam);		 
	}
	public ParameterType getParameterType()
	{
		return(null);
	}

	public EditorPanel getEditorPanel()
	{
		return(getEditor());
	}
	public EditorPanel getEditorPanelWithInstance()
	{
		EditorPanel ep = getEditor();
		ep.edit(this);
		return(ep);
	}
	private EditorPanel getEditor() {
			if ( getPickerType() == DeclareEnumPicker.ASSIGNMENT ) {
					return new AssignmentParameterEditor();
			}
			else if ( getPickerType() == DeclareEnumPicker.INSTANTIATION ) {
					return new InstantiationParameterEditor();
			}
			else
					return new SubsetParameterEditorPanel();	
	}
	public String getCodeValue()
	{
		return(subsetFilter.getCodeValue());
	}
	public String getDisplayValue()
	{
		if(subsetFilter != null)
			return(subsetFilter.getDisplayName());
		else
			return("");
	}
	public int getPickerType()
	{
		return(basePicker.getPickerType());
	}
	public ImageIcon getIcon() {
		return oncotcap.util.OncoTcapIcons.getImageIcon("subsetparameter.jpg");	
	}

		public String getClassName() {
				if ( getPickerType() == DeclareEnumPicker.ASSIGNMENT )
						return "AssignmentParameter";
				else if ( getPickerType() == DeclareEnumPicker.INSTANTIATION )
						return "Instantiation";
				else
						return "SubsetParameter";	
		}
		
		public Collection<VariableDeclaration> getVariableDeclarations()
		{
			Vector<VariableDeclaration> decls = new Vector<VariableDeclaration>();
			EnumDefinition en;
			Iterator it = getVariableDefinitions().iterator();
			while(it.hasNext())
			{
				Object nxt = it.next();
				if(nxt instanceof EnumDefinition)
				{
					en = (EnumDefinition) nxt;
					decls.add(new VariableDeclaration(null,
							                          en.getName(),
							                          "OncEnum",
							                          en.getLevelList()));
				}
			}
			return(decls);
		}
		public Collection<ProcessDeclaration> getReferencedProcessDeclarations()
		{
			Vector<ProcessDeclaration> decls = new Vector<ProcessDeclaration>();
			EnumDefinition en;
			Iterator it = getVariableDefinitions().iterator();
			while(it.hasNext())
			{
				Object nxt = it.next();
				if(nxt instanceof EnumDefinition)
				{
					en = (EnumDefinition) nxt;
					ProcessDeclaration pDec = en.getProcessDeclaration();
					if(pDec != null)
						decls.add(pDec);
				}
			}
			return(decls);
		}
		public Collection<InstructionProvider> getAdditionalProviders()
		{
			return(getVariableDefinitions());
		}
		public Collection<Instruction> getInstructions()
		{
			return new Vector<Instruction>();
		}
}