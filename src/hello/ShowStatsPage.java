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

import baggage.hypertoolkit.html.Includes;
import baggage.hypertoolkit.html.Renderable;
import baggage.hypertoolkit.html.Renderables;
import baggage.hypertoolkit.views.Page;

import static baggage.hypertoolkit.html.Html.div;
import static baggage.hypertoolkit.html.Html.h1;

public class ShowStatsPage extends Page {
    private Period period;

    public ShowStatsPage(Period period) {
        this.period = period;
    }

    @Override
    protected Renderable getBody() {
        return new Renderables(
                h1("Those are my stats"),
                div().add("Start: " + period.getStartYear()),
                div().add("End: " + period.getEndYear())
        );
    }

    @Override
    protected String getTitle() {
        return "My Statistics";
    }

    @Override
    protected Includes getIncludes() {
        return new Includes();
    }
}
