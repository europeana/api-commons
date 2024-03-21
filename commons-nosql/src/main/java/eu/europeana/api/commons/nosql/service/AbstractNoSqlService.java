package eu.europeana.api.commons.nosql.service;


import java.io.Serializable;
import java.util.List;
import dev.morphia.query.QueryResults;
import eu.europeana.api.commons.nosql.entity.NoSqlEntity;

/**
 * @author Willem-Jan Boogerd, Sven Schlarb 
 */
public interface AbstractNoSqlService<E extends NoSqlEntity, T extends Serializable> {

	/**
	 * @param object - the object to be stored in the database
	 * @return the persisted object
	 */
	E store(E object);

	/**
	 * 
	 * 
	 * @param id - the database id of the object to be deleted
	 */
	void remove(final T id);

	/*
	 * FINDERS
	 */

	/**
	 * Returns a count of all records
	 * 
	 * @return the number of total records
	 */
	long count();

	/**
	 * Find all elements for this service.
	 * 
	 * @return All entities for the defined entity type
	 */
	 Iterable<E> findAll();

	/**
	 * 
	 * 
	 * @param id the database ID for the object persisted in the database
	 * @return the database object
	 */
	E findByID(final T id);

	/**
	 * Checks if a entity with the given ID exists.
	 * 
	 * @param id - the database ID
	 * @return true if the object with the given id is found in database
	 */
	boolean exists(final T id);
	
	/**
	 * Extract the List of elements from query results 
	 * @return list of entities of type T
	 */
	public List<T> asList(QueryResults<T> queryResults);

}
