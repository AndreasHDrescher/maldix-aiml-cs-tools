package net.kurbel.cstools.parser;

import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.Media;

public class Concept {
	
	private String name;
	
	private List<ConceptContent> contents;
	
	private Concept parentConcept;
	
	public Concept() {
		this.contents = new ArrayList<ConceptContent>();
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

	public List<ConceptContent> getContents() {
		return this.contents;
	}

	public void addContent(ConceptContent content) {
		this.contents.add(content);
	}

	@Override
	public String toString() {
		return this.name;
	}
}
