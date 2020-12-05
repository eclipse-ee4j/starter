# Eclipse Starter for Jakarta EE

The Eclipse Starter for Jakarta EE is a source code generator for Jakarta EE projects.

See [https://start.jakarta.ee](https://start.jakarta.ee).

## How to create a specification example?

To create a specification example you need to do three things:

- Create the templates that will serve as base for the example. We use thymeleaf to process the templates.
- Create a specification handler
- Add the new specification handler to StarterService

For example: 

We have a handler for the JSON-P specification. You can check the template file [here](https://github.com/eclipse-ee4j/starter/tree/master/starter-core/src/main/resources/json-p), the handler [here](https://github.com/eclipse-ee4j/starter/blob/master/starter-core/src/main/java/org/eclipse/starter/core/specification/handler/JSONPHandler.java) and the change in the StarterService [here](https://github.com/eclipse-ee4j/starter/blob/27f0c45cf6cc10327df0a0a606e6f7253e874029/starter-core/src/main/java/org/eclipse/starter/core/service/StarterService.java#L37).

To start the project using the tomee plugin, just run:

`mvn -o install tomee:run`

If you want to create a new template project you can run:

```
curl --location --request POST 'http://localhost:8080/starterapi_war/starter/v1' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=70F001DFBBDAD72370C7414FE2ACA897; GUEST_LANGUAGE_ID=en_US' \
--data-raw '{
    "packageName": "com.myproject",
    "projectName": "myProject",
    "specifications": ["jax-rs","json-b","json-p"]
}' --output test.zip
```
