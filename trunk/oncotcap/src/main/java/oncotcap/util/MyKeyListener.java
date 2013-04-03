package oncotcap.util;


/*
 * Swing version
 */

import java.awt.event.*;

public class MyKeyListener  
                          implements KeyListener,
                                     ActionListener {
    static final String newline = "\n";

		
    /** Handle the key typed event from the text field. */
    public void keyTyped(KeyEvent e) {
        displayInfo(e, "KEY TYPED: ");
    }

    /** Handle the key pressed event from the text field. */
    public void keyPressed(KeyEvent e) {
        displayInfo(e, "KEY PRESSED: ");
    }

    /** Handle the key released event from the text field. */
    public void keyReleased(KeyEvent e) {
        displayInfo(e, "KEY RELEASED: ");
    }

    /** Handle the button click. */
    public void actionPerformed(ActionEvent e) {
		Logger.log( "MyKeyListener actionPerformed e= " + e);
	}

    /*
     * We have to jump through some hoops to avoid
     * trying to print non-printing characters 
     * such as Shift.  (Not only do they not print, 
     * but if you put them in a String, the characters
     * afterward won't show up in the text area.)
     */
    protected void displayInfo(KeyEvent e, String s){
        String charString, keyCodeString, modString, tmpString;

        char c = e.getKeyChar();
        int keyCode = e.getKeyCode();
        int modifiers = e.getModifiers();

        if (Character.isISOControl(c)) {
            charString = "key character = "
                       + "(an unprintable control character)";
        } else {
            charString = "key character = '"
                       + c + "'";
        }

        keyCodeString = "key code = " + keyCode
                        + " ("
                        + KeyEvent.getKeyText(keyCode)
                        + ")";

        modString = "modifiers = " + modifiers;
        tmpString = KeyEvent.getKeyModifiersText(modifiers);
        if (tmpString.length() > 0) {
            modString += " (" + tmpString + ")";
        } else {
            modString += " (no modifiers)";
        }

        Logger.log(s + newline
                           + "    " + charString + newline
                           + "    " + keyCodeString + newline
                           + "    " + modString + newline);
    }
}
