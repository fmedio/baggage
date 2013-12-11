/*
 * The MIT License
 *
 * Copyright (c) 2010 Fabrice Medio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package baggage.hypertoolkit.request;

import baggage.Bag;
import org.apache.commons.lang3.StringUtils;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public abstract class RequestParser<QueryType> {

    public abstract QueryType parse(Bag<String, String> parameters) throws InvalidRequestException;

    public abstract Bag<String, String> toParameters(QueryType tee);

    protected String requiredString(String key, Bag<String, String> parameters) throws InvalidRequestException {
        validateRequiredKey(key, parameters);
        return parameters.get(key);
    }

    protected InternetAddress requiredEmail(String key, Bag<String, String> parameters) throws InvalidRequestException {
        validateRequiredKey(key, parameters);
        final String value = parameters.get(key);
        try {
            return new InternetAddress(value, true);
        } catch (AddressException e) {
            throw new InvalidRequestException("Invalid email address");
        }
    }

    protected int requiredInt(String key, Bag<String, String> bag) throws InvalidRequestException {
        validateRequiredKey(key, bag);
        try {
            return Integer.parseInt(bag.get(key));
        } catch (NumberFormatException e) {

        }

        throw new InvalidRequestException(key);
    }

    protected double requiredDouble(String key, Bag<String, String> bag) throws InvalidRequestException {
        validateRequiredKey(key, bag);
        try {
            return Double.parseDouble(bag.get(key));
        } catch (NumberFormatException e) {

        }

        throw new InvalidRequestException(key);
    }

    protected void validateRequiredKey(String requiredKey, Bag<String, String> parameters) throws InvalidRequestException {
        if (!parameters.containsKey(StringUtils.defaultString(requiredKey))) {
            throw new InvalidRequestException(requiredKey);
        } else {
            final String value = parameters.get(StringUtils.defaultString(requiredKey));
            if (StringUtils.isEmpty(value)) {
                throw new InvalidRequestException(requiredKey);
            }
        }
    }

    protected int optionalInt(String key, Bag<String, String> bag, int defaultValue) {
        try {
            return Integer.parseInt(bag.get(key));
        } catch (Throwable t) {

        }

        return defaultValue;
    }

    protected long optionalLong(String key, Bag<String, String> bag, long defaultValue) {
        try {
            return Long.parseLong(bag.get(key));
        } catch (Throwable t) {

        }

        return defaultValue;
    }

    protected String optionalString(String key, Bag<String, String> bag, String defaultValue) {
        if (bag.containsKey(StringUtils.defaultString(key))) {
            return bag.get(key);
        } else {
            return defaultValue;
        }
    }

    protected long requiredLong(String name, Bag<String, String> parameters) throws InvalidRequestException {
        validateRequiredKey(name, parameters);
        try {
            return Long.parseLong(parameters.get(name));
        } catch (NumberFormatException e) {

        }

        throw new InvalidRequestException(name);
    }
}
