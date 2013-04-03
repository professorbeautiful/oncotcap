package oncotcap.engine;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;


import oncotcap.datalayer.persistible.CodeBundle;
import oncotcap.datalayer.persistible.action.*;
import oncotcap.display.common.WaitWindow;
import oncotcap.util.MatrixMath;
import oncotcap.util.StringHelper;
import oncotcap.engine.java.JavaParser;

public class SortedInstructionList extends ArrayList<InstructionAndValues>
{
	private ArrayList vars = new ArrayList();
	private int[][] directDependencyMatrix = {};
	private int[][] fullDependencyMatrix;
	boolean debugging = true;
	
	public static void main(String [] args)
	{
		SortedInstructionList list = new SortedInstructionList();
		ValueMapPath path = new ValueMapPath();
		list.addWithoutSort(newInstruction("{String e; e = a;}", path));  //sorts OK.
		list.addWithoutSort(newInstruction("{String e = a;}", path));     // e not identified as a "set variable".
		list.addWithoutSort(newInstruction("meth(e)", path));     // e not identified as a "set variable".
		list.addWithoutSort(newInstruction("a = b", path));
		list.addWithoutSort(newInstruction("b *= d", path));
		list.addWithoutSort(newInstruction("b = c", path));
		list.sort();
		list.print();
	}
	private static InstructionAndValues newInstruction(String instruction, ValueMapPath path)
	{
		return(new InstructionAndValues(new TestInstruction(instruction), path));
	}
	private static class TestInstruction implements Instruction
	{
		private String code;
		TestInstruction(String code)
		{
			this.code = code;
		}
		public Collection<String> getAllVariables(ValueMapPath path)
		{
			return(JavaParser.getAllVariables(code));
		}
		public Collection<VariableDependency> getVariableDependencies(ValueMapPath path)
		{
			return(JavaParser.getDependencies(code));
		}
		public Collection<String> getSetVariables(ValueMapPath path)
		{
			return(JavaParser.getSetVariables(code));
		}

		public ClassSectionDeclaration getSectionDeclaration()
		{
			return(null);
		}
		public InstructionProvider getEnclosingInstructionProvider()
		{
			return(null);
		}
	}
	private void print()
	{
		for(InstructionAndValues iav : this)
		{
			System.out.println("SortedInstructionList.print: "
					+ iav.getInstruction());
		}
	}
	public boolean add(InstructionAndValues provider)
	{
		if(super.add(provider))
		{
			//sort();
			return(true);
		}
		else
			return(false);
	}
	public void add(int index, InstructionAndValues element)
	{
		super.add(index, element);
		//sort();
	}
	public void addWithoutSort(InstructionAndValues provider)
	{
		super.add(provider);
	}
	public boolean addAll(Collection providers)
	{
		if(super.addAll(providers))
		{
			//sort();
			return(true);
		}
			return(false);
	}
	public boolean addAll(int index, Collection providers)
	{
		if(super.addAll(index, providers))
		{
			sort();
			return(true);
		}
		else
			return(false);
	}
	public InstructionAndValues	remove(int index)
	{
		InstructionAndValues p = super.remove(index);
		sort();
		return(p);
	}
	public boolean remove(Object o)
	{
		if(super.remove(o))
		{
			//sort();
			return(true);
		}
		else
			return(false);
	}
	protected  void removeRange(int fromIndex, int toIndex)
	{
		super.removeRange(fromIndex, toIndex);
		sort();
	}
	public InstructionAndValues set(int index, InstructionAndValues element)
	{
		InstructionAndValues p = super.set(index, element);
		//sort();    // is this really necessary???
		return(p);
	}
	void splitByIfClauses(){
		oncotcap.util.Logger.logTheTime("SortedInstructionList.splitByIfClauses()");
		Iterator it = iterator();
		while(it.hasNext())
		{
			Object obj = it.next();
			InstructionAndValues iav = (InstructionAndValues) obj;
			Instruction instruction = iav.getInstruction();
/*			if (instruction instanceof AddGenericCode.GenericCodeInstruction) {
				AddGenericCode gen = (AddGenericCode) instruction;
				oncotcap.util.Logger.log("   " 
					+ iav.getValues().substitute(gen.toString()));
			}
*/			// Need to implement nice toString methods.
			Vector<InstructionAndValues> v = iav.splitByIfClauses();
			if(v != null){
				oncotcap.util.Logger.log("Replacing IAV's due to ifClause, resulting in "
						+ v.size() + " elements");
				remove(obj);
				addAll(v);
			}
			// We replace obj by separate IAV's, one for each action, all with the same ifClause.
			// This allows each action to sort separately.
		}
	}
	void getAllVariables(){
		oncotcap.util.Logger.logTheTime("SortedInstructionList.getAllVariables()");
		//parse all of the code sections and get the variables
		Iterator it = iterator();
		vars.clear();
		while(it.hasNext())
		{
			Object obj = it.next();
			InstructionAndValues iav = (InstructionAndValues) obj;
			Instruction instruction = iav.getInstruction();
			if (instruction instanceof AddGenericCode.GenericCodeInstruction) {
				AddGenericCode.GenericCodeInstruction genericCode = (AddGenericCode.GenericCodeInstruction) instruction;
				//System.out.println("genericCode is " + genericCode.getEnclosingInstructionProvider().toString());
//				if(genericCode.getEnclosingInstructionProvider().toString().contains("message"))
//					System.out.println("MESSAGE: " + iav);
			}
			Collection decls = iav.getAllVariables();
			addVars(decls);
			//System.out.println("SortedInstructionList.getAllVariables: decls.size= " + decls.size());
		}
	}
	
