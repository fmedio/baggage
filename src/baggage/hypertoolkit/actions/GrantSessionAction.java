/*
 * Copyright (c) 2010, 2011 Fabrice Medio.  All Rights Reserved.
 */

package baggage.hypertoolkit.actions;

import baggage.hypertoolkit.Action;
import baggage.hypertoolkit.request.GrantSessionRequest;
import baggage.hypertoolkit.request.GrantSessionRequestParser;
import baggage.hypertoolkit.request.RequestParser;
import baggage.hypertoolkit.security.AuthenticationResult;
import baggage.hypertoolkit.security.AuthenticationService;
import baggage.hypertoolkit.security.CookieJar;
import baggage.hypertoolkit.views.JSONResponse;
import baggage.hypertoolkit.views.Resource;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GrantSessionAction extends Action<GrantSessionRequest> {
    public static String ID = "session";

    private AuthenticationService authenticationService;

    public GrantSessionAction(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Resource execute(HttpServletRequest servletRequest, GrantSessionRequest request) {
        CookieJar cookieJar = new CookieJar(authenticationService.getAppPrefix(), authenticationService.getSecretKey(), servletRequest);
        return execute(cookieJar, request);
    }

    public final Resource execute(CookieJar cookieJar, GrantSessionRequest request) {
        AuthenticationResult result = authenticationService.authenticate(request.getIdentifier(), request.getPassword());

        if (result.isOk()) {
            cookieJar.grantSessionCookie(result.getPrincipal());
            return new JSONResponse(new JSONObject().put("success", true), HttpServletResponse.SC_OK, cookieJar.getCookies());
        }

        return new JSONResponse(new JSONObject().put("success", false));
    }

    @Override
    public RequestParser<GrantSessionRequest> makeRequestParser() {
        return new GrantSessionRequestParser();
    }
}
