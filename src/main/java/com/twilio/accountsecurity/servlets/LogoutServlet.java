package com.twilio.accountsecurity.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/api/logout"})
public class LogoutServlet extends BaseServlet {

    private SessionManager sessionManager;

    public LogoutServlet(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public LogoutServlet() {
        this.sessionManager = new SessionManager();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        sessionManager.logOut(request);
        response.sendRedirect(request.getContextPath() + "/index.html");
        respondWith(response, 200);
    }
}
