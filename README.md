# Eclipse Starter for Jakarta EE
mvn archetype:generate -DarchetypeGroupId=org.eclipse.ee4j -DarchetypeArtifactId=jakarta-starter -DarchetypeVersion=0.1-SNAPSHOT

docker build -t jakartaee-cafe:v1 .

docker run -it --rm -p 8080:8080 jakartaee-cafe:v1
