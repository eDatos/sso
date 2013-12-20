package org.siemac.metamac.cas.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.configuration.CombinedConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationBuilder;
import org.apache.commons.configuration.ConfigurationConverter;
import org.apache.commons.configuration.DatabaseConfiguration;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.configuration.XMLPropertiesConfiguration;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ConfigurationServiceImpl implements InitializingBean, ConfigurationService {

    private static final Logger     LOG                                = LoggerFactory.getLogger(ConfigurationServiceImpl.class);

    private static final String     TABLE_CONFIGURATIONS_NAME          = "TB_DATA_CONFIGURATIONS";
    private static final String     TABLE_CONFIGURATIONS_COLUMN_KEY    = "CONF_KEY";
    private static final String     TABLE_CONFIGURATIONS_COLUMN_VALUE  = "CONF_VALUE";

    private static final String     ENVIROMENT_CONF_KEY_DATA           = "environment.metamac.data";
    private static final String     ENVIROMENT_CONF_KEY_DB_DRIVER_NAME = "environment.metamac.configuration.db.driver_name";
    private static final String     ENVIROMENT_CONF_KEY_DB_URL         = "environment.metamac.configuration.db.url";
    private static final String     ENVIROMENT_CONF_KEY_DB_USERNAME    = "environment.metamac.configuration.db.username";
    private static final String     ENVIROMENT_CONF_KEY_DB_PASSWORD    = "environment.metamac.configuration.db.password";

    // Configurations
    private Configuration           configuration                      = null;
    private PropertiesConfiguration environmentConfigurationProperties = null;

    // COnfigurations in Files
    private Resource                systemConfigurationFile            = null;
    private Resource                configurationFile                  = null;
    private ConfigurationBuilder    factory                            = null;

    // Configurations in Database
    private DatabaseConfiguration   databaseConfiguration              = null;
    private DataSource              configurationsDatasource           = null;

    public void setSystemConfigurationFile(Resource systemaConfigurationFile) {
        this.systemConfigurationFile = systemaConfigurationFile;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // Check environment properties
        if (systemConfigurationFile == null) {
            throw new IllegalArgumentException("ERROR The property \"systemConfigurationFile\" has not been properly initialized");
        }

        try {
            // For working with continuous integration, version control and local development, if exist a Local configuration, it overwrite the default configuration.
            InputStream is = retrieveLocalConfiguration();
            if (is == null) {
                is = systemConfigurationFile.getInputStream();
            }

            PropertiesConfiguration propertiesConfiguration = new XMLPropertiesConfiguration();
            propertiesConfiguration.load(is);
            environmentConfigurationProperties = propertiesConfiguration;
            SystemConfiguration.setSystemProperties(propertiesConfiguration);
        } catch (Throwable e) {
            throw new IllegalStateException("Properties have not been configured", e);
        }

        if (environmentConfigurationProperties.getProperty(ENVIROMENT_CONF_KEY_DB_DRIVER_NAME) != null && environmentConfigurationProperties.getProperty(ENVIROMENT_CONF_KEY_DB_USERNAME) != null
                && environmentConfigurationProperties.getProperty(ENVIROMENT_CONF_KEY_DB_PASSWORD) != null && environmentConfigurationProperties.getProperty(ENVIROMENT_CONF_KEY_DB_URL) != null) {
            initConfigurationInDatabase();
        } else if (environmentConfigurationProperties.getProperty(ENVIROMENT_CONF_KEY_DATA) != null) {
            initConfigurationInFiles();
        } else {
            throw new IllegalArgumentException("ERROR One of configuration source is required: \"systemConfigurationFile\" or \"configurationDatasource\", has not been properly initialized");
        }
    }

    @Override
    public Configuration getConfig() {
        return configuration;
    }

    @Override
    public Properties getProperties() {
        return ConfigurationConverter.getProperties(getConfig());
    }

    private void initConfigurationInFiles() {
        LOG.info("Loading configuration properties from files");
        try {
            if (configurationFile == null) {
                configurationFile = new ClassPathResource("conf/config.xml");
            }

            factory = new DefaultConfigurationBuilder(configurationFile.getFile());

            CombinedConfiguration conf = (CombinedConfiguration) factory.getConfiguration();
            conf.setForceReloadCheck(true);
            configuration = conf;
        } catch (Throwable e) {
            throw new IllegalStateException("Properties have not been configured", e);
        }
    }

    private void initConfigurationInDatabase() {
        LOG.info("Loading configuration properties from database");
        try {
            // Setup Datasource
            configurationsDatasource = new BasicDataSource();
            ((BasicDataSource) configurationsDatasource).setDriverClassName((String) environmentConfigurationProperties.getProperty(ENVIROMENT_CONF_KEY_DB_DRIVER_NAME));
            ((BasicDataSource) configurationsDatasource).setUsername((String) environmentConfigurationProperties.getProperty(ENVIROMENT_CONF_KEY_DB_USERNAME));
            ((BasicDataSource) configurationsDatasource).setPassword((String) environmentConfigurationProperties.getProperty(ENVIROMENT_CONF_KEY_DB_PASSWORD));
            ((BasicDataSource) configurationsDatasource).setUrl((String) environmentConfigurationProperties.getProperty(ENVIROMENT_CONF_KEY_DB_URL));

            databaseConfiguration = new DatabaseConfiguration(configurationsDatasource, TABLE_CONFIGURATIONS_NAME, TABLE_CONFIGURATIONS_COLUMN_KEY, TABLE_CONFIGURATIONS_COLUMN_VALUE);
            databaseConfiguration.isEmpty();
            configuration = databaseConfiguration;
        } catch (Throwable e) {
            throw new IllegalStateException("Properties have not been configured", e);
        }
    }

    private InputStream retrieveLocalConfiguration() {
        try {
            String path = StringUtils.replace(systemConfigurationFile.getURI().toString(), ".xml", "-LOCAL.xml");
            File file = new File(new URI(path));
            if (file.exists()) {
                return new FileInputStream(file);
            }
        } catch (Throwable e) {
        }
        return null;
    }

}
