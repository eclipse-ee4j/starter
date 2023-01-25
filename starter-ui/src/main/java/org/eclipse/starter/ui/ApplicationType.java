package org.eclipse.starter.ui;

public enum ApplicationType {
  RESTFUL_SERVICE("RESTful Service"), WEB_APPLICATION("Web Application");
  private final String displayName;

  ApplicationType(final String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
