package oncotcap.datalayer.persistible;

public class ProblemContext extends Keyword
{
	 public String originator;
	 public String name;
	 public String description;

	public ProblemContext(oncotcap.util.GUID guid){
		super(guid);
		  keyword = new String();
	 }

	 public ProblemContext() {
		  keyword = new String();
	 }
	 
	 public ProblemContext(String problemContext) {
		  keyword = problemContext;
	 }	

		public String toString() {
				return keyword;
		}
}	 
