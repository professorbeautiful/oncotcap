package oncotcap.engine.java;

import java.util.*;
import oncotcap.engine.VariableDependency;
import oncotcap.util.*;

public class JavaParser
{
	public static Collection<VariableDependency> getDependencies(String code)
	{
		//Logger.logTheTime(" JavaParser.getDependencies");
		if(code == null || code.trim().equals(""))
			return new Vector<VariableDependency>();
		
		OncASTTreeModel astTreeModel = null;
		try {
			DissectString dString = new DissectString(code);
			astTreeModel = dString.dissectString();
		}
		catch(Exception e) {
				System.out.println("Exception parsing code: JavaParser");
		}
		if(astTreeModel != null)
			return(astTreeModel.getVariableDependencies());
		else
			return new Vector<VariableDependency>();
	}
	
	public static Collection<String> getSetVariables(String code)
	{
		//Logger.logTheTime(" JavaParser.getSetVariables");
		if(code == null || code.trim().equals(""))
			return new Vector<String>();
	
		OncASTTreeModel astTreeModel = null;
		try {
				
			DissectString dString = new DissectString(code);
			astTreeModel = dString.dissectString();
		}
		catch(Exception e) {
				System.out.println("Exception parsing code: JavaParser");
		}
		if(astTreeModel != null)
			return(astTreeModel.getAllSetVariables());
		else
			return new Vector<String>();

	}
	
	public static Collection<String> getAllVariables(String code)
	{
		//Logger.logTheTime(" JavaParser.getAllVariables");

		if(code == null || code.trim().equals(""))
			return new Vector<String>();
		
		OncASTTreeModel astTreeModel = null;
		try {
			DissectString dString = new DissectString(code);
			astTreeModel = dString.dissectString();
		}
		catch(Exception e) {
				System.out.println("Exception parsing code: JavaParser");
		}
		if(astTreeModel != null)
			return(astTreeModel.getAllVariables());
		else
			return new Vector<String>();
	}
    public static void main(String[] args) {
				System.out.println("Variable Dependencies"  + 
													 getDependencies("a = (b = d) + c;") );
			System.out.println("Set Variable"  + 
													 getSetVariables("a = b + c;") );
			System.out.println("All Variables"  + 
													 getAllVariables("b + c;") );
		}

}
