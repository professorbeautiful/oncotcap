package oncotcap.protege.plugins;

import edu.stanford.smi.protegex.widget.abstracttable.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.widget.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.protegex.widget.instancetable.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;

/**
 *  Description of the Class
 *
 * @author    William Grosso <grosso@smi.stanford.edu>
 */
public class OInstanceTableWidget extends InstanceTableWidget  {


    private boolean _isEditable;

    public OInstanceTableWidget() {
        setPreferredRows(2);
        setPreferredColumns(2);
    }

    protected void addActionButtonsToComponent(LabeledComponent centerPiece) {
        if (_state.isDisplayViewInstanceButton()) {
            centerPiece.addHeaderButton(new Action_ViewInstance(this, _displayTable));
        }
        if (_state.isDisplayCreateInstanceButton()) {
            centerPiece.addHeaderButton(new Action_OCreateInstance(this));
        }
        if (_state.isDisplayAddInstanceButton()) {
            centerPiece.addHeaderButton(new Action_AddInstance(this));
        }
        if (_state.isDisplayRemoveInstanceButton()) {
            centerPiece.addHeaderButton(new Action_RemoveInstance(this, _displayTable));
        }
        if (_state.isDisplayDeleteInstanceButton()) {
            centerPiece.addHeaderButton(new Action_DeleteInstance(this, _displayTable));
        }
        if (_state.isDisplayPrototypeButton()) {
            centerPiece.addHeaderButton(new Action_CreateUsingPrototype(this, _displayTable));
        }
    }

    public void addValue(Instance value) {
        if (!_values.contains(value)) {
            _values.add(value);
            _tableModel.setValues(_values);
            if (_state.isAutoSelectInsertions()) {
                int rowNumber = _values.size() - 1;
                _displayTable.setRowSelectionInterval(rowNumber, rowNumber);
            }
            valueChanged();
        }
        return;
    }

    public void addValues(Collection values) {
        if (null == values) {
            return;
        }
        Iterator i = values.iterator();
        while (i.hasNext()) {
            addValue((Instance) i.next());
        }
    }

    protected void buildTableComponents() {
        _state = new InstanceTableWidgetState(getPropertyList(), getAllowedClses(), getKnowledgeBase());
        _values = new ArrayList();
        _tableModel = new InstanceTableModel(null, _values, _state);
        _displayTable = new InstanceTable(_tableModel, _state.isHighlightSelectedRow());
        _tableModel.setUnderlyingTable(_displayTable);
        _displayTable.setEditor(createEditor());
    }

    private AbstractTableWidgetCellEditor createEditor() {
        if (_state.isEditInPlace()) {
            return new AbstractTableWidgetCellEditor(_displayTable, _state, getProject());
        }
        return null;
    }

    public WidgetConfigurationPanel createWidgetConfigurationPanel() {
        // hack to make sure our state is consistent with the plist.
        _state = new InstanceTableWidgetState(getPropertyList(), getAllowedClses(), getKnowledgeBase());
        return new InstanceTableConfigurationPanel(this);
    }

    public void dispose() {
        super.dispose();
    }

    public Collection getAllowedClses() {
        return getCls().getTemplateSlotAllowedValues(getSlot());
    }

    public InstanceTableWidgetState getState() {
        return _state;
    }

    public Collection getValues() {
        return _values;
    }

    public void initialize() {
        buildTableComponents();
        setTableColumnWidths();
        LabeledComponent centerPiece = new LabeledComponent(getLabel(), ComponentFactory.createScrollPane(_displayTable));
        addActionButtonsToComponent(centerPiece);
        JComponent warnings = InstanceTableConfigurationChecks.getShortWarning(getCls(), getSlot());
        if (null != warnings) {
            JPanel panelWithWarning = new JPanel(new BorderLayout());
            panelWithWarning.add(centerPiece, BorderLayout.CENTER);
            panelWithWarning.add(warnings, BorderLayout.SOUTH);
            add(panelWithWarning);
        } else {
            add(centerPiece);
        }
        if (false == isRuntime()) {
            (_displayTable.getTableHeader()).setReorderingAllowed(false);
        }
        return;
    }

    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        return InstanceTableConfigurationChecks.checkValidity(cls, slot);
    }

    public static void main(String[] args) {
        edu.stanford.smi.protege.Application.main(args);
    }

    public void removeValue(Instance value) {
        if (_values.contains(value)) {
            _values.remove(value);
            _tableModel.setValues(_values);
            valueChanged();
        }
        return;
    }

    public void removeValues(Collection values) {
        if (null == values) {
            return;
        }
        Iterator i = values.iterator();
        while (i.hasNext()) {
            removeValue((Instance) i.next());
        }
    }

    public void selectInstanceIfAppropriate(Instance instance) {
        if (_state.isEditInPlace()) {
            return;
        }
        int selectedRow = _displayTable.getRowForInstance(instance);
        (_displayTable.getSelectionModel()).setSelectionInterval(selectedRow, selectedRow);
    }

    public void setEditable(boolean b) {
        _isEditable = b;
    }

    public boolean isEditable() {
        return _isEditable;
    }

    protected void setTableColumnWidths() {
        int counter = 0;
        Iterator i = (_state.getSlotVisibilityDescriptions()).iterator();
        while (i.hasNext()) {
            VisibleSlotDescription vsd = (VisibleSlotDescription) i.next();
            _displayTable.setPreferredWidthForColumn(counter, vsd.preferredSize);
            counter++;
        }
    }

    public void setValues(java.util.Collection values) {
        _displayTable.stopEditing();
        _values.clear();
        if ((null != values) || (0 == values.size())) {
            _values.addAll(values);
        }
        _tableModel.setValues(_values);
        _displayTable.clearSelection();
        repaint();
    }
}

