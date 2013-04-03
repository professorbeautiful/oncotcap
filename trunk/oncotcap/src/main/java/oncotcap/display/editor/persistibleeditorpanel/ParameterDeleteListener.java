package oncotcap.display.editor.persistibleeditorpanel;

import java.util.Collection;

import oncotcap.datalayer.persistible.parameter.Parameter;

public interface ParameterDeleteListener
{
	public void parametersDeleted(Collection<Parameter> deletedParameters);
}
