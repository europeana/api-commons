package eu.europeana.api.common.zoho;

import com.zoho.crm.api.record.APIException;
import com.zoho.crm.api.record.DownloadHandler;
import com.zoho.crm.api.record.FileBodyWrapper;
import com.zoho.crm.api.record.RecordOperations;
import com.zoho.crm.api.util.APIResponse;
import com.zoho.crm.api.util.Model;
import com.zoho.crm.api.util.StreamWrapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class PhotoDownloadService {

  Logger LOG= LogManager.getLogger(PhotoDownloadService.class);
public int downloadContactPhotos(String moduleAPIName, long zohoUSrecordID,long zohoEURecordID, String destinationFolder) throws Exception{
  RecordOperations recordOperations = new RecordOperations();
  APIResponse<DownloadHandler> response = recordOperations.getPhoto(zohoUSrecordID, moduleAPIName);
  if (response != null)
  {

    if (Arrays.asList(204, 304).contains(response.getStatusCode()))
    {
     // LOG.info(response.getStatusCode() == 204 ? "No Content for record "+zohoUSrecordID : "Not Modified record "+zohoUSrecordID);
      return response.getStatusCode();
    }
    if (response.isExpected())
    {
      DownloadHandler downloadHandler = response.getObject();
      if (downloadHandler instanceof FileBodyWrapper)
      {
        FileBodyWrapper fileBodyWrapper = (FileBodyWrapper) downloadHandler;
        StreamWrapper streamWrapper = fileBodyWrapper.getFile();

        String filename = destinationFolder + File.separatorChar +zohoEURecordID+"_Contacts_photo.png";



        File file = new File(filename);
      //  File file = new File(destinationFolder + File.separatorChar +zohoEURecordID+"_"+streamWrapper.getName());
        InputStream is = streamWrapper.getStream();
        OutputStream os = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1)
        {
          os.write(buffer, 0, bytesRead);
        }
        is.close();
        os.flush();
        os.close();

        LOG.info("Download success for US record "+zohoUSrecordID+" is : " + filename);
      }
      else if (downloadHandler instanceof APIException)
      {
        APIException exception = (APIException) downloadHandler;
        LOG.error("Status: " + exception.getStatus().getValue() + " Code: " + exception.getCode().getValue() + " Details: ");
        for (Map.Entry<String, Object> entry : exception.getDetails().entrySet())
        {
          LOG.error(entry.getKey() + ": " + entry.getValue());
        }
        LOG.error(" Message: " + exception.getMessage().getValue());
      }

    }
    else
    {// If response is not as expected
      Model responseObject = response.getModel();
      Class<? extends Model> clas = responseObject.getClass();
      java.lang.reflect.Field[] fields = clas.getDeclaredFields();
      for (java.lang.reflect.Field field : fields)
      {
        field.setAccessible(true);
        LOG.info(field.getName() + ":" + field.get(responseObject));
      }
    }
    return response.getStatusCode();
  }
  return 0;
}
}
