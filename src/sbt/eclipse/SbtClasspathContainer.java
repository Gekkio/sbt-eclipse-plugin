package sbt.eclipse;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		Object[] jarArray = getFiles(lib_managed).toArray();
		File[] jarFileArray = new File[jarArray.length];
		System.arraycopy(jarArray, 0, jarFileArray, 0, jarArray.length);
		return jarFileArray;
	}

	private List<File> getFiles(File aStartingDir) {
		List<File> result = new ArrayList<File>();

		File[] filesAndDirs = aStartingDir.listFiles();
		List<File> filesDirs = Arrays.asList(filesAndDirs);

		for (File file : filesDirs) {
			if (file.isFile() && file.getName().endsWith(".jar"))
				result.add(file);
			if (file.isDirectory()) {
				List<File> deeperList = getFiles(file);
				result.addAll(deeperList);
			}
		}

		return result;
	}

}
