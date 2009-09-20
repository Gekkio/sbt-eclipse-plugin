package sbt.eclipse.logic;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 * @author Joonas Javanainen
 *
 */
public abstract class AbstractConfigurer {
    
    protected IJavaProject javaProject;
    protected IProject project;

    public AbstractConfigurer(IProject project) throws CoreException {
        this.javaProject = (IJavaProject) project.getNature(JavaCore.NATURE_ID);
        this.project = project;
    }    
    
    public abstract void run(IProgressMonitor monitor) throws CoreException;

}
