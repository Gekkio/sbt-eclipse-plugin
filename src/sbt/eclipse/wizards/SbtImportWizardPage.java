package sbt.eclipse.wizards;

import java.io.File;

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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import sbt.eclipse.model.ProjectInformation;

public class SbtImportWizardPage extends WizardPage {

	private Text selectRootText;
	private Label projectFoundLabelResult;
	private Label projectOrganizationLabelResult;
	private Label projectNameLabelResult;
	private Label projectVersionLabelResult;
	private Label sbtVersionLabelResult;
	private File root;
	private ProjectInformation projectInfo;

	public File getRoot() {
		return root;
	}

	public ProjectInformation getProjectInfo() {
		return projectInfo;
	}

	/**
	 * @param pageName
	 */
	protected SbtImportWizardPage(String pageName) {
		super(pageName);
		setTitle("Select SBT project");
		setDescription("Import SBT project by selecting its root directory");
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(3, false));
		setControl(composite);
		Label selectRootLabel = new Label(composite, SWT.NONE);
		selectRootLabel.setText("Project root directory:");
		selectRootText = new Text(composite, SWT.BORDER | SWT.BORDER_SOLID);
		selectRootText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		selectRootText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				checkRootDirectory();
			}
		});

		Button selectRootButton = new Button(composite, SWT.NONE);
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

		Group projectInfoGroup = new Group(composite, SWT.SHADOW_OUT);
		projectInfoGroup.setText("Project information");
		projectInfoGroup.setLayout(new GridLayout(3, false));
		projectInfoGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 3, 1));

		Label projectFoundLabel = new Label(projectInfoGroup, SWT.NONE);
		projectFoundLabel.setText("SBT project found?");
		projectFoundLabelResult = new Label(projectInfoGroup, SWT.NONE);
		projectFoundLabelResult.setLayoutData(new GridData(SWT.FILL,
				SWT.CENTER, true, false, 2, 1));
		projectFoundLabelResult.setText("No");

		Label projectNameLabel = new Label(projectInfoGroup, SWT.NONE);
		projectNameLabel.setText("Project name");
		projectNameLabelResult = new Label(projectInfoGroup, SWT.NONE);
		projectNameLabelResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 2, 1));

		Label projectOrganizationLabel = new Label(projectInfoGroup, SWT.NONE);
		projectOrganizationLabel.setText("Organization");
		projectOrganizationLabelResult = new Label(projectInfoGroup, SWT.NONE);
		projectOrganizationLabelResult.setLayoutData(new GridData(SWT.FILL,
				SWT.CENTER, true, false, 2, 1));

		Label projectVersionLabel = new Label(projectInfoGroup, SWT.NONE);
		projectVersionLabel.setText("Version");
		projectVersionLabelResult = new Label(projectInfoGroup, SWT.NONE);
		projectVersionLabelResult.setLayoutData(new GridData(SWT.FILL,
				SWT.CENTER, true, false, 2, 1));

		Label sbtVersionLabel = new Label(projectInfoGroup, SWT.NONE);
		sbtVersionLabel.setText("SBT version");
		sbtVersionLabelResult = new Label(projectInfoGroup, SWT.NONE);
		sbtVersionLabelResult.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 2, 1));
	}

	protected boolean fillProjectInfo(File buildProperties) {
		projectInfo = new ProjectInformation(buildProperties);
		if (projectInfo.name == null) {
			return false;
		}
		projectNameLabelResult.setText(projectInfo.name);
		projectOrganizationLabelResult.setText(projectInfo.organization);
		projectVersionLabelResult.setText(projectInfo.version);
		sbtVersionLabelResult.setText(projectInfo.sbtVersion);
		return true;
	}

	protected void checkRootDirectory() {
		String rootText = selectRootText.getText();
		if (rootText == null || rootText.isEmpty())
			return;
		root = new File(rootText);
		if (root.exists() && root.isDirectory()) {
			File projectDir = new File(root, "project");
			File buildProperties = new File(projectDir, "build.properties");
			if (buildProperties.isFile()) {
				if (fillProjectInfo(buildProperties)) {
					projectFoundLabelResult.setText("Yes");
					setPageComplete(true);
					return;
				}
			}
		}
		setPageComplete(false);
		projectInfo = null;
		projectNameLabelResult.setText("");
		projectOrganizationLabelResult.setText("");
		projectFoundLabelResult.setText("No");
	}

}
