package org.eclipse.starter.ui;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
@ApplicationScoped
public class FetchHtmlUtility {

    private static final String FOOTER_TEMPLATE = "_template_footer_.html";
    private static final String HEADER_TEMPLATE = "_template_header_.html";

    Logger logger = LoggerFactory.getLogger(FetchHtmlUtility.class.getName());
    
    public String getHeaderHtml() throws IOException {
        String absoluteDiskPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(HEADER_TEMPLATE);
        String content = readFile(absoluteDiskPath, Charset.defaultCharset());
        Document document = Jsoup.parse(content);
        Elements headerFragment = document.select("body");
        StringBuilder headerHtml = new StringBuilder();
        headerHtml.append(headerFragment.html());
        logger.info("element: {}", headerHtml);
        return headerHtml.toString();
    }
    
    public String getFooterHtml() throws IOException {
        String absoluteDiskPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FOOTER_TEMPLATE);
        String content = readFile(absoluteDiskPath, Charset.defaultCharset());
        Document document = Jsoup.parse(content);
        Elements footerFragment = document.select("body");
        StringBuilder footerHtml =   new StringBuilder();
        footerHtml.append(footerFragment.html());
        logger.info("element: {}", footerFragment.html());
        return footerHtml.toString();
    }
    
    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
