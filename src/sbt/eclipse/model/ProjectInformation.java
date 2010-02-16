package sbt.eclipse.model;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;

public class ProjectInformation {

	public final String name;
	public final String organization;
	public final String version;
	public final String sbtVersion;
	public final String sbtScalaVersion;
	public final String buildScalaVersions;

	public String getMainCompileDirectoryName() {
		return "classes";
	}

	public String getTestCompileDirectoryName() {
		return "test-classes";
	}

	public String getResourcesDirectoryName() {
		return "resources";
	}

	public String getTestDirectoryName() {
		return "test";
	}

	public String getSourceDirectoryName() {
		return "src";
	}

	public String getMainDirectoryName() {
		return "main";
	}

	public String getScalaDirectoryName() {
		return "scala";
	}

	public String getJavaDirectoryName() {
		return "java";
	}

	public String getMainResourcesOutputDirectoryName() {
		return "resources";
	}

	public String getTestResourcesOutputDirectoryName() {
		return "test-resources";
	}

	public IFolder getSourcePath(IProject project) {
		return project.getFolder(getSourceDirectoryName());
	}

	public IFolder getMainSourcePath(IProject project) {
		return getSourcePath(project).getFolder(getMainDirectoryName());
	}

	public IFolder getMainScalaSourcePath(IProject project) {
		return getMainSourcePath(project).getFolder(getScalaDirectoryName());
	}

	public IFolder getMainJavaSourcePath(IProject project) {
		return getMainSourcePath(project).getFolder(getJavaDirectoryName());
	}

	public IFolder getMainResourcesPath(IProject project) {
		return getMainSourcePath(project)
				.getFolder(getResourcesDirectoryName());
	}

	public IFolder getMainCompilePath(IProject project) {
		return getOutputPath(project).getFolder(getMainCompileDirectoryName());
	}

	public IFolder getMainResourcesOutputPath(IProject project) {
		return getOutputPath(project).getFolder(
				getMainResourcesOutputDirectoryName());
	}

	public IFolder getTestSourcePath(IProject project) {
		return getSourcePath(project).getFolder(getTestDirectoryName());
	}

	public IFolder getTestScalaSourcePath(IProject project) {
		return getTestSourcePath(project).getFolder(getScalaDirectoryName());
	}

	public IFolder getTestJavaSourcePath(IProject project) {
		return getTestSourcePath(project).getFolder(getJavaDirectoryName());
	}

	public IFolder getTestResourcesPath(IProject project) {
		return getTestSourcePath(project)
				.getFolder(getResourcesDirectoryName());
	}

	public IFolder getTestCompilePath(IProject project) {
		return getOutputPath(project).getFolder(getTestCompileDirectoryName());
	}

	public IFolder getTestResourcesOutputPath(IProject project) {
		return getOutputPath(project).getFolder(
				getTestResourcesOutputDirectoryName());
	}

	public IFolder getOutputPath(IProject project) {
		return getOutputRootPath(project).getFolder("eclipse");
	}

	public IFolder getOutputRootPath(IProject project) {
		return project.getFolder("target");
	}

	public IFolder getManagedDependencyRootPath(IProject project) {
		return project.getFolder("lib_managed");
	}

	public IFolder getBuilderPath(IProject project) {
		return project.getFolder("project");
	}

	public IFolder getBootPath(IProject project) {
		return getBuilderPath(project).getFolder("boot");
	}

	public IFolder getBuilderProjectPath(IProject project) {
		return getBuilderPath(project).getFolder("build");
	}

	public IFolder getBuilderProjectOutputPath(IProject project) {
		return getBuilderProjectPath(project).getFolder("target");
	}

	public IFolder getPluginsPath(IProject project) {
		return getBuilderPath(project).getFolder("plugins");
	}

	public IFolder getPluginsOutputPath(IProject project) {
		return getPluginsPath(project).getFolder("target");
	}

	public ProjectInformation(File buildProperties) {
		FileInputStream input = null;
		try {
			input = new FileInputStream(buildProperties);
			Properties loadedProperties = new Properties();
			loadedProperties.load(input);
			name = loadedProperties.getProperty("project.name");
			organization = loadedProperties.getProperty("project.organization");
			version = loadedProperties.getProperty("project.version");
			sbtVersion = loadedProperties.getProperty("sbt.version");
			sbtScalaVersion = loadedProperties.getProperty("def.scala.version");
			buildScalaVersions = loadedProperties
					.getProperty("build.scala.versions");
		} catch (Exception e) {
			throw new IllegalStateException("Failed to read build.properties");
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (Exception e) {
					throw new IllegalStateException(
							"Failed to close build.properties InputStream", e);
				}
			}
		}
	}

}
