package sbt.eclipse;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

/**
 * 
 * @author Francisco Treacy
 *
 */
public class SbtClasspathContainerInitializer extends ClasspathContainerInitializer {

	public SbtClasspathContainerInitializer() {}

	@Override
    public void initialize(IPath containerPath, IJavaProject project) throws CoreException {
		SbtClasspathContainer container = new SbtClasspathContainer(containerPath, project);
		updateClasspathContainer(containerPath, project, container);
    }

    @Override
    public void requestClasspathContainerUpdate(IPath containerPath, IJavaProject project, IClasspathContainer containerSuggestion) throws CoreException {
    	updateClasspathContainer(containerPath, project, containerSuggestion);
    }
    
	private void updateClasspathContainer(IPath containerPath, IJavaProject project, IClasspathContainer container) throws JavaModelException {
    	JavaCore.setClasspathContainer(containerPath, new IJavaProject[] { project }, new IClasspathContainer[] { container }, null);

		/*
    	if (JavaCore.getClasspathContainer(containerPath, project) == null) {
        	IClasspathEntry[] entries = project.getRawClasspath();
			IClasspathEntry newEntry = JavaCore.newContainerEntry(containerPath);        	
        	IClasspathEntry[] n = new IClasspathEntry[entries.length + 1];
        	n[0] = newEntry;
        	System.arraycopy(entries, 0, n, 1, entries.length);
        	
        	project.setRawClasspath(n, null);  
        	JavaCore.setClasspathContainer(containerPath, new IJavaProject[] { project }, null, null);
    	} */
    }

    @Override
    public boolean canUpdateClasspathContainer(IPath containerPath, IJavaProject project) {
        return true;
    }
    
}
