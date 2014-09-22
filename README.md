Sample Gradle Plugin
====================

Sample project demonstrating usage of Gradle plugin 

Written as support material for a talk at [DroidconFR](http://fr.droidcon.com/2014/)


1). build test & publish your plugin into the project local maven repo

```bash
cd plugin
gradle clean uploadArchives
```

2). build the Android sample 

```bash
cd app
gradle clean assembleFreeDebug
```




