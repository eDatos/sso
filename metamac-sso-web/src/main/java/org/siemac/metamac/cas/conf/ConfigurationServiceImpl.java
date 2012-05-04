package org.siemac.metamac.cas.conf;

import java.util.Properties;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationBuilder;
import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.SystemConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ConfigurationServiceImpl implements InitializingBean, ConfigurationService {

    private Resource             systemConfigurationFile = null;
    private Resource             configurationFile       = null;
    private ConfigurationBuilder factory                 = null;
    private Configuration        configuration           = null;

    public void setSystemConfigurationFile(Resource systemaConfigurationFile) {
        this.systemConfigurationFile = systemaConfigurationFile;
    }

    public void afterPropertiesSet() throws Exception {
        if (systemConfigurationFile == null) {
            throw new IllegalArgumentException("Error no se ha inicializado la propiedad \"systemConfigurationFile\" correctamente");
        }
        if (configurationFile == null) {
            configurationFile = new ClassPathResource("conf/config.xml");
        }
        init();
    }

    private void init() {
        try {
            SystemConfiguration.setSystemProperties(systemConfigurationFile.getFile().getAbsolutePath());
            factory = new DefaultConfigurationBuilder(configurationFile.getFile());

            CombinedConfiguration conf = (CombinedConfiguration) factory.getConfiguration();
            conf.setForceReloadCheck(true);
            configuration = conf;
        } catch (Throwable e) {
            throw new IllegalStateException("No se ha podido configurar correctamente las propiedades", e);
        }
    }

    public Configuration getConfig() {
        return configuration;
    }

    public Properties getProperties() {
        return ConfigurationConverter.getProperties(getConfig());
    }
}
