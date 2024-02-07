package eu.europeana.api.common.zoho;

import com.zoho.crm.api.attachments.APIException;
import com.zoho.crm.api.attachments.ActionHandler;
import com.zoho.crm.api.attachments.ActionResponse;
import com.zoho.crm.api.attachments.ActionWrapper;
import com.zoho.crm.api.attachments.FileBodyWrapper;
import com.zoho.crm.api.attachments.SuccessResponse;
import com.zoho.crm.api.util.APIResponse;
import com.zoho.crm.api.util.Model;
import com.zoho.crm.api.util.StreamWrapper;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class OrgAttachmentUploadService {

  Logger LOG = LogManager.getLogger(OrgAttachmentUploadService.class);
  public int uploadAttachments(String moduleAPIName, Long recordId, String absoluteFilePath) throws Exception
  {
    OrgAttachmentsOperations attachmentsOperations = new OrgAttachmentsOperations();
     FileBodyWrapper fileBodyWrapper = new FileBodyWrapper();
   // String filename  = URLEncoder.encode(absoluteFilePath, "UTF-8");

    System.out.println(" absolute file path : "+absoluteFilePath);

    StreamWrapper streamWrapper = new StreamWrapper(absoluteFilePath);




    fileBodyWrapper.setFile(streamWrapper);

    APIResponse<ActionHandler> response = attachmentsOperations.createAttachment(recordId, moduleAPIName, fileBodyWrapper);
    if (response != null) {

      if (response.isExpected()) {
         ActionHandler actionHandler = response.getObject();
        if (actionHandler instanceof ActionWrapper) {
           ActionWrapper actionWrapper = (ActionWrapper) actionHandler;

          List<ActionResponse> actionResponses = (List<ActionResponse>) actionWrapper.getData();

          for (ActionResponse actionResponse : actionResponses) {
            if (actionResponse instanceof SuccessResponse) {
              SuccessResponse successResponse = (SuccessResponse) actionResponse;

              StringBuilder br = new StringBuilder();
              br.append("OrgRecordId : " +recordId);
              br.append(" Status: " + successResponse.getStatus().getValue());
//              br.append(" Code: " + successResponse.getCode().getValue());
//              br.append(" Details: ");
//              if (successResponse.getDetails() != null) {
//                for (Map.Entry<String, Object> entry : successResponse.getDetails().entrySet()) {
//                  br.append(" "+entry.getKey() + ": " + entry.getValue());
//                }
//              }
              br.append(" Message: " + successResponse.getMessage());
              LOG.info(br.toString());

            } else if (actionResponse instanceof APIException) {
              APIException exception = (APIException) actionResponse;
              StringBuilder br = new StringBuilder();
              br.append(" Org RecordId : " +recordId);
              br.append(" Status: " + exception.getStatus().getValue());
              br.append(" Code: " + exception.getCode().getValue());
              br.append(" Details: ");
              if (exception.getDetails() != null) {
                for (Map.Entry<String, Object> entry : exception.getDetails().foreachEntry()) {
                  br.append(" "+entry.getKey() + ": " + entry.getValue());
                }
              }
              br.append(" Message: " + exception.getMessage());
              LOG.error(br.toString());
            }
          }
        } else if (actionHandler instanceof com.zoho.crm.api.attachments.APIException) {
           APIException exception = (APIException) actionHandler;

          StringBuilder br = new StringBuilder();

          br.append(" Status: " + exception.getStatus().getValue());
          br.append(" Code: " + exception.getCode().getValue());
          br.append(" Details: ");
          if (exception.getDetails() != null) {
            for (Map.Entry<String, Object> entry : exception.getDetails().entrySet()) {
              br.append(" "+entry.getKey() + ": " + entry.getValue());
            }
          }
          br.append(" Message: " + exception.getMessage());

          LOG.info(br.toString());
        }
      } else {
        Model responseObject = response.getModel();
        Class<? extends Model> clas = responseObject.getClass();
        java.lang.reflect.Field[] fields = clas.getDeclaredFields();
        for (java.lang.reflect.Field field : fields) {
          field.setAccessible(true);
          LOG.info(field.getName() + ":" + field.get(responseObject));
        }
      }
      return response.getStatusCode();
    }
    return 0;
  }

}
