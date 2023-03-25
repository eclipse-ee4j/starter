# Eclipse Starter for Jakarta EE

This is the official Eclipse Foundation Starter for Jakarta EE. It generates code to help get started with Jakarta EE projects using Maven. It is possible to do so using Maven Archetypes or a web UI.

## Current Release

Please note that this is a development version of the starter. For the current release, please visit the [project website](https://start.jakarta.ee) or use Maven Central. This code will allow you to use features that are not yet released. It will also allow you to build the starter from source.

## Building from Source

In order to build the project from source, please download this repository on your file system (the easiest way may be to [download as zip](https://github.com/eclipse-ee4j/starter/archive/refs/heads/master.zip)). Then execute the following. Please ensure you have installed a [Java SE 11+ implementation](https://adoptium.net/?variant=openjdk11) and [Maven 3+](https://maven.apache.org/download.cgi) (we have tested with Java SE 11 and Java SE 17).

```
mvn clean install
```

## Generate Jakarta EE Project Using Archetypes 
In order to run the Maven Archetype and generate a sample Jakarta EE project, please execute the following. Please ensure you have installed a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8) and [Maven 3+](https://maven.apache.org/download.cgi) (we have tested with Java SE 8, Java SE 11 and Java SE 17).

```
mvn archetype:generate -DarchetypeGroupId="org.eclipse.starter" -DarchetypeArtifactId="jakarta-starter" -DarchetypeVersion="2.0-SNAPSHOT"
```

If you use the defaults, this will generate the Jakarta EE project under a directory named `jakartaee-hello-world`. The README.md file 
under that directory will contain instructions on how to run the sample.

If desired, you can easily use the Maven Archetype from a Maven capable IDE such as [Eclipse](https://www.eclipse.org/ide). The generated starter code is simply Maven projects. You can easily load, explore and run the code in a Maven capable IDE such as [Eclipse](https://www.eclipse.org/ide).

## Instructions On How To Use Archetypes From An IDE Such As Eclipse
Install Maven:
Before you can use Maven in Eclipse, you need to install it on your system. You can download Maven from the official website (https://maven.apache.org/download.cgi) and follow the installation instructions.

Install the Maven Integration for Eclipse:
The Maven Integration for Eclipse is a plugin that provides integration between Eclipse and Maven. To install the plugin, open Eclipse and go to Help > Eclipse Marketplace. Search for "Maven Integration for Eclipse" and click on the "Install" button. Follow the installation wizard and restart Eclipse when prompted.

Create a new Maven project:
To use an archetype in Eclipse, you need to create a new Maven project and select the archetype you want to use. To do this, go to File > New > Project > Maven > Maven Project. In the "New Maven Project" dialog, select "Create a simple project" and click "Next". Enter the Group Id, Artifact Id, and Version for your project, and then click "Next". In the "Select an Archetype" dialog, choose the archetype you want to use and click "Next". You can browse the available archetypes by clicking the "Add Archetype" button and entering the details of the archetype you want to use. Finally, enter any additional information required by the archetype and click "Finish" to create the project.

Configure the Maven project:
Once you have created the Maven project, you need to configure it to use the archetype. To do this, right-click on the project in the Project Explorer view and select "Maven > Update Project". In the "Update Maven Project" dialog, select the "Force Update of Snapshots/Releases" checkbox and click "OK". This will download and install the archetype and its dependencies.

Build and run the project:
You can now build and run the project as you would with any other Maven project. You can use the Maven Build menu in Eclipse to run the build, or you can use the command line to run the build. You can also run the project from within Eclipse by right-clicking on the project and selecting "Run As > Maven Build" and specifying the appropriate goals.

It is recommended that you refer to the documentation of the specific archetype you are using for any additional configuration or usage instructions.

##  Running the UI
In order to run the UI, please execute the following from this directory. You can also simply build the war from Maven and deploy the war to either WildFly 26 or JBoss EAP 7.4. You can do this in an IDE if desired. Note that you can override the underlying archetype version used by setting the `ARCHETYPE_VERSION` environment variable (the default version assumed is 2.0.0).

```
mvn clean package wildfly:run --file ui/pom.xml
```

Once WildFly starts, please go to http://localhost:8080/jakarta-starter-ui. Unzip the file the UI generates and follow the README.md in the unzipped directory.

## Known Issues
* Note that Payara does not yet work on the Apple M1 chip. If you are on an M1 chip, we suggest you use another runtime for the time being with the Archetype.

## Contributing

We welcome contributions to the project in many forms. Please see the [Contributing](CONTRIBUTING.md) page for more
information.
