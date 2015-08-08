package net.kurbel.cstools.shell;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.kurbel.cstools.nodes.AimlNode;
import net.kurbel.cstools.nodes.CsElementNode;
import net.kurbel.cstools.nodes.CsMatchResponderNode;
import net.kurbel.cstools.parser.AimlParser;
import net.kurbel.cstools.parser.Concept;
import net.kurbel.cstools.parser.ConceptContent;
import net.kurbel.cstools.parser.ConceptParser;

public class Aiml2ChatscriptConverterL2S {

	public static void main(String[] args) {
		String conceptFileString = "aimlfiles/GCSN.txt";
		String aimlFileString = "aimlfiles/Agronom_bis_Astromom.aiml";
		
		File conceptFile = new File(conceptFileString);
		
		System.out.println("Parse Concept-Datei...");
		List<Concept> concepts = null;
		try {
			concepts = ConceptParser.parse(conceptFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			System.out.println("Konnte Concept Datei nicht laden.");
			e.printStackTrace();
			System.exit(0);
		}
		
		System.out.println("Parse AIML-Datei...");
		List<AimlNode> aimlNodes = null;
		try {
			aimlNodes = AimlParser.parse(new File(aimlFileString));
		}
		catch (IOException e) {
			System.out.println("Konnte AIML Datei nicht laden.");
			e.printStackTrace();
			System.exit(0);
		}
		
		for (AimlNode node : aimlNodes) {
			String aliceCategoryString = node.getCategory();
			CsElementNode chatScriptNode = new CsElementNode();
			chatScriptNode.setTopic(aliceCategoryString);
			for (String random : node.getRandoms() ) {
				CsMatchResponderNode responderNode = new CsMatchResponderNode(aliceCategoryString);
				responderNode.setSentence(random);
				for(Concept concept : concepts) {
					for (ConceptContent conceptContent : concept.getContents()) {

						String content = conceptContent.getParsedContent();
						System.out.println("Content " + content);
						System.out.println("RANDOM  " + random);

						String expanded = " " + random + " ";
						if (expanded.indexOf(" " + content + " ")>-1) {
							responderNode.addConcept(concept.getName());
							System.out.println(String.format("Match %s with %s in %s", concept, content, random));
						}
					}
				}
				chatScriptNode.getResponderNodes().add(responderNode);
			}
			System.out.println(chatScriptNode);
		}

	}

}
