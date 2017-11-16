package com.twilio.accountsecurity.servlets.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.accountsecurity.exceptions.ParseRequestException;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class RequestParser {

    private ObjectMapper objectMapper;

    public RequestParser() {
        this.objectMapper = new ObjectMapper();
    }

    public <T> T parse(HttpServletRequest request, Class<T> klass) {
        try {
            BufferedReader reader = request.getReader();
            return objectMapper.readValue(reader, klass);
        } catch (IOException e) {
            throw new ParseRequestException(e.getMessage());
        }
    }
}
