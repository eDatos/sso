package org.siemac.metamac.cas.conf;

import java.util.Properties;

import org.apache.commons.configuration.Configuration;

public interface ConfigurationService {

    Configuration getConfig();

    Properties getProperties();

}