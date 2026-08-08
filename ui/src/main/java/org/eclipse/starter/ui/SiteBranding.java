package org.eclipse.starter.ui;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

// Topbar/footer fetched verbatim from jakarta.ee; see ui/scripts/fetch-site-branding.py.
@Named @ApplicationScoped
public class SiteBranding {
    private static final Logger LOGGER = Logger
        .getLogger(MethodHandles.lookup().lookupClass().getName());

    private static final String TOPBAR_PATH = "/site-branding/topbar.html";
    private static final String FOOTER_PATH = "/site-branding/footer.html";

    private String topbar = "";
    private String footer = "";

    @PostConstruct
    void load() {
        topbar = read(TOPBAR_PATH);
        footer = read(FOOTER_PATH);
    }

    private String read(String resource) {
        try (InputStream in = getClass().getResourceAsStream(resource)) {
            if (in == null) {
                LOGGER.log(Level.WARNING, "Missing site branding resource: {0}", resource);
                return "";
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to read " + resource, e);
            return "";
        }
    }

    public String getTopbar() { return topbar; }
    public String getFooter() { return footer; }
}
