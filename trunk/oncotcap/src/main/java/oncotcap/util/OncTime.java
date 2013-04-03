package oncotcap.util;

import java.util.Vector;
import oncotcap.datalayer.*;

public class OncTime extends AbstractPersistible
{
	public double time;
	public int unit = -1;
	public String strTime;
	
	public static final int YEARS = 0;
	public static final int MONTHS = 1;
	public static final int DAYS = 2;
	public static final int WEEKS = 3;

	public static final Object intToUnitConversionTable [] = Util.newArray("java.lang.Object", 4);
	public static final Vector allTimeUnits = new Vector();
	public static final TimeUnit YEAR = new TimeUnit("Years", YEARS);
	public static final TimeUnit MONTH = new TimeUnit("Months", MONTHS);
	public static final TimeUnit DAY = new TimeUnit("Days", DAYS);
	public static final TimeUnit WEEK = new TimeUnit("Weeks", WEEKS);
	public static final double timeConversionTable[][] = {
		
		  {	1.0,			//tcapYear,  tcapYear 1#
			12.0,			//tcapYear,  tcapMonth 12#
			360.0,			//tcapYear,  tcapDay 360#
			52.0 },			//tcapYear,  tcapWeek 52#
		  { 0.083333333,	//tcapMonth, tcapYear (1 / 12)
			1.0,			//tcapMonth, tcapMonth 1#
			30.0,			//tcapMonth, tcapDay 30#
			4.33333333 },	//tcapMonth, tcapWeek (52 / 12)
		  { 0.0027777777,	//tcapDay,   tcapYear (1 / 360)
			0.033333333,	//tcapDay,   tcapMonth (1 / 30)
			1.0,			//tcapDay,   tcapDay 1#
			0.14285714 },	//tcapDay,   tcapWeek (1 / 7)
		  { 0.019230769,	//tcapWeek,  tcapYear (1 / 52)
			0.23076923,		//tcapWeek,  tcapMonth (12 / 52)
			7.0,			//tcapWeek,  tcapDay 7#
			1.0	} };		//tcapWeek,  tcapWeek 1#

							/* save for later in case we add HOURS at a later date...									  
							tcapYear, tcapHour = 8640#
							tcapMonth, tcapHour = 720#
							tcapDay, tcapHour = 24#
							tcapWeek, tcapHour = 168#
							tcapHour, tcapYear = (1 / 8640)
							tcapHour, tcapMonth = (1 / 720)
							tcapHour, tcapDay = (1 / 24)
							tcapHour, tcapHour = 1#
							tcapHour, tcapWeek = (1 / 168) */


	public static void main(String [] args)
	{
//		OncTime oneM = new OncTime(MONTHS, 1.0);
//		OncTime fifteenD = new OncTime(DAYS, 15.455);
		OncoTCapDataSource data = oncotcap.Oncotcap.getDataSource();
//		oneM.update();
//		fifteenD.update();
//		data.commit();
		java.util.Collection times = data.find(ReflectionHelper.classForName("oncotcap.util.OncTime"));
		java.util.Iterator timesit = times.iterator();
		while(timesit.hasNext())
			System.out.println(((OncTime) timesit.next()));
		
//		Logger.log("add " + oneM.add(fifteenD));
	}
	public OncTime(oncotcap.util.GUID guid)
	{
		super(guid);
		this.unit = MONTHS;
		this.time = 0.0;
	}
	public OncTime()
	{
		this(MONTHS);
	}

	public OncTime(int unit)
	{
		this(unit, 0.0);
	}
	public OncTime(TimeUnit unit)
	{
		this(unit.getIntVal());
	}
	public OncTime(String unit)
	{
		this(getUnit(unit));
	}
	public OncTime(String unit, double value)
	{
		this(getUnit(unit), value);
	}
	public OncTime(double value, String unit)
	{
		this(getUnit(unit), value);
	}
	public OncTime(double value, int unit)
	{
		this.unit = unit;
		this.time = value;
	}
	public OncTime(double value, TimeUnit unit)
	{
		this(value, unit.getIntVal());
	}
	public OncTime(int unit, double value)
	{
		this.unit = unit;
		this.time = value;
	}
	
	public int getUnit()
	{
		return(unit);
	}
	public static String getUnit(int unit)
	{
		switch(unit)
		{
			case MONTHS:
				return( "Months");
			case WEEKS:
				return("Weeks");
			case DAYS:
				return("Days");
			case YEARS:
				return("Years");
			default:
				return("Unknown");
		}
		
	}

