package sbt.eclipse.logic;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;

import sbt.eclipse.Constants;

/**
 * @author Joonas Javanainen
 * 
 */
public class ClasspathRemoverConfigurer extends AbstractConfigurer {

    /**
     * @param project
     * @throws CoreException
     */
    public ClasspathRemoverConfigurer(IProject project) throws CoreException {
        super(project);
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
