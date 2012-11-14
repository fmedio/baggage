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

package hello;

import clutter.hypertoolkit.Action;
import clutter.hypertoolkit.ActionId;
import clutter.hypertoolkit.RequestHandler;
import clutter.hypertoolkit.request.RequestParser;
import clutter.hypertoolkit.security.CookieJar;
import clutter.hypertoolkit.views.Resource;
import clutter.hypertoolkit.views.TextResource;

public class ShowStatsAction extends Action<Period> {
    public static final ActionId ID = new ActionId() {
        @Override
        public String getName() {
            return "showStats";
        }

        @Override
        public RequestHandler makeAction(Object o) {
            return new ShowStatsAction();
        }
    };

    @Override
    public Resource execute(CookieJar cookieJar, Period period) {
        return new TextResource("start: " + period.getStartYear() + ", end: " + period.getEndYear());
    }

    @Override
    public RequestParser<Period> makeRequestParser() {
        return new PeriodRequestParser();
    }

}
