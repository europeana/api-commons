package eu.europeana.api.commons.oauth2.model;

import java.util.Date;

/**
 * @author Willem-Jan Boogerd www.eledge.net
 */
public interface ApiKey{

    void setApiKey(String apiKey);

    String getApiKey();

    String getPrivateKey();

    void setPrivateKey(String privateKey);

    String getApplicationName();

    void setApplicationName(String applicationName);

    Long getUsageLimit();

    void setUsageLimit(Long usageLimit);

    String getEmail();

    void setEmail(String email);

    String getCompany();

    void setCompany(String company);

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    Date getRegistrationDate();

    String getDescription();

    void setDescription(String description);

    String getWebsite();

    void setWebsite(String website);

    Date getActivationDate();

    void setActivationDate(Date activationDate);
}