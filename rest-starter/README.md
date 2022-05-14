# Eclipse Starter for Jakarta EE (REST/CRUD)
This Eclipse Foundation starter for Jakarta EE Maven Archetype generates simple REST CRUD code.

## Generate Jakarta EE Project
In order to run the Maven Archetype and generate a sample Jakarta EE project, please execute the following. Please ensure you have installed a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8) and [Maven 3+](https://maven.apache.org/download.cgi) (we have tested with Java SE 8, Java SE 11 and Java SE 17).

```
mvn archetype:generate -DarchetypeGroupId="org.eclipse.starter" -DarchetypeArtifactId="jakarta-starter" -DarchetypeVersion="1.1.0-SNAPSHOT"
```

Note that by default the Archetype will use Payara as the Jakarta EE runtime And Jakarta 8 as version. You can use GlassFish or TomEE instead (please see the sections below for the commands to execute instead)

If you want to specify the Jakarta version you can run the following command:

```
mvn archetype:generate -DarchetypeGroupId="org.eclipse.starter" -DarchetypeArtifactId="jakarta-starter" -DarchetypeVersion="1.1.0-SNAPSHOT" -DjakartaVersion=9
```

#### For now , only the generation for the Payara runtime supports Jakarta 9.

If you use the defaults, this will generate the Jakarta EE project under a directory named `jakartaee-cafe`. You can then run the project by executing the following command from the `jakartaee-cafe` directory. Please ensure you have installed a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8) (we have tested with Java SE 8, Java SE 11 and Java SE 17). Note, the [Maven Wrapper](https://maven.apache.org/wrapper/) is already included in the project, so a Maven install is not actually needed.

```
./mvnw clean package payara-micro:start
```

Once Payara Micro starts, you can access the project at http://localhost:8080.

You can also run the project via Docker. To build the Docker image, execute the following commands from the `jakartaee-cafe` directory. Please ensure you have installed a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8) and [Docker](https://docs.docker.com/get-docker/) (we have tested with Java SE 8, Java SE 11 and Java SE 17). Note, the [Maven Wrapper](https://maven.apache.org/wrapper/) is already included in the project, so a Maven install is not actually needed.

```
./mvnw clean package
docker build -t jakartaee-cafe:v1 .
```

You can then run the Docker image by executing:

```
docker run -it --rm -p 8080:8080 jakartaee-cafe:v1
```

Once Payara starts, you can access the project at http://localhost:8080/jakartaee-cafe.

## GlassFish
* To generate a sample Jakarta EE project with GlassFish, please execute the following. Please ensure you have installed a [Java SE 8 implementation](https://adoptium.net/?variant=openjdk8) and [Maven 3+](https://maven.apache.org/download.cgi). Please note that the generated application will only work with Java SE 8 for GlassFish 5.

  ```
  mvn archetype:generate -DarchetypeGroupId="org.eclipse.starter" -DarchetypeArtifactId="jakarta-starter" -DarchetypeVersion="1.1.0-SNAPSHOT" -Druntime="glassfish"
  ```

* To run the generated project with GlassFish, please execute the following from the project directory - named `jakartaee-cafe` by default. Please ensure you have installed a [Java SE 8 implementation](https://adoptium.net/?variant=openjdk8). Note, the [Maven Wrapper](https://maven.apache.org/wrapper/) is already included in the project, so a Maven install is not actually needed.

  ```
  ./mvnw clean package cargo:run
  ```
 
  Once GlassFish starts, you can access the project at http://localhost:8080/jakartaee-cafe.
  Note that GlassFish currently does not include an official Docker image.
  
## TomEE
* To generate a sample Jakarta EE project with TomEE, please execute the following. Please ensure you have installed a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8) and [Maven 3+](https://maven.apache.org/download.cgi) (we have tested with Java SE 8, Java SE 11 and Java SE 17).

  ```
  mvn archetype:generate -DarchetypeGroupId="org.eclipse.starter" -DarchetypeArtifactId="jakarta-starter" -DarchetypeVersion="1.1.0-SNAPSHOT" -Druntime="tomee"
  ```

* To run the generated project with TomEE, please execute the following from the project directory - named `jakartaee-cafe` by default. Please ensure you have installed a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8) (we have tested with Java SE 8, Java SE 11 and Java SE 17). Note, the [Maven Wrapper](https://maven.apache.org/wrapper/) is already included in the project, so a Maven install is not actually needed.

  ```
  ./mvnw clean package tomee:run
  ```
 
  Once TomEE starts, you can access the project at http://localhost:8080/jakartaee-cafe.

* To run the generated project with TomEE and Docker, execute the following commands from the `jakartaee-cafe` directory. Please ensure you have installed a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8) and [Docker](https://docs.docker.com/get-docker/) (we have tested with Java SE 8, Java SE 11 and Java SE 17). Note, the [Maven Wrapper](https://maven.apache.org/wrapper/) is already included in the project, so a Maven install is not actually needed.

  ```
  ./mvnw clean package
  docker build -t jakartaee-cafe:v1 .
  docker run -it --rm -p 8080:8080 jakartaee-cafe:v1
  ```
  
  Once TomEE starts, you can access the project at http://localhost:8080/jakartaee-cafe.
