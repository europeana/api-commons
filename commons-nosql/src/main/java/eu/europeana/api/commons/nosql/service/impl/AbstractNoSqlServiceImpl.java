package eu.europeana.api.commons.nosql.service.impl;
import java.io.Serializable;

import org.mongodb.morphia.Key;

import eu.europeana.api.commons.nosql.dao.NosqlDao;
import eu.europeana.api.commons.nosql.entity.NoSqlEntity;
import eu.europeana.api.commons.nosql.service.AbstractNoSqlService;

/**
 * Implementation od the {@link AbstractNoSqlService}
 *
 * @param <E> The type of the concrete NoSql Entity
 * @param <T> The type of the serializable class for id attribute
 */
public abstract class AbstractNoSqlServiceImpl<E extends NoSqlEntity, T extends Serializable> implements AbstractNoSqlService<E, T> {

	private NosqlDao<E,T> dao;

	/*
	 * (non-Javadoc)
	 * @see eu.europeana.api.commons.nosql.service.AbstractNoSqlService#remove(java.io.Serializable)
	 */
	@Override
	public void remove(T id) {
		dao.deleteById(id);
	}

	/*
	 * (non-Javadoc)
	 * @see eu.europeana.api.commons.nosql.service.AbstractNoSqlService#findByID(java.io.Serializable)
	 */
	@Override
	public E findByID(T id) {
		return dao.get(id);
	}

	/*
	 * (non-Javadoc)
	 * @see eu.europeana.api.commons.nosql.service.AbstractNoSqlService#findAll()
	 */
	@Override
	public Iterable<E> findAll() {
		return dao.find().asList();
	}

	/*
	 * (non-Javadoc)
	 * @see eu.europeana.api.commons.nosql.service.AbstractNoSqlService#store(eu.europeana.api.commons.nosql.entity.NoSqlEntity)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E store(E object) {
		Key<E> key = dao.save(object);
		return dao.get((T) key.getId());
	}

	/*
	 * (non-Javadoc)
	 * @see eu.europeana.api.commons.nosql.service.AbstractNoSqlService#exists(java.io.Serializable)
	 */
	@Override
	public boolean exists(T id) {
		return dao.exists("_id", id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see eu.europeana.api.commons.nosql.service.AbstractNoSqlService#count()
	 */
	@Override
	public long count() {
		return dao.count();
	}

	/**
	 * Used by Bean configuration to inject Entity based DAO.
	 * 
	 * @param dao
	 *            DAO object with entity based generic set
	 */
	public final void setDao(NosqlDao<E,T> dao) {
		this.dao = dao;
	}

	/**
	 * Getter for DAO, only available for internal usage.
	 * 
	 * @return Generic DAO class
	 */
	protected NosqlDao<E,T> getDao() {
		return dao;
	}
}
