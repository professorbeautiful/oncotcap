package oncotcap.display.editor.persistibleeditorpanel;

import java.util.Collection;

import oncotcap.datalayer.persistible.parameter.Parameter;

public interface ParameterAddListener
{
	public void parametersAdded(Collection<Parameter> addedParameters);
}
