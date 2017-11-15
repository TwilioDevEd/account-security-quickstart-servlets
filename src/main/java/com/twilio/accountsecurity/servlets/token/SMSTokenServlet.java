package com.twilio.accountsecurity.servlets.token;

import com.twilio.accountsecurity.exceptions.TokenVerificationException;
import com.twilio.accountsecurity.services.TokenService;
import com.twilio.accountsecurity.servlets.BaseServlet;
import com.twilio.accountsecurity.servlets.SessionManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = {"/api/token/sms"})
public class SMSTokenServlet extends BaseServlet {

    private TokenService tokenService;
    private SessionManager sessionManager;

    public SMSTokenServlet(TokenService tokenService, SessionManager sessionManager) {
        this.tokenService = tokenService;
        this.sessionManager = sessionManager;
    }

    public SMSTokenServlet() {
        this.tokenService = new TokenService();
        this.sessionManager = new SessionManager();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Optional<String> loggedUsername = sessionManager.getLoggedUsername(request);
        if(loggedUsername.isPresent()) {
            try {
                tokenService.sendSmsToken(loggedUsername.get());
                respondWith(response, 200);
            } catch(TokenVerificationException e) {
                respondWith(response, 500, e.getMessage());
            }
        } else {
            respondWith(response, 500, "You are not logged in");
        }
    }
}
