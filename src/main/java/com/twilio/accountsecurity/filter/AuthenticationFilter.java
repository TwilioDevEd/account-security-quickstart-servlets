package com.twilio.accountsecurity.filter;


import com.twilio.accountsecurity.servlets.SessionManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.twilio.accountsecurity.config.Settings.loginRequiredPaths;
import static com.twilio.accountsecurity.config.Settings.twoFaRequiredPaths;

@WebFilter(filterName = "authentication", urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {

    private final SessionManager sessionManager;

    @SuppressWarnings("unused")
    public AuthenticationFilter() {
        this(new SessionManager());
    }

    public AuthenticationFilter(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (isAuthorized(request)) {
            chain.doFilter(servletRequest, servletResponse);
        } else {
            response.sendRedirect("/login");
        }
    }

    @Override
    public void destroy() {

    }

    private boolean isAuthorized(HttpServletRequest request) {
        String contextPath = request.getServletPath();
        boolean requiresLogin = pathRequiresLogin(contextPath);
        boolean requires2FA = pathRequires2FA(contextPath);
        return (requiresLogin && sessionManager.isAuthenticatedFirstStep(request)) ||
                (requires2FA && sessionManager.isAuthenticatedSecondStep(request)) ||
                (!requires2FA && !requiresLogin);
    }

    private boolean pathRequiresLogin(String contextPath) {
        return loginRequiredPaths.stream().anyMatch((path) -> contextPath.startsWith(path));
    }

    private boolean pathRequires2FA(String contextPath) {
        return twoFaRequiredPaths.stream().anyMatch((path) -> contextPath.startsWith(path));
    }
}
