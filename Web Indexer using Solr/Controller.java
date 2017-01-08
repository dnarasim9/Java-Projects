import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;


public class Controller
{
    public static void main(String[] args) throws Exception
    {
        String crawlStorageFolder = "download/";
        int numberOfCrawlers = 7;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxPagesToFetch(5000);
        config.setMaxDepthOfCrawling(5);
        config.setIncludeBinaryContentInCrawling(true);

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        controller.addSeed("http://cinema.usc.edu/");


        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(MyCrawler.class,numberOfCrawlers);

        System.out.println("DONE");


        int fetchesAttempted = 0;
        int fetchesSucceeded = 0;
        int fetchesAborteded = 0;
        int fetchesFailed = 0;
        int totalUrlsExtracted = 0;
        List<Object> results=controller.getCrawlersLocalData();
        HashSet<String> allUniqueUrls = new HashSet<String>();
        HashSet<String> schoolUrls = new HashSet<String>();
        HashSet<String> uscUrls = new HashSet<String>();
        HashSet<String> outsideUrls = new HashSet<String>();
        HashMap<Integer,Integer> noOfstatusCodes = new HashMap<Integer, Integer>();
        noOfstatusCodes.put(200,0);
        noOfstatusCodes.put(301,0);
        noOfstatusCodes.put(401,0);
        noOfstatusCodes.put(403,0);
        noOfstatusCodes.put(404,0);
        int lessThanOne = 0;
        int oneToTen = 0;
        int tenToHundred = 0;
        int hundredToOne = 0;
        int greaterThanOne = 0;
        int text = 0;
        int gif = 0;
        int jpeg = 0;
        int png = 0;
        int pdf = 0;
        int word = 0;
        int xml = 0;



        FileWriter fileWriter1 = null;
        CSVPrinter csvFilePrinter1 = null;
        CSVFormat csvFileFormat1 = CSVFormat.DEFAULT.withRecordSeparator("\n");
        FileWriter fileWriter2 = null;
        CSVPrinter csvFilePrinter2 = null;
        CSVFormat csvFileFormat2 = CSVFormat.DEFAULT.withRecordSeparator("\n");
        FileWriter fileWriter3 = null;
        CSVPrinter csvFilePrinter3 = null;
        CSVFormat csvFileFormat3 = CSVFormat.DEFAULT.withRecordSeparator("\n");
        FileWriter fileWriter4 = null;
        CSVPrinter csvFilePrinter4 = null;
        CSVFormat csvFileFormat4 = CSVFormat.DEFAULT.withRecordSeparator("\n");
        FileWriter fileWriter5 = null;
        CSVPrinter csvFilePrinter5 = null;
        CSVFormat csvFileFormat5 = CSVFormat.DEFAULT.withRecordSeparator("\n");
        try
        {
            fileWriter1 = new FileWriter("fetch.csv");
            csvFilePrinter1 = new CSVPrinter(fileWriter1, csvFileFormat1);
            List p1 = new ArrayList();
            p1.add("URL");
            p1.add("HTTP STATUSCODE");
            csvFilePrinter1.printRecord(p1);

            fileWriter2 = new FileWriter("visit.csv");
            csvFilePrinter2 = new CSVPrinter(fileWriter2, csvFileFormat2);
            List p2 = new ArrayList();
            p2.add("URL");
            p2.add("SIZE");
            p2.add("NO. OF OUTLINKS");
            p2.add("CONTENT_TYPE");
            csvFilePrinter2.printRecord(p2);

            fileWriter3 = new FileWriter("urls.csv");
            csvFilePrinter3 = new CSVPrinter(fileWriter3, csvFileFormat3);
            List p3 = new ArrayList();
            p3.add("URL");
            p3.add("WHERE URL RESIDES");
            csvFilePrinter3.printRecord(p3);

            fileWriter4 = new FileWriter("pagerankdata.csv");
            csvFilePrinter4 = new CSVPrinter(fileWriter4, csvFileFormat4);
            List p4 = new ArrayList();

            fileWriter5 = new FileWriter("dict.csv");
            csvFilePrinter5 = new CSVPrinter(fileWriter5, csvFileFormat5);
            List p5 = new ArrayList();

        }
        catch (Exception e)
        {
            System.out.println("Error in CsvFileWriter !!!");
        }

////        File file1 = new File("fetch.csv");
//        File file2 = new File("visit.csv");
//        File file3 = new File("urls.csv");
       File file4 = new File("crawlReport.txt");
//        //file1.createNewFile();
//        file2.createNewFile();
//        file3.createNewFile();
        file4.createNewFile();
//
//
//
//        FileWriter fetch = new FileWriter(file1);
//        FileWriter visit = new FileWriter(file2);
//        FileWriter urls = new FileWriter(file3);
        FileWriter report = new FileWriter(file4);

            for (Object o : results)
            {

                {
                    CrawlerProperties properties = (CrawlerProperties) o;

                    for (int i = 0; i < properties.urls.size(); i++)
                    {
                        List p1 = new ArrayList();
                        p1.add(properties.urls.get(i));
                        p1.add(properties.statusCodes.get(i));
                        csvFilePrinter1.printRecord(p1);
                        //fetch.write(properties.urls.get(i) + "," + properties.statusCodes.get(i)+"\n");
                    }


                    for (int i = 0; i < properties.successfulURLs.size(); i++)
                    {
                        List p2 = new ArrayList();
                        p2.add(properties.successfulURLs.get(i));
                        p2.add(properties.sizes.get(i));
                        p2.add(properties.numberOfOutlinks.get(i));
                        p2.add(properties.contentTypes.get(i));
                        csvFilePrinter2.printRecord(p2);
                        List p4 = new ArrayList();
                        p4.add(properties.successfulURLs.get(i));
                        Set<WebURL> links = properties.links.get(i);
                        for(WebURL l : links)
                        {
                            p4.add(l.getURL());
                        }
                        csvFilePrinter4.printRecord(p4);

                        List p5 = new ArrayList();
                        p5.add(properties.successfulURLs.get(i));
                        p5.add(properties.filenames.get(i));
                        csvFilePrinter5.printRecord(p5);
                        //visit.write(properties.successfulURLs.get(i) + "," + properties.sizes.get(i) + "," + properties.contentTypes.get(i) + "," + properties.numberOfOutlinks.get(i)+"\n");
                    }


                    for (int i = 0; i < properties.ourls.size(); i++)
                    {
                        List p3 = new ArrayList();
                        p3.add(properties.ourls.get(i));
                        p3.add(properties.types.get(i));
                        csvFilePrinter3.printRecord(p3);
                        //urls.write(properties.ourls.get(i) + "," + properties.types.get(i)+"\n");
                    }

                    //-----------Statistics-----------

                    fetchesAttempted += properties.urls.size();


                    for (int i = 0; i < properties.statusCodes.size(); i++)
                    {
                        if (properties.statusCodes.get(i) == 200)
                            fetchesSucceeded++;
                        else
                        {
                            if (properties.statusCodes.get(i) >= 300 && properties.statusCodes.get(i) < 400)
                                fetchesAborteded++;
                            else
                                fetchesFailed++;
                        }
                        try
                        {
                            int value = noOfstatusCodes.get(properties.statusCodes.get(i));
                            noOfstatusCodes.put(properties.statusCodes.get(i), value + 1);
                        }
                        catch(NullPointerException e)
                        {
                            noOfstatusCodes.put(properties.statusCodes.get(i), 1);
                        }
                    }

                    totalUrlsExtracted += properties.ourls.size();
                    for (int i = 0; i < properties.ourls.size(); i++)
                    {
                        WebURL url = properties.ourls.get(i);
                        allUniqueUrls.add(url.getURL());
                        if (url.getSubDomain().toLowerCase().contains("cinema") && url.getDomain().toLowerCase().contains("usc"))
                        {
                            schoolUrls.add(url.getURL());
                        } else
                        {
                            if (url.getDomain().toLowerCase().contains("usc"))
                            {
                                uscUrls.add(url.getURL());
                            } else
                            {
                                outsideUrls.add(url.getURL());
                            }
                        }
                    }

                    for (int i = 0; i < properties.sizesStats.size(); i++)
                    {
                        int s = properties.sizesStats.get(i);
                        if (s <= 1024)
                            lessThanOne++;
                        if (s > 1024 && s <= 10240)
                            oneToTen++;
                        if (s > 10240 && s <= 102400)
                            tenToHundred++;
                        if (s > 102400 && s <= 1024000)
                            hundredToOne++;
                        if (s > 1024000)
                            greaterThanOne++;
                    }

                    for(int i=0 ; i<properties.contentTypesStats.size();i++)
                    {
                        String content = properties.contentTypesStats.get(i);
                        if(content.toLowerCase().contains("text/html"))
                            text++;
                        if(content.toLowerCase().contains("image/gif"))
                            gif++;
                        if(content.toLowerCase().contains("image/jpeg"))
                            jpeg++;
                        if(content.toLowerCase().contains("image/png"))
                            png++;
                        if(content.toLowerCase().contains("application/pdf"))
                            pdf++;
                        if(content.toLowerCase().contains("application/msword"))
                            word++;
                        if(content.toLowerCase().contains("application/xml"))
                            xml++;
                    }

                }


//

            }

        report.write("Fetch Statistics" + "\n");
        report.write("==============" + "\n");
        report.write(" # fetches attempted:" + fetchesAttempted + "\n");
        report.write(" # fetches succeeded:" + fetchesSucceeded+ "\n");
        report.write(" # fetches aborted:" + fetchesAborteded+ "\n");
        report.write(" # fetches failed:" + fetchesFailed+ "\n\n");

        report.write("Outgoing URLs:" + "\n");
        report.write("==============" + "\n");
        report.write("Total URLs extracted:" + totalUrlsExtracted+ "\n");
        report.write(" # unique URLs extracted:" + allUniqueUrls.size()+ "\n");
        report.write(" # all URLs within school:" + schoolUrls.size()+ "\n");
        report.write(" # unique URLs outside school:" + outsideUrls.size()+ "\n");
        report.write(" # unique URLs outside USC:" + uscUrls.size()+ "\n\n");

        report.write("Status Codes:"+ "\n");
        report.write("==============" + "\n");
        report.write("200 OK:"+ noOfstatusCodes.get(200)+ "\n");
        report.write("301 Moved Permanently:"+ noOfstatusCodes.get(301)+"\n");
        report.write("401 Unauthorised:"+ noOfstatusCodes.get(401)+"\n");
        report.write("403 Forbidden:"+noOfstatusCodes.get(403)+ "\n");
        report.write("404 Not Found:"+ noOfstatusCodes.get(404)+"\n\n");

        report.write("File Sizes:" + "\n");
        report.write("==========="+ "\n");
        report.write("< 1KB:" + lessThanOne + "\n");
        report.write("1KB ~ <10KB:" + oneToTen + "\n");
        report.write("10KB ~ <100KB:" + tenToHundred +"\n");
        report.write("100KB ~ <1MB:" + hundredToOne +"\n");
        report.write(" >= 1MB:" + greaterThanOne + "\n\n");

        report.write("Content Types:" + "\n");
        report.write("==============" + "\n");
        report.write("text/html:" + text +"\n");
        report.write("image/gif:" + gif +"\n");
        report.write("image/jpeg:" + jpeg +  "\n");
        report.write("image/png:" + png + "\n");
        report.write("application/pdf:" + pdf + "\n");
        report.write("application/msword:" + word + "\n");
        report.write("application/xml:" + xml + "\n");



        fileWriter1.flush();
        fileWriter1.close();
        csvFilePrinter1.close();
        fileWriter2.flush();
        fileWriter2.close();
        csvFilePrinter2.close();
        fileWriter3.flush();
        fileWriter3.close();
        csvFilePrinter3.close();
        fileWriter4.flush();
        fileWriter4.close();
        csvFilePrinter4.close();
        fileWriter5.flush();
        fileWriter5.close();
        csvFilePrinter5.close();
//        fetch.flush();
//        fetch.close();
//        visit.flush();
//        visit.close();
//        urls.flush();
//        urls.close();
        report.flush();
        report.close();


    }



}