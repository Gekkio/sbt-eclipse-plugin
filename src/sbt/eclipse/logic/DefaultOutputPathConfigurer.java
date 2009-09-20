package sbt.eclipse.logic;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import sbt.eclipse.Constants;

/**
 * Sets the default output folder for a project.
 * 
 * @author Joonas Javanainen
 * 
 */
public class DefaultOutputPathConfigurer extends AbstractConfigurer {

    /**
     * @param project
     * @throws CoreException
     */
    public DefaultOutputPathConfigurer(IProject project) throws CoreException {
        super(project);
    }

    @Override
    public void run(IProgressMonitor monitor) throws CoreException {
        IPath outputFolder = project.getFolder(Constants.DEFAULT_OUTPUT_FOLDER)
                .getFullPath();
        javaProject.setOutputLocation(outputFolder, monitor);
    }

}
