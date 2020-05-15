
# Try it out!

Download and unzip [Eclipse GlassFish Web Profile](https://www.eclipse.org/downloads/download.php?file=/glassfish/web-5.1.0.zip)

```
[glassfish-install-dir]$ ./bin/asadmin start-domain
```

Download and unzip [simple-hello.zip](samples/simple-hello.zip)

```
$ cd simple-hello
$ mvn clean package
$ cp target/simple-hello.war [glassfish-install-dir]/glassfish/domains/domain1/autodeploy
```

Then, go to [http://localhost:8080/simple-hello/hello](http://localhost:8080/simple-hello/hello)

# Related Projects

* [Your First Cup: An Introduction to the Jakarta EE Platform](https://eclipse-ee4j.github.io/jakartaee-firstcup/)

* [The Jakarta EE Tutorial](https://eclipse-ee4j.github.io/jakartaee-tutorial/)

* [First Cup Examples](https://github.com/eclipse-ee4j/jakartaee-firstcup-examples)

* [Jakarta EE Tutorial Examples](https://github.com/eclipse-ee4j/jakartaee-tutorial-examples)
