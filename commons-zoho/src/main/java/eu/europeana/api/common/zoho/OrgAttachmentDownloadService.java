package eu.europeana.api.common.zoho;

import com.zoho.crm.api.ParameterMap;
import com.zoho.crm.api.attachments.AttachmentsOperations;
import com.zoho.crm.api.attachments.AttachmentsOperations.GetAttachmentsParam;
import com.zoho.crm.api.attachments.FileBodyWrapper;
import com.zoho.crm.api.attachments.ResponseHandler;
import com.zoho.crm.api.attachments.ResponseWrapper;
import com.zoho.crm.api.record.APIException;
import com.zoho.crm.api.util.APIResponse;
import com.zoho.crm.api.util.Model;
import com.zoho.crm.api.util.StreamWrapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class OrgAttachmentDownloadService {

  Logger LOG = LogManager.getLogger(OrgAttachmentDownloadService.class);


  public List<Long> getAttachments(String moduleAPIName, Long recordId)
      throws Exception {

    List<Long> attachmentIds = new ArrayList<>();
    AttachmentsOperations attachmentsOperations = new AttachmentsOperations();
    ParameterMap paramInstance = new ParameterMap();
    paramInstance.add(GetAttachmentsParam.PAGE, 1);
    paramInstance.add(GetAttachmentsParam.PER_PAGE, 10);
    paramInstance.add(GetAttachmentsParam.FIELDS, "id");
   // paramInstance.add(GetAttachmentsParam.IDS, "347706117069001");
    APIResponse<ResponseHandler> response = attachmentsOperations.getAttachments(recordId, moduleAPIName, paramInstance);
    if (response != null)
    {

      if (Arrays.asList(204, 304).contains(response.getStatusCode()))
      {
       // System.out.println(response.getStatusCode() == 204 ? "No Content" : "Not Modified");
        return null;
      }
      if (response.isExpected())
      {
        ResponseHandler responseHandler = response.getObject();
        if (responseHandler instanceof ResponseWrapper)
        {
          ResponseWrapper responseWrapper = (ResponseWrapper) responseHandler;
          List<com.zoho.crm.api.attachments.Attachment> attachments = responseWrapper.getData();
          for (com.zoho.crm.api.attachments.Attachment attachment : attachments)
          {

            attachmentIds.add(attachment.getId());
//            com.zoho.crm.api.users.MinifiedUser owner = attachment.getOwner();
//              if (owner != null)
//              {
//                System.out.println("Attachment Owner User-Name: " + owner.getName());
//                System.out.println("Attachment Owner User-ID: " + owner.getId());
//                System.out.println("Attachment Owner User-Email: " + owner.getEmail());
//              }
//              System.out.println("Attachment Modified Time: " + attachment.getModifiedTime());
//              System.out.println("Attachment File Name: " + attachment.getFileName());
//              System.out.println("Attachment Created Time: " + attachment.getCreatedTime());
//              System.out.println("Attachment File Size: " + attachment.getSize());
//              ParentId parentId = attachment.getParentId();
//              if (parentId != null)
//              {
//                System.out.println("Attachment parent record Name: " + parentId.getName());
//                System.out.println("Attachment parent record ID: " + parentId.getId());
//              }
//              System.out.println("Attachment is Editable: " + attachment.getEditable());
//              System.out.println("Attachment SharingPermission: " + attachment.getSharingPermission());
//              System.out.println("Attachment File ID: " + attachment.getFileId());
//              System.out.println("Attachment File Type: " + attachment.getType());
//              System.out.println("Attachment seModule: " + attachment.getSeModule());
//              com.zoho.crm.api.users.MinifiedUser modifiedBy = attachment.getModifiedBy();
//              if (modifiedBy != null)
//              {
//                System.out.println("Attachment Modified By User-Name: " + modifiedBy.getName());
//                System.out.println("Attachment Modified By User-ID: " + modifiedBy.getId());
//                System.out.println("Attachment Modified By User-Email: " + modifiedBy.getEmail());
//              }
//              System.out.println("Attachment Type: " + attachment.getAttachmentType());
//              System.out.println("Attachment State: " + attachment.getState());
//              System.out.println("Attachment ID: " + attachment.getId());


//              com.zoho.crm.api.users.MinifiedUser createdBy = attachment.getCreatedBy();
//              if (createdBy != null)
//              {
//                System.out.println("Attachment Created By User-Name: " + createdBy.getName());
//                System.out.println("Attachment Created By User-ID: " + createdBy.getId());
//                System.out.println("Attachment Created By User-Email: " + createdBy.getEmail());
//              }
//              System.out.println("Attachment LinkUrl: " + attachment.getLinkUrl());
          }

          LOG.info("Attachment found for record :" +recordId+" attachmentIDs :"+attachmentIds);
//          Info info = responseWrapper.getInfo();
//          if (info != null)
//          {
//            if (info.getPerPage() != null)
//            {
//              System.out.println("Attachment Info PerPage: " + info.getPerPage().toString());
//            }
//            if (info.getCount() != null)
//            {
//              System.out.println("Attachment Info Count: " + info.getCount().toString());
//            }
//            if (info.getPage() != null)
//            {
//              System.out.println("Attachment Info Page: " + info.getPage().toString());
//            }
//            if (info.getMoreRecords() != null)
//            {
//              System.out.println("Record Info MoreRecords: " + info.getMoreRecords().toString());
//            }
//          }
        }
        else if (responseHandler instanceof APIException)
        {
          APIException exception = (APIException) responseHandler;
          StringBuilder br = new StringBuilder();
          br.append(" Status: ").append(exception.getStatus().getValue());
          br.append(" Code: " + exception.getCode().getValue());
          br.append(" Details: ");
          for (Map.Entry<String, Object> entry : exception.getDetails().entrySet())
          {
            br.append(" " +entry.getKey() + ": " + entry.getValue());
          }
          br.append(" Message: " + exception.getMessage());
         LOG.error(br.toString());
        }

      }
      else
      {
        Model responseObject = response.getModel();
        Class<? extends Model> clas = responseObject.getClass();
        java.lang.reflect.Field[] fields = clas.getDeclaredFields();
        for (java.lang.reflect.Field field : fields)
        {
          field.setAccessible(true);
          LOG.error(field.getName() + ":" + field.get(responseObject));
        }
      }
    }

    return attachmentIds;
  }
  public  int downloadAttachment(String moduleAPIName, Long recordId, Long attachmentId,Long newRecordId, String destinationFolder) throws Exception {
    AttachmentsOperations attachmentOperations = new AttachmentsOperations();
    APIResponse<ResponseHandler> response = attachmentOperations.getAttachment(attachmentId,
        recordId, moduleAPIName);
    if (response != null) {
      if (response.getStatusCode() == 204) {
        LOG.info("Record : "+ recordId +" Attachment ID " + attachmentId + " No Content");
        return response.getStatusCode();
      }
      if (response.isExpected()) {
        ResponseHandler responseHandler = response.getObject();
        if (responseHandler instanceof FileBodyWrapper) {

          FileBodyWrapper fileBodyWrapper = (FileBodyWrapper) responseHandler;
          StreamWrapper streamWrapper = fileBodyWrapper.getFile();


          //String filename  =streamWrapper.getName();
         String filename  = URLDecoder.decode(streamWrapper.getName(), "UTF-8");

          File dir = new File(destinationFolder + File.separatorChar +newRecordId);
          if (!dir.exists()) dir.mkdirs();

          File file = new File(destinationFolder + File.separatorChar +newRecordId+File.separatorChar+filename);

          InputStream is = streamWrapper.getStream();
          OutputStream os = new FileOutputStream(file);
          byte[] buffer = new byte[1024];
          int bytesRead;
          while ((bytesRead = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
          }
          is.close();
          os.flush();
          os.close();

          LOG.info("Downloaded successfully! File : "+ file.getName()+" Record : "+ newRecordId +" Attachment ID :" + attachmentId + " ");

        } else if (responseHandler instanceof APIException) {
          APIException exception = (APIException) responseHandler;

          StringBuilder br = new StringBuilder();

          br.append(" Status: ").append(exception.getStatus().getValue());
          br.append(" Code: " + exception.getCode().getValue());
          br.append(" Details: ");
          for (Map.Entry<String, Object> entry : exception.getDetails().entrySet()) {
            br.append(" " + entry.getKey() + ": " + entry.getValue());
          }
          br.append(" Message: " + exception.getMessage());

          LOG.error(br.toString());
        }
      } else {
        Model responseObject = response.getModel();
        Class<? extends Model> clas = responseObject.getClass();
        java.lang.reflect.Field[] fields = clas.getDeclaredFields();
        for (java.lang.reflect.Field field : fields) {
          field.setAccessible(true);
          LOG.error(field.getName() + ":" + field.get(responseObject));
        }
      }

      return response.getStatusCode();
    }
    return 0;
  }


}
