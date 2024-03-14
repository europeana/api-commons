package eu.europeana.api.commons.nosql.dao.impl;

import java.io.Serializable;
import com.mongodb.ReadPreference;
import dev.morphia.Datastore;
import dev.morphia.dao.BasicDAO;
import eu.europeana.api.commons.nosql.dao.NosqlDao;
import eu.europeana.api.commons.nosql.entity.NoSqlEntity;
/**
 * Implementation of a NosqlDao
 *
 * @param <E> the entity class
 * @param <T> the class of the entity Id
 */
public class NosqlDaoImpl<E extends NoSqlEntity, T extends Serializable> extends BasicDAO<E, T> implements NosqlDao<E, T> {

	private Class<E> clazz;

	/**
	 * Default constructor
	 * @param datastore The datastore to connect t o
	 * @param clazz The implementation class
	 */
	public NosqlDaoImpl(Datastore datastore, Class<E> clazz) {
		super(clazz, datastore);
		datastore.getDB().setReadPreference(ReadPreference.secondaryPreferred());
		this.clazz = clazz;
	}

	/*
	 * (non-Javadoc)
	 * @see eu.europeana.api.commons.nosql.dao.NosqlDao#deleteAll()
	 */
	@Override
	public void deleteAll() {
		try {
			delete(clazz.getDeclaredConstructor().newInstance());
		} catch (Exception e) {
		}
	}

}
