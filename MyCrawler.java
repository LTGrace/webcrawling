import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableWorkbook;

public class MyCrawler extends WebCrawler{
	 
	 private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|json|xml"
             + "|mp3|zip|gz))$");
	 //WritableWorkbook book = Workbook.createWorkbook(new File("test.xls"));
		/**
		* This method receives two parameters. The first parameter is the page
		* in which we have discovered this new url and the second parameter is
		* the new url. You should implement this function to specify whether
		* the given url should be crawled or not (based on your crawling logic).
		* In this example, we are instructing the crawler to ignore urls that
		* have css, js, git, ... extensions and to only accept urls that start
		* with "http://www.viterbi.usc.edu/". In this case, we didn't need the
		* referringPage parameter to make the decision.
		*/
	 
	 CrawlStat myCrawlStat;
	 
	 public MyCrawler() {
		 
		 myCrawlStat = new CrawlStat();
		 
	 }
	 
	 @Override
	 public  boolean shouldVisit(Page referringPage, WebURL url) {
		 
		    String href = url.getURL().toLowerCase();
		    return !FILTERS.matcher(href).matches()
		           && (href.startsWith("https://www.mercurynews.com") || href.startsWith("http://www.mercurynews.com"));
	 }
	 @Override
	 public void visit(Page page) {
		 
		//the resulting content type of the file
		 String type = page.getContentType();	
         int index = type.indexOf(";");
         if(index != -1) {
        	 
        	 type = type.substring(0, index);
        
         }
         
	         myCrawlStat.incContentType(type);
			 String url = page.getWebURL().getURL();
		     myCrawlStat.incFetchSuccessPages();
		     int statuscode = page.getStatusCode();
		     myCrawlStat.incStatusCode(String.valueOf(statuscode));
			 System.out.println(url +  "," + statuscode);
			 myCrawlStat.setFetchedPage(url +  "," + statuscode);
		
	       //the size of the file
			 long filesize = 0;
			 filesize = page.getContentData().length;
			 //if(type.equals("image/png") || type.equals("text/html") || type.equals("image/jpeg")|| type.equals("application/pdf") || type.equals("image/gif"))
			 myCrawlStat.incFilesSize(filesize);
	         
	         Set<WebURL> totaluniquelinks = new HashSet<>();
		 
			 if(page.getParseData() instanceof HtmlParseData) {
				 
				
			         HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			         
			         //the outlinks found
			       	 totaluniquelinks = htmlParseData.getOutgoingUrls();
		       	 
			       	 Iterator<WebURL> it = totaluniquelinks.iterator();
	
			       	 while(it.hasNext()) {
			       		 
			       		 String weburl = it.next().getURL().toLowerCase();
			       		 
			       		 myCrawlStat.incOutgoingUrls(weburl);
			       		 
			       		 if(weburl.startsWith("https://www.mercurynews.com") || weburl.startsWith("http://www.mercurynews.com")) {
			       		 	
			       			myCrawlStat.setallUrls(weburl + "," + "OK");
			       			
			       		    System.out.println(weburl + "," + "OK");
			       		 }
			       		 else {
			       			 
			       			myCrawlStat.setallUrls(weburl + "," + "N_OK");
			       			
			       	 		System.out.println(weburl + "," + "N_OK");
			       	 		
			       		 }
			       		   
			       	 }
			       	 myCrawlStat.incTotalLinks(totaluniquelinks.size());
			 }
		 
			 myCrawlStat.setVisitedPage(url + "," + filesize + "," + totaluniquelinks.size()+","+type);
	       	 logger.warn("Visited Page url-size-outlinks-type:"+ url + "," + filesize + "," + totaluniquelinks.size()+","+type);
        
   }
	 
	 @Override
	 public Object getMyLocalData() {
		 
	        return myCrawlStat;
	 }
	 
	@Override
	protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
        // Do nothing by default
        // Sub-classed can override this to add their custom functionality
		    String urlStr = webUrl.getURL().toLowerCase();
		    
			myCrawlStat.incProcessedPages();
			
			if(statusCode < 200 ||
	                statusCode > 299) {
			
				myCrawlStat.incFetchFailure();
				
				myCrawlStat.incStatusCode(String.valueOf(statusCode));
				
				myCrawlStat.setFetchedPage(urlStr +  "," + statusCode);
				
				 if(urlStr.startsWith("https://www.mercurynews.com") || urlStr.startsWith("http://www.mercurynews.com"))
		       		 	
		       			myCrawlStat.setallUrls(urlStr + "," + "OK");
		       		
		       		 else
		       			 
		       			myCrawlStat.setallUrls(urlStr + "," + "N_OK");
				 
				 
				System.out.println(urlStr + "," + statusCode);
				logger.warn("Skipping URL: {}, StatusCode: {}, {}, {}", urlStr, statusCode);
		  }
			
			
	}
	
}