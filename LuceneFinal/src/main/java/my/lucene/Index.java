package my.lucene;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Index {

		public static void Index(String anaa, String sim) throws IOException {
			
			String PathToIndex = "src/index";
			
			String PathToCranDocs = "src/cran/cran.all.1400";
			
			String PathToStop = "src/stop.txt";
			
			File directory = new File(PathToIndex);
			
			File Stopdirectory = new File(PathToStop);
			
			Scanner stp = new Scanner(Stopdirectory);
			
			String row = stp.nextLine();
			
			List<String> stpwrd = new ArrayList<String>();
			
			while(stp.hasNext()){
				
				row = stp.nextLine();
				
			}

			String[] delFiles;    
	           if(directory.isDirectory()){
	        	   delFiles = directory.list();
	               for (int i=0; i<delFiles.length; i++) {
	                   File my = new File(directory, delFiles[i]); 
	                   my.delete();
	               }
	            }
			
	        List<String> stp_wrd_lst = Arrays.asList("a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "could", "did", "do", "does", "doing", "down", "during", "each", "few", "for", "from", "further", "had", "has", "have", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "it", "it's", "its", "itself", "let's", "me", "more", "most", "my", "myself", "nor", "of", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "she", "she'd", "she'll", "she's", "should", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "we", "we'd", "we'll", "we're", "we've", "were", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "would", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves");
	           
//			List<String> stopWordList = Arrays.asList();
			
			CharArraySet stopWordSet = new CharArraySet( stp_wrd_lst, true);
			
			Analyzer ana = null;
			
			if(anaa == "EnglishAnalyzer") {
				ana = new EnglishAnalyzer(stopWordSet);
			}
			else if(anaa == "StandardAnalyzer") {
				ana = new StandardAnalyzer(stopWordSet);
			}
			
			IndexWriterConfig iwc = new IndexWriterConfig(ana);
			
			iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
			
			if(sim == "ClassicSimilarity") {
				iwc.setSimilarity(new ClassicSimilarity());
			}
			else if(sim == "BM25Similarity") {
				iwc.setSimilarity(new BM25Similarity());
			}
			else if(sim == "LMDirichletSimilarity") {
				iwc.setSimilarity(new LMDirichletSimilarity());
			}
			
			Directory drc = FSDirectory.open(Paths.get(PathToIndex));
			
			IndexWriter iw = new IndexWriter(drc, iwc);
			
			File fobj = new File(PathToCranDocs);
			
			Scanner sread = new Scanner(fobj);
			
			sread.useDelimiter("\n");
			
			int index_file_count = 0;
			
			String line = sread.nextLine();
			
			Map<Integer, Map<String,String>> docMap=new HashMap<Integer, Map<String,String>>();
			
			String token;
			
			Map<String,String> dataMap =  new HashMap< String,String>();
			
			while(line != null) {
				
				String title ="";
				String author ="";
				String pub = "";
				String words ="";
				String token1 = "";
				
				if(line.contains(".I")) {
					dataMap=new HashMap<String, String>();
					index_file_count++;
					dataMap.put("index", Integer.toString(index_file_count));
					token1 = ".T";
					line = sread.nextLine();
				}
				
				if(line.contains(".T")) {
					line = sread.nextLine();
					if (!line.contains(".A"))
					{
						while(token1 == ".T") {
							title = title + line+ " ";
							line = sread.nextLine();
							if(line.contains(".A")) {
								token1 = ".A";
							}
						}
					}
					token1 = ".A";
					dataMap.put("title", title);
				}
				
				if(line.contains(".A")) {
					line = sread.nextLine();
					if (!line.contains(".B"))
					{
						while(token1 == ".A") {
					
						author = author + line+ " ";
						line = sread.nextLine();
							if(line.contains(".B")) {
								token1 = ".B";
							}
						}
					}
					token1 = ".B";
					dataMap.put("author", author);
				}
				
				if(line.contains(".B")) {
					line = sread.nextLine();
					if (!line.contains(".W"))
					{
						while(token1 == ".B") {
							pub = pub + line + " ";
							line = sread.nextLine();
							if(line.contains(".W")) {
								token1 = ".W";
							}
						}
					}
					token1 = ".W";
					dataMap.put("published", pub);
				}
				
				if(line.contains(".W")) {
					line = sread.nextLine();
					if (!line.contains(".I"))
					{
						while(token1 == ".W") {
							words = words + line + " ";
							if(!sread.hasNext()) {
								line=null;
								token1="";
							}
							else {
							line = sread.nextLine();
								if(line.contains(".I")) {
									token1 = ".I";
								}
							}
						}
					}
					token1 = ".I";
					dataMap.put("content", words);
				}
				
				docMap.put(index_file_count,dataMap);
				
			}

			for (Map.Entry<Integer, Map<String,String>> entry : docMap.entrySet()) {
				
				//System.out.println(entry.getValue().get("title"));
				Document doc = new Document();
				doc.add(new TextField("index", entry.getKey()+"", Field.Store.YES));
				doc.add(new TextField("title", entry.getValue().get("title"), Field.Store.YES));
				doc.add(new TextField("author", entry.getValue().get("author"), Field.Store.YES));
				doc.add(new TextField("published", entry.getValue().get("published"), Field.Store.YES));
				doc.add(new TextField("content", entry.getValue().get("content"), Field.Store.YES));
				//System.out.println(doc);
				iw.addDocument(doc);
				
			}
			
			iw.close();
			drc.close();
		}
}


