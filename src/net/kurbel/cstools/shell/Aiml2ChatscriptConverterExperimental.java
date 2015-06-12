package net.kurbel.cstools.shell;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.kurbel.cstools.nodes.AimlNode;
import net.kurbel.cstools.parser.ConceptParser;
import net.kurbel.cstools.parser.Concept;
import net.kurbel.cstools.parser.Stopwords;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.de.GermanStemmer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiTermQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.vectorhighlight.FieldQuery;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.tartarus.snowball.ext.German2Stemmer;
import org.tartarus.snowball.ext.PorterStemmer;



public class Aiml2ChatscriptConverterExperimental {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ParseException 
	 * @throws InvalidTokenOffsetsException 
	 */
	public static void main(String[] args) throws IOException, ParseException, InvalidTokenOffsetsException {
			
		String stopwordFileString = "aimlfiles/stopwoerter.txt";
		File stopwordFile= new File(stopwordFileString);
		Stopwords stopwords = new Stopwords();
		stopwords.parse(stopwordFile);
		
		// Ein Standard Analysierer, ohne Stopwörter (EMPTY_SET)
		//GermanAnalyzer analyzer = new GermanAnalyzer(Version.LUCENE_45, stopwords.getCharArraySet());
		KeywordAnalyzer analyzer = new KeywordAnalyzer();
		
		// Der Index wird im RAM abgelegt
		Directory index = new RAMDirectory();

		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_45, analyzer);

		System.out.println("Parse die Concepts-Datei...");
		File conceptFile = new File("aimlfiles/GCSN.txt");
		List<Concept> concepts = ConceptParser.parse(conceptFile);

		System.out.println("Erstelle Lucene Index...");
		IndexWriter w = new IndexWriter(index, config);
		for (Concept concept : concepts) {
			addConcept(w, concept.getName(), concept.getContents());
		}
		w.close();
		
		concepts = null;
		
		System.out.println("... alle Vorbereitungen beendet.");
		QueryParser parser = new QueryParser(Version.LUCENE_45, "text", analyzer);
		parser.setAllowLeadingWildcard(false);
		

		
		
		String querystr = args.length > 0 ? args[0] : "Bremer*";
		
		int hitsPerPage = 10;
		IndexReader reader = IndexReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);		
		
		File aimlFile = new File("aimlfiles/Agronom_bis_Astromom.aiml");
		org.jsoup.nodes.Document doc = Jsoup.parse(aimlFile, "utf-8");
		
		for (Element e : doc.select("category")) {
			
			System.out.println("Category: " + e.text());
			
			AimlNode node = new AimlNode();
			
			Element pattern = e.select("pattern").first();
			
			node.setCategory(pattern.text());
			
			for (Element reply : e.select("li")) {

				node.addRandom(reply.text());

				String[] snippetStrings = reply.text().split("[ :,!.\"\\?]");
				
				for (String snippet : snippetStrings) {
					if (snippet.length() <= 3)
						continue;
					//System.out.println(node.toString());
					
					/*German2Stemmer stemmer = new German2Stemmer();
					
					stemmer.setCurrent(snippet);
					stemmer.stem();
					String stemmed = stemmer.getCurrent();*/
					
					String queryString = snippet;// + " OR " + stemmed + "~0.8";
					System.out.println("Searching " + queryString);
					Query query = parser.parse(queryString  );
					
					//QueryScorer scorer = new QueryScorer(q);
					
					if (query != null) {
						TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, false);
						
						TopFieldCollector tfcollector = TopFieldCollector.create(Sort.INDEXORDER, 10, false, false, false, false);
						
						 searcher.search(query, tfcollector);

						 ScoreDoc[] hits = searcher.search(query, 10).scoreDocs;
						//ScoreDoc[] hits =tfcollector.topDocs().scoreDocs;
						

						System.out.println("Found " + hits.length + " hits.");
						for(int i=0;i<hits.length;++i) {
							int docId = hits[i].doc;
							float foo = hits[i].score;
							
							Document d = searcher.doc(docId);

							List<Term> hitTerms = new LinkedList<Term>();
							List<Term> rest = new LinkedList<Term>();
							GetHitTerms(query, searcher, docId, hitTerms, rest);
							//System.out.println(d.toString());
							for (Term t : hitTerms) {
								System.out.println(t.text());
							}
							
							
							//Explanation explanation = searcher.explain(q, docId);
							//System.out.println(explanation.toHtml());
							
							System.out.println((i + 1) + ". ["+ foo + "] in " + d.get("concept") + "\t" +  join(",\n", d.getValues("text")));
						}
					}					
				}			
			}
		}						
    }
	
	static void GetHitTerms(Query query,IndexSearcher searcher,int docId,List<Term> hitTerms,List<Term>rest) throws IOException
	{
	    if (query instanceof TermQuery)
	    {
	        if (searcher.explain(query, docId).isMatch() == true) 
	            hitTerms.add(((TermQuery)query).getTerm());
	        else
	            rest.add(((TermQuery)query).getTerm());
	        return;
	    }

	    if (query instanceof BooleanQuery)
	    {
	        BooleanClause[] clauses = ((BooleanQuery)query ).getClauses();
	        if (clauses == null) return;

	        for (BooleanClause bc : clauses)
	        {
	            GetHitTerms(bc.getQuery(), searcher, docId,hitTerms,rest);
	        }
	        return;
	    }

	    if (query instanceof MultiTermQuery)
	    {
	        if (!(query instanceof FuzzyQuery)) //FuzzQuery doesn't support SetRewriteMethod
	            ((MultiTermQuery)query ).setRewriteMethod(MultiTermQuery.SCORING_BOOLEAN_QUERY_REWRITE);

	        GetHitTerms(query.rewrite(searcher.getIndexReader()), searcher, docId,hitTerms,rest);
	    }
	}
	
	private static void addConcept(IndexWriter w, String concept, Iterable<String> text) throws IOException
	{
		  Document doc = new org.apache.lucene.document.Document();
		  doc.add(new TextField("concept", concept, Field.Store.YES));
		//  doc.add(new TextField("text", join(" ", text), Field.Store.YES));
		  for (String t : text)
		  {
			  doc.add(new TextField("text", t, Field.Store.YES));
		  }
		  w.addDocument(doc);
	}
		 

	private static String join (String delim, Iterable<String> data) {
		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < data.; i++) {
//			sb.append(data[i]);
//			if (i >= data.length-1) {break;}
//			sb.append(delim);
//		}
		for (String dat : data)
		{
			sb.append(dat);
			sb.append(delim);
		}
		return sb.toString();
	}
	 
	private static String join (String delim, String[] data) {
		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < data.; i++) {
//			sb.append(data[i]);
//			if (i >= data.length-1) {break;}
//			sb.append(delim);
//		}
		for (String dat : data)
		{
			sb.append(dat);
			sb.append(delim);
		}
		return sb.toString();
	}
	 
	public static void convertToChatscript(AimlNode node) 
	{
		
	}

}
