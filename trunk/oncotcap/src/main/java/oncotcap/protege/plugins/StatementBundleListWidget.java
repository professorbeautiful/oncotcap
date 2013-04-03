package oncotcap.protege.plugins;


import java.util.*;
import java.awt.*;
import javax.swing.*;
import edu.stanford.smi.protege.widget.*;
import edu.stanford.smi.protege.action.*;
import edu.stanford.smi.protege.event.*;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.util.*;
import edu.stanford.smi.protege.ui.*;

import oncotcap.datalayer.persistible.StatementTemplate;
import oncotcap.datalayer.persistible.StatementBundle;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.datalayer.*;
import oncotcap.util.*;


/**
 * Standard widget for acquiring and displaying instances in an ordered list.
 *
 * @author    Ray Fergerson <fergerson@smi.stanford.edu>
 */
public class StatementBundleListWidget extends AbstractListWidget {

	StatementBundle statementBundle;
	StatementTemplate statementTemplate = null;

	private AllowableAction _createInstanceAction;
	private AllowableAction _addInstancesAction;
	private AllowableAction _removeInstancesAction;
	private AllowableAction _deleteInstancesAction;
	private AllowableAction _viewInstanceAction;
	private boolean _showNewInstances = true;

	private FrameListener _instanceListener = new FrameAdapter() {
		public void browserTextChanged(FrameEvent event) {
			super.browserTextChanged(event);
			// Log.trace("changed", this, "browserTextChanged");
			repaint();
		}
	};

	protected void addButtons(LabeledComponent c) {
		addButton(getViewInstanceAction());
		addButton(getCreateInstanceAction());
		addButton(new ReferencersAction(this));
		addButton(getAddInstancesAction());
		addButton(getRemoveInstancesAction());
		addButton(getDeleteInstancesAction());
	}

	public void addItem(Object item) {
		super.addItem(item);
		addListener(CollectionUtilities.createCollection(item));
	}

	public void addItems(Collection items) {
		super.addItems(items);
		addListener(items);
	}

	protected void addListener(Collection values) {
		Iterator i = values.iterator();
		while (i.hasNext()) {
			Instance instance = (Instance) i.next();
			instance.addFrameListener(_instanceListener);
		}
	}

	public void dispose() {
		removeListener(getValues());
		super.dispose();
	}

	protected Action getAddInstancesAction() {
		_addInstancesAction = new AddAction("Select SB's") {
			public void onAdd() {
				handleAddAction();
			}
		};
		return _addInstancesAction;
	}

	public Action getCreateInstanceAction() {
		_createInstanceAction = new CreateAction("Create SB") {
			public void onCreate() {
				handleCreateAction();
			}
		};
		return _createInstanceAction;
	}

	protected Action getDeleteInstancesAction() {
		_deleteInstancesAction = new DeleteInstancesAction("Delete Selected Instances", this);
		return _deleteInstancesAction;
	}

	protected Action getRemoveInstancesAction() {
		_removeInstancesAction = new RemoveAction("Remove Selected Instances", this) {
			public void onRemove(Object o) {
				handleRemoveAction((Instance) o);
			}
		};
		return _removeInstancesAction;
	}

	protected Action getViewInstanceAction() {
		_viewInstanceAction = new ViewAction("View SB's", this) {
			public void onView(Object o) {
				handleViewAction((Instance) o);
			}
		};
		return _viewInstanceAction;
	}

	protected void handleAddAction() {
		Collection clses = getCls().getTemplateSlotAllowedClses(getSlot());
		String title = (String) _addInstancesAction.getValue(Action.SHORT_DESCRIPTION);
		//addItems(DisplayUtilities.pickInstances(InstanceListWidget.this, clses, title));
		addItems(DisplayUtilities.pickInstances(this, clses, title));
	}

