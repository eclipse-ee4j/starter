
# Try it out!

Download and unzip [Eclipse GlassFish](https://glassfish.org/download)

```
[glassfish-install-dir]$ ./bin/asadmin start-domain
```

Download and unzip [simple-hello.zip](https://eclipse-ee4j.github.io/starter/samples/simple-hello.zip)

```
$ cd simple-hello
$ mvn clean package
$ cp target/simple-hello.war [glassfish-install-dir]/glassfish/domains/domain1/autodeploy
```

Then, go to 

```
http://localhost:8080/simple-hello/hello
```

# Learn more!

Your first stop needs to be [Your First Cup: An Introduction to the Jakarta EE Platform](https://eclipse-ee4j.github.io/jakartaee-firstcup/).

You can further explore [First Cup Examples](https://github.com/eclipse-ee4j/jakartaee-firstcup-examples).

And then look into [The Jakarta EE Tutorial](https://eclipse-ee4j.github.io/jakartaee-tutorial/toc.html) and [Jakarta EE Tutorial Examples](https://github.com/eclipse-ee4j/jakartaee-tutorial-examples).
