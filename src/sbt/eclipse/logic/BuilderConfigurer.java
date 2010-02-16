package sbt.eclipse.logic;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class BuilderConfigurer extends AbstractConfigurer {

	/**
	 * @param project
	 * @throws CoreException
	 */
	public BuilderConfigurer(IProject project) throws CoreException {
		super(project);
	}

	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub
		
	}

}
