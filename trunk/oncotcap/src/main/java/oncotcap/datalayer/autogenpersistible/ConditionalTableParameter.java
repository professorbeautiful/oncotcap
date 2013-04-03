package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import java.lang.reflect.*;
import java.util.*;


import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.persistible.parameter.SingleParameter;
import oncotcap.engine.*;
import oncotcap.util.*;

public class ConditionalTableParameter extends FunctionParameter 
		implements Instruction, 
			InstructionProvider,
			SingleParameter {
		private Persistible conditionValue;
		private String instanceUsabilityStatus;
		private Integer versionNumber;
		private DefaultPersistibleList valueMapEntriesContainingMe;
		private DefaultPersistibleList possibleOutcomes;
		private StateMatrix stateMatrix;
		private DefaultPersistibleList variableDefinitions;
		private DefaultPersistibleList inputs;
		private DefaultPersistibleList outputs;
		private DefaultPersistibleList function;
		
		protected String singleParameterID = null;
		public String displayName = null;
		protected String defaultDisplayName = null;
		
		private String idString = null;
		
		
		public ConditionalTableParameter() {
				init();
		}
		
		
		public ConditionalTableParameter(oncotcap.util.GUID guid) {
				super(guid);
				init();
		}
		private void init() {
				initSingleParams();
				Method setter = null;
				Method getter = null;
				setter = getSetter("setKeywords", DefaultPersistibleList.class);
				setMethodMap.put("keywords", setter);
				getter = getGetter("getKeywords");
				getMethodMap.put("keywords", getter);
				setter = getSetter("setConditionValue", Persistible.class);
				setMethodMap.put("conditionValue", setter);
				getter = getGetter("getConditionValue");
				getMethodMap.put("conditionValue", getter);
				setter = getSetter("setInstanceUsabilityStatus", String.class);
				setMethodMap.put("instanceUsabilityStatus", setter);
				getter = getGetter("getInstanceUsabilityStatus");
				getMethodMap.put("instanceUsabilityStatus", getter);
				setter = getSetter("setVersionNumber", Integer.class);
				setMethodMap.put("versionNumber", setter);
				getter = getGetter("getVersionNumber");
				getMethodMap.put("versionNumber", getter);
				setter = getSetter("setValueMapEntriesContainingMe", DefaultPersistibleList.class);
				setMethodMap.put("valueMapEntriesContainingMe", setter);
				getter = getGetter("getValueMapEntriesContainingMe");
				getMethodMap.put("valueMapEntriesContainingMe", getter);
				setter = getSetter("setPossibleOutcomes", DefaultPersistibleList.class);
				setMethodMap.put("possibleOutcomes", setter);
				getter = getGetter("getPossibleOutcomes");
				getMethodMap.put("possibleOutcomes", getter);
				setter = getSetter("setStateMatrix", StateMatrix.class);
				setMethodMap.put("stateMatrix", setter);
				getter = getGetter("getStateMatrix");
				getMethodMap.put("stateMatrix", getter);
				setter = getSetter("setVariableDefinitions", DefaultPersistibleList.class);
				setMethodMap.put("variableDefinitions", setter);
				getter = getGetter("getVariableDefinitions");
				getMethodMap.put("variableDefinitions", getter);
				setter = getSetter("setInputs", DefaultPersistibleList.class);
				setMethodMap.put("inputs", setter);
				getter = getGetter("getInputs");
				getMethodMap.put("inputs", getter);
				setter = getSetter("setOutputs", DefaultPersistibleList.class);
				setMethodMap.put("outputs", setter);
				getter = getGetter("getOutputs");
				getMethodMap.put("outputs", getter);
				setter = getSetter("setFunction", DefaultPersistibleList.class);
				setMethodMap.put("function", setter);
				getter = getGetter("getFunction");
				getMethodMap.put("function", getter);
		}
		
		public Persistible getConditionValue(){
			
				return conditionValue;
		}
		public String getInstanceUsabilityStatus(){
				return instanceUsabilityStatus;
		}
		public Integer getVersionNumber(){
				return versionNumber;
		}
		public DefaultPersistibleList getValueMapEntriesContainingMe(){
				return valueMapEntriesContainingMe;
		}
		public DefaultPersistibleList getPossibleOutcomes(){
				return possibleOutcomes;
		}
		public StateMatrix getStateMatrix(){
				return stateMatrix;
		}
		public DefaultPersistibleList getVariableDefinitions(){
				// Get all the variables ( enums ) associated with this parameter
				// Get the state matrix and get the heading row and the 
				// condition 
				DefaultPersistibleList variableDefinitions = 
						new DefaultPersistibleList();
				System.out.println("variableDefinitions " + 
													 variableDefinitions
													 + " stateMatrix " + stateMatrix);
				if (stateMatrix != null)
						variableDefinitions.set(stateMatrix.getMatrixHeading());
				// If there is a condition get that variable (enum) also
				if ( conditionValue != null 
						 && conditionValue instanceof VariableDefinition) 
						variableDefinitions.add(conditionValue);
				
				return variableDefinitions;
		}
		public DefaultPersistibleList getInputs(){
				return inputs;
		}
		public DefaultPersistibleList getOutputs(){
				return outputs;
		}
		public DefaultPersistibleList getFunction(){
				return function;
		}
		public void setConditionValue(Persistible var ){
				conditionValue = var;
		}
		
		public void setInstanceUsabilityStatus(String var ){
				instanceUsabilityStatus = var;
		}
		
		public void setVersionNumber(Integer var ){
				versionNumber = var;
		}
		
		public void setValueMapEntriesContainingMe(java.util.Collection  var ){
				if ( valueMapEntriesContainingMe== null)
						valueMapEntriesContainingMe = new DefaultPersistibleList();
				valueMapEntriesContainingMe.set(var);
		}
		
		public void setPossibleOutcomes(java.util.Collection  var ){
				if ( possibleOutcomes== null)
						possibleOutcomes = new DefaultPersistibleList();
				possibleOutcomes.set(var);
		}
		
		public void setStateMatrix(StateMatrix var ){
				stateMatrix = var;
				//System.out.println("setStateMatrix in ctp " + stateMatrix ) ;
		}
		
		public void setVariableDefinitions(java.util.Collection  var ){
				if ( variableDefinitions== null)
						variableDefinitions = new DefaultPersistibleList();
				variableDefinitions.set(var);
		}
		
		public void setInputs(java.util.Collection  var ){
				if ( inputs== null)
						inputs = new DefaultPersistibleList();
				inputs.set(var);
		}
		
		public void setOutputs(java.util.Collection  var ){
				if ( outputs== null)
						outputs = new DefaultPersistibleList();
				outputs.set(var);
		}
		
		public void setFunction(java.util.Collection  var ){
				if ( function== null)
						function = new DefaultPersistibleList();
				function.set(var);
		}
		// Add one element to the function list
		public void setFunction(ConditionalDiscreteStateFunction  var ){
				if ( function== null)
						function = new DefaultPersistibleList();
				function.add(var);
		}
		
		public String toString() {
				return getNotation();
		}
		public String getNotation() {
				Vector headings = null;
				if ( stateMatrix == null ) 
						return getDefaultName();
				
				headings = stateMatrix.getMatrixHeading();
				if (headings == null)
						return getDefaultName();
				
				StringBuilder setNotation = new StringBuilder();
				for ( int i = 0; i < headings.size(); i++) {
						setNotation.append(headings.elementAt(i));
						if ( i+1 < headings.size() )
								setNotation.append(" * ");
				}
				if ( conditionValue != null ) {
						setNotation.append(" | ");
						setNotation.append(conditionValue);
				}
				return setNotation.toString();
		}
		
		public Class getPanelClass() {
				return ConditionalTableParameterPanel.class;
		}
		public String getPrettyName() {
				return "ConditionalTable";
		}
		public ImageIcon getIcon() {
				return icon;
		}
		
		// Implement SingleParameter
		
		public  ParameterType getParameterType(){
				return ParameterType.CONDITIONAL_TABLE_PARAMETER;
		}
		public void initSingleParams() {
				setDefaultName("Conditional.Table");
				setSingleParameterID("Conditional.Table");
				singleParameterList.add(this);
		}
		
		public String getID(){
				if(idString == null)
						idString = getGUID().toString() + getSingleParameterID();
				return(idString);
		}
		public String getSingleParameterID() {
				return(singleParameterID);
		}
		public void setSingleParameterID(String id) {
				singleParameterID = id;
		}
		public String getDefaultName(){ return defaultDisplayName; }
		
		public String getCodeValue(){
				return getDisplayName(); //toString();
		}
		public String getDisplayValue(){ return toString();
		}
		public void setDisplayValue(Object value){}
		
		public void setDisplayName(String displayName) {
				this.displayName = displayName;
				if ( this instanceof Persistible) {
						update();
				}
		}
		public String getDisplayName() {
				if ( displayName == null ) 
						return defaultDisplayName;
				else
						return displayName;
		}
		protected void setDefaultName(String defaultDisplayName) {
				this.defaultDisplayName = defaultDisplayName;
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
		public EditorPanel getParameterEditorPanel() {
				return(new ConditionalParameterEditor());	
		}
		public EditorPanel getParameterEditorPanelWithInstance() {
				System.out.println("ConditionalTable");
				
				EditorPanel ep = new ConditionalParameterEditor();
				//ep.setPreferredSize(new Dimension(500,500));
				ep.edit(this);
				return(ep);
		}
		
		public Parameter getParameter(){
				return this;
		}
		
		public Vector getLevelDistribution(Hashtable levelsIDependOn) {
				Vector levelDistribution = new Vector();
				// Assemble an ordered list of probabilities
				// Based on the values set in levelsIDependOn 
				// find the correct CDSF
				// ex levelsIDependOn=[GENDER=MALE] and 
				// this parameter represents smoking | gender
				// locate the cdsf that has GENDER=MALE
				EnumDefinition enumDefinition = null;
				if ( getConditionValue() != null 
						 && getConditionValue() instanceof EnumDefinition)
						enumDefinition = (EnumDefinition)getConditionValue(); 
				// Change condition value to a enum and level
				EnumLevel enumLevel =  null;
				ConditionalDiscreteStateFunction cdsf = null;
				if ( enumDefinition != null ) {
						//get the level from the levelsIDependOn hashtable 
						//using the EnumDefinition as the key
						enumLevel = 
								(EnumLevel)levelsIDependOn.get(enumDefinition);
						cdsf = getCDSFWithCondition(enumLevel);
				}
				if ( cdsf != null ) {
						// First get a list of all statematrix rows
						// get the outcome in order for each row
						Vector probabilitiesInOrder = cdsf.getOutcomes();
						Vector distribution = 
								multinomialGetDistribution(1, probabilitiesInOrder);
						System.out.println("distribution " + distribution);
				}
				// Just so there is no confusion assemble levels with the 
				// distribution
				int i = 0;
				// 	while ( i < probabilitiesInOrder.size() ) {
				// 						// What levels correspond to this probability
				// 						levelDistribution.addElement(
				// 													 new Distribution(stateMatrixRows.get(i), 
				// 																						distribution.get(i)));
				// 				}
				return new Vector();
		}
		
		public ConditionalDiscreteStateFunction 
				getCDSFWithCondition(EnumLevel level) {
				Vector fns = getFunction();
				int i = 0;
				while ( fns != null && i < fns.size() ){
						Object cvObject = 
								((ConditionalDiscreteStateFunction)fns.elementAt(i)).getConditionValue();
						if ( cvObject == null && level == null ){
							// There is no condition value 
							return (ConditionalDiscreteStateFunction)fns.elementAt(i);
						}
						else if ( cvObject instanceof EnumLevel ) {
								if ( ((EnumLevel)cvObject).equals(level) ) 
										return (ConditionalDiscreteStateFunction)fns.elementAt(i);
						}
						i++;
				}
				return null;
		}
		// Temporary dummy method 
		public Vector multinomialGetDistribution(int numberOfTries, 
																						 Vector probabilityDistribution) {
				Vector distribution = new Vector();
				for ( int i = 0; i < probabilityDistribution.size(); i++) {
						distribution.addElement
								(new Integer(i));
				}
				return distribution;
		}
		
		//Implement Instruction
		public Collection<String> getAllVariables(ValueMapPath path){
				// Return a list of all the eum definitions used to build this 
				// table
				return (new Vector());
		}
		public Collection<VariableDependency> getVariableDependencies(ValueMapPath path){
				// Get all the variables in the condition - currently
				// the table only supports 1 condition - conditionvalue would 
				// need to change to a vector and ui changed 
				
				return (new Vector());
		}
		
		public Collection<String> getSetVariables(ValueMapPath path){
				// Same as getAllVariables because all the variables used will need to
				// be declares and/or initialized if not already done.
				return getAllVariables(path);
		}
		public ClassSectionDeclaration getSectionDeclaration(){
				return(null);
		}
		public InstructionProvider getEnclosingInstructionProvider(){
			
				return(this);
		}
		public Object clone() {
			ConditionalTableParameter newInstance  = null;
			try {
					newInstance = (ConditionalTableParameter)getClass().newInstance();
			} catch ( Exception e) {
					System.out.println("Problem cloning: " + getClass());
					e.printStackTrace();
			}
			// Copy the condition value
			newInstance.setConditionValue(getConditionValue()); // Enum

			// Copy the outcomes and the state matrix rows
			StateMatrix stateMatrix = null;
			StateMatrix clonedStateMatrix = null;
			HashMap<StateMatrixRow, StateMatrixRow> matrixRowMap = null;
			StateMatrixRow clonedRow = null;
			try {
				clonedStateMatrix = StateMatrix.class.newInstance();
				stateMatrix = getStateMatrix();
				DefaultPersistibleList stateMatrixRows = stateMatrix.getStateMatrixRows();
				DefaultPersistibleList clonedStateMatrixRows = null;
	
				if ( stateMatrixRows != null && stateMatrixRows.size() > 0) { 
					clonedStateMatrixRows = new DefaultPersistibleList();
					matrixRowMap = new HashMap<StateMatrixRow, StateMatrixRow>();
				}
				for ( Object obj : stateMatrixRows){
					if ( obj instanceof StateMatrixRow ){
						clonedRow = (StateMatrixRow)((StateMatrixRow)obj).clone();
						clonedStateMatrixRows.add(clonedRow);
						// also create a temporary map of teh rows for later use in constructing the
						matrixRowMap.put((StateMatrixRow)obj, clonedRow);
						// functions / outcomes
					}
				}
				clonedStateMatrix.setStateMatrixRows(clonedStateMatrixRows);
			} catch ( Exception e) {
				System.out.println("Problem cloning: StateMatrix");
				e.printStackTrace();
			}
			// Copy the functions
			ConditionalDiscreteStateFunction function = null;
			ConditionalDiscreteStateFunction clonedFunction = null;
			ConditionalOutcome outcome = null;
			ConditionalOutcome clonedOutcome = null;
			DefaultPersistibleList clonedOutcomes = null;
			for ( Object f : getFunction() ) {
				// For each function order is important here
				function = (ConditionalDiscreteStateFunction)f;
				clonedFunction = new ConditionalDiscreteStateFunction();
				clonedFunction.setConditionValue(function.getConditionValue()); // EnumLevel
				clonedOutcomes = new DefaultPersistibleList();
				for ( Object o : function.getOutcomes() ){
					outcome = (ConditionalOutcome)o;
					try {
						clonedOutcome = outcome.getClass().newInstance();
						clonedOutcome.setConditionValue(outcome.getConditionValue());
						clonedRow = matrixRowMap.get(outcome.getStateMatrixRow());
						if ( clonedRow != null ){
							clonedOutcome.setStateMatrixRow(clonedRow);
						}
						clonedOutcome.setOutcome(outcome.getOutcome());
						clonedOutcomes.add(clonedOutcome);
					}
					catch ( Exception ex ){
						System.out.println("Error trying to clone outcome.");
					}
				}
				clonedFunction.setConditionValue(function.getConditionValue());
				clonedFunction.setFunctionParameter(newInstance);
				clonedFunction.setOutcomes(clonedOutcomes);
				newInstance.setFunction(clonedFunction);

			}
			
			newInstance.setStateMatrix(clonedStateMatrix);
			// Create variable definitions for clone - easier than cloning them
			newInstance.setVariableDefinitions(newInstance.getVariableDefinitions());

			return newInstance;
		}
		public ProcessDeclaration getProcessDeclaration(){
			return null;
		}
		public Collection<InstructionProvider> getAdditionalProviders()
		{
			return(getVariableDefinitions());
		}
		public Collection<Instruction> getInstructions()
		{
			return new Vector<Instruction>();
		}
		public Collection<ProcessDeclaration> getReferencedProcesses(){
			Vector<ProcessDeclaration> decls = new Vector<ProcessDeclaration>();
			EnumDefinition en;
			Iterator it = getVariableDefinitions().iterator();
			while(it.hasNext())
			{
				Object nxt = it.next();
				if(nxt instanceof EnumDefinition)
				{
					en = (EnumDefinition) nxt;
					ProcessDeclaration pDec = en.getProcessDeclaration();
					if(pDec != null)
						decls.add(pDec);
				}
			}
			return(decls);	
		}
}
 
