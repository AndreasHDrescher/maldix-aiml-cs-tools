package net.kurbel.cstools.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.kurbel.cstools.nodes.AimlNode;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.highlight.QueryScorer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.tartarus.snowball.ext.German2Stemmer;

public class AimlParser {

	public static List<AimlNode> parse(File aimlFile) throws IOException {
		
		List<AimlNode> nodes = new ArrayList<AimlNode>();
		
		org.jsoup.nodes.Document doc = Jsoup.parse(aimlFile, "UTF-8");
		
		for (Element e : doc.select("category")) {
			
			AimlNode node = new AimlNode();
			
			Element pattern = e.select("pattern").first();
			
			node.setCategory(pattern.text());
			
			for (Element reply : e.select("li")) {				
				node.addRandom(reply.text());			
			}
			
			nodes.add(node);
		}
		
		return nodes;
	}
	
}
