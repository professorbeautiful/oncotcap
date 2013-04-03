package oncotcap.process.treatment;

import java.util.*;
import oncotcap.util.*;

public class DayList extends Vector {
	/** This must be changed to allow for administration durations.
	 ** Probably DayList will end up being an interface,
	 ** and Time will end up being an abstract class.
	 **/
	public DayList(String days) throws Throwable{
		super();
		String dayString = days;
		int i;
		while((i=dayString.indexOf(","))>0 || dayString.length()>0) {
			String thisChunk;
			if(i<=0)
				thisChunk = dayString;
			else
				thisChunk = dayString.substring(0,i);
			thisChunk = thisChunk.trim();
			//Logger.log("i= " + i + "  dayString " + dayString + "  thisChunk: " + thisChunk);
			try{
				if (thisChunk.toUpperCase().startsWith("Q")){
					expandQNotation(thisChunk);
				} else {
					Integer itemp = new Integer(thisChunk);
					add(itemp);
					//Logger.log("DayList size is " + size() + "string is " + thisChunk +
					//				   "  value is /" + itemp + "or" + itemp.toString() + " or " + itemp.intValue());
				}
			}
			catch (NumberFormatException e){
				Logger.log("DayList:  bad number format: thisChunk = /" + thisChunk + "/");
				throw(e);
			}
			if (i<=0)
				dayString = "";
			else
				dayString = dayString.substring(i+1);
		}
	}
	public void expandQNotation(String thisChunk) throws Throwable{
		int whereIsX = thisChunk.toUpperCase().indexOf('X');
		int whereIsAt = thisChunk.toUpperCase().indexOf('@');
		
		String everyS= thisChunk.substring(1,whereIsX);
		int every = (new Integer(everyS)).intValue();
		String repeatS;
		int start;
		if (whereIsAt<=0) {
			repeatS = thisChunk.substring(whereIsX+1);
			start = every;
		} else {
			repeatS = thisChunk.substring(whereIsX+1,whereIsAt);
			String startS = thisChunk.substring(whereIsAt+1);
			start = (new Integer(startS)).intValue();
		}
		//Logger.log("everyS " + everyS + "    repeatS " + repeatS);
		int repeat = (new Integer(repeatS)).intValue();
		for (int i = 1; i<=repeat; i++)
			add(new Integer(every*(i-1) + start));
	}

	public String toString(){
		//Logger.log("DayList in toString() size is " + size());
		String dayNumbersParsed = "";
		Iterator i = iterator();
		while (i.hasNext()){
			if (dayNumbersParsed.length()!=0)
				dayNumbersParsed = dayNumbersParsed.concat(" & " );
			dayNumbersParsed = dayNumbersParsed.concat(i.next().toString());
			//Logger.log("DayList in toString() size is " + size() + "  return: " + dayNumbersParsed);
		}
		return (dayNumbersParsed);
	}
}