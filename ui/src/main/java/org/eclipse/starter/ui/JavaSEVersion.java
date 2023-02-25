package org.eclipse.starter.ui;

public enum JavaSEVersion {
  JAVA_SE_17("Java SE 17", "17"),
  JAVA_SE_11("Java SE 11", "11"),
  JAVA_SE_8("Java SE 8", "8");

  private final String displayName;
  private final String version;

  JavaSEVersion(final String displayName, final String version) {
    this.displayName = displayName;
    this.version = version;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getVersion() {
    return version;
  }
}
