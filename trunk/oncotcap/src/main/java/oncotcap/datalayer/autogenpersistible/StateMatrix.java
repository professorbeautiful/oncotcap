package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;
import java.util.Iterator;
import java.util.Vector;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.persistibleeditorpanel.*;


public class StateMatrix extends AutoGenPersistibleWithKeywords 
 {
	private String instanceUsabilityStatus;
	private Integer versionNumber;
	private DefaultPersistibleList stateMatrixRows;
  private ConditionalTableParameter conditionalTableParameter;
		 

public StateMatrix() {
init();
}


public StateMatrix(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setInstanceUsabilityStatus", String.class);
	setMethodMap.put("instanceUsabilityStatus", setter);
	getter = getGetter("getInstanceUsabilityStatus");
	getMethodMap.put("instanceUsabilityStatus", getter);
	setter = getSetter("setVersionNumber", Integer.class);
	setMethodMap.put("versionNumber", setter);
	getter = getGetter("getVersionNumber");
	getMethodMap.put("versionNumber", getter);

	setter = getSetter("setStateMatrixRows", DefaultPersistibleList.class);
	setMethodMap.put("stateMatrixRows", setter);
	getter = getGetter("getStateMatrixRows");
	getMethodMap.put("stateMatrixRows", getter);


	setter = 
			getSetter("setConditionalTableParameter", 
								ConditionalTableParameter.class);
	setMethodMap.put("conditionalTableParameter", setter);
	getter = getGetter("getConditionalTableParameter");
	getMethodMap.put("conditionalTableParameter", getter);
}

	public String getInstanceUsabilityStatus(){
		return instanceUsabilityStatus;
	}
	public Integer getVersionNumber(){
		return versionNumber;
	}
	public DefaultPersistibleList getStateMatrixRows(){
		return stateMatrixRows;
	}
	public ConditionalTableParameter getConditionalTableParameter(){
		return conditionalTableParameter;
	}
	public void setInstanceUsabilityStatus(String var ){
		instanceUsabilityStatus = var;
	}

	public void setVersionNumber(Integer var ){
		versionNumber = var;
	}

	public void setStateMatrixRows(java.util.Collection  var ){
		if ( stateMatrixRows== null)
			stateMatrixRows = new DefaultPersistibleList();
		stateMatrixRows.set(var);
		//System.out.println("setStateMatrix in sm " + stateMatrixRows ) ;

	}
	public void setConditionalTableParameter(ConditionalTableParameter var ){
		conditionalTableParameter = var;
	}

	public int getNumberRows() {
		if ( stateMatrixRows != null)
			return stateMatrixRows.size();
		return 0;
	}

	public String toString() {
			Iterator i = getStateMatrixRows().iterator();
			StringBuilder str = new StringBuilder();
			while ( i.hasNext() ) {
					str.append(i.next().toString());
					str.append(" , ");
			}
			return str.toString();
	}
	public Class getPanelClass()
	{
		return StateMatrixPanel.class;
	}
	public String getPrettyName()
	{
		return "StateMatrix";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
		 
		 // COnvenience Methods
		 public Vector getMatrixHeading() {
				 // Loop thru the state matrix rows and find the one where 
				 // Separate the table headings from the rest of the data
				 StateMatrixRow smr = null;
				 Iterator i = getStateMatrixRows().iterator();
				 while ( i.hasNext() ) {
						 smr = (StateMatrixRow)i.next();
						 if ( smr.getIsHeading() == true)
								 return (Vector)smr.getRowColumns();
				 }
				 return null;
				 
		 }
		 public StateMatrixRow  getStateMatrixRowHeading() {
				 // Loop thru the state matrix rows and find the one where 
				 // Separate the table headings from the rest of the data
				 StateMatrixRow smr = null;
				 if ( getStateMatrixRows() != null ) {
						 Iterator i = getStateMatrixRows().iterator();
						 while ( i.hasNext() ) {
								 smr = (StateMatrixRow)i.next();
								 if ( smr.getIsHeading() == true)
										 return smr;
						 }
				 }
				 return smr;
				 
		 }
		 public Vector getDataAsVectorOfVectors() {
				 if ( stateMatrixRows == null ) 
						 return null;
				 Vector vOfV = new Vector();
				 StateMatrixRow smr = null;
				 Iterator i = getStateMatrixRows().iterator();
				 while ( i.hasNext() ) {
						 smr = (StateMatrixRow)i.next();
						 if ( smr.getIsHeading() == false) {
								 // Clone this vector
								 Vector rowColumns = (Vector)smr.getRowColumns();
								 vOfV.add((Vector)rowColumns.clone());
						 }
				 }
				 return vOfV;
		 }

		 public Array[][] getMatrixAs2DArray() {
				 Vector vOfV = getDataAsVectorOfVectors();
				 return null;
		 }
}
