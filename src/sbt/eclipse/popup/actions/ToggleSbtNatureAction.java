package sbt.eclipse.popup.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;

import sbt.eclipse.Constants;
import sbt.eclipse.logic.NatureConfigurer;

/**
 * Action that toggles the SBT nature in a project.
 */
public class ToggleSbtNatureAction extends AbstractProjectAction {

	@Override
	protected void run(IAction action, IProject project) throws CoreException {
		new NatureConfigurer(project, Constants.SBT_NATURE_ID, !project
				.hasNature(Constants.SBT_NATURE_ID)).run(null);
		project.touch(null);
	}

}
