package com.twilio.accountsecurity.services;

import com.authy.AuthyApiClient;
import com.authy.api.Params;
import com.authy.api.Verification;
import com.twilio.accountsecurity.exceptions.TokenVerificationException;
import com.twilio.accountsecurity.servlets.requests.CheckPhoneVerificationRequest;
import com.twilio.accountsecurity.servlets.requests.StartPhoneVerificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.twilio.accountsecurity.config.Settings.authyId;

public class PhoneVerificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

    private AuthyApiClient authyApiClient;

    public PhoneVerificationService(AuthyApiClient authyApiClient) {
        this.authyApiClient = authyApiClient;
    }

    public PhoneVerificationService() {
        this.authyApiClient = new AuthyApiClient(authyId());
    }

    public void start(StartPhoneVerificationRequest request) {
        Params params = new Params();
        params.setAttribute("code_length", "4");
        Verification verification = authyApiClient
                .getPhoneVerification()
                .start(request.getPhoneNumber(),
                       request.getCountryCode(),
                       request.getVia(),
                       params);

        if(!verification.isOk()) {
            logAndThrow("Error requesting phone verification. " +
                    verification.getMessage());
        }
    }

    public void check(CheckPhoneVerificationRequest request) {
        Verification verification = authyApiClient
                .getPhoneVerification()
                .check(request.getPhoneNumber(),
                        request.getCountryCode(),
                        request.getToken());

        if(!verification.isOk()) {
            logAndThrow("Error verifying token. " + verification.getMessage());
        }
    }

    private void logAndThrow(String message) {
        LOGGER.warn(message);
        throw new TokenVerificationException(message);
    }
}
