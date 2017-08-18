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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class JsonResource implements Resource {
    private JsonElement o;
    private int httpStatus;
    private final Cookie[] cookies;
    private Gson gson;

    public JsonResource(JsonElement o) {
        this(o, HttpServletResponse.SC_OK);
    }

    public JsonResource(JsonElement o, int httpStatus) {
        this(o, httpStatus, new Cookie[0]);
    }

    public JsonResource(JsonElement o, int httpStatus, Cookie[] cookies) {
        this.o = o;
        this.httpStatus = httpStatus;
        this.cookies = cookies;
        gson = new GsonBuilder().setPrettyPrinting().create();
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

    @Override
    public Cookie[] cookies() {
        return cookies;
    }

    public void render(OutputStream outputStream) throws IOException {
        PrintWriter pw = new PrintWriter(outputStream);
        pw.print(gson.toJson(o));
        pw.flush();
    }
}
