package sbt.eclipse;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import sbt.eclipse.logic.ClasspathContainerConfigurer;
import sbt.eclipse.logic.RemoveClasspathContainerConfigurer;

public class SbtProjectNature implements IProjectNature {

	private IProject project;

	public void configure() throws CoreException {
		if (project == null || !project.isOpen()) {
			throw new IllegalStateException("Invalid project");
		}
		IJavaProject javaProject = JavaCore.create(project);
		new ClasspathContainerConfigurer(
				SbtClasspathContainer.CLASSPATH_CONTAINER_ID, project)
				.run(null);
		javaProject.save(null, true);
	}

	public void deconfigure() throws CoreException {
		IJavaProject javaProject = JavaCore.create(project);
		new RemoveClasspathContainerConfigurer(
				SbtClasspathContainer.CLASSPATH_CONTAINER_ID, project)
				.run(null);
		javaProject.save(null, true);
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

}
