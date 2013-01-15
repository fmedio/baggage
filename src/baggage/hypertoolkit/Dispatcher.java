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
import baggage.hypertoolkit.security.CookieJar;
import baggage.hypertoolkit.views.Resource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Dispatcher<ServiceType extends App> extends HttpServlet {
    private Clock clock;
    private ServiceType services;
    private ActionResolver<ServiceType> actionResolver;

    public Dispatcher(ServiceType services, ActionResolver<ServiceType> actionResolver) {
        this.services = services;
        this.actionResolver = actionResolver;
        this.clock = new SystemClock();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionId<ServiceType> actionId = actionResolver.resolve(request);
        RequestHandler requestHandler = actionId.makeAction(services);
        Resource resource;
        try {
            CookieJar cookieJar = new CookieJar(services.getName(), services.getSecretKey(), request, response);
            long before = clock.nowMillis();
            resource = requestHandler.handle(cookieJar, request);
            long elapsed = clock.nowMillis() - before;
            if (requestHandler.log()) {
                Log.info(this, "Dispatched to " + requestHandler.getClass().getSimpleName() + " in " + elapsed + "ms");
            }
        } catch (Throwable t) {
            Log.error(requestHandler, "Error executing " + requestHandler.getClass().getSimpleName(), t);
            resource = new ErrorPage(t);
        }
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
