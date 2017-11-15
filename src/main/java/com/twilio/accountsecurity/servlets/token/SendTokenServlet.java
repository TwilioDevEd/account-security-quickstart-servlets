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

@WebServlet(urlPatterns = {"/api/token/sms", "/api/token/voice"})
public class SendTokenServlet extends BaseServlet {

    private TokenService tokenService;
    private SessionManager sessionManager;

    public SendTokenServlet(TokenService tokenService, SessionManager sessionManager) {
        this.tokenService = tokenService;
        this.sessionManager = sessionManager;
    }

    public SendTokenServlet() {
        this.tokenService = new TokenService();
        this.sessionManager = new SessionManager();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Optional<String> loggedUsernameOptional = sessionManager.getLoggedUsername(request);
        if(loggedUsernameOptional.isPresent()) {
            String loggedUsername = loggedUsernameOptional.get();
            try {
                String via = request.getServletPath().replace("/api/token/", "");
                switch (via) {
                    case "sms":
                        tokenService.sendSmsToken(loggedUsername);
                        respondWith(response, 200);
                        break;
                    case "voice":
                        tokenService.sendVoiceToken(loggedUsername);
                        respondWith(response, 200);
                        break;
                    default:
                        respondWith(response, 404);
                        break;
                }
            } catch(TokenVerificationException e) {
                respondWith(response, 500, e.getMessage());
            }
        } else {
            respondWith(response, 500, "You are not logged in");
        }
    }
}
