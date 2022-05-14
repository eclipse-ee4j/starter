package org.eclipse.starter.ui;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.WebResourceSet;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.EmptyResourceSet;
import org.apache.catalina.webresources.StandardRoot;

public class Main {

  private static final Logger logger = Logger.getLogger(
      MethodHandles.lookup().lookupClass().getName());
  public static final String WEB_CONTENT_PATH = "src/main/webapp/";
  public static final String TOMCAT_BASE_DIR = "tomcat-base-dir";
  public static final String DEFAULT_DOC_BASE = "default-doc-base";
  public static final String ADDITIONAL_WEB_INF_CLASSES_FOLDER = "target/classes";
  public static final String RESOURCES = "/WEB-INF/classes";
  public static final String CATALINA_STARTUP_EXIT_ON_INIT_FAILURE = "org.apache.catalina.startup.EXIT_ON_INIT_FAILURE";

  public static void main(String[] args) throws Exception {
    File root = getRootFolder();
    System.setProperty(CATALINA_STARTUP_EXIT_ON_INIT_FAILURE, "true");
    Tomcat tomcat = new Tomcat();
    Path tempPath = Files.createTempDirectory(TOMCAT_BASE_DIR);
    tomcat.setBaseDir(tempPath.toString());

    String webPort = System.getenv("PORT");
    if (webPort == null || webPort.isEmpty()) {
      webPort = "8080";
    }

    tomcat.setPort(Integer.parseInt(webPort));
    File webContentFolder = new File(root.getAbsolutePath(), WEB_CONTENT_PATH);
    if (!webContentFolder.exists()) {
      webContentFolder = Files.createTempDirectory(DEFAULT_DOC_BASE).toFile();
    }
    StandardContext ctx = (StandardContext) tomcat.addWebapp("",
        webContentFolder.getAbsolutePath());
    ctx.setParentClassLoader(Main.class.getClassLoader());

    logger.log(Level.INFO, "Configuring app with basedir : {0}",
        new Object[]{webContentFolder.getAbsolutePath()});

    File additionWebInfClassesFolder = new File(root.getAbsolutePath(),
        ADDITIONAL_WEB_INF_CLASSES_FOLDER);
    WebResourceRoot resources = new StandardRoot(ctx);

    WebResourceSet resourceSet;
    if (additionWebInfClassesFolder.exists()) {
      resourceSet = new DirResourceSet(resources, RESOURCES,
          additionWebInfClassesFolder.getAbsolutePath(), "/");

      logger.log(Level.INFO, "loading WEB-INF resources from as {0}",
          new Object[]{additionWebInfClassesFolder.getAbsolutePath()});
    } else {
      resourceSet = new EmptyResourceSet(resources);
    }
    resources.addPreResources(resourceSet);
    ctx.setResources(resources);

    tomcat.getConnector();
    tomcat.start();
    tomcat.getServer().await();
  }

  private static File getRootFolder() {
    try {
      File root;
      String runningJarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()
          .getPath().replaceAll("\\\\", "/");
      int lastIndexOf = runningJarPath.lastIndexOf("/target/");
      if (lastIndexOf < 0) {
        root = new File("");
      } else {
        root = new File(runningJarPath.substring(0, lastIndexOf));
      }
      logger.log(Level.INFO, "Application resolved root folder: {0}.",
          new Object[]{root.getAbsolutePath()});
      return root;
    } catch (URISyntaxException ex) {
      throw new RuntimeException(ex);
    }
  }
}
