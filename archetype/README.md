# Eclipse Starter for Jakarta EE
This Eclipse Foundation starter for Jakarta EE Maven Archetype generates sample getting started code.

## Generate Jakarta EE Project
In order to run the Maven Archetype and generate a sample Jakarta EE project, please execute the following. Please ensure you have installed a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8) and [Maven 3+](https://maven.apache.org/download.cgi) (we have tested with Java SE 8, Java SE 11 and Java SE 17).

```
mvn archetype:generate -DarchetypeGroupId="org.eclipse.starter" -DarchetypeArtifactId="jakarta-starter" -DarchetypeVersion="2.0-SNAPSHOT"
```

If you use the defaults, this will generate the Jakarta EE project under a directory named `jakartaee-hello-world`. The README.md file 
under that directory will contain instructions on how to run the sample.