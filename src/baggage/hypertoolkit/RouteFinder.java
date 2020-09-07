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

import baggage.Log;
import baggage.hypertoolkit.actions.FourOhFour;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RouteFinder {
    private final Map<Key, Supplier<RequestHandler>> map;
    private final Supplier<RequestHandler> defaultHandler;
    private final String webDir;

    public RouteFinder(Supplier<RequestHandler> defaultHandler, String webDir) {
        this.defaultHandler = defaultHandler;
        this.webDir = webDir;
        map = new HashMap<>();
    }

    public void route(HttpMethod method, String route, Supplier<RequestHandler> handler) {
        map.put(new Key(method, route), handler);
    }

    public Supplier<RequestHandler> resolve(HttpServletRequest request) {
        Log.debug(this, "RequestURI = " + request.getRequestURI() +
                ", ServletPath = " + request.getServletPath());
        String name = request.getRequestURI();
        if (!name.equals("/")) {
            name = name.replaceAll("^/", "");
        }

        for (String extension : StaticFileHandler.MIME_TYPES.keySet()) {
            if (name.endsWith(extension)) {
                return() -> new StaticFileHandler(webDir);
            }
        }

        if (StringUtils.isEmpty(name)) {
            return defaultHandler;
        }

        Key key = new Key(HttpMethod.fromString(request.getMethod().toUpperCase()), name);
        if (map.containsKey(key)) {
            return map.get(key);

        } else {
            Log.debug(this, "404: " + key.method + " " + name);
            return () -> new FourOhFour();
        }
    }

    private static class Key {
        private final HttpMethod method;
        private final String route;

        public Key(HttpMethod method, String route) {
            this.method = method;
            this.route = route;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            if (method != key.method) return false;
            return route.equals(key.route);
        }

        @Override
        public int hashCode() {
            int result = method.hashCode();
            result = 31 * result + route.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Key{" +
                    "method=" + method +
                    ", route='" + route + '\'' +
                    '}';
        }
    }
}
