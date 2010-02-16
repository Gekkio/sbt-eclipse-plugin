package sbt.eclipse;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class SbtProjectNature implements IProjectNature {

	private IProject project;

	public void configure() throws CoreException {
		if (project == null || !project.isOpen()) {
			throw new IllegalStateException("Invalid project");
		}
	}

	public void deconfigure() throws CoreException {
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

}
