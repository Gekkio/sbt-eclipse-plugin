package sbt.eclipse.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;

import sbt.eclipse.Constants;

/**
 * Finds and adds all default source folders and their respective output folders
 * for a project and adds them as build paths.
 * 
 * @author Joonas Javanainen
 * 
 */
public class SourceFoldersConfigurer extends AbstractConfigurer {

    private static final SourceFoldersDefinition DEFAULT_SOURCES = new SourceFoldersDefinition(
            "target/classes", "src/main/java", "src/main/scala");
    private static final SourceFoldersDefinition DEFAULT_RESOURCES = new SourceFoldersDefinition(
            "target/resources", "src/main/resources");
    private static final SourceFoldersDefinition DEFAULT_TEST_SOURCES = new SourceFoldersDefinition(
            "target/test-classes", "src/test/java", "src/test/scala");
    private static final SourceFoldersDefinition DEFAULT_TEST_RESOURCES = new SourceFoldersDefinition(
            "target/test-resources", "src/test/resources");

    /**
     * @param project
     * @throws CoreException
     */
    public SourceFoldersConfigurer(IProject project) throws CoreException {
        super(project);
    }

    @Override
    public void run(IProgressMonitor monitor) throws CoreException {
        List<SourceFoldersDefinition> definitions = Arrays.asList(
                DEFAULT_SOURCES, DEFAULT_RESOURCES, DEFAULT_TEST_SOURCES,
                DEFAULT_TEST_RESOURCES);
        List<List<IPath>> wantedFolders = new ArrayList<List<IPath>>();
        for (SourceFoldersDefinition definition : definitions) {
            wantedFolders.add(lookupFolders(definition.folders));
        }

        // Don't try to add source folders that have already been added
        List<IClasspathEntry> classpaths = new ArrayList<IClasspathEntry>();
        for (IClasspathEntry entry : javaProject.getRawClasspath()) {
            if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
                for (List<IPath> wantedFoldersList : wantedFolders) {
                    wantedFoldersList.remove(entry.getPath());
                }
            }
            classpaths.add(entry);
        }

        // Add all source folders with respective output paths
        for (int i = 0; i < definitions.size(); i++) {
            SourceFoldersDefinition definition = definitions.get(i);
            List<IPath> wantedFoldersList = wantedFolders.get(i);
            IPath output = project.getFolder(definition.output).getFullPath();
            
            for (IPath path : wantedFoldersList) {
                classpaths.add(JavaCore.newSourceEntry(path,
                        Constants.EMPTY_PATH_ARRAY, output));
            }

        }
        javaProject.setRawClasspath(classpaths
                .toArray(Constants.EMPTY_CLASSPATHENTRY_ARRAY), monitor);
    }

    private List<IPath> lookupFolders(Collection<String> wantedFolders) {
        List<IPath> result = new ArrayList<IPath>();
        for (String path : wantedFolders) {
            IFolder folder = project.getFolder(path);
            if (folder.exists()) {
                result.add(folder.getFullPath());
            }
        }
        return result;
    }

    protected static class SourceFoldersDefinition {

        public final Collection<String> folders;
        public final String output;

        public SourceFoldersDefinition(String output, String... sourceFolders) {
            this.folders = Arrays.asList(sourceFolders);
            this.output = output;
        }

    }

}
