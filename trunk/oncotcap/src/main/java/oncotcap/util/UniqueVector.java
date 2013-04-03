package oncotcap.util;

import java.io.*;
import java.util.*;

public class UniqueVector extends Vector {
		public boolean add(Object obj) {
				if ( !this.contains(obj) ) {
						super.add(obj);
						return true;
				}
				return false;
		}
}
