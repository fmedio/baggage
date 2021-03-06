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
import baggage.Clock;
import baggage.Log;
import baggage.SystemClock;
import baggage.hypertoolkit.html.ErrorPage;
import baggage.hypertoolkit.views.Resource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Supplier;

public class Dispatcher extends HttpServlet {
    private Clock clock;
    private RouteFinder routeFinder;

    public Dispatcher(RouteFinder routeFinder) {
        this.routeFinder = routeFinder;
        this.clock = new SystemClock();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Supplier<RequestHandler> f = routeFinder.resolve(request);
        RequestHandler requestHandler = f.get();
        Resource resource;
        try {
            long before = clock.nowMillis();
            resource = requestHandler.handle(request);
            long elapsed = clock.nowMillis() - before;
            if (requestHandler.log()) {
                Log.info(this, "Dispatched to " + requestHandler.getClass().getSimpleName() + " in " + elapsed + "ms");
            }
        } catch (Throwable t) {
            Log.error(requestHandler, "Error executing " + requestHandler.getClass().getSimpleName(), t);
            resource = new ErrorPage(t);
        }

        Arrays.stream(resource.cookies()).forEach(c -> response.addCookie(c));

        response.setContentType(resource.getContentType());
        response.setStatus(resource.getHttpStatus());
        Bag<String, String> extraHeaders = resource.extraHeaders();
        for (String headerName : extraHeaders.keySet()) {
            for (String value : extraHeaders.getValues(headerName)) {
                response.addHeader(headerName, value);
            }
        }


        ClosingGuardStream outputStream = new ClosingGuardStream(response.getOutputStream());
        resource.render(outputStream);
        outputStream.flush();
        outputStream.doCloseForReal();
    }

}
