/*
 * Copyright (c) 2010, 2011 Fabrice Medio.  All Rights Reserved.
 */

package clutter.hypertoolkit.actions;

import clutter.hypertoolkit.Action;
import clutter.hypertoolkit.ActionId;
import clutter.hypertoolkit.App;
import clutter.hypertoolkit.RequestHandler;
import clutter.hypertoolkit.request.GrantSessionRequest;
import clutter.hypertoolkit.request.GrantSessionRequestParser;
import clutter.hypertoolkit.request.RequestParser;
import clutter.hypertoolkit.security.AuthenticationResult;
import clutter.hypertoolkit.security.AuthenticationService;
import clutter.hypertoolkit.security.CookieJar;
import clutter.hypertoolkit.views.JSONResponse;
import clutter.hypertoolkit.views.Resource;
import org.json.JSONObject;

public class GrantSessionAction extends Action<GrantSessionRequest> {
    private static class JavaGenericsSuckAss<T extends App> implements ActionId<T> {
        @Override
        public String getName() {
            return "session";
        }

        @Override
        public RequestHandler makeAction(T app) {
            return new GrantSessionAction(app.getAuthenticationService());
        }
    }

    ;

    public static ActionId ID = new JavaGenericsSuckAss();

    private AuthenticationService authenticationService;

    public GrantSessionAction(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Resource execute(CookieJar cookieJar, GrantSessionRequest request) {
        AuthenticationResult result = authenticationService.authenticate(request.getIdentifier(), request.getPassword());

        if (result.isOk()) {
            cookieJar.grantSessionCookie(result.getPrincipal());
            return new JSONResponse(new JSONObject().put("success", true));
        }

        return new JSONResponse(new JSONObject().put("success", false));
    }

    @Override
    public RequestParser<GrantSessionRequest> makeRequestParser() {
        return new GrantSessionRequestParser();
    }
}
