package oncotcap.datalayer.persistible;

import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.datalayer.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.util.*;

public class UndefinedBooleanExpression extends BooleanExpression
		{
	public UndefinedBooleanExpression() {
	}
	public UndefinedBooleanExpression(oncotcap.util.GUID guid) {
		super(guid);
	}
		public String toString()
		{
				if ( getLeftHandSide() != null)
						return " " + getLeftHandSide().toString();
				else
						return "UNDEFINED BOOLEAN EXPRESSION";
			// 	return(this.rightHandSide.toString() 
// 							 + this.operator.toString() 
// 							 + this.leftHandSide.toString());
		}
		public EditorPanel getEditorWithInstance()
		{
				return(null);
		}
		public EditorPanel getEditorPanelWithInstance()
		{
				return(null);
		}
		public EditorPanel getEditorPanel()
		{
				// forward this to the appropriate object which is the level list
				if  ( getRightHandSide() != null && 
							getRightHandSide() instanceof EnumLevelList) {
						return ((EnumLevelList)getRightHandSide()).getEditorPanelWithInstance();
				}
				else 
						return(null);
		}
}
