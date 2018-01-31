//package edu.uci.ics.crawler4j.examples.localdata;
import java.util.*;

public class CrawlStat {
	
	  static int[] numFetch = new int[3];
	  
	  static int[] numUrl = new int[4];
	  
	  static Set<String> urls = new HashSet<String>();
	  static Set<String> urlInside = new HashSet<String>();
      static Set<String> urlOutside = new HashSet<String>();
      
      static Map<Integer, Integer> statusCode_s = new HashMap<Integer, Integer>();
      
      static int[] fileSize_s = new int[5];
      
      static Map<String, Integer> contentType_s = new HashMap<String, Integer>();
      
      static int linkSize = 0;
      
//    private int totalProcessedPages;
//    private long totalLinks;
//    private long totalTextSize;
//
//    public int getTotalProcessedPages() {
//        return totalProcessedPages;
//    }
//
//    public void setTotalProcessedPages(int totalProcessedPages) {
//        this.totalProcessedPages = totalProcessedPages;
//    }
//
//    public void incProcessedPages() {
//        this.totalProcessedPages++;
//    }
//
//    public long getTotalLinks() {
//        return totalLinks;
//    }
//
//    public void setTotalLinks(long totalLinks) {
//        this.totalLinks = totalLinks;
//    }
//
//    public long getTotalTextSize() {
//        return totalTextSize;
//    }
//
//    public void setTotalTextSize(long totalTextSize) {
//        this.totalTextSize = totalTextSize;
//    }
//
//    public void incTotalLinks(int count) {
//        this.totalLinks += count;
//    }
//
//    public void incTotalTextSize(int count) {
//        this.totalTextSize += count;
//    }
}