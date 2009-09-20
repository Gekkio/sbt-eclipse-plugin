package sbt.eclipse.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import sbt.eclipse.SbtClasspathContainer;
import sbt.eclipse.logic.ClasspathContainerConfigurer;
import sbt.eclipse.logic.ClasspathRemoverConfigurer;
import sbt.eclipse.logic.DefaultOutputPathConfigurer;
import sbt.eclipse.logic.SourceFoldersConfigurer;
import sbt.eclipse.logic.UnmanagedLibsConfigurer;

/**
 * @author Joonas Javanainen
 * 
 */
public class SbtImportWizard extends Wizard implements IImportWizard {

    private SbtImportWizardPage page;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        if (!page.isPageComplete()) {
            return false;
        }
        Job job = new WorkspaceJob("Importing SBT project") {
            @Override
            public IStatus runInWorkspace(IProgressMonitor monitor)
                    throws CoreException {
                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                String name = page.getLoadedProperties().getProperty(
                        "project.name");
                IProjectDescription description = workspace
                        .newProjectDescription(name);
                description.setLocation(new Path(page.getRoot()
                        .getAbsolutePath()));
                description.setNatureIds(new String[] { JavaCore.NATURE_ID });
                IWorkspaceRoot root = workspace.getRoot();
                IProject project = root.getProject(name);
                project.create(description, monitor);
                project.open(monitor);

                new ClasspathRemoverConfigurer(project).run(monitor);
                new DefaultOutputPathConfigurer(project).run(monitor);
                new SourceFoldersConfigurer(project).run(monitor);
                new UnmanagedLibsConfigurer(project).run(monitor);
                new ClasspathContainerConfigurer(
                        SbtClasspathContainer.CLASSPATH_CONTAINER_ID, project)
                        .run(monitor);

                return Status.OK_STATUS;
            }
        };
        job.schedule();
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        page = new SbtImportWizardPage("Import SBT project");
        addPage(page);
    }

}
