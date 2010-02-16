package sbt.eclipse.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNatureDescriptor;
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
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import sbt.eclipse.Constants;
import sbt.eclipse.logic.ClasspathRemoverConfigurer;
import sbt.eclipse.logic.NatureConfigurer;

/**
 * @author Joonas Javanainen
 * 
 */
public class SbtImportWizard extends Wizard implements IImportWizard {

    private SbtImportWizardPage page;

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
                String name = page.getBuildProperties().name;
                IProjectDescription description = workspace
                        .newProjectDescription(name);
                description.setLocation(new Path(page.getRoot()
                        .getAbsolutePath()));
                description.setNatureIds(new String[] { JavaCore.NATURE_ID });
                IWorkspaceRoot root = workspace.getRoot();
                IProject project = root.getProject(name);
                project.create(description, monitor);
                project.open(monitor);

                IJavaProject javaProject = JavaCore.create(project);

                // Clean up default classpath entries
                new ClasspathRemoverConfigurer(project, javaProject)
                        .run(monitor);

                if (page.isScalaProject()) {
                    new NatureConfigurer(project, Constants.SCALA_NATURE_ID,
                            true).run(monitor);
                }
                if (page.isWarProject()) {
                    /*
                     * new NatureConfigurer(project,
                     * Constants.WST_MODULECORE_NATURE_ID, true) .run(monitor);
                     * new NatureConfigurer(project,
                     * Constants.WST_FACET_NATURE_ID, true).run(monitor);
                     */
                }
                new NatureConfigurer(project, Constants.SBT_NATURE_ID, true)
                        .run(monitor);

                return Status.OK_STATUS;
            }
        };
        job.schedule();
        return true;
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
    }

    @Override
    public void addPages() {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        boolean scalaAvailable = false;
        boolean wstAvailable = false;
        for (IProjectNatureDescriptor natureDescriptor : workspace
                .getNatureDescriptors()) {
            if (natureDescriptor.getNatureId()
                    .equals(Constants.SCALA_NATURE_ID)) {
                scalaAvailable = true;
            } else if (natureDescriptor.getNatureId().equals(
                    Constants.WST_MODULECORE_NATURE_ID)) {
                wstAvailable = true;
            }
        }
        page = new SbtImportWizardPage("Import SBT project", scalaAvailable,
                wstAvailable);
        addPage(page);
    }

}
