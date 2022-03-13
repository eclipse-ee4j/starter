# Eclipse Starter for Jakarta EE
This is the official Eclipse Foundation starter for Jakarta EE. It generates code to help get started with Jakarta EE projects using Maven. It is possible to do so using a Maven Archetypes. In the future the Starter will include a Web UI.

## Building Maven Archetype from Source
In order to build the Maven Archetype from source, please download this repository on your file system (the easiest way may be to [download as zip](https://github.com/eclipse-ee4j/starter/archive/refs/heads/master.zip)). Then execute (please ensure you have installed a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8) and [Maven 3+](https://maven.apache.org/download.cgi)):

```
mvn clean install
```

## Generate Jakarta EE Project
In order to run the Maven Archetype and generate a sample Jakarta EE project, please execute:

```
mvn archetype:generate -DarchetypeGroupId=org.eclipse -DarchetypeArtifactId=jakarta-starter -DarchetypeVersion=1.0
```

If you use the defaults, this will generate the Jakarta EE project under a directory named `jakartaee-cafe`. You can then run the project by executing the following command from the `jakartaee-cafe` directory:

```
mvn clean package payara-micro:start
```

Once Payara Micro starts, you can access the project at http://localhost:8080.

You can also run the project via Docker. To build the Docker image, execute the following commands from the `jakartaee-cafe` directory: 

```
mvn clean package
docker build -t jakartaee-cafe:v1 .
```

You can then run the Docker image by executing:

```
docker run -it --rm -p 8080:8080 jakartaee-cafe:v1
```

Once Payara starts, you can access the project at http://localhost:8080/jakartaee-cafe.
