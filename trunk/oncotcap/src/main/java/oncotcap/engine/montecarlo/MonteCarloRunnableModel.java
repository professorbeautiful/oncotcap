package oncotcap.engine.montecarlo;

import oncotcap.engine.AbstractStochasticRunnableModel;
import oncotcap.engine.ProcessController;
import oncotcap.engine.java.PackageDir;
import oncotcap.datalayer.persistible.*;
import oncotcap.util.StringHelper;

public class MonteCarloRunnableModel extends AbstractStochasticRunnableModel
{
	private PackageDir packageDir;
	private ProcessDeclaration starterProcess;
	private ModelController controller;
	
	public MonteCarloRunnableModel(PackageDir packageDir, ProcessDeclaration starterProcess, ModelController controller)
	{
		super();
		this.packageDir = packageDir;
		this.starterProcess = starterProcess;
		this.controller = controller;
	}
	public void pause(){}
	
	public void run()
	{
		ProcessController runner = new ProcessController(this, packageDir.packageName + "." + StringHelper.javaName(controller.getStarterProcess().getName()), controller.getOutputScreen());
		runner.launch();
	}
}
