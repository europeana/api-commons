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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class OrgDataMigrationService {

  public static final String RECORDS_FILE = "Institutions_2023_12_28.csv";
  Logger LOG = LogManager.getLogger(OrgDataMigrationService.class);
  public String destinationFolderForAttachments = "C://europeana//projects//zohomigration//zoho_us_org_attachment";

  private ZohoAccessConfiguration config;


  private OrgAttachmentDownloadService downloadService;

  private OrgAttachmentUploadService uploadService;


  private static ZohoInMemoryTokenStore tokenStore ;

  public OrgDataMigrationService(ZohoAccessConfiguration config,OrgAttachmentDownloadService downloadService,OrgAttachmentUploadService uploadService){
    this.config = config;
    this.uploadService=uploadService;
    this.downloadService=downloadService;
  }

  public void getAttachmentsInfo(String modulename){
    Long startTime = System.currentTimeMillis();
    LOG.info("Starting Org Attachment download at "+startTime);
    List<Long> lastProcessedRecordIds = new ArrayList<>();

    try {

      Map<Long, Long> oldOrgIdToNewOrgId = getOrganizationIdsFromFile();

      LOG.info("Processing "+oldOrgIdToNewOrgId.size()+" number of records!");

      Map<Long,Map<Long,List<Long>>> newOrgIdToOldDatamap = new HashMap<>();

      getOrCreateAccessToZoho(DataCenterVal.US);

      LOG.info("Finding attachment details ...");
      Map<Long,Long> attachmentToOldOrgId = new HashMap<>();
      for(var entry : oldOrgIdToNewOrgId.entrySet()){
       // System.out.println(entry.getKey() +"_"+entry.getValue());
        List<Long> attachmentIDs = getAttachmentIdsForOrg(modulename,entry.getKey());
        if( attachmentIDs!=null && !attachmentIDs.isEmpty())
        {
          attachmentIDs.forEach(p-> attachmentToOldOrgId.put(p,entry.getKey()));
        }
      }

      LOG.info("Found "+attachmentToOldOrgId.size()+" number of organizations with attachment!");

      System.out.println(attachmentToOldOrgId);



//      LOG.info("Starting attachment download....");
//
//      List<Long> alreadyDownloaded = getDownloadedrecordIds();
//      for (var entry :attachmentToOldOrgId.entrySet()) {
//        //System.out.println(entry.getKey() +"_"+entry.getValue());
//
//        Long oldRecordID = entry.getValue();
//        Long oldAttachmentID = entry.getKey();
//        Long newRecordId= oldOrgIdToNewOrgId.get(oldRecordID);
//
//        if(!alreadyDownloaded.contains(oldRecordID)) {
//
//          if (downloadService.downloadAttachment(modulename, oldRecordID, oldAttachmentID,
//              newRecordId,
//              destinationFolderForAttachments) != 0) {
//            lastProcessedRecordIds.add(oldAttachmentID);
//          }
//        }
//        else{
//          LOG.info("Attachment for record : " +oldRecordID + " already download" );
//        }
//      }

    }
    catch (Exception e){
      e.printStackTrace();
    }
    finally {
      LOG.info("Last Processed record IDs ="+lastProcessedRecordIds);
      Long endTime = System.currentTimeMillis() -startTime;
      LOG.info("Attachment download process ended in "+endTime+" milliseconds !!");
    }

  }


  public void downloadAttachments(String modulename){
    Long startTime = System.currentTimeMillis();
    LOG.info("Starting Org Attachment download at "+startTime);
    List<Long> lastProcessedRecordIds = new ArrayList<>();

    try {

      Map<Long, Long> oldOrgIdToNewOrgId = getOrganizationIdsFromFile();

    //  LOG.info("Processing "+oldOrgIdToNewOrgId.size()+" number of records!");

     // Map<Long,Map<Long,List<Long>>> newOrgIdToOldDatamap = new HashMap<>();

      getOrCreateAccessToZoho(DataCenterVal.US);

      LOG.info("Finding attachment details ...");
     Map<Long,Long> attachmentToOldOrgId = getAttachmentIdsFromFile();
//      for(var entry : oldOrgIdToNewOrgId.entrySet()){
//        // System.out.println(entry.getKey() +"_"+entry.getValue());
//        List<Long> attachmentIDs = getAttachmentIdsForOrg(modulename,entry.getKey());
//        if( attachmentIDs!=null && !attachmentIDs.isEmpty())
//        {
//          attachmentIDs.forEach(p-> attachmentToOldOrgId.put(p,entry.getKey()));
//        }
//      }
//
//      LOG.info("Found "+attachmentToOldOrgId.size()+" number of organizations with attachment!");
//
//      System.out.println(attachmentToOldOrgId);






      LOG.info("Starting attachment download....");

      List<Long> alreadyDownloaded = getDownloadedrecordIds();
      for (var entry :attachmentToOldOrgId.entrySet()) {
        //System.out.println(entry.getKey() +"_"+entry.getValue());

        Long oldRecordID = entry.getValue();
        Long oldAttachmentID = entry.getKey();
        Long newRecordId= oldOrgIdToNewOrgId.get(oldRecordID);

       //Attachment of 1482250000004375767L is having quotes in filename which is causing download issues

        if(oldRecordID!= 1482250000004375767L && oldRecordID!=1482250000010686001L && !alreadyDownloaded.contains(oldAttachmentID)) {

          if (downloadService.downloadAttachment(modulename, oldRecordID, oldAttachmentID,
              newRecordId,
              destinationFolderForAttachments) != 0) {
            lastProcessedRecordIds.add(oldAttachmentID);
          }
        }
        else{
          LOG.info("Attachment id: " +oldAttachmentID + " for record "+ oldRecordID + "already download" );
        }
      }

    }
    catch (Exception e){
      e.printStackTrace();
    }
    finally {
      LOG.info("Last downloaded attachment IDs ="+lastProcessedRecordIds);
      Long endTime = System.currentTimeMillis() -startTime;
      LOG.info("Attachment download process ended in "+endTime+" milliseconds !!");
    }

  }


  private List<Long> getDownloadedrecordIds() throws Exception {

    List<Long> idList = new ArrayList<>();
    InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
        "downloadedAttachments.csv");
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);

    for (CSVRecord csvRecord : csvParser) {
      String zohoUSRecordId = csvRecord.get("recordIdUS");
      if (StringUtils.isNotBlank(zohoUSRecordId)) {
        idList.add(Long.parseLong(zohoUSRecordId));
      }
    }
    System.out.println("Already Downloaded ids  :" +idList);
    return idList;
  }

  private List<Long> getAttachmentIdsForOrg(String moduleAPIName, Long key) throws Exception {

    return  downloadService.getAttachments(moduleAPIName,key);

  }

  private Map<Long,Long> getOrganizationIdsFromFile() throws IOException {
    Map<Long,Long> idList = new HashMap<>();

    InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
        RECORDS_FILE);
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);

    for (CSVRecord csvRecord : csvParser) {
      String zohoUSRecordID = csvRecord.get("Zoho old ID");
      String ZohoEURecordId = csvRecord.get("Record Id");
      if (StringUtils.isNotBlank(zohoUSRecordID) && StringUtils.isNotBlank(ZohoEURecordId)) {
        zohoUSRecordID = zohoUSRecordID.substring(zohoUSRecordID.indexOf("_")+1);
        ZohoEURecordId = ZohoEURecordId.substring(ZohoEURecordId.indexOf("_")+1);
        idList.put(Long.parseLong(zohoUSRecordID),Long.parseLong(ZohoEURecordId));
      }else {
        System.out.println(" Incorrect data :" + zohoUSRecordID + "_" + ZohoEURecordId );
      }
    }
   System.out.println(idList);
    return idList;

  }



  private Map<Long,Long> getAttachmentIdsFromFile() throws IOException {
    Map<Long,Long> idList = new HashMap<>();

    InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
        "attachmentIDList.csv");
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    CSVParser csvParser = CSVFormat.DEFAULT.withHeader().parse(reader);

    for (CSVRecord csvRecord : csvParser) {
      String zohoUSRecordID = csvRecord.get("oldRecordId");
      String attachmentIDList = csvRecord.get("attachmentList");
      if(StringUtils.isNotBlank(zohoUSRecordID) && StringUtils.isNotBlank(attachmentIDList))
      {
        String[] attachmentIDs = attachmentIDList.split("-");
        if(ArrayUtils.isNotEmpty(attachmentIDs))
        {
          Arrays.stream(attachmentIDs).forEach(p->
                  idList.put(Long.parseLong(p),Long.parseLong(zohoUSRecordID))
              );
        }

      }
      else
      {
        LOG.error("Invalid record in attachment List input file!!");
      }


    }
    System.out.println("AttachmentID to OldRecordID Of zohoUS Map" + idList);
    return idList;

  }




    public void uploadAttachments(String moduleAPIName) throws Exception
    {
      Long startTime = System.currentTimeMillis();
      LOG.info("Starting Attachment upload at "+startTime);
      List<Long> lastProcessedRecordIds =new ArrayList<>();
      try {

        getOrCreateAccessToZoho(DataCenterVal.EU);

        List<Path> fileList = getFileNamesToUpload(destinationFolderForAttachments);
        //  fileList.forEach(p-> System.out.println(p.getFileName().toString()));

        LOG.info("Uploading "+fileList.size()+" number of Attachments");

        for (Path path : fileList) {

          Long recordId = Long.parseLong(path.getParent().getFileName().toString());

          //System.out.println(recordId+"  "+path.toString());
        //  String tokenizedFilename[] = (path.getFileName().toString()).split("_");
          //if (StringUtils.isNotBlank(tokenizedFilename[0])) {
           // Long recordId = Long.parseLong(tokenizedFilename[0]);
            if( uploadService.uploadAttachments(moduleAPIName,recordId,path.toString()) != 0){
              lastProcessedRecordIds.add(recordId);
           }
          //}
        }
      }catch (Exception e){
        e.printStackTrace();
      }
      finally {
        LOG.info("Last Processed record ID ="+lastProcessedRecordIds);
        Long endTime = System.currentTimeMillis() -startTime;
        LOG.info("Attachment upload process ended in "+endTime+" milliseconds !!");

      }


  }

  private List<Path> getFileNamesToUpload(String localFolderForAttachments) {

    List<Path> fileList = new ArrayList<>();
    try (Stream<Path> paths = Files.walk(Paths.get(localFolderForAttachments))) {
      fileList = paths
          .filter(Files::isRegularFile)
          .collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return fileList;
  }


  private void  getOrCreateAccessToZoho(DataCenterVal datacenter) throws SDKException {
    ZohoAccessClient client = new ZohoAccessClient();

    if (tokenStore != null) {
      Token tokenById = tokenStore.findTokenById(config.getZohoEUUserName());
      if (tokenById != null && tokenById instanceof OAuthToken) {
        System.out.println("Token Expires in : -" + ((OAuthToken) tokenById).getExpiresIn());
      }
    } else {
      tokenStore = new ZohoInMemoryTokenStore();
    }

    switch (datacenter) {
      case EU -> {
        System.out.println("Initializing access to EU data center");
        client.initialize(
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
        client.initialize(
            new ZohoInMemoryTokenStore(),
            config.getZohoUSUserName(),
            config.getZohoUSClientId(),
            config.getZohoUSClientSecret(),
            config.getZohoUSRefreshToken(),
            config.getZohoUSRedirectUrl(),
            datacenter);
      }
    }

  }
  }
