package com.twilio.accountsecurity.servlets.token

import com.twilio.accountsecurity.exceptions.TokenVerificationException
import com.twilio.accountsecurity.services.TokenService
import com.twilio.accountsecurity.servlets.SessionManager
import com.twilio.accountsecurity.servlets.requests.RequestParser
import com.twilio.accountsecurity.servlets.requests.VerifyTokenRequest
import spock.lang.Specification
import spock.lang.Subject

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession

class VerifyTokenServletSpec extends Specification {

    TokenService tokenService = Mock()
    SessionManager sessionManager = Mock()

    HttpServletRequest request = Mock()
    HttpServletResponse response = Mock()
    HttpSession session = Mock()
    PrintWriter responseWritter = Mock()

    @Subject def subject = new VerifyTokenServlet(tokenService, sessionManager, new RequestParser())

    def token = 'abcd1234'
    def requestJson = '{"token": "' + token + '"}'
    def requestObject = new VerifyTokenRequest(token)

    def "doPost - returns 200"() {
        when:
        subject.doPost(request, response)

        then:
        1 * sessionManager.getLoggedUsername(request) >> Optional.of('username')
        1 * request.getReader() >> new BufferedReader(new StringReader(requestJson))
        1 * tokenService.verify('username', token)
        1 * response.setStatus(200)
    }

    def "doPost - returns 400 for TokenVerificationException"() {
        when:
        subject.doPost(request, response)

        then:
        1 * sessionManager.getLoggedUsername(request) >> Optional.of('username')
        1 * request.getReader() >> new BufferedReader(new StringReader(requestJson))
        1 * tokenService.verify('username', token) >> {throw new TokenVerificationException("message")}
        1 * response.setStatus(400)
        1 * response.getWriter() >> responseWritter
        1 * responseWritter.print("message")
    }

    def "doPost - return 500 for user not logged in"() {
        when:
        subject.doPost(request, response)

        then:
        1 * sessionManager.getLoggedUsername(request) >> Optional.empty()
        1 * response.setStatus(500)
        1 * response.getWriter() >> responseWritter
        1 * responseWritter.print("You are not logged in")
    }

}
