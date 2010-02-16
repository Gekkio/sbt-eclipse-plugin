package sbt.eclipse.logic;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Abstract base class for classes that can configure Eclipse projects.
 */
public abstract class AbstractConfigurer {

	protected IProject project;

	public AbstractConfigurer(IProject project) throws CoreException {
		this.project = project;
	}

	/**
	 * Processes the underlying project.
	 * 
	 * @param monitor
	 *            progress monitor (can be null)
	 * @throws CoreException
	 */
	public abstract void run(IProgressMonitor monitor) throws CoreException;

}
