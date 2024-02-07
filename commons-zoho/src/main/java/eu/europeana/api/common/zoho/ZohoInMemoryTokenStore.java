package eu.europeana.api.common.zoho;

import com.zoho.api.authenticator.OAuthToken;
import com.zoho.api.authenticator.Token;
import com.zoho.api.authenticator.store.TokenStore;
import com.zoho.crm.api.exception.SDKException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ZohoInMemoryTokenStore implements TokenStore {

  private final Map<String, Token> tokenStore = new ConcurrentHashMap<>();

  @Override
  public Token findToken(Token token) throws SDKException {

    if(token instanceof OAuthToken){
      return tokenStore.get(((OAuthToken)token).getUserSignature().getName());
    }
    return null;
  }

  @Override
  public Token findTokenById(String s) throws SDKException {
    return tokenStore.get(s);
  }

  @Override
  public void saveToken(Token token) throws SDKException {
    if(token instanceof OAuthToken) {
      tokenStore.put(((OAuthToken)token).getUserSignature().getName(), token);
    }
  }
  @Override
  public void deleteToken(String s) throws SDKException {

  }

  @Override
  public List<Token> getTokens() throws SDKException {
    return new ArrayList<>(tokenStore.values());
  }

  @Override
  public void deleteTokens() throws SDKException {
    tokenStore.clear();
  }
}
