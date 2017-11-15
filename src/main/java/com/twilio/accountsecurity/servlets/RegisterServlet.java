package com.twilio.accountsecurity.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.accountsecurity.exceptions.UserExistsException;
import com.twilio.accountsecurity.exceptions.UserRegistrationException;
import com.twilio.accountsecurity.services.RegisterService;
import com.twilio.accountsecurity.servlets.requests.RequestParser;
import com.twilio.accountsecurity.servlets.requests.UserRegisterRequest;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/api/register"})
public class RegisterServlet extends BaseServlet {

    private RegisterService registerService;
    private SessionManager sessionManager;
    private RequestParser requestParser;

    public RegisterServlet(RegisterService registerService,
                           SessionManager sessionManager,
                           RequestParser requestParser) {
        this.registerService = registerService;
        this.sessionManager = sessionManager;
        this.requestParser = requestParser;
    }

    public RegisterServlet() {
        this.registerService = new RegisterService();
        this.sessionManager = new SessionManager();
        this.requestParser = new RequestParser();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            UserRegisterRequest registerRequest = requestParser.parse(request,
                    UserRegisterRequest.class);
            registerService.register(registerRequest);
            sessionManager.logIn(request, registerRequest.getUsername());
            respondWith(response, 200);
        } catch (UserExistsException e) {
            respondWith(response, 412, "User already exists");
        } catch (UserRegistrationException e) {
            respondWith(response, 500, e.getMessage());
        }
    }
}
