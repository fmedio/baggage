/*
 * Copyright (c) 2010, 2011 Fabrice Medio.  All Rights Reserved.
 */

package baggage.hypertoolkit.security;

import javax.crypto.SecretKey;

public interface AuthenticationService {
    public AuthenticationResult authenticate(String identifier, String candidatePassword);
    public String getAppPrefix();
    public SecretKey getSecretKey();
}