	void createDirectDependencyMatrix(){
//		create a new dependency matrix size will be based on how many
		//variables we have (it will be an n x n)
		//rows will be variable names that are modified, columns are the
		//variables that the modifications are dependent on.
		int nVars = vars.size();
		int [] arrayDims = {nVars, nVars};
		directDependencyMatrix = (int [][]) Array.newInstance(int.class, arrayDims);
		
		//loop through all code providers again, adding the dependencies
		Iterator it = iterator();
		while(it.hasNext())
		{
			InstructionAndValues iav = (InstructionAndValues) it.next();
			/*if(iav.getInstruction() instanceof AddGenericCode.GenericCodeInstruction) {
				@SuppressWarnings("unused") int stop_here = 0;
			}*/
			// This could be causing eclipse to throw some thread-related errors.
//			if(iav.toString().contains("Unscaled") && iav.toString().contains("message")){
//				System.out.println("Debug the variable dependencies");
//			}
				
			Collection<VariableDependency> dependsColl = iav.getVariableDependencies();
			for(VariableDependency tDepend : dependsColl)
			{
				int modIndex = getVarIndex(tDepend.leftVariableName);
				int depIdx = getVarIndex(tDepend.rightVariableName);
				if(modIndex >= 0 && depIdx >= 0)
				{
					directDependencyMatrix[modIndex][depIdx] = 1;
				}
				if(tDepend.getOperatorType() == VariableDependency.STAR_ASSIGN)
					directDependencyMatrix[modIndex][modIndex] = 1;
			}
		}
		if(debugging){
			Iterator my_iterator = iterator();
			if(my_iterator.hasNext()) {
				Instruction inst = ((InstructionAndValues)my_iterator.next()).getInstruction();
				String context =inst.getEnclosingInstructionProvider().getProcessDeclaration()
				+ (inst.getSectionDeclaration().toString().contains("DeclarationSection") ?
						" DeclarationSection"
						:
						"." + inst.getSectionDeclaration() + "()");
			//	System.out.println("DIRECT DEPENDENCY MATRIX for " + context);
			//	printMatrix(directDependencyMatrix);
			//	System.out.println("===========================================================\n\n");
			}		
		}
	}
	void createFullDependencyMatrix(){
		//build the fullDependencyMatrix matrix, based on Roger's "Variable graphs and
		//management" document
		int n;
		int nVars = vars.size();
		int[][] dependZeroDiag = MatrixMath.zeroDiagonal(directDependencyMatrix);
		int[][] d = MatrixMath.copy(dependZeroDiag);
		fullDependencyMatrix = MatrixMath.copy(dependZeroDiag);
		for(n = 1; n < nVars; n++)
		{
			d = MatrixMath.multiply(d, dependZeroDiag);
			fullDependencyMatrix = MatrixMath.add(fullDependencyMatrix, d);
			fullDependencyMatrix = MatrixMath.toOnes(fullDependencyMatrix);
		}
		
		//CHECK FOR CYCLES, 
		for(int i=0; i<fullDependencyMatrix.length-1; i++)
			for(int j=i+1; j<fullDependencyMatrix.length; j++)
				if(fullDependencyMatrix[i][j]==1 && fullDependencyMatrix[j][i]==1 ){
					System.err.println("ERROR: mutually dependent variables" 
							+ vars.get(i) + ", " + vars.get(j) + " [createDependencyMatrix()]");
				}
		//TODO: ADD TO A LIST OF CYCLES FOR LATER HANDLING.
		if(debugging) {
			Iterator my_iterator = iterator();
			if(iterator().hasNext()){
				Instruction inst = ((InstructionAndValues)my_iterator.next()).getInstruction();
				String context =inst.getEnclosingInstructionProvider().getProcessDeclaration()
					+ (inst.getSectionDeclaration().toString().contains("DeclarationSection") ?
							" DeclarationSection"
							:
							"." + inst.getSectionDeclaration() + "()");
			//	System.out.println("\nfullDependencyMatrix for " + context); 
			//	printMatrix(fullDependencyMatrix);
			//	System.out.println("===========================================================\n\n");
			}
		}
	}
	
