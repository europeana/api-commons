package eu.europeana.api.commons.definitions.vocabulary;

/**
 * This is an interface for user roles enumeration
 * 
 * @author GrafR
 *
 */
public interface Role {
    
    
    /**
     * @return the name of the role
     */
    public String getName();
    
    /**
     * @return the permissions for given role
     */
    public String[] getPermissions();

}

