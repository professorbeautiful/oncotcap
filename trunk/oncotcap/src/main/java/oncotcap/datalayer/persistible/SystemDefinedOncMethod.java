package oncotcap.datalayer.persistible;

import java.util.Vector;
import java.util.Iterator;
import java.util.Collection;
import oncotcap.util.ReflectionHelper;
import oncotcap.Oncotcap;

public class SystemDefinedOncMethod extends MethodDeclaration
{
	private static Vector allSystemDefinedMethods = new Vector();
	static{ initAllMethods(); }
	
	private static void initAllMethods()
	{
		Class clsOncMethod =  ReflectionHelper.classForName("oncotcap.datalayer.persistible.SystemDefinedOncMethod");
		Collection methods = Oncotcap.getDataSource().find(clsOncMethod);
		Iterator it = methods.iterator();
		while(it.hasNext())
		{
			SystemDefinedOncMethod om = (SystemDefinedOncMethod) it.next();
			allSystemDefinedMethods.add(om);
			String sectionName = om.getName().trim();
			if(sectionName.equals("init"))
				INIT_SECTION = om;
			else if(sectionName.equals("update"))
				UPDATE_SECTION = om;
			else if(sectionName.equals("collection update"))
				COLLECTION_UPDATE_SECTION = om;
			else if(sectionName.equals("collection init"))
				COLLECTION_INIT_SECTION = om;
		}
	}
	
	public static SystemDefinedOncMethod INIT_SECTION;
	public static SystemDefinedOncMethod UPDATE_SECTION;
	public static SystemDefinedOncMethod COLLECTION_INIT_SECTION;
	public static SystemDefinedOncMethod COLLECTION_UPDATE_SECTION;
	
	public SystemDefinedOncMethod() {
	}
	public SystemDefinedOncMethod(oncotcap.util.GUID guid) {
		super(guid);
	}
	public static Vector getAll()
	{
		return(allSystemDefinedMethods);
	}
	public static void main(String [] args)
	{
		Vector all = getAll();
		Iterator it = all.iterator();
		while(it.hasNext())
			System.out.println(it.next());
	}
}