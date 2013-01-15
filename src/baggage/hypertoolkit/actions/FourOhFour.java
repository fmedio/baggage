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

package baggage.hypertoolkit.actions;

import baggage.hypertoolkit.Action;
import baggage.hypertoolkit.ActionId;
import baggage.hypertoolkit.RequestHandler;
import baggage.hypertoolkit.request.NilRequestParser;
import baggage.hypertoolkit.request.RequestParser;
import baggage.hypertoolkit.security.CookieJar;
import baggage.hypertoolkit.views.FourOhFourPage;
import baggage.hypertoolkit.views.Resource;

public class FourOhFour extends Action {
    public static final ActionId ID = new ActionId() {
        public String getName() {
            return "404";
        }

        public RequestHandler makeAction(Object services) {
            return new FourOhFour();
        }
    };

    public Resource execute(CookieJar cookieJar, Object query) {
        return new FourOhFourPage();
    }

    public RequestParser makeRequestParser() {
        return new NilRequestParser();
    }

}
