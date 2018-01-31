package hw4;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.*;
import java.io.*;

public class ExtractLinks {

	public static void main(String[] args) throws Exception{
		
        Map<String, String> fileUrlMap = new HashMap<String, String>();
        Map<String, String> urlFileMap = new HashMap<String, String>();
	
        BufferedReader br = new BufferedReader(new FileReader("/Users/ChrisChou/Sites/solr-7.1.0/WSJ/WSJ_map.csv"));
        String line = "";
        while ((line = br.readLine()) != null) {
            String[] fileUrl = line.split(",");
            fileUrlMap.put(fileUrl[0], fileUrl[1]);
            urlFileMap.put(fileUrl[1], fileUrl[0]);
        }
        br.close();
//		System.out.println(fileUrlMap);
		
		String dirPath = "/Users/ChrisChou/Sites/solr-7.1.0/WSJ/WSJ";
		File dir = new File(dirPath);
		Set<String> edges = new HashSet<String>();
		for(File file: dir.listFiles()){
			Document doc = Jsoup.parse(file, "UTF-8", fileUrlMap.get(file.getName()));
			Elements links = doc.select("a[href]");
						
			for(Element link: links){
				String url = link.attr("abs:href").trim();
				if(urlFileMap.containsKey(url)) {
					edges.add(file.getName() + " " + urlFileMap.get(url));
				}
			}
		}
		
		PrintWriter writer = new PrintWriter(new FileWriter("edgeList.txt")); 
		for(String s: edges){
			writer.println(s);
		}
		writer.close();
		
		 
//	    BufferedWriter writer = new BufferedWriter(new FileWriter("edgeList.txt"));
//		
//		for(String s: edges){
//			writer.write(s);
//			writer.newLine();
//		}
//		writer.flush();
//		writer.close();
		
		
	
	}
}
	
