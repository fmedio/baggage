/*
 * Copyright (c) 2010, 2011 Fabrice Medio.  All Rights Reserved.
 */

package baggage.hypertoolkit.request;

import baggage.hypertoolkit.UrlParam;

public class GrantSessionRequest {
    @UrlParam
    public String identifier;

    @UrlParam
    public String password;

    public GrantSessionRequest(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getPassword() {
        return password;
    }
}
