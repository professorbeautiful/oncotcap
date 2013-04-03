package oncotcap.datalayer.persistible;

import java.util.Enumeration;
import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.display.common.*;
import oncotcap.util.StringHelper;
import oncotcap.util.GUID;
import oncotcap.Oncotcap;


public class SubsetFilter extends OncFilter
{
	public TcapString rootFilterObj = 
			(TcapString)Oncotcap.getDataSource().find
			(new GUID("888e669800000000000000fc3b655f5d")); //SUBSET
	private int displayValueFlavor = DeclareEnumPicker.BOOLEAN_EXPRESSION;

	private String initVarName = null;
	
	public SubsetFilter(oncotcap.util.GUID guid){
		super(guid);
	}
	
	public SubsetFilter()
	{

		this(true);
	}
	public SubsetFilter(boolean saveToDataSource)
	{
		super(saveToDataSource);
		if(!saveToDataSource)
			rootNode = new OncTreeNode(rootFilterObj,Persistible.DO_NOT_SAVE);
	}
	public SubsetFilter(String filterName)
	{
		this(filterName, true);
	}
	public SubsetFilter(String filterName, boolean saveToDataSource)
	{
		super(saveToDataSource);
		rootFilterObj = new TcapString(filterName, saveToDataSource);
		rootNode = new OncTreeNode(rootFilterObj);		
	}
	public Object clone()
	{
		return(clone(true));
	}
	public OncFilter clone(boolean saveToDataSource)
	{
		SubsetFilter sf = new SubsetFilter(saveToDataSource);
		sf.rootNode = (OncTreeNode) rootNode.clone(saveToDataSource);
		sf.keywordsOnly = keywordsOnly;
		sf.displayValueFlavor = displayValueFlavor;
		return(sf);		
	}
		public int getDisplayValueFlavor() {
				return displayValueFlavor;
		}
		public void setDisplayValueFlavor(int flavor) {
				displayValueFlavor = flavor;
				update();
		}

		public void setRootNode(OncTreeNode rootNode) {
				if ( rootNode != null ) 
						rootNode.update();
				super.setRootNode(rootNode);
		}

		public String getDisplayName() {
				// 	System.out.println("getDisplayValueFlavor in getDisplayName "  
				// 													 + getDisplayValueFlavor());
				if ( getDisplayValueFlavor() == 
						 DeclareEnumPicker.BOOLEAN_EXPRESSION) {
						// Use the filter to construct a name
						//System.out.println( "Code string " + getBooleanValue().toString());
						return super.addToName().toString();
				}
				else if (getDisplayValueFlavor() == 
								 DeclareEnumPicker.ASSIGNMENT) {
						return super.addToName().toString();
				}
				else {
						return  super.addToName().toString();
				}
		}
		public String getCodeValue() {
				if ( getDisplayValueFlavor() == DeclareEnumPicker.BOOLEAN_EXPRESSION)
				{
						// Use the filter to construct a name
						return(getBooleanValue());
				}
				else if (getDisplayValueFlavor() == DeclareEnumPicker.ASSIGNMENT)
				{
						return(getAssignmentValue());
				}
				else if (getDisplayValueFlavor() == DeclareEnumPicker.INSTANTIATION)
				{
						return(getInitializationValue());
				}
				else
					return("");
		}
		private String getInitializationVariableName()
		{
			if(initVarName == null)
				initVarName = StringHelper.getUniqueVariableName();

			return(initVarName);
		}
		public String getInitializationValue()
		{
			return(getInitializationVariableName());
		}
		public String getAssignmentValue()
		{
			return("ENUM ASSIGNMENT HERE");
		}
		public String getBooleanValue() {
				// Use the filter to construct a name
				return addToValue().toString();
		}
		public StringBuffer addToValue() {
				// Visit each node 
				return addToValue(rootNode, new StringBuffer(), " ");
		}

		public StringBuffer addToValue(OncTreeNode node, StringBuffer nameString, 
													 String separator) {
				if ( node.getUserObject() instanceof TcapLogicalOperator ) {
						nameString.append("(");
						if ( node.getUserObject().equals(TcapLogicalOperator.AND) ) 
								separator = " && ";
						else if  ( node.getUserObject().equals(TcapLogicalOperator.OR) ) 
								separator = " || ";
						else if ( node.getUserObject().equals(TcapLogicalOperator.NOT) ) 
								separator = " !";
 
						// 	separator = " " 
						//+ ((TcapLogicalOperator)node.getUserObject()).getJavaCodeString() + " ";
						if ( node.getUserObject() == TcapLogicalOperator.NOT)
								nameString.append(separator);
				}
				else {
						if ( node.getUserObject() instanceof BooleanExpression ) {
								nameString.append("(");
								BooleanExpression bool = 
										(BooleanExpression)node.getUserObject();
								String name = "undefined";
								String process = "undefined";
								EnumDefinition lhs = null;
								if ( bool.getLeftHandSide() != null 
										 &&  bool.getLeftHandSide() instanceof EnumDefinition ) {
										lhs = 
												(EnumDefinition) bool.getLeftHandSide();
										// Build var name 
										if ( lhs.getProcess() != null) {
												nameString.append("((");
												process = 
														StringHelper.javaNameKeepBQuotes(lhs.getProcess().toString());
												nameString.append(process);
												nameString.append(") getContainerInstance(");
												nameString.append(process);
												nameString.append(".class))");
										}
										nameString.append(".");
										name = StringHelper.variableNameKeepBackQuotes(lhs.getName());
										nameString.append(name);
								}
								if ( displayValueFlavor == DeclareEnumPicker.ASSIGNMENT 
										 || displayValueFlavor == DeclareEnumPicker.INSTANTIATION ) 
										nameString.append("=");
								else if ( displayValueFlavor == DeclareEnumPicker.BOOLEAN_EXPRESSION ) {
										if (bool.getOperator() != null ){
												if (bool.getOperator().toString().equals("="))
														nameString.append("==");
												else 
														nameString.append(bool.getOperator().toString());
										}
								}
								
								if ( bool.getRightHandSide() != null 
										 &&  bool.getRightHandSide() instanceof EnumLevel ) {
										EnumLevel rhs = 
												(EnumLevel) bool.getRightHandSide();
										// Build var name 
										nameString.append(process);
										nameString.append(".");
										name = StringHelper.javaNameKeepBQuotes(lhs.getName());
										nameString.append(name);
										nameString.append(".");
										nameString.append(StringHelper.javaNameKeepBQuotes(rhs.toString()));
										nameString.append(")");

								}
						}
				}
        if (node.getChildCount() >= 0) {
            for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                OncTreeNode n = (OncTreeNode)e.nextElement();
                addToValue(n, nameString, separator);
								if ( e.hasMoreElements() ) 
										nameString.append(separator);
            }
        }
				if ( node.getUserObject() instanceof TcapLogicalOperator ) {
						nameString.append(")");
				}
				return nameString;
    }

}

