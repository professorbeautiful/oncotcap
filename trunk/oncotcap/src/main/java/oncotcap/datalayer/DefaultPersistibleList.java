package oncotcap.datalayer;

import java.util.*;

public class DefaultPersistibleList extends Vector
		implements PersistibleList
{
		public void set(Collection allItems) {
				this.clear();
				if ( allItems != null ) 
					this.addAll(allItems);
		}
		public Iterator getIterator(){
				return this.iterator();
		}
}
