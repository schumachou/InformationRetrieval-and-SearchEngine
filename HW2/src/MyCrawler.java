/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//package edu.uci.ics.crawler4j.examples.basic;

import java.util.Set;
import java.util.regex.Pattern;
//import java.util.*;

//import org.apache.http.Header;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.*;

/**
 * @author Yasser Ganjisaffar
 */
public class MyCrawler extends WebCrawler {
	
    private final static Pattern FILTERS = Pattern.compile(
            ".*(\\.(css|js|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v" +
            "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
    private static final Pattern imgPatterns = Pattern.compile(".*(\\.(bmp|gif|jpe?g|png|tiff?))$");
    
    CrawlStat myCrawlStat;

    public MyCrawler() {
        myCrawlStat = new CrawlStat();
    }
    

    /**
     * This function is called once the header of a page is fetched. It can be
     * overridden by sub-classes to perform custom logic for different status
     * codes. For example, 404 pages can be logged, etc.
     *
     * @param webUrl WebUrl containing the statusCode
     * @param statusCode Html Status Code number
     * @param statusDescription Html Status COde description
     */
    @Override
    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
        
    	CrawlStat.numFetch[0]++;
   	 
	   	if(statusCode < 200 || statusCode > 299) {
	   		CrawlStat.numFetch[2]++;
	  	} else {
	  		CrawlStat.numFetch[1]++;
	   	} 
	   	
	   	//collect all status code
	   	Integer oldValue = CrawlStat.statusCode_s.get(statusCode);
		
		if (oldValue == null) {
			CrawlStat.statusCode_s.put(statusCode, 1);
		}
		else {
			CrawlStat.statusCode_s.put(statusCode, oldValue + 1);
		}
	   	
	   	try(FileWriter fw = new FileWriter("fetch_Wall_Street_Journal.csv", true)) {   
	   		 
	   		 fw.append(webUrl.toString().replace(',','_'));
	   		 fw.append(',');
	   		 fw.append("" + statusCode);
	   		 fw.append('\n');
	           	 
	     } catch (IOException e) {
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
        
        if (page.getParseData() instanceof HtmlParseData) { //html
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
//            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();             
 	   
       	    String contentType = page.getContentType();
        	String[] token = contentType.split(Pattern.quote(";"));
        	
        	//whether content type is what we want or not
        	if(!token[0].equals("text/html") && !token[0].equals("application/pdf")) {
        		return;
        	}
        	
        	//collect all content type
       	    Integer oldValue = CrawlStat.contentType_s.get(token[0]);
    		if (oldValue == null) {
    			CrawlStat.contentType_s.put(token[0], 1);
    		}
    		else {
    			CrawlStat.contentType_s.put(token[0], oldValue + 1);
    		}
    		
    		//collect file size
       	    int fileSize = html.length();
       	    if(fileSize < 1000) {
       	    	CrawlStat.fileSize_s[0]++;
       	    } else if(fileSize < 10000) {
       	    	CrawlStat.fileSize_s[1]++;
       	    } else if(fileSize < 100000) {
       	    	CrawlStat.fileSize_s[2]++;
       	    } else if(fileSize < 1000000) {
       	    	CrawlStat.fileSize_s[3]++;
       	    } else {
       	    	CrawlStat.fileSize_s[4]++;
       	    }
    		
    		//write visit_Wall_Street_Journal.csv
            try(FileWriter fw = new FileWriter("visit_Wall_Street_Journal.csv", true)) {
            	
	           	fw.append(page.getWebURL().toString().replace(',','_'));
	           	fw.append(",");
	            fw.append("" + fileSize);
	          	fw.append(",");
	          	fw.append("" + links.size());
	         	fw.append(",");
	           	fw.append(token[0]);
	           	fw.append("\n");
           	 
            } catch (IOException e) {
           	 	e.printStackTrace();
            }
        } else { //image
        	if (!imgPatterns.matcher(url).matches() || !((page.getParseData() instanceof BinaryParseData))) {
                    return;
        	}
        	
			//collect all content type
		    String contentType = page.getContentType();
		    Integer oldValue = CrawlStat.contentType_s.get(contentType);
		
			if (oldValue == null) {
				CrawlStat.contentType_s.put(contentType, 1);
			} else {
				CrawlStat.contentType_s.put(contentType, oldValue + 1);
			}
			
			//collect file size
		    int fileSize = page.getContentData().length;
 		    if(fileSize < 1000) {
 		    	CrawlStat.fileSize_s[0]++;
 		    } else if(fileSize < 10000) {
 		    	CrawlStat.fileSize_s[1]++;
 		    } else if(fileSize < 100000) {
 		    	CrawlStat.fileSize_s[2]++;
 		    } else if(fileSize < 1000000) {
 		    	CrawlStat.fileSize_s[3]++;
		    } else {
		    	CrawlStat.fileSize_s[4]++;
		    }
			
			//write visit_Wall_Street_Journal.csv
			try(FileWriter fw = new FileWriter("visit_Wall_Street_Journal.csv", true)) {
				
			   	fw.append(page.getWebURL().toString().replace(',','_'));
			   	fw.append(",");
			    fw.append("" + page.getContentData().length);
			  	fw.append(",");
			  	fw.append("0");
			 	fw.append(",");
			   	fw.append(page.getContentType());
			   	fw.append("\n");
				 
			} catch (IOException e) {
				 	e.printStackTrace();
			}
        	
        }
    }    
    
    
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
 
         String href = url.getURL().toLowerCase();
       
         //collect all urls
         CrawlStat.numUrl[0]++;
         
         boolean repeat = true;
         if(!CrawlStat.urls.contains(url.toString().replace(',','_'))) {      
             repeat = false;
         }
               
	     try(FileWriter fw = new FileWriter("urls_Wall_Street_Journal.csv", true)) {
	    	 fw.append(url.toString().replace(',','_'));
	    	 fw.append(",");
	         CrawlStat.urls.add(url.toString().replace(',','_'));
	    	 if(href.startsWith("https://www.wsj.com/") || href.startsWith("http://www.wsj.com/")){
	    		 fw.append("OK");
	    	 	 CrawlStat.urlInside.add(url.toString().replace(',','_'));
	    	 } else {
	    		 fw.append("N_OK");
	    	 	 CrawlStat.urlOutside.add(url.toString().replace(',','_'));
	    	 }
	    	 fw.append("\n");
	     } catch (IOException e) {
	    	 e.printStackTrace();
	     }
         
	   
	        if (FILTERS.matcher(href).matches()) {
	            return false;
	        }

	        if (imgPatterns.matcher(href).matches()) {
	            return true;
	        }
	        
	        if(repeat) {
	        	return false;
	        }
	        
	        if(href.startsWith("https://www.wsj.com/") || href.startsWith("http://www.wsj.com/")){
                return true;
  
	        }
	        return false;
	     
     }
     

     /**
      * The CrawlController instance that has created this crawler instance will
      * call this function just before terminating this crawler thread. Classes
      * that extend WebCrawler can override this function to pass their local
      * data to their controller. The controller then puts these local data in a
      * List that can then be used for processing the local data of crawlers (if needed).
      *
      * @return currently NULL
      */
     @Override
     public Object getMyLocalData() { 	
    	 CrawlStat.numUrl[2] =  CrawlStat.urlInside.size();
    	 CrawlStat.numUrl[3] = CrawlStat.urlOutside.size();
    	 CrawlStat.numUrl[1] = CrawlStat.urls.size();
    	 
//    	 System.out.print(CrawlStat.urlInside);
//    	 System.out.print(CrawlStat.urlOutside);
    	 
    	 Object[] result = {CrawlStat.numFetch, CrawlStat. numUrl, CrawlStat.statusCode_s, CrawlStat.fileSize_s, CrawlStat.contentType_s};
    	 return result;
     }
}