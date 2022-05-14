package org.eclipse.starter.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;

@WebServlet(urlPatterns = {"/download.zip"}, name = "StarterServlet")
public class StarterServlet extends HttpServlet {

  private static final Logger logger = Logger.getLogger(
      MethodHandles.lookup().lookupClass().getName());

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    logger.info("Generating project from archetype");
  }
}
