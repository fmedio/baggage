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
import baggage.Maybe;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@IntegrationTest
public class CookieJarTest extends BaseTestCase {
    private Map<String, Cookie> cookiesByName;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private CookieJar cookieJar;

    public void testSessionCookie() throws Exception {
        cookieJar.grantSessionCookie(new Member(42));

        assertTrue(cookiesByName.containsKey(SESSION_COOKIE_NAME));
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
        cookiesByName.put(SESSION_COOKIE_NAME, new Cookie(SESSION_COOKIE_NAME, "not an encrypted string"));
        assertFalse(cookieJar.hasValidSession());
    }

    public void testSetAndGet() throws Exception {
        cookieJar.setString("someKey", "someValue");
        assertEquals("someValue", cookieJar.getString("someKey").getValue());
        assertFalse(cookieJar.getString("someOtherKey").hasValue());
    }

    public void testCookiesAreEncrypted() throws Exception {
        Cookie cookie = new Cookie("someKey", "this value is clearly not encrypted");
        cookiesByName.put("someKey", cookie);
        assertFalse(cookieJar.getString("someKey").hasValue());
    }

    public void testSetLong() throws Exception {
        cookieJar.setLong("foo", 42l);
        assertTrue(cookieJar.getLong("foo").hasValue());
        assertEquals(42l, (long) cookieJar.getLong("foo").getValue());

        cookieJar.setString("bar", "not a long");
        assertFalse(cookieJar.getLong("bar").hasValue());
    }

    public void testHttpServletRequestHasNoCookies() throws Exception {
        when(request.getCookies()).thenReturn(null);
        final Maybe<String> maybe = new CookieJar(APP_NAME, secretKey, request, response).getString("any string, really");
        assertFalse(maybe.hasValue());
    }

    @Override
    protected void setUp() throws Exception {
        cookiesByName = new HashMap<String, Cookie>();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getCookies()).thenAnswer(new Answer<Cookie[]>() {
            @Override
            public Cookie[] answer(InvocationOnMock invocationOnMock) throws Throwable {
                return cookiesByName.values().toArray(new Cookie[0]);
            }
        });

        doAnswer(new Answer<Cookie>() {
            @Override
            public Cookie answer(InvocationOnMock invocationOnMock) throws Throwable {
                Cookie cookie = (Cookie) invocationOnMock.getArguments()[0];
                cookiesByName.put(cookie.getName(), cookie);
                return cookie;
            }
        }).when(response).addCookie(any(Cookie.class));
        cookieJar = new CookieJar(APP_NAME, secretKey, request, response);
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
