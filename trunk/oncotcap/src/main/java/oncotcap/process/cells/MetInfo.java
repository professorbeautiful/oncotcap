package oncotcap.process.cells;

import oncotcap.process.treatment.AbstractPatient;
import oncotcap.util.OncEnum;

public class MetInfo
{
	public AbstractPatient patient;
	public OncEnum organ;
	
	public MetInfo(AbstractPatient patient, OncEnum organ)
	{
		this.patient = patient;
		this.organ = organ;
	}

	public String toString()
	{
		return(new String("Metastasis occured in the " + organ));
	}

}