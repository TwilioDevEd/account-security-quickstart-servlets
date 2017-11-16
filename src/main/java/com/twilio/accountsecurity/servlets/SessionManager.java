package com.twilio.accountsecurity.servlets;

import com.twilio.accountsecurity.exceptions.AuthenticationException;
import com.twilio.accountsecurity.models.UserModel;
import com.twilio.accountsecurity.repository.UserRepository;
import com.twilio.accountsecurity.services.PasswordEncoder;
import com.twilio.accountsecurity.servlets.requests.LoginRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class SessionManager {

    public static final String AUTHENTICATED_DB = "authenticated-db";
    public static final String AUTHENTICATED_AUTHY = "authenticated-authy";
    public static final String USERNAME = "username";

    private static final int MAX_INACTIVE_INTERVAL = 30 * 60;

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public SessionManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public SessionManager() {
        this.userRepository = new UserRepository();
        this.passwordEncoder = new PasswordEncoder();
    }

    public void logInFirstStep(HttpServletRequest request, String username, String password) {
        UserModel user = userRepository.findByUsername(username);
        if(user == null) {
            throw new AuthenticationException("User not found");
        }
        String candidateHashedPassword = passwordEncoder.encode(password,
                user.getSalt().getBytes());
        if(!candidateHashedPassword.equals(user.getPassword())) {
            throw new AuthenticationException("Invalid password");
        }
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

    public void logOut(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }


}

