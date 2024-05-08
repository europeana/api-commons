package eu.europeana.corelib.db.wrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import com.mongodb.MongoClient;
import com.mongodb.MongoClientException;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.Mapper;
import dev.morphia.mapping.MapperOptions;

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
  public ApiMongoConnector() {}


  /**
   * Mongo create datastore
   * 
   * @param connectionUri - the connection URI, without database name
   * @param truststore - the name of the trustore file available in classpath in the /config folder
   * @param truststorePass - the password needd to access the trustore
   * 
   * @return the datastore
   */
  public Datastore createDatastore(String connectionUri, String truststore, String truststorePass) {
    return createDatastore(connectionUri, truststore, truststorePass, DEFAULT_IDLE_TIME);
  }

  public Datastore createDatastore(String connectionUri, String truststore, String truststorePass,
      int maxConnectionIdleTime) {
    return createDatastore(connectionUri, truststore, truststorePass, maxConnectionIdleTime, (String[]) null);
  }

  public Datastore createDatastore(String connectionUri, String truststore, String truststorePass,
      int maxConnectionIdleTime, String... mongoModelPackage) {
    Datastore datastore = null;
    Morphia connection;
    try {
      log.debug("Connecting to mongo server:" + connectionUri);

      MongoClientOptions.Builder mco =
          buildMongoConnectionOptions(connectionUri, truststore, truststorePass);
      if (maxConnectionIdleTime > 0) {
        mco.maxConnectionIdleTime(maxConnectionIdleTime);
      } else {
        mco.maxConnectionIdleTime(DEFAULT_IDLE_TIME);
      }

      MongoClientURI mongoUri = new MongoClientURI(connectionUri, mco);
      mongoClient = new MongoClient(mongoUri);

      if (mongoModelPackage != null && mongoModelPackage.length > 0) {
        datastore = createConnectionAndIndices(mongoUri, mongoModelPackage);
      } else {
        connection = new Morphia();
        datastore = connection.createDatastore(mongoClient, mongoUri.getDatabase());
      }
      log.info(String.format("Connection to db '%s' mongo server was successful",
          mongoUri.getDatabase()));
    } catch (MongoException e) {
      // runtime exceptions will be logged by the system
      throw e;
    }
    return datastore;
  }


  protected Datastore createConnectionAndIndices(MongoClientURI mongoUri,
      String... mongoModelPackage) {
   
    MapperOptions options = new MapperOptions();
    options.setMapSubPackages(true);
    Mapper packageMapper = new Mapper(options);
    Morphia connection = new Morphia(packageMapper);
    // map classes
    for (String modelPackage : mongoModelPackage) {
      connection.mapPackage(modelPackage, false);
    }
    Datastore datastore = connection.createDatastore(mongoClient, mongoUri.getDatabase());

    // to create indexes
    datastore.ensureIndexes();
    return datastore;
  }

  protected MongoClientOptions.Builder buildMongoConnectionOptions(String connectionUri,
      String truststore, String truststorePass) {

    MongoClientOptions.Builder mco = MongoClientOptions.builder();

    if (isSslEnabled(connectionUri)) {
      // allow invalid host names (required for host aliases)
      mco.sslEnabled(true).sslInvalidHostNameAllowed(true);

      // if the trustore is configured will be used for mongo connection
      if (!StringUtils.isEmpty(truststore)) {
        validateTrustStoreConfig(truststore, truststorePass);
        log.debug("Enabling ssl connection using truststore: " + truststore + ":" + truststorePass);
        SSLContext sc = getSslContext(truststore, truststorePass);
        mco.sslContext(sc);
      }
    }
    
    //set read and write concerns
    mco.readPreference(defaultReadPreference());
    mco.writeConcern(defaultWriteConcern());
    mco.readConcern(defaultReadConcern());
    return mco;
  }


  private ReadConcern defaultReadConcern() {
    return ReadConcern.MAJORITY;
  }


  protected WriteConcern defaultWriteConcern() {
    return WriteConcern.MAJORITY;
  }


  protected ReadPreference defaultReadPreference() {
    return ReadPreference.primaryPreferred();
  }

  public SSLContext getSslContext(String truststore, String truststorePass) {
    if (sslContext != null){
      return sslContext;  
    }
    
    // lazy initialization
    if (truststore.startsWith("/")) {
        return loadTrustStoreFromFile(truststore, truststorePass);
    } else {
        return loadTrustStoreFromClasspath(truststore, truststorePass); 
    }
  }
  
  /**
   * Close the connection to mongo
   */
  public void close() {
    if (mongoClient != null) {
      try {
        log.info("Shutting down connections to Mongo...");
        mongoClient.close();
      } catch (Throwable th) {
        log.error("cannot close mongo connetions", th);
      }
    }
  }

  protected void validateTrustStoreConfig(String truststore, String truststorePass) {
    // store and pass are mandatory
    if (StringUtils.isEmpty(truststore) || StringUtils.isEmpty(truststorePass))
      throw new IllegalArgumentException(
          "Both trustore and truststorePass must be provided, when trustore is used! " + truststore
              + ":" + truststorePass);
  }

  private boolean isSslEnabled(String connectionUri) {
    return connectionUri.contains("tls=true") || connectionUri.contains("ssl=true");
  }

  private SSLContext loadTrustStoreFromClasspath(String truststore, String truststorePass) {
    log.debug("Loading trustore from classpath: {}", truststore);
    String trustStoreLocation = "/" + truststore;
    URL trustStoreUri = getClass().getResource(trustStoreLocation);
    if (trustStoreUri == null) {
      // fallback for backward compatibility, search in /config/folder
      log.info("truststore not at location: {}, search in config subfolder.", trustStoreLocation);
      trustStoreLocation = "/config/" + truststore;
      trustStoreUri = getClass().getResource(trustStoreLocation);
      if (trustStoreUri == null) {
        log.info("truststore not at location: {}, search in config subfolder.", trustStoreLocation);
        throw new MongoClientException(
            "cannot find trustore file in classpath: " + trustStoreLocation);
      }
    }
    
    try (InputStream stream = getClass().getResourceAsStream(trustStoreLocation)) {
      return loadCertificateToSslContext(stream, truststorePass);
    } catch (KeyManagementException | IOException | KeyStoreException | NoSuchAlgorithmException
        | CertificateException ex) {
      throw new MongoClientException("Cannot initialize mongo truststore", ex);
    }
  }


  private SSLContext loadTrustStoreFromFile(String truststore, String truststorePass) {
    //if absolute file path read from file system
    File trustoreFile = new File(truststore); 
    if (!trustoreFile.exists()) {
      throw new MongoClientException("Trustore file location does not exist: " + truststore);
    }
      // fallback for backward compatibility
      log.debug("Loading truststore from file {}", trustoreFile);
      
      try (InputStream stream = new FileInputStream(trustoreFile)) {
        return loadCertificateToSslContext(stream, truststorePass);
      } catch (KeyManagementException | IOException | KeyStoreException | NoSuchAlgorithmException
          | CertificateException ex) {
        throw new MongoClientException("Cannot initialize mongo truststore", ex);
      }
  }


  private SSLContext loadCertificateToSslContext(InputStream stream, String truststorePass)
      throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException,
      KeyManagementException {
    KeyStore jks = KeyStore.getInstance("JKS");
    jks.load(stream, truststorePass.toCharArray());

    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(jks);

    // add the TLS protocol version to configurations when needed
    sslContext = SSLContext.getInstance("TLSv1.2");
    sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());
    return sslContext;
  }



}
