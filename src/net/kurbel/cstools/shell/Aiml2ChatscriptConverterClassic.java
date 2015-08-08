package net.kurbel.cstools.shell;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import net.kurbel.cstools.converter.ChatScriptNode2String;
import net.kurbel.cstools.nodes.AimlNode;
import net.kurbel.cstools.nodes.CsElementNode;
import net.kurbel.cstools.nodes.CsMatchResponderNode;
import net.kurbel.cstools.parser.*;

public class Aiml2ChatscriptConverterClassic {

	public static void main(String[] args) {

		boolean debugMode = false;
		if (args.length > 0) {
			String debug = args[0];
			if ("-debug".equals(debug)) {
				debugMode = true;
			}
		}
		
		String stopwordsFileString = "aimlfiles/stopwoerter.txt";
		String conceptFileString = "aimlfiles/GCSN.txt";
		//	String aimlFileString = "aimlfiles/Agronom_bis_Astromom.aiml";
		String aimlFileString = "aimlfiles/MaldixIunterLINKS.aiml";

		String conceptFileOutString = "aimlfiles/output/MaldixIunterLINKS.top";

		File stopwordsFile = new File(stopwordsFileString);

		Stopwords stopwords = new Stopwords();
		try {

			stopwords.parse(stopwordsFile);	
		} catch (Exception e) {
			System.out.println("Konnte Stopwort Datei nicht laden.");
			e.printStackTrace();
		}

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


		try {
			File dir = new File("./aimlfiles");
			File [] files = dir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.endsWith(".aiml");
				}
			});

			for (File aimlFile : files) {

				System.out.println("Parse AIML-Datei... " + aimlFile.getName());
				List<AimlNode> aimlNodes = null;
				try {
					aimlNodes = AimlParser.parse(aimlFile);
				}
				catch (IOException e) {
					System.out.println("Konnte AIML Datei nicht laden.");
					e.printStackTrace();
					System.exit(0);
				}

				BufferedWriter chatscriptWriter = null;
				try {
					//File chatscriptFileOut = new File(conceptFileOutString);
					conceptFileOutString = "aimlfiles/output/" + aimlFile.getName().substring(0, aimlFile.getName().lastIndexOf('.')) + ".top";
					chatscriptWriter = new BufferedWriter
							(new OutputStreamWriter(new FileOutputStream(conceptFileOutString),"UTF-8"));
				}
				catch (Exception x) {
					x.printStackTrace();
					System.exit(0);
				}

				for (AimlNode node : aimlNodes) {
					String aliceCategoryString = node.getCategory();
					CsElementNode chatScriptNode = new CsElementNode();
					chatScriptNode.setCategory(aliceCategoryString);
					chatScriptNode.setTopic(node.getTopic());
					for (String random : node.getRandoms() ) {
						CsMatchResponderNode responderNode = new CsMatchResponderNode(node.getTopic());
						responderNode.setSentence(random);
						String searchableRandom = new String(random);
						searchableRandom = searchableRandom.replaceAll("[\\.\\!\\?\\-\\,\\;\\:\\=]", "");

						for(Concept concept : concepts) {
							for (ConceptContent conceptContent : concept.getContents()) {

								if (!conceptContent.isValidContent())
									continue;

								String content = conceptContent.getParsedContent();

								if (stopwords.isStopword(content.toLowerCase()))
									continue;

								String expanded = " " + searchableRandom + " ";
								if (expanded.indexOf(" " + content + " ")>-1) {
									responderNode.addConcept(concept.getName());
									if (debugMode)
										System.out.println(String.format("Match %s with %s in %s", concept, content, random));
								}
							}
						}
						chatScriptNode.getResponderNodes().add(responderNode);
					}
					//System.out.println(chatScriptNode);
					try {
						if (chatscriptWriter!=null)
							chatscriptWriter.append(ChatScriptNode2String.toText(chatScriptNode));
					}
					catch (Exception x) {
						x.printStackTrace();
					}
				}

				try {
					chatscriptWriter.flush();
					chatscriptWriter.close();
				}
				catch (Exception x) {
					x.printStackTrace();
				}
			}}
		catch (Exception x) {
			x.printStackTrace();
		}

	}

}
