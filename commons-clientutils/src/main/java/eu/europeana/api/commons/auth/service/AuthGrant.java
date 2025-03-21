package eu.europeana.api.commons.auth.service;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.StringEntity;

import static eu.europeana.api.commons.auth.service.GrantConstants.*;

/**
 * @author Hugo
 * @since 11 Mar 2025
 */
public class AuthGrant extends Properties {

    private static final long serialVersionUID = 1L;

    public AuthGrant(Properties args) {
        super(args);
        this.keySet().retainAll(params);
    }

    public AuthGrant(String args) {
        parse(args);
        this.keySet().retainAll(params);
    }

    public HttpEntity getEntity() {
        StringBuilder sb = new StringBuilder();
        for ( Object key : keySet() ) {
            Object value = get(key);
            if ( value == null ) { continue; }

            String str = value.toString();
            if ( StringUtils.isBlank(str) ) { continue; }

            if ( !sb.isEmpty() ) { sb.append("&"); }
            sb.append(key.toString()).append("=").append(str);
        }
        return new StringEntity(sb.toString()
                              , ContentType.APPLICATION_FORM_URLENCODED);
    }

    public AuthGrant newRefreshGrant(String refreshToken) {
        Properties props = new Properties(this);
        props.put(grant_type, GrantType.refresh_token);
        props.put(refresh_token, refreshToken);
        return new AuthGrant(props);
    }

    private void parse(String args) {
        if ( args.contains("&") ) {
            for ( String line : args.split("&") ) {
                String[] arg = line.split("=");
                if ( arg.length != 2) { continue; }
    
                setProperty(arg[0], arg[1]);
            }
            return;
        }
        
        if ( args.contains("\n") ) {
            for ( String line : args.split("\n") ) {
                String[] arg = line.split(":");
                if ( arg.length != 2) { continue; }
    
                setProperty(arg[0], arg[1]);
            }
        }
    }
}
