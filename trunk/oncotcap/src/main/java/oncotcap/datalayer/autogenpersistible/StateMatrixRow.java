package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class StateMatrixRow extends AutoGenPersistibleWithKeywords 
 {
	private DefaultPersistibleList rowColumns;
	private String instanceUsabilityStatus;
	private Integer versionNumber;
	private Boolean isHeading;
	private Boolean isDefaultRow;


public StateMatrixRow() {
init();
}


public StateMatrixRow(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setRowColumns", DefaultPersistibleList.class);
	setMethodMap.put("rowColumns", setter);
	getter = getGetter("getRowColumns");
	getMethodMap.put("rowColumns", getter);
	setter = getSetter("setInstanceUsabilityStatus", String.class);
	setMethodMap.put("instanceUsabilityStatus", setter);
	getter = getGetter("getInstanceUsabilityStatus");
	getMethodMap.put("instanceUsabilityStatus", getter);
	setter = getSetter("setVersionNumber", Integer.class);
	setMethodMap.put("versionNumber", setter);
	getter = getGetter("getVersionNumber");
	getMethodMap.put("versionNumber", getter);
	setter = getSetter("setIsHeading", Boolean.class);
	setMethodMap.put("isHeading", setter);
	getter = getGetter("getIsHeading");
	getMethodMap.put("isHeading", getter);
	setter = getSetter("setIsDefaultRow", Boolean.class);
	setMethodMap.put("isDefaultRow", setter);
	getter = getGetter("getIsDefaultRow");
	getMethodMap.put("isDefaultRow", getter);
}

	public DefaultPersistibleList getRowColumns(){
		return rowColumns;
	}
	public Object getRowColumn(int indx){
			if ( rowColumns != null ) 
					return rowColumns.elementAt(indx);
			return null;
	}
	public Boolean getIsHeading(){
			if ( isHeading == null ) 
					return false;
		return isHeading;
	}
	public String getInstanceUsabilityStatus(){
		return instanceUsabilityStatus;
	}
	public Integer getVersionNumber(){
		return versionNumber;
	}
	public void setRowColumns(java.util.Collection  var ){
		if ( rowColumns== null)
			rowColumns = new DefaultPersistibleList();
		rowColumns.set(var);
		//System.out.println("setRowColumns in smr " +  rowColumns) ;
	}

	public void setInstanceUsabilityStatus(String var ){
		instanceUsabilityStatus = var;
	}

	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}
	public void setIsHeading(Boolean var ){
		isHeading = var;
	}
	public int getNumberOfColumns() {
		if ( rowColumns != null)
			return rowColumns.size();
		return 0;
	}

	public String toString() {
			if ( rowColumns != null )
					return rowColumns.toString() + " : " + getIsHeading();
		return getGUID().toString();
	}
	public Class getPanelClass()
	{
		return StateMatrixRowPanel.class;
	}
	public String getPrettyName()
	{
		return "StateMatrixRow";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
	
	public Object clone() {
		StateMatrixRow newInstance  = null;
		try {
				newInstance = (StateMatrixRow)getClass().newInstance();
		} catch ( Exception e) {
				System.out.println("Problem cloning: " + getClass());
				e.printStackTrace();
		}
		DefaultPersistibleList rowColumns = getRowColumns();
		DefaultPersistibleList clonedRowColumns = new DefaultPersistibleList();
		for ( Object obj: rowColumns ){
			clonedRowColumns.add(obj);
		}
		newInstance.setRowColumns(clonedRowColumns);
		newInstance.setIsHeading(getIsHeading());
		
		return newInstance;
	}


	/**
	 * @return Returns the isDefaultRow.
	 */
	public Boolean getIsDefaultRow() {
		if ( isDefaultRow == null )
			return false;
		return isDefaultRow;
	}


	/**
	 * @param isDefaultRow The isDefaultRow to set.
	 */
	public void setIsDefaultRow(Boolean isDefaultRow) {
		this.isDefaultRow = isDefaultRow;
	}
}
