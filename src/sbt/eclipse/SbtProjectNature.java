package sbt.eclipse;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import sbt.eclipse.logic.ClasspathContainerConfigurer;
import sbt.eclipse.logic.DefaultOutputPathConfigurer;
import sbt.eclipse.logic.DerivedFoldersConfigurer;
import sbt.eclipse.logic.SourceFoldersConfigurer;
import sbt.eclipse.model.ProjectInformation;

public class SbtProjectNature implements IProjectNature {

	private ProjectInformation projectInformation;
	private IProject project;

	public ProjectInformation getProjectInformation() {
		return projectInformation;
	}

	public static SbtProjectNature create(IProject project)
			throws CoreException {
		return (SbtProjectNature) project.getNature(Constants.SBT_NATURE_ID);
	}

	public void updateConfiguration() throws CoreException {
		IJavaProject javaProject = JavaCore.create(project);
		// Add managed library container
		new ClasspathContainerConfigurer(project, javaProject,
				SbtClasspathContainer.CLASSPATH_CONTAINER_ID, true).run(null);
		// Mark all SBT-built folders as derived + hidden
		new DerivedFoldersConfigurer(project, this, true).run(null);
		// Set default output path
		new DefaultOutputPathConfigurer(project, javaProject, this).run(null);
		// Configure source folders
		new SourceFoldersConfigurer(project, javaProject, this).run(null);
		javaProject.save(null, true);
	}

	public void configure() throws CoreException {
		if (project == null || !project.isOpen()) {
			throw new IllegalStateException("Invalid project");
		}
		updateConfiguration();
	}

	public void deconfigure() throws CoreException {
		if (project == null || !project.isOpen()) {
			throw new IllegalStateException("Invalid project");
		}
		IJavaProject javaProject = JavaCore.create(project);
		// Remove managed library container
		new ClasspathContainerConfigurer(project, javaProject,
				SbtClasspathContainer.CLASSPATH_CONTAINER_ID, false).run(null);
		// Mark all SBT-built folders as not derived + not hidden
		new DerivedFoldersConfigurer(project, this, false).run(null);
		javaProject.save(null, true);
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
		this.projectInformation = new ProjectInformation(project);
	}

}
