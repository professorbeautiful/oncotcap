package oncotcap.datalayer.persistible;

import javax.swing.*;
import java.awt.Dimension;

import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.util.*;

public class BooleanExpression extends AbstractDroppable
		implements Editable, HyperTextMenuListener {
		private Persistible leftHandSide;
		private Persistible rightHandSide;
		private TcapOperator operator;
		private int expressionType; // boolean_expression, assignment,instantiation

	public BooleanExpression(oncotcap.util.GUID guid){
		super(guid);
		}

		public BooleanExpression()
		{
			this(true);
		}
		public BooleanExpression(boolean saveToDataSource)
		{
			if(!saveToDataSource)
				this.setPersistibleState(Persistible.DO_NOT_SAVE);
			// if(saveToDataSource)
// 				System.out.println("PERSISTING BOOLEAN EXPRESSION");
		}
		public void setLeftHandSide(Persistible lhs ) {
				this.leftHandSide = lhs;
				//update();
		}
		public void setRightHandSide(Persistible rhs ) {	
				this.rightHandSide = rhs;   
				//update();
		}
		public void setOperator(TcapOperator op ) {
				this.operator = op;
				//update();
		}
		public void setExpressionType(int type ) {
				this.expressionType = type;
				//update();
		}
		public Persistible getLeftHandSide() {
				return this.leftHandSide;
		}
		public Persistible getRightHandSide() {
				return this.rightHandSide;
		}
		public TcapOperator getOperator() {
				return this.operator;
		}
		public int getExpressionType() {
				return this.expressionType;
		}
		public String toString()
		{
				String rhs = null;
				String lhs = null;
			
				if ( leftHandSide != null) {
						lhs = leftHandSide.toString();
				}
				if ( rightHandSide != null)
						rhs = rightHandSide.toString();
				
				if ( getOperator() == null ) 
						return lhs + " " + "=" + " " +  rhs;
				else 
						return lhs + " " + getOperator() + " " +  rhs;
		}
		public String getValueString() {
				StringBuffer valueString = new StringBuffer();
			
				//System.out.println("BooleanExpression " + getGUID() );
				if ( leftHandSide != null) {
						valueString.append(leftHandSide.toString());
				}
				if ( expressionType == DeclareEnumPicker.ASSIGNMENT 
						 || expressionType == DeclareEnumPicker.INSTANTIATION ) 
						valueString.append("=");
				else if ( expressionType == DeclareEnumPicker.BOOLEAN_EXPRESSION ) {
						if (getOperator() != null ){
								if (getOperator().toString().equals("="))
										valueString.append("==");
								else 
										valueString.append(getOperator().toString());
						}
				}
				if ( rightHandSide != null)
						valueString.append(rightHandSide.toString());

				return valueString.toString();
		}

		public EditorPanel getEditorWithInstance()
		{
				//System.out.println("getEditorWithInstance");
				return(new BooleanExpressionEditorPanel(this));
		}
		public EditorPanel getEditorPanelWithInstance()
		{
				//System.out.println("getEditorPanelWithInstance");
				return(new BooleanExpressionEditorPanel(this));
		}
		public EditorPanel getEditorPanel()
		{
				//System.out.println("getEditorPanel");
				return(new BooleanExpressionEditorPanel(this));
		}
		public void hyperTextMenuChanged(HyperTextMenuEvent e) {
				//System.out.println("hyper text menu event    -->" + e.getSelectedItem().getClass());
				if (e.getSelectedItem() instanceof LevelAndList) {
						EnumLevel level = ((LevelAndList)e.getSelectedItem()).getLevel();
						EnumLevelList levelList =
								((LevelAndList)e.getSelectedItem()).getLevelList();
						level.setLevelList(levelList);
						setRightHandSide(level);
				}
		}

		public static BooleanExpression dropKeywordCreateEnum(Keyword keyword, boolean showOperator) {
				BooleanExpression bool = new BooleanExpression(false);
				bool.setRightHandSide(null); 
				EnumDefinition enumDefinition = 
						new EnumDefinition(keyword.toString(), 
															 false);
				enumDefinition.setKeyword(keyword);
				bool.setLeftHandSide(enumDefinition);
				bool.setExpressionType(DeclareEnumPicker.ASSIGNMENT);
				
				BooleanExpressionEditorPanel editorPanel = 
						new BooleanExpressionEditorPanel(bool, showOperator);
				editorPanel.edit(bool);
				bool.addSaveListener(editorPanel);
				JDialog modalEditorDialog = 
						new JDialog((JFrame)null,
												"Specify Process and Enum Level List",
												true);
				modalEditorDialog.add(editorPanel);
				modalEditorDialog.setSize(new Dimension(600,100));
				modalEditorDialog.setVisible(true);
				// Create a table and drop it on the canvas
				// Get information from the boolean editor panel
			// 	System.out.println("Boolean right hand side " 
// 													 + bool);
				editorPanel.syncEditor();
				if (bool.getRightHandSide() instanceof EnumLevel) {
						EnumLevel level = (EnumLevel)bool.getRightHandSide();
						EnumLevelList levelList = level.getLevelList();
				}
				return bool;
		}
}
