/*
 * Copyright (c) 2010, 2011 Fabrice Medio.  All Rights Reserved.
 */

package clutter.hypertoolkit.request;

import clutter.Bag;
import clutter.ListBag;

public class GrantSessionRequestParser extends RequestParser<GrantSessionRequest> {
    public static final String IDENTIFIER = "identifier";
    public static final String PASSWORD = "password";

    @Override
    public GrantSessionRequest parse(Bag<String, String> parameters) throws InvalidRequestException {
        return new GrantSessionRequest(
                requiredString(IDENTIFIER, parameters),
                requiredString(PASSWORD, parameters)
        );
    }

    @Override
    public Bag<String, String> toParameters(GrantSessionRequest tee) {
        return new ListBag<String, String>();
    }
}
