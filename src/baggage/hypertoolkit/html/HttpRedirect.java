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

package baggage.hypertoolkit.html;

import baggage.Bag;
import baggage.Bags;
import baggage.hypertoolkit.views.Resource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class HttpRedirect<QueryType> implements Resource {
    private String target;
    private Cookie[] cookies;

    public HttpRedirect(String target) {
        this(target, new Cookie[0]);
    }

    public HttpRedirect(String target, Cookie[] cookies) {
        this.target = target;
        this.cookies = cookies;
    }

    @Override
    public int getHttpStatus() {
        return HttpServletResponse.SC_MOVED_TEMPORARILY;
    }

    @Override
    public String getContentType() {
        return "text/plain";
    }

    @Override
    public Bag<String, String> extraHeaders() {
        return Bags.newBag("Location", target);
    }

    @Override
    public Cookie[] cookies() {
        return cookies;
    }

    @Override
    public void render(OutputStream outputStream) throws IOException {

    }
}
