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

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ActionResolver<T extends BaseServices> {
    private Map<String, Function<T, RequestHandler>> map;
    private Function<T, RequestHandler> defaultHandler;

    public ActionResolver(Function<T, RequestHandler> defaultHandler) {
        this.defaultHandler = defaultHandler;
        map = new HashMap<>();
    }

    public void route(String name, Function<T, RequestHandler> handler) {
        map.put(name, handler);
    }

    public Function<T, RequestHandler> resolve(HttpServletRequest request) {
        String name = request.getRequestURI()
                .replaceAll("^" + request.getServletPath() + "/", "")
                .replaceAll("^/", "");

        for (String extension : StaticFileHandler.MIME_TYPES.keySet()) {
            if (name.endsWith(extension)) {
                return t -> new StaticFileHandler(t.getWebDir());
            }
        }

        if (StringUtils.isEmpty(name)) {
            return defaultHandler;
        }
        if (map.containsKey(name)) {
            return map.get(name);
        } else {
            Log.debug(this, "404: " + name);
            return t -> new FourOhFour();
        }
    }

}
