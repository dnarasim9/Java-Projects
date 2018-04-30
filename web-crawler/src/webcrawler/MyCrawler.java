package webcrawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import com.google.common.io.Files;
import java.net.URL;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {
  // static int count =0;
  private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v|jpeg|png|bmp|gif|tiff" + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
  private static final Pattern filePatterns = Pattern.compile(".*(\\.(pdf|docx|html|htm|doc?))");
  public static int fetchAttempted = 0;
  public static int fetchSucceded = 0;
  public static int fetchFailedorAborted = 0;
  public static int aborted = 0;
  public static int totalUrls = 0;
  public static int totalUniqueUrlWithinCinema = 0;
  public static int totalUniqueUrlWithinUsc = 0;
  public static int totalUniqueUrlOutsideUsc=0;
  public static int count=0;

  /*Size variables*/
  public static int size_counter_1KB=0;
  public static int size_counter_1_10KB=0;
  public static int size_counter_10_100KB=0;
  public static int size_counter_100_1MB=0;
  public static int size_counter_1MB=0;
  public static int size_of_url=0;

  /*Status Code Variables*/
  public static int status_code_200=0;
  public static int status_code_301=0;
  public static int status_code_302=0;
  public static int status_code_401=0;
  public static int status_code_402=0;
  public static int status_code_404=0;
  public static int status_code_2XX=0;
  public static int status_code_3XX=0;
  public static int status_code_410=0;
  public static int status_code_500=0;


  /*content -type variables*/
  public static int text_html=0;
  public static int app_pdf=0;
  public static int app_msword=0;



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

  @Override
  public boolean shouldVisit(Page referringPage, WebURL url) {

    totalUrls=totalUrls+1;
    String href = url.getURL().toLowerCase();
    String relUrl = url.getURL();

    String check = "";
    if(relUrl.contains("cinema.usc.edu")){
      totalUniqueUrlWithinCinema++;
      check = "OK";
    }else if(relUrl.contains("usc.edu")){
      totalUniqueUrlWithinUsc++;
      check = "USC";
    }else{
      totalUniqueUrlOutsideUsc++;
      check = "outUSC";
    }

    File f3= new File("Documents\\urls.csv");
    try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f3, true)))) {
      out.println(url.getURL()+","+check);

    }catch (IOException e) {
      e.printStackTrace();
    }
    return !FILTERS.matcher(href).matches()
        &&href.startsWith("http://cinema.usc.edu/");



  }

  @Override
  public void onStart() {
    // TODO Auto-generated method stub
    //super.onStart();
    File f1=new File("Documents\\fetch.csv");
    File f2=new File("Documents\\visit.csv");
    File f3=new File("Documents\\urls.csv");
    File report = new File("Documents\\CrawlReport.txt");
    FileWriter f4;
    FileWriter f5;
    FileWriter f6;
    FileWriter reportFileWriter;
    try {
      f4 = new FileWriter(f1);
      f4.write("URL"+","+"Status"+"\n");
      f4.close();
      f5 = new FileWriter(f2);
      f5.write("URL"+","+"File Size"+","+"Number of outlinks"+","+"Content-type"+"\n");
      f5.close();
      f6= new FileWriter(f3);
      f6.write("URL"+","+"Type of URL"+"\n");
      f6.close();
      reportFileWriter = new FileWriter(report);
      reportFileWriter.write("Name : Dheemanth Hassan Narasimhan"+"\n");
      //reportFileWriter.write("\n");
      reportFileWriter.write("USC ID :2168438818"+"\n");
      //reportFileWriter.write("\n");
      reportFileWriter.write("School Crawled : USC School of Cinematic Arts"+"\n");
      //reportFileWriter.write("\n");
      reportFileWriter.close();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }


  /**
   * This function is called when a page is fetched and ready
   * to be processed by your program.
   */
  @Override
  public void visit(Page page) {
    String url = page.getWebURL().getURL();
    System.out.println("URL: " + url);

    if (page.getParseData() instanceof HtmlParseData) {

      HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
      String text = htmlParseData.getText();
      String html = htmlParseData.getHtml();
      Set<WebURL> links = htmlParseData.getOutgoingUrls();
        /*size calculation*/
      size_of_url=(Integer)page.getContentData().length;

      if(size_of_url < 1024)
        size_counter_1KB++;
      else if(size_of_url>=1024 && size_of_url<10240)
        size_counter_1_10KB++;
      else if(size_of_url>=10240 && size_of_url<102400)
        size_counter_10_100KB++;
      else if(size_of_url>=102400 && size_of_url<1024000)
        size_counter_100_1MB++;
      else if(size_of_url>=1024000)
        size_counter_1MB++;

        /*content-type detection*/
      if(page.getContentType().contains("text/html"))
        text_html=text_html+1;
      else  if(page.getContentType().contains("application/pdf"))
        app_pdf=app_pdf+1;
      else  if(page.getContentType().contains("application/msword"))
        app_msword=app_msword+1;
      File f5=new File("Documents\\visit.csv");
      try(PrintWriter p1 = new PrintWriter(new BufferedWriter(new FileWriter(f5,true))))
      {
        p1.println(url+","+page.getContentData().length+","+page.getParseData().getOutgoingUrls().size()+","+page.getContentType());
      }
      catch(IOException e)
      {
        e.printStackTrace();
      }
    }
    else if(filePatterns.matcher(url).matches())
    {
      if(size_of_url < 1024)
        size_counter_1KB++;
      else if(size_of_url>=1024 && size_of_url<10240)
        size_counter_1_10KB++;
      else if(size_of_url>=10240 && size_of_url<102400)
        size_counter_10_100KB++;
      else if(size_of_url>=102400 && size_of_url<1024000)
        size_counter_100_1MB++;
      else if(size_of_url>=1024000)
        size_counter_1MB++;


      /*content-type detection*/
      if(page.getContentType().contains("text/html"))
        text_html=text_html+1;
      else  if(page.getContentType().contains("application/pdf"))
        app_pdf=app_pdf+1;
      else  if(page.getContentType().contains("application/msword"))
        app_msword=app_msword+1;
      File f5=new File("Documents\\visit.csv");
      try(PrintWriter p1 = new PrintWriter(new BufferedWriter(new FileWriter(f5,true))))
      {
        p1.println(url+","+page.getContentData().length+","+page.getParseData().getOutgoingUrls().size()+","+page.getContentType());
      }
      catch(IOException e)
      {
        e.printStackTrace();
      }
    }

  }

  @Override
  protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
    // Do nothing by default
    // Sub-classed can override this to add their custom functionality
    fetchAttempted=fetchAttempted+1;
    int status = statusCode;
    String s = Integer.toString(status);

    if(status==200)
      status_code_200=status_code_200+1;
    else if(status==301)
      status_code_301=status_code_301+1;
    else if(status==302)
      status_code_302=status_code_302+1;
    else if(status==401)
      status_code_401=status_code_401+1;
    else if(status==402)
      status_code_402=status_code_402+1;
    else if(status==404)
      status_code_404=status_code_404+1;
    else if(s.startsWith("2"))
      status_code_2XX=status_code_2XX+1;
    else if(s.startsWith("3"))
      status_code_3XX=status_code_3XX+1;
    else if(s.startsWith("4"))
      status_code_410=status_code_410+1;
    else if(s.startsWith("5"))
      status_code_500=status_code_500+1;


    if(statusCode==200)
    {
      fetchSucceded=fetchSucceded+1;
    }else
    if(s.startsWith("3"))
    {
      aborted=aborted+1;
    }
    else
    {
      fetchFailedorAborted=fetchFailedorAborted+1;
    }

    File f1=new File("Documents\\fetch.csv");
    try(PrintWriter p1 = new PrintWriter(new BufferedWriter(new FileWriter(f1,true))))
    {
      p1.println(webUrl.getURL()+","+statusCode);
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
  }

}