package sbt.eclipse.popup.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;

import sbt.eclipse.logic.SourceFoldersConfigurer;

/**
 * @author Joonas Javanainen
 * 
 */
public class ConfigureSourceFoldersAction extends AbstractProjectAction {

    public ConfigureSourceFoldersAction() {
        super();
    }

    /**
     * @param project
     * @throws CoreException
     */
    protected void run(IAction action, IProject project) throws CoreException {
        new SourceFoldersConfigurer(project).run(null);
    }

}
