#if (${jakartaVersion} == '8')
    #if (${runtime} == 'tomee')
        #set ($baseImage = "tomee:17-jdk-8.0.10-plume")
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
    #set ($baseImage = "tomee:9.0.0-M7-jre11-alpine-plume")
    #set ($deployDirectory = "/usr/local/tomee/webapps/")
#end
FROM $baseImage
COPY target/jakartaee-hello-world.war $deployDirectory