/*
 * Copyright (c) 2010, 2011 Fabrice Medio.  All Rights Reserved.
 */

package baggage.hypertoolkit.request;

import baggage.Bag;
import baggage.ListBag;

public class IdRequestParser extends RequestParser<Long> {
    public static final String ID = "id";

    @Override
    public Long parse(Bag<String, String> parameters) throws InvalidRequestException {
        return requiredLong(ID, parameters);
    }

    @Override
    public Bag<String, String> toParameters(Long tee) {
        final ListBag<String, String> bag = new ListBag<String, String>();
        bag.put("id", tee.toString());
        return bag;
    }
}
