/*
 * Copyright (c) 2010, 2011 Fabrice Medio.  All Rights Reserved.
 */

package clutter.hypertoolkit.security;

public class AuthenticationResult {
    private boolean ok;
    private Principal principal;

    public AuthenticationResult() {
        this.ok = false;
    }

    public AuthenticationResult(Principal principal) {
        this.ok = true;
        this.principal = principal;
    }

    public boolean isOk() {
        return ok;
    }

    public Principal getPrincipal() {
        return principal;
    }
}
