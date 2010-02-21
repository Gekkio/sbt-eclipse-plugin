package sbt.eclipse;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPageExtension;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Francisco Treacy
 *
 * This dialog allows for adding resolved SBT dependencies (lib_managed) to this project's classpath.
 *
 */
public class SbtClasspathContainerPage extends WizardPage implements IClasspathContainerPage, IClasspathContainerPageExtension {

    public SbtClasspathContainerPage() {
        super("Add SBT Dependency Library", "Add SBT Dependency Library", 
        		ImageDescriptor.createFromImage(JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_LIBRARY)));
        setDescription("This dialog allows for adding SBT resolved dependencies (lib_managed) into this project's classpath.");
        setPageComplete(true);
    }
    
    public void initialize(IJavaProject project, IClasspathEntry[] currentEntries) { }
    
    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
        composite.setFont(parent.getFont());
        setControl(composite);    
    }
      
    public boolean finish() {  
        return true;        
    }

    public IClasspathEntry getSelection() {
        IPath containerPath = SbtClasspathContainer.CLASSPATH_CONTAINER_ID;
        return JavaCore.newContainerEntry(containerPath);
    }

    public void setSelection(IClasspathEntry containerEntry) { }  
}
