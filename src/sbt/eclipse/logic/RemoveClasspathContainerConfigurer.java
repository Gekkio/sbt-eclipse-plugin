package sbt.eclipse.logic;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;

import sbt.eclipse.Constants;

public class RemoveClasspathContainerConfigurer extends AbstractConfigurer {

	private IPath containerId;

	public RemoveClasspathContainerConfigurer(IPath containerId,
			IProject project) throws CoreException {
		super(project);
		this.containerId = containerId;
	}

	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		List<IClasspathEntry> classpaths = new ArrayList<IClasspathEntry>();

		IClasspathEntry[] rawClasspath = javaProject.getRawClasspath();
		for (int i = 0; i < rawClasspath.length; i++) {
			IClasspathEntry entry = rawClasspath[i];
			if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
				if (entry.getPath().equals(containerId)) {
					continue;
				}
			}
			classpaths.add(entry);
		}
		javaProject.setRawClasspath(classpaths
				.toArray(Constants.EMPTY_CLASSPATHENTRY_ARRAY), monitor);
	}

}
