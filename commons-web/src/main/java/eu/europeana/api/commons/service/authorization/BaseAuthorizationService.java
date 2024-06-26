package eu.europeana.api.commons.service.authorization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import eu.europeana.api.commons.definitions.config.i18n.I18nConstants;
import eu.europeana.api.commons.definitions.exception.ApiWriteLockException;
import eu.europeana.api.commons.definitions.vocabulary.Role;
import eu.europeana.api.commons.exception.ApiKeyExtractionException;
import eu.europeana.api.commons.exception.AuthorizationExtractionException;
import eu.europeana.api.commons.nosql.entity.ApiWriteLock;
import eu.europeana.api.commons.nosql.service.ApiWriteLockService;
import eu.europeana.api.commons.oauth2.utils.OAuthUtils;
import eu.europeana.api.commons.web.exception.ApplicationAuthenticationException;
import eu.europeana.api.commons.web.model.vocabulary.Operations;

public abstract class BaseAuthorizationService implements AuthorizationService {

  RsaVerifier signatureVerifier;
  private Logger log = LogManager.getLogger(getClass());

  public Logger getLog() {
    return log;
  }

  protected RsaVerifier getSignatureVerifier() {
    if (signatureVerifier == null) {
      signatureVerifier = new RsaVerifier(getSignatureKey());
    }
    return signatureVerifier;
  }

