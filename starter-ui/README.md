This project serves the UI. It has s Servlet endpoint, which is supposed to take parameters, e.g. groupId, artifactId, version, runtime etc., and generate a downloadable zip folder.
The following instruction works on `*nix` environment.

### Generate the war
Following maven command will generate the war.
```
mvn clean compile war:war
```
The command will generate `jakarta-starter-ui-1.1.0-SNAPSHOT.war` in the `/target` folder. Rename it to `ROOT.war`

```a
mv target/jakarta-starter-ui-1.1.0-SNAPSHOT.war ROOT.war 
```

### Deployment & Running the UI
This war would expect tomcat 10, as this servlet was written with the Jakarta EE specification.
Download Tomcat 10 from the following URL-

`https://tomcat.apache.org/download-10.cgi`

Unzip it, and put it in a folder. It can be at any location. Then, remove all the contents of `webapps` folder.

```
cd ~/apache-tomcat-10.0.21/webapps
rm -rf . 
```

Copy the `ROOT.war` and paste it into this `webapps` folder. For example, the following command will run the tomcat.

```
cd ~/apache-tomcat-10.0.21/bin 
sh catalina.sh start
```

That's it. Now open the browser and hit `http://localhost:8080`

