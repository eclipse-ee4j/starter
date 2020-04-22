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

    JAX_RS("jax-rs", new String[]{"2.1.6"}, "jakarta.ws.rs", "jakarta.ws.rs-api");

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
