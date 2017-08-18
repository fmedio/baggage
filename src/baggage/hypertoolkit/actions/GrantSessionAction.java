/*
 * Copyright (c) 2010, 2011 Fabrice Medio.  All Rights Reserved.
 */

package baggage.hypertoolkit.actions;

import baggage.hypertoolkit.TypedAction;
import baggage.hypertoolkit.request.GrantSessionRequest;
import baggage.hypertoolkit.security.AuthenticationResult;
import baggage.hypertoolkit.security.AuthenticationService;
import baggage.hypertoolkit.security.CookieJar;
import baggage.hypertoolkit.views.JsonResource;
import baggage.hypertoolkit.views.Resource;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GrantSessionAction extends TypedAction<GrantSessionRequest> {
    public static String ID = "session";

    private AuthenticationService authenticationService;

    public GrantSessionAction(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected Class<GrantSessionRequest> requestClass() {
        return GrantSessionRequest.class;
    }

    @Override
    public Resource execute(HttpServletRequest servletRequest, GrantSessionRequest request) {
        CookieJar cookieJar = new CookieJar(authenticationService.getAppPrefix(), authenticationService.getSecretKey(), servletRequest);
        return execute(cookieJar, request);
    }

    public final Resource execute(CookieJar cookieJar, GrantSessionRequest request) {
        AuthenticationResult result = authenticationService.authenticate(request.getIdentifier(), request.getPassword());

        JsonObject o = new JsonObject();

        if (result.isOk()) {
            cookieJar.grantSessionCookie(result.getPrincipal());
            o.addProperty("success", true);
            return new JsonResource(o, HttpServletResponse.SC_OK, cookieJar.getCookies());
        } else {
            o.addProperty("success", false);
            return new JsonResource(o);
        }
    }
}
