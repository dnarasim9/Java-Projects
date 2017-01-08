import com.google.common.io.Files;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;


public class MyCrawler extends WebCrawler {
    int size = 0;
    String contentType = "";
    CrawlerProperties props = new CrawlerProperties();
    ArrayList<String> urls = new ArrayList<String>();

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp3|zip|gz))$");

    /**
     * This method receives two parameters. The first parameter is the page
     * in which we have discovered this new url and the second parameter is
     * the new url. You should implement this function to specify whether
     * the given url should be crawled or not (based on your crawling logic).
     * In this example, we are instructing the crawler to ignore urls that
     * have css, js, git, ... extensions and to only accept urls that start
     * with "http://www.ics.uci.edu/". In this case, we didn't need the
     * referringPage parameter to make the decision.
     */

    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        if (FILTERS.matcher(href).matches()) {
            return false;
        }
        //System.out.println("DOMAIN, SUB : " + url.getDomain() + " " + url.getSubDomain());
        return (url.getSubDomain().toLowerCase().contains("cinema") && url.getDomain().toLowerCase().contains("usc"));
    }


    /**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */

    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);
        //props.fetchStats("attempted");
        ParseData parseData = page.getParseData();
//            String text = htmlParseData.getText();
//            String html = htmlParseData.getHtml();
        Set<WebURL> links = parseData.getOutgoingUrls();

//            System.out.println("Text length: " + text.length());
//            System.out.println("Html length: " + html.length());
//            System.out.println("Number of outgoing links: " + links.size());
        int statusCode = page.getStatusCode();

        //props.updateNoOfStatusCodes(statusCode);


        //props.fetchStats("succeeded");
//        props.addSuccessfulURL(url, page.getContentData().length, links.size(), page.getContentType());
        props.addLinksAndCintentType(page.getContentData().length,page.getContentType());
        //Downloading pages
        String extension = "";
        try {
            contentType = page.getContentType();
            TikaConfig config = TikaConfig.getDefaultConfig();
            MimeTypes allTypes = config.getMimeRepository();
            MimeType mimeType = allTypes.forName(contentType.split(";")[0]);
            extension = mimeType.getExtension();
        } catch (MimeTypeException e) {
            extension = "";
        }

        if (extension.equals(".pdf")
                || extension.equals(".doc")
                || extension.equals(".docx")
                || extension.equals(".html")
                || extension.equals(".htm"))

        {




            String hashedName = UUID.randomUUID() + extension;
            String filename = "D:\\Dheemanth\\US\\USC\\CSCI 572 - Information Retrieval and Web Search Engines\\Homeworks\\Homework 2 - Web Crawling\\Crawl Storage Folder\\newdir" + hashedName;
            props.addSuccessfulURL(filename,url, page.getContentData().length, links.size(), page.getContentType(), links);
            try
            {
                Files.write(page.getContentData(), new File(filename));
                logger.info("Stored: {}", url);
            }
            catch (IOException iox)
            {
                logger.error("Failed to write file: " + filename, iox);
            }
        }

        for (WebURL l : links) {
            String sub = l.getSubDomain();
            String dom = l.getDomain();

            if (sub.toLowerCase().contains("cinema") && dom.toLowerCase().contains("usc")) {
                props.addOutgoingLinks(l, "OK");
            } else {
                if (dom.toLowerCase().contains("usc")) {
                    props.addOutgoingLinks(l, "USC");
                } else {
                    props.addOutgoingLinks(l, "outUSC");
                }
            }
        }


    }

    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
        props.addURL(webUrl.getURL());
        props.addStatusCode(statusCode);

    }

    @Override
    protected void onPageBiggerThanMaxSize(String urlStr, long pageSize) {
        System.out.println("Big Page");
    }

    @Override
    protected void onContentFetchError(WebURL webUrl) {
        System.out.println("Content Error");
    }

    protected void onUnhandledException(WebURL webUrl, Throwable e) {
        System.out.println("un handeled exception"+e);
    }


    public Object getMyLocalData() {
        return props;
    }
}

