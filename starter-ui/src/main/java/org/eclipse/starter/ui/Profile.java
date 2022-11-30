package org.eclipse.starter.ui;

public enum Profile {
  PLATFORM("Platform","api"),
  WEB_PROFILE("Web Profile","web-api"),
  CORE_PROFILE("Core Profile","core-api");

  private final String displayName;
  private  final String profile;

  Profile(final String displayName, final String profile) {
    this.displayName = displayName;
    this.profile = profile;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getProfile() {
    return profile;
  }
}
