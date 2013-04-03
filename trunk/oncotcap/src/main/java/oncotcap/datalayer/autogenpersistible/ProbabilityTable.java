package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;
import java.util.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.util.*;



public class ProbabilityTable extends AutoGenPersistibleWithKeywords {
		private Integer versionNumber;
		private DefaultPersistibleList probabilities;
		private Integer defaultValueRow;
		private String heading = null;
		public ProbabilityTable() {
				init();
		}
		
		
		public ProbabilityTable(oncotcap.util.GUID guid) {
				super(guid);
				init();
		}

		private void init() {
				Method setter = null;
				Method getter = null;
				setter = getSetter("setVersionNumber", Integer.class);
				setMethodMap.put("versionNumber", setter);
				getter = getGetter("getVersionNumber");
				getMethodMap.put("versionNumber", getter);
				setter = getSetter("setProbabilities", DefaultPersistibleList.class);
				setMethodMap.put("probabilities", setter);
				getter = getGetter("getProbabilities");
				getMethodMap.put("probabilities", getter);
				setter = getSetter("setDefaultValueRow", Integer.class);
				setMethodMap.put("defaultValueRow", setter);
				getter = getGetter("getDefaultValueRow");
				getMethodMap.put("defaultValueRow", getter);
		}
		
		public Integer getVersionNumber(){
				return versionNumber;
		}
		public DefaultPersistibleList getProbabilities(){
				return probabilities;
		}
		public Integer getDefaultValueRow(){
				return defaultValueRow;
		}
		public void setVersionNumber(Integer var ){
				versionNumber = var;
		}
		
		public void setProbabilities(java.util.Collection  var ){
				if ( probabilities== null)
						probabilities = new DefaultPersistibleList();
				probabilities.set(var);
			// 	System.out.println("setting probabilities " + getGUID() 
// 													 + " " + probabilities + " var " + var);
		}
		public void setProbabilities(DefaultPersistibleList  var ){
				if ( probabilities== null)
						probabilities = new DefaultPersistibleList();
				probabilities.set(var);
		}
		public void setDefaultValueRow(Integer var ){
				defaultValueRow = var;
		}
		
		public String toString() {
				// Return a html string in the form of a table 
				StringBuffer htmlString = new StringBuffer();
				htmlString.append("<TABLE border=\"1\">");
				// Loop through the probabilities each element is a 
				// boolean expression representing a row in the table
				if ( probabilities == null ||  probabilities.size() == 0)
						return "Probability Table Undefined";
				Iterator i = probabilities.iterator();
				// Create heading 
				// Fill in data
				while ( i.hasNext() ) {
						BooleanExpression expr = (BooleanExpression)i.next();
						htmlString.append("<TR>");
						htmlString.append("<TD>");
						if ( expr.getLeftHandSide() != null )
								htmlString.append(expr.getLeftHandSide().toString());
						htmlString.append("</TD>");
						htmlString.append("<TD>");
						if ( expr.getRightHandSide() != null )
								htmlString.append(expr.getRightHandSide().toString());
						htmlString.append("</TD>");
						htmlString.append("</TR>");
				}
				htmlString.append("</TABLE>");
				
				return htmlString.toString();
		}
		
		public Class getPanelClass() {
				return ProbabilityTablePanel.class;
		}
		public String getPrettyName() {
				return "ProbabilityTable";
		}
		public ImageIcon getIcon() {
				return icon;
		}
		public void initProbTable(String heading, EnumLevelList levels){
				this.heading = heading;
				// Initialize the percentages & level columns
				SortedList sortedLevels = null;
				if ( levels != null ) {
						if ( !levels.isNumericList() ){
								sortedLevels = levels.getLevels();
						}
				}
				probabilities = new DefaultPersistibleList();
				for ( int i = 0; i < sortedLevels.size(); i++){
						BooleanExpression probability = new BooleanExpression();
						probability.setLeftHandSide((Persistible)sortedLevels.get(i));
						// no operator - no initial value
						probabilities.addElement(probability);
				}
				this.defaultValueRow = new Integer(probabilities.size());
		}
		
		public String getHeading(){
				return heading;
		}
		public void setHeading(String var ){
				heading = var;
		}
//    public void addColumn(Object headerLabel,
//                          Object[] values) {
//         DefaultTableModel model = (DefaultTableModel)getModel();
// 				TableColumn col = new TableColumn(model.getColumnCount());
    
//         // Ensure that auto-create is off
// 				setAutoCreateColumnsFromModel(false); 
//         if (getAutoCreateColumnsFromModel()) {
//             throw new IllegalStateException();
//         }
//         col.setHeaderValue(headerLabel);
//         addColumn(col);
//         model.addColumn(headerLabel.toString(), values);
// 				int colPos = model.getColumnCount()-1;
// 				moveColumn(colPos-1,colPos);
//     }
}
