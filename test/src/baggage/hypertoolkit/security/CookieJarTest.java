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

import baggage.BaseTestCase;
import baggage.Fallible;
import baggage.IntegrationTest;

import javax.servlet.http.Cookie;

@IntegrationTest
public class CookieJarTest extends BaseTestCase {
    private CookieJar cookieJar;

    public void testSessionCookie() throws Exception {
        cookieJar.grantSessionCookie(new Member(42));

        assertTrue(cookieJar.hasValidSession());
        assertEquals(42, cookieJar.currentPrincipal());

        cookieJar.invalidateSession();
        assertFalse(cookieJar.hasValidSession());
    }

    public void testNoCurrentMember() throws Exception {
        assertFalse(cookieJar.hasValidSession());
        assertFailure(SecurityException.class, new Fallible() {
            @Override
            public void execute() throws Exception {
                cookieJar.currentPrincipal();
            }
        });
    }

    public void testCorruptedCookieValue() throws Exception {
        cookieJar = new CookieJar(SESSION_COOKIE_NAME, secretKey, new Cookie[] {new Cookie(SESSION_COOKIE_NAME, "not an encrypted string")});
        assertFalse(cookieJar.hasValidSession());
    }

    public void testSetAndGet() throws Exception {
        cookieJar.addEncryptedCookie("someKey", "someValue");
        assertEquals("someValue", cookieJar.stringValue("someKey").get());
        assertFalse(cookieJar.stringValue("someOtherKey").isPresent());
    }

    public void testCookiesAreEncrypted() throws Exception {
        Cookie cookie = new Cookie("someKey", "this value is clearly not encrypted");
        cookieJar = new CookieJar(SESSION_COOKIE_NAME, secretKey, new Cookie[] {cookie});
        assertFalse(cookieJar.stringValue("someKey").isPresent());
    }

    public void testSetLong() throws Exception {
        cookieJar.addEncryptedCookie("foo", 42l);
        assertTrue(cookieJar.longValue("foo").isPresent());
        assertEquals(42l, (long) cookieJar.longValue("foo").get());

        cookieJar.addEncryptedCookie("bar", "not a long");
        assertFalse(cookieJar.longValue("bar").isPresent());
    }


    @Override
    protected void setUp() throws Exception {
        cookieJar = new CookieJar(APP_NAME, secretKey, new Cookie[0]);
    }

    private class Member implements Principal {
        private long id;

        private Member(int id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }

    private static final String APP_NAME = "myAppName";
    private static final String SESSION_COOKIE_NAME = APP_NAME + "-cookiejar";

}
