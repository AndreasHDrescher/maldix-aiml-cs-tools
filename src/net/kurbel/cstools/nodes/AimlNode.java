package net.kurbel.cstools.nodes;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.StringUtil;

public class AimlNode {

	private String category;
	
	private List<String> randoms;
	
	public AimlNode()
	{
		setRandoms(new ArrayList<String>());
	}

	public List<String> getRandoms() {
		return randoms;
	}

	public void setRandoms(List<String> randoms) {
		this.randoms = randoms;
	}

	public String getCategory() {
		return category;
	}

	public String getTopic() {
		if (!StringUtil.isBlank(this.getCategory())) {
			String first = this.getCategory().substring(0, 1);
			String rest = this.getCategory().substring(1);
			return first + rest.toLowerCase();
		}
		return "";
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public void addRandom(String random){
		this.getRandoms().add(random);
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("CATEGORY = ");
		sb.append(this.getCategory());
		sb.append("\n");
		for (String s : this.getRandoms()) {
			sb.append("\t");
			sb.append(s);
			sb.append("\n");
		}
		return sb.toString();
	}
	
}
