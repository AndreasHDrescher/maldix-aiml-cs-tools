package net.kurbel.cstools.nodes;

import java.util.ArrayList;
import java.util.List;

public class CsElementNode {

	private String topic;
	private String category;
	
	private List<CsMatchResponderNode> responderNodes;
	
	public CsElementNode() {
		this.responderNodes = new ArrayList<CsMatchResponderNode>();
	}
		
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getCategory() {
		return this.category;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public String getTopic() {
		return this.topic;
	}
	
	public void setResponderNodes(List<CsMatchResponderNode> rules) {
		this.responderNodes = rules;
	}
	
	public List<CsMatchResponderNode> getResponderNodes() {
		return this.responderNodes;
	}
	
}
