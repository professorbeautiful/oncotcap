package oncotcap.process.treatment;

public class Dose  {  // cant extend Double !!!
	double dose;
	DoseUnit units;
	public Dose(double dose) {
		this.dose = dose;
	}
	public Dose(double dose, DoseUnit units) {
		this.dose = dose;
		this.units = units;
	}
	public double get(){
		return(dose);
	}
	public void set(double dose){
		this.dose = dose;
	}

	public Object clone()
	{
		return(new Dose(dose, units));
	}
}
