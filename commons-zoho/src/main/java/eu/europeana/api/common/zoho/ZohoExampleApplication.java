package eu.europeana.api.common.zoho;

import com.zoho.crm.api.exception.SDKException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application
 *
 * @author Shweta Nazare, Sergiu Gordea, Luthien Dulk
 * Created on 12 feb 2024
 */
@SpringBootApplication
public class ZohoExampleApplication implements CommandLineRunner {

	@Autowired
	eu.europeana.zohomigration.ExampleZohoService service;

	public static void main(String[] args) {
		//SpringApplication.run(ZohoExampleApplication.class, args);
		SpringApplication application  = new SpringApplication(ZohoExampleApplication.class);
		application.setWebApplicationType(WebApplicationType.NONE);
		application.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (args!=null && args.length > 0){
			System.out.println("Do not read this message.");
		}
	}
}
