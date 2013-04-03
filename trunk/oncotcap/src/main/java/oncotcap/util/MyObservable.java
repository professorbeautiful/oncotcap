package oncotcap.util;
import java.util.*;

public class MyObservable extends Observable {
	int n=0;
	public static void main(String[] args){
		MyObservable m = new MyObservable();
		Observer obs = new Observer(){
			public void update(Observable obs, Object arg){
				Logger.log(((MyObservable)obs).n++ + ": obs says hasChanged reports " + obs.hasChanged());
			}
		};
		Logger.log("Will add Observer");
		m.addObserver(obs);
		Logger.log("A" + m.hasChanged());
		m.notifyObservers(new Integer(m.n));
		Logger.log("B" + m.hasChanged());
		m.setChanged();
		Logger.log("C" + m.hasChanged());
		m.notifyObservers(new Integer(m.n));
	}
	public boolean hasChanged(){
		boolean b = super.hasChanged();
		Logger.log("hasChanged = " + b);
		return(b);
	}
	/** Results:
Will add Observer
hasChanged = false		<= triggered from WITHIN the Logger.log()for A.
Afalse					<= A
hasChanged = false		<= Note that the first notifyObservers does not happen
Bfalse					<= B
hasChanged = true		<= again, from the System.out.porintln for C
Ctrue					<= C
hasChanged = false		<= the toggle is reset BEFORE the observers are notified.
0: obs says hasChanged reports false

	** Interpretation:
	**
	** First setChanged must be called.  Note that it is protected-
	** cannot be called externally to package.  Here it is called from main()
	** in a static context.  See
	** file:///C:/Program%20Files/JavaSoft/Java%20Language%20Specification/names.doc.html#62587
	**
	** Second, notifyObservers must be called--- that can happen
	** externally.  Observers will not be notified unless setChanged
	** has already been called.
	**/
}