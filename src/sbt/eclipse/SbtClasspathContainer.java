package sbt.eclipse;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

	public static final Path CLASSPATH_CONTAINER_ID = new Path(
			"sbt.eclipse.CLASSPATH_CONTAINER");

	public SbtClasspathContainer(IPath path, IJavaProject project) {
		this.path = path;
		this.projectRoot = project.getProject().getLocation().makeAbsolute()
				.toFile();
	}

	public IClasspathEntry[] getClasspathEntries() {
		ArrayList<IClasspathEntry> entryList = new ArrayList<IClasspathEntry>();
        Map<JarInformation, File> libs = new TreeMap<JarInformation, File>();
        Map<String, File> sources = new HashMap<String, File>();
        getJars(libs, sources);
        for (File jar : libs.values()) {
            String jarName = jar.getName().substring(0, jar.getName().length() - 4);
            File sourceFile = sources.get(jarName);
            IPath sourcePath = null;
            if (sourceFile != null) {
                sourcePath = new Path(sourceFile.getAbsolutePath());
            }
            entryList.add(JavaCore.newLibraryEntry(new Path(jar
                    .getAbsolutePath()), sourcePath, null));
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

	private void getJars(Map<JarInformation, File> libs, Map<String, File> sources) {
		File lib_managed = new File(projectRoot, "lib_managed");
		if (!lib_managed.exists() || !lib_managed.isDirectory()) return;
		getFiles(lib_managed, libs, sources);
	}

	private void getFiles(File aStartingDir, Map<JarInformation, File> libs, Map<String, File> sources) {
		File[] filesAndDirs = aStartingDir.listFiles();
		List<File> filesDirs = Arrays.asList(filesAndDirs);

		for (File file : filesDirs) {
		    if (file.isFile()) {
		        if (file.getName().endsWith("-sources.jar")) {
		            sources.put(file.getName().substring(0, file.getName().length() - 12), file);
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
