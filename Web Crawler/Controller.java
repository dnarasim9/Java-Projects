    import java.io.BufferedWriter;
    import java.io.File;
    import java.io.FileWriter;
    import java.io.IOException;
    import java.io.PrintWriter;

    import edu.uci.ics.crawler4j.crawler.CrawlConfig;
    import edu.uci.ics.crawler4j.crawler.CrawlController;
    import edu.uci.ics.crawler4j.fetcher.PageFetcher;
    import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
    import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

    public class Controller {
        public static int total_uniqueUrlsExtracted = 0;
        public static int totalUrlsExtracted = 0;
        public static int totalUrlsFetched = 0;
        
       public static void main(String[] args) throws Exception {
           String crawlStorageFolder = "D:\\Dheemanth\\US\\USC\\CSCI 572 - Information Retrieval and Web Search Engines\\Homeworks\\Homework 2 - Web Crawling\\Crawl Storage Folder";
           String storageFolder = "D:\\Dheemanth\\US\\USC\\CSCI 572 - Information Retrieval and Web Search Engines\\Homeworks\\Homework 2 - Web Crawling\\Crawl Storage Folder\\Storage Folder";
           int numberOfCrawlers = 7;

           CrawlConfig config = new CrawlConfig();
           config.setCrawlStorageFolder(crawlStorageFolder);
           config.setMaxDownloadSize(3000000);
           config.setMaxDepthOfCrawling(5);
           config.setMaxPagesToFetch(5000);
           config.setUserAgentString("4");
           config.setIncludeBinaryContentInCrawling(true);
           config.setResumableCrawling(false);
         
           String[] crawlDomains = {"http://cinema.usc.edu/"};
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
           for (String domain : crawlDomains) {
               controller.addSeed(domain);
             }

            //controller.addSeed("http://cinema.usc.edu/");
           
           

           /*
            * Start the crawl. This is a blocking operation, meaning that your code
            * will reach the line after this only when crawling is finished.
            */
           controller.start(MyCrawler.class, numberOfCrawlers);
           writeToReport_1("Fetch Statistics");
           writeToReport_1("=====================");
           
           writeToReport("Fetches Attemped:",MyCrawler.fetchAttempted);
           writeToReport("Fetches Succeeded:", MyCrawler.fetchSucceded);
           writeToReport("Fetches Aborted:", MyCrawler.aborted);
           writeToReport("Fetches Failed:", MyCrawler.fetchFailedorAborted);
          
           writeToReport_1("Outgoing URL's");
           writeToReport_1("=====================");
         
           totalUrlsExtracted=MyCrawler.fetchAttempted+MyCrawler.totalUrls;
           total_uniqueUrlsExtracted = MyCrawler.totalUniqueUrlWithinCinema+MyCrawler.totalUniqueUrlWithinUsc+MyCrawler.totalUniqueUrlOutsideUsc;
           writeToReport("Total URLS Extracted:",totalUrlsExtracted);
           writeToReport("# unique URLs extracted:",total_uniqueUrlsExtracted);
           writeToReport("# unique URLs within School:",MyCrawler.totalUniqueUrlWithinCinema);
           writeToReport("# unique USC URLs outside School::",MyCrawler.totalUniqueUrlWithinUsc);
           writeToReport("# unique URLs outside USC:",MyCrawler.totalUniqueUrlOutsideUsc);
           
           writeToReport_1("Status Codes");
           writeToReport_1("=====================");
           if(MyCrawler.status_code_200>0)
           writeToReport("200 OK: ",MyCrawler.status_code_200);
           if(MyCrawler.status_code_301>0)
           writeToReport("301 Moved Permanently: ",MyCrawler.status_code_301);
           if(MyCrawler.status_code_302>0)
           writeToReport("302 : ",MyCrawler.status_code_302);
           if(MyCrawler.status_code_401>0)
           writeToReport("400 : ",MyCrawler.status_code_401);
           if(MyCrawler.status_code_402>0)
           writeToReport("402 : ",MyCrawler.status_code_402);
           if(MyCrawler.status_code_404>0)
           writeToReport("404 Not Found: ",MyCrawler.status_code_404);
           if(MyCrawler.status_code_2XX>0)
           writeToReport("2XX : ",MyCrawler.status_code_2XX);
           if(MyCrawler.status_code_3XX>0)
           writeToReport("3XX : ",MyCrawler.status_code_3XX);
           if(MyCrawler.status_code_410>0)
           writeToReport("410 Gone: ",MyCrawler.status_code_410);
           if(MyCrawler.status_code_500>0)
           writeToReport("500 Internal Server Error: ",MyCrawler.status_code_500);
             
           
           writeToReport_1("File Sizes");
           writeToReport_1("=====================");
           
           writeToReport("< 1 KB :" ,MyCrawler.size_counter_1KB);
           writeToReport("1KB ~ < 10 KB :" ,MyCrawler.size_counter_1_10KB);
           writeToReport("10KB ~ < 100 KB :" ,MyCrawler.size_counter_10_100KB);
           writeToReport("100KB ~ < 1 MB :" ,MyCrawler.size_counter_100_1MB);
           writeToReport("> = 1 MB :" ,MyCrawler.size_counter_1MB);
                          
           writeToReport_1("Content Types");
           writeToReport_1("=====================");
           
           writeToReport("text/html:" ,MyCrawler.text_html);
           writeToReport("application/pdf:" ,MyCrawler.app_pdf);
           writeToReport("application/msword:" ,MyCrawler.app_msword);
                 
          
           
       }

        public static void writeToReport(String Name, int value) throws IOException {
            File repfile=new File("D:\\Dheemanth\\US\\USC\\CSCI 572 - Information Retrieval and Web Search Engines\\Homeworks\\Homework 2 - Web Crawling\\Crawl Storage Folder\\CrawlReport.txt");
           try(PrintWriter p1 = new PrintWriter(new BufferedWriter(new FileWriter(repfile,true))))
           {
               p1.println("\n" +Name+value +"\n");
               
           
           }
        }
        public static void writeToReport_1(String Name) throws IOException {
            File repfile=new File("D:\\Dheemanth\\US\\USC\\CSCI 572 - Information Retrieval and Web Search Engines\\Homeworks\\Homework 2 - Web Crawling\\Crawl Storage Folder\\CrawlReport.txt");
           try(PrintWriter p1 = new PrintWriter(new BufferedWriter(new FileWriter(repfile,true))))
           {
               p1.println("\n" +Name +"\n");
               
           
           }
        }
    }
