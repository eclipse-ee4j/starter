#if (${jakartaVersion} == '8')
    #if (${runtime} == 'payara')
        #if (${javaVersion} == '17')
            #if (${profile} == 'full')
                #set ($baseImage = "payara/server-full:5.2022.5-jdk17")
            #else
                #set ($baseImage = "payara/server-web:5.2022.5-jdk17")
            #end
        #elseif (${javaVersion} == '11')
            #if (${profile} == 'full')
                #set ($baseImage = "payara/server-full:5.2022.5-jdk11")
            #else
                #set ($baseImage = "payara/server-web:5.2022.5-jdk11")
            #end
        #else
            #if (${profile} == 'full')
                #set ($baseImage = "payara/server-full:5.2022.5")
            #else
                #set ($baseImage = "payara/server-web:5.2022.5")
            #end
        #end
        #set ($deployDirectory = "$DEPLOY_DIR")
    #elseif (${runtime} == 'tomee')
        #if (${javaVersion} == '17')
            #set ($baseImage = "tomee:8-jre17-plume")
        #elseif (${javaVersion} == '11')
            #set ($baseImage = "tomee:8-jre11-plume")
        #elseif (${javaVersion} == '8')
            #set ($baseImage = "tomee:8-jre8-plume")
        #else
            #set ($baseImage = "tomee:8-plume")
        #end
        #set ($deployDirectory = "/usr/local/tomee/webapps/")
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
    #if (${runtime} == 'payara')
        #if (${javaVersion} == '21')
            #if (${profile} == 'full')
                #set ($baseImage = "payara/server-full:6.2024.12-jdk21")
            #else
                #set ($baseImage = "payara/server-web:6.2024.12-jdk21")
            #end
        #elseif (${javaVersion} == '17')
            #if (${profile} == 'full')
                #set ($baseImage = "payara/server-full:6.2024.12-jdk17")
            #else
                #set ($baseImage = "payara/server-web:6.2024.12-jdk17")
            #end
        #else
            #if (${profile} == 'full')
                #set ($baseImage = "payara/server-full:6.2024.12")
            #else
                #set ($baseImage = "payara/server-web:6.2024.12")
            #end
        #end
        #set ($deployDirectory = "$DEPLOY_DIR")
    #elseif (${runtime} == 'glassfish')
        #set ($baseImage = "ghcr.io/eclipse-ee4j/glassfish:7.0.20")
        #set ($deployDirectory = "/opt/glassfish7/glassfish/domains/domain1/autodeploy")
    #elseif (${runtime} == 'tomee')
        #if (${javaVersion} == '17')
            #set ($baseImage = "tomee:9-jre17-plume")
        #elseif (${javaVersion} == '11')
            #set ($baseImage = "tomee:9-jre11-plume")
        #else
            #set ($baseImage = "tomee:9-plume")
        #end
        #set ($deployDirectory = "/usr/local/tomee/webapps/")
    #elseif (${runtime} == 'wildfly')
        #if (${javaVersion} == '21')
            #set ($baseImage = "quay.io/wildfly/wildfly:latest-jdk21")
        #elseif (${javaVersion} == '17')
            #set ($baseImage = "quay.io/wildfly/wildfly:latest-jdk17")
        #elseif (${javaVersion} == '11')
            #set ($baseImage = "quay.io/wildfly/wildfly:latest-jdk11")
        #else
            #set ($baseImage = "quay.io/wildfly/wildfly")
        #end
        #set ($deployDirectory = "/opt/jboss/wildfly/standalone/deployments/")
    #end
#end
#if (${runtime} == 'open-liberty')
    ## Once Open Liberty has a proper release for EE 11, this should be removed.
    #if (${jakartaVersion} == '11')
        #set ($baseImage = "icr.io/appcafe/open-liberty:beta")
    #elseif (${javaVersion} == '21')
        #set ($baseImage = "icr.io/appcafe/open-liberty:full-java21-openj9-ubi-minimal")
    #elseif (${javaVersion} == '17')
        #set ($baseImage = "icr.io/appcafe/open-liberty:full-java17-openj9-ubi")
    #elseif (${javaVersion} == '11')
        #set ($baseImage = "icr.io/appcafe/open-liberty:full-java11-openj9-ubi")
    #elseif (${javaVersion} == '8')
        #set ($baseImage = "icr.io/appcafe/open-liberty:full-java8-openj9-ubi")
    #else
        #set ($baseImage = "icr.io/appcafe/open-liberty")
    #end
    #set ($deployDirectory = "/config/apps/")
#end
FROM $baseImage
#if (${runtime} == 'open-liberty')
COPY src/main/liberty/config/server.xml /config/server.xml
#end
COPY target/${artifactId}.war $deployDirectory
#if ((${runtime} == 'wildfly') && (${profile} == 'full'))
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0", "-c","standalone-full.xml"]
#end
