package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;

import org.xml.sax.SAXException;

public class HtmlParse {

   public static void main(final String[] args) throws IOException,SAXException, TikaException {
	   
	  File dir = new File("/Users/ChrisChou/Sites/solr-7.1.0/WSJ/WSJ");
//	  File dir = new File("/Users/ChrisChou/Sites/WSJ");
	  PrintWriter writer = new PrintWriter(new FileWriter("big3.txt"));
	  int count = 1;
	  for (File file : dir.listFiles()) { 
	  
	      //detecting the file type
		  FileInputStream inputstream = new FileInputStream(file);
	      BodyContentHandler handler = new BodyContentHandler(-1);
	      Metadata metadata = new Metadata();
	      ParseContext pcontext = new ParseContext();
	      
	      //Html parser 
	      HtmlParser htmlparser = new HtmlParser();
	      htmlparser.parse(inputstream, handler, metadata,pcontext);
	      
	      writer.print(handler.toString());
	      
	      String title =  metadata.get("title");
	      System.out.println(title + " " + count++);

	  }
	  writer.close();
	  
	  System.out.println("done");
	  
	  

      	
   }
}