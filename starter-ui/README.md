This project serves the UI. It has s Servlet endpoint, which is supposed to take parameters, e.g. groupId, artifactId, version, runtime etc., and generate a downloadable zip folder.
The following instruction works on `*nix` environment.

### Generate the war
Following maven command will generate the war.
```
mvn clean package
```
The command will generate `jakarta-starter-ui-1.1.0-SNAPSHOT.war` in the `/target` folder. 

### Deployment & Running the UI
This war would expect a Server 5+ or Jakarta EE 9+ runtime (such as tomcat 10 or Eclipse GlassFish 6).

### Deployment on Tomcat 10
Download Tomcat 10 from the following URL-

`https://tomcat.apache.org/download-10.cgi`

Unzip it, and put it in a folder. It can be at any location. Then, remove all the contents of `webapps` folder.

```
cd ~/apache-tomcat-10.0.21/webapps
rm -rf . 
```
Rename the applicatoin WAR to `ROOT.war`

```a
mv target/jakarta-starter-ui-1.1.0-SNAPSHOT.war ROOT.war 
```
Copy the `ROOT.war` and paste it into this `webapps` folder. 

The following command will run Tomcat.

```
cd ~/apache-tomcat-10.0.21/bin 
sh catalina.sh start
```

That's it. Now open the browser and hit `http://localhost:8080`

