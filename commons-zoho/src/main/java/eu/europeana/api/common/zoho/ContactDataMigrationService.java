package eu.europeana.api.common.zoho;

import com.zoho.api.authenticator.OAuthToken;
import com.zoho.api.authenticator.Token;
import com.zoho.crm.api.exception.SDKException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ContactDataMigrationService {

  public static final String RECORDS_FILE = "Contacts_2023_12_27.csv";
  Logger LOG = LogManager.getLogger(ContactDataMigrationService.class);
  public String destinationFolderForPhotos = "C://europeana//projects//zohomigration//zoho_us_contact_photos";

private ZohoAccessConfiguration config;


private PhotoDownloadService downloadService;

private PhotoUploadService uploadService;


private static ZohoInMemoryTokenStore tokenStore ;

  public ContactDataMigrationService(ZohoAccessConfiguration config,PhotoDownloadService downloadService,PhotoUploadService uploadService){
      this.config = config;
      this.uploadService=uploadService;
      this.downloadService=downloadService;
  }



  public void downloadPhotos(String modulename){

    Long startTime = System.currentTimeMillis();
    LOG.info("Starting Photo download at "+startTime);
    Long lastProcessedRecordId = 0L;

    try {
      Long oldCRMId = Long.parseLong("1482250000060440024");
      Long newCRMID = Long.parseLong("486281000001038434");


      Map<Long,Long> recordIds =  getRecordListFromFile();


      getOrCreateAccessToZoho(DataCenterVal.US);
      for (var entry :recordIds.entrySet()) {
        System.out.println(entry.getKey() +"_"+entry.getValue());
      if( downloadService.downloadContactPhotos(modulename, entry.getKey(), entry.getValue(),
            destinationFolderForPhotos) != 0) {
          lastProcessedRecordId = entry.getKey();
         }
       }

    }
    catch (Exception e){
      e.printStackTrace();
    }
    finally {
      LOG.info("Last Processed record ID ="+lastProcessedRecordId);
      Long endTime = System.currentTimeMillis() -startTime;
      LOG.info("Photo download process ended in "+endTime+" milliseconds !!");
    }

  }

  public void uploadPhotos(String modulename) {

    Long startTime = System.currentTimeMillis();
    LOG.info("Starting Photo upload at "+startTime);
    List<Long> lastProcessedRecordIds =new ArrayList<>();
    try {

      getOrCreateAccessToZoho(DataCenterVal.EU);

      List<Path> fileList = getFileNamesToUpload(destinationFolderForPhotos);
    //  fileList.forEach(p-> System.out.println(p.getFileName().toString()));

      LOG.info("Uploading "+fileList.size()+" number of photos");

      for (Path path : fileList) {
        String tokenizedFilename[] = (path.getFileName().toString()).split("_");
        if (StringUtils.isNotBlank(tokenizedFilename[0])) {
          Long recordId = Long.parseLong(tokenizedFilename[0]);
        if( uploadService.uploadContactPhotos(modulename,recordId,path.toString()) != 0){
          lastProcessedRecordIds.add(recordId);
        }
        }
      }
    }catch (Exception e){
      e.printStackTrace();
    }
    finally {
      LOG.info("Last Processed record ID ="+lastProcessedRecordIds);
      Long endTime = System.currentTimeMillis() -startTime;
      LOG.info("Photo upload process ended in "+endTime+" milliseconds !!");

    }
  }





  private boolean getOrCreateAccessToZoho(DataCenterVal datacenter) throws SDKException {
    ZohoAccessClient client = new ZohoAccessClient();

    if (tokenStore != null) {
      Token tokenById = tokenStore.findTokenById(config.getZohoEUUserName());

      if (tokenById != null && tokenById instanceof OAuthToken) {
        System.out.println("Token Expires in : -" + ((OAuthToken) tokenById).getExpiresIn());
      }

    } else {
      tokenStore = new ZohoInMemoryTokenStore();
    }

    switch (datacenter){

      case EU -> {

        System.out.println("Initializing access to EU data center");



        return  client.initialize(
            tokenStore,
            config.getZohoEUUserName(),
            config.getZohoEUClientId(),
            config.getZohoEUClientSecret(),
            config.getZohoEURefreshToken(),
            config.getZohoEURedirectUrl(),
            datacenter);
      }
      case US -> {

        System.out.println("Initializing access to US data center");
        return  client.initialize(
            new ZohoInMemoryTokenStore(),
            config.getZohoUSUserName(),
            config.getZohoUSClientId(),
            config.getZohoUSClientSecret(),
            config.getZohoUSRefreshToken(),
            config.getZohoUSRedirectUrl(),
            datacenter);
      }
    }




  return false;

  }

  private static List<Path> getFileNamesToUpload(String localContactPhotoStorageDir) {
    List<Path> fileList = new ArrayList<>();
    try (Stream<Path> paths = Files.walk(Paths.get(localContactPhotoStorageDir))) {
      fileList = paths
          .filter(Files::isRegularFile)
          .collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return fileList;
  }

  private Map<Long,Long> getRecordListFromFile() throws Exception {

    Map<Long,Long> idList = new HashMap<>();

    String filePath ="C://europeana//projects//zohomigration//zohomigration//src//main//resources//Contacts_2023_12_18.csv";

    InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
        RECORDS_FILE);
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);

    for (CSVRecord csvRecord : csvParser) {
      String zohoUSRecordID = csvRecord.get("Old CRM ID");
      String ZohoEURecordId = csvRecord.get("Record Id");
      if (StringUtils.isNotBlank(zohoUSRecordID) && StringUtils.isNotBlank(ZohoEURecordId)) {
        zohoUSRecordID = zohoUSRecordID.substring(zohoUSRecordID.indexOf("_")+1);
        ZohoEURecordId = ZohoEURecordId.substring(ZohoEURecordId.indexOf("_")+1);
        idList.put(Long.parseLong(zohoUSRecordID),Long.parseLong(ZohoEURecordId));
      }
    }
    //System.out.println(idList);
    return idList;
  }

}
