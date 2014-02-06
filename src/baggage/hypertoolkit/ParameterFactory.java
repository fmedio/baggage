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

package baggage.hypertoolkit;

import baggage.Bag;
import baggage.ListBag;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class ParameterFactory {
    public Bag<String, String> getParameters(HttpServletRequest servletRequest) {
        Bag<String, String> map = new ListBag<>();
        Enumeration names = servletRequest.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            for (String value : servletRequest.getParameterValues(name)) {
                map.put(name, value);
            }
        }
        return map;
    }

    public Bag<String, String> getHeaders(HttpServletRequest servletRequest) {
        Bag<String, String> result = new ListBag<String, String>();
        Enumeration<String> headers = servletRequest.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerName = headers.nextElement();
            final Enumeration<String> headerValues = servletRequest.getHeaders(headerName);
            while (headerValues.hasMoreElements()) {
                String headerValue = headerValues.nextElement();
                result.put(headerName, headerValue);
            }
        }
        return result;
    }
}
