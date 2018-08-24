package eu.europeana.api.commons.nosql.embedded;

import java.io.IOException;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoIterable;

public class EmbeddedMongoServerTest {

	public static void main(String[] args) throws IOException{
//		int port = 27017;
		EmbeddedMongoServer ms = null;
		try{
			int port = 10001;
			ms = new EmbeddedMongoServer(port);
			MongoClient mc = new MongoClient("localhost", port);
			MongoIterable<String> dbs = mc.listDatabaseNames();
			System.out.println(dbs.first());
			mc.close();
		}finally{
			if (ms != null)
				ms.stop();
		}
	}
}
