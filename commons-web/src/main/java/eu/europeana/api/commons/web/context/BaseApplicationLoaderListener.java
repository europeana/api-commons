package eu.europeana.api.commons.web.context;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;

import eu.europeana.api.commons.net.socks.SocksProxyConfig;

public abstract class BaseApplicationLoaderListener {

	public static final String SOCKS_PROXY_URL = "socks.proxy.url";
	Properties props = new Properties();
	Logger logger = LogManager.getLogger(getClass());
	
	public BaseApplicationLoaderListener(){
		System.out.println("instantiation of ApplicationLoaderListener");
		loadProperties();		
	}
	
	public Logger getLogger() {
		return logger;
	}

	protected Properties getProps() {
		return props;
	}
	
	protected void registerSocksProxy(){
		String socksProxyUrl = getProps().getProperty(SOCKS_PROXY_URL);
		if(StringUtils.isEmpty(socksProxyUrl))
			return;
		try {
			SocksProxyConfig socksProxy;
			socksProxy = new SocksProxyConfig(socksProxyUrl);
			socksProxy.init();
		} catch (URISyntaxException e) {
			logger.error("Cannot register socks proxy. The application might not be correctly initialized! URL: " + socksProxyUrl, e);
		}
		
	}

	protected void loadProperties(){
		String propsLocation = getAppConfigFile();
		try {
			EncodedResource propsResource = new EncodedResource( new ClassPathResource(propsLocation), "UTF-8");
			PropertiesLoaderUtils.fillProperties(getProps(), propsResource);
		} catch (IOException e) {
			logger.warn("Cannot read properties from classath: " + propsLocation, e);		
		}
	}

	protected abstract String getAppConfigFile();
}
