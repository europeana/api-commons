package eu.europeana.api.commons.definitions.search.enrich;

/**
 * Represents a query for enriching text data based on specific parameters.
 */
public class EnrichQuery {

    private String text;
    private String lang;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
