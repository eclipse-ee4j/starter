package org.eclipse.starter.ui;


import static java.util.logging.Level.SEVERE;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.logging.Logger;

@Named
@RequestScoped
public class Project {

  private static final Logger logger = Logger.getLogger(
      MethodHandles.lookup().lookupClass().getName());
  private static final String ARCHETYPE_GROUP_ID = "org.eclipse.starter";
  private static final String ARCHETYPE_VERSION = "1.1.0";

  private JakartaEEVersion jakartaEEVersion;
  private Profile profile;
  private String groupId = "com.example";
  private String artifactId = "demo";
  private String version = "1.0.0-SNAPSHOT"; //default value

  public JakartaEEVersion getJakartaEEVersion() {
    return jakartaEEVersion;
  }

  public void setJakartaEEVersion(final JakartaEEVersion jakartaEEVersion) {
    this.jakartaEEVersion = jakartaEEVersion;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(final String groupId) {
    this.groupId = groupId;
  }

  public String getArtifactId() {
    return artifactId;
  }

  public void setArtifactId(final String artifactId) {
    this.artifactId = artifactId;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(final String version) {
    this.version = version;
  }

  public Profile getProfile() {
    return profile;
  }

  public void setProfile(final Profile profile) {
    this.profile = profile;
  }

  public void generate() {
    final var downloadUrl = String.format(
        "/download.zip?archetypeGroupId=%s&archetypeArtifactId=%s&archetypeVersion=%s&groupId=%s&artifactId=%s&profile=%s&version=%s",
        ARCHETYPE_GROUP_ID, jakartaEEVersion.getArcheTypeArtifactId(), ARCHETYPE_VERSION, groupId,
        artifactId, profile.getProfile(), version);

    var context = FacesContext.getCurrentInstance();
    var response = (HttpServletResponse) context.getExternalContext().getResponse();
    context.responseComplete();
    try {
      final var redirectUrl = getApplicationUri()
          .map(root -> root + downloadUrl)
          .orElse(downloadUrl);
      response.sendRedirect(redirectUrl + "?faces-redirect=true");
    } catch (IOException e) {
      logger.log(SEVERE, "Was not able to generate file", e);
      throw new RuntimeException(e);
    }
  }

  private Optional<String> getApplicationUri() {
    try {
      var ext = FacesContext.getCurrentInstance().getExternalContext();
      var uri = new URI(ext.getRequestScheme(),
          null, ext.getRequestServerName(), ext.getRequestServerPort(),
          ext.getRequestContextPath(), null, null);
      return Optional.of(uri.toASCIIString());
    } catch (URISyntaxException e) {
      logger.log(SEVERE, "Was not able to get context path", e);
      return Optional.empty();
    }
  }
}
