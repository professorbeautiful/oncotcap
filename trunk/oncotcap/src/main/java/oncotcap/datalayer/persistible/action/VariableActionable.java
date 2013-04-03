/**
 * 
 */
package oncotcap.datalayer.persistible.action;

import oncotcap.datalayer.Persistible;
import oncotcap.datalayer.persistible.parameter.DeclareVariable;
import oncotcap.datalayer.persistible.parameter.VariableType;

/**
 * @author morris
 *
 */
public interface VariableActionable {
	public Persistible getVariable();
	public void setVariable(Object var);
}
