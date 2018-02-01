# Information Retrieval and Search Engine
## HW2
Built a crawler upon open sourcea crawler4j library crawling on 20,000 webpages from Wall Street Journal and generate the statistics of crawled files
* CrawlReport.txt: statistic of pages
* visit.csv: files the crawler successfully downloaded
* fetch.csv: URLs the crawler attempts to fetch 
* urls.csv: URLs that were discovered and processed in some way
* Limit the crawler to only visit HTML, doc, pdf and different image format URLs and record the meta data for those file types

## HW3
Created Inverted-Index using Hadoop on Google Cloud Platform
* Wrote a Map-Reduce job in java to create an Inverted-Index of words occurring in a collection of 3,036 English books

## HW4
Wrote a PHP program to request search result from solr add compared different ranking algorithm
* Indexed the crawled files fetched in HW2
* Inplemented PageRank algorithms as another ranking algorithm using NetworkX library and compared the search results with default tf-idf ranking algorithm.

## HW5
Enhenced the search engine by adding	Implemented Spell Checking, AutoComplete and Snippets features 
* Implement Spell Checkings and AutoComplete in search input field. Spell check is based on crawled files and SpellCorrector PHP library(fixed several bugs) , AutoComplete is using AJAX calls on Solr index
* Implemented Snippet for search result
