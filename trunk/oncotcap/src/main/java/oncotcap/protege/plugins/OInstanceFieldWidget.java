/* Override the default ID creation for new instances. Use the
 * oncotcap.util.GUID
 * 
 */
package oncotcap.protege.plugins;


import java.util.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.ui.*;

public class OInstanceFieldWidget 
	 extends  edu.stanford.smi.protege.widget.InstanceFieldWidget {

    protected void handleCreateAction() {
        Collection clses = getCls().getTemplateSlotAllowedClses(getSlot());
        Cls cls = DisplayUtilities.pickConcreteCls(OInstanceFieldWidget.this, clses);
        if (cls != null) {
	    // Use GUID routine to generate the instance
	    // ID
	    String instanceId = oncotcap.util.GUID.nextGUID();
            Instance instance = getKnowledgeBase().createInstance(instanceId, cls);
            if (instance instanceof Cls) {
                Cls newcls = (Cls) instance;
                if (newcls.getDirectSuperclassCount() == 0) {
                    newcls.addDirectSuperclass(getKnowledgeBase().getRootCls());
                }
            }
            showInstance(instance);
            setDisplayedInstance(instance);
        }
    }
}
