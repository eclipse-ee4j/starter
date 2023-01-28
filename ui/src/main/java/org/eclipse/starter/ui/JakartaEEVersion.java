package org.eclipse.starter.ui;

public enum JakartaEEVersion {
  JAKARTA_EE_10("Jakarta EE 10", "jakartaee10-minimal"),
  JAKARTA_EE_9_1("Jakarta EE 9.1", "jakartaee9.1-minimal"),
  JAKARTA_EE_8("Jakarta EE 8", "jakartaee8-minimal"),
  ;
  private final String displayName;
  private final String archeTypeArtifactId;

  JakartaEEVersion(final String displayName, final String archeTypeArtifactId) {
    this.displayName = displayName;
    this.archeTypeArtifactId = archeTypeArtifactId;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getArcheTypeArtifactId() {
    return archeTypeArtifactId;
  }
}
