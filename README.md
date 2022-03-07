# Eclipse Starter for Jakarta EE
This is the official Eclipse Foundation starter for Jakarta EE. It generates code to help get started with Jakarta EE projects using Maven. It is possible to do so using a Maven Archetypes. In the future the Starter will include a Web UI.

## Building Maven Archetype from Source
In order to build the Maven Archetype from source, please download this repository on your file system. Then execute:

```
mvn clean install
```


mvn archetype:generate -DarchetypeGroupId=org.eclipse.ee4j -DarchetypeArtifactId=jakarta-starter -DarchetypeVersion=0.1-SNAPSHOT

docker build -t jakartaee-cafe:v1 .

docker run -it --rm -p 8080:8080 jakartaee-cafe:v1
