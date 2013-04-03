package oncotcap.datalayer.persistible.parameter;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.editor.persistibleeditorpanel.*;

public class SimonCT extends AbstractParameter
{
	private double alpha;
	private double beta;
	private double prResp0;
	private double prResp1;
	public int r1;
	public int n1;
	public int r;
	public int n;
	private DefaultSingleParameter betaParam;
	private DefaultSingleParameter alphaParam;
	private DefaultSingleParameter pr0Param;
	private DefaultSingleParameter pr1Param;
	private DefaultSingleParameter r1Param;
	private DefaultSingleParameter n1Param;
	private DefaultSingleParameter rParam;
	private DefaultSingleParameter nParam;
	private DefaultPersistibleList variableDefinitions = new DefaultPersistibleList();
	public SimonCT(oncotcap.util.GUID guid){
		super(guid);
	}
	public SimonCT()
	{
		this(true);
	}
	public SimonCT(boolean saveToDataSource)
	{
		super(saveToDataSource);
	}
	public SimonCT(SimonCT des, boolean saveToDataSource)
	{
		super(saveToDataSource);
		setAlpha(des.alpha);
		setBeta(des.beta);
		setProbResp0(des.prResp0);
		setProbResp1(des.prResp1);
		setR1(des.r1);
		setR(des.r);
		setN1(des.n1);
		setN(des.n);
		
/*		this.alpha = des.alpha;
		this.beta = des.beta;
		this.prResp0 = des.prResp0;
		this.prResp1 = des.prResp1;
		betaParam = (DefaultSingleParameter) ((DefaultSingleParameter) des.singleParameterList.getSingleParameter("SimonCT.Beta")).clone();
		alphaParam = (DefaultSingleParameter) ((DefaultSingleParameter) des.singleParameterList.getSingleParameter("SimonCT.Alpha")).clone();
		pr0Param = (DefaultSingleParameter) ((DefaultSingleParameter) des.singleParameterList.getSingleParameter("SimonCT.Pr0")).clone();
		pr1Param = (DefaultSingleParameter) ((DefaultSingleParameter) des.singleParameterList.getSingleParameter("SimonCT.Pr1")).clone();
		r1Param = (DefaultSingleParameter) ((DefaultSingleParameter) des.singleParameterList.getSingleParameter("SimonCT.R1")).clone();
		n1Param = (DefaultSingleParameter) ((DefaultSingleParameter) des.singleParameterList.getSingleParameter("SimonCT.N1")).clone();
		rParam = (DefaultSingleParameter) ((DefaultSingleParameter) des.singleParameterList.getSingleParameter("SimonCT.R")).clone();
		nParam = (DefaultSingleParameter) ((DefaultSingleParameter) des.singleParameterList.getSingleParameter("SimonCT.N")).clone();
*/
	}
	public void initSingleParams()
	{
		betaParam = new DefaultSingleParameter("Beta", this, "SimonCT.Beta"); //0.1
		//betaParam.setDisplayValue("0.1");
		alphaParam = new DefaultSingleParameter("Alpha", this, "SimonCT.Alpha"); //0.1
		//alphaParam.setDisplayValue("0.1");
		pr0Param = new DefaultSingleParameter("Un-useful Response Probability", this, "SimonCT.Pr0"); //0.2
		//pr0Param.setDisplayValue("0.2");
		pr1Param = new DefaultSingleParameter("Useful Response Probablility", this, "SimonCT.Pr1"); //0.4
		//pr1Param.setDisplayValue("0.4");
		r1Param = new DefaultSingleParameter("Number of First-Stage Responses", this, "SimonCT.R1"); //3.0
		//r1Param.setDisplayValue("3.0");
		n1Param = new DefaultSingleParameter("First Stage Sample Size", this, "SimonCT.N1"); //17.0
		//n1Param.setDisplayValue("17.0");
		rParam = new DefaultSingleParameter("Total Number of Responses", this, "SimonCT.R"); //10.0
		//rParam.setDisplayValue("10.0");
		nParam = new DefaultSingleParameter("Full Sample Size", this, "SimonCT.N"); //37.0
		//nParam.setDisplayValue("37.0");
		
		singleParameterList.add(betaParam);
		singleParameterList.add(alphaParam);
		singleParameterList.add(pr0Param);
		singleParameterList.add(pr1Param);
		singleParameterList.add(r1Param);
		singleParameterList.add(n1Param);
		singleParameterList.add(rParam);
		singleParameterList.add(nParam);

	}
	public void addSingleParameter(SingleParameter sp)
	{
		if(sp.getSingleParameterID().equals("SimonCT.Beta"))
		{
			singleParameterList.replace(betaParam, sp);
			betaParam = (DefaultSingleParameter) sp;
		}
		else if(sp.getSingleParameterID().equals("SimonCT.Alpha"))
		{
			singleParameterList.replace(alphaParam, sp);
			alphaParam = (DefaultSingleParameter) sp;
		}
		else if(sp.getSingleParameterID().equals("SimonCT.Pr0"))
		{
			singleParameterList.replace(pr0Param, sp);
			pr0Param = (DefaultSingleParameter) sp;
		}
		else if(sp.getSingleParameterID().equals("SimonCT.Pr1"))
		{
			singleParameterList.replace(pr1Param, sp);
			pr1Param = (DefaultSingleParameter) sp;
		}
		else if(sp.getSingleParameterID().equals("SimonCT.R1"))
		{
			singleParameterList.replace(r1Param, sp);
			r1Param = (DefaultSingleParameter) sp;
		}
		else if(sp.getSingleParameterID().equals("SimonCT.N1"))
		{
			singleParameterList.replace(n1Param, sp);
			n1Param = (DefaultSingleParameter) sp;
		}
		else if(sp.getSingleParameterID().equals("SimonCT.R"))
		{
			singleParameterList.replace(rParam, sp);
			rParam = (DefaultSingleParameter) sp;
		}
		else if(sp.getSingleParameterID().equals("SimonCT.N"))
		{
			singleParameterList.replace(nParam, sp);
			nParam = (DefaultSingleParameter) sp;
		}

	}
	public String getbetaParamName()
	{
		return(betaParam.getDisplayName());
	}
	public void setbetaParamName(String name)
	{
		betaParam.setDisplayName(name);
	}
	public String getalphaParamName()
	{
		return(alphaParam.getDisplayName());
	}
	public void setalphaParamName(String name)
	{
		alphaParam.setDisplayName(name);
	}
	public String getpr0ParamName()
	{
		return(pr0Param.getDisplayName());
	}
	public void setpr0ParamName(String name)
	{
		pr0Param.setDisplayName(name);
	}
	public String getpr1ParamName()
	{
		return(pr1Param.getDisplayName());
	}
	public void setpr1ParamName(String name)
	{
		pr1Param.setDisplayName(name);
	}
	public String getr1ParamName()
	{
		return(r1Param.getDisplayName());
	}
	public void setr1ParamName(String name)
	{
		r1Param.setDisplayName(name);
	}
	public String getn1ParamName()
	{
		return(n1Param.getDisplayName());
	}
	public void setn1ParamName(String name)
	{
		n1Param.setDisplayName(name);
	}
	public String getrParamName()
	{
		return(rParam.getDisplayName());
	}
	public void setrParamName(String name)
	{
		rParam.setDisplayName(name);
	}
	public String getnParamName()
	{
		return(nParam.getDisplayName());
	}
	public void setnParamName(String name)
	{
		nParam.setDisplayName(name);
	}