	public void setUnit(TimeUnit unit)
	{
		this.unit = unit.getIntVal();
		update();
	}
	public void setUnit(int unit)
	{
		this.unit = unit;
		update();
	}
	public void convert(int unitToConvertTo)
	{
		time = convert(time, unit, unitToConvertTo);
		unit = unitToConvertTo;
	}
	public void convert(TimeUnit unitToConvertTo)
	{
		convert(unitToConvertTo.getIntVal());
	}
	public static double convert( double time, int oldUnit, int newUnit)
	{
		if(oldUnit >= 0 && oldUnit <=3 && newUnit >= 0 && newUnit <= 3)
			return(time * timeConversionTable[oldUnit][newUnit]);
		else
			return(0.0);
	}
	public static double convert( double time, TimeUnit oldUnit, TimeUnit newUnit)
	{
		return(convert(time, oldUnit.getIntVal(), newUnit.getIntVal()));
	}
	public static double convert(OncTime time, int newUnit)
	{
		return(convert(time.getTime(), time.getUnit(), newUnit));
	}
	public static double convert(OncTime time, TimeUnit newUnit)
	{
		return(convert(time, newUnit.getIntVal()));
	}
	public void setTime(double t)
	{
		setTime(t, unit);
	}
	public void setTime(double t, TimeUnit unit)
	{
		setTime(t, unit.getIntVal());
	}
	public void setTime(double t, int unit)
	{
		this.unit = unit;
		this.time = t;
		this.strTime = Double.toString(t);
		update();
	}
	public void setTime(String t)
	{
		this.strTime = t;
		if(StringHelper.isNumeric(t))
			this.time = Double.parseDouble(t);
		else
			this.time = 0;
		update();
	}
	public String getTimeString()
	{
		return(strTime);
	}
	public double getTime()
	{
		return(time);
	}
	public OncTime add(OncTime a)
	{
		if (unit != a.getUnit())
			return(new OncTime(unit, time + convert(a.getTime(), a.getUnit(), unit)));
		else
			return(new OncTime(unit, time + a.getTime()));
	}
	public OncTime subtract(OncTime b)
	{
		if (unit != b.getUnit())
			return(new OncTime(unit, time - convert(b.getTime(), b.getUnit(), unit)));
		else
			return(new OncTime(unit, time + b.getTime()));
	}

	public String toString()
	{
		return(toString(time, unit));
	}
	
	public static String toString(double time, int unit)
	{
		int iTemp = new Double(time).intValue();
		double remainder = time - iTemp;

		switch(unit)
		{
			case MONTHS:
				return( iTemp + "m " + new Double(convert(remainder, MONTHS, DAYS) + 0.5).intValue() + "d");
			case WEEKS:
				return( iTemp + "w " + new Double(convert(remainder, WEEKS, DAYS) + 0.5).intValue() + "d");				
			case DAYS:
				return(new Double(time + 0.5).intValue() + "d");
			case YEARS:
				return( iTemp + "y " + new Double(convert(remainder, YEARS, DAYS) + 0.5).intValue() + "d");
			default:
				return(new String( time + " " + getUnit(unit) ));
		}
	}
	public static OncTime add(OncTime a, OncTime b)
	{
		return(a.add(b));
	}

	public static OncTime subtract(OncTime a, OncTime b)
	{
		return(a.subtract(b));
	}
	public TimeUnit getTimeUnit()
	{
		if(unit == -1)
			return(null);
		else
			return((TimeUnit) intToUnitConversionTable[unit]);
	}
	private static int getUnit(String unit)
	{
		if (unit.equalsIgnoreCase("MONTHS"))
			return(MONTHS);
		else if(unit.equalsIgnoreCase("MONTH"))
			return(MONTHS);
		else if(unit.equalsIgnoreCase("WEEKS"))
			return(WEEKS);
		else if(unit.equalsIgnoreCase("WEEK"))
			return(WEEKS);
		else if(unit.equalsIgnoreCase("DAYS"))
			return(DAYS);
		else if(unit.equalsIgnoreCase("DAY"))
			return(DAYS);
		else if(unit.equalsIgnoreCase("YEARS"))
			return(YEARS);
		else if(unit.equalsIgnoreCase("YEAR"))
			return(YEARS);

		return(-1);
	}
	public static class TimeUnit
	{
		String name;
		int oldEnumVal;

		private TimeUnit(){}
		private TimeUnit(String name, int oldEnumVal)
		{
			this.name = name;
			this.oldEnumVal = oldEnumVal;
			allTimeUnits.add(this);
			intToUnitConversionTable[oldEnumVal] = this;
		}
		public int getIntVal()
		{
			return(oldEnumVal);
		}
		public String toString()
		{
			return(name);
		}
		public String getVarName()
		{
			return("OncTime." + name.toUpperCase().substring(0,name.length() - 1));
		}
	}
	public static Vector getTimeUnits()
	{
		return(allTimeUnits);
	}
}
