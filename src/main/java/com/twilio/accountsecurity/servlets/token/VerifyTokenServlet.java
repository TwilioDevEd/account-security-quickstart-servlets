package com.twilio.accountsecurity.servlets.token;

import com.twilio.accountsecurity.exceptions.TokenVerificationException;
import com.twilio.accountsecurity.services.TokenService;
import com.twilio.accountsecurity.servlets.BaseServlet;
import com.twilio.accountsecurity.servlets.SessionManager;
import com.twilio.accountsecurity.servlets.requests.RequestParser;
import com.twilio.accountsecurity.servlets.requests.VerifyTokenRequest;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = {"/api/token/onetouchstatus"})
public class VerifyTokenServlet extends BaseServlet {

    private TokenService tokenService;
    private SessionManager sessionManager;
    private RequestParser requestParser;

    public VerifyTokenServlet(TokenService tokenService,
                              SessionManager sessionManager,
                              RequestParser requestParser) {
        this.tokenService = tokenService;
        this.sessionManager = sessionManager;
        this.requestParser = requestParser;
    }

    public VerifyTokenServlet() {
        this.tokenService = new TokenService();
        this.sessionManager = new SessionManager();
        this.requestParser = new RequestParser();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Optional<String> loggedUsernameOptional = sessionManager.getLoggedUsername(request);
        if(loggedUsernameOptional.isPresent()) {
            VerifyTokenRequest verifyTokenRequest = requestParser
                    .parse(request, VerifyTokenRequest.class);
            String loggedUsername = loggedUsernameOptional.get();
            try {
                tokenService.verify(loggedUsername, verifyTokenRequest.getToken());
                respondWith(response, 200);
            } catch (TokenVerificationException e) {
                respondWith(response, 400, e.getMessage());
            }
        } else {
            respondWith(response, 500, "You are not logged in");
        }
    }
}
