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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientException;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;

/**
 * Api Mongo connector
 *
 */
public class ApiMongoConnector {

	private static Logger log = Logger.getLogger(ApiMongoConnector.class);
	SSLContext sslContext = null;
	MongoClient mongoClient = null;

	/**
	 * Default constructor
	 */
	public ApiMongoConnector() {
	}


	/**
	 * Mongo create datastore
	 * 
	 * @param connectionUri
	 *            - the connection URI, without database name
	 * @param truststore 
	 *            - the name of the trustore file available in classpath in the /config folder 
	 * @param truststorePass 
	 *            - the password needd to access the  trustore  
	 *            
	 * @return the datastore
	 */
	public Datastore createDatastore(String connectionUri, String truststore, String truststorePass) {
		Datastore datastore = null;
		Morphia connection = new Morphia();
		try {
			log.debug("Connecting to mongo server:" + connectionUri);
			
			MongoClientOptions.Builder mco = MongoClientOptions.builder();
			if(isSslEnabled(connectionUri)){
				validateTrustStoreConfig(truststore, truststorePass);
				log.debug("Enabling ssl connection using truststore: " + truststore + ":"+truststorePass);
				SSLContext sc = getSslContext(truststore, truststorePass);
				mco.sslEnabled(true).sslInvalidHostNameAllowed(true).socketFactory(sc.getSocketFactory());
			}
			
			MongoClientURI mongoUri = new MongoClientURI(connectionUri, mco);
			mongoClient = new MongoClient(mongoUri);
			
			datastore = connection.createDatastore(mongoClient, mongoUri.getDatabase());
			log.info(String.format("Connection to db '%s' mongo server was successful", mongoUri.getDatabase()));
		} catch (MongoException e) {
			//redundant, runtime exceptions should be logged by the system 
			//log.error(e.getMessage());
			throw e;
		}
		return datastore;
	}

	 /**
     * Close the connection to mongo
     */
    public void close() {
        if(mongoClient != null){
        	try{
		    	log.info("Shutting down connections to Mongo...");
		        mongoClient.close();
        	}catch(Throwable th){
        		log.error("cannot close mongo connetions", th);
        	}
        }
    }
    
	protected void validateTrustStoreConfig(String truststore, String truststorePass) {
		//store and pass are mandatory
		if(StringUtils.isEmpty(truststore) || StringUtils.isEmpty(truststorePass))
			throw new IllegalArgumentException("Trustore and truststorePass must not be null when ssl is enabled! " 
					+ truststore + ":"+truststorePass);
	}

	private boolean isSslEnabled(String connectionUri) {
		return connectionUri.contains("ssl=true");
	}

	private SSLContext initSSLContext(String truststore, String truststorePass) {
		try {
			// TODO - make keystore type and SSL version configurable
			String trustStoreLocation = "/config/" + truststore;
			URL trustStoreUri = getClass().getResource(trustStoreLocation);
			if (trustStoreUri == null)
				throw new FileNotFoundException("cannot find trustore file in classpath: " + trustStoreLocation);

			KeyStore jks = KeyStore.getInstance("JKS");

			jks.load(new FileInputStream(new File(trustStoreUri.getFile())), truststorePass.toCharArray());

			String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
			tmf.init(jks);

			sslContext = SSLContext.getInstance("TLSv1.2");
			sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
			return sslContext;
		} catch (KeyManagementException | IOException | KeyStoreException | NoSuchAlgorithmException
				| CertificateException ex) {

			throw new MongoClientException("Cannot initialize mongo truststore", ex);
		}
	}

	public SSLContext getSslContext(String truststore, String truststorePass) {
		// lazy initialization
		if (sslContext == null)
			initSSLContext(truststore, truststorePass);
		return sslContext;
	}
	
}
