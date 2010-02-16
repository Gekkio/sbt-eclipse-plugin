package sbt.eclipse.logic;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author Joonas Javanainen
 * 
 */
public abstract class AbstractConfigurer {

	protected IProject project;

	public AbstractConfigurer(IProject project) throws CoreException {
		this.project = project;
	}

	public abstract void run(IProgressMonitor monitor) throws CoreException;

}
