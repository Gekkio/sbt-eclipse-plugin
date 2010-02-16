sbt-eclipse-plugin
==================

This is no more than a classpath container for Eclipse.

You just add it to your project and it will automatically include all dependencies under SBT's `lib_managed`.

 - Download the first release from the `dist` folder (click on the jar and then `View Raw`)
 - Drop it into your `plugins` Eclipse directory and restart
 - Right-click on the project -> Build path -> Add libraries...
 - Click on `SBT Eclipse Classpath Container` and Finish

Now all your SBT-managed dependencies are included in your project's build path.

### Important note
**If and only if** you have trouble compiling, you will have to manually change the order of classpath inclusion. For this right-click project -> Properties -> Java Build Path -> Order and Export, and move `SBT Dependency Library` *up* - it needs to be listed before Scala libraries and the JRE.

Also, if you issue the `sbt update` action, then you need to refresh (F5) your project to take the changes into account.

### Other issues
I only tested it with Eclipse 3.4 on Linux with Scala 2.7.5-final, and Eclipse 3.5 on Mac OS X with Scala 2.7.5-final.
Please don't hesitate to contact me at francisco dot treacy at gmail.
