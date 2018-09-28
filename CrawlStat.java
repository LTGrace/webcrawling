import java.util.ArrayList;
import java.util.List;

public class CrawlStat {
	
	private int totalProcessedPages;
    private long totalLinks;
    private long totalTextSize;
    private long fetchsuccess;
    private long fetchfailorabort;
    private List<String> fetchedpages = new ArrayList<String>();
    private List<String> visitedpages = new ArrayList<String>();
    private List<String> allUrls = new ArrayList<String>();
    private List<String> OutgoingUrls = new ArrayList<String>();
    private List<String> statusCode = new ArrayList<String>();
    private List<Long> filesize = new ArrayList<Long>();
    private List<String> ContentType = new ArrayList<String>();

    public int getTotalProcessedPages() {
        return totalProcessedPages;
    }

    public void setTotalProcessedPages(int totalProcessedPages) {
        this.totalProcessedPages = totalProcessedPages;
    }

    public void incProcessedPages() {
        this.totalProcessedPages++;
    }
    
    public void incFetchSuccessPages() {
    	this.fetchsuccess++;
    }
    
    public void incFetchFailure() {
    	
    	this.fetchfailorabort++;
    	
    }
    
    public long getFetchFailure() {
    	
    	return fetchfailorabort;
    }
    
    public long getFetchSuccessPages() {
    	
    	return fetchsuccess;
    }

    public long getTotalLinks() {
        return totalLinks;
    }

    public void setTotalLinks(long totalLinks) {
        this.totalLinks = totalLinks;
    }

    public long getTotalTextSize() {
        return totalTextSize;
    }

    public void setTotalTextSize(long totalTextSize) {
        this.totalTextSize = totalTextSize;
    }

    public void incTotalLinks(int count) {
        this.totalLinks += count;
    }

    public void incTotalTextSize(int count) {
        this.totalTextSize += count;
    }
    
    public void setFetchedPage(String fetchpage) {
    	
    	this.fetchedpages.add(fetchpage);
    	
    }
    
    public List<String> getFetchedPage(){
    	
    	return fetchedpages;
    	
    }
    
    public void setVisitedPage(String visitpage) {
    	
    	this.visitedpages.add(visitpage);
    	
    }
    
    public List<String> getVisitedPage(){
    	
    	return visitedpages;
    	
    }
    
    public void setallUrls(String url) {
    	
    	this.allUrls.add(url);
    	
    }
    
    public List<String> getallUrls(){
    	
    	return allUrls;
    	
    }
    
    public void incOutgoingUrls(String url) {
    	
    	  this.OutgoingUrls.add(url);
    	  
    }
    
    public List<String> getOutgoingUrls(){
    	
    	  return OutgoingUrls;
    }
    
    public void incStatusCode(String statuscode){
    	
    	   this.statusCode.add(statuscode);
    	
    }
    
    public List<String> getStatusCodes(){
    	
    	  return statusCode;
    }
    
    public void incFilesSize(long textsize) {
    	  
    	filesize.add(textsize);
    }
    
    public List<Long> getFilesSize(){
    	
    	return filesize;
    	
    }
    
    public void incContentType(String contenttype) {
    	
    	ContentType.add(contenttype);
    	
    }
    
    public List<String> getContentType(){
    	
    	return ContentType;
    }
	
}
