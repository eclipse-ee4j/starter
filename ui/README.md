# Eclipse Starter for Jakarta EE UI
This is the UI for the official Eclipse Foundation Starter for Jakarta EE. It generates sample Jakarta EE getting started code.

## Current Release
Please note that this is a development version of the UI. For the current release, please use the latest version at https://start.jakarta.ee. This code will allow you to use UI features that are not yet released. It will also allow you to build the UI from source.

## Building from Source
In order to build the project from source, please download this repository on your file system (the easiest 
way may be to [download as zip](https://github.com/eclipse-ee4j/starter/archive/refs/heads/master.zip)).
Then execute the following from the parent directory (not this directory). Please ensure you have installed 
a [Java SE 17 implementation](https://adoptium.net/?variant=openjdk17) and 
[Maven 3+](https://maven.apache.org/download.cgi).

```
mvn clean install
```

##  Running the UI
In order to run the UI, please execute the following from this directory. You can also simply build the war from Maven and deploy the war to either WildFly 28 or JBoss EAP 8. You can do this in an IDE if desired. Note that you can override the underlying archetype version used by setting the `ARCHETYPE_VERSION` environment variable (the default version will be the most recent released to 
Maven Central, see [here](https://mvnrepository.com/artifact/org.eclipse.starter/jakarta-starter) for the latest value).

```
mvn clean package wildfly:dev
```

Once WildFly starts, please go 
to [http://localhost:8080/jakarta-starter-ui](http://localhost:8080/jakarta-starter-ui). Unzip the file the UI generates and follow the README.md in the unzipped directory.
