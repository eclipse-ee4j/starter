package org.eclipse.starter.ui;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

@WebServlet(urlPatterns = {"/download.zip"}, name = "StarterServlet")
public class StarterServlet extends HttpServlet {

  private static final Logger logger = Logger.getLogger(
      MethodHandles.lookup().lookupClass().getName());
  private static final String DOWNLOADABLE_FILE_NAME = "jakartaee-cafe.zip";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    logger.info("Generating project from archetype");

    copyDownloadableToResponse(resp);
    resp.flushBuffer();
  }

  private void copyDownloadableToResponse(HttpServletResponse resp) throws IOException {
    InputStream download = this.getClass()
        .getClassLoader()
        .getResourceAsStream(DOWNLOADABLE_FILE_NAME); //TODO will be replaced by actual file

    resp.setContentType("application/zip");
    resp.setHeader("Content-Disposition", "attachment;filename=\"" + DOWNLOADABLE_FILE_NAME + "\"");
    OutputStream out = resp.getOutputStream();

    if (download != null) {
      IOUtils.copy(download, out);
    } else {
      throw new RuntimeException("No downloadable found");
    }
  }
}
