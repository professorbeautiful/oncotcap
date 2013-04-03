package oncotcap.process.treatment;

import java.util.*;

public class DoseUnit {
	/** This could be an Enum.
	 ** Also, Enum could have a constructor based on an entryTable from
	 ** NoviceInterface.pprj
	 **/
	String unit;
	static String[] s={"mg", "ug", "mg/m2", "ug/m2"};
	static ArrayList listOfDoseUnits = new ArrayList((Collection)Arrays.asList(s));

	public DoseUnit(String unit){
			this.unit = unit;
	}
	public static ArrayList getKnownDoseUnits() {
		return (listOfDoseUnits);
	}
	public static void addToKnownDoseUnits(String units){
		listOfDoseUnits.add(units);
	}
}
