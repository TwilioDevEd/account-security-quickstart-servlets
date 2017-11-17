package com.twilio.accountsecurity.services;

import com.authy.AuthyApiClient;
import com.authy.OneTouchException;
import com.authy.api.ApprovalRequestParams;
import com.authy.api.Hash;
import com.authy.api.OneTouchResponse;
import com.authy.api.Token;
import com.twilio.accountsecurity.exceptions.TokenVerificationException;
import com.twilio.accountsecurity.models.UserModel;
import com.twilio.accountsecurity.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.twilio.accountsecurity.config.Settings.authyId;

public class TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

    private AuthyApiClient authyClient;
    private UserRepository userRepository;

    public TokenService(AuthyApiClient authyClient, UserRepository userRepository) {
        this.authyClient = authyClient;
        this.userRepository = userRepository;
    }

    public TokenService() {
        this.authyClient = new AuthyApiClient(authyId());
        this.userRepository = new UserRepository();
    }

    public void sendSmsToken(String username) {
        Hash hash = authyClient
                .getUsers()
                .requestSms(getUserAuthyId(username));

        if(!hash.isOk()) {
            logAndThrow("Problem sending token over SMS");
        }
    }

    public void sendVoiceToken(String username) {
        UserModel user = userRepository.findByUsername(username);

        Hash hash = authyClient.getUsers().requestCall(user.getAuthyId());
        if(!hash.isOk()) {
            logAndThrow("Problem sending the token on a call");
        }
    }

    public String sendOneTouchToken(String username) {
        UserModel user = userRepository.findByUsername(username);

        try {
            ApprovalRequestParams params = new ApprovalRequestParams
                    .Builder(user.getAuthyId(), "Login requested for Account Security account.")
                    .setSecondsToExpire(120L)
                    .addDetail("Authy ID", user.getAuthyId().toString())
                    .addDetail("Username", user.getUsername())
                    .addDetail("Location", "San Francisco, CA")
                    .addDetail("Reason", "Demo by Account Security")
                    .build();
            OneTouchResponse response = authyClient
                    .getOneTouch()
                    .sendApprovalRequest(params);

            if(!response.isSuccess()) {
                logAndThrow("Problem sending the token with OneTouch");
            }
            return response.getApprovalRequest().getUUID();
        } catch (OneTouchException e) {
            logAndThrow("Problem sending the token with OneTouch: " + e.getMessage());
        }
        return null;
    }

    public void verify(String username, String token) {
        Token verificationResult = authyClient
                .getTokens()
                .verify(getUserAuthyId(username), token);

        if(!verificationResult.isOk()) {
            logAndThrow("Token verification failed");
        }
    }

    public String retrieveOneTouchStatus(String uuid) {
        try {
            return authyClient
                    .getOneTouch()
                    .getApprovalRequestStatus(uuid)
                    .getApprovalRequest()
                    .getStatus();
        } catch (OneTouchException e) {
            logAndThrow(e.getMessage());
            return "";
        }
    }

    private void logAndThrow(String message) {
        LOGGER.warn(message);
        throw new TokenVerificationException(message);
    }

    private Integer getUserAuthyId(String username) {
        UserModel user = userRepository.findByUsername(username);
        return user.getAuthyId();
    }
}
