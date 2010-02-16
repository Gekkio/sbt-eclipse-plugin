package sbt.eclipse.logic;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import sbt.eclipse.Constants;

/**
 * Adds/removes a classpath container to a project if it does not yet exist.
 * 
 * @author Joonas Javanainen
 * 
 */
public class ClasspathContainerConfigurer extends AbstractConfigurer {

	private final boolean status;

	private IJavaProject javaProject;

	private IPath containerId;

	/**
	 * @param project
	 * @throws CoreException
	 */
	public ClasspathContainerConfigurer(IProject project,
			IJavaProject javaProject, IPath containerId, boolean status)
			throws CoreException {
		super(project);
		this.javaProject = javaProject;
		this.containerId = containerId;
		this.status = status;
	}

	private List<IClasspathEntry> processClasspaths(
			IClasspathEntry[] rawClasspath) {
		List<IClasspathEntry> classpaths = new ArrayList<IClasspathEntry>();

		IClasspathEntry container = JavaCore.newContainerEntry(containerId);

		int index = -1;
		int jreIndex = -1;
		int scalaIndex = -1;

		for (int i = 0; i < rawClasspath.length; i++) {
			IClasspathEntry entry = rawClasspath[i];
			if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
				IPath path = entry.getPath();
				if (status) {
					if (path.equals(Constants.JRE_CONTAINER_ID)) {
						jreIndex = i;
					} else if (path.equals(Constants.SCALA_CONTAINER_ID)) {
						scalaIndex = i;
					} else if (path.equals(containerId)) {
						index = i;
					}
				} else {
					if (path.equals(containerId)) {
						index = i;
					}
				}
			}
			classpaths.add(entry);
		}

		if (status) {
			if (index < 0) {
				if (scalaIndex < 0) {
					if (jreIndex < 0) {
						classpaths.add(container);
					} else {
						classpaths.add(jreIndex, container);
					}
				} else {
					classpaths.add(scalaIndex, container);
				}
			}
		} else {
			if (index >= 0) {
				classpaths.remove(index);
			}
		}
		return classpaths;
	}

	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		List<IClasspathEntry> classpaths = processClasspaths(javaProject
				.getRawClasspath());
		javaProject.setRawClasspath(classpaths
				.toArray(Constants.EMPTY_CLASSPATHENTRY_ARRAY), monitor);
	}

}
