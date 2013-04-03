/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License");  you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 *
 * The Original Code is Protege-2000.
 *
 * The Initial Developer of the Original Code is Stanford University. Portions
 * created by Stanford University are Copyright (C) 2001.  All Rights Reserved.
 *
 * Protege-2000 was developed by Stanford Medical Informatics
 * (http://www.smi.stanford.edu) at the Stanford University School of Medicine
 * with support from the National Library of Medicine, the National Science
 * Foundation, and the Defense Advanced Research Projects Agency.  Current
 * information about Protege can be obtained at http://protege.stanford.edu
 *
 * Contributor(s):
 */
package oncotcap.protege.plugins;

import edu.stanford.smi.protegex.widget.instancetable.*;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;

import edu.stanford.smi.protege.resource.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.ui.*;

import oncotcap.util.GUID;
/**
 *  Description of the Class
 *
 * @author    William Grosso <grosso@smi.stanford.edu>
 */
public class Action_OCreateInstance extends AbstractAction {
    private String _dialogTitle;
    private OInstanceTableWidget _widget;
    private InstanceTableWidgetState _widgetState;
    private KnowledgeBase _kb;

    public Action_OCreateInstance(OInstanceTableWidget widget) {
        super((widget.getState()).getCreateInstanceButtonTooltip(), Icons.getCreateIcon());
        _widget = widget;
        _widgetState = _widget.getState();
        _dialogTitle = _widgetState.getCreateInstanceDialogTitle();
        _kb = _widget.getKnowledgeBase();
    }

    public void actionPerformed(ActionEvent e) {
        if (_widget.isEditable()) {
            Cls concreteCls = getClsForInstance();
            if (null == concreteCls) {
                return;
            }

            Instance instance = createInstance(concreteCls);
            if (null == instance) {
                return;
            }
            _widget.addValue(instance);
            _widget.selectInstanceIfAppropriate(instance);
            if (_widgetState.isCreateFormForNewInstances()) {
                displayInstanceForm(instance);
            }
        }
    }

    private Instance createInstance(Cls cls) {
	// Use GUID routine to generate the instance
	// ID
	String instanceId = GUID.nextGUID();
        return (_widget.getKnowledgeBase()).createInstance(instanceId, cls);
    }

    private void displayInstanceForm(Instance instance) {
        (_widget.getProject()).show(instance);
    }

    protected Collection getAllowedClasses() {
        Instance instance = _widget.getInstance();
        Slot slot = _widget.getSlot();
        Cls cls = instance.getDirectType();
        return new ArrayList(cls.getTemplateSlotAllowedClses(slot));
    }

    private Cls getClsForInstance() {
        Collection allowedClasses = getAllowedClasses();
        if (allowedClasses.size() == 0) {
            allowedClasses.add(_kb.getRootCls());
        }
        return DisplayUtilities.pickConcreteCls((JComponent) _widget, allowedClasses, _dialogTitle);
    }
}