	public void sort()
	{
		if(size()==0)
			return;
		Instruction inst = ((InstructionAndValues)get(0)).getInstruction();
		String context =inst.getEnclosingInstructionProvider().getProcessDeclaration()
		+ (inst.getSectionDeclaration().toString().contains("DeclarationSection") ?
				" DeclarationSection"
				:
				"." + inst.getSectionDeclaration() + "()");
		//WaitWindow.theWaitWindow.setText("");
		WaitWindow.theWaitWindow.setDetail("Sorting:  " + context);
		
//		for(int i = 0; i < (this.size()); i++)
//		{
//			System.out.println("ALL: " + super.get(i));
//		}
		splitByIfClauses();
		getAllVariables();
		createDirectDependencyMatrix();
		createFullDependencyMatrix();

		//sort the instructions based on the directDependencyMatrix and fullDependencyMatrix matrices.
		InstructionAndValues tempProvider;
		int i, j;
		String probe = "HER2expression";   
		boolean iContainsProbe, jContainsProbe;
		for(i = 0; i < (this.size() - 1); i++)
		{
			iContainsProbe = super.get(i).toString().contains(probe) || get(i).getAllVariables().contains(probe);
//			if(iContainsProbe)
//				System.out.println("=i=" + i + " " + super.get(i));
			for(j = i + 1; j < this.size(); j++)
			{
				jContainsProbe = super.get(j).toString().contains(probe) || get(j).getAllVariables().contains(probe);
//				if(jContainsProbe)
//					System.out.println("=j=" + j + " " + super.get(j));
				//print(); System.out.println("==");
				//TODO:  write a toString method for the Instructions.
				if(iContainsProbe && jContainsProbe) {
					int comparison = compareCS(super.get(i), super.get(j));
//					System.out.println(" COMPARISON (" + probe + ")");
//					System.out.println("    " + super.get(i));
//					System.out.print("  " + (comparison == -1 ? "<" : (comparison == 0 ? "=" : ">")) + " ");
//					System.out.println(super.get(j));
//					System.out.println(" Which are assignments? " +
//							(get(i).getSetVariable() != null ?  "" : "first one (" + get(i).getSetVariable() + ")")
//									+
//							(get(j).getSetVariable() != null ?  "" : "2nd one (" + get(j).getSetVariable() + ")")
//					);
//					System.out.println(" Which are modifications? " +
//							(isModification(get(i)) ?  "" : "first one")
//									+
//							(isModification(get(j)) ?  "" : "   2nd one" )
//					);
				}
				int comparison = compareCS(super.get(i), super.get(j));
				if(comparison > 0)
				{
					tempProvider = super.get(i);
					super.set(i, super.get(j));
					super.set(j, tempProvider);
				}
			}
		}
		
		//next, since self dependency (i.e. x = x * 2, x *= 2, i += 3, etc) 
		//is not handled correctly by this sort.  Check for self dependency 
		//and order accordingly...  This only matters in the case where you have two
		//statements that set the same variable and one of the statements is a 
		//direct assignment ( a = b ).
		
		//  I think this section is no longer necessary.
		for(i = 0; i < (this.size() - 1); i++)
		{
			for(j = i + 1; j < this.size(); j++)
			{
				if(checkSetStatus(super.get(i), super.get(j)) > 0)
				{
					int minIdx = Math.min(i, j);
					int maxIdx = Math.max(i, j);
					tempProvider = super.get(maxIdx);
					for(int k = maxIdx; k > minIdx; k--)
					{
						super.set(k, super.get(k-1));
					}
					super.set(minIdx, tempProvider);
				}
			}
		}
	}
	boolean isModification(InstructionAndValues iav){
		if(iav.getSetVariable() == null)
			return(false);
		if(iav.getClass().equals(ModifyVariableAction.class))
			return(true);
		for(VariableDependency vd : iav.getVariableDependencies()){
			// this assumes that there is only one set variable.
			// this assumes that it' appear on the rhs!  NOT TRUE.
			if(vd.leftVariableName.equals(vd.rightVariableName))
				return(true);
		}
		return(false);
	}
	boolean isInitialization(InstructionAndValues iav){
		return(iav.getSetVariable() != null);
	}
	boolean requiresVariable(InstructionAndValues iav, String var){
		if(iav.getAllVariables().contains(var)) {
			if(iav.getSetVariable() == null)
				return(true);
			for(VariableDependency vd : iav.getVariableDependencies()){
			// this assumes that there is only one set variable.
				if(vd.leftVariableName.equals(var) && ! isModification(iav))
					return(false); // not on the RHS.
			}
			return(true);
		}
		return(false);
	}

