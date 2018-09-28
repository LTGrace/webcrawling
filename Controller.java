import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.slf4j.LoggerFactory;

public class Controller {
	
	 private static final Logger logger = LoggerFactory.getLogger(Controller.class);
	
	public static void main(String[] args) throws Exception {
		/*
		 * 
		 *The code below will save downloaded files in /data/crawl
		 */
		List<String> fetchedpages;
		List<String> visitedpages;
		List<String> allUrls;
		List<String> OutgoingUrls;
		Set<String> uniqueUrls = new HashSet<>();
		Set<String> uniqueWithinUrls = new HashSet<>();
		Set<String> uniqueOutsideUrls = new HashSet<>();
		List<String> statusCodes;
		List<Long> FilesSize;
		Map<String,Integer> statuscodemap = new HashMap<>();
		Map<String,Integer> FilesSizemap = new HashMap<>();
		FilesSizemap.put("< 1KB", 0);
		FilesSizemap.put("1KB ~ <10KB", 0);
		FilesSizemap.put("10KB ~ <100KB", 0);
		FilesSizemap.put("100KB ~ <1MB", 0);
		FilesSizemap.put(">= 1MB", 0);
		List<String> ContentType;
		Map<String,Integer> ContentTypeMap = new HashMap<>();
		
        String crawlStorageFolder = "/Users/gracelt/eclipse-workspace/Crawler_Project/data/crawl";
        int numberOfCrawlers = 7;
        CrawlConfig config = new CrawlConfig();
        config.setIncludeHttpsPages(true);
        config.setIncludeBinaryContentInCrawling(true);
        config.setPolitenessDelay(200);
        config.setMaxDepthOfCrawling(16);
        config.setMaxPagesToFetch(20000);
        config.setCrawlStorageFolder(crawlStorageFolder);
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
* 
*/
		//there is only a single seed, http://viterbi.usc.edu/
		controller.addSeed("https://www.mercurynews.com/");
		/*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(MyCrawler.class, numberOfCrawlers);
        
        //fetch file
        
        File csv1 = new File("/Users/gracelt/Desktop/fetch.csv");
        BufferedWriter bw1 = new BufferedWriter(new FileWriter(csv1,true)); 
        
        //visit file
        
        File csv2 = new File("/Users/gracelt/Desktop/visit.csv");
        BufferedWriter bw2 = new BufferedWriter(new FileWriter(csv2,true));
        
        //url file
        
        File csv3 = new File("/Users/gracelt/Desktop/url.csv");
        BufferedWriter bw3 = new BufferedWriter(new FileWriter(csv3,true));
        
        List<Object> crawlersLocalData = controller.getCrawlersLocalData();
        long totalOutLinks = 0;
        long totalTextSize = 0;
        int totalProcessedPages = 0;
        int fetchedsuccessPages = 0;
        int fetchedfailure = 0;
        
        for (Object localData : crawlersLocalData) {
            CrawlStat stat = (CrawlStat) localData;
            fetchedpages = stat.getFetchedPage();
            visitedpages = stat.getVisitedPage();
            allUrls = stat.getallUrls();
            OutgoingUrls = stat.getOutgoingUrls();
            statusCodes = stat.getStatusCodes();
            FilesSize = stat.getFilesSize();
            ContentType = stat.getContentType();
          //write data in fetch.csv
            for(String fetchedpage:fetchedpages) {
            	bw1.write(fetchedpage);
            	bw1.newLine();
            }
          //write data in visit.csv
            for(String visitedpage:visitedpages) {
            	
            	bw2.write(visitedpage);
            	bw2.newLine();
            	
            }
            
         //write data in url.csv
            for(String url:allUrls) {
            	
            	bw3.write(url);
            	bw3.newLine();
            }
            
            for(String OutgoingUrl: OutgoingUrls) {
            	
            	uniqueUrls.add(OutgoingUrl);
            	
            	if(OutgoingUrl.startsWith("https://www.mercurynews.com"))
            		
            		uniqueWithinUrls.add(OutgoingUrl);
            	
            	else
            		
            		uniqueOutsideUrls.add(OutgoingUrl);
            		
            	
            }
            
            for(String statuscode:statusCodes) {
            	
            	statuscodemap.put(statuscode,statuscodemap.getOrDefault(statuscode, 0)+1);
            	
            }
            
            for(long filesize: FilesSize) {
           
            	if(filesize/1024 < 1)
            	
            	    FilesSizemap.put("< 1KB", FilesSizemap.get("< 1KB")+1);
            	
            	else if(filesize/1024 >=1 && filesize/10/1024 < 1)
            		
            		FilesSizemap.put("1KB ~ <10KB", FilesSizemap.get("1KB ~ <10KB")+1);
            	
            	else if(filesize/10/1024 >= 1  && filesize/100/1024 <1)
            		
            		FilesSizemap.put("10KB ~ <100KB", FilesSizemap.get("10KB ~ <100KB")+1);
            	
            	else if(filesize/100/1024 >= 1  && filesize/1024/1024 <1)
            		
            		FilesSizemap.put("100KB ~ <1MB", FilesSizemap.get("100KB ~ <1MB")+1);
            	
            	else 
            		
            		FilesSizemap.put(">= 1MB", FilesSizemap.get(">= 1MB")+1);
         
            }
            
            for(String type:ContentType) {
            	
            	ContentTypeMap.put(type,ContentTypeMap.getOrDefault(type, 0)+1);
            	
            }
            

             fetchedsuccessPages += stat.getFetchSuccessPages();
             fetchedfailure += stat.getFetchFailure();
             totalProcessedPages += stat.getTotalProcessedPages();
             totalOutLinks += stat.getTotalLinks();
             
        }
        
         bw1.close();
         bw2.close();
         bw3.close();
         System.out.println("totalProcessedPage:" +totalProcessedPages);
         System.out.println("fetchedsuccessPages:"+fetchedsuccessPages);
         System.out.println("fetchedfailure:"+fetchedfailure);
         System.out.println("fetchedabort:" + (totalProcessedPages-fetchedsuccessPages- fetchedfailure));
         System.out.println("totalOutLinks:"+totalOutLinks);
         System.out.println("uniqueUrls:" + uniqueUrls.size());
         System.out.println("uniqueWithUrls:" + uniqueWithinUrls.size());
         System.out.println("uniqueOutsideUrls:"+uniqueOutsideUrls.size());
         for(String statuscode:statuscodemap.keySet()) {
        	 
        	 System.out.println("StatusCode:" + statuscode + ":" + statuscodemap.get(statuscode));
        	 
         }
         for(String filesize:FilesSizemap.keySet()) {
        	 
        	 System.out.println("FileSize:" + filesize + ":" + FilesSizemap.get(filesize));
        	 
         }
         for(String contenttype:ContentTypeMap.keySet()) {
        	 
        	 System.out.println("ContentType:" + contenttype + ":" + ContentTypeMap.get(contenttype));
        	 
         }
         
    }
}
