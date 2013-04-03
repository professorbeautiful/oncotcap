package oncotcap.util;
import java.util.*;

public class Descriptors extends Vector {
	public boolean hasDescriptor (String s) {
		Iterator iter = iterator();
		while (iter.hasNext()){
			if (s.equals((String)iter.next()))
				return(true);
		}
		return(false);
	}
}
