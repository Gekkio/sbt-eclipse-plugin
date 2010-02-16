package sbt.eclipse.popup.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;

import sbt.eclipse.Constants;
import sbt.eclipse.SbtProjectNature;

/**
 * Updates SBT configuration in a project.
 */
public class UpdateConfigurationAction extends AbstractProjectAction {

	@Override
	protected void run(IAction action, IProject project) throws CoreException {
		SbtProjectNature sbtProject = (SbtProjectNature) project
				.getNature(Constants.SBT_NATURE_ID);
		project.refreshLocal(IResource.DEPTH_INFINITE, null);
		sbtProject.updateConfiguration();
		project.touch(null);
	}

}
