package eu.europeana.api.commons.nosql.dao;


import java.io.Serializable;

import org.mongodb.morphia.dao.DAO;

import eu.europeana.api.commons.nosql.entity.NoSqlEntity;

/**
 * Generic DAO service layer. Used in combination with a DAO instance for every type
 * of object, although some methods are generic and can be used for every entity.
 * 
 * @author Willem-Jan Boogerd, Sven Schalrb
 */
public interface NosqlDao<E extends NoSqlEntity, T extends Serializable> extends DAO<E, T> {
	/**
	 * Only for internal (test) usage, clears a table...
	 */
	void deleteAll();

}
