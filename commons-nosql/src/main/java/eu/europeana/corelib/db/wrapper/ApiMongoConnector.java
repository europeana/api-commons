package eu.europeana.corelib.db.wrapper;

import java.io.File;
import java.io.FileInputStream;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	private static final Logger log = LogManager.getLogger(ApiMongoConnector.class);
	SSLContext sslContext = null;
	MongoClient mongoClient = null;
	int DEFAULT_IDLE_TIME = 5000;  
	
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
	    return createDatastore(connectionUri, truststore, truststorePass, DEFAULT_IDLE_TIME);
	}

	public Datastore createDatastore(String connectionUri, String truststore, String truststorePass, int maxConnectionIdleTime) {
		Datastore datastore = null;
		Morphia connection = new Morphia();
		try {
			log.debug("Connecting to mongo server:" + connectionUri);
			
			MongoClientOptions.Builder mco = buildMongoConnectionOptions(connectionUri, truststore, truststorePass);
			if(maxConnectionIdleTime > 0) {
			    mco.maxConnectionIdleTime(maxConnectionIdleTime);
			} else {
			    mco.maxConnectionIdleTime(DEFAULT_IDLE_TIME);
			} 
			
			MongoClientURI mongoUri = new MongoClientURI(connectionUri, mco);
			mongoClient = new MongoClient(mongoUri);
			
			datastore = connection.createDatastore(mongoClient, mongoUri.getDatabase());
			datastore.ensureIndexes();
			log.info(String.format("Connection to db '%s' mongo server was successful", mongoUri.getDatabase()));
		} catch (MongoException e) {
			//runtime exceptions will be logged by the system 
			throw e;
		}
		return datastore;
	}

	private MongoClientOptions.Builder buildMongoConnectionOptions(String connectionUri, String truststore,
			String truststorePass) {
		
		MongoClientOptions.Builder mco = MongoClientOptions.builder();
		
		if(isSslEnabled(connectionUri)){
			//allow invalid host names (required by mongo)
			mco.sslEnabled(true).sslInvalidHostNameAllowed(true);
			
			//if the trustore is configured will be used for mongo connection
			if(!StringUtils.isEmpty(truststore)){
				validateTrustStoreConfig(truststore, truststorePass);
				log.debug("Enabling ssl connection using truststore: " + truststore + ":"+truststorePass);
				SSLContext sc = getSslContext(truststore, truststorePass);
				mco.socketFactory(sc.getSocketFactory());	
			}		
		}
		return mco;
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
			throw new IllegalArgumentException("Both trustore and truststorePass must be provided, when trustore is used! " 
					+ truststore + ":"+truststorePass);
	}

	private boolean isSslEnabled(String connectionUri) {
		return connectionUri.contains("ssl=true");
	}

	private SSLContext initSSLContext(String truststore, String truststorePass) {
		
		// TODO - make keystore type and SSL version configurable
		String trustStoreLocation = "/config/" + truststore;
		URL trustStoreUri = getClass().getResource(trustStoreLocation);
		if (trustStoreUri == null) {
		    throw new MongoClientException("cannot find trustore file in classpath: " + trustStoreLocation);  
		}
		
		try(FileInputStream stream = new FileInputStream(new File(trustStoreUri.getFile()))) {
			KeyStore jks = KeyStore.getInstance("JKS");
			
			jks.load(stream, truststorePass.toCharArray());

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
