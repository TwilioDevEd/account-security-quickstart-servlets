package com.twilio.accountsecurity.servlets.phoneverification;

import com.twilio.accountsecurity.exceptions.ParseRequestException;
import com.twilio.accountsecurity.exceptions.PhoneVerificationException;
import com.twilio.accountsecurity.services.PhoneVerificationService;
import com.twilio.accountsecurity.servlets.BaseServlet;
import com.twilio.accountsecurity.servlets.requests.StartPhoneVerificationRequest;
import com.twilio.accountsecurity.servlets.requests.RequestParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/api/phone-verification/start"})
public class StartPhoneVerificationServlet extends BaseServlet {

    private PhoneVerificationService phoneVerificationService;
    private RequestParser requestParser;

    public StartPhoneVerificationServlet(PhoneVerificationService phoneVerificationService, RequestParser requestParser) {
        this.phoneVerificationService = phoneVerificationService;
        this.requestParser = requestParser;
    }

    public StartPhoneVerificationServlet() {
        this.phoneVerificationService = new PhoneVerificationService();
        this.requestParser = new RequestParser();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            StartPhoneVerificationRequest requestBody = requestParser.parse(request,
                    StartPhoneVerificationRequest.class);
            phoneVerificationService.start(requestBody);
            respondWith(response, 200);
        } catch( ParseRequestException | PhoneVerificationException e) {
            respondWith(response, 500, e.getMessage());
        }
    }
}
