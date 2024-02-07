package eu.europeana.api.common.zoho;

import com.zoho.crm.api.ParameterMap;
import com.zoho.crm.api.record.APIException;
import com.zoho.crm.api.record.FileBodyWrapper;
import com.zoho.crm.api.record.FileHandler;
import com.zoho.crm.api.record.RecordOperations;
import com.zoho.crm.api.record.SuccessResponse;
import com.zoho.crm.api.util.APIResponse;
import com.zoho.crm.api.util.Model;
import com.zoho.crm.api.util.StreamWrapper;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class PhotoUploadService {

  Logger LOG = LogManager.getLogger(PhotoUploadService.class);
  public int uploadContactPhotos(String moduleAPIName, long recordId, String absoluteFilePath)
      throws Exception {
    RecordOperations recordOperations = new RecordOperations();
    FileBodyWrapper fileBodyWrapper = new FileBodyWrapper();
    StreamWrapper streamWrapper = new StreamWrapper(absoluteFilePath);
    fileBodyWrapper.setFile(streamWrapper);
    ParameterMap paramInstance = new ParameterMap();
//		paramInstance.add(UploadPhotoParam.RESTRICT_TRIGGERS, "workflow");
    APIResponse<FileHandler> response = recordOperations.uploadPhoto(recordId, moduleAPIName, fileBodyWrapper, paramInstance);
    if (response != null)
    {

      if (response.isExpected())
      {
        FileHandler fileHandler = response.getObject();
        if (fileHandler instanceof SuccessResponse)
        {
          SuccessResponse successResponse = (SuccessResponse) fileHandler;
          StringBuilder br = new StringBuilder();
          br.append("Record "+ recordId + " - "+ " Status: " + successResponse.getStatus().getValue()+" Code: " + successResponse.getCode().getValue());

          for (Map.Entry<String, Object> entry : successResponse.getDetails().entrySet())
          {
            br.append(" "+ entry.getKey() + ": " + entry.getValue());
          }
          br.append(" Message: " + successResponse.getMessage().getValue());

          LOG.info(br.toString());
        }
        else if (fileHandler instanceof APIException)
        {
          APIException exception = (APIException) fileHandler;
          StringBuilder brr = new StringBuilder();

          brr.append("Record "+ recordId);
          brr.append(" Status: " + exception.getStatus().getValue());
          brr.append(" Code: " + exception.getCode().getValue());
          brr.append(" Details: ");
          for (Map.Entry<String, Object> entry : exception.getDetails().entrySet())
          {
            brr.append(" "+ entry.getKey() + ": " + entry.getValue());
          }
          brr.append(" Message: " + exception.getMessage().getValue());
          LOG.error(brr.toString());
        }
      }
      else
      {
        Model responseObject = response.getModel();
        Class<? extends Model> clas = responseObject.getClass();
        java.lang.reflect.Field[] fields = clas.getDeclaredFields();
        for (java.lang.reflect.Field field : fields)
        {
          LOG.error(field.getName() + ":" + field.get(responseObject));
        }
      }

      return response.getStatusCode();
    }
    return 0;
  }
}
