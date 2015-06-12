package net.kurbel.cstools.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

public class Stopwords {

	private HashSet<String> stopwords;
	
	public Stopwords() {
		this.stopwords = new HashSet<String>();
	}
	
	public void parse(File stopwordFile) throws IOException {
		BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(stopwordFile), "UTF-8"));
		StringBuilder sb = new StringBuilder();
		while (bufReader.ready()) {
			String line = bufReader.readLine();
			if (!this.stopwords.contains(line)) {
				this.stopwords.add(line);
			}
		}
		bufReader.close();
	}
		
	public boolean isStopword(String word) {
		return this.stopwords.contains(word);
	}
	
	public CharArraySet getCharArraySet() {
		Collection<String> c= this.stopwords;
		boolean ignoreCase = true;
		CharArraySet set = new CharArraySet(Version.LUCENE_45, c, ignoreCase);
		return set;
		
	}
}
