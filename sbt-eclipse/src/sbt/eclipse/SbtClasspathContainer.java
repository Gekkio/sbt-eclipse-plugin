package sbt.eclipse;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import sbt.eclipse.model.BuildProperties;
import sbt.eclipse.model.ProjectInformation;

/**
 * @author Francisco Treacy
 * 
 */
public class SbtClasspathContainer implements IClasspathContainer {

	private final IPath path;
	private final File projectRoot;
	private final IWorkspace workspace;
	private final SbtProjectNature sbtProject;

	public static final Path CLASSPATH_CONTAINER_ID = new Path(
			"sbt.eclipse.CLASSPATH_CONTAINER");

	public SbtClasspathContainer(IPath path, IJavaProject project) {
		this.path = path;
		this.projectRoot = project.getProject().getLocation().makeAbsolute()
				.toFile();
		SbtProjectNature sbtProject = null;
		try {
			sbtProject = (SbtProjectNature) project.getProject().getNature(
					Constants.SBT_NATURE_ID);
		} catch (CoreException e) {
		}
		this.sbtProject = sbtProject;
		this.workspace = project.getProject().getWorkspace();
	}

	/**
	 * Scan all workspace projects that have SBT natures and try to guess their
	 * JAR names so that we can use workspace resolution.
	 * 
	 * @return map of jar name guesses to projects
	 */
	protected Map<String, IProject> scanWorkspaceProjects() {
		Map<String, IProject> results = new HashMap<String, IProject>();
		for (IProject project : workspace.getRoot().getProjects()) {
			try {
				if (project.isAccessible()
						&& project.hasNature(Constants.SBT_NATURE_ID)) {
					SbtProjectNature nature = (SbtProjectNature) project
							.getNature(Constants.SBT_NATURE_ID);
					BuildProperties bp = nature.getProjectInformation()
							.getBuildProperties();
					StringBuilder nameGuess = new StringBuilder();
					nameGuess.append(bp.name);
					nameGuess.append("-");
					nameGuess.append(bp.version);
					results.put(nameGuess.toString(), project);
					if (this.sbtProject != null) {
						StringBuilder crossBuiltName = new StringBuilder();
						crossBuiltName.append(bp.name);
						crossBuiltName.append("_");
						crossBuiltName.append(this.sbtProject
								.getProjectInformation().getScalaVersion());
						crossBuiltName.append("-");
						crossBuiltName.append(bp.version);
						results.put(crossBuiltName.toString(), project);
					}
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return results;
	}

	public IClasspathEntry[] getClasspathEntries() {
		ArrayList<IClasspathEntry> entryList = new ArrayList<IClasspathEntry>();
		Map<JarInformation, File> libs = new TreeMap<JarInformation, File>();
		Map<String, File> sources = new HashMap<String, File>();
		Map<String, IProject> workspaceProjects = scanWorkspaceProjects();
		getJars(libs, sources);
		for (File jar : libs.values()) {
			String jarName = jar.getName().substring(0,
					jar.getName().length() - 4);
			IProject project = workspaceProjects.get(jarName);
			if (project == null) {
				File sourceFile = sources.get(jarName);
				IPath sourcePath = null;
				if (sourceFile != null) {
					sourcePath = new Path(sourceFile.getAbsolutePath());
				}
				entryList.add(JavaCore.newLibraryEntry(new Path(jar
						.getAbsolutePath()), sourcePath, null));
			} else {
				// Workspace resolution worked!
				entryList.add(JavaCore.newProjectEntry(project.getFullPath()));
			}
		}
		IClasspathEntry[] entryArray = new IClasspathEntry[entryList.size()];
		return (IClasspathEntry[]) entryList.toArray(entryArray);
	}

	public String getDescription() {
		return "SBT Dependency Library";
	}

	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}

	public IPath getPath() {
		return path;
	}

	private void getJars(Map<JarInformation, File> libs,
			Map<String, File> sources) {
		File lib_managed;
		if (this.sbtProject != null) {
			ProjectInformation pi = this.sbtProject.getProjectInformation();
			lib_managed = pi.getManagedDependencyPath().getRawLocation()
					.toFile();
		} else {
			lib_managed = new File(projectRoot, "lib_managed");
		}
		if (!lib_managed.exists() || !lib_managed.isDirectory())
			return;
		getFiles(lib_managed, libs, sources);
	}

	private void getFiles(File aStartingDir, Map<JarInformation, File> libs,
			Map<String, File> sources) {
		File[] filesAndDirs = aStartingDir.listFiles();
		List<File> filesDirs = Arrays.asList(filesAndDirs);

		for (File file : filesDirs) {
			if (file.isFile()) {
				if (file.getName().endsWith("-sources.jar")) {
					sources.put(file.getName().substring(0,
							file.getName().length() - 12), file);
				} else if (file.getName().endsWith(".jar")) {
					libs.put(JarInformation.fromFile(file), file);
				}
			}
			if (file.isDirectory()) {
				getFiles(file, libs, sources);
			}
		}
	}

}
