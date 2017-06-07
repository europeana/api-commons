package eu.europeana.api.commons.definitions.model;

import java.util.Date;

import eu.europeana.api.commons.definitions.model.Entity;

public interface Concept extends eu.europeana.corelib.definitions.edm.entity.Concept, Entity {

	public void setDefinition(String definition);

	public String getDefinition();

	void setTimestamp(Date timestamp);

	Date getTimestamp();
	
}
