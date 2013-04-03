package oncotcap.util;

import java.lang.reflect.*;
import java.util.*;
import oncotcap.datalayer.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.storage.clips.*;

/**
 **  Relink.  A utility to link existing Protege instances together
 **  where a one way link previously existed and a new inverse slot is
 **  added.  Give relink the project (-p <full path to project>), the
 **  classname of the class that previously contained the slot (now an
 **  inverse slot) pointing to instance(s) of a class where the new
 **  inverse slot was created (-c <Protege class name>), and the old
 **  slot where the instances to be relinked currently exist (-s
 **  <protege slot name)
 **
 **  ex: java oncotcap.util.Relink -p u:\oncotcap\TcapData\oncotcap.pprj -c CodeBundle -s actions
 **
 **/
public class Relink  {
		static String projectName = null;
		static String className = null;
		static String slotName = null;

		public static void main(String[] args) {
				handleCommandLine(args);
				Collection protegeErrors = new ArrayList();
				Project project = new Project(projectName,
															protegeErrors);
				if ( project == null ) {
						System.out.println("Invalid project name.");
						System.exit(1);
				}
				KnowledgeBase kb = project.getKnowledgeBase();
				
				Vector existingValues = new Vector();

				Cls cls = kb.getCls(className);
				if ( cls == null ) {
						System.out.println("Invalid class name.");
						System.exit(1);
				}
		
				Slot slot = kb.getSlot(slotName);
				if ( slot == null ) {
						System.out.println("Invalid slot name.");
						System.exit(1);
				}
				
				// For each instance of the cls collect all the existing instances
				// in the slot and add them to the cls again
				Collection instances = cls.getInstances();
				Iterator i = instances.iterator();
				while ( i.hasNext()) {
						Instance obj = (Instance)i.next();
						Collection values = obj.getOwnSlotValues(slot);
						obj.setOwnSlotValues(slot, values);
				}
				try {
						project.save(protegeErrors);
						if ( protegeErrors.size() > 0 ) {
								System.out.println(protegeErrors);
						}
				}
				catch (Exception e) {
						e.printStackTrace();
				}
    }

public static void handleCommandLine(String [] args)
	{
		int n;
		if (args.length < 6) {
				System.out.println("syntax: Relink -p  projectPath -c class -s slot");
				System.exit(1);
		}
				
		for(n = 0; n < args.length; n++)
		{
			System.out.println("ARG:" + n + " " + args[n]);
			if(args[n].toLowerCase().startsWith("-c"))
			{
					className = args[n+1];
			}
			else if(args[n].toLowerCase().startsWith("-s"))
			{
					slotName = args[n+1];
			}
			else if(args[n].toLowerCase().startsWith("-p"))
			{
					projectName = args[n+1];
			}
		}
		if ( className == null || projectName == null || slotName == null) {
				System.out.println("syntax: Relink -p  projectPath -c class -s slot");
				System.out.println("all arguments are REQUIRED");
				System.exit(1);
		}
	}

}

