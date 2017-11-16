package com.twilio.accountsecurity.servlets;

import com.twilio.accountsecurity.exceptions.AuthenticationException;
import com.twilio.accountsecurity.exceptions.ParseRequestException;
import com.twilio.accountsecurity.servlets.requests.LoginRequest;
import com.twilio.accountsecurity.servlets.requests.RequestParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns={"/api/login"})
public class LoginServlet extends BaseServlet {

    private SessionManager sessionManager;
    private RequestParser requestParser;

    public LoginServlet(SessionManager sessionManager, RequestParser requestParser) {
        this.sessionManager = sessionManager;
        this.requestParser = requestParser;
    }

    public LoginServlet() {
        this.sessionManager = new SessionManager();
        this.requestParser = new RequestParser();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            LoginRequest loginRequest = requestParser.parse(request, LoginRequest.class);
            sessionManager.logInFirstStep(request, loginRequest.getUsername(),
                    loginRequest.getPassword());
            response.sendRedirect(request.getContextPath() + "/2fa/index.html");
            respondWith(response, 200);
        } catch (ParseRequestException e) {
            respondWith(response, 500, e.getMessage());
        } catch (AuthenticationException e) {
            respondWith(response, 401, e.getMessage());
        }
    }
}
