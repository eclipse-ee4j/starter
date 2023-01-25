package org.eclipse.starter.ui;

public enum JakartaRuntime {
  GLASSFISH("Glassfish", "glassfish"),
  PAYARA("Payara", "payara"),
  WILDFLY("WildFly", "wildfly"),
  OPEN_LIBERTY("Open Liberty", "liberty"),
  TOMEE("TomEE", "tomee"),
  NONE("None", "none");

  private final String displayName;
  private final String name;

  JakartaRuntime(final String displayName, final String name) {
    this.displayName = displayName;
    this.name = name;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getName() {
    return name;
  }
}
