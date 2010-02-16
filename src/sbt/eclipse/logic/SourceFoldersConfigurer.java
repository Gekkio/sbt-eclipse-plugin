package sbt.eclipse.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import sbt.eclipse.Constants;
import sbt.eclipse.SbtProjectNature;
import sbt.eclipse.model.ProjectInformation;

/**
 * Finds and adds all default source folders and their respective output folders
 * for a project and adds them as build paths.
 * 
 * @author Joonas Javanainen
 * 
 */
public class SourceFoldersConfigurer extends AbstractConfigurer {

	private final IJavaProject javaProject;
	private final SbtProjectNature sbtProject;

	/**
	 * @param project
	 * @throws CoreException
	 */
	public SourceFoldersConfigurer(IProject project, IJavaProject javaProject,
			SbtProjectNature sbtProject) throws CoreException {
		super(project);
		this.javaProject = javaProject;
		this.sbtProject = sbtProject;
	}

	@Override
	public void run(IProgressMonitor monitor) throws CoreException {
		ProjectInformation pi = sbtProject.getProjectInformation();
		List<SourceFoldersDefinition> definitions = new ArrayList<SourceFoldersDefinition>();

		definitions.add(new SourceFoldersDefinition(pi.getMainCompilePath(), pi
				.getMainJavaSourcePath(), pi.getMainScalaSourcePath()));

		definitions.add(new SourceFoldersDefinition(pi
				.getMainResourcesOutputPath(), pi.getMainResourcesPath()));

		definitions.add(new SourceFoldersDefinition(pi.getTestCompilePath(), pi
				.getTestJavaSourcePath(), pi.getTestScalaSourcePath()));

		definitions.add(new SourceFoldersDefinition(pi
				.getTestResourcesOutputPath(), pi.getTestResourcesPath()));

		// Don't try to add source folders that have already been added
		List<IClasspathEntry> classpaths = new ArrayList<IClasspathEntry>();
		for (IClasspathEntry entry : javaProject.getRawClasspath()) {
			if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
				IFolder folder = project.getFolder(entry.getPath());
				if (folder.exists()) {
					for (SourceFoldersDefinition sourceFolders : definitions) {
						sourceFolders.folders.remove(folder);
					}
				} else {
					continue;
				}
			}
			classpaths.add(entry);
		}

		List<IClasspathEntry> newClasspaths = new ArrayList<IClasspathEntry>();
		// Add all source folders with respective output paths
		for (SourceFoldersDefinition definition : definitions) {
			for (IFolder singleFolder : definition.folders) {
				if (singleFolder.exists()) {
					newClasspaths.add(JavaCore.newSourceEntry(singleFolder
							.getFullPath(), Constants.EMPTY_PATH_ARRAY,
							definition.output.getFullPath()));
				}
			}
		}
		newClasspaths.addAll(classpaths);

		javaProject.setRawClasspath(newClasspaths
				.toArray(Constants.EMPTY_CLASSPATHENTRY_ARRAY), monitor);
	}

	protected static class SourceFoldersDefinition {

		public final Collection<IFolder> folders;
		public final IFolder output;

		public SourceFoldersDefinition(IFolder output, IFolder... sourceFolders) {
			this.folders = Arrays.asList(sourceFolders);
			this.output = output;
		}

	}

}
