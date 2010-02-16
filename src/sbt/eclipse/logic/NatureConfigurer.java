package sbt.eclipse.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import sbt.eclipse.Constants;

/**
 * Adds/removes a project nature.
 */
public class NatureConfigurer extends AbstractConfigurer {

	private final String natureId;
	private final boolean status;

	public NatureConfigurer(IProject project, String natureId, boolean status)
			throws CoreException {
		super(project);
		this.natureId = natureId;
		this.status = status;
	}

	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		IProjectDescription description = project.getDescription();
		List<String> natures = new ArrayList<String>(Arrays.asList(description
				.getNatureIds()));
		if (status) {
			natures.add(natureId);
		} else {
			natures.remove(natureId);
		}
		description.setNatureIds(natures.toArray(Constants.EMPTY_STRING_ARRAY));
		project.setDescription(description, null);
	}

}
