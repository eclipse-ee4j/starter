FROM tomcat:10.0.27-jdk17-corretto

RUN rm -rf /usr/local/tomcat/webapps/* 2>/dev/null

ADD target/jakarta-starter-ui.war /usr/local/tomcat/webapps/ROOT.war

