package org.apereo.cas.support.saml.authentication.principal;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.apereo.cas.authentication.principal.AbstractWebApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class to represent that this service wants to use SAML. We use this in
 * combination with the CentralAuthenticationServiceImpl to choose the right
 * UniqueTicketIdGenerator.
 *
 * @author Scott Battaglia
 * @since 3.1
 */
@Entity
@DiscriminatorValue("saml")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SamlService extends AbstractWebApplicationService {

    private static final Logger LOGGER           = LoggerFactory.getLogger(SamlService.class);

    private static final long serialVersionUID = -6867572626767140223L;

    public SamlService() {
    }

    @Column
    private String            requestId;

    public String getRequestId() {
        return requestId;
    }
    /**
     * Instantiates a new SAML service.
     *
     * @param id the service id
     * @param originalUrl the original url
     * @param artifactId the artifact id
     * @param requestId the request id
     */
    @JsonCreator
    protected SamlService(@JsonProperty("id") final String id, @JsonProperty("originalUrl") final String originalUrl, @JsonProperty("artifactId") final String artifactId,
            @JsonProperty("requestID") final String requestId) {
        super(id, originalUrl, artifactId);
        this.requestId = requestId;
    }
}
