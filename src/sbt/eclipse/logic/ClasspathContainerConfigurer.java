package sbt.eclipse.logic;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;

import sbt.eclipse.Constants;

/**
 * Adds a classpath container to a project if it does not yet exist.
 * 
 * @author Joonas Javanainen
 * 
 */
public class ClasspathContainerConfigurer extends AbstractConfigurer {

	private IPath containerId;

	/**
	 * @param project
	 * @throws CoreException
	 */
	public ClasspathContainerConfigurer(IPath containerId, IProject project)
			throws CoreException {
		super(project);
		this.containerId = containerId;
	}

	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		List<IClasspathEntry> classpaths = new ArrayList<IClasspathEntry>();

		IClasspathEntry scalaContainer = null;
		IClasspathEntry jreContainer = null;
		for (IClasspathEntry entry : javaProject.getRawClasspath()) {
			if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
				IPath path = entry.getPath();
				if (path.equals(containerId)) {
					return;
				} else if (path.equals(Constants.JRE_CONTAINER_ID)) {
					jreContainer = entry;
				} else if (path.equals(Constants.SCALA_CONTAINER_ID)) {
					scalaContainer = entry;
				}
			}
			classpaths.add(entry);
		}

		IClasspathEntry container = JavaCore.newContainerEntry(containerId);
		if (scalaContainer == null) {
			if (jreContainer == null) {
				classpaths.add(container);
			} else {
				classpaths.add(classpaths.indexOf(jreContainer), container);
			}
		} else {
			classpaths.add(classpaths.indexOf(scalaContainer), container);
		}

		javaProject.setRawClasspath(classpaths
				.toArray(Constants.EMPTY_CLASSPATHENTRY_ARRAY), monitor);
	}

}
