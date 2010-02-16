package sbt.eclipse.model;

import java.io.File;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * Container class for SBT project information.
 */
public class ProjectInformation {

	private final IContainer container;
	private final BuildProperties buildProperties;

	/**
	 * Should be configurable but currently just takes the first scala version.
	 */
	public String getScalaVersion() {
		String[] versions = buildProperties.buildScalaVersions.split("\\s+");
		return versions[0];
	}

	public BuildProperties getBuildProperties() {
		return buildProperties;
	}

	public IPath getMainCompileDirectoryName() {
		return Path.fromPortableString("classes");
	}

	public IPath getTestCompileDirectoryName() {
		return Path.fromPortableString("test-classes");
	}

	public IPath getResourcesDirectoryName() {
		return Path.fromPortableString("resources");
	}

	public IPath getTestDirectoryName() {
		return Path.fromPortableString("test");
	}

	public IPath getSourceDirectoryName() {
		return Path.fromPortableString("src");
	}

	public IPath getMainDirectoryName() {
		return Path.fromPortableString("main");
	}

	public IPath getScalaDirectoryName() {
		return Path.fromPortableString("scala");
	}

	public IPath getJavaDirectoryName() {
		return Path.fromPortableString("java");
	}

	public IPath getMainResourcesOutputDirectoryName() {
		return Path.fromPortableString("resources");
	}

	public IPath getTestResourcesOutputDirectoryName() {
		return Path.fromPortableString("test-resources");
	}

	public IFolder getSourcePath() {
		return container.getFolder(getSourceDirectoryName());
	}

	public IFolder getMainSourcePath() {
		return getSourcePath().getFolder(getMainDirectoryName());
	}

	public IFolder getMainScalaSourcePath() {
		return getMainSourcePath().getFolder(getScalaDirectoryName());
	}

	public IFolder getMainJavaSourcePath() {
		return getMainSourcePath().getFolder(getJavaDirectoryName());
	}

	public IFolder getMainResourcesPath() {
		return getMainSourcePath().getFolder(getResourcesDirectoryName());
	}

	public IFolder getMainCompilePath() {
		return getOutputPath().getFolder(getMainCompileDirectoryName());
	}

	public IFolder getMainResourcesOutputPath() {
		return getOutputPath().getFolder(getMainResourcesOutputDirectoryName());
	}

	public IFolder getTestSourcePath() {
		return getSourcePath().getFolder(getTestDirectoryName());
	}

	public IFolder getTestScalaSourcePath() {
		return getTestSourcePath().getFolder(getScalaDirectoryName());
	}

	public IFolder getTestJavaSourcePath() {
		return getTestSourcePath().getFolder(getJavaDirectoryName());
	}

	public IFolder getTestResourcesPath() {
		return getTestSourcePath().getFolder(getResourcesDirectoryName());
	}

	public IFolder getTestCompilePath() {
		return getOutputPath().getFolder(getTestCompileDirectoryName());
	}

	public IFolder getTestResourcesOutputPath() {
		return getOutputPath().getFolder(getTestResourcesOutputDirectoryName());
	}

	public IFolder getCrossPath(IFolder path) {
		return path.getFolder("scala_" + getScalaVersion());
	}

	public IFolder getOutputPath() {
		return getCrossPath(getOutputRootPath());
	}

	public IFolder getOutputRootPath() {
		return container.getFolder(Path.fromPortableString("target"));
	}

	public IFolder getManagedDependencyPath() {
		return getCrossPath(getManagedDependencyRootPath());
	}

	public IFolder getManagedDependencyRootPath() {
		return container.getFolder(Path.fromPortableString("lib_managed"));
	}

	public IFolder getBuilderPath() {
		return container.getFolder(Path.fromPortableString("project"));
	}

	public IFolder getBootPath() {
		return getBuilderPath().getFolder("boot");
	}

	public IFolder getBuilderProjectPath() {
		return getBuilderPath().getFolder("build");
	}

	public IFolder getBuilderProjectOutputPath() {
		return getBuilderProjectPath().getFolder("target");
	}

	public IFolder getPluginsPath() {
		return getBuilderPath().getFolder("plugins");
	}

	public IFolder getPluginsOutputPath() {
		return getPluginsPath().getFolder("target");
	}

	public ProjectInformation(IContainer container) {
		this.container = container;
		File buildPropertiesFile = getBuilderPath().getFile("build.properties")
				.getRawLocation().toFile();
		this.buildProperties = new BuildProperties(buildPropertiesFile);
	}

}
