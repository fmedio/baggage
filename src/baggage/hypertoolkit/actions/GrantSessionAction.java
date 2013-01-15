/*
 * Copyright (c) 2010, 2011 Fabrice Medio.  All Rights Reserved.
 */

package baggage.hypertoolkit.actions;

import baggage.hypertoolkit.Action;
import baggage.hypertoolkit.ActionId;
import baggage.hypertoolkit.App;
import baggage.hypertoolkit.RequestHandler;
import baggage.hypertoolkit.request.GrantSessionRequest;
import baggage.hypertoolkit.request.GrantSessionRequestParser;
import baggage.hypertoolkit.request.RequestParser;
import baggage.hypertoolkit.security.AuthenticationResult;
import baggage.hypertoolkit.security.AuthenticationService;
import baggage.hypertoolkit.security.CookieJar;
import baggage.hypertoolkit.views.JSONResponse;
import baggage.hypertoolkit.views.Resource;
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
