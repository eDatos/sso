package org.siemac.metamac.sso.utils;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.CommonServiceExceptionType;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.SsoClientConstants;

public class SecurityUtils {

    /**
     * Retrieves MetamacPrincipal in ServiceContext
     */
    public static MetamacPrincipal getMetamacPrincipal(ServiceContext ctx) throws MetamacException {
        Object principalProperty = ctx.getProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE);
        if (principalProperty == null) {
            throw new MetamacException(CommonServiceExceptionType.SECURITY_PRINCIPAL_NOT_FOUND);
        }
        MetamacPrincipal metamacPrincipal = (MetamacPrincipal) principalProperty;
        if (!metamacPrincipal.getUserId().equals(ctx.getUserId())) {
            throw new MetamacException(CommonServiceExceptionType.SECURITY_PRINCIPAL_NOT_FOUND);
        }
        return metamacPrincipal;
    }
    
    
    /**
     * Throw an expection if operation not allowed
     */
    public static void throwExceptionIfOperationNotAllowed(ServiceContext ctx) throws MetamacException {
        throw new MetamacException(CommonServiceExceptionType.SECURITY_OPERATION_NOT_ALLOWED, ctx.getUserId());
    }

}
