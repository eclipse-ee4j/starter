<p align="center">
    <img src="images/jakartaee-logo.jpg" width="800">
</p>

# Eclipse Starter for Jakarta EE
This is the official Eclipse Foundation starter for Jakarta EE. It generates code to help get started with Jakarta EE projects using Maven. It is possible to do so using a Maven Archetypes. In the future the Starter will include a Web UI.

## Building Maven Archetype from Source
In order to build the Maven Archetype from source, please download this repository on your file system (the easiest way may be to [download as zip](https://github.com/eclipse-ee4j/starter/archive/refs/heads/master.zip)). Then execute (please ensure you have installed a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8) and [Maven 3+](https://maven.apache.org/download.cgi)):

```
mvn clean install
```

## Generate Jakarta EE Project
In order to run the Maven Archetype and generate a sample Jakarta EE project, please execute (please ensure you have installed a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8) and [Maven 3+](https://maven.apache.org/download.cgi)):

```
mvn archetype:generate -DarchetypeGroupId="org.eclipse" -DarchetypeArtifactId="jakarta-starter" -DarchetypeVersion="1.0.0-SNAPSHOT"
```

If desired, you can easily use the Maven Archetype from a Maven capable IDE such as [Eclipse](https://www.eclipse.org/ide).

If you use the defaults, this will generate the Jakarta EE project under a directory named `jakartaee-cafe`. You can then run the project by executing the following command from the `jakartaee-cafe` directory (please ensure you have installed a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8) and [Maven 3+](https://maven.apache.org/download.cgi)):

```
mvn clean package payara-micro:start
```

Once Payara Micro starts, you can access the project at http://localhost:8080.

You can also run the project via Docker. To build the Docker image, execute the following commands from the `jakartaee-cafe` directory (please ensure you have installed a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8), [Maven 3+](https://maven.apache.org/download.cgi) and [Docker](https://docs.docker.com/get-docker/)): 

```
mvn clean package
docker build -t jakartaee-cafe:v1 .
```

You can then run the Docker image by executing:

```
docker run -it --rm -p 8080:8080 jakartaee-cafe:v1
```

Once Payara starts, you can access the project at http://localhost:8080/jakartaee-cafe.

The generated starter code is simply a Maven project. You can easily load, explore and run the code in a Maven capable IDE such as [Eclipse](https://www.eclipse.org/ide).

**[The Jakarta EE Tutorial](https://eclipse-ee4j.github.io/jakartaee-tutorial)** is a comprehensive guide to 
developing enteriprise applications for the [Jakarta EE Platform](https://jakarta.ee).


## Try it out!

Download and unzip [Eclipse GlassFish](https://glassfish.org/download)

```
[glassfish-install-dir]$ ./bin/asadmin start-domain
```

Download and unzip [simple-hello.zip](https://eclipse-ee4j.github.io/starter/samples/simple-hello.zip)

```
$ cd simple-hello
$ mvn clean package
$ cp target/simple-hello.war [glassfish-install-dir]/glassfish/domains/domain1/autodeploy
```

Then, go to 

```
http://localhost:8080/simple-hello/hello
```

## Learn more!

Your first stop needs to be [Your First Cup: An Introduction to the Jakarta EE Platform](https://eclipse-ee4j.github.io/jakartaee-firstcup/).

You can further explore [First Cup Examples](https://github.com/eclipse-ee4j/jakartaee-firstcup-examples).

And then look at the [Jakarta EE Tutorial Examples](https://github.com/eclipse-ee4j/jakartaee-tutorial-examples).
