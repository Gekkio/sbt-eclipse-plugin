package sbt.eclipse.model;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class ProjectInformation {

  public final String name;
  public final String organization;
  public final String version;
  public final String sbtVersion;
  public final String sbtScalaVersion;
  public final String buildScalaVersions;

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
      buildScalaVersions = loadedProperties.getProperty("build.scala.versions");
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
