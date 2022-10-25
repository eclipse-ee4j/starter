Welcome to the official Eclipse Foundation starter for Jakarta EE. The starter is a Maven Archetype that generates
sample code to get you going quickly with simple Jakarta EE microservices projects. The starter will include a web UI in
a subsequent release.

## Generate Jakarta EE Project

In order to run the Maven Archetype and generate a sample Jakarta EE project, please execute the following. Please
ensure you have installed a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8)
and [Maven 3+](https://maven.apache.org/download.cgi) (we have tested with Java SE 8, Java SE 11 and Java SE 17).


## Generate an archetype
(This is work in progress, as of 2022-10-22, the Jakarta EE 8 and 10 Minimal Archetypes have been released)

<script>
function generateMvnCommand() {
    const mavenArchetype = document.getElementById("mavenArchetype").value;
    const mvnArchetypeArray = mavenArchetype.split(",");
    const mvnArchetypeGroupId = mvnArchetypeArray[0];
    const mvnArchetypeArtifactId = mvnArchetypeArray[1];
    const mvnArchetypeVersion = mvnArchetypeArray[2];

    const groupId = document.getElementById("groupId").value;
    const artifactId = document.getElementById("artifactId").value;
    const projectVersion = document.getElementById("projectVersion").value;
    const mvnArchetypeGenerate = document.getElementById("mvnArchetypeGenerate");

    if (!mavenArchetype || !groupId || !artifactId || !projectVersion) {
        mvnArchetypeGenerate.value = "Please fill in all fields";
        return;
    }

    mvnArchetypeGenerate.value = `mvn archetype:generate -DarchetypeGroupId=${mvnArchetypeGroupId} -DarchetypeArtifactId=${mvnArchetypeArtifactId} -DarchetypeVersion=${mvnArchetypeVersion} -DgroupId=${groupId} -DartifactId=${artifactId} -Dversion=${projectVersion} -DinteractiveMode=false`;
}

function copyMvnCommand() {
    const mvnArchetypeGenerate = document.getElementById("mvnArchetypeGenerate");
    mvnArchetypeGenerate.select();
    mvnArchetypeGenerate.setSelectionRange(0, 99999);
    navigator.clipboard.writeText(document.getElementById("mvnArchetypeGenerate").value);
}

</script>

<form onchange="generateMvnCommand()">
    <div class="form-row">
        <div class="form-group" >
            <label for="mavenArchetype">Archetype</label>
            <select class="form-control" id="mavenArchetype" onchange="generateMvnCommand()">
                <option value="org.eclipse.starter,jakartaee10-minimal,1.0.1">Jakarta EE 10 Minimal Archetype</option>
                <option value="org.eclipse.starter,jakartaee9.1-minimal,1.0.0">Jakarta EE 9.1 Minimal Archetype</option>
                <option value="org.eclipse.starter,jakartaee8-minimal,1.0.0">Jakarta EE 8 Minimal Archetype</option>
            </select>
        </div>
    </div>
    <div class="form-row">
        <div class="form-group">
            <label for="groupId">Group</label>
            <input class="form-control" type="text" id="groupId" value="com.example" onchange="generateMvnCommand()">
        </div>
        <div class="form-group">
            <label for="artifactId">Artifact</label>
            <input type="text" class="form-control" id="artifactId" value="demo" onchange="generateMvnCommand()">
        </div>
        <div class="form-group">
            <label for="projectVersion">Version</label>
            <input type="text" class="form-control" id="projectVersion" value="1.0-SNAPSHOT" onchange="generateMvnCommand()">
        </div>
    </div>
    <div class="form-group">
        <label for="mvnArchetypeGenerate">
            Just copy this command as is in a terminal where you want to start your project and press enter...
        </label>
        <textarea class="form-control"
                  id="mvnArchetypeGenerate"
                  rows="11"
                  readonly
                  aria-describedby="mvnCommandHelp"
                  onclick="copyMvnCommand()">
        </textarea>
        <small id="mvnCommandHelp" class="form-text text-muted">By clicking on the text it will be copied to the
            clipboard
        </small>
    </div>
</form>

<script>
    generateMvnCommand();
</script>


## Example projects

If desired, you can easily use the Maven Archetype from a Maven capable IDE such
as [Eclipse](https://www.eclipse.org/ide).

If you use the defaults, this will generate the Jakarta EE project under a directory named `jakartaee-cafe`. You can
then run the project by executing the following command from the `jakartaee-cafe` directory. Please ensure you have
installed a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8)
and [Maven 3+](https://maven.apache.org/download.cgi) (we have tested with Java SE 8, Java SE 11 and Java SE 17).

```
mvn clean package payara-micro:start
```

Once Payara Micro starts, you can access the project at http://localhost:8080.

You can also run the project via Docker. To build the Docker image, execute the following commands from
the `jakartaee-cafe` directory. Please ensure you have installed
a [Java SE 8+ implementation](https://adoptium.net/?variant=openjdk8), [Maven 3+](https://maven.apache.org/download.cgi)
and [Docker](https://docs.docker.com/get-docker/) (we have tested with Java SE 8, Java SE 11 and Java SE 17).

```
mvn clean package
docker build -t jakartaee-cafe:v1 .
```

You can then run the Docker image by executing:

```
docker run -it --rm -p 8080:8080 jakartaee-cafe:v1
```

Once Payara starts, you can access the project at http://localhost:8080/jakartaee-cafe.

The generated starter code is simply a Maven project. You can easily load, explore and run the code in a Maven capable
IDE such as [Eclipse](https://www.eclipse.org/ide).

We hope you enjoy your Jakarta EE journey!

## Learn more!

There are many excellent free resources to learn Jakarta EE! The following are some that you should begin exploring
alongside the starter.

* The [Jakarta EE Tutorial](https://eclipse-ee4j.github.io/jakartaee-tutorial) is a comprehensive reference for
  developing applications with Jakarta EE.
* The [First Cup](https://eclipse-ee4j.github.io/jakartaee-firstcup/) is part of the Tutorial and is a gentle hands-on
  introduction to Jakarta EE.
* You can further explore the [First Cup Examples](https://github.com/eclipse-ee4j/jakartaee-firstcup-examples) to get a
  feel for how Jakarta EE applications look like.
* The [Jakarta EE Tutorial Examples](https://github.com/eclipse-ee4j/jakartaee-tutorial-examples) is a very
  comprehensive resource showing you how to use many Jakarta EE APIs and features.
* The [Cargo Tracker](https://eclipse-ee4j.github.io/cargotracker/) project demonstrates first-hand how you can develop
  applications with Jakarta EE using widely adopted architectural best practices like Domain-Driven Design (DDD).
