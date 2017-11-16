package com.twilio.accountsecurity.servlets;

import com.twilio.accountsecurity.exceptions.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class SessionManager {

    public static final String AUTHENTICATED_DB = "authenticated-db";
    public static final String AUTHENTICATED_AUTHY = "authenticated-authy";
    public static final String USERNAME = "username";

    private static final int MAX_INACTIVE_INTERVAL = 30 * 60;

    public void logInFirstStep(HttpServletRequest request, String username) {
        HttpSession session = request.getSession();
        session.setAttribute(AUTHENTICATED_DB, true);
        session.removeAttribute(AUTHENTICATED_AUTHY);
        session.setAttribute(USERNAME, username);
        session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
    }

    public Optional<String> getLoggedUsername(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return Optional.of((String) session.getAttribute(USERNAME));
        }

        return Optional.empty();
    }

    public void logInSecondStep(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if(!(boolean)session.getAttribute(AUTHENTICATED_DB)) {
            throw new AuthenticationException("User did not login");
        }
        session.setAttribute(AUTHENTICATED_AUTHY, true);
    }
}