	public String getValue()
	{
		return("");
	}
	public boolean check()
	{
		return(true);
	}
	public ParameterType getParameterType()
	{
		return(ParameterType.SIMON_TWO_STAGE_DESIGN);
	}
	public Object clone()
	{
		return(clone(true));
	}
	public Parameter clone(boolean saveToDataSource)
	{
		SimonCT rVal = new SimonCT(this, saveToDataSource); 
		rVal.setOriginalSibling(getOriginalSibling());
		return(rVal);
	}
	public String getStringValue()
	{
		return(toString());
	}
	public SingleParameter getMatchingSingleParameter(SingleParameter sp)
	{
		return(singleParameterList.getSingleParameter(sp.getSingleParameterID()));
	}
	public SingleParameter getSingleParameter(String singleParameterID)
	{
		return(singleParameterList.getSingleParameter(singleParameterID));
	}
	public void addVariableDefinition(VariableDefinition varDef)
	{
		variableDefinitions.add(varDef);
	}
	public DefaultPersistibleList getVariableDefinitions(){
		return variableDefinitions;
	}

	public EditorPanel getEditorPanelWithInstance()
	{
		ParameterEditor se = new ParameterEditor();
		se.edit(this);
		return(se);
	}
	public EditorPanel getEditorPanel()
	{
		return(new ParameterEditor());
	}
	public EditorPanel getParameterEditorPanelWithInstance()
	{
		Phase2SimonEditorPanel se = new Phase2SimonEditorPanel();
		se.edit(this);
		return(se);
	}
	public EditorPanel getParameterEditorPanel()
	{
		return(new Phase2SimonEditorPanel());
	}

