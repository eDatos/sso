package org.siemac.metamac.sso.validation;

import java.util.ArrayList;
import java.util.Collection;

import javax.net.ssl.HostnameVerifier;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Saml11TicketValidator;
import org.siemac.metamac.sso.client.MetamacPrincipal;
import org.siemac.metamac.sso.client.MetamacPrincipalAccess;
import org.siemac.metamac.sso.exception.TicketValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateTicket {

    private static final Logger LOG                          = LoggerFactory.getLogger(ValidateTicket.class);
    private static final String PROP_ATTRIBUTE_ACL           = "acl";
    private static final String PROP_ATTRIBUTE_ACL_SEPARATOR = "#";

    private String              serverUrlPrefix              = null;

    // tolerance in ms
    private long                tolerance                    = 10000L;
    private boolean             renew                        = false;
    private String              encoding                     = null;

    public ValidateTicket(String serverUrlPrefix) {
        this.serverUrlPrefix = serverUrlPrefix;
    }

    public void setTolerance(long tolerance) {
        this.tolerance = tolerance;
    }

    public long getTolerance() {
        return tolerance;
    }

    public void setRenew(boolean renew) {
        this.renew = renew;
    }

    public boolean isRenew() {
        return renew;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding() {
        return encoding;
    }

    @SuppressWarnings("unchecked")
    public MetamacPrincipal validateTicket(String ticket, String service) throws TicketValidationException {

        HostnameVerifier hostnameVerifier = null;
        final Saml11TicketValidator ticketValidator = new Saml11TicketValidator(this.serverUrlPrefix);
        ticketValidator.setTolerance(this.tolerance);
        ticketValidator.setRenew(this.renew);
        ticketValidator.setHostnameVerifier(hostnameVerifier);
        ticketValidator.setEncoding(this.encoding);

        try {
            final Assertion assertion = ticketValidator.validate(ticket, service);

            if (LOG.isDebugEnabled()) {
                LOG.debug("Successfully authenticated user: " + assertion.getPrincipal().getName());
            }

            AttributePrincipal attributePrincipal = assertion.getPrincipal();
            Object object = attributePrincipal.getAttributes().get(ValidateTicket.PROP_ATTRIBUTE_ACL);
            Collection<String> acls = null;
            if (object != null) {
                if (object instanceof Collection<?>) {
                    acls = (Collection<String>) object;
                } else {
                    acls = new ArrayList<String>();
                    acls.add(object.toString());
                }
            }

            MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
            metamacPrincipal.setUserId(assertion.getPrincipal().getName());
            if (acls != null) {
                for (String acl : acls) {
                    String[] aclParts = acl.split(ValidateTicket.PROP_ATTRIBUTE_ACL_SEPARATOR);
                    if (aclParts.length != 2 && aclParts.length != 3) {
                        LOG.warn("Ignoring ACL {} because the size is incorrect", acl);
                        continue;
                    }
                    String application = aclParts[0];
                    String role = aclParts[1];
                    String operation = null;
                    if (aclParts.length == 3) {
                        operation = aclParts[2];
                        if (operation.length() == 0) {
                            operation = null;
                        }
                    }
                    metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(role, application, operation));
                }
            }
            return metamacPrincipal;
        } catch (final org.jasig.cas.client.validation.TicketValidationException e) {
            throw new TicketValidationException(e);
        }
    }

}
