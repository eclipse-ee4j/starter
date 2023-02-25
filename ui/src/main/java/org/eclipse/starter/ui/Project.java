package org.eclipse.starter.ui;


import static java.util.logging.Level.SEVERE;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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
  private JavaSEVersion javaSEVersion;
  private ApplicationType applicationType;
  private Set<ApplicationFeature> applicationFeatures = new HashSet<ApplicationFeature>();
  private JakartaRuntime jakartaRuntime;
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

  public JavaSEVersion getJavaSEVersion() {
    return javaSEVersion;
  }

  public void setJavaSEVersion(final JavaSEVersion javaSEVersion) {
    this.javaSEVersion = javaSEVersion;
  }

  public Set<ApplicationFeature> getApplicationFeatures() {
    return applicationFeatures;
  }

  public void setApplicationFeatures(
      final Set<ApplicationFeature> applicationFeatures) {
    this.applicationFeatures = applicationFeatures;
  }

  public JakartaRuntime getJakartaRuntime() {
    return jakartaRuntime;
  }

  public void setJakartaRuntime(final JakartaRuntime jakartaRuntime) {
    this.jakartaRuntime = jakartaRuntime;
  }

  public ApplicationType getApplicationType() {
    return applicationType;
  }

  public void setApplicationType(final ApplicationType applicationType) {
    this.applicationType = applicationType;
  }

  public void generate() {
    final String downloadUrl = String.format(
        "/download.zip?archetypeGroupId=%s&archetypeArtifactId=%s&archetypeVersion=%s&groupId=%s&artifactId=%s&profile=%s&version=%s",
        ARCHETYPE_GROUP_ID, jakartaEEVersion.getArcheTypeArtifactId(), ARCHETYPE_VERSION, groupId,
        artifactId, profile.getProfile(), version);

    FacesContext context = FacesContext.getCurrentInstance();
    HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
    context.responseComplete();
    try {
      final Object redirectUrl = getApplicationUri()
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
