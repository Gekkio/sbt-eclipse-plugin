sbt-eclipse-plugin
==================

This is no more than a classpath container for Eclipse.

You just add it to your project and it will automatically include all dependencies under SBT's `lib_managed`.

 - Download the first release from [here](http://zoptio.nl/projects/sbt-eclipse-plugin/sbt.eclipse_0.1.0.jar)
 - Drop it into your `plugins` Eclipse directory and restart
 - Right-click on the project -> Build path -> Add libraries...
 - Click on `SBT Eclipse Classpath Container` and Finish

Now all your SBT-managed dependencies are included in your project's build path.

Important note: Until I figure out how to change it, you will have to manually change the order of classpath inclusion. For this right-click project -> Properties -> Java Build Path -> Order and Export, and move `SBT Dependency Library` *up* - it needs to be listed before Scala libraries and the JRE.

Also, if you issue the `sbt update` action, then you need to refresh (F5) your project to take the changes into account.

To be done
----------

 - Find how to programmatically move it up to avoid conflicts with Scala/JRE
 - Hide the `lib_managed` folder
 - Possibly ping for changes and refresh automatically
