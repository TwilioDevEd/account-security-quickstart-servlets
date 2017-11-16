package com.twilio.accountsecurity.servlets

import com.twilio.accountsecurity.exceptions.AuthenticationException
import com.twilio.accountsecurity.services.TokenService
import com.twilio.accountsecurity.servlets.requests.LoginRequest
import com.twilio.accountsecurity.servlets.requests.RequestParser
import spock.lang.Specification
import spock.lang.Subject

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

class LoginServletSpec extends Specification {

    TokenService tokenService = Mock()
    SessionManager sessionManager = Mock()

    HttpServletRequest request = Mock()
    HttpServletResponse response = Mock()
    HttpSession session = Mock()
    PrintWriter responseWritter = Mock()

    @Subject def subject = new LoginServlet(sessionManager, new RequestParser())

    def password = 'password'
    def username = 'username'
    def requestJson = '{"username": "' + username + '", "password":"' + password + '"}'
    def requestObject = new LoginRequest(username, password)

    def "doPost - login successfully"() {
        when:
        subject.doPost(request, response)

        then:
        1 * request.getReader() >> new BufferedReader(new StringReader(requestJson))
        1 * sessionManager.logInFirstStep(request, username, password)
        1 * request.getContextPath() >> 'servlet-path'
        1 * response.sendRedirect('servlet-path/2fa/index.html')
        1 * response.setStatus(200)
    }

    def "doPost - return 500 for parse error"() {
        when:
        subject.doPost(request, response)

        then:
        1 * request.getReader() >> new BufferedReader(new StringReader("{"))
        1 * response.setStatus(500)
        1 * response.getWriter() >> responseWritter
        1 * responseWritter.print(_)
    }

    def "doPost - return 401"() {
        when:
        subject.doPost(request, response)

        then:
        1 * request.getReader() >> new BufferedReader(new StringReader(requestJson))
        1 * sessionManager.logInFirstStep(request, username, password) >> {
            throw new AuthenticationException("message")}
        1 * response.setStatus(401)
        1 * response.getWriter() >> responseWritter
        1 * responseWritter.print("message")
    }
}
