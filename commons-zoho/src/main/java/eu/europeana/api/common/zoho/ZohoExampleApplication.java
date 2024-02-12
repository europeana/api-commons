package eu.europeana.api.common.zoho;

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

//	@Autowired
//	ContactDataMigrationService service;
//
//	@Autowired
//	OrgDataMigrationService serviceorg;

	public static void main(String[] args) {
		//SpringApplication.run(ZohoExampleApplication.class, args);
		SpringApplication application  = new SpringApplication(ZohoExampleApplication.class);
		application.setWebApplicationType(WebApplicationType.NONE);
		application.run(args);


	}

	/**
	 * Valid arguments :
	 *    CONTACT_PHOTO_DOWNLOAD
	 *    CONTACT_PHOTO_UPLOAD
	 *
	 *    ORG_ATTACHMENT_DOWNLOAD
	 *    ORG_ATTACHMENT_UPLOAD
	 *
	 * @param args
	 * @throws Exception
	 */
	@Override
	public void run(String... args) throws Exception {
		if (args!=null && args.length > 0){
			System.out.println("Do not read this message.");
		}

	}

}
