# Eclipse Starter for Jakarta EE
This is the official Eclipse Foundation starter for Jakarta EE. It generates code to help get started with Jakarta EE projects using Maven. It is possible to do so using a Maven Archetypes. In the future the starter will include a Web UI.

## Current Release
Please note that this is a development version of the starter. For the current release, please visit the [project website](https://eclipse-ee4j.github.io/starter/). This code will allow you to use features that are not yet released. It will also allow you to build the starter from source.

## Building from Source
In order to build the project from source, please download this repository on your file system (the easiest way may be to [download as zip](https://github.com/eclipse-ee4j/starter/archive/refs/heads/master.zip)). Then execute the following. Please ensure you have installed a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8) and [Maven 3+](https://maven.apache.org/download.cgi) (we have tested with Java SE 8, Java SE 11 and Java SE 17).

```
mvn clean install
```

## Generate Jakarta EE Projects
There are currenty two separate Archetypes available. They generate slightly different code targeting different use cases.

* The [minimal archetype](minimal-starter) generates very simple Jakarta EE code, including a "Hello World" REST endpoint.
* The [REST/CRUD archetype](rest-starter) generates a simple RESTful CRUD service using a representative set of Jakarta EE technologies such as REST, Enterprise Beans, CDI, Persistence, JSON Binding and Bean Validation. It also includes JUnit tests.

Please look at the documentation for the respective archetypes on how to use them. If desired, you can easily use the Maven Archetypes from a Maven capable IDE such as [Eclipse](https://www.eclipse.org/ide). The generated starter code is simply Maven projects. You can easily load, explore and run the code in a Maven capable IDE such as [Eclipse](https://www.eclipse.org/ide).

## Roadmap
The following is a high level roadmap for the project. All contributions are welcome advancing any of this work.
* Add support for other [Jakarta EE compatible runtimes](https://jakarta.ee/compatibility) such as WildFly and Open Liberty.
* Transition to Jakarta EE 9.1.
* Add starter UI capability.
* Add instructions for Eclipse IDE.
* Add support for generating a Faces UI instead of REST.
* Improve look and feel.

## Known Issues
* Note that Payara does not yet work on the Apple M1 chip. If you are on an M1 chip, we suggest you use GlassFish or TomEE for the time being with the archetypes.
