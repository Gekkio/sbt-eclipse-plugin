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

        for (IClasspathEntry entry : javaProject.getRawClasspath()) {
            if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
                if (entry.getPath().equals(containerId)) {
                    return;
                }
            }
            classpaths.add(entry);
        }

        classpaths.add(JavaCore.newContainerEntry(containerId));
        javaProject.setRawClasspath(classpaths
                .toArray(Constants.EMPTY_CLASSPATHENTRY_ARRAY), monitor);
    }

}
