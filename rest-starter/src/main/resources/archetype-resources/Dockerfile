#if (${jakartaVersion} == '8')
#if (${runtime} == 'tomee')
#set ($baseImage = "tomee:17-jdk-8.0.10-plume")
#set ($deployDirectory = "/usr/local/tomee/webapps/")
#else
#set ($baseImage = "payara/server-full:5.2021.10")
#set ($deployDirectory = "$DEPLOY_DIR")
#end
#else
#set ($baseImage = "tomee:9.0.0-M7-jre11-alpine-plume")
#set ($deployDirectory = "/usr/local/tomee/webapps/")
#end
FROM $baseImage
COPY target/jakartaee-hello-world.war $deployDirectory