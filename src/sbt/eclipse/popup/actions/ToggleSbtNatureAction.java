package sbt.eclipse.popup.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;

import sbt.eclipse.Constants;

public class ToggleSbtNatureAction extends AbstractProjectAction {

	@Override
	protected void run(IAction action, IProject project) throws CoreException {
		IProjectDescription description = project.getDescription();
		List<String> natures = new ArrayList<String>(Arrays.asList(description
				.getNatureIds()));
		if (project.hasNature(Constants.SBT_NATURE_ID)) {
			natures.remove(Constants.SBT_NATURE_ID);
		} else {
			natures.add(Constants.SBT_NATURE_ID);
		}
		description.setNatureIds(natures.toArray(Constants.EMPTY_STRING_ARRAY));
		project.setDescription(description, null);
		project.touch(null);
	}

}
