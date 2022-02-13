FROM payara/server-full

COPY target/jakarta-starter.war /tmp
COPY post-boot-commands.asadmin /opt/payara/config/