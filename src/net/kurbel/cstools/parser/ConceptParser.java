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
		
		Pattern conceptContentPattern = Pattern.compile("([\\p{L}0-9\\'\\\"´`\\!\\?\\-\\_\\&\\%\\$\\§\\ ]+)[\\s]*", Pattern.COMMENTS );
		Pattern enclosedInQuotes = Pattern.compile("\\\"(.+)\\\"");

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
				String parsedContent = conceptContentElement.trim();

                boolean isPhrase = false;
				if (parsedContent.indexOf('_') > -1) {
                    isPhrase = true;
                    parsedContent = parsedContent.replaceAll("_"," ");
				}

                Matcher enclosedMatcher = enclosedInQuotes.matcher(parsedContent);
                if (enclosedMatcher.matches()) {
                    isPhrase = true;
                    parsedContent = enclosedMatcher.group(1);
                }

				concept.addContent(new ConceptContent(parsedContent, isPhrase));
				
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
