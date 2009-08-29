package sbt.eclipse.popup.actions;

import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Joonas Javanainen
 * 
 */
public abstract class AbstractProjectAction implements IObjectActionDelegate {

	private ISelection selection;

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		if (selection instanceof IStructuredSelection) {
			Iterator<?> iterator = ((IStructuredSelection) selection).iterator();
			while (iterator.hasNext()) {
				Object value = iterator.next();
				IProject project = null;
				if (value instanceof IProject) {
					project = (IProject) value;
				} else if (value instanceof IAdaptable) {
					IAdaptable adaptable = (IAdaptable) value;
					project = (IProject) adaptable.getAdapter(IProject.class);
				}
				try {
					run(action, project);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected abstract void run(IAction action, IProject project)
			throws Exception;

}
