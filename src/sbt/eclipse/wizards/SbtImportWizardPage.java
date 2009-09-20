package sbt.eclipse.wizards;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author joonas
 * @since 30.8.2009
 * 
 */
public class SbtImportWizardPage extends WizardPage {

    private Label selectRootLabel;
    private Text selectRootText;
    private Button selectRootButton;
    private Label projectFoundLabel;
    private Label projectFoundLabelResult;
    private Label projectOrganizationLabel;
    private Label projectOrganizationLabelResult;
    private Label projectNameLabel;
    private Label projectNameLabelResult;
    private Properties loadedProperties;
    private File root;

    public Properties getLoadedProperties() {
        return loadedProperties;
    }

    public File getRoot() {
        return root;
    }

    /**
     * @param pageName
     */
    protected SbtImportWizardPage(String pageName) {
        super(pageName);
        setTitle("Select SBT project");
        setDescription("Import SBT project by selecting its root directory");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
     * .Composite)
     */
    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(3, false));
        setControl(composite);
        selectRootLabel = new Label(composite, SWT.NONE);
        selectRootLabel.setText("Project root directory:");
        selectRootText = new Text(composite, SWT.BORDER_SOLID);
        selectRootText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false));
        selectRootText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                checkRootDirectory();
            }
        });
        selectRootButton = new Button(composite, SWT.NONE);
        selectRootButton.setText("Browse");
        selectRootButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                DirectoryDialog dialog = new DirectoryDialog(getShell(),
                        SWT.NONE);
                dialog.setText("Select the SBT project root directory");
                String result = dialog.open();
                if (result != null)
                    selectRootText.setText(result);
            }
        });
        projectFoundLabel = new Label(composite, SWT.NONE);
        projectFoundLabel.setText("SBT project found?");
        projectFoundLabelResult = new Label(composite, SWT.NONE);
        projectFoundLabelResult.setLayoutData(new GridData(SWT.FILL,
                SWT.CENTER, true, false, 2, 1));
        projectFoundLabelResult.setText("No");
        projectNameLabel = new Label(composite, SWT.NONE);
        projectNameLabel.setText("Project name");
        projectNameLabelResult = new Label(composite, SWT.NONE);
        projectNameLabelResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
                true, false, 2, 1));
        projectOrganizationLabel = new Label(composite, SWT.NONE);
        projectOrganizationLabel.setText("Organization");
        projectOrganizationLabelResult = new Label(composite, SWT.NONE);
        projectOrganizationLabelResult.setLayoutData(new GridData(SWT.FILL,
                SWT.CENTER, true, false, 2, 1));
    }

    protected boolean fillProjectInfo(File buildProperties) throws IOException {
        FileInputStream input = null;
        try {
            input = new FileInputStream(buildProperties);
            loadedProperties = new Properties();
            loadedProperties.load(input);
            if (!loadedProperties.containsKey("project.name"))
                return false;
            if (!loadedProperties.containsKey("sbt.version"))
                return false;
            projectNameLabelResult.setText(loadedProperties
                    .getProperty("project.name"));
            projectOrganizationLabelResult.setText(loadedProperties
                    .getProperty("project.organization"));
            return true;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void checkRootDirectory() {
        String rootText = selectRootText.getText();
        if (rootText == null || rootText.isEmpty())
            return;
        root = new File(rootText);
        if (root.exists() && root.isDirectory()) {
            File projectDir = new File(root, "project");
            File buildProperties = new File(projectDir, "build.properties");
            if (buildProperties.exists() && buildProperties.isFile()) {
                try {
                    if (fillProjectInfo(buildProperties)) {
                        projectFoundLabelResult.setText("Yes");
                        setPageComplete(true);
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        setPageComplete(false);
        projectNameLabelResult.setText("");
        projectOrganizationLabelResult.setText("");
        projectFoundLabelResult.setText("No");
    }

}
