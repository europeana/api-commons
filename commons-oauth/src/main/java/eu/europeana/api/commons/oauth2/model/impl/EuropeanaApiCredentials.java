package eu.europeana.api.commons.oauth2.model.impl;

import eu.europeana.api.commons.oauth2.model.ApiCredentials;

public class EuropeanaApiCredentials implements ApiCredentials{

    public static final String USER_ANONYMOUS = "annonymous";
    
    private String userName;

    public EuropeanaApiCredentials(String userName) {
	this.userName = userName;
    }
    
    @Override
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
