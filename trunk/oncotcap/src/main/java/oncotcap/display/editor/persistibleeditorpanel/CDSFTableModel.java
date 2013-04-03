package oncotcap.display.editor.persistibleeditorpanel;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.dnd.*;
import java.util.*;
import java.lang.reflect.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import java.awt.datatransfer.*;
import java.beans.PropertyVetoException;

import oncotcap.datalayer.*;
import oncotcap.util.*;
import oncotcap.display.common.*;
import oncotcap.display.browser.*;
import oncotcap.display.editor.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.datalayer.autogenpersistible.*;


public class CDSFTableModel extends AbstractTableModel {
		ConditionalDiscreteStateFunction cdsf = null;
		String firstColumnName = "Dependent";
		String lastColumnName = "Proportion";
		Vector defaultRow = new Vector();
		Boolean TRUE = new Boolean(true);		
		Boolean FALSE = new Boolean(false);		
		public CDSFTableModel(){
				System.out.println("empty constructor");
		}
		public CDSFTableModel(ConditionalDiscreteStateFunction cdsf){
				this.cdsf = cdsf;
			
		}
		private void initDefaultRow() {
				if (cdsf.getStateMatrix() != null
						&& cdsf.getStateMatrix().getMatrixHeading() != null) {
						for ( int i = 0; 
									i < cdsf.getStateMatrix().getMatrixHeading().size(); i++) {
								defaultRow.add(FALSE);
						}
				}
		}
		public ConditionalDiscreteStateFunction getCDSF() {
				return cdsf;
		}
		public void setCDSF(ConditionalDiscreteStateFunction cdsf) {
				this.cdsf = cdsf;
		}
		public void setFirstColumnName(String name ) {
				firstColumnName = name;
		}
		public void setLastColumnName(String name ) {
				lastColumnName = name;
		}
		public String getFirstColumnName() {
				return firstColumnName;
		}
		public String getLastColumnName() {
				return lastColumnName;
		}
		
		public Class<?> getColumnClass(int columnIndex) {
				if ( columnIndex == 0)
						return Boolean.class;
				if ( columnIndex == getColumnCount() - 1 ) {
						if ("Events".equals(lastColumnName) ){
								return EventDeclaration.class;
						}
						else 
								return String.class;
				}
				return String.class;
		}
		public int getColumnCount() {
				// Column count is number of columns in state matrix + 2 ( default
				// and proportion or event column)
				if ( cdsf != null && 	cdsf.getStateMatrix() != null ) {
						Vector headings = 
								cdsf.getStateMatrix().getMatrixHeading();
						if ( headings != null )
								return headings.size() + 2;
				}
				return 0;
		}
		public String getColumnName(int columnIndex) {
				if ( columnIndex < 0 || columnIndex >= getColumnCount()) {
						System.out.println("CDSFTableModel: Array index out of bounds");
						return null; // Really an error
				}
				if ( columnIndex == 0 ) 
						return firstColumnName;
				if ( columnIndex == getColumnCount()-1 ) 
						return lastColumnName;
				return getCDSFHeadingName(columnIndex);
		}
		public int getRowCount() {
				if ( cdsf != null && cdsf.getOutcomes() != null ) {
						return cdsf.getOutcomes().size();
				}
				return 0;
		}
		public Object getValueAt(int rowIndex, int columnIndex) {

				if ( columnIndex == 0 ) {
						if (  rowIndex > defaultRow.size() ) 
								return FALSE;
						else {
								initDefaultRow();
								return defaultRow.elementAt(rowIndex);
						}
				}
				if ( columnIndex < 0 || columnIndex >= getColumnCount()) {
						System.out.println("CDSFTableModel: Array index out of bounds");
						return null; // Really an error
				}
				return getCDSFValueAt(rowIndex, columnIndex);
				
		}
		public boolean isCellEditable(int rowIndex, int columnIndex) {
				return true;
		}
		//void removeTableModelListener(TableModelListener l) {}
		public void setValueAt(Object aValue,int rowIndex, int colIndex) {
				// set the values for the outcome
				if ( colIndex == 0 ) {
						// Determine the number of rows 
						// give radio box effect
						defaultRow.clear();
						StateMatrixRow smr = null;
						for ( int i = 0; i < getRowCount(); i++) { 
							if ( i != rowIndex){
								defaultRow.add(FALSE);
								smr = getStateMatrixRow(i+1); 
								smr.setIsDefaultRow(false);
							}
							else {
								defaultRow.add(TRUE);
								// clear all other tables? - no dependent per table
								smr = getStateMatrixRow(i+1); // set the isDefaultRow
								smr.setIsDefaultRow(true);
							}
							//smr.update();
						}
				}
				else if ( colIndex == getColumnCount()-1 && cdsf != null ) {
						// should check to see if this is a default row TODO
						Vector outcomes = cdsf.getOutcomes();
						if ( (outcomes != null) && (outcomes.size() > rowIndex) ) {
								if ( outcomes.elementAt(rowIndex) 
										 instanceof ConditionalEventOutcome){
										if ( aValue != null ) 
										
												((ConditionalEventOutcome)outcomes.elementAt(rowIndex)).setOutcome((EventDeclaration)aValue);

								}
								else {
										((ConditionalOutcome)outcomes.elementAt(rowIndex)).setOutcome(aValue);
										((ConditionalOutcome)outcomes.elementAt(rowIndex)).setPersistibleState(Persistible.DIRTY);
										((ConditionalOutcome)outcomes.elementAt(rowIndex)).update();
								}
						}
				}
				cdsf.setPersistibleState(Persistible.DIRTY);
				cdsf.update();
				fireTableCellUpdated(rowIndex, colIndex-1);
				fireTableChanged(new TableModelEvent(this, rowIndex, rowIndex));
		}


