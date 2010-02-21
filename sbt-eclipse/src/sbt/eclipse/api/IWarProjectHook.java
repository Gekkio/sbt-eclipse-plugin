package sbt.eclipse.api;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

public interface IWarProjectHook {

	void importProject(IProject project) throws CoreException;

}
