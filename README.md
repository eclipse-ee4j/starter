Welcome to the official Eclipse Foundation starter for Jakarta EE. The starter is a Maven Archetype that generates sample code to get you going quickly with simple Jakarta EE microservices projects. The starter will include a web UI in a subsequent release.

## Generate Jakarta EE Project
In order to run the Maven Archetype and generate a sample Jakarta EE project, please execute the following. Please ensure you have installed a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8) and [Maven 3+](https://maven.apache.org/download.cgi) (we have tested with Java SE 8, Java SE 11 and Java SE 17).


- [Generate maven command](./starter.html)


If desired, you can easily use the Maven Archetype from a Maven capable IDE such as [Eclipse](https://www.eclipse.org/ide).

If you use the defaults, this will generate the Jakarta EE project under a directory named `jakartaee-cafe`. You can then run the project by executing the following command from the `jakartaee-cafe` directory. Please ensure you have installed a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8) and [Maven 3+](https://maven.apache.org/download.cgi) (we have tested with Java SE 8, Java SE 11 and Java SE 17).

```
mvn clean package payara-micro:start
```

Once Payara Micro starts, you can access the project at http://localhost:8080.

You can also run the project via Docker. To build the Docker image, execute the following commands from the `jakartaee-cafe` directory. Please ensure you have installed a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8), [Maven 3+](https://maven.apache.org/download.cgi) and [Docker](https://docs.docker.com/get-docker/) (we have tested with Java SE 8, Java SE 11 and Java SE 17). 

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

We hope you enjoy your Jakarta EE journey!

## Learn more!
There are many excellent free resources to learn Jakarta EE! The following are some that you should begin exploring alongside the starter.

* The [Jakarta EE Tutorial](https://eclipse-ee4j.github.io/jakartaee-tutorial) is a comprehensive reference for developing applications with Jakarta EE.
* The [First Cup](https://eclipse-ee4j.github.io/jakartaee-firstcup/) is part of the Tutorial and is a gentle hands-on introduction to Jakarta EE.
* You can further explore the [First Cup Examples](https://github.com/eclipse-ee4j/jakartaee-firstcup-examples) to get a feel for how Jakarta EE applications look like.
* The [Jakarta EE Tutorial Examples](https://github.com/eclipse-ee4j/jakartaee-tutorial-examples) is a very comprehensive resource showing you how to use many Jakarta EE APIs and features.
* The [Cargo Tracker](https://eclipse-ee4j.github.io/cargotracker/) project demonstrates first-hand how you can develop applications with Jakarta EE using widely adopted architectural best practices like Domain-Driven Design (DDD).
