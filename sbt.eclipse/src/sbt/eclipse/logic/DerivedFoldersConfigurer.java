package sbt.eclipse.logic;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import sbt.eclipse.SbtProjectNature;
import sbt.eclipse.model.ProjectInformation;

/**
 * Sets all SBT-built folders as derived + hidden.
 */
public class DerivedFoldersConfigurer extends AbstractConfigurer {

	private final boolean status;
	private final SbtProjectNature sbtProject;

	public DerivedFoldersConfigurer(IProject project,
			SbtProjectNature sbtProject, boolean status) throws CoreException {
		super(project);
		this.sbtProject = sbtProject;
		this.status = status;
	}

	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		ProjectInformation pi = sbtProject.getProjectInformation();
		derived(pi.getOutputRootPath());
		derived(pi.getManagedDependencyRootPath());
		derived(pi.getBuilderProjectOutputPath());
		derived(pi.getPluginsOutputPath());
		derived(pi.getBootPath());
	}

	private void derived(IFolder folder) throws CoreException {
		if (folder.exists()) {
			folder.setDerived(status);
			folder.setHidden(status);
		}
	}

}
