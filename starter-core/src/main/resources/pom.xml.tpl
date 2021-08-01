<?xml version="1.0" encoding="UTF-8"?>
 <project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
   <modelVersion>4.0.0</modelVersion>
   <groupId>[# th:utext="${groupId}"/]</groupId>
   <artifactId>[# th:utext="${artifactId}"/]</artifactId>
   <version>1.0-SNAPSHOT</version>
   <packaging>war</packaging>

   <properties>
    <final.name>[# th:utext="${artifactId}"/</final.name>
   </properties>

   <dependencies>
       [# th:each="dependency : ${dependencies}"]
       <dependency>
         <groupId>[# th:utext="${dependency.groupId}"/]</groupId>
         <artifactId>[# th:utext="${dependency.artifactId}"/]</artifactId>
         <version>[# th:utext="${dependency.version}"/]</version>
         <scope>[# th:utext="${dependency.scope}"/]</scope>
         <type>[# th:utext="${dependency.type}"/]</type>
       </dependency>
        [/]
    </dependencies>

    <build>
        <finalName>[# th:utext="${artifactId}"/]</finalName>
        <plugins>
        [# th:each="plugin : ${plugins}"]
            [# th:utext="${plugin.pluginDefinition}"/]
        [/]
        </plugins>

    </build>

 </project>
