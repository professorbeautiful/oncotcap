package oncotcap.process.clinicaltrial;

public class TrialInfo
{
	public int n1;
	public int r1;
	public int n;
	public int r;
	private String description = null;
	private TrialInfo(){}
	public TrialInfo(int n1, int r1, int n, int r)
	{
		this.n1 = n1;
		this.n = n;
		this.r1 = r1;
		this.r = r;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDescription()
	{
		if (description == null)
		{
			description =  "<HTML>"
						 + "<CENTER>"
						 + "<I>DESIGN:</I>  "
						 + "First Stage (n1) = " + n1
						 + ",   must exceed "
						 + " " + r1 + " responses,"
						 + "<BR>"
						 + "         Full sample size (n) = " + n + ",   must exceed "
						 + " " + r + " responses."
						 + "</HTML>";
		}
		return(description);
	}

	public String toString()
	{
		return(getDescription());
	}

}