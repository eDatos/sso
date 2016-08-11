package org.siemac.metamac.cas.authentication.principal;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.listener.ApplicationStartupListener;

public class ApplicationStartup extends ApplicationStartupListener {

    @Override
    public String projectName() {
        return "authentication-service-internal";
    }

    @Override
    public void checkApplicationProperties() throws MetamacException {
        checkRequiredProperty(METAMAC_ORGANISATION);
    }
}
