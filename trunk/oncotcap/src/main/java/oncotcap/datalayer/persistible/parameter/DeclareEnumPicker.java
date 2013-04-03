package oncotcap.datalayer.persistible.parameter;

import oncotcap.datalayer.persistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;

public class DeclareEnumPicker extends AbstractIndividualParameter
{

  public static final int BOOLEAN_EXPRESSION = 0;
  public static final int ASSIGNMENT = 1;
	public static final int INSTANTIATION = 2;

	protected boolean allowMultiples = true;
	protected int pickerType = BOOLEAN_EXPRESSION;	
	private String name;
	private Keyword attributeBaseKeyword;

	protected EnumSubsetFilter attributeFilter = null;
	
	public DeclareEnumPicker(oncotcap.util.GUID guid){
		super(guid);
	}
	
	public DeclareEnumPicker()
	{
		this(true);	
	}
	public DeclareEnumPicker(boolean saveToDataSource)
	{
		super(saveToDataSource);
	}
	public void setName(String name)
	{
		this.name = name;
		update();
	}
	public String getName()
	{
		return(name);
	}
	public String getValue()
	{
		return(getStringValue());
	}
	public String getCodeValue()
	{
		return(getValue());
	}
	public String getDisplayValue()
	{
		return(getValue());
	}
	public void setDisplayValue(Object val)
	{
		if(val instanceof String)
			setName((String) val);
		else if(val instanceof Keyword)
			setName(((Keyword) val).getKeyword());
	}
	public EnumSubsetFilter getAttributeFilter()
	{
		if(attributeFilter == null)
		{
			if(attributeBaseKeyword == null)
				attributeFilter = new EnumSubsetFilter(KeywordFilter.CHARACTERISTIC_ROOT);
			else
				attributeFilter = new EnumSubsetFilter(attributeBaseKeyword);
		}
		return(attributeFilter);
	}
	public void setAttributeFilter(EnumSubsetFilter filter)
	{
		this.attributeFilter = filter;
		update();
	}
	public Keyword getAttributeBaseKeyword()
	{
		return(attributeBaseKeyword);
	}
  public boolean getAllowMultiples() {
			return allowMultiples;
  }
	public int getPickerType() {
			return pickerType;
	}
  public void setAllowMultiples(boolean multiples) {
			allowMultiples = multiples;
			update();
  }
	public void setPickerType(int type) {
	// 		System.out.println("setting picker type " + this.getClass()
// 												 + " type " + type);
			pickerType = type;
			update();
	}

	public void setAttributeBaseKeyword(Keyword word)
	{
		attributeBaseKeyword = word;
		update();
	}
	public Object clone()
	{
		return(clone(true));
	}
	public Parameter clone(boolean saveToDataSource)
	{
		DeclareEnumPicker picker = new DeclareEnumPicker(saveToDataSource);
		picker.setOriginalSibling(getOriginalSibling());
		if(name != null)
			picker.setName(new String(name));
//		picker.setFilter((KeywordFilter) filter.clone());
		picker.setAttributeBaseKeyword(attributeBaseKeyword);
		picker.setPickerType(pickerType);
		picker.setAllowMultiples(allowMultiples);
		return(picker);		
	}
	public void initSingleParams()
	{
		setDefaultName("Characteristic");
		setSingleParameterID("DeclareEnumPicker.Name");

		singleParameterList.add(this);
	}

	public ParameterType getParameterType()
	{
		return(ParameterType.ENUM_PICKER);
	}
	public String getStringValue()
	{
		if(name == null)
			return("");
		else
			return(name);
	}
	public EditorPanel getEditorPanel()
	{
		return(new ParameterEditor());	
	}
	public EditorPanel getEditorPanelWithInstance()
	{
		EditorPanel ep = new ParameterEditor();
		ep.edit(this);
		return(ep);
	}
	public EditorPanel getParameterEditorPanel()
	{
		return(new DeclareEnumPickerEditor());	
	}
	public EditorPanel getParameterEditorPanelWithInstance()
	{
		EditorPanel ep = new DeclareEnumPickerEditor();
		ep.edit(this);
		return(ep);
	}
/*	private void writeIDVarSection(Writer fw, StatementBundle sb) throws IOException {}
	public void writeMethodSection(Writer write, StatementBundle sb) throws IOException {}
	public void writeDeclarationSection(Writer fw, StatementBundle sb) throws IOException
	{
		writeIDVarSection(fw, sb);
	}
	public VariableType getType()
	{
		return(null);
	} */
	public boolean check()
	{
		return(true);
	}
	public String toString() {
		return getName();
	}
}
