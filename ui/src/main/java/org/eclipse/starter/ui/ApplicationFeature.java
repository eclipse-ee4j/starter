package org.eclipse.starter.ui;

public enum ApplicationFeature {
  PERSISTENCE("Persistence"),
  VALIDATION("Validation"),
  TEST("Test"),
  DOCKER("Docker");

  private final String displayName;

  ApplicationFeature(final String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
