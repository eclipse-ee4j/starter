/********************************************************************************
 * Copyright (c) 2020 Jeyvison Nascimento and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Jeyvison Nascimento - initial API and implementation
 ********************************************************************************/

package org.eclipse.starter.core;

public enum JakartaSpecOption {

    ACTIVATION("activation", new String[]{"1.2.2"}, "jakarta.activation", "jakarta.activation-api"),
    ANNOTATIONS("annotations", new String[]{"1.3.5"}, "jakarta.annotation", "jakarta.annotation-api"),
    AUTHENTICATION("authentication", new String[]{"1.1.3"}, "jakarta.security.auth.message", "jakarta.security.auth.message-api"),
    AUTHORIZATION("authorization", new String[]{"1.5.0"}, "jakarta.authorization", "jakarta.authorization-api"),
    BATCH("batch", new String[]{"1.0.2"}, "jakarta.batch", "jakarta.batch-api"),
    CDI("cdi", new String[]{"2.0.2"}, "jakarta.enterprise", "jakarta.enterprise.cdi-api"),
    CONCURRENCY("concurrency", new String[]{"1.1.2"}, "jakarta.enterprise.concurrent", "jakarta.enterprise.concurrent-api"),
    CONNECTORS("connectors", new String[]{"1.7.4"}, "jakarta.resource", "jakarta.resource-api"),
    EJB("ejb", new String[]{"3.2.6"}, "jakarta.ejb", "jakarta.ejb-api"),
    EL("el", new String[]{"3.0.3"}, "jakarta.el", "jakarta.el-api"),
    INTERCEPTORS("interceptors", new String[]{"1.2.5"}, "jakarta.interceptor", "jakarta.interceptor-api"),
    JAX_RS("jax-rs", new String[]{"2.1.6"}, "jakarta.ws.rs", "jakarta.ws.rs-api"),
    JSF("jsf", new String[]{"2.3.2"}, "jakarta.faces", "jakarta.faces-api"),
    JSP("jsp", new String[]{"2.3.6"}, "jakarta.servlet.jsp", "jakarta.servlet.jsp-api"),
    JSONB("jsonb", new String[]{"1.0.2"}, "jakarta.json.bind", "jakarta.json.bind-api"),
    JSONP("jsonp", new String[]{"1.1.6"}, "jakarta.json", "jakarta.json-api"),
    JSTL("jstl", new String[]{"1.2.7"}, "jakarta.servlet.jsp.jstl", "jakarta.servlet.jsp.jstl-api"),
    MAIL("mail", new String[]{"1.6.5"}, "jakarta.mail", "jakarta.mail-api"),
    MESSAGING("messaging", new String[]{"2.0.3"}, "jakarta.jms", "jakarta.jms-api"),
    PERSISTENCE("persistence", new String[]{"2.2.3"}, "jakarta.persistence", "jakarta.persistence-api"),
    SECURITY("security", new String[]{"1.0.2"}, "jakarta.security.enterprise", "jakarta.security.enterprise-api"),
    SERVLET("servlet", new String[]{"4.0.3"}, "jakarta.servlet", "jakarta.servlet-api"),
    TRANSACTIONS("transactions", new String[]{"1.3.3"}, "jakarta.transaction", "jakarta.transaction-api"),
    VALIDATION( "validation", new String[]{"2.0.2"}, "jakarta.validation", "jakarta.validation-api"),
    XML_BIND("xml-bind", new String[]{"2.3.3"}, "jakarta.xml.bind", "jakarta.xml.bind-api"),
    XML_WS("xml-ws", new String[]{"2.3.3"}, "jakarta.xml.ws", "jakarta.xml.ws-api"),
    WEB_SOCKET("web-socket", new String[]{"1.1.2"}, "jakarta.websocket", "jakarta.websocket-api");

    JakartaSpecOption(String name, String[] versions, String groupId, String artifactId){
        this.name = name;
        this.versions = versions;
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    public static JakartaSpecOption getSpec(String name){

        for(JakartaSpecOption jakartaSpecOption: JakartaSpecOption.values()){
            if(jakartaSpecOption.name.equals(name)){
                return jakartaSpecOption;
            }
        }

        return null;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public String[] getVersions() {
        return versions;
    }

    private String artifactId;
    private String groupId;
    private String name;
    private String[] versions;



}
