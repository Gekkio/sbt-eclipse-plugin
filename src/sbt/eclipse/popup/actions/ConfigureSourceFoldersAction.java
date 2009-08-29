package sbt.eclipse.popup.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;

/**
 * @author Joonas Javanainen
 * 
 */
public class ConfigureSourceFoldersAction extends AbstractProjectAction {

	private static final IClasspathEntry[] EMPTY_CLASSPATHENTRY_ARRAY = new IClasspathEntry[0];
	private static final IPath[] EMPTY_PATH_ARRAY = new IPath[0];
	private static final List<String> DEFAULT_SOURCES = Arrays.asList(
			"src/main/java", "src/main/scala", "src/main/resources");
	private static final List<String> DEFAULT_TEST_SOURCES = Arrays.asList(
			"src/test/java", "src/test/scala", "src/test/resources");

	public ConfigureSourceFoldersAction() {
		super();
	}

	/**
	 * @param project
	 * @throws CoreException
	 */
	protected void run(IAction action, IProject project) throws CoreException {
		IJavaProject javaProject = (IJavaProject) project
				.getNature(JavaCore.NATURE_ID);
		List<IClasspathEntry> classpaths = new ArrayList<IClasspathEntry>();
		List<IPath> wantedSourceFolders = lookupSourceFolders(project);
		List<IPath> wantedTestSourceFolders = lookupTestSourceFolders(project);
		for (IClasspathEntry entry : javaProject.getRawClasspath()) {
			if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
				wantedSourceFolders.remove(entry.getPath());
				wantedTestSourceFolders.remove(entry.getPath());
			}
			classpaths.add(entry);
		}
		for (IPath path : wantedSourceFolders) {
			classpaths.add(JavaCore.newSourceEntry(path));
		}
		IPath testOutput = project.getFolder("target/test-classes").getFullPath();
		for (IPath path : wantedTestSourceFolders) {
			classpaths.add(JavaCore
					.newSourceEntry(path, EMPTY_PATH_ARRAY, testOutput));
		}
		javaProject.setRawClasspath(classpaths.toArray(EMPTY_CLASSPATHENTRY_ARRAY),
				null);
	}

	private List<IPath> lookupSourceFolders(IProject project) {
		List<IPath> result = new ArrayList<IPath>();
		for (String path : DEFAULT_SOURCES) {
			IFolder folder = project.getFolder(path);
			if (folder.exists()) {
				result.add(folder.getFullPath());
			}
		}
		return result;
	}

	private List<IPath> lookupTestSourceFolders(IProject project) {
		List<IPath> result = new ArrayList<IPath>();
		for (String path : DEFAULT_TEST_SOURCES) {
			IFolder folder = project.getFolder(path);
			if (folder.exists()) {
				result.add(folder.getFullPath());
			}
		}
		return result;
	}

}
