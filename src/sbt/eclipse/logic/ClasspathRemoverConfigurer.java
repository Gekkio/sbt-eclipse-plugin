package sbt.eclipse.logic;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;

import sbt.eclipse.Constants;

/**
 * Removes all source directories from classpath.
 */
public class ClasspathRemoverConfigurer extends AbstractConfigurer {

	private final IJavaProject javaProject;

	public ClasspathRemoverConfigurer(IProject project, IJavaProject javaProject)
			throws CoreException {
		super(project);
		this.javaProject = javaProject;
	}

	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		List<IClasspathEntry> classpaths = new ArrayList<IClasspathEntry>();

		for (IClasspathEntry entry : javaProject.getRawClasspath()) {
			if (entry.getEntryKind() != IClasspathEntry.CPE_SOURCE) {
				classpaths.add(entry);
			}
		}
		javaProject.setRawClasspath(classpaths
				.toArray(Constants.EMPTY_CLASSPATHENTRY_ARRAY), monitor);
	}

}
