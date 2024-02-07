package eu.europeana.api.common.zoho;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ZohomigrationApplication implements CommandLineRunner {

	@Autowired
	ContactDataMigrationService service;

	@Autowired
	OrgDataMigrationService serviceorg;

	public static void main(String[] args) {
		//SpringApplication.run(ZohomigrationApplication.class, args);
		SpringApplication application  = new SpringApplication(ZohomigrationApplication.class);
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
		if (args!=null && args.length > 0)
		{
			Set<String> tasks = Set.of(args);
			if(tasks.contains("CONTACT_PHOTO_DOWNLOAD")){
				service.downloadPhotos("Contacts");
			}

			if(tasks.contains("CONTACT_PHOTO_UPLOAD")){
				service.uploadPhotos("Contacts");
			}

			if(tasks.contains("ORG_ATTACHMENT_DOWNLOAD")){
       serviceorg.downloadAttachments("Accounts");
			}

			if(tasks.contains("ORG_ATTACHMENT_UPLOAD")){
				serviceorg.uploadAttachments("Accounts");
			}
		}
	}

}
