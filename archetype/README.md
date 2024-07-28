# Eclipse Starter for Jakarta EE
This official Eclipse Foundation Starter for Jakarta EE Maven Archetype generates sample getting started code.

## Current Release
Please note that this is a development version of the Archetype. For the current release, please use the latest version from Maven Central. This code will allow you to use features that are not yet released. It will also allow you to build the Starter from source.

## Building from Source
In order to build the project from source, please download this repository on your file system (the easiest way may be to [download as zip](https://github.com/eclipse-ee4j/starter/archive/refs/heads/master.zip)). Then execute the following from this directory or the parent. Please ensure you have installed a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8) and [Maven 3+](https://maven.apache.org/download.cgi) (we have tested with Java SE 8, Java SE 11 and Java SE 17).

```
mvn clean install
```

## Generate Jakarta EE Project
In order to run the Maven Archetype and generate a sample Jakarta EE project, please execute the following. Please ensure you have installed a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8) and [Maven 3+](https://maven.apache.org/download.cgi) (we have tested with Java SE 8, Java SE 11 and Java SE 17).

```
mvn archetype:generate -DarchetypeGroupId="org.eclipse.starter" -DarchetypeArtifactId="jakarta-starter"
```

If you use the defaults, this will generate the Jakarta EE project under a directory named `jakartaee-hello-world`. The README.md file under that directory will contain instructions on how to run the sample.

If desired, you can easily use the Maven Archetype from a Maven capable IDE such as [Eclipse](https://www.eclipse.org/ide). The generated starter code is simply Maven projects. You can easily load, explore and run the code in a Maven capable IDE such as [Eclipse](https://www.eclipse.org/ide).

To run a specific version of the Archetype, including the locally installed development version, specify the `archetypeVersion` property.

```
mvn archetype:generate -DarchetypeGroupId="org.eclipse.starter" -DarchetypeArtifactId="jakarta-starter" -DarchetypeVersion="2.4.0-SNAPSHOT"
```
