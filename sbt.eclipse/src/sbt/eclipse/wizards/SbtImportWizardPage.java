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

import sbt.eclipse.model.BuildProperties;

public class SbtImportWizardPage extends WizardPage {

	private Text selectRootText;
	private Label projectFoundLabelResult;
	private Label projectOrganizationLabelResult;
	private Label projectNameLabelResult;
	private Label projectVersionLabelResult;
	private Label sbtVersionLabelResult;
	private File root;
	private BuildProperties buildProperties;
	private final boolean scalaAvailable;
	private final boolean wstAvailable;
	private boolean scalaProject = false;
	private boolean warProject = false;

	public boolean isScalaProject() {
		return scalaProject;
	}

	public boolean isWarProject() {
		return warProject;
	}

	public File getRoot() {
		return root;
	}

	public BuildProperties getBuildProperties() {
		return buildProperties;
	}

	/**
	 * @param pageName
	 */
	protected SbtImportWizardPage(String pageName, boolean scalaAvailable,
			boolean wstAvailable) {
		super(pageName);
		this.scalaAvailable = scalaAvailable;
		this.scalaProject = scalaAvailable;
		this.wstAvailable = wstAvailable;
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

		final Button scalaCheckbox = new Button(composite, SWT.CHECK);
		if (scalaAvailable) {
			scalaCheckbox.setSelection(scalaProject);
			scalaCheckbox.setText("Scala project");
			scalaCheckbox.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					scalaProject = scalaCheckbox.getSelection();
				}
			});
		} else {
			scalaCheckbox.setEnabled(false);
			scalaCheckbox.setForeground(this.getShell().getDisplay()
					.getSystemColor(SWT.COLOR_DARK_RED));
			scalaCheckbox.setText("No Scala plugin installed");
		}
		scalaCheckbox.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 3, 1));

		final Button warCheckbox = new Button(composite, SWT.CHECK);
		if (wstAvailable) {
			warCheckbox.setSelection(warProject);
			warCheckbox.setText("WAR project");
			warCheckbox.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					warProject = warCheckbox.getSelection();
				}
			});
		} else {
			warCheckbox.setEnabled(false);
			warCheckbox.setForeground(this.getShell().getDisplay()
					.getSystemColor(SWT.COLOR_DARK_RED));
			warCheckbox.setText("No WST plugin installed");
		}
		warCheckbox.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 3, 1));

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

	protected boolean fillProjectInfo(File buildPropertiesFile) {
		this.buildProperties = new BuildProperties(buildPropertiesFile);
		if (buildProperties.name == null) {
			return false;
		}
		projectNameLabelResult.setText(buildProperties.name);
		projectOrganizationLabelResult.setText(buildProperties.organization);
		projectVersionLabelResult.setText(buildProperties.version);
		sbtVersionLabelResult.setText(buildProperties.sbtVersion);
		return true;
	}

	protected void checkRootDirectory() {
		String rootText = selectRootText.getText();
		if (rootText == null || rootText.isEmpty())
			return;
		root = new File(rootText);
		if (root.isDirectory()) {
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
		buildProperties = null;
		projectNameLabelResult.setText("");
		projectOrganizationLabelResult.setText("");
		projectFoundLabelResult.setText("No");
	}

}
