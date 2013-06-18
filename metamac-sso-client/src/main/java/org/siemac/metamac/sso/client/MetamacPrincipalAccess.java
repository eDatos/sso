package org.siemac.metamac.sso.client;

import java.io.Serializable;

public class MetamacPrincipalAccess implements Serializable {

    private static final long serialVersionUID = -5049583473275224276L;
    
    private String            role;
    private String            application;
    private String            operation;

    public MetamacPrincipalAccess() {
    }

    public MetamacPrincipalAccess(String role, String application, String operation) {
        super();
        this.role = role;
        this.application = application;
        this.operation = operation;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}