	private boolean hasIfClause(InstructionAndValues iav){
		InstructionProvider ip = iav.getInstruction().getEnclosingInstructionProvider();
		CodeBundle cb;
		if( ip instanceof InitVariableAction) {
			cb = ((InitVariableAction)ip).getCodeBundleContainingMe();
//			if(cb.toString().contains("IF"))
//				System.out.println("IFCLAUSE HERE");
/*		if( ip instanceof DefaultOncAction  && 
				!((DefaultOncAction)ip).getIfClauseVariables(iav.getValues()).isEmpty())
				return true;
				// This section fails.
*/
				if( ip instanceof DefaultOncAction  && 
							StringHelper.notEmpty(cb.ifClause))
					return true;
		}
		return false;
	}

	/**
	 ** Compares two code sections for order purposes.  This method
	 ** depends on the fullDependencyMatrix matrix being already created.  Returns 1
	 ** if cs1 > cs2, -1 if cs1 < cs2 and 0 if they are equal.  If the
	 ** there is a cross dependancy Integer.MAX_VALUE is returned.
	 **/
	
// we will save the comparisons here for later review.
	Integer comparisons[][] = null;
	private int compareCS(InstructionAndValues iav1, InstructionAndValues iav2)
	{
		//for now we only compare the first modified variable from each
		//section.  The current state of Actions allows this, in the
		//future if more than one variable is modified by any one action
		//this function will need to be updated to compare all variables.
		//This should be accomplishable with the same "fullDependencyMatrix" matrix, all
		//combinations of modified variables will need to be compared, if
		//they are all > or < or = then everything is fine, otherwise
		//another, non-sortable, state will need to be returned.
		//side note: if someone types an expression like:
		//x *= [ x * 2; z = 10;]  an action could actually modify two
		//variables...  This would compile and run, but may not get
		//ordered correctly.
		
		
		//if we have the case:
		//		a = b;
		//    a *= f(c);
		//order the setting (a=b) statement first
		//int checkSet = 0;
		//if( (checkSet = checkSetStatus(iav1, iav2)) != 0)
		//	return(checkSet);

		//push all events to the bottom....
		//  this is fine!  -rd
		if(isEvent(iav1))
			return(1);
		if(isEvent(iav2))
			return(-1);
		
		//otherwise compare the modifications defined in the fullDependencyMatrix matrix
		
		/* TODO: now that we support more than one modification per provider
		 * check ALL MODIFICATIONS instead of just the first one- wes
		 * In fact, throw a warning- rd.
		 */
		
		if(comparisons == null)
			comparisons = new Integer[size()][size()];
		if (iav1.toString().equals("Instantiate BaxMediatedApoptosis")){
//			System.out.println("BREAK: Instantiate BaxMediatedApoptosis");
			Collection<String> c = iav1.getAllVariables();
//			System.out.println(c );
		}
		String setVar1 = iav1.getSetVariable();
//		if (iav2.toString().equals("Instantiate BaxMediatedApoptosis"))
//			System.out.println("BREAK: Instantiate BaxMediatedApoptosis");
		String setVar2 = iav2.getSetVariable();
		//push all non-assignments to the bottom....
		//  this is fine!  -rd
		if(setVar1==null)
			return(1);
		if(setVar2==null)
			return(-1);

		// Use the dependency matrix.

		if( setVar1.equals(setVar2)){
//			System.out.println(setVar1);
			int value = 0;
			if(isModification(iav1) && !isModification(iav2))
				value = 1;  // Push iav1 below iav2.
			else if(!isModification(iav1) && isModification(iav2))
				value = -1;  // Push iav2 below iav1.
			else if(hasIfClause(iav1) && !hasIfClause(iav2))
				value = 1;  // Push iav1 below iav2.
			else if(!hasIfClause(iav1) && hasIfClause(iav2))
				value = -1;  // Push iav2 below iav1.
			String stringrep = value == 1 ? "iav1 below iav2" :
					value == -1 ? "iav1 above iav2" : "equal";
/*			if(iav1.toString().contains("p53_functionality") ||
					iav2.toString().contains("p53_functionality") ||
					iav1.toString().contains("UPSTREAM") ||
					iav2.toString().contains("UPSTREAM"))
*/			//System.out.println("Comparison: " + stringrep + 
			//			"\n iav1  " + iav1 + "\n iav2  " + iav2);
			return value;
		}
		else {
			// Just use the dependency matrix.
			int idx1 = getVarIndex(setVar1);
			int idx2 = getVarIndex(setVar2);
			if(idx1 >= 0 && idx2 >= 0)
			{
				if(fullDependencyMatrix[idx1][idx2] != 0 && fullDependencyMatrix[idx2][idx1] == 0)
					return(1);
				else if(fullDependencyMatrix[idx1][idx2] == 0 && fullDependencyMatrix[idx2][idx1] != 0)
					return(-1);
				else if(fullDependencyMatrix[idx1][idx2] == 0 && fullDependencyMatrix[idx2][idx1] == 0)
					return(0);
				else
				{
					String warnText = "A dependency cycle exists between variables " + setVar1 + " and " + setVar2;
//					OncMessageBox.showWarning(warnText, "Variable fullDependencyMatrix error.");
					System.err.println("WARNING: " + warnText);
					return(Integer.MAX_VALUE);
				}
			}
			return(0);
		}
		
/*//		if(iav1.getInstruction().getEnclosingInstructionProvider() instanceof AddVariableAction)
//		{
//			if(((AddVariableAction)iav1.getInstruction().getEnclosingInstructionProvider()).getGUID().getStrId().equals("888e66b800000036000000fe44d6f769"))
//				System.out.println("Blech");
//		}
//		if(iav2.getInstruction().getEnclosingInstructionProvider() instanceof AddVariableAction)
//		{
//			if(((AddVariableAction)iav2.getInstruction().getEnclosingInstructionProvider()).getGUID().getStrId().equals("888e66b800000036000000fe44d6f769"))
//				System.out.println("Blech");
//		}

 * 		Roger says:
 * 		iav1 comes first if:
 * 			LHS(iav1) is anywhere in the variables of iav2  (either RHS or not classified)
 * 				e.g.     b = a;    c = b;
 * 		or
 * 			LHS(iav1) = LHS(iav2), and LHS(iav1) is in RHS(iav2) (modification) but not in RHS(iav1) (initialization)   
 * 				e.g.     b = a;    b = b * 2;
 
		if(mod1Coll != null && mod2Coll != null && mod1Coll.size() > 0 && mod2Coll.size() > 0)
		{
			// Both have at least one assignment.
			String mod1 = iav1.getValues().substituteJavaName((String) mod1Coll.toArray()[0]);
			String mod2 = iav2.getValues().substituteJavaName((String) mod2Coll.toArray()[0]);
			if(mod1.contains("unscaled") || mod2.contains("unscaled")) {
				System.out.println(" I found a pair using unscaledRecurrenceScore");
				System.out.println("  " + mod1);
				System.out.println("  " + mod2);
			}
			if(mod1.equals(mod2)) {
				if(isModification(iav1)){
					
				}
					
				Collection vars1 = iav1.getAllVariables();
				Collection vars2 = iav2.getAllVariables();
				Iterator varDepIter1 = iav1.getVariableDependencies().iterator();
				VariableDependency varDep1 = null;
				if(varDepIter1.hasNext()) iav1.getVariableDependencies().iterator().next();
				return(0);
			}

		//	if((mod1.equals("logkill_DrugA") && mod2.equals("initial_logkill_DrugA")) || (mod2.equals("logkill_DrugA") && mod1.equals("initial_logkill_DrugA")))
		//	if(mod1.equals("initial_logkill_DrugA") || mod2.equals("initial_logkill_DrugA"))
	//			System.out.println("Blech");
			int idx1 = getVarIndex(mod1);
			int idx2 = getVarIndex(mod2);
			if(idx1 >= 0 && idx2 >= 0)
			{
				if(fullDependencyMatrix[idx1][idx2] != 0 && fullDependencyMatrix[idx2][idx1] == 0)
					return(1);
				else if(fullDependencyMatrix[idx1][idx2] == 0 && fullDependencyMatrix[idx2][idx1] != 0)
					return(-1);
				else if(fullDependencyMatrix[idx1][idx2] == 0 && fullDependencyMatrix[idx2][idx1] == 0)
					return(0);
				else
				{
					String warnText = "Variable fullDependencyMatrix exists between variables " + mod1 + " and " + mod2;
//					OncMessageBox.showWarning(warnText, "Variable fullDependencyMatrix error.");
					System.out.println("WARNING: " + warnText);
					return(Integer.MAX_VALUE);
				}
			}
			else
				return(0);
		}
		else
			return(0);
*/	}
	private boolean isEvent(InstructionAndValues iav)
	{
		InstructionProvider ip = iav.getInstruction().getEnclosingInstructionProvider();
		
		if(ip instanceof ScheduleEventAction || ip instanceof TriggerEventAction)
			return(true);
		else
			return(false);
	}
	private void addVars(Collection<String> variables)
	{
		for(String var : variables)
		{
			addVar(var);
		}
	}
	private void addVar(String varName)
	{
		if(!vars.contains(varName))
			vars.add(varName);
	}
	private int getVarIndex(String varName)
	{
			return(vars.indexOf(varName));
	}
	private int checkSetStatus(InstructionAndValues iav1, InstructionAndValues iav2)
	{
		String var1;
		String var2;
		int setStatus = 0;
		InstructionProvider inst1 = iav1.getInstruction().getEnclosingInstructionProvider();
		InstructionProvider inst2 = iav2.getInstruction().getEnclosingInstructionProvider();
		//checkInst(inst1, inst2);
		//checkInst(inst2, inst2);
		if(iav1 != null && iav2 != null)
		{
			Collection<VariableDependency> deps1 = iav1.getVariableDependencies();
			Collection<VariableDependency> deps2 = iav2.getVariableDependencies();
			for(VariableDependency dep1 : deps1)
			{
				for(VariableDependency dep2 : deps2)
				{
					String lVar1 = iav1.getValues().substituteJavaName(dep1.getLeftVariableName());
					String lVar2 = iav2.getValues().substituteJavaName(dep2.getLeftVariableName());
				//	if(lVar1.equals("median_mitosis_time") && lVar2.equals("median_mitosis_time"))
				//		System.out.println("blech");
					
					int opType1 = dep1.getOperatorType();
					int opType2 = dep2.getOperatorType();
					String op1 = VariableDependency.getOperatorString(opType1);
					String op2 = VariableDependency.getOperatorString(opType2);
					if(lVar1.equals(lVar2))
					{
						if(opType1 == opType2)
							return(0);
						else if(opType1 == VariableDependency.ASSIGN)
							return(-1);
						else if(opType2 == VariableDependency.ASSIGN)
							return(1);
						else
							return(0);
					}
				}
			}
		}
		return(0);
	}
/*	private int checkSetStatus(InstructionAndValues iav1, InstructionAndValues iav2, String var)
	{
		if(variableSet(iav1, var) && ! variableSet(iav2, var)) // && variableModified(iav2, var))
			return(-1);
		else if(variableSet(iav2, var) && ! variableSet(iav1, var)) // && variableModified(iav1, var))
			return(1);
		else
			return(0);
	}
	*/
	
