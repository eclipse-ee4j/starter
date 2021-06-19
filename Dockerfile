FROM alpine:latest

EXPOSE 8080

RUN apk add openjdk11 maven git

RUN git clone https://github.com/eclipse-ee4j/starter.git /starter

WORKDIR /starter

RUN mvn install

CMD mvn tomee:run





