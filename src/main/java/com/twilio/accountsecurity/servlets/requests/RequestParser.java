package com.twilio.accountsecurity.servlets.requests;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class RequestParser {

    private ObjectMapper objectMapper;

    public RequestParser() {
        this.objectMapper = new ObjectMapper();
    }

    public <T> T parse(HttpServletRequest request, Class<T> klass)
            throws IOException {
        BufferedReader reader = request.getReader();
        return objectMapper.readValue(reader, klass);
    }
}
