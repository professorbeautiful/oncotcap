/**
 * 
 */
package oncotcap.display.browser;

import java.awt.event.ActionEvent;

import javax.swing.Icon;

/**
 * @author morris
 *
 */
public class HelpAction extends OncAbstractAction {

	/**
	 * @param actionName
	 * @param icon
	 */
	public HelpAction(String actionName, Icon icon) {
		super(actionName, icon);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param actionName
	 */
	public HelpAction(String actionName) {
		super(actionName);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		System.out.println("Call for HELP!");

	}

}
