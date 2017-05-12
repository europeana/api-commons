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
package eu.europeana.corelib.db.wrapper;

import org.apache.log4j.Logger;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;

/**
 * Api Mongo connector
 *
 */
public class ApiMongoConnector {

	private static Logger log = Logger.getLogger(ApiMongoConnector.class);

	/**
	 * Default constructor
	 */
	public ApiMongoConnector() {
	}

	/**
	 * Mongo create datastore
	 * @param serverAddress server address
	 * @param dbName database name
	 * @return
	 */
	public Datastore createDatastore(String connectionUri, String dbName) {
		Datastore datastore = null;
		Morphia connection = new Morphia();
		try {
			log.info(String.format("Connecting to '%s' mongo server: %s", dbName, connectionUri));
			MongoClient mongoClient = new MongoClient(new MongoClientURI(connectionUri));
				datastore = connection.createDatastore(mongoClient, dbName);
			log.info(String.format(
					"Connection to db '%s' mongo server was successful", dbName));
		} catch (MongoException e) {
			log.error(e.getMessage());
		}
		return datastore;
	}
}
