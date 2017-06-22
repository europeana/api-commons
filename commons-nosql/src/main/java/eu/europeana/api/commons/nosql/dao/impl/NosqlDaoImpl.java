/*
 * Copyright 2007-2012 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved 
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 *  
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under 
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of 
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under 
 *  the Licence.
 */

package eu.europeana.api.commons.nosql.dao.impl;

import java.io.Serializable;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

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
		datastore.getDB().slaveOk();
		this.clazz = clazz;
	}

	/*
	 * (non-Javadoc)
	 * @see eu.europeana.api.commons.nosql.dao.NosqlDao#deleteAll()
	 */
	@Override
	public void deleteAll() {
		try {
			delete(clazz.newInstance());
		} catch (Exception e) {
		}
	}

}
