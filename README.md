# Eclipse Starter for Jakarta EE

The Eclipse Starter for Jakarta EE is a source code generator for Jakarta EE projects.

See [https://start.jakarta.ee](https://start.jakarta.ee).

If you want to run the project locally just run:

`mvn install tomee:run`

You can access http://localhost:8080/starter-api/starter/v1/specifications to get a list of the
available specifications and their versions.

If you want to create a new template project you can run, for example, the following curl command:

```
curl --location --request POST 'http://localhost:8080/starter-api/starter/v1   ' \
 --header 'Content-Type: application/json' \
 --data-raw '{
     "packageName": "com.my.project",
     "projectName": "myProject",
     "specifications": {
         "jax-rs":"2.1.6",
         "jsf":"latest"
     }
 }' -o myProject.zip
 ```
 
 If you dont know the version you want or just don't care, you can send *latest* as a version.