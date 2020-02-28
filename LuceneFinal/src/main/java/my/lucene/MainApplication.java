package my.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.queryparser.classic.ParseException;

public class MainApplication {

	public static void main(String[] args) throws IOException, ParseException {

		List<String> analist = Arrays.asList("EnglishAnalyzer", "StandardAnalyzer");
		
		List<String> simlist = Arrays.asList("ClassicSimilarity", "BM25Similarity", "LMDirichletSimilarity");
		
		System.out.println("\nSearching...................!!");
		
		for (String ana : analist) {
			
			for (String sim : simlist) {
				
				Index.Index(ana,sim);
				
				String path = Search.Search(ana, sim);
				
				System.out.println("\nPath To Result files using Analyser = " + ana + " Similarity = " + sim + " is " + path);
				
				System.out.println(" ");
				
			}
			
		}
		
		System.out.println("Done Searching...................:)\n");
		
		System.out.println("For calculating MAP use Trec Eval by further instructions.....\n");
		
		System.out.println("The best result is achieved by using Analyser = EnglishAnalyser and Similarity = BM25Similarity and the value of map achieved is 0.4087\n");
		
	}
}
