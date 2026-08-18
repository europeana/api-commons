package eu.europeana.api.commons.definitions.search.enrich;

import java.util.List;
import java.util.Objects;

/**
 * Represents a request for enriching data based on specified parameters.
 *
 * This class encapsulates the details of an enrichment request, including
 * a type identifier and a list of queries. It provides methods to access
 * and modify these details.
 */
public class EnrichRequest {

    private String type;
    private List<EnrichQuery> query;
    private int rows;

    public EnrichRequest() {
    }

    public EnrichRequest(String type, List<EnrichQuery> query, int rows) {
        this.type = type;
        this.query = query;
        this.rows = rows;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<EnrichQuery> getQuery() {
        return query;
    }

    public void setQuery(List<EnrichQuery> query) {
        this.query = query;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnrichRequest)) return false;
        EnrichRequest that = (EnrichRequest) o;
        return rows == that.rows &&
                Objects.equals(type, that.type) &&
                Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, query, rows);
    }
}