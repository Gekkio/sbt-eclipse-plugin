sbt-eclipse-plugin
==================

This is no more than a classpath container for Eclipse.

You just add it to your project and it will automatically include all dependencies under SBT's `lib_managed`.

 - Download the first release from http://zoptio.nl/projects/sbt-eclipse-plugin/sbt.eclipse_0.1.0.jar
 - Drop it into your `plugins` Eclipse directory and restart
 - Right-click on the project -> Build path -> Add libraries...
 - Click on `SBT Eclipse Classpath Container` and Finish

Now all your SBT-managed dependencies are included in your project's build path.

Important note: Until I figure out how to change it, you will have to manually change the order of classpath inclusion. For this right-click project -> Properties -> Java Build Path -> Order and Export, and move `SBT Dependency Library` *up* - it needs to be listed before Scala libraries and the JRE.