  @Override
  /**
   *
   */
  public Authentication authorizeReadAccess(HttpServletRequest request)
      throws ApplicationAuthenticationException {
    Authentication authentication = null;
    // check and verify jwt token
    String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authorization != null && authorization.startsWith(OAuthUtils.TYPE_BEARER)) {
      // if jwt token submitted
      authentication = authorizeReadByJwtToken(request);
    } else {
      // user id not available, verify apiKey only
      authentication = authorizeReadByApiKey(request);
    }
    return authentication;
  }

  private Authentication authorizeReadByApiKey(HttpServletRequest request)
      throws ApplicationAuthenticationException {
    String wsKey;
    // extract api key with other methods
    try {
      wsKey = OAuthUtils.extractApiKey(request);
    } catch (ApiKeyExtractionException | AuthorizationExtractionException e) {
      throw new ApplicationAuthenticationException(I18nConstants.INVALID_APIKEY,
          I18nConstants.INVALID_APIKEY, new String[] {e.getMessage()}, HttpStatus.UNAUTHORIZED, e);
    }

    // check if empty
    if (StringUtils.isEmpty(wsKey))
      throw new ApplicationAuthenticationException(I18nConstants.EMPTY_APIKEY,
          I18nConstants.EMPTY_APIKEY, null);

    // validate api key
    try {
      getClientDetailsService().loadClientByClientId(wsKey);
    } catch (ClientRegistrationException e) {
      // invalid api key
      throw new ApplicationAuthenticationException(I18nConstants.INVALID_APIKEY,
          I18nConstants.INVALID_APIKEY, new String[] {wsKey}, HttpStatus.UNAUTHORIZED, e);
    } catch (OAuth2Exception e) {
      // validation failed through API Key service issues
      // silently approve request
      getLog().info("Invocation of API Key Service failed. Silently approve apikey: " + wsKey, e);
    }

    // anonymous user, only the client application is verified by API key
    return OAuthUtils.buildReadOnlyAuthenticationToken(getApiName(), wsKey);
  }

  private Authentication authorizeReadByJwtToken(HttpServletRequest request)
      throws ApplicationAuthenticationException {
    Authentication authentication = null;
    try {
      // String jwtToken = OAuthUtils.extractPayloadFromAuthorizationHeader(request, OAuthUtils.);
      Map<String, Object> data =
          OAuthUtils.extractCustomData(request, getSignatureVerifier(), getApiName());
      String wsKey = OAuthUtils.extractApiKey(data);

      // check if null
      if (wsKey == null)
        throw new ApplicationAuthenticationException(I18nConstants.MISSING_APIKEY,
            I18nConstants.MISSING_APIKEY, null, HttpStatus.UNAUTHORIZED, null);

      if (data.containsKey(OAuthUtils.USER_ID)) {
        // read access is provided to any authenticated user
        List<Authentication> authList = new ArrayList<Authentication>();
        //for read acccess the resource access is not mandatory
        OAuthUtils.processResourceAccessClaims(getApiName(), data, authList, mustVerifyResourceAccessForRead());
        if (!authList.isEmpty()) {
          authentication = authList.get(0);
        } else {
          // for backward compatibility, we allow read access to users that don't have a token
          // created specifically for current API
          // TODO: in the future we might still want to verify the scope of the JWT token.
          authentication = OAuthUtils.buildReadOnlyAuthenticationToken(getApiName(), data,wsKey);
        }
      }
    } catch (ApiKeyExtractionException | AuthorizationExtractionException e) {
      throw new ApplicationAuthenticationException(I18nConstants.INVALID_JWTTOKEN,
          I18nConstants.INVALID_JWTTOKEN, new String[] {e.getMessage()}, HttpStatus.UNAUTHORIZED,
          e);
    }

    return authentication;
  }
  
  /*
   * (non-Javadoc)
   *
   * @see eu.europeana.api.commons.service.authorization.AuthorizationService#
   * authorizeWriteAccess(javax.servlet.http.HttpServletRequest, java.lang.String)
   */
  public Authentication authorizeWriteAccess(HttpServletRequest request, String operation)
      throws ApplicationAuthenticationException {

    return authorizeOperation(request, operation);
  }

  private Authentication authorizeOperation(HttpServletRequest request, String operation)
      throws ApplicationAuthenticationException {

    //invalid configurations
    if (getSignatureVerifier() == null) {
      throw new ApplicationAuthenticationException(I18nConstants.OPERATION_NOT_AUTHORIZED,
          I18nConstants.OPERATION_NOT_AUTHORIZED,
          new String[] {"No signature key configured for verification of JWT Token"},
          HttpStatus.INTERNAL_SERVER_ERROR);
    }

    List<? extends Authentication> authenticationList;
    boolean verifyResourceAccess = isResourceAccessVerificationRequired(operation);
    try {
      //parses and validates the jwt token
      authenticationList =
          OAuthUtils.processJwtToken(request, getSignatureVerifier(), getApiName(), verifyResourceAccess);
    } catch (ApiKeyExtractionException | AuthorizationExtractionException e) {
      throw new ApplicationAuthenticationException(I18nConstants.OPERATION_NOT_AUTHORIZED,
          I18nConstants.OPERATION_NOT_AUTHORIZED, new String[] {"Invalid token or ApiKey"},
          HttpStatus.UNAUTHORIZED, e);
    }

    if(authenticationList == null || authenticationList.isEmpty()) {
      throw new ApplicationAuthenticationException(I18nConstants.OPERATION_NOT_AUTHORIZED,
          I18nConstants.OPERATION_NOT_AUTHORIZED, new String[] {"Invalid token or ApiKey, resource access not granted!"},
          HttpStatus.FORBIDDEN);
    }

    if(verifyResourceAccess) {
      //verify permissions
      return checkPermissions(authenticationList, getApiName(), operation);
    } else {
      //return authenticated user and client
      return authenticationList.get(0);
    }
  }


  /**
   * This method verifies write access rights for particular api and operation
   *
   * @param authenticationList The list of authentications extracted from the JWT token
   * @param api The name of the called api
   * @param operation The name of called api operation
   * @throws ApplicationAuthenticationException if the access to the api operation is not authorized
   */
  @SuppressWarnings("unchecked")
  protected Authentication checkPermissions(List<? extends Authentication> authenticationList,
      String api, String operation) throws ApplicationAuthenticationException {

    final boolean isEmptyAuthenticationList = (authenticationList == null || authenticationList.isEmpty());

    if(isEmptyAuthenticationList) {

      if(isResourceAccessVerificationRequired(operation)){
        //access verification required but
        throw new ApplicationAuthenticationException(I18nConstants.OPERATION_NOT_AUTHORIZED,
            I18nConstants.OPERATION_NOT_AUTHORIZED,
            new String[] {"No or invalid authorization provided"}, HttpStatus.FORBIDDEN);
      } else {
        //TODO:
        return null;
      }


    }




    if (authenticationList == null || authenticationList.isEmpty()) {

    }

    List<GrantedAuthority> authorityList;

    for (Authentication authentication : authenticationList) {

      authorityList = (List<GrantedAuthority>) authentication.getAuthorities();

      if (api.equals(authentication.getDetails())
          && isOperationAuthorized(operation, authorityList)) {
        // access granted
        return authentication;
      }
    }

    // not authorized
    throw new ApplicationAuthenticationException(I18nConstants.OPERATION_NOT_AUTHORIZED,
        I18nConstants.OPERATION_NOT_AUTHORIZED,
        new String[] {
            "Operation not permitted or not GrantedAuthority found for operation:" + operation},
        HttpStatus.FORBIDDEN);
  }


  public Authentication checkPermissions(Authentication authentication, String operation)
      throws ApplicationAuthenticationException {
    return checkPermissions(List.of(authentication), getApiName(), operation);
  }


  /**
   * This method performs checking, whether an operation is supported for provided authorities
   *
   * @param operation the called api operation
   * @param authorities the list of granted authorities (as provided through the JWT token)
   * @return true if operation authorized
   */
  private boolean isOperationAuthorized(String operation, List<GrantedAuthority> authorities) {

    if(!isResourceAccessVerificationRequired(operation)) {
      return true;
    }

    Role userRole;
    List<String> allowedOperations;

    for (GrantedAuthority authority : authorities) {
      userRole = getRoleByName(authority.getAuthority());
      if (userRole == null) {
        continue;
      }
      allowedOperations = Arrays.asList(userRole.getPermissions());
      if (allowedOperations.contains(operation)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Check if a write lock is in effect. Returns HttpStatus.LOCKED in case the write lock is active.
   * To be used for preventing access to the write operations when the application is locked Needs
   * to be called explicitly in the verifyWriteAccess methods of individual apis
   *
   *
   * @param operationName
   * @throws ApplicationAuthenticationException
   */
  public void checkWriteLockInEffect(String operationName)
      throws ApplicationAuthenticationException {
    ApiWriteLock activeWriteLock;
    try {
      activeWriteLock = getApiWriteLockService().getLastActiveLock(ApiWriteLock.LOCK_WRITE_TYPE);
      // refuse operation if a write lock is effective (allow only unlock and retrieve
      // operations)
      if (activeWriteLock == null) {
        // the application is not locked
        return;
      }

      if (!isMaintenanceOperation(operationName)) {
        // unlock operation should be permitted when the application is locked
        // activeWriteLock.getEnded()==null
        throw new ApplicationAuthenticationException(I18nConstants.LOCKED_MAINTENANCE,
            I18nConstants.LOCKED_MAINTENANCE, null, HttpStatus.LOCKED, null);
      }
    } catch (ApiWriteLockException e) {
      throw new ApplicationAuthenticationException(I18nConstants.LOCKED_MAINTENANCE,
          I18nConstants.LOCKED_MAINTENANCE, null, HttpStatus.LOCKED, e);
    }
  }

  /**
   * Indicates if the given operation is allowed when locked for maintenance. Basically
   *
   * @param operationName
   * @return
   */
  protected boolean isMaintenanceOperation(String operationName) {
    return getMaintenanceOperations().contains(operationName);
  }

  /**
   * Indicate if the resource access needs to be verified for read operations. This indicates if the resourceAccess is available in jwt tokens used for the current API
   * Default is true.
   * @return true if the resourceAceess field needs to be processed for read access
   */
  protected  boolean mustVerifyResourceAccessForRead() {
    return true;
  }

  /**
   * Method to indicate if the resource access (i.e. user has the role which grants permissions for the operation) is required.
   * Client authentication is mandatory, but apis might grant access to all users if the token is valid
   * Api should overwrite this method in order to disable resource access verification
   *
   * @return true if the resource access needs to be verified
   */
  protected boolean isResourceAccessVerificationRequired(String operation) {
    return true;
  }
  
  /**
   * Returns the list of
   *
   * @return
   */
  protected Set<String> getMaintenanceOperations() {
    return Set.of(Operations.WRITE_UNLOCK);
  }

  protected abstract ApiWriteLockService getApiWriteLockService();

  /**
   * This method returns the api specific Role for the given role name
   *
   * @param name the name of user role
   * @return the user role
   */
  protected abstract Role getRoleByName(String name);

  /**
   *
   * @return key used to verify the JWT token signature
   */
  protected abstract String getSignatureKey();

  /**
   *
   * @return the service providing the client details
   */
  protected abstract ClientDetailsService getClientDetailsService();

  /**
   *
   * @return the name of the API calling the authorization service
   */
  protected abstract String getApiName();

}