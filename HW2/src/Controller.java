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

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * @author Yasser Ganjisaffar
 */
public class Controller {
	private static final Logger logger =
	        LoggerFactory.getLogger(Controller.class);
	
    public static void main(String[] args) throws Exception {
    	
        String crawlStorageFolder = "./data/crawl/root";
        int numberOfCrawlers = 40;
        int maxDepthOfCrawling = 16;
        int maxPagesToFetch = 20000;
//        int politenessDelay = 500;
  
        CrawlConfig config = new CrawlConfig();
        config.setMaxDepthOfCrawling(maxDepthOfCrawling);
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxPagesToFetch(maxPagesToFetch);
        config.setIncludeBinaryContentInCrawling(true);
//        config.setPolitenessDelay(politenessDelay);

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
    	controller.addSeed("https://www.wsj.com/");

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(MyCrawler.class, numberOfCrawlers);
        
        List<Object> crawlersLocalData = controller.getCrawlersLocalData();
        logger.info("local data: {}", crawlersLocalData.get(0)); // local data
        
        
        PrintWriter writer = new PrintWriter("CrawlReport_Wall_Street_Journal.txt", "UTF-8");
        writer.println("Name: Cheng-I Chou");
        writer.println("UCS ID: 7765631809");
        writer.println("News site crawled: wsj.com");
        writer.println("");
        writer.println("Fetch Statistics");
        writer.println("================");
        writer.println("# fetches attempted: " +  ((int[])((Object[])(crawlersLocalData.get(0)))[0])[0]);
        writer.println("# fetches succeeded: " + ((int[])((Object[])(crawlersLocalData.get(0)))[0])[1]);
        writer.println("# fetches failed or aborted :" + ((int[])((Object[])(crawlersLocalData.get(0)))[0])[2]);
        writer.println("");
        writer.println("Outgoing URLs:");
        writer.println("================");
        writer.println("Total URLs extracted: " + ((int[])((Object[])(crawlersLocalData.get(0)))[1])[0]);
        writer.println("# unique URLs extracted: " + ((int[])((Object[])(crawlersLocalData.get(0)))[1])[1]);
        writer.println("# unique URLs within News Site :" + ((int[])((Object[])(crawlersLocalData.get(0)))[1])[2]);
        writer.println("# unique URLs outside News Site :" + ((int[])((Object[])(crawlersLocalData.get(0)))[1])[3]);
        writer.println("");
        
        writer.println("Status Codes:");
        writer.println("================");
        if(((Object[])(crawlersLocalData.get(0)))[2] instanceof HashMap) {
        	for(HashMap.Entry<Integer, Integer> item : ((HashMap<Integer, Integer>)((Object[])(crawlersLocalData.get(0)))[2]).entrySet()) {
        		writer.print(item.getKey() + " ");
        		String discription;
            	switch (item.getKey()) {
                case 200:
                	discription = "OK";
                    break;
                case 301:  
                	discription = "Moved Permanently";
                    break;
                case 302:  
                	discription = "Found";
                    break;
                case 400:  
                	discription = "null";
                    break;
                case 404:  
                	discription = "Not Found";
                    break;
                case 503:  
                	discription = "Service Unavailable";
                    break;  
                case 504:  
                	discription = "Gateway Timeout";
                    break;
                    
                default: 
                	discription = "Others";
            	}
        	
        		writer.println(discription + ": " + item.getValue());  
        	}
        }    	
        
        writer.println("");
        
        writer.println("File Sizes: ");
        writer.println("================");
        writer.println("< 1KB: " + ((int[])((Object[])(crawlersLocalData.get(0)))[3])[0]);
        writer.println("1KB ~ <10KB: " + ((int[])((Object[])(crawlersLocalData.get(0)))[3])[1]);
        writer.println("10KB ~ <100KB :" + ((int[])((Object[])(crawlersLocalData.get(0)))[3])[2]);
        writer.println("100KB ~ <1MB: " + ((int[])((Object[])(crawlersLocalData.get(0)))[3])[3]);
        writer.println(">= 1MB :" + ((int[])((Object[])(crawlersLocalData.get(0)))[3])[4]);
        writer.println("");
        
        writer.println("Content Types:");
        writer.println("================");
        if(((Object[])(crawlersLocalData.get(0)))[4] instanceof HashMap) {
        	HashMap<String, Integer> contentType = (HashMap<String, Integer>)(((Object[])(crawlersLocalData.get(0)))[4]);
        	for(HashMap.Entry<String, Integer> item : contentType.entrySet())
        		writer.println(item.getKey() + ": " + item.getValue());
        }
        
        writer.close();
        
    }
}