	public VariableType getType()
	{
		return(null);	
	}

	public void setAlpha(double alpha)
	{
		if(alphaParam == null)
			initSingleParams();

		alphaParam.setDisplayValue(Double.toString(alpha));
		
		this.alpha = alpha;
		update();
	}
	public void setAlpha(String strAlpha)
	{
		if(! (strAlpha == null || strAlpha.trim().equals("")))
			setAlpha(Double.parseDouble(strAlpha));
	}
	public double getAlpha()
	{
		return(alpha);
	}
	public String getAlphaAsString()
	{
		return(Double.toString(alpha));
	}
	public void setBeta(double beta)
	{
		if(betaParam == null)
			initSingleParams();

		betaParam.setDisplayValue(Double.toString(beta));

		this.beta = beta;
		update();
	}
	public void setBeta(String strBeta)
	{
		if(! (strBeta == null || strBeta.trim().equals("")))
			setBeta(Double.parseDouble(strBeta));
	}
	public double getBeta()
	{
		return(beta);
	}
	public String getBetaAsString()
	{
		return(Double.toString(beta));
	}
	public void setProbResp0(double p)
	{
		if(pr0Param == null)
			initSingleParams();

		pr0Param.setDisplayValue(Double.toString(p));

		this.prResp0 = p;
		update();
	}
	public void setProbResp0(String strP)
	{
		if(! (strP == null || strP.trim().equals("")))
			setProbResp0(Double.parseDouble(strP));
	}
	public double getProbResp0()
	{
		return(prResp0);
	}
	public String getProbResp0AsString()
	{
		return(Double.toString(prResp0));
	}
	public void setProbResp1(String strP)
	{
		if(! (strP == null || strP.trim().equals("")))
			setProbResp1(Double.parseDouble(strP));
	}
	public void setProbResp1(double p)
	{
		if(pr1Param == null)
			initSingleParams();

		pr1Param.setDisplayValue(Double.toString(p));

		this.prResp1 = p;
		update();
	}
	public String getProbResp1AsString()
	{
		return(Double.toString(prResp1));
	}
	public double getProbResp1()
	{
		return(prResp1);
	}
	public void setR1(int r)
	{
		if(r1Param == null)
			initSingleParams();

		r1Param.setDisplayValue(Double.toString(r));

		this.r1 = r;
		update();
	}
	public int getR1()
	{
		return(r1);
	}
	public void setR(int r)
	{
		if(rParam == null)
			initSingleParams();

		rParam.setDisplayValue(Double.toString(r));

		this.r = r;
		update();
	}
	public int getR()
	{
		return(r);
	}
	public void setN1(int n)
	{
		if(n1Param == null)
			initSingleParams();

		n1Param.setDisplayValue(Double.toString(n));

		this.n1 = n;
		update();
	}
	public int getN1()
	{
		return(n1);
	}
	public void setN(int n)
	{
		if(nParam == null)
			initSingleParams();

		nParam.setDisplayValue(Double.toString(n));

		this.n = n;
		update();
	}
	public int getN()
	{
		return(n);
	}

/*	public void writeDeclarationSection(Writer write, StatementBundle sb) throws IOException
	{

	}

	public void writeMethodSection(Writer write, StatementBundle sb) throws IOException
	{

	}*/
}
