package com.twilio.accountsecurity.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.accountsecurity.exceptions.UserExistsException;
import com.twilio.accountsecurity.exceptions.UserRegistrationException;
import com.twilio.accountsecurity.services.RegisterService;
import com.twilio.accountsecurity.servlets.requests.UserRegisterRequest;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/api/register"})
public class RegisterServlet extends HttpServlet {

    private RegisterService registerService;
    private ObjectMapper objectMapper;
    private SessionManager sessionManager;

    public RegisterServlet(RegisterService registerService, ObjectMapper objectMapper, SessionManager sessionManager) {
        this.registerService = registerService;
        this.objectMapper = objectMapper;
        this.sessionManager = sessionManager;
    }

    public RegisterServlet() {
        this.registerService = new RegisterService();
        this.objectMapper = new ObjectMapper();
        this.sessionManager = new SessionManager();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            UserRegisterRequest registerRequest = parseRequestBody(request,
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

    private void respondWith(HttpServletResponse response, int statusCode) {
        response.setStatus(statusCode);
    }

    private void respondWith(HttpServletResponse response, int statusCode, String body)
            throws IOException {
        PrintWriter writer = response.getWriter();
        writer.print(body);
        writer.flush();
        respondWith(response, statusCode);
    }

    private <T> T parseRequestBody(HttpServletRequest request, Class<T> klass)
            throws IOException {
        BufferedReader reader = request.getReader();
        return objectMapper.readValue(reader, klass);
    }
}
