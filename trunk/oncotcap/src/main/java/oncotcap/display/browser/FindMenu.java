package oncotcap.display.browser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

import oncotcap.datalayer.SearchText;

/**  Completion of this will await another day (or maybe not).
 * @author day
 *
 */
@SuppressWarnings("serial")
public class FindMenu extends JMenu {
	static Vector<FindAction> findActions = new Vector<FindAction>();
	FindAction theFindAction = null;
	;
	public FindMenu(){
		super("Find");
		setMnemonic('f');
		FindTextField findField =  new FindTextField();
		add(findField);
		findField.addActionListener(new ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent e) {
				if (e.getSource() instanceof FindTextField) {
					FindTextField jtf = (FindTextField) e.getSource();
					String text = jtf.getText();
					if(theFindAction == null && ! text.equals("")){
						FindAction theFindAction = getFindAction(text);
					}
					if(theFindAction != null){
						//newSearch.addActionListener(theFindAction);
						// TODO; perform the action.
					}				
				}
			}
			})	;
		add(new JMenuItem("Find next", 'n'));
	}
	FindAction getFindAction(String text) {
		for(FindAction f : findActions){
			//TODO:  complete this
			JMenuItem newSearch = new JMenuItem(text);
			add(newSearch);
		}
		return null;
	}
	class FindTextField extends JTextField {
		
	}
/*	class FindAction extends AbstractAction {
		String stringToFind;
		public FindAction(String s){
			stringToFind = s;
		}
		public String getString() {
			return stringToFind;
		}
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}*/
	
}
