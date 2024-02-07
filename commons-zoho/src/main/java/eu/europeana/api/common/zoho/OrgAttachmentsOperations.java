package eu.europeana.api.common.zoho;

import com.zoho.crm.api.attachments.ActionHandler;
import com.zoho.crm.api.attachments.AttachmentsOperations;
import com.zoho.crm.api.attachments.FileBodyWrapper;
import com.zoho.crm.api.exception.SDKException;
import com.zoho.crm.api.util.APIResponse;
import com.zoho.crm.api.util.CommonAPIHandler;

public class OrgAttachmentsOperations extends AttachmentsOperations {

  @Override
  public APIResponse<ActionHandler> createAttachment(Long recordId, String module, FileBodyWrapper request) throws SDKException {
    CommonAPIHandler handlerInstance = new CommonAPIHandler();
    String apiPath = new String();
    apiPath = apiPath.concat("/crm/v5/");
    apiPath = apiPath.concat(module.toString());
    apiPath = apiPath.concat("/");
    apiPath = apiPath.concat(recordId.toString());
    apiPath = apiPath.concat("/Attachments");
    handlerInstance.setAPIPath(apiPath);
    handlerInstance.setHttpMethod("POST");
    handlerInstance.setCategoryMethod("CREATE");
    handlerInstance.setContentType("multipart/form-data");
    handlerInstance.setRequest(request);
    handlerInstance.setMandatoryChecker(true);
    return handlerInstance.apiCall(ActionHandler.class, "multipart/form-data");
  }

}
