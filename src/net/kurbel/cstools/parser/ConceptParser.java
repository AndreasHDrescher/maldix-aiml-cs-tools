package net.kurbel.cstools.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.queries.function.valuesource.IfFunction;

import net.kurbel.cstools.parser.Concept;

public class ConceptParser {

	public static List<Concept> parse(File conceptFile) throws IOException {
		
		List<Concept> concepts = new ArrayList<Concept>();
		
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(conceptFile), "UTF-8"));
		StringBuilder sb = new StringBuilder();
		while (bufReader.ready()) {
			sb.append(bufReader.readLine());
			sb.append(" ");
		}
		bufReader.close();
		
		String alltogether = sb.toString();
		
		Pattern conceptPattern = Pattern.compile("concept: (~[a-z_]+)[^\\(]*?\\((.*?)\\)", Pattern.CASE_INSENSITIVE  | Pattern.DOTALL );
		
		Matcher m = conceptPattern.matcher(alltogether);
		
		Pattern conceptContentPattern = Pattern.compile("\\\"{0,1}([a-zA-Z0-9öäüÖÄÜßéèûôâáààóòâçéèêëîïôûùüÿñæœ'´`\\!\\?\\-\\_\\&\\%\\$\\§\\ ]+)\\\"{0,1}[\\s]*", Pattern.COMMENTS );

		while (m.find()) {
			
			Concept concept = new Concept();
			
			String conceptName = m.group(1);
			concept.setName(conceptName);
			
//			System.out.println("Concept: " + conceptName);
			
			String conceptContent = m.group(2);
					
			Matcher contentMatcher = conceptContentPattern.matcher(conceptContent);
		
			while (contentMatcher.find()) {
				String conceptContentElement = contentMatcher.group(1).trim();
				if(conceptContentElement=="")continue;
				
				//System.out.println(conceptContentElement);
				
				concept.addContent(conceptContentElement.trim().replace("_", " "));
				
				/*
				if (conceptContentElement.startsWith("\"")) {
					concept.addContent(conceptContentElement);
				}
				else {
					concept.addContent(conceptContentElement.trim().split("[\\s\\_\\-]"));
				}*/
			}
			
			concepts.add(concept);
		}
	
		return concepts;
	}

}
