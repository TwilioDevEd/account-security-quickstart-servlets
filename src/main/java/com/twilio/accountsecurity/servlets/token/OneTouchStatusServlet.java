package com.twilio.accountsecurity.servlets.token;

import com.twilio.accountsecurity.exceptions.AuthenticationException;
import com.twilio.accountsecurity.exceptions.TokenVerificationException;
import com.twilio.accountsecurity.services.TokenService;
import com.twilio.accountsecurity.servlets.BaseServlet;
import com.twilio.accountsecurity.servlets.SessionManager;
import com.twilio.accountsecurity.servlets.responses.OneTouchStatusResponse;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/api/token/onetouchstatus"})
public class OneTouchStatusServlet extends BaseServlet {

    private TokenService tokenService;
    private SessionManager sessionManager;

    public OneTouchStatusServlet(TokenService tokenService, SessionManager sessionManager) {
        this.tokenService = tokenService;
        this.sessionManager = sessionManager;
    }

    public OneTouchStatusServlet() {
        this.tokenService = new TokenService();
        this.sessionManager = new SessionManager();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String onetouchUUID = (String) request.getSession().getAttribute("onetouchUUID");
        try {
            String status = tokenService.retrieveOneTouchStatus(onetouchUUID);
            if (status.equals("approved")) {
                sessionManager.logInSecondStep(request);
            }
            respondWith(response, 200, new OneTouchStatusResponse(status));
        } catch (TokenVerificationException  e) {
            respondWith(response, 500, e.getMessage());
        } catch (AuthenticationException e) {
            respondWith(response, 401, e.getMessage());
        }
    }
}
