#if (${jakartaVersion} == '8')
    #if (${runtime} == 'tomee')
        #set ($baseImage = "tomee:17-jdk-8.0.10-plume")
        #set ($deployDirectory = "/usr/local/tomee/webapps/")
    #elseif (${runtime} == 'payara')
        #if (${javaVersion} == '17')
            #set ($baseImage = "payara/server-full:5.2022.5-jdk17")
        #elseif (${javaVersion} == '11')
            #set ($baseImage = "payara/server-full:5.2022.5-jdk11")
        #else
            #set ($baseImage = "payara/server-full:5.2022.5")
        #end
        #set ($deployDirectory = "$DEPLOY_DIR")
    #end
#else
    #set ($baseImage = "tomee:9.0.0-M7-jre11-alpine-plume")
    #set ($deployDirectory = "/usr/local/tomee/webapps/")
#end
FROM $baseImage
COPY target/jakartaee-hello-world.war $deployDirectory