	//is this variable contained in this InstructionProvider
	private boolean variableSet(InstructionAndValues ip, String var)
	{
		return(ip.getSetVariables().contains(var));
	}
	private String nSpaces(int n){
		String s = "";
		for(int i=0; i<n; i++) s = s + " ";
		return s;
	}
	private void printMatrix(int [][] mat)
	{
		int matrixGap = 2;
		int rows = mat.length;
		int maxVarnameLength = 0;
		for(int row = 0; row < rows; row++)
			maxVarnameLength = Math.max(maxVarnameLength, vars.get(row).toString().length());
		//Print column headers.
		for(int hRow=0; hRow<=maxVarnameLength; hRow++) {
			String s = nSpaces(2*maxVarnameLength + 6 - hRow);
			for(int nv=0; nv<vars.size(); nv++){
				if(vars.get(nv).toString().length() > maxVarnameLength - hRow)
					s = s + vars.get(nv).toString().charAt(maxVarnameLength - hRow);
				else
					s = s + " ";
				s = s + nSpaces(matrixGap);
			}
			System.out.println(s);
		}
		if(rows > 0)
		{
			int cols = mat[0].length;
			for(int row = 0; row < rows; row++)
			{
				System.out.print("  " + vars.get(row) + nSpaces(maxVarnameLength-vars.get(row).toString().length()+2));
//				if(vars.get(row).toString().length() <= 8)
//					System.out.print("\t");
				for(int column = 0; column < cols; column++)
					System.out.print(nSpaces(matrixGap) + mat[row][column]);
				System.out.print("\n");
			}
		}
	}

}
