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

import com.google.common.collect.Lists;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CookieJar {
    private static final String SESSION_COOKIE_FORMAT = "UUID: {0}, UserId: {1}";
    private String appPrefix;
    private SecretKey secretKey;
    private static final String SESSION_KEY_NAME = "cookiejar";
    private final List<Cookie> cookies;

    public CookieJar(String appPrefix, SecretKey secretKey, Cookie[] cookies) {
        this.appPrefix = appPrefix;
        this.secretKey = secretKey;
        this.cookies = cookies == null? new ArrayList<>() : Lists.newArrayList(cookies);
    }
    public CookieJar(String appPrefix, SecretKey secretKey, HttpServletRequest request) {
        this(appPrefix, secretKey, request.getCookies());
    }

    public Cookie[] getCookies() {
        return cookies.toArray(new Cookie[0]);
    }

    public void addEncryptedCookie(String key, String value) {
        final String cookieName = cookieName(key);
        ClearText clearText = new ClearText(value.getBytes());
        CipherText cipherText = clearText.encrypt(secretKey);
        final Cookie cookie = new Baker().makeCookie(cookieName, cipherText.base64());
        cookies.add(cookie);
    }

    private String cookieName(String key) {
        return appPrefix + "-" + key;
    }

    public void addEncryptedCookie(String key, long value) {
        addEncryptedCookie(key, Long.toString(value));
    }

    public Optional<String> stringValue(String key) {
        return cookies.stream()
                .filter(c -> c.getName().equals(cookieName(key)))
                .findFirst()
                .map(c -> {
                    CipherText cipherText = new CipherText(c.getValue());
                    try {
                        ClearText clearText = cipherText.decrypt(secretKey);
                        return clearText.toString();
                    } catch (Exception e) {
                        return "";
                    }
                });
    }

    public Optional<Long> longValue(String key) {
        try {
            return stringValue(key).map(v -> Long.parseLong(v));
        } catch (Throwable t) {
            return Optional.empty();
        }
    }

    public long currentPrincipal() throws SecurityException {
        final Optional<String> contents = stringValue(SESSION_KEY_NAME);

        if (!contents.isPresent()) {
            throw new SecurityException();
        }

        try {
            Object[] objects = new MessageFormat(SESSION_COOKIE_FORMAT).parse(contents.get());
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
        addEncryptedCookie(SESSION_KEY_NAME, value);
    }

    public void invalidateSession() {
        cookies.stream()
                .filter(c -> cookieName(SESSION_KEY_NAME).equals(c.getName()))
                .forEach(c -> c.setValue(""));
    }
}
