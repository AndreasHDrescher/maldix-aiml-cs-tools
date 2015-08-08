package net.kurbel.cstools.parser;

/**
 * Created by Danny on 08.08.2015.
 */
public class ConceptContent {

    private String parsedContent;

    /**
     * phrase is true if the content is enclosed in quotes
     * or combined using underscores _
     */
    private boolean isPhrase = false;

    public ConceptContent(String parsedContent, boolean isPhrase) {
        this.parsedContent = parsedContent;
        this.isPhrase = isPhrase;
    }

    public boolean isValidContent() {
        return this.getParsedContent()!=null && !"".equals(this.getParsedContent().trim());
    }
    public boolean isPhrase() {
        return this.isPhrase;
    }

    public String getParsedContent() {
        return this.parsedContent;
    }

    @Override
    public String toString() {
        return this.getParsedContent();
    }
}
