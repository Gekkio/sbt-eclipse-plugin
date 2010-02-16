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

		definitions.add(new SourceFoldersDefinition(pi
				.getMainCompilePath(project),
				pi.getMainJavaSourcePath(project), pi
						.getMainScalaSourcePath(project)));

		definitions.add(new SourceFoldersDefinition(pi
				.getMainResourcesOutputPath(project), pi
				.getMainResourcesPath(project)));

		definitions.add(new SourceFoldersDefinition(pi
				.getTestCompilePath(project),
				pi.getTestJavaSourcePath(project), pi
						.getTestScalaSourcePath(project)));

		definitions.add(new SourceFoldersDefinition(pi
				.getTestResourcesOutputPath(project), pi
				.getTestResourcesPath(project)));

		// Don't try to add source folders that have already been added
		List<IClasspathEntry> classpaths = new ArrayList<IClasspathEntry>();
		for (IClasspathEntry entry : javaProject.getRawClasspath()) {
			if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
				for (SourceFoldersDefinition sourceFolders : definitions) {
					sourceFolders.folders.remove(project.getFolder(entry
							.getPath()));
				}
			}
			classpaths.add(entry);
		}

		// Add all source folders with respective output paths
		for (SourceFoldersDefinition definition : definitions) {
			for (IFolder singleFolder : definition.folders) {
				if (singleFolder.exists()) {
					classpaths.add(JavaCore.newSourceEntry(singleFolder
							.getFullPath(), Constants.EMPTY_PATH_ARRAY,
							definition.output.getFullPath()));
				}
			}
		}

		javaProject.setRawClasspath(classpaths
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
