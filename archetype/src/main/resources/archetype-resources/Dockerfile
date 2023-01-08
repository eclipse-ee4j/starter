#if (${jakartaVersion} == '8')
    #if (${runtime} == 'tomee')
        #if (${javaVersion} == '8')
           #set ($baseImage = "tomee:8.0.13-jre8-alpine-plume")
        #elseif (${javaVersion} == '11')
           #set ($baseImage = "tomee:8.0.13-jre11-alpine-plume")
        #else
           #set ($baseImage = "tomee:8.0.13-jre17-alpine-plume")
        #end
        #set ($deployDirectory = "/usr/local/tomee/webapps/")
    #elseif (${runtime} == 'payara')
        #if (${javaVersion} == '17')
            #if (${profile} == 'full')
                #set ($baseImage = "payara/server-full:5.2022.5-jdk17")
            #elseif (${profile} == 'web')
                #set ($baseImage = "payara/server-web:5.2022.5-jdk17")
            #end
        #elseif (${javaVersion} == '11')
            #if (${profile} == 'full')
                #set ($baseImage = "payara/server-full:5.2022.5-jdk11")
            #elseif (${profile} == 'web')
                #set ($baseImage = "payara/server-web:5.2022.5-jdk11")
            #end        
        #else
            #if (${profile} == 'full')
                #set ($baseImage = "payara/server-full:5.2022.5")
            #elseif (${profile} == 'web')
                #set ($baseImage = "payara/server-web:5.2022.5")
            #end        
        #end
        #set ($deployDirectory = "$DEPLOY_DIR")
    #end
#else
    #if (${javaVersion} == '11')
        #set ($baseImage = "tomee:9.0-jre11-alpine-plume")
    #else
        #set ($baseImage = "tomee:9.0-jre17-alpine-plume")
    #end
    #set ($deployDirectory = "/usr/local/tomee/webapps/")
#end
FROM $baseImage
COPY target/jakartaee-hello-world.war $deployDirectory