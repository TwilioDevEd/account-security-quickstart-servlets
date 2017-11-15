package com.twilio.accountsecurity.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class SessionManager {

    public static final String AUTHENTICATED_DB = "authenticated-db";
    public static final String AUTHENTICATED_AUTHY = "authenticated-authy";
    public static final String USERNAME = "username";

    private static final int MAX_INACTIVE_INTERVAL = 30 * 60;

    public void logIn(HttpServletRequest request, String username) {
        HttpSession session = request.getSession();
        session.setAttribute(AUTHENTICATED_DB, true);
        session.removeAttribute(AUTHENTICATED_AUTHY);
        session.setAttribute(USERNAME, username);
        session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
    }

    
}

