package org.siemac.metamac.sso.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MetamacPrincipal implements Serializable {

    private static final long serialVersionUID = -4106261968611466871L;
    
    private String                       userId;
    private List<MetamacPrincipalAccess> accesses;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<MetamacPrincipalAccess> getAccesses() {
        if (accesses == null) {
            accesses = new ArrayList<MetamacPrincipalAccess>();
        }
        return accesses;
    }
}