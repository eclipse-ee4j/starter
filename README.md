# Eclipse Starter for Jakarta EE
This is the official Eclipse Foundation starter for Jakarta EE. It generates code to help get started with Jakarta EE projects using Maven. It is possible to do so using a Maven Archetypes. In the future the Starter will include a Web UI.

## Building Maven Archetype from Source
In order to build the Maven Archetype from source, please download this repository on your file system. Then execute:

```
mvn clean install
```

## Generate Jakarta EE Project
In order to run the Maven Archetype and generate a sample Jakarta EE project, please execute:

```
mvn archetype:generate -DarchetypeGroupId=org.eclipse.ee4j -DarchetypeArtifactId=jakarta-starter -DarchetypeVersion=0.1-SNAPSHOT
```

If you use the defaults, this will generate the Jakarta EE project under a directory named `jakartaee-cafe`. You can then run the project by executing:

```
mvn clean package payara-micro:start
```

Once Payara Micro starts, you can access the project at http://localhost:8080.

docker build -t jakartaee-cafe:v1 .

docker run -it --rm -p 8080:8080 jakartaee-cafe:v1
