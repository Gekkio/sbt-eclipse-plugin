package sbt.eclipse;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 * @author Francisco Treacy
 * 
 */
public class SbtClasspathContainer implements IClasspathContainer {

	private final IPath path;
	private final File projectRoot;
	private final File[] EMPTY_FILE_ARRAY = new File[0];

	public static final Path CLASSPATH_CONTAINER_ID = new Path(
			"sbt.eclipse.CLASSPATH_CONTAINER");

	public SbtClasspathContainer(IPath path, IJavaProject project) {
		this.path = path;
		this.projectRoot = project.getProject().getLocation().makeAbsolute()
				.toFile();
	}

	public IClasspathEntry[] getClasspathEntries() {
		ArrayList<IClasspathEntry> entryList = new ArrayList<IClasspathEntry>();
		for (File jar : getJars())
			entryList.add(JavaCore.newLibraryEntry(new Path(jar
					.getAbsolutePath()), null, null));
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

	private File[] getJars() {

		File lib_managed = new File(projectRoot, "lib_managed");
		if (!lib_managed.exists() || !lib_managed.isDirectory())
			throw new RuntimeException(
					String.format("The %s directory should exist; you may want to run 'sbt update' and refresh your project workspace.", lib_managed.getAbsolutePath()));
		return getFiles(lib_managed).values().toArray(EMPTY_FILE_ARRAY);
	}

	private Map<JarInformation, File> getFiles(File aStartingDir) {
		Map<JarInformation, File> result = new TreeMap<JarInformation, File>();

		File[] filesAndDirs = aStartingDir.listFiles();
		List<File> filesDirs = Arrays.asList(filesAndDirs);

		for (File file : filesDirs) {
			if (file.isFile() && file.getName().endsWith(".jar"))
				result.put(JarInformation.fromFile(file), file);
			if (file.isDirectory()) {
				Map<JarInformation, File> deeperFiles = getFiles(file);
				result.putAll(deeperFiles);
			}
		}

		return result;
	}

}
