package org.siemac.metamac.cas.conf;

import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class ConfigurationServicePropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements InitializingBean {

    private Properties           properties           = null;
    private ConfigurationService configurationService = null;

    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public void afterPropertiesSet() throws Exception {
        if (configurationService == null) {
            throw new IllegalArgumentException("Falta la propiedad \"configurationService\"");
        }

        properties = configurationService.getProperties();
        setProperties(properties);
    }

    public Properties getProperties() {
        return properties;
    }

}