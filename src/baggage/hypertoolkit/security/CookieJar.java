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

package baggage.hypertoolkit.security;

import baggage.Bag;
import baggage.ListBag;
import baggage.Maybe;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.UUID;

public class CookieJar {
    private static final String SESSION_COOKIE_FORMAT = "UUID: {0}, UserId: {1}";
    private String appPrefix;
    private SecretKey secretKey;
    private HttpServletRequest request;
    private final HttpServletResponse response;
    private static final String SESSION_KEY_NAME = "cookiejar";

    public CookieJar(String appPrefix, SecretKey secretKey, HttpServletRequest request, HttpServletResponse response) {
        this.appPrefix = appPrefix;
        this.secretKey = secretKey;
        this.request = request;
        this.response = response;
    }

    public void setString(String key, String value) {
        final String cookieName = appPrefix + "-" + key;
        ClearText clearText = new ClearText(value.getBytes());
        CipherText cipherText = clearText.encrypt(secretKey);
        final Cookie cookie = new Baker().makeCookie(cookieName, cipherText.base64());
        response.addCookie(cookie);
    }

    public Maybe<String> getString(String key) {
        final Cookie cookie = parseCookies().get(appPrefix + "-" + key);
        if (cookie == null) {
            return new Maybe<String>();
        }

        CipherText cipherText = new CipherText(cookie.getValue());
        try {
            ClearText clearText = cipherText.decrypt(secretKey);
            return new Maybe<String>(clearText.toString());
        } catch (Exception e) {
            return new Maybe<String>();
        }
    }


    public void setLong(String key, long value) {
        setString(key, Long.toString(value));
    }

    public Maybe<Long> getLong(String key) {
        final Maybe<String> string = getString(key);
        if (string.hasValue()) {
            try {
                return new Maybe<Long>(Long.parseLong(string.getValue()));
            } catch (Exception e) {
            }
        }
        return new Maybe<Long>();
    }

    public long currentPrincipal() throws SecurityException {
        final Maybe<String> contents = getString(SESSION_KEY_NAME);
        if (!contents.hasValue()) {
            throw new SecurityException();
        }

        try {
            Object[] objects = new MessageFormat(SESSION_COOKIE_FORMAT).parse(contents.getValue());
            return Long.parseLong((String) objects[1]);
        } catch (Throwable t) {
            throw new SecurityException();
        }
    }

    public boolean hasValidSession() {
        try {
            currentPrincipal();
            return true;
        } catch (SecurityException e) {
            return false;
        }
    }


    public void grantSessionCookie(Principal principal) {
        String value = MessageFormat.format(SESSION_COOKIE_FORMAT, UUID.randomUUID().toString(), principal.getId());
        setString(SESSION_KEY_NAME, value);
    }

    public void invalidateSession() {
        setString(SESSION_KEY_NAME, "");
    }

    private Bag<String, Cookie> parseCookies() {
        final ListBag<String, Cookie> bag = new ListBag<String, Cookie>();

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                bag.put(cookie.getName(), cookie);
            }
        }
        return bag;
    }
}
