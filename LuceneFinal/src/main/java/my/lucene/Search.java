package my.lucene;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Search {

	public static String Search(String anaa, String sim) throws IOException, ParseException {
		
		String PathToIndex = "src/index";
		
		String PathToQry = "src/cran/cran.qry";
		
		String PathToStop = "src/stop.txt";
		
		File Stopdirectory = new File(PathToStop);
		
		Scanner stp = new Scanner(Stopdirectory);
		
		String row = stp.nextLine();
		
		List<String> stpwrd = new ArrayList<String>();
		
		while(stp.hasNext()){
			
			row = stp.nextLine();
			
		}
		
		List<String> words_stop = Arrays.asList("a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "could", "did", "do", "does", "doing", "down", "during", "each", "few", "for", "from", "further", "had", "has", "have", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "it", "it's", "its", "itself", "let's", "me", "more", "most", "my", "myself", "nor", "of", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "she", "she'd", "she'll", "she's", "should", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "we", "we'd", "we'll", "we're", "we've", "were", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "would", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves");
		
		CharArraySet stopWordSet = new CharArraySet(words_stop, true);
		
		Analyzer ana = null;
		
		if(anaa == "EnglishAnalyzer") {
			ana = new EnglishAnalyzer(stopWordSet);
		}
		else if(anaa == "StandardAnalyzer") {
			ana = new StandardAnalyzer(stopWordSet);
		}
		
		Directory drc = FSDirectory.open(Paths.get(PathToIndex));
		
		IndexReader ir = DirectoryReader.open(drc);
		
		IndexSearcher is = new IndexSearcher(ir);
		
		if(sim == "ClassicSimilarity") {
			is.setSimilarity(new ClassicSimilarity());
		}
		else if(sim == "BM25Similarity") {
			is.setSimilarity(new BM25Similarity());
		}
		else if(sim == "LMDirichletSimilarity") {
			is.setSimilarity(new LMDirichletSimilarity());
		}
		
		File fobj = new File(PathToQry);
		
		Scanner sread = new Scanner(fobj);
		
		Map<Integer, Map<String,String>> docMap=new HashMap<Integer, Map<String,String>>();
		
		Map<String,String> dataMap =  new HashMap< String,String>();
		
		String line = sread.nextLine();
		
		int index_file_count = 0;
		
		while(line != null) {
			
			String token1 = "";
			String qry = "";
			
			if(line.contains(".I")) {
				dataMap=new HashMap<String, String>();
				index_file_count++;
				dataMap.put("ID", Integer.toString(index_file_count));
				token1 = ".W";
				line = sread.nextLine();
			}
			
			if(line.contains(".W")) {
				line = sread.nextLine();
				if (!line.contains(".I"))
				{
					while(token1 == ".W") {
						qry = qry + line + " ";
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
				dataMap.put("QRY", qry);
			}
			
			docMap.put(index_file_count, dataMap);
		}
		
		MultiFieldQueryParser qp = new MultiFieldQueryParser(new String[] {"title", "published", "author", "content"}, ana);
		
		ArrayList<String> vars = new ArrayList<String>();
		
		for (Map.Entry<Integer, Map<String,String>> entry : docMap.entrySet()) {
			int ind = entry.getKey();
			String qry_val = entry.getValue().get("QRY");
			qry_val = qry_val.replaceAll("[^a-zA-Z0-9\\s+]", "");
			org.apache.lucene.search.Query qry_val_parse = qp.parse(qry_val);
			ScoreDoc[] hits = is.search(qry_val_parse, 1000).scoreDocs;
            for (int i = 0; i < hits.length; i++) {
                Document hitDoc = is.doc(hits[i].doc);
                int rank = i+1;
                double sc = hits[i].score;
                if (sc >0){
                  vars.add(ind + " Q0 " + hitDoc.get("index") + " "+ rank + " "+ sc  +" uncosidered \n");
                }
              }
		}
		
		String path = "src/" +anaa+"_"+sim+".out" ;
		
		FileWriter myWriter = new FileWriter(path);
		
		for (String i : vars) {
			
			myWriter.write(i);
			
		}
		
		myWriter.close();
		ir.close();
		drc.close();
		
		return path;
		
	}
	
}
