package com.twilio.accountsecurity.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class BaseServlet extends HttpServlet {

    protected void respondWith(HttpServletResponse response, int statusCode) {
        response.setStatus(statusCode);
    }

    protected void respondWith(HttpServletResponse response, int statusCode, String body)
            throws IOException {
        PrintWriter writer = response.getWriter();
        writer.print(body);
        writer.flush();
        respondWith(response, statusCode);
    }

}