		private String getCDSFHeadingName(int columnIndex) {
				// Get the headings for the function from the CDSF
				// State Matrix
				if ( cdsf != null ) {
						if (cdsf.getStateMatrix() != null ) {
								Vector headings = 
										cdsf.getStateMatrix().getMatrixHeading();
								if ( headings != null && headings.size() >= columnIndex){
										return headings.elementAt(columnIndex-1).toString();
								}
						}
						
				}
				return null;
		}
		private Object getCDSFValueAt(int rowIndex, int columnIndex) {
				
				// Get the value for the function from the CDSF
				// State Matrix and the outcomes
				// Get boolean value from the first column
				if ( columnIndex == 0 ) {
						return defaultRow.elementAt(rowIndex);
				}
				// get the values from the outcomes
				else if ( columnIndex == getColumnCount()-1) {
						Vector outcomes = cdsf.getOutcomes();
						if ( (outcomes != null) && (outcomes.size() > rowIndex) ) {
							//if ( rowIndex == 3 && columnIndex == 3 )
								//System.out.println("3 - 3 " + ((ConditionalOutcome)outcomes.elementAt(rowIndex)).getOutcome());
							return ((ConditionalOutcome)outcomes.elementAt(rowIndex)).getOutcome();
						}
				}
				else if ( cdsf != null ) {
						if (cdsf.getStateMatrix() != null ) {
								Vector rows = 
										cdsf.getStateMatrix().getStateMatrixRows();
								if ( rows != null && rows.size() > rowIndex+1){
										StateMatrixRow row = 
												(StateMatrixRow)rows.elementAt(rowIndex+1);
										return row.getRowColumn(columnIndex-1);
								}
						}
						
				}
				return null;
		}
		public StateMatrixRow getStateMatrixRow(int row){
			StateMatrixRow smr = null;
			if ( cdsf != null ) {
				if (cdsf.getStateMatrix() != null ) {
						Vector rows = 
								cdsf.getStateMatrix().getStateMatrixRows();
						if ( rows != null && rows.size() > row){
								smr = (StateMatrixRow)rows.elementAt(row);
						}
				}
			}
			return smr;
		}
		
		public String toString() {
				if ( cdsf != null ) 
						return "CDSFTableModel: rows " + cdsf.getStateMatrix()
								+ " outcomes " + cdsf.getOutcomes();
				else
						return "CDSFTableModel: null cdsf";
		
		}
}
