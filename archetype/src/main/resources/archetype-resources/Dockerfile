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
    #elseif (${runtime} == 'wildfly')
        #if (${javaVersion} == '17')
            #set ($baseImage = "quay.io/wildfly/wildfly:26.1.2.Final-jdk17")
        #elseif (${javaVersion} == '11')
            #set ($baseImage = "quay.io/wildfly/wildfly:26.1.2.Final-jdk11")
        #else
            #set ($baseImage = "quay.io/wildfly/wildfly:26.1.2.Final")
        #end
        #set ($deployDirectory = "/opt/jboss/wildfly/standalone/deployments/")    
    #end
#else
    #if (${runtime} == 'tomee')
        #if (${javaVersion} == '11')
            #set ($baseImage = "tomee:9.0-jre11-alpine-plume")
        #else
            #set ($baseImage = "tomee:9.0-jre17-alpine-plume")
        #end
        #set ($deployDirectory = "/usr/local/tomee/webapps/")
    #elseif (${runtime} == 'wildfly')
        #if (${javaVersion} == '17')
            #set ($baseImage = "quay.io/wildfly/wildfly:27.0.1.Final-jdk17")
        #else
            #set ($baseImage = "quay.io/wildfly/wildfly:27.0.1.Final-jdk11")
        #end
        #set ($deployDirectory = "/opt/jboss/wildfly/standalone/deployments/")    
    #end
#end
FROM $baseImage
COPY target/jakartaee-hello-world.war $deployDirectory
#if ((${runtime} == 'wildfly') && ${profile} == 'full')
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0", "-c","standalone-full.xml"]
#end