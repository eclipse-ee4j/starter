/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.eclipse.starter.mavengenerator;

import java.util.Optional;
import java.util.Properties;

/**
 * Parameters specific to Jakarta EE archetypes
 */
public class EeArchetypeGenerateParameters extends ArchetypeGenerateParameters {

    private Optional<String> profile = Optional.empty();

    public <THIS extends EeArchetypeGenerateParameters> THIS profile(final String value) {
        this.profile = Optional.ofNullable(value);
        return (THIS)this;
    }

    @Override
    protected Properties getProperties() {
        Properties properties = super.getProperties();
        profile.ifPresent(v -> properties.put("profile", v));
        return properties;
    }
}
