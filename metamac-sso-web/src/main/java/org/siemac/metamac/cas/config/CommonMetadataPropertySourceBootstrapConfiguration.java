package org.siemac.metamac.cas.config;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.conf.ConfigurationServiceImpl;
import org.siemac.metamac.core.common.util.MetamacEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class CommonMetadataPropertySourceBootstrapConfiguration implements PropertySourceLocator {

    final static Logger LOGGER = LoggerFactory.getLogger(CommonMetadataPropertySourceBootstrapConfiguration.class);

    @Bean("configurationService")
    public ConfigurationServiceImpl configurationService() {
        ConfigurationServiceImpl configurationServiceImpl = new ConfigurationServiceImpl();
        configurationServiceImpl.setSystemConfigurationFile(new ClassPathResource("metamac/environment.xml"));
        return configurationServiceImpl;
    }

    @Override
    public PropertySource<?> locate(Environment environment) {
        Properties decodedProperties = decode(configurationService().getProperties());
        return new PropertiesPropertySource(getClass().getSimpleName(), decodedProperties);
    }

    private Properties decode(Properties properties) {
        Properties decodeProperties = new Properties();
        for (String key : properties.stringPropertyNames()) {
            if (key.endsWith(".password")) {
                try {
                    decodeProperties.setProperty(key, MetamacEncryptor.decrypt(properties.getProperty(key)));
                } catch (Exception e) {
                    LOGGER.error("An error has ocurred decrypting password property: " + key, e);
                    decodeProperties.setProperty(key, StringUtils.EMPTY);
                }
            } else {
                decodeProperties.setProperty(key, properties.getProperty(key));
            }
        }
        return decodeProperties;
    }

}
