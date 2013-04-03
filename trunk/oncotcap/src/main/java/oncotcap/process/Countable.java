/**
 * 
 */
package oncotcap.process;

/**
 * @author morris
 *
 */
public interface Countable {
	public int getCount();
	public void setCount(int count);
	public void incrementCount(int incrementBy);
	public void decrementCount(int decrementBy);

}
