package eu.europeana.api.commons.nosql.embedded;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

public class EmbeddedMongoServer{

	private static Logger LOG = LogManager.getLogger(EmbeddedMongoServer.class);
	public static final int DEFAULT_MONGO_PORT = 10001;
	MongodStarter starter;
	MongodExecutable mongodExecutable;
	
	public EmbeddedMongoServer(){
		this(DEFAULT_MONGO_PORT);
	}
	
	public EmbeddedMongoServer(int port) {
		try {
			//Version.Main version = Version.Main.PRODUCTION;
			Version version = Version.V3_5_5;
			IMongodConfig conf = new MongodConfigBuilder().version(version)
					.net(new Net(port, Network.localhostIsIPv6())).build();
			System.out.println(version);
			starter = MongodStarter.getDefaultInstance();
			mongodExecutable = starter.prepare(conf);
		} catch (IOException e) {
			LOG.error("cannot start embedded mongo server", e);
		}
	}

	public void start() throws IOException {
		mongodExecutable.start();
	}
	
	public void stop() throws IOException {
		mongodExecutable.stop();
	}
}
