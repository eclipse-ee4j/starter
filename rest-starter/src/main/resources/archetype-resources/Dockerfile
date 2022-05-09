#if (${runtime} == 'tomee')
#set ($baseImage = "tomee:17-jdk-8.0.10-plume")
#set ($deployDirectory = "/usr/local/tomee/webapps/")
#else
#set ($baseImage = "payara/server-full:5.2021.10")
#set ($deployDirectory = "$DEPLOY_DIR")
#end
FROM $baseImage
COPY target/jakartaee-hello-world.war $deployDirectory