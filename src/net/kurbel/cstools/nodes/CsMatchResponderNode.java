package net.kurbel.cstools.nodes;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.StringUtil;

public class CsMatchResponderNode {

	private String responderType = "u";
	
	private List<String> conceptList;
	
	private String sentence;
	
	private String parentTopic;
	
	public CsMatchResponderNode(String parentTopic)
	{
		this.setConceptList(new ArrayList<String>());
		this.setParentTopic(parentTopic);
	}
	
	
	public void addConcept(String conceptName) {
		if (this.conceptList.contains(conceptName))
			return;
		this.conceptList.add(conceptName);
	}

	public List<String> getConceptList() {
		return conceptList;
	}

	public void setConceptList(List<String> conceptList) {
		this.conceptList = conceptList;
	}

	public String getResponderType() {
		return responderType;
	}

	public void setResponderType(String responderType) {
		this.responderType = responderType;
	}
	
	public String getSentence()	{
		return this.sentence;
	}
	
	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
		
	public void setParentTopic(String parentTopic) {
		this.parentTopic = parentTopic;
	}
	
	public String getParentTopic() {
		return this.parentTopic;
	}
}
