package org.siemac.metamac.sso.validation;

import java.util.List;

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

    private static Logger log                          = LoggerFactory.getLogger(ValidateTicket.class);
    private static String PROP_ATTRIBUTE_ACL           = "acl";
    private static String PROP_ATTRIBUTE_ACL_SEPARATOR = "#";

    private String        serverUrlPrefix              = null;

    private long          tolerance                    = 10000L;                                       // ms
    private boolean       renew                        = false;
    private String        encoding                     = null;

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

    public MetamacPrincipal validateTicket(String ticket, String service) throws TicketValidationException {

        // String disableXmlSchemaValidation = "false";
        HostnameVerifier hostnameVerifier = null; // getHostnameVerifier(filterConfig)
        final Saml11TicketValidator ticketValidator = new Saml11TicketValidator(this.serverUrlPrefix);
        ticketValidator.setTolerance(this.tolerance);
        ticketValidator.setRenew(this.renew);
        ticketValidator.setHostnameVerifier(hostnameVerifier);
        ticketValidator.setEncoding(this.encoding);

        try {
            final Assertion assertion = ticketValidator.validate(ticket, service);

            if (log.isDebugEnabled()) {
                log.debug("Successfully authenticated user: " + assertion.getPrincipal().getName());
            }

            AttributePrincipal attributePrincipal = assertion.getPrincipal();
            @SuppressWarnings("unchecked")
            List<String> acls = (List<String>) attributePrincipal.getAttributes().get(ValidateTicket.PROP_ATTRIBUTE_ACL); // TODO

            MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
            metamacPrincipal.setUserId(assertion.getPrincipal().getName());
            if (acls != null) {
                for (String acl : acls) {
                    String[] aclParts = acl.split(ValidateTicket.PROP_ATTRIBUTE_ACL_SEPARATOR);
                    if (aclParts.length != 2 && aclParts.length != 3) {
                        log.warn("Ignoring ACL {} because the size is incorrect", acl);
                        continue;
                    }
                    String application = aclParts[0]; // TODO
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
