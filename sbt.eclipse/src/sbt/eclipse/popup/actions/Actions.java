package sbt.eclipse.popup.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;

public final class Actions {

	/**
	 * Converts an object into {@link IProject} if possible.
	 * 
	 * @param value
	 *            object to be converted
	 * @return converted project or null
	 */
	public static IProject convertToProject(Object value) {
		if (value instanceof IProject) {
			return (IProject) value;
		}
		if (value instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) value;
			return (IProject) adaptable.getAdapter(IProject.class);
		}
		return null;
	}

}
