package oncotcap.util;

public class IdPropValueList extends SortedList
{
        public String name;
	public IdPropValueList(String name, String [] s)
	{
		super(new StringCompare(), s);
                this.name = name;
	}

        public boolean equals(Object obj)
        {
          if( !(obj instanceof IdPropValueList))
            return(false);

          IdPropValueList id = (IdPropValueList) obj;

          if ( ! id.name.equalsIgnoreCase(this.name) )
            return(false);

          return(super.equals(obj));

        }
}