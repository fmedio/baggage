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

package baggage.hypertoolkit.views;

import baggage.Bag;
import baggage.Bags;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class JSONResponse implements Resource {
    private Object o;
    private int httpStatus;
    private final Cookie[] cookies;

    public JSONResponse(Object o) {
        this(o, HttpServletResponse.SC_OK);
    }

    public JSONResponse(Object o, int httpStatus) {
        this(o, httpStatus, new Cookie[0]);
    }

    public JSONResponse(Object o, int httpStatus, Cookie[] cookies) {
        this.o = o;
        this.httpStatus = httpStatus;
        this.cookies = cookies;
    }

    public JSONResponse(JSONArray o) {
        this(o, HttpServletResponse.SC_OK);
    }

    public JSONResponse(boolean b) {
        this(new Boolean(b), HttpServletResponse.SC_OK);
    }

    public String getContentType() {
        return "application/json";
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public Bag<String, String> extraHeaders() {
        return Bags.newBag();
    }

    public void render(OutputStream outputStream) throws IOException {
        PrintWriter pw = new PrintWriter(outputStream);
        if (o instanceof JSONObject) {
            pw.println(((JSONObject) o).toString(4));
        } else {
            pw.println(o.toString());
        }
        pw.flush();
    }
}
