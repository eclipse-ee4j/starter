# Starter-UI

This project serves the UI. It has s Servlet endpoint, which is supposed to take parameters, e.g. groupId, artifactId,
version, runtime etc., and generate a downloadable zip folder.
The following instruction works on `*nix` environment.

## Pre-requisites

- Docker or a servlet container (e.g. Tomcat) that supports servlet 5.0.

## Generate the war

Following maven command will generate the war.

```shell
mvn clean package
```

The command will generate `jakarta-starter-ui.war` in the `./artifact` folder.

## Deployment & Running the UI

This war would expect a Server 5+ or Jakarta EE 9+ runtime (such as tomcat 10 or Eclipse GlassFish 6).

## Deployment on Tomcat 10

Download Tomcat 10 from the following URL:

[https://tomcat.apache.org/download-10.cgi](https://tomcat.apache.org/download-10.cgi)

Unzip it, and put it in a folder. It can be at any location. Then, remove all the contents of `webapps` folder.

```shell
cd ~/apache-tomcat-10.0.21/webapps
rm -rf . 
```

Rename the application WAR to `ROOT.war`

```shell
mv artifact/jakarta-starter-ui.war ROOT.war 
```

Copy the `ROOT.war` and paste it into this `webapps` folder.

The following command will run Tomcat.

```
cd ~/apache-tomcat-10.0.21/bin 
sh catalina.sh start
```

That's it. Now open the browser and hit `http://localhost:8080`

## Docker with Tomcat 10

With Docker you can deploy without having to install Tomcat 10 on your local machine.

### Build the image

from the `starter-ui` folder, run the following command to build the image.

```shell
docker build -t eclipse/starter-ui .
```

### Run the container

```shell
docker run -it --rm -p 8080:8080 eclipse/starter-ui
```

### run in dev mode

In this you will mount a volume to the webapp folder so that you can make changes to the code and every new
`maven package` will be reflected in the running container.

```shell
docker run -it --rm -p 8080:8080 -v "$(pwd)/articat:/usr/local/tomcat/webapps" eclipse/starter-ui
```

See also the `build-docker.sh` and `run-docker.sh` scripts.

The endpoint is now available at `http://localhost:8080/jakarta-starter-ui`

e.g. [sample artifact](http://localhost:8080/jakarta-starter-ui/download.zip?archetypeGroupId=org.eclipse.starter&archetypeArtifactId=jakartaee10-minimal&archetypeVersion=1.1.0&groupId=com.sample&artifactId=hello_world&version=1.0.0-SNAPSHOT)

```