	protected void handleCreateAction() {
			/*

			oncotcap.datalayer.OncoTCapDataSource dataSource = 
			oncotcap.Oncotcap.getDataSource();
			Class stClass = 
			ReflectionHelper.classForName("oncotcap.datalayer.persistible.StatementTemplate");
			// THis method has been deleted this widgte needs work to 
			// be able to work with ProtegeDaatSource - not going to 
			// make changes since this widget may also be obsolete
		Collection statementTemplates = dataSource.find(stClass);
		Object retval = oncotcap.display.common.ListDialog.getValue(
			"CREATE or pick a Template on which to base your modeling statement",
			new Vector(statementTemplates),
			true);   // Add CREATE row.
		System.out.println("handleCreateAction:  retval is " + retval + "  CREATE is " + ListDialog.CREATE);
		if (retval instanceof StatementTemplate)
			statementTemplate = (StatementTemplate) retval;
		if (ListDialog.CREATE.equals(retval)) {
			statementTemplate = (StatementTemplate) EditorDialog.showEditor(new StatementTemplate());
			statementTemplate.update();
		}
		if (statementTemplate == null)
			return;
		statementBundle = new StatementBundle(statementTemplate);
		statementBundle.update();
		String guid = statementBundle.getGUID().toString();
		System.out.println("statementBundle.getGUID().toString() equals " + guid);
		Instance inst = getKnowledgeBase().getInstance(guid);
		addItem(inst);
		statementBundle =
						 (StatementBundle)EditorDialog.showEditor(statementBundle);
			*/
	}

	protected void handleRemoveAction(Instance instance) {
		removeItem(instance);
	}

	protected void handleViewAction(Instance instance) {
		StatementBundle statementBundle = (StatementBundle) getPersistible(instance);
		System.out.println("handleViewAction: statementBundle " + statementBundle);
		statementBundle =
						 (StatementBundle)EditorDialog.showEditor(statementBundle);
	}

	Persistible getPersistible (Instance inst) {
		GUID guid = GUID.fromString(inst.getName());
		System.out.println("getPersistible: guid = " + guid);
		ProtegeDataSource dataSource = (ProtegeDataSource) DataSourceStatus.getDataSource();
		return (dataSource.find(guid));   // This should also be an abstract method of OncoTCapDataSource.
	}

	public void setRenderer () {
		super.setRenderer(new ListCellRenderer() {
			public Component getListCellRendererComponent(
				JList list,
				Object value,
				int index,
				boolean isSelected,
				boolean cellHasFocus)
			{
				System.out.println("In  StatementBundleListWidget:  getListCellRendererComponent, \n"
								   + "   value arg is of type " + value.getClass().getName());

				StatementBundle  sb = (StatementBundle) getPersistible((Instance)value );
				if (sb == null)
					System.out.println("Error:  sb is null in return from getPersistible");
				//StatementTemplateEditorPane pane;
				//pane = new StatementTemplateEditorPane( sb );
				EditorPanel panel = sb.getEditorPanelWithInstance();
				//while (panel.getText().indexOf("\n") >= 0)
				//	panel.setText(panel.getText().substring(0,panel.getText().indexOf("\n"))
				//				  + panel.getText().substring(panel.getText().indexOf("\n")));

				panel.getPreferredSize().setSize(100.0, 50.0);
				
				panel.setBackground(isSelected ? Color.blue : Color.lightGray.brighter());
				panel.setBorder(BorderFactory.createEtchedBorder());
				//setForeground(isSelected ? Color.white : Color.black);
				return panel;
			}
		}
		);
	}
	public void setInstance(Instance instance) {  //overrides super
		super.setInstance(instance);
		//_list.setCellRenderer(new OwnSlotValueFrameRenderer(instance, getSlot()));
		setRenderer();
	}

	public void setRenderer(ListCellRenderer renderer) {
		this.setRenderer();
	}
	public void initialize() {
		super.initialize();
		System.out.println( "StatementBundleListWidget initialized");
		addButtons(getLabeledComponent());
		setRenderer();   ///FrameRenderer.createInstance()  factory method.
	}

	public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
		boolean isSuitable;
		if (cls == null || slot == null) {
			isSuitable = false;
		} else {
			boolean isInstance = cls.getTemplateSlotValueType(slot) == ValueType.INSTANCE;
			boolean isMultiple = cls.getTemplateSlotAllowsMultipleValues(slot);
			isSuitable = isInstance && isMultiple;
		}
		return isSuitable;
	}

	protected void removeListener(Collection values) {
		Iterator i = values.iterator();
		while (i.hasNext()) {
			Instance instance = (Instance) i.next();
			instance.removeFrameListener(_instanceListener);
		}
	}

	public void setEditable(boolean b) {
		setAllowed(_createInstanceAction, b);
		setAllowed(_addInstancesAction, b);
		setAllowed(_removeInstancesAction, b);
		setAllowed(_deleteInstancesAction, b);
	}

	public void setValues(Collection values) {
		removeListener(getValues());
		addListener(values);
		super.setValues(values);
	}

	public boolean getShowNewInstances() {
		return _showNewInstances;
	}

	public void setShowNewInstances(boolean b) {
		_showNewInstances = b;
	}
}
