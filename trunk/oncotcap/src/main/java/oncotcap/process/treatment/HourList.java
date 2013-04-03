package oncotcap.process.treatment;

import java.util.*;
import oncotcap.util.*;

public class HourList extends Vector {

	public HourList(String hours) throws Throwable{
		super();
		String hourString = hours;
		int i;
		while((i=hourString.indexOf(","))>0 || hourString.length()>0) {
			String thisChunk;
			if(i<=0)
				thisChunk = hourString;
			else
				thisChunk = hourString.substring(0,i);
			thisChunk = thisChunk.trim();
			Logger.log("i= " + i + "  hourString " + hourString + "  thisChunk: " + thisChunk);
			try{
					Double itemp = new Double(thisChunk);
					add(itemp);
					Logger.log("HourList size is " + size() + "string is " + thisChunk +
									   "  value is /" + itemp + " or " + itemp.toString());
				
			}
			catch (NumberFormatException e){
				Logger.log("HourList:  bad number format: thisChunk = /" + thisChunk + "/");
				throw(e);
			}
			if (i<=0)
				hourString = "";
			else
				hourString = hourString.substring(i+1);
		}
	}

	public String toString(){
		Logger.log("HourList in toString() size is " + size());
		String hourNumbersParsed = "";
		Iterator i = iterator();
		while (i.hasNext()){
			if (hourNumbersParsed.length()!=0)
				hourNumbersParsed = hourNumbersParsed.concat(" & " );
			hourNumbersParsed = hourNumbersParsed.concat(i.next().toString());
			Logger.log("HourList in toString() size is " + size() + "  return: " + hourNumbersParsed);
		}
		return (hourNumbersParsed);
	}
}