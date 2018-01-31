import java.io.IOException;
import java.util.StringTokenizer;
import java.util.*;
import java.lang.StringBuffer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class InvertedIndexJob {

  public static class TokenizerMapper extends Mapper<Object, Text, Text, Text>{

    private Text word = new Text();
    private Text docID = new Text();

    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
      String line = value.toString();
      StringTokenizer tokenizer = new StringTokenizer(line);
    
      docID.set(tokenizer.nextToken());
      while (tokenizer.hasMoreTokens()) {
        word.set(tokenizer.nextToken());
        context.write(word, docID);
      }
    }
  }

  public static class DocFreqReducer extends Reducer<Text,Text,Text,Text> {

    public void reduce(Text key, Iterable<Text> values, Context context)
                      throws IOException, InterruptedException {

      Map<String, Integer> docFreq = new HashMap<String, Integer>();

      for (Text value : values) {
        String docID = value.toString();
        Integer oldValue = docFreq.get(docID);
        if (oldValue == null) {
          docFreq.put(docID, 1);
        }
        else {
          docFreq.put(docID, oldValue + 1);
        }
      }

      StringBuffer docList = new StringBuffer("");
      for(Map.Entry<String,Integer> entry : docFreq.entrySet()) {
        docList.append(entry.getKey() + ":" + entry.getValue() + "\t");
      }
      context.write(key, new Text(docList.toString()));
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Inverted Index");
    job.setJarByClass(InvertedIndexJob.class);

    job.setMapperClass(TokenizerMapper.class);
    // job.setCombinerClass(DocFreqReducer.class);
    job.setReducerClass(DocFreqReducer.class);
    
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}