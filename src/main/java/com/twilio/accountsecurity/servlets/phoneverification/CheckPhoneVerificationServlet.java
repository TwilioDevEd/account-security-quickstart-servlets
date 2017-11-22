package com.twilio.accountsecurity.servlets.phoneverification;

import com.twilio.accountsecurity.exceptions.ParseRequestException;
import com.twilio.accountsecurity.exceptions.PhoneVerificationException;
import com.twilio.accountsecurity.services.PhoneVerificationService;
import com.twilio.accountsecurity.servlets.BaseServlet;
import com.twilio.accountsecurity.servlets.requests.CheckPhoneVerificationRequest;
import com.twilio.accountsecurity.servlets.requests.RequestParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/api/phone-verification/check"})
public class CheckPhoneVerificationServlet extends BaseServlet {

    private PhoneVerificationService phoneVerificationService;
    private RequestParser requestParser;

    public CheckPhoneVerificationServlet(PhoneVerificationService phoneVerificationService, RequestParser requestParser) {
        this.phoneVerificationService = phoneVerificationService;
        this.requestParser = requestParser;
    }

    public CheckPhoneVerificationServlet() {
        this.phoneVerificationService = new PhoneVerificationService();
        this.requestParser = new RequestParser();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            CheckPhoneVerificationRequest requestBody = requestParser.parse(request,
                    CheckPhoneVerificationRequest.class);
            phoneVerificationService.check(requestBody);
            respondWith(response, 200);
        } catch( ParseRequestException | PhoneVerificationException e) {
            respondWith(response, 500, e.getMessage());
        }
    }
}
