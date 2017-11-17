package com.twilio.accountsecurity.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class BaseServlet extends HttpServlet {

    private ObjectMapper objectMapper;

    public BaseServlet() {
        this.objectMapper = new ObjectMapper();
    }

    protected void respondWith(HttpServletResponse response, int statusCode) {
        response.setStatus(statusCode);
    }

    protected void respondWith(HttpServletResponse response, int statusCode, Object o) throws IOException {
        this.respondWith(response, statusCode, objectMapper.writeValueAsString(o));
    }

    protected void respondWith(HttpServletResponse response, int statusCode, String body)
            throws IOException {
        PrintWriter writer = response.getWriter();
        if(body != null) {
            writer.print(body);
            writer.flush();
            respondWith(response, statusCode);
        }
    }

}
