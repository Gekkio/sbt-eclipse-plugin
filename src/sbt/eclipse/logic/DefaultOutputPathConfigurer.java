package sbt.eclipse.logic;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;

import sbt.eclipse.SbtProjectNature;

/**
 * Sets the default output folder for a project.
 */
public class DefaultOutputPathConfigurer extends AbstractConfigurer {

	private final IJavaProject javaProject;
	private final SbtProjectNature sbtProject;

	public DefaultOutputPathConfigurer(IProject project,
			IJavaProject javaProject, SbtProjectNature sbtProject)
			throws CoreException {
		super(project);
		this.javaProject = javaProject;
		this.sbtProject = sbtProject;
	}

	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		IPath output = sbtProject.getProjectInformation().getOutputRootPath()
				.getProjectRelativePath();
		javaProject.setOutputLocation(output, monitor);
	}

}
