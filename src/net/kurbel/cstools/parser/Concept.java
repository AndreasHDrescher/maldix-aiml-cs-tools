package net.kurbel.cstools.parser;

import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.Media;

public class Concept {
	
	private String name;
	
	private List<String> contents;
	
	private Concept parentConcept;
	
	public Concept() {
		this.contents = new ArrayList<String>();
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setParentConcept(Concept parentConcept) {
		this.parentConcept = parentConcept;
	}
	
	public Concept getParentConcept() {
		return this.parentConcept;
	}
	
	public void setContents(List<String> contents) {
		this.contents = contents;
	}
	
	public List<String> getContents() {
		return this.contents;
	}
	
	public void addContent(String[] contents) {
		for (String content : contents) {
			addContent(content);
		}
	}
	
	public void addContent(String content) {
		this.getContents().add(content);
	}
	
	public String getFullContent() {
		StringBuilder sb = new StringBuilder();
		for (String content : contents) {
			sb.append(content);
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
