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

package clutter.hypertoolkit.views;

import clutter.Bag;
import clutter.Bags;
import clutter.hypertoolkit.html.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import static clutter.hypertoolkit.html.Html.*;

public abstract class Page implements Resource {

    protected abstract Renderable getBody();

    protected abstract String getTitle();

    protected abstract Includes getIncludes();

    public int getHttpStatus() {
        return HttpServletResponse.SC_OK;
    }

    @Override
    public Bag<String, String> extraHeaders() {
        return Bags.newBag();
    }

    public String getContentType() {
        return "text/html; charset=UTF-8";
    }

    private Renderable getHeaderContents() {
        Renderables headerContents = new Renderables()
                .add(title().add(getTitle()));

        for (CssInclude cssInclude : getIncludes().getCss()) {
            headerContents.add(cssInclude);
        }

        for (JavascriptInclude javascriptInclude : getIncludes().getJavascript()) {
            headerContents.add(javascriptInclude);
        }

        return headerContents;
    }

    public void render(OutputStream outputStream) throws IOException {
        Tag html = new Tag("html")
                .add(head().add(getHeaderContents()))
                .add(body().add(getBody()));

        PrintWriter pw = new PrintWriter(outputStream);
        pw.println("<!DOCTYPE HTML>");
        html.render(pw);
        pw.flush();
    }
}
