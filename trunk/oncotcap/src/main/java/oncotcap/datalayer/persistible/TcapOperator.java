package oncotcap.datalayer.persistible;

import java.util.*;
import oncotcap.Oncotcap;
import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.util.*;

public class TcapOperator extends AbstractDroppable
		implements Editable {
		private String name;
		private static Vector allOperators = null;
		private static Vector equalOnly = null;
		private static Vector booleanExpressionOperators = null;
		private static Vector assignmentOperators = null;

		static{initAllOperators();}

	public TcapOperator(oncotcap.util.GUID guid){
		super(guid);
		}

		public TcapOperator()	{
		}
	
		private static void initAllOperators() {
				if (allOperators == null)
						{
								Class clsOperator = 
										ReflectionHelper.classForName
										("oncotcap.datalayer.persistible.TcapOperator");
								OncoTCapDataSource dataSource = Oncotcap.getDataSource();
								allOperators  = new Vector(dataSource.find(clsOperator));
								//System.out.println("operators " + clsOperator + " " +  allOperators);
						}
		}
		
		public static Vector getAllOperators() {
				return (allOperators);
		}

		public static Vector getBooleanExpressionOperators() {
				if ( booleanExpressionOperators == null ) 
						booleanExpressionOperators = new Vector();
				else
						return booleanExpressionOperators;
				Iterator i = allOperators.iterator();
				while (i.hasNext()) {
						TcapOperator op = (TcapOperator)i.next();
						if (!op.getName().equals("P"))
								booleanExpressionOperators.addElement(op);
				}

				return (booleanExpressionOperators);
		}
		public static Vector getEqualOnly() {
				if ( equalOnly == null)
						equalOnly = new Vector();
				else 
						return equalOnly;
				Iterator i = allOperators.iterator();
				while (i.hasNext()) {
						TcapOperator op = (TcapOperator)i.next();
						if (op.getName().equals("="))
								equalOnly.addElement(op);
				}
				return (equalOnly);
		}
		public static Vector getAssignmentOperators() {
				if ( assignmentOperators == null)
						assignmentOperators = new Vector();
				else 
						return assignmentOperators;
				Iterator i = allOperators.iterator();
				while (i.hasNext()) {
						TcapOperator op = (TcapOperator)i.next();
						if (op.getName().equals("=") || op.getName().equals("P") )
								assignmentOperators.addElement(op);
				}
				return (assignmentOperators);
		}


		public void setName(String n) {
				this.name = n;
		}
	
		public String getName() {
				return this.name;
		}

		public String toString()
		{
				return(getName());
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
				return(null);
		